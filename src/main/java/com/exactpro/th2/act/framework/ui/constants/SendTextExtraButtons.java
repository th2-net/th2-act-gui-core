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

package com.exactpro.th2.act.framework.ui.constants;

public enum SendTextExtraButtons {
	
	CONTROL		("ctrl"),
	ALT			("alt"),
	SHIFT		("shift"),
	WINDOWS		("windows"),
	COMMAND		("command"),
	
	UP			("up"),
	DOWN		("down"),
	LEFT		("left"),
	RIGHT		("right"),
	RETURN		("return"),
	ENTER		("enter"),
	SPACE		("space"),
	TAB			("tab"),
	ESC			("esc"),
	END			("end"),
	HOME		("home"),
	INSERT		("insert"),
	DELETE		("delete"),
	BACKSPACE	("backspace"),
	PAGE_UP		("pageup"),
	PAGE_DOWN	("pagedown"),
	
	F1			("f1"),
	F2			("f2"),
	F3			("f3"),
	F4			("f4"),
	F5			("f5"),
	F6			("f6"),
	F7			("f7"),
	F8			("f8"),
	F9			("f9"),
	F10			("f10"),
	F11			("f11"),
	F12			("f12"),

	NUM_0		("num0"),
	NUM_1		("num1"),
	NUM_2		("num2"),
	NUM_3		("num3"),
	NUM_4		("num4"),
	NUM_5		("num5"),
	NUM_6		("num6"),
	NUM_7		("num7"),
	NUM_8		("num8"),
	NUM_9		("num9"),

	ADD			("add"),
	SUBTRACT	("subtract"),
	
	NON_BREAKING_SPACE	("nbsp");
	
	private final String value;

	SendTextExtraButtons(String value) {
		this.value = value;
	}
	
	public String handCommand() {
		return "#" + this.value + "#";
	}

	public String rawCommand() {
		return this.value;
	}

}
