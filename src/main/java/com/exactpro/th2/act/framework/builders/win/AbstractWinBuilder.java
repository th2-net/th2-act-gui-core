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

import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.builders.AbstractBuilder;
import com.exactpro.th2.act.grpc.hand.rhactions.RhWinActionsMessages;
import com.google.protobuf.Int32Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractWinBuilder<T extends AbstractWinBuilder<T>> extends AbstractBuilder<T> {
	
	public static final String WIN_LOCATOR_FIELD_NAME = "winLocator";
	
	protected String id;
	protected String execute;
	protected WinLocator winLocator;
	protected boolean fromRoot;
	protected boolean experimental;

	public AbstractWinBuilder(UIFrameworkContext context) {
		super(context);
	}
	
	public T id(String id) {
		this.id = id;
		return getBuilder();
	}

	public T execute(String execute) {
		this.execute = execute;
		return getBuilder();
	}

	public T winLocator(WinLocator winLocator) {
		this.winLocator = winLocator;
		return getBuilder();
	}

	public T fromRoot(boolean fromRoot) {
		this.fromRoot = fromRoot;
		return getBuilder();
	}

	public T experimental(boolean experimental) {
		this.experimental = experimental;
		return getBuilder();
	}

	protected List<RhWinActionsMessages.WinLocator> buildWinLocator(WinLocator winLocator) {
		if (winLocator.isRoot())
			return buildRootLocator(winLocator);

		List<RhWinActionsMessages.WinLocator> builtLocators = new ArrayList<>();
		WinLocator currentLocator = winLocator;

		do {
			RhWinActionsMessages.WinLocator.Builder builder = RhWinActionsMessages.WinLocator.newBuilder();
			builder.setLocator(currentLocator.getLocatorType().getName());
			builder.setMatcher(currentLocator.getMatcher());
			if (currentLocator.getMatcherIndex() != null)
				builder.setMatcherIndex(Int32Value.of(currentLocator.getMatcherIndex()));
			builtLocators.add(builder.build());
			currentLocator = currentLocator.getParent();

		} while (currentLocator != null && !currentLocator.isRoot());

		Collections.reverse(builtLocators);
		return builtLocators;
	}


	private List<RhWinActionsMessages.WinLocator> buildRootLocator(WinLocator locator) {
		RhWinActionsMessages.WinLocator message = RhWinActionsMessages.WinLocator.newBuilder()
				.setLocator(locator.getLocatorType().getName())
				.build();
		return Collections.singletonList(message);
	}
	protected RhWinActionsMessages.BaseWinParams buildBaseParam() {
		RhWinActionsMessages.BaseWinParams.Builder params = RhWinActionsMessages.BaseWinParams.newBuilder();
		addIfNotEmpty(id, params::setId);
		addIfNotEmpty(execute, params::setExecute);
		params.setFromRoot(fromRoot);
		params.setExperimentalDriver(experimental);

		return params.build();
	}
}
