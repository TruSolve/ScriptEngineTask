package com.trusolve.atlassian.bamboo.plugins.scriptengine.tasks;

import com.atlassian.bamboo.deployments.execution.DeploymentTaskContext;
import com.atlassian.bamboo.deployments.execution.DeploymentTaskType;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;

public class ScriptEngineDeployTask extends ScriptEngineBaseTask implements DeploymentTaskType
{
	@Override
	public TaskResult execute(DeploymentTaskContext taskContext) throws TaskException
	{
		return super.execute(taskContext);
	}
}
