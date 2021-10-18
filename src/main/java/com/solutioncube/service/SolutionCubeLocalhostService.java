package com.solutioncube.service;

import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solutioncube.config.Config;
import com.solutioncube.dao.SolutionCubeDAO;
import com.solutioncube.helper.MongoTemplateGenerator;
import com.solutioncube.pojo.Firm;

@Service
public class SolutionCubeLocalhostService {

	@Autowired
	private Config config;	
	
	@Autowired
	private MongoTemplateGenerator mongoTemplateGenerator;

	public void migrateProdDbIntoLocal() {

		for (Firm firm : config.getFirms()) 
			migrate(firm.getConfigIndex());		
	}

	private void migrate(int configIndex) {

		Set<String> collectionNames = mongoTemplateGenerator.generateProdMongoTemplate(configIndex).getCollectionNames();
		for (String collectionName : collectionNames) {			 
			List<JSONObject> collectionData = mongoTemplateGenerator.generateProdMongoTemplate(configIndex).findAll(JSONObject.class, collectionName);
			SolutionCubeDAO.saveBulkJsonData(mongoTemplateGenerator.generateMongoTemplate(configIndex), collectionName, collectionData);
		}
	}
}
