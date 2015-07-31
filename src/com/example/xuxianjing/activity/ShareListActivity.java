package com.example.xuxianjing.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.Util.Utils;
import com.example.xuxianjing.bean.ShareBean;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShareListActivity extends BaseActivity {
	private static final int SHARE_RETURN = 0X12;
	private PullToRefreshListView mPullRefreshListView;
	private ArrayList<ShareBean> shareList;
	private ShareListAdapter adapter;
	private ListView actualListView;
	private ImageView setImageView;
	private int pageCount = 10;

	@Override
	public void initWidget() {
		setContentView(R.layout.activity_share_list);
		shareList = new ArrayList<ShareBean>();
		TopBar topBar = new TopBar(this, "我的分享");
		setImageView = (ImageView) findViewById(R.id.setting_imageview);
		setImageView.setVisibility(View.VISIBLE);
//		TextView textView = (TextView) findViewById(R.id.btn_issue);
//		textView.setVisibility(View.VISIBLE);
//		textView.setText("去分享");
//		textView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View view) {
//				Utils.startActivityForResult(ShareListActivity.this, SendMessageActivity.class, SHARE_RETURN);
//			}
//		});
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (mPullRefreshListView.isHeaderShown()) {
					pageCount = 10;
					shareList.clear();
					getRemoteData();
				} else if (mPullRefreshListView.isFooterShown()) {
					AVQuery<AVObject> query = new AVQuery<AVObject>("share");
					query.whereEqualTo("uid", AVUser.getCurrentUser().getObjectId());
					query.setLimit(10); // 限制最多10个结果
					query.setSkip(pageCount); // 忽略前10个
					query.orderByDescending("createdAt");
					query.findInBackground(new FindCallback<AVObject>() {
						public void done(List<AVObject> avObjects, AVException e) {
							destroyLoading();
							mPullRefreshListView.onRefreshComplete();
							if (e == null) {
								Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
								if (avObjects.size() == 0) {
									MyApplication.showToast("已经加载完所有数据！");
									return;
								}
								pageCount = pageCount + avObjects.size();
								for (int i = 0; i < avObjects.size(); i++) {
									ShareBean bean = new ShareBean();
									AVFile avFile = avObjects.get(i).getAVFile("attached");
									bean.setImageUrl(avFile.getUrl());
									bean.setContent(avObjects.get(i).getString("content"));
									shareList.add(bean);
								}
								adapter.notifyDataSetChanged();
							} else {
								Log.d("失败", "查询错误: " + e.getMessage());
							}
						}
					});
				}

			}
		});
		actualListView = mPullRefreshListView.getRefreshableView();
		adapter = new ShareListAdapter(ShareListActivity.this, shareList);
		actualListView.setAdapter(adapter);
		loading("正在加载数据...");
		getRemoteData();
	}

	private void getRemoteData() {
		AVQuery<AVObject> query = new AVQuery<AVObject>("share");
		query.whereEqualTo("uid", AVUser.getCurrentUser().getObjectId());
		query.setLimit(10); // 限制最多10个结果
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> avObjects, AVException e) {
				destroyLoading();
				mPullRefreshListView.onRefreshComplete();
				if (e == null) {
					Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
					if (avObjects.size() == 0) {
						return;
					}
					for (int i = 0; i < avObjects.size(); i++) {
						ShareBean bean = new ShareBean();
						AVFile avFile = avObjects.get(i).getAVFile("attached");
						bean.setImageUrl(avFile.getUrl());
						bean.setContent(avObjects.get(i).getString("content"));
						shareList.add(bean);

					}
					adapter.notifyDataSetChanged();
				} else {
					Log.d("失败", "查询错误: " + e.getMessage());
				}
			}
		});
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.setting_imageview:
			Utils.startActivity(ShareListActivity.this, SettingActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == SHARE_RETURN) {
			loading("正在加载数据...");
			shareList.clear();
			getRemoteData();
		}
	}

}
