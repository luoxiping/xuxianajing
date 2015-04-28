package com.example.xuxianjing.activity;


import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

public abstract class BaseLoginActivity extends BaseFragmentActivity {
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(createContentView());
		initData();
	}

	/**
	 * ���ò���
	 * 
	 * @return
	 */
	protected abstract int createContentView();
	
	/**
	 * ��ʼ������
	 * 
	 * @return
	 */
	protected abstract void initView();
	
	/**
	 * ��ʼ����������
	 * 
	 * @return
	 */
	protected abstract void initLocalData();
	
	/**
	 * ��ʼ��Զ������
	 * 
	 * @return
	 */
	protected abstract void initRemoteData();
	
	private void initData() {
		initView();
		initLocalData();
		initRemoteData();
	};

	/**
	 * �رյ�ǰActivity������д����
	 * 
	 * @return
	 */
	public void back() {
		// TODO Auto-generated method stub
		finish();
	}
	
	@Override
	public void onBackPressed() {
		back();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR) {
				event.startTracking();
			} else {
				onBackPressed();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
