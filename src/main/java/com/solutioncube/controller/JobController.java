package com.solutioncube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solutioncube.common.IService;
import com.solutioncube.common.TaskParameterGenerator;
import com.solutioncube.helper.AsyncHelper;
import com.solutioncube.service.ErisyemBulkDataService;

@RestController
public class JobController {

	@Autowired
	private AsyncHelper asyncHelper;
	
	@Autowired
	private ErisyemBulkDataService erisyemBulkDataService;
	
	@Autowired
	private IService erisyemService;
	
	@Autowired
	private IService vanucciService;

	@Autowired
	TaskParameterGenerator taskParameterGenerator;
	
	@RequestMapping("/")
	public String home() {

		return "SolutionCube Running!";
	}

	@PostMapping("/erisyemRunBulkData")
	public String erisyemRunBulkData() {

		taskParameterGenerator.generateTaskParameter(0).getMongoTemplate().getDb().drop();
		asyncHelper.waitTillEndOfSynchronizedFunc(erisyemService.runStaticTasksAsync());
		erisyemBulkDataService.runBulkData();
		return "Erisyem Bulk Data Service run successfully";
	}
	
	@PostMapping("/erisyemRunStaticTasksAsync")
	public String erisyemRunStaticTasksAsync() {

		taskParameterGenerator.generateTaskParameter(0).getMongoTemplate().getDb().drop();
		erisyemService.runStaticTasksAsync();
		return "Erisyem service started running for static tasks asynchronously successfully";
	}

	@PostMapping("/vanucciRunStaticTasksAsync")
	public String vanucciRunStaticTasksAsync() {

		taskParameterGenerator.generateTaskParameter(1).getMongoTemplate().getDb().drop();
		vanucciService.runStaticTasksAsync();
		return "Vanucci service started running for static tasks asynchronously successfully";
	}
}