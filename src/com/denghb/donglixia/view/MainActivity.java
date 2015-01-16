package com.denghb.donglixia.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.StaggeredGridView;
import android.support.v4.widget.StaggeredGridView.OnScrollListener;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;

import com.denghb.donglixia.R;
import com.denghb.donglixia.adapter.DonglixiaAdapter;
import com.denghb.donglixia.model.Donglixia;
import com.denghb.donglixia.tools.Helper;
import com.denghb.donglixia.tools.bitmap.ImageFetcher;
import com.denghb.donglixia.tools.bitmap.ImageWorker;

public class MainActivity extends Activity {
	private final static String TAG = MainActivity.class.getSimpleName();
	private StaggeredGridView mSGV;
	// private SGVAdapter mAdapter;
	private DonglixiaAdapter mAdapter;

	private final OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(ViewGroup view, int scrollState) {
			Log.d(TAG, "[onScrollStateChanged] scrollState:" + scrollState);
			switch (scrollState) {
			case SCROLL_STATE_IDLE:
				setTitle("SCROLL_STATE_IDLE");
				break;

			case SCROLL_STATE_FLING:
				setTitle("SCROLL_STATE_FLING");
				break;

			case SCROLL_STATE_TOUCH_SCROLL:
				setTitle("SCROLL_STATE_TOUCH_SCROLL");
				break;

			default:
				break;
			}
		}

		@Override
		public void onScroll(ViewGroup view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			Log.d(TAG, "[onScroll] firstVisibleItem:" + firstVisibleItem + " visibleItemCount:" + visibleItemCount
					+ " totalItemCount:" + totalItemCount);

		}
	};
	private final List<Donglixia> list = new ArrayList<Donglixia>();
	private ImageWorker mImageFetcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mImageFetcher = new ImageFetcher(this, 240);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		// mAdapter = new SGVAdapter(this);
		mAdapter = new DonglixiaAdapter(this, mImageFetcher, list);

		mSGV = (StaggeredGridView) findViewById(R.id.grid);
		mSGV.setColumnCount(2);
		mSGV.setAdapter(mAdapter);
		mSGV.setOnScrollListener(mScrollListener);
		String url = "http://donglixia.sinaapp.com/app/service/";
		Log.d("MainActivity", "current url:" + url);
		DonglixiaTask task = new DonglixiaTask(this);
		task.execute(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	class DonglixiaTask extends AsyncTask<String, Integer, List<Donglixia>> {

		private final Context mContext;

		public DonglixiaTask(Context context) {
			super();
			mContext = context;
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
			list.addAll(result);
			mAdapter.notifyDataSetChanged();

		}

		@Override
		protected void onPreExecute() {
		}

		public List<Donglixia> parseNewsJSON(String url) throws IOException {
			List<Donglixia> donglixias = new ArrayList<Donglixia>();
			// 留出头部
			donglixias.add(new Donglixia());
			String json = "";
			if (Helper.checkConnection(mContext)) {
				try {
					json = Helper.getStringFromUrl(url);

					String de1 = json.substring(1, 9);
					String de2 = json.substring(10, json.length());
					json = de1 + de2;

					Log.i("json:", json);
					byte[] result = Base64.decode(json, Base64.DEFAULT);

					json = new String(result);
					Log.i("data:", json);

				} catch (IOException e) {
					Log.e("IOException is : ", e.toString());
					e.printStackTrace();
					return donglixias;
				} catch (Exception e) {

				}
			}
			Log.d("MainActiivty", "json:" + json);

			try {
				if (null != json) {
					JSONObject newsObject = new JSONObject(json);
					// JSONObject jsonObject = newsObject.getJSONObject("data");
					JSONArray dataJson = newsObject.getJSONArray("DATA");

					for (int i = 0; i < dataJson.length(); i++) {

						JSONObject obj = dataJson.getJSONObject(i);
						String urls = obj.getString("URL");
						String tag = obj.getString("TAG");

						Donglixia donglixia = new Donglixia();
						donglixia.setUrl(urls);
						donglixia.setTag(tag == null ? "" : tag);
						donglixias.add(donglixia);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return donglixias;
		}
	}
}