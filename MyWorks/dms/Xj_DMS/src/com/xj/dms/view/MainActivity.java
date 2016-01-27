package com.xj.dms.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xj.dms.common.BaseActivity;
import com.xj.dms.common.MyApp;
import com.xj.dms.view.login.LoginActivity;
/**
 * 主界面(信息页)
 * @author qinfan
 *
 * 2015-11-6
 */
public class MainActivity extends BaseActivity implements OnClickListener{
	private LinearLayout ll_main_fragment;
	private Button btn_notice,
				   btn_power_network_information,
				   btn_power_network_plan,
				   btn_workdesk;
	private FragmentManager manager;
	private FragmentTransaction transaction;
	private long mExitTime;
	private List<Button> buttons;
	private int[] drawables = {R.drawable.network_info_n,R.drawable.notice_n,R.drawable.netwpok_plan_n};
	private int[] drawables_s = {R.drawable.network_info_s,R.drawable.notice_s,R.drawable.network_plan_s};
	private int flag=0;//标志当前是在那个fragment，如果重复点击通过下面判断则不会重复加载

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_main);
		//设置标题栏左边图标
		getbtn_back().setImageResource(R.drawable.logo_small);
		init();
	}

	private void init(){
		btn_power_network_information = (Button) 
				findViewById(R.id.btn_power_network_information);
		btn_notice = (Button) 
				findViewById(R.id.btn_notice);
		btn_power_network_plan = (Button) 
				findViewById(R.id.btn_power_network_plan);
		btn_workdesk = (Button)
				findViewById(R.id.btn_workdesk);
		ll_main_fragment = (LinearLayout)
				findViewById(R.id.ll_main_fragment);
		/**将按钮添加到集合*/
		buttons = new ArrayList<Button>();
		buttons.add(btn_power_network_information);
		buttons.add(btn_notice);
		buttons.add(btn_power_network_plan);
		/**设置按钮点击事件*/
		btn_power_network_information.setOnClickListener(this);
		btn_notice.setOnClickListener(this);
		btn_power_network_plan.setOnClickListener(this);
		btn_workdesk.setOnClickListener(this);

		setCurrageFragment(1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_power_network_information:
			changeColor(btn_power_network_information);
			if(flag !=1){
				flag = 1;
				setCurrageFragment(flag);
			}
			break;
		case R.id.btn_notice:
			changeColor(btn_notice);
			if(flag != 2){
				flag = 2;
				setCurrageFragment(flag);
			}
			break;

		case R.id.btn_power_network_plan:
			changeColor(btn_power_network_plan);
			if(flag != 3){
				flag = 3;
				setCurrageFragment(flag);
			}
			break;

		case R.id.btn_workdesk:
			Intent intent;
			/**
			 * 判断用户是否登录，
			 * 已经登录直接跳入主菜单界面
			 * 未登录则跳入登录界面
			 */
			if(MyApp.isLogin){
				intent = new Intent();
				intent.setClass(
						MainActivity.this, 
						MainMenuActivity.class
				);
				startActivity(intent);
			}else{
				intent = new Intent();
				intent.setClass(
						MainActivity.this, 
						LoginActivity.class
				);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}
	
	/**改变底部banner按钮背景*/
	private void changeColor(Button btn){
		Drawable drawable = null ;
		for (int i = 0; i < 3; i++) {
			if(btn.getId() == buttons.get(i).getId()){
				drawable = getResources().getDrawable(drawables_s[i]); 
				btn.setTextColor(Color.parseColor("#013F80"));
				btn.setBackgroundResource(R.drawable.banner_bg);
			}else{
				drawable = getResources()
						.getDrawable(drawables[i]); 
				buttons.get(i).setBackgroundColor(
						Color.parseColor("#00000000")
				);
				buttons.get(i).setTextColor(
						Color.parseColor("#646464")
				);
			}
			drawable.setBounds(
					0, 0, 
					drawable.getMinimumWidth(), 
					drawable.getMinimumHeight()
					);
			buttons.get(i).setCompoundDrawables(
					null, drawable, null, null
			);
		}
	}
	
	/**切换Fragment*/
	private void setCurrageFragment(int flag){
		manager = getSupportFragmentManager();
		transaction = manager.beginTransaction();
		switch (flag) {
		case 1:
			setCenterTitle("电网信息");
			PowerNetworkInfoFragment myFragment =
					new PowerNetworkInfoFragment();
			transaction.replace(
					R.id.ll_main_fragment, 
					myFragment, 
					"myFragment"
			);
			transaction.commit();
			break;
			
		case 2:
			setCenterTitle("通知公告");
			NoticeFragment noticeFragment = 
					new NoticeFragment();
			transaction.replace(
					R.id.ll_main_fragment,
					noticeFragment,
					"myFragment"
			);
			transaction.commit();
			break;
			
		case 3:
			setCenterTitle("电网计划");
			PlanFragment planFragment = 
					new PlanFragment();
			transaction.replace(
					R.id.ll_main_fragment, 
					planFragment, 
					"myFragment"
			);
			transaction.commit();
			break;

		default:
			break;
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				showToast("再按一次退出应用");
				mExitTime = System.currentTimeMillis();
			} else {
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
