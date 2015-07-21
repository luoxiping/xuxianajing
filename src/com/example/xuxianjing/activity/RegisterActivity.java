package com.example.xuxianjing.activity;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.Util.Utils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends BaseActivity {
	private EditText phoneEditText;
	private EditText passwordEditText;
	private Button registerBtn;

	@Override
	public void initWidget() {
		setContentView(R.layout.register);
		TopBar topBar = new TopBar(this, "注册");
		phoneEditText = (EditText) findViewById(R.id.phone_edit);
		passwordEditText = (EditText) findViewById(R.id.pwd_edit);
		registerBtn = (Button) findViewById(R.id.register_button);
		registerBtn.setOnClickListener(this);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.register_button:
			register();
			break;

		default:
			break;
		}
	}

	private void register() {
		String phone = phoneEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();
		
		if (TextUtils.isEmpty(phone)) {
			MyApplication.showToast("请填写姓名");
			return;
		}
		if (TextUtils.isEmpty(password)) {
			MyApplication.showToast("请填写密码");
			return;
		}
		
		loading("登录中......");
		AVUser user = new AVUser();
		user.setUsername(phone);
		user.setPassword(password);
		user.signUpInBackground(new SignUpCallback() {
			public void done(AVException e) {
				destroyLoading();
				if (e == null) {
					MyApplication.showToast("登录成功");
					Utils.startActivity(RegisterActivity.this,
							LoginActivity.class);
					finish();
				} else {
					MyApplication.showToast("登录失败");
				}
			}
		});

	}

}
