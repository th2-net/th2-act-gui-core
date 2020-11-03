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

import com.exactpro.th2.act.configuration.CustomConfiguration;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkException;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SessionWatcher extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(SessionWatcher.class);
	
	public static final Long DEFAULT_EXPIRATION_TIME = 30L; 

	private final UIFramework framework;
	private final Map<RhSessionID, Long> sessions;

	private final long sessionExpirationMs;
	private final AtomicBoolean run;
	
	public static SessionWatcher create(UIFramework framework, CustomConfiguration customConfiguration) {
		long timeout;
		if (customConfiguration != null && customConfiguration.getSessionExpirationTime() != null) {
			timeout = customConfiguration.getSessionExpirationTime();
		} else {
			timeout = DEFAULT_EXPIRATION_TIME;
		}		
		return new SessionWatcher(framework, timeout);
	}

	public SessionWatcher(UIFramework framework, long sessionTimeoutMin) {
		super("SESSION-WATCHER");
		this.framework = framework;
		this.sessionExpirationMs = TimeUnit.MINUTES.toMillis(sessionTimeoutMin);
		this.sessions = new ConcurrentHashMap<>();
		this.run = new AtomicBoolean(false);
	}

	@Override
	public void run() {
		this.run.set(true);
		while (this.run.get()) {
			try {
				long sleepingTime = closeSessionIfTimeOver();
				logger.trace("Sleeping {} ms", sleepingTime);
				Thread.sleep(sleepingTime);
			} catch (InterruptedException e) {
				this.run.set(false);
			}
		}
		
		logger.info("Session watcher was stopped");
	}

	public void updateSessionTime(RhSessionID session) {
		sessions.put(session, System.currentTimeMillis());
	}

	public void removeSession(RhSessionID session) {
		sessions.remove(session);
	}

	public void close() {
		this.run.set(false);
		this.interrupt();
	}


	private long closeSessionIfTimeOver() {
		long timeToNextSessionEnd = sessionExpirationMs;
		Set<RhSessionID> sessionsIds = new LinkedHashSet<>(sessions.keySet());

		for (RhSessionID sessionsId : sessionsIds) {
			Long time = this.sessions.get(sessionsId);
			if (time == null) {
				continue;
			}

			long currentTime = System.currentTimeMillis();
			long sessionEnd = time + sessionExpirationMs;
			long timeToEndSession = sessionEnd - currentTime;

			if (timeToEndSession <= 0)
			{
				logger.warn("Session {} is inactive more than {} minutes. It will be closed due to timeout",
						sessionsId.getId(), sessionExpirationMs);

				try {
					framework.getHandExecutor().unregister(sessionsId);
					framework.unregisterSession(sessionsId);
				} catch (UIFrameworkException e) {
					logger.error("Enable to close session", e);
					sessions.remove(sessionsId);
				}
			}
			else if (timeToNextSessionEnd > timeToEndSession)
				timeToNextSessionEnd = timeToEndSession;
			
		}

		return timeToNextSessionEnd;
	}
}
