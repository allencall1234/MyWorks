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
 * 自定义直方图底部视图
 */
public class GraphBottomView extends View {

	private String[] titles;
	private int[] colors = { R.color.model_1, R.color.model_2, R.color.model_3,
			R.color.model_4, R.color.model_5, R.color.model_6, R.color.model_7,
			R.color.model_8, R.color.model_9, R.color.model_10, R.color.model_11,
			R.color.model_12};
	private Paint mPaint;
	private Paint textPaint;

	private int textX,textY; //标签文字的宽度和高度
	private int mSide; // 矩形块边长

	public GraphBottomView(Context context,String[] titles){
		this(context,null,0);
		this.titles = titles;
	}
	
	public GraphBottomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		setLayerType(LAYER_TYPE_SOFTWARE, null);
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mPaint.setStyle(Style.FILL_AND_STROKE);
		mPaint.setStrokeWidth(1);
		mPaint.setShadowLayer(4, 1, 1, Color.DKGRAY);
		
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(getResources().getColor(R.color.graph_bottom_text_color));
		textPaint.setTextSize(getResources().getDimension(R.dimen.graph_small_textsize));
		textY = (int) (textPaint.descent() - textPaint.ascent());
	}

	public GraphBottomView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int height;
		
		int width = MeasureSpec.getSize(widthMeasureSpec);
		
		mSide = width/12;
		
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		}else {
			int line = (titles.length - 1)/4 + 1;  //颜色标签的行数
			
			height = line * (textY/2 + 30 + 2 * mSide);
		}
		
		setMeasuredDimension(width, height);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			canvas.save();
			
			canvas.translate(mSide + mSide * 3 * (i%4), mSide / 2 + (i/4) * (mSide * 2 + textY/2 + 30));
			mPaint.setColor(getResources().getColor(colors[i]));
			canvas.drawRect(0, 0, mSide, mSide, mPaint);
		
			textX = (int) textPaint.measureText(titles[i]);
			canvas.drawText(titles[i], mSide/2 - textX/2, mSide + textY/2 + 30, textPaint);
			canvas.restore();
		}

	}

}
