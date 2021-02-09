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

package com.exactpro.th2.act.framework.ui.utils;


import com.exactpro.th2.act.framework.ui.constants.SendTextExtraButtons;

public class UIUtils {

	public static String keyCombo(String... button) {
		StringBuilder sb = new StringBuilder();
		sb.append('#');
		boolean first = true;
		for (String s : button) {
			if (!first)
				sb.append('+');
			sb.append(s);
			first = false;
		}
		sb.append('#');
		return sb.toString();
	}
	
	public static String keyCombo(SendTextExtraButtons mod, String button) {
		return keyCombo(mod.rawCommand(), button);
	}

	public static String keyCombo(SendTextExtraButtons mod, SendTextExtraButtons button) {
		return keyCombo(mod.rawCommand(), button.rawCommand());
	}

	public static String keyCombo(SendTextExtraButtons mod, SendTextExtraButtons mod2, String button) {
		return keyCombo(mod.rawCommand(), mod2.rawCommand(), button);
	}

	public static String keyCombo(SendTextExtraButtons mod, SendTextExtraButtons mod2, SendTextExtraButtons button) {
		return keyCombo(mod.rawCommand(), mod2.rawCommand(), button.rawCommand());
	}	
}
