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

package com.exactpro.th2.act;

import com.exactpro.th2.configuration.MicroserviceConfiguration;
import com.exactpro.th2.configuration.Th2Configuration;

import java.io.IOException;
import java.io.InputStream;

public class Configuration extends MicroserviceConfiguration {

	public static Configuration load(InputStream inputStream) throws IOException {
		return YAML_READER.readValue(inputStream, Configuration.class);
	}

	private Th2Configuration th2 = new Th2Configuration();

	public Th2Configuration getTh2() {
		return th2;
	}

	public void setTh2(Th2Configuration th2) {
		this.th2 = th2;
	}
}
