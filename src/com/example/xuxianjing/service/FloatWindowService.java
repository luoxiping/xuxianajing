package com.example.xuxianjing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.xuxianjing.Util.MyWindowManager;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;

public class FloatWindowService extends Service {
	/**
	 * 定时器，定时检测当前是创建还是撤销悬浮窗
	 */
	private Timer timer;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (timer == null) {
			timer = new Timer();
			
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	class RefreshTask extends TimerTask {

		@Override
		public void run() {
			//当前界面是桌面，且没有悬浮窗
			if (isHome() && MyWindowManager) {
				
			}
		}
		
	}

	/**
	 * 判断当前桌面是否是桌面
	 * @return
	 */
	private boolean isHome(){
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);  
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);  
        return getHomes().contains(rti.get(0).topActivity.getPackageName());  
	}

	/**
	 * 获得属于桌面的应用的应用包名称
	 * @return 返回包含所有包名的字符串列表
	 */
	private List<String> getHomes(){
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = this.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,  
                PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo) {  
            names.add(ri.activityInfo.packageName);  
        } 
		return names;  
	}

}
