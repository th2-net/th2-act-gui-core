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

public class ScrollDivUntilBuilder extends AbstractScrollDivToBuilder<ScrollDivUntilBuilder> {

	private Direction direction;
	private Integer searchOffset;
	private Boolean doScrollTo;
	
	
	protected ScrollDivUntilBuilder(UIWebFrameworkContext context) {
		super(context);
	}

	@Override
	protected ScrollDivUntilBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "ScrollDivUntil";
	}

	public ScrollDivUntilBuilder direction(Direction direction) {
		this.direction = direction;
		return this;
	}

	public ScrollDivUntilBuilder searchOffset(Integer searchOffset) {
		this.searchOffset = searchOffset;
		return this;
	}

	public ScrollDivUntilBuilder doScrollTo(Boolean doScrollTo) {
		this.doScrollTo = doScrollTo;
		return this;
	}

	@Override
	protected RhWebActions buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(locator, LOCATOR_PARAM, locator2, LOCATOR2_PARAM);
		RhActionsMessages.ScrollDivUntil.Builder builder = RhActionsMessages.ScrollDivUntil.newBuilder();
		addIfNotNull(wait, builder::setWait);
		addIfNotNull(webId, builder::setWebId);
		this.writeLocator(builder::setLocator, builder::setMatcher);
		addIfNotNull(wait2, builder::setWait2);
		this.writeLocator(this.locator2, builder::setLocator2, builder::setMatcher2);
		addIfNotNull(yOffset, builder::setYOffset);
		if (direction != null) {
			builder.setSearchDir(direction.direction);
		}
		addIfNotNull(searchOffset, builder::setSearchOffset);
		addIfNotNull(doScrollTo, builder::setDoScrollTo);
		return RhWebActions.newBuilder().setScrollDivUntil(builder).build();
	}
	
	public enum Direction {
		BOTH(RhActionsMessages.ScrollDivUntil.Direction.BOTH),
		UP(RhActionsMessages.ScrollDivUntil.Direction.UP),
		DOWN(RhActionsMessages.ScrollDivUntil.Direction.DOWN);

		private final RhActionsMessages.ScrollDivUntil.Direction direction;
		
		Direction(RhActionsMessages.ScrollDivUntil.Direction direction) {
			this.direction = direction;
		}
	}
}
