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

import com.atlassian.bamboo.build.BuildDefinition;
import com.atlassian.bamboo.build.CustomBuildProcessorServer;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.variable.VariableContext;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;

public class ScriptEngineBuildProcessorServer
	extends ScriptEngineCore
	implements CustomBuildProcessorServer
{
	private BuildContext buildContext = null;

	@Override
	public BuildContext call() throws InterruptedException, Exception
	{
		// this.executeScript(script, scriptLanguage, scriptContext, isFile);
		return null;
	}

	@Override
	public void init(BuildContext buildContext)
	{
		this.buildContext = buildContext;
		BuildDefinition buildDefinition = buildContext.getBuildDefinition();
		
		VariableContext variableContext = buildContext.getVariableContext();
		
//		planKey = variableContext.getEffectiveVariables().get("planKey").getValue();
//		planResultKey = PlanKeys.getPlanResultKey(variableContext.getEffectiveVariables().get("buildResultKey").getValue());
	}

}
