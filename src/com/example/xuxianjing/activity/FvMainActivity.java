package com.example.xuxianjing.activity;

import com.example.xuxianjing.R;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class FvMainActivity extends BaseActivity {

	@Override
	public void initWidget() {
		setContentView(R.layout.fv_activity_main);
		Button startFloatWindow = (Button) findViewById(R.id.start_float_window);
		startFloatWindow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(FvMainActivity.this, FloatWindowService.class);
				startService(intent);
				finish();
			}
		});
	}

	@Override
	public void widgetClick(View v) {

	}

}
