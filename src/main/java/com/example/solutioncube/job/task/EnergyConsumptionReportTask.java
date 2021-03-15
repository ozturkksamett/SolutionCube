package com.example.solutioncube.job.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.solutioncube.job.JobParameter;
import com.example.solutioncube.job.Task;

public class EnergyConsumptionReportTask {

	@Autowired
	JobParameter jobParameter;
	
	@Autowired
	private Task task;

	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/reports/energy/consumption/summary?_sortOrder=ASC&ts.since=%s&ts.until=%s";

	public void executeDaily() {

		task.execute(String.format(URI, jobParameter.getSinceDate(), jobParameter.getTillDate()), COLLECTION_NAME);
	}
}
