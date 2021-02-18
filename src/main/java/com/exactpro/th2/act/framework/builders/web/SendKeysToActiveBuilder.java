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

public class SendKeysToActiveBuilder extends WebDefaultBuilder<SendKeysToActiveBuilder> {
	
	private String text1, text2;
	
	protected SendKeysToActiveBuilder(UIFrameworkContext context) {
		super(context);
	}

	@Override
	protected SendKeysToActiveBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "SendKeysToActive";
	}

	public SendKeysToActiveBuilder text(String text1) {
		this.text1 = text1;
		return this;
	}

	public SendKeysToActiveBuilder text2(String text2) {
		this.text2 = text2;
		return this;
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(text1, LOCATOR_PARAM);
		RhActionsMessages.SendKeysToActive.Builder builder = RhActionsMessages.SendKeysToActive.newBuilder();
		
		builder.setText(text1);
		addIfNotNull(text2, builder::setText2);

		return RhAction.newBuilder().setSendKeysToActive(builder).build();
	}
}
