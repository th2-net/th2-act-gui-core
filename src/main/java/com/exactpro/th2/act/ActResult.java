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

import java.util.Map;

public class ActResult {

	private ActExecutionStatus scriptStatus;
	private String statusInfo;
	private String errorInfo;
	private Map<String, String> data;
	private Object sessionID;

	public ActExecutionStatus getScriptStatus() {
		return scriptStatus;
	}

	public void setScriptStatus(ActExecutionStatus scriptStatus) {
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

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public Object getSessionID() {
		return sessionID;
	}

	public void setSessionID(Object sessionID) {
		this.sessionID = sessionID;
	}

	public enum ActExecutionStatus {
		SUCCESS,
		ACT_ERROR,
		HAND_ERROR,
		COMPILE_ERROR,
		EXECUTION_ERROR,
		UNKNOWN_ERROR;
	}
}
