package com.xj.cnooc.view;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xj.cnooc.adapter.GridViewAdapter;
import com.xj.cnooc.common.BaseActivity;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.https.HttpURL;
import com.xj.cnooc.model.ColumnImageBean;
import com.xj.cnooc.model.UserBean;
import com.xj.cnooc.view.bbs.BBSFragmentActivity;
import com.xj.cnooc.view.chat.ChatHallActivity;
import com.xj.cnooc.view.support.FaultEvaluateActivity;
import com.xj.cnooc.view.support.FaultSupportActivity;
import com.xj.cnooc.view.support.KnowledgeBaseActivity;

public class MainGridViewActivity extends BaseActivity implements OnItemClickListener
{
	private ImageView iv_user_head;// 用户头像
	
	private TextView tv_username;// 用户名
	private TextView tv_user_roles;// 用户角色
	private TextView tv_account;// 用户账号
	private TextView tv_department;// 所属单位
	private TextView tv_last_login_time;// 上次登录时间
	
	private GridView gridview;
	private GridViewAdapter adapter;
	
	private UserBean bean;
	
	
	private List<ColumnImageBean> grid_list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		
		initView();
	}
	
	private void initView() 
	{
		getGirdViewData();
		
		iv_user_head = (ImageView) findViewById(R.id.iv_user_head);
		
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_user_roles = (TextView) findViewById(R.id.tv_user_roles);
		tv_account = (TextView) findViewById(R.id.tv_account);
		tv_department = (TextView) findViewById(R.id.tv_department);
		tv_last_login_time = (TextView) findViewById(R.id.tv_last_login_time);
		
		gridview = (GridView) findViewById(R.id.gridview);
		
		bean = MyApp.globelUser;
		tv_username.setText(bean.getName());
		tv_user_roles.setText(bean.getRolename());
		tv_account.setText("账号：" + bean.getLoginName());
		tv_department.setText("单位：" + bean.getVendername());
		tv_last_login_time.setText("上次登录：" + bean.getBeforedate());
		
//		handler2.postDelayed(runnable, 1000 * 60);
		
		// 设置头像
//		ImageLoader.getInstance().displayImage(HttpURL.SERVICE_URL + bean.getPhoto(), iv_user_head, ImageLoadOptions.getOptions());
		ImageLoader.getInstance().loadImage(HttpURL.SERVICE_URL + bean.getPhoto(), new ImageLoadingListener() 
		{
			@Override
			public void onLoadingStarted(String imageUri, View view)
			{
				iv_user_head.setImageResource(R.drawable.login_head_img);
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) 
			{
				iv_user_head.setImageResource(R.drawable.login_head_img);
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) 
			{
				iv_user_head.setImageBitmap(loadedImage);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) 
			{
				
			}
		});
		
		// 为gridview添加适配器
		adapter = new GridViewAdapter(MainGridViewActivity.this, grid_list);
		gridview.setAdapter(adapter);
		
		// gridview的item点击监听
		gridview.setOnItemClickListener(this);
		
	}
	
	private void getGirdViewData()
	{
        grid_list = new ArrayList<ColumnImageBean>();
		
		grid_list.add(new ColumnImageBean(" 故障支持", R.drawable.failure_support_img));
		grid_list.add(new ColumnImageBean(" 评估支持", R.drawable.assessment_support_img));
		grid_list.add(new ColumnImageBean("电网知识库", R.drawable.knowledge_base_img));
		grid_list.add(new ColumnImageBean(" 即时交流", R.drawable.timely_communication_img));
		grid_list.add(new ColumnImageBean(" 论坛交流", R.drawable.bbs_communication_img));
		grid_list.add(new ColumnImageBean("  设置", R.drawable.setting_img));
	}
	
	private void resume() 
	{
		grid_list.get(0).setIndex(MyApp.hitchtotal);
	    grid_list.get(1).setIndex(MyApp.asstotal);
	    grid_list.get(2).setIndex(MyApp.recordtotal);
	    grid_list.get(4).setIndex(MyApp.bbsTotal);
	    adapter.notifyDataSetChanged();
       
	}
	
//	// 广播
//	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) 
//		{
//			String action = intent.getAction();
//			if (action.equals("MainGridViewActivityUpdate1"))
//			{
//			   int position = intent.getIntExtra("position", 0);
//			   int bbsTotal = intent.getIntExtra("bbsTotal", LoginActivity.bbsTotal); 
//			}
//		}
//	};

//	// 注册广播
//	public void registerBoradcastReceiver() 
//	{
//		IntentFilter myIntentFilter = new IntentFilter();
//		myIntentFilter.addAction("MainGridViewActivityUpdate");
//		// 注册广播
//		registerReceiver(mBroadcastReceiver, myIntentFilter);
//	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		Intent intent = new Intent();
		switch (position)
		{
			case 0:
				intent.setClass(MainGridViewActivity.this,
						FaultSupportActivity.class);
				startActivity(intent);
				break;
			case 1:
				intent.setClass(MainGridViewActivity.this,
						FaultEvaluateActivity.class);
				startActivity(intent);
				break;
			case 2:
				intent.setClass(MainGridViewActivity.this,
						KnowledgeBaseActivity.class);
				startActivity(intent);
				break;
			case 3:
				startActivity(new Intent(MainGridViewActivity.this,
						ChatHallActivity.class));
				break;
			case 4:
				startActivity(new Intent(MainGridViewActivity.this, BBSFragmentActivity.class));
				break;
			case 5:
				showSettingActivity();
				break;
	
			default:
				break;
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		/*
		 * 在主界面屏蔽掉辅助触摸按钮
		 */
		RunningTaskInfo taskInfo = getRunningTaskInfo(this);
		String clsBaseActivityName = taskInfo.baseActivity.getClassName();
		
		resume();// 界面刷新 

		if (clsBaseActivityName.equals("com.xj.cnooc.view.LoginActivity")
				|| clsBaseActivityName
						.equals("com.xj.cnooc.view.MainGridViewActivity")) {
			mTouchView.removeView();
		}
		
	}

	/**
	 * 获取当前活动任务
	 * 
	 * @param mContext
	 * @return
	 */
	public static RunningTaskInfo getRunningTaskInfo(Context mContext) {
		ActivityManager manager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskinfos = manager.getRunningTasks(1);

		if (runningTaskinfos != null) {
			return runningTaskinfos.get(0);
		} else {
			return null;
		}
    }
}
