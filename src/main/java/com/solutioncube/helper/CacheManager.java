package com.solutioncube.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheManager {

	private static final Logger logger = LoggerFactory.getLogger(CacheManager.class);

	private static Map<String, List<JSONObject>> tasks;

	public static void add(String key, List<JSONObject> value) {

		if (tasks == null)
			clear();

		if(tasks.containsKey(key)) {
			List<JSONObject> oldValue = tasks.get(key);
			value.addAll(oldValue);
			tasks.put(key, value);
		}
		else 
			tasks.put(key, value);
		
		logger.info(key+ " cached");
	}
	
	public static void remove(String key) {

		if (tasks == null)
			clear();

		if(tasks.containsKey(key)) 
			tasks.remove(key);		
		
		logger.info(key+ " removed");
	}
	
	public static List<JSONObject> get(String key) {

		if (tasks == null)
			return null;

		return tasks.get(key);
	}
	
	public static void clear() {

		tasks = new HashMap<String, List<JSONObject>>();
		logger.info("Cache cleaned");
	}
}
