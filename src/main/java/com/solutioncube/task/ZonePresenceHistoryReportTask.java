package com.solutioncube.task;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.pojo.TaskParameter;

public class ZonePresenceHistoryReportTask implements ITask {
	
	private static final Logger logger = LoggerFactory.getLogger(ZonePresenceHistoryReportTask.class);
	private final String BASE_COLLECTION_NAME = "Trackers";
	private final String COLLECTION_NAME = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/zone/presence/history?trackerId=%s&ts.since=%s&ts.until=%s&_perPage=100";

	@Override
	public void execute(TaskParameter taskParameter) {

		logger.info("Execution Started");
		List<String> trackers = taskParameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		List<String> trackerIds = new ArrayList<String>();
		logger.info("Trackers Size: "+trackers.size());
		for (String tracker : trackers) {

			JSONObject trackerJSONObject = new JSONObject(tracker);
			String trackerId = trackerJSONObject.getString("_id");
			trackerIds.add(trackerId);
		}

		for (List<String> trackerIdsPartition : Lists.partition(trackerIds, 50)) {

			String trackerIdsParam = String.join(",", trackerIdsPartition);

			taskParameter.setUri(String.format(URI, trackerIdsParam, taskParameter.getSinceDateAsString(), taskParameter.getNowAsString()));
			taskParameter.setCollectionName(COLLECTION_NAME);
			new Task().execute(taskParameter);
		}
		logger.info("Execution Done");
	}
}