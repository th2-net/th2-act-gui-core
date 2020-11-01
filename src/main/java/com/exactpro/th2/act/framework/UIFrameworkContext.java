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

import com.exactpro.th2.act.framework.builders.BuilderManager;
import com.exactpro.th2.act.framework.ui.UIElement;
import com.exactpro.th2.act.grpc.hand.RhAction;
import com.exactpro.th2.act.grpc.hand.RhActionsList;
import com.exactpro.th2.act.grpc.hand.RhBatchResponse;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.infra.grpc.EventID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UIFrameworkContext {

	private RhSessionID sessionID;
	private EventID parentEventId;

	private HandExecutor handExecutor;

	private List<RhAction> buffer;
	private Map<String, RhBatchResponse> rhResponses;
	private Map<String, ContextInfoData> contextInfo;

	private MainWindowWrapper<? extends UIElement> mainWindowWrapper;

	public UIFrameworkContext(RhSessionID sessionID, HandExecutor handExecutor) {
		this.sessionID = sessionID;
		this.handExecutor = handExecutor;
		this.buffer = new ArrayList<>();
		this.rhResponses = new LinkedHashMap<>();
		this.contextInfo = new LinkedHashMap<>();
	}

	public RhBatchResponse submit(String eventName) {
		return this.submit(true, eventName, Collections.emptyMap());
	}

	public RhBatchResponse submit(String eventName, Map<String, String> eventParamList) {
		return this.submit(true, eventName, eventParamList);
	}

	public RhBatchResponse submit(boolean clear, String eventName, Map<String, String> eventParamList) {
		if (buffer.isEmpty())
			return null;
		
		RhActionsList.Builder builder = RhActionsList.newBuilder();
		builder.setSessionId(sessionID);
		for (RhAction rhAction : buffer) {
			builder.addRhAction(rhAction);
		}
		RhBatchResponse response = handExecutor.executeWinGuiScript(eventParamList, eventName,
				this.parentEventId, builder);
		this.rhResponses.put(eventName, response);
		if (clear) {
			buffer.clear();
		}
		return response;
	}

	public void setParentEventId(EventID parentEventId) {
		this.parentEventId = parentEventId;
	}

	public EventID getParentEventId() {
		return parentEventId;
	}

	public void addRhAction(RhAction action) {
		this.buffer.add(action);
	}

	public RhSessionID getSessionID() {
		return sessionID;
	}

	public BuilderManager createBuilderManager() {
		return new BuilderManager(this);
	}

	public MainWindowWrapper<? extends UIElement> getMainWindowWrapper() {
		return mainWindowWrapper;
	}

	public void setMainWindow(MainWindowWrapper<? extends UIElement> mainWindowWrapper) {
		this.mainWindowWrapper = mainWindowWrapper;
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
}
