package com.solutioncube.common;

import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class TokenGenerator {

	private static final Logger logger = LoggerFactory.getLogger(TokenGenerator.class);
	
	public static String generateToken(String username, String password) {
		
		String token = "";
		
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\"username\":\""+username+"\","+"\"password\":\""+password+"\"}");
		Request request = new Request.Builder()
		  .url("https://api.triomobil.com/facility/v1/auth")
		  .post(body)
		  .addHeader("x-trio-token-ttl", "172800")
		  .addHeader("x-trio-observe-notifications", "false")
		  .addHeader("content-type", "application/json")
		  .build();

		try {
			
			Response response = client.newCall(request).execute();
			String jsonData = response.body().string();			
			JSONObject jsonObject = new JSONObject(jsonData);
			token = jsonObject.getString("token");
		} catch (IOException e) {
			
			logger.error("Error while generating token. Exception: ", e.getMessage());			
		}
		
		return token;
	}
}