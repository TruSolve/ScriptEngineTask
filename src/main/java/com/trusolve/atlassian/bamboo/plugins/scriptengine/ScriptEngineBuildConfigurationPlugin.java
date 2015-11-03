package com.trusolve.atlassian.bamboo.plugins.scriptengine;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.atlassian.bamboo.build.Job;
import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.v2.build.BaseBuildConfigurationAwarePlugin;
import com.atlassian.bamboo.v2.build.configuration.MiscellaneousBuildConfigurationPlugin;
import com.atlassian.bamboo.ww2.actions.build.admin.create.BuildConfiguration;
import com.atlassian.core.util.PairType;
import com.google.common.collect.Lists;

public class ScriptEngineBuildConfigurationPlugin
	extends BaseBuildConfigurationAwarePlugin
	implements MiscellaneousBuildConfigurationPlugin
{
	@Override
	public ErrorCollection validate(BuildConfiguration buildConfiguration)
	{
		ErrorCollection errorCollection = super.validate(buildConfiguration);
		if( buildConfiguration.getProperty(ScriptEngineConstants.SCRIPTENGINE_BUILDPROCESSORSERVERENABLE) == null )
		{
			buildConfiguration.setProperty(ScriptEngineConstants.SCRIPTENGINE_BUILDPROCESSORSERVERENABLE, false);
			return errorCollection;
		}

		if (StringUtils.isEmpty(buildConfiguration.getProperty(ScriptEngineConstants.SCRIPTENGINE_SCRIPTTYPE).toString()))
		{
			errorCollection.addError(ScriptEngineConstants.SCRIPTENGINE_SCRIPTTYPE, "Please specify the script language type.");
		}

		if (StringUtils.isEmpty(buildConfiguration.getProperty(ScriptEngineConstants.SCRIPTENGINE_SCRIPTLOCATION).toString()))
		{
			errorCollection.addError(ScriptEngineConstants.SCRIPTENGINE_SCRIPTLOCATION, "Please specify the script location.");
		}
		else
		{
			if( "FILE".equals(buildConfiguration.getProperty(ScriptEngineConstants.SCRIPTENGINE_SCRIPTLOCATION).toString()))
			{
				if (StringUtils.isEmpty(buildConfiguration.getProperty(ScriptEngineConstants.SCRIPTENGINE_SCRIPTFILE).toString()))
				{
					errorCollection.addError(ScriptEngineConstants.SCRIPTENGINE_SCRIPTFILE, "Please specify the script file location you would like to execute.");
				}
			}
			else
			{
				if (StringUtils.isEmpty(buildConfiguration.getProperty(ScriptEngineConstants.SCRIPTENGINE_SCRIPTBODY).toString()))
				{
					errorCollection.addError(ScriptEngineConstants.SCRIPTENGINE_SCRIPTBODY, "Please specify the script text you would like to execute.");
				}
			}
		}
        return errorCollection;
	}

	@Override
	public void addDefaultValues(BuildConfiguration buildConfiguration)
	{
		super.addDefaultValues(buildConfiguration);
		buildConfiguration.addProperty(ScriptEngineConstants.SCRIPTENGINE_BUILDPROCESSORSERVERENABLE,  false);
		buildConfiguration.addProperty(ScriptEngineConstants.SCRIPTENGINE_SCRIPTLOCATION, "INLINE");
		buildConfiguration.addProperty(ScriptEngineConstants.SCRIPTENGINE_SCRIPTTYPE, "js");
	}

	@Override
	protected void populateContextForEdit(Map<String, Object> context, BuildConfiguration buildConfiguration, Plan plan)
	{
		super.populateContextForEdit(context, buildConfiguration, plan);
		context.put(ScriptEngineConstants.SCRIPTENGINE_LOCATIONTYPES, getLocationTypes());
	}

	private List<PairType> getLocationTypes()
	{
		PairType file = new PairType("FILE", "File");
		PairType inline = new PairType("INLINE", "Inline");
		return Lists.newArrayList(new PairType[] { inline, file });
	}

	@Override
	public boolean isConfigurationMissing(BuildConfiguration buildConfiguration)
	{
		if ( buildConfiguration.getProperty("com.trusolve.labelexpiration.enable") == null )
		{
			return(true);
		}
		return super.isConfigurationMissing(buildConfiguration);
	}

	@Override
	public boolean isApplicableTo(Plan plan)
	{
		return plan instanceof Job;
	}
}