package com.example.xuxianjing.activity;

import java.util.ArrayList;
import com.androidquery.AQuery;
import com.example.xuxianjing.R;
import com.example.xuxianjing.bean.ShareBean;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareListAdapter extends BaseAdapter {
	private ArrayList<ShareBean> items;
	private Activity activity;
	
	public ShareListAdapter(Activity activity, ArrayList<ShareBean> items) {
		this.activity = activity;
		this.items = items;
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
			holder.textView = (TextView) conternView.findViewById(R.id.content);
			conternView.setTag(holder);
		} else {
			holder = (ViewHolder) conternView.getTag();
		}
		AQuery aq = new AQuery(holder.imageView);
		aq.image(items.get(position).getImageUrl(), true, true, 300, R.drawable.ic_launcher);
		holder.textView.setText(items.get(position).getContent());
		return conternView;
	}
	
	class ViewHolder{
		ImageView imageView;
		TextView textView;
	}

}
