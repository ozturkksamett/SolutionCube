package com.solutioncube.task;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.pojo.TaskParameter;

public class SensorCountHistoryReportTask implements ITask {
	
	private static final Logger logger = LoggerFactory.getLogger(SensorCountHistoryReportTask.class);
	private final String BASE_COLLECTION_NAME = "SensorCounters";
	private final String COLLECTION_NAME = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/sensor/count/history?sensorCounterId=%s&ts.since=%s&_limit=5000";

	@Override
	public void execute(TaskParameter taskParameter) {

		logger.info("Execution Started");
		List<String> sensorCounters = taskParameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		logger.info("SensorCounters Size: "+sensorCounters.size());
		for (String sensorCounter : sensorCounters) {
			taskParameter.generateToken();
			JSONObject sensorCounterJSONObject = new JSONObject(sensorCounter);
			String sensorCounterId = sensorCounterJSONObject.getString("_id");
			taskParameter.setId(sensorCounterId);
			taskParameter.setIdColumnName("sensorCounterId");
			taskParameter.setUri(String.format(URI, sensorCounterId, taskParameter.getSinceDateAsString()));
			taskParameter.setCollectionName(COLLECTION_NAME);
			new Task().execute(taskParameter);
			taskParameter.setId(null);
			taskParameter.setIdColumnName(null);
		}
		logger.info("Execution Done");
	}
}