package com.denghb.donglixia.tools;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonHelper {

	/** GSON对象 */
	private static final Gson GSON = new Gson();

	/**
	 * 得到返回的JSON对象
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String, Object> jsonStrngToMap(String json) {
		return GSON.fromJson(json, new TypeToken<Map<String, Object>>() {
		}.getType());
	}
}
