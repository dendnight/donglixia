package com.denghb.donglixia.obtain;

import org.apache.http.HttpResponse;

import com.denghb.donglixia.Constants;
import com.denghb.donglixia.http.HttpRetriever;
import com.denghb.donglixia.tools.Helper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 喜欢
 * 
 * <pre>
 * Description
 * Copyright:	Copyright (c)2012  
 * Company:		东篱下
 * Author:		denghb
 * Version:		1.0  
 * Create at:	2015年1月31日 下午9:03:29  
 *  
 * 修改历史:
 * 日期    作者    版本  修改描述
 * ------------------------------------------------------------------
 * 
 * </pre>
 */
public class LoveObtain extends Thread {

	private static final String TAG = LoveObtain.class.getSimpleName();
	private Context context;

	private Handler handler;

	/** 版本代码 */
	private int id;

	public LoveObtain(Context context, Handler handler, int id) {
		this.context = context;
		this.handler = handler;
		this.id = id;
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
		String url = Constants.Server.love(id);

		HttpResponse response = httpRetriever.requestPost(url, null);

		msg.what = Constants.What.Donglixia.LOVE;
		msg.obj = isUpdate;
		handler.sendMessage(msg);
	}
}