package com.solutioncube.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solutioncube.common.IAsyncExecutableTaskFunc;
import com.solutioncube.common.ITask;

@Component
public class TaskExecutor {

	@Autowired
	private TaskParameterGenerator taskParameterGenerator;
	
	@Autowired
	private AsyncHelper asyncHelper;

	public Collection<Future<Boolean>> execTasks(List<ITask> tasks, int configIndex, boolean isAsync) {
		
		Collection<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();

		if(isAsync) {

			tasks.forEach(task -> {

				IAsyncExecutableTaskFunc taskFunc = (taskParameter) -> { task.execute(taskParameter); };
				futures.add(asyncHelper.execAsync(taskFunc, taskParameterGenerator.generateTaskParameter(configIndex)));
			});
		}
		else {
			
			tasks.forEach(task -> {

				task.execute(taskParameterGenerator.generateTaskParameter(configIndex));
			});		
		}
		
		return futures;
	}	
}