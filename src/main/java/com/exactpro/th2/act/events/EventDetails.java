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

import com.exactpro.th2.common.grpc.EventID;
import com.exactpro.th2.common.grpc.MessageID;
import com.google.protobuf.ByteString;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class EventDetails {
	
	private EventInfo info;
	private boolean status;
	private List<MessageID> messageIDList;
	private ByteString buffer;

	public EventDetails() {
		this.messageIDList = Collections.emptyList();
	}

	public EventDetails(EventDetails copyFrom) {
		this.info = copyFrom.info;
		this.status = copyFrom.status;
		this.messageIDList = copyFrom.messageIDList;
		this.buffer = copyFrom.buffer;
	}

	public EventDetails(EventInfo info) {
		this.info = info;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<MessageID> getMessageIDList() {
		return messageIDList;
	}

	public void setMessageIDList(List<MessageID> messageIDList) {
		this.messageIDList = messageIDList;
	}

	public ByteString getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteString buffer) {
		this.buffer = buffer;
	}

	public EventInfo getInfo() {
		return info;
	}

	public void setInfo(EventInfo info) {
		this.info = info;
	}

	public static class EventInfo {
		private EventID eventId;
		private EventID parentEventId;
		private String eventName;
		private Instant startTime;
		private Instant endTime;

		public EventInfo() {
		}

		public EventInfo(EventInfo info) {
			this.eventId = info.eventId;
			this.parentEventId = info.parentEventId;
			this.eventName = info.eventName;
			this.startTime = info.startTime;
			this.endTime = info.endTime;
		}

		public EventID getEventId() {
			return eventId;
		}

		public void setEventId(EventID eventId) {
			this.eventId = eventId;
		}

		public EventID getParentEventId() {
			return parentEventId;
		}

		public void setParentEventId(EventID parentEventId) {
			this.parentEventId = parentEventId;
		}

		public String getEventName() {
			return eventName;
		}

		public void setEventName(String eventName) {
			this.eventName = eventName;
		}

		public Instant getStartTime() {
			return startTime;
		}

		public void setStartTime(Instant startTime) {
			this.startTime = startTime;
		}

		public Instant getEndTime() {
			return endTime;
		}

		public void setEndTime(Instant endTime) {
			this.endTime = endTime;
		}
	}
	
}
