package com.solutioncube.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.solutioncube.config.Config;
import com.solutioncube.dao.SolutionCubeDAO;
import com.solutioncube.helper.MongoTemplateGenerator;
import com.solutioncube.pojo.Firm;

@Service
public class SolutionCubeLocalhostService {

	private static final Logger logger = LoggerFactory.getLogger(SolutionCubeLocalhostService.class);
	
	@Autowired
	Config config;	
	
	@Autowired
	MongoTemplateGenerator mongoTemplateGenerator;

	public void copyDbIntoLocal() {
		
		for (Firm firm : config.getFirms()) {
			mongoTemplateGenerator.generateMongoTemplate(firm.getConfigIndex()).getDb().drop();
			runMigration(firm.getConfigIndex());		
		}

		logger.info("copyDbIntoLocal finished");
	}

	private void runMigration(int configIndex) {
		
		Set<String> collectionNames = mongoTemplateGenerator.generateProdMongoTemplate(configIndex).getCollectionNames();
		collectionNames.remove("system.views");
		for (String collectionName : collectionNames) {		

			List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
			MongoCollection<Document> mongoCollection = mongoTemplateGenerator.generateProdMongoTemplate(configIndex).getCollection(collectionName);
			logger.info(collectionName+" - "+mongoCollection.countDocuments()+" saving..");			
			FindIterable<Document> iterable = mongoCollection.find();
			iterable.noCursorTimeout(false);
			for (Iterator<Document> iterator = iterable.iterator(); iterator.hasNext();) {
				Document document = (Document) iterator.next();
				if(document.containsKey("_id"))
					document.remove("_id");
				jsonObjects.add(new JSONObject(document.toJson()));
				if(jsonObjects.size() == 10000) {

					SolutionCubeDAO.saveBulkJsonData(mongoTemplateGenerator.generateMongoTemplate(configIndex), collectionName, jsonObjects);	
					jsonObjects = new ArrayList<JSONObject>();
				}
			}
			SolutionCubeDAO.saveBulkJsonData(mongoTemplateGenerator.generateMongoTemplate(configIndex), collectionName, jsonObjects);	
		}
	}
}
