package com.xj.dms.view;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.xj.dms.common.BaseActivity;
import com.xj.dms.model.NoticePlanBean;
import com.xj.dms.model.PlanBean;
/**
 * 通知、计划详情
 * @author qinfan
 *
 * 2015-11-6
 */
public class NoticePlanDetailActivity extends BaseActivity implements OnClickListener{
	private Button btn_before;
	private Button btn_next;
	private TextView notice_title;
	private TextView notice_content;
	private TextView notice_time;
	private ArrayList<NoticePlanBean> noticePlanBeans;
	private NoticePlanBean noticePlanBean;
	private ArrayList<PlanBean> planBeans;
	private PlanBean planBean;
	private int position;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_notice_plan_detail);
		init();
	}

	private void init(){
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		position = intent.getIntExtra("position", 0);

		btn_before = (Button) findViewById(R.id.before);
		btn_next = (Button) findViewById(R.id.after);
		notice_title = (TextView) findViewById(R.id.notice_title);
		notice_content = (TextView) findViewById(R.id.notice_content);
		notice_time = (TextView) findViewById(R.id.notice_time);

		if(type.equals("notice")){
			setCenterTitle("公告详情");
			noticePlanBeans = intent.getParcelableArrayListExtra("noticePlanBeans");
			noticePlanBean = noticePlanBeans.get(position);
			setcontent();
		}else{
			setCenterTitle("计划详情");
			planBeans = intent.getParcelableArrayListExtra("planBeans");
			planBean = planBeans.get(position);
			notice_title.setVisibility(View.GONE);
			setPlanContent();
		}

		btn_before.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		getbtn_back().setOnClickListener(this);  
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.before:
			position-=1;
			if(position >= 0){
				if(type.equals("notice")){
					noticePlanBean = noticePlanBeans.get(position);
					setcontent();
				}
				if(type.equals("plan")){
					planBean = planBeans.get(position);
					setPlanContent();
				}
			}else{
				showToast("当前是第一条！");
				position = 0;
			}
			break;

		case R.id.after:
			position+=1;
			if(type.equals("notice")){
				if(position <= noticePlanBeans.size()-1){
					noticePlanBean = noticePlanBeans.get(position);
					setcontent();
				}else{
					position = noticePlanBeans.size()-1;
				}
			}
			if(type.equals("plan")){
				if(position <= planBeans.size()-1){
					planBean = planBeans.get(position);
					setPlanContent();
				}else{
					position = planBeans.size()-1;
				}
			}

			break;

		case R.id.iv_back:
			finish();
			break;
		}
	}

	private void setcontent(){
		notice_time.setText(noticePlanBean.getDateTime());
		notice_content.setText(noticePlanBean.getFollowing());
		if(type.equals("notice")){
			notice_title.setText(noticePlanBean.getServiceName());
		}
	}
	
	private void setPlanContent(){
		notice_time.setText(planBean.getCreateDate());
		notice_content.setText(planBean.getContent());
	}
}
