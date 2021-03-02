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

package com.exactpro.th2.act.framework.builders.web;

import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.RhAction;
import com.exactpro.th2.act.grpc.hand.rhactions.RhActionsMessages;

public class WaitForNewBuilder extends AbstractWebBuilder<WaitForNewBuilder> {

	public static final String SECONDS_PARAM = "seconds";
	public static final String CHECK_MILLIS_PARAM = "checkMillis";
	
	private Integer seconds;
	private Integer checkMillis;

	protected WaitForNewBuilder(UIFrameworkContext context) {
		super(context);
	}

	@Override
	protected WaitForNewBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WaitForNew";
	}

	public WaitForNewBuilder seconds(int seconds) {
		this.seconds = seconds;
		return this;
	}

	public WaitForNewBuilder checkMillis(Integer checkMillis) {
		this.checkMillis = checkMillis;
		return this;
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		checkRequiredFields(this.seconds, SECONDS_PARAM, this.checkMillis, CHECK_MILLIS_PARAM, this.locator, LOCATOR_PARAM);
		RhActionsMessages.WaitForNew.Builder builder = RhActionsMessages.WaitForNew.newBuilder();
		addIfNotNull(webId, builder::setWebId);
		this.writeLocator(builder::setLocator, builder::setMatcher);
		builder.setSeconds(this.seconds);
		builder.setCheckMillis(this.checkMillis);
		return RhAction.newBuilder().setWaitForNew(builder).build();
	}
}
