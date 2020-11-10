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
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class ActServer {
	private final Logger logger = LoggerFactory.getLogger(getClass().getName() + "@" + hashCode());
	private final Server server;
	private final List<BindableService> services;

	public ActServer(MicroserviceConfiguration configuration) throws IOException {
		services = createService(configuration);

		ServerBuilder<?> serverBuilder = ServerBuilder.forPort(configuration.getPort());
		for (BindableService service : services) {
			serverBuilder.addService(service);
		}
		
		this.server = serverBuilder.build();
		this.server.start();
				
		logger.info("'{}' started, listening on port '{}'", ActServer.class.getSimpleName(), configuration.getPort());
	}

	public void stop() throws InterruptedException {
		if (server.shutdown().awaitTermination(1, TimeUnit.SECONDS)) {
			logger.warn("Server didn't stop gracefully");
			server.shutdownNow();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon threads.
	 */
	public void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	public List<BindableService> getServices() {
		return services;
	}

	public abstract List<BindableService> createService(MicroserviceConfiguration configuration);
}
