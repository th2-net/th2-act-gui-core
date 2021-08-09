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

public abstract class AbstractScrollDivToBuilder<T extends AbstractScrollDivToBuilder<T>> extends AbstractWebBuilder<T> {
	
	protected Integer wait2;
	protected WebLocator locator2;
	protected Integer yOffset;
	
	protected AbstractScrollDivToBuilder(UIWebFrameworkContext context) {
		super(context);
	}

	public T wait2(Integer wait2) {
		this.wait2 = wait2;
		return getBuilder();
	}

	public T locator2(WebLocator locator2) {
		this.locator2 = locator2;
		return getBuilder();
	}

	public T yOffset(Integer yOffset) {
		this.yOffset = yOffset;
		return getBuilder();
	}
	
	
	public static class ScrollDivToBuilder extends AbstractScrollDivToBuilder<ScrollDivToBuilder> {

		protected ScrollDivToBuilder(UIWebFrameworkContext context) {
			super(context);
		}

		@Override
		protected ScrollDivToBuilder getBuilder() {
			return this;
		}

		@Override
		protected String getActionName() {
			return "ScrollDivTo";
		}
		
		@Override
		protected RhWebActions buildAction() throws UIFrameworkBuildingException {
			this.checkRequiredFields(locator, LOCATOR_PARAM, locator2, LOCATOR2_PARAM);
			RhActionsMessages.ScrollDivTo.Builder builder = RhActionsMessages.ScrollDivTo.newBuilder();
			addIfNotNull(wait, builder::setWait);
			addIfNotNull(webId, builder::setWebId);
			this.writeLocator(builder::setLocator, builder::setMatcher);
			addIfNotNull(wait2, builder::setWait2);
			this.writeLocator(this.locator2, builder::setLocator2, builder::setMatcher2);
			addIfNotNull(yOffset, builder::setYOffset);
			return RhWebActions.newBuilder().setScrollDivTo(builder).build();
		}
	}

	
}
