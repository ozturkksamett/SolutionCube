package com.example.solutioncube.job.task;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.solutioncube.common.Task;
import com.example.solutioncube.common.TaskParameter;

@Component
public class SensorMeasurementHistoryReportTask {

	@Autowired
	Task task;

	private final String BASE_COLLECTION_NAME = "Sensors";
	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/sensor/measurements/history?sensorId=%s&ts.since=%s&ts.until=%s";

	public void executeDaily(TaskParameter taskParameter) {

		List<String> sensors = taskParameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		
		for (String sensor : sensors) {

			JSONObject sensorJSONObject = new JSONObject(sensor);
			String sensorId = sensorJSONObject.getString("_id");
			taskParameter.setId(sensorId);
			taskParameter.setIdColumnName("sensorId");
			taskParameter.setUri(String.format(URI, sensorId, taskParameter.getSinceDateAsString(), taskParameter.getNowAsString()));
			taskParameter.setCollectionName(COLLECTION_NAME);
			task.execute(taskParameter);
			taskParameter.setId(null);
			taskParameter.setIdColumnName(null);
		}
	}

}
