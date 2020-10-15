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

package com.exactpro.th2.act.framework.builders;

import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.RhAction;
import com.exactpro.th2.act.grpc.hand.rhactions.RhWinActionsMessages;

public class SendTextBuilder extends DefaultBuilder<SendTextBuilder> {
	
	public static final String TEXT_FIELD_NAME = "text";

	protected String text;
	protected String clearBefore;
	protected String isDirectText;

	public SendTextBuilder(UIFrameworkContext context) {
		super(context);
	}

	public SendTextBuilder text(String text) {
		this.text = text;
		return this;
	}

	public SendTextBuilder clearBefore(boolean clearBefore) {
		return clearBefore(String.valueOf(clearBefore));
	}

	public SendTextBuilder clearBefore(String clearBefore) {
		this.clearBefore = clearBefore;
		return this;
	}

	public SendTextBuilder isDirectText(String isDirectText) {
		this.isDirectText = isDirectText;
		return this;
	}

	public SendTextBuilder isDirectText(boolean isDirectText) {
		return this.isDirectText(String.valueOf(isDirectText));
	}

	@Override
	protected SendTextBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WinSendText";
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(text, TEXT_FIELD_NAME);
		RhWinActionsMessages.WinSendText.Builder sendTextBuilder = RhWinActionsMessages.WinSendText.newBuilder();
		sendTextBuilder.setText(text);
		if (this.winLocator != null) {
			sendTextBuilder.addAllLocators(buildWinLocator(this.winLocator));
		}
		addIfNotEmpty(id, sendTextBuilder::setId);
		addIfNotEmpty(execute, sendTextBuilder::setExecute);
		addIfNotEmpty(isDirectText, sendTextBuilder::setIsDirectText);
		addIfNotEmpty(clearBefore, sendTextBuilder::setClearBefore);
		
		return RhAction.newBuilder().setWinSendText(sendTextBuilder.build()).build();
	}
}
