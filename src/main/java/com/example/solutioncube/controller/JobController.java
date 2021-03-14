package com.example.solutioncube.controller;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import javax.validation.Valid;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solutioncube.job.DailyJob;
import com.example.solutioncube.job.OneTimeJob;
import com.example.solutioncube.payload.ScheculedRequest;
import com.example.solutioncube.payload.ScheculedResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class JobController {

	@Autowired
	private Environment env;
	
	@Autowired
	private OneTimeJob oneTimeJob;
	
	@Autowired
	private Scheduler scheduler;

	private static final Logger logger = LoggerFactory.getLogger(JobController.class);

	@RequestMapping("/")
	public String home() {
		
		return "Hello World!";
	}
	
	@PostMapping("/schedule")
	public ResponseEntity<ScheculedResponse> schedule(@Valid @RequestBody ScheculedRequest scheculedRequest) {

		try {

			Calendar c = Calendar.getInstance();
			TimeZone tz = c.getTimeZone();
			ZonedDateTime dateTime = ZonedDateTime.of(scheculedRequest.getDateTime(), tz.toZoneId());			

			if (dateTime.isBefore(ZonedDateTime.now())) {
				ScheculedResponse sheculedResponse = new ScheculedResponse(false,
						"DateTime must be after current time");
				return ResponseEntity.badRequest().body(sheculedResponse);
			}
			
			oneTimeJob.execute();
			
			JobDetail jobDetail = buildJobDetail(scheculedRequest);
			
			Trigger trigger = buildJobTrigger(jobDetail, dateTime);
			
			scheduler.scheduleJob(jobDetail, trigger);
			
			ScheculedResponse scheculedResponse = new ScheculedResponse(true, jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "SolutionCube job started Successfully!");
			
			return ResponseEntity.ok(scheculedResponse);
		} catch (SchedulerException ex) {
			
			logger.error("Error scheduling SolutionCube job", ex);
			ScheculedResponse scheculedResponse = new ScheculedResponse(false, "Error scheduling job. Please try later!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheculedResponse);
		}
	}

	private JobDetail buildJobDetail(ScheculedRequest scheculedRequest) {
		
		return JobBuilder.newJob(DailyJob.class).withIdentity(UUID.randomUUID().toString(), "Solutioncube")
				.withDescription("SolutionCube tablolarını besler").storeDurably().build();
	}

	private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
		
		return TriggerBuilder.newTrigger().forJob(jobDetail)
				.withIdentity(jobDetail.getKey().getName(), "SolutionCube")
				.withDescription("SolutionCube tablolarını besler").startAt(Date.from(startAt.toInstant()))
				.withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(Integer.parseInt(env.getProperty("custom.intervalAsMinutes")))).build();
	}

}
