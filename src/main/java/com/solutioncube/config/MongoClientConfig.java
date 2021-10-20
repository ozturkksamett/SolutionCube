package com.solutioncube.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoClientConfig {

//	@Value("${PROD_MONGODB_URI}")
//	private String prodConnString;
	
//	@Bean
//	public MongoClient prodMongoClient() {
//		
//    	return null; //MongoClients.create(prodConnString); 
//    }
	
	@Bean
	public MongoClient mongoClient() {

    	return MongoClients.create(System.getenv("MONGODB_URI")); 
    }
}