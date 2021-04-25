package com.example.solutioncube.job;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

@Component
public class Task {
	
	@Autowired
	JobParameter jobParameter;

	@Autowired
	private MongoTemplate mongoTemplate;

	private static final Logger logger = LoggerFactory.getLogger(Task.class);

	public void execute(String uri, String collectionName) { 

		logger.info("collectionName:"+collectionName+" çalışması yapılıyor..."); 
		
		OkHttpClient client = new OkHttpClient();		
		client.setConnectTimeout(30, TimeUnit.MINUTES);
		client.setReadTimeout(30, TimeUnit.MINUTES);
		
		Request request = new Request.Builder().url(uri).get().addHeader("authorization", jobParameter.getToken()).build();
		
		String jsonResponse = "";
		int errorIndex = 0;
		try {
			
			Response response = client.newCall(request).execute();
			
			jsonResponse = response.body().string();
			
			JSONArray jsonArray = new JSONArray(jsonResponse);
			
			logger.info("Collection Name : " + collectionName + " - Colllection Size : " + jsonArray.length());
			
			for (int i = 0; i < jsonArray.length(); i++) {
				
				errorIndex = i;
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				if(jobParameter.getId() != null) {

				    jsonObject.put(jobParameter.getIdColumnName(), jobParameter.getId());
				}

				try {

					save(jsonObject.toString(), collectionName);
					logger.info("Task: " + collectionName + " JsonArray["+i+"] saved successfully.");	
				} catch (Exception e) {

					logger.error("Error while executing task: " + collectionName + " JsonArray["+errorIndex+"]." + " Exception is " + e.getMessage());
				}
			}	
		} catch (Exception e) {

			logger.error("Error while executing task: " + collectionName + " at JsonArray["+errorIndex+"]" + ". Exception is " + e.getMessage());		
			
			JSONObject logJsonObject = new JSONObject();
			logJsonObject.put("Collection Name", collectionName);
			logJsonObject.put("URI", uri);
			logJsonObject.put("Response", jsonResponse.substring(0, 255));
			logJsonObject.put("Exception Time", LocalDateTime.now());
			logJsonObject.put("Exception Message", e.getMessage());
			logJsonObject.put("Exception Stack Trace", e.getStackTrace());
			save(logJsonObject.toString(), "logs");
		}
	}
	
	private void save(String data, String collectionName) {

		BasicDBObject basicDBObject = BasicDBObject.parse(data);
		mongoTemplate.insert(basicDBObject, collectionName);
	}
}
