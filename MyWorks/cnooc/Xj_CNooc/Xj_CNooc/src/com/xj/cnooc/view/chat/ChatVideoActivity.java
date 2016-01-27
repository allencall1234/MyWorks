package com.xj.cnooc.view.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatDefine;
import com.bairuitech.anychat.AnyChatVideoCallEvent;
import com.xj.cnooc.adapter.RoleListAdapter1;
import com.xj.cnooc.common.BaseActivity;
import com.xj.cnooc.model.RoleInfo;
import com.xj.cnooc.view.R;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChatVideoActivity extends BaseActivity implements
		AnyChatBaseEvent, AnyChatVideoCallEvent,
		OnTouchListener {
	// handle send msg
	private static final int MSG_VIDEOGESPREK = 1; // 视频对话时间刷新消息
	private final int UPDATEVIDEOBITDELAYMILLIS = 200; // 监听音频视频的码率的间隔刷新时间（毫秒）

	int userID;
	private boolean bSelfVideoOpened = false; // 本地视频是否已打开
	private boolean bOtherVideoOpened = false; // 对方视频是否已打开
	private int mVideogesprekSec = 0; // 音视频对话的时间
	private Boolean mFirstGetVideoBitrate = false; // "第一次"获得视频码率的标致
	private Boolean mFirstGetAudioBitrate = false; // "第一次"获得音频码率的标致

	private SurfaceView mOtherView; // 远程视频显示
	private SurfaceView mMyView; // 本地视频显示
	private ImageButton mImgBtnReturn; // 返回
	private TextView mTitleName; // 标题
	private ImageButton mImgSwitchVideo; // 切换设备前后摄像头
	private Button mEndCallBtn;
	private ImageButton mBtnCameraCtrl; // 控制视频的按钮
	private ImageButton mBtnSpeakCtrl; // 控制音频的按钮
	private Dialog mDialog;
	private TextView mVideogesprekTimeTV; // 视频对话时间
	private Timer mVideogesprekTimer;

	private TimerTask mTimerTask;
	private Handler mHandler;

	public AnyChatCoreSDK anyChatSDK;

	private List<RoleInfo> mRoleInfoList = new ArrayList<RoleInfo>();
	private ListView roleList = null;
	private RoleListAdapter1 mAdapter;
	private ImageButton mShowRoles; // 视频界面显示好友列表菜单

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video_frame);
		Intent intent = getIntent();
		userID = Integer.parseInt(intent.getStringExtra("UserID"));

		InitSDK();
		InitLayout();
		updateTime();

		// 如果视频流过来了，则把背景设置成透明的
		handler.postDelayed(runnable, UPDATEVIDEOBITDELAYMILLIS);
	}

	private void InitSDK() {
		anyChatSDK = AnyChatCoreSDK.getInstance(this);
		anyChatSDK.SetBaseEvent(this);
		anyChatSDK.SetVideoCallEvent(this);
		anyChatSDK.mSensorHelper.InitSensor(this);
		AnyChatCoreSDK.mCameraHelper.SetContext(this);
	}

	private void InitLayout() {
		mMyView = (SurfaceView) findViewById(R.id.surface_local);
		mOtherView = (SurfaceView) findViewById(R.id.surface_remote);
		mImgBtnReturn = (ImageButton) this.findViewById(R.id.returnImgBtn);
		mTitleName = (TextView) this.findViewById(R.id.titleName);
		mImgSwitchVideo = (ImageButton) findViewById(R.id.ImgSwichVideo);
		mEndCallBtn = (Button) findViewById(R.id.endCall);
		mBtnSpeakCtrl = (ImageButton) findViewById(R.id.btn_speakControl);
		mBtnCameraCtrl = (ImageButton) findViewById(R.id.btn_cameraControl);
		mVideogesprekTimeTV = (TextView) findViewById(R.id.videogesprekTime);

		mShowRoles = (ImageButton) findViewById(R.id.btn_MenuControl);

		mTitleName.setText("与 \"" + anyChatSDK.GetUserName(userID) + "\" 对话中");
		mImgSwitchVideo.setVisibility(View.VISIBLE);


		mImgBtnReturn.setOnClickListener(onClickListener);
		mBtnSpeakCtrl.setOnClickListener(onClickListener);
		mBtnCameraCtrl.setOnClickListener(onClickListener);
		mImgSwitchVideo.setOnClickListener(onClickListener);
		mEndCallBtn.setOnClickListener(onClickListener);
		mShowRoles.setOnClickListener(onClickListener);
		// 如果是采用Java视频采集，则需要设置Surface的CallBack
		if (AnyChatCoreSDK
				.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER) == AnyChatDefine.VIDEOCAP_DRIVER_JAVA) {
			mMyView.getHolder().addCallback(AnyChatCoreSDK.mCameraHelper);
		}

		// 如果是采用Java视频显示，则需要设置Surface的CallBack
		if (AnyChatCoreSDK
				.GetSDKOptionInt(AnyChatDefine.BRAC_SO_VIDEOSHOW_DRIVERCTRL) == AnyChatDefine.VIDEOSHOW_DRIVER_JAVA) {
			int index = anyChatSDK.mVideoHelper.bindVideo(mOtherView
					.getHolder());
			anyChatSDK.mVideoHelper.SetVideoUser(index, userID);
		}

		// mMyView.setZOrderOnTop(true);

		anyChatSDK.UserCameraControl(userID, 1);
		anyChatSDK.UserSpeakControl(userID, 1);

		// 判断是否显示本地摄像头切换图标
		if (AnyChatCoreSDK
				.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER) == AnyChatDefine.VIDEOCAP_DRIVER_JAVA) {
			if (AnyChatCoreSDK.mCameraHelper.GetCameraNumber() > 1) {
				// 默认打开前置摄像头
				AnyChatCoreSDK.mCameraHelper
						.SelectVideoCapture(AnyChatCoreSDK.mCameraHelper.CAMERA_FACING_FRONT);
			}
		} else {
			String[] strVideoCaptures = anyChatSDK.EnumVideoCapture();
			if (strVideoCaptures != null && strVideoCaptures.length > 1) {
				// 默认打开前置摄像头
				for (int i = 0; i < strVideoCaptures.length; i++) {
					String strDevices = strVideoCaptures[i];
					if (strDevices.indexOf("Front") >= 0) {
						anyChatSDK.SelectVideoCapture(strDevices);
						break;
					}
				}
			}
		}

		// 根据屏幕方向改变本地surfaceview的宽高比
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			adjustLocalVideo(true);
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			adjustLocalVideo(false);
		}

		anyChatSDK.UserCameraControl(-1, 1);// -1表示对本地视频进行控制，打开本地视频
		anyChatSDK.UserSpeakControl(-1, 1);// -1表示对本地音频进行控制，打开本地音频

		mMyView.setOnTouchListener(this);

		updateUserList();
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			try {
				int videoBitrate = anyChatSDK.QueryUserStateInt(userID,
						AnyChatDefine.BRAC_USERSTATE_VIDEOBITRATE);
				int audioBitrate = anyChatSDK.QueryUserStateInt(userID,
						AnyChatDefine.BRAC_USERSTATE_AUDIOBITRATE);
				if (videoBitrate > 0) {
					// handler.removeCallbacks(runnable);
					mFirstGetVideoBitrate = true;
					mOtherView.setBackgroundColor(Color.TRANSPARENT);
				}

				if (audioBitrate > 0) {
					mFirstGetAudioBitrate = true;
				}

				if (mFirstGetVideoBitrate) {
					if (videoBitrate <= 0) {
						Toast.makeText(ChatVideoActivity.this, "对方视频中断了!",
								Toast.LENGTH_SHORT).show();
						// 重置下，如果对方退出了，有进去了的情况
						mFirstGetVideoBitrate = false;
						mOtherView.setBackgroundResource(R.drawable.login);
					}
				}

				if (mFirstGetAudioBitrate) {
					if (audioBitrate <= 0) {
						Toast.makeText(ChatVideoActivity.this, "对方音频中断了！",
								Toast.LENGTH_SHORT).show();
						// 重置下，如果对方退出了，有进去了的情况
						mFirstGetAudioBitrate = false;
					}
				}
				handler.postDelayed(runnable, UPDATEVIDEOBITDELAYMILLIS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private void updateTime() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				// 刷新视频对话时间
				case MSG_VIDEOGESPREK:
					mVideogesprekTimeTV.setText(BaseMethod
							.getTimeShowStr(mVideogesprekSec++));
					break;
				default:
					break;
				}
			}

		};

		initVideogesprekTimer();
	}

	// 初始化视频对话定时器
	private void initVideogesprekTimer() {
		if (mVideogesprekTimer == null)
			mVideogesprekTimer = new Timer();

		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(MSG_VIDEOGESPREK);
			}
		};

		mVideogesprekTimer.schedule(mTimerTask, 1000, 1000);
	}

	// 点击事件
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			// 返回
			case (R.id.returnImgBtn):
			case (R.id.endCall): {
				showEndVideoCallDialog();
				break;
			}
			// 摄像头切换
			case (R.id.ImgSwichVideo): {
				// 如果是采用Java视频采集，则在Java层进行摄像头切换
				if (AnyChatCoreSDK
						.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER) == AnyChatDefine.VIDEOCAP_DRIVER_JAVA) {
					AnyChatCoreSDK.mCameraHelper.SwitchCamera();
					return;
				}

				String strVideoCaptures[] = anyChatSDK.EnumVideoCapture();
				String temp = anyChatSDK.GetCurVideoCapture();
				for (int i = 0; i < strVideoCaptures.length; i++) {
					if (!temp.equals(strVideoCaptures[i])) {
						anyChatSDK.UserCameraControl(-1, 0);
						bSelfVideoOpened = false;
						anyChatSDK.SelectVideoCapture(strVideoCaptures[i]);
						anyChatSDK.UserCameraControl(-1, 1);
						break;
					}
				}
			}
				break;
			// 控制自己语音的开关
			case R.id.btn_speakControl:
				if ((anyChatSDK.GetSpeakState(-1) == 1)) {
					mBtnSpeakCtrl.setImageResource(R.drawable.speak_off);
					anyChatSDK.UserSpeakControl(-1, 0);
				} else {
					mBtnSpeakCtrl.setImageResource(R.drawable.speak_on);
					anyChatSDK.UserSpeakControl(-1, 1);
				}

				break;
			// 控制自己视频的开关
			case R.id.btn_cameraControl:
				if ((anyChatSDK.GetCameraState(-1) == 2)) {
					mBtnCameraCtrl.setImageResource(R.drawable.camera_off);
					anyChatSDK.UserCameraControl(-1, 0);
				} else {
					mBtnCameraCtrl.setImageResource(R.drawable.camera_on);
					anyChatSDK.UserCameraControl(-1, 1);
				}
				break;

			case R.id.btn_MenuControl:
				showPopupWindow();
				break;

			default:
				break;
			}
		}
	};

	// 关闭视频呼叫确认框
	private void showEndVideoCallDialog() {
		mDialog = DialogFactory.getDialog(DialogFactory.DIALOGID_ENDCALL,
				userID, this);
		mDialog.show();
	}

	private void refreshAV() {
		anyChatSDK.UserCameraControl(userID, 1);
		anyChatSDK.UserSpeakControl(userID, 1);
		anyChatSDK.UserCameraControl(-1, 1);
		anyChatSDK.UserSpeakControl(-1, 1);
		mBtnSpeakCtrl.setImageResource(R.drawable.speak_on);
		mBtnCameraCtrl.setImageResource(R.drawable.camera_on);
		bOtherVideoOpened = false;
		bSelfVideoOpened = false;
	}

	private void destroyCurActivity() {
		onPause();
		onDestroy();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		anyChatSDK.SetBaseEvent(this);
		anyChatSDK.SetVideoCallEvent(this);

		// 如果是采用Java视频显示，则需要设置Surface的CallBack
		if (AnyChatCoreSDK
				.GetSDKOptionInt(AnyChatDefine.BRAC_SO_VIDEOSHOW_DRIVERCTRL) == AnyChatDefine.VIDEOSHOW_DRIVER_JAVA) {
			int index = anyChatSDK.mVideoHelper.bindVideo(mOtherView
					.getHolder());
			anyChatSDK.mVideoHelper.SetVideoUser(index, userID);
		}

		refreshAV();
	}

	@Override
	protected void onResume() {
		super.onResume();
		CallingCenter.mContext = ChatVideoActivity.this;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mHandler.removeMessages(MSG_VIDEOGESPREK);
		handler.removeCallbacks(runnable);
		anyChatSDK.UserCameraControl(userID, 0);
		anyChatSDK.UserSpeakControl(userID, 0);
		anyChatSDK.UserCameraControl(-1, 0);
		anyChatSDK.UserSpeakControl(-1, 0);
		anyChatSDK.SetBaseEvent(null);
		anyChatSDK.SetRecordSnapShotEvent(null);
		anyChatSDK.SetVideoCallEvent(null);
		anyChatSDK.mSensorHelper.DestroySensor();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			 showEndVideoCallDialog();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void adjustLocalVideo(boolean bLandScape) {
		float width;
		float height = 0;
		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		width = (float) dMetrics.widthPixels / 4;
		// LinearLayout layoutLocal = (LinearLayout) this
		// .findViewById(R.id.frame_local_area);
		// FrameLayout.LayoutParams layoutParams =
		// (android.widget.FrameLayout.LayoutParams) layoutLocal
		// .getLayoutParams();
		if (bLandScape) {

			if (AnyChatCoreSDK
					.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_WIDTHCTRL) != 0) {
				height = width
						* AnyChatCoreSDK
								.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_HEIGHTCTRL)
						/ AnyChatCoreSDK
								.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_WIDTHCTRL)
						+ 5;
			} else {
				height = (float) 3 / 4 * width + 5;
			}
		} else {

			if (AnyChatCoreSDK
					.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_HEIGHTCTRL) != 0) {
				height = width
						* AnyChatCoreSDK
								.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_WIDTHCTRL)
						/ AnyChatCoreSDK
								.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_HEIGHTCTRL)
						+ 5;
			} else {
				height = (float) 4 / 3 * width + 5;
			}
		}
		// layoutParams.width = (int) width;
		// layoutParams.height = (int) height;
		// layoutLocal.setLayoutParams(layoutParams);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			adjustLocalVideo(true);
			AnyChatCoreSDK.mCameraHelper.setCameraDisplayOrientation();
		} else {
			adjustLocalVideo(false);
			AnyChatCoreSDK.mCameraHelper.setCameraDisplayOrientation();
		}

	}

	@Override
	public void OnAnyChatConnectMessage(boolean bSuccess) {
	}

	@Override
	public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
	}

	@Override
	public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {

	}

	@Override
	public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
		
	}

	@Override
	public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {
		Intent intent = new Intent();
		intent.setAction("UserUpdate");
		intent.putExtra("dwUserId", dwUserId);
		intent.putExtra("bEnter", bEnter);
		sendBroadcast(intent);
		
		playMusic.playCallReceivedMusic(this);
		
		if (!bEnter) {

			for (int i = 0; i < mRoleInfoList.size(); i++) {
				if (mRoleInfoList.get(i).getUserID()
						.equals(String.valueOf(dwUserId))) {
					mRoleInfoList.remove(i);
					mAdapter.notifyDataSetChanged();
				}
			}

			// 对方离开房间后,自动切换到第一个
			if (dwUserId == userID) {
				Toast.makeText(ChatVideoActivity.this, "对方已离开房间!",
						Toast.LENGTH_SHORT).show();
				mTitleName.setText("没有选择视频对象!");
				mVideogesprekSec = 0;
				anyChatSDK.UserCameraControl(dwUserId, 0);
				anyChatSDK.UserSpeakControl(dwUserId, 0);
				bOtherVideoOpened = false;
			}

		} else {
			RoleInfo info = new RoleInfo();
			info.setUserID(String.valueOf(dwUserId));
			info.setName(anyChatSDK.GetUserName(dwUserId));

			mRoleInfoList.add(info);
			mAdapter.notifyDataSetChanged();

		}

	}

	private void updateVideoUI(int dwUserId) {
		int index = anyChatSDK.mVideoHelper.bindVideo(mOtherView.getHolder());
		anyChatSDK.mVideoHelper.SetVideoUser(index, dwUserId);

		anyChatSDK.UserCameraControl(dwUserId, 1);
		anyChatSDK.UserSpeakControl(dwUserId, 1);
		mTitleName
				.setText("与 \"" + anyChatSDK.GetUserName(dwUserId) + "\" 对话中");
		userID = dwUserId;
	}

	@Override
	public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
		// 网络连接断开之后，上层需要主动关闭已经打开的音视频设备
		if (bOtherVideoOpened) {
			anyChatSDK.UserCameraControl(userID, 0);
			anyChatSDK.UserSpeakControl(userID, 0);
			bOtherVideoOpened = false;
		}
		if (bSelfVideoOpened) {
			anyChatSDK.UserCameraControl(-1, 0);
			anyChatSDK.UserSpeakControl(-1, 0);
			bSelfVideoOpened = false;
		}

		// Intent mIntent = new Intent("NetworkDiscon");
		// 发送广播
		// sendBroadcast(mIntent);

		// 销毁当前界面
		destroyCurActivity();
	}


	@Override
	public void OnAnyChatVideoCallEvent(int dwEventType, int dwUserId,
			int dwErrorCode, int dwFlags, int dwParam, String userStr) {
		// this.finish();
	}

	// 更新用户列表
	private void updateUserList() {
		mRoleInfoList.clear();
		int[] userID = anyChatSDK.GetOnlineUser();

		for (int index = 0; index < userID.length; ++index) {
			RoleInfo info = new RoleInfo();
			info.setName(anyChatSDK.GetUserName(userID[index]));
			info.setUserID(String.valueOf(userID[index]));
			if (this.userID == userID[index]) {
				info.setCheck(true);
			}
			mRoleInfoList.add(info);
		}

		mAdapter = new RoleListAdapter1(getApplicationContext(),
				R.layout.role_item, mRoleInfoList);
		roleList = (ListView) findViewById(R.id.rolesList);
		roleList.setAdapter(mAdapter);
		roleList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int userid = Integer.parseInt(mRoleInfoList.get(arg2)
						.getUserID());
				if (userid != ChatVideoActivity.this.userID) {
					mVideogesprekSec = 0;

					mRoleInfoList.get(arg2).setCheck(true);
					mRoleInfoList.get(getUserIndex()).setCheck(false);
					mAdapter.notifyDataSetChanged();

					updateVideoUI(userid);
				}

			}
		});
	}

	private int getUserIndex() {
		int index = 0;
		for (int i = 0; i < mRoleInfoList.size(); i++) {
			if (mRoleInfoList.get(i).getUserID().equals(String.valueOf(userID))) {
				return i;
			}
		}
		return index;
	}

	private void showPopupWindow() {

		int visibility = roleList.getVisibility();
		roleList.setVisibility(visibility == View.GONE ? View.VISIBLE
				: View.GONE);

	}

	private int lastX, lastY;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX = (int) event.getRawX();
			lastY = (int) event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int dx = (int) (event.getRawX() - lastX);
			int dy = (int) (event.getRawY() - lastY);

			int l = v.getLeft() + dx;
			int t = v.getTop() + dy;
			int r = v.getRight() + dx;
			int b = v.getBottom() + dy;

			if (l < 0) {
				l = 0;
				r = l + v.getWidth();
			}

			if (t < mOtherView.getTop()) {
				t = mOtherView.getTop();
				b = t + v.getHeight();
			}

			if (r > mOtherView.getWidth()) {
				r = mOtherView.getWidth();
				l = r - v.getWidth();
			}

			if (b > mOtherView.getBottom()) {
				b = mOtherView.getBottom();
				t = b - v.getHeight();
			}

			v.layout(l, t, r, b);

			lastX = (int) event.getRawX();
			lastY = (int) event.getRawY();

			v.postInvalidate();
			break;
		case MotionEvent.ACTION_UP:

			int centerX = v.getLeft() + v.getWidth() / 2;
			int centerY = v.getTop() + v.getHeight() / 2;

			int cx = mOtherView.getWidth() / 2;
			int cy = mOtherView.getTop() + mOtherView.getHeight() / 2;

			if (centerX >= cx && centerY < cy) { // 第一象限
				v.layout(mOtherView.getWidth() - v.getWidth(),
						mOtherView.getTop(), mOtherView.getWidth(),
						mOtherView.getTop() + v.getHeight());
			} else if (centerX >= cx && centerY >= cy) {// 第四象限
				v.layout(mOtherView.getWidth() - v.getWidth(),
						mOtherView.getBottom() - v.getHeight(),
						mOtherView.getWidth(), mOtherView.getBottom());

			} else if (centerX < cx && centerY >= cy) {// 第三象限
				v.layout(0, mOtherView.getBottom() - v.getHeight(),
						v.getWidth(), mOtherView.getBottom());

			} else {// 第二象限

				v.layout(0, mOtherView.getTop(), v.getWidth(),
						mOtherView.getTop() + v.getHeight());
			}

			v.postInvalidate();
			break;

		default:
			break;
		}

		return true;
	}
}
