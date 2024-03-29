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

package com.exactpro.th2.act.framework.builders.win;

import com.exactpro.th2.act.framework.UIWinFrameworkContext;

public class WinBuilderManager {

	private final UIWinFrameworkContext context;

	public WinBuilderManager(UIWinFrameworkContext context) {
		this.context = context;
	}

	public SendTextBuilder sendText() {
		return new SendTextBuilder(context);
	}

	public ClickBuilder click() {
		return new ClickBuilder(context);
	}

	public SearchElementBuilder searchElement() {
		return new SearchElementBuilder(context);
	}

	public WaitBuilder waitAction() {
		return new WaitBuilder(context);
	}

	public WaitForAttributeBuilder waitForAttribute() {
		return new WaitForAttributeBuilder(context);
	}

	public GetActiveWindowBuilder getActiveWindow() {
		return new GetActiveWindowBuilder(context);
	}

	public GetWindowBuilder getWindow() {
		return new GetWindowBuilder(context);
	}

	public OpenBuilder openWindow() {
		return new OpenBuilder(context);
	}

	public GetDataFromClipboardBuilder getDataFromClipboard() {
		return new GetDataFromClipboardBuilder(context);
	}

	public ToggleCheckBoxBuilder toggleCheckbox() {
		return new ToggleCheckBoxBuilder(context);
	}

	public ScrollUsingTextBuilder scrollUsingTextBuilder() {
		return new ScrollUsingTextBuilder(context);
	}

	public GetElementAttributeBuilder getElAttribute() {
		return new GetElementAttributeBuilder(context);
	}

	public TableSearchBuilder tableSearch()
	{
		return new TableSearchBuilder(context);
	}

	public CheckElementBuilder checkElement() {
		return new CheckElementBuilder(context);
	}

	public WaitForElementBuilder waitForElement() {
		return new WaitForElementBuilder(context);
	}

	public MaximizeMainWindowBuilder maximizeMainWindow() {
		return new MaximizeMainWindowBuilder(context);
	}

	public GetScreenshotBuilder getScreenshot() {
		return new GetScreenshotBuilder(context);
	}

	public RestartDriverBuilder restartDriver() {
		return new RestartDriverBuilder(context);
	}

	public GetElementColorBuilder getElementColor() {
		return new GetElementColorBuilder(context);
	}

	public DragAndDropBuilder dragAndDrop() {
		return new DragAndDropBuilder(context);
	}

	public ScrollToElementBuilder scrollToElement() {
		return new ScrollToElementBuilder(context);
	}

	public ColorsCollectorBuilder colorsCollector() {
		return new ColorsCollectorBuilder(context);
	}

	public UIWinFrameworkContext getContext() {
		return context;
	}
}
