package com.solutioncube.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoClient;
import com.solutioncube.config.Config;
import com.solutioncube.pojo.Firm;
import com.solutioncube.pojo.TaskParameter;

@Component
public class TaskParameterGenerator {

	private static final Logger logger = LoggerFactory.getLogger(TaskParameterGenerator.class);
	
	private static final int INTERVAL_DAY = 30;
	private static LocalDate initialDate = LocalDate.of(2020, 12, 01);
	public static boolean isBulkData = false;
	
	@Autowired
	private Config config;	

	@Autowired
	private MongoClient mongoClient;
	
	public TaskParameter generateTaskParameter(int configIndex) {
		
		return isBulkData 
				? generateTaskParameterForBulkDataOfErisyem(generateTaskParameterForDailyTasks(configIndex))
				: generateTaskParameterForDailyTasks(configIndex); 
	}

	private TaskParameter generateTaskParameterForDailyTasks(int configIndex) {
		
		Firm firm = config.getFirms()[configIndex];
		MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, firm.getName());
		int interval = config.getInterval(); 

		TaskParameter taskParameter = new TaskParameter();
		
		taskParameter.setFirm(firm);
		taskParameter.setMongoTemplate(mongoTemplate);
		taskParameter.setSinceDate(LocalDateTime.of(LocalDateTime.now().minusMinutes(interval).toLocalDate(), LocalTime.MIDNIGHT));
		taskParameter.setTillDate(taskParameter.getSinceDate().plusMinutes(interval));
		taskParameter.generateToken();
		
		return taskParameter;
	}

	private TaskParameter generateTaskParameterForBulkDataOfErisyem(TaskParameter taskParameter) {
		
		taskParameter.setSinceDate(LocalDateTime.of(initialDate, LocalTime.MIDNIGHT));
		taskParameter.setTillDate(LocalDateTime.of(initialDate.plusDays(INTERVAL_DAY), LocalTime.MIDNIGHT));
		
		return taskParameter;
	}
	
	public static LocalDate getInitialDate() {
		return initialDate;
	}

	public static void setInitialDate(LocalDate initialDate) {
		TaskParameterGenerator.initialDate = initialDate;
		logger.info("Initial Date: " + TaskParameterGenerator.getInitialDate());  
	}

	public static int getIntervalDay() {
		return INTERVAL_DAY;
	}
}