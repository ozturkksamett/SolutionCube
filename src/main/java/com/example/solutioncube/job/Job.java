package com.example.solutioncube.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.example.solutioncube.service.AsyncJobService;

@Component
public class Job extends QuartzJobBean {

	@Autowired
	private AsyncJobService asyncJobService;
	
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		
		asyncJobService.runAsync();
	}
}