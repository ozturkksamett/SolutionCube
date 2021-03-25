package com.example.solutioncube.job.task;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.example.solutioncube.job.JobParameter;
import com.example.solutioncube.job.Task;
import com.google.common.collect.Lists;

@Component
public class ZonePresenceSummaryReportTask {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	JobParameter jobParameter;
	
	@Autowired
	private Task task;
	
	private final String BASE_COLLECTION_NAME = "Trackers";
	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/zone/presence/summary?_sortOrder=ASC&trackerId=%s&ts.since=%s&ts.until=%s";
  
	public void executeDaily() {

		List<String> trackers = mongoTemplate.findAll(String.class, BASE_COLLECTION_NAME);
		List<String> trackerIds = new ArrayList<String>();

		for (String tracker : trackers) {

			JSONObject trackerJSONObject = new JSONObject(tracker);
			String trackerId = trackerJSONObject.getString("_id");
			trackerIds.add(trackerId);
		}

		for (List<String> trackerIdsPartition : Lists.partition(trackerIds, 50)) {

			String trackerIdsParam = String.join(",", trackerIdsPartition);

			task.execute(String.format(URI, trackerIdsParam, jobParameter.getSinceDate(), jobParameter.getTillDate()), COLLECTION_NAME);
		}	
	}
}
