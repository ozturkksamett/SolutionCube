package com.example.demo.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.job.task.AlarmRulesTask;
import com.example.demo.job.task.SensorsTask;

@Component
public class OneTimeJob {

	@Autowired
	JobParameter jobParameter;
	
	@Autowired
	SensorsTask sensorsTask;
	
	@Autowired
	AlarmRulesTask alarmRulesTask;

	public void execute() {
		
		jobParameter.generateJobParameter();
		
		sensorsTask.executeOneTime();
		alarmRulesTask.executeOneTime();
	}
}
