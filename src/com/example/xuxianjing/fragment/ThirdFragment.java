package com.example.xuxianjing.fragment;

import java.lang.reflect.Field;

import com.avos.avoscloud.LogUtil.log;
import com.example.xuxianjing.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ThirdFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_first, container, false);
		TextView textView = (TextView) view.findViewById(R.id.text);
		textView.setText("第三页");
		log.e("XX", "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		return view;
	}
	
	

}
