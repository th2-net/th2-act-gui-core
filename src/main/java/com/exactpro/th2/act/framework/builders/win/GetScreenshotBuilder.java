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

import com.exactpro.th2.act.framework.UIWinFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.rhactions.RhWinActionsMessages;
import com.exactpro.th2.act.grpc.hand.rhactions.RhWinActionsMessages.RhWinActions;

public class GetScreenshotBuilder extends AbstractWinBuilder<GetScreenshotBuilder> {
	
	public GetScreenshotBuilder(UIWinFrameworkContext context) {
		super(context);
	}

	@Override
	protected GetScreenshotBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "GetScreenshot";
	}

	@Override
	protected RhWinActions buildAction() throws UIFrameworkBuildingException {
		RhWinActionsMessages.WinGetScreenshot.Builder builder = RhWinActionsMessages.WinGetScreenshot.newBuilder();
		if (winLocator != null) {
			builder.addAllLocators(buildWinLocator(this.winLocator));	
		}
		builder.setBaseParams(buildBaseParam());
		return RhWinActions.newBuilder().setWinGetScreenshot(builder).build();
	}
}
