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

package com.exactpro.th2.act.actions;

import com.exactpro.th2.act.ActResult;
import com.exactpro.th2.act.configuration.CustomConfiguration;
import com.exactpro.th2.act.framework.UIFramework;
import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkException;
import com.exactpro.th2.act.grpc.hand.RhBatchResponse;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.common.grpc.EventID;
import org.slf4j.Logger;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public abstract class ActAction<T> {
	
	protected final UIFramework framework;
	protected final String name;
	protected final Logger logger;

	public ActAction(UIFramework framework) {
		this.framework = framework;
		this.name = getName();
		this.logger = getLogger();
	}
	
	protected abstract String getName();
	protected abstract Map<String, String> convertRequestParams(T details);

	protected abstract RhSessionID getSessionID(T details);
	protected abstract EventID getParentEventId(T details);
	protected abstract Logger getLogger();
	protected abstract void collectActions(T details, UIFrameworkContext context, ActResult result) throws UIFrameworkException;
	protected abstract void processResult(ActResult result) throws UIFrameworkException;
	protected abstract String getStatusInfo();
	protected boolean storeParentEvent() {
		return true;
	}

	public void run(T details) {

		RhSessionID sessionID = getSessionID(details);

		ActResult actResult = new ActResult();
		UIFrameworkContext frameworkContext = null;
		try {
			frameworkContext = framework.newExecution(sessionID);
			EventID parentEventId = getParentEventId(details);
			if (storeParentEvent()) {
				Map<String, String> requestParams = convertRequestParams(details);
				EventID loginEvent = framework.createParentEvent(parentEventId, this.name, requestParams);
				frameworkContext.setParentEventId(loginEvent);
			} else {
				frameworkContext.setParentEventId(parentEventId);
			}

			this.collectActions(details, frameworkContext, actResult);
			this.submitActions(details, frameworkContext, actResult);
			actResult.setSessionID(sessionID);

		} catch (UIFrameworkException e) {
			logger.error("Cannot execute", e);
			actResult.setScriptStatus(ActResult.ActExecutionStatus.ACT_ERROR);
			actResult.setErrorInfo("Cannot unregister framework session:" + e.getMessage());
		} finally {
			if (frameworkContext != null) {
				framework.onExecutionFinished(frameworkContext);
			}
		}

		try {
			this.processResult(actResult);
		} catch (UIFrameworkException e) {
			logger.error("Cannot process act result", e);
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
}
