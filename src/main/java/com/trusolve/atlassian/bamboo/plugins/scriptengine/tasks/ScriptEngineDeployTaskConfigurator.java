package com.trusolve.atlassian.bamboo.plugins.scriptengine.tasks;

import java.util.Map;
import java.util.Set;

import com.atlassian.bamboo.task.TaskDefinition;
import com.google.common.collect.ImmutableSet;
import com.trusolve.atlassian.bamboo.plugins.scriptengine.ScriptEngineConstants;

public class ScriptEngineDeployTaskConfigurator extends ScriptEngineBaseTaskConfigurator
{
	private static final Set<String> FIELDS = ImmutableSet.of(
			ScriptEngineConstants.SCRIPTENGINE_DEPLOYRUNONSERVER,
			ScriptEngineConstants.SCRIPTENGINE_SCRIPTTYPE, 
			ScriptEngineConstants.SCRIPTENGINE_SCRIPTBODY, 
			ScriptEngineConstants.SCRIPTENGINE_SCRIPTLOCATION, 
			ScriptEngineConstants.SCRIPTENGINE_SCRIPTFILE);

	@Override
	public void populateContextForCreate(Map<String, Object> context)
	{
		super.populateContextForCreate(context);
		context.put(ScriptEngineConstants.SCRIPTENGINE_DEPLOYRUNONSERVER, "false");
		context.put(ScriptEngineConstants.SCRIPTENGINE_ISDEPLOYMENT, "true");
	}

	@Override
	public void populateContextForEdit(Map<String, Object> context, TaskDefinition taskDefinition)
	{
		super.populateContextForEdit(context, taskDefinition);
		context.put(ScriptEngineConstants.SCRIPTENGINE_ISDEPLOYMENT, "true");
	}

	@Override
	protected Set<String> getFields()
	{
		return FIELDS;
	}
}
