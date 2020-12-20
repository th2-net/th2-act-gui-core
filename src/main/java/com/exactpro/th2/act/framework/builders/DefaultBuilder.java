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
import com.google.protobuf.Int32Value;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public abstract class DefaultBuilder<T extends DefaultBuilder<T>> {
	
	public static final String WIN_LOCATOR_FIELD_NAME = "winLocator";
	
	protected String id;
	protected String execute;
	protected WinLocator winLocator;
	
	protected final UIFrameworkContext context;

	public DefaultBuilder(UIFrameworkContext context) {
		this.context = context;
	}

	protected abstract T getBuilder();
	protected abstract String getActionName();
	protected abstract RhAction buildAction() throws UIFrameworkBuildingException;
	
	public void build() throws UIFrameworkBuildingException {
		this.context.addRhAction(this.buildAction());
	}
	
	public T id(String id) {
		this.id = id;
		return getBuilder();
	}

	public T execute(String execute) {
		this.execute = execute;
		return getBuilder();
	}

	public T winLocator(WinLocator winLocator) {
		this.winLocator = winLocator;
		return getBuilder();
	}

	protected List<RhWinActionsMessages.WinLocator> buildWinLocator(WinLocator winLocator) {

		List<RhWinActionsMessages.WinLocator> builtLocators = new ArrayList<>();
		WinLocator currentLocator = winLocator;

		do {
			RhWinActionsMessages.WinLocator.Builder builder = RhWinActionsMessages.WinLocator.newBuilder();
			builder.setLocator(currentLocator.getLocatorType().getName());
			builder.setMatcher(currentLocator.getMatcher());
			if (currentLocator.getMatcherIndex() != null)
				builder.setMatcherIndex(Int32Value.of(currentLocator.getMatcherIndex()));
			builtLocators.add(builder.build());
			currentLocator = currentLocator.getParent();

		} while (currentLocator != null && !currentLocator.isRoot());

		Collections.reverse(builtLocators);
		return builtLocators;
	}
	
	protected void addIfNotEmpty(String arg, Consumer<String> function) {
		if (arg != null && !arg.isEmpty()) {
			function.accept(arg);
		}
	}
	
	private boolean isNullOrEmpty(Object obj) {
		return (obj == null || (obj instanceof String && ((String) obj).isEmpty()));
	}
	
	protected void checkRequiredFields(String actionName, List<Pair<Object, String>> fields) throws UIFrameworkBuildingException {
		Set<String> nullableFields = new LinkedHashSet<>();
		for (Pair<Object, String> field : fields) {
			if (isNullOrEmpty(field.getKey()))
				nullableFields.add(field.getValue());
		}
		throw new UIFrameworkBuildingException(actionName, nullableFields);
	}

	protected void checkRequiredFields(Object value, String name) throws UIFrameworkBuildingException {
		if (isNullOrEmpty(value))
			throw new UIFrameworkBuildingException(getActionName(), name);
	}

	protected void checkRequiredFields(Object value, String name, Object value2, String name2) throws UIFrameworkBuildingException {
		Set<String> nullableFields = new LinkedHashSet<>();
		if (isNullOrEmpty(value))
			nullableFields.add(name);
		if (isNullOrEmpty(value2))
			nullableFields.add(name2);
		if (!nullableFields.isEmpty())
			throw new UIFrameworkBuildingException(getActionName(), nullableFields);
	}

	protected void checkRequiredFields(Object value, String name, Object value2, String name2, Object value3, String name3) throws UIFrameworkBuildingException {
		Set<String> nullableFields = new LinkedHashSet<>();
		if (isNullOrEmpty(value))
			nullableFields.add(name);
		if (isNullOrEmpty(value2))
			nullableFields.add(name2);
		if (isNullOrEmpty(value3))
			nullableFields.add(name3);
		if (!nullableFields.isEmpty())
			throw new UIFrameworkBuildingException(getActionName(), nullableFields);
	}

	protected void checkRequiredFields(MandatoryFieldDesc... fields) throws UIFrameworkBuildingException {
		Set<String> nullableFields = new LinkedHashSet<>();
		for (MandatoryFieldDesc field : fields) {
			if (isNullOrEmpty(field.value))
				nullableFields.add(field.fieldName);
		}
		if (!nullableFields.isEmpty())
			throw new UIFrameworkBuildingException(getActionName(), nullableFields);
	}
	
	protected static class MandatoryFieldDesc {
		protected final Object value;
		protected final String fieldName;

		public MandatoryFieldDesc(Object value, String fieldName) {
			this.value = value;
			this.fieldName = fieldName;
		}
	}
	
}
