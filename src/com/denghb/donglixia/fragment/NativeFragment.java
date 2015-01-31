package com.denghb.donglixia.fragment;

import java.util.ArrayList;
import java.util.List;

import com.denghb.donglixia.Constants;
import com.denghb.donglixia.R;
import com.denghb.donglixia.activity.ImageListActivity;
import com.denghb.donglixia.adapter.ImageGroupAdapter;
import com.denghb.donglixia.model.ImageGroup;
import com.denghb.donglixia.obtain.NativeObtain;
import com.denghb.donglixia.widget.StaggeredGridView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

/**
 * 本地
 * 
 * <pre>
 * Description
 * Copyright:	Copyright (c)2012  
 * Company:		东篱下
 * Author:		denghb
 * Version:		1.0  
 * Create at:	2015年1月30日 下午9:08:24  
 *  
 * 修改历史:
 * 日期    作者    版本  修改描述
 * ------------------------------------------------------------------
 * 
 * </pre>
 */
@SuppressLint("HandlerLeak")
public class NativeFragment extends BaseFragment implements AbsListView.OnItemClickListener {

	private StaggeredGridView mGridView;

	private ImageGroupAdapter mAdapter;

	private List<ImageGroup> list = new ArrayList<ImageGroup>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_donglixia, container, false);
		mGridView = (StaggeredGridView) rootView.findViewById(R.id.grid_view_donglixia);

		mAdapter = new ImageGroupAdapter(getActivity(), list);

		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);

		NativeObtain nativeObtain = new NativeObtain(getActivity(), handler);
		nativeObtain.start();
		return rootView;
	}

	/**
	 * 更新UI
	 */
	private final Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == Constants.What.Native.GROUP) {

				list.addAll((ArrayList<ImageGroup>) msg.obj);
				mAdapter.notifyDataSetChanged();

			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ImageGroup imageGroup = mAdapter.getItem(position);
		if (imageGroup == null) {
			return;
		}
		ArrayList<String> childList = imageGroup.getImages();
		Intent mIntent = new Intent(getActivity(), ImageListActivity.class);
		mIntent.putExtra(Constants.Extra.DIR_NAME, imageGroup.getDirName());
		mIntent.putStringArrayListExtra(Constants.Extra.URLS, childList);
		startActivity(mIntent);
		getActivity().overridePendingTransition(0, 0);

	}

}
