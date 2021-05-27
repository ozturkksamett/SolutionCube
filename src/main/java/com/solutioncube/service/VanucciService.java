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
import com.solutioncube.common.ServiceRunType;
import com.solutioncube.common.TaskExecutor;
import com.solutioncube.task.AlarmHistoryReportTask;
import com.solutioncube.task.AlarmRulesTask;
import com.solutioncube.task.EnergyMeasurementsHistoryReportTask;
import com.solutioncube.task.EnergyMetersTask;
import com.solutioncube.task.SensorMeasurementHistoryReportTask;
import com.solutioncube.task.SensorsTask;

@Service
@Qualifier("vanucciService")
public class VanucciService implements IService {
	
	private static final int CONFIG_INDEX = 1;
	
	private static final List<ITask> STATIC_TASKS = Arrays.asList(new ITask[] {

			new SensorsTask()
			,new AlarmRulesTask()
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
	public void run(ServiceRunType serviceRunType) {

		switch (serviceRunType) {
		case STATIC :			
			taskExecutor.execTasks(STATIC_TASKS, CONFIG_INDEX);
			break;
		case DAILY :			
			taskExecutor.execTasks(DAILY_TASKS, CONFIG_INDEX);
			break;
		}
	}
	
	@Override
	public Collection<Future<Boolean>> runAsync(ServiceRunType serviceRunType) {

		Collection<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
		
		switch (serviceRunType) {
		case STATIC :			
			futures = taskExecutor.execTasksAsync(STATIC_TASKS, CONFIG_INDEX);
			break;
		case DAILY :			
			futures = taskExecutor.execTasksAsync(DAILY_TASKS, CONFIG_INDEX);
			break;
		}
		
		return futures;
	}
}