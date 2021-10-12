package com.solutioncube.helper;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheManager {

	private static final Logger logger = LoggerFactory.getLogger(CacheManager.class);

	private static Map<String, JSONArray> tasks;

	public static void add(String key, JSONArray value) {

		if (tasks == null)
			clear();

		tasks.merge(key, value, JSONArray::put);
	}

	public static JSONArray get(String key) {

		if (tasks == null)
			return null;

		return tasks.get(key);
	}
	
	public static void clear() {

		tasks = new HashMap<String, JSONArray>();
		logger.info("Cache cleaned");
	}
}
