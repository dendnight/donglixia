package com.denghb.donglixia.obtain;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.denghb.donglixia.Constants;
import com.denghb.donglixia.http.HttpRetriever;
import com.denghb.donglixia.tools.Helper;
import com.denghb.donglixia.tools.MemoryCache;

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
public class InfoObtain extends Thread {

	private static final String TAG = InfoObtain.class.getSimpleName();

	private Context context;

	private Handler handler;

	private int id;

	public InfoObtain(Context context, Handler handler, int id) {
		this.context = context;
		this.handler = handler;
		this.id = id;
	}

	@Override
	public void run() {
		if (!Helper.checkConnection(context)) {
			return;
		}
		// 请求完毕发消息
		Message msg = new Message();

		String[] urls = null;

		// 判断是否有缓存
		MemoryCache cache = MemoryCache.instance();
		Object object = cache.read(id);
		
		int status = Constants.Status.COMPLETED;
		if (null != object && object instanceof String[]) {
			urls = (String[]) object;
		} else {

			HttpRetriever httpRetriever = new HttpRetriever();
			String url = Constants.Server.info(id);
			HttpResponse response = httpRetriever.requestPost(url, null);

			String json = httpRetriever.decodeToJsonString(response);

			// 获取
			try {
				JSONObject jsonObject = new JSONObject(json);
				JSONArray dataArray = jsonObject
						.getJSONArray(Constants.JSON.DATA);
				status = jsonObject.getInt(Constants.JSON.STATUS);
				
				int length = dataArray.length();
				urls = new String[length];
				for (int i = 0; i < length; i++) {
					urls[i] = dataArray.getString(i);
				}
				msg.arg1 = Constants.Status.COMPLETED;
			} catch (JSONException e) {
				Log.d(TAG, e.getMessage(), e);
			}
		}
		msg.what = Constants.What.Donglixia.INFO;
		msg.obj = urls;
		msg.arg1 = status;

		handler.sendMessage(msg);
	}
}
