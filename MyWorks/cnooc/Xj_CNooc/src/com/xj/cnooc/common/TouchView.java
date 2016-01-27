package com.xj.cnooc.common;

import java.util.List;

import com.xj.cnooc.common.TouchLayout.OnCenterClickListener;
import com.xj.cnooc.common.TouchLayout.OnItemClickListener;
import com.xj.cnooc.view.R;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * 悬浮按钮控制类
 * 
 * @author zhulanting 2015/9/17
 * 
 */
public class TouchView implements OnCenterClickListener {
	/**
	 * 主聊天界面类名
	 */
	private static final String MAIN_CHAT_VIEW = "com.xj.cnooc.view.chat.ChatHallActivity";	
	private static final String MAIN_FALUT_VIEW = "com.xj.cnooc.view.support.FaultSupportActivity";
	private static final String MAIN_EVALATOR_VIEW = "com.xj.cnooc.view.support.FaultEvaluateActivity";
	private static final String MAIN_BBS_VIEW = "com.xj.cnooc.view.bbs.BBSFragmentActivity";
	private static final String MAIN_LIBRARY_VIEW = "com.xj.cnooc.view.support.KnowledgeBaseActivity";
	
	private static Object mLock = new Object();
	private static TouchView current;

	private Settings mSettings;

	public static TouchView getInstance(Context context) {
		synchronized (mLock) {
			if (current == null) {
				current = new TouchView(context);
			}
			return current;
		}
	}

	private Context mContext;

	private WindowManager.LayoutParams mTouchDotParams = null;
	private WindowManager.LayoutParams mTouchMainParams = null;
	private WindowManager mWindowManager = null;
	private TouchDotView mTouchDotView = null;
	private View mTouchMainView = null;
	private TouchLayout mView = null;

	private int mCurrentShowing = 0;

	public static final int SHOWING_DOTVIEW = 1;
	public static final int SHOWING_MAINVIEW = 2;
	public static final int SHOWING_NONE = 0;

	private TouchView(Context context) {
		this.mContext = context;

		mSettings = Settings.getInstance(mContext);

		mTouchDotParams = new WindowManager.LayoutParams();
		mTouchMainParams = new WindowManager.LayoutParams();
		mWindowManager = (WindowManager) context.getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);

		mTouchMainView = LayoutInflater.from(mContext).inflate(
				R.layout.touch_main_view, null);
		mView = (TouchLayout) mTouchMainView
				.findViewById(R.id.main_circle_layout);
		mView.setOnCenterClickListener(this);

