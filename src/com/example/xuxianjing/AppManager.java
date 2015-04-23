package com.example.xuxianjing;

import java.util.Stack;

import com.example.xuxianjing.activity.BaseActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

public class AppManager {
	private static Stack<BaseActivity> activityStack;
    private static AppManager instance;
 
    private AppManager() {
    	
    }
    
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        
        return instance;
    }
 
    /**
     * ���Activity��ջ
     */
    public void addActivity(BaseActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<BaseActivity>();
        }
        activityStack.add(activity);
    }
 
    /**
     * ��ȡ��ǰActivity��ջ��Activity��
     */
    public BaseActivity currentActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        BaseActivity activity = activityStack.lastElement();
        return activity;
    }
 
    /**
     * ��ȡ��ǰActivity��ջ��Activity�� û���ҵ��򷵻�null
     */
    public BaseActivity findActivity(Class<?> cls) {
        BaseActivity activity = null;
        for (BaseActivity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return activity;
    }
 
    /**
     * ������ǰActivity��ջ��Activity��
     */
    public void finishActivity() {
        BaseActivity activity = activityStack.lastElement();
        finishActivity(activity);
    }
 
    /**
     * ����ָ����Activity(����)
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }
 
    /**
     * ����ָ����Activity(����)
     */
    public void finishActivity(Class<?> cls) {
        for (BaseActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }
 
    /**
     * �رճ���ָ��activity�����ȫ��activity ���cls��������ջ�У���ջȫ�����
     * 
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        for (BaseActivity activity : activityStack) {
            if (!(activity.getClass().equals(cls))) {
                finishActivity(activity);
            }
        }
    }
 
    /**
     * ��������Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
 
    /**
     * Ӧ�ó����˳�
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            System.exit(0);
        }
    }
}
