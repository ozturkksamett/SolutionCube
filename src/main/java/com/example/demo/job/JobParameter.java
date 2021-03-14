package com.example.demo.job;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JobParameter {

	@Autowired
	private Environment env;
	
	private String token;
	private String sinceDate;
	private String tillDate;

	public String getToken() {
		return token;
	}

	public String getSinceDate() {
		return sinceDate;
	}

	public String getTillDate() {
		return tillDate;
	}
	
	public void generateJobParameter() {
		this.token = generateToken();
		this.sinceDate = generateDate(LocalDateTime.now().minusMinutes(Integer.parseInt(env.getProperty("custom.intervalAsMinutes"))));
		this.tillDate = generateDate(LocalDateTime.now());
	}
 
	private String generateDate(LocalDateTime date) {
		
		//ex:2021-03-14T01%3A14%3A00.000%2B00%3A00
		return date.getYear()+"-"
				+String.format("%02d", date.getMonthValue())+"-"
				+String.format("%02d", date.getDayOfMonth())+"T"
				+String.format("%02d", date.getHour())+"%3A"
				+String.format("%02d", date.getMinute())+"%3A"
				+String.format("%02d", date.getSecond())+".000%2B00%3A00";
	}
	
	private String generateToken() {
		
		return TokenGenerator.generateToken(env.getProperty("spring.security.user.name"), env.getProperty("spring.security.user.password"));
	}
}
