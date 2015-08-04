package com.example.xuxianjing.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.ImageThumbnail;
import com.example.xuxianjing.Util.SharePreferenceUtil;
import com.example.xuxianjing.Util.Utils;
import com.example.xuxianjing.adapter.SingleDmAdapter;
import com.example.xuxianjing.dialog.Effectstype;
import com.example.xuxianjing.dialog.NiftyDialogBuilder;

public class ChangeHeadActivity extends BaseActivity {
	private ImageView headImageView;
	private Effectstype effect;
	private NiftyDialogBuilder dialogBuilder;
	private String name;
	private String fileName;
	private String imagePath;
	private Bitmap mBitmap;
	private String headPath;
	
	@Override
	public void initWidget(Bundle savedInstanceState) {
		setContentView(R.layout.activity_head_change);
		headImageView = (ImageView) findViewById(R.id.head);
		dialogBuilder = new NiftyDialogBuilder(this, R.style.dialog_untran);
		headPath = SharePreferenceUtil.getInstance(getApplicationContext()).getString("head", "");
		if (TextUtils.isEmpty(headPath)) {
			headImageView.setImageResource(R.drawable.head);
		} else {
			MyApplication.display(headImageView, headPath);
		}
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.btn_change_head:
			final String[] txts = new String[] { "本地相册", "相机拍照" };
			View view2 = LayoutInflater.from(ChangeHeadActivity.this).inflate(R.layout.custom_view, null);
			effect = Effectstype.Slideleft;
			String defaultStr = "你好";

			dialogBuilder.withTitle("标题")
					// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					// .withDividerColor("#11000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					.withIcon(getResources().getDrawable(R.drawable.ic_launcher)).isCancelableOnTouchOutside(true) // def
																													// |
																													// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view2, this).show();

			ListView listView = (ListView) view2.findViewById(R.id.list);
			final SingleDmAdapter singleBaseAdapter = new SingleDmAdapter(this, txts, defaultStr);
			listView.setAdapter(singleBaseAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
					// singleBaseAdapter.setName(txts[position]);
					// singleBaseAdapter.notifyDataSetChanged();
					switch (position) {
					case 0:
						Utils.startActivityForResult(ChangeHeadActivity.this, ImageLocalActivity.class, 0x11); // 本地相册
						break;
					case 1:
						Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						Uri imageUri = Uri
								.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpeg"));
						// 指定照片保存路径（SD卡），workupload.jpg为一个临时文件，每次拍照后这个图片都会被替换
						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
						startActivityForResult(cameraIntent, 0x12);
						break;

					default:
						break;
					}
					dialogBuilder.dismiss();
				}
			});
			break;
		case R.id.btn_save_head:
			loading("保存中...");
			final AVFile avFile;
			if (mBitmap == null) {
				destroyLoading();
				MyApplication.showToast("您必须先更换头像");
				return;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
			byte[] bs = out.toByteArray();
			final AVUser user = AVUser.getCurrentUser();
			String name = user.getUsername() + "_" + System.currentTimeMillis();
			avFile = new AVFile(name, bs);
			avFile.saveInBackground(new SaveCallback() {
				
				@Override
				public void done(AVException e) {
					if (e == null) {
						user.put("headav", avFile);
						user.saveInBackground(new SaveCallback() {
							
							@Override
							public void done(AVException e) {
								destroyLoading();
								if (e == null) {
									MyApplication.showToast("保存成功");
								} else {
									MyApplication.showToast("保存失败");
								}
							}
						});
					}else {
						destroyLoading();
						MyApplication.showToast("保存失败");
					}
				}
				});
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0x11 && resultCode == RESULT_OK) {
			imagePath = data.getStringExtra("imageUrl");
			fileName = imagePath;
			mBitmap = BitmapFactory.decodeFile(fileName);
			String[] names = fileName.split("/");
			name = names[names.length - 1];
			MyApplication.display(headImageView, imagePath);
		} else if (requestCode == 0x12 && resultCode == RESULT_OK) {
			// 将保存在本地的图片取出并缩小后显示在界面上
			name = "head.jpeg";
			fileName = Environment.getExternalStorageDirectory() + "/head.jpeg";
			imagePath = fileName;
			mBitmap = BitmapFactory.decodeFile(fileName);
			Bitmap camorabitmap = BitmapFactory.decodeFile(fileName);
			if (null != camorabitmap) {
				// 下面这两句是对图片按照一定的比例缩放，这样就可以完美地显示出来。
				int scale = ImageThumbnail.reckonThumbnail(camorabitmap.getWidth(), camorabitmap.getHeight(), 500, 600);
				Bitmap bitMap = ImageThumbnail.PicZoom(camorabitmap, camorabitmap.getWidth() / scale,
						camorabitmap.getHeight() / scale);
				// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				camorabitmap.recycle();
				// 将处理过的图片显示在界面上，并保存到本地
				headImageView.setVisibility(View.VISIBLE);
				MyApplication.display(headImageView, imagePath);
				String photoLocalPath = ImageThumbnail.savaPhotoToLocal(data, bitMap);
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
