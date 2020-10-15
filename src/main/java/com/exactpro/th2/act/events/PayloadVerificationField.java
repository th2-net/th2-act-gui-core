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

package com.exactpro.th2.act.events;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PayloadVerificationField
{
	
	public static final String TYPE = "field";
	public static final String OPERATION = "EQUAL";
	public static final String STATUS_PASSED = "PASSED";
	public static final String STATUS_FAILED = "FAILED";
	
	private final String expected, actual;
	private final boolean passed; 

	public PayloadVerificationField(String expected, String actual)
	{
		this.expected = expected;
		this.actual = actual;
		this.passed = expected.equalsIgnoreCase(actual);
	}

	public PayloadVerificationField(String expected, String actual, boolean passed)
	{
		this.expected = expected;
		this.actual = actual;
		this.passed = passed;
	}

	public String getType()
	{
		return TYPE;
	}

	public String getOperation()
	{
		return OPERATION;
	}

	public String getStatus()
	{
		return passed ? STATUS_PASSED : STATUS_FAILED;
	}

	public boolean getKey()
	{
		return false;
	}

	public String getActual()
	{
		return actual;
	}

	public String getExpected()
	{
		return expected;
	}

	@JsonIgnore
	public boolean isPassed() {
		return passed;
	}
}
