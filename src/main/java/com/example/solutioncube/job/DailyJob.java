package com.example.solutioncube.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.example.solutioncube.job.task.AlarmRulesTask;
import com.example.solutioncube.job.task.SensorsTask;

@Component
public class DailyJob extends QuartzJobBean  {
	
	@Autowired
	JobParameter jobParameter;

	@Autowired
	SensorsTask sensorsTask;
	
	@Autowired
	AlarmRulesTask alarmRulesTask;
	

	@Override
	protected void executeInternal (JobExecutionContext jobExecutionContext) throws JobExecutionException {

		jobParameter.generateJobParameter();
		
		sensorsTask.executeDaily();
		alarmRulesTask.executeDaily();
	}
}