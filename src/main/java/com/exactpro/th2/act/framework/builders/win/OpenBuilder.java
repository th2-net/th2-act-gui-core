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

package com.exactpro.th2.act.framework.builders.win;

import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.RhAction;
import com.exactpro.th2.act.grpc.hand.rhactions.RhWinActionsMessages;

public class OpenBuilder extends WinDefaultBuilder<OpenBuilder> {
	
	public static final String WORK_DIR_FIELD_NAME = "workDir";
	public static final String APP_FILE_FIELD_NAME = "appFile";
	
	private String workDir;
	private String appFile;
	private String appArgs;

	public OpenBuilder(UIFrameworkContext context) {
		super(context);
	}

	public OpenBuilder workDir(String workDir) {
		this.workDir = workDir;
		return this;
	}

	public OpenBuilder appFile(String appFile) {
		this.appFile = appFile;
		return this;
	}

	public OpenBuilder appArgs(String appArgs) {
		this.appArgs = appArgs;
		return this;
	}
	
	@Override
	protected OpenBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WinOpen";
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		checkRequiredFields(workDir, WORK_DIR_FIELD_NAME, appFile, APP_FILE_FIELD_NAME);
		RhWinActionsMessages.WinOpen.Builder builder = RhWinActionsMessages.WinOpen.newBuilder();
		builder.setAppFile(appFile);
		builder.setWorkDir(workDir);
		addIfNotEmpty(appArgs, builder::setAppArgs);
		addIfNotEmpty(id, builder::setId);
		addIfNotEmpty(execute, builder::setExecute);
		return RhAction.newBuilder().setWinOpen(builder).build();
	}
}
