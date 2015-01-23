package com.denghb.donglixia.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denghb.donglixia.R;
import com.denghb.donglixia.model.Donglixia;
import com.denghb.donglixia.widget.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 
 * @author denghb
 * 
 */
public class DonglixiaAdapter extends BaseAdapter {

	private final Context context;
	private final LayoutInflater mLayoutInflater;

	private final List<Donglixia> list;
	DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	private final Random mRandom;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

	public DonglixiaAdapter(Context context, List<Donglixia> list) {
		super();
		this.context = context;
		this.mLayoutInflater = LayoutInflater.from(context);
		this.list = list;

		mRandom = new Random();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		return null == list ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("position :", position + "");
		// 东篱下属性
		final Donglixia donglixia = list.get(position);
		ViewHolder viewholder = null;

		if (null == convertView) {
			convertView = mLayoutInflater.inflate(R.layout.item, parent, false);

			viewholder = new ViewHolder();
			viewholder.imageView = (DynamicHeightImageView) convertView
					.findViewById(R.id.donglixia_image);
			viewholder.tagView = (TextView) convertView
					.findViewById(R.id.donglixia_tag);

			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}

		// if (null != viewholder) {

		viewholder.tagView.setText(donglixia.getTag());
		// 处理图片
		ImageLoader.getInstance().displayImage(donglixia.getUrl(),
				viewholder.imageView, options, animateFirstListener);

		double positionHeight = getPositionRatio(position);
		viewholder.imageView.setHeightRatio(positionHeight);

		return convertView;
	}

	private double getPositionRatio(final int position) {
		double ratio = sPositionHeightRatios.get(position, 0.0);
		// if not yet done generate and stash the columns height
		// in our real world scenario this will be determined by
		// some match based on the known height and width of the image
		// and maybe a helpful way to get the column height!
		if (ratio == 0) {
			ratio =  (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
			sPositionHeightRatios.append(position, ratio);
		}
		return ratio;
	}

	// *** 内容 ***
	private class ViewHolder {
		DynamicHeightImageView imageView;
		TextView tagView;
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
