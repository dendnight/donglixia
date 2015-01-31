package com.denghb.donglixia.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.denghb.donglixia.R;
import com.denghb.donglixia.tools.Helper;
import com.denghb.donglixia.utlis.FileUtil;
import com.denghb.donglixia.utlis.Util;
import com.denghb.donglixia.widget.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 某个图片组中图片列表适配器
 * 
 * @author likebamboo
 */
public class ImageListAdapter extends BaseAdapter {

	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	/**
	 * 上下文对象
	 */
	private Context mContext = null;

	/**
	 * 图片列表
	 */
	private ArrayList<String> mDataList = new ArrayList<String>();

	/**
	 * 选中的图片列表
	 */
	private ArrayList<String> mSelectedList = new ArrayList<String>();

	public ImageListAdapter(Context context, ArrayList<String> list) {
		mDataList = list;
		mContext = context;
		mSelectedList = Util.getSeletedImages(context);
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public String getItem(int position) {
		if (position < 0 || position > mDataList.size()) {
			return null;
		}
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_imagelist, null);
			holder.mImageView = (DynamicHeightImageView) view.findViewById(R.id.item_imagelist_image);
			holder.mClickArea = view.findViewById(R.id.item_imagelist_cb_click_area);
			holder.mSelectedCb = (CheckBox) view.findViewById(R.id.item_imagelist_cb);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		final String path = getItem(position);
		// 加载图片
		ImageLoader.getInstance().displayImage(FileUtil.getFormatFilePath(path), holder.mImageView, Helper.displayImageOptions(),
				animateFirstListener);

		holder.mSelectedCb.setChecked(false);
		// 该图片是否选中
		for (String selected : mSelectedList) {
			if (selected.equals(path)) {
				holder.mSelectedCb.setChecked(true);
			}
		}

		// 可点区域单击事件
		holder.mClickArea.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean checked = holder.mSelectedCb.isChecked();
				holder.mSelectedCb.setChecked(!checked);
				if (!checked) {
					addImage(path);
				} else {
					deleteImage(path);
				}
			}
		});

		return view;
	}

	/**
	 * 将图片地址添加到已选择列表中
	 * 
	 * @param path
	 */
	private void addImage(String path) {
		if (mSelectedList.contains(path)) {
			return;
		}
		mSelectedList.add(path);
	}

	/**
	 * 将图片地址从已选择列表中删除
	 * 
	 * @param path
	 */
	private void deleteImage(String path) {
		mSelectedList.remove(path);
	}

	/**
	 * 获取已选中的图片列表
	 * 
	 * @return
	 */
	public ArrayList<String> getSelectedImgs() {
		return mSelectedList;
	}

	static class ViewHolder {
		public DynamicHeightImageView mImageView;

		public View mClickArea;

		public CheckBox mSelectedCb;
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
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
