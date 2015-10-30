/* Copyright 2015 TruSolve, LLC

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.trusolve.atlassian.bamboo.plugins.scriptengine.tasks;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.core.util.PairType;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

public class ScriptEngineTaskConfigurator extends AbstractTaskConfigurator
{
	public static final String SCRIPTENGINE_SCRIPTBODY = "scriptbody";
	public static final String SCRIPTENGINE_SCRIPTTYPE = "scripttype";
	public static final String SCRIPTENGINE_SCRIPTLOCATION = "scriptlocation";
	public static final String SCRIPTENGINE_SCRIPT = "script";

	private static final Set<String> FIELDS = ImmutableSet.of(
		SCRIPTENGINE_SCRIPTTYPE, 
		SCRIPTENGINE_SCRIPTBODY, 
		SCRIPTENGINE_SCRIPTLOCATION, 
		SCRIPTENGINE_SCRIPT);

	@Override
	public void populateContextForCreate(@NotNull Map<String, Object> context)
	{
		super.populateContextForCreate(context);
		context.put(SCRIPTENGINE_SCRIPTTYPE, "js");
		context.put("locationTypes", getLocationTypes());
	}

	@Override
	public void populateContextForView(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition)
	{
		super.populateContextForView(context, taskDefinition);
		taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, FIELDS);
	}

	@Override
	public void populateContextForEdit(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition)
	{
		super.populateContextForEdit(context, taskDefinition);
		taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, FIELDS);
		context.put("locationTypes", getLocationTypes());
	}

	public List<PairType> getLocationTypes()
	{
		PairType file = new PairType("FILE", "File");
		PairType inline = new PairType("INLINE", "Inline");
		return Lists.newArrayList(new PairType[] { inline, file });
	}

	@NotNull
	@Override
	public Map<String, String> generateTaskConfigMap(@NotNull ActionParametersMap params, @Nullable TaskDefinition previousTaskDefinition)
	{
		final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
		taskConfiguratorHelper.populateTaskConfigMapWithActionParameters(config, params, FIELDS);
		return config;
	}

	@Override
	public void validate(@NotNull ActionParametersMap params, @NotNull ErrorCollection errorCollection)
	{
		super.validate(params, errorCollection);

		if (StringUtils.isEmpty(params.getString(ScriptEngineTaskConfigurator.SCRIPTENGINE_SCRIPTTYPE)))
		{
			errorCollection.addError(ScriptEngineTaskConfigurator.SCRIPTENGINE_SCRIPTTYPE, "Please specify the script language type.");
		}

		if (StringUtils.isEmpty(params.getString(ScriptEngineTaskConfigurator.SCRIPTENGINE_SCRIPTBODY)))
		{
			errorCollection.addError(ScriptEngineTaskConfigurator.SCRIPTENGINE_SCRIPTBODY, "Please specify the script text you would like to execute.");
		}
	}
}
