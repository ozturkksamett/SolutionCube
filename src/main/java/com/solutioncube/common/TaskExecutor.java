package com.solutioncube.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solutioncube.helper.AsyncHelper;
import com.solutioncube.pojo.TaskParameter;

@Component
public class TaskExecutor {

	@Autowired
	private TaskParameterGenerator taskParameterGenerator;
	
	@Autowired
	private AsyncHelper asyncHelper;

	public Collection<Future<Boolean>> execTasksAsync(List<ITask> tasks, int configIndex) {
		
		Collection<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
		
		tasks.forEach(task -> {

			IAsyncExecutableTaskFunc taskFunc = (taskParameter) -> { task.execute(taskParameter); };
			TaskParameter taskParameter = taskParameterGenerator.generateTaskParameter(configIndex);
			futures.add(asyncHelper.execAsync(taskFunc, taskParameter));
		});
		
		return futures;
	}	
}