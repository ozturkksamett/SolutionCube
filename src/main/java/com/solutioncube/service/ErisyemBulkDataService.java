package com.solutioncube.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.solutioncube.common.ITask;
import com.solutioncube.common.TaskExecutor;
import com.solutioncube.common.TaskParameterGenerator;
import com.solutioncube.task.AlarmHistoryReportTask;
import com.solutioncube.task.EnergyConsumptionReportTask;
import com.solutioncube.task.EnergyMeasurementsHistoryReportTask;
import com.solutioncube.task.OutageHistoryReportTask;
import com.solutioncube.task.PositionHistoryReportTask;
import com.solutioncube.task.SensorCountHistoryReportTask;
import com.solutioncube.task.SensorCountSummaryReportTask;
import com.solutioncube.task.SensorMeasurementHistoryReportTask;
import com.solutioncube.task.SensorMeasurementSummaryReportTask;
import com.solutioncube.task.TemperatureMeasurementsHistoryReportTask;
import com.solutioncube.task.TemperatureMeasurementsSummaryReportTask;
import com.solutioncube.task.ZonePresenceHistoryReportTask;
import com.solutioncube.task.ZonePresenceSummaryReportTask;

@Service
public class ErisyemBulkDataService {
	
	private static final Logger logger = LoggerFactory.getLogger(ErisyemBulkDataService.class);
	
	private static final int CONFIG_INDEX = 0;

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
	
	@Async
    public void runBulkData() {
    	
    	logger.info("Erisyem Bulk Data Service started running..");
    	Instant start = Instant.now();
    	TaskParameterGenerator.isBulkData = true;
    	taskExecutor.execTasks(TASKS_WHICH_ONLY_WITH_SINCE_PARAM, CONFIG_INDEX);
    	while (TaskParameterGenerator.getInitialDate().isBefore(LocalDate.now())) {
    		
    		logger.info("Initial Date: " + TaskParameterGenerator.getInitialDate());    		
    		taskExecutor.execTasks(TASKS_WHICH_WITH_BOTH_SINCE_AND_TILL_PARAM, CONFIG_INDEX); 		
    		TaskParameterGenerator.setInitialDate(TaskParameterGenerator.getInitialDate().plusDays(TaskParameterGenerator.getIntervalDay()));    		
    	}
    	TaskParameterGenerator.isBulkData = false;
    	Instant finish = Instant.now();
    	logger.info("Erisyem Bulk Data Service finished running. Duration: " + Duration.between(start, finish).toMinutes() + " minutes.");    	
	}        
}