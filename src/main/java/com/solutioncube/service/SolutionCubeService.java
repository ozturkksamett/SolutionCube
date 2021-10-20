package com.solutioncube.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solutioncube.common.ExecutionType;
import com.solutioncube.helper.CacheManager;
import com.solutioncube.helper.ServiceRunner;

@Service
public class SolutionCubeService {

	private static final Logger logger = LoggerFactory.getLogger(SolutionCubeService.class);

	@Autowired
	private ServiceRunner serviceRunner;

	public void runCollectionsProcess(boolean isAsync) {

		logger.info("runCollectionsProcess started");	
		CacheManager.clear();
		serviceRunner.runAllRegisteredServices(ExecutionType.PROCESS_CONVERSION, isAsync);	
	}
}
