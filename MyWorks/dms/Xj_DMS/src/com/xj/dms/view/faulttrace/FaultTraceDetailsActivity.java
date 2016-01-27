package com.xj.dms.view.faulttrace;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xj.dms.common.BaseActivity;
import com.xj.dms.model.FaultTraceBean;
import com.xj.dms.view.R;

public class FaultTraceDetailsActivity extends BaseActivity implements OnClickListener
{
	private TextView tv_dutyPersonnel;// 当班人员
	private TextView tv_schedulePersonnel;// 调度人员
	private TextView tv_sign;// 标签
	private TextView tv_happenTime;// 发生时间 
	private TextView tv_faultName;// 故障名称
	private TextView tv_happenCauses;// 发生原因
	private TextView tv_happenLocation;// 发生位置
	private TextView tv_faultIndex;// 故障索引
	private TextView tv_solveTime;// 解决时间
	private TextView tv_dealwithMeasures;// 处理措施
	
	private FaultTraceBean faultBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_faulttrace_details);
		
		initView();
	}

	private void initView()
	{
		setCenterTitle("shutdown报告详情");
		
		faultBean = (FaultTraceBean) getIntent().getSerializableExtra("FaultTraceBean");
		
		tv_dutyPersonnel = (TextView) findViewById(R.id.tv_dutyPersonnel);
		tv_schedulePersonnel = (TextView) findViewById(R.id.tv_schedulePersonnel);
		tv_sign = (TextView) findViewById(R.id.tv_sign);
		tv_happenTime = (TextView) findViewById(R.id.tv_happenTime);
		tv_faultName = (TextView) findViewById(R.id.tv_faultName);
		tv_happenCauses = (TextView) findViewById(R.id.tv_happenCauses);
		tv_happenLocation = (TextView) findViewById(R.id.tv_happenLocation);
		tv_faultIndex = (TextView) findViewById(R.id.tv_faultIndex);
		tv_solveTime = (TextView) findViewById(R.id.tv_solveTime);
		tv_dealwithMeasures = (TextView) findViewById(R.id.tv_dealwithMeasures);
		
		tv_dutyPersonnel.setText("当班人员 : " + faultBean.getDutyMan());
		tv_schedulePersonnel.setText("调度人员 : " + faultBean.getDispatchMan());
		tv_sign.setText(faultBean.getTagname());
		tv_happenTime.setText("发生时间 : " + faultBean.getHappenTime());
		tv_faultName.setText(faultBean.getDiscription());
		tv_happenCauses.setText(faultBean.getReason());
		tv_happenLocation.setText(faultBean.getPlace());
		tv_faultIndex.setText(faultBean.getBreakdownIndex());
		tv_solveTime.setText("解决时间 : " + faultBean.getSolveTime());
		tv_dealwithMeasures.setText(faultBean.getMeasures());
		
		getbtn_back().setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
			case R.id.iv_back:
				finish();
				break;
	
			default:
				break;
		}
	}
	
}
