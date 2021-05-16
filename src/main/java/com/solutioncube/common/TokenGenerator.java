package com.solutioncube.common;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.pojo.ApiResponse;
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

		ApiResponse apiResponse = null;
		try {
			
			Response response = client.newCall(request).execute();
			apiResponse = new ApiResponse(response.body().string(), response.headers());
			JSONObject jsonObject = new JSONObject(apiResponse.getResponseBody());
			token = jsonObject.getString("token");
		} catch (Exception e) {
			
			logger.error("Error while generating token. ApiResponse: " + apiResponse.toString() + " Exception: " + e.getMessage());			
		}

		return token;
	}
}