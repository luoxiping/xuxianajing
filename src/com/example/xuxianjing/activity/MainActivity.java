package com.example.xuxianjing.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.AppManager;
import com.example.xuxianjing.Util.ImageThumbnail;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.Util.TopBar.IssueListener;
import com.example.xuxianjing.Util.Utils;
import com.example.xuxianjing.adapter.SingleDmAdapter;
import com.example.xuxianjing.dialog.Effectstype;
import com.example.xuxianjing.dialog.NiftyDialogBuilder;
import com.example.xuxianjing.view.library.PhotoView;

public class MainActivity extends BaseActivity implements IssueListener {
	private Button upLoadBtn;
	private Button getImageBtn;
	private ImageView mImageView;
	private ImageView getImageView;
	private ImageView setImageView;
	private Effectstype effect;
	private NiftyDialogBuilder dialogBuilder;
	private String name;
	private String fileName;
	private String imagePath;
	private AVFile fileAf;
	private long mExitTime;

	@Override
	public void initWidget() {
		setContentView(R.layout.main);
		setImageView = (ImageView) findViewById(R.id.setting_imageview);
		setImageView.setVisibility(View.VISIBLE);
		upLoadBtn = (Button) findViewById(R.id.upload_picture);
		mImageView = (ImageView) findViewById(R.id.image);
		getImageView = (ImageView) findViewById(R.id.get_image);
		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// final Dialog mDialog = new Dialog(MainActivity.this,
				// R.style.MyAlertDialog);
				// View view2 =
				// LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_image,
				// null);
				// mDialog.setCancelable(true);
				// mDialog.setContentView(view2);
				// mDialog.show();
				//
				// PhotoView mImageView = (PhotoView)
				// view2.findViewById(R.id.imageview);
				// AQuery aq = new AQuery(mImageView);
				// aq.image(imagePath, true, true, 200, R.drawable.ic_launcher);
				Bundle data = new Bundle();
				data.putString("imagePath", imagePath);
				Utils.startActivity(MainActivity.this, ImageActivity.class, data);
				overridePendingTransition(R.anim.fade_in, R.anim.a_to_b_of_out_1);
			}
		});
		TopBar topBar = new TopBar(this, "主页");
		findViewById(R.id.btn_back).setVisibility(View.GONE);
		dialogBuilder = new NiftyDialogBuilder(this, R.style.dialog_untran);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.upload_picture:
			// Utils.startActivity(this, FirstActivity.class);

			final String[] txts = new String[] { "本地相册", "相机拍照" };
			View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_view, null);
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
						Utils.startActivityForResult(MainActivity.this, ImageLocalActivity.class, 0x11); // 本地相册
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
		case R.id.get_picture:
			getRemoteImage();
			break;

		case R.id.setting_imageview:
			Utils.startActivity(MainActivity.this, SettingActivity.class);
			break;
		case R.id.go_share_btn:
//			Utils.startActivity(MainActivity.this, SendMessageActivity.class);
			Utils.startActivity(MainActivity.this, ShareListActivity.class);
			break;

		default:
			break;
		}
	}

	private void getRemoteImage() {
		String url = fileAf.getUrl();
		AQuery aq = new AQuery(getImageView);
		aq.image(url, true, true, 200, R.drawable.ic_launcher);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0x11 && resultCode == RESULT_OK) {
			imagePath = data.getStringExtra("imageUrl");
			fileName = imagePath;
			String[] names = fileName.split("/");
			name = names[names.length - 1];
			AQuery aq = new AQuery(mImageView);
			aq.image(imagePath, true, true, 260, R.drawable.ic_launcher);
		} else if (requestCode == 0x12 && resultCode == RESULT_OK) {
			// 将保存在本地的图片取出并缩小后显示在界面上
			name = "workupload.jpg";
			fileName = Environment.getExternalStorageDirectory() + "/workupload.jpg";
			imagePath = fileName;
			Bitmap camorabitmap = BitmapFactory.decodeFile(fileName);
			if (null != camorabitmap) {
				// 下面这两句是对图片按照一定的比例缩放，这样就可以完美地显示出来。
				int scale = ImageThumbnail.reckonThumbnail(camorabitmap.getWidth(), camorabitmap.getHeight(), 500, 600);
				Bitmap bitMap = ImageThumbnail.PicZoom(camorabitmap, camorabitmap.getWidth() / scale,
						camorabitmap.getHeight() / scale);
				// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				camorabitmap.recycle();
				// 将处理过的图片显示在界面上，并保存到本地
				mImageView.setVisibility(View.VISIBLE);
//				mImageView.setImageBitmap(bitMap);
				AQuery aq = new AQuery(mImageView);
				aq.image(imagePath, true, true, 260, R.drawable.ic_launcher);
				String photoLocalPath = ImageThumbnail.savaPhotoToLocal(data, bitMap);
			}
			//返回缩略图
			// String sdStatus = Environment.getExternalStorageState();
			// if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			// MyApplication.showToast("请检查SD卡是否存在！");
			// return;
			// }
			// name = new DateFormat().format("yyyyMMdd_hhmmss",
			// Calendar.getInstance(Locale.CHINA)) + ".jpg";
			// MyApplication.showToast(name);
			// Bundle bundle = data.getExtras();
			// Bitmap bitmap = (Bitmap) bundle.get("data");//
			// 获取相机返回的数据，并转换为Bitmap图片格式
			//
			// FileOutputStream b = null;
			// File file = new File(Environment.getExternalStorageDirectory() ,
			// "myImage");
			// file.mkdirs();// 创建文件夹
			// fileName = Environment.getExternalStorageDirectory() +
			// "/myImage/"+name;
			// imagePath = fileName;
			// try {
			// b = new FileOutputStream(fileName);
			// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
			// } finally {
			// try {
			// b.flush();
			// b.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
			// mImageView.setImageBitmap(bitmap);
		}
	}

	@Override
	public void issue() {
		loading("上传图片中...");
		try {
			fileAf = AVFile.withAbsoluteLocalPath(name, fileName);
			fileAf.saveInBackground(new SaveCallback() {
				@Override
				public void done(AVException e) {
					destroyLoading();
					if (e != null) {
						// 上传失败
						MyApplication.showToast("上传失败！");
					} else {
						// 上传成功
						MyApplication.showToast("上传成功！");
					}
				}
			}, new ProgressCallback() {
				@Override
				public void done(Integer percentDone) {
					// 打印进度
					// System.out.println("uploading: " + percentDone);
				}
			});
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				MyApplication.showToast("再按一次退出程序");
				mExitTime = System.currentTimeMillis();
			} else {
				AppManager.getAppManager().AppExit(MainActivity.this);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
