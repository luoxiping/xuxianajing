package com.example.xuxianjing.activity;

import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.SharePreferenceUtil;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.Util.Utils;

import android.view.View;
import android.widget.Button;

public class SettingActivity extends BaseActivity {
	private Button loginBtn;
	
	@Override
	public void initWidget() {
		setContentView(R.layout.activity_setting);
		
		TopBar topBar = new TopBar(SettingActivity.this, "设置");
		loginBtn = (Button) findViewById(R.id.login_btn);
		if (SharePreferenceUtil.getInstance(getApplicationContext()).getString("token", null) == null) {
			loginBtn.setText("登录");
		} else {
			loginBtn.setText("注销");
		}
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			if (loginBtn.equals("登录")) {
				Utils.startActivity(SettingActivity.this, LoginActivity.class);
				finish();
			} else {
				exit();
			}
			break;

		default:
			break;
		}
	}

	private void exit() {
		SharePreferenceUtil.getInstance(getApplicationContext()).setString("token", null);
		Utils.startActivity(SettingActivity.this, LoginActivity.class);
		finish();
	}

}
