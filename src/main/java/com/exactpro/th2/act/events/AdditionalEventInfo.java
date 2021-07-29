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

package com.exactpro.th2.act.events;

import java.util.Map;

public class AdditionalEventInfo {
	
	private String inputTableHeader;
	private Map<String, String> inputTable;
	
	private String description;

	private String errorText;
	private Throwable throwable;
	
	private boolean status = true;

	public String getInputTableHeader() {
		return inputTableHeader;
	}

	public Map<String, String> getInputTable() {
		return inputTable;
	}

	public String getDescription() {
		return description;
	}

	public String getErrorText() {
		return errorText;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public boolean isStatus() {
		return status;
	}
	
	public void setError(String errorText, Throwable throwable) {
		this.errorText = errorText;
		this.throwable = throwable;
		this.status = false;
	}

	public void setInputTableHeader(String inputTableHeader) {
		this.inputTableHeader = inputTableHeader;
	}

	public void setInputTable(Map<String, String> inputTable) {
		this.inputTable = inputTable;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
