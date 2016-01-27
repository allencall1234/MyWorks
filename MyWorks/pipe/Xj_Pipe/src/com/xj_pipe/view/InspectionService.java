package com.xj_pipe.view;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.xj_pipe.base.MyApp;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class InspectionService extends Service {

	private LayoutInflater inflater = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				UserHttp.getMainDot(MyApp.userInfo.getStaffId(),
						new HttpDataCallBack() {

							@Override
							public void HttpSuccess(String _result) {
								// TODO Auto-generated method stub
								try {
									int taskCount = new JSONObject(_result)
									 .getInt("emergencyTaskCount");

									Log.i("zlt", "taskCount = " + taskCount
											+ ",localUnDealMsg = "
											+ MyApp.localUnDealMsg);
									// 如果从网络获取的未读应急任务数量大于本地任务未读任务数量就进行处理
									if (!MyApp.isShow
											&& (taskCount > MyApp.localUnDealMsg)) {
										// 如果程序在前台运行则显示对话框,程序运行在后台则显示通知栏
										MyApp.isShow = true;
										if (isAppOnForeground()) {
											showDialog(taskCount
													- MyApp.localUnDealMsg);
										} else {
											showNotification(taskCount
													- MyApp.localUnDealMsg);
										}
									}

									mHandler.sendEmptyMessageDelayed(0, 5000);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							@Override
							public void HttpFail(int ErrCode) {
								// TODO Auto-generated method stub

							}
						});

				break;
			case 1:

				if (MyApp.userInfo == null) {
					mHandler.sendEmptyMessageDelayed(1, 5000);
				} else {
					mHandler.removeMessages(1);
					mHandler.sendEmptyMessage(0);
				}
				break;
			default:
				break;
			}

		};
	};

	private void showNotification(int count) {
		// TODO Auto-generated method stub
		Log.i("zlt", "showNotification");
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				getApplicationContext());

		builder.setContentTitle("通知")
				.setContentText(String.format("你有%1$d条新的应急任务", count))
				.setContentIntent(
						getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
				// .setNumber(number)//显示数量
				.setTicker("新的消息")// 通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
				.setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
				// .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
				.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_VIBRATE)//
				// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
				// Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
				// requires VIBRATE permission
				.setSmallIcon(R.drawable.app_icon).setAutoCancel(true);
		// 点击的意图ACTION是跳转到Intent

		Intent resultIntent = new Intent(getApplicationContext(),
				ActivityInspection.class);
		resultIntent.putExtra("taskType", 2);
		resultIntent.putExtra("fromService", true);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		// resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, resultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);
		manager.notify(100, builder.build());

	}

	/**
	 * @获取默认的pendingIntent,为了防止2.3及以下版本报错
	 * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT 点击去除：
	 *           Notification.FLAG_AUTO_CANCEL
	 */
	public PendingIntent getDefalutIntent(int flags) {
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
				new Intent(), flags);
		return pendingIntent;
	}

	private void showDialog(int count) {
		Log.i("zlt", "showDialog");
		final Dialog dialog = new Dialog(getApplicationContext(),
				R.style.CommonDialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);

		View view = inflater.inflate(R.layout.dialog_new_message, null);
		TextView title = (TextView) view.findViewById(R.id.no_app_title);
		title.setText(String.format("你有%1$d条新的应急任务", count));

		Button cancelBtn = (Button) view.findViewById(R.id.dialog_view_cancel);
		Button okBtn = (Button) view.findViewById(R.id.dialog_view_ok);

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();

				Intent intent = new Intent(getApplicationContext(),
						ActivityInspection.class);
				intent.putExtra("taskType", 2);
				intent.putExtra("fromService", true);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

		dialog.setContentView(view);

		dialog.getWindow()
				.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		inflater = LayoutInflater.from(getApplicationContext());
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		mHandler.sendEmptyMessageDelayed(1, 5000);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHandler.removeMessages(0);
		mHandler.removeMessages(1);
		mHandler = null;
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
