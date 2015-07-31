package com.example.xuxianjing.activity.reside;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.xuxianjing.LoadingDialog;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.Utils;
import com.example.xuxianjing.activity.MainActivity;
import com.example.xuxianjing.activity.SendMessageActivity;
import com.example.xuxianjing.activity.ShareListAdapter;
import com.example.xuxianjing.bean.ShareBean;
import com.example.xuxianjing.view.CircleImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.special.ResideMenu.ResideMenu;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午1:33
 * Mail: specialcyci@gmail.com
 */
public class HomeFragment extends Fragment {
	private LoadingDialog dialog;
    private View parentView;
    private ResideMenu resideMenu;
    
    private static final int SHARE_RETURN = 0X12;
	private PullToRefreshListView mPullRefreshListView;
	private ArrayList<ShareBean> shareList;
	private ShareListAdapter adapter;
	private ListView actualListView;
	private int pageCount = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.home, container, false);
        shareList = new ArrayList<ShareBean>();
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        MainActivity parentActivity = (MainActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        CircleImageView imageButton = (CircleImageView) parentView.findViewById(R.id.write_image);
        imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Utils.startActivityForResult(getActivity(), SendMessageActivity.class, SHARE_RETURN);
			}
		});
//        parentView.findViewById(R.id.pull_refresh_list);
        mPullRefreshListView = (PullToRefreshListView) parentView.findViewById(R.id.pull_refresh_list);
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
		adapter = new ShareListAdapter(getActivity(), shareList);
		actualListView.setAdapter(adapter);
		loading("正在加载数据...");
		getRemoteData();
        // add gesture operation's ignored views
//        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);
//        resideMenu.addIgnoredView(ignored_view);
    }
    
    private void getRemoteData() {
		AVQuery<AVObject> query = new AVQuery<AVObject>("share");
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != getActivity().RESULT_OK) {
			return;
		}
		if (requestCode == SHARE_RETURN) {
			loading("正在加载数据...");
			shareList.clear();
			getRemoteData();
		}
	}
    
    private void loading(String msg){
    	if (dialog == null) {
    		dialog = new LoadingDialog(getActivity());
		}   	
    	dialog.show(msg);
    }
    
    private void destroyLoading(){
    	if (dialog == null) {
			dialog = new LoadingDialog(getActivity());
		}
    	dialog.cancel();
    }

}
