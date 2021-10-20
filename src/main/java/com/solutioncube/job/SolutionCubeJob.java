package com.solutioncube.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.solutioncube.helper.EmailSender;
import com.solutioncube.service.SolutionCubeJobService;

@Component
public class SolutionCubeJob extends QuartzJobBean {
	
	private static final Logger logger = LoggerFactory.getLogger(SolutionCubeJob.class);

	@Autowired
	SolutionCubeJobService solutionCubeJobService;

	@Autowired
	EmailSender emailSender;
	
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		
		logger.info("SolutionCubeJob started running");	
		solutionCubeJobService.runDailySolutionCubeJob(false);	
		emailSender.sendDailyJobReportMail();
		logger.info("SolutionCubeJob finished running");	
	}
}