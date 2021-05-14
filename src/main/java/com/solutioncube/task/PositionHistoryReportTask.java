package com.solutioncube.task;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.pojo.TaskParameter;

public class PositionHistoryReportTask implements ITask {

	private static final Logger logger = LoggerFactory.getLogger(PositionHistoryReportTask.class);
	private final String BASE_COLLECTION_NAME = "Trackers";
	private final String COLLECTION_NAME = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/indoor/positions/history?trackerId=%s&ts.since=%s&_limit=5000";
  
	@Override
	public void execute(TaskParameter taskParameter) {
		
		logger.info("Execution Started");
		List<String> trackers = taskParameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		logger.info("Trackers Size: "+trackers.size());
		for (String tracker : trackers) {
			taskParameter.setToken(taskParameter.generateToken());
			JSONObject trackerJSONObject = new JSONObject(tracker);
			String trackerId = trackerJSONObject.getString("_id");
			taskParameter.setId(trackerId);
			taskParameter.setIdColumnName("trackerId");
			taskParameter.setUri(String.format(URI, trackerId, taskParameter.getSinceDateAsString()));
			taskParameter.setCollectionName(COLLECTION_NAME);
			new Task().execute(taskParameter);
			taskParameter.setId(null);
			taskParameter.setIdColumnName(null);
		}
		logger.info("Execution Done");
	}
}