package com.example.xuxianjing.activity;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.LogUtil.log;
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

public class BindPhoneActivity extends BaseActivity {
	private static final int AUTH_CODE_TIME = 0;

	private EditText phoneEditText;
	private Button smsBtn;
	private EditText codeSmsEditText;
	private String username;
	private String password;

	@Override
	public void initWidget(Bundle savedInstanceState) {
		setContentView(R.layout.activity_bind_phone);
		TopBar topBar = new TopBar(this, "绑定手机");
		phoneEditText = (EditText) findViewById(R.id.phone_edit);
		smsBtn = (Button) findViewById(R.id.get_auth_code_button);
		smsBtn.setOnClickListener(this);
		codeSmsEditText = (EditText) findViewById(R.id.auth_code_edit);
		getPreData();
	}

	private void getPreData() {
		username = getIntent().getExtras().getString("username");
		password = getIntent().getExtras().getString("password");
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.get_auth_code_button:
			final String mobile = phoneEditText.getText().toString().trim();
			if (TextUtils.isEmpty(mobile)) {
				MyApplication.showToast(R.string.hint_input_mobile);
				return;
			}
			if (!Utils.valiPhoneNumber(mobile)) {
				MyApplication.showToast(R.string.tip_error_account_format);
				return;
			}
			smsBtn.setEnabled(false);
			smsBtn.setTextColor(Color.LTGRAY);
			log.e("XXXXXXXXXXXXXX", username + "=======" + password);
			sendSmsVcode(mobile, username, password);
			break;

		case R.id.register_button:
			bindMobile();
			break;

		default:
			break;
		}
	}

	private void bindMobile() {
		String codeSms = codeSmsEditText.getText().toString().trim();
		final String mobile = phoneEditText.getText().toString().trim();
		if (TextUtils.isEmpty(codeSms)) {
			MyApplication.showToast("请填写验证码");
			return;
		}
		loading("手机绑定中...");
		AVUser.verifyMobilePhoneInBackground(codeSms, new AVMobilePhoneVerifyCallback() {

			@Override
			public void done(AVException e) {
				if (e == null) {
					AVUser currentUser = AVUser.getCurrentUser();
					if (!currentUser.isMobilePhoneVerified()) {
						MyApplication.showToast("手机绑定成功！");
						setLoadDialogText("正在登录中...");
						AVUser.logInInBackground(mobile, password, new LogInCallback() {

							@Override
							public void done(AVUser user, AVException e) {
								destroyLoading();
								if (user != null) {
									SharePreferenceUtil.getInstance(getApplicationContext()).setString("token", user.getSessionToken());
									SharePreferenceUtil.getInstance(getApplicationContext()).setString("phone", user.getUsername());
									SharePreferenceUtil.getInstance(getApplicationContext()).setString("uid", user.getUuid());
									Utils.startActivity(BindPhoneActivity.this, MainActivity.class);
									finish();
						        } else {
						        	MyApplication.showToast("登陆失败");
						        }
							}
						   
						});
					} else {
						destroyLoading();
						MyApplication.showToast("手机绑定失败！");
					}
				} else {
					destroyLoading();
					MyApplication.showToast("验证码错误！");
				}
			}
		});
	}

	private void sendSmsVcode(final String mobile, String username, String pwd) {
		if (smsBtn.getText().toString().trim().equals("重新发送")) {
			// 如果你的账号需要重新发送短信请参考下面的代码
			AVUser.requestMobilePhoneVerifyInBackground(mobile, new RequestMobileCodeCallback() {

				@Override
				public void done(AVException e) {
					// 发送了验证码以后做点什么呢
				}
			});
		} else {
			AVUser user = new AVUser();
			user.setUsername(username);
			user.setPassword(pwd);
			user.setMobilePhoneNumber(mobile); // 本号码随机生成如有雷同纯属巧合
			user.signUpInBackground(new SignUpCallback() {

				@Override
				public void done(AVException e) {
					if (e == null) {
						
					} else {
						
					}
				}
			});
		}
		

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int second = 60;
					while (second >= 0) {
						Message msg = Message.obtain();
						msg.arg1 = second;
						msg.what = AUTH_CODE_TIME;
						handler.sendMessage(msg);
						--second;
						Thread.sleep(1000);
					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							smsBtn.setTextColor(getResources().getColor(R.color.blue_text));
							smsBtn.setEnabled(true);
						}
					});
				} catch (InterruptedException e) {
				}
			}
		}).start();

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case AUTH_CODE_TIME:
				int arg = msg.arg1;
				if (arg > 0) {
					smsBtn.setEnabled(false);
					smsBtn.setText(arg + "");
					// getAuthCodeBtn.setBackgroundColor(R.color.dark_gray);
				} else {
					smsBtn.setEnabled(true);
					smsBtn.setText("重新发送");
					// getAuthCodeBtn.setBackgroundResource(R.drawable.btn_blue_selector);
				}
				break;

			default:
				break;
			}
		}

	};

}
