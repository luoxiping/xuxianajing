package com.example.xuxianjing.activity;

import com.androidquery.AQuery;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.view.library.PhotoView;
import com.example.xuxianjing.view.library.PhotoViewAttacher.OnPhotoTapListener;
import com.example.xuxianjing.view.library.PhotoViewAttacher.OnViewTapListener;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class ImageActivity extends BaseActivity {
	private String imagePath;
	
	@Override
	public void initWidget() {
		setContentView(R.layout.activity_image);
		imagePath = getIntent().getExtras().getString("imagePath");
		PhotoView mImageView = (PhotoView) findViewById(R.id.imageview);
		mImageView.setOnViewTapListener(new OnViewTapListener() {
			
			@Override
			public void onViewTap(View view, float x, float y) {
				finish();
			}
		});
		AQuery aq = new AQuery(mImageView);
		aq.image(imagePath, true, true, 200, R.drawable.ic_launcher);
		
	}

	@Override
	public void widgetClick(View v) {
		
	}

}
