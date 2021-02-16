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

import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.RhAction;
import com.exactpro.th2.act.grpc.hand.rhactions.RhActionsMessages;

public class KeyActionBuilder extends WebDefaultBuilder<KeyActionBuilder> {

	public static final String KEY_PARAM = "key";
	private KeyActionType keyAction;
	private String key;

	protected KeyActionBuilder(UIFrameworkContext context) {
		super(context);
	}

	@Override
	protected KeyActionBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "KeyAction";
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(key, KEY_PARAM);
		RhActionsMessages.KeyAction.Builder builder = RhActionsMessages.KeyAction.newBuilder();
		if (keyAction != null) {
			builder.setKeyAction(keyAction.actionType);
		}
		builder.setKey(key);
		return RhAction.newBuilder().setKeyAction(builder).build();
	}
	
	public enum KeyActionType {
		PRESS(RhActionsMessages.KeyAction.ActionType.PRESS),
		UP(RhActionsMessages.KeyAction.ActionType.UP),
		DOWN(RhActionsMessages.KeyAction.ActionType.DOWN);
		
		private final RhActionsMessages.KeyAction.ActionType actionType;
		KeyActionType(RhActionsMessages.KeyAction.ActionType actionType) {
			this.actionType = actionType;
		}
	}
	
}
