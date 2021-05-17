package com.solutioncube.task;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.pojo.TaskParameter;

public class EnergyMeasurementsHistoryReportTask implements ITask {

	private static final Logger logger = LoggerFactory.getLogger(EnergyMeasurementsHistoryReportTask.class);
	private final String BASE_COLLECTION_NAME = "EnergyMeters";
	private final String COLLECTION_NAME = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/energy/measurements/history?_sortOrder=ASC&energyMeterId=%s&ts.since=%s&_limit=5000";
  
	@Override
	public void execute(TaskParameter taskParameter) {
		
		logger.info("Execution Started");
		List<String> energyMeters = taskParameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		logger.info("EnergyMeters Size: "+energyMeters.size());
		for (String energyMeter : energyMeters) {
			taskParameter.generateToken();
			JSONObject energyMetersJSONObject = new JSONObject(energyMeter);
			String energyMeterId = energyMetersJSONObject.getString("_id");
			taskParameter.setId(energyMeterId);
			taskParameter.setIdColumnName("energyMeterId");
			taskParameter.setUri(String.format(URI, energyMeterId, taskParameter.getSinceDateAsString()));
			taskParameter.setCollectionName(COLLECTION_NAME);
			new Task().execute(taskParameter);
			taskParameter.setId(null);
			taskParameter.setIdColumnName(null);
		}		
		logger.info("Execution Done");
	}
}