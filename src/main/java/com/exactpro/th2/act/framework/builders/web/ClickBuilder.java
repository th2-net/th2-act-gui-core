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

public class ClickBuilder extends AbstractWebBuilder<ClickBuilder> {

	private ClickButton button;
	private Integer xOffset;
	private Integer yOffset;
	private ClickModifiers[] modifiers;
	
	protected ClickBuilder(UIWebFrameworkContext context) {
		super(context);
	}

	@Override
	protected ClickBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "Click";
	}
	
	public ClickBuilder button(ClickButton button) {
		this.button = button;
		return this;
	}

	public ClickBuilder offsets(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		return this;
	}

	public ClickBuilder modifiers(ClickModifiers... modifiers) {
		this.modifiers = modifiers;
		return this;
	}

	@Override
	protected RhWebActions buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(this.locator, LOCATOR_PARAM);
		RhActionsMessages.Click.Builder builder = RhActionsMessages.Click.newBuilder();
		addIfNotNull(wait, builder::setWait);
		addIfNotNull(webId, builder::setWebId);
		this.writeLocator(builder::setLocator, builder::setMatcher);
		
		if (this.button != null) {
			builder.setButton(this.button.value);
		}
		if (this.modifiers != null) {
			RhActionsMessages.Click.ModifiersList.Builder list = RhActionsMessages.Click.ModifiersList.newBuilder();
			for (ClickModifiers modifier : this.modifiers) {
				list.addModifier(modifier.value);
			}
			builder.setModifiers(list);
		}
		if (xOffset != null && yOffset != null) {
			builder.setXOffset(xOffset);
			builder.setYOffset(yOffset);
		}
		return RhWebActions.newBuilder().setClick(builder).build();
	}
	
	public enum ClickButton {
		LEFT(RhActionsMessages.Click.Button.LEFT),
		RIGHT(RhActionsMessages.Click.Button.RIGHT),
		MIDDLE(RhActionsMessages.Click.Button.MIDDLE),
		DOUBLE(RhActionsMessages.Click.Button.DOUBLE);
		
		private final RhActionsMessages.Click.Button value;

		ClickButton(RhActionsMessages.Click.Button value) {
			this.value = value;
		}
	}

	public enum ClickModifiers {
		CTRL(RhActionsMessages.Click.ModifiersList.Modifier.CTRL),
		SHIFT(RhActionsMessages.Click.ModifiersList.Modifier.SHIFT),
		ALT(RhActionsMessages.Click.ModifiersList.Modifier.ALT);

		private final RhActionsMessages.Click.ModifiersList.Modifier value;

		ClickModifiers(RhActionsMessages.Click.ModifiersList.Modifier value) {
			this.value = value;
		}
	}
}
