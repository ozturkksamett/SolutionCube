package com.solutioncube.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.common.ITask;
import com.solutioncube.common.Task;
import com.solutioncube.pojo.Parameter;

public class Sensors implements ITask {
	
	private static final Logger logger = LoggerFactory.getLogger(Sensors.class);
	private final String COLLECTION_NAME = this.getClass().getSimpleName();
	private final String URI = "https://api.triomobil.com/facility/v1/sensors?_perPage=50";

	@Override
	public void execute(Parameter parameter) {

		logger.info("Execution Started");
		parameter.setUri(URI);
		parameter.setCollectionName(COLLECTION_NAME);
		new Task().execute(parameter);
		logger.info("Execution Done");
	}
}