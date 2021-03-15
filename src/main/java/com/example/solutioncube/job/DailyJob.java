package com.example.solutioncube.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.example.solutioncube.job.task.AlarmHistoryReportTask;
import com.example.solutioncube.job.task.AlarmRulesTask;
import com.example.solutioncube.job.task.EnergyConsumptionReportTask;
import com.example.solutioncube.job.task.EnergyMeasurementsHistoryReportTask;
import com.example.solutioncube.job.task.EnergyMetersTask;
import com.example.solutioncube.job.task.FloorPlansTask;
import com.example.solutioncube.job.task.LatestEnergyMeasurementsReportTask;
import com.example.solutioncube.job.task.LatestTemperatureMeasurementsReportTask;
import com.example.solutioncube.job.task.SensorCountersTask;
import com.example.solutioncube.job.task.SensorsTask;
import com.example.solutioncube.job.task.TemperatureMeasurementsHistoryReportTask;
import com.example.solutioncube.job.task.TemperatureMeasurementsSummaryReportTask;
import com.example.solutioncube.job.task.TemperatureSensorsTask;
import com.example.solutioncube.job.task.TrackersTask;
import com.example.solutioncube.job.task.ZonePresenceHistoryReportTask;
import com.example.solutioncube.job.task.ZonePresenceSummaryReportTask;
import com.example.solutioncube.job.task.ZonesTask;

@Component
public class DailyJob extends QuartzJobBean  {
	
	@Autowired
	JobParameter jobParameter;

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
	

	@Override
	protected void executeInternal (JobExecutionContext jobExecutionContext) throws JobExecutionException {

		jobParameter.generateJobParameter();
		
		/*sensorsTask.executeDaily();
		alarmRulesTask.executeDaily();
		sensorCountersTask.executeDaily();
		trackersTask.executeDaily();
		floorPlansTask.executeDaily();
		zonesTask.executeDaily();
		energyMetersTask.executeDaily();
		temperatureSensorsTask.executeDaily();*/
		
		latestEnergyMeasurementsReportTask.executeDaily();
		energyMeasurementsHistoryReportTask.executeDaily();
		energyConsumptionReportTask.executeDaily();
		latestTemperatureMeasurementsReportTask.executeDaily();
		temperatureMeasurementsHistoryReportTask.executeDaily();
		temperatureMeasurementsSummaryReportTask.executeDaily();
		zonePresenceHistoryReportTask.executeDaily();
		zonePresenceSummaryReportTask.executeDaily();
		alarmHistoryReportTask.executeDaily();
		
	}
}