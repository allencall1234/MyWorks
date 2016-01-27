package com.xj_pipe.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.xj_pipe.base.BaseActivity;
import com.xj_pipe.utils.MyUtils;

/**
 * 欢迎界面,APP入口
 * @author Administrator
 *
 */
public class WelcomeActivity extends BaseActivity
{
	private boolean isFirstIn = false;//是否第一次进入标志
	
	Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_welcome);
		
		hideTitleBanner();// 隐藏标题栏
		initView();
	}
	
	@Override
	protected void onResume() 
	{
		/**
		 * 设置为竖屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
		{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
		
	}

	private void initView() 
	{
		//获取存入SharedPreferences的值
		SharedPreferences preferences = getSharedPreferences("first_start", MODE_PRIVATE);
		//首次则默认为true
		isFirstIn = preferences.getBoolean("isFirstIn", true);
		
		enterMainActivity();
	}
	
	/**根据是否第一次进入应用判断是否跳入引导界面*/
	private void enterMainActivity()
	{
		new Handler().postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				if (isFirstIn) 
				{
					intent = new Intent(WelcomeActivity.this, GuideActivity.class);
				} 
				else
				{
					intent = new Intent(WelcomeActivity.this, LoginActivity.class);
				}
				
				boolean flag = judgeNetWorkState();
				if (flag)
				{
//					if (MyUtils.isGPSOpen(WelcomeActivity.this) == false)
//					{
//						 // 转到手机设置界面，用户设置GPS
//                        Intent intent = new Intent(
//                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
//					}
//					else
//					{
						WelcomeActivity.this.startActivity(intent);
						WelcomeActivity.this.finish();
//					}
					
					
				}
				
				
			}
		}, 2000);
	}
	
	private boolean judgeNetWorkState()
	{
		boolean isNetWork = MyUtils.isNetworkConnected(this);
		if (isNetWork)
		{
			return true;
		} 
		else 
		{
			LayoutInflater inflater = LayoutInflater.from(this);
			
			final Dialog tipDialog = new Dialog(this, R.style.CommonDialog);
			tipDialog.setCancelable(true);
			tipDialog.setCanceledOnTouchOutside(false);
			View view = inflater.inflate(R.layout.dialog_network, null);
			view.findViewById(R.id.dialog_view_cancel).setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					tipDialog.dismiss();
					finish();
				}
			});
			
			view.findViewById(R.id.dialog_view_setting).setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					 Intent intent;
					 int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                     if(currentapiVersion < 11)
                     {  
                         intent = new Intent();  
                         intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");  
                     }
                     else
                     {  
                         //3.0以后  
                         //intent = new Intent( android.provider.Settings.ACTION_WIRELESS_SETTINGS);  
                         intent = new Intent( android.provider.Settings.ACTION_SETTINGS);  
                     }  
                     startActivity(intent);
					 tipDialog.dismiss();
					 finish();
				}
			});
			
			tipDialog.setContentView(view);
			tipDialog.show();
			
			return false;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		WelcomeActivity.this.startActivity(intent);
		WelcomeActivity.this.finish();
	}

}
