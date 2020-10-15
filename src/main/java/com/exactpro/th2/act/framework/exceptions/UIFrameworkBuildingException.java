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

package com.exactpro.th2.act.framework.exceptions;

import com.exactpro.th2.act.framework.exceptions.UIFrameworkException;

import java.util.Collection;

public class UIFrameworkBuildingException extends UIFrameworkException {

	public UIFrameworkBuildingException() {
	}

	public UIFrameworkBuildingException(String message) {
		super(message);
	}

	public UIFrameworkBuildingException(String message, Throwable cause) {
		super(message, cause);
	}

	public UIFrameworkBuildingException(Throwable cause) {
		super(cause);
	}

	public UIFrameworkBuildingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public UIFrameworkBuildingException(String actionName, String fieldName) {
		super(String.format("Required field is null: %s for action: %s", fieldName, actionName));
	}

	public UIFrameworkBuildingException(String actionName, Collection<String> fieldName) {
		super(String.format("Required fields is null: [%s] for action: %s",
				String.join(",", fieldName), actionName));
	}
}
