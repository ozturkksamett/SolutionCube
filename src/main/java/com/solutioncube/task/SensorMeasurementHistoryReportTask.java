package com.solutioncube.task;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.solutioncube.common.IProcess;
import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.helper.CacheManager;
import com.solutioncube.pojo.TaskParameter;

public class SensorMeasurementHistoryReportTask implements ITask, IProcess {

	private static final Logger logger = LoggerFactory.getLogger(SensorMeasurementHistoryReportTask.class);
	private final String BASE_COLLECTION_NAME = "Sensors";
	private final String COLLECTION_NAME = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/sensor/measurements/history?sensorId=%s&ts.since=%s&ts.until=%s&_limit=5000";

	@Override
	public void execute(TaskParameter taskParameter) {

		logger.info("Execution Started");
		List<String> sensors = taskParameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		logger.info("Sensors Size: "+sensors.size());
		for (String sensor : sensors) {

			JSONObject sensorJSONObject = new JSONObject(sensor);
			String sensorId = sensorJSONObject.getString("_id");
			taskParameter.setId(sensorId);
			taskParameter.setIdColumnName("sensorId");
			taskParameter.setUri(String.format(URI, sensorId, taskParameter.getSinceDateAsString(), taskParameter.getTillDateAsString()));
			taskParameter.setCollectionName(COLLECTION_NAME);
			new Task().execute(taskParameter);
			taskParameter.setId(null);
			taskParameter.setIdColumnName(null);
		}
		process(taskParameter.getMongoTemplate());
		logger.info("Execution Done");
	}

	@Override
	public void process(MongoTemplate mongoTemplate) {
		
		logger.info("Process Started");
		
		List<JSONObject> jsonObjects = CacheManager.get(COLLECTION_NAME);
		
		// Implement Algorithm
		for (int i = 0; i < jsonObjects.size(); i++) {
			
		}		
		
		logger.info("Process Done");
	}
}