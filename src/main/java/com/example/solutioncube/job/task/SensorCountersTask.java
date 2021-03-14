package com.example.solutioncube.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.solutioncube.job.JobParameter;
import com.example.solutioncube.job.Task;

@Component
public class SensorCountersTask {

	@Autowired
	JobParameter jobParameter;

	@Autowired
	private Task task;

	private final String COLLECTION_NAME = this.getClass().getName().substring(34,
			this.getClass().getName().length() - 4);
	private final String URI = "https://api.triomobil.com/facility/v1/sensorCounters?%s_sortOrder=ASC&_sortBy=label";

	public void executeDaily() {

		task.execute(String.format(URI, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
	}

	public void executeOneTime() {

		task.execute(String.format(URI, ""), COLLECTION_NAME);
	}
}
