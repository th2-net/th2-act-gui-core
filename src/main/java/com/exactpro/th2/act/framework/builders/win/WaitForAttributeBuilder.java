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

package com.exactpro.th2.act.framework.builders.win;

import com.exactpro.th2.act.framework.UIWinFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.rhactions.RhWinActionsMessages;
import com.exactpro.th2.act.grpc.hand.rhactions.RhWinActionsMessages.RhWinActions;

public class WaitForAttributeBuilder extends AbstractWinBuilder<WaitForAttributeBuilder> {
	
	public static final String ATTRIBUTE_NAME_FIELD_NAME = "attributeName";
	public static final String EXPECTED_VALUE_FIELD_NAME = "expectedValue";
	public static final String MAX_TIMEOUT_FIELD_NAME = "maxTimeout";

	private String attributeName;
	private String expectedValue;
	private String maxTimeout;
	private String checkInterval;

	public WaitForAttributeBuilder(UIWinFrameworkContext context) {
		super(context);
	}

	public WaitForAttributeBuilder attributeName(String attributeName) {
		this.attributeName = attributeName;
		return this;
	}

	public WaitForAttributeBuilder expectedValue(String expectedValue) {
		this.expectedValue = expectedValue;
		return this;
	}

	public WaitForAttributeBuilder maxTimeout(String maxTimeout) {
		this.maxTimeout = maxTimeout;
		return this;
	}

	public WaitForAttributeBuilder maxTimeout(int maxTimeout) {
		return maxTimeout(String.valueOf(maxTimeout));
	}

	public WaitForAttributeBuilder checkInterval(String checkInterval) {
		this.checkInterval = checkInterval;
		return this;
	}

	public WaitForAttributeBuilder checkInterval(int checkInterval) {
		return checkInterval(String.valueOf(checkInterval));
	}

	@Override
	protected WaitForAttributeBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WinWaitForAttribute";
	}

	@Override
	protected RhWinActions buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(attributeName, ATTRIBUTE_NAME_FIELD_NAME, expectedValue, EXPECTED_VALUE_FIELD_NAME, 
				maxTimeout, MAX_TIMEOUT_FIELD_NAME);
		RhWinActionsMessages.WinWaitForAttribute.Builder winForAttr = RhWinActionsMessages.WinWaitForAttribute.newBuilder();
		winForAttr.addAllLocators(this.buildWinLocator(this.winLocator));
		winForAttr.setAttributeName(attributeName);
		winForAttr.setExpectedValue(expectedValue);
		winForAttr.setMaxTimeout(maxTimeout);
		this.addIfNotEmpty(checkInterval, winForAttr::setCheckInterval);
		winForAttr.setBaseParams(buildBaseParam());
		return RhWinActions.newBuilder().setWinWaitForAttribute(winForAttr).build();
	}
}
