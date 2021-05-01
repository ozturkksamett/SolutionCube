package com.example.solutioncube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solutioncube.config.Config;
import com.example.solutioncube.service.ErisyemService;
import com.example.solutioncube.service.VanucciService;

@RestController
public class JobController {
	
	@Autowired
	private ErisyemService erisyemService;

	@Autowired
	private VanucciService vanucciService;

	@Autowired
	private Config config;

	@RequestMapping("/")
	public String home() {

		return "Hello World!";
	}

	@PostMapping("/erisyem-one-time-run")
	public String erisyemOneTimeRun() {

		erisyemService.oneTimeRun();
		return "Erisyem service run successfully";
	}

	@PostMapping("/vanucci-one-time-run")
	public String vanucciOneTimeRun() {

		vanucciService.oneTimeRun();
		return "Vanucci service run successfully";
	}
	
	@PostMapping("/debugging")
	public String debugging() {
		
		return config.getFirms()[0].getName();
	}
}