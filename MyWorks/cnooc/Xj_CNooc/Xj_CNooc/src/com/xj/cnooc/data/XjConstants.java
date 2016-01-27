package com.xj.cnooc.data;

import android.annotation.SuppressLint;

/**
 * 保存系统设置数据
 * 
 * @author zhulanting 2015/9/19
 * 
 */
@SuppressLint("SdCardPath")
public class XjConstants {

	public static final String EXTRA_STRING = "extra_string";

	public static final String TOUCH_DOT_VIEW_POS_X_KEY = "touch_dot_pos_x";
	public static final String TOUCH_DOT_VIEW_POS_Y_KEY = "touch_dot_pos_y";

	public static final int DEFAULT_TOUCH_DOT_VIEW_POS_X = 0;
	public static final int DEFAULT_TOUCH_DOT_VIEW_POS_Y = 0;

	public static final String INIT_APPLICATION_VERSION_CODE = "init_version_code";

	public static final String ENABLE_ASSISTIVE_KEY = "enable_assisitive";
	public static final String ENABLE_AUTO_UPDATE_KEY = "enable_auto_update";
	public static final String ENABLE_VIBRATOR_KEY = "enable_virbator";
	public static final String ENABLE_VOICE_KEY = "enable_voice";
	public static final String ENABLE_NOTIFICATION_KEY = "enable_notication";
}
