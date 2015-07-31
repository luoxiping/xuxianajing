package com.example.xuxianjing.activity;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.SharePreferenceUtil;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.Util.Utils;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends BaseActivity {
	private EditText passwordEditText;
	private EditText userNameEditText;
	private Button registerBtn;

	@Override
	public void initWidget(Bundle savedInstanceState) {
		setContentView(R.layout.register);
		TopBar topBar = new TopBar(this, "注册");
		userNameEditText = (EditText) findViewById(R.id.user_edit);
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
		final String username = userNameEditText.getText().toString().trim();
		final String password = passwordEditText.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			MyApplication.showToast(R.string.hint_input_username);
			return;
		}
		if (TextUtils.isEmpty(password)) {
			MyApplication.showToast(R.string.hint_input_pwd);
			return;
		}
		loading("注册中......");
		AVUser user = new AVUser();
		user.setUsername(username);
		user.setPassword(password);
		user.signUpInBackground(new SignUpCallback() {
			public void done(AVException e) {
				destroyLoading();
				if (e == null) {
					MyApplication.showToast("注册成功");
					setLoadDialogText("登录中...");
					AVUser.logInInBackground(username, password, new LogInCallback() {

						@Override
						public void done(AVUser user, AVException e) {
							destroyLoading();
							if (user != null) {
								MyApplication.showToast("登陆成功");
								SharePreferenceUtil.getInstance(getApplicationContext()).setString("token", user.getSessionToken());
								SharePreferenceUtil.getInstance(getApplicationContext()).setString("phone", user.getUsername());
								SharePreferenceUtil.getInstance(getApplicationContext()).setString("uid", user.getUuid());
								Utils.startActivity(RegisterActivity.this, MainActivity.class);
								finish();
					        } else {
					        	MyApplication.showToast("登陆失败");
					        }
						}
					   
					});
//					Bundle data = new Bundle();
//					data.putString("username", username);
//					data.putString("password", password);
//					Utils.startActivity(RegisterActivity.this, MainActivity.class, data);
					finish();
				} else {
					MyApplication.showToast("注册失败");
				}
			}
		});
	}

}
