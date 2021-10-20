package com.solutioncube.pojo;

public class Firm {

	private int configIndex;
	
	private String name;
	
	private String username;
	
	private String password;
	
	public int getConfigIndex() {
		return configIndex;
	}

	public void setConfigIndex(int configIndex) {
		this.configIndex = configIndex;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString(){

		return new StringBuilder()
		        .append("\nName: " + this.name.toString())
		        .toString();
	}
}