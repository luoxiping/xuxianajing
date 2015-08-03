package com.example.xuxianjing.service;

import java.util.ArrayList;
import java.util.List;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HeadService extends Service {

	private ArrayList<Integer> idItems = new ArrayList<Integer>();
    
    @Override  
    public IBinder onBind(Intent intent) {  
        return null;  
    }  
      
    @Override  
    public void onCreate() {  
        super.onCreate();
       
        
    }  
      
    @Override  
    public void onStart(Intent intent, int startId) {  
        super.onStart(intent, startId);  
    }  
      
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        idItems = intent.getExtras().getIntegerArrayList("ids");
        setReaded(idItems);
        return START_REDELIVER_INTENT;
    }  
    
    private void setReaded(final List<Integer> idItems) {
		
	}

}
