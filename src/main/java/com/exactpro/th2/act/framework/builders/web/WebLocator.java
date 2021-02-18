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

import com.exactpro.th2.act.grpc.hand.rhactions.RhActionsMessages;

public class WebLocator {
	
	private final LocatorType locatorType;
	private final String matcher;

	private WebLocator(LocatorType locatorType, String matcher) {
		this.locatorType = locatorType;
		this.matcher = matcher;
	}
	
	public static WebLocator byCssSelector(String value) {
		return new WebLocator(LocatorType.CSS_SELECTOR, value);
	}

	public static WebLocator byTagName(String value) {
		return new WebLocator(LocatorType.TAG_NAME, value);
	}

	public static WebLocator byId(String value) {
		return new WebLocator(LocatorType.ID, value);
	}

	public static WebLocator byXPath(String value) {
		return new WebLocator(LocatorType.XPATH, value);
	}

	public LocatorType getLocatorType() {
		return locatorType;
	}

	public String getMatcher() {
		return matcher;
	}

	public enum LocatorType {
		NOT_SET(RhActionsMessages.Locator.NOT_SET),
		CSS_SELECTOR(RhActionsMessages.Locator.CSS_SELECTOR),
		TAG_NAME(RhActionsMessages.Locator.TAG_NAME),
		ID(RhActionsMessages.Locator.ID),
		XPATH(RhActionsMessages.Locator.XPATH);
		
		private final RhActionsMessages.Locator locator;

		LocatorType(RhActionsMessages.Locator locator) {
			this.locator = locator;
		}

		public RhActionsMessages.Locator getLocator() {
			return locator;
		}
	}
}
