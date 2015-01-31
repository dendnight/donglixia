package com.denghb.donglixia.fragment;

import java.util.ArrayList;
import java.util.List;

import com.denghb.donglixia.Constants;
import com.denghb.donglixia.R;
import com.denghb.donglixia.activity.ViewPagerActivity;
import com.denghb.donglixia.adapter.DonglixiaAdapter;
import com.denghb.donglixia.model.Donglixia;
import com.denghb.donglixia.obtain.MainObtain;
import com.denghb.donglixia.tools.Helper;
import com.denghb.donglixia.widget.MaterialDialog;
import com.denghb.donglixia.widget.StaggeredGridView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

/**
 * 东篱下
 * 
 * <pre>
 * Description
 * Copyright:	Copyright (c)2012  
 * Company:		东篱下
 * Author:		denghb
 * Version:		1.0  
 * Create at:	2015年1月30日 下午9:08:36  
 *  
 * 修改历史:
 * 日期    作者    版本  修改描述
 * ------------------------------------------------------------------
 * 
 * </pre>
 */
@SuppressLint("HandlerLeak")
public class DonglixiaFragment extends BaseFragment implements AbsListView.OnScrollListener,
		AbsListView.OnItemClickListener {

	private static final String TAG = DonglixiaFragment.class.getSimpleName();

	private StaggeredGridView mGridView;
	private boolean mHasRequestedMore;
	private DonglixiaAdapter mAdapter;

	private ArrayList<Donglixia> list;

	private int page = 1;
	private String tag = "";
	private int total = 1;

	private View footerView;

	private MaterialDialog mMaterialDialog;

	/** 是否请求查询 */
	private boolean mHasRequestedSearch;

	/** 非WIFI下加载 */
	private boolean isNotWifiLoad = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_donglixia, root, false);

		isRequest();

		mGridView = (StaggeredGridView) rootView.findViewById(R.id.grid_view_donglixia);

		list = Helper.generateSampleData(10);
		mAdapter = new DonglixiaAdapter(getActivity(), list);

		mGridView.setAdapter(mAdapter);
		mGridView.setOnScrollListener(this);
		mGridView.setOnItemClickListener(this);

		footerView = inflater.inflate(R.layout.item_footer, null);
		footerView.setVisibility(View.GONE);
		mGridView.addFooterView(footerView);

		return rootView;
	}

	/**
	 * 是否请求服务
	 */
	private void isRequest() {
		if (null == mMaterialDialog) {
			mMaterialDialog = new MaterialDialog(getActivity());
		}

		boolean isConn = Helper.checkConnection(getActivity());
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
		boolean isWifi = Helper.isWifi(getActivity());
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

		MainObtain mainObtain = new MainObtain(getActivity(), handler, url);
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
			case Constants.What.Donglixia.LIST:

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
		mMaterialDialog.setTitle("提示").setMessage("您当前不是最新版本是否更新？").setPositiveButton("确定", new View.OnClickListener() {
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
	public void onScrollStateChanged(final AbsListView view, final int scrollState) {
		Log.d(TAG, "onScrollStateChanged:" + scrollState);
	}

	@Override
	public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
			final int totalItemCount) {
		Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem + " visibleItemCount:" + visibleItemCount
				+ " totalItemCount:" + totalItemCount);
		// 是否加载，不能超过总条数
		if (!mHasRequestedMore && total > totalItemCount) {
			int lastInScreen = firstVisibleItem + visibleItemCount;
			if (lastInScreen >= totalItemCount) {
				Log.d(TAG, "onScroll lastInScreen - so load more");
				mHasRequestedMore = true;
				isRequest();
			}
		}
		// 显示没有更多的了
		if (null != footerView) {
			if (total == totalItemCount) {
				footerView.setVisibility(View.VISIBLE);
			} else {
				footerView.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		try {
			Donglixia donglixia = list.get(position);
			// 空对象
			if (null == donglixia) {
				return;
			}
			Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
			// 将当前的图片列表
			intent.putStringArrayListExtra(Constants.Extra.URLS, donglixia.getUrls());
			intent.putExtra(Constants.Extra.IMAGE_POSITION, 0);
			startActivity(intent);
			// 动画
			getActivity().overridePendingTransition(0, 0);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage(), e);
		}

	}

}
