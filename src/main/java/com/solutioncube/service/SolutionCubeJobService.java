package com.solutioncube.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solutioncube.common.IService;
import com.solutioncube.helper.AsyncHelper;

@Service
public class SolutionCubeJobService {

	private static final Logger logger = LoggerFactory.getLogger(SolutionCubeJobService.class);

	@Autowired
	private AsyncHelper asyncHelper;
		
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

	public void runSolutionCubeJob() {

		logger.info("SolutionCubeJobService started running..");		
		Collection<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();		
		registerServices().forEach(service -> {
			
			futures.addAll(service.runDailyTasksAsync()); 
		});		
		asyncHelper.waitTillEndOfSynchronizedFunc(futures);		
		logger.info("SolutionCubeJobService finished running.");
	}
}