package com.xj.dms.view.schedule;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.xj.dms.common.BaseActivity;
import com.xj.dms.common.BaseDialog.OnAgreeListener;
import com.xj.dms.common.BaseDialog.OnRejectedListener;
import com.xj.dms.common.MyApp;
import com.xj.dms.common.SelectionDialog;
import com.xj.dms.https.HttpDataCallBack;
import com.xj.dms.https.HttpJsonUtils;
import com.xj.dms.https.UserHttp;
import com.xj.dms.model.TaskBean;
import com.xj.dms.view.MainMenuActivity;
import com.xj.dms.view.R;

/**
 * 任务查询显示详情界面
 *
 */
public class ScheduleDetailsActivity extends BaseActivity implements OnClickListener
{
	private int title_type = -1;
	
	private TextView tv_taskName;// 任务名称
	private TextView tv_taskNumber;// 任务编号
	private TextView tv_taskType;// 任务类型
	private TextView tv_belongto;// 所属装置
	
	private TextView tv_applicant;// 申请人
	private TextView tv_applicationDate;// 申请日期
	private TextView tv_developStaff;// 制定人员
	private TextView tv_setDate;// 制定日期
	private TextView tv_planStart;// 计划开始时间
	private TextView tv_planEnd;// 计划完成时间
	
	private TextView tv_taskDescription;// 任务描述
	private TextView tv_attachmentNname;// 附件名称
//	private Button btn_download;// 下载
	
	private TextView tv_developer;// 制定人
	private TextView tv_developer_opinion;// 制定人意见
	
	private TextView tv_weixiujiandu;// 维修监督
	private TextView tv_weixiujiandu_opinion;// 维修监督意见
	
	private TextView tv_dianlijiandu;// 电力监督
	private TextView tv_dianlijiandu_opinion;// 电力监督意见
	
	private TextView tv_ludilingdao;// 陆地监督
	private TextView tv_ludilingdao_opinion;// 陆地监督审批意见
	
	private List<TaskBean> taskDetailsList;
	private Button btn_approval;// 审批按钮
	
	private int taskId = -1;
	
	private TaskBean bean;
	
