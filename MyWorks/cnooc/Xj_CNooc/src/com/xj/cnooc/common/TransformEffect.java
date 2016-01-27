package com.xj.cnooc.common;

import com.nineoldandroids.view.ViewHelper;

import android.annotation.SuppressLint;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

/**
 * viewPager切换动画效果，目前只完善了几种
 * 
 * @author zhulanting
 */
public class TransformEffect implements PageTransformer {

	private final static float MIN_SCALE = 0.75f;
	private static final float ROT_MAX = 20.0f;
	private static final float MIN_ALPHA = 0.5f;
	private TransitionEffect effect;

	public TransformEffect() {
		// TODO Auto-generated constructor stub
		this.effect = TransitionEffect.Standard;
	}

	public TransformEffect(TransitionEffect effect) {
		// TODO Auto-generated constructor stub
		this.effect = effect;
	}

	@Override
	public void transformPage(View view, float position) {
		// TODO Auto-generated method stub
		doTransformPage(view, position, effect);
	}

	private void doTransformPage(View view, float position,
			TransitionEffect effect) {
		// TODO Auto-generated method stub
		switch (effect) {
		case Standard:
			break;
		case Depth:
			animateDepth(view, position);
			break;
		case Tablet:
			animateTablet(view, position);
			break;
		case CubeIn:
			animateCube(view, position);
			break;
		case CubeOut:

			break;
		case FlipHorizontal:

			break;
		case FlipVertical:

			break;
		case Stack:

			break;
		case ZoomIn:

			break;
		case ZoomOut:
			animateZoomOut(view, position);
			break;
		case RotateDown:
			animateRotateDown(view, position);
			break;
		case RotateUp:

			break;
		case Accordion:

			break;

		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	private void animateZoomOut(View view, float position) {
		// TODO Auto-generated method stub
		if (position <= 1) // a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
		{ // [-1,1]
			// Modify the default slide transition to shrink the page as well
			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
			float vertMargin = view.getHeight() * (1 - scaleFactor) / 2;
			float horzMargin = view.getWidth() * (1 - scaleFactor) / 2;
			if (position < 0) {
				view.setTranslationX(horzMargin - vertMargin / 2);
			} else {
				view.setTranslationX(-horzMargin + vertMargin / 2);
			}

			// Scale the page down (between MIN_SCALE and 1)
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);

			// Fade the page relative to its size.
			view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
					/ (1 - MIN_SCALE) * (1 - MIN_ALPHA));
		}
	}

