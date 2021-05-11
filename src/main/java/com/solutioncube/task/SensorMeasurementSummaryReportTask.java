package com.solutioncube.task;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.pojo.TaskParameter;

public class SensorMeasurementSummaryReportTask implements ITask {

	private static final Logger logger = LoggerFactory.getLogger(SensorMeasurementSummaryReportTask.class);
	private final String BASE_COLLECTION_NAME = "Sensors";
	private final String COLLECTION_NAME = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/sensor/measurements/summary?_groupBy=hour&_sortOrder=ASC&sensorId=%s&ts.since=%s&ts.until=%s&_perPage=50";

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
			taskParameter.setUri(String.format(URI, sensorId, taskParameter.getSinceDateAsString(), taskParameter.getNowAsString()));
			taskParameter.setCollectionName(COLLECTION_NAME);
			new Task().execute(taskParameter);
			taskParameter.setId(null);
			taskParameter.setIdColumnName(null);
		}
		logger.info("Execution Done");
	}
}