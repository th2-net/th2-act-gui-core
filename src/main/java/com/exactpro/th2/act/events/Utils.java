/*
 * Copyright 2024 Exactpro (Exactpro Systems Limited)
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

package com.exactpro.th2.act.events;

import java.util.concurrent.atomic.AtomicLong;

import static com.exactpro.th2.common.event.EventUtils.generateUUID;

public class Utils {
    private static final AtomicLong ID_COUNTER = new AtomicLong();
    private static final String UUID = generateUUID();

    public static String generateId() {
        return UUID + '-' + ID_COUNTER.incrementAndGet();
    }
}
