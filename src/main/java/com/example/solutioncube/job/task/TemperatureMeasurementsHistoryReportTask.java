package com.example.solutioncube.job.task;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.example.solutioncube.job.JobParameter;
import com.example.solutioncube.job.Task;

@Component
public class TemperatureMeasurementsHistoryReportTask {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	JobParameter jobParameter;
	
	@Autowired
	private Task task;

	private final String BASE_COLLECTION_NAME = "TemperatureSensors";
	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/temperature/measurements/history?_sortOrder=ASC&temperatureSensorId=%s&ts.since=%s";

	public void executeDaily() {
		
		List<String> temperatureSensors = mongoTemplate.findAll(String.class, BASE_COLLECTION_NAME);
		
		for (String temperatureSensor : temperatureSensors) {
			
			JSONObject temperatureSensorJSONObject = new JSONObject(temperatureSensor);
			String temperatureSensorId = temperatureSensorJSONObject.getString("_id");

			task.execute(String.format(URI, temperatureSensorId, jobParameter.getSinceDate()), COLLECTION_NAME);
		}		
	}
}
