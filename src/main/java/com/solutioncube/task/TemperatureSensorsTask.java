package com.solutioncube.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.pojo.TaskParameter;

public class TemperatureSensorsTask implements ITask {
	
	private static final Logger logger = LoggerFactory.getLogger(TemperatureSensorsTask.class);
	private final String COLLECTION_NAME = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/temperatureSensors?_perPage=50";

	@Override
	public void execute(TaskParameter taskParameter) {
		
		logger.info("Execution Started");
		taskParameter.setUri(URI);
		taskParameter.setCollectionName(COLLECTION_NAME);		
		new Task().execute(taskParameter);
		logger.info("Execution Done");
	}

}