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

import java.util.Collection;

public class ExecuteJSBuilder extends AbstractWebBuilder<ExecuteJSBuilder> {
	
	public static final String COMMANDS_PRAMS = "commands";
	
	protected String commands;

	protected ExecuteJSBuilder(UIFrameworkContext context) {
		super(context);
	}

	public ExecuteJSBuilder command(String command) {
		this.commands = command;
		return this;
	}

	public ExecuteJSBuilder commands(String... command) {
		this.commands = String.join(";", command);
		return this;
	}

	public ExecuteJSBuilder commands(Collection<String> command) {
		this.commands = String.join(";", command);
		return this;
	}

	@Override
	protected ExecuteJSBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "ExecuteJS";
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(this.commands, COMMANDS_PRAMS);
		var builder = RhActionsMessages.ExecuteJS.newBuilder();
		builder.setCommands(this.commands);
		return RhAction.newBuilder().setExecuteJs(builder).build();
	}
}
