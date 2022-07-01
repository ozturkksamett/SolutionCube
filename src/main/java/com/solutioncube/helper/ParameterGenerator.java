package com.solutioncube.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solutioncube.config.Config;
import com.solutioncube.pojo.Parameter;

@Component
public class ParameterGenerator {

	private static final Logger logger = LoggerFactory.getLogger(ParameterGenerator.class);
	
	private static final int INTERVAL_DAY = 1;
	private static LocalDate initialDate = LocalDate.of(2022, 6, 28);
	public static LocalTime intialLocalTime = LocalTime.of(05,00 );
	public static LocalTime endLocalTime = LocalTime.of(21,00 );
	public static LocalDateTime initialLocalDateTime = LocalDateTime.of(initialDate, intialLocalTime);
	private static LocalDate endDate = LocalDate.of(2022, 3, 03);
	public static boolean isBulkData = false;
	  
	@Autowired
	private Config config;	

	@Autowired
	private MongoTemplateGenerator mongoTemplateGenerator;
	
	public Parameter generateTaskParameter(int configIndex) {
		
		return !isBulkData ? generateTaskParameterForDailyTasks(configIndex) : generateTaskParameterForBulkDataOfErisyem(generateTaskParameterForDailyTasks(configIndex));
	}

	
	private Parameter generateTaskParameterForDailyTasks(int configIndex) {
		
		Parameter parameter = new Parameter();
		
		parameter.setFirm(config.getFirms()[configIndex]);
		parameter.setMongoTemplate(mongoTemplateGenerator.generateMongoTemplate(configIndex));
		parameter.setSinceDate(LocalDateTime.of(LocalDateTime.now().minusMinutes(config.getInterval()).toLocalDate(), LocalTime.MIDNIGHT));
		parameter.setTillDate(parameter.getSinceDate().plusMinutes(config.getInterval()));
		parameter.generateToken();
		
		return parameter;
	}

	private Parameter generateTaskParameterForBulkDataOfErisyem(Parameter parameter) {
		
		parameter.setSinceDate(LocalDateTime.of(initialDate, intialLocalTime));
		parameter.setTillDate(LocalDateTime.of(initialDate.plusDays(INTERVAL_DAY), endLocalTime));
		
		return parameter;
	}
	
	public static LocalDate getInitialDate() {
		return initialDate;
	}

	public static LocalTime getInitialLocalTime() {
		return intialLocalTime;
	}
	public static LocalTime getEndLocalTime() {
		return endLocalTime;
	}
	public static LocalDateTime getInitialLocalDateDate() {
		return initialLocalDateTime;
	}
	public static LocalDate getEndDate() {
		return endDate;
	}
	
	public static void setInitialDate(LocalDate initialDate) {
		ParameterGenerator.initialDate = initialDate;
		logger.info("Initial Date: " + ParameterGenerator.getInitialDate());  
	}

	public static int getIntervalDay() {
		return INTERVAL_DAY;
	}
}