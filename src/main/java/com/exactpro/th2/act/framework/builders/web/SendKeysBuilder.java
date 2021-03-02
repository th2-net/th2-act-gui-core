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

public class SendKeysBuilder extends AbstractWebBuilder<SendKeysBuilder> {
	
	public static final String TEXT_PARAM = "text";
	public static final String TEXT2_PARAM = "text2";

	private String text;
	private Integer wait2;
	private WebLocator locator2;
	private String text2;
	
	private Boolean canBeDisabled;
	private Boolean clear;
	private Boolean checkInput;
	private Boolean needClick;
	

	protected SendKeysBuilder(UIFrameworkContext context) {
		super(context);
	}

	@Override
	protected SendKeysBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "SendKeys";
	}

	public SendKeysBuilder text(String text) {
		this.text = text;
		return this;
	}

	public SendKeysBuilder wait2(Integer wait2) {
		this.wait2 = wait2;
		return this;
	}

	public SendKeysBuilder locator2(WebLocator locator2) {
		this.locator2 = locator2;
		return this;
	}

	public SendKeysBuilder text2(String text2) {
		this.text2 = text2;
		return this;
	}

	public SendKeysBuilder canBeDisabled(Boolean canBeDisabled) {
		this.canBeDisabled = canBeDisabled;
		return this;
	}

	public SendKeysBuilder clear(Boolean clear) {
		this.clear = clear;
		return this;
	}

	public SendKeysBuilder checkInput(Boolean checkInput) {
		this.checkInput = checkInput;
		return this;
	}

	public SendKeysBuilder needClick(Boolean needClick) {
		this.needClick = needClick;
		return this;
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(locator, LOCATOR_PARAM, text, TEXT_PARAM);
		RhActionsMessages.SendKeys.Builder builder = RhActionsMessages.SendKeys.newBuilder();
		addIfNotNull(wait, builder::setWait);
		addIfNotNull(webId, builder::setWebId);
		this.writeLocator(builder::setLocator, builder::setMatcher);
		builder.setText(text);
		if (locator2 != null || wait2 != null || text2 != null) {
			this.checkRequiredFields(locator2, LOCATOR2_PARAM, text2, TEXT2_PARAM);
			addIfNotNull(wait2, builder::setWait2);
			this.writeLocator(locator2, builder::setLocator2, builder::setMatcher2);
			builder.setText2(text2);
		}
		
		addIfNotNull(canBeDisabled, builder::setCanBeDisabled);
		addIfNotNull(clear, builder::setClear);
		addIfNotNull(checkInput, builder::setCheckInput);
		addIfNotNull(needClick, builder::setNeedClick);
		
		return RhAction.newBuilder().setSendKeys(builder).build();
	}
}
