package com.denghb.donglixia.obtain;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.denghb.donglixia.Constants;
import com.denghb.donglixia.http.HttpRetriever;
import com.denghb.donglixia.model.Donglixia;
import com.denghb.donglixia.tools.Helper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 主操作
 * 
 * @author denghb
 *
 */
public class InfoObtain extends Thread {

	private Context context;

	private Handler handler;

	private String url;

	public InfoObtain(Context context, Handler handler, String url) {
		this.context = context;
		this.handler = handler;
		this.url = url;
	}

	@Override
	public void run() {
		if(!Helper.checkConnection(context))
		{
			return;
		}
		// 请求完毕发消息
		Message msg = new Message();
		List<Donglixia> list = new ArrayList<Donglixia>();

		HttpRetriever httpRetriever = new HttpRetriever();
		HttpResponse response = httpRetriever.requestPost(url, null);

		String json = httpRetriever.decodeToJsonString(response);

		// 获取
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray dataArray = jsonObject.getJSONArray("DATA");

			int length = dataArray.length();
			for (int i = 0; i < length; i++) {

				Donglixia donglixia = new Donglixia();
				donglixia.setUrl(dataArray.getString(i));
				list.add(donglixia);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 成功
		msg.what = Constants.WHAT.COMPLETED;
		msg.obj = list;
		handler.sendMessage(msg);
	}
}
