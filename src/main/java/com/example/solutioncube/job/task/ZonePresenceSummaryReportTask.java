package com.example.solutioncube.job.task;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.solutioncube.common.Task;
import com.example.solutioncube.common.TaskParameter;
import com.google.common.collect.Lists;

@Component
public class ZonePresenceSummaryReportTask {

	@Autowired
	Task task;
	
	private final String BASE_COLLECTION_NAME = "Trackers";
	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/zone/presence/summary?trackerId=%s&ts.since=%s&ts.until=%s";
  
	public void executeDaily(TaskParameter taskParameter) {

		List<String> trackers = taskParameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		List<String> trackerIds = new ArrayList<String>();

		for (String tracker : trackers) {

			JSONObject trackerJSONObject = new JSONObject(tracker);
			String trackerId = trackerJSONObject.getString("_id");
			trackerIds.add(trackerId);
		}

		for (List<String> trackerIdsPartition : Lists.partition(trackerIds, 50)) {

			String trackerIdsParam = String.join(",", trackerIdsPartition);

			taskParameter.setUri(String.format(URI, trackerIdsParam, taskParameter.getSinceDateAsString(), taskParameter.getNowAsString()));
			taskParameter.setCollectionName(COLLECTION_NAME);
			task.execute(taskParameter);
		}	
	}
}
