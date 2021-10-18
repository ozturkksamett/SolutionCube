package com.solutioncube.collection;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.common.IProcess;
import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.dao.SolutionCubeDAO;
import com.solutioncube.helper.CacheManager;
import com.solutioncube.pojo.Parameter;

public class AlarmHistoryReport implements ITask, IProcess {

	private static final Logger logger = LoggerFactory.getLogger(AlarmHistoryReport.class);
	private final String COLLECTION_NAME = this.getClass().getSimpleName();
	private final String URI = "https://api.triomobil.com/facility/v1/reports/alarm/history?_sortOrder=ASC&violation.ts.since=%s&violation.ts.until=%s&recovery.ts.since=%s&recovery.ts.until=%s&ts.since=%s&ts.until=%s&_perPage=50";
	
	@Override
	public void execute(Parameter parameter) {

		logger.info("Execution Started");
		parameter
				.setUri(String.format(URI, parameter.getSinceDateAsString(), parameter.getTillDateAsString(),
						parameter.getSinceDateAsString(), parameter.getTillDateAsString(),
						parameter.getSinceDateAsString(), parameter.getTillDateAsString()));
		parameter.setCollectionName(COLLECTION_NAME);
		new Task().execute(parameter);
		logger.info("Execution Done");
	}

	@Override
	public void process(Parameter parameter) {

		logger.info("Process Started");

		List<JSONObject> jsonObjects = CacheManager.get(COLLECTION_NAME+parameter.getFirm().getConfigIndex());
		
		if(jsonObjects == null)
			return;
		
		List<JSONObject> processedJsonObjects = new ArrayList<JSONObject>();
		
		Comparator<JSONObject> comparator = (c1, c2) -> {
			return ZonedDateTime.parse(c1.getJSONObject("violation").getString("ts")).toLocalDateTime()
					.compareTo(ZonedDateTime.parse(c2.getJSONObject("violation").getString("ts")).toLocalDateTime());
		};
		
		Collections.sort(jsonObjects, comparator);
		
		for (int i = 0; i < jsonObjects.size(); i++) {

			JSONObject jsonObject = jsonObjects.get(i);
			LocalDateTime violationTs = ZonedDateTime.parse(jsonObject.getJSONObject("violation").getString("ts")).toLocalDateTime();
			LocalDateTime recoveryTs = ZonedDateTime.parse(jsonObject.getJSONObject("recovery").getString("ts")).toLocalDateTime();
			if(isSameDay(violationTs, recoveryTs)) {
				processedJsonObjects.add(jsonObject);
				continue;
			} else {				
				addJsonObjectToProcessedList(processedJsonObjects, new JSONObject(jsonObject.toString()), violationTs, recoveryTs);
			}			
		}

		SolutionCubeDAO.saveBulkJsonData(parameter.getMongoTemplate(), COLLECTION_NAME + "Processed", processedJsonObjects);
		
		logger.info("Process Done");
	}

	private void addJsonObjectToProcessedList(List<JSONObject> processedJsonObjects, JSONObject jsonObject, LocalDateTime violationTs, LocalDateTime recoveryTs) {

		if(violationTs.getDayOfYear() == recoveryTs.getDayOfYear()) {
			
			jsonObject.getJSONObject("violation").put("ts", violationTs.toString());
			jsonObject.getJSONObject("recovery").put("ts", recoveryTs.toString());

			logger.info("violation:"+jsonObject.getJSONObject("violation").getString("ts"));
			logger.info("recovery:"+jsonObject.getJSONObject("recovery").getString("ts"));
			processedJsonObjects.add(jsonObject);
			return;
		} 
		
		LocalDateTime newRecoveryTs = violationTs.toLocalDate().atTime(LocalTime.MAX);
		jsonObject.getJSONObject("recovery").put("ts", newRecoveryTs.toString());

		logger.info("violation:"+jsonObject.getJSONObject("violation").getString("ts"));
		logger.info("recovery:"+jsonObject.getJSONObject("recovery").getString("ts"));
		processedJsonObjects.add(jsonObject);
		
		violationTs = LocalDateTime.of(violationTs.plusDays(1).toLocalDate(), LocalTime.MIDNIGHT);	

		addJsonObjectToProcessedList(processedJsonObjects, new JSONObject(jsonObject.toString()), violationTs, recoveryTs);
	}

	private boolean isSameDay(LocalDateTime t1, LocalDateTime t2) {
		
		return t1.getDayOfYear() == t2.getDayOfYear() && t1.getYear() == t2.getYear();
	}

	@Override
	public String getCollectionName() {
		return COLLECTION_NAME;
	}
}