package com.example.xuxianjing.activity;

import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.SharePreferenceUtil;
import com.example.xuxianjing.Util.Utils;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class SplashActivity extends BaseActivity {
	private final int SPLASH_DISPLAY_LENGHT = 1000; 

	@Override
	public void initWidget(Bundle savedInstanceState) {
		setContentView(R.layout.activity_splash);
    	new Handler().postDelayed(new Runnable(){  
            @Override  
            public void run() {  
            	if (SharePreferenceUtil.getInstance(getApplicationContext()).getString("token", null) != null) {
        			Utils.startActivity(SplashActivity.this, MainActivity3.class);
        			finish();
        		} else {
        			Utils.startActivity(SplashActivity.this, LoginActivity.class);
        			finish();
        		}
            }  
        }, SPLASH_DISPLAY_LENGHT);
	}

	@Override
	public void widgetClick(View v) {
		
	}
}
