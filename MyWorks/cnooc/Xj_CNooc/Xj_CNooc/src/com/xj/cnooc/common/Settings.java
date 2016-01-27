package com.xj.cnooc.common;


import com.xj.cnooc.data.XjConstants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 系统设置保存
 * @author zhulanting 2015/9/17
 *
 */
public class Settings extends XjConstants {
	private static Settings current = null;
	private static Object mLock = new Object();

	public static void init(Context context) {
		synchronized (mLock) {
			if (current == null) {
				current = new Settings(context);
			}
		}
	}

	public static Settings getInstance() {
		synchronized (mLock) {
			if (current == null) {
				throw new NullPointerException("Setting is not int");
			}
			return current;
		}
	}

	public static Settings getInstance(Context context) {
		synchronized (mLock) {
			if (current == null) {
				current = new Settings(context);
			}
			return current;
		}
	}

	private SharedPreferences mSpref = null;
	private Context mContext;

	private int mTouchDotPosX;
	private int mTouchDotPosY;

	private boolean isEnableAssistive;
	
	private boolean isEnableNotification;
	private boolean isEnableVirbrator;
	private boolean isEnableVoice;
	
	public Settings(Context context) {
		mContext = context;
		mSpref = PreferenceManager.getDefaultSharedPreferences(context);

		int init_code = mSpref.getInt(INIT_APPLICATION_VERSION_CODE, -1);
		
		isEnableAssistive = mSpref.getBoolean(ENABLE_ASSISTIVE_KEY, true);
		isEnableNotification = mSpref.getBoolean(ENABLE_NOTIFICATION_KEY, true);
		isEnableVirbrator = mSpref.getBoolean(ENABLE_VIBRATOR_KEY, true);
		isEnableVoice = mSpref.getBoolean(ENABLE_VOICE_KEY, true);

		mTouchDotPosX = mSpref.getInt(TOUCH_DOT_VIEW_POS_X_KEY,
				DEFAULT_TOUCH_DOT_VIEW_POS_X);
		mTouchDotPosY = mSpref.getInt(TOUCH_DOT_VIEW_POS_Y_KEY,
				DEFAULT_TOUCH_DOT_VIEW_POS_Y);

	}

	public boolean isEnableAutoUpdate() {
		return mSpref.getBoolean(ENABLE_AUTO_UPDATE_KEY, true);
	}

	public void setEnableAutoUpdate(boolean enable) {
		mSpref.edit().putBoolean(ENABLE_AUTO_UPDATE_KEY, enable).commit();
	}

	/**
	 * 是否启动 虚拟按键助手
	 * */
	public void setEnableAssistive(boolean enable) {
		isEnableAssistive = enable;
		mSpref.edit().putBoolean(ENABLE_ASSISTIVE_KEY, enable).commit();
	}
	
	public boolean isEnableAssistive() {
		return isEnableAssistive;
	}

	public int getTouchPositionX() {
		return mTouchDotPosX;
	}

	public int getTouchPositionY() {
		return mTouchDotPosY;
	}

	/**
	 * 设置TouchDot的位置
	 * */
	public void setTouchPosition(int x, int y) {
		mTouchDotPosX = x;
		mTouchDotPosY = y;
		Editor edit = mSpref.edit();
		edit.putInt(TOUCH_DOT_VIEW_POS_X_KEY, x);
		edit.putInt(TOUCH_DOT_VIEW_POS_Y_KEY, y);
		edit.commit();
	}

	/**
	 * 是否打开提醒功能
	 */
	
	public void setEnableNotificaction(boolean enable){
		isEnableNotification = enable;
		mSpref.edit().putBoolean(ENABLE_NOTIFICATION_KEY, enable).commit();
	}
	
	public boolean isEnableNotification() {
		return isEnableNotification;
	}
	
	/**
	 * 是否打开震动效果
	 * */
	public void setEnableVibrator(boolean enable) {
		isEnableVirbrator = enable;
		mSpref.edit().putBoolean(ENABLE_VIBRATOR_KEY, enable).commit();
	}

	public boolean isEnableVirbrator() {
		return isEnableVirbrator;
	}
	
	/**
	 * 是否使用声音提醒
	 */
	public void setEnableVoice(boolean enable) {
		isEnableVoice = enable;
		mSpref.edit().putBoolean(ENABLE_VOICE_KEY, enable).commit();
	}
	
	public boolean isEnableVoice() {
		return isEnableVoice;
	}
	
}
