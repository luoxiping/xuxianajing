package com.example.xuxianjing.activity;

import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.view.library.PhotoView;
import com.example.xuxianjing.view.library.PhotoViewAttacher.OnViewTapListener;

import android.os.Bundle;
import android.view.View;

public class ImageActivity extends BaseActivity {
	private String imagePath;
	
	@Override
	public void initWidget(Bundle savedInstanceState) {
		setContentView(R.layout.activity_image);
		imagePath = getIntent().getExtras().getString("imagePath");
		PhotoView mImageView = (PhotoView) findViewById(R.id.imageview);
		mImageView.setOnViewTapListener(new OnViewTapListener() {
			
			@Override
			public void onViewTap(View view, float x, float y) {
				finish();
			}
		});
		MyApplication.display(mImageView, imagePath);
	}

	@Override
	public void widgetClick(View v) {
		
	}

}
