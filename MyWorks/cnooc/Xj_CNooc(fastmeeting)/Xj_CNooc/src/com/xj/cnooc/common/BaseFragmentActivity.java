package com.xj.cnooc.common;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.xj.cnooc.view.SettingActivity;

/**
 * BaseFragmentActivity ,所有的FragmentActivity需继续该类,便于辅助功能键的活动。
 * 
 * @author zhulanting
 *
 */
public class BaseFragmentActivity extends FragmentActivity {
	private Settings mSettings = null;
	private TouchView mTouchView = null;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mSettings = Settings.getInstance(getApplicationContext());
		mTouchView = TouchView.getInstance(getApplicationContext());
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// 当检测到程序进入后台时,隐藏辅助功能按钮
		if (mSettings.isEnableAssistive() && !isAppOnForeground()) {
			mTouchView.removeView();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 进入Activity时,判断设置中是否激活辅助按键,如果状态被激活，则显示辅助按键
		if (mSettings.isEnableAssistive()) {
			mTouchView.showView();
		}
	}

	/**
	 * 显示设置界面,继承BaseFrammentActivity后,在所属活动页中直接调用该方法,可以打开设置页
	 * 添加Intent.FLAG_ACTIVITY_NO_HISTORY属性,使得设置页进入后台后自动关闭，不会留在任务栈中
	 */
	public void showSettingActivity() {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), SettingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
	}

	/**
	 * 检测程序是否在前台运行
	 * 
	 * @return
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

}
