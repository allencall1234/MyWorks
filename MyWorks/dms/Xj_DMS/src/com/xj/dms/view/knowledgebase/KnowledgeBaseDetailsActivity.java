package com.xj.dms.view.knowledgebase;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.xj.dms.common.BaseActivity;
import com.xj.dms.model.FaultTraceBean;
import com.xj.dms.model.KnowledgeBaseBean;
import com.xj.dms.view.R;

public class KnowledgeBaseDetailsActivity extends BaseActivity implements OnClickListener
{

	private TextView tv_title;// 标题
	private TextView tv_keywords;// 关键词
	private TextView tv_type;// 类型
	private TextView tv_describtion;// 内容描述
	private TextView tv_attachmentNname;// 附件名称
//	private Button btn_download;// 下载
	
	private KnowledgeBaseBean knowledgeBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_knowledgebase_details);
		
		initView();
	}

	private void initView()
	{
		setCenterTitle("电网知识库明细");
		
		knowledgeBean = (KnowledgeBaseBean) getIntent().getSerializableExtra("KnowledgeBaseBean");
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_keywords = (TextView) findViewById(R.id.tv_keywords);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_describtion = (TextView) findViewById(R.id.tv_describtion);
		tv_attachmentNname = (TextView) findViewById(R.id.tv_attachmentNname);
//		btn_download = (Button) findViewById(R.id.btn_download);
		
		tv_title.setText("标    题 : " + knowledgeBean.getTitle());
		tv_keywords.setText("关键词 : " + knowledgeBean.getKeyword());
		tv_type.setText("类    型 : " + knowledgeBean.getType());
		tv_describtion.setText(" " + knowledgeBean.getContext());
		tv_attachmentNname.setText(knowledgeBean.getFilename());
		
		getbtn_back().setOnClickListener(this);
//		btn_download.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
			case R.id.iv_back:
				finish();
				break;
				
//			case R.id.btn_download:
//				break;
	
			default:
				break;
		}
	}
	
	

}
