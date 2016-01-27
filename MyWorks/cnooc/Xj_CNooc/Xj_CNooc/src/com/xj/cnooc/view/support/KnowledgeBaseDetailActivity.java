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
import android.widget.RatingBar;
import android.widget.TextView;
import com.xj.cnooc.adapter.FeedBackAdapter;
import com.xj.cnooc.adapter.FinishPingYiAdapter;
import com.xj.cnooc.common.BaseActivity;
import com.xj.cnooc.common.MyListView;
import com.xj.cnooc.model.AttachmentBean;
import com.xj.cnooc.model.FaultSupportModel;
import com.xj.cnooc.model.FeedBackModel;
import com.xj.cnooc.model.PingYiModel;
import com.xj.cnooc.view.R;
import com.xj.cnooc.view.bbs.DownloadDetailsActivity;
/**
 * 电网知识库评估和故障类型详情
 * @author qinfan
 *
 * 2015-9-24
 */
public class KnowledgeBaseDetailActivity extends BaseActivity implements OnClickListener{
	private MyListView listView;
	private MyListView feedBackListView;
	private FinishPingYiAdapter adapter;
	private FeedBackAdapter feedBackAdapter;
//	private Button btn_back;
//	private Button btn_setting;
//	private RelativeLayout rellayout_back;
//	private RelativeLayout rellayout_setting;
	private ImageView iv_back;
	private ImageView iv_setting;
	private TextView knowledge_base_tv_originator;
	private TextView knowledge_base_tv_faultClassify;//故障分类
	private TextView knowledge_base_tv_faultType;//故障类型
	private TextView knowledge_base_tv_belongto;//所属电网
	private TextView knowledge_base_tv_support_manufacturer;//支持厂家
	private TextView knowledge_base_tv_runWay;//电网运行方式
	private TextView knowledge_base_tv_support_expert;//支持专家
	private TextView knowledge_base_tv_treatment_measures_detail;//故障简述详情
	private TextView knowledge_base_tv_fault_treatment_measures;//简述文字
	private TextView knowledge_base_tv_date;//发布时间
	private TextView tv_suggest_content;//审批建议
	private TextView tv_spName_content;//审批人
	private TextView tv_result_content;//报告结论
	private TextView tv_bgName_content;//报告人
	private TextView tv_knowledge_success_result_content;//入库结论
	private TextView btn_attachment_name;
	private List<Map<String, Object>> listItems=new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> feedBacklistItems=new ArrayList<Map<String,Object>>();
	private FaultSupportModel faultSupportModel=null;
	private List<PingYiModel> pingYiModels=null;
	private List<FeedBackModel> feedBackModels=null;
	private List<AttachmentBean> attachmentBeans=null;
	private Button knowledge_base_btn_download_attachment;

