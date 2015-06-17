package com.example.xuxianjing.activity;

import com.example.xuxianjing.R;

import android.view.View;

public class FirstActivity extends BaseActivity {

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_first);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.radio_my_game:
			break;
		case R.id.radio_trade:
			break;
		case R.id.radio_center:
			break;
		default:
			break;
		}
	}

}
