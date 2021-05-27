package com.solutioncube.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solutioncube.common.IService;
import com.solutioncube.common.ServiceRunType;
import com.solutioncube.helper.AsyncHelper;
import com.solutioncube.helper.ServiceRunner;
import com.solutioncube.helper.TaskParameterGenerator;

@Service
public class SolutionCubeJobService {

	private static final Logger logger = LoggerFactory.getLogger(SolutionCubeJobService.class);

	@Autowired
	private ServiceRunner serviceRunner;

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

	public void runDailySolutionCubeJobAsync() {

		TaskParameterGenerator.tilDate = LocalDateTime.now();	
		asyncHelper.waitTillEndOfSynchronizedFunc(serviceRunner.runServicesAsync(registerServices(), ServiceRunType.DAILY));	
		logger.info("SolutionCubeJobService finished running.");	
	}
	
	public void runDailySolutionCubeJob() {

		TaskParameterGenerator.tilDate = LocalDateTime.now();		
		serviceRunner.runServices(registerServices(), ServiceRunType.DAILY);	
		logger.info("SolutionCubeJobService finished running.");
	}
}