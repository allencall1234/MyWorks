package com.xj.cnooc.view;

import com.xj.cnooc.utils.UserHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;

@SuppressWarnings("unused")
public class WelcomeActivity extends Activity
{
	private SharedPreferences preferences;// 保存app启动信息 
	private Editor editor;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		initView();
	}
	
	protected void initView() 
	{
		UserHelper.getInstance();
		UserHelper.init(WelcomeActivity.this);
		
		setContentView(R.layout.welcome_view);
		
		 preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);  
		 //判断是不是首次登录，  首次登录进入引导界面
		 if (preferences.getBoolean("firststart", true)) 
		 { 
			 editor = preferences.edit();  
			 //将登录标志位设置为false，下次登录时不在显示首次登录界面  
			 editor.putBoolean("firststart", false);  
			 editor.commit();
			 // 开启线程实现界面定时切换
			 new Handler().postDelayed(new Runnable() 
			 {
					
				@Override
				public void run() 
				{
				 
					 Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
					 WelcomeActivity.this.startActivity(intent);
					 WelcomeActivity.this.finish();
					
				}
			 }, 2000);
			 
			 return;
			 
		  }
		 
		  login();// 不是第一次启动由欢迎界面进入登录界面
		 
	}
	
	private void login() 
	{
		 // 开启线程实现界面切换
		 new Handler().postDelayed(new Runnable() 
		 {
			@Override
			public void run() 
			{
				Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
				WelcomeActivity.this.startActivity(intent);
				WelcomeActivity.this.finish();
				
			}
		 }, 2000);
		  
	}


}
