package com.denghb.donglixia.activity;

import com.denghb.donglixia.Constants;
import com.denghb.donglixia.R;
import com.denghb.donglixia.tools.Helper;
import com.denghb.donglixia.widget.touch.ExtendedViewPager;
import com.denghb.donglixia.widget.touch.TouchImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class ViewPagerActivity extends Activity {

	/**
	 * Step 1: Download and set up v4 support library:
	 * http://developer.android.com/tools/support-library/setup.html Step 2:
	 * Create ExtendedViewPager wrapper which calls
	 * TouchImageView.canScrollHorizontallyFroyo Step 3: ExtendedViewPager is a
	 * custom view and must be referred to by its full package name in XML Step
	 * 4: Write TouchImageAdapter, located below Step 5. The ViewPager in the
	 * XML should be ExtendedViewPager
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager);
		ExtendedViewPager mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);

		String[] urls = getIntent().getStringArrayExtra(Constants.Extra.URLS);
		int position = getIntent().getIntExtra(Constants.Extra.IMAGE_POSITION, 0);
		mViewPager.setAdapter(new TouchImageAdapter(this, urls));
		mViewPager.setCurrentItem(position);

	}

	static class TouchImageAdapter extends PagerAdapter {

		private final String[] urls;
		private final LayoutInflater mLayoutInflater;

		public TouchImageAdapter(Context context, String[] urls) {
			this.mLayoutInflater = LayoutInflater.from(context);

			this.urls = urls;
		}

		@Override
		public int getCount() {
			return urls.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View view = mLayoutInflater.inflate(R.layout.item_viewpager,
					container, false);
			final ViewHolder holder = new ViewHolder();
			assert view != null;
			holder.imageView = (TouchImageView) view.findViewById(R.id.image);
			holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
			
            container.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

			ImageLoader.getInstance().displayImage(urls[position],
					holder.imageView, Helper.displayImageOptions(),
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.progressBar.setProgress(0);
							holder.progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							holder.progressBar.setVisibility(View.GONE);
							holder.imageView.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							holder.progressBar.setVisibility(View.GONE);
							holder.imageView.setVisibility(View.VISIBLE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
							holder.progressBar.setProgress(Math.round(100.0f
									* current / total));
						}
					});

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		static class ViewHolder {
			TouchImageView imageView;
			ProgressBar progressBar;
		}
	}
}
