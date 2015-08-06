package com.example.xuxianjing.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

public class Utils {
	private static long lastClickTime = System.currentTimeMillis();
	/**
	 * 防止控件被重复点击
	 * 
	 * @param distance
	 *            间隔 默认500毫秒
	 * @return
	 */
	public static boolean isFastDoubleClick(int distance) {
		long time = System.currentTimeMillis();
		long timeD = ((long) time - lastClickTime);
		if (0 < timeD && timeD < (long) distance) {
			Log.i("xdt", "++Double _timeD= " + timeD);
			return true;
		}
		lastClickTime = time;
		return false;
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
	
	/**
	 * 手机号验证
	 * 
	 * @param email
	 * @return
	 */
	public static boolean valiPhoneNumber(String num) {
//		String regex = "^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";
		String regex = "\\d{11}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(num);

		return matcher.matches();
	}
	
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Activity activity) {
	    try {
	        PackageManager manager = activity.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
	        String version = info.versionName;
	        return version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "获取版本号异常";
	    }
	}
}
