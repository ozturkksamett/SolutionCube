package com.example.demo.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.job.JobParameter;
import com.example.demo.job.Task;

@Component
public class SensorsTask {

	@Autowired
	JobParameter jobParameter;
	
	@Autowired
	private Task task;

	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/sensors?%s_sortBy=label&_sortOrder=ASC";
  
	public void executeDaily() {
		
		task.execute(String.format(URI, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
	}

	public void executeOneTime() {
		
		task.execute(String.format(URI, ""), COLLECTION_NAME);
	}
}
