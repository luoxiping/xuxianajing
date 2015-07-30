package com.example.xuxianjing;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoadingDialog {
	private Context context;
	private Dialog dialog;
	private ImageView loadingImage;
	private TextView tipTextView;
	private Animation animation;

	public LoadingDialog(Context context) {
		this.context = context;
		dialog = new Dialog(context, R.style.loading_dialog);
		
		LayoutInflater inflater = LayoutInflater.from(context);  
        View view = inflater.inflate(R.layout.loading_dialog, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.loading_dialog);
        
        loadingImage = (ImageView) view.findViewById(R.id.loading_image);  
        tipTextView = (TextView) view.findViewById(R.id.tip_view);
        animation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);  
        
        
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(layout, new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT,  
                LinearLayout.LayoutParams.MATCH_PARENT));
	}
	
	public void show(){
		try {
			if (null!=dialog && !dialog.isShowing()) {
				dialog.show();
				loadingImage.startAnimation(animation); 
			}
		} catch (Exception e) {}
	}
	
	public void show(String msg){
		if (null != tipTextView) {
			tipTextView.setText(msg);
		}
		show();
	}
	
	public void show(int resid){
		show(context.getString(resid));
	}
	
	public void cancel(){
		try {
			if (null!=dialog && dialog.isShowing()) {
				if (null != loadingImage) {
					loadingImage.clearAnimation();
				}
				dialog.cancel();
			}
		} catch (Exception e) {}
	}
	
	public void setLoadingText(String text){
		try {
			if (null!=dialog && dialog.isShowing()) {
				tipTextView.setText(text);
			}
		} catch (Exception e) {}
	}

}
