package com.solutioncube.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solutioncube.common.IService;
import com.solutioncube.common.TaskParameterGenerator;
import com.solutioncube.config.Config;
import com.solutioncube.helper.AsyncHelper;
import com.solutioncube.job.JobScheduler;
import com.solutioncube.service.ErisyemBulkDataService;

@RestController
public class JobController {

	private static final Logger logger = LoggerFactory.getLogger(JobController.class);
	
	private static final int ERISYEM_CONFIG_INDEX = 0;
	private static final int VANUCCI_CONFIG_INDEX = 1;
	
	@Autowired
	private JobScheduler jobScheduler;
	
	@Autowired
	private AsyncHelper asyncHelper;
	
	@Autowired
	private ErisyemBulkDataService erisyemBulkDataService;
	
	@Autowired
	private IService erisyemService;
	
	@Autowired
	private IService vanucciService;

	@Autowired
	TaskParameterGenerator taskParameterGenerator;
	
	@RequestMapping("/")
	public String home() {

		logger.info("home");
		return "SolutionCube Running!";
	}
	
	@RequestMapping("/schedule")
	public String schedule() {

		logger.info("schedule");
		jobScheduler.scheduleDailyJob();
		return "SolutionCubeJob scheduled successfully!";
	}
	
	@PostMapping("/erisyemRunBulkData")
	public String erisyemRunBulkData() {

		logger.info("erisyemRunBulkData");
		taskParameterGenerator.generateTaskParameter(ERISYEM_CONFIG_INDEX).getMongoTemplate().getDb().drop();
		asyncHelper.waitTillEndOfSynchronizedFunc(erisyemService.runStaticTasksAsync());
		erisyemBulkDataService.runBulkData();
		return "Erisyem Bulk Data Service run successfully";
	}
	
	@PostMapping("/erisyemRunStaticTasksAsync")
	public String erisyemRunStaticTasksAsync() {

		logger.info("erisyemRunStaticTasksAsync");
		taskParameterGenerator.generateTaskParameter(ERISYEM_CONFIG_INDEX).getMongoTemplate().getDb().drop();
		erisyemService.runStaticTasksAsync();
		return "Erisyem service started running for static tasks asynchronously successfully";
	}

	@PostMapping("/vanucciRunStaticTasksAsync")
	public String vanucciRunStaticTasksAsync() {

		logger.info("vanucciRunStaticTasksAsync");
		taskParameterGenerator.generateTaskParameter(VANUCCI_CONFIG_INDEX).getMongoTemplate().getDb().drop();
		vanucciService.runStaticTasksAsync();
		return "Vanucci service started running for static tasks asynchronously successfully";
	}
	
	@PostMapping("/debug")
	public String debug() {

		logger.error("App Crashed");		
		return config.getFirms()[1].getName();
	}
	@Autowired
	private Config config;
}