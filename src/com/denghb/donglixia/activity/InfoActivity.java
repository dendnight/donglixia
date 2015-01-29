package com.denghb.donglixia.activity;

import java.util.ArrayList;
import java.util.List;

import com.denghb.donglixia.Constants;
import com.denghb.donglixia.R;
import com.denghb.donglixia.adapter.DonglixiaAdapter;
import com.denghb.donglixia.model.Donglixia;
import com.denghb.donglixia.obtain.InfoObtain;
import com.denghb.donglixia.tools.Helper;
import com.denghb.donglixia.widget.StaggeredGridView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

@SuppressLint("HandlerLeak")
public class InfoActivity extends Activity implements
		AbsListView.OnItemClickListener {

	private static final String TAG = InfoActivity.class.getSimpleName();

	private StaggeredGridView mGridView;
	private DonglixiaAdapter mAdapter;

	private List<Donglixia> list;

	private String[] urls;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		request();
		mGridView = (StaggeredGridView) findViewById(R.id.grid_view);

		list = Helper.generateSampleData(1);
		mAdapter = new DonglixiaAdapter(this, list);
		LayoutInflater layoutInflater = getLayoutInflater();

		// 头部
		View header = layoutInflater.inflate(R.layout.info_header, null);
		mGridView.addHeaderView(header);
		// 尾部
		View footer = layoutInflater.inflate(R.layout.info_footer, null);
		mGridView.addFooterView(footer);

		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);

	}
	
	/**
	 * 请求服务器
	 */
	private void request() {
		// 接收传来的ID
		int i = getIntent().getIntExtra(Constants.Extra.ID, 0);
		// 请求url
		InfoObtain infoObtain = new InfoObtain(this, handler, i);
		infoObtain.start();

	}

	/**
	 * 更新UI
	 */
	private final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == Constants.What.INFO) {
				List<Donglixia> temp = new ArrayList<Donglixia>();

				urls = (String[]) msg.obj;
				int length = urls.length;
				for (int i = 0; i < length; i++) {
					Donglixia donglixia = new Donglixia();
					donglixia.setUrl(urls[i]);
					temp.add(donglixia);
				}
				list.clear();
				list.addAll(temp);
				mAdapter.notifyDataSetChanged();

			}
		}
	};

	/**
	 * 跳转到
	 * 
	 * @param position
	 */
	private void startViewPagerActivity(int position) {

		// 空对象
		if(null == urls)
		{
			return;
		}
		
		Intent intent = new Intent(InfoActivity.this, ViewPagerActivity.class);
		intent.putExtra(Constants.Extra.URLS, urls);
		intent.putExtra(Constants.Extra.IMAGE_POSITION, position - 1);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		startViewPagerActivity(position);
	}

	// 处理后退键的情况
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// finish当前activity
	private void finishActivity() {
		this.finish();
		overridePendingTransition(0, 0);
	}
}
