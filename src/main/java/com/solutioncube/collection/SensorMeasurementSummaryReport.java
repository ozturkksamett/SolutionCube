package com.solutioncube.collection;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.pojo.Parameter;

public class SensorMeasurementSummaryReport implements ITask {

	private static final Logger logger = LoggerFactory.getLogger(SensorMeasurementSummaryReport.class);
	private final String BASE_COLLECTION_NAME = "Sensors";
	private final String COLLECTION_NAME = this.getClass().getSimpleName();
	private final String URI = "https://api.triomobil.com/facility/v1/reports/sensor/measurements/summary?_groupBy=hour&_sortOrder=ASC&sensorId=%s&ts.since=%s&ts.until=%s&_perPage=50";

	@Override
	public void execute(Parameter parameter) {

		logger.info("Execution Started");
		List<String> sensors = parameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		logger.info("Sensors Size: "+sensors.size());
		for (String sensor : sensors) {

			JSONObject sensorJSONObject = new JSONObject(sensor);
			String sensorId = sensorJSONObject.getString("_id");
			parameter.setId(sensorId);
			parameter.setIdColumnName("sensorId");
			parameter.setUri(String.format(URI, sensorId, parameter.getSinceDateAsString(), parameter.getTillDateAsString()));
			parameter.setCollectionName(COLLECTION_NAME);
			new Task().execute(parameter);
			parameter.setId(null);
			parameter.setIdColumnName(null);
		}
		logger.info("Execution Done");
	}

	@Override
	public String getCollectionName() {
		return COLLECTION_NAME;
	}
}