		mView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position, long id,
					String name) {
				// TODO Auto-generated method stub

				ComponentName currentComponent = getTopActivity(mContext);
				String currentClass = currentComponent.getClassName();
				String toClass = null;
				
				switch (view.getId()) {
				case R.id.main_touch_chat:
					toClass = MAIN_CHAT_VIEW;
					break;
				case R.id.main_touch_evaluate:
					toClass = MAIN_EVALATOR_VIEW;
					break;
				case R.id.main_touch_bbs:
					toClass = MAIN_BBS_VIEW;
					break;
				case R.id.main_touch_fault:
					toClass = MAIN_FALUT_VIEW;
					break;
				case R.id.main_touch_library:
					toClass = MAIN_LIBRARY_VIEW;
					break;
				default:
					break;
				}
				if (!currentClass.equals(toClass)) {
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.setClassName(mContext, toClass);
					mContext.startActivity(intent);
				}
			}
		});

	}

	public void removeView() {
		if (mCurrentShowing == SHOWING_DOTVIEW) {
			mWindowManager.removeView(mTouchDotView);
			mCurrentShowing = SHOWING_NONE;
		} else if (mCurrentShowing == SHOWING_MAINVIEW) {
			mWindowManager.removeView(mTouchMainView);
			mCurrentShowing = SHOWING_NONE;
		}
	}

	public void showView() {

		if (mCurrentShowing != SHOWING_NONE) { // 如果当前状态不为none,表面已经激活
			return;
		}
		setupLayoutParams();
		showTouchDotView();
	}

	public int getShowingView() {
		return mCurrentShowing;
	}

	private void setupLayoutParams() {
		// 设置window type
		mTouchDotParams.type = LayoutParams.TYPE_PRIORITY_PHONE;
		// 设置图片格式，效果为背景透明
		mTouchDotParams.format = PixelFormat.RGBA_8888;

		/*
		 * 设置Window flag /* 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */
		mTouchDotParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		// 调整悬浮窗口至左上角，便于调整坐标
		mTouchDotParams.gravity = Gravity.LEFT | Gravity.TOP;
		// 以屏幕左上角为原点，设置x、y初始值
		mTouchDotParams.x = mSettings.getTouchPositionX();
		mTouchDotParams.y = mSettings.getTouchPositionY();

		// 设置悬浮窗口长宽数据
		mTouchDotParams.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		mTouchDotParams.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

		/*----*/
		mTouchMainParams.type = LayoutParams.TYPE_PRIORITY_PHONE;
		mTouchMainParams.format = PixelFormat.RGBA_8888;
		mTouchMainParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		mTouchMainParams.gravity = Gravity.CENTER;
		mTouchMainParams.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		mTouchMainParams.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
	}

	public WindowManager.LayoutParams getTouchDotParams() {
		return mTouchDotParams;
	}

	public WindowManager.LayoutParams getTouchMainParams() {
		return mTouchMainParams;
	}

	public void showTouchDotView() {
		if (mTouchDotView == null) {
			createTouchDotView();
		}
		if (mCurrentShowing == SHOWING_DOTVIEW) {
			return;
		} else if (mCurrentShowing == SHOWING_MAINVIEW) {
			mWindowManager.removeView(mTouchMainView);
		}
		// 显示TouchDotView
		mWindowManager.addView(mTouchDotView, mTouchDotParams);
		mCurrentShowing = SHOWING_DOTVIEW;
	}

	public void showTouchMainView() {
		if (mTouchMainView == null) {
			createTouchMainView();
		}
		if (mCurrentShowing == SHOWING_MAINVIEW) {
			return;
		} else if (mCurrentShowing == SHOWING_DOTVIEW) {
			mWindowManager.removeView(mTouchDotView);
		}
		mWindowManager.addView(mTouchMainView, mTouchMainParams);
		mCurrentShowing = SHOWING_MAINVIEW;
	}

	public void reload() {
		int showing = mCurrentShowing;
		removeView();
		mTouchDotView = null;
		mTouchMainView = null;
		if (showing == SHOWING_DOTVIEW) {
			showTouchDotView();
		} else if (showing == SHOWING_MAINVIEW) {
			showTouchMainView();
		}
	}

	private void createTouchDotView() {
		mTouchDotView = new TouchDotView(mContext);
		mTouchDotView.setOnTouchDotViewListener(mScrollListener);
	}

	private void createTouchMainView() {
		mTouchMainView = new TouchItemView(mContext);
	}

	public int getCurrentShowing() {
		return mCurrentShowing;
	}

	private TouchDotView.OnTouchDotViewListener mScrollListener = new TouchDotView.OnTouchDotViewListener() {
		@Override
		public void onScrollTo(View v, int x, int y) {
			mTouchDotParams.x = x;
			mTouchDotParams.y = y;
			mWindowManager.updateViewLayout(v, mTouchDotParams);
			mSettings.setTouchPosition(x, y);
		}

		@Override
		public void onTouchUp(View view, int x, int y) {
		}

		@Override
		public void onSingleTap(View view) {
			showTouchMainView();
		}

		@Override
		public void onLongPress() {
			removeView();
			mSettings.setEnableAssistive(false);
		}

		@Override
		public boolean onDoubleTap() {
			return false;
		}
	};

	@Override
	public void onCenterClick() {
		// TODO Auto-generated method stub
		if (mCurrentShowing == TouchView.SHOWING_DOTVIEW) {
			showTouchMainView();
		} else if (mCurrentShowing == TouchView.SHOWING_MAINVIEW) {
			showTouchDotView();
		}
	}

	@Override
	public void onMove(MotionEvent event1, MotionEvent event2) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 获取当前栈顶的Activity
	 * @param mContext
	 * @return
	 */
	public static ComponentName getTopActivity(Context mContext) {
		ActivityManager manager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskinfos = manager.getRunningTasks(1);

		if (runningTaskinfos != null) {
			return runningTaskinfos.get(0).topActivity;
		} else {
			return null;
		}
	}

}
