package com.example.xuxianjing;

import com.avos.avoscloud.AVOSCloud;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class MyApplication extends Application {
	private static Context mContext;
	@Override
	public void onCreate() {
		super.onCreate();
	    AVOSCloud.initialize(this, "ftou3kqoj8yapb9tf9rg2duda0c0rv6h3klzpgo10w5shyy1", "ug8xmn84uk7x8jfm0l9wrxj4pln83b92oh0bibgphms62sp3");
	    mContext = getApplicationContext();
	}
	
	public static void showToast(String msg){
		Toast.makeText(mContext, msg, 1000).show();
	}
	
	public static void showToast(int resId){
		Toast.makeText(mContext, mContext.getString(resId), 1000).show();
	}

}
