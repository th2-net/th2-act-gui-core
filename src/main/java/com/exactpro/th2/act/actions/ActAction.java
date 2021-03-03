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

package com.exactpro.th2.act.actions;

import com.exactpro.th2.act.ActResult;
import com.exactpro.th2.act.framework.UIFramework;
import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.UIFrameworkSessionContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkException;
import com.exactpro.th2.act.grpc.hand.RhBatchResponse;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.common.grpc.EventID;
import org.slf4j.Logger;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public abstract class ActAction<T, K extends UIFrameworkContext, L extends UIFrameworkSessionContext<K>> {
	
	protected final UIFramework<K, L> framework;
	protected final Logger logger;

	public ActAction(UIFramework<K, L> framework) {
		this.framework = framework;
		this.logger = getLogger();
	}
	
	protected abstract String getName();
	protected abstract Map<String, String> convertRequestParams(T details);

	protected abstract RhSessionID getSessionID(T details);
	protected abstract EventID getParentEventId(T details);
	protected abstract Logger getLogger();
	protected abstract void collectActions(T details, K context, ActResult result) throws UIFrameworkException;
	protected abstract void processResult(ActResult result) throws UIFrameworkException;
	protected abstract String getStatusInfo();
	protected boolean storeParentEvent() {
		return true;
	}

	public void run(T details) {
		RhSessionID sessionID = getSessionID(details);

		ActResult actResult = createActResult();
		K frameworkContext = null;
		EventID eventId = null;

		try {
			frameworkContext = framework.newExecution(sessionID);
			eventId = this.processAndGetEventId(frameworkContext, details);

			this.collectActions(details, frameworkContext, actResult);
			this.submitActions(details, frameworkContext, actResult);
			actResult.setSessionID(sessionID);
		} catch (Exception e) {
			logger.error("An error occurred while executing action", e);
			framework.createErrorEvent(eventId, "Error: " + getName(), "An internal action error has occurred", e);
			actResult.setScriptStatus(ActResult.ActExecutionStatus.ACT_ERROR);
			actResult.setErrorInfo("Cannot unregister framework session:" + e.getMessage());
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
			default: return ActResult.ActExecutionStatus.UNKNOWN_ERROR;
		}
	}

	protected void submitActions(T request, UIFrameworkContext frameworkContext, ActResult respBuild) {
		RhBatchResponse response = frameworkContext.submit(getName(), convertRequestParams(request));
		if (response == null || response.getScriptStatus() == RhBatchResponse.ScriptExecutionStatus.SUCCESS) {
			respBuild.setStatusInfo(getStatusInfo());
			respBuild.setScriptStatus(ActResult.ActExecutionStatus.SUCCESS);
		} else {
			respBuild.setErrorInfo(response.getErrorMessage());
			respBuild.setScriptStatus(this.convertStatusFromRh(response.getScriptStatus()));
		}
	}

	protected void putIfNotEmpty(String key, String value, Map<String, String> params) {
		if (isNotEmpty(value))
			params.put(key, value);
	}

	protected ActResult createActResult() {
		return new ActResult();
	}

	protected EventID processAndGetEventId(K frameworkContext, T details) {
		EventID parentEventId = getParentEventId(details);
		EventID actionEvent = null;
		if (storeParentEvent()) {
			Map<String, String> requestParams = convertRequestParams(details);
			actionEvent = framework.createParentEvent(parentEventId, getName(), requestParams);
			frameworkContext.setParentEventId(actionEvent);
		} else {
			frameworkContext.setParentEventId(parentEventId);
		}

		return actionEvent != null ? actionEvent : parentEventId;
	}


	private void processResult(ActResult actResult, EventID eventId) {
		try {
			this.processResult(actResult);
		} catch (Exception e) {
			logger.error("An error occurred while processing act result", e);
			framework.createErrorEvent(eventId, "Processing act result", "Unable to process act result", e);
		}
	}
}
