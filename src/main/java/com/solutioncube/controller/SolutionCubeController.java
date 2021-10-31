package com.solutioncube.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solutioncube.common.ExecutionType;
import com.solutioncube.common.IService;
import com.solutioncube.helper.ParameterGenerator;
import com.solutioncube.helper.ServiceRunner;
import com.solutioncube.service.SolutionCubeService;

@RestController
public class SolutionCubeController {

	private static final Logger logger = LoggerFactory.getLogger(SolutionCubeController.class);
	
	private static final int VANUCCI_CONFIG_INDEX = 1;

	@Autowired
	private SolutionCubeService solutionCubeService;
	
	@Autowired
	ParameterGenerator parameterGenerator;
	
	@Autowired
	ServiceRunner serviceRunner;
	
	@Autowired
    IService vanucciService;
	
	@PostMapping("/process-conversion")
	public String runCollectionsProcessForConversion() {
		
		logger.info("runCollectionsProcessForConversion started");		
		solutionCubeService.runCollectionsProcessForConversion();		
		return "Process for conversion finished successfully";
	}
	
	
	@PostMapping("/VanucciRunBulkData")
	public String vanucciRunBulkData() {

		logger.info("vanucciRunBulkData");
//		parameterGenerator.generateTaskParameter(VANUCCI_CONFIG_INDEX).getMongoTemplate().getDb().drop();
//		serviceRunner.runService(vanucciService, ExecutionType.BULK_DATA_WITH_BOTH_SINCE_AND_TILL_PARAM, false);
		return "Vanucci solutionCubeLocalhostService finished running for bulk data successfully";
	}
	
}
