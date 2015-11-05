package com.trusolve.atlassian.bamboo.plugins.scriptengine;

import java.util.Map;

import javax.script.SimpleScriptContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.bamboo.deployments.execution.events.DeploymentFinishedEvent;
import com.atlassian.bamboo.deployments.results.DeploymentResult;
import com.atlassian.bamboo.deployments.results.service.DeploymentResultService;
import com.atlassian.bamboo.resultsummary.ResultsSummaryManager;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.event.api.EventListener;

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
			if( "ccom.trusolve.atlassian.bamboo.plugins.ScriptEngine:scriptEngineDeployTask".equals(taskDefinition.getPluginKey()))
			{
				executeScript(taskDefinition.getConfiguration(), deploymentFinishedEvent.getDeploymentResultId());
			}
		}
	}
	
	private void executeScript(Map<String,String> config, long deploymentResultId)
	{
		final String scriptDeployRunOnServer = config.get(ScriptEngineConstants.SCRIPTENGINE_DEPLOYRUNONSERVER);
		final String scriptLanguage = config.get(ScriptEngineConstants.SCRIPTENGINE_SCRIPTTYPE);
		final String scriptBody = config.get(ScriptEngineConstants.SCRIPTENGINE_SCRIPTBODY);
		final String scriptFile = config.get(ScriptEngineConstants.SCRIPTENGINE_SCRIPTFILE);
		final String scriptLocation = config.get(ScriptEngineConstants.SCRIPTENGINE_SCRIPTLOCATION);

		if( scriptDeployRunOnServer == null || "true".equalsIgnoreCase(scriptDeployRunOnServer) )
		{
			return;
		}
		
		
		SimpleScriptContext ssc = new SimpleScriptContext();
		ssc.setAttribute("deploymentResultId", deploymentResultId, SimpleScriptContext.ENGINE_SCOPE);

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
		}
	}
}
