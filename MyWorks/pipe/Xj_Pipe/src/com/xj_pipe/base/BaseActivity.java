package com.xj_pipe.base;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.WriterException;
import com.xj_pipe.common.QRCodeEncode;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;
import com.xj_pipe.update.UpdateFailedListener;
import com.xj_pipe.update.UpdateManager;
import com.xj_pipe.utils.ToastUtils;
import com.xj_pipe.view.LoginActivity;
import com.xj_pipe.view.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author qinfan
 * 
 *         2015-12-14
 */
public class BaseActivity extends Activity implements OnClickListener {

	public static final int CREATE_QRCODE_DIALOG = 1000; // 创建二维码对话框
	public static final int CREATE_MODITY_CODE_DIALOG = CREATE_QRCODE_DIALOG + 1; // 创建修改密码对话框
	public static final int CREATE_VERSION_DIALOG = CREATE_QRCODE_DIALOG + 2; // 创建版本更新对话框
	public static final int CREATE_EXIT_USER_DIALOG = CREATE_QRCODE_DIALOG + 3; // 创建退出对话框
	public static final int CREATE_EXIT_DIALOG = CREATE_QRCODE_DIALOG + 4;

	private RelativeLayout rl_title_banner;// 标题栏布局
	private TextView tv_left_title, tv_center_title, tv_right_title;// 标题栏控件
	// 内容区域的布局
	private LinearLayout ll_content;
	private View contentView;
	private Toast mToast;
	private PopupWindow pWindow;
	private ImageView iv_logo, iv_more;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_base);

		MyApp.activities.add(this);

		initViews();
		addLisenter();// 设置左边、右边标题点击事件
	}

	private void initViews() {
		tv_left_title = (TextView) findViewById(R.id.title_banner_left);
		tv_center_title = (TextView) findViewById(R.id.tv_title_banner_center);
		tv_right_title = (TextView) findViewById(R.id.title_banner_right);

		rl_title_banner = (RelativeLayout) findViewById(R.id.rl_title_banner);
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		
		iv_logo = (ImageView) findViewById(R.id.iv_logo);
//		iv_more = (ImageView) findViewById(R.id.iv_more);
	}

	private void addLisenter() {
		if (tv_left_title.getVisibility() == View.VISIBLE) {
			tv_left_title.setOnClickListener(this);
		}
		tv_right_title.setOnClickListener(this);
	}

	/***
	 * 设置内容区域
	 * 
	 * @param resId
	 *            资源文件ID
	 */
	public void setContentLayout(int resId) {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contentView = inflater.inflate(resId, null);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		contentView.setLayoutParams(layoutParams);
		if (null != ll_content) {
			ll_content.addView(contentView);
		}
	}

	/**
	 * 隐藏上方的标题栏
	 */
	public void hideTitleBanner() {

		if (null != rl_title_banner) {
			rl_title_banner.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 获取标题栏
	 */
	public RelativeLayout getTitleBanner()
	{
		return rl_title_banner;
	}

	/**
	 * 显示上方标题栏
	 */
	public void showTitleBanner() {
		if (rl_title_banner != null
				&& rl_title_banner.getVisibility() == View.GONE) {
			rl_title_banner.setVisibility(View.VISIBLE);
		}
	}

	/***
	 * 设置内容区域
	 * 
	 * @param view
	 *            View对象
	 */
	public void setContentLayout(View view) {
		if (null != ll_content) {
			ll_content.addView(view);
		}
	}

	/**
	 * 得到内容的View
	 * 
	 * @return
	 */
	public View getLyContentView() {

		return contentView;
	}

	/**
	 * 得到标题栏左边控件
	 * 
	 * @return
	 */
	public TextView getLeftTitle() {
		return tv_left_title;
	}

	/**
	 * 得到标题栏右边控件
	 * 
	 * @return
	 */
	public TextView getRightTitle() {
		return tv_right_title;
	}

	/**
	 * 得到标题栏中间控件
	 * 
	 * @return
	 */
	public TextView getCenterTitle() {
		return tv_center_title;
	}
	
	/**
	 * 得到标题栏Logo
	 */
	public ImageView getLogo()
	{
		return iv_logo;
	}
	
	/**
	 * 得到标题栏右边更多按钮
	 */
	public ImageView getRightIV()
	{
		return iv_more;
	}

	/**
	 * 设置左边标题
	 * 
	 * @param title
	 */
	public void setLeftTitle(String title) {

		if (null != tv_left_title) {
			tv_left_title.setText(title);
		}
	}

	/**
	 * 设置中间标题
	 * 
	 * @param title
	 */
	public void setCenterTitle(String title) {

		if (null != tv_center_title) {
			tv_center_title.setText(title);
		}
	}

	/**
	 * 设置右边标题
	 * 
	 * @param title
	 */
	public void setRightTitle(String title) {

		if (null != tv_right_title) {
			tv_right_title.setText(title);
		}
	}
	
	/**
	 * 设置左边Logo图片
	 */
	public void setLiftLogo(int resid)
	{
		if (null != iv_logo)
		{
			iv_logo.setBackgroundResource(resid);
		}
	}

	/**
	 * 隐藏左边的标题
	 */
	public void hideLeftTitle() {

		if (null != tv_left_title) {
			tv_left_title.setVisibility(View.GONE);
		}
	}

	/***
	 * 隐藏右边的标题
	 */
	public void hideRightTitle() {
		if (null != tv_right_title) {
			tv_right_title.setVisibility(View.GONE);
		}
	}

	/***
	 * 隐藏中间标题
	 */
	public void hidetv_center_title() {
		if (null != tv_center_title) {
			tv_center_title.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 隐藏左边Logo
	 */
	public void hideLeftLogo()
	{
		if (null != iv_logo) 
		{
			iv_logo.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 
	 */

	/**
	 * 监听更多
	 */
	public void onMoreLisenter() {
		if (pWindow == null) { // 当pWindow不存在时，创建popupWindow
			LayoutInflater inflater = LayoutInflater.from(this);
			View menu = inflater.inflate(R.layout.activity_base_menu, null);
			menu.setFocusableInTouchMode(true);
			menu.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN && pWindow.isShowing()) {
						pWindow.dismiss();
						return true;
					}
					return false;
				}
			});
			String versionName = null;
			try {
				versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			versionName = String.format("版本检查(%1$s)", versionName);
			
			menu.findViewById(R.id.menu_qrcode).setOnClickListener(this);
			menu.findViewById(R.id.menu_modify_code).setOnClickListener(this);
			menu.findViewById(R.id.menu_version).setOnClickListener(this);
			menu.findViewById(R.id.menu_exit_user).setOnClickListener(this);
			menu.findViewById(R.id.menu_exit).setOnClickListener(this);
			
			((TextView)menu.findViewById(R.id.menu_version)).setText(versionName);
			
			pWindow = new PopupWindow(menu, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		// 判断当pWindow处于显示状态，点击则取消,否则显示该popupWindow
		if (pWindow.isShowing()) {
			pWindow.dismiss();
		} else {
			pWindow.setFocusable(true);
			pWindow.setOutsideTouchable(true);
			pWindow.setBackgroundDrawable(new BitmapDrawable()); // 一定要添加这行，否则会影响其他的事件触发
			pWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
			pWindow.showAtLocation(ll_content, Gravity.RIGHT | Gravity.TOP, 0,
					rl_title_banner.getHeight() + getStatusHeight());
		}

	}

	/**
	 * 监听返回
	 */
	public void onBackLisenter() {

	}

	/**
	 * 隐藏软键盘
	 */
	public boolean hideSoftInput() {
		boolean state = false;
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			state = inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
		return state;
	}

	/**
	 * 获取状态栏的高度
	 * 
	 * @return 状态栏高度
	 */
	public int getStatusHeight() {
		int screenHeight = this.getWindowManager().getDefaultDisplay()
				.getHeight();

		Rect outRect = new Rect();
		this.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);

		return screenHeight - outRect.height();
	}

	/**
	 * 获取标题栏的高度
	 * 
	 * @return 标题栏高度
	 */
	public int getTitleHeight() {
		return rl_title_banner.getHeight();
	}

	public void showToast(CharSequence s) {
		if (mToast == null) {
			mToast = Toast.makeText(getApplicationContext(), null,
					Toast.LENGTH_SHORT);
		}
		mToast.setText(s);
		mToast.show();
	}

	/**
	 * 获取屏幕尺寸,返回二维数组,分别存储宽度和高度值
	 * 
	 * @return
	 */
	public int[] getWindowSize() {
		Display display = getWindowManager().getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		return new int[] { point.x, point.y };
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.title_banner_left:
			onBackLisenter();
			break;

		case R.id.title_banner_right:
			onMoreLisenter();
			break;

		case R.id.menu_qrcode:
			showDialog(CREATE_QRCODE_DIALOG);
			pWindow.dismiss();
			break;
		case R.id.menu_modify_code:
			showDialog(CREATE_MODITY_CODE_DIALOG);
			pWindow.dismiss();
			break;
		case R.id.menu_version:
//			showDialog(CREATE_VERSION_DIALOG);
			try {
				new UpdateManager(this).checkUpdate(new UpdateFailedListener() {
					
					@Override
					public void onFailed() {
						// TODO Auto-generated method stub
						showToast("当前已经是最新版本!");
						
					}
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pWindow.dismiss();
			break;
		case R.id.menu_exit_user:
			showDialog(CREATE_EXIT_USER_DIALOG);
			pWindow.dismiss();
			break;
		case R.id.menu_exit:
			showDialog(CREATE_EXIT_DIALOG);
			pWindow.dismiss();
			break;
		}
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(this);
		switch (id) {
		case CREATE_QRCODE_DIALOG:
			final Dialog dialog = new Dialog(this, R.style.CommonDialog);
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(true);

			View view = inflater.inflate(R.layout.dialog_qrcode, null);
			view.findViewById(R.id.dialog_view_ok).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
			String temp = "http://218.17.162.229:8103/pis/downLoadApp.action";
			ImageView qrView = (ImageView) view
					.findViewById(R.id.dialog_view_qrcode);
			int width = (int) getResources().getDimension(R.dimen.qr_size);
			try {
				qrView.setImageBitmap(QRCodeEncode.createQRCode(temp, width));
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			dialog.setContentView(view);
			return dialog;

		case CREATE_MODITY_CODE_DIALOG:
			final Dialog dialog1 = new Dialog(this, R.style.CommonDialog);
			dialog1.setCancelable(true);
			dialog1.setCanceledOnTouchOutside(false);
			View view1 = inflater.inflate(R.layout.dialog_modify_code, null);
			EditText dialog_view_username = (EditText) view1.findViewById(R.id.dialog_view_username);
			final EditText preEditText = (EditText) view1
					.findViewById(R.id.dialog_view_precode);
			final EditText newEditText = (EditText) view1
					.findViewById(R.id.dialog_view_newcode);

			final TextView noticeView = (TextView) view1
					.findViewById(R.id.dialog_notice_view);
			
			dialog_view_username.setText(MyApp.userInfo.getUserName());
//			dialog_view_username.setText("123");

			view1.findViewById(R.id.dialog_view_ok).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String preCode = preEditText.getText().toString().trim();
							String newCode = newEditText.getText().toString().trim();
							
							if (TextUtils.isEmpty(preCode)) {
								noticeView.setText("旧密码不能为空!");
								noticeView.setVisibility(View.VISIBLE);
								return;
							}

							if (TextUtils.isEmpty(newCode)) {
								noticeView.setText("新密码不能为空!");
								noticeView.setVisibility(View.VISIBLE);
								return;
							}

//							if (!preCode.equals(newCode)) {
//								noticeView.setText("密码不匹配!");
//								noticeView.setVisibility(View.VISIBLE);
//								return;
//							}
							
							LoginActivity.preferences.edit().putString("username", "").commit();
							LoginActivity.preferences.edit().putString("password", "").commit();
							LoginActivity.preferences.edit().putBoolean("isRememberPwd", false).commit();
							
							dialog1.dismiss();
							
							UserHttp.modifyPassword(MyApp.userInfo.getStaffId(), preCode, newCode, new HttpDataCallBack() 
							{
								@Override
								public void HttpSuccess(String _result)
								{
									try 
									{
										JSONObject object = new JSONObject(_result);
										if (object.getBoolean("status") == true)
										{
											ToastUtils.ShowMessage(BaseActivity.this, "密码修改成功!");
											startActivity(new Intent(BaseActivity.this, LoginActivity.class));
											finish();
											
										}
										else
										{
											if (object.getString("message").equals("1")) 
											{
												ToastUtils.ShowMessage(BaseActivity.this, "原密码错误!");
											}
											else if (object.getString("message").equals("2"))
											{
												ToastUtils.ShowMessage(BaseActivity.this, "修改异常!");
											}
										}
									} 
									catch (JSONException e) 
									{
										e.printStackTrace();
									}
								}
								
								@Override
								public void HttpFail(int ErrCode) 
								{
									ToastUtils.ShowMessage(BaseActivity.this, "网络请求失败，请检查您的网络!");
								}
							});
						}
					});
			view1.findViewById(R.id.dialog_view_cancel).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog1.dismiss();
						}
					});

			dialog1.setContentView(view1);
			return dialog1;

		case CREATE_VERSION_DIALOG:
			final Dialog dialog2 = new Dialog(this, R.style.CommonDialog);
			dialog2.setCancelable(true);
			dialog2.setCanceledOnTouchOutside(true);

			View view2 = inflater.inflate(R.layout.dialog_version, null);
			view2.findViewById(R.id.dialog_view_cancel).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog2.dismiss();
						}
					});
			view2.findViewById(R.id.dialog_view_ok).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog2.dismiss();
						}
					});
			dialog2.setContentView(view2);
			return dialog2;

		case CREATE_EXIT_USER_DIALOG:
			final Dialog dialog3 = new Dialog(this, R.style.CommonDialog);
			dialog3.setCancelable(true);
			dialog3.setCanceledOnTouchOutside(true);

			View view3 = inflater.inflate(R.layout.dialog_exit_user, null);
			view3.findViewById(R.id.dialog_view_cancel).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog3.dismiss();
						}
					});
			view3.findViewById(R.id.dialog_view_ok).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog3.dismiss();
							startActivity(new Intent(getBaseContext(),
									LoginActivity.class));
							MyApp.exitApplication();
						}
					});
			dialog3.setContentView(view3);

			return dialog3;
		case CREATE_EXIT_DIALOG:
			final Dialog dialog4 = new Dialog(this, R.style.CommonDialog);
			dialog4.setCancelable(true);
			dialog4.setCanceledOnTouchOutside(false);

			View view4 = inflater.inflate(R.layout.dialog_exit, null);
			view4.findViewById(R.id.dialog_view_cancel).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog4.dismiss();
						}
					});
			view4.findViewById(R.id.dialog_view_ok).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyApp.exitApplication();
						}
					});
			dialog4.setContentView(view4);
			return dialog4;
		default:
			break;
		}
		return super.onCreateDialog(id);
	}

	/**
	 * 第二次显示对话框的时候，更新对话框内容。
	 * 第一次调用showDialog,直接调用OnCreateDialog放,第二次则直接调用onPrepareDialog方法
	 */
	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		switch (id) {
		case CREATE_QRCODE_DIALOG:

			break;
		case CREATE_MODITY_CODE_DIALOG:
			dialog.findViewById(R.id.dialog_notice_view).setVisibility(
					View.GONE);
			((EditText) dialog.findViewById(R.id.dialog_view_precode))
					.setText("");
			((EditText) dialog.findViewById(R.id.dialog_view_newcode))
					.setText("");
			break;
		case CREATE_VERSION_DIALOG:
			break;

		default:
			break;
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			onMoreLisenter();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
