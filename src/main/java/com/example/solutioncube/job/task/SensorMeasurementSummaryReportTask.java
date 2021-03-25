package com.example.solutioncube.job.task;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.example.solutioncube.job.JobParameter;
import com.example.solutioncube.job.Task;

@Component
public class SensorMeasurementSummaryReportTask {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	JobParameter jobParameter;

	@Autowired
	private Task task;

	private final String BASE_COLLECTION_NAME = "Sensors";
	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/sensor/measurements/summary?_groupBy=hour&_sortOrder=ASC&sensorId=%s&ts.since=%s&ts.until=%s";

	public void executeDaily() {

		List<String> sensors = mongoTemplate.findAll(String.class, BASE_COLLECTION_NAME);

		for (String sensor : sensors) {

			JSONObject sensorJSONObject = new JSONObject(sensor);
			String sensorId = sensorJSONObject.getString("_id");
			jobParameter.setId(sensorId);
			task.execute(String.format(URI, sensorId, jobParameter.getSinceDate(), jobParameter.getTillDate()), COLLECTION_NAME);
			jobParameter.setId(null);
		}
	}
}
