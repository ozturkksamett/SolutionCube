package com.example.solutioncube.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.solutioncube.common.Task;
import com.example.solutioncube.common.TaskParameter;

@Component
public class EnergyConsumptionReportTask {

	@Autowired
	Task task;

	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/energy/consumption/summary?_sortOrder=ASC&ts.since=%s&ts.until=%s";

	public void executeDaily(TaskParameter taskParameter) {
		
		taskParameter.setUri(String.format(URI, taskParameter.getSinceDateAsString(), taskParameter.getNowAsString()));
		taskParameter.setCollectionName(COLLECTION_NAME);
		task.execute(taskParameter);
	}
}
