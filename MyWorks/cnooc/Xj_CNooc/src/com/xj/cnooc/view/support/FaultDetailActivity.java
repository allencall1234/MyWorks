package com.xj.cnooc.view.support;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.cnooc.biz.UserHttpBiz;
import com.xj.cnooc.common.BaseActivity;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.https.HttpDataCallBack;
import com.xj.cnooc.model.AttachmentBean;
import com.xj.cnooc.model.FaultSupportModel;
import com.xj.cnooc.view.LoginActivity;
import com.xj.cnooc.view.R;
import com.xj.cnooc.view.bbs.DownloadDetailsActivity;
/**
 * 故障详情界面
 * @author qinfan
 *
 * 2015-9-7
 */
public class FaultDetailActivity extends BaseActivity implements OnClickListener{
//	private Button backBtn;
//	private Button btn_setting;
//	private RelativeLayout rellayout_back;
//	private RelativeLayout rellayout_setting;
	private ImageView iv_back;
	private ImageView iv_setting;
	private TextView title;
	private TextView createrName;//发起人
	private TextView tv_faultClassify;//故障分类
	private TextView tv_faultType;//故障类型
	private TextView tv_belongto;//所属电网
	private TextView tv_support_manufacturer;//支持厂家
	private TextView tv_runWay;//电网运行方式
	private TextView tv_support_expert;//支持专家
	private TextView tv_treatment_measures_detail;//故障简述详情
	private TextView tv_date;//发布时间
	private TextView tv_fault_treatment_measures;//简述文字
	private TextView btn_attachment_name;
	private Button btn_check_approval;
	private String type;
	private String fault_id;
	private String titleText;
	private EditText et_approval;
	private FaultSupportModel faultSupportModel=null;
	private List<AttachmentBean> attachmentBeans=null;
	private List<AttachmentBean> dm_attachmentBeans=null;
	private Button btn_download_attachment;
	
	private Button btn_dm_download_attachment;
	private TextView btn_dm_attachment_name;
	private TextView tv_dm_attachment;

