package com.example.solutioncube.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.solutioncube.common.TaskParameter;
import com.example.solutioncube.config.Config;
import com.example.solutioncube.job.task.AlarmHistoryReportTask;
import com.example.solutioncube.job.task.AlarmRulesTask;
import com.example.solutioncube.job.task.EnergyMeasurementsHistoryReportTask;
import com.example.solutioncube.job.task.EnergyMetersTask;
import com.example.solutioncube.job.task.SensorMeasurementHistoryReportTask;
import com.example.solutioncube.job.task.SensorsTask;

@Service
public class VanucciService {
	
	@Autowired
	SensorsTask sensorsTask;
	
	@Autowired
	AlarmRulesTask alarmRulesTask;
	
	@Autowired
	EnergyMetersTask energyMetersTask;

	@Autowired
	EnergyMeasurementsHistoryReportTask energyMeasurementsHistoryReportTask;

	@Autowired
	AlarmHistoryReportTask alarmHistoryReportTask;

	@Autowired
	SensorMeasurementHistoryReportTask sensorMeasurementHistoryReportTask;

	@Autowired
	private Config config;
	
	public void oneTimeRun() {
		TaskParameter taskParameter = new TaskParameter(config, 1); 
		sensorsTask.executeOneTime(taskParameter);
		alarmRulesTask.executeOneTime(taskParameter);
		energyMetersTask.executeOneTime(taskParameter);
	}

    @Async
	public void run() {
    	TaskParameter taskParameter = new TaskParameter(config, 1); 
		energyMeasurementsHistoryReportTask.executeDaily(taskParameter);
		alarmHistoryReportTask.executeDaily(taskParameter);		
		sensorMeasurementHistoryReportTask.executeDaily(taskParameter);	
	}
}