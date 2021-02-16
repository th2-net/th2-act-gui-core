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

public class WaitBuilder extends WinDefaultBuilder<WaitBuilder> {
	
	public static final String WAITING_TIME_FIELD_NAME = "waitingTime";
	
	private int waitingTime;

	public WaitBuilder(UIFrameworkContext context) {
		super(context);
	}

	public WaitBuilder time(int waitingTime) {
		this.waitingTime = waitingTime;
		return this;
	}

	@Override
	protected WaitBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WinWait";
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(waitingTime, WAITING_TIME_FIELD_NAME);
		RhWinActionsMessages.WinWait.Builder builder = RhWinActionsMessages.WinWait.newBuilder();
		builder.setMillis(waitingTime);
		this.addIfNotEmpty(this.id, builder::setId);
		this.addIfNotEmpty(this.execute, builder::setExecute);
		return RhAction.newBuilder().setWinWait(builder).build();
	}
}
