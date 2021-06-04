package com.solutioncube.job;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solutioncube.config.Config;

@Component
public class JobScheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(JobScheduler.class);
	
	@Autowired
	private Config config;

	@Autowired
	private Scheduler scheduler;
	
	public void scheduleDailyJob() {		

		ZonedDateTime zonedDateTime = getZonedDateTime();
		JobDetail jobDetail = buildJobDetail(SolutionCubeJob.class, "SolutionCubeJob");		
		Trigger jobTrigger = buildJobTriggerForForever(jobDetail, zonedDateTime);

		try {
			
			scheduler.scheduleJob(jobDetail, jobTrigger);
		} catch (SchedulerException e) {

			logger.error("Error while scheduling SolutionCubeJob. Exception: " + e.getMessage());			
		}
		
		logger.info(String.format("SolutionCubeJob scheduled successfully. Scheduleld time: %t", zonedDateTime));
	}

	private ZonedDateTime getZonedDateTime() {
		
		LocalDateTime localDateTime = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIDNIGHT).plusHours(21);
		ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, Calendar.getInstance().getTimeZone().toZoneId());		
		
		return zonedDateTime;
	}

	private JobDetail buildJobDetail(Class <? extends Job> jobClass, String jobName) {

		return JobBuilder.newJob(jobClass)
				.withIdentity(jobName, "SolutionCube")
				.withDescription("TrioMobil verilerini SolutionCube veritabanına taşır").storeDurably().build();
	}

	private Trigger buildJobTriggerForForever(JobDetail jobDetail, ZonedDateTime startAt) {

		return TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity(jobDetail.getKey().getName(), "SolutionCube")
				.withDescription("SolutionCube SolutionCubeJob Trigger").startAt(Date.from(startAt.toInstant()))
				.withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(config.getInterval()))
				.build();
	}
}