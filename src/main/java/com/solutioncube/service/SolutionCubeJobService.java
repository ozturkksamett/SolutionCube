package com.solutioncube.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solutioncube.common.IService;
import com.solutioncube.common.ExecutionType;
import com.solutioncube.helper.ApiErrorLogger;
import com.solutioncube.helper.CacheManager;
import com.solutioncube.helper.ServiceRunner;

@Service
public class SolutionCubeJobService {

	private static final Logger logger = LoggerFactory.getLogger(SolutionCubeJobService.class);

	@Autowired
	private ServiceRunner serviceRunner;

	@Autowired
	private IService erisyemService;
	
	@Autowired
	private IService vanucciService;
	
	private List<IService> registerServices() {

		return Arrays.asList(new IService[] {
				
				erisyemService
				,vanucciService
		});
	}

	public void runDailySolutionCubeJob(boolean isAsync) {

		logger.info("runDailySolutionCubeJob started");	
		CacheManager.clear();
		ApiErrorLogger.clear();
		serviceRunner.runServices(registerServices(), ExecutionType.DAILY_COLLECTIONS, isAsync);	
		serviceRunner.runServices(registerServices(), ExecutionType.PROCESS_DAILY_COLLECTIONS, isAsync);	
		ApiErrorLogger.print();	
	}
}