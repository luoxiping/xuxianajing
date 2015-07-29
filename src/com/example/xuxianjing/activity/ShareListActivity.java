package com.example.xuxianjing.activity;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.Util.Utils;
import com.example.xuxianjing.bean.ShareBean;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ShareListActivity extends BaseActivity {
	private static final int SHARE_RETURN = 0X12;
	private PullToRefreshListView mPullRefreshListView;
	private ArrayList<ShareBean> shareList;
	private ShareListAdapter adapter;
	private ListView actualListView;
	
	@Override
	public void initWidget() {
		setContentView(R.layout.activity_share_list);
		shareList = new ArrayList<ShareBean>();
		TopBar topBar = new TopBar(this, "分享主页");
		TextView textView = (TextView) findViewById(R.id.btn_issue);
		textView.setVisibility(View.VISIBLE);
		textView.setText("去分享");
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Utils.startActivityForResult(ShareListActivity.this, SendMessageActivity.class, SHARE_RETURN);
			}
		});
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
			}
		});
		actualListView = mPullRefreshListView.getRefreshableView();
//		ShareBean bean = new ShareBean();
//    	
//    	bean.setImageUrl("http://ac-ftou3kqo.clouddn.com/Yy3nFkehxiCrFdyZb4m5b2pZhu41ijk76FTx563K");
//    	bean.setContent("Hello World!");
//    	shareList.add(bean);
//		adapter = new ShareListAdapter(ShareListActivity.this, shareList);
//        actualListView.setAdapter(adapter);
		getRemoteData();
	}

	private void getRemoteData() {
		loading("正在加载数据...");
		AVQuery<AVObject> query = new AVQuery<AVObject>("share");
		query.whereEqualTo("uid", AVUser.getCurrentUser().getUuid());
		query.findInBackground(new FindCallback<AVObject>() {
		    public void done(List<AVObject> avObjects, AVException e) {
		    	destroyLoading();
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
		            adapter = new ShareListAdapter(ShareListActivity.this, shareList);
		            actualListView.setAdapter(adapter);
		        } else {
		            Log.d("失败", "查询错误: " + e.getMessage());
		        }
		    }
		});
	}

	@Override
	public void widgetClick(View v) {
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == SHARE_RETURN) {
			getRemoteData();
		}
	}

}
