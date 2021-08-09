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

public class OpenBuilder extends AbstractWebBuilder<OpenBuilder> {
	
	public static final String OPEN_URL_FIELD_NAME = "url"; 

	private String url;

	protected OpenBuilder(UIWebFrameworkContext context) {
		super(context);
	}

	@Override
	protected OpenBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "Open";
	}

	public OpenBuilder url(String url) {
		this.url = url;
		return this;
	}

	@Override
	protected RhWebActions buildAction() throws UIFrameworkBuildingException {
		checkRequiredFields(this.url, OPEN_URL_FIELD_NAME);
		RhActionsMessages.Open.Builder builder = RhActionsMessages.Open.newBuilder();
		builder.setUrl(this.url);
		return RhWebActions.newBuilder().setOpen(builder).build();
	}
}
