package com.trusolve.atlassian.bamboo.plugins.scriptengine.tasks;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.trusolve.atlassian.bamboo.plugins.scriptengine.ScriptEngineConstants;

public class ScriptEngineBuildTaskConfigurator extends ScriptEngineBaseTaskConfigurator
{
	private static final Set<String> FIELDS = ImmutableSet.of(
		ScriptEngineConstants.SCRIPTENGINE_SCRIPTTYPE, 
		ScriptEngineConstants.SCRIPTENGINE_SCRIPTBODY, 
		ScriptEngineConstants.SCRIPTENGINE_SCRIPTLOCATION, 
		ScriptEngineConstants.SCRIPTENGINE_SCRIPTFILE);

	@Override
	protected Set<String> getFields()
	{
		return FIELDS;
	}
}
