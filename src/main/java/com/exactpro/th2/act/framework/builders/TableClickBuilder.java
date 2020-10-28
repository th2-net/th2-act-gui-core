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

package com.exactpro.th2.act.framework.builders;

import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.RhAction;
import com.exactpro.th2.act.grpc.hand.rhactions.RhWinActionsMessages;

import java.util.Iterator;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class TableClickBuilder extends DefaultBuilder<TableClickBuilder> {
	private Map<String, String> searchParams;
	private String columnName;
	private int columnIndex;


	public TableClickBuilder(UIFrameworkContext context) {
		super(context);
	}


	public TableClickBuilder searchParams(Map<String, String> searchParams) {
		this.searchParams = searchParams;
		return this;
	}

	public TableClickBuilder columnName(String columnName) {
		this.columnName = columnName;
		return this;
	}

	public TableClickBuilder columnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
		return this;
	}


	@Override
	protected TableClickBuilder getBuilder() {
		return this;
	}

	@Override
	protected String getActionName() {
		return "WinTableClick";
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException {
		validateParams();

		RhWinActionsMessages.WinTableClick.Builder builder = RhWinActionsMessages.WinTableClick.newBuilder();
		builder.addAllLocators(buildWinLocator(this.winLocator));
		addIfNotEmpty(id, builder::setId);
		addIfNotEmpty(execute, builder::setExecute);
		builder.setSearchParams(createFilters(searchParams));
		builder.setTargetColumn(columnName);
		builder.setColumnIndex(String.valueOf(columnIndex));

		return RhAction.newBuilder().setWinTableClick(builder.build()).build();
	}


	private void validateParams() throws UIFrameworkBuildingException {
		if (searchParams == null || searchParams.isEmpty() || isEmpty(columnName))
			throw new UIFrameworkBuildingException("Search params or column name cannot be empty");

		if (columnIndex < 0)
			throw new UIFrameworkBuildingException("Column index cannot be negative");
	}


	private static String createFilters(Map<String, String> searchParams) {
		StringBuilder result = new StringBuilder();

		Iterator<Map.Entry<String, String>> iterator = searchParams.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> searchParam = iterator.next();
			result.append(searchParam.getKey()).append("=").append(searchParam.getValue());
			if (iterator.hasNext())
				result.append(";");
		}

		return result.toString();
	}
}
