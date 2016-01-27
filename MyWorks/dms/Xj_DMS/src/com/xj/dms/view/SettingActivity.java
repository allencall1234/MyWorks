package com.xj.dms.view;

import java.io.IOException;

import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.xj.dms.common.BaseActivity;
import com.xj.dms.common.MyApp;
import com.xj.dms.update.UpdateFailedListener;
import com.xj.dms.update.UpdateManager;
/**
 * 设置界面
 * @author qinfan
 *
 * 2015-11-6
 */
public class SettingActivity extends BaseActivity implements OnClickListener{
	private RelativeLayout rl_alert_user_info,rl_update;
	private TextView tv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_setting);
		init();
	}
	
	private void init(){
		hidebtn_more();
		setCenterTitle("设置");
		
		getbtn_back().setOnClickListener(this);
		
		rl_alert_user_info = (RelativeLayout) findViewById(R.id.rl_alert_user_info);
		rl_alert_user_info.setOnClickListener(this);
		
		rl_update = (RelativeLayout) findViewById(R.id.rl_update);
		rl_update.setOnClickListener(this);
		
		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_back.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	Handler handler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			if (msg.what == 0)
			{
				showToast("当前已是最新版本!");
			}
		};
	};

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.rl_alert_user_info:
			if(MyApp.isLogin){
				intent = new Intent();
				intent.setClass(SettingActivity.this, AlterUserInfoActivity.class);
				startActivity(intent);
			} else {
				showToast("您还未登录，不能进行修改");
			}
			break;
			
		case R.id.iv_back:
			finish();
			break;
			
		case R.id.tv_back:
			finish();
			break;
			
		case R.id.rl_update:
			try
			{
				new UpdateManager(this).checkUpdate(new UpdateFailedListener()
				{
					@Override
					public void onFailed() 
					{
						handler.sendEmptyMessage(0);
					}
				});;
			} 
			catch (NotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
//			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
//			    @Override
//			    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
//			        switch (updateStatus) {
//			        case UpdateStatus.Yes: // has update
//			            UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
//			            break;
//			        case UpdateStatus.No: // has no update
//			            Toast.makeText(SettingActivity.this, "已经是最新版本！", Toast.LENGTH_SHORT).show();
//			            break;
//			        case UpdateStatus.Timeout: // time out
//			            Toast.makeText(SettingActivity.this, "超时", Toast.LENGTH_SHORT).show();
//			            break;
//			        }
//			    }
//			});
//			UmengUpdateAgent.update(this);
			break;
			
		default:
			break;
		}
	}
}
