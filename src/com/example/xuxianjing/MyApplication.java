package com.example.xuxianjing;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;
import org.kymjs.kjframe.bitmap.BitmapConfig;
import com.avos.avoscloud.AVOSCloud;
import com.example.xuxianjing.cache.ACache;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class MyApplication extends Application {
	public static int screenW;
    public static int screenH;
	private static Context mContext;
	public static KJBitmap kjb;
	public static ACache mCache;
	
	@Override
	public void onCreate() {
		super.onCreate();
	    AVOSCloud.initialize(this, "ftou3kqoj8yapb9tf9rg2duda0c0rv6h3klzpgo10w5shyy1", "ug8xmn84uk7x8jfm0l9wrxj4pln83b92oh0bibgphms62sp3");
	    mContext = getApplicationContext();
	    BitmapConfig config = new BitmapConfig();
        config.isDEBUG = true; //当前是否是调试模式，会打印一些调试log信息
        //如下设置会将图片缓存到SD卡根目录的KJBitmap文件夹内的cache文件夹中
        BitmapConfig.CACHEPATH = "xuxianjing/cache"; //这个就是我们图片缓存的路径了
        kjb = new KJBitmap(config);
        mCache = ACache.get(mContext);
	}
	
	public static void showToast(String msg){
		Toast.makeText(mContext, msg, 1000).show();
	}
	
	public static void showToast(int resId){
		Toast.makeText(mContext, mContext.getString(resId), 1000).show();
	}
	
	public static void display(final View view, String imagePath){
		kjb.display(view, imagePath, new BitmapCallBack() {

			@Override
			public void onFailure(Exception e) {
				super.onFailure(e);
				view.setBackgroundResource(R.drawable.ic_launcher);
			}

			@Override
			public void onPreLoad() {
				super.onPreLoad();
				view.setBackgroundResource(R.drawable.pictures_no);
			}
			
		});
	}

}
