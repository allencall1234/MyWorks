package com.xj.dms.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.xj.dms.common.BaseActivity;
import com.xj.dms.common.MyApp;
import com.xj.dms.common.MyDialog;
import com.xj.dms.https.HttpDataCallBack;
import com.xj.dms.https.UserHttp;
import com.xj.dms.model.NetPowerBean;
/**
 * 欢迎界面，程序入口
 * @author qinfan
 *
 * 2015-11-6
 */
public class WelcomeActivity extends BaseActivity {
	private boolean isFirstIn = false;//是否第一次进入标志

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.welcome_page);
		hideTitleView();
		init();
	}

	private void init(){
		//获取存入SharedPreferences的值
		SharedPreferences preferences = getSharedPreferences("first_pref",
				MODE_PRIVATE);
		//首次则默认为true
		isFirstIn = preferences.getBoolean("isFirstIn", true);
		
		//获取电网信息
		UserHttp.getNetPowerInfo(
				new HttpDataCallBack() {
					@Override
					public void HttpSuccess(String _result) {
						try {
							JSONObject jsonObject = 
									new JSONObject(_result);
							String str_activePower = 
									jsonObject.getString("activePower");
							String str_reactivePower = 
									jsonObject.getString("reactivePower");
							String str_hotPower = 
									jsonObject.getString("hotPower");
							
							MyApp.netPowerBean = new NetPowerBean();
							MyApp.netPowerBean.setActivePower("当前有用功："+str_activePower+"KW");
							MyApp.netPowerBean.setReactivePower("当前无用功："+str_reactivePower+"KW");
							MyApp.netPowerBean.setHotPower("热备："+str_hotPower+"KW");
							
							enterMainActivity();
							
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void HttpFail(int ErrCode) {
						MyDialog dialog = 
								new MyDialog(WelcomeActivity.this ,"" ,TAG[1]);
						dialog.showDialog();
					}
				});
	}
	
	/**根据是否第一次进入应用判断是否跳入引导界面*/
	private void enterMainActivity(){
		new Handler().postDelayed(new Runnable() {
			Intent intent = new Intent();
			@Override
			public void run() {
				if (isFirstIn) {
					intent = new Intent(
							WelcomeActivity.this,
							GuideActivity.class
					);
				} else {
					intent = new Intent(
							WelcomeActivity.this, 
							MainActivity.class
					);
				}
				WelcomeActivity.this.startActivity(intent);
				WelcomeActivity.this.finish();
			}
		}, 2000);
	}
}
