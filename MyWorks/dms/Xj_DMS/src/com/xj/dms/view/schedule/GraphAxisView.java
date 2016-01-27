package com.xj.dms.view.schedule;

import com.xj.dms.view.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义坐标轴
 *
 */
public class GraphAxisView extends View {

	private Paint mPaint = null;

	private int scale;

	private int mHeight;

	public GraphAxisView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(3);
	}

	public GraphAxisView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public GraphAxisView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mHeight = getHeight();
		scale = mHeight * 7 / (8 * 100);
	}
 
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawLine(getWidth()-1, 0, getWidth()-1, getHeight() * 7 / 8, mPaint);
		
//		canvas.save();
//		canvas.rotate(30,getWidth(),0);
//		canvas.drawLine(getWidth(), 0, getWidth(), getResources().getDimension(R.dimen.graph_axis_length), mPaint);
//		canvas.restore();
		
		mPaint.reset();
		mPaint.setTextSize(getResources().getDimension(
				R.dimen.graph_small_textsize));
		for (int i = 0; i <= 100; i += 10) {
			canvas.drawText(
					String.valueOf(i),
					getWidth()
							- getResources().getDimension(
									R.dimen.graph_axis_rightmargin),
					getHeight() * 7 / 8 - i * scale, mPaint);
		}
	}

}
