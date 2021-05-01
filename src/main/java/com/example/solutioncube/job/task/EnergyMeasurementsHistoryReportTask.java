package com.example.solutioncube.job.task;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.solutioncube.common.Task;
import com.example.solutioncube.common.TaskParameter;

@Component
public class EnergyMeasurementsHistoryReportTask {
	
	@Autowired
	Task task;

	private final String BASE_COLLECTION_NAME = "EnergyMeters";
	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/energy/measurements/history?_sortOrder=ASC&energyMeterId=%s&ts.since=%s";
  
	public void executeDaily(TaskParameter taskParameter) {
		
		List<String> energyMeters = taskParameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		
		for (String energyMeter : energyMeters) {
			
			JSONObject energyMetersJSONObject = new JSONObject(energyMeter);
			String energyMeterId = energyMetersJSONObject.getString("_id");
			taskParameter.setId(energyMeterId);
			taskParameter.setIdColumnName("energyMeterId");
			taskParameter.setUri(String.format(URI, energyMeterId, taskParameter.getSinceDateAsString()));
			taskParameter.setCollectionName(COLLECTION_NAME);
			task.execute(taskParameter);
			taskParameter.setId(null);
			taskParameter.setIdColumnName(null);
		}		
	}
}
