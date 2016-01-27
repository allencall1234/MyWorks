package com.xj.cnooc.view.bbs;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xj.cnooc.adapter.DownloadListViewAdapter;
import com.xj.cnooc.common.BaseActivity;
import com.xj.cnooc.model.AttachmentBean;
import com.xj.cnooc.view.R;

public class DownloadDetailsActivity extends BaseActivity implements
		OnClickListener {
	private ListView lv_download;
	private DownloadListViewAdapter adapter;

	// private Button btn_back;// 返回
	// private RelativeLayout rellayout_back;
	// private RelativeLayout rellayout_setting;
	private ImageView iv_back;
	private ImageView iv_setting;
	private TextView tv_center_title;// 标题
	// private Button btn_setting;// 设置

	private Context context;

	private ArrayList<AttachmentBean> attList;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);

		attList = (ArrayList<AttachmentBean>) getIntent().getSerializableExtra(
				"att_list");
		context = DownloadDetailsActivity.this;
		initView();

	}

	private void initView() {
		lv_download = (ListView) findViewById(R.id.lv_download);

		tv_center_title = (TextView) findViewById(R.id.tv_center_title);

		// btn_back = (Button) findViewById(R.id.btn_back);
		// btn_setting = (Button) findViewById(R.id.btn_setting);
		// rellayout_back = (RelativeLayout) findViewById(R.id.rellayout_back);
		// rellayout_setting = (RelativeLayout)
		// findViewById(R.id.rellayout_setting);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_setting = (ImageView) findViewById(R.id.iv_setting);

		tv_center_title.setText("下载详情");
		adapter = new DownloadListViewAdapter(attList, context);

		lv_download.setAdapter(adapter);

		iv_back.setOnClickListener(this);
		iv_setting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		case R.id.iv_setting:
			showSettingActivity();
			break;
		// case R.id.btn_back:
		// finish();
		// break;
		//
		// case R.id.btn_setting:
		// startActivity(new Intent(context, SettingActivity.class));

		default:
			break;
		}
	}

}
