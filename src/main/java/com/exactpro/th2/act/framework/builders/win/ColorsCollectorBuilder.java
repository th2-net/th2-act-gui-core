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

public class ColorsCollectorBuilder extends WindowBuilder<ColorsCollectorBuilder>{
	private String startXOffset;
	private String startYOffset;
	private String endXOffset;
	private String endYOffset;


	public ColorsCollectorBuilder(UIFrameworkContext context) {
		super(context);
	}


	public ColorsCollectorBuilder startOffset(int x, int y) {
		return startOffset(String.valueOf(x), String.valueOf(y));
	}

	public ColorsCollectorBuilder startOffset(String x, String y) {
		this.startXOffset = x;
		this.startYOffset = y;
		return getBuilder();
	}

	public ColorsCollectorBuilder endOffset(int x, int y) {
		return endOffset(String.valueOf(x), String.valueOf(y));
	}

	public ColorsCollectorBuilder endOffset(String x, String y) {
		this.endXOffset = x;
		this.endYOffset = y;
		return getBuilder();
	}

	@Override
	protected ColorsCollectorBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WinColorsCollector";
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(this.winLocator, WIN_LOCATOR_FIELD_NAME);
		RhWinActionsMessages.WinColorsCollector.Builder builder = RhWinActionsMessages.WinColorsCollector.newBuilder();
		builder.addAllLocators(buildWinLocator(this.winLocator));
		builder.setBaseParams(buildBaseParam());

		if (StringUtils.isNotEmpty(startXOffset) && StringUtils.isNotEmpty(startYOffset))
			builder.setStartXOffset(startXOffset).setStartYOffset(startYOffset);

		if (StringUtils.isNotEmpty(endXOffset) && StringUtils.isNotEmpty(endYOffset))
			builder.setStartXOffset(endXOffset).setStartYOffset(endYOffset);
		
		return RhAction.newBuilder().setWinColorsCollector(builder.build()).build();
	}
}
