package com.example.xuxianjing.activity;

import java.util.ArrayList;

import com.avos.avoscloud.AVUser;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.bean.ShareBean;
import com.example.xuxianjing.view.CircleImageView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareListAdapter extends BaseAdapter {
	private ArrayList<ShareBean> items = new ArrayList<ShareBean>();
	private Activity activity;
	
	public ShareListAdapter(Activity activity, ArrayList<ShareBean> shareList) {
		this.activity = activity;
		this.items = shareList;
	}
	
	@Override
	public int getCount() {
		return items == null ? 0 : items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View conternView, ViewGroup parentView) {
		ViewHolder holder = new ViewHolder();
		if (conternView == null) {
			conternView = LayoutInflater.from(activity).inflate(R.layout.activity_share_list_item, null, false);
			holder.imageView = (ImageView) conternView.findViewById(R.id.imageview);
			holder.headView = (CircleImageView) conternView.findViewById(R.id.head_image);
			holder.textView = (TextView) conternView.findViewById(R.id.content);
			holder.nameTextView = (TextView) conternView.findViewById(R.id.name);
			holder.timeView = (TextView) conternView.findViewById(R.id.time);
			conternView.setTag(holder);
		} else {
			holder = (ViewHolder) conternView.getTag();
		}
		holder.nameTextView.setText(AVUser.getCurrentUser().getUsername());
		MyApplication.display(holder.headView, items.get(position).getHeadPath());
		MyApplication.display(holder.imageView, items.get(position).getImageUrl());
		holder.textView.setText(items.get(position).getContent());
		holder.timeView.setText(items.get(position).getTime());
		return conternView;
	}
	
	class ViewHolder{
		ImageView imageView;
		CircleImageView headView;
		TextView textView;
		TextView nameTextView;
		TextView timeView;
	}

}
