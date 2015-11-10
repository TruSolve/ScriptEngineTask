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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.core.util.PairType;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.trusolve.atlassian.bamboo.plugins.scriptengine.ScriptEngineConstants;

public class ScriptEngineTaskConfigurator extends AbstractTaskConfigurator
{
	private static final Logger log = LoggerFactory.getLogger(ScriptEngineTaskConfigurator.class);
	
	private static final Set<String> FIELDS = ImmutableSet.of(
		ScriptEngineConstants.SCRIPTENGINE_RUNONSERVER,
		ScriptEngineConstants.SCRIPTENGINE_SCRIPTTYPE, 
		ScriptEngineConstants.SCRIPTENGINE_SCRIPTBODY, 
		ScriptEngineConstants.SCRIPTENGINE_SCRIPTLOCATION, 
		ScriptEngineConstants.SCRIPTENGINE_SCRIPTFILE);
	
	@Override
	public void populateContextForCreate(@NotNull Map<String, Object> context)
	{
		super.populateContextForCreate(context);
		context.put(ScriptEngineConstants.SCRIPTENGINE_SCRIPTTYPE, "js");
		context.put(ScriptEngineConstants.SCRIPTENGINE_LOCATIONTYPES, getLocationTypes());
		log.debug("Populated context for create");
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
		context.put(ScriptEngineConstants.SCRIPTENGINE_LOCATIONTYPES, getLocationTypes());
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

		if (StringUtils.isEmpty(params.getString(ScriptEngineConstants.SCRIPTENGINE_SCRIPTTYPE)))
		{
			errorCollection.addError(ScriptEngineConstants.SCRIPTENGINE_SCRIPTTYPE, "Please specify the script language type.");
		}

		if (StringUtils.isEmpty(params.getString(ScriptEngineConstants.SCRIPTENGINE_SCRIPTLOCATION)))
		{
			errorCollection.addError(ScriptEngineConstants.SCRIPTENGINE_SCRIPTLOCATION, "Please specify the script location.");
		}
		else
		{
			if( "FILE".equals(params.getString(ScriptEngineConstants.SCRIPTENGINE_SCRIPTLOCATION)))
			{
				if (StringUtils.isEmpty(params.getString(ScriptEngineConstants.SCRIPTENGINE_SCRIPTFILE)))
				{
					errorCollection.addError(ScriptEngineConstants.SCRIPTENGINE_SCRIPTFILE, "Please specify the script file location you would like to execute.");
				}
			}
			else
			{
				if (StringUtils.isEmpty(params.getString(ScriptEngineConstants.SCRIPTENGINE_SCRIPTBODY)))
				{
					errorCollection.addError(ScriptEngineConstants.SCRIPTENGINE_SCRIPTBODY, "Please specify the script text you would like to execute.");
				}
			}
		}
	}
}
