package com.solutioncube.collection;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.pojo.Parameter;

public class TemperatureMeasurementsSummaryReport implements ITask {

	private static final Logger logger = LoggerFactory.getLogger(TemperatureMeasurementsSummaryReport.class);
	private final String BASE_COLLECTION_NAME = "TemperatureSensors";
	private final String COLLECTION_NAME = this.getClass().getSimpleName();
	private final String URI = "https://api.triomobil.com/facility/v1/reports/temperature/measurements/summary?temperatureSensorId=%s&ts.since=%s&ts.until=%s&_perPage=50";

	@Override
	public void execute(Parameter parameter) {
	
		logger.info("Execution Started");
		List<String> temperatureSensors = parameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		logger.info("TemperatureSensors Size: "+temperatureSensors.size());
		for (String temperatureSensor : temperatureSensors) {
			
			JSONObject temperatureSensorJSONObject = new JSONObject(temperatureSensor);
			String temperatureSensorId = temperatureSensorJSONObject.getString("_id");
			parameter.setUri(String.format(URI, temperatureSensorId, parameter.getSinceDateAsString(), parameter.getTillDateAsString()));
			parameter.setCollectionName(COLLECTION_NAME);
			new Task().execute(parameter);
		}
		logger.info("Execution Done");
	}
}