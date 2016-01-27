package com.xj.cnooc.view.chat;

import java.util.ArrayList;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatTextMsgEvent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xj.cnooc.adapter.AnyChatMessageAdapter;
import com.xj.cnooc.adapter.RoleListAdapter;
import com.xj.cnooc.common.BaseActivity;
import com.xj.cnooc.common.CircleImageView;
import com.xj.cnooc.common.DragListView;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.https.HttpURL;
import com.xj.cnooc.model.AnyChatMessage;
import com.xj.cnooc.model.RoleInfo;
import com.xj.cnooc.utils.ImageLoadOptions;
import com.xj.cnooc.view.R;
import com.xj.cnooc.view.chat.SlidingMenu.OnMenuSlidingListener;

/**
 * 聊天室房间
 * 
 * @author zhulanting 2015/10/22
 */
public class ChatRoomActivity extends BaseActivity implements AnyChatBaseEvent,
		AnyChatTextMsgEvent, OnClickListener {

	private AnyChatCoreSDK anyChatSDK;

	private int roomId; // 房间id
	private String roomName; // 房间名字
	private boolean isName; // 判断是否是房间名字或者房间id
	private int UserselfID; // 当前用户id
	private CircleImageView mLogo; // 当前用户头像

	private List<RoleInfo> mRoleInfoList = new ArrayList<RoleInfo>(); // 当前聊天室成员列表
	private RoleListAdapter mAdapter; // 聊天室成员列表适配
	private ListView mRoleList; // 聊天室成员列表显示list

	private SlidingMenu mMenu; // 聊天室侧滑布局

	private AnyChatMessageAdapter messageAdapter = null; // 公屏聊天适配器
	private DragListView messageList = null; // 公屏聊天显示列表
	private List<AnyChatMessage> messages = new ArrayList<AnyChatMessage>(); // 公屏聊天消息列表

	private EditText editText = null; // 消息编辑框
	private Button sendButton = null; // 发送消息按钮

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	};
	
	protected void initData() {
		// TODO Auto-generated method stub
		setContentView(R.layout.anychat_room_layout);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		InitSdk();

		Intent intent = getIntent();
		isName = intent.getBooleanExtra("isName", false);
		roomId = intent.getIntExtra("roomId", 1);
		roomName = intent.getStringExtra("roomName");

		if (isName) {
			anyChatSDK.EnterRoomEx(roomName, "");
			((TextView) findViewById(R.id.current_room)).setText("当前房间:"
					+ roomName);
			((TextView) findViewById(R.id.chat_name)).setText("房间" + roomName);

		} else {
			anyChatSDK.EnterRoom(roomId, "");
			((TextView) findViewById(R.id.current_room)).setText("当前房间:房间"
					+ roomId);
			((TextView) findViewById(R.id.chat_name)).setText("房间" + roomId);
		}

		InitView();
		
		registerBoradcastReceiver();
	}

	// 广播
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("UserUpdate")) {
				AnyChatMessage message = new AnyChatMessage();
				message.setType(0);
				message.setMessage(anyChatSDK.GetUserName(intent.getIntExtra("dwUserId",-1))
						+ (intent.getBooleanExtra("bEnter", false) ? "进入房间" : "离开房间"));
				messages.add(message);
				messageAdapter.notifyDataSetChanged();
			}
		}
	};

	// 注册广播
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("UserUpdate");
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * 初始化话AnyChatCoreSDK,设置基本接口和聊天接口
	 */
	private void InitSdk() {
		anyChatSDK = AnyChatCoreSDK.getInstance(this);
		
	}

	/**
	 * 初始化布局
	 */
	private void InitView() {
		mRoleList = (ListView) findViewById(R.id.roleListView);
		mLogo = (CircleImageView) findViewById(R.id.chat_logo);
		mMenu = (SlidingMenu) findViewById(R.id.id_menu);
		messageList = (DragListView) findViewById(R.id.mListView);
		editText = (EditText) findViewById(R.id.edit_user_comment);
		sendButton = (Button) findViewById(R.id.btn_chat_send);
		sendButton.setOnClickListener(this);
		mLogo.setOnClickListener(this);
		
		findViewById(R.id.chat_setting).setOnClickListener(this);
		findViewById(R.id.current_room).setOnClickListener(this);
		
		mMenu.setOnMenuSlidingListner(new OnMenuSlidingListener() {

			@Override
			public void onMenuSliding(float alpha) {
				// TODO Auto-generated method stub
				mLogo.setAlpha(alpha * 3.3f);
			}
		});
		mMenu.toggle();

		ImageLoader.getInstance().displayImage(
				HttpURL.SERVICE_URL + MyApp.globelUser.getPhoto(), mLogo,
				ImageLoadOptions.getOptions());

	} 

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		anyChatSDK.SetBaseEvent(this);
		anyChatSDK.SetTextMessageEvent(this);
		updateUserList();
		updateMessageList();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		anyChatSDK.LeaveRoom(-1);
		anyChatSDK.SetTextMessageEvent(null);
		anyChatSDK.SetBaseEvent(null);
		unregisterReceiver(mBroadcastReceiver);
	}

	@Override
	public void OnAnyChatConnectMessage(boolean bSuccess) {
		// TODO Auto-generated method stub
	}

	@Override
	public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
		// TODO Auto-generated method stub
	}

	@Override
	public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
		updateUserList();

		// 用户进入房间时，更新公屏消息
		AnyChatMessage message = new AnyChatMessage();
		message.setType(0);
		message.setMessage(MyApp.globelUser.getName() + "进入房间");
		messages.add(message);
		messageAdapter.notifyDataSetChanged();
	}

	@Override
	public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
		// TODO Auto-generated method stub
	}

	@Override
	public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {
		
		playMusic.playCallReceivedMusic(this);
		
		if (bEnter) {
			RoleInfo roleInfo = new RoleInfo();
			roleInfo.setUserID(String.valueOf(dwUserId));
			roleInfo.setName(anyChatSDK.GetUserName(dwUserId));
			roleInfo.setRoleIconID(R.drawable.head_img);
			mRoleInfoList.add(roleInfo);
			mAdapter.notifyDataSetChanged();
		} else {
			for (int i = 0; i < mRoleInfoList.size(); i++) {
				if (mRoleInfoList.get(i).getUserID().equals("" + dwUserId)) {
					mRoleInfoList.remove(i);
					mAdapter.notifyDataSetChanged();
				}
			}
		}

		AnyChatMessage message = new AnyChatMessage();
		message.setType(0);
		message.setMessage(anyChatSDK.GetUserName(dwUserId)
				+ (bEnter ? "进入房间" : "离开房间"));
		messages.add(message);
		messageAdapter.notifyDataSetChanged();
	}

	@Override
	public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
		// TODO Auto-generated method stub
		showToast("聊天室连接断开!");
		this.setResult(1);
		finish();
	}

	@Override
	public void OnAnyChatTextMessage(int dwFromUserid, int dwToUserid,
			boolean bSecret, String message) {
		// TODO Auto-generated method stub
		AnyChatMessage msg = new AnyChatMessage();
		msg.setType(2);
		msg.setMessage(message);
		msg.setUserName(anyChatSDK.GetUserName(dwFromUserid));

		messages.add(msg);
		messageAdapter.notifyDataSetChanged();
	}

	/**
	 * 更新公屏聊天消息列表
	 */
	private void updateMessageList() {
		// TODO Auto-generated method stub
		messageAdapter = new AnyChatMessageAdapter(getApplicationContext(),
				R.layout.anychat_system_message, messages);
		messageList.setAdapter(messageAdapter);
		messageList.setSelection(messages.size() - 1);
	}

	/**
	 * 更新聊天室当前用户列表
	 */
	private void updateUserList() {
		mRoleInfoList.clear();
		int[] userID = anyChatSDK.GetOnlineUser();
		RoleInfo userselfInfo = new RoleInfo();
		userselfInfo.setName(MyApp.globelUser.getName() + "(自己)");
		userselfInfo.setUserID(String.valueOf(UserselfID));
		userselfInfo.setRoleIconID(R.drawable.head_img);
		mRoleInfoList.add(userselfInfo);

		for (int index = 0; index < userID.length; ++index) {
			RoleInfo info = new RoleInfo();
			info.setName(anyChatSDK.GetUserName(userID[index]));
			info.setUserID(String.valueOf(userID[index]));
			info.setRoleIconID(R.drawable.head_img);
			mRoleInfoList.add(info);
		}

		mAdapter = new RoleListAdapter(ChatRoomActivity.this, mRoleInfoList);

		mRoleList.setAdapter(mAdapter);
		mRoleList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 != 0) {
					Intent intent = new Intent();
					intent.setClass(ChatRoomActivity.this,
							ChatVideoActivity.class);
					intent.putExtra("UserID", mRoleInfoList.get(arg2)
							.getUserID());
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.setResult(0);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_chat_send: // 在公屏发送消息
			String text = editText.getText().toString().trim();
			if (text != null && text.length() > 0) {
				AnyChatMessage message = new AnyChatMessage();
				message.setType(1);
				message.setMessage(text);
				messages.add(message);
				int flag = anyChatSDK.SendTextMessage(-1, 0, text);
				if (flag == 0) {
					messageAdapter.notifyDataSetChanged();
					editText.setText(null);
				}
			} else {
				showToast("请输入发送内容！");
			}
			break;
		case R.id.chat_setting:
			showSettingActivity();
			break;
		case R.id.chat_logo: // 点击公屏上自己的头像,侧滑菜单来回切换
			mMenu.toggle();
			break;
		case R.id.current_room:
			finish();
			break;
		default:
			break;
		}
	}

}
