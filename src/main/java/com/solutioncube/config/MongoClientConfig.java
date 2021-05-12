package com.solutioncube.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoClientConfig {

	@Autowired
	private Config config;
	
    public @Bean MongoClient mongoClient() {
    	
    	String connString;
    	//connString = System.getenv("MONGODB_URI"); // Production
    	connString = config.getMongoDbUri(); // Test
    	return MongoClients.create(connString); // Test 
    }
}