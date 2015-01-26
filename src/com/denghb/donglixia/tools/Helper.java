package com.denghb.donglixia.tools;

import com.denghb.donglixia.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.Dialog;
import android.content.Context;
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

	public static DisplayImageOptions displayImageOptions() {
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.empty)
				.showImageForEmptyUri(R.drawable.empty)
				.showImageOnFail(R.drawable.empty).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	/**
	 * loading dialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {
		return createLoadingDialog(context, msg, 0);
	}

	/**
	 * loading dialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, int resId) {
		return createLoadingDialog(context, null, resId);
	}

	private static Dialog createLoadingDialog(Context context, String msg,
			int resId) {
		LayoutInflater inflater = LayoutInflater.from(context);
		// 得到加载view
		View v = inflater.inflate(R.layout.dialog_loading, null);
		// 加载布局
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
		// dialog_loading.xml中的ImageView
		ImageView imageView = (ImageView) v
				.findViewById(R.id.dialog_loading_img);
		// 提示文字
		TextView tipTextView = (TextView) v
				.findViewById(R.id.dialog_loading_txt);
		// 加载动画
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.dialog_loading);
		// 使用ImageView显示动画
		imageView.startAnimation(animation);
		// 设置加载信息
		if (null == msg) {
			tipTextView.setText(resId);
		} else {
			tipTextView.setText(msg);
		}
		// 创建自定义样式dialog
		Dialog loadingDialog = new Dialog(context, R.style.dialog_loading);
		// 不可以用“返回键”取消
		loadingDialog.setCancelable(false);

		// 设置布局
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));

		return loadingDialog;
	}
}
