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
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.rhactions.RhActionsMessages;
import com.exactpro.th2.act.grpc.hand.rhactions.RhActionsMessages.RhWebActions;

public class ScrollToBuilder extends AbstractWebBuilder<ScrollToBuilder> {
	
	protected ScrollToBuilder(UIWebFrameworkContext context) {
		super(context);
	}

	@Override
	protected ScrollToBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "ScrollTo";
	}

	@Override
	protected RhWebActions buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(locator, LOCATOR_PARAM);
		RhActionsMessages.ScrollTo.Builder builder = RhActionsMessages.ScrollTo.newBuilder();
		addIfNotNull(wait, builder::setWait);
		addIfNotNull(webId, builder::setWebId);
		this.writeLocator(builder::setLocator, builder::setMatcher);
		return RhWebActions.newBuilder().setScrollTo(builder).build();
	}
}
