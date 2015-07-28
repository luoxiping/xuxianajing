package com.example.xuxianjing.activity;

import com.example.xuxianjing.R;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class SendMessageActivity extends BaseActivity {
	private EditText mEditText;
	private ImageView mImageView;

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_send_message);
		mEditText = (EditText) findViewById(R.id.edit);
		mImageView = (ImageView) findViewById(R.id.imageview);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.add_btn:
			
			break;

		default:
			break;
		}
	}

}
