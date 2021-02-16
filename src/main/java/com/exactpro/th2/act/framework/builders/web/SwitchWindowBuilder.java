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

package com.exactpro.th2.act.framework.builders.web;

import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.RhAction;
import com.exactpro.th2.act.grpc.hand.rhactions.RhActionsMessages;

public class SwitchWindowBuilder extends WebDefaultBuilder<SwitchWindowBuilder> {
	
	public static final String WINDOW_NAME_PARAM = "window";
	private Integer windowName;
	
	protected SwitchWindowBuilder(UIFrameworkContext context) {
		super(context);
	}

	@Override
	protected SwitchWindowBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "SwitchWindow";
	}

	public SwitchWindowBuilder windowName(int windowName) {
		this.windowName = windowName;
		return this;
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		checkRequiredFields(this.windowName, WINDOW_NAME_PARAM);
		RhActionsMessages.SwitchWindow.Builder builder = RhActionsMessages.SwitchWindow.newBuilder();
		builder.setWindow(this.windowName);
		return RhAction.newBuilder().setSwitchWindow(builder).build();
	}
}
