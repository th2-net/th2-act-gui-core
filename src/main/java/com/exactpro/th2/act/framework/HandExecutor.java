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

package com.exactpro.th2.act.framework;

import com.exactpro.th2.act.events.EventDetails;
import com.exactpro.th2.act.events.EventStoreHandler;
import com.exactpro.th2.act.events.verification.VerificationDetail;
import com.exactpro.th2.act.grpc.hand.RhActionsList;
import com.exactpro.th2.act.grpc.hand.RhBatchResponse;
import com.exactpro.th2.act.grpc.hand.RhBatchService;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.act.grpc.hand.RhTargetServer;
import com.exactpro.th2.common.grpc.EventID;

import java.time.Instant;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HandExecutor {

	private final RhBatchService handConnector;
	private final EventStoreHandler eventStoreHandler;

	public HandExecutor(RhBatchService handConnector, EventStoreHandler eventStoreHandler) {
		this.handConnector = handConnector;
		this.eventStoreHandler = eventStoreHandler;
	}
	
	public RhSessionID register(RhTargetServer request) {
		return this.handConnector.register(request);
	}

	public void unregister(RhSessionID sessionID) {
		this.handConnector.unregister(sessionID);
	}

	public RhBatchResponse executeWinGuiScript(Map<String, String> requestParams, String eventName,
	                                           EventID parentEventId, RhActionsList.Builder rhActionsList) {
		Instant startTime = Instant.now();
		EventID eventId = EventID.newBuilder().setId(UUID.randomUUID().toString()).build();
		rhActionsList.setParentEventId(eventId);
		RhBatchResponse response = handConnector.executeRhActionsBatch(rhActionsList.build());
		Instant endTime = Instant.now();

		EventDetails.EventInfo info = new EventDetails.EventInfo();
		info.setParentEventId(parentEventId);
		info.setStartTime(startTime);
		info.setEndTime(endTime);
		info.setEventName(eventName);
		info.setEventId(eventId);
		
		eventStoreHandler.storeEvent(info, requestParams, response);
		return response;
	}

	public void executeVerification(List<VerificationDetail> verification, String eventName, EventID parentEventId) {

		EventDetails.EventInfo info = new EventDetails.EventInfo();
		info.setParentEventId(parentEventId);
		info.setEventName(eventName);

		eventStoreHandler.storeVerification(info, verification);
	}
	
	public EventID logParentEvent(EventID parentId, String eventName, Map<String, String> requestParams) {
		EventID newEventId = EventID.newBuilder().setId(UUID.randomUUID().toString()).build();
		EventDetails.EventInfo info = new EventDetails.EventInfo();
		info.setEventId(newEventId);
		info.setParentEventId(parentId);
		info.setEventName(eventName);
		eventStoreHandler.storeEvent(info, requestParams);
		return newEventId;
	}

	public EventID logErrorEvent(EventID parentId, String eventName, Map<String, String> requestParams,
								 String errorDescription, Throwable throwable) {
		EventID newEventId = EventID.newBuilder().setId(UUID.randomUUID().toString()).build();
		EventDetails.EventInfo info = new EventDetails.EventInfo();
		info.setEventId(newEventId);
		info.setParentEventId(parentId);
		info.setEventName(eventName);
		eventStoreHandler.storeErrorEvent(info, requestParams, errorDescription, throwable);
		return newEventId;
	}
	
}
