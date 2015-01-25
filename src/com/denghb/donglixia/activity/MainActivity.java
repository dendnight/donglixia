package com.denghb.donglixia.activity;

import java.util.ArrayList;
import java.util.List;

import com.denghb.donglixia.Constants;
import com.denghb.donglixia.R;
import com.denghb.donglixia.adapter.DonglixiaAdapter;
import com.denghb.donglixia.model.Donglixia;
import com.denghb.donglixia.obtain.MainObtain;
import com.denghb.donglixia.tools.Helper;
import com.denghb.donglixia.widget.StaggeredGridView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity implements
		AbsListView.OnScrollListener, AbsListView.OnItemClickListener,View.OnClickListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	public static final String SAVED_DATA_KEY = "SAVED_DATA";

	private StaggeredGridView mGridView;
	private boolean mHasRequestedMore;
	private DonglixiaAdapter mAdapter;

	private final List<Donglixia> list = new ArrayList<Donglixia>();

	private int page = 1;
	private String tag = "";

	private Dialog dialog;
	
	private EditText etSearch;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
		mGridView.setOnScrollListener(this);
		mGridView.setOnItemClickListener(this);
		
		etSearch = (EditText)header.findViewById(R.id.search_txt);
		Button btnSearch = (Button)header.findViewById(R.id.search_btn);
		btnSearch.setOnClickListener(this);
		btnSearch.setTag(1);
	}
	@Override
	public void onClick(View v) {
		if(1 == (Integer)v.getTag())
		{
			tag = etSearch.getText().toString();
			request();				
			list.clear();
		}
	}
	
	/**
	 * 请求服务器
	 */
	private void request() {
		String url = "http://donglixia.sinaapp.com/app/service/?p=" + page + "&tag=" + tag;
		// 页数+1
		page++;
		// 请求url
		MainObtain mainObtain = new MainObtain(this, handler, url);
		mainObtain.start();

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

				list.addAll((List<Donglixia>) msg.obj);
				mAdapter.notifyDataSetChanged();
				mHasRequestedMore = false;
			}
		}
	};

	@Override
	public void onScrollStateChanged(final AbsListView view,
			final int scrollState) {
		Log.d(TAG, "onScrollStateChanged:" + scrollState);
	}

	@Override
	public void onScroll(final AbsListView view, final int firstVisibleItem,
			final int visibleItemCount, final int totalItemCount) {
		Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem
				+ " visibleItemCount:" + visibleItemCount + " totalItemCount:"
				+ totalItemCount);
		// our handling
		if (!mHasRequestedMore && 1 < totalItemCount) {
			int lastInScreen = firstVisibleItem + visibleItemCount;
			if (lastInScreen >= totalItemCount) {
				Log.d(TAG, "onScroll lastInScreen - so load more");
				mHasRequestedMore = true;
				request();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {

		int did = list.get(position - 1).getId();
		Log.i(TAG, "ID:" + did);

		Intent intent = new Intent(this, InfoActivity.class);
		intent.putExtra(Constants.Extra.ID, did);
		startActivity(intent);
		
		overridePendingTransition(0, 0);

	}

	/** 上一次按back的时间ms */
	private long backLastPressedTime;

	/** 2次back的最大允许的间隔，超过这个时间将重新计算 */
	private static final long BACK_CHECK_INTERVAL = 10000;
	
	@Override
	public void onBackPressed() {

		if (null != dialog && dialog.isShowing()) {
			dialog.dismiss();
			return;
		}
		
		long now = System.currentTimeMillis();
		if (now - backLastPressedTime > BACK_CHECK_INTERVAL) {
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			backLastPressedTime = now;
		} else {
			super.onBackPressed();
			System.gc();
			this.finish();
		}
	}

}
