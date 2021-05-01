package com.example.solutioncube.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Component
public class MongoTemplateGenerator {
		 
	private static String connString;
	
	@Value("${config.mongoDbUri}")
    public void setConnString(String connStr){
    	//connString = connStr; // Test
    	connString = System.getenv("MONGODB_URI"); // Production
    }
	
    public static MongoClient mongoClient() {
     	
    	return MongoClients.create(connString); 
    }

    public static MongoTemplate generateMongoTemplate(String mongoDbName) {
    	
        return new MongoTemplate(mongoClient(), mongoDbName); 
    }      
}