package com.example.solutioncube.job.task;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.example.solutioncube.job.JobParameter;
import com.example.solutioncube.job.Task;

@Component
public class EnergyMeasurementsHistoryReportTask {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	JobParameter jobParameter;
	
	@Autowired
	private Task task;

	private final String BASE_COLLECTION_NAME = "EnergyMeters";
	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/energy/measurements/history?_sortOrder=ASC&energyMeterId=%s&ts.since=%s";
  
	public void executeDaily() {
		
		List<String> energyMeters = mongoTemplate.findAll(String.class, BASE_COLLECTION_NAME);
		
		for (String energyMeter : energyMeters) {
			
			JSONObject energyMetersJSONObject = new JSONObject(energyMeter);
			String energyMeterId = energyMetersJSONObject.getString("_id");
			jobParameter.setId(energyMeterId, "energyMeterId");
			task.execute(String.format(URI, energyMeterId, jobParameter.getSinceDate()), COLLECTION_NAME);
			jobParameter.setId(null, null);
		}		
	}
}
