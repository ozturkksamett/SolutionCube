package com.solutioncube.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solutioncube.service.SolutionCubeLocalhostService;

@RestController
public class Controller {

	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	
	@Autowired
	SolutionCubeLocalhostService solutionCubeLocalhostService;
	
	@PostMapping("/copy-db-into-local")
	public String copyDbIntoLocal() {

		logger.info("copyDbIntoLocal");
		solutionCubeLocalhostService.migrateProdDbIntoLocal();
		return "SolutionCubeLocalhostService finished running successfully";
	}
}
