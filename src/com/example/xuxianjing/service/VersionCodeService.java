package com.example.xuxianjing.service;

import java.util.List;

import org.simple.eventbus.EventBus;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class VersionCodeService extends Service {
	private String versionName;
    
    @Override  
    public IBinder onBind(Intent intent) {  
        return null;  
    }  
      
    @Override  
    public void onCreate() {  
        super.onCreate();
        EventBus.getDefault().register(this);  
    }  
      
    @Override  
    public void onStart(Intent intent, int startId) {  
        super.onStart(intent, startId);  
    }  
      
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
    	versionName = intent.getExtras().getString("version");
        getVersionName();
        return START_REDELIVER_INTENT;
    }

	private void getVersionName() {
		AVQuery<AVObject> queryHead = new AVQuery<AVObject>("versionCode");
		queryHead.whereEqualTo("objectId", "55c320c900b01b1ab6a36f5a");
		queryHead.findInBackground(new FindCallback<AVObject>() {
			
			@Override
			public void done(List<AVObject> avList, AVException e) {
				String version = avList.get(0).getString("versioncode");
				if (version.equals("获取版本号异常")) {
					Log.e("XX", "11111111111111111111111111111111111111");
					stopSelf();
				} else if (version.equals(versionName)){
					Log.e("XX", "2222222222222222222222222222222222222222");
					stopSelf();
				} else {
					Log.e("XX", "3333333333333333333333333333333333333333");
					EventBus.getDefault().post("下载新版本APP", "newapp");
					stopSelf();
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);  
	}  

}
