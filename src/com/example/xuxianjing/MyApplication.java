package com.example.xuxianjing;

import com.avos.avoscloud.AVOSCloud;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		//���ʹ�������ڵ㣬��������д��� AVOSCloud.useAVCloudUS();
	    AVOSCloud.initialize(this, "ftou3kqoj8yapb9tf9rg2duda0c0rv6h3klzpgo10w5shyy1", "ug8xmn84uk7x8jfm0l9wrxj4pln83b92oh0bibgphms62sp3");
	}

}
