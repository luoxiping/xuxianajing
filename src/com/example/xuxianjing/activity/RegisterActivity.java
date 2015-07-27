package com.example.xuxianjing.activity;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.Util.Utils;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends BaseActivity {
	private static final int AUTH_CODE_TIME = 0;
	
	private EditText phoneEditText;
	private EditText passwordEditText;
	private EditText userNameEditText;
	private Button registerBtn;
	private Button smsBtn;

	@Override
	public void initWidget() {
		setContentView(R.layout.register);
		TopBar topBar = new TopBar(this, "注册");
		userNameEditText = (EditText) findViewById(R.id.user_edit);
		phoneEditText = (EditText) findViewById(R.id.phone_edit);
		passwordEditText = (EditText) findViewById(R.id.pwd_edit);
		registerBtn = (Button) findViewById(R.id.register_button);
		registerBtn.setOnClickListener(this);
		smsBtn = (Button) findViewById(R.id.get_auth_code_button);
		smsBtn.setOnClickListener(this);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.register_button:
			register();
			break;
		case R.id.get_auth_code_button:
			final String mobile = phoneEditText.getText().toString().trim();
			final String username = userNameEditText.getText().toString().trim();
			final String pwd = passwordEditText.getText().toString().trim();
			if (TextUtils.isEmpty(username)) {
				MyApplication.showToast(R.string.hint_input_username);
				return;
			}
			if (TextUtils.isEmpty(pwd)) {
				MyApplication.showToast(R.string.hint_input_pwd);
				return;
			}
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
			sendSmsVcode(mobile, username, pwd);
			break;

		default:
			break;
		}
	}

	

	private void sendSmsVcode(String mobile, String username, String pwd) {
		AVUser user = new AVUser();
	    user.setUsername(username);
	    user.setPassword(pwd);
	    user.setMobilePhoneNumber(mobile);
	    try {
	    	if (smsBtn.getText().toString().equals("获取验证码")) {
	    		user.signUpInBackground(new SignUpCallback() {
					
					@Override
					public void done(AVException arg0) {
						
					}
				});
			} else {
				//如果你的账号需要重新发送短信请参考下面的代码
			    AVUser.requestMobilePhoneVerifyInBackground(mobile, new RequestMobileCodeCallback() {

			      @Override
			      public void done(AVException e) {
			          //发送了验证码以后做点什么呢
			    	  
			      }
			    });
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
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
							smsBtn.setTextColor(getResources()
									.getColor(R.color.blue_text));
							smsBtn.setEnabled(true);
						}
					});
				} catch (InterruptedException e) {
				}
			}
		}).start();
	    
	}
	
	Handler handler = new Handler(){

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
