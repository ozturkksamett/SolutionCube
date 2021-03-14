package com.example.solutioncube.job;

import java.io.IOException;

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
		System.out.println("ok http başladı");
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(uri).get().addHeader("authorization", jobParameter.getToken()).build();

		try {
			System.out.println("task başladı");
			Response response = client.newCall(request).execute();
			System.out.println("task bitti");
			String jsonData = response.body().string();
			System.out.println("jsonData:"+jsonData);
			JSONArray jsonArray = new JSONArray(jsonData);

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonObject = jsonArray.getJSONObject(i);				
				BasicDBObject basicDBObject = BasicDBObject.parse(jsonObject.toString());
				mongoTemplate.insert(basicDBObject, collectionName);
			}
		} catch (IOException e) {
			logger.error("Error execute job " + collectionName, e);
			e.printStackTrace();
		}
	}
}
