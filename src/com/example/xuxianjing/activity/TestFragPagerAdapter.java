package com.example.xuxianjing.activity;

import com.example.xuxianjing.Util.Constant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

public class TestFragPagerAdapter extends FragmentPagerAdapter {

	public TestFragPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		Bundle bundle = new Bundle();
		bundle.putInt(Constant.KEY, Constant.images[arg0%getCount()]);
		Fragment frag = new TestFragment();
		frag.setArguments(bundle);
		return frag;
	}

	@Override
	public int getCount() {
		return 5;
	}
}
