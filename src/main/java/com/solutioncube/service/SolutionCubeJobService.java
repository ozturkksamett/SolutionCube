package com.solutioncube.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solutioncube.common.ExecutionType;
import com.solutioncube.helper.ApiErrorLogger;
import com.solutioncube.helper.CacheManager;
import com.solutioncube.helper.ServiceRunner;

@Service
public class SolutionCubeJobService {

	private static final Logger logger = LoggerFactory.getLogger(SolutionCubeJobService.class);

	@Autowired
	private ServiceRunner serviceRunner;

	public void runDailySolutionCubeJob(boolean isAsync) {

		logger.info("runDailySolutionCubeJob started");	
		CacheManager.clear();
		ApiErrorLogger.clear();
		serviceRunner.runAllRegisteredServices(ExecutionType.DAILY_COLLECTIONS, isAsync);	
		serviceRunner.runAllRegisteredServices(ExecutionType.PROCESS_DAILY_COLLECTIONS, isAsync);	
		ApiErrorLogger.print();	
	}
}