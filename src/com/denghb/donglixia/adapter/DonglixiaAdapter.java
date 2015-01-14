package com.denghb.donglixia.adapter;

import java.util.List;

import com.denghb.donglixia.R;
import com.denghb.donglixia.model.Donglixia;
import com.denghb.donglixia.tools.bitmap.ImageWorker;

import android.content.Context;
import android.support.v4.widget.StaggeredGridView.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author denghb
 *
 */
public class DonglixiaAdapter extends BaseAdapter {

	private Context context;
    private LayoutInflater mLayoutInflater;
	private ImageWorker mImageFetcher;
	
	private List<Donglixia> list;
	
	public DonglixiaAdapter(Context context,ImageWorker mImageFetcher,List<Donglixia> list) {
		super();
		this.context = context;
		this.mImageFetcher = mImageFetcher;
		this.list = list;
	}

	@Override
	public int getCount() {
		return null == list?0:list.size();
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
		// 东篱下属性
		Donglixia donglixia = list.get(position);

		ViewHolder viewholder = null;
		if(null == convertView)
		{
			convertView = mLayoutInflater.inflate(R.layout.donglixia_item, parent);
			
			viewholder = new ViewHolder();
			viewholder.imageView = (ImageView)convertView.findViewById(R.id.donglixia_image);
			viewholder.tagView = (TextView)convertView.findViewById(R.id.donglixia_tag);
            
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder) convertView.getTag();
		}
		// 处理图片
		//viewholder.imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ));

		viewholder.tagView.setText(donglixia.getTag());
        mImageFetcher.loadImage(donglixia.getUrl(), viewholder.imageView);
        

        LayoutParams lp = new LayoutParams(convertView.getLayoutParams());
        lp.span = 3;
        convertView.setLayoutParams(lp);
		return convertView;
	}
	
	
	// *** 内容 ***
	private class ViewHolder {
        ImageView imageView;
        TextView tagView;
    }

}
