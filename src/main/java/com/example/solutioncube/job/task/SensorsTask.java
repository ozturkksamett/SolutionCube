package com.example.solutioncube.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.solutioncube.common.Task;
import com.example.solutioncube.common.TaskParameter;

@Component
public class SensorsTask {

	@Autowired
	Task task;

	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/sensors";

	public void executeOneTime(TaskParameter taskParameter) {
		
		taskParameter.setUri(URI);
		taskParameter.setCollectionName(COLLECTION_NAME);
		task.execute(taskParameter);
	}
}
