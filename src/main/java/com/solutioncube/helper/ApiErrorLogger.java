package com.solutioncube.helper;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.pojo.ApiErrorLog;
import com.solutioncube.pojo.ApiResponse;
import com.solutioncube.pojo.Parameter;

public class ApiErrorLogger {

	private static final Logger logger = LoggerFactory.getLogger(ApiErrorLogger.class);

	private static ArrayList<ApiErrorLog> apiErrors;

	public static void log(Parameter parameter, ApiResponse apiResponse, Exception e) {

		if(apiErrors == null)
			clear();
		
		apiErrors.add(new ApiErrorLog(parameter.getCollectionName(), 
				"\nError while calling api." 
						+ "\nUri: " + parameter.getUri() 
						+ "\nToken: " + parameter.getToken() 
						+ "\nApi Response: " + apiResponse.toString() 
						+ "\nException: " + e.getMessage()));
	}

	public static void print() {

		apiErrors.forEach(apiError -> {

			logger.error(apiError.toString());
		});
	}

	public static void clear() {

		apiErrors = new ArrayList<ApiErrorLog>();
	}

	public static ArrayList<ApiErrorLog> getApiErrors() {
		
		return apiErrors;
	}
}