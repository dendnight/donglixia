package com.denghb.donglixia.obtain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;

import com.denghb.donglixia.Constants;
import com.denghb.donglixia.http.HttpRetriever;
import com.denghb.donglixia.model.Donglixia;
import com.denghb.donglixia.tools.Helper;
import com.denghb.donglixia.tools.JsonHelper;
import com.google.gson.internal.LinkedTreeMap;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 获取“东篱下”的数据
 * 
 * <pre>
 * Description
 * Copyright:	Copyright (c)2012  
 * Company:		东篱下
 * Author:		denghb
 * Version:		1.0  
 * Create at:	2015年1月31日 下午7:29:55  
 *  
 * 修改历史:
 * 日期    作者    版本  修改描述
 * ------------------------------------------------------------------
 * 
 * </pre>
 */
public class MainObtain extends Thread {

	private static final String TAG = MainObtain.class.getSimpleName();

	private Context context;

	private Handler handler;

	private String url;

	public MainObtain(Context context, Handler handler, String url) {
		this.context = context;
		this.handler = handler;
		this.url = url;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		if (!Helper.checkConnection(context)) {
			return;
		}
		// 请求完毕发消息
		Message msg = new Message();
		List<Donglixia> list = new ArrayList<Donglixia>();

		// 获取
		int total = 0;
		int status = 0;
		try {

			HttpRetriever httpRetriever = new HttpRetriever();
			HttpResponse response = httpRetriever.requestPost(url, null);

			String json = httpRetriever.decodeToJsonString(response);

			Map<String, Object> map = JsonHelper.jsonStrngToMap(json);

			total = (int) (Double.parseDouble(map.get(Constants.JSON.TOTAL).toString()));
			status = (int) (Double.parseDouble(map.get(Constants.JSON.STATUS).toString()));
			Object data = map.get(Constants.JSON.DATA);

			// 主要数据
			if (data instanceof LinkedTreeMap<?, ?>) {
				LinkedTreeMap<?, ?> linkedTreeMap = (LinkedTreeMap<?, ?>) data;
				Set<?> keySet = linkedTreeMap.keySet();// 获取map的key值的集合，set集合
				for (Object key : keySet) {// 遍历key
					LinkedTreeMap<?, ?> donglixaMap = (LinkedTreeMap<?, ?>) linkedTreeMap.get(key);
					Integer id = Integer.valueOf(key.toString());
					Object obj = donglixaMap.get(Constants.JSON.TAG);
					String tag = (null == obj?"":obj.toString());
					Integer love = Integer.valueOf(donglixaMap.get(Constants.JSON.LOVE).toString());
					ArrayList<String> urls = (ArrayList<String>) donglixaMap.get(Constants.JSON.URLS);
					list.add(new Donglixia(id, tag, love, urls));
				}
			}

		} catch (Exception e) {
			Log.d(TAG, e.getMessage(), e);
		}
		// 成功
		msg.what = Constants.What.Donglixia.LIST;
		msg.obj = list;
		msg.arg1 = status;
		msg.arg2 = total;
		handler.sendMessage(msg);
	}
}
