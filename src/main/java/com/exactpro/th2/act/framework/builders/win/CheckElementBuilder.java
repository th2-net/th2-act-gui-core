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
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.RhAction;
import com.exactpro.th2.act.grpc.hand.rhactions.RhWinActionsMessages;

public class CheckElementBuilder extends AbstractWinBuilder<CheckElementBuilder> {
	
	private boolean saveElement = false;
	
	public CheckElementBuilder(UIFrameworkContext context) {
		super(context);
	}

	@Override
	protected CheckElementBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WinCheckElement";
	}

	public CheckElementBuilder saveElement(boolean saveElement) {
		this.saveElement = saveElement;
		return this;
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		RhWinActionsMessages.WinCheckElement.Builder builder = RhWinActionsMessages.WinCheckElement.newBuilder();
		builder.addAllLocators(buildWinLocator(this.winLocator));
		builder.setBaseParams(buildBaseParam());
		builder.setSaveElement(saveElement);
		return RhAction.newBuilder().setWinCheckElement(builder.build()).build();
	}
}
