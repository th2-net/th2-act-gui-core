/*
 * Copyright 2021-2021 Exactpro (Exactpro Systems Limited)
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

public class ExecutionParams {

	private String eventName;
	private boolean storeActionMessages;
	private AdditionalEventInfo additionalEventInfo;
	private boolean clearBuffer = true;
	private InternalMessageType messageType = InternalMessageType.PLAIN_STRING;


	private ExecutionParams() {
	}


	public String getEventName() {
		return eventName;
	}

	public boolean isStoreActionMessages() {
		return storeActionMessages;
	}

	public AdditionalEventInfo getAdditionalEventInfo() {
		return additionalEventInfo;
	}

	public boolean hasAdditionalEventInfo() {
		return additionalEventInfo != null;
	}

	public boolean isClearBuffer() {
		return clearBuffer;
	}

	public InternalMessageType getMessageType() {
		return messageType;
	}

	public static ExecutionParamsBuilder builder() {
		return new ExecutionParamsBuilder();
	}

	public static ExecutionParams createDefaultParams(String eventName) {
		return builder()
				.setEventName(eventName)
				.build();
	}


	public static class ExecutionParamsBuilder {
		private final ExecutionParams executionParams;

		private ExecutionParamsBuilder() {
			this.executionParams = new ExecutionParams();
		}

		public ExecutionParamsBuilder setEventName(String eventName) {
			this.executionParams.eventName = eventName;
			return this;
		}

		public ExecutionParamsBuilder setStoreActionMessages(boolean storeActionMessages) {
			this.executionParams.storeActionMessages = storeActionMessages;
			return this;
		}

		public ExecutionParamsBuilder setAdditionalEventInfo(AdditionalEventInfo additionalEventInfo) {
			this.executionParams.additionalEventInfo = additionalEventInfo;
			return this;
		}

		public ExecutionParamsBuilder setClearBuffer(boolean clearBuffer) {
			this.executionParams.clearBuffer = clearBuffer;
			return this;
		}

		public ExecutionParamsBuilder setMessageType(InternalMessageType messageType) {
			this.executionParams.messageType = messageType;
			return this;
		}

		public ExecutionParams build() {
			return executionParams;
		}
	}
}
