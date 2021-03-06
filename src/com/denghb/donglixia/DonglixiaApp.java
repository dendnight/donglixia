package com.denghb.donglixia;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;

/**
 * DonglixiaApp
 * 
 * <pre>
 * Description
 * Copyright:	Copyright (c)2012  
 * Company:		东篱下
 * Author:		denghb
 * Version:		1.0  
 * Create at:	2015年1月31日 上午12:06:37  
 *  
 * 修改历史:
 * 日期    作者    版本  修改描述
 * ------------------------------------------------------------------
 * 
 * </pre>
 */
public class DonglixiaApp extends Application {

	private static Context mContext;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		this.mContext = this;
	}

	/**
	 * 获取Application的 上下文对象
	 * 
	 * @return
	 */
	public static Context getContext() {
		return mContext;
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				// 50 Mb
				.diskCacheSize(50 * 1024 * 1024).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
