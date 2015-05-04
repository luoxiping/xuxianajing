package com.example.xuxianjing.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import com.androidquery.AQuery;
import com.example.xuxianjing.MyApplication;
import com.example.xuxianjing.R;
import com.example.xuxianjing.Util.TopBar;
import com.example.xuxianjing.Util.Utils;
import com.example.xuxianjing.adapter.SingleDmAdapter;
import com.example.xuxianjing.dialog.Effectstype;
import com.example.xuxianjing.dialog.NiftyDialogBuilder;

public class MainActivity extends BaseActivity {
	private Button upLoadBtn;
	private ImageView mImageView;
	private Effectstype effect;
	private NiftyDialogBuilder dialogBuilder;

	@Override
	public void initWidget() {
		setContentView(R.layout.main);
		upLoadBtn = (Button) findViewById(R.id.upload_picture);
		mImageView = (ImageView) findViewById(R.id.image);
		TopBar topBar = new TopBar(this, "主页");
		dialogBuilder = new NiftyDialogBuilder(MainActivity.this,
				R.style.dialog_untran);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.upload_picture:
			final String[] txts = new String[] { "本地相册", "相机拍照" };
			View view2 = LayoutInflater.from(MainActivity.this).inflate(
					R.layout.custom_view, null);
			effect = Effectstype.Slideleft;
			String defaultStr = "你好";

			dialogBuilder
					.withTitle("标题")
					// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					// .withDividerColor("#11000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					.withIcon(
							getResources().getDrawable(R.drawable.ic_launcher))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view2, this).show();

			ListView listView = (ListView) view2.findViewById(R.id.list);
			final SingleDmAdapter singleBaseAdapter = new SingleDmAdapter(this,
					txts, defaultStr);
			listView.setAdapter(singleBaseAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parentView, View view,
						int position, long id) {
					// singleBaseAdapter.setName(txts[position]);
					// singleBaseAdapter.notifyDataSetChanged();
					switch (position) {
					case 0:
						Utils.startActivityForResult(MainActivity.this,
								ImageLocalActivity.class, 0x11);
						break;
					case 1:
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
						startActivityForResult(intent, 0x12);
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
			String url = data.getStringExtra("imageUrl");
			Log.e("图片地址：", url);
			AQuery aq = new AQuery(mImageView);
			aq.image(url, true, true, 200, R.drawable.ic_launcher);
		} else if(requestCode == 0x12 && resultCode == RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				MyApplication.showToast("请检查SD卡是否存在！");
				return;
			}
			String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";	
			MyApplication.showToast(name);
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
		
			FileOutputStream b = null;
			File file = new File(Environment.getExternalStorageDirectory() , "myImage");
			file.mkdirs();// 创建文件夹
			String fileName = Environment.getExternalStorageDirectory() + "/myImage/"+name;

			try {
				b = new FileOutputStream(fileName);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			mImageView.setImageBitmap(bitmap);
		}
	}

}
