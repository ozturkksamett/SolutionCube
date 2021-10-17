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
import com.solutioncube.pojo.Parameter;

@Component
public class ParameterGenerator {

	private static final Logger logger = LoggerFactory.getLogger(ParameterGenerator.class);
	
	private static final int INTERVAL_DAY = 30;
	private static LocalDate initialDate = LocalDate.of(2021, 06, 06);
	public static boolean isBulkData = false;
	
	@Autowired
	private Config config;	

	@Autowired
	private MongoClient mongoClient;
	
	public Parameter generateTaskParameter(int configIndex) {
		
		return isBulkData 
				? generateTaskParameterForBulkDataOfErisyem(generateTaskParameterForDailyTasks(configIndex))
				: generateTaskParameterForDailyTasks(configIndex); 
	}

	private Parameter generateTaskParameterForDailyTasks(int configIndex) {
		
		Firm firm = config.getFirms()[configIndex];
		MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, firm.getName());
		int interval = config.getInterval(); 

		Parameter parameter = new Parameter();
		
		parameter.setFirm(firm);
		parameter.setMongoTemplate(mongoTemplate);
		parameter.setSinceDate(LocalDateTime.of(LocalDateTime.now().minusMinutes(interval).toLocalDate(), LocalTime.MIDNIGHT));
		parameter.setTillDate(parameter.getSinceDate().plusMinutes(interval));
		parameter.generateToken();
		
		return parameter;
	}

	private Parameter generateTaskParameterForBulkDataOfErisyem(Parameter parameter) {
		
		parameter.setSinceDate(LocalDateTime.of(initialDate, LocalTime.MIDNIGHT));
		parameter.setTillDate(LocalDateTime.of(initialDate.plusDays(INTERVAL_DAY), LocalTime.MIDNIGHT));
		
		return parameter;
	}
	
	public static LocalDate getInitialDate() {
		return initialDate;
	}

	public static void setInitialDate(LocalDate initialDate) {
		ParameterGenerator.initialDate = initialDate;
		logger.info("Initial Date: " + ParameterGenerator.getInitialDate());  
	}

	public static int getIntervalDay() {
		return INTERVAL_DAY;
	}
}