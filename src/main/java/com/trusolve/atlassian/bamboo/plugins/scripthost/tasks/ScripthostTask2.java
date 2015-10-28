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

import java.io.FileReader;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.atlassian.bamboo.build.BuildDefinitionManager;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.deployments.execution.DeploymentContext;
import com.atlassian.bamboo.labels.LabelManager;
import com.atlassian.bamboo.plan.PlanKeys;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.plan.PlanResultKey;
import com.atlassian.bamboo.resultsummary.ResultsSummaryManager;
import com.atlassian.bamboo.task.CommonTaskContext;
import com.atlassian.bamboo.task.CommonTaskType;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.bamboo.variable.VariableDefinitionContext;

public class ScripthostTask2 implements CommonTaskType {
	private PlanManager planManager = null;
	private LabelManager labelManager = null;
	private ResultsSummaryManager resultsSummaryManager = null;

	@Override
	public TaskResult execute(CommonTaskContext taskContext) throws TaskException {
		final TaskResultBuilder builder = TaskResultBuilder.newBuilder(taskContext);
		final BuildLogger buildLogger = taskContext.getBuildLogger();

		final ConfigurationMap config = taskContext.getConfigurationMap();

		final String scriptLanguage = config.get(ScripthostTaskConfigurator.SCRIPTHOST_SCRIPTTYPE);
		final String script = config.get(ScripthostTaskConfigurator.SCRIPTHOST_SCRIPTBODY);
		final String scriptFile = config.get(ScripthostTaskConfigurator.SCRIPTHOST_SCRIPT);
		final String scriptLocation = config.get(ScripthostTaskConfigurator.SCRIPTHOST_SCRIPTLOCATION);
		DeploymentContext dc = null;
		
		
//		labelManager.addLabel("deploy", com.atlassian.bamboo.plan.PlanKeys.getPlanResultKey(taskContext.getDeploymentContext().getVariableContext().getEffectiveVariables().get("buildResultKey").getValue()),null);
		
		try {
			ScriptEngineManager factory = new ScriptEngineManager();
			ScriptEngine engine = factory.getEngineByName(scriptLanguage);
			if (engine == null) {
				buildLogger.addErrorLogEntry("Script engine " + scriptLanguage + " not found.");
				builder.failed();
			} else {
				engine.put("taskContext", taskContext);
				engine.put("builder", builder);
				engine.put("buildLogger", buildLogger);
				engine.put("planManager", planManager);
				engine.put("labelManager", labelManager);
				engine.put("resultsSummaryManager", resultsSummaryManager);

				if ("FILE".equals(scriptLocation)) {
					engine.eval(new FileReader(scriptFile));
				} else {
					engine.eval(script);
				}
			}
		} catch (Exception e) {
			buildLogger.addErrorLogEntry("Script Exception: " + e.getMessage());
			builder.failed();
		}
		return builder.build();
	}

	public PlanManager getPlanManager() {
		return planManager;
	}

	public void setPlanManager(PlanManager planManager) {
		this.planManager = planManager;
	}

	public LabelManager getLabelManager() {
		return labelManager;
	}

	public void setLabelManager(LabelManager labelManager) {
		this.labelManager = labelManager;
	}

	public ResultsSummaryManager getResultsSummaryManager() {
		return resultsSummaryManager;
	}

	public void setResultsSummaryManager(ResultsSummaryManager resultsSummaryManager) {
		this.resultsSummaryManager = resultsSummaryManager;
	}
}
