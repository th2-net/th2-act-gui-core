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

package com.exactpro.th2.act.framework;

import com.exactpro.th2.act.events.AdditionalEventInfo;
import com.exactpro.th2.act.grpc.hand.RhActionList;
import com.exactpro.th2.act.grpc.hand.RhActionsBatch;
import com.exactpro.th2.act.grpc.hand.RhBatchResponse;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.common.event.EventUtils;
import com.exactpro.th2.common.grpc.EventID;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class UIFrameworkContext<T> {

	private RhSessionID sessionID;
	private EventID parentEventId;

	private HandExecutor handExecutor;
	
	private Map<String, ContextInfoData> contextInfo;
	protected List<T> buffer;

	public UIFrameworkContext(RhSessionID sessionID, HandExecutor handExecutor) {
		this.sessionID = sessionID;
		this.handExecutor = handExecutor;
		this.contextInfo = new LinkedHashMap<>();
		this.buffer = new ArrayList<>();
	}

	/**
	 * @deprecated
	 * This method is no longer acceptable to compute time between versions.
	 * <p> Use {@link #submit(ExecutionParams)} instead.
	 */
	@Deprecated(forRemoval = true)
	public RhBatchResponse submit(String eventName) {
		return this.submit(ExecutionParams.createDefaultParams(eventName));
	}

	/**
	 * @deprecated
	 * This method is no longer acceptable to compute time between versions.
	 * <p> Use {@link #submit(ExecutionParams)} instead.
	 */
	@Deprecated(forRemoval = true)
	public RhBatchResponse submit(String eventName, boolean storeActionMessages) {
		return this.submit(eventName, storeActionMessages, true, null, InternalMessageType.PLAIN_STRING);
	}

	/**
	 * @deprecated
	 * This method is no longer acceptable to compute time between versions.
	 * <p> Use {@link #submit(ExecutionParams)} instead.
	 */
	@Deprecated(forRemoval = true)
	public RhBatchResponse submit(String eventName, boolean storeActionMessages, AdditionalEventInfo info) {
		return this.submit(eventName, true, storeActionMessages, info, InternalMessageType.PLAIN_STRING);
	}

	/**
	 * @deprecated
	 * This method is no longer acceptable to compute time between versions.
	 * <p> Use {@link #submit(ExecutionParams)} instead.
	 */
	@Deprecated(forRemoval = true)
	public RhBatchResponse submit(boolean clearBuffer, String eventName, boolean storeActionMessages, AdditionalEventInfo info) {
		return this.submit(eventName, clearBuffer, storeActionMessages, info, InternalMessageType.PLAIN_STRING);
	}

	/**
	 * @deprecated
	 * This method is no longer acceptable to compute time between versions.
	 * <p> Use {@link #submit(ExecutionParams)} instead.
	 */
	@Deprecated(forRemoval = true)
	public RhBatchResponse submit(String eventName, boolean clearBuffer, boolean storeActionMessages, AdditionalEventInfo info, InternalMessageType messageType) {
		ExecutionParams executionParams = ExecutionParams.builder()
				.setEventName(eventName)
				.setClearBuffer(clearBuffer)
				.setStoreActionMessages(storeActionMessages)
				.setAdditionalEventInfo(info)
				.setMessageType(messageType)
				.build();
		return this.submit(executionParams);
	}

	protected abstract RhActionList buildActionList();

	public RhBatchResponse submit(ExecutionParams executionParams) {
		if (this.buffer.isEmpty())
			return null;

		RhActionsBatch.Builder builder = RhActionsBatch.newBuilder()
				.setSessionId(sessionID)
				.setEventName(executionParams.getEventName())
				.setParentEventId(this.parentEventId)
				.setStoreActionMessages(executionParams.isStoreActionMessages())
				.setExecutionId(EventUtils.generateUUID())
				.setMessageType(executionParams.getMessageType().getType());

		if (executionParams.hasAdditionalEventInfo()) {
			AdditionalEventInfo additionalEventInfo = executionParams.getAdditionalEventInfo();
			var eventInfo = RhActionsBatch.AdditionalEventInfo.newBuilder();
			if (StringUtils.isNotEmpty(additionalEventInfo.getDescription())) {
				eventInfo.setDescription(additionalEventInfo.getDescription());
			}
			if (MapUtils.isNotEmpty(additionalEventInfo.getInputTable()) &&
					StringUtils.isNotEmpty(additionalEventInfo.getInputTableHeader())) {
				eventInfo.setPrintTable(true);
				eventInfo.setRequestParamsTableTitle(additionalEventInfo.getInputTableHeader());
				additionalEventInfo.getInputTable().forEach((k, v) -> {eventInfo.addKeys(k); eventInfo.addValues(v);});
			}
			builder.setAdditionalEventInfo(eventInfo);
		}

		builder.setRhAction(buildActionList());

		if (executionParams.isClearBuffer()) {
			this.buffer.clear();
		}

		return handExecutor.executeWinGuiScript(builder.build());
	}

	public void setParentEventId(EventID parentEventId) {
		this.parentEventId = parentEventId;
	}

	public EventID getParentEventId() {
		return parentEventId;
	}

	public RhSessionID getSessionID() {
		return sessionID;
	}

	public void addContextInfo(String name, ContextInfoData infoData) {
		ContextInfoData contextInfoData = this.contextInfo.get(name);
		if (contextInfoData != null)
			contextInfoData.merge(infoData.getData());
		else
			this.contextInfo.put(name, infoData);
	}

	public void addContextData(String name, Map<String, String> data) {
		ContextInfoData contextInfoData = this.contextInfo.get(name);
		if (contextInfoData == null)
			addContextInfo(name, new ContextInfoData(data));
		else
			contextInfoData.merge(data);
	}

	public ContextInfoData getContextInfo(String methodName) {
		return contextInfo.get(methodName);
	}
	
	public void addAction(T action) {
		this.buffer.add(action);
	}
}
