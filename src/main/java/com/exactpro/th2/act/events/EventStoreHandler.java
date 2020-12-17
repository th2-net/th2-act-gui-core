/*
 * Copyright 2020-2020 Exactpro (Exactpro Systems Limited)
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
import com.exactpro.th2.act.grpc.hand.RhBatchResponse;
import com.exactpro.th2.eventstore.grpc.EventStoreServiceGrpc.EventStoreServiceBlockingStub;
import com.exactpro.th2.eventstore.grpc.Response;
import com.exactpro.th2.eventstore.grpc.StoreEventRequest;
import com.exactpro.th2.infra.grpc.Event;
import com.exactpro.th2.infra.grpc.EventID;
import com.exactpro.th2.infra.grpc.EventStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.*;

public class EventStoreHandler
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final ObjectMapper mapper = new ObjectMapper();
	private final EventStoreServiceBlockingStub eventStoreConnector;

	public EventStoreHandler(EventStoreServiceBlockingStub eventStoreConnector)
	{
		this.eventStoreConnector = eventStoreConnector;
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

	private static List<Object> createPayloadFromErrorParams(String text, Throwable t) {
		Map<String, String> obj = new LinkedHashMap<>();
		if (text != null && text.isEmpty()) {
			obj.put("Error text", text);
		}
		if (t != null) {
			obj.put("Exception message", ExceptionUtils.getMessage(t));
			obj.put("Stack trace", ExceptionUtils.getStackTrace(t));
		}
		
		if (MapUtils.isNotEmpty(obj))
		{
			List<Object> payload = new ArrayList<>(2);
			payload.add(new EventPayloadMessage("Error details"));
			payload.add(new EventPayloadTable(obj, false));
			return payload;
		} else {
			return Collections.emptyList();
		}
	}
	
	private static List<Object> createMainPayload(Map<String, String> requestParams, RhBatchResponse response)
	{
		Map<String, String> responseMap = new LinkedHashMap<>();
		responseMap.put("Action status", response.getScriptStatus().name());
		responseMap.put("Errors", response.getErrorMessage());
		responseMap.put("SessionId", response.getSessionId());

		List<Object> payload = new ArrayList<>(4);
		payload.addAll(createPayloadFromRequestParams(requestParams));
		payload.add(new EventPayloadMessage("Response"));
		payload.add(new EventPayloadTable(responseMap));
		return payload;
	}

	public String writePayloadBody(List<Object> payload, EventID eventId)
	{
		try
		{
			return mapper.writeValueAsString(payload);
		}
		catch (JsonProcessingException e)
		{
			logger.error("Error while creating body, event " + eventId, e);
			return e.getMessage();
		}
	}

	private ByteString byteStringFromPayload(List<Object> objects, EventID eventID) {
		String stringBody = writePayloadBody(objects, eventID);
		return ByteString.copyFrom(stringBody, Charset.defaultCharset());
	}
	
	public void storeEvent(EventDetails.EventInfo info, Map<String, String> requestParams, RhBatchResponse response)
	{
		logger.debug("Storing execution event");
		// Create main event with request and response information
		EventDetails details = new EventDetails(info);
		details.setBuffer(this.byteStringFromPayload(createMainPayload(requestParams, response), info.getEventId()));
		details.setStatus(response.getScriptStatus() == RhBatchResponse.ScriptExecutionStatus.SUCCESS);
		details.setMessageIDList(response.getAttachedMessageIdsList());

		this.storeEvent(details);
	}

	public void storeEvent(EventDetails.EventInfo info, Map<String, String> requestParams)
	{
		logger.debug("Storing parent event");
		// Create main event with request and response information
		EventDetails details = new EventDetails(info);
		details.setStatus(true);
		if (MapUtils.isNotEmpty(requestParams)) {
			details.setBuffer(this.byteStringFromPayload(createPayloadFromRequestParams(requestParams), info.getEventId()));
		}
		this.storeEvent(details);
	}

	public void storeErrorEvent(EventDetails.EventInfo info, Map<String, String> requestParams, String text, Throwable t)
	{
		logger.debug("Storing parent event");
		// Create main event with request and response information
		EventDetails details = new EventDetails(info);
		details.setStatus(false);
		List<Object> buffer = new ArrayList<>();
		if (MapUtils.isNotEmpty(requestParams)) {
			buffer.addAll(createPayloadFromRequestParams(requestParams));
		}
		if (text != null || t != null) {
			buffer.addAll(createPayloadFromErrorParams(text, t));
		}
		
		if (!buffer.isEmpty()) {
			details.setBuffer(this.byteStringFromPayload(buffer, info.getEventId()));
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
		eventDetails.setInfo(info);
		eventDetails.setBuffer(this.byteStringFromPayload(Collections.singletonList(verificationResult), verificationEventId));
		eventDetails.setStatus(verifier.isSuccess());
		
		this.storeEvent(eventDetails);
	} 

	private EventID storeEvent(EventDetails eventDetails)
	{
		Event event = createEvent(eventDetails);
		Response response = eventStoreConnector.storeEvent(StoreEventRequest.newBuilder()
				.setEvent(event)
				.build());
		if (response.hasError())
		{
			logger.warn("Could not store event: " + response.getError().getValue());
			throw new RuntimeException(response.getError().getValue());
		}
		logger.info("Event ID = " + event.getId());

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
