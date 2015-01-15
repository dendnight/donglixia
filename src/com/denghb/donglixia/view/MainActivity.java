package com.denghb.donglixia.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.StaggeredGridView;
import android.support.v4.widget.StaggeredGridView.LayoutParams;
import android.support.v4.widget.StaggeredGridView.OnScrollListener;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
		mSGV.setColumnCount(4);
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

	private final class SGVAdapter extends BaseAdapter {

		LayoutInflater mInflater;

		public SGVAdapter(Context ctx) {
			mInflater = LayoutInflater.from(ctx);
		}

		@Override
		public int getCount() {
			return 30;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		Random r = new Random();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final LayoutParams lp;
			final View v;
			switch (position) {
			case 0:
			case 29:
				v = mInflater.inflate(R.layout.element_header, parent, false);
				lp = new LayoutParams(v.getLayoutParams());
				lp.span = mSGV.getColumnCount();
				break;
			case 8:
			case 9:
			case 18:
			case 19:
				v = mInflater.inflate(R.layout.element_item_small, parent, false);
				lp = new LayoutParams(v.getLayoutParams());
				// lp.span = 1;
				break;
			case 10:
			case 20:
				v = mInflater.inflate(R.layout.element_item_large, parent, false);
				lp = new LayoutParams(v.getLayoutParams());
				lp.span = 4;
				break;
			default:
				v = mInflater.inflate(R.layout.element_item, parent, false);
				lp = new LayoutParams(v.getLayoutParams());
				lp.span = 2;
				break;
			}
			v.setLayoutParams(lp);
			return v;
		}
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
			List<Donglixia> duitangs = new ArrayList<Donglixia>();
			String json = "";
			if (Helper.checkConnection(mContext)) {
				try {
					json = Helper.getStringFromUrl(url);

					json = json.substring(1, json.length());
					Log.i("json:", json);
					byte[] result = Base64.decode(json, Base64.DEFAULT);

					json = new String(result);
					Log.i("data:", json);
					/*
					 * String de1 = ss.substring(0, 9); System.out.println("s1:"
					 * + de1);
					 * 
					 * String de2 = ss.substring(10, ss.length());
					 * System.out.println("s2:" + de2);
					 * 
					 * String de = de1 + de2;
					 * 
					 * System.out.println(new String(Base64.decode(de)));
					 */
				} catch (IOException e) {
					Log.e("IOException is : ", e.toString());
					e.printStackTrace();
					return duitangs;
				} catch (Exception e) {

				}
			}
			Log.d("MainActiivty", "json:" + json);

			try {
				if (null != json) {
					JSONObject newsObject = new JSONObject(json);
					// JSONObject jsonObject = newsObject.getJSONObject("data");
					JSONArray dataJson = newsObject.getJSONArray("data");

					for (int i = 0; i < dataJson.length(); i++) {
						String urls = dataJson.getString(i);
						Donglixia newsInfo1 = new Donglixia();
						newsInfo1.setUrl(urls);
						newsInfo1.setTag("美图");
						duitangs.add(newsInfo1);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return duitangs;
		}
	}
}