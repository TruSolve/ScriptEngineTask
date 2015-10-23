package com.trusolve.atlassian.bamboo.plugins.scripthost.tasks;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.task.TaskType;

public class ScripthostTask2 implements TaskType
{

	@Override
	public TaskResult execute(TaskContext taskContext) throws TaskException
	{
		final TaskResultBuilder builder = TaskResultBuilder.newBuilder(taskContext);
        final BuildLogger buildLogger = taskContext.getBuildLogger();

        final ConfigurationMap config = taskContext.getConfigurationMap();
        
		return builder.build();
	}

}
