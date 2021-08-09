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

public class SendTextBuilder extends AbstractWinBuilder<SendTextBuilder> {
	
	public static final String TEXT_FIELD_NAME = "text";

	protected String text;
	protected String clearBefore;
	protected String isDirectText;

	public SendTextBuilder(UIWinFrameworkContext context) {
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
	protected RhWinActions buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(text, TEXT_FIELD_NAME);
		RhWinActionsMessages.WinSendText.Builder sendTextBuilder = RhWinActionsMessages.WinSendText.newBuilder();
		sendTextBuilder.setText(text);
		if (this.winLocator != null) {
			sendTextBuilder.addAllLocators(buildWinLocator(this.winLocator));
		}
		sendTextBuilder.setBaseParams(buildBaseParam());
		addIfNotEmpty(isDirectText, sendTextBuilder::setIsDirectText);
		addIfNotEmpty(clearBefore, sendTextBuilder::setClearBefore);
		
		return RhWinActions.newBuilder().setWinSendText(sendTextBuilder.build()).build();
	}
}
