package com.solutioncube.common;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.pojo.ApiResponse;
import com.solutioncube.pojo.Firm;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TokenGenerator {

	private static final Logger logger = LoggerFactory.getLogger(TokenGenerator.class);
	
	public static String generateToken(Firm firm) {

		String token = "";
		
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create("{\"username\":\""+firm.getUsername()+"\","+"\"password\":\""+firm.getPassword()+"\"}", mediaType);
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
			
			logger.error("\nError while generating token."
					+ "\nApiResponse: " + apiResponse.toString() 
					+ "\nException: " + e.getMessage());			
		}

		return token;
	}
}