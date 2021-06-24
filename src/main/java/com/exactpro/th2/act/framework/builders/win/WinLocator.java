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

public class WinLocator {
		
	private final WinLocator parent;
	private final WinLocatorType locatorType;
	private final String matcher;
	private Integer matcherIndex;

	private WinLocator(WinLocator parent, WinLocatorType locatorType, String matcher) {
		this.parent = parent;
		this.locatorType = locatorType;
		this.matcher = matcher;
	}

	private WinLocator(WinLocator parent, WinLocatorType locatorType, String matcher, Integer matcherIndex) {
		this(parent, locatorType, matcher);
		this.matcherIndex = matcherIndex;
	}

	protected WinLocator byLocator(WinLocatorType locator, String matcher, Integer index) {
		return new WinLocator(this, locator, matcher, index);
	}

	protected WinLocator byLocator(WinLocatorType locator, String matcher) {
		return new WinLocator(this, locator, matcher);
	}

	public WinLocator byName(String name) {
		return byName(name, null);
	}

	public WinLocator byName(String name, Integer index) {
		return this.byLocator(WinLocatorType.NAME, name, index);
	}

	public WinLocator byId(String id) {
		return byId(id, null);
	}

	public WinLocator byCachedId(String id) {
		return byLocator(WinLocatorType.CACHED_ELEMENT, id);
	}

	public WinLocator byCachedId(String id, Integer index) {
		return byLocator(WinLocatorType.CACHED_ELEMENT, id, index);
	}

	public WinLocator byId(String id, Integer index) {
		return this.byLocator(WinLocatorType.ACCESSIBILITY_ID, id, index);
	}

	public WinLocator byXpath(String xpath) {
		return this.byLocator(WinLocatorType.XPATH, xpath);
	}

	public static WinLocator fromCachedId(String cachedId) {
		return root().byLocator(WinLocatorType.CACHED_ELEMENT, cachedId);
	}

	public static WinLocator fromCachedId(String cachedId, Integer index) {
		return root().byLocator(WinLocatorType.CACHED_ELEMENT, cachedId, index);
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

	public Integer getMatcherIndex() {
		return matcherIndex;
	}

	public boolean isCached() {
		return this.locatorType == WinLocatorType.CACHED_ELEMENT;
	}
	
	public static WinLocator root() {
		return RootLocator.INSTANCE;
	}

	public enum WinLocatorType {
		CACHED_ELEMENT("cachedId"),
		ACCESSIBILITY_ID("accessibilityId"),
		NAME("name"),
		XPATH("xpath"),
		ROOT("root");

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
			super(null, WinLocatorType.ROOT, null);
		}

		@Override
		public boolean isRoot() {
			return true;
		}
	}
}
