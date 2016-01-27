package com.xj.cnooc.common;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.xj.cnooc.view.SettingActivity;

public abstract class BaseActivity extends Activity {
	public static final int REFRESH_DATA = 0x01;// 刷新数据
	public static final int UPDATE_DATA = 0x02;// 加载下一页

	private MyApp myApp = null;
	private boolean firstPressed = true;

	public TouchView mTouchView = null;
	public Settings mSettings = null;

	/** 第一次按下返回键的时间设定为0 **/
	private long firstPressedTime = 0;

	private Toast mToast = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 每打开一个activity界面，就将此activity添加到集合中
		if (myApp == null) {
			myApp = MyApp.getInstance();
		}
		myApp.contextList.add(this);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题

		mTouchView = TouchView.getInstance(getApplicationContext());
		mSettings = Settings.getInstance(getApplicationContext());
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

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// 当检测到程序进入后台时,隐藏功能按钮
		if (mSettings.isEnableAssistive() && !isAppOnForeground()) {
			mTouchView.removeView();
		}
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

	// 只有当前界面是主界面才提示,否则在没做特殊处理的时候直接返回
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 后退键
			if (getClass().getName().equals(
					"com.xj.cnooc.view.MainGridViewActivity")) {
				if (firstPressed) {
					long secondTime = System.currentTimeMillis();
					// 第二次按下的时间和第一次按下的事件间隔在1秒内
					if (secondTime - firstPressedTime < 1000) {// 1秒内，认为是按了两下后退键，退出程序
						myApp.exitActivities();// 调用退出程序的方法，结束整个进程
						System.exit(0);
						return true;
					} else {// 如果时间过长，将第一次按下时间和是否按下置为默认
						firstPressedTime = System.currentTimeMillis();
					}
				} else {
					firstPressedTime = System.currentTimeMillis();
				}
				Toast.makeText(getApplicationContext(), "请再次按下后退键退出应用",
						Toast.LENGTH_SHORT).show();
				firstPressed = true;
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showToast(CharSequence s) {
		if (mToast == null) {
			mToast = Toast.makeText(getApplicationContext(), "",
					Toast.LENGTH_SHORT);
		}
		mToast.setText(s);
		mToast.show();
	}

	/**
	 * 显示设置界面,继承BaseActivity后,在所属活动页中直接调用该方法,可以打开设置页
	 * 添加Intent.FLAG_ACTIVITY_NO_HISTORY属性,使得设置页进入后台后自动关闭，不会留在任务栈中
	 */
	public void showSettingActivity() {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), SettingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
	}
}
