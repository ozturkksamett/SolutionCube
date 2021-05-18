package com.solutioncube.common;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solutioncube.pojo.ApiResponse;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class ApiCaller {

	private static final Logger logger = LoggerFactory.getLogger(ApiCaller.class);
	
	public static ApiResponse call(Request request) {
		
		try {

			URL proxyUrl = new URL(System.getenv("PROXIMO_URL"));
		
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl.getHost(), 80));
			
			String userInfo = proxyUrl.getUserInfo();
			String username = userInfo.substring(0, userInfo.indexOf(':'));
			String password = userInfo.substring(userInfo.indexOf(':') + 1);
			Authenticator proxyAuthenticator = new Authenticator() {
				  @Override 
				  public Request authenticate(Route route, Response response) {
				       String credential = Credentials.basic(username, password);
				       return response.request().newBuilder()
				           .header("Proxy-Authorization", credential)
				           .build();
				  }				
			};
			
			OkHttpClient client = new OkHttpClient.Builder()
					.connectTimeout(60, TimeUnit.MINUTES)
					.writeTimeout(60, TimeUnit.MINUTES)
					.readTimeout(60, TimeUnit.MINUTES)
					.proxy(proxy)
					.proxyAuthenticator(proxyAuthenticator)
					.build();		

			Response response = client.newCall(request).execute();
			
			return new ApiResponse(response.body().string(), response.headers());
		} catch (Exception e) {

			logger.error("\nError while calling api." + "\nException: " + e.getMessage());
		}
		
		return null;
	}
}