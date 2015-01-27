package com.denghb.donglixia;

import android.content.Context;

public class SharedAppContext {

	private static Context context = null;

	public static Context getContentContext() {
		return context;
	}

	public static void setContentContext(Context ctx) {
		context = ctx;
	}

}