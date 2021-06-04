package com.solutioncube.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solutioncube.common.IService;
import com.solutioncube.common.TaskType;
import com.solutioncube.helper.ServiceRunner;
import com.solutioncube.helper.TaskParameterGenerator;
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
	TaskParameterGenerator taskParameterGenerator;

	@Autowired
	ServiceRunner serviceRunner;
	
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
		//taskParameterGenerator.generateTaskParameter(ERISYEM_CONFIG_INDEX).getMongoTemplate().getDb().drop();
		//serviceRunner.runService(erisyemService, TaskType.TASKS_WHICH_STATIC, true);
		serviceRunner.runService(erisyemService, TaskType.TASKS_WHICH_ONLY_WITH_SINCE_PARAM, false);
		serviceRunner.runService(erisyemService, TaskType.TASKS_WHICH_WITH_BOTH_SINCE_AND_TILL_PARAM, false);
		return "Erisyem service finished running for bulk data successfully";
	}
	
	//@PostMapping("/erisyemRunStaticTasks")
	public String erisyemRunStaticTasks() {

		logger.info("erisyemRunStaticTasks");
		taskParameterGenerator.generateTaskParameter(ERISYEM_CONFIG_INDEX).getMongoTemplate().getDb().drop();
		serviceRunner.runService(erisyemService, TaskType.TASKS_WHICH_STATIC, true);
		return "Erisyem service finished running for static tasks asynchronously successfully";
	}

	@PostMapping("/vanucciRunStaticTasks")
	public String vanucciRunStaticTasks() {

		logger.info("vanucciRunStaticTasks");
		taskParameterGenerator.generateTaskParameter(VANUCCI_CONFIG_INDEX).getMongoTemplate().getDb().drop();
		serviceRunner.runService(vanucciService, TaskType.TASKS_WHICH_STATIC, true);
		return "Vanucci service finished running for static tasks asynchronously successfully";
	}
}