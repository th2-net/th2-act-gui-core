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

import com.exactpro.th2.act.configuration.CustomConfiguration;
import com.exactpro.th2.act.events.EventStoreHandler;
import com.exactpro.th2.act.events.StubEventStoreHandler;
import com.exactpro.th2.act.grpc.hand.RhBatchService;
import com.exactpro.th2.common.schema.factory.CommonFactory;

public abstract class ActConnections<C extends CustomConfiguration> {

	protected final RhBatchService handConnector;
	protected final EventStoreHandler eventStoreHandler;

	protected final CommonFactory commonFactory;
	protected final C customConfiguration;

	public ActConnections(CommonFactory commonFactory) throws Exception {
		this.commonFactory = commonFactory;
		this.handConnector = commonFactory.getGrpcRouter().getService(RhBatchService.class);
		this.eventStoreHandler = new StubEventStoreHandler(commonFactory);
		this.customConfiguration = createCustomConfiguration(commonFactory);
	}

	public RhBatchService getHandConnector() {
		return handConnector;
	}

	public EventStoreHandler getEventStoreHandler() {
		return eventStoreHandler;
	}

	public C getCustomConfiguration() {
		return customConfiguration;
	}

	public void close() {
		commonFactory.close();
	}


	protected abstract C createCustomConfiguration(CommonFactory commonFactory);
}
