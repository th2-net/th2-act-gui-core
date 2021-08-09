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

import com.exactpro.th2.act.framework.UIWebFrameworkContext;
import com.exactpro.th2.act.framework.builders.AbstractBuilder;
import com.exactpro.th2.act.grpc.hand.rhactions.RhActionsMessages;
import com.exactpro.th2.act.grpc.hand.rhactions.RhActionsMessages.RhWebActions;

import java.util.function.Consumer;

public abstract class AbstractWebBuilder<T extends AbstractWebBuilder<T>> extends AbstractBuilder<T, RhWebActions> {
	
	public static final String LOCATOR_PARAM = "locator";
	public static final String WAIT_PARAM = "wait";
	public static final String LOCATOR2_PARAM = "locator2";
	public static final String WEB_ID_PARAM = "webId";

	protected Integer wait;
	protected WebLocator locator;
	protected String webId;
	
	
	protected void writeLocator(WebLocator locator, Consumer<RhActionsMessages.Locator> locatorConsumer, Consumer<String> matcherConsumer) {
		if (locator != null) {
			locatorConsumer.accept(locator.getLocatorType().getLocator());
			matcherConsumer.accept(locator.getMatcher());
		}
	}

	protected void writeLocator(Consumer<RhActionsMessages.Locator> locatorConsumer, Consumer<String> matcherConsumer) {
		if (this.locator != null) {
			locatorConsumer.accept(this.locator.getLocatorType().getLocator());
			matcherConsumer.accept(this.locator.getMatcher());
		}
	}

	public T webId(String webId) {
		this.webId = webId;
		return getBuilder();
	}

	public T wait(int wait) {
		this.wait = wait;
		return getBuilder();
	}

	public T locator(WebLocator locator) {
		this.locator = locator;
		return getBuilder();
	}

	protected AbstractWebBuilder(UIWebFrameworkContext context) {
		super(context);
	}
	
}
