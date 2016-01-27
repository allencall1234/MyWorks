package com.xj.dms.view.schedule;

import org.json.JSONException;
import org.json.JSONObject;

import com.xj.dms.common.BaseActivity;
import com.xj.dms.common.MyApp;
import com.xj.dms.common.SlideShowView;
import com.xj.dms.https.HttpDataCallBack;
import com.xj.dms.https.UserHttp;
import com.xj.dms.view.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScheduleManagementActivity extends BaseActivity implements OnClickListener
{
	private TextView tv_task_query;// 任务查询
	private TextView tv_task_approval;// 任务审批
	private TextView tv_task_statistical;// 任务统计
	
	private RelativeLayout layout_count;
	private TextView tv_num;// 未处理审批数目
	
	private ImageView iv_back;// 返回按钮
	
	public static int totalNum;// 未审批任务数量
	
	private static final int TAG = 0x01;
	
	public static SlideShowView slideShowView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_management);
		
		initView();
	}
	
	private void initView() 
	{
		tv_task_query = (TextView) findViewById(R.id.tv_task_query);
		tv_task_approval = (TextView) findViewById(R.id.tv_task_approval);
		tv_task_statistical = (TextView) findViewById(R.id.tv_task_statistical);
		
		layout_count = (RelativeLayout) findViewById(R.id.layout_count);
		tv_num = (TextView) findViewById(R.id.tv_num);
		
		iv_back = getbtn_back();
		
		handler.sendEmptyMessage(0);
		
		tv_task_query.setOnClickListener(this);
		tv_task_approval.setOnClickListener(this);
		tv_task_statistical.setOnClickListener(this);
		
		iv_back.setOnClickListener(this);
		
		slideShowView = (SlideShowView) findViewById(R.id.slideshowView);
		
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
			case R.id.tv_task_query:// 任务查询
				startActivity(new Intent(this, ScheduleActivity.class));
				break;
			case R.id.tv_task_approval:// 任务审批
				Intent intent = new Intent(this, ScheduleApprovalActivity.class);
				startActivityForResult(intent, TAG);
				break;
			case R.id.tv_task_statistical:// 任务统计
				startActivity(new Intent(this, ScheduleGraphActivity.class));
				break;
			case R.id.iv_back:
				finish();
				break;
			default:
				break;
		}
	}
	
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what)
			{
				case 0:
					UserHttp.unApprovalTaskQuery(0, 0, "", MyApp.userBean.getUserId(), new HttpDataCallBack() 
					{
						@Override
						public void HttpSuccess(String _result)
						{
							try 
							{
								JSONObject object = new JSONObject(_result);
								totalNum = object.getInt("total");
								if (totalNum > 0) 
								{
									layout_count.setVisibility(View.VISIBLE);
									tv_num.setText(String.valueOf(totalNum));
								}
							}
							catch (JSONException e)
							{
								e.printStackTrace();
							}
						}
						
						@Override
						public void HttpFail(int ErrCode) 
						{
							showToast("网络请求失败，请您检查网络!");
						}
					});
					break;
	
				default:
					break;
			}
		};
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (requestCode == TAG) 
		{
			if (resultCode == 0x02 && totalNum > 0) 
			{
				layout_count.setVisibility(View.VISIBLE);
				tv_num.setText(String.valueOf(totalNum));
			}
		}
	};
	
}
