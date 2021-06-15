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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableSearchBuilder extends AbstractWinBuilder<TableSearchBuilder> {

	private List<TableSearchFilter> searchParams;
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

	public TableSearchBuilder addSearchParams(String name, String value) {
		return this.addSearchParams(name, value, null);
	}

	public TableSearchBuilder addSearchParams(String name, String value, Integer index) {
		if (this.searchParams == null) {
			this.searchParams = new ArrayList<>();
		}
		this.searchParams.add(new TableSearchFilter(name, value, index));
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
		builder.setId(id);
		addIfNotEmpty(execute, builder::setExecute);
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

	private static String createFilters(List<TableSearchFilter> searchParams) {
		StringBuilder result = new StringBuilder();

		Iterator<TableSearchFilter> iterator = searchParams.iterator();
		while (iterator.hasNext()) {
			TableSearchFilter searchParam = iterator.next();
			result.append(searchParam.name).append("=").append(searchParam.value);
			if (searchParam.index != null) {
				result.append("=").append(searchParam.index);
			}
			if (iterator.hasNext())
				result.append(";");
		}

		return result.toString();
	}

	private static class TableSearchFilter {
		private final String name;
		private final String value;
		private final Integer index;

		public TableSearchFilter(String name, String value, Integer index) {
			this.name = name;
			this.value = value;
			this.index = index;
		}
	}
	
}