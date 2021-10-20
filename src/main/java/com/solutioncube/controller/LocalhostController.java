package com.solutioncube.controller;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.solutioncube.service.SolutionCubeLocalhostService;

@RestController
public class LocalhostController {

	private static final Logger logger = LoggerFactory.getLogger(LocalhostController.class);

	@Autowired
	SolutionCubeLocalhostService solutionCubeLocalhostService;

	// @PostMapping("/copy-db-into-local")
	public String copyDbIntoLocal() {

		logger.info("copyDbIntoLocal");
		Instant start = Instant.now();
		solutionCubeLocalhostService.copyDbIntoLocal();
		Instant finish = Instant.now();
		logger.info(String.format("Duration: %d minutes", Duration.between(start, finish).toMinutes()));
		return "SolutionCubeLocalhostService finished running successfully";
	}
}
