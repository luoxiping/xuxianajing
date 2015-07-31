package com.example.xuxianjing.activity;

import org.simple.eventbus.Subscriber;

import com.example.xuxianjing.LoadingDialog;
import com.example.xuxianjing.Util.AppManager;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public abstract class BaseActivity extends Activity implements OnClickListener {
	private LoadingDialog dialog;
	private static final int ACTIVITY_RESUME = 0;
    private static final int ACTIVITY_STOP = 1;
    private static final int ACTIVITY_PAUSE = 2;
    private static final int ACTIVITY_DESTROY = 3;
 
    public int activityState;
 
    // 是否允许全屏
    private boolean mAllowFullScreen = true;
 
    public abstract void initWidget(Bundle savedInstanceState);
 
    public abstract void widgetClick(View v);
 
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;
    }
 
    @Override
    public void onClick(View v) {
        widgetClick(v);
    }
 
    /***************************************************************************
     * 
     * 打印Activity生命周期
     * 
     ***************************************************************************/
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (mAllowFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消标题
        }
        AppManager.getAppManager().addActivity(this);
        initWidget(savedInstanceState);
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
        AppManager.getAppManager().finishActivity(this);
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
    
    public void setLoadDialogText(String text){
    	dialog.setLoadingText(text);
    }
    
    @Subscriber(tag = "csuicide")  
    private void csuicideMyself(String msg) {  
        finish();  
    }
 
}
