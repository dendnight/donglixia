package com.denghb.donglixia.obtain;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.denghb.donglixia.Constants;
import com.denghb.donglixia.http.HttpRetriever;
import com.denghb.donglixia.tools.Helper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 版本检查
 * 
 * @author denghb
 *
 */
public class VersionObtain extends Thread {

	private static final String TAG = VersionObtain.class.getSimpleName();
	private Context context;

	private Handler handler;

	/** 版本代码 */
	private int versionCode;

	public VersionObtain(Context context, Handler handler,int versionCode) {
		this.context = context;
		this.handler = handler;
		this.versionCode = versionCode;
	}

	@Override
	public void run() {
		if (!Helper.checkConnection(context)) {
			return;
		}
		// 是否要更新？
		boolean isUpdate = false;
		// 请求完毕发消息
		Message msg = new Message();

		HttpRetriever httpRetriever = new HttpRetriever();
		String url = Constants.Server.version();
		
		HttpResponse response = httpRetriever.requestPost(url, null);

		String json = httpRetriever.jsonString(response);

		// 获取
		try {
			JSONObject jsonObject = new JSONObject(json);
			int version = jsonObject.getInt(Constants.JSON.VERSION);
			if(versionCode < version)
			{
				isUpdate = true;
			}
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage(), e);
		}

		msg.what = Constants.What.VERSION;
		msg.obj = isUpdate;
		handler.sendMessage(msg);
	}
}