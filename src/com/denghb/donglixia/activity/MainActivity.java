package com.denghb.donglixia.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.denghb.donglixia.R;
import com.denghb.donglixia.activity.MainActivity.DonglixiaTask;
import com.denghb.donglixia.adapter.DonglixiaAdapter;
import com.denghb.donglixia.model.Donglixia;
import com.denghb.donglixia.tools.Helper;
import com.denghb.donglixia.widget.StaggeredGridView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "StaggeredGridActivity";
    public static final String SAVED_DATA_KEY = "SAVED_DATA";

    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private DonglixiaAdapter mAdapter;

	private final List<Donglixia> list = new ArrayList<Donglixia>();

    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);

        LayoutInflater layoutInflater = getLayoutInflater();
        
        // 头部
        View header = layoutInflater.inflate(R.layout.header, null);

        mGridView.addHeaderView(header);
        
        
		// list 可以缓存
		mAdapter = new DonglixiaAdapter(this, list);


        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);
        
        String url = "http://donglixia.sinaapp.com/app/service/";
		Log.d("MainActivity", "current url:" + url);
		DonglixiaTask task = new DonglixiaTask(this);
		task.execute(url);
        
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.menu_sgv_dynamic, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//			case R.id.col1:
//				mGridView.setColumnCount(1);
//				break;
//			case R.id.col2:
//				mGridView.setColumnCount(2);
//				break;
//			case R.id.col3:
//				mGridView.setColumnCount(3);
//				break;
//		}
		return true;
	}
	

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                            " visibleItemCount:" + visibleItemCount +
                            " totalItemCount:" + totalItemCount);
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                onLoadMoreItems();
            }
        }
    }

    // 加载更多
    private void onLoadMoreItems() {
        
        // stash all the data in our backing store
//        mData.addAll(sampleData);
        // notify the adapter that we can update now
//        mAdapter.notifyDataSetChanged();
        mHasRequestedMore = false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(this, "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        Toast.makeText(this, "Item Long Clicked: " + position, Toast.LENGTH_SHORT).show();
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
