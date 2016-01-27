package com.xj.cnooc.view.chat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatDefine;
import com.xj.cnooc.common.BaseActivity;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.https.HttpURL;
import com.xj.cnooc.view.R;

/**
 * 聊天等待大厅
 * @author Administrator
 *
 */
public class ChatHallActivity extends BaseActivity implements
		android.view.View.OnClickListener, AnyChatBaseEvent {

	private AnyChatCoreSDK anyChatSDK;

	private LinearLayout room[] = new LinearLayout[9]; // 房间布局
	private int roomIds[] = {
			R.id.chat_room_1,
			R.id.chat_room_2, // 房间布局id
			R.id.chat_room_3, R.id.chat_room_4, R.id.chat_room_5,
			R.id.chat_room_6, R.id.chat_room_7, R.id.chat_room_8,
			R.id.chat_room_9 };

	private int roomId; // 根据房间id进入房间
	private String roomName; // 根据房间名字进入房间
	private boolean isName; // 判断是否根据房间名字进入房间

	private boolean isInit; //判断是否初始化成功,是否能进入房间
	private Intent intent;

	private TextView initTextView = null; // 显示登录提示

	private int time;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				initTextView.setText("聊天室初始化化成功");
				handler.sendEmptyMessageDelayed(1, 500);
				break;
			case 1:
				initTextView.setText(MyApp.globelUser.getName() + ",欢迎进入聊天室!");
				isInit = true;
				break;
			case 3:
				anyChatSDK.Logout();
				ChatHallActivity.this.finish();
				break;
			case 4:
				if (time < 10 && !isInit) {
					time++;
					handler.sendEmptyMessageDelayed(4, 1000);
				}else if(!isInit){
					showToast("连接聊天室超时!");
					handler.sendEmptyMessageDelayed(3,500);
				}else {
					handler.removeMessages(4);
				}
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	};

	protected void initData() {
		// TODO Auto-generated method stub
		setContentView(R.layout.anychat_rooms);

		ApplyVideoConfig();
		InitSDK();

		login();

		for (int i = 0; i < roomIds.length; i++) {
			room[i] = (LinearLayout) findViewById(roomIds[i]);
			room[i].setOnClickListener(this);
		}

		initTextView = (TextView) findViewById(R.id.init_message);
		
		findViewById(R.id.hall_back).setOnClickListener(this);
		handler.sendEmptyMessage(4);
	}

	private void login() {
		// TODO Auto-generated method stub
		anyChatSDK.Connect(HttpURL.CHAT_SERVICE_IP, HttpURL.CHAT_PORT);
		anyChatSDK.Login(MyApp.globelUser.getName(), "123456");
	}

	// 初始化SDK
	private void InitSDK() {
		if (anyChatSDK == null) {
			anyChatSDK = AnyChatCoreSDK.getInstance(this);
			
			anyChatSDK.InitSDK(android.os.Build.VERSION.SDK_INT, 0);

			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_AUTOROTATION, 1);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		anyChatSDK.SetBaseEvent(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (!isInit) {
			showToast("房间正在尝试初始化,请稍后进入");
			return;
		}
		
		intent = new Intent(ChatHallActivity.this, ChatRoomActivity.class);
		switch (v.getId()) {
		case R.id.hall_back:
			finish();
			return;
		case R.id.chat_room_1: // 预定义房间1
			roomId = 1;
			break;
		case R.id.chat_room_2: // 预定义房间2
			roomId = 2;
			break;
		case R.id.chat_room_3: // 预定义房间3
			roomId = 3;
			break;
		case R.id.chat_room_4: // 预定义房间4
			roomId = 4;
			break;
		case R.id.chat_room_5: // 预定义房间5
			roomId = 5;
			break;
		case R.id.chat_room_6: // 预定义房间6
			roomId = 6;
			break;
		case R.id.chat_room_7: // 预定义房间7
			roomId = 7;
			break;
		case R.id.chat_room_8: // 预定义房间8
			roomId = 8;
			break;
		case R.id.chat_room_9: // 自定义方面名
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("请输入房间号");

			final EditText editText = new EditText(this);
			editText.setSingleLine(true);
			editText.setMaxEms(10);
			dialog.setView(editText);

			dialog.setPositiveButton("进入", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					roomName = editText.getText().toString();
					if (roomName != null && roomName.length() > 0) {
						isName = true;
						intent.putExtra("isName", isName);
						intent.putExtra("roomId", roomId);
						intent.putExtra("roomName", roomName);
						startActivityForResult(intent, 0);
					} else {
						showToast("房间号不能为空");
					}
				}
			});

			dialog.setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});

			dialog.show();
			return;

		default:
			break;
		}
		isName = false;
		intent.putExtra("isName", isName);
		intent.putExtra("roomId", roomId);
		intent.putExtra("roomName", roomName);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		anyChatSDK.Logout();
	}

	@Override
	public void OnAnyChatConnectMessage(boolean bSuccess) {
		// TODO Auto-generated method stub
		if (bSuccess) {
			handler.sendEmptyMessageDelayed(0, 100);
		}
	}

	@Override
	public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
		// TODO Auto-generated method stub
	}

	@Override
	public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
		// TODO Auto-generated method stub
	}

	@Override
	public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
		// TODO Auto-generated method stub
	}

	@Override
	public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {
		// TODO Auto-generated method stub
	}

	@Override
	public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
		// TODO Auto-generated method stub
		showToast("聊天室连接断开!");
		handler.sendEmptyMessageDelayed(3, 100);
	}
	
	/**
	 * 视频图像配置
	 */
	private void ApplyVideoConfig() {
		ConfigEntity configEntity = ConfigService.LoadConfig(this);
		if (configEntity.mConfigMode == 1) // 自定义视频参数配置
		{
			// 设置本地视频编码的码率（如果码率为0，则表示使用质量优先模式）
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_BITRATECTRL,
					configEntity.mVideoBitrate);
			if (configEntity.mVideoBitrate == 0) {
				// 设置本地视频编码的质量
				AnyChatCoreSDK.SetSDKOptionInt(
						AnyChatDefine.BRAC_SO_LOCALVIDEO_QUALITYCTRL,
						configEntity.mVideoQuality);
			}
			// 设置本地视频编码的帧率
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_FPSCTRL,
					configEntity.mVideoFps);
			// 设置本地视频编码的关键帧间隔
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_GOPCTRL,
					configEntity.mVideoFps * 4);
			// 设置本地视频采集分辨率
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_WIDTHCTRL,
					configEntity.mResolutionWidth);
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_HEIGHTCTRL,
					configEntity.mResolutionHeight);
			// 设置视频编码预设参数（值越大，编码质量越高，占用CPU资源也会越高）
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_PRESETCTRL,
					configEntity.mVideoPreset);
		}
		// 让视频参数生效
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_LOCALVIDEO_APPLYPARAM,
				configEntity.mConfigMode);
		// P2P设置
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_NETWORK_P2PPOLITIC,
				configEntity.mEnableP2P);
		// 本地视频Overlay模式设置
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_LOCALVIDEO_OVERLAY,
				configEntity.mVideoOverlay);
		// 回音消除设置
		AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_AUDIO_ECHOCTRL,
				configEntity.mEnableAEC);
		// 平台硬件编码设置
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_CORESDK_USEHWCODEC,
				configEntity.mUseHWCodec);
		// 视频旋转模式设置
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_LOCALVIDEO_ROTATECTRL,
				configEntity.mVideoRotateMode);
		// 本地视频采集偏色修正设置
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_LOCALVIDEO_FIXCOLORDEVIA,
				configEntity.mFixColorDeviation);
		// 视频GPU渲染设置
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_VIDEOSHOW_GPUDIRECTRENDER,
				configEntity.mVideoShowGPURender);
		// 本地视频自动旋转设置
		AnyChatCoreSDK.SetSDKOptionInt(
				AnyChatDefine.BRAC_SO_LOCALVIDEO_AUTOROTATION,
				configEntity.mVideoAutoRotation);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 0) {
			switch (resultCode) {
			case 1:		//网络连接失败
				this.finish();
				break;
			default:
				break;
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
