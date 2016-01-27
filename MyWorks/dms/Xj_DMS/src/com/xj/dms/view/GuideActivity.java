package com.xj.dms.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.xj.dms.adapter.ViewPagerAdapter;
import com.xj.dms.common.BaseActivity;
/**
 * 引导界面
 * @author qinfan
 *
 * 2015-11-6
 */
@SuppressLint("InflateParams")
public class GuideActivity extends BaseActivity implements OnClickListener{
	private ViewPager vp_guide;
	private View guide_1,guide_2,guide_3;
	private ViewPagerAdapter adapter;
	private List<View> listItems;
	private Button btn_enter;
	private TextView tv_jump;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_guide);
		hideTitleView();
		initView();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initView(){
		vp_guide = (ViewPager) findViewById(R.id.vp_guide);

		//动态加载三个引导View
		LayoutInflater li = getLayoutInflater();
		guide_1 = li.inflate(R.layout.guide_1, null);
		guide_2 = li.inflate(R.layout.guide_2, null);
		guide_3 = li.inflate(R.layout.guide_3, null);
		
		tv_jump = (TextView) guide_1.findViewById(R.id.tv_jump_guide);
		btn_enter = (Button) guide_3.findViewById(R.id.btn_enter);

		adapter=new ViewPagerAdapter(getImageViewLists());
		//set Adapter for ViewPager
		vp_guide.setAdapter(adapter);
		
		tv_jump.setOnClickListener(this);
		btn_enter.setOnClickListener(this);
	}

	private List<View> getImageViewLists(){
		listItems=new ArrayList<View>();
		listItems.add(guide_1);
		listItems.add(guide_2);
		listItems.add(guide_3);

		return listItems;
	}

	@Override
	public void onClick(View v) {
		//退出引导界面之前，改变isFirstIn的值，下次在欢迎界面的时候通过读取此值进行判断
		SharedPreferences preferences = 
				getSharedPreferences(
						"first_pref", MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirstIn", false);
		editor.commit();
		
		enterMainActivity();
	}
	
	//进入主界面
	private void enterMainActivity(){
		Intent intent = new Intent();
		intent.setClass(GuideActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
