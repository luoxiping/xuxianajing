package com.example.xuxianjing.activity;

import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.fragment.SecondFragment;
import com.example.xuxianjing.fragment.ThirdFragment;

public class FirstActivity extends FragmentActivity implements OnClickListener {
	private RadioButton radio_my_game;
	private RadioButton radio_trade;
	private RadioButton radio_center;
	private ViewPager viewPager;
	private FirstFragment firstFragment;
	private SecondFragment secondFragment;
	private ThirdFragment thirdFragment;
	private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //确定为竖屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initWidget();
    }
	
	public void initWidget() {
		setContentView(R.layout.fragment_main_first);
		TopBar topBar = new TopBar(this, "Main");
		viewPager = (ViewPager) findViewById(R.id.view_page);
		radio_my_game = (RadioButton) findViewById(R.id.radio_my_game);
		radio_trade = (RadioButton) findViewById(R.id.radio_trade);
		radio_center = (RadioButton) findViewById(R.id.radio_center);
		radio_my_game.setOnClickListener(this);
		radio_trade.setOnClickListener(this);
		radio_center.setOnClickListener(this);
		firstFragment = new FirstFragment();
		secondFragment = new SecondFragment();
		thirdFragment = new ThirdFragment();
		fragmentList.add(firstFragment);
		fragmentList.add(secondFragment);
		fragmentList.add(thirdFragment);
		viewPager.setOffscreenPageLimit(3);
		FragmentManager fm = getSupportFragmentManager();
		viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			
			@Override
			public int getCount() {
				return fragmentList.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				return fragmentList.get(arg0);
			}
		});
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					radio_my_game.setChecked(true);
					break;
				case 1:
					radio_trade.setChecked(true);
					break;
				case 2:
					radio_center.setChecked(true);
					break;

				default:
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		viewPager.setCurrentItem(0);
		radio_my_game.setChecked(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.radio_my_game:
			viewPager.setCurrentItem(0);
			break;
		case R.id.radio_trade:
			viewPager.setCurrentItem(1);
			break;
		case R.id.radio_center:
			viewPager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}
	
	

}
