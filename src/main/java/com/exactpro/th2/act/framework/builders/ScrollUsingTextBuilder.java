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

public class ScrollUsingTextBuilder extends DefaultBuilder<ScrollUsingTextBuilder> {
	
	public static final String TEXT_LOCATOR_FIELD = "textLocators";
	public static final String TEXT_TO_SEND_FIELD = "textToSend";

	private WinLocator textLocators;
	private String maxIterations;
	private String textToSend;
	
	public ScrollUsingTextBuilder(UIFrameworkContext context) {
		super(context);
	}

	@Override
	protected ScrollUsingTextBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WinScrollUsingText";
	}

	public ScrollUsingTextBuilder textLocators(WinLocator textLocators) {
		this.textLocators = textLocators;
		return getBuilder();
	}

	public ScrollUsingTextBuilder maxIterations(String maxIterations) {
		this.maxIterations = maxIterations;
		return getBuilder();
	}

	public ScrollUsingTextBuilder maxIterations(int maxIterations) {
		this.maxIterations = String.valueOf(maxIterations);
		return getBuilder();
	}

	public ScrollUsingTextBuilder textToSend(String textToSend) {
		this.textToSend = textToSend;
		return getBuilder();
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(winLocator, WIN_LOCATOR_FIELD_NAME, textLocators, TEXT_LOCATOR_FIELD, textToSend, TEXT_TO_SEND_FIELD);
		RhWinActionsMessages.WinScrollUsingText.Builder winScrollUsingText = RhWinActionsMessages.WinScrollUsingText.newBuilder();
		winScrollUsingText.addAllLocators(this.buildWinLocator(this.winLocator));
		winScrollUsingText.addAllTextLocators(this.buildWinLocator(this.textLocators));
		winScrollUsingText.setTextToSend(this.textToSend);
		addIfNotEmpty(id, winScrollUsingText::setId);
		addIfNotEmpty(execute, winScrollUsingText::setExecute);
		addIfNotEmpty(maxIterations, winScrollUsingText::setMaxIterations);
		return RhAction.newBuilder().setWinScrollUsingText(winScrollUsingText).build();
	}
}
