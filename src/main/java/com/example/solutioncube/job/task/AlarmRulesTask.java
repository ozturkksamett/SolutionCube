package com.example.solutioncube.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.solutioncube.job.JobParameter;
import com.example.solutioncube.job.Task;


@Component
public class AlarmRulesTask {
	

	@Autowired
	JobParameter jobParameter;
	
	@Autowired
	private Task task;

	private final String COLLECTION_NAME 		= this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI_SENSOR_ALARM 		= "https://api.triomobil.com/facility/v1/alarmRules?%s_sortOrder=ASC&_sortBy=label&model=sensor&state=ALARM";
	private final String URI_SENSOR_OFFLINE 	= "https://api.triomobil.com/facility/v1/alarmRules?%s_sortOrder=ASC&_sortBy=label&model=sensor&state=OFFLINE";
	private final String URI_SENSOR_OK 			= "https://api.triomobil.com/facility/v1/alarmRules?%s_sortOrder=ASC&_sortBy=label&model=sensor&state=OK";
	private final String URI_TEMPERATURE_ALARM 	= "https://api.triomobil.com/facility/v1/alarmRules?%s_sortOrder=ASC&_sortBy=label&model=temperatureSensor&state=ALARM";
	private final String URI_TEMPERATURE_OFFLINE = "https://api.triomobil.com/facility/v1/alarmRules?%s_sortOrder=ASC&_sortBy=label&model=temperatureSensor&state=OFFLINE";
	private final String URI_TEMPERATURE_OK 	= "https://api.triomobil.com/facility/v1/alarmRules?%s_sortOrder=ASC&_sortBy=label&model=temperatureSensor&state=OK";
	private final String URI_ENERGYMETER_ALARM 	= "https://api.triomobil.com/facility/v1/alarmRules?%s_sortOrder=ASC&_sortBy=label&model=energyMeter&state=ALARM";
	private final String URI_ENERGYMETER_OFFLINE = "https://api.triomobil.com/facility/v1/alarmRules?%s_sortOrder=ASC&_sortBy=label&model=energyMeter&state=OFFLINE";
	private final String URI_ENERGYMETER_OK 	= "https://api.triomobil.com/facility/v1/alarmRules?%s_sortOrder=ASC&_sortBy=label&model=energyMeter&state=OK";
	private final String URI_TRACKER_ALARM 		= "https://api.triomobil.com/facility/v1/alarmRules?%s_sortOrder=ASC&_sortBy=label&model=tracker&state=ALARM";
	private final String URI_TRACKER_OFFLINE 	= "https://api.triomobil.com/facility/v1/alarmRules?%s_sortOrder=ASC&_sortBy=label&model=tracker&state=OFFLINE";
	private final String URI_TRACKER_OK  		= "https://api.triomobil.com/facility/v1/alarmRules?%s_sortOrder=ASC&_sortBy=label&model=tracker&state=OK";

	public void executeDaily() {
		
		task.execute(String.format(URI_SENSOR_ALARM, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
		task.execute(String.format(URI_SENSOR_OFFLINE, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
		task.execute(String.format(URI_SENSOR_OK, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
		task.execute(String.format(URI_TEMPERATURE_ALARM, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
		task.execute(String.format(URI_TEMPERATURE_OFFLINE, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
		task.execute(String.format(URI_TEMPERATURE_OK, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
		task.execute(String.format(URI_ENERGYMETER_ALARM, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
		task.execute(String.format(URI_ENERGYMETER_OFFLINE, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
		task.execute(String.format(URI_ENERGYMETER_OK, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
		task.execute(String.format(URI_TRACKER_ALARM, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
		task.execute(String.format(URI_TRACKER_OFFLINE, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
		task.execute(String.format(URI_TRACKER_OK, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
	}

	public void executeOneTime() {
		
		task.execute(String.format(URI_SENSOR_ALARM, ""), COLLECTION_NAME);
		task.execute(String.format(URI_SENSOR_OFFLINE, ""), COLLECTION_NAME);
		task.execute(String.format(URI_SENSOR_OK, ""), COLLECTION_NAME);
		task.execute(String.format(URI_TEMPERATURE_ALARM, ""), COLLECTION_NAME);
		task.execute(String.format(URI_TEMPERATURE_OFFLINE, ""), COLLECTION_NAME);
		task.execute(String.format(URI_TEMPERATURE_OK, ""), COLLECTION_NAME);
		task.execute(String.format(URI_ENERGYMETER_ALARM, ""), COLLECTION_NAME);
		task.execute(String.format(URI_ENERGYMETER_OFFLINE, ""), COLLECTION_NAME);
		task.execute(String.format(URI_ENERGYMETER_OK, ""), COLLECTION_NAME);
		task.execute(String.format(URI_TRACKER_ALARM, ""), COLLECTION_NAME);
		task.execute(String.format(URI_TRACKER_OFFLINE, ""), COLLECTION_NAME);
		task.execute(String.format(URI_TRACKER_OK, ""), COLLECTION_NAME);
	}

}
