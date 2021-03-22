package com.example.solutioncube.job.task;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.example.solutioncube.job.JobParameter;
import com.example.solutioncube.job.Task;

public class SensorCountSummaryReportTask {
	
	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	JobParameter jobParameter;
	
	@Autowired
	private Task task;

	private final String BASE_COLLECTION_NAME = "SensorCounters";
	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/sensor/count/summary?sensorCounterId=%s&ts.since=%s&ts.until=%s";
  
	public void executeDaily() {
		
	List<String> sensorCounters = mongoTemplate.findAll(String.class, BASE_COLLECTION_NAME);
		
		for (String sensorCounter : sensorCounters) {
			
			JSONObject sensorCounterJSONObject = new JSONObject(sensorCounter);
			String sensorCounterId = sensorCounterJSONObject.getString("_id");

			task.execute(String.format(URI, sensorCounterId, jobParameter.getSinceDate(), jobParameter.getTillDate()), COLLECTION_NAME);
		}
	}
}
