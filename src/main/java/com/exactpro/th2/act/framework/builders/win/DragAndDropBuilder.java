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

public class DragAndDropBuilder extends WindowBuilder<DragAndDropBuilder> {
	public static final String FROM_WIN_LOCATOR_FIELD_NAME = "fromWinLocator";
	public static final String TO_WIN_LOCATOR_FIELD_NAME = "toWinLocator";

	private WinLocator toWinLocator;
	private String fromOffsetX;
	private String fromOffsetY;
	private String toOffsetX;
	private String toOffsetY;


	public DragAndDropBuilder(UIFrameworkContext context) {
		super(context);
	}


	public DragAndDropBuilder toWinLocator(WinLocator locator) {
		this.toWinLocator = locator;
		return getBuilder();
	}

	public DragAndDropBuilder fromOffset(String x, String y) {
		this.fromOffsetX = x;
		this.fromOffsetY = y;
		return getBuilder();
	}

	public DragAndDropBuilder toOffset(String x, String y) {
		this.toOffsetX = x;
		this.toOffsetY = y;
		return getBuilder();
	}


	@Override
	protected DragAndDropBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WinDragAndDrop";
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(this.winLocator, FROM_WIN_LOCATOR_FIELD_NAME, this.toWinLocator, TO_WIN_LOCATOR_FIELD_NAME);
		RhWinActionsMessages.WinDragAndDrop.Builder builder = RhWinActionsMessages.WinDragAndDrop.newBuilder();
		builder.addAllFromLocators(buildWinLocator(this.winLocator));
		builder.addAllToLocators(buildWinLocator(this.toWinLocator));
		builder.setBaseParams(buildBaseParam());

		if (!StringUtils.isEmpty(fromOffsetX) && !StringUtils.isEmpty(fromOffsetY))
			builder.setFromOffsetX(fromOffsetX).setFromOffsetY(fromOffsetY);

		if (!StringUtils.isEmpty(toOffsetX) && !StringUtils.isEmpty(toOffsetY))
			builder.setToOffsetX(toOffsetX).setToOffsetY(toOffsetY);

		return RhAction.newBuilder().setWinDragAndDrop(builder.build()).build();
	}
}
