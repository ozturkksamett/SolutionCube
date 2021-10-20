package com.solutioncube.collection;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.pojo.Parameter;

public class ZonePresenceSummaryReport implements ITask {
	
	private static final Logger logger = LoggerFactory.getLogger(ZonePresenceSummaryReport.class);
	private final String BASE_COLLECTION_NAME = "Trackers";
	private final String COLLECTION_NAME = this.getClass().getSimpleName();
	private final String URI = "https://api.triomobil.com/facility/v1/reports/zone/presence/summary?trackerId=%s&ts.since=%s&ts.until=%s&_perPage=100";
	
	@Override
	public void execute(Parameter parameter) {

		logger.info("Execution Started");
		List<String> trackers = parameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		List<String> trackerIds = new ArrayList<String>();
		logger.info("Trackers Size: "+trackers.size());
		for (String tracker : trackers) {

			JSONObject trackerJSONObject = new JSONObject(tracker);
			String trackerId = trackerJSONObject.getString("_id");
			trackerIds.add(trackerId);
		}

		for (List<String> trackerIdsPartition : Lists.partition(trackerIds, 50)) {

			String trackerIdsParam = String.join(",", trackerIdsPartition);

			parameter.setUri(String.format(URI, trackerIdsParam, parameter.getSinceDateAsString(), parameter.getTillDateAsString()));
			parameter.setCollectionName(COLLECTION_NAME);
			new Task().execute(parameter);
		}
		logger.info("Execution Done");
	}

	@Override
	public String getCollectionName() {
		return COLLECTION_NAME;
	}
}