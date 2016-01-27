package com.xj.cnooc.common;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xj.cnooc.model.BBSBean;
import com.xj.cnooc.model.PostBean;
import com.xj.cnooc.model.UserBean;

public class MyApp extends Application {
	private NotificationManager mNotificationManager;
	// private MediaPlayer mMediaPlayer;

	public static MyApp mInstance;

	public static UserBean globelUser;// 保存当前成功登录的用户的信息
	public static List<BBSBean> bbsTitle;// 保存的论坛技术讨论区
	public static List<PostBean> post_list;// 保存帖子的列表
	public static int hitchtotal;// 故障支持首页数量
	public static int asstotal;// 评估支持首页数量
	public static int recordtotal;// 知识库首页数量
	public static int bbsTotal;// 论坛首页数量

	@Override
	public void onCreate() {
		super.onCreate();

		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				this).writeDebugLogs().build();
		ImageLoader.getInstance().init(configuration);
		mInstance = this;
		init();
	}

	private void init() {
		// mMediaPlayer = MediaPlayer.create(this, R.raw.notify);
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
	}

	public NotificationManager getNotificationManager() {
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		return mNotificationManager;
	}

	// public synchronized MediaPlayer getMediaPlayer() {
	// if (mMediaPlayer == null)
	// mMediaPlayer = MediaPlayer.create(this, R.raw.notify);
	// return mMediaPlayer;
	// }

	public static MyApp getInstance() {
		return mInstance;
	}

	/** 保存所有打开的activity界面 */
	public LinkedList<Activity> contextList = new LinkedList<Activity>();

	/**
	 * 当用户点击[退出]时，关闭所有activity
	 */
	public void exitActivities() {
		Log.d("asd", contextList.size() + "");
		for (Activity context : contextList) {
			if (context != null) {
				context.finish();
			}
		}
	}

}
