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

import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.RhAction;
import com.exactpro.th2.act.grpc.hand.rhactions.RhWinActionsMessages;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ClickBuilder extends AbstractWinBuilder<ClickBuilder> {
	private String xOffset;
	private String yOffset;

	private MouseClickButton button;
	private ClickBorder border;

	public ClickBuilder(UIFrameworkContext context) {
		super(context);
	}
	
	@Override
	protected ClickBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WinClick";
	}

	@Deprecated(since = "2.2.1", forRemoval = true)
	public ClickBuilder offset(int x, int y) {
		this.xOffset = String.valueOf(x);
		this.yOffset = String.valueOf(y);
		return getBuilder();
	}

	public ClickBuilder offset(String x, String y) {
		this.xOffset = x;
		this.yOffset = y;
		return getBuilder();
	}

	public ClickBuilder button(MouseClickButton button) {
		this.button = button;
		return getBuilder();
	}

	@Deprecated(since = "2.2.1", forRemoval = true)
	public ClickBuilder border(ClickBorder border) {
		this.border = border;
		return getBuilder();
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		this.checkRequiredFields(this.winLocator, WIN_LOCATOR_FIELD_NAME);
		RhWinActionsMessages.WinClick.Builder clickBuilder = RhWinActionsMessages.WinClick.newBuilder();
		clickBuilder.addAllLocators(buildWinLocator(this.winLocator));
		clickBuilder.setBaseParams(buildBaseParam());

		if (button != null) {
			clickBuilder.setButton(button.grpcButton);
		}

		if (!StringUtils.isEmpty(xOffset) && !StringUtils.isEmpty(yOffset)) {
			if (border != null) {
				Pair<String, String> coordinates = convertCoordinates(border, xOffset, yOffset);
				clickBuilder.setXOffset(coordinates.getLeft()).setYOffset(coordinates.getRight());
			} else {
				clickBuilder.setXOffset(xOffset).setYOffset(yOffset);
			} 
		}

		return RhAction.newBuilder().setWinClick(clickBuilder.build()).build();
	}

	public enum MouseClickButton {

		LEFT(RhWinActionsMessages.WinClick.Button.LEFT),
		RIGHT(RhWinActionsMessages.WinClick.Button.RIGHT),
		MIDDLE(RhWinActionsMessages.WinClick.Button.MIDDLE),
		DOUBLE(RhWinActionsMessages.WinClick.Button.DOUBLE);

		private final RhWinActionsMessages.WinClick.Button grpcButton;

		MouseClickButton(RhWinActionsMessages.WinClick.Button grpcButton) {
			this.grpcButton = grpcButton;
		}
	}


	@Deprecated(since = "2.2.1", forRemoval = true)
	private Pair<String, String> convertCoordinates(ClickBorder border, String xOffset, String yOffset) throws UIFrameworkBuildingException {
		switch (border) {
			case LEFT_TOP: return new ImmutablePair<>(xOffset, yOffset);
			case LEFT_BOTTOM: return new ImmutablePair<>(xOffset, "height + " + yOffset);
			case RIGHT_TOP: return new ImmutablePair<>("width + " + xOffset, yOffset);
			case RIGHT_BOTTOM: return new ImmutablePair<>("width + " + xOffset, "height + " + yOffset);
			default: throw new UIFrameworkBuildingException("Unsupported border type");
		}
	}


	@Deprecated(since = "2.2.1", forRemoval = true)
	public enum ClickBorder {

		LEFT_TOP(RhWinActionsMessages.WinClick.AttachedBorder.LEFT_TOP),
		LEFT_BOTTOM(RhWinActionsMessages.WinClick.AttachedBorder.LEFT_BOTTOM),
		RIGHT_TOP(RhWinActionsMessages.WinClick.AttachedBorder.RIGHT_TOP),
		RIGHT_BOTTOM(RhWinActionsMessages.WinClick.AttachedBorder.RIGHT_BOTTOM);

		private final RhWinActionsMessages.WinClick.AttachedBorder grpcBorder;

		ClickBorder(RhWinActionsMessages.WinClick.AttachedBorder grpcBorder) {
			this.grpcBorder = grpcBorder;
		}
	}
}
