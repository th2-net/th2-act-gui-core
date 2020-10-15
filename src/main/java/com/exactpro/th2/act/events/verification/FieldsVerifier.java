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

package com.exactpro.th2.act.events.verification;

import com.exactpro.th2.act.events.EventPayloadVerification;
import com.exactpro.th2.act.events.PayloadVerificationField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FieldsVerifier {

	public static final String PRESENCE_PREFIX = "Presence of '%s'", FOUND_VALUE = "FOUND", NOT_FOUND_VALUE = "NOT FOUND";
	public static final String NO_VALUE = "[NO VALUE]";

	private static final Logger logger = LoggerFactory.getLogger(FieldsVerifier.class);
	
	private final List<VerificationDetail> details;
	private boolean success;

	public FieldsVerifier(List<VerificationDetail> details) {
		this.details = details;
	}

	public EventPayloadVerification transform() {
		logger.debug("Fields verification started");
		EventPayloadVerification payloadVerification = new EventPayloadVerification();

		this.success = true;
		for (VerificationDetail detail : details) {
			PayloadVerificationField verification = new PayloadVerificationField(detail.getExpected(),
					detail.getActual(), detail.isEquals());
			payloadVerification.getFields().put(detail.getParamName(), verification);
			if (!detail.isEquals()) {
				success = false;
			}
		}
		
		return payloadVerification;
	}

	public boolean isSuccess() {
		return success;
	}
}
