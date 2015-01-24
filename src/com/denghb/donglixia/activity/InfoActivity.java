package com.denghb.donglixia.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.denghb.donglixia.Constants;
import com.denghb.donglixia.R;
import com.denghb.donglixia.adapter.DonglixiaAdapter;
import com.denghb.donglixia.model.Donglixia;
import com.denghb.donglixia.obtain.InfoObtain;
import com.denghb.donglixia.obtain.MainObtain;
import com.denghb.donglixia.tools.Helper;
import com.denghb.donglixia.widget.StaggeredGridView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class InfoActivity extends Activity implements
		AbsListView.OnItemClickListener {

	private static final String TAG = InfoActivity.class.getSimpleName();
	public static final String SAVED_DATA_KEY = "SAVED_DATA";

	private StaggeredGridView mGridView;
	private DonglixiaAdapter mAdapter;

	private final List<Donglixia> list = new ArrayList<Donglixia>();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		request();
		
		mGridView = (StaggeredGridView) findViewById(R.id.grid_view);

		LayoutInflater layoutInflater = getLayoutInflater();

		// 头部
		View header = layoutInflater.inflate(R.layout.header, null);

		mGridView.addHeaderView(header);

		// list 可以缓存
		mAdapter = new DonglixiaAdapter(this, list);

		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);

	}
	/**
	 * 请求服务器
	 */
	private void request()
	{
		// 接收传来的ID
		int i = getIntent().getIntExtra(Constants.Extra.ID, 0);
		String url = "http://donglixia.sinaapp.com/app/service/info/?i=" + i;
		// 请求url
		InfoObtain infoObtain = new InfoObtain(this, handler, url);
		infoObtain.start();

	}
	
	/**
	 * 更新UI
	 */
	private final Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Constants.WHAT.COMPLETED) {
				
				list.addAll((List<Donglixia>)msg.obj);
				mAdapter.notifyDataSetChanged();
			}
		}
	};
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		Toast.makeText(this, "Item Clicked: " + position, Toast.LENGTH_SHORT)
				.show();

	}

}
