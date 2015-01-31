package com.denghb.donglixia.activity;

import com.denghb.donglixia.R;
import com.denghb.donglixia.fragment.DonglixiaFragment;
import com.denghb.donglixia.fragment.NativeFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * 
 * <pre>
 * Description
 * Copyright:	Copyright (c)2012  
 * Company:		东篱下
 * Author:		denghb
 * Version:		1.0  
 * Create at:	2015年1月30日 下午9:23:31  
 *  
 * 修改历史:
 * 日期    作者    版本  修改描述
 * ------------------------------------------------------------------
 * 
 * </pre>
 */
public class BaseActivity extends FragmentActivity {

	private ViewPager viewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);

		viewPager = (ViewPager) findViewById(R.id.base_view_pager);
		viewPager.setAdapter(new BaseFragmentPagerAdapter(getSupportFragmentManager()));
		viewPager.setCurrentItem(0);
	}

	private class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

		Fragment donglixiaFragment;
		Fragment nativeFragment;

		BaseFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			donglixiaFragment = new DonglixiaFragment();
			nativeFragment = new NativeFragment();
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return donglixiaFragment;
			case 1:
				return nativeFragment;
			default:
				return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_donglixia);
			case 1:
				return getString(R.string.title_native);
			default:
				return null;
			}
		}
	}
}
