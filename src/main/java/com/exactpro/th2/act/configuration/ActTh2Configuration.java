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

package com.exactpro.th2.act.configuration;

import com.exactpro.th2.configuration.Configuration;
import com.exactpro.th2.configuration.Th2Configuration;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.System.getenv;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.math.NumberUtils.toInt;

public class ActTh2Configuration extends Th2Configuration
{
	public static final String ENV_HAND_GRPC_HOST = "HAND_GRPC_HOST";
	public static final String DEFAULT_HAND_GRPC_HOST = "localhost";

	public static String getEnvHandGrpcHost() {
		return defaultIfNull(getenv(ENV_HAND_GRPC_HOST), DEFAULT_HAND_GRPC_HOST);
	}

	public static final String ENV_HAND_GRPC_PORT = "HAND_GRPC_PORT";
	public static final int DEFAULT_HAND_GRPC_PORT = 8080;

	public static int getEnvHandGrpcPort() {
		return toInt(getenv(ENV_HAND_GRPC_PORT), DEFAULT_HAND_GRPC_PORT);
	}

	private String handGrpcHost = getEnvHandGrpcHost();
	private int handGrpcPort = getEnvHandGrpcPort();

	public static Th2Configuration load(InputStream inputStream) throws IOException
	{
		return Configuration.YAML_READER.readValue(inputStream, ActTh2Configuration.class);
	}

	public String getHandGRPCHost()
	{
		return handGrpcHost;
	}

	public void setHandGRPCHost(String handGRPCHost)
	{
		this.handGrpcHost = handGRPCHost;
	}

	public int getHandGRPCPort()
	{
		return handGrpcPort;
	}

	public void setHandGRPCPort(int handGRPCPort)
	{
		this.handGrpcPort = handGRPCPort;
	}

	@Override
	public String toString() {
		return "ActTh2Configuration{" +
				"connectivityQueueNames=" + getConnectivityQueueNames() +
				", th2EventStorageGRPCHost='" + getTh2EventStorageGRPCHost() + '\'' +
				", th2EventStorageGRPCPort=" + getTh2EventStorageGRPCPort() +
				", handGRPCHost='" + handGrpcHost + '\'' +
				", handGRPCPort=" + handGrpcPort +
				'}';
	}
}
