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

public class RestartDriverBuilder extends WindowBuilder<RestartDriverBuilder> {
	
	public RestartDriverBuilder(UIWinFrameworkContext context) {
		super(context);
	}

	@Override
	protected RestartDriverBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WinRestartDriver";
	}

	@Override
	protected RhWinActions buildAction() throws UIFrameworkBuildingException {
		RhWinActionsMessages.WinRestartDriver.Builder restartDriver = RhWinActionsMessages.WinRestartDriver.newBuilder();
		restartDriver.setBaseParams(buildBaseParam());
		return RhWinActions.newBuilder().setWinRestartDriver(restartDriver.build()).build();
	}
}
