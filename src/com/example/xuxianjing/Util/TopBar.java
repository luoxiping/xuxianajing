package com.example.xuxianjing.Util;

import com.example.xuxianjing.R;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TopBar {
	Activity activity;
	IssueListener issueListener = null; 
	BtnRightListener btnRightListener = null;
	ImageView ivHome;
	LinearLayout llHome;

	public interface IssueListener {
		public void issue();
	}
	
	public interface BtnRightListener {
		public void btnRightLis();
	}

	public TopBar(Activity activity) {
		this.activity = activity;
	}
	
	public TopBar(final Activity activity, String title) {
		this.activity = activity;
		bindData(title);
	}

	public void bindData(String title) {
		((TextView) findViewById(R.id.top_title)).setText(title);
		bindData();
	}

	public void bindData() {
		initBack();
	}

	private void initBack() {
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				activity.finish();
			}
		});

	}

	public void setIssueListener(String name, final IssueListener issueListener) {
		this.issueListener = issueListener;
		TextView btn = (TextView)findViewById(R.id.btn_issue);
		if(!name.equals("")){
			btn.setVisibility(View.VISIBLE);
			btn.setText(name);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (Utils.isFastDoubleClick(1500))
						//防止重复点击
						return ;
					issueListener.issue();
				}
			});
		}
	}
	
	public void setBtnRightListener(String name, final BtnRightListener btnRightListener) {
		this.btnRightListener = btnRightListener;
		Button btn = (Button)findViewById(R.id.btn_right);
		if(!name.equals("")){
			btn.setVisibility(View.VISIBLE);
			btn.setText(name);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (Utils.isFastDoubleClick(1500))
						//防止重复点击
						return ;
					btnRightListener.btnRightLis();
				}
			});
		}
	}
	
	public View findViewById(int id){
		if(activity == null)
			return null;
		return activity.findViewById(id);
	}
}