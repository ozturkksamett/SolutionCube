package com.solutioncube.task;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.solutioncube.common.IProcess;
import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.dao.SolutionCubeDAO;
import com.solutioncube.helper.CacheManager;
import com.solutioncube.pojo.TaskParameter;

public class AlarmHistoryReportTask implements ITask, IProcess {

	private static final Logger logger = LoggerFactory.getLogger(AlarmHistoryReportTask.class);
	private final String COLLECTION_NAME = this.getClass().getSimpleName().substring(0,
			this.getClass().getSimpleName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/alarm/history?_sortOrder=ASC&violation.ts.since=%s&violation.ts.until=%s&recovery.ts.since=%s&recovery.ts.until=%s&ts.since=%s&ts.until=%s&_perPage=50";
    private MongoTemplate mongoTemplate;
	
	@Override
	public void execute(TaskParameter taskParameter) {

		logger.info("Execution Started");
		taskParameter
				.setUri(String.format(URI, taskParameter.getSinceDateAsString(), taskParameter.getTillDateAsString(),
						taskParameter.getSinceDateAsString(), taskParameter.getTillDateAsString(),
						taskParameter.getSinceDateAsString(), taskParameter.getTillDateAsString()));
		taskParameter.setCollectionName(COLLECTION_NAME);
		new Task().execute(taskParameter);
		mongoTemplate = taskParameter.getMongoTemplate();
		process();
		logger.info("Execution Done");
	}

	@Override
	public void process() {

		logger.info("Process Started");

		List<JSONObject> processedJsonObjects = new ArrayList<JSONObject>();

		List<JSONObject> jsonObjects = CacheManager.get(COLLECTION_NAME);

		Comparator<JSONObject> comparator = (c1, c2) -> {
			return LocalDateTime.parse(c1.getJSONObject("violation").getString("ts"))
					.compareTo(LocalDateTime.parse(c2.getJSONObject("violation").getString("ts")));
		};
		
		Collections.sort(jsonObjects, comparator);
		
		for (int i = 0; i < jsonObjects.size(); i++) {

			JSONObject jsonObject = jsonObjects.get(i);
			LocalDateTime violationTs = LocalDateTime.parse(jsonObject.getJSONObject("violation").getString("ts"));
			LocalDateTime recoveryTs = LocalDateTime.parse(jsonObject.getJSONObject("recovery").getString("ts"));
			if(isSameDay(violationTs, recoveryTs)) {
				processedJsonObjects.add(jsonObject);
				continue;
			} else {
				addJsonObjectToProcessedList(processedJsonObjects, jsonObject, violationTs, recoveryTs);
			}			
		}

		SolutionCubeDAO.saveJsonData(mongoTemplate, COLLECTION_NAME + "Processed", jsonObjects);
		
		logger.info("Process Done");
	}

	private void addJsonObjectToProcessedList(List<JSONObject> processedJsonObjects, JSONObject jsonObject, LocalDateTime violationTs, LocalDateTime recoveryTs) {

		if(violationTs.getDayOfYear() == recoveryTs.getDayOfYear()) {
			jsonObject.getJSONObject("violation").put("ts", violationTs.toString());
			jsonObject.getJSONObject("recovery").put("ts", recoveryTs.toString());
			processedJsonObjects.add(jsonObject);
			return;
		}
		
		LocalDateTime newRecoveryTs = violationTs.toLocalDate().atTime(LocalTime.MAX);
		jsonObject.getJSONObject("recovery").put("ts", newRecoveryTs.toString());
		processedJsonObjects.add(jsonObject);
		
		violationTs = LocalDateTime.of(violationTs.plusDays(1).toLocalDate(), LocalTime.MIDNIGHT);		

		addJsonObjectToProcessedList(processedJsonObjects, jsonObject, violationTs, recoveryTs);
	}

	private boolean isSameDay(LocalDateTime t1, LocalDateTime t2) {
		
		return t1.getDayOfYear() == t2.getDayOfYear() && t1.getYear() == t2.getYear();
	}
}