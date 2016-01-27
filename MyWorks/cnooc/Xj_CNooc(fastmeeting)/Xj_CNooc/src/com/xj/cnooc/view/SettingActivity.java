package com.xj.cnooc.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xj.cnooc.common.BaseActivity;
import com.xj.cnooc.common.CustomDialog;
import com.xj.cnooc.common.CustomSwitch;
import com.xj.cnooc.common.CustomSwitch.OnChangeListener;

/**
 * 
 * @author Administrator 设置界面
 *
 */
public class SettingActivity extends BaseActivity implements OnClickListener,
		OnChangeListener {
	private RelativeLayout rl_update_app;// 软件更新
	private Button btn_exit;// 退出
	// 标题
	// private Button btn_back;
	private ImageView iv_back;
	private ImageView iv_setting;
	private TextView tv_center_title;
	private TextView tv_right_title;
	// private Button btn_setting;

	/**
	 * 辅助按钮开关,用于显示以及隐藏辅助按钮
	 * <p>
	 * add by zhulanting 2015/9/19
	 */
	private CustomSwitch mSwitch_float;
	/**
	 * 新消息通知开关
	 */
	private CustomSwitch mSwitch_notification;
	/**
	 * 声音提醒开关
	 */
	private CustomSwitch mSwitch_voice;
	/**
	 * 震动提醒开关
	 */
	private CustomSwitch mSwitch_vibrate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.setting_view);
		initView();// 初始化界面
	}

	private void initView() {
		rl_update_app = (RelativeLayout) findViewById(R.id.rl_update_app);
		btn_exit = (Button) findViewById(R.id.btn_exit);

		mSwitch_float = (CustomSwitch) findViewById(R.id.switch_float);
		mSwitch_float.setChecked(mSettings.isEnableAssistive());
		mSwitch_float.setOnChangeListener(this);

		mSwitch_notification = (CustomSwitch) findViewById(R.id.switch_notification);
		mSwitch_notification.setChecked(mSettings.isEnableNotification());
		showOrHideView(mSettings.isEnableNotification());
		mSwitch_notification.setOnChangeListener(this);

		mSwitch_voice = (CustomSwitch) findViewById(R.id.switch_voice);
		mSwitch_voice.setChecked(mSettings.isEnableVoice());
		mSwitch_voice.setOnChangeListener(this);

		mSwitch_vibrate = (CustomSwitch) findViewById(R.id.switch_vibrate);
		mSwitch_vibrate.setChecked(mSettings.isEnableVirbrator());
		mSwitch_vibrate.setOnChangeListener(this);

		// btn_back = (Button) findViewById(R.id.btn_back);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_setting = (ImageView) findViewById(R.id.iv_setting);
		tv_center_title = (TextView) findViewById(R.id.tv_center_title);
		tv_right_title = (TextView) findViewById(R.id.tv_right_title);
		// btn_setting = (Button) findViewById(R.id.btn_setting);

		tv_center_title.setText("设置");
		tv_right_title.setVisibility(View.GONE);
		iv_setting.setVisibility(View.GONE);

		iv_back.setOnClickListener(this);

		// 软件更新监听
		rl_update_app.setOnClickListener(this);

		// 退出
		btn_exit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.rl_update_app:// 在线更新
			// UmengUpdateAgent.update(this);
			showToast("当前软件已更新到最新版本!");
			break;
		case R.id.btn_exit:// 退出
			CustomDialog dialog = new CustomDialog(SettingActivity.this,
					R.style.mystyle, R.layout.customdialog);
			dialog.show();
			break;

		default:
			break;
		}
	}

	// add by zhulanting 2015/9/19
	@Override
	public void onChanged(CustomSwitch mSwitch, boolean checkState) {
		// TODO Auto-generated method stub
		switch (mSwitch.getId()) {
		case R.id.switch_float:
			if (checkState) {
				mTouchView.showView();
			} else {
				mTouchView.removeView();
			}
			mSettings.setEnableAssistive(checkState);
			break;
		case R.id.switch_notification:
			showOrHideView(checkState);
			mSettings.setEnableNotificaction(checkState);
			break;
		case R.id.switch_voice:
			mSettings.setEnableVoice(checkState);
			break;
		case R.id.switch_vibrate:
			mSettings.setEnableVibrator(checkState);
			break;
		default:
			break;
		}
	}

	private void showOrHideView(boolean checkState) {
		// TODO Auto-generated method stub
		findViewById(R.id.rl_switch_voice).setVisibility(
				checkState ? View.VISIBLE : View.GONE);
		findViewById(R.id.rl_switch_vibrate).setVisibility(
				checkState ? View.VISIBLE : View.GONE);
		findViewById(R.id.view1).setVisibility(
				checkState ? View.VISIBLE : View.GONE);
		findViewById(R.id.view2).setVisibility(
				checkState ? View.VISIBLE : View.GONE);
	}
}
