/*
 * Copyright 2020-2021 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exactpro.th2.act.events;

import com.exactpro.th2.act.events.verification.FieldsVerifier;
import com.exactpro.th2.act.events.verification.VerificationDetail;
import com.exactpro.th2.common.grpc.Event;
import com.exactpro.th2.common.grpc.EventBatch;
import com.exactpro.th2.common.grpc.EventID;
import com.exactpro.th2.common.grpc.EventStatus;
import com.exactpro.th2.common.schema.factory.CommonFactory;
import com.exactpro.th2.common.schema.message.MessageRouter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

public class EventStoreHandler
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final ObjectMapper mapper = new ObjectMapper();
	private final MessageRouter<EventBatch> eventBatchRouter;

	public EventStoreHandler(CommonFactory commonFactory)
	{
		this.eventBatchRouter = commonFactory.getEventBatchRouter();
	}

	private static List<Object> createPayloadFromRequestParams(Map<String, String> requestParams) {
		if (MapUtils.isNotEmpty(requestParams))
		{
			List<Object> payload = new ArrayList<>(2);
			payload.add(new EventPayloadMessage("Request parameters"));
			payload.add(new EventPayloadTable(requestParams, false));
			return payload;
		} else {
			return Collections.emptyList();
		}
	}

	private void createPayloadFromErrorParams(String text, Throwable t, EventPayloadBuilder builder) {
		Map<String, String> obj = new LinkedHashMap<>();
		if (text != null && text.isEmpty()) {
			obj.put("Error text", text);
		}
		if (t != null) {
			obj.put("Exception message", ExceptionUtils.getMessage(t));
			obj.put("Stack trace", ExceptionUtils.getStackTrace(t));
		}
		
		if (MapUtils.isNotEmpty(obj)) {
			builder.printTable("Error details", obj);
		}
	}
	

	public byte[] writePayloadBody(List<Object> payload, EventID eventId) {
		try {
			return mapper.writeValueAsBytes(payload);
		} catch (JsonProcessingException e) {
			logger.error("Error while creating body, event " + eventId, e);
			return e.getMessage().getBytes(StandardCharsets.UTF_8);
		}
	}

	private ByteString byteStringFromPayload(List<Object> objects, EventID eventID) {
		return ByteString.copyFrom(writePayloadBody(objects, eventID));
	}

	public void storeEvent(EventDetails.EventInfo info, AdditionalEventInfo additionalEventInfo) {
		logger.debug("Storing execution event");
		
		// Create main event with request and response information
		EventDetails details = new EventDetails(info);
		details.setStatus(additionalEventInfo.isStatus());
		
		EventPayloadBuilder payloadBuilder = new EventPayloadBuilder();
		
		if (StringUtils.isNotEmpty(additionalEventInfo.getDescription())) {
			payloadBuilder.printText("Description:\n" + additionalEventInfo.getDescription());
		}
		
		if (StringUtils.isNotEmpty(additionalEventInfo.getInputTableHeader())
				&& MapUtils.isNotEmpty(additionalEventInfo.getInputTable())) {
			payloadBuilder.printTable(additionalEventInfo.getInputTableHeader(),
					additionalEventInfo.getInputTable());
		}

		createPayloadFromErrorParams(additionalEventInfo.getErrorText(), additionalEventInfo.getThrowable(),
				payloadBuilder);
		
		if (!payloadBuilder.isEmpty()) {
			details.setBuffer(payloadBuilder.toByteString());
		}

		this.storeEvent(details);
	}

	public void storeVerification(EventDetails.EventInfo info, List<VerificationDetail> details) {
		logger.debug("Storing verification event");
		
		Instant start = Instant.now();
		FieldsVerifier verifier = new FieldsVerifier(details);
		EventPayloadVerification verificationResult = verifier.transform();
		
		EventID verificationEventId = EventID.newBuilder().setId(UUID.randomUUID().toString()).build();

		EventDetails.EventInfo copiedInfo = new EventDetails.EventInfo(info);
		copiedInfo.setStartTime(start);
		copiedInfo.setEventId(verificationEventId);
		copiedInfo.setEndTime(Instant.now());
		
		EventDetails eventDetails = new EventDetails();
		eventDetails.setInfo(copiedInfo);
		eventDetails.setBuffer(this.byteStringFromPayload(Collections.singletonList(verificationResult), verificationEventId));
		eventDetails.setStatus(verifier.isSuccess());
		
		this.storeEvent(eventDetails);
	} 

	private EventID storeEvent(EventDetails eventDetails)
	{
		Event event = createEvent(eventDetails);
		try {
			eventBatchRouter.send(EventBatch.newBuilder().addEvents(event).build());
			logger.info("Event ID = " + event.getId());
		} catch (IOException e) {
			logger.warn("Could not store event", e);
			throw new RuntimeException("Could not store event", e);
		}

		return event.getId();
	}

	private Event createEvent(EventDetails eventDetails)
	{
		EventID id = eventDetails.getInfo().getEventId();
		if (id == null) {
			id = EventID.newBuilder().setId(UUID.randomUUID().toString()).build();
		}
		
		Event.Builder builder = Event.newBuilder()
				.setId(id)
				.setName(eventDetails.getInfo().getEventName())
				.setStatus(eventDetails.isStatus() ? EventStatus.SUCCESS : EventStatus.FAILED)
				.setStartTimestamp(this.timestampFromInstant(eventDetails.getInfo().getStartTime()))
				.setEndTimestamp(this.timestampFromInstant(eventDetails.getInfo().getEndTime()));
		
		if (eventDetails.getMessageIDList() != null) {
			builder.addAllAttachedMessageIds(eventDetails.getMessageIDList());
		}
		
		if (eventDetails.getBuffer() != null) {
			builder.setBody(eventDetails.getBuffer());
		}

		EventID parentEventId = eventDetails.getInfo().getParentEventId();
		if (parentEventId != null) {
			builder.setParentId(parentEventId);
		}
		return builder.build();
	}
	
	private Timestamp timestampFromInstant(Instant instant) {
		if (instant == null) {
			instant = Instant.now();
		}
		return Timestamp.newBuilder().setSeconds(instant.getEpochSecond())
				.setNanos(instant.getNano()).build();
	}
}
