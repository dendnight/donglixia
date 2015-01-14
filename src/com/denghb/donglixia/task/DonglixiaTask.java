package com.denghb.donglixia.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.denghb.donglixia.model.Donglixia;
import com.denghb.donglixia.tools.Helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class DonglixiaTask extends AsyncTask<String, Integer, List<Donglixia>> {

		private Context mContext;
		private int mType = 1;

		public DonglixiaTask(Context context, int type) {
			super();
			mContext = context;
			mType = type;
		}

		@Override
		protected List<Donglixia> doInBackground(String... params) {
			try {
				return parseNewsJSON(params[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Donglixia> result) {

		}

		@Override
		protected void onPreExecute() {
		}

		public List<Donglixia> parseNewsJSON(String url) throws IOException {
			List<Donglixia> duitangs = new ArrayList<Donglixia>();
			String json = "";
			if (Helper.checkConnection(mContext)) {
				try {
					json = Helper.getStringFromUrl(url);

				} catch (IOException e) {
					Log.e("IOException is : ", e.toString());
					e.printStackTrace();
					return duitangs;
				}
			}
			Log.d("MainActiivty", "json:" + json);

			try {
				if (null != json) {
					JSONObject newsObject = new JSONObject(json);
					JSONObject jsonObject = newsObject.getJSONObject("data");
					JSONArray blogsJson = jsonObject.getJSONArray("blogs");

					for (int i = 0; i < blogsJson.length(); i++) {
						JSONObject newsInfoLeftObject = blogsJson.getJSONObject(i);
						Donglixia newsInfo1 = new Donglixia();
						//newsInfo1.setAlbid(newsInfoLeftObject.isNull("albid") ? "" : newsInfoLeftObject.getString("albid"));
						duitangs.add(newsInfo1);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return duitangs;
		}
	}