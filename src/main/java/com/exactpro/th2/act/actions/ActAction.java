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

import com.exactpro.th2.act.framework.UIFramework;
import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkException;
import com.exactpro.th2.act.grpc.ActResponse;
import com.exactpro.th2.act.grpc.hand.RhBatchResponse;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.infra.grpc.EventID;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public abstract class ActAction<T> {
	
	protected final UIFramework framework;
	protected final String name;
	protected final Logger logger;

	private StreamObserver<ActResponse> responseObserver;

	public ActAction(UIFramework framework, StreamObserver<ActResponse> responseObserver) {
		this.framework = framework;
		this.name = getName();
		this.logger = getLogger();
		this.responseObserver = responseObserver;
	}
	
	protected abstract String getName();
	protected abstract Map<String, String> convertRequestParams(T details);

	protected abstract RhSessionID getSessionID(T details);
	protected abstract EventID getParentEventId(T details);
	protected abstract Logger getLogger();
	protected abstract void collectActions(T details, UIFrameworkContext context, ActResponse.Builder respBuild) throws UIFrameworkException;
	protected abstract String getStatusInfo();
	protected boolean storeParentEvent() {
		return true;
	}

	public void run(T details) {

		RhSessionID sessionID = getSessionID(details);

		ActResponse.Builder respBuild = ActResponse.newBuilder();
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

			this.collectActions(details, frameworkContext, respBuild);
			this.submitActions(details, frameworkContext, respBuild);
			respBuild.setSessionID(sessionID);

		} catch (UIFrameworkException e) {
			logger.error("Cannot execute", e);
			respBuild.setScriptStatus(ActResponse.ExecutionStatus.ACT_ERROR);
			respBuild.setErrorInfo("Cannot unregister framework session:" + e.getMessage());
		} finally {
			if (frameworkContext != null) {
				framework.onExecutionFinished(frameworkContext);
			}
		}

		responseObserver.onNext(respBuild.build());
		responseObserver.onCompleted();
	}

	protected ActResponse.ExecutionStatus convertStatusFromRh(RhBatchResponse.ScriptExecutionStatus status) {
		switch (status) {
			case EXECUTION_ERROR: return ActResponse.ExecutionStatus.EXECUTION_ERROR;
			case SUCCESS: return ActResponse.ExecutionStatus.SUCCESS;
			case COMPILE_ERROR: return ActResponse.ExecutionStatus.COMPILE_ERROR;
			default: return ActResponse.ExecutionStatus.UNRECOGNIZED;
		}
	}

	protected void submitActions(T request, UIFrameworkContext frameworkContext, ActResponse.Builder respBuild) {
		RhBatchResponse response = frameworkContext.submit(getName(), convertRequestParams(request));
		if (response == null || response.getScriptStatus() == RhBatchResponse.ScriptExecutionStatus.SUCCESS) {
			respBuild.setStatusInfo(getStatusInfo());
			respBuild.setScriptStatus(ActResponse.ExecutionStatus.SUCCESS);
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
