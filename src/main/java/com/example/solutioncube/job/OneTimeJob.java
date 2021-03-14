package com.example.solutioncube.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.solutioncube.job.task.AlarmRulesTask;
import com.example.solutioncube.job.task.SensorsTask;

@Component
public class OneTimeJob {

	@Autowired
	JobParameter jobParameter;
	
	@Autowired
	SensorsTask sensorsTask;
	
	@Autowired
	AlarmRulesTask alarmRulesTask;

	public void execute() {
		System.out.println("job parameter başladı");
		jobParameter.generateJobParameter();
		
		System.out.println("Sensor task başladı");
		sensorsTask.executeOneTime();
		
		alarmRulesTask.executeOneTime();
	}
}
