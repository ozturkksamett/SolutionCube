package com.solutioncube.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;

public class SolutionCubeDAO {

	private static final Logger logger = LoggerFactory.getLogger(SolutionCubeDAO.class);
	
	public static void saveJsonData(MongoTemplate mongoTemplate, String collectionName, List<JSONObject> jsonObjects) {

		List<BasicDBObject> basicDBObjectList = new ArrayList<BasicDBObject>();

		for (int i = 0; i < jsonObjects.size(); i++) 
			basicDBObjectList.add(BasicDBObject.parse(jsonObjects.get(i).toString()));

		try {

			mongoTemplate.insert(basicDBObjectList, collectionName);
			//logger.info(collectionName + " - " + jsonObjects.size() + " saved successfully.");
		} catch (Exception e) {

			logger.error("\nError while saving." + "\nCollection: " + collectionName + "\nException: " + e.getMessage());
		}
	}
}
