package com.example.xuxianjing.view;

import com.example.xuxianjing.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RocketLauncher extends LinearLayout {
	private ImageView launcherImg;
	public static int width;
	public static int height;
	
	public RocketLauncher(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.launcher, this);
		launcherImg = (ImageView) findViewById(R.id.launcher_img);
		width = launcherImg.getLayoutParams().width;
		height = launcherImg.getLayoutParams().height;
	}
	
	public void updateLauncherStatus(boolean isReadyToLaunch){
		if (isReadyToLaunch) {
			launcherImg.setImageResource(R.drawable.launcher_bg_fire);
		} else {
			launcherImg.setImageResource(R.drawable.launcher_bg_hold);
		}
	}

}
