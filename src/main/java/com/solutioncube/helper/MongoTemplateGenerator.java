package com.solutioncube.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoClient;
import com.solutioncube.config.Config;

@Component
public class MongoTemplateGenerator {

	@Autowired
	private Config config;	

	@Autowired
	@Qualifier("prodMongoClient")
	private MongoClient prodMongoClient;

	@Autowired
	@Qualifier("mongoClient")
	private MongoClient mongoClient;
	
	public MongoTemplate generateProdMongoTemplate(int configIndex) {

		return null; //new MongoTemplate(prodMongoClient, config.getFirms()[configIndex].getName());
	}
	
	public MongoTemplate generateMongoTemplate(int configIndex) {

		return new MongoTemplate(mongoClient, config.getFirms()[configIndex].getName());
	}
}
