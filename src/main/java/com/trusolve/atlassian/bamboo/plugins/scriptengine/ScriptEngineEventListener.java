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

package com.trusolve.atlassian.bamboo.plugins.scriptengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.SimpleScriptContext;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.builder.BuildState;
import com.atlassian.bamboo.deployments.execution.events.DeploymentFinishedEvent;
import com.atlassian.bamboo.deployments.results.DeploymentResult;
import com.atlassian.bamboo.deployments.results.service.DeploymentResultService;
import com.atlassian.bamboo.labels.Label;
import com.atlassian.bamboo.plan.PlanResultKey;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.atlassian.bamboo.resultsummary.ResultsSummaryCriteria;
import com.atlassian.bamboo.resultsummary.ResultsSummaryManager;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.event.api.EventListener;
import com.atlassian.sal.api.transaction.TransactionCallback;

public class ScriptEngineEventListener
	extends ScriptEngineCore
{
	private static final Logger log = LoggerFactory.getLogger(ScriptEngineEventListener.class);
	
	@EventListener
	public void handleDeploymentScripts(DeploymentFinishedEvent deploymentFinishedEvent)
	{
		DeploymentResult deploymentResult = deploymentResultService.getDeploymentResult(deploymentFinishedEvent.getDeploymentResultId());
		for(TaskDefinition taskDefinition : deploymentResult.getEnvironment().getTaskDefinitions())
		{
			if( ScriptEngineConstants.PLUGIN_KEY.equals(taskDefinition.getPluginKey()))
			{
				executeScript(taskDefinition.getConfiguration(), deploymentFinishedEvent.getDeploymentResultId(), deploymentResult);
			}
		}
	}
	
	private void executeScript(Map<String,String> config, long deploymentResultId, DeploymentResult deploymentResult )
	{
		boolean buildFailed = false;
		
		configurationInit(config);

		if( scriptRunOnServer == null || ! "true".equalsIgnoreCase(scriptRunOnServer) )
		{
			return;
		}
		
		BuildLogger buildLogger = buildLoggerManager.getLogger(deploymentResult.getKey());
		buildLogger.addBuildLogEntry("Starting server side JSR 223 script execution");
		
		SimpleScriptContext ssc = new SimpleScriptContext();
		ssc.setAttribute("deploymentResultId", deploymentResultId, ScriptContext.ENGINE_SCOPE);
		ssc.setAttribute("deploymentResult", deploymentResult, ScriptContext.ENGINE_SCOPE);
		ssc.setAttribute("buildLogger", buildLogger, ScriptContext.ENGINE_SCOPE);

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
			buildLogger.addErrorLogEntry("Error executing server side script", e);
			buildFailed = true;
		}
		buildLogger.addBuildLogEntry("Finished server side JSR 223 script execution");

		if( buildFailed )
		{
			deploymentResultService.updateDeploymentState(deploymentResultId, BuildState.FAILED, deploymentResult.getCustomData());
		}
	}
}
