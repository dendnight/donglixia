/**
 * ImageListActivity.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-23
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.denghb.donglixia.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.ArrayList;

import com.denghb.donglixia.Constants;
import com.denghb.donglixia.R;
import com.denghb.donglixia.adapter.ImageListAdapter;
import com.denghb.donglixia.utlis.Util;

/**
 * 某个文件夹下的所有图片列表
 * 
 * @author likebamboo
 */
public class ImageListActivity extends Activity implements OnItemClickListener {

    /**
     * 图片列表GridView
     */
    private GridView mImagesGv = null;

    /**
     * 图片地址数据源
     */
    private ArrayList<String> mImages = new ArrayList<String>();

    /**
     * 适配器
     */
    private ImageListAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        String title = "";
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

        initView();
        if (getIntent().hasExtra(Constants.Extra.URLS)) {
            mImages = getIntent().getStringArrayListExtra(Constants.Extra.URLS);
            setAdapter(mImages);
        }
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        mImagesGv = (GridView)findViewById(R.id.images_gv);
    }

    /**
     * 构建并初始化适配器
     * 
     * @param datas
     */
    private void setAdapter(ArrayList<String> datas) {
        mAdapter = new ImageListAdapter(this, datas);
        mImagesGv.setAdapter(mAdapter);
        mImagesGv.setOnItemClickListener(this);
    }

    /*
     * (non-Javadoc)
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent i = new Intent(this, ViewPagerActivity.class);
        i.putStringArrayListExtra(Constants.Extra.URLS,mImages);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (mAdapter != null) {
            Util.saveSelectedImags(this, mAdapter.getSelectedImgs());
        }
        super.onBackPressed();
    }

}
