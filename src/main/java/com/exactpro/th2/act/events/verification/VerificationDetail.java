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

package com.exactpro.th2.act.events.verification;

import org.apache.commons.lang3.StringUtils;

public class VerificationDetail {
	
	private String paramName;
	private String expected;
	private String actual;
	private boolean equals;

	public VerificationDetail(String paramName, String expected, String actual, boolean equals) {
		this.paramName = paramName;
		this.expected = expected;
		this.actual = actual;
		this.equals = equals;
	}

	public VerificationDetail() {
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getExpected() {
		return expected;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}

	public boolean isEquals() {
		return equals;
	}

	public void setEquals(boolean equals) {
		this.equals = equals;
	}
	
	public static VerificationDetail compare(String field, String expected, String actual) {
		return new VerificationDetail(field, expected, actual, StringUtils.equals(expected, actual));
	}

	public static VerificationDetail compareIgnoreCase(String field, String expected, String actual) {
		return new VerificationDetail(field, expected, actual, StringUtils.equalsIgnoreCase(expected, actual));
	}
}
