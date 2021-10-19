package com.solutioncube.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solutioncube.common.IService;
import com.solutioncube.common.ExecutionType;
import com.solutioncube.helper.ServiceRunner;
import com.solutioncube.helper.ParameterGenerator;
import com.solutioncube.job.JobScheduler;

@RestController
public class JobController {

	private static final Logger logger = LoggerFactory.getLogger(JobController.class);
	
	private static final int ERISYEM_CONFIG_INDEX = 0;
	private static final int VANUCCI_CONFIG_INDEX = 1;	
	
	@Autowired
	IService erisyemService;
	
	@Autowired
	IService vanucciService;
	
	@Autowired
	JobScheduler jobScheduler;	

	@Autowired
	ParameterGenerator parameterGenerator;

	@Autowired
	ServiceRunner serviceRunner;
	
	@RequestMapping("/")
	public String home() {

		logger.info("home");
		return "SolutionCube Running!";
	}
	
	@PostMapping("/schedule")
	public String schedule() {

		logger.info("schedule");
		jobScheduler.scheduleDailyJob();
		return "SolutionCubeJob scheduled successfully!";
	}
	
	//@PostMapping("/erisyemRunBulkData")
	public String erisyemRunBulkData() {

		logger.info("erisyemRunBulkData");
		//parameterGenerator.generateTaskParameter(ERISYEM_CONFIG_INDEX).getMongoTemplate().getDb().drop();
		//serviceRunner.runService(erisyemService, ExecutionType.STATIC_COLLECTIONS, true);
		serviceRunner.runService(erisyemService, ExecutionType.BULK_DATA_ONLY_WITH_SINCE_PARAM, false);
		serviceRunner.runService(erisyemService, ExecutionType.BULK_DATA_WITH_BOTH_SINCE_AND_TILL_PARAM, false);
		return "Erisyem solutionCubeLocalhostService finished running for bulk data successfully";
	}
	
	//@PostMapping("/erisyemRunStaticTasks")
	public String erisyemRunStaticTasks() {

		logger.info("erisyemRunStaticTasks");
		parameterGenerator.generateTaskParameter(ERISYEM_CONFIG_INDEX).getMongoTemplate().getDb().drop();
		serviceRunner.runService(erisyemService, ExecutionType.STATIC_COLLECTIONS, true);
		return "Erisyem solutionCubeLocalhostService finished running for static tasks asynchronously successfully";
	}

	//@PostMapping("/vanucciRunStaticTasks")
	public String vanucciRunStaticTasks() {

		logger.info("vanucciRunStaticTasks");
		parameterGenerator.generateTaskParameter(VANUCCI_CONFIG_INDEX).getMongoTemplate().getDb().drop();
		serviceRunner.runService(vanucciService, ExecutionType.STATIC_COLLECTIONS, true);
		return "Vanucci solutionCubeLocalhostService finished running for static tasks asynchronously successfully";
	}
}