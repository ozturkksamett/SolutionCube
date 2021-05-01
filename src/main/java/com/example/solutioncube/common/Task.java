package com.example.solutioncube.common;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

@Component
public class Task {

	private static final Logger logger = LoggerFactory.getLogger(Task.class);
	
	private MongoTemplate mongoTemplate;
	
	public void execute(TaskParameter taskParameter) { 

		String firmName = taskParameter.getFirm().getName();
		String uri = taskParameter.getUri();
		String collectionName = taskParameter.getCollectionName();
		this.mongoTemplate = taskParameter.getMongoTemplate();
		
		logger.info("firm: " + firmName + " collection name: " + collectionName + " çalışması yapılıyor.."); 
		
		OkHttpClient client = new OkHttpClient();		
		client.setConnectTimeout(30, TimeUnit.MINUTES);
		client.setReadTimeout(30, TimeUnit.MINUTES);
		
		Request request = new Request.Builder().url(uri).get().addHeader("authorization", taskParameter.getToken()).build();
		
		String jsonResponse = "";
		int errorIndex = 0;
		try {
			
			Response response = client.newCall(request).execute();
			
			jsonResponse = response.body().string();
			
			JSONArray jsonArray = new JSONArray(jsonResponse);
			
			logger.info("firm: " + firmName + " collection name : " + collectionName + " colllection size : " + jsonArray.length());
			
			for (int i = 0; i < jsonArray.length(); i++) {
				
				errorIndex = i;
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				if(taskParameter.getId() != null) {

				    jsonObject.put(taskParameter.getIdColumnName(), taskParameter.getId());
				}

				try {

					save(jsonObject.toString(), collectionName);
					logger.info("firm: " + firmName + " collection name: " + collectionName + " JsonArray["+i+"] saved successfully.");	
				} catch (Exception e) {

					logger.error("Error while saving " + firmName + " collection name: " + collectionName + " at JsonArray["+errorIndex+"]." + " Exception is " + e.getMessage());
				}
			}	
		} catch (Exception e) {

			logger.error("Error while executing " + firmName + " collection name: " + collectionName + " at JsonArray["+errorIndex+"]" + ". Exception is " + e.getMessage());		
			
			JSONObject logJsonObject = new JSONObject();
			logJsonObject.put("Firm Name", firmName);
			logJsonObject.put("Collection Name", collectionName);
			logJsonObject.put("URI", uri);
			logJsonObject.put("Response", jsonResponse);
			logJsonObject.put("Exception Time", LocalDateTime.now());
			logJsonObject.put("Exception Message", e.getMessage());
			logJsonObject.put("Exception Stack Trace", e.getStackTrace());
			save(logJsonObject.toString(), "Logs");
		}
	}
	
	private void save(String data, String collectionName) {

		BasicDBObject basicDBObject = BasicDBObject.parse(data);
		mongoTemplate.insert(basicDBObject, collectionName);
	}
}