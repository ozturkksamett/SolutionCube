package com.solutioncube.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.solutioncube.common.IService;
import com.solutioncube.common.ITask;
import com.solutioncube.common.TaskType;
import com.solutioncube.helper.TaskExecutor;
import com.solutioncube.task.AlarmHistoryReportTask;
import com.solutioncube.task.AlarmRulesTask;
import com.solutioncube.task.EnergyMeasurementsHistoryReportTask;
import com.solutioncube.task.EnergyMetersTask;
import com.solutioncube.task.SensorMeasurementHistoryReportTask;
import com.solutioncube.task.SensorsTask;

@Service
@Qualifier("vanucciService")
public class VanucciService implements IService {

	private static final String SERVICE_NAME = "Vanucci";
	
	private static final int CONFIG_INDEX = 1;
	
	private static final List<ITask> STATIC_TASKS = Arrays.asList(new ITask[] {

			new SensorsTask()
			//,new AlarmRulesTask()
			,new EnergyMetersTask()
	});

	private static final List<ITask> DAILY_TASKS = Arrays.asList(new ITask[] {

			new EnergyMeasurementsHistoryReportTask()
			,new AlarmHistoryReportTask()
			,new SensorMeasurementHistoryReportTask()
	});
	
	@Autowired
	private TaskExecutor taskExecutor;	
	
	@Override
	public Collection<Future<Boolean>> run(TaskType taskType, boolean isAsync) {

		Collection<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
		
		switch (taskType) {
		case TASKS_WHICH_STATIC :			
			futures = taskExecutor.execTasks(STATIC_TASKS, CONFIG_INDEX, isAsync);
			break;
		case TASKS_WHICH_DAILY :			
			futures = taskExecutor.execTasks(DAILY_TASKS, CONFIG_INDEX, isAsync);
			break;
		case TASKS_WHICH_ONLY_WITH_SINCE_PARAM:
			break;
		case TASKS_WHICH_WITH_BOTH_SINCE_AND_TILL_PARAM:
			break;
		}
		
		return futures;
	}

	@Override
	public String getServiceName() {

		return SERVICE_NAME;
	}
}