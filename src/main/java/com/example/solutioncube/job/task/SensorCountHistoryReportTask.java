package com.example.solutioncube.job.task;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.solutioncube.common.Task;
import com.example.solutioncube.common.TaskParameter;

@Component
public class SensorCountHistoryReportTask {

	@Autowired
	Task task;

	private final String BASE_COLLECTION_NAME = "SensorCounters";
	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/sensor/count/history?sensorCounterId=%s&ts.since=%s";
  
	public void executeDaily(TaskParameter taskParameter) {
		
	List<String> sensorCounters = taskParameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		
		for (String sensorCounter : sensorCounters) {
			
			JSONObject sensorCounterJSONObject = new JSONObject(sensorCounter);
			String sensorCounterId = sensorCounterJSONObject.getString("_id");
			taskParameter.setId(sensorCounterId);
			taskParameter.setIdColumnName("sensorCounterId");
			taskParameter.setUri(String.format(URI, sensorCounterId, taskParameter.getSinceDateAsString()));
			taskParameter.setCollectionName(COLLECTION_NAME);
			task.execute(taskParameter);
			taskParameter.setId(null);
			taskParameter.setIdColumnName(null);
		}
	}
}
