package com.example.xuxianjing.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapConfig;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.SharePreferenceUtil;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.Util.Utils;
import com.example.xuxianjing.bean.ShareBean;
import com.example.xuxianjing.view.CircleImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
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
	private CircleImageView circleImageView;
	private String headPath;

	@Override
	public void initWidget(Bundle savedInstanceState) {
		setContentView(R.layout.activity_share_list);
		shareList = new ArrayList<ShareBean>();
		TopBar topBar = new TopBar(this, "我的感悟");
		setImageView = (ImageView) findViewById(R.id.setting_imageview);
		setImageView.setVisibility(View.VISIBLE);
		circleImageView = (CircleImageView) findViewById(R.id.head_image);
		circleImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Utils.startActivity(ShareListActivity.this, ChangeHeadActivity.class);
			}
		});
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
									getHeadData(avObjects, i);
								}
								
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
		headPath = SharePreferenceUtil.getInstance(getApplicationContext()).getString("head", "");
		if (TextUtils.isEmpty(headPath)) {
			circleImageView.setImageResource(R.drawable.head);
		} else {
			MyApplication.display(circleImageView, headPath);
		}
		getRemoteData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		headPath = SharePreferenceUtil.getInstance(getApplicationContext()).getString("head", "");
		if (TextUtils.isEmpty(headPath)) {
			circleImageView.setImageResource(R.drawable.head);
		} else {
			MyApplication.display(circleImageView, headPath);
		}
	}



	private void getHeadData(final List<AVObject> avObjectList, final int i) {
		AVQuery<AVObject> queryHead = new AVQuery<AVObject>("Head");
		queryHead.whereEqualTo("uid", avObjectList.get(i).getObjectId());
		queryHead.findInBackground(new FindCallback<AVObject>() {
			
			@Override
			public void done(List<AVObject> avObjects, AVException e) {
				ShareBean bean = new ShareBean();
				AVFile avFile = avObjectList.get(i).getAVFile("attached");
				bean.setHeadPath(headPath);
				bean.setImageUrl(avFile.getUrl());
				bean.setContent(avObjectList.get(i).getString("content"));
				shareList.add(bean);
				if (avObjectList.size() == (i + 1)) {
					adapter.notifyDataSetChanged();
				}
			}
		});
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
						getHeadData(avObjects, i);
					}
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
