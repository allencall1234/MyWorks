package com.xj.dms.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xj.dms.common.BaseActivity;
import com.xj.dms.common.MyApp;
import com.xj.dms.common.MyDialog;
import com.xj.dms.view.login.LoginActivity;
/**
 * 更多界面
 * @author qinfan
 *
 * 2015-11-5
 */
public class MoreActivity extends BaseActivity implements OnClickListener{
	private ImageView user_head_image;
	private TextView tv_noLogin,tv_back;
	private LinearLayout ll_user_info;
	private RelativeLayout rl_login_myworkdesk,
						   rl_back_userInfo,
						   rl_exit_userLogin,
						   exit_app,
						   rl_setting;
	private View login_info_line;
	private TextView login_user_name,login_user_role,platform,loginDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_more);
		setCenterTitle("更多");
		getbtn_more().setImageResource(R.drawable.menu_off);
		getbtn_back().setImageResource(R.drawable.logo_small);
		init();
	}
	
	private void init(){
		tv_back = (TextView) findViewById(R.id.tv_back);
		user_head_image = (ImageView) findViewById(R.id.user_head_image);
		tv_noLogin = (TextView) findViewById(R.id.tv_noLogin);
		ll_user_info = (LinearLayout) findViewById(R.id.ll_user_info);
		rl_login_myworkdesk = (RelativeLayout) findViewById(R.id.rl_login_myworkdesk);
		rl_back_userInfo = (RelativeLayout) findViewById(R.id.rl_back_userInfo);
		rl_exit_userLogin = (RelativeLayout) findViewById(R.id.rl_exit_userLogin);
		login_info_line = (View) findViewById(R.id.login_info_line);
		exit_app = (RelativeLayout) findViewById(R.id.exit_app);
		rl_setting = (RelativeLayout) findViewById(R.id.rl_setting);
		login_user_name = (TextView) findViewById(R.id.login_user_name);
		login_user_role = (TextView) findViewById(R.id.login_user_role);
		platform = (TextView) findViewById(R.id.platform);
		loginDate = (TextView) findViewById(R.id.loginDate);
		
		getbtn_more().setOnClickListener(this);
		tv_back.setOnClickListener(this);
		rl_exit_userLogin.setOnClickListener(this);
		rl_exit_userLogin.setOnClickListener(this);
		exit_app.setOnClickListener(this);
		rl_setting.setOnClickListener(this);
		rl_login_myworkdesk.setOnClickListener(this);
		rl_back_userInfo.setOnClickListener(this);
		
		if(MyApp.isLogin){
			ll_user_info.setVisibility(View.VISIBLE);
			tv_noLogin.setVisibility(View.GONE);
			rl_login_myworkdesk.setVisibility(View.GONE);
			rl_back_userInfo.setVisibility(View.VISIBLE);
			rl_exit_userLogin.setVisibility(View.VISIBLE);
			login_info_line.setVisibility(View.VISIBLE);
			login_user_name.setText(MyApp.userBean.getName());
			login_user_role.setText(MyApp.userBean.getRole());
			platform.setText(MyApp.userBean.getPlatform());
			loginDate.setText(MyApp.userBean.getLoginDate()); 
			user_head_image.setBackgroundResource(R.drawable.head_img);
		}else{
			ll_user_info.setVisibility(View.GONE);
			tv_noLogin.setVisibility(View.VISIBLE);
			rl_login_myworkdesk.setVisibility(View.VISIBLE);
			rl_back_userInfo.setVisibility(View.GONE);
			rl_exit_userLogin.setVisibility(View.GONE);
			login_info_line.setVisibility(View.GONE);
			user_head_image.setBackgroundResource(R.drawable.image_head);
		}
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_more:
			finish();
			break;
			
		case R.id.tv_back:
			finish();
			break;
			
		case R.id.rl_exit_userLogin:
			exitDialog("确定退出用户登录");
			break;
			
		case R.id.exit_app:
			exitDialog("确定退出系统");
			break;
			
		case R.id.rl_setting:
			intent = new Intent();
			intent.setClass(MoreActivity.this, SettingActivity.class);
			startActivity(intent);
			break;
			
		case R.id.rl_login_myworkdesk:
			intent = new Intent();
			intent.setClass(MoreActivity.this, LoginActivity.class);
			startActivity(intent);
			break;
			
		case R.id.rl_back_userInfo:
			intent = new Intent();
			intent.setClass(MoreActivity.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
	
	private void exitDialog(String title){
		MyDialog dialog = new MyDialog(MoreActivity.this,title,TAG[0]);
		dialog.showDialog();
	}
}
