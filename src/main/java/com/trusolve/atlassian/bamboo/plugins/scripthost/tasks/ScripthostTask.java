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

package com.trusolve.atlassian.bamboo.plugins.scripthost.tasks;

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.bamboo.variable.VariableDefinitionContext;

public class ScripthostTask implements TaskType
{

	@Override
	public TaskResult execute(TaskContext taskContext) throws TaskException
	{
		final TaskResultBuilder builder = TaskResultBuilder.newBuilder(taskContext);
        final BuildLogger buildLogger = taskContext.getBuildLogger();

        final ConfigurationMap config = taskContext.getConfigurationMap();
        
        final String scriptLanguage = config.get(ScripthostTaskConfigurator.SCRIPTHOST_SCRIPTTYPE);
        final String script = config.get(ScripthostTaskConfigurator.SCRIPTHOST_SCRIPTBODY);
        
        try
		{
        	ScriptEngineManager factory = new ScriptEngineManager();
        	ScriptEngine engine = factory.getEngineByName( scriptLanguage );
			if( engine == null )
			{
				buildLogger.addErrorLogEntry("Script engine " + scriptLanguage + " not found.");
				builder.failed();
			}
			else
			{
				engine.put("taskContext", taskContext);
				engine.put("builder", builder);
				engine.put("buildLogger", buildLogger);
				engine.eval(script);
			}
		}
		catch( Exception e )
		{
			buildLogger.addErrorLogEntry("Script Exception: " + e.getMessage() );
			builder.failed();
		}
		return builder.build();
	}

}
