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

package com.exactpro.th2.act.framework;

import com.exactpro.th2.act.ActConnections;
import com.exactpro.th2.act.configuration.CustomConfiguration;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkException;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkIsBusyException;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.common.grpc.EventID;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class UIFramework<T extends UIFrameworkContext, K extends UIFrameworkSessionContext<T>> implements AutoCloseable {

	private static final Logger logger = LoggerFactory.getLogger(UIFramework.class);
	
	protected final Map<RhSessionID, K> contexts;
	protected final ActConnections<? extends CustomConfiguration> connections;
	protected final CustomConfiguration configuration;
	protected final HandExecutor handExecutor;
	protected final SessionWatcher sessionWatcher;


	public UIFramework(ActConnections<? extends CustomConfiguration> connections) {
		this.connections = connections;
		this.configuration = connections.getCustomConfiguration();
		this.handExecutor = new HandExecutor(connections.getHandConnector(), connections.getEventStoreHandler());
		this.contexts = new ConcurrentHashMap<>();
		this.sessionWatcher = SessionWatcher.create(this, configuration);
		this.sessionWatcher.start();
	}

	private Pair<K, Boolean> createContext(RhSessionID sessionID) {
		synchronized (this) {
			boolean created = false;
			K frameworkSessionContext = contexts.get(sessionID);
			if (frameworkSessionContext == null) {
				frameworkSessionContext = createSessionContext(createContext(sessionID, this.handExecutor));
				contexts.put(sessionID, frameworkSessionContext);
				created = true;
				sessionWatcher.updateSessionTime(sessionID);
			}
			return new ImmutablePair<>(frameworkSessionContext, created);
		}
	}

	protected abstract T createContext(RhSessionID sessionID, HandExecutor executor);
	protected abstract K createSessionContext(T context);
	
	private boolean checkBusy(final K context) {
		if (context.isBusy() || context.isInvalidated()) {
			return false;
		}
		synchronized (context.getContext()) {
			if (context.isBusy() || context.isInvalidated()) {
				return false;
			}
			context.setBusy(true);
			return true;
		}
	}
	
	private void releaseExecution(final K context) {
		synchronized (context.getContext()) {
			context.setBusy(false);
		}
		context.getContext().setParentEventId(null);
	}

	private boolean invalidate(final K context) {
		if (context.isBusy()) {
			return false;
		}
		synchronized (context.getContext()) {
			if (context.isBusy()) {
				return false;
			}
			context.setInvalidated(true);
			return true;
		}
	}
	
	public void registerSession(RhSessionID sessionID) throws UIFrameworkException {
		K sessionContext = this.contexts.get(sessionID);
		boolean existed = sessionContext != null;
		if (!existed) {
			Pair<K, Boolean> context = this.createContext(sessionID);
			existed = !context.getValue();
		}
		
		if (existed) {
			throw new UIFrameworkException("Session is already created: " + sessionID.getId());
		}
	}
	

	public T newExecution(RhSessionID sessionID) throws UIFrameworkException {
		
		K sessionContext = this.contexts.get(sessionID);
		if (sessionContext == null) {
			throw new UIFrameworkException("Session is not created properly: " + sessionID.getId());
		}
		if (!this.checkBusy(sessionContext)) {
			throw new UIFrameworkIsBusyException(sessionID);
		}
		sessionWatcher.updateSessionTime(sessionID);

		return sessionContext.getContext();
	}
	
	public void onExecutionFinished(UIFrameworkContext context) {
		RhSessionID sessionID = context.getSessionID();
		this.releaseExecution(this.contexts.get(sessionID));
		this.sessionWatcher.updateSessionTime(sessionID);
	}

	public void unregisterSession(RhSessionID sessionID) throws UIFrameworkException {
		K sessionContext = this.contexts.get(sessionID);
		if (sessionContext == null) {
			throw new UIFrameworkException("Session is not created properly: " + sessionID.getId());
		}

		if (!this.invalidate(sessionContext)) {
			throw new UIFrameworkIsBusyException(sessionID);
		}
		sessionWatcher.removeSession(sessionID);

		this.contexts.remove(sessionID);
	}

	public HandExecutor getHandExecutor() {
		return handExecutor;
	}
	
	public EventID createParentEvent(EventID parentEventId, String name) {
		return this.handExecutor.logParentEvent(parentEventId, name, Collections.emptyMap());
	}

	public EventID createParentEvent(EventID parentEventId, String name, Map<String, String> requested) {
		return this.handExecutor.logParentEvent(parentEventId, name, requested);
	}

	public EventID createErrorEvent(EventID parentEventId, String name, Map<String, String> requested, String error, Throwable t) {
		return this.handExecutor.logErrorEvent(parentEventId, name, requested, error, t);
	}

	public EventID createErrorEvent(EventID parentEventId, String name, String error, Throwable t) {
		return this.createErrorEvent(parentEventId, name, Collections.emptyMap(), error, t);
	}

	@Override
	public void close() {
		connections.close();
		sessionWatcher.close();
	}

	public CustomConfiguration getConfiguration() {
		return configuration;
	}
}
