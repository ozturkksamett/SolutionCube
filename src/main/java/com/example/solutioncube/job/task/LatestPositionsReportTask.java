package com.example.solutioncube.job.task;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.example.solutioncube.job.JobParameter;
import com.example.solutioncube.job.Task;

@Component
public class LatestPositionsReportTask {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	JobParameter jobParameter;
	
	@Autowired
	private Task task;

	private final String BASE_COLLECTION_NAME = "Trackers";
	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/indoor/positions/latest?_sortOrder=ASC&_sortBy=label&trackerId=%s";
  
	public void executeDaily() {
		
		List<String> trackers = mongoTemplate.findAll(String.class, BASE_COLLECTION_NAME);
		
		for (String tracker : trackers) {
			
			JSONObject trackerJSONObject = new JSONObject(tracker);
			String trackerId = trackerJSONObject.getString("_id");

			task.execute(String.format(URI, trackerId), COLLECTION_NAME);
		}		
	}
}
