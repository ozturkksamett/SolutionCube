package com.example.solutioncube.job;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DailyJobScheduler {

	@Autowired
	private Environment env;

	@Autowired
	private Scheduler scheduler;
	
	@PostConstruct
	public void scheduleDailyJob() {

		TimeZone timeZone = Calendar.getInstance().getTimeZone();
		LocalDateTime localDateTime = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIDNIGHT);
		//LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(10);
		ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, timeZone.toZoneId());

		System.out.println("Schedule başladı: Şu zamanda başlayacak: " + zonedDateTime);

		JobDetail jobDetail = buildJobDetail();

		Trigger trigger = buildJobTrigger(jobDetail, zonedDateTime);

		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {

			System.err.println("Schedule edilirken hata alındı. Hata: " + e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("Schedule başarılı bir şekilde sonlandı."); 
	}


	private JobDetail buildJobDetail() {

		return JobBuilder.newJob(DailyJob.class)
				.withIdentity(UUID.randomUUID().toString(), "Solutioncube")
				.withDescription("SolutionCube tablolarını besler").storeDurably().build();
	}

	private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {

		return TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity(jobDetail.getKey().getName(), "SolutionCube")
				.withDescription("SolutionCube tablolarını besler").startAt(Date.from(startAt.toInstant()))
				.withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(Integer.parseInt(env.getProperty("custom.intervalAsMinutes"))))
				.build();
	}
}
