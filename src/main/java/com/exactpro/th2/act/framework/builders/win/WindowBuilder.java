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

import com.exactpro.th2.act.framework.UIWinFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import org.apache.commons.lang3.StringUtils;

public abstract class WindowBuilder<T extends WindowBuilder<T>> extends AbstractWinBuilder<T>
{
	public static final String WINDOW_NAME_FIELD_NAME = "windowName",
			ACCESSIBILITY_ID_FILED_NAME = "accessibilityId";

	protected String windowName, accessibilityId;

	public WindowBuilder(UIWinFrameworkContext context)
	{
		super(context);
	}

	public T windowName(String windowName) {
		this.windowName = windowName;
		return getBuilder();
	}

	public T accessibilityId(String accessibilityId) {
		this.accessibilityId = accessibilityId;
		return getBuilder();
	}

	public void checkWindowIds() throws UIFrameworkBuildingException
	{
		if (StringUtils.isBlank(accessibilityId) && StringUtils.isBlank(windowName))
			throw new UIFrameworkBuildingException(
					String.format("Fields %s and %s cannot be empty at the same time for action %s",
							WINDOW_NAME_FIELD_NAME, ACCESSIBILITY_ID_FILED_NAME, getActionName()));
	}
}
