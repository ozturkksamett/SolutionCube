package com.example.solutioncube.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.example.solutioncube.job.task.AlarmHistoryReportTask;
import com.example.solutioncube.job.task.EnergyConsumptionReportTask;
import com.example.solutioncube.job.task.EnergyMeasurementsHistoryReportTask;
import com.example.solutioncube.job.task.LatestEnergyMeasurementsReportTask;
import com.example.solutioncube.job.task.LatestPositionsReportTask;
import com.example.solutioncube.job.task.LatestSensorMeasurementReportTask;
import com.example.solutioncube.job.task.LatestTemperatureMeasurementsReportTask;
import com.example.solutioncube.job.task.OutageHistoryReportTask;
import com.example.solutioncube.job.task.PositionHistoryReportTask;
import com.example.solutioncube.job.task.SensorMeasurementHistoryReportTask;
import com.example.solutioncube.job.task.SensorMeasurementSummaryReportTask;
import com.example.solutioncube.job.task.TemperatureMeasurementsHistoryReportTask;
import com.example.solutioncube.job.task.TemperatureMeasurementsSummaryReportTask;
import com.example.solutioncube.job.task.ZonePresenceHistoryReportTask;
import com.example.solutioncube.job.task.ZonePresenceSummaryReportTask;

@Component
public class DailyJob extends QuartzJobBean {

	@Autowired
	JobParameter jobParameter;
	
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

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		
		System.out.println("daily job başladı");
		
		jobParameter.generateJobParameter();

		positionHistoryReportTask.executeDaily();
		latestPositionsReportTask.executeDaily();
		outageHistoryReportTask.executeDaily();

		latestEnergyMeasurementsReportTask.executeDaily();
		energyMeasurementsHistoryReportTask.executeDaily();
		energyConsumptionReportTask.executeDaily();
		latestTemperatureMeasurementsReportTask.executeDaily();
		temperatureMeasurementsHistoryReportTask.executeDaily();
		temperatureMeasurementsSummaryReportTask.executeDaily();
		zonePresenceHistoryReportTask.executeDaily();
		zonePresenceSummaryReportTask.executeDaily();
		alarmHistoryReportTask.executeDaily();
		
		latestSensorMeasurementReportTask.executeDaily();
		sensorMeasurementHistoryReportTask.executeDaily();
		sensorMeasurementSummaryReportTask.executeDaily();
		
		System.out.println("daily job bitti");
	}
}