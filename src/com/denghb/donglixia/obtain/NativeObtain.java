package com.denghb.donglixia.obtain;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import com.denghb.donglixia.Constants;
import com.denghb.donglixia.model.ImageGroup;

/**
 * 获取本地图片
 * 
 * <pre>
 * Description
 * Copyright:	Copyright (c)2012  
 * Company:		东篱下
 * Author:		denghb
 * Version:		1.0  
 * Create at:	2015年1月31日 上午12:53:45  
 *  
 * 修改历史:
 * 日期    作者    版本  修改描述
 * ------------------------------------------------------------------
 * 
 * </pre>
 */
public class NativeObtain extends Thread {
	private static final String TAG = MainObtain.class.getSimpleName();

	private Context mContext;

	private Handler handler;

	public NativeObtain(Context mContext, Handler handler) {
		this.mContext = mContext;
		this.handler = handler;
	}

	@Override
	public void run() {

		// 请求完毕发消息
		Message msg = new Message();

		// 存放图片<文件夹,该文件夹下的图片列表>键值对
		ArrayList<ImageGroup> mGruopList = new ArrayList<ImageGroup>();
		// 获取
		int total = 0;
		int status = 0;
		Cursor mCursor = null;
		try {
			Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			ContentResolver mContentResolver = mContext.getContentResolver();
			// 构建查询条件，且只查询jpeg和png的图片
			StringBuilder selection = new StringBuilder();
			selection.append(Media.MIME_TYPE).append("=?");
			selection.append(" or ");
			selection.append(Media.MIME_TYPE).append("=?");

			// 初始化游标
			mCursor = mContentResolver.query(mImageUri, null, selection.toString(), new String[] { "image/jpeg",
					"image/png" }, Media.DATE_TAKEN);
			// 遍历结果
			while (mCursor.moveToNext()) {
				// 获取图片的路径
				String path = mCursor.getString(mCursor.getColumnIndex(Media.DATA));

				// 获取该图片的所在文件夹的路径
				File file = new File(path);
				String parentName = "";
				if (file.getParentFile() != null) {
					parentName = file.getParentFile().getName();
				} else {
					parentName = file.getName();
				}
				// 构建一个imageGroup对象
				ImageGroup item = new ImageGroup();
				// 设置imageGroup的文件夹名称
				item.setDirName(parentName);

				// 寻找该imageGroup是否是其所在的文件夹中的第一张图片
				int searchIdx = mGruopList.indexOf(item);
				if (searchIdx >= 0) {
					// 如果是，该组的图片数量+1
					ImageGroup imageGroup = mGruopList.get(searchIdx);
					imageGroup.addImage(path);
				} else {
					// 否则，将该对象加入到groupList中
					item.addImage(path);
					mGruopList.add(item);
				}
			}

		} catch (Exception e) {
			Log.d(TAG, e.getMessage(), e);
		} finally {
			// 关闭游标
			if (mCursor != null && !mCursor.isClosed()) {
				mCursor.close();
			}
		}
		// 成功
		msg.what = Constants.What.Native.GROUP;
		msg.obj = mGruopList;
		msg.arg1 = status;
		msg.arg2 = total;
		handler.sendMessage(msg);
	}
}
