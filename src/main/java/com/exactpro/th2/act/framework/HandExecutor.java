/*
 * Copyright 2020-2024 Exactpro (Exactpro Systems Limited)
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

import com.exactpro.th2.act.events.AdditionalEventInfo;
import com.exactpro.th2.act.events.EventDetails;
import com.exactpro.th2.act.events.EventStoreHandler;
import com.exactpro.th2.act.events.verification.VerificationDetail;
import com.exactpro.th2.act.grpc.hand.RhActionsBatch;
import com.exactpro.th2.act.grpc.hand.RhBatchResponse;
import com.exactpro.th2.act.grpc.hand.RhBatchService;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.act.grpc.hand.RhTargetServer;
import com.exactpro.th2.common.grpc.EventID;

import java.time.Instant;
import java.util.List;

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

	public RhBatchResponse executeWinGuiScript(RhActionsBatch rhActionsList) {
		return handConnector.executeRhActionsBatch(rhActionsList);
	}

	public void executeVerification(List<VerificationDetail> verification, String eventName, EventID parentEventId) {

		EventDetails.EventInfo info = new EventDetails.EventInfo();
		info.setParentEventId(parentEventId);
		info.setEventName(eventName);

		eventStoreHandler.storeVerification(info, verification);
	}
	
	public EventID logEvent(EventID parentId, String eventName, AdditionalEventInfo eventInfo) {
		Instant now = Instant.now();
		EventID newEventId = eventStoreHandler.generateEventId(now);
		EventDetails.EventInfo info = new EventDetails.EventInfo();
		info.setEventId(newEventId);
		info.setParentEventId(parentId);
		info.setEventName(eventName);
		info.setStartTime(now);
		eventStoreHandler.storeEvent(info, eventInfo);
		return newEventId;
	}
	
}
