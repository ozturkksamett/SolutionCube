package com.solutioncube.collection;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.common.IProcess;
import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.dao.SolutionCubeDAO;
import com.solutioncube.helper.CacheManager;
import com.solutioncube.pojo.Parameter;

public class SensorMeasurementHistoryReport implements ITask, IProcess {

	private static final Logger logger = LoggerFactory.getLogger(SensorMeasurementHistoryReport.class);
	private final String BASE_COLLECTION_NAME = "Sensors";
	private final String COLLECTION_NAME = this.getClass().getSimpleName();
	private final String URI = "https://api.triomobil.com/facility/v1/reports/sensor/measurements/history?sensorId=%s&ts.since=%s&ts.until=%s&_limit=5000";

	@Override
	public void execute(Parameter parameter) {

		logger.info("Execution Started");
		logger.info("Firm:"+parameter.getFirm().getName());
		List<String> sensors = parameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		logger.info("Sensors Size: " + sensors.size());
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
	public void process(Parameter parameter) {
		
		logger.info("Process Started");
		
		List<JSONObject> jsonObjects = CacheManager.get(COLLECTION_NAME+parameter.getFirm().getConfigIndex());
		
		if(jsonObjects == null)
			return;
		
		List<JSONObject> processedJsonObjects = new ArrayList<JSONObject>();
		
		Map<String, List<JSONObject>> groupBySensorIdMap = jsonObjects.stream().collect(Collectors.groupingBy(j -> j.getString("sensorId")));
		
		for (List<JSONObject> sensors : groupBySensorIdMap.values()) {
			
			Comparator<JSONObject> comparator = (c1, c2) -> {
				return ZonedDateTime.parse(c1.getString("ts")).toLocalDateTime().compareTo(ZonedDateTime.parse(c2.getString("ts")).toLocalDateTime());
			};
			
			Collections.sort(sensors, comparator);

			boolean isStatusChanged = false;
			JSONObject sensor = sensors.get(0);
			int digital = getDigitalKey(sensor, parameter);
			String sts = sensor.getString("ts");
			String ets = sensor.getString("ts");
			for (int i = 1; i < sensors.size(); i++) {

				JSONObject s = sensors.get(i);
				isStatusChanged = digital != getDigitalKey(s, parameter);
				if(!isStatusChanged) {
					ets = s.getString("ts");
					continue;
				}
				sensor.put("StartDateTime", sts);
				sensor.put("EndDateTime", ets);
				processedJsonObjects.add(sensor);
				sensor = sensors.get(i);
				digital = getDigitalKey(sensor, parameter);
				sts = sensor.getString("ts");
				ets = sensor.getString("ts");
			}				
		}

		SolutionCubeDAO.saveJsonData(parameter.getMongoTemplate(), COLLECTION_NAME + "Processed", processedJsonObjects);
		
		logger.info("Process Done");
	}

	private int getDigitalKey(JSONObject jsonObject, Parameter parameter) {
		
		String key = "";
		int i = parameter.getFirm().getConfigIndex();
		switch (i) {
		case 0:
			key = "digital2";
			break;
		case 1:
			key = "digital1";
			break;
		default:
			break;
		}
		if(!jsonObject.getJSONObject("measurement").has(key))
			logger.info("key:"+key+" not found, jsonObject:"+jsonObject);
		return jsonObject.getJSONObject("measurement").getInt(key);
	}
}