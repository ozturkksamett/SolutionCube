package com.solutioncube.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solutioncube.common.ExecutionType;
import com.solutioncube.common.IService;
import com.solutioncube.config.Config;
import com.solutioncube.helper.CacheManager;
import com.solutioncube.helper.MongoTemplateGenerator;
import com.solutioncube.helper.ParameterGenerator;
import com.solutioncube.helper.ServiceRunner;
import com.solutioncube.job.SolutionCubeJobScheduler;

@RestController
public class SolutionCubeJobController {

	private static final Logger logger = LoggerFactory.getLogger(SolutionCubeJobController.class);

	@Autowired
	Config config;
	
	@Autowired
	SolutionCubeJobScheduler solutionCubeJobScheduler;	

	@Autowired
	MongoTemplateGenerator mongoTemplateGenerator;
	
	private static final int VANUCCI_CONFIG_INDEX = 1;
	
	@Autowired
	ParameterGenerator parameterGenerator;
	
	@Autowired
	private IService vanucciService;
	
	@Autowired
	private IService erisyemService;

	@Autowired
	ServiceRunner serviceRunner;
	
	@RequestMapping("/")
	public String home() {

		logger.info("home");
		return "SolutionCube is Running!";
	}
	
	@PostMapping("/schedule")
	public String schedule() {

		logger.info("schedule");
		solutionCubeJobScheduler.scheduleDailyJob();
		return "SolutionCubeJob scheduled successfully!";
	}
	
	@PostMapping("/register-firm")
	public String registerFirm(@RequestBody Integer configIndex) {
	
		logger.info("registerFirm");
		int registedServiceCount = serviceRunner.getRegisteredServices().size();
		if(configIndex != registedServiceCount-1)
			return "Invalid Config Index!";
		serviceRunner.runGivenIndexedServices(Arrays.asList(configIndex), ExecutionType.STATIC_COLLECTIONS, true);
		return "Service registered successfully";
	}
	
	@PostMapping("/update-firm-static-collections")
	public String updateFirmStaticCollections(@RequestBody Integer configIndex) {
	
		logger.info("updateFirmStaticCollections");
		int registedServiceCount = serviceRunner.getRegisteredServices().size();
		if(configIndex >= registedServiceCount)
			return "Invalid Config Index!";
		serviceRunner.runGivenIndexedServices(Arrays.asList(configIndex), ExecutionType.STATIC_COLLECTIONS, true);
		return "Static collections updated successfully";
	}
	
	@PostMapping("/erisyemRunBulkData")
	public String erisyemRunBulkData() {

		logger.info("erisyemRunBulkData");
		//parameterGenerator.generateTaskParameter(ERISYEM_CONFIG_INDEX).getMongoTemplate().getDb().drop();
		//serviceRunner.runService(erisyemService, ExecutionType.STATIC_COLLECTIONS, true);
		serviceRunner.runService(erisyemService, ExecutionType.BULK_DATA_ONLY_WITH_SINCE_PARAM, false);
		serviceRunner.runService(erisyemService, ExecutionType.BULK_DATA_WITH_BOTH_SINCE_AND_TILL_PARAM, false);
		return "Erisyem solutionCubeLocalhostService finished running for bulk data successfully";
	}
//	
//	@PostMapping("/erisyemRunStaticTasks")
//	public String erisyemRunStaticTasks() {
//
//		logger.info("erisyemRunStaticTasks");
//		parameterGenerator.generateTaskParameter(ERISYEM_CONFIG_INDEX).getMongoTemplate().getDb().drop();
//		serviceRunner.runService(erisyemService, ExecutionType.STATIC_COLLECTIONS, true);
//		return "Erisyem solutionCubeLocalhostService finished running for static tasks asynchronously successfully";
//	}
//
	
	@PostMapping("/VanucciRunBulkData")
	public String vanucciRunBulkData() {

		logger.info("vanucciRunBulkData");
		CacheManager.clear();
//		parameterGenerator.generateTaskParameter(VANUCCI_CONFIG_INDEX).getMongoTemplate().getDb().drop();
//		serviceRunner.runService(vanucciService, ExecutionType.STATIC_COLLECTIONS, true);
		serviceRunner.runService(vanucciService, ExecutionType.BULK_DATA_WITH_BOTH_SINCE_AND_TILL_PARAM, false);
		//serviceRunner.runService(vanucciService, ExecutionType.BULK_DATA_ONLY_WITH_SINCE_PARAM, false);
		return "Vanucci finished running for bulk data successfully";
	}
	
	
	@PostMapping("/erisyemRunStaticTasks")
	public String vanucciRunStaticTasks() {

//		logger.info("vanucciRunStaticTasks");
//		parameterGenerator.generateTaskParameter(VANUCCI_CONFIG_INDEX).getMongoTemplate().getDb().drop();
		//serviceRunner.runService(erisyemService, ExecutionType.STATIC_COLLECTIONS, true);
		return "ErisyemService finished running for static tasks asynchronously successfully";
	}
	
}