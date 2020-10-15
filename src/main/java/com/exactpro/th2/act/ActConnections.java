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

package com.exactpro.th2.act;

import com.exactpro.th2.act.configuration.ActTh2Configuration;
import com.exactpro.th2.act.events.EventStoreHandler;
import com.exactpro.th2.act.grpc.hand.RhBatchGrpc;
import com.exactpro.th2.configuration.MicroserviceConfiguration;
import com.exactpro.th2.eventstore.grpc.EventStoreServiceGrpc;
import io.grpc.ManagedChannel;

import static io.grpc.ManagedChannelBuilder.forAddress;

public class ActConnections {

	private final ManagedChannel eventChannel;
	private final ManagedChannel handChannel;
	private final EventStoreServiceGrpc.EventStoreServiceBlockingStub eventStoreConnector;
	private final RhBatchGrpc.RhBatchBlockingStub handConnector;
	private final EventStoreHandler eventStoreHandler;

	public ActConnections(MicroserviceConfiguration configuration) {
		ActTh2Configuration th2Configuration = (ActTh2Configuration) configuration.getTh2();
		this.eventChannel = forAddress(th2Configuration.getTh2EventStorageGRPCHost(), th2Configuration.getTh2EventStorageGRPCPort()).usePlaintext().build();
		this.eventStoreConnector = EventStoreServiceGrpc.newBlockingStub(eventChannel);
		this.handChannel = forAddress(th2Configuration.getHandGRPCHost(), th2Configuration.getHandGRPCPort()).usePlaintext().build();
		this.handConnector = RhBatchGrpc.newBlockingStub(handChannel);
		this.eventStoreHandler = new EventStoreHandler(eventStoreConnector);
	}

	public RhBatchGrpc.RhBatchBlockingStub getHandConnector() {
		return handConnector;
	}

	public EventStoreHandler getEventStoreHandler() {
		return eventStoreHandler;
	}

	public void close() {
		if (eventChannel != null) {
			eventChannel.shutdownNow();
		}
		if (handChannel != null) {
			handChannel.shutdownNow();
		}
	}
}
