package com.solutioncube.controller;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.solutioncube.service.SolutionCubeLocalhostService;

@RestController
public class SolutionCubeLocalhostController {

	private static final Logger logger = LoggerFactory.getLogger(SolutionCubeLocalhostController.class);

	@Autowired
	SolutionCubeLocalhostService solutionCubeLocalhostService;

	//@PostMapping("/runMigration")
	public String runMigration() {

		logger.info("runMigration");
		Instant start = Instant.now();
		solutionCubeLocalhostService.runMigration();
		Instant finish = Instant.now();
		logger.info(String.format("Duration: %d minutes", Duration.between(start, finish).toMinutes()));
		return "SolutionCubeLocalhostService finished running successfully";
	}
}
