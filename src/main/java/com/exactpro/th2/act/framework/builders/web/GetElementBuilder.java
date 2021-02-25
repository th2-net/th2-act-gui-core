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

public class GetElementBuilder extends AbstractWebBuilder<GetElementBuilder> {
	
	public static final String ID_PARAM = "id";
	
	protected String id;

	protected GetElementBuilder(UIFrameworkContext context) {
		super(context);
	}

	@Override
	protected GetElementBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "GetElement";
	}

	public GetElementBuilder id(String id) {
		this.id = id;
		return this;
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(locator, LOCATOR_PARAM);
		RhActionsMessages.GetElement.Builder builder = RhActionsMessages.GetElement.newBuilder();
		addIfNotNull(wait, builder::setWait);
		addIfNotNull(webId, builder::setWebId);
		this.writeLocator(builder::setLocator, builder::setMatcher);
		addIfNotNull(id, builder::setId);
		return RhAction.newBuilder().setGetElement(builder).build();
	}
}
