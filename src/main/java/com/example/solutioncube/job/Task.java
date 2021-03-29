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
		
		try {
			
			Response response = client.newCall(request).execute();
			
			jsonResponse = response.body().string();
			
			JSONArray jsonArray = new JSONArray(jsonResponse);
			
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				if(jobParameter.getId() != null) {

				    jsonObject.put(jobParameter.getIdColumnName(), jobParameter.getId());
				}

				save(jsonObject.toString(), collectionName);
			}
			
			logger.info("collectionName:"+collectionName+" çalışması başarılı bir şekilde sonlandı."); 		
		} catch (Exception e) {

			logger.error("Error execute job " + collectionName, e);
			e.printStackTrace();
			
			JSONObject logJsonObject = new JSONObject();
			logJsonObject.put("Collection Name", collectionName);
			logJsonObject.put("URI", uri);
			//logJsonObject.put("Response", jsonResponse);
			logJsonObject.put("Exception Message", e.getMessage());
			//logJsonObject.put("Exception Stack Trace", e.getStackTrace());
			logJsonObject.put("Exception Time", LocalDateTime.now());
			save(logJsonObject.toString(), "logs");
		}
	}
	
	private void save(String data, String collectionName) {

		BasicDBObject basicDBObject = BasicDBObject.parse(data);
		mongoTemplate.insert(basicDBObject, collectionName);
	}
}