	private String approvalOpinion = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.public_schedule_details);
		
		initView();
	}

	private void initView() 
	{
		title_type = getIntent().getIntExtra("title_type", 0);
		taskId = getIntent().getIntExtra("id", 0);
		
		setCenterTitle("调度任务信息详情");
		
		tv_taskName = (TextView) findViewById(R.id.tv_taskName);
		tv_taskNumber = (TextView) findViewById(R.id.tv_taskNumber);
		tv_taskType = (TextView) findViewById(R.id.tv_taskType);
		tv_belongto = (TextView) findViewById(R.id.tv_belongto);
		
		tv_applicant = (TextView) findViewById(R.id.tv_applicant);
		tv_applicationDate = (TextView) findViewById(R.id.tv_applicationDate);
		tv_developStaff = (TextView) findViewById(R.id.tv_developStaff);
		tv_setDate = (TextView) findViewById(R.id.tv_setDate);
		tv_planStart = (TextView) findViewById(R.id.tv_planStart);
		tv_planEnd = (TextView) findViewById(R.id.tv_planEnd);
		
		tv_taskDescription = (TextView) findViewById(R.id.tv_taskDescription);
		
		tv_attachmentNname = (TextView) findViewById(R.id.tv_attachmentNname);
//		btn_download = (Button) findViewById(R.id.btn_download);
		
		tv_developer = (TextView) findViewById(R.id.tv_developer);
		tv_developer_opinion = (TextView) findViewById(R.id.tv_developer_opinion);
		
		tv_weixiujiandu = (TextView) findViewById(R.id.tv_weixiujiandu);
		tv_weixiujiandu_opinion = (TextView) findViewById(R.id.tv_weixiujiandu_opinion);
		
		tv_dianlijiandu = (TextView) findViewById(R.id.tv_dianlijiandu);
		tv_dianlijiandu_opinion = (TextView) findViewById(R.id.tv_dianlijiandu_opinion);
		
		tv_ludilingdao = (TextView) findViewById(R.id.tv_ludijiandu);
		tv_ludilingdao_opinion = (TextView) findViewById(R.id.tv_ludijiandu_opinion);
		btn_approval = (Button) findViewById(R.id.btn_approval);
		
		if (MyApp.userBean.getRole().equals("维修监督")) 
		{
			if (title_type == 1) // 任务查询
			{
				btn_approval.setVisibility(View.GONE);
			}
			else if (title_type == 2) // 任务审批
			{
				btn_approval.setVisibility(View.GONE);
				tv_dianlijiandu.setVisibility(View.GONE);
				tv_dianlijiandu_opinion.setVisibility(View.GONE);
				tv_ludilingdao.setVisibility(View.GONE);
				tv_ludilingdao_opinion.setVisibility(View.GONE);
			}
			else if (title_type == 3) // 任务待审
			{
				tv_weixiujiandu.setVisibility(View.GONE);
				tv_weixiujiandu_opinion.setVisibility(View.GONE);
				tv_dianlijiandu.setVisibility(View.GONE);
				tv_dianlijiandu_opinion.setVisibility(View.GONE);
				tv_ludilingdao.setVisibility(View.GONE);
				tv_ludilingdao_opinion.setVisibility(View.GONE);
			}
		}
		else if (MyApp.userBean.getRole().equals("电力监督")) 
		{
			if (title_type == 1) // 任务查询
			{
				btn_approval.setVisibility(View.GONE);
			}
			else if (title_type == 2) // 任务审批
			{
				btn_approval.setVisibility(View.GONE);
				tv_ludilingdao.setVisibility(View.GONE);
				tv_ludilingdao_opinion.setVisibility(View.GONE);
			}
			else if (title_type == 3) // 任务待审
			{
				tv_dianlijiandu.setVisibility(View.GONE);
				tv_dianlijiandu_opinion.setVisibility(View.GONE);
				tv_ludilingdao.setVisibility(View.GONE);
				tv_ludilingdao_opinion.setVisibility(View.GONE);
			}
		}
		else if (MyApp.userBean.getRole().equals("陆地领导"))
		{
			if (title_type == 1) // 任务查询
			{
				btn_approval.setVisibility(View.GONE);
			}
			else if (title_type == 2) // 任务审批
			{
				btn_approval.setVisibility(View.GONE);
			}
			else if (title_type == 3) // 任务待审
			{
				tv_ludilingdao.setVisibility(View.GONE);
				tv_ludilingdao_opinion.setVisibility(View.GONE);
			}
		}
		else
		{
			btn_approval.setVisibility(View.GONE);
		}
		
		handler.sendEmptyMessage(0);
		
		getbtn_back().setOnClickListener(this);
//		btn_download.setOnClickListener(this);
		btn_approval.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
			case R.id.iv_back:
				finish();
				break;
				
//			case R.id.btn_download:// 下载
//				
//				break;
				
			case R.id.btn_approval:// 审批
				approval();
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
					UserHttp.taskDetailsQuery(taskId, new HttpDataCallBack()
					{
						@Override
						public void HttpSuccess(String _result) 
						{
							bean = getTaskDetailsData(_result);
							
							tv_taskName.setText("  任务名称 : " + bean.getTname());
							tv_taskNumber.setText("  任务编号 : " + bean.getTnum());
							tv_taskType.setText("  任务类型 : " + bean.getTtype());
							tv_belongto.setText("  所属装置 : " + bean.getPname());
							
							tv_applicant.setText("  申请人 : " + bean.getTpropose());
							tv_applicationDate.setText("  申请日期 : " + bean.getTapplydate());
							tv_developStaff.setText("  制定人员 : " + bean.getTframer());
							tv_setDate.setText("  制定日期 : " + bean.getTestdate());
							tv_planStart.setText("  计划开始 : " + bean.getPlanstarttime());
							tv_planEnd.setText("  计划完成 : " + bean.getPlanendtime());
							
							tv_taskDescription.setText("  " + bean.getTdescribe());
							tv_attachmentNname.setText("  " + bean.getFilename());
							
							tv_developer_opinion.setText("  " + bean.getTframerstr());
							tv_weixiujiandu_opinion.setText("  " + bean.getWxexapatt());
							tv_dianlijiandu_opinion.setText("  " + bean.getDlexapatt());
							tv_ludilingdao_opinion.setText("  " + bean.getLdexapatt());
							
							
						}
						
						@Override
						public void HttpFail(int ErrCode) 
						{
							showToast("网络请求失败，请检查您的网络!");
						}
						
					});
					break;
				case 1:// 同意
					UserHttp.taskApproval(taskId, approvalOpinion, "1", MyApp.userBean.getUserId(), new HttpDataCallBack()
					{
						@Override
						public void HttpSuccess(String _result)
						{
							boolean sign = isSuccess(_result);
							if (sign == true)
							{
								if (MyApp.userBean.getRole().equals("维修监督"))
								{
									tv_weixiujiandu.setVisibility(View.VISIBLE);
									tv_weixiujiandu_opinion.setVisibility(View.VISIBLE);
									tv_weixiujiandu_opinion.setText(approvalOpinion);
								}
								else if (MyApp.userBean.getRole().equals("电力监督")) 
								{
									tv_dianlijiandu.setVisibility(View.VISIBLE);
									tv_dianlijiandu_opinion.setVisibility(View.VISIBLE);
									tv_dianlijiandu_opinion.setText(approvalOpinion);
								}
								else if (MyApp.userBean.getRole().equals("陆地领导"))
								{
									tv_ludilingdao.setVisibility(View.VISIBLE);
									tv_ludilingdao_opinion.setVisibility(View.VISIBLE);
									tv_ludilingdao_opinion.setText(approvalOpinion);
								}
								btn_approval.setVisibility(View.GONE);
							}
							
							ScheduleManagementActivity.totalNum -= 1;
							
							setResult(0x02);
							Intent intent = new Intent(ScheduleDetailsActivity.this, MainMenuActivity.class);
							startActivity(intent);
							finish();
							
						}
						
						@Override
						public void HttpFail(int ErrCode)
						{
							showToast("网络请求失败，请检查您的网络!");
						}
					});
					
					break;
				case 2:// 驳回
					UserHttp.taskApproval(taskId, approvalOpinion, "2", MyApp.userBean.getUserId(), new HttpDataCallBack() 
					{
						@Override
						public void HttpSuccess(String _result) 
						{
							boolean sign = isSuccess(_result);
							if (sign == true)
							{
								btn_approval.setVisibility(View.GONE);
								tv_ludilingdao.setVisibility(View.VISIBLE);
								tv_ludilingdao_opinion.setVisibility(View.VISIBLE);
							}
							
							ScheduleManagementActivity.totalNum -= 1;
							
							setResult(0x02);
							Intent intent = new Intent(ScheduleDetailsActivity.this, MainMenuActivity.class);
							startActivity(intent);
							finish();
						}
						
						@Override
						public void HttpFail(int ErrCode)
						{
							showToast("网络请求失败，请检查您的网络!");
						}
					});
					break;
	
				default:
					break;
			}
		};
	};
	
	/**
	 * 审批弹出框
	 */
	private void approval() 
	{
		SelectionDialog dialog = new SelectionDialog(this);
		
		dialog.setOnRejectedListener(new OnRejectedListener()
		{
			@Override
			public void OnRejected(String result) 
			{
				showToast("您选择的是驳回");
				SelectionDialog dialog = new SelectionDialog(ScheduleDetailsActivity.this, "确认驳回", true);
				dialog.setOnRejectedListener(new OnRejectedListener() 
				{
					@Override
					public void OnRejected(String result) 
					{
						showToast("您选择的是确认驳回");
						approvalOpinion = result;
						tv_ludilingdao_opinion.setText("  " + result);
						handler.sendEmptyMessage(2);
					}
				});
				
				dialog.show();
			}
		});
		
	    // 同意监听
		dialog.setOnAgreeListener(new OnAgreeListener()
		{
			@Override
			public void OnAgree(String result) 
			{
				showToast("您选择的是同意");
				SelectionDialog dialog = new SelectionDialog(ScheduleDetailsActivity.this, 1);
			
				// 确定监听
				dialog.setOnAgreeListener(new OnAgreeListener() 
				{
					@Override
					public void OnAgree(String result) 
					{
						approvalOpinion = MyApp.userBean.getRole() + "审批通过";
						handler.sendEmptyMessage(1);
					}
				});
				
				// 取消监听
				dialog.setOnCancelListener(new OnCancelListener() 
				{
					@Override
					public void onCancel(DialogInterface dialog) 
					{
						dialog.dismiss();
					}
				});
				
				dialog.show();
			}
		});
		dialog.show();
	}
	
	/**
	 * 获取任务详情
	 * @param result
	 * @return
	 */
	private TaskBean getTaskDetailsData(String result)
	{
		try 
		{
			JSONObject object = new JSONObject(result);
			JSONObject object2 = object.getJSONObject("task");
			bean = (TaskBean) HttpJsonUtils.putJsonToObject(object2, "com.xj.dms.model.TaskBean");
		} 
		
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return bean;
		
	}
	
	/**
	 * 判断审批是否操作成功
	 * @param result
	 * @return
	 */
	private boolean isSuccess (String result)
	{
		boolean flag = false;
		try 
		{
			JSONObject object = new JSONObject(result);
			if (object.getBoolean("status") == true) 
			{
				showToast("审批操作成功!");
				flag = true;
			}
			else
			{
				showToast("审批操作失败!");
				flag = false;
			}
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return flag;
	}

}
