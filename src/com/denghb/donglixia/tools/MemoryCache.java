package com.denghb.donglixia.tools;

import java.util.WeakHashMap;

import android.util.Log;

/**
 * 缓存对象至内存
 * 
 * <pre>
 * Description
 * Copyright:	Copyright (c)2013  
 * Company:		Donglixia
 * Author:		hb
 * Version:		1.0  
 * Create at:	2014年8月23日 下午7:17:17  
 *  
 * 修改历史:
 * 日期    作者    版本  修改描述
 * ------------------------------------------------------------------
 * 
 * </pre>
 */
public class MemoryCache {

	private static final String TAG = MemoryCache.class.getSimpleName();

	private static final WeakHashMap<String, Object> cache = new WeakHashMap<String, Object>();

	private static MemoryCache memoryCache;

	private MemoryCache() {

	}

	/**
	 * 实例化
	 * 
	 * @return
	 */
	public static MemoryCache instance() {
		if (memoryCache == null) {
			memoryCache = new MemoryCache();
		}
		return memoryCache;
	}

	/**
	 * 缓存
	 * 
	 * @param key
	 * @param object
	 */
	public void cache(String key, Object object) {
		Log.d(TAG, "cache object by key: " + key);
		cache.put(key, object);
	}

	/**
	 * 缓存
	 * 
	 * @param key
	 * @param object
	 */
	public void cache(int key, Object object) {
		cache("KEY_" + key, object);
	}

	/**
	 * 读取
	 * 
	 * @param key
	 * @return
	 */
	public Object read(String key) {
		Log.d(TAG, "read object by key: " + key);
		return cache.get(key);
	}

	/**
	 * 读取
	 * 
	 * @param key
	 * @return
	 */
	public Object read(int key) {
		return read("KEY_" + key);
	}

	/**
	 * 清理
	 */
	public void clear() {
		Log.d(TAG, "clear all");
		cache.clear();
	}
}