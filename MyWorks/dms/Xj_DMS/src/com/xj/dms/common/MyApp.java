package com.xj.dms.common;

import java.util.LinkedList;

import android.app.Activity;
import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xj.dms.model.NetPowerBean;
import com.xj.dms.model.UserBean;

public class MyApp extends Application {
	public static boolean isLogin = false;
	public static MyApp mInstance;
	public static UserBean userBean = null;
	public static NetPowerBean netPowerBean = null;
	public static int taskApprovalFlag = -1;
	
	@Override
	public void onCreate() {
		super.onCreate();
		ImageLoaderConfiguration config =
				new ImageLoaderConfiguration.Builder(this)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(
						new Md5FileNameGenerator())
						.tasksProcessingOrder(QueueProcessingType.LIFO)
						.writeDebugLogs() // Remove
						.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
		mInstance = this;
	}
	
	public static MyApp getInstance() {
		return mInstance;
	}

	/** 保存所有打开的activity界面 */
	public static LinkedList<Activity> contextList = new LinkedList<Activity>();

	/**
	 * 当用户点击[退出]时，关闭所有activity
	 */
	public static void exitActivities() {
		for (Activity context : contextList) {
			if (context != null) {
				context.finish();
			}
		}
	}
}
