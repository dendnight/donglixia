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
	public static final String SAVED_DATA_KEY = "SAVED_DATA";

	private StaggeredGridView mGridView;
	private DonglixiaAdapter mAdapter;

	private final List<Donglixia> list = new ArrayList<Donglixia>();

	private String[] urls;

	private Dialog dialog;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		dialog = Helper.createLoadingDialog(this, "");
		dialog.show();
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
	private void request() {
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
			dialog.dismiss();
			
			if (msg.what == Constants.WHAT.COMPLETED) {
				List<Donglixia> myList = (List<Donglixia>) msg.obj;

				// 跳转至大图
				urls = new String[myList.size()];

				for (int i = 0; i < urls.length; i++) {
					urls[i] = myList.get(i).getUrl();
				}

				startViewPagerActivity(0);

				list.addAll(myList);
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

		Intent intent = new Intent(InfoActivity.this, ViewPagerActivity.class);
		intent.putExtra(Constants.Extra.URLS, urls);
		intent.putExtra(Constants.Extra.IMAGE_POSITION, position - 1);
		startActivity(intent);
		
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

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
		overridePendingTransition(0,0);
	}
}
