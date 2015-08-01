package com.example.xuxianjing.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.ImageThumbnail;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.Util.Utils;
import com.example.xuxianjing.adapter.SingleDmAdapter;
import com.example.xuxianjing.bean.ShareBean;
import com.example.xuxianjing.dialog.Effectstype;
import com.example.xuxianjing.dialog.NiftyDialogBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SendMessageActivity extends BaseActivity {
	private EditText mEditText;
	private ImageView mImageView;
	private Effectstype effect;
	private NiftyDialogBuilder dialogBuilder;
	private String name;
	private String fileName;
	private String imagePath;
	private Bitmap mBitmap;

	@Override
	public void initWidget(Bundle savedInstanceState) {
		setContentView(R.layout.activity_send_message);
		TopBar topBar = new TopBar(this, "");
		TextView textView = (TextView) findViewById(R.id.btn_issue);
		textView.setVisibility(View.VISIBLE);
		textView.setText("分享");
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				loading("分享中...");
				final AVFile avFile;
				final String content = mEditText.getText().toString().trim();
				if (TextUtils.isEmpty(content)) {
					destroyLoading();
					MyApplication.showToast("请输入分享内容!");
					return;
				}
				if (mBitmap == null) {
					destroyLoading();
					MyApplication.showToast("您必须分享一张图片!");
					return;
				}
				String headPath = Environment.getExternalStorageState() + "/xuxianjing/cache/head.jpg";
				Bitmap headBitmap = BitmapFactory.decodeFile(headPath);
				ByteArrayOutputStream outHead = new ByteArrayOutputStream();
				headBitmap.compress(Bitmap.CompressFormat.JPEG, 60, outHead);
				final byte[] bsHead = outHead.toByteArray();
				
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
				byte[] bs = out.toByteArray();
				AVUser user = AVUser.getCurrentUser();
				final String nameHead = user.getUsername() + "__" + System.currentTimeMillis();
				String name = user.getUsername() + "_" + System.currentTimeMillis();
				avFile = new AVFile(name, bs);
				
				avFile.saveInBackground(new SaveCallback() {
					
					@Override
					public void done(AVException e) {
						if (e == null) {
							final AVFile avFileHead = new AVFile(nameHead, bsHead);
							avFileHead.saveInBackground(new SaveCallback() {
								
								@Override
								public void done(AVException e) {
									if (e == null) {
										AVObject avObject = new AVObject("share");
										avObject.put("attached", avFile);
										avObject.put("attachedHead", avFileHead);
										avObject.put("content", content);
										avObject.put("uid", AVUser.getCurrentUser().getObjectId());
										avObject.saveInBackground(new SaveCallback() {
											
											@Override
											public void done(AVException e) {
												destroyLoading();
												if (e == null) {
													MyApplication.showToast("分享成功!");
													setResult(RESULT_OK);
//													Utils.startActivity(SendMessageActivity.this, ShareListActivity.class);
													finish();
												} else {
													MyApplication.showToast(e.toString());
												}
											}
										});
									} else {
										destroyLoading();
										MyApplication.showToast(e.toString());
									}
								}
							});
							
						} else {
							destroyLoading();
							MyApplication.showToast(e.toString());
						}
					}
				});
			}
		});
		dialogBuilder = new NiftyDialogBuilder(this, R.style.dialog_untran);
		mEditText = (EditText) findViewById(R.id.edit);
		mImageView = (ImageView) findViewById(R.id.imageview);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.add_btn:
			final String[] txts = new String[] { "本地相册", "相机拍照" };
			View view2 = LayoutInflater.from(SendMessageActivity.this).inflate(R.layout.custom_view, null);
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
						Utils.startActivityForResult(SendMessageActivity.this, ImageLocalActivity.class, 0x11); // 本地相册
						break;
					case 1:
						Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						Uri imageUri = Uri
								.fromFile(new File(Environment.getExternalStorageDirectory(), "workupload.jpg"));
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
			MyApplication.display(mImageView, imagePath);
		} else if (requestCode == 0x12 && resultCode == RESULT_OK) {
			// 将保存在本地的图片取出并缩小后显示在界面上
			name = "workupload.jpg";
			fileName = Environment.getExternalStorageDirectory() + "/workupload.jpg";
			imagePath = fileName;
			Bitmap camorabitmap = BitmapFactory.decodeFile(fileName);
			mBitmap = camorabitmap;
			if (null != camorabitmap) {
				// 下面这两句是对图片按照一定的比例缩放，这样就可以完美地显示出来。
				int scale = ImageThumbnail.reckonThumbnail(camorabitmap.getWidth(), camorabitmap.getHeight(), 500, 600);
				Bitmap bitMap = ImageThumbnail.PicZoom(camorabitmap, camorabitmap.getWidth() / scale,
						camorabitmap.getHeight() / scale);
				// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				camorabitmap.recycle();
				// 将处理过的图片显示在界面上，并保存到本地
				mImageView.setVisibility(View.VISIBLE);
				// mImageView.setImageBitmap(bitMap);
				MyApplication.display(mImageView, imagePath);
				String photoLocalPath = ImageThumbnail.savaPhotoToLocal(data, bitMap);
			}
		}
	}

}
