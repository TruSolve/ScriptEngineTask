package com.trusolve.atlassian.bamboo.plugins.scriptengine;
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

import javax.script.SimpleScriptContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.bamboo.agent.AgentType;
import com.atlassian.bamboo.build.CustomBuildProcessorServer;
import com.atlassian.bamboo.builder.BuildState;
import com.atlassian.bamboo.plan.PlanResultKey;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.v2.build.agent.BuildAgent;
import java.util.Map;

public class ScriptEngineBuildProcessorServer
	extends ScriptEngineCore
	implements CustomBuildProcessorServer
{
	private static final Logger log = LoggerFactory.getLogger(ScriptEngineBuildProcessorServer.class);
	
	private BuildContext buildContext = null;

	@Override
	public BuildContext call() throws InterruptedException, Exception
	{
		ResultsSummary resultsSummary = resultsSummaryManager.getResultsSummary(buildContext.getPlanResultKey());
		BuildAgent buildAgent = agentManager.getAgent(resultsSummary.getBuildAgentId());
		if( buildAgent != null && ! AgentType.LOCAL.equals(buildAgent.getType()))
		{
			for(TaskDefinition taskDefinition : buildContext.getTaskDefinitions() )
			{
				if( ScriptEngineConstants.PLUGIN_KEY.equals(taskDefinition.getPluginKey()))
				{
					configurationInit(taskDefinition.getConfiguration());

					SimpleScriptContext ssc = new SimpleScriptContext();
					ssc.setAttribute("buildContext", buildContext, SimpleScriptContext.ENGINE_SCOPE);

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
						buildContext.getCurrentResult().setBuildState(BuildState.FAILED);
						buildContext.getCurrentResult().getBuildErrors().add("Script Execution Failed." + e.getMessage());
					}
				}
			}
		}
		return buildContext;
	}

	@Override
	public void init(BuildContext buildContext)
	{
		this.buildContext = buildContext;
	}

}
