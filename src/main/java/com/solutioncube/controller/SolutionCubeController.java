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
	private IService vanucciService;
	
	@PostMapping("/process-conversion")
	public String runCollectionsProcessForConversion() {
		
		logger.info("runCollectionsProcessForConversion started");		
		solutionCubeService.runCollectionsProcessForConversion();		
		return "Process for conversion finished successfully";
	}
	
	

	
	
	@PostMapping("/vanucciStaticData")
	public String vanucciStaticData() {

		logger.info("vanucciStaticData");
		//parameterGenerator.generateTaskParameter(VANUCCI_CONFIG_INDEX).getMongoTemplate().getDb().drop();
		serviceRunner.runService(vanucciService, ExecutionType.STATIC_COLLECTIONS, false);
		return "vanucciStaticData data successfully";
	}
	
}
