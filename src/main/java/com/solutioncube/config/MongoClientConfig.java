package com.solutioncube.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoClientConfig {

    public @Bean MongoClient mongoClient() {
    	
    	return MongoClients.create(System.getenv("MONGODB_URI")); 
    }
}