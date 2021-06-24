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

package com.exactpro.th2.act.framework.builders;

import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.grpc.hand.RhAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public abstract class AbstractBuilder<T extends AbstractBuilder<T>> {
	
	protected final UIFrameworkContext context;

	protected AbstractBuilder(UIFrameworkContext context) {
		this.context = context;
	}

	protected abstract T getBuilder();
	protected abstract String getActionName();
	protected abstract RhAction buildAction() throws UIFrameworkBuildingException;

	public void build() throws UIFrameworkBuildingException {
		this.context.addRhAction(this.buildAction());
	}

	protected void addIfNotEmpty(String arg, Consumer<String> function) {
		if (StringUtils.isNotEmpty(arg)) {
			function.accept(arg);
		}
	}

	protected <V> void addIfNotNull(V arg, Consumer<V> function) {
		if (arg != null) {
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

	protected void checkRequiredFields(AbstractBuilder.MandatoryFieldDesc... fields) throws UIFrameworkBuildingException {
		Set<String> nullableFields = new LinkedHashSet<>();
		for (AbstractBuilder.MandatoryFieldDesc field : fields) {
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
