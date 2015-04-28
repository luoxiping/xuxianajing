package com.example.xuxianjing.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.xuxianjing.AppManager;

public class Utils {
	
	public static void showMessage(String msg){
		Toast.makeText(AppManager.getAppManager().currentActivity(), msg, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * @param activity
	 * @param clazz
	 * @param resultCode
	 */
	public static void startActivityForResult(Activity activity, Class clazz,
			int resultCode) {
		startActivityForResult(activity, clazz, null, resultCode);
	}
	
	/**
	 * @param activity
	 * @param clazz
	 * @param bundle
	 * @param resultCode
	 */
	public static void startActivityForResult(Activity activity, Class clazz,
			Bundle bundle, int resultCode) {
		Intent intent = new Intent();
		intent.setClass(activity, clazz);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		activity.startActivityForResult(intent, resultCode);
	}

	/**
	 * @param context
	 * @param clazz
	 */
	public static void startActivity(Context context, Class clazz) {
		Intent intent = new Intent();
		intent.setClass(context, clazz);
		// 2012-05-15 ���������activity��task���ڣ�
		// ��Activity֮�ϵ�����Activity������.�Ӷ��������ʱ���ֲ�ѯ�����⡣
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
		// ((Activity) context).finish();

	}

	/**
	 * @param context
	 * @param clazz
	 * @param bundle
	 */
	public static void startActivity(Context context, Class clazz, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(context, clazz);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		context.startActivity(intent);
		// ((Activity) context).finish();
	}
}
