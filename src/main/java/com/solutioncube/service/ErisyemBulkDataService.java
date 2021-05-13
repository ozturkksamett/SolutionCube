package com.solutioncube.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.solutioncube.common.IService;
import com.solutioncube.common.TaskParameterGenerator;
import com.solutioncube.helper.AsyncHelper;

@Service
public class ErisyemBulkDataService {
	
	private static final Logger logger = LoggerFactory.getLogger(ErisyemBulkDataService.class);
	
	@Autowired
	private IService erisyemService;

	@Autowired
	private AsyncHelper asyncHelper;
	
	@Async
    public void runBulkData() {
    	
    	logger.info("Erisyem Bulk Data Service started running..");
    	Instant start = Instant.now();
    	TaskParameterGenerator.isBulkData = true;
    	while (TaskParameterGenerator.getInitialDate().isBefore(LocalDate.now())) {
    		
    		logger.info("Initial Date: " + TaskParameterGenerator.getInitialDate());    		
    		asyncHelper.waitTillEndOfSynchronizedFunc(erisyemService.runDailyTasksAsync());    		
    		TaskParameterGenerator.setInitialDate(TaskParameterGenerator.getInitialDate().plusDays(TaskParameterGenerator.getIntervalDay()));    		
    	}
    	TaskParameterGenerator.isBulkData = false;
    	Instant finish = Instant.now();
    	logger.info("Erisyem Bulk Data Service finished running. Duration: " + Duration.between(start, finish).toHours() + " hours.");    	
	}        
}