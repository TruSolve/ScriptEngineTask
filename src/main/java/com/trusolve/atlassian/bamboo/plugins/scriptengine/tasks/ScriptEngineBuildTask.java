package com.trusolve.atlassian.bamboo.plugins.scriptengine.tasks;

import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskType;

public class ScriptEngineBuildTask extends ScriptEngineBaseTask implements TaskType
{

	@Override
	public TaskResult execute(TaskContext taskContext) throws TaskException
	{
		return super.execute(taskContext);
	}

}
