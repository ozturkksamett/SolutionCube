package com.solutioncube.pojo;

public class ApiErrorLog {

	private String collection;
	private String error;
	
	public ApiErrorLog(String collection, String error) {

		setCollection(collection);
		setError(error);
	}
	
	public String getCollection() {
		return collection;
	}
	
	public void setCollection(String collection) {
		this.collection = collection;
	}
	
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
	
	@Override
	public String toString() {
		return "ApiErrorLog [Collection=" + collection + ", Error=" + error + "]";
	}
}