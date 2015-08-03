package com.example.xuxianjing.activity;

import java.util.ArrayList;
import java.util.List;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.AppManager;
import com.example.xuxianjing.Util.SharePreferenceUtil;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.Util.Utils;
import com.example.xuxianjing.activity.reside.CalendarFragment;
import com.example.xuxianjing.activity.reside.ProfileFragment;
import com.example.xuxianjing.activity.reside.SettingsFragment;
import com.example.xuxianjing.bean.ShareBean;
import com.example.xuxianjing.view.CircleImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity3 extends BaseActivity {
	private static final int SHARE_RETURN = 0X12;
	
	private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;
	private PullToRefreshListView mPullRefreshListView;
	private ArrayList<ShareBean> shareList;
	private ShareListAdapter adapter;
	private ListView actualListView;
	private int pageCount = 10;
	private long mExitTime;
	private String headPath;

	@Override
	public void initWidget(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main_discover);
		shareList = new ArrayList<ShareBean>();
		TopBar topBar = new TopBar(this, "感悟");
		findViewById(R.id.btn_back).setVisibility(View.GONE);
		findViewById(R.id.btn_right).setVisibility(View.VISIBLE);
		CircleImageView imageButton = (CircleImageView) findViewById(R.id.write_image);
        imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Utils.startActivityForResult(MainActivity3.this, SendMessageActivity.class, SHARE_RETURN);
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
									bean.setHeadPath("");
									bean.setImageUrl(avFile.getUrl());
									bean.setContent(avObjects.get(i).getString("content"));
									shareList.add(bean);
//									getHeadData(avObjects, i);
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
		adapter = new ShareListAdapter(MainActivity3.this, shareList);
		actualListView.setAdapter(adapter);
		
		findViewById(R.id.btn_my_icon).setVisibility(View.VISIBLE);
		setUpMenu();
        loading("正在加载数据...");
        if(TextUtils.isEmpty(MyApplication.mCache.getAsString(AVUser.getCurrentUser().getObjectId() + "head"))){
			getHeadData();
		} else {
			headPath = MyApplication.mCache.getAsString(AVUser.getCurrentUser().getObjectId() + "head");
			SharePreferenceUtil.getInstance(getApplicationContext()).setString("head", headPath);
			getRemoteData(); 
		}
	}
	
	private void getHeadData() {
		AVQuery<AVObject> queryHead = new AVQuery<AVObject>("Head");
		queryHead.whereEqualTo("uid", AVUser.getCurrentUser().getObjectId());
		queryHead.findInBackground(new FindCallback<AVObject>() {
			
			@Override
			public void done(List<AVObject> avObjects, AVException e) {
				if (e == null) {
					Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
					if (avObjects.size() == 0) {
						MyApplication.mCache.put(AVUser.getCurrentUser().getObjectId() + "head", "默认头像");
					} else {
						headPath = avObjects.get(avObjects.size() - 1).getAVFile("attached").getUrl();
						MyApplication.mCache.put(AVUser.getCurrentUser().getObjectId() + "head", headPath);
						SharePreferenceUtil.getInstance(getApplicationContext()).setString("head", headPath);
					}
				} else {
				}
				getRemoteData();
			}
		});
	}
	
	private void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     "感悟");
        itemProfile  = new ResideMenuItem(this, R.drawable.icon_profile,  "我的感悟");
        itemCalendar = new ResideMenuItem(this, R.drawable.icon_calendar, "Calendar");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "设置");

        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemCalendar.setOnClickListener(this);
        itemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.btn_my_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        findViewById(R.id.btn_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
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
//						AVQuery<AVObject> queryHead = new AVQuery<AVObject>("Head");
//						queryHead.whereEqualTo("uid", avObjects.get(i).get("uid"));
//						queryHead.findInBackground(new FindCallback<AVObject>() {
//
//							@Override
//							public void done(List<AVObject> avObjects, AVException e) {
//								if (avObjects == null || avObjects.size() == 0) {
//									headPath2 = headPath;
//								} else {
//									headPath2 = avObjects.get(avObjects.size() - 1)
//											.getAVFile("attached").getUrl();
//								}
//								
//							}
//						});
						ShareBean bean = new ShareBean();
						AVFile avFile = avObjects.get(i).getAVFile("attached");
						bean.setHeadPath("");
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
//	String headPath2;
//	private void getHeadData(final List<AVObject> avObjectList) {
//		for (int i = 0; i < avObjectList.size(); i++) {
//			AVQuery<AVObject> queryHead = new AVQuery<AVObject>("Head");
//			queryHead.whereEqualTo("uid", avObjectList.get(i).get("uid"));
//			queryHead.findInBackground(new FindCallback<AVObject>() {
//
//				@Override
//				public void done(List<AVObject> avObjects, AVException e) {
//					if (avObjects == null || avObjects.size() == 0) {
//						headPath2 = headPath;
//					} else {
//						headPath2 = avObjects.get(avObjects.size() - 1)
//								.getAVFile("attached").getUrl();
//					}
//					
//				}
//			});
//			shareList.get(i).setHeadPath(headPath2);
//			
//		}
//		adapter.notifyDataSetChanged();
//	}
	
	
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


	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
//            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
//            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

	@Override
	public void widgetClick(View view) {
		if (view == itemHome){
			
//            changeFragment(new HomeFragment());
        }else if (view == itemProfile){
        	Utils.startActivity(MainActivity3.this, ShareListActivity.class);
//            changeFragment(new ProfileFragment());
        }else if (view == itemCalendar){
//            changeFragment(new CalendarFragment());
        }else if (view == itemSettings){
        	Utils.startActivity(MainActivity3.this, SettingActivity.class);
//            changeFragment(new SettingsFragment());
        }

        resideMenu.closeMenu();
	}
	
	// What good method is to access resideMenu？
    public ResideMenu getResideMenu(){
        return resideMenu;
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				MyApplication.showToast("再按一次退出程序");
				mExitTime = System.currentTimeMillis();
			} else {
				AppManager.getAppManager().AppExit(MainActivity3.this);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
