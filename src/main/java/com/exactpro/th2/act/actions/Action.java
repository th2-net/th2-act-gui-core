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

package com.exactpro.th2.act.actions;

import com.exactpro.th2.act.ActResult;
import com.exactpro.th2.act.Result;
import com.exactpro.th2.act.events.AdditionalEventInfo;
import com.exactpro.th2.act.framework.ExecutionParams;
import com.exactpro.th2.act.framework.UIFramework;
import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.UIFrameworkSessionContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkException;
import com.exactpro.th2.act.grpc.hand.RhBatchResponse;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.common.grpc.EventID;
import org.slf4j.Logger;

import java.util.Map;

public abstract class Action<T, K extends UIFrameworkContext<?>, L extends UIFrameworkSessionContext<K>, R extends Result<?>> {
	protected final UIFramework<K, L> framework;
	protected final Logger logger;

	protected Map<String, String> requestTable;

	public Action(UIFramework<K, L> framework) {
		this.framework = framework;
		this.logger = getLogger();
	}

	protected abstract String getName();
	protected abstract Map<String, String> convertRequestParams(T details);

	protected abstract RhSessionID getSessionID(T details);
	protected abstract EventID getParentEventId(T details);
	protected abstract Logger getLogger();
	protected abstract void collectActions(T details, K context, R result) throws UIFrameworkException;
	protected abstract void processResult(R result) throws UIFrameworkException;
	protected abstract String getStatusInfo();

	protected boolean storeParentEvent() {
		return true;
	}
	protected boolean storeActionMessages() {
		return false;
	}

	protected String getRequestTableHeader() {
		return "Request parameters";
	}

	protected String getDescription() {
		return null;
	}

	public void run(T details) {
		RhSessionID sessionID = getSessionID(details);

		R actResult = createActResult();
		K frameworkContext = null;
		EventID eventId = null;

		try {
			eventId = getParentEventId(details);
			frameworkContext = framework.newExecution(sessionID);
			this.requestTable = this.convertRequestParams(details);
			eventId = this.processAndGetEventId(eventId, frameworkContext, details);

			this.collectActions(details, frameworkContext, actResult);
			this.submitActions(frameworkContext, actResult);
			actResult.setSessionID(sessionID);
		} catch (Exception e) {
			logger.error("An error occurred while executing action. Cannot unregister framework session", e);
			printEventError(eventId, "Error: " + getName(), "An internal action error has occurred", e);
			actResult.setScriptStatus(ActResult.ActExecutionStatus.ACT_ERROR);
			actResult.setErrorInfo(e.getMessage());
		} finally {
			this.processResult(actResult, eventId);
			if (frameworkContext != null) {
				framework.onExecutionFinished(frameworkContext);
			}
		}
	}

	protected ActResult.ActExecutionStatus convertStatusFromRh(RhBatchResponse.ScriptExecutionStatus status) {
		switch (status) {
			case EXECUTION_ERROR: return ActResult.ActExecutionStatus.EXECUTION_ERROR;
			case SUCCESS: return ActResult.ActExecutionStatus.SUCCESS;
			case COMPILE_ERROR: return ActResult.ActExecutionStatus.COMPILE_ERROR;
			case HAND_INTERNAL_ERROR: return ActResult.ActExecutionStatus.HAND_INTERNAL_ERROR;
			case UNRECOGNIZED:
			default: return ActResult.ActExecutionStatus.UNKNOWN_ERROR;
		}
	}

	protected void submitActions(UIFrameworkContext<?> frameworkContext, R respBuild) {
		AdditionalEventInfo info = null;
		if (!storeParentEvent()) {
			info = createAdditionalEventInfo();
		}
		ExecutionParams executionParams = ExecutionParams.builder()
				.setEventName(getName())
				.setStoreActionMessages(storeActionMessages())
				.setAdditionalEventInfo(info)
				.build();
		RhBatchResponse response = frameworkContext.submit(executionParams);
		if (response == null || response.getScriptStatus() == RhBatchResponse.ScriptExecutionStatus.SUCCESS) {
			respBuild.setStatusInfo(getStatusInfo());
			respBuild.setScriptStatus(ActResult.ActExecutionStatus.SUCCESS);
		} else {
			respBuild.setErrorInfo(response.getErrorMessage());
			respBuild.setScriptStatus(this.convertStatusFromRh(response.getScriptStatus()));
		}
	}

	protected abstract R createActResult();

	protected EventID processAndGetEventId(EventID parentEventId, K frameworkContext, T details) {
		EventID actionEvent = null;
		if (storeParentEvent()) {
			actionEvent = framework.createEvent(parentEventId, getName(), createAdditionalEventInfo());
			frameworkContext.setParentEventId(actionEvent);
		} else {
			frameworkContext.setParentEventId(parentEventId);
		}

		return actionEvent != null ? actionEvent : parentEventId;
	}

	protected AdditionalEventInfo createAdditionalEventInfo() {
		AdditionalEventInfo addEvInfo = new AdditionalEventInfo();
		addEvInfo.setInputTableHeader(getRequestTableHeader());
		addEvInfo.setInputTable(this.requestTable);
		addEvInfo.setDescription(getDescription());
		return addEvInfo;
	}

	private void processResult(R actResult, EventID eventId) {
		try {
			this.processResult(actResult);
		} catch (Exception e) {
			logger.error("An error occurred while processing act result", e);
			printEventError(eventId, "Processing act result", "Unable to process act result", e);
		}
	}

	protected void printEventError(EventID eventId, String eventName, String errorText, Throwable throwable) {
		AdditionalEventInfo addEvInfo = new AdditionalEventInfo();
		addEvInfo.setError(errorText, throwable);
		if (!storeParentEvent()) {
			addEvInfo.setInputTableHeader(getRequestTableHeader());
			addEvInfo.setInputTable(this.requestTable);
			addEvInfo.setDescription(getDescription());
		}
		framework.createEvent(eventId, eventName, addEvInfo);
	}
}
