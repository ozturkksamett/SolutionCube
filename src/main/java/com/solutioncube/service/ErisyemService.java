package com.solutioncube.service;

import java.time.LocalDate;
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
import com.solutioncube.helper.AsyncHelper;
import com.solutioncube.helper.TaskExecutor;
import com.solutioncube.helper.TaskParameterGenerator;
import com.solutioncube.task.AlarmHistoryReportTask;
import com.solutioncube.task.AlarmRulesTask;
import com.solutioncube.task.EnergyConsumptionReportTask;
import com.solutioncube.task.EnergyMeasurementsHistoryReportTask;
import com.solutioncube.task.EnergyMetersTask;
import com.solutioncube.task.FloorPlansTask;
import com.solutioncube.task.LatestEnergyMeasurementsReportTask;
import com.solutioncube.task.LatestPositionsReportTask;
import com.solutioncube.task.LatestSensorMeasurementReportTask;
import com.solutioncube.task.LatestTemperatureMeasurementsReportTask;
import com.solutioncube.task.OutageHistoryReportTask;
import com.solutioncube.task.PositionHistoryReportTask;
import com.solutioncube.task.SensorCountHistoryReportTask;
import com.solutioncube.task.SensorCountLatestReportTask;
import com.solutioncube.task.SensorCountSummaryReportTask;
import com.solutioncube.task.SensorCountersTask;
import com.solutioncube.task.SensorMeasurementHistoryReportTask;
import com.solutioncube.task.SensorMeasurementSummaryReportTask;
import com.solutioncube.task.SensorsTask;
import com.solutioncube.task.TemperatureMeasurementsHistoryReportTask;
import com.solutioncube.task.TemperatureMeasurementsSummaryReportTask;
import com.solutioncube.task.TemperatureSensorsTask;
import com.solutioncube.task.TrackersTask;
import com.solutioncube.task.ZonePresenceHistoryReportTask;
import com.solutioncube.task.ZonePresenceSummaryReportTask;
import com.solutioncube.task.ZonesTask;

@Service
@Qualifier("erisyemService")
public class ErisyemService implements IService {
	
	private static final String SERVICE_NAME = "Erişyem";
	
	private static final int CONFIG_INDEX = 0;

	private static final List<ITask> STATIC_TASKS = Arrays.asList(new ITask[] {

			new SensorsTask()
			,new AlarmRulesTask()
			,new SensorCountersTask()
			,new TrackersTask()
			,new FloorPlansTask()
			,new ZonesTask()
			,new EnergyMetersTask()
			,new TemperatureSensorsTask()
	});

	private static final List<ITask> DAILY_TASKS = Arrays.asList(new ITask[] {

//			new PositionHistoryReportTask()
//			,new LatestPositionsReportTask()
//			,new OutageHistoryReportTask()
//			,new LatestEnergyMeasurementsReportTask()
//			,new EnergyMeasurementsHistoryReportTask()
//			,new EnergyConsumptionReportTask()
//			,new LatestTemperatureMeasurementsReportTask()
//			,new TemperatureMeasurementsHistoryReportTask()
//			,new TemperatureMeasurementsSummaryReportTask()
			new ZonePresenceHistoryReportTask()
//			,new ZonePresenceSummaryReportTask()
//			,new AlarmHistoryReportTask()
//			,new LatestSensorMeasurementReportTask()
//			,new SensorMeasurementHistoryReportTask()
//			,new SensorMeasurementSummaryReportTask()
//			,new SensorCountHistoryReportTask()
//			,new SensorCountLatestReportTask()
//			,new SensorCountSummaryReportTask()
	});
	
	private static final List<ITask> TASKS_WHICH_ONLY_WITH_SINCE_PARAM = Arrays.asList(new ITask[] {

			new PositionHistoryReportTask()					
			,new EnergyMeasurementsHistoryReportTask()			
			,new TemperatureMeasurementsHistoryReportTask()			
			,new SensorCountHistoryReportTask()
	});

	private static final List<ITask> TASKS_WHICH_WITH_BOTH_SINCE_AND_TILL_PARAM = Arrays.asList(new ITask[] {

			new OutageHistoryReportTask()
			,new EnergyConsumptionReportTask()
			,new TemperatureMeasurementsSummaryReportTask()
			,new ZonePresenceHistoryReportTask()
			,new ZonePresenceSummaryReportTask()
			,new AlarmHistoryReportTask()
			,new SensorMeasurementHistoryReportTask()
			,new SensorMeasurementSummaryReportTask()
			,new SensorCountSummaryReportTask()
	});
	
	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private AsyncHelper asyncHelper;

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
	    	TaskParameterGenerator.isBulkData = true;
			futures = taskExecutor.execTasks(TASKS_WHICH_ONLY_WITH_SINCE_PARAM, CONFIG_INDEX, isAsync);
	    	TaskParameterGenerator.isBulkData = false;
			break;
		case TASKS_WHICH_WITH_BOTH_SINCE_AND_TILL_PARAM:
	    	TaskParameterGenerator.isBulkData = true;
	    	while (TaskParameterGenerator.getInitialDate().isBefore(LocalDate.now())) {
	    		
	    		asyncHelper.waitTillEndOfSynchronizedFunc(taskExecutor.execTasks(TASKS_WHICH_WITH_BOTH_SINCE_AND_TILL_PARAM, CONFIG_INDEX, isAsync)); 		
	    		TaskParameterGenerator.setInitialDate(TaskParameterGenerator.getInitialDate().plusDays(TaskParameterGenerator.getIntervalDay()));    		
	    	}
	    	TaskParameterGenerator.isBulkData = false;
			break;
		}
		
		return futures;
	}

	@Override
	public String getServiceName() {

		return SERVICE_NAME;
	}
}