package com.example.solutioncube.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.example.solutioncube.pojo.Firm;

@ConfigurationProperties(prefix = "config")
@Configuration
public class Config {
	
	private Firm[] firms;
	
	private int interval;
	
	private String mongoDbUri;

	public Firm[] getFirms() {
		return firms;
	}

	public void setFirms(Firm[] firms) {
		this.firms = firms;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getMongoDbUri() {
		return mongoDbUri;
	}

	public void setMongoDbUri(String mongoDbUri) {
		this.mongoDbUri = mongoDbUri;
	}
}