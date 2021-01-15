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
import org.apache.commons.lang3.StringUtils;

public class GetActiveWindowBuilder extends DefaultBuilder<GetActiveWindowBuilder> {
	
	public static final String WINDOW_NAME_FIELD_NAME = "windowName",
			ACCESSIBILITY_ID_FILED_NAME = "accessibilityId";
	
	private String windowName, accessibilityId;
	
	public GetActiveWindowBuilder(UIFrameworkContext context) {
		super(context);
	}

	public GetActiveWindowBuilder windowName(String windowName) {
		this.windowName = windowName;
		return this;
	}

	public GetActiveWindowBuilder accessibilityId(String accessibilityId) {
		this.accessibilityId = accessibilityId;
		return this;
	}

	@Override
	protected GetActiveWindowBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WinGetActiveWindow";
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		if (StringUtils.isBlank(accessibilityId) && StringUtils.isBlank(windowName))
			throw new UIFrameworkBuildingException(
					String.format("Fields %s and %s cannot be empty at the same time for action %s",
							WINDOW_NAME_FIELD_NAME, ACCESSIBILITY_ID_FILED_NAME, getActionName()));
		
		RhWinActionsMessages.WinGetActiveWindow.Builder builder = RhWinActionsMessages.WinGetActiveWindow.newBuilder();
		addIfNotEmpty(id, builder::setId);
		addIfNotEmpty(execute, builder::setExecute);
		addIfNotEmpty(windowName, builder::setWindowName);
		addIfNotEmpty(accessibilityId, builder::setAccessibilityId);
		return RhAction.newBuilder().setWinGetActiveWindow(builder).build();
	}
}