	private TextView tv_center_title;
	private RatingBar ratingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_knowledge_base_detail);
		init();
	}

	private void init(){
//		btn_back = (Button)findViewById(R.id.btn_back);
//		btn_back.setOnClickListener(this);
//		
//		btn_setting = (Button)findViewById(R.id.btn_setting);
//		btn_setting.setOnClickListener(this);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_setting = (ImageView) findViewById(R.id.iv_setting);
		
		iv_back.setOnClickListener(this);
		iv_setting.setOnClickListener(this);

		tv_center_title = (TextView)findViewById(R.id.tv_center_title);

		knowledge_base_tv_originator = (TextView)findViewById(R.id.knowledge_base_tv_originator);
		knowledge_base_tv_originator.setFocusable(true);
		knowledge_base_tv_originator.setFocusableInTouchMode(true);
		knowledge_base_tv_originator.requestFocus();

		knowledge_base_tv_faultClassify = (TextView)findViewById(R.id.knowledge_base_tv_faultClassify);
		knowledge_base_tv_faultType = (TextView)findViewById(R.id.knowledge_base_tv_faultType);
		knowledge_base_tv_belongto = (TextView)findViewById(R.id.knowledge_base_tv_belongto);
		knowledge_base_tv_runWay = (TextView)findViewById(R.id.knowledge_base_tv_runWay);
		knowledge_base_tv_support_manufacturer = (TextView)findViewById(R.id.knowledge_base_tv_support_manufacturer);
		knowledge_base_tv_support_expert = (TextView)findViewById(R.id.knowledge_base_tv_support_expert);
		knowledge_base_tv_treatment_measures_detail = (TextView)findViewById(R.id.knowledge_base_tv_treatment_measures_detail);
		knowledge_base_tv_date = (TextView)findViewById(R.id.knowledge_base_tv_date);
		btn_attachment_name = (TextView)findViewById(R.id.knowledge_base_btn_attachment_name);
		knowledge_base_tv_fault_treatment_measures = (TextView)findViewById(R.id.knowledge_base_tv_fault_treatment_measures);

		tv_suggest_content = (TextView)findViewById(R.id.tv_suggest_content);
		tv_spName_content = (TextView)findViewById(R.id.tv_spName_content);
		tv_result_content = (TextView)findViewById(R.id.tv_result_content);
		tv_bgName_content = (TextView)findViewById(R.id.tv_bgName_content);
		tv_knowledge_success_result_content = (TextView)findViewById(R.id.tv_knowledge_success_result_content);

		listView = (MyListView)findViewById(R.id.pingyiListView);
		feedBackListView = (MyListView)findViewById(R.id.feedBackListView);

		ratingBar = (RatingBar)findViewById(R.id.knowledge_success_rating);
		knowledge_base_btn_download_attachment = (Button)findViewById(R.id.knowledge_base_btn_download_attachment);
		knowledge_base_btn_download_attachment.setOnClickListener(this);
		
		Intent intent=getIntent();
		Bundle bundle = intent.getExtras();
		String type=bundle.getString("type");
		Serializable s1 = bundle.getSerializable("falutSupportData");
		if(faultSupportModel == null && s1 != null) {
			faultSupportModel = (FaultSupportModel)s1;
		}
		Serializable pingyiSerializable=bundle.getSerializable("pingyiList");
		if(pingYiModels == null && pingyiSerializable != null) {
			pingYiModels = (List<PingYiModel>)pingyiSerializable;
			for (int i = 0; i < pingYiModels.size(); i++) {
				Map<String, Object> map=new HashMap<String, Object>();
				int isAdopted=pingYiModels.get(i).getIsAdopted();
				if(isAdopted==0){
					map.put("pingyi_name", "（未采纳）"+pingYiModels.get(i).getCreaterName()+":");
				}else{
					map.put("pingyi_name", "（采纳）"+pingYiModels.get(i).getCreaterName());
				}
				map.put("pingyi_content", pingYiModels.get(i).getContent());
				map.put("pingyi_date", pingYiModels.get(i).getCreateTime());

				listItems.add(map);
			}
		}

		Serializable feedBackSerializable=bundle.getSerializable("feedbackList");
		if(feedBackModels == null && feedBackSerializable != null) {
			feedBackModels = (List<FeedBackModel>)feedBackSerializable;
			for (int i = 0; i < feedBackModels.size(); i++) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("feed_back_content", feedBackModels.get(i).getContent());
				map.put("feed_back_name", feedBackModels.get(i).getCreateTime()+"  "+feedBackModels.get(i).getCreateName());

				feedBacklistItems.add(map);
			}
		}

		if(type.equals("1")){
			tv_center_title.setText("故障详情");
			knowledge_base_tv_faultClassify.setText("故障分类："+faultSupportModel.getSupportSortName());
			knowledge_base_tv_faultType.setText("故障类型："+faultSupportModel.getType());
			if(faultSupportModel.getType().equals("一般")){
				knowledge_base_tv_support_manufacturer.setText("支持厂家："+faultSupportModel.getFactoryUserName());
			}
			if(faultSupportModel.getType().equals("严重") || faultSupportModel.getType().equals("隐患")){
				knowledge_base_tv_support_manufacturer.setText("支持专家："+faultSupportModel.getProficientName());
			}
			knowledge_base_tv_support_expert.setVisibility(View.GONE);
		}
		
		if(type.equals("2")){
			tv_center_title.setText("评估详情");
			knowledge_base_tv_fault_treatment_measures.setText("评估内容：");
			knowledge_base_tv_faultClassify.setText("评估分类："+faultSupportModel.getSupportSortName());
			knowledge_base_tv_faultType.setText("评估类型："+faultSupportModel.getType());
			knowledge_base_tv_support_expert.setVisibility(View.GONE);
			knowledge_base_tv_support_manufacturer.setText("支持专家："+faultSupportModel.getProficientName());
		}
		knowledge_base_tv_originator.setText("发起人："+faultSupportModel.getCreaterName());
		knowledge_base_tv_belongto.setText("所属电网："+faultSupportModel.getElectricNetName());
		knowledge_base_tv_runWay.setText("电网运行方式："+faultSupportModel.getElectricRunTypeName());
		knowledge_base_tv_support_expert.setVisibility(View.GONE);
		knowledge_base_tv_treatment_measures_detail.setText("        "+faultSupportModel.getDescription().trim());
		Serializable attachmentSerializable=bundle.getSerializable("attachmentList");
		String pathName="";
		if(attachmentBeans == null && attachmentSerializable != null){
			attachmentBeans = (List<AttachmentBean>)attachmentSerializable;
			for (int i = 0; i < attachmentBeans.size(); i++) {
				pathName+="   "+attachmentBeans.get(i).getName();
			}
		}
		if(pathName==null || pathName.equals("")){
			btn_attachment_name.setText("无附件");
			knowledge_base_btn_download_attachment.setVisibility(View.GONE);
		}else{
			btn_attachment_name.setText(pathName);
		}

		knowledge_base_tv_date.setText("发布时间："+faultSupportModel.getCreateTime());

		tv_suggest_content.setText(faultSupportModel.getApprovalContent());
		tv_spName_content.setText(faultSupportModel.getApprovalUserName());
		tv_result_content.setText(faultSupportModel.getConclusion().trim());
		tv_bgName_content.setText("报告人："+faultSupportModel.getSuperName());
		if(faultSupportModel.getCompeten_grade().equals("") || faultSupportModel.getCompeten_grade()==null){
			ratingBar.setRating(0);
		}else{
			ratingBar.setRating(Integer.parseInt(faultSupportModel.getCompeten_grade()));
		}
		tv_knowledge_success_result_content.setText(faultSupportModel.getCompeten_advice());

		adapter = new FinishPingYiAdapter(KnowledgeBaseDetailActivity.this, listItems);
		listView.setAdapter(adapter);

		feedBackAdapter = new FeedBackAdapter(KnowledgeBaseDetailActivity.this, feedBacklistItems);
		feedBackListView.setAdapter(feedBackAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.knowledge_base_detail, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
			
//		case R.id.btn_back:
//			finish();
//			break;
			
		case R.id.knowledge_base_btn_download_attachment:
//			Toast.makeText(KnowledgeBaseDetailActivity.this, ""+faultSupportModel.getPath(), 1).show();
			intent = new Intent(KnowledgeBaseDetailActivity.this, DownloadDetailsActivity.class);
			intent.putExtra("att_list", (Serializable)attachmentBeans);
			startActivity(intent);
			break;
			
		case R.id.iv_setting:
			showSettingActivity();
			break;
			
//		case R.id.btn_setting:
//			intent=new Intent();
//			intent.setClass(KnowledgeBaseDetailActivity.this, SettingActivity.class);
//			startActivity(intent);
//			break;

		default:
			break;
		}
	}

}
