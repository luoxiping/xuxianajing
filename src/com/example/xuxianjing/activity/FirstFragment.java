package com.example.xuxianjing.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.avos.avoscloud.LogUtil.log;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.Util.Utils;
import com.example.xuxianjing.fragment.SecondFragment;
import com.example.xuxianjing.fragment.ThirdFragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

public class FirstFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_first, container, false);
		TextView textView = (TextView) view.findViewById(R.id.text);
		textView.setText("第二页");
		log.e("XX", "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
		return view;
	}


}
