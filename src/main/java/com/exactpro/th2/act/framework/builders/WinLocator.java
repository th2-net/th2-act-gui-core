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

public class WinLocator {
		
	private final WinLocator parent;
	private final WinLocatorType locatorType;
	private final String matcher;

	private WinLocator(WinLocator parent, WinLocatorType locatorType, String matcher) {
		this.parent = parent;
		this.locatorType = locatorType;
		this.matcher = matcher;
	}

	protected WinLocator byLocator(WinLocatorType locator, String matcher) {
		return new WinLocator(this, locator, matcher);
	}

	public WinLocator byName(String name) {
		return this.byLocator(WinLocatorType.NAME, name);
	}

	public WinLocator byId(String id) {
		return this.byLocator(WinLocatorType.ACCESSIBILITY_ID, id);
	}

	public WinLocator byCachedId(String cachedId) {
		//TODO probably parent = ROOT not this...
		return this.byLocator(WinLocatorType.CACHED_ELEMENT, cachedId);
	}
	
	public boolean isRoot() {
		return false;
	}

	public WinLocator getParent() {
		return parent;
	}

	public WinLocatorType getLocatorType() {
		return locatorType;
	}

	public String getMatcher() {
		return matcher;
	}
	
	public static WinLocator root() {
		return RootLocator.INSTANCE;
	}

	public enum WinLocatorType {
		
		CACHED_ELEMENT("cachedId"),
		ACCESSIBILITY_ID("accessibilityId"),
		NAME("name");
		
		private final String name;

		WinLocatorType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
	
	private static class RootLocator extends WinLocator {

		private static final RootLocator INSTANCE = new RootLocator();
		
		protected RootLocator() {
			super(null, null, null);
		}

		@Override
		public boolean isRoot() {
			return true;
		}
	}
}
