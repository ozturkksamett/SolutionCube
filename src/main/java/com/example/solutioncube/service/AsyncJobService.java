package com.example.solutioncube.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsyncJobService {
	
	@Autowired
	private ErisyemService erisyemService;
	
	//@Autowired
	//private VanucciService vanucciService;
	
	public void runAsync() {

		erisyemService.run();
		//vanucciService.run();
	}
}
