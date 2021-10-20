package com.solutioncube.collection;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.pojo.Parameter;

public class PositionHistoryReport implements ITask {

	private static final Logger logger = LoggerFactory.getLogger(PositionHistoryReport.class);
	private final String BASE_COLLECTION_NAME = "Trackers";
	private final String COLLECTION_NAME = this.getClass().getSimpleName();
	private final String URI = "https://api.triomobil.com/facility/v1/reports/indoor/positions/history?trackerId=%s&ts.since=%s&_limit=5000";
  
	@Override
	public void execute(Parameter parameter) {
		
		logger.info("Execution Started");
		List<String> trackers = parameter.getMongoTemplate().findAll(String.class, BASE_COLLECTION_NAME);
		logger.info("Trackers Size: "+trackers.size());
		for (String tracker : trackers) {
			
			JSONObject trackerJSONObject = new JSONObject(tracker);
			String trackerId = trackerJSONObject.getString("_id");
			parameter.setId(trackerId);
			parameter.setIdColumnName("trackerId");
			parameter.setUri(String.format(URI, trackerId, parameter.getSinceDateAsString()));
			parameter.setCollectionName(COLLECTION_NAME);
			new Task().execute(parameter);
			parameter.setId(null);
			parameter.setIdColumnName(null);
		}
		logger.info("Execution Done");
	}

	@Override
	public String getCollectionName() {
		return COLLECTION_NAME;
	}
}