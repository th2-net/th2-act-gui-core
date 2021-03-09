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
import com.exactpro.th2.act.framework.builders.win.GetScreenshotBuilder;

public class WebBuilderManager {

	private final UIFrameworkContext context;
	
	public WebBuilderManager(UIFrameworkContext context) {
		this.context = context;
	}
	
	public OpenBuilder open() {
		return new OpenBuilder(this.context);
	}
	
	public ClickBuilder click() {
		return new ClickBuilder(this.context);
	}
	
	public SetCheckboxBuilder setCheckbox() {
		return new SetCheckboxBuilder(this.context);
	}
	
	public SendKeysBuilder sendKeys() {
		return new SendKeysBuilder(this.context);
	}
	
	public WaitForElementBuilder waitForElement() {
		return new WaitForElementBuilder(this.context);
	}
	
	public WaitForNewBuilder waitForNew() {
		return new WaitForNewBuilder(this.context);
	}
	
	public WaitBuilder waitAction() {
		return new WaitBuilder(this.context);
	}
	
	public GetElementBuilder getElementBuilder() {
		return new GetElementBuilder(this.context);
	}
	
	public GetElementInnerHtmlBuilder getElementInnerHtml() {
		return new GetElementInnerHtmlBuilder(this.context);
	}
	
	public GetElementValueBuilder getElementValue() {
		return new GetElementValueBuilder(this.context);
	}
	
	public GetElementAttributeBuilder getElementAttribute() {
		return new GetElementAttributeBuilder(this.context);
	}
	
	public GetDynamicTableBuilder getDynamicTable() {
		return new GetDynamicTableBuilder(this.context);
	}
	
	public ScrollToBuilder scrollTo() {
		return new ScrollToBuilder(this.context);
	}
	
	public AbstractScrollDivToBuilder.ScrollDivToBuilder scrollDivTo() {
		return new AbstractScrollDivToBuilder.ScrollDivToBuilder(this.context);
	}
	
	public ScrollDivUntilBuilder scrollDivUntil() {
		return new ScrollDivUntilBuilder(this.context);
	}
	
	public PageSourceBuilder pageSource() {
		return new PageSourceBuilder(this.context);
	}
	
	public RefreshBuilder refresh() {
		return new RefreshBuilder(this.context);
	}
	
	public SelectBuilder select() {
		return new SelectBuilder(this.context);
	}
	
	public ClearElementBuilder clearElement() {
		return new ClearElementBuilder(this.context);
	}
	
	public OutputBuilder output() {
		return new OutputBuilder(this.context);
	}
	
	public FindElementBuilder findElement() {
		return new FindElementBuilder(this.context);
	}
	
	public KeyActionBuilder keyAction() {
		return new KeyActionBuilder(this.context);
	}
	
	public SendKeysToActiveBuilder sendKeysToActive() {
		return new SendKeysToActiveBuilder(this.context);
	}
	
	public SwitchWindowBuilder switchWindow() {
		return new SwitchWindowBuilder(this.context);
	}

	public AcceptAlertBuilder acceptAlert() {
		return new AcceptAlertBuilder(this.context);
	}

	public DismissAlertBuilder dismissAlert() {
		return new DismissAlertBuilder(this.context);
	}
	
	public CheckImageAvailabilityBuilder checkImageAvailability() {
		return new CheckImageAvailabilityBuilder(this.context);
	}
	
	public GetScreenshotBuilder getScreenshot() {
		return new GetScreenshotBuilder(this.context);
	}

	public GetElementScreenshotBuilder getElementScreenshot() {
		return new GetElementScreenshotBuilder(this.context);
	}

	public ExecuteJSBuilder executeJS() {
		return new ExecuteJSBuilder(this.context);
	}

	public ExecuteJSElementBuilder executeJSElement() {
		return new ExecuteJSElementBuilder(this.context);
	}
	
	public UIFrameworkContext getContext() {
		return context;
	}
}
