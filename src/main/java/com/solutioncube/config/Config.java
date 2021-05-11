package com.solutioncube.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.solutioncube.pojo.Firm;

@ConfigurationProperties(prefix = "config")
@Configuration
public class Config {
	
	private Firm[] firms;
	
	private String mongoDbUri;

	private int interval;
	
	public Firm[] getFirms() {
		return firms;
	}

	public void setFirms(Firm[] firms) {
		this.firms = firms;
	}

	public String getMongoDbUri() {
		return mongoDbUri;
	}

	public void setMongoDbUri(String mongoDbUri) {
		this.mongoDbUri = mongoDbUri;
	}
	
	public int getInterval() {
		return interval;
	}
	
	public void setInterval(int interval) {
		this.interval = interval;
	}
}