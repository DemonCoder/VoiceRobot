package com.itgrape.robot;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	private static Context mContext;
	private static Context settingContext;
	
	@Override
	public void onCreate() {
		super.onCreate();
		setMainContext(mContext);
		setSettingContext(settingContext);
	}

	public static Context getMainContext() {
		return mContext;
	}
	public static void setMainContext(Context mainContext) {
		mContext = mainContext;
	}
	
	public static Context getSettingContext() {
		return settingContext;
	}
	public static void setSettingContext(Context settingContext) {
		MyApplication.settingContext = settingContext;
	}
}
