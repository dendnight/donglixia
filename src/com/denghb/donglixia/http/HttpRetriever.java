package com.denghb.donglixia.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Base64;
import android.util.Log;

/**
 * http请求
 * 
 * <pre>
 * Description
 * Copyright:	Copyright (c)2013  
 * Company:		DENDNIGHT
 * Author:		hb
 * Version:		1.0  
 * Create at:	2014年8月23日 下午12:35:20  
 *  
 * 修改历史:
 * 日期    作者    版本  修改描述
 * ------------------------------------------------------------------
 * 
 * </pre>
 */
public class HttpRetriever {

	private static final String TAG = "HttpRetriever";

	private final HttpClient defaultClient;

	public HttpRetriever() {
		this.defaultClient = DefaultClient.getDefaultClientInstance();
	}

	/**
	 * 1. HttpGet<br/>
	 * 2. Set http headers<br/>
	 * 3. Execute HttpGet request
	 * */
	public HttpResponse requestGet(String url, Map<String, String> headers) {
		// 1.
		HttpGet httpGet = new HttpGet(url);
		Log.i(TAG, "httpGet url: " + url);
		// 2.
		if (headers != null) {
			Iterator<Entry<String, String>> iterator = headers.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				String headeName = entry.getKey();
				String headerValue = entry.getValue();
				Log.d(TAG, "name: " + headeName + " value: " + headerValue);
				httpGet.addHeader(headeName, headerValue);
			}
		}

		// 3.
		HttpResponse httpResponse = null;
		try {
			httpResponse = defaultClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {

			// TODO Connection reset by peer
			try {
				httpResponse = defaultClient.execute(httpGet);
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return httpResponse;
	}

	/**
	 * 1. HttpPost 2. Set http headers 3. ContentProducer 4. HttpEntity 5.
	 * Execute HttpPost request
	 * */
	public HttpResponse requestPost(String url, Map<String, String> headers) {
		// 1.
		HttpPost httpPost = new HttpPost(url);
		Log.i(TAG, "httpPost url: " + url);

		// 2.
		if (headers != null) {
			Iterator<Entry<String, String>> iterator = headers.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				String headeName = entry.getKey();
				String headerValue = entry.getValue();
				Log.d(TAG, "name: " + headeName + " value: " + headerValue);
				httpPost.addHeader(headeName, headerValue);
			}
		}

		// 3.
		ContentProducer contentProducer = new ContentProducer() {
			@Override
			public void writeTo(OutputStream outstream) throws IOException {
				Writer writer = new OutputStreamWriter(outstream, HTTP.UTF_8);
				// TODO writer.write(requestBody);
				writer.flush();
				writer.close();
			}
		};

		// 4.
		HttpEntity entity = new EntityTemplate(contentProducer);
		httpPost.setEntity(entity);

		// 5.
		HttpResponse httpResponse = null;
		try {
			httpResponse = defaultClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			try {
				httpResponse = defaultClient.execute(httpPost);
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return httpResponse;
	}

	/**
	 * 返回json数据
	 * 
	 * @param response
	 * @return
	 */
	public String jsonString(HttpResponse response) {
		StatusLine statusLine = response.getStatusLine();
		int statueCode = statusLine.getStatusCode();
		if (statueCode == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			try {
				return EntityUtils.toString(entity, HTTP.UTF_8);
			} catch (IOException e) {
				Log.d(TAG, e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * 转换成json
	 * 
	 * @param response
	 * @return
	 */
	public String decodeToJsonString(HttpResponse response) {
		String json = jsonString(response);
		if (null != json) {
			try {
				byte[] result = Base64.decode("ey" + json, Base64.DEFAULT);
				json = new String(result);
				Log.d(TAG, json);
				return json;
			} catch (Exception e) {
				Log.d(TAG, e.getMessage(), e);
			}
		}
		return null;
	}
	
}
