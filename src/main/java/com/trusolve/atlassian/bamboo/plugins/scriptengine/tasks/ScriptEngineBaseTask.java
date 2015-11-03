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

import javax.script.SimpleScriptContext;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.task.CommonTaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.variable.VariableContext;
import com.atlassian.bamboo.variable.VariableDefinitionContext;
import com.trusolve.atlassian.bamboo.plugins.scriptengine.ScriptEngineConstants;
import com.trusolve.atlassian.bamboo.plugins.scriptengine.ScriptEngineCore;

public abstract class ScriptEngineBaseTask
	extends ScriptEngineCore
{	
	public TaskResult execute(CommonTaskContext taskContext) throws TaskException
	{
		final TaskResultBuilder builder = TaskResultBuilder.newBuilder(taskContext);
		final BuildLogger buildLogger = taskContext.getBuildLogger();

		final ConfigurationMap config = taskContext.getConfigurationMap();

		final String scriptDeployRunOnServer = config.get(ScriptEngineConstants.SCRIPTENGINE_DEPLOYRUNONSERVER);
		final String scriptLanguage = config.get(ScriptEngineConstants.SCRIPTENGINE_SCRIPTTYPE);
		final String scriptBody = config.get(ScriptEngineConstants.SCRIPTENGINE_SCRIPTBODY);
		final String scriptFile = config.get(ScriptEngineConstants.SCRIPTENGINE_SCRIPTFILE);
		final String scriptLocation = config.get(ScriptEngineConstants.SCRIPTENGINE_SCRIPTLOCATION);

		if( scriptDeployRunOnServer != null && "true".equalsIgnoreCase(scriptDeployRunOnServer) )
		{
			VariableContext variableContext = taskContext.getCommonContext().getVariableContext();
			VariableDefinitionContext scriptCountVariable = variableContext.getEffectiveVariables().get(ScriptEngineConstants.SCRIPTENGINE_DEPLOYRUNONSERVERCOUNT);
			int scriptCount = 1;
			if( scriptCountVariable != null )
			{
				try
				{
					scriptCount = Integer.parseInt(scriptCountVariable.getValue());
				}
				catch( NumberFormatException nfe )
				{
					buildLogger.addErrorLogEntry("Error parsing number of post execution scripts.", nfe);
					builder.failed();
				}
				scriptCount++;
			}
			variableContext.addResultVariable(ScriptEngineConstants.SCRIPTENGINE_DEPLOYRUNONSERVERCOUNT, Integer.toString(scriptCount));
			variableContext.addResultVariable(ScriptEngineConstants.SCRIPTENGINE_SCRIPTBODY + "." + Integer.toString(scriptCount), scriptBody);
			variableContext.addResultVariable(ScriptEngineConstants.SCRIPTENGINE_SCRIPTFILE + "." + Integer.toString(scriptCount), scriptFile);
			variableContext.addResultVariable(ScriptEngineConstants.SCRIPTENGINE_SCRIPTLOCATION + "." + Integer.toString(scriptCount), scriptLocation);
			variableContext.addResultVariable(ScriptEngineConstants.SCRIPTENGINE_SCRIPTTYPE + "." + Integer.toString(scriptCount), scriptLanguage);
			return builder.build();
		}
		
		
		SimpleScriptContext ssc = new SimpleScriptContext();
		ssc.setAttribute("taskContext", taskContext, SimpleScriptContext.ENGINE_SCOPE);
		ssc.setAttribute("builder", builder, SimpleScriptContext.ENGINE_SCOPE);
		ssc.setAttribute("buildLogger", buildLogger, SimpleScriptContext.ENGINE_SCOPE);

		try
		{
			if ("FILE".equals(scriptLocation))
			{
				this.executeScript(scriptFile, scriptLanguage, ssc, true);
			}
			else
			{
				this.executeScript(scriptBody, scriptLanguage, ssc, false);
			}
		}
		catch (Exception e)
		{
			buildLogger.addErrorLogEntry("Script engine exception.", e);
			builder.failed();
		}
		return builder.build();
	}
}
