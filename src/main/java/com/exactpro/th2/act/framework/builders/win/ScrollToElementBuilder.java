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

package com.exactpro.th2.act.framework.builders.win;

import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.RhAction;
import com.exactpro.th2.act.grpc.hand.rhactions.RhWinActionsMessages;
import org.apache.commons.lang3.StringUtils;

public class ScrollToElementBuilder extends WindowBuilder<ScrollToElementBuilder> {
	public static final String ELEMENT_WIN_LOCATOR_FIELD_NAME = "elementWinLocator";
	public static final String ACTION_WIN_LOCATOR_FIELD_NAME = "actionWinLocator";

	private static final int DEFAULT_MAX_ITERATION_COUNT = 10;

	private WinLocator actionWinLocator;
	private int maxIterations;
	private ScrollType scrollType;
	private String clickOffsetX;
	private String clickOffsetY;
	private boolean isElementInTree = false;
	private boolean isElementShouldBeDisplayed = false;
	private String textToSend;


	public ScrollToElementBuilder(UIFrameworkContext context) {
		super(context);
	}


	public ScrollToElementBuilder actionWinLocator(WinLocator locator) {
		this.actionWinLocator = locator;
		return getBuilder();
	}

	public ScrollToElementBuilder maxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
		return getBuilder();
	}

	public ScrollToElementBuilder scrollType(ScrollType scrollType) {
		this.scrollType = scrollType;
		return getBuilder();
	}

	public ScrollToElementBuilder offset(String x, String y) {
		this.clickOffsetX = x;
		this.clickOffsetY = y;
		return getBuilder();
	}

	public ScrollToElementBuilder elementInTree() {
		this.isElementInTree = true;
		return getBuilder();
	}

	public ScrollToElementBuilder elementShouldBeDisplayed() {
		this.isElementShouldBeDisplayed = true;
		return getBuilder();
	}

	public ScrollToElementBuilder textToSend(String textToSend) {
		this.textToSend = textToSend;
		return getBuilder();
	}


	@Override
	protected ScrollToElementBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WinScrollToElement";
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(this.winLocator, ELEMENT_WIN_LOCATOR_FIELD_NAME, this.actionWinLocator, ACTION_WIN_LOCATOR_FIELD_NAME);
		RhWinActionsMessages.WinScrollToElement.Builder builder = RhWinActionsMessages.WinScrollToElement.newBuilder();
		builder.addAllActionLocators(buildWinLocator(this.winLocator));
		builder.addAllActionLocators(buildWinLocator(this.actionWinLocator));
		addIfNotEmpty(id, builder::setId);
		addIfNotEmpty(execute, builder::setExecute);
		addIfNotEmpty(execute, builder::setExecute);

		if (!StringUtils.isEmpty(clickOffsetX) && !StringUtils.isEmpty(clickOffsetY))
			builder.setClickOffsetX(clickOffsetX).setClickOffsetY(clickOffsetY);

		builder.setMaxIterations(maxIterations == 0 ? DEFAULT_MAX_ITERATION_COUNT : maxIterations);
		builder.setScrollType(scrollType.getScrollType());
		builder.setIsElementInTree(isElementInTree);
		builder.setIsElementShouldBeDisplayed(isElementShouldBeDisplayed);
		addIfNotEmpty(textToSend, builder::setTextToSend);

		return RhAction.newBuilder().setWinScrollToElement(builder.build()).build();
	}


	public enum ScrollType {
		CLICK(RhWinActionsMessages.WinScrollToElement.ScrollType.CLICK),
		TEXT(RhWinActionsMessages.WinScrollToElement.ScrollType.TEXT);

		private final RhWinActionsMessages.WinScrollToElement.ScrollType scrollType;

		ScrollType(RhWinActionsMessages.WinScrollToElement.ScrollType scrollType) {
			this.scrollType = scrollType;
		}

		public RhWinActionsMessages.WinScrollToElement.ScrollType getScrollType() {
			return scrollType;
		}
	}
}
