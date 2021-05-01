package com.example.solutioncube.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.solutioncube.common.TaskParameter;
import com.example.solutioncube.config.Config;
import com.example.solutioncube.job.task.AlarmHistoryReportTask;
import com.example.solutioncube.job.task.AlarmRulesTask;
import com.example.solutioncube.job.task.EnergyConsumptionReportTask;
import com.example.solutioncube.job.task.EnergyMeasurementsHistoryReportTask;
import com.example.solutioncube.job.task.EnergyMetersTask;
import com.example.solutioncube.job.task.FloorPlansTask;
import com.example.solutioncube.job.task.LatestEnergyMeasurementsReportTask;
import com.example.solutioncube.job.task.LatestPositionsReportTask;
import com.example.solutioncube.job.task.LatestSensorMeasurementReportTask;
import com.example.solutioncube.job.task.LatestTemperatureMeasurementsReportTask;
import com.example.solutioncube.job.task.OutageHistoryReportTask;
import com.example.solutioncube.job.task.PositionHistoryReportTask;
import com.example.solutioncube.job.task.SensorCountHistoryReportTask;
import com.example.solutioncube.job.task.SensorCountLatestReportTask;
import com.example.solutioncube.job.task.SensorCountSummaryReportTask;
import com.example.solutioncube.job.task.SensorCountersTask;
import com.example.solutioncube.job.task.SensorMeasurementHistoryReportTask;
import com.example.solutioncube.job.task.SensorMeasurementSummaryReportTask;
import com.example.solutioncube.job.task.SensorsTask;
import com.example.solutioncube.job.task.TemperatureMeasurementsHistoryReportTask;
import com.example.solutioncube.job.task.TemperatureMeasurementsSummaryReportTask;
import com.example.solutioncube.job.task.TemperatureSensorsTask;
import com.example.solutioncube.job.task.TrackersTask;
import com.example.solutioncube.job.task.ZonePresenceHistoryReportTask;
import com.example.solutioncube.job.task.ZonePresenceSummaryReportTask;
import com.example.solutioncube.job.task.ZonesTask;

@Service
public class ErisyemService {
	
	@Autowired
	SensorsTask sensorsTask;
	
	@Autowired
	AlarmRulesTask alarmRulesTask;

	@Autowired
	SensorCountersTask sensorCountersTask;
	
	@Autowired
	TrackersTask trackersTask;
	
	@Autowired
	FloorPlansTask floorPlansTask;
	
	@Autowired
	ZonesTask zonesTask;
	
	@Autowired
	EnergyMetersTask energyMetersTask;
	
	@Autowired
	TemperatureSensorsTask temperatureSensorsTask;
	
	@Autowired
	LatestEnergyMeasurementsReportTask latestEnergyMeasurementsReportTask;

	@Autowired
	EnergyMeasurementsHistoryReportTask energyMeasurementsHistoryReportTask;

	@Autowired
	EnergyConsumptionReportTask energyConsumptionReportTask;

	@Autowired
	LatestTemperatureMeasurementsReportTask latestTemperatureMeasurementsReportTask;

	@Autowired
	TemperatureMeasurementsHistoryReportTask temperatureMeasurementsHistoryReportTask;

	@Autowired
	TemperatureMeasurementsSummaryReportTask temperatureMeasurementsSummaryReportTask;

	@Autowired
	ZonePresenceHistoryReportTask zonePresenceHistoryReportTask;

	@Autowired
	ZonePresenceSummaryReportTask zonePresenceSummaryReportTask;

	@Autowired
	AlarmHistoryReportTask alarmHistoryReportTask;

	@Autowired
	LatestPositionsReportTask latestPositionsReportTask;

	@Autowired
	PositionHistoryReportTask positionHistoryReportTask;

	@Autowired
	OutageHistoryReportTask outageHistoryReportTask;
	
	@Autowired
	LatestSensorMeasurementReportTask latestSensorMeasurementReportTask;
	
	@Autowired
	SensorMeasurementHistoryReportTask sensorMeasurementHistoryReportTask;
	
	@Autowired
	SensorMeasurementSummaryReportTask sensorMeasurementSummaryReportTask;

	@Autowired
	SensorCountHistoryReportTask sensorCountHistoryReportTask;
	
	@Autowired
	SensorCountLatestReportTask sensorCountLatestReportTask;

	@Autowired
	SensorCountSummaryReportTask sensorCountSummaryReportTask;

	@Autowired
	private Config config;
	
	public void oneTimeRun() {
		TaskParameter taskParameter = new TaskParameter(config, 0);	
		sensorsTask.executeOneTime(taskParameter);
		alarmRulesTask.executeOneTime(taskParameter);
		sensorCountersTask.executeOneTime(taskParameter);
		trackersTask.executeOneTime(taskParameter);
		floorPlansTask.executeOneTime(taskParameter);
		zonesTask.executeOneTime(taskParameter);
		energyMetersTask.executeOneTime(taskParameter);
		temperatureSensorsTask.executeOneTime(taskParameter);	
	}

    @Async
	public void run() {
    	TaskParameter taskParameter = new TaskParameter(config, 0);
		positionHistoryReportTask.executeDaily(taskParameter);
		latestPositionsReportTask.executeDaily(taskParameter);
		outageHistoryReportTask.executeDaily(taskParameter);
		latestEnergyMeasurementsReportTask.executeDaily(taskParameter);
		energyMeasurementsHistoryReportTask.executeDaily(taskParameter);
		energyConsumptionReportTask.executeDaily(taskParameter);
		latestTemperatureMeasurementsReportTask.executeDaily(taskParameter);
		temperatureMeasurementsHistoryReportTask.executeDaily(taskParameter);
		temperatureMeasurementsSummaryReportTask.executeDaily(taskParameter);
		zonePresenceHistoryReportTask.executeDaily(taskParameter);
		zonePresenceSummaryReportTask.executeDaily(taskParameter);
		alarmHistoryReportTask.executeDaily(taskParameter);		
		latestSensorMeasurementReportTask.executeDaily(taskParameter);
		sensorMeasurementHistoryReportTask.executeDaily(taskParameter);
		sensorMeasurementSummaryReportTask.executeDaily(taskParameter);
		sensorCountHistoryReportTask.executeDaily(taskParameter);
		sensorCountLatestReportTask.executeDaily(taskParameter);
		sensorCountSummaryReportTask.executeDaily(taskParameter);	
	}
}