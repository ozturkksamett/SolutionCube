package com.example.solutioncube.job;

import java.io.IOException;
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

		System.out.println("collectionName:"+collectionName+" task çalışması yapılıyor..."); 
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(uri).get().addHeader("authorization", jobParameter.getToken()).build();
		System.out.println("URI:"+uri);
		try {
			client.setConnectTimeout(30, TimeUnit.MINUTES);
			client.setReadTimeout(30, TimeUnit.MINUTES);
			Response response = client.newCall(request).execute();
			String jsonData = response.body().string();
			System.out.println("jsonData:"+jsonData);
			JSONArray jsonArray = new JSONArray(jsonData);
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonObject = jsonArray.getJSONObject(i);				
				BasicDBObject basicDBObject = BasicDBObject.parse(jsonObject.toString());
				mongoTemplate.insert(basicDBObject, collectionName);
			}
			System.out.println("collectionName:"+collectionName+" task çalışması başarılı bir şekilde sonlandı."); 
		} catch (IOException e) {
			logger.error("Error execute job " + collectionName, e);
			e.printStackTrace();
		}
	}
}
