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
import android.util.Log;

/**
 * 主操作
 * 
 * @author denghb
 *
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

			JSONObject jsonObject = new JSONObject(json);
			JSONArray dataArray = jsonObject.getJSONArray(Constants.JSON.DATA);
			total = jsonObject.getInt(Constants.JSON.TOTAL);
			status = jsonObject.getInt(Constants.JSON.STATUS);

			int length = dataArray.length();
			for (int i = 0; i < length; i++) {

				JSONObject obj = dataArray.getJSONObject(i);
				String urls = obj.getString(Constants.JSON.URL);
				String tag = obj.getString(Constants.JSON.TAG);
				int id = obj.getInt(Constants.JSON.ID);
				int love = obj.getInt(Constants.JSON.LOVE);

				Donglixia donglixia = new Donglixia();
				donglixia.setId(id);
				donglixia.setLove(love);
				donglixia.setUrl(urls);
				donglixia.setTag(tag == null ? "" : tag);
				list.add(donglixia);
			}
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage(), e);
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
