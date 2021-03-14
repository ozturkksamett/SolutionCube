package com.example.solutioncube.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.solutioncube.job.JobParameter;
import com.example.solutioncube.job.Task;

@Component
public class TrackersTask {

	@Autowired
	JobParameter jobParameter;
	
	@Autowired
	private Task task;

	private final String COLLECTION_NAME = this.getClass().getName().substring(34, this.getClass().getName().length() - 4);
	private final String URI_BLE = "https://api.triomobil.com/facility/v1/trackers?%s_sortOrder=ASC&_sortBy=label&type=ble";
	private final String URI_UA = "https://api.triomobil.com/facility/v1/trackers?%s_sortOrder=ASC&_sortBy=label&type=ua";
	private final String URI_BLE_LOGGER = "https://api.triomobil.com/facility/v1/trackers?%s_sortOrder=ASC&_sortBy=label&type=ble-logger";
	private final String URI_UA_LOGGER = "https://api.triomobil.com/facility/v1/trackers?%s_sortOrder=ASC&_sortBy=label&type=ua-logger";
  
	public void executeDaily() {
		
		task.execute(String.format(URI_BLE, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
		task.execute(String.format(URI_UA, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
		task.execute(String.format(URI_BLE_LOGGER, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
		task.execute(String.format(URI_UA_LOGGER, "audit.createdAt.since=" + jobParameter.getSinceDate() + "&"), COLLECTION_NAME);
	}

	public void executeOneTime() {
		
		task.execute(String.format(URI_BLE, ""), COLLECTION_NAME);
		task.execute(String.format(URI_UA, ""), COLLECTION_NAME);
		task.execute(String.format(URI_BLE_LOGGER, ""), COLLECTION_NAME);
		task.execute(String.format(URI_UA_LOGGER, ""), COLLECTION_NAME);
	}
}
