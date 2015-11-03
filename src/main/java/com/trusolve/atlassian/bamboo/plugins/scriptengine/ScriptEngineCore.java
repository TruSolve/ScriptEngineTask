package com.trusolve.atlassian.bamboo.plugins.scriptengine;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.atlassian.bamboo.build.artifact.ArtifactManager;
import com.atlassian.bamboo.build.test.TestCollationService;
import com.atlassian.bamboo.configuration.SystemInfo;
import com.atlassian.bamboo.deployments.results.service.DeploymentResultService;
import com.atlassian.bamboo.labels.LabelManager;
import com.atlassian.bamboo.logger.ErrorUpdateHandler;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.resultsummary.ResultsSummaryManager;
import com.atlassian.bamboo.v2.build.agent.capability.AgentContext;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityConfigurationManager;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.atlassian.bamboo.variable.CustomVariableContext;
import com.atlassian.sal.api.transaction.TransactionTemplate;

abstract public class ScriptEngineCore
{
	// The following variables/managers are NOT safe to run on remote agents
	// These likely will only function on an agent that is executing on the
	// local
	// server.
	private PlanManager planManager = null;
	public PlanManager getPlanManager()
	{
		return planManager;
	}
	public void setPlanManager(PlanManager planManager)
	{
		this.planManager = planManager;
	}

	private LabelManager labelManager = null;
	public LabelManager getLabelManager()
	{
		return labelManager;
	}
	public void setLabelManager(LabelManager labelManager)
	{
		this.labelManager = labelManager;
	}

	private ResultsSummaryManager resultsSummaryManager = null;
	public ResultsSummaryManager getResultsSummaryManager()
	{
		return resultsSummaryManager;
	}
	public void setResultsSummaryManager(ResultsSummaryManager resultsSummaryManager)
	{
		this.resultsSummaryManager = resultsSummaryManager;
	}

	private TransactionTemplate transactionTemplate = null;
	public TransactionTemplate getTransactionTemplate()
	{
		return transactionTemplate;
	}
	public void setTransactionTemplate(TransactionTemplate transactionTemplate)
	{
		this.transactionTemplate = transactionTemplate;
	}

	private DeploymentResultService deploymentResultService = null;
	public DeploymentResultService getDeploymentResultService()
	{
		return deploymentResultService;
	}
	public void setDeploymentResultService(DeploymentResultService deploymentResultService)
	{
		this.deploymentResultService = deploymentResultService;
	}

	// The following variables/managers ARE safe to run on remote agents and can
	// be used on
	// any build task.
	private TestCollationService testCollationService = null;

	public TestCollationService getTestCollationService()
	{
		return testCollationService;
	}

	public void setTestCollationService(TestCollationService testCollationService)
	{
		this.testCollationService = testCollationService;
	}

	private ProcessService processService = null;

	public ProcessService getProcessService()
	{
		return processService;
	}

	public void setProcessService(ProcessService processService)
	{
		this.processService = processService;
	}

	private ArtifactManager artifactManager = null;

	public ArtifactManager getArtifactManager()
	{
		return artifactManager;
	}

	public void setArtifactManager(ArtifactManager artifactManager)
	{
		this.artifactManager = artifactManager;
	}

	private CustomVariableContext customVariableContext = null;

	public CustomVariableContext getCustomVariableContext()
	{
		return customVariableContext;
	}

	public void setCustomVariableContext(CustomVariableContext customVariableContext)
	{
		this.customVariableContext = customVariableContext;
	}

	private ErrorUpdateHandler errorUpdateHandler = null;

	public ErrorUpdateHandler getErrorUpdateHandler()
	{
		return errorUpdateHandler;
	}

	public void setErrorUpdateHandler(ErrorUpdateHandler errorUpdateHandler)
	{
		this.errorUpdateHandler = errorUpdateHandler;
	}

	private AgentContext agentContext = null;

	public AgentContext getAgentContext()
	{
		return agentContext;
	}

	public void setAgentContext(AgentContext agentContext)
	{
		this.agentContext = agentContext;
	}

	private CapabilityConfigurationManager capabilityConfigurationManager = null;

	public CapabilityConfigurationManager getCapabilityConfigurationManager()
	{
		return capabilityConfigurationManager;
	}

	public void setCapabilityConfigurationManager(CapabilityConfigurationManager capabilityConfigurationManager)
	{
		this.capabilityConfigurationManager = capabilityConfigurationManager;
	}

	private CapabilityContext capabilityContext = null;

	public CapabilityContext getCapabilityContext()
	{
		return capabilityContext;
	}

	public void setCapabilityContext(CapabilityContext capabilityContext)
	{
		this.capabilityContext = capabilityContext;
	}

	private CapabilityDefaultsHelper capabilityDefaultsHelper = null;

	public CapabilityDefaultsHelper getCapabilityDefaultsHelper()
	{
		return capabilityDefaultsHelper;
	}

	public void setCapabilityDefaultsHelper(CapabilityDefaultsHelper capabilityDefaultsHelper)
	{
		this.capabilityDefaultsHelper = capabilityDefaultsHelper;
	}

	private SystemInfo systemInfo = null;

	public SystemInfo getSystemInfo()
	{
		return systemInfo;
	}

	public void setSystemInfo(SystemInfo systemInfo)
	{
		this.systemInfo = systemInfo;
	}

	protected void executeScript(String script, String scriptLanguage, ScriptContext scriptContext, boolean isFile) throws FileNotFoundException, ScriptException
	{
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName(scriptLanguage);
		if (engine == null)
		{
			throw new ScriptException("Script engine " + scriptLanguage + " not found.");
		}
		else
		{
			scriptContext.setAttribute("planManager", planManager, ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("labelManager", labelManager, ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("resultsSummaryManager", resultsSummaryManager, ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("transactionTemplate", transactionTemplate, ScriptContext.ENGINE_SCOPE);

			scriptContext.setAttribute("testCollationService", this.testCollationService, ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("processService", this.processService, ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("artifactManager", this.artifactManager, ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("customVariableContext", this.customVariableContext, ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("errorUpdateHandler", this.errorUpdateHandler, ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("agentContext", this.agentContext, ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("capabilityConfigurationManager", this.capabilityConfigurationManager, ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("capabilityContext", this.capabilityContext, ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("capabilityDefaultsHelper", this.capabilityDefaultsHelper, ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("systemInfo", this.systemInfo, ScriptContext.ENGINE_SCOPE);

			engine.setContext(scriptContext);

			if (isFile)
			{
				engine.eval(new FileReader(script));
			}
			else
			{
				engine.eval(script);
			}
		}
	}
}