package com.solutioncube.task;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.solutioncube.common.IProcess;
import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.helper.CacheManager;
import com.solutioncube.pojo.TaskParameter;

public class AlarmHistoryReportTask implements ITask, IProcess {

	private static final Logger logger = LoggerFactory.getLogger(AlarmHistoryReportTask.class);
	private final String COLLECTION_NAME = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/alarm/history?_sortOrder=ASC&violation.ts.since=%s&violation.ts.until=%s&recovery.ts.since=%s&recovery.ts.until=%s&ts.since=%s&ts.until=%s&_perPage=50";

	@Override
	public void execute(TaskParameter taskParameter) {

		logger.info("Execution Started");
		taskParameter.setUri(String.format(URI, taskParameter.getSinceDateAsString(), taskParameter.getTillDateAsString(), taskParameter.getSinceDateAsString(), taskParameter.getTillDateAsString(), taskParameter.getSinceDateAsString(), taskParameter.getTillDateAsString()));
		taskParameter.setCollectionName(COLLECTION_NAME);
		new Task().execute(taskParameter);
		logger.info("Execution Done");
	}

	@Override
	public void process() {
		
		logger.info("Process Started");
		
		JSONArray jsonArray = CacheManager.get(COLLECTION_NAME);
		
		// Implement Algorithm
		for (int i = 0; i < jsonArray.length(); i++) {
			
		}		
		
		logger.info("Process Done");
	}
}