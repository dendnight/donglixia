package com.denghb.donglixia.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.denghb.donglixia.R;
import com.denghb.donglixia.model.ImageGroup;
import com.denghb.donglixia.tools.Helper;
import com.denghb.donglixia.utlis.FileUtil;
import com.denghb.donglixia.widget.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 分组图片适配器
 * 
 * @author likebamboo
 */
public class ImageGroupAdapter extends BaseAdapter {
	/**
	 * 上下文对象
	 */
	private Context mContext = null;

	/**
	 * 图片列表
	 */
	private List<ImageGroup> mDataList = null;
	
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();


	public ImageGroupAdapter(Context context, List<ImageGroup> list) {
		mDataList = list;
		mContext = context;
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public ImageGroup getItem(int position) {
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
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_imagegroup, null);
			holder.mImageView = (DynamicHeightImageView) view.findViewById(R.id.item_imagegroup_image);
			holder.mTitleView = (TextView) view.findViewById(R.id.item_imagegroup_title);
			holder.mCountView = (TextView) view.findViewById(R.id.item_imagegroup_count);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		ImageGroup item = getItem(position);
		if (item != null) {
			// 图片路径
			String path = item.getFirstImgPath();
			// 标题
			holder.mTitleView.setText(item.getDirName());
			// 计数
			holder.mCountView.setText(mContext.getString(R.string.image_count, item.getImageCount()));
			holder.mImageView.setTag(path);
			// 加载图片
			ImageLoader.getInstance().displayImage(FileUtil.getFormatFilePath(item.getFirstImgPath()), holder.mImageView,
					Helper.displayImageOptions(), animateFirstListener);

		}
		return view;
	}

	static class ViewHolder {
		public DynamicHeightImageView mImageView;

		public TextView mTitleView;

		public TextView mCountView;
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
