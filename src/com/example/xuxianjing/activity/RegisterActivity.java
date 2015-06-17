package com.example.xuxianjing.activity;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.Utils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends BaseActivity {
	private EditText nameEditText;
	private EditText passwordEditText;
	private EditText passwordAgainEditText;
	private EditText emailEditText;
	private Button registerBtn;

	@Override
	public void initWidget() {
		setContentView(R.layout.register);
		nameEditText = (EditText) findViewById(R.id.name);
		passwordEditText = (EditText) findViewById(R.id.password);
		passwordAgainEditText = (EditText) findViewById(R.id.password_again);
		emailEditText = (EditText) findViewById(R.id.email);
		registerBtn = (Button) findViewById(R.id.register);
		registerBtn.setOnClickListener(this);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.register:
			register();
			break;

		default:
			break;
		}
	}

	private void register() {
		String name = nameEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();
		String passwordAgain = passwordAgainEditText.getText().toString()
				.trim();
		String email = emailEditText.getText().toString().trim();
		if (TextUtils.isEmpty(name)) {
			MyApplication.showToast("请填写姓名");
			return;
		}
		if (TextUtils.isEmpty(password)) {
			MyApplication.showToast("请填写密码");
			return;
		}
		if (TextUtils.isEmpty(passwordAgain)) {
			MyApplication.showToast("请再次填写密码");
			return;
		}
		if (!password.equals(passwordAgain)) {
			MyApplication.showToast("密码不一致");
			return;
		}
		loading("登录中......");
		AVUser user = new AVUser();
		user.setUsername(name);
		user.setPassword(password);
		user.setEmail(email);
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
