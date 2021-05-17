package com.solutioncube.task;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.pojo.TaskParameter;

public class TemperatureMeasurementsHistoryReportTask implements ITask{
	
	private static final Logger logger = LoggerFactory.getLogger(TemperatureMeasurementsHistoryReportTask.class);
	private final String BASE_COLLECTION_NAME = "TemperatureSensors";
	private final String COLLECTION_NAME = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/temperature/measurements/history?temperatureSensorId=%s&ts.since=%s&_limit=5000";

	@Override
	public void execute(TaskParameter taskParameter) {
		
		logger.info("Execution Started");
		List<String> temperatureSensors = taskParameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		logger.info("TemperatureSensors Size: "+temperatureSensors.size());
		for (String temperatureSensor : temperatureSensors) {
			taskParameter.generateToken();
			JSONObject temperatureSensorJSONObject = new JSONObject(temperatureSensor);
			String temperatureSensorId = temperatureSensorJSONObject.getString("_id");
			taskParameter.setId(temperatureSensorId);
			taskParameter.setIdColumnName("temperatureSensorId");
			taskParameter.setUri(String.format(URI, temperatureSensorId, taskParameter.getSinceDateAsString()));
			taskParameter.setCollectionName(COLLECTION_NAME);
			new Task().execute(taskParameter);
			taskParameter.setId(null);
			taskParameter.setIdColumnName(null);
		}		
		logger.info("Execution Done");
	}
}