package com.solutioncube.pojo;

import com.squareup.okhttp.Headers;

public class ApiResponse {

	private String responseBody;
	
	private Headers headers;
	
	public ApiResponse(String responseBody, Headers headers) {
		
		this.responseBody = responseBody;
		this.headers = headers;
	}
	
	public String getResponseBody() {
		return responseBody;
	}
	
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
	
	public Headers getHeaders() {
		return headers;
	}
	
	public void setHeaders(Headers headers) {
		this.headers = headers;
	}

	@Override
	public String toString(){

		return new StringBuilder()
		        .append(" ResponseBody: " + this.responseBody.toString())
		        .append(" Headers: " + this.headers.toString())
		        .toString();
	}
}