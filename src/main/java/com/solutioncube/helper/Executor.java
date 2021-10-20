package com.solutioncube.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solutioncube.common.IAsyncExecutableTaskFunc;
import com.solutioncube.common.IProcess;
import com.solutioncube.common.ITask;

@Component
public class Executor {

	@Autowired
	private ParameterGenerator parameterGenerator;

	@Autowired
	private AsyncHelper asyncHelper;

	public Collection<Future<Boolean>> execTasks(List<ITask> tasks, int configIndex, boolean isAsync) {

		Collection<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();

		if (isAsync) {

			tasks.forEach(task -> {

				IAsyncExecutableTaskFunc taskFunc = (taskParameter) -> {
					task.execute(taskParameter);
				};
				futures.add(asyncHelper.execAsync(taskFunc, parameterGenerator.generateTaskParameter(configIndex)));
			});
		} else {

			tasks.forEach(task -> {

				task.execute(parameterGenerator.generateTaskParameter(configIndex));
			});
		}

		return futures;
	}

	public Collection<Future<Boolean>> execTask(ITask task, int configIndex, boolean isAsync) {

		Collection<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();

		if (isAsync) {

			IAsyncExecutableTaskFunc taskFunc = (taskParameter) -> {
				task.execute(taskParameter);
			};
			futures.add(asyncHelper.execAsync(taskFunc, parameterGenerator.generateTaskParameter(configIndex)));
		} else {

			task.execute(parameterGenerator.generateTaskParameter(configIndex));
		}

		return futures;
	}
	
	public Collection<Future<Boolean>> execProcesses(List<IProcess> processes, int configIndex, boolean isAsync) {

		Collection<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();

		if (isAsync) {

			processes.forEach(process -> {

				IAsyncExecutableTaskFunc processFunc = (taskParameter) -> {
					process.process(taskParameter);
				};
				futures.add(asyncHelper.execAsync(processFunc, parameterGenerator.generateTaskParameter(configIndex)));
			});
		} else {

			processes.forEach(process -> {

				process.process(parameterGenerator.generateTaskParameter(configIndex));
			});
		}

		return futures;
	}

	public Collection<Future<Boolean>> execProcess(IProcess process, int configIndex, boolean isAsync) {

		Collection<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();

		if (isAsync) {

			IAsyncExecutableTaskFunc processFunc = (taskParameter) -> {
				process.process(taskParameter);
			};
			futures.add(asyncHelper.execAsync(processFunc, parameterGenerator.generateTaskParameter(configIndex)));
		} else {

			process.process(parameterGenerator.generateTaskParameter(configIndex));
		}

		return futures;
	}
}