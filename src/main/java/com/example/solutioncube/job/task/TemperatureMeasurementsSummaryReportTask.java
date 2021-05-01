package com.example.solutioncube.job.task;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.solutioncube.common.Task;
import com.example.solutioncube.common.TaskParameter;

@Component
public class TemperatureMeasurementsSummaryReportTask {

	@Autowired
	Task task;

	private final String BASE_COLLECTION_NAME = "TemperatureSensors";
	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/temperature/measurements/summary?temperatureSensorId=%s&ts.since=%s&ts.until=%s";

	public void executeDaily(TaskParameter taskParameter) {
		
		List<String> temperatureSensors = taskParameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		
		for (String temperatureSensor : temperatureSensors) {
			
			JSONObject temperatureSensorJSONObject = new JSONObject(temperatureSensor);
			String temperatureSensorId = temperatureSensorJSONObject.getString("_id");
			taskParameter.setUri(String.format(URI, temperatureSensorId, taskParameter.getSinceDateAsString(), taskParameter.getNowAsString()));
			taskParameter.setCollectionName(COLLECTION_NAME);
			task.execute(taskParameter);
		}		
	}
}
