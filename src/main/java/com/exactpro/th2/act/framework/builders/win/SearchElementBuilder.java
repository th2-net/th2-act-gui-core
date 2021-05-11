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

import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.RhAction;
import com.exactpro.th2.act.grpc.hand.rhactions.RhWinActionsMessages;

public class SearchElementBuilder extends AbstractWinBuilder<SearchElementBuilder> {
	
	private boolean isExperimental = true;
	private boolean multipleElements = false;
	
	public SearchElementBuilder(UIFrameworkContext context) {
		super(context);
	}

	@Override
	protected SearchElementBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WinSearchElement";
	}

	public SearchElementBuilder experimental(boolean experimental) {
		isExperimental = experimental;
		return this;
	}

	public SearchElementBuilder multipleElements(boolean multipleElements) {
		this.multipleElements = multipleElements;
		return this;
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(winLocator, WIN_LOCATOR_FIELD_NAME);
		RhWinActionsMessages.WinSearchElement.Builder searchElementBuilder = RhWinActionsMessages.WinSearchElement.newBuilder();
		searchElementBuilder.addAllLocators(this.buildWinLocator(winLocator));
		this.addIfNotEmpty(id, searchElementBuilder::setId);
		this.addIfNotEmpty(execute, searchElementBuilder::setExecute);
		searchElementBuilder.setNonExperimental(!isExperimental);
		searchElementBuilder.setMultipleElements(multipleElements);
		return RhAction.newBuilder().setWinSearchElement(searchElementBuilder.build()).build();
	}
}
