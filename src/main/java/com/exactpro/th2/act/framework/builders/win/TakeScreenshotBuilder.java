/*
 * Copyright 2021-2021 Exactpro (Exactpro Systems Limited)
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

public class TakeScreenshotBuilder extends AbstractWinBuilder<TakeScreenshotBuilder> {
	
	private String name;
	
	public TakeScreenshotBuilder(UIFrameworkContext context) {
		super(context);
	}

	@Override
	protected TakeScreenshotBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "TakeScreenshot";
	}

	public TakeScreenshotBuilder name(String name) {
		this.name = name;
		return this;
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		var builder = RhWinActionsMessages.WinTakeScreenshot.newBuilder();
		if (winLocator != null) {
			builder.addAllLocators(buildWinLocator(this.winLocator));	
		}
		builder.setBaseParams(buildBaseParam());
		addIfNotEmpty(this.name, builder::setName);
		return RhAction.newBuilder().setWinTakeScreenshot(builder).build();
	}
}
