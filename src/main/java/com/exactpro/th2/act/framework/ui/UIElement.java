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

package com.exactpro.th2.act.framework.ui;

import com.exactpro.th2.act.framework.builders.BuilderManager;
import com.exactpro.th2.act.framework.builders.WinLocator;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

//todo rename???
public class UIElement {
	protected final BuilderManager builders;
	protected final Map<String, WinLocator> cachedElements;


	public UIElement(BuilderManager builders) {
		this.builders = builders;
		this.cachedElements = new HashMap<>();
	}


	protected WinLocator findAndSaveLocators(WinLocator locator, String actionId) throws UIFrameworkBuildingException {
		return findAndSaveLocators(locator, actionId, true);
	}

	protected WinLocator findAndSaveLocators(WinLocator locator, String actionId, boolean save) throws UIFrameworkBuildingException {
		builders.searchElement().id(actionId).winLocator(locator).build();
		WinLocator winLocator = WinLocator.root().byCachedId(actionId);
		if (save) {
			this.cachedElements.put(actionId, winLocator);
		}
		return winLocator;
	}

	protected WinLocator getLocatorOrSearch(String actionId, Supplier<WinLocator> locator) throws UIFrameworkBuildingException {
		WinLocator winLocator = this.cachedElements.get(actionId);
		if (winLocator == null) {
			winLocator = this.findAndSaveLocators(locator.get(), actionId);
		}
		return winLocator;
	}

	protected void clickContext(String contextButtonName) throws UIFrameworkBuildingException {
		this.builders.waitAction().time(250).build();
		this.builders.clickContext().winLocator(WinLocator.root().byName(contextButtonName)).build();
	}
}
