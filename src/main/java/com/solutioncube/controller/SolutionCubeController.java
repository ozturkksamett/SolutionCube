package com.solutioncube.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solutioncube.service.SolutionCubeService;

@RestController
public class SolutionCubeController {

	private static final Logger logger = LoggerFactory.getLogger(SolutionCubeController.class);

	@Autowired
	private SolutionCubeService solutionCubeService;
	
	@PostMapping("/process-conversion")
	public String runCollectionsProcessForConversion() {
		
		logger.info("runCollectionsProcessForConversion started");		
		solutionCubeService.runCollectionsProcessForConversion();		
		return "Process for conversion finished successfully";
	}
}
