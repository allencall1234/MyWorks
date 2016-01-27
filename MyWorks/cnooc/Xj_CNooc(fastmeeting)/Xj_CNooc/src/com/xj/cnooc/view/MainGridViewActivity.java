package com.xj.cnooc.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xj.cnooc.adapter.GridViewAdapter;
import com.xj.cnooc.common.BaseActivity;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.https.HttpURL;
import com.xj.cnooc.model.ColumnImageBean;
import com.xj.cnooc.model.UserBean;
import com.xj.cnooc.view.bbs.BBSFragmentActivity;
import com.xj.cnooc.view.support.FaultEvaluateActivity;
import com.xj.cnooc.view.support.FaultSupportActivity;
import com.xj.cnooc.view.support.KnowledgeBaseActivity;

public class MainGridViewActivity extends BaseActivity implements
		OnItemClickListener {
	private ImageView iv_user_head;// 用户头像

	private TextView tv_username;// 用户名
	private TextView tv_user_roles;// 用户角色
	private TextView tv_account;// 用户账号
	private TextView tv_department;// 所属单位
	private TextView tv_last_login_time;// 上次登录时间

	private GridView gridview;
	private GridViewAdapter adapter;

	private UserBean bean;

	private List<ColumnImageBean> grid_list;

	private ProgressBar mProgressBar;
	private TextView titleTextView;
	private TextView process_Text;
	private TextView volume_Text;
	private LinearLayout loadingLayout;
	private Dialog mDialog;

	private int progress;
	private boolean cancelLoading;

	private String mSavePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);

		initView();
	}

	private void initView() {
		mSavePath = mSettings.getAppPath();

		getGirdViewData();

		iv_user_head = (ImageView) findViewById(R.id.iv_user_head);

		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_user_roles = (TextView) findViewById(R.id.tv_user_roles);
		tv_account = (TextView) findViewById(R.id.tv_account);
		tv_department = (TextView) findViewById(R.id.tv_department);
		tv_last_login_time = (TextView) findViewById(R.id.tv_last_login_time);

		gridview = (GridView) findViewById(R.id.gridview);

		bean = MyApp.globelUser;
		tv_username.setText(bean.getName());
		tv_user_roles.setText(bean.getRolename());
		tv_account.setText("账号：" + bean.getLoginName());
		tv_department.setText("单位：" + bean.getVendername());
		tv_last_login_time.setText("上次登录：" + bean.getBeforedate());

		// handler2.postDelayed(runnable, 1000 * 60);

		// 设置头像
		// ImageLoader.getInstance().displayImage(HttpURL.SERVICE_URL +
		// bean.getPhoto(), iv_user_head, ImageLoadOptions.getOptions());
		ImageLoader.getInstance().loadImage(
				HttpURL.SERVICE_URL + bean.getPhoto(),
				new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						iv_user_head
								.setImageResource(R.drawable.login_head_img);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						iv_user_head
								.setImageResource(R.drawable.login_head_img);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						iv_user_head.setImageBitmap(loadedImage);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {

					}
				});

		// 为gridview添加适配器
		adapter = new GridViewAdapter(MainGridViewActivity.this, grid_list);
		gridview.setAdapter(adapter);

		// gridview的item点击监听
		gridview.setOnItemClickListener(this);

	}

	private void getGirdViewData() {
		grid_list = new ArrayList<ColumnImageBean>();

		grid_list.add(new ColumnImageBean(" 故障支持",
				R.drawable.failure_support_img));
		grid_list.add(new ColumnImageBean(" 评估支持",
				R.drawable.assessment_support_img));
		grid_list.add(new ColumnImageBean("电网知识库",
				R.drawable.knowledge_base_img));
		grid_list.add(new ColumnImageBean(" 即时交流",
				R.drawable.timely_communication_img));
		grid_list.add(new ColumnImageBean(" 论坛交流",
				R.drawable.bbs_communication_img));
		grid_list.add(new ColumnImageBean("  设置", R.drawable.setting_img));
	}

	private void resume() {
		grid_list.get(0).setIndex(MyApp.hitchtotal);
		grid_list.get(1).setIndex(MyApp.asstotal);
		grid_list.get(2).setIndex(MyApp.recordtotal);
		grid_list.get(4).setIndex(MyApp.bbsTotal);
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		switch (position) {
		case 0:
			intent.setClass(MainGridViewActivity.this,
					FaultSupportActivity.class);
			startActivity(intent);
			break;
		case 1:
			intent.setClass(MainGridViewActivity.this,
					FaultEvaluateActivity.class);
			startActivity(intent);
			break;
		case 2:
			intent.setClass(MainGridViewActivity.this,
					KnowledgeBaseActivity.class);
			startActivity(intent);
			break;
		case 3:

			String pkgName = getResources().getString(R.string.pkg_name);
			String clsName = getResources().getString(R.string.cls_name);

			if (!isAppInstalled(pkgName)) { // 如果没有安装视频插件,则打开下载对话框,否则直接启动视频插件
				Log.i(MainGridViewActivity.this.toString(), "Package " + pkgName + " is not installed!");
				showCommonDialog();
				return;
			}

			ComponentName apk2Component1 = new ComponentName(pkgName, clsName);
			isAppOnForeground(pkgName);
			apk2Component1 = new ComponentName(pkgName, clsName);
			Intent mIntent = new Intent();
			Bundle mBundle = new Bundle();
			mBundle.putString("userName", MyApp.globelUser.getLoginName());
			mBundle.putString("userPwd", "123456");
			mBundle.putString("svrAddress", "218.17.162.229");
			mBundle.putString("svrPort", "1089");
			mIntent.putExtras(mBundle);
			mIntent.setComponent(apk2Component1);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(mIntent);

			break;
		case 4:
			startActivity(new Intent(MainGridViewActivity.this,
					BBSFragmentActivity.class));
			break;
		case 5:
			showSettingActivity();
			break;

		default:
			break;
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mProgressBar.setProgress(progress);
				volume_Text.setText(getVolumeBySize(msg.arg2) + "/"
						+ getVolumeBySize(msg.arg1));
				process_Text.setText(progress + "%");
				break;
			case 1:
				mSettings.setAppPath(mSavePath);
				installApk();
				break;
			default:
				break;
			}
		};
	};

	private String getVolumeBySize(int size) {
		String volume;
		String unit = "b";
		if (size > 1000000) {
			volume = String.format("%.1f", (float) size / 1000000);
			unit = "M";
		} else if (size > 1000) {
			volume = String.valueOf(size / 1000);
			unit = "K";
		} else {
			volume = String.valueOf(size);
		}
		return volume + unit;
	}

	/**
	 * 下载文件线程
	 */
	class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					cancelLoading = false;
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					URL url = new URL(HttpURL.downloadApkUrl);
					Log.i(MainGridViewActivity.this.toString(), "loading url = "  + url);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					Log.i(MainGridViewActivity.this.toString(), "file size is  "  + getVolumeBySize(length));
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, "xjchat.apk");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					Log.i(MainGridViewActivity.this.toString(), "Reading file!!!");
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						Message message = new Message();
						message.what = 0;
						message.arg1 = length;
						message.arg2 = count;
						mHandler.sendMessage(message);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(1);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelLoading);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, "xjchat.apk");
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		startActivity(i);
	}

	/**
	 * 显示是否下载视频App对话框
	 */
	private void showCommonDialog() {
		mDialog = new Dialog(this, R.style.mystyle);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		View dialogView = LayoutInflater.from(this).inflate(
				R.layout.no_app_dialog, null);

		titleTextView = (TextView) dialogView.findViewById(R.id.no_app_title);
		process_Text = (TextView) dialogView.findViewById(R.id.loading_process);
		volume_Text = (TextView) dialogView.findViewById(R.id.loading_volume);
		mProgressBar = (ProgressBar) dialogView
				.findViewById(R.id.loading_progress);
		loadingLayout = (LinearLayout) dialogView
				.findViewById(R.id.loading_layout);

		dialogView.findViewById(R.id.no_app_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 判断手机中是否已经下载了视频插件，如果下载了直接安装,没有下载就开启线程下载
//						File file = new File(mSavePath, "xjchat.apk");
//						if (file.exists()) {
//							installApk();
//							mDialog.dismiss();
//						} else {
							v.setVisibility(View.GONE);
							titleTextView.setText("正在下载视频插件...");
							mProgressBar.setVisibility(View.VISIBLE);
							loadingLayout.setVisibility(View.VISIBLE);
							Log.i(MainGridViewActivity.this.toString(), "DownLoading apk!!!");
							new downloadApkThread().start();//开启下载线程
//						}
					}
				});
		dialogView.findViewById(R.id.no_app_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						cancelLoading = true;
						mDialog.dismiss();
					}
				});

		mDialog.setContentView(dialogView);
		mDialog.show();
	}

	/**
	 * 检测视频插件是否安装
	 * 
	 * @param uri
	 *            app包名
	 * @return
	 */
	private boolean isAppInstalled(String uri) {
		PackageManager pm = getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}

	/**
	 * 检测程序是否在运行
	 * 
	 * @return
	 */
	public boolean isAppOnForeground(String pkgName) {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(pkgName)) {
				activityManager.killBackgroundProcesses(pkgName);
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		/*
		 * 在主界面屏蔽掉辅助触摸按钮
		 */
		RunningTaskInfo taskInfo = getRunningTaskInfo(this);
		String clsBaseActivityName = taskInfo.baseActivity.getClassName();

		resume();// 界面刷新

		if (clsBaseActivityName.equals("com.xj.cnooc.view.LoginActivity")
				|| clsBaseActivityName
						.equals("com.xj.cnooc.view.MainGridViewActivity")) {
			mTouchView.removeView();
		}

	}

	/**
	 * 获取当前活动任务
	 * 
	 * @param mContext
	 * @return
	 */
	public static RunningTaskInfo getRunningTaskInfo(Context mContext) {
		ActivityManager manager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskinfos = manager.getRunningTasks(1);

		if (runningTaskinfos != null) {
			return runningTaskinfos.get(0);
		} else {
			return null;
		}
	}
}
