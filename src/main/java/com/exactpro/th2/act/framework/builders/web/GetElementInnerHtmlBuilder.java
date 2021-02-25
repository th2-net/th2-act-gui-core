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

public class GetElementInnerHtmlBuilder extends GetElementBuilder {
	
	protected GetElementInnerHtmlBuilder(UIFrameworkContext context) {
		super(context);
	}

	@Override
	protected String getActionName() {
		return "GetElementInnerHtml";
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(locator, LOCATOR_PARAM);
		RhActionsMessages.GetElementInnerHtml.Builder builder = RhActionsMessages.GetElementInnerHtml.newBuilder();
		addIfNotNull(wait, builder::setWait);
		addIfNotNull(webId, builder::setWebId);
		this.writeLocator(builder::setLocator, builder::setMatcher);
		builder.setId(id);
		return RhAction.newBuilder().setGetElementInnerHtml(builder).build();
	}
}
