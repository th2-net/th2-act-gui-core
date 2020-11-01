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

package com.exactpro.th2.act.framework;

import java.util.LinkedHashMap;
import java.util.Map;

public class ContextInfoData {
	
	private Map<String, String> data;

	public ContextInfoData(Map<String, String> data) {
		this.data = data;
	}

	public ContextInfoData() {
		this.data = null;
	}
	
	public void merge(Map<String, String> data) {
		if (data == null) {
			return;
		}
		if (this.data == null) {
			this.data = new LinkedHashMap<>();
		}
		this.data.putAll(data);
	}

	public Map<String, String> getData() {
		return data;
	}
}
