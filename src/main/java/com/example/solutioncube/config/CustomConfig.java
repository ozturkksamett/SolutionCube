package com.example.solutioncube.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "custom")
public class CustomConfig {

	private int intervalAsMinutes; 

	public int getIntervalAsMinutes() {
		return intervalAsMinutes;
	}

	public void setIntervalAsMinutes(int intervalAsMinutes) {
		this.intervalAsMinutes = intervalAsMinutes;
	}

}
