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

public class ExpectedField
{
	private final String field;
	private final String value;
	private final String actionId;
	private final boolean presenceCheck;

	public ExpectedField(String field, String value, String actionId)
	{
		this.field = field;
		this.value = value;
		this.actionId = actionId;
		this.presenceCheck = false;
	}

	public ExpectedField(String field, String value, String actionId, boolean presenceCheck)
	{
		this.field = field;
		this.value = value;
		this.actionId = actionId;
		this.presenceCheck = presenceCheck;
	}

	public String getField()
	{
		return field;
	}

	public String getValue()
	{
		return value;
	}

	public boolean isPresenceCheck()
	{
		return presenceCheck;
	}

	public String getActionId() {
		return actionId;
	}
}
