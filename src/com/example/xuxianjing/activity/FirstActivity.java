package com.example.xuxianjing.activity;

import java.util.ArrayList;

import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.Util.Utils;
import com.example.xuxianjing.fragment.FirstFragment;
import com.example.xuxianjing.fragment.SecondFragment;
import com.example.xuxianjing.fragment.ThirdFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

public class FirstActivity extends BaseActivity  {
	private RadioButton radio_my_game;
	private RadioButton radio_trade;
	private RadioButton radio_center;
	private ViewPager viewPager;
	private FirstFragment firstFragment;
	private SecondFragment secondFragment;
	private ThirdFragment thirdFragment;
	private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
	
	@Override
	public void initWidget() {
		setContentView(R.layout.activity_first);
		viewPager = (ViewPager) findViewById(R.id.view_page);
		radio_my_game = (RadioButton) findViewById(R.id.radio_my_game);
		radio_trade = (RadioButton) findViewById(R.id.radio_trade);
		radio_center = (RadioButton) findViewById(R.id.radio_center);
		radio_my_game.setOnClickListener(this);
		radio_trade.setOnClickListener(this);
		radio_center.setOnClickListener(this);
		TopBar topBar = new TopBar(this, "主页");
		
		firstFragment = new FirstFragment();
		secondFragment = new SecondFragment();
		thirdFragment = new ThirdFragment();
		fragmentList.add(firstFragment);
		fragmentList.add(secondFragment);
		fragmentList.add(thirdFragment);
		viewPager.setOffscreenPageLimit(3);
//		viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.radio_my_game:
			MyApplication.showToast("第一个");
			break;
		case R.id.radio_trade:
			MyApplication.showToast("第二个");
			break;
		case R.id.radio_center:
			MyApplication.showToast("第三个");
			break;
		default:
			break;
		}
	}
	
	private class MyViewPagerAdapter extends FragmentPagerAdapter {

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}
		
	}

}
