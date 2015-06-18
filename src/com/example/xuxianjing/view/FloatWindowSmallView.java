package com.example.xuxianjing.view;

import java.lang.reflect.Field;

import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.MyWindowManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatWindowSmallView extends LinearLayout {
	public static int windowViewWidth;
	public static int windowViewHeight;
	private WindowManager windowManager;
	private LinearLayout smallWindomLayout;
	private int rocketWidth;
	private int rocketHeight;
	private ImageView rocketImg;
	private boolean isPressed;
	/**
	 * 记录手指按下时在小浮窗View上的横坐标
	 */
	private float xInView;
	private float yInView;
	/**
	 * 记录手指当前在屏幕上的x坐标
	 */
	private float xInScreen;
	private float yInScreen;
	/**
	 * 记录手指按下时在屏幕上的x坐标
	 */
	private float xDownInScreen;
	private float yDownInScreen;
	/**
	 * 状态栏高度
	 */
	private static int statusBarHeight;
	/**
	 * 小浮窗的参数
	 */
	private WindowManager.LayoutParams mParams;
	
	public FloatWindowSmallView(Context context) {
		super(context);
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.fv_float_window_small, this);
		smallWindomLayout = (LinearLayout) findViewById(R.id.small_window_layout);
		windowViewWidth = smallWindomLayout.getLayoutParams().width;
		windowViewHeight = smallWindomLayout.getLayoutParams().height;
		rocketImg = (ImageView) findViewById(R.id.rocket_img);
		rocketWidth = rocketImg.getLayoutParams().width;
		rocketHeight = rocketImg.getLayoutParams().height;
		TextView percentView = (TextView) findViewById(R.id.percent);
		percentView.setText(MyWindowManager.getUsedPercentValue(context));
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isPressed = true;
			//当手指按下去的时候要记录必要的相关记录
			xInView = event.getX();
			yInView = event.getY();
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY() - getStatusBarHeight();
			break;
		case MotionEvent.ACTION_MOVE:
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			//手指一动的是偶更新小浮窗的位置和状态
			updateViewStatue();
			updateViewPosition();
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 更新小浮窗在屏幕中的位置
	 */
	private void updateViewPosition() {
		
	}

	private void updateViewStatue() {
		
	}

	/** 
     * 用于获取状态栏的高度。 
     *  
     * @return 返回状态栏高度的像素值。 
     */  
    private int getStatusBarHeight() {  
        if (statusBarHeight == 0) {  
            try {  
                Class<?> c = Class.forName("com.android.internal.R$dimen");  
                Object o = c.newInstance();  
                Field field = c.getField("status_bar_height");  
                int x = (Integer) field.get(o);  
                statusBarHeight = getResources().getDimensionPixelSize(x);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return statusBarHeight;  
    }

}
