package com.trusolve.atlassian.bamboo.plugins.scriptengine.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.bamboo.deployments.execution.DeploymentTaskContext;
import com.atlassian.bamboo.deployments.execution.DeploymentTaskType;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;

public class ScriptEngineDeployTask extends ScriptEngineBaseTask implements DeploymentTaskType
{
	private static final Logger log = LoggerFactory.getLogger(ScriptEngineDeployTask.class);
	
	@Override
	public TaskResult execute(DeploymentTaskContext taskContext) throws TaskException
	{
		return super.execute(taskContext);
	}
}
