package com.xj.dms.view;

import java.io.IOException;
import java.util.ArrayList;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.dms.adapter.ViewPagerAdapter;
import com.xj.dms.common.BaseActivity;
import com.xj.dms.common.MyApp;
import com.xj.dms.common.MyPagerOnClickListener;
import com.xj.dms.update.UpdateFailedListener;
import com.xj.dms.update.UpdateManager;
import com.xj.dms.view.faulttrace.FaultTraceActivity;
import com.xj.dms.view.knowledgebase.KnowledgeBaseActivity;
import com.xj.dms.view.schedule.ScheduleManagementActivity;

@SuppressWarnings("deprecation")
public class MainMenuActivity extends BaseActivity {
	private static boolean check = false;
	private long mExitTime;
	private Context context;
	private LocalActivityManager manager;
	private ViewPager viewPager;
	private TextView tv_diaodu_management;// 调度管理
	private TextView tv_knowledge_base;// 知识库
	private TextView tv_fault_trace;// 故障追溯

	private ImageView iv_schedule;
	private ImageView iv_knowledgeBase;
	private ImageView iv_faultTrace;

	private ArrayList<View> view_list = new ArrayList<View>();
	private int currIndex = 0;

	private static final String SCHEDULE_MANAGEMENT_ACTIVITY = "ScheduleManagementActivity";// 调度管理
	private static final String KNOWLEDGE_BASE_ACTIVITY = "KnowledgeBaseActivity";// 知识库
	private static final String FAULT_TRACE_ACTIVITY = "FaultTraceActivity";// 故障追溯

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_main_menu);

		context = MainMenuActivity.this;
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);

		initView();
		initTextView();
		initViewPagerAdapter();
	}

	private void initView() {
		getbtn_back().setImageResource(R.drawable.logo_small);

		viewPager = (ViewPager) findViewById(R.id.viewPager);

		tv_diaodu_management = (TextView) findViewById(R.id.tv_diaodu_management);
		tv_knowledge_base = (TextView) findViewById(R.id.tv_knowledge_base);
		tv_fault_trace = (TextView) findViewById(R.id.tv_fault_trace);

		iv_schedule = (ImageView) findViewById(R.id.iv_schedule);
		iv_knowledgeBase = (ImageView) findViewById(R.id.iv_knowledgeBase);
		iv_faultTrace = (ImageView) findViewById(R.id.iv_faultTrace);

	}

	// 头标点击监听
	private void initTextView() {
		iv_schedule
				.setOnClickListener(new MyPagerOnClickListener(0, viewPager));
		iv_knowledgeBase.setOnClickListener(new MyPagerOnClickListener(1,
				viewPager));
		iv_faultTrace.setOnClickListener(new MyPagerOnClickListener(2,
				viewPager));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		try {
			if (!check) {
				check = true;
				new UpdateManager(this).checkUpdate(new UpdateFailedListener() {

					@Override
					public void onFailed() {
//						showToast("no update!");
					}
				});
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		ScheduleManagementActivity.slideShowView.stopPlay();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initViewPagerAdapter() {
		Intent intent = new Intent(context, ScheduleManagementActivity.class);
		view_list.add(getView(SCHEDULE_MANAGEMENT_ACTIVITY, intent));

		Intent intent2 = new Intent(context, KnowledgeBaseActivity.class);
		view_list.add(getView(KNOWLEDGE_BASE_ACTIVITY, intent2));

		Intent intent3 = new Intent(context, FaultTraceActivity.class);
		view_list.add(getView(FAULT_TRACE_ACTIVITY, intent3));

		viewPager.setAdapter(new ViewPagerAdapter(view_list));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 通过activity获取视图
	 */
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int index) {
			@SuppressWarnings("unused")
			Animation animation = null;
			switch (index) {
			case 0:
				iv_schedule.setImageDrawable(getResources().getDrawable(
						R.drawable.schedule_management_pressed));
				changColor(tv_diaodu_management, "调度管理");
				if (currIndex == 1) {
					iv_knowledgeBase.setImageDrawable(getResources()
							.getDrawable(R.drawable.knowledge_base_normal));
				} else if (currIndex == 2) {
					iv_faultTrace.setImageDrawable(getResources().getDrawable(
							R.drawable.fault_trace_normal));
				}
				viewPager.setCurrentItem(0);
				break;

			case 1:
				iv_knowledgeBase.setImageDrawable(getResources().getDrawable(
						R.drawable.knowledge_base_pressed));
				changColor(tv_knowledge_base, "知识库");
				if (currIndex == 0) {
					iv_schedule.setImageDrawable(getResources().getDrawable(
							R.drawable.schedule_management_normal));
				} else if (currIndex == 2) {
					iv_faultTrace.setImageDrawable(getResources().getDrawable(
							R.drawable.fault_trace_normal));
				}
				viewPager.setCurrentItem(1);
				break;

			case 2:
				iv_faultTrace.setImageDrawable(getResources().getDrawable(
						R.drawable.fault_trace_pressed));
				changColor(tv_fault_trace, "故障追溯");
				if (currIndex == 0) {
					iv_schedule.setImageDrawable(getResources().getDrawable(
							R.drawable.schedule_management_normal));
				} else if (currIndex == 1) {
					iv_knowledgeBase.setImageDrawable(getResources()
							.getDrawable(R.drawable.knowledge_base_normal));
				}
				viewPager.setCurrentItem(2);
				break;

			}
			currIndex = index;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

	}

	// 改变字体颜色
	public void changColor(TextView textView, String textTitle) {
		tv_diaodu_management.setTextColor(Color.parseColor("#63625E"));
		tv_knowledge_base.setTextColor(Color.parseColor("#63625E"));
		tv_fault_trace.setTextColor(Color.parseColor("#63625E"));
		textView.setTextColor(Color.parseColor("#013f80"));
		setCenterTitle(textTitle);
	}

	
	@Override
    public boolean dispatchKeyEvent(KeyEvent event)
	{
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) 
            {                        
            	if ((System.currentTimeMillis() - mExitTime) > 2000) 
            	{
     				Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
     				mExitTime = System.currentTimeMillis();
     			} 
            	else 
            	{
     				MyApp.exitActivities();
     				System.exit(0);
     			}
     			return true;
            }
            return super.dispatchKeyEvent(event);
    }
	 
}
