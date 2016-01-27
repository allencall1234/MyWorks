package com.xj_pipe.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class TimeView extends TextView {

	private Paint mPaint;
	private Bitmap dot;

	private int mWdith, mHeight;

	private static final int STATE_HEAD = 1;
	private static final int STATE_BODY = 2;
	private static final int STATE_FOOT = 3;

	private int state;
	private int radius;
	
	private int num;
	
	public TimeView(Context context) {
		// TODO Auto-generated constructor stub
		this(context, null);
	}

	public TimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mPaint = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextSize(30);
		mPaint.setStyle(Style.FILL_AND_STROKE);

		state = 2;
		radius = 25;
		num = 1;
	}

	public void setState(int state) {
		this.state = state;
		invalidate();
	}
	
	public void setNum(int num) {
		this.num = num;
		invalidate();
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		
		Log.i("zlt", "widthSize = " + widthSize);
		if (widthSize < 200) {
			widthSize = 200;
		}
	
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		Log.i("zlt",  "heightSize = " + heightSize);
		
		if (heightMode == MeasureSpec.EXACTLY) {
			if (heightSize < 100) {
				heightSize = 100;
			}
		}
//		
		setMeasuredDimension(widthSize, heightSize);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWdith = getWidth();
		mHeight = getHeight();
		Log.i("zlt", "mWdith = " + mWdith + ",mHeight = " + mHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawline(canvas);
		drawCircle(canvas);
		drawText(canvas);
	}

	private void drawText(Canvas canvas) {
		// TODO Auto-generated method stub
		int textWidth = (int) mPaint.measureText(num+"");
		int textHeight = (int) (mPaint.ascent() - mPaint.descent());
		mPaint.setColor(Color.BLACK);
		canvas.drawText(num+"", getWidth()/2 - textWidth/2, getHeight()/2 - textHeight/2 - 5, mPaint);
	}

	private void drawCircle(Canvas canvas) {
		// TODO Auto-generated method stub
		mPaint.setColor(Color.RED);
		canvas.drawCircle(mWdith/2, mHeight/2, radius, mPaint);
	}

	private void drawline(Canvas canvas) {
		// TODO Auto-generated method stub
		mPaint.setColor(Color.BLACK);
		int from, to;
		from = state > 1 ? 0 : mHeight / 2;
		to = state < 3 ? mHeight : mHeight / 2;
		canvas.drawLine(mWdith / 2, from, mWdith / 2, to, mPaint);
	}
}
