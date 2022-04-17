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

package com.exactpro.th2.act.actions;

import com.exactpro.th2.act.ActResult;
import com.exactpro.th2.act.framework.UIFramework;
import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.UIFrameworkSessionContext;


public abstract class ActAction<T, K extends UIFrameworkContext<?>, L extends UIFrameworkSessionContext<K>> extends Action<T, K, L, ActResult> {
	public ActAction(UIFramework<K, L> framework) {
		super(framework);
	}

	@Override
	protected ActResult createActResult() {
		return new ActResult();
	}
}
