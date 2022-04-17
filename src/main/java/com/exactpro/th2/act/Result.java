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

package com.exactpro.th2.act;

import java.util.Map;

public class Result<T> {
	protected ActResult.ActExecutionStatus scriptStatus;
	protected String statusInfo;
	protected String errorInfo;
	protected Map<String, T> data;
	protected Object sessionID;
	protected String executionId;

	public ActResult.ActExecutionStatus getScriptStatus() {
		return scriptStatus;
	}

	public void setScriptStatus(ActResult.ActExecutionStatus scriptStatus) {
		this.scriptStatus = scriptStatus;
	}

	public String getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public Map<String, T> getData() {
		return data;
	}

	public void setData(Map<String, T> data) {
		this.data = data;
	}

	public Object getSessionID() {
		return sessionID;
	}

	public void setSessionID(Object sessionID) {
		this.sessionID = sessionID;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public enum ActExecutionStatus {
		SUCCESS,
		COMPILE_ERROR,
		EXECUTION_ERROR,
		ACT_ERROR,
		HAND_INTERNAL_ERROR,
		UNKNOWN_ERROR
	}
}
