package com.example.solutioncube.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solutioncube.job.OneTimeJob;

@RestController
public class JobController {
	
	@Autowired
	private OneTimeJob oneTimeJob;
	
	@RequestMapping("/")
	public String home() {

		return "Hello World! LocalDateTime is " + LocalDateTime.now();
	}

	@PostMapping("/executeOneTime")
	public String executeOneTime() {

		oneTimeJob.execute();
		return "One Time Job executed successfully";
	}
}
