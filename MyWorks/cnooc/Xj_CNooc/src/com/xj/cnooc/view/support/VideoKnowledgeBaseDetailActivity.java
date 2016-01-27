package com.xj.cnooc.view.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xj.cnooc.common.BaseActivity;
import com.xj.cnooc.model.AttachmentBean;
import com.xj.cnooc.view.R;
import com.xj.cnooc.view.bbs.DownloadDetailsActivity;
/**
 * 知识库视频、资料详情界面
 * @author qinfan
 *
 * 2015-10-12
 */
public class VideoKnowledgeBaseDetailActivity extends BaseActivity implements OnClickListener{
	private TextView video_knowledge_base_tv_originator;
	private TextView video_knowledge_base_tv_faultClassify;
	private TextView video_knowledge_base_btn_attachment_name;
	private TextView video_knowledge_base_tv_date;
//	private Button btn_back;
//	private Button btn_setting;
//	private RelativeLayout rellayout_back;
//	private RelativeLayout rellayout_setting;
	private ImageView iv_back;
	private ImageView iv_setting;
	private TextView tv_center_title;
	private Button video_knowledge_base_btn_download_attachment;
	private String path;
	private List<AttachmentBean> attachmentBeans=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_knowledge_base_detail);
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
		
		video_knowledge_base_tv_originator = (TextView)findViewById(R.id.video_knowledge_base_tv_originator);
		video_knowledge_base_tv_faultClassify = (TextView)findViewById(R.id.video_knowledge_base_tv_faultClassify);
		video_knowledge_base_btn_attachment_name = (TextView)findViewById(R.id.video_knowledge_base_btn_attachment_name);
		video_knowledge_base_tv_date = (TextView)findViewById(R.id.video_knowledge_base_tv_date);
		
		Bundle bundle=getIntent().getExtras();
		video_knowledge_base_tv_originator.setText("标题："+bundle.getString("videoTitle"));
		video_knowledge_base_tv_faultClassify.setText("上传者："+bundle.getString("createName"));
		video_knowledge_base_btn_attachment_name.setText(bundle.getString("attachmentName"));
		video_knowledge_base_tv_date.setText("发布时间："+bundle.getString("createTime"));
		path=bundle.getString("attachmentPath");
		video_knowledge_base_btn_download_attachment = (Button)findViewById(R.id.video_knowledge_base_btn_download_attachment);
		video_knowledge_base_btn_download_attachment.setOnClickListener(this);
		
		attachmentBeans=new ArrayList<AttachmentBean>();
		AttachmentBean attachmentBean=new AttachmentBean();
		attachmentBean.setPath(bundle.getString("attachmentPath"));
		attachmentBean.setName(bundle.getString("attachmentName"));
		attachmentBean.setParentId(bundle.getString("parentId"));
		attachmentBean.setId(bundle.getString("id"));
		attachmentBean.setType("2");
		attachmentBeans.add(attachmentBean);
		
		if(bundle.getString("type").equals("1")){
			tv_center_title.setText("视频详情");
		}
		if(bundle.getString("type").equals("2")){
			tv_center_title.setText("资料详情");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.video_knowledge_base_detail, menu);
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
			
		case R.id.video_knowledge_base_btn_download_attachment:
			intent = new Intent(VideoKnowledgeBaseDetailActivity.this, DownloadDetailsActivity.class);
			intent.putExtra("att_list", (Serializable)attachmentBeans);
			startActivity(intent);
			break;
			
		case R.id.iv_setting:
			showSettingActivity();
			break;
			
//		case R.id.btn_setting:
//			intent=new Intent();
//			intent.setClass(VideoKnowledgeBaseDetailActivity.this, SettingActivity.class);
//			startActivity(intent);
//			break;

		default:
			break;
		}
	}

}
