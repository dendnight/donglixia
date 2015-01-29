package com.denghb.donglixia.activity;

import java.util.ArrayList;
import java.util.List;

import com.denghb.donglixia.Constants;
import com.denghb.donglixia.R;
import com.denghb.donglixia.adapter.DonglixiaAdapter;
import com.denghb.donglixia.model.Donglixia;
import com.denghb.donglixia.obtain.InfoObtain;
import com.denghb.donglixia.obtain.MainObtain;
import com.denghb.donglixia.obtain.VersionObtain;
import com.denghb.donglixia.tools.Helper;
import com.denghb.donglixia.widget.StaggeredGridView;
import com.denghb.donglixia.widget.materialdialog.MaterialDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity implements
		AbsListView.OnScrollListener, AbsListView.OnItemClickListener,
		View.OnClickListener {

	private static final String TAG = MainActivity.class.getSimpleName();

	private StaggeredGridView mGridView;
	private boolean mHasRequestedMore;
	private DonglixiaAdapter mAdapter;

	private List<Donglixia> list;

	private int page = 1;
	private String tag = "";
	private int total = 1;
	
	private EditText etSearch;

	private MaterialDialog mMaterialDialog;
	
	/** 是否请求查询 */
	private boolean mHasRequestedSearch;
	
	/** 非WIFI下加载 */
	private boolean isNotWifiLoad = false;
	
	@SuppressLint({ "NewApi", "InflateParams" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		isRequest();

		mGridView = (StaggeredGridView) findViewById(R.id.grid_view);

		LayoutInflater layoutInflater = getLayoutInflater();

		// 头部
		View header = layoutInflater.inflate(R.layout.header, null);

		mGridView.addHeaderView(header);

		list = Helper.generateSampleData(10);
		mAdapter = new DonglixiaAdapter(this, list);

		mGridView.setAdapter(mAdapter);
		mGridView.setOnScrollListener(this);
		mGridView.setOnItemClickListener(this);

		etSearch = (EditText) header.findViewById(R.id.search_txt);
		Button btnSearch = (Button) header.findViewById(R.id.search_btn);
		btnSearch.setOnClickListener(this);
		btnSearch.setTag(1);

		try {
			// 获取版本信息
			PackageInfo info = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0);
			int versionCode = info.versionCode;
			VersionObtain versionObtain = new VersionObtain(this, handler,
					versionCode);
			versionObtain.start();
		} catch (NameNotFoundException e) {
			Log.d(TAG, e.getMessage(), e);
		}

	}

	@Override
	public void onClick(View v) {
		if (1 == (Integer) v.getTag()) {
			// 没有请求
			if (!mHasRequestedSearch) {
				page = 1;
				tag = etSearch.getText().toString();
				isRequest();
				list.clear();
				mHasRequestedSearch = true;
			}

		}
	}

	/**
	 * 是否请求服务
	 */
	private void isRequest() {
		if (null == mMaterialDialog) {
			mMaterialDialog = new MaterialDialog(this);
		}

		boolean isConn = Helper.checkConnection(this);
		if (!isConn) {
			mMaterialDialog.setTitle("提示").setMessage(" (─.─||| 没有连接到互联网 ")
					.setPositiveButton("确定", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mMaterialDialog.dismiss();
						}
					}).setCanceledOnTouchOutside(false).show();
			return;
		}

		// 判断是否是WIFI
		boolean isWifi = Helper.isWifi(this);
		if (!isWifi || isNotWifiLoad) {
			mMaterialDialog.setTitle("提示").setMessage("您当前不是Wi-Fi连接请确认是否继续？")
					.setPositiveButton("确定", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							isNotWifiLoad = true;
							request();
							mMaterialDialog.dismiss();
						}
					}).setNegativeButton("取消", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mMaterialDialog.dismiss();
						}
					}).setCanceledOnTouchOutside(false).show();
		} else {
			request();
		}
	}

	/**
	 * 请求服务器
	 */
	private void request() {

		// 请求url
		String url = Constants.Server.list(page, tag);

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

			switch (msg.what) {
			case Constants.What.LIST:

				// 第一次加载把之前的给清掉
				if (1 == page) {
					list.clear();
				}

				// 总条数
				total = msg.arg2;
				
				list.addAll((List<Donglixia>) msg.obj);
				mAdapter.notifyDataSetChanged();
				mHasRequestedMore = false;
				mHasRequestedSearch = false;

				// 页数+1
				page++;
				
				break;
			case Constants.What.VERSION:
				// 版本低提示
				if (null != msg.obj && (Boolean) msg.obj) {
					showUpdate();
				}
				break;

			default:
				break;
			}

		}
	};

	private void showUpdate() {
		mMaterialDialog.setTitle("提示").setMessage("您当前不是最新版本是否更新？")
				.setPositiveButton("确定", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMaterialDialog.dismiss();

						// 打开App主页
						Uri uri = Uri.parse(Constants.Server.home());
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
					}
				}).setNegativeButton("取消", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMaterialDialog.dismiss();
					}
				}).setCanceledOnTouchOutside(false).show();
	}

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
		// 是否加载，不能超过总条数
		if (!mHasRequestedMore && total > totalItemCount) {
			int lastInScreen = firstVisibleItem + visibleItemCount;
			if (lastInScreen >= totalItemCount) {
				Log.d(TAG, "onScroll lastInScreen - so load more");
				mHasRequestedMore = true;
				isRequest();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		try {
			Donglixia d = list.get(position - 1);
			// 空对象
			if(null == d || null == d.getUrl())
			{
				return;
			}
			// 请求数据然后缓存
			InfoObtain infoObtain = new InfoObtain(this, handler, d.getId());
			infoObtain.start();

			Intent intent = new Intent(MainActivity.this,
					ViewPagerActivity.class);
			// 将当前的图片传上去
			intent.putExtra(Constants.Extra.URLS, new String[] { d.getUrl() });
			intent.putExtra(Constants.Extra.IMAGE_POSITION, 0);
			intent.putExtra(Constants.Extra.ID, d.getId());

			// 创建详情页
			intent.putExtra(Constants.Extra.CREATE_INFO, true);
			startActivity(intent);

			// 动画
			overridePendingTransition(0, 0);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage(), e);
		}

	}

	/** 上一次按back的时间ms */
	private long backLastPressedTime;

	/** 2次back的最大允许的间隔，超过这个时间将重新计算 */
	private static final long BACK_CHECK_INTERVAL = 10000;

	// 处理后退键的情况
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			long now = System.currentTimeMillis();
			if (now - backLastPressedTime > BACK_CHECK_INTERVAL) {
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
				backLastPressedTime = now;
			} else {
				super.onBackPressed();
				System.gc();
				this.finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

}
