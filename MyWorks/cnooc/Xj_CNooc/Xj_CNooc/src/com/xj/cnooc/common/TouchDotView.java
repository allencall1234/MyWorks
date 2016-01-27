package com.xj.cnooc.common;

import com.xj.cnooc.view.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 悬浮按钮收缩状态视图
 * 
 * @author zhulanting 2015/9/17
 * 
 */
public class TouchDotView extends LinearLayout {

	private static final String TAG = "TouchDotView";

	private GestureDetector mGestureDetector;
	private ImageView mTopViewIconImg;
	private Context mContext;
	private OnTouchDotViewListener mOnTouchDotViewListener;

	public TouchDotView(Context context) {
		this(context, null);
	}

	public TouchDotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		mGestureDetector = new GestureDetector(mContext, mOnGestureListener);
		inflate(mContext, R.layout.touch_dot_view, this);
		initView();
	}

	private void initView() {
		mTopViewIconImg = (ImageView) findViewById(R.id.top_view_icon);
	}

	public ImageView getTouchDotImageView() {
		return mTopViewIconImg;
	}

	GestureDetector.SimpleOnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (mOnTouchDotViewListener != null) {
				int w = getWidth() / 2;
				int h = getHeight() / 2;
				int x = (int) (e2.getRawX() - w);
				int y = (int) (e2.getRawY() - h);
				mOnTouchDotViewListener.onScrollTo(TouchDotView.this, x, y);
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void onLongPress(MotionEvent e) {
			if (mOnTouchDotViewListener != null) {
				mOnTouchDotViewListener.onLongPress();
			}
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (mOnTouchDotViewListener != null) {
				return mOnTouchDotViewListener.onDoubleTap();
			}
			return false;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			return super.onDoubleTapEvent(e);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			mTopViewIconImg.setImageResource(R.drawable.icon_center);
			if (mOnTouchDotViewListener != null) {
				mOnTouchDotViewListener.onSingleTap(TouchDotView.this);
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			return false;
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = mGestureDetector.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			mTopViewIconImg.setImageResource(R.drawable.icon_center);
			if (mOnTouchDotViewListener != null) {
				int w = getWidth() / 2;
				int h = getHeight() / 2;
				int x = (int) (event.getRawX() - w);
				int y = (int) (event.getRawY() - h);
				mOnTouchDotViewListener.onTouchUp(this, x, y);
			}
		}
		return result;
	}

	public void setOnTouchDotViewListener(OnTouchDotViewListener listener) {
		this.mOnTouchDotViewListener = listener;
	}

	public abstract interface OnTouchDotViewListener {
		public abstract void onScrollTo(View view, int x, int y);

		public abstract void onTouchUp(View view, int x, int y);

		public abstract void onSingleTap(View view);

		public abstract void onLongPress();

		public abstract boolean onDoubleTap();
	}
}
