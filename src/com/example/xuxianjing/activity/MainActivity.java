package com.example.xuxianjing.activity;

import com.example.xuxianjing.R;
import com.example.xuxianjing.adapter.SingleDmAdapter;
import com.example.xuxianjing.dialog.Effectstype;
import com.example.xuxianjing.dialog.NiftyDialogBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

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
		dialogBuilder = new NiftyDialogBuilder(MainActivity.this, R.style.dialog_untran);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.upload_picture:
			final String[] txts = new String[] { "本地相册", "相机拍照"};
			View view2 = LayoutInflater.from(MainActivity.this)
					.inflate(R.layout.custom_view, null);
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
							getResources().getDrawable(
									R.drawable.ic_launcher))
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
					singleBaseAdapter.setName(txts[position]);
					singleBaseAdapter.notifyDataSetChanged();
					dialogBuilder.dismiss();
				}
			});
			break;

		default:
			break;
		}
	}

}
