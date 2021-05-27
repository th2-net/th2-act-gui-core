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

package com.exactpro.th2.act.framework.builders.win;

import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.RhAction;
import com.exactpro.th2.act.grpc.hand.rhactions.RhWinActionsMessages.WinTableSearch;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;

public class TableSearchBuilder extends AbstractWinBuilder<TableSearchBuilder> {

	private Map<String, String> searchParams;
	private String columnName;
	private int columnIndex;
	private int firstRowIndex;
	private String rowNameFormat;
	private String rowElementNameFormat;
	private String rowElementValueFormat;
	private String saveResult;


	public TableSearchBuilder(UIFrameworkContext context)
	{
		super(context);
	}

	public TableSearchBuilder searchParams(Map<String, String> searchParams) {
		this.searchParams = searchParams;
		return this;
	}

	public TableSearchBuilder columnName(String columnName) {
		this.columnName = columnName;
		return this;
	}

	public TableSearchBuilder columnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
		return this;
	}

	public TableSearchBuilder setFirstRowIndex(int firstRowIndex)
	{
		this.firstRowIndex = firstRowIndex;
		return this;
	}

	public TableSearchBuilder setRowNameFormat(String rowNameFormat)
	{
		this.rowNameFormat = rowNameFormat;
		return this;
	}

	public TableSearchBuilder setRowElementNameFormat(String rowElementNameFormat)
	{
		this.rowElementNameFormat = rowElementNameFormat;
		return this;
	}

	public TableSearchBuilder setRowElementValueFormat(String rowElementValueFormat)
	{
		this.rowElementValueFormat = rowElementValueFormat;
		return this;
	}

	public TableSearchBuilder saveResult(boolean save) {
		this.saveResult = String.valueOf(save);
		return this;
	}


	@Override
	protected TableSearchBuilder getBuilder()
	{
		return this;
	}

	@Override
	protected String getActionName()
	{
		return "TableSearch";
	}

	@Override
	protected RhAction buildAction() throws UIFrameworkBuildingException
	{
		validateParams();

		WinTableSearch.Builder builder = WinTableSearch.newBuilder();
		builder.addAllLocators(buildWinLocator(winLocator));
		builder.setBaseParams(buildBaseParam());
		addIfNotEmpty(String.valueOf(firstRowIndex), builder::setFirstRowIndex);
		addIfNotEmpty(rowNameFormat, builder::setRowNameFormat);
		addIfNotEmpty(rowElementNameFormat, builder::setRowElementNameFormat);
		addIfNotEmpty(rowElementValueFormat, builder::setRowElementValueFormat);
		builder.setSearchParams(createFilters(searchParams));
		builder.setTargetColumn(columnName);
		builder.setColumnIndex(String.valueOf(columnIndex));
		addIfNotEmpty(saveResult, builder::setSaveResult);

		return RhAction.newBuilder().setWinTableSearch(builder.build()).build();
	}

	private void validateParams() throws UIFrameworkBuildingException {
		if (searchParams == null || searchParams.isEmpty() || columnName == null)
			throw new UIFrameworkBuildingException("Search parameters and column name must be specified");

		if (columnIndex < 0)
			throw new UIFrameworkBuildingException("Column index cannot be negative");
		
		if (StringUtils.isBlank(id))
			throw new UIFrameworkBuildingException("Id cannot be empty");
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