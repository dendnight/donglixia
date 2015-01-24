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

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 主操作
 * 
 * @author denghb
 *
 */
public class MainObtain extends Thread {

	private Context context;

	private Handler handler;

	private String url;

	public MainObtain(Context context, Handler handler, String url) {
		this.context = context;
		this.handler = handler;
		this.url = url;
	}

	@Override
	public void run() {
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

				JSONObject obj = dataArray.getJSONObject(i);
				String urls = obj.getString("URL");
				String tag = obj.getString("TAG");
				int id = obj.getInt("ID");
				int love = obj.getInt("LOVE");

				Donglixia donglixia = new Donglixia();
				donglixia.setId(id);
				donglixia.setLove(love);
				donglixia.setUrl(urls);
				donglixia.setTag(tag == null ? "" : tag);
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
