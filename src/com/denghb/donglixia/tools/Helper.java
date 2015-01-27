package com.denghb.donglixia.tools;

import java.util.ArrayList;

import com.denghb.donglixia.R;
import com.denghb.donglixia.model.Donglixia;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author denghb
 *
 */
public class Helper {
	/**
	 * 检查网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * 是否是WIFI连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getTypeName().equals("WIFI")) {
			return true;
		}
		return false;
	}

	/**
	 * 图片缓存配置
	 * 
	 * @return
	 */
	public static DisplayImageOptions displayImageOptions() {
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.empty)
				.showImageForEmptyUri(R.drawable.empty)
				.showImageOnFail(R.drawable.empty).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	/**
	 * 生成10个假数据
	 * 
	 * @return
	 */
	public static ArrayList<Donglixia> generateSampleData() {
		final ArrayList<Donglixia> data = new ArrayList<Donglixia>(10);

		for (int i = 0; i < 10; i++) {
			data.add(new Donglixia());
		}
		return data;
	}
}
