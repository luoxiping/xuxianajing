package com.example.xuxianjing.activity;

import org.simple.eventbus.Subscriber;
import com.example.xuxianjing.LoadingDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
	private static final int ACTIVITY_RESUME = 0;
    private static final int ACTIVITY_STOP = 1;
    private static final int ACTIVITY_PAUSE = 2;
    private static final int ACTIVITY_DESTROY = 3;
 
    public int activityState;
    private LoadingDialog dialog;
    private boolean mAllowFullScreen = true;
 
    public abstract void initWidget();
 
    public abstract void widgetClick(View v);
 
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;
    }
 
    @Override
    public void onClick(View v) {
        widgetClick(v);
    }
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //确定为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (mAllowFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        initWidget();
    }
 
    @Override
    protected void onStart() {
        super.onStart();
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        activityState = ACTIVITY_RESUME;
    }
 
    @Override
    protected void onStop() {
        super.onResume();
        activityState = ACTIVITY_STOP;
    }
 
    @Override
    protected void onPause() {
        super.onPause();
        activityState = ACTIVITY_PAUSE;
    }
 
    @Override
    protected void onRestart() {
        super.onRestart();
    }
 
    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityState = ACTIVITY_DESTROY;
    }
    
    public void loading(String msg){
    	if (dialog == null) {
    		dialog = new LoadingDialog(this);
		}   	
    	dialog.show(msg);
    }
    
    public void destroyLoading(){
    	if (dialog == null) {
			dialog = new LoadingDialog(this);
		}
    	dialog.cancel();
    }
    
    @Subscriber(tag = "csuicide")  
    private void csuicideMyself(String msg) {  
        finish();  
    }
 
}