	Handler handler=new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if(msg.what==0){
				UserHttpBiz.submitFaultSupportData(Integer.parseInt(fault_id), 4, et_approval.getText().toString(),titleText,MyApp.globelUser.getAccountid(), new HttpDataCallBack() {

					@Override
					public void HttpSuccess(String _result) {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(2);
						Intent intent=new Intent();
						intent.setClass(FaultDetailActivity.this,
								FaultSupportActivity.class);
						startActivity(intent);
						finish();
					}

					@Override
					public void HttpFail(int ErrCode) {
						// TODO Auto-generated method stub

					}
				});
			}
			if(msg.what==1){
				UserHttpBiz.submitEvaluateSupportData(Integer.parseInt(fault_id), 4, et_approval.getText().toString(),titleText,MyApp.globelUser.getAccountid(), new HttpDataCallBack() {

					@Override
					public void HttpSuccess(String _result) {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(3);
						Intent intent=new Intent();
						intent.setClass(FaultDetailActivity.this,
								FaultEvaluateActivity.class);
						startActivity(intent);
						finish();
					}

					@Override
					public void HttpFail(int ErrCode) {
						// TODO Auto-generated method stub

					}
				});
			}
			if(msg.what==2)
			{
				UserHttpBiz.getFaultSupportNum(MyApp.globelUser.getAccountid(), MyApp.globelUser.getType(), new HttpDataCallBack() 
				{
					@Override
					public void HttpSuccess(String _result) 
					{
//						try 
//						{
//							JSONObject object = new JSONObject(_result);
//							if (object.getBoolean("status") == true)
//							{
//								LoginActivity.hitchtotal = object.getInt("hitchtotal");
//							}
//						}
//						catch (JSONException e) 
//						{
//							e.printStackTrace();
//						}
						MyApp.hitchtotal -= 1;
						
					}
					
					@Override
					public void HttpFail(int ErrCode) 
					{
						
					}
				}); 
			}
			if(msg.what==3)
			{
				UserHttpBiz.getFaultSupportNum(MyApp.globelUser.getAccountid(), MyApp.globelUser.getType(), new HttpDataCallBack() 
				{
					@Override
					public void HttpSuccess(String _result) 
					{
//						try 
//						{
//							JSONObject object = new JSONObject(_result);
//							if (object.getBoolean("status") == true)
//							{
//								LoginActivity.asstotal = object.getInt("asstotal");
//							}
//						}
//						catch (JSONException e) 
//						{
//							e.printStackTrace();
//						}
						MyApp.asstotal -= 1;
					}
					
					@Override
					public void HttpFail(int ErrCode) 
					{
						
					}
				}); 
			}
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
		setContentView(R.layout.activity_fault_detail);
		init();
		
	}

	private void init(){
		et_approval = (EditText)findViewById(R.id.et_approval);
//		backBtn = (Button)findViewById(R.id.btn_back);
//		rellayout_back = (RelativeLayout) findViewById(R.id.rellayout_back);
//		rellayout_setting = (RelativeLayout) findViewById(R.id.rellayout_setting);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_setting = (ImageView) findViewById(R.id.iv_setting);
		title = (TextView)findViewById(R.id.tv_center_title); 
		createrName = (TextView)findViewById(R.id.tv_originator);
		tv_faultClassify = (TextView)findViewById(R.id.tv_faultClassify);
		tv_faultType = (TextView)findViewById(R.id.tv_faultType);
		tv_belongto = (TextView)findViewById(R.id.tv_belongto);
		tv_runWay = (TextView)findViewById(R.id.tv_runWay);
		tv_support_manufacturer = (TextView)findViewById(R.id.tv_support_manufacturer);
		tv_support_expert = (TextView)findViewById(R.id.tv_support_expert);
		tv_treatment_measures_detail = (TextView)findViewById(R.id.tv_treatment_measures_detail);
		tv_date = (TextView)findViewById(R.id.tv_date);
		tv_fault_treatment_measures = (TextView)findViewById(R.id.tv_fault_treatment_measures);
		btn_attachment_name = (TextView)findViewById(R.id.btn_attachment_name);
		btn_check_approval = (Button)findViewById(R.id.btn_check_approval);
		btn_check_approval.setOnClickListener(this);
		
		btn_download_attachment = (Button)findViewById(R.id.btn_download_attachment);
		btn_download_attachment.setOnClickListener(this);

		
		btn_dm_download_attachment = (Button)findViewById(R.id.btn_dm_download_attachment);
		btn_dm_attachment_name = (TextView)findViewById(R.id.btn_dm_attachment_name);
		tv_dm_attachment = (TextView)findViewById(R.id.tv_dm_attachment);
		
		btn_dm_download_attachment.setOnClickListener(this);
//		backBtn.setOnClickListener(this);
//		
//		btn_setting = (Button)findViewById(R.id.btn_setting);
//		btn_setting.setOnClickListener(this);
		
//		rellayout_back.setOnClickListener(this);
//		rellayout_setting.setOnClickListener(this);
		
		iv_back.setOnClickListener(this);
		iv_setting.setOnClickListener(this);
		
		Intent intent=getIntent();
		Bundle bundle = intent.getExtras();
		type=bundle.getString("type");
		Serializable s1 = bundle.getSerializable("falutSupportData");
		if(faultSupportModel == null && s1 != null) {
			faultSupportModel = (FaultSupportModel)s1;
		}
		Serializable attachmentSerializable=bundle.getSerializable("attachmentList");
		String pathName="";
		if(attachmentBeans == null && attachmentSerializable != null){
			attachmentBeans = (List<AttachmentBean>)attachmentSerializable;
			for (int i = 0; i < attachmentBeans.size(); i++) {
				pathName+="    "+attachmentBeans.get(i).getName();
			}
		}
		if(pathName==null || pathName.equals("")){
			btn_attachment_name.setText("无附件");
			btn_download_attachment.setVisibility(View.GONE);
		}else{
			btn_attachment_name.setText(pathName);
		}
		fault_id = faultSupportModel.getId();
		titleText = faultSupportModel.getTitle();
		if(type.equals("故障详情")){
			title.setText("故障详情");
			createrName.setText("发起人："+faultSupportModel.getCreaterName());
			tv_faultClassify.setText("故障分类："+faultSupportModel.getSupportSortName());
			tv_faultType.setText("故障类型："+faultSupportModel.getType());
			tv_belongto.setText("所属电网："+faultSupportModel.getElectricNetName());
			if(faultSupportModel.getType().equals("一般")){
				tv_support_manufacturer.setText("支持厂家："+faultSupportModel.getFactoryUserName());
			}
			if(faultSupportModel.getType().equals("严重") || faultSupportModel.getType().equals("隐患")){
				tv_support_manufacturer.setText("支持专家："+faultSupportModel.getProficientName());
			}
			tv_runWay.setText("电网运行方式:"+faultSupportModel.getElectricRunTypeName());
			tv_support_expert.setVisibility(View.GONE);
			tv_treatment_measures_detail.setText("        "+faultSupportModel.getDescription().trim());
			tv_date.setText("发布时间："+faultSupportModel.getCreateTime());
		}else if(type.equals("评估详情")){
			title.setText("评估详情");
			tv_fault_treatment_measures.setText("评估内容：");
			createrName.setText("发起人："+faultSupportModel.getCreaterName());
			tv_faultClassify.setText("评估分类："+faultSupportModel.getSupportSortName());
			tv_faultType.setText("评估类型："+faultSupportModel.getType());
			tv_belongto.setText("所属电网："+faultSupportModel.getElectricNetName());
			tv_support_manufacturer.setVisibility(View.GONE);
			tv_runWay.setText("电网运行方式:"+faultSupportModel.getElectricRunTypeName());
			tv_support_expert.setText("支持专家："+faultSupportModel.getProficientName());
			tv_treatment_measures_detail.setText("        "+faultSupportModel.getDescription().trim());
			tv_dm_attachment.setVisibility(View.VISIBLE);
			btn_dm_attachment_name.setVisibility(View.VISIBLE);
			btn_dm_download_attachment.setVisibility(View.VISIBLE);
			Serializable dm_attachmentSerializable=bundle.getSerializable("dm_attachmentList");
			String dm_pathName="";
			if(dm_attachmentBeans == null && attachmentSerializable != null){
				dm_attachmentBeans = (List<AttachmentBean>)dm_attachmentSerializable;
				for (int i = 0; i < dm_attachmentBeans.size(); i++) {
					dm_pathName+="    "+dm_attachmentBeans.get(i).getName();
				}
			}
			if(dm_pathName==null || dm_pathName.equals("")){
				btn_dm_attachment_name.setText("无附件");
				btn_dm_download_attachment.setVisibility(View.GONE);
			}else{
				btn_dm_attachment_name.setText(dm_pathName);
			}
			tv_date.setText("发布时间："+faultSupportModel.getCreateTime());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fault_detail, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent ;
		switch (v.getId()) {
		case R.id.iv_back:
			if(type.equals("故障详情")){
				intent=new Intent();
				intent.setClass(FaultDetailActivity.this,
						FaultSupportActivity.class);
				startActivity(intent);
				finish();
			}
			if(type.equals("评估详情")){
				intent=new Intent();
				intent.setClass(FaultDetailActivity.this,
						FaultEvaluateActivity.class);
				startActivity(intent);
				finish();
			}
			break;
			
//		case R.id.btn_back:
//			if(type.equals("故障详情")){
//				intent=new Intent();
//				intent.setClass(FaultDetailActivity.this,
//						FaultSupportActivity.class);
//				startActivity(intent);
//				finish();
//			}
//			if(type.equals("评估详情")){
//				intent=new Intent();
//				intent.setClass(FaultDetailActivity.this,
//						FaultEvaluateActivity.class);
//				startActivity(intent);
//				finish();
//			}
//			break;

		case R.id.btn_check_approval:
			if(et_approval.getText().toString().equals("") || et_approval.getText().toString()==null){
				Toast.makeText(FaultDetailActivity.this, "审批意见不能为空！", 1).show();
			}else{
				if(type.equals("故障详情")){
					Thread thread=new Thread(new Runnable() {

						@Override
						public void run() {
							handler.sendEmptyMessage(0);
						}
					});
					thread.start();
				}
				if(type.equals("评估详情")){
					new Thread(new Runnable() {

						@Override
						public void run() {
							handler.sendEmptyMessage(1);
						}
					}).start();

				}
			}
			break;
			
		case R.id.btn_download_attachment:
//			Toast.makeText(FaultDetailActivity.this, ""+faultSupportModel.getPath(), 1).show();
			intent = new Intent(FaultDetailActivity.this, DownloadDetailsActivity.class);
			intent.putExtra("att_list", (Serializable)attachmentBeans);
			startActivity(intent);
			break;
			
		case R.id.btn_dm_download_attachment:
			intent = new Intent(FaultDetailActivity.this, DownloadDetailsActivity.class);
			intent.putExtra("att_list", (Serializable)dm_attachmentBeans);
			startActivity(intent);
			break;
			
		case R.id.iv_setting:
			showSettingActivity();
			break;
			
//		case R.id.btn_setting:
//			intent=new Intent();
//			intent.setClass(FaultDetailActivity.this, SettingActivity.class);
//			startActivity(intent);
//			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
 		if (keyCode == KeyEvent.KEYCODE_BACK) {
 			if(type.equals("故障详情")){
				Intent intent=new Intent();
				intent.setClass(FaultDetailActivity.this,
						FaultSupportActivity.class);
				startActivity(intent);
				finish();
			}
			if(type.equals("评估详情")){
				Intent intent=new Intent();
				intent.setClass(FaultDetailActivity.this,
						FaultEvaluateActivity.class);
				startActivity(intent);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


}
