package com.example.xuxianjing.activity;

import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.SharePreferenceUtil;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.Util.Utils;

public class LoginActivity extends BaseActivity {
	private EditText accoutEdit;
	private EditText pwdEdit;
	private Button loginButton;
	private boolean isHidden = true;
	private ImageView showPwd;
	private TextView registerTextView;
	
	@Override
	public void initWidget() {
		setContentView(R.layout.activity_my_main);
		TopBar topBar = new TopBar(this, "登陆");
		findViewById(R.id.btn_back).setVisibility(View.GONE);
		registerTextView = (TextView) findViewById(R.id.register);
		registerTextView.setOnClickListener(this);
		accoutEdit = (EditText) findViewById(R.id.account_edit);
		pwdEdit = (EditText) findViewById(R.id.pwd_edit);
		loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(this);
		
		showPwd = (ImageView) findViewById(R.id.show_pwd);
		showPwd.setOnClickListener(this);
		
//		AVUser currentUser = AVUser.getCurrentUser();
//		if (currentUser != null) {
//			Utils.startActivity(LoginActivity.this, MainActivity.class);
//			finish();
//		} 
		
		if (SharePreferenceUtil.getInstance(getApplicationContext()).getString("token", null) != null) {
			Utils.startActivity(LoginActivity.this, MainActivity.class);
			finish();
		}
		if (SharePreferenceUtil.getInstance(getApplicationContext()).getString("phone", null) != null) {
			accoutEdit.setText(SharePreferenceUtil.getInstance(getApplicationContext()).getString("phone", null));
		}
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.login_button:
			
			final String accout = accoutEdit.getText().toString();
			final String password = pwdEdit.getText().toString();
			if (handleLoginInput(accout, password)) {
				loading("正在登录中...");
				AVUser.logInInBackground(accout, password, new LogInCallback() {

					@Override
					public void done(AVUser user, AVException e) {
						destroyLoading();
						if (user != null) {
							SharePreferenceUtil.getInstance(getApplicationContext()).setString("token", user.getSessionToken());
							SharePreferenceUtil.getInstance(getApplicationContext()).setString("phone", user.getUsername());
							SharePreferenceUtil.getInstance(getApplicationContext()).setString("uid", user.getUuid());
							Utils.startActivity(LoginActivity.this, MainActivity.class);
							finish();
				        } else {
				        	MyApplication.showToast("登陆失败");
				        }
					}
				   
				});
			}
			break;
		case R.id.show_pwd:
			switchPwdTextVisible();
			break;
		case R.id.register:
			Utils.startActivity(LoginActivity.this, RegisterActivity.class);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 
	 * @param accout
	 * @param pwd
	 */
	public boolean handleLoginInput(String accout, String pwd) {
		if (TextUtils.isEmpty(accout)) {
			Toast.makeText(this, "请输入手机号", Toast.LENGTH_LONG).show();
			return false;
		}
		
		if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
			return false;
		}
		
		return true;
	}
	
	private void switchPwdTextVisible() {
		if (isHidden) {
			pwdEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			showPwd.setImageResource(R.drawable.password_visible);
		} else {
			pwdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
			showPwd.setImageResource(R.drawable.password_invisible);
		}
		isHidden = !isHidden;
		pwdEdit.postInvalidate();
		
		moveCursorToLast(pwdEdit);
	}
	
	/**
	 * 
	 * @param editText
	 */
	public static void moveCursorToLast(EditText editText) {
		if (editText == null) {
			return;
		}

		CharSequence cs = editText.getText();
		if (cs instanceof Spannable) {
			Spannable spanText = (Spannable) cs;
			Selection.setSelection(spanText, cs.length());
		}
	}

}
