package com.xj.cnooc.view.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xj.cnooc.adapter.FinishPingYiAdapter;
import com.xj.cnooc.common.BaseActivity;
import com.xj.cnooc.common.MyListView;
import com.xj.cnooc.model.AttachmentBean;
import com.xj.cnooc.model.FaultSupportModel;
import com.xj.cnooc.model.PingYiModel;
import com.xj.cnooc.view.R;
import com.xj.cnooc.view.bbs.DownloadDetailsActivity;

/**
 * 已回复列表详情界面
 * 
 * @author qinfan 2015-9-18
 */
public class FinishFaultDetailActivity extends BaseActivity implements
		OnClickListener {
	// private Button btn_Back;
	// private Button btn_setting;
	// private RelativeLayout rellayout_back;
	// private RelativeLayout rellayout_setting;
	private ImageView iv_back;
	private ImageView iv_setting;
	private MyListView listView;
	private FinishPingYiAdapter adapter;
	private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	private TextView finish_tv_originator;
	private TextView finish_tv_faultClassify;// 故障分类
	private TextView finish_tv_faultType;// 故障类型
	private TextView finish_tv_belongto;// 所属电网
	private TextView finish_tv_support_manufacturer;// 支持厂家
	private TextView finish_tv_runWay;// 电网运行方式
	private TextView finish_tv_support_expert;// 支持专家
	private TextView finish_tv_treatment_measures_detail;// 故障简述详情
	private TextView finish_tv_date;// 发布时间
	private TextView finish_tv_fault_treatment_measures;// 简述文字
	private TextView btn_attachment_name;
	private FaultSupportModel faultSupportModel = null;
	private List<PingYiModel> pingYiModels = null;
	private List<AttachmentBean> attachmentBeans = null;
	private List<AttachmentBean> dm_attachmentBeans = null;
	private TextView tv_center_title;
	private Button finish_btn_download_attachment;

	private Button finish_btn_dm_download_attachment;
	private TextView finish_btn_dm_attachment_name;
	private TextView finish_tv_dm_attachment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish_fault_detail);
		init();
	}

	private void init() {
		// btn_Back = (Button)findViewById(R.id.btn_back);
		// btn_Back.setOnClickListener(this);

		// btn_setting = (Button)findViewById(R.id.btn_setting);
		// btn_setting.setOnClickListener(this);
		finish_btn_dm_download_attachment = (Button) findViewById(R.id.finish_btn_dm_download_attachment);
		finish_btn_dm_attachment_name = (TextView) findViewById(R.id.finish_btn_dm_attachment_name);
		finish_tv_dm_attachment = (TextView) findViewById(R.id.finish_tv_dm_attachment);
		finish_btn_dm_download_attachment.setOnClickListener(this);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_setting = (ImageView) findViewById(R.id.iv_setting);
		iv_back.setOnClickListener(this);
		iv_setting.setOnClickListener(this);

		tv_center_title = (TextView) findViewById(R.id.tv_center_title);

		finish_tv_originator = (TextView) findViewById(R.id.finish_tv_originator);
		finish_tv_originator.setFocusable(true);
		finish_tv_originator.setFocusableInTouchMode(true);
		finish_tv_originator.requestFocus();

		finish_tv_faultClassify = (TextView) findViewById(R.id.finish_tv_faultClassify);
		finish_tv_faultType = (TextView) findViewById(R.id.finish_tv_faultType);
		finish_tv_belongto = (TextView) findViewById(R.id.finish_tv_belongto);
		finish_tv_runWay = (TextView) findViewById(R.id.finish_tv_runWay);
		finish_tv_support_manufacturer = (TextView) findViewById(R.id.finish_tv_support_manufacturer);
		finish_tv_support_expert = (TextView) findViewById(R.id.finish_tv_support_expert);
		finish_tv_treatment_measures_detail = (TextView) findViewById(R.id.finish_tv_treatment_measures_detail);
		finish_tv_date = (TextView) findViewById(R.id.finish_tv_date);
		finish_tv_fault_treatment_measures = (TextView) findViewById(R.id.finish_tv_fault_treatment_measures);
		btn_attachment_name = (TextView) findViewById(R.id.finish_btn_attachment_name);

		finish_btn_download_attachment = (Button) findViewById(R.id.finish_btn_download_attachment);
		finish_btn_download_attachment.setOnClickListener(this);

		listView = (MyListView) findViewById(R.id.pingyiListView);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String type = bundle.getString("type");
		Serializable s1 = bundle.getSerializable("falutSupportData");
		if (faultSupportModel == null && s1 != null) {
			faultSupportModel = (FaultSupportModel) s1;
		}
		Serializable pingyiSerializable = bundle.getSerializable("pingyiList");
		if (pingYiModels == null && pingyiSerializable != null) {
			pingYiModels = (List<PingYiModel>) pingyiSerializable;
			for (int i = 0; i < pingYiModels.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				int isAdopted = pingYiModels.get(i).getIsAdopted();
				if (isAdopted == 0) {
					map.put("pingyi_name", "（未采纳）"
							+ pingYiModels.get(i).getCreaterName() + ":");
				} else {
					map.put("pingyi_name", "（采纳）"
							+ pingYiModels.get(i).getCreaterName());
				}
				map.put("pingyi_content", pingYiModels.get(i).getContent());
				map.put("pingyi_date", pingYiModels.get(i).getCreateTime());

				listItems.add(map);
			}
		}

		Serializable attachmentSerializable = bundle
				.getSerializable("attachmentList");
		String pathName = "";
		if (attachmentBeans == null && attachmentSerializable != null) {
			attachmentBeans = (List<AttachmentBean>) attachmentSerializable;
			for (int i = 0; i < attachmentBeans.size(); i++) {
				pathName += "   " + attachmentBeans.get(i).getName();
			}
		}
		if (pathName == null || pathName.equals("")) {
			btn_attachment_name.setText("无附件");
			finish_btn_download_attachment.setVisibility(View.GONE);
		} else {
			btn_attachment_name.setText(pathName);
		}

		if (type.equals("故障详情")) {
			tv_center_title.setText("故障详情");
			finish_tv_originator.setText("发起人："
					+ faultSupportModel.getCreaterName());
			finish_tv_faultClassify.setText("故障分类："
					+ faultSupportModel.getSupportSortName());
			finish_tv_faultType.setText("故障类型：" + faultSupportModel.getType());
			finish_tv_belongto.setText("所属电网："
					+ faultSupportModel.getElectricNetName());
			finish_tv_runWay.setText("电网运行方式:"
					+ faultSupportModel.getElectricRunTypeName());
			if (faultSupportModel.getType().equals("一般")) {
				finish_tv_support_manufacturer.setText("支持厂家："
						+ faultSupportModel.getFactoryUserName());
			}
			if (faultSupportModel.getType().equals("严重")
					|| faultSupportModel.getType().equals("隐患")) {
				finish_tv_support_manufacturer.setText("支持专家："
						+ faultSupportModel.getProficientName());
			}
			finish_tv_support_expert.setVisibility(View.GONE);
			finish_tv_treatment_measures_detail.setText("        "
					+ faultSupportModel.getDescription().trim());
			finish_tv_date.setText("发布时间：" + faultSupportModel.getCreateTime());
		}
		if (type.equals("评估详情")) {
			finish_btn_dm_attachment_name.setVisibility(View.VISIBLE);
			finish_btn_dm_download_attachment.setVisibility(View.VISIBLE);
			finish_tv_dm_attachment.setVisibility(View.VISIBLE);

			tv_center_title.setText("评估详情");
			finish_tv_fault_treatment_measures.setText("评估内容：");
			finish_tv_originator.setText("发起人："
					+ faultSupportModel.getCreaterName());
			finish_tv_faultClassify.setText("评估分类："
					+ faultSupportModel.getSupportSortName());
			finish_tv_faultType.setText("评估类型：" + faultSupportModel.getType());
			finish_tv_belongto.setText("所属电网："
					+ faultSupportModel.getElectricNetName());
			finish_tv_support_expert.setText("支持专家："
					+ faultSupportModel.getProficientName());
			finish_tv_runWay.setText("电网运行方式:"
					+ faultSupportModel.getElectricRunTypeName());
			finish_tv_support_manufacturer.setVisibility(View.GONE);
			finish_tv_treatment_measures_detail.setText("        "
					+ faultSupportModel.getDescription().trim());
			finish_tv_date.setText("发布时间：" + faultSupportModel.getCreateTime());

			Serializable dm_attachmentSerializable = bundle
					.getSerializable("dm_attachmentList");
			String dm_pathName = "";
			if (dm_attachmentBeans == null && attachmentSerializable != null) {
				dm_attachmentBeans = (List<AttachmentBean>) dm_attachmentSerializable;
				for (int i = 0; i < dm_attachmentBeans.size(); i++) {
					dm_pathName += "    " + dm_attachmentBeans.get(i).getName();
				}
			}
			if (dm_pathName == null || dm_pathName.equals("")) {
				finish_btn_dm_attachment_name.setText("无附件");
				finish_btn_dm_download_attachment.setVisibility(View.GONE);
			} else {
				finish_btn_dm_attachment_name.setText(dm_pathName);
			}
		}

		adapter = new FinishPingYiAdapter(FinishFaultDetailActivity.this,
				listItems);
		listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.finish_fault_detail, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		// case R.id.btn_back:
		// finish();
		// break;

		case R.id.finish_btn_download_attachment:
			intent = new Intent(FinishFaultDetailActivity.this,
					DownloadDetailsActivity.class);
			intent.putExtra("att_list", (Serializable) attachmentBeans);
			startActivity(intent);
			break;

		case R.id.finish_btn_dm_download_attachment:
			intent = new Intent(FinishFaultDetailActivity.this,
					DownloadDetailsActivity.class);
			intent.putExtra("att_list", (Serializable) dm_attachmentBeans);
			startActivity(intent);
			break;

		case R.id.iv_setting:
			showSettingActivity();
			break;

		// case R.id.btn_setting:
		// intent=new Intent();
		// intent.setClass(FinishFaultDetailActivity.this,
		// SettingActivity.class);
		// startActivity(intent);
		// break;

		default:
			break;
		}
	}

}