	private void animateRotateDown(View view, float position) {
		// TODO Auto-generated method stub
		/*
		 * 左右动画一样
		 */
		if (position <= 1) {

			mRot = (ROT_MAX * position);
			ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.5f);
			ViewHelper.setPivotY(view, view.getMeasuredHeight());
			ViewHelper.setRotation(view, mRot);
		}
	}

	@SuppressLint("NewApi")
	private void animateDepth(View view, float position) {
		// TODO Auto-generated method stub
		if (position <= 0) {
			// view.setAlpha(1);
		} else if (position <= 1) {
			view.setAlpha(1 - position);
			view.setTranslationX(view.getWidth() * -position);
			float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
					* (1 - Math.abs(position));
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);
		}
	}

	public enum TransitionEffect {
		Standard, Depth, Tablet, CubeIn, CubeOut, FlipVertical, FlipHorizontal, Stack, ZoomIn, ZoomOut, RotateUp, RotateDown, Accordion
	}

	private Matrix mMatrix = new Matrix();
	private Camera mCamera = new Camera();
	private float[] mTempFloat2 = new float[2];

	private float mTrans;

	private float mRot;

	protected float getOffsetXForRotation(float degrees, int width, int height) {
		mMatrix.reset();
		mCamera.save();
		mCamera.rotateY(Math.abs(degrees));
		mCamera.getMatrix(mMatrix);
		mCamera.restore();

		mMatrix.preTranslate(-width * 0.5f, -height * 0.5f);
		mMatrix.postTranslate(width * 0.5f, height * 0.5f);
		mTempFloat2[0] = width;
		mTempFloat2[1] = height;
		mMatrix.mapPoints(mTempFloat2);
		return (width - mTempFloat2[0]) * (degrees > 0.0f ? 1.0f : -1.0f);
	}

	protected void animateTablet(View view, float position) {
		if (position <= -1) {

		} else if (position <= 1) {
			mTrans = getOffsetXForRotation(mRot, view.getWidth(),
					view.getHeight());
			mRot = -30.0f * position;
			mTrans = getOffsetXForRotation(mRot, view.getWidth(),
					view.getHeight());
			if (position <= 0) {
				mTrans *= -1;
			}
			ViewHelper.setPivotX(view, view.getWidth() * 0.5f);
			ViewHelper.setPivotY(view, view.getHeight() * 0.5f);
			ViewHelper.setTranslationX(view, mTrans);
			ViewHelper.setRotationY(view, mRot);
		}
	}

	/**
	 * position参数指明给定页面相对于屏幕中心的位置。它是一个动态属性，会随着页面的滚动而改变。当一个页面填充整个屏幕是，它的值是0，
	 * 当一个页面刚刚离开屏幕的右边时
	 * ，它的值是1。当两个也页面分别滚动到一半时，其中一个页面的位置是-0.5，另一个页面的位置是0.5。基于屏幕上页面的位置
	 * ，通过使用诸如setAlpha()、setTranslationX()、或setScaleY()方法来设置页面的属性，来创建自定义的滑动动画。
	 */
	private void animateCube(View view, float position) {
		if (position <= 0) {
			ViewHelper.setPivotX(view, view.getMeasuredWidth());
			ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);
			// 只在Y轴做旋转操作
			ViewHelper.setRotationY(view, 90f * position);
		} else if (position <= 1) {
			ViewHelper.setPivotX(view, 0);
			ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);
			ViewHelper.setRotationY(view, 90f * position);
		}
	}

	/*
	 * private void animateAccordion(View left, View right, float
	 * positionOffset) { if (mState != State.IDLE) { if (left != null) {
	 * manageLayer(left, true); ViewHelper.setPivotX(left,
	 * left.getMeasuredWidth()); ViewHelper.setPivotY(left, 0);
	 * ViewHelper.setScaleX(left, 1 - positionOffset); } if (right != null) {
	 * manageLayer(right, true); ViewHelper.setPivotX(right, 0);
	 * ViewHelper.setPivotY(right, 0); ViewHelper.setScaleX(right,
	 * positionOffset); } } }
	 * 
	 * private void animateZoom(View left, View right, float positionOffset,
	 * boolean in) { if (mState != State.IDLE) { if (left != null) {
	 * manageLayer(left, true); mScale = in ? ZOOM_MAX + (1 - ZOOM_MAX) * (1 -
	 * positionOffset) : 1 + ZOOM_MAX - ZOOM_MAX * (1 - positionOffset);
	 * ViewHelper.setPivotX(left, left.getMeasuredWidth() * 0.5f);
	 * ViewHelper.setPivotY(left, left.getMeasuredHeight() * 0.5f);
	 * ViewHelper.setScaleX(left, mScale); ViewHelper.setScaleY(left, mScale); }
	 * if (right != null) { manageLayer(right, true); mScale = in ? ZOOM_MAX +
	 * (1 - ZOOM_MAX) * positionOffset : 1 + ZOOM_MAX - ZOOM_MAX *
	 * positionOffset; ViewHelper.setPivotX(right, right.getMeasuredWidth() *
	 * 0.5f); ViewHelper.setPivotY(right, right.getMeasuredHeight() * 0.5f);
	 * ViewHelper.setScaleX(right, mScale); ViewHelper.setScaleY(right, mScale);
	 * } } }
	 * 
	 * private void animateRotate(View left, View right, float positionOffset,
	 * boolean up) { if (mState != State.IDLE) { if (left != null) {
	 * manageLayer(left, true); mRot = (up ? 1 : -1) * (ROT_MAX *
	 * positionOffset); mTrans = (up ? -1 : 1) (float) (getMeasuredHeight() -
	 * getMeasuredHeight() Math.cos(mRot * Math.PI / 180.0f));
	 * ViewHelper.setPivotX(left, left.getMeasuredWidth() * 0.5f);
	 * ViewHelper.setPivotY(left, up ? 0 : left.getMeasuredHeight());
	 * ViewHelper.setTranslationY(left, mTrans); ViewHelper.setRotation(left,
	 * mRot); } if (right != null) { manageLayer(right, true); mRot = (up ? 1 :
	 * -1) * (-ROT_MAX + ROT_MAX * positionOffset); mTrans = (up ? -1 : 1)
	 * (float) (getMeasuredHeight() - getMeasuredHeight() Math.cos(mRot *
	 * Math.PI / 180.0f)); ViewHelper.setPivotX(right, right.getMeasuredWidth()
	 * * 0.5f); ViewHelper.setPivotY(right, up ? 0 : right.getMeasuredHeight());
	 * ViewHelper.setTranslationY(right, mTrans); ViewHelper.setRotation(right,
	 * mRot); } } }
	 * 
	 * private void animateFlipHorizontal(View left, View right, float
	 * positionOffset, int positionOffsetPixels) { if (mState != State.IDLE) {
	 * if (left != null) { manageLayer(left, true); mRot = 180.0f *
	 * positionOffset; if (mRot > 90.0f) { left.setVisibility(View.INVISIBLE); }
	 * else { if (left.getVisibility() == View.INVISIBLE)
	 * left.setVisibility(View.VISIBLE); mTrans = positionOffsetPixels;
	 * ViewHelper.setPivotX(left, left.getMeasuredWidth() * 0.5f);
	 * ViewHelper.setPivotY(left, left.getMeasuredHeight() * 0.5f);
	 * ViewHelper.setTranslationX(left, mTrans); ViewHelper.setRotationY(left,
	 * mRot); } } if (right != null) { manageLayer(right, true); mRot = -180.0f
	 * * (1 - positionOffset); if (mRot < -90.0f) {
	 * right.setVisibility(View.INVISIBLE); } else { if (right.getVisibility()
	 * == View.INVISIBLE) right.setVisibility(View.VISIBLE); mTrans =
	 * -getWidth() - getPageMargin() + positionOffsetPixels; ViewHelper
	 * .setPivotX(right, right.getMeasuredWidth() * 0.5f);
	 * ViewHelper.setPivotY(right, right.getMeasuredHeight() * 0.5f);
	 * ViewHelper.setTranslationX(right, mTrans); ViewHelper.setRotationY(right,
	 * mRot); } } } }
	 * 
	 * private void animateFlipVertical(View left, View right, float
	 * positionOffset, int positionOffsetPixels) { if (mState != State.IDLE) {
	 * if (left != null) { manageLayer(left, true); mRot = 180.0f *
	 * positionOffset; if (mRot > 90.0f) { left.setVisibility(View.INVISIBLE); }
	 * else { if (left.getVisibility() == View.INVISIBLE)
	 * left.setVisibility(View.VISIBLE); mTrans = positionOffsetPixels;
	 * ViewHelper.setPivotX(left, left.getMeasuredWidth() * 0.5f);
	 * ViewHelper.setPivotY(left, left.getMeasuredHeight() * 0.5f);
	 * ViewHelper.setTranslationX(left, mTrans); ViewHelper.setRotationX(left,
	 * mRot); } } if (right != null) { manageLayer(right, true); mRot = -180.0f
	 * * (1 - positionOffset); if (mRot < -90.0f) {
	 * right.setVisibility(View.INVISIBLE); } else { if (right.getVisibility()
	 * == View.INVISIBLE) right.setVisibility(View.VISIBLE); mTrans =
	 * -getWidth() - getPageMargin() + positionOffsetPixels; ViewHelper
	 * .setPivotX(right, right.getMeasuredWidth() * 0.5f);
	 * ViewHelper.setPivotY(right, right.getMeasuredHeight() * 0.5f);
	 * ViewHelper.setTranslationX(right, mTrans); ViewHelper.setRotationX(right,
	 * mRot); } } } }
	 * 
	 * protected void animateStack(View left, View right, float positionOffset,
	 * int positionOffsetPixels) { if (mState != State.IDLE) { if (right !=
	 * null) { manageLayer(right, true); mScale = (1 - SCALE_MAX) *
	 * positionOffset + SCALE_MAX; mTrans = -getWidth() - getPageMargin() +
	 * positionOffsetPixels; ViewHelper.setScaleX(right, mScale);
	 * ViewHelper.setScaleY(right, mScale); ViewHelper.setTranslationX(right,
	 * mTrans); } if (left != null) { left.bringToFront(); } } }
	 */

}
