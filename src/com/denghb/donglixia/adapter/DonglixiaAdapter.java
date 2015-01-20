package com.denghb.donglixia.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.widget.StaggeredGridView.LayoutParams;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.denghb.donglixia.R;
import com.denghb.donglixia.model.Donglixia;
import com.denghb.donglixia.tools.bitmap.ImageWorker;

/**
 * 
 * @author denghb
 * 
 */
public class DonglixiaAdapter extends BaseAdapter {

	private final Context context;
	private final LayoutInflater mLayoutInflater;
	private final ImageWorker mImageFetcher;

	private final List<Donglixia> list;

	public DonglixiaAdapter(Context context, ImageWorker mImageFetcher, List<Donglixia> list) {
		super();
		this.context = context;
		this.mLayoutInflater = LayoutInflater.from(context);
		this.mImageFetcher = mImageFetcher;
		this.list = list;
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
		LayoutParams lp = null;
		ViewHolder viewholder = null;
		// 头部
		if (0 == position) {
			convertView = mLayoutInflater.inflate(R.layout.header, parent, false);
			lp = new LayoutParams(convertView.getLayoutParams());
			lp.span = 2;

			// convertView.setTag(convertView);

		} else {

			// if (null == convertView) {
			convertView = mLayoutInflater.inflate(R.layout.item, parent, false);

			viewholder = new ViewHolder();
			viewholder.imageView = (ImageView) convertView.findViewById(R.id.donglixia_image);
			viewholder.tagView = (TextView) convertView.findViewById(R.id.donglixia_tag);

			// convertView.setTag(viewholder);
			// } else {
			// viewholder = (ViewHolder) convertView.getTag();
			// }

			// if (null != viewholder) {

			viewholder.tagView.setText(donglixia.getTag());
			// 处理图片
			mImageFetcher.loadImage(donglixia.getUrl(), viewholder.imageView);
			// }

			lp = new LayoutParams(convertView.getLayoutParams());
			lp.span = 1;
		}
		convertView.setLayoutParams(lp);

		return convertView;
	}

	// *** 内容 ***
	private class ViewHolder {
		ImageView imageView;
		TextView tagView;
	}

}
