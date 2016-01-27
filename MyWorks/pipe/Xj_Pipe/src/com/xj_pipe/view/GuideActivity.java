package com.xj_pipe.view;

import java.util.ArrayList;
import java.util.List;

import com.xj_pipe.adapter.ViewPagerAdapter;
import com.xj_pipe.base.BaseActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 引导界面
 * @author Administrator
 *
 */
public class GuideActivity extends BaseActivity implements OnClickListener
{
	private ViewPagerAdapter<View> adapter;
	private List<View> viewList;
	private SharedPreferences preferences;// 保存app启动信息
	
	private ViewPager vp_guide;
	private View guide_view1, guide_view2, guide_view3, guide_view4, guide_view5;
	private Button btn_enterLogin;// 进入登录界面
	private TextView tv_jump;// 跳过引导界面

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_guide);
		
		hideTitleBanner();// 隐藏标题栏
		initView();
	}
	
	/**
	 * 初始化界面UI
	 */
	private void initView() 
	{
		vp_guide = (ViewPager) findViewById(R.id.vp_guide);
		
		//动态加载三个引导View
		LayoutInflater li = getLayoutInflater();
		guide_view1 = li.inflate(R.layout.guide_view1, null);
		guide_view2 = li.inflate(R.layout.guide_view2, null);
		guide_view3 = li.inflate(R.layout.guide_view3, null);
		guide_view4 = li.inflate(R.layout.guide_view4, null);
		guide_view5 = li.inflate(R.layout.guide_view5, null);
		
//		tv_jump = (TextView) guide_view1.findViewById(R.id.tv_jump_guide);
		btn_enterLogin = (Button) guide_view5.findViewById(R.id.btn_enterLogin);
		
		adapter = new ViewPagerAdapter<View>(getImageViewLists());
		vp_guide.setAdapter(adapter);
		
//		tv_jump.setOnClickListener(this);
		btn_enterLogin.setOnClickListener(this);
		
	}
	
	private List<View> getImageViewLists()
	{
		viewList = new ArrayList<View>();
		viewList.add(guide_view1);
		viewList.add(guide_view2);
		viewList.add(guide_view3);
		viewList.add(guide_view4);
		viewList.add(guide_view5);

		return viewList;
	}

	@Override
	public void onClick(View v)
	{
		//退出引导界面之前，改变isFirstIn的值，下次在欢迎界面的时候通过读取此值进行判断
		preferences = getSharedPreferences("first_start", MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirstIn", false);
		editor.commit();
		
		Intent intent = new Intent();
		intent.setClass(GuideActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onMoreLisenter() {
		// TODO Auto-generated method stub
	}
}
