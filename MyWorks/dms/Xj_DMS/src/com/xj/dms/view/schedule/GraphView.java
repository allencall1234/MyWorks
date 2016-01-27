package com.xj.dms.view.schedule;

import java.util.Arrays;
import java.util.Random;

import com.xj.dms.view.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 自定义直方图视图
 */
public class GraphView extends View {

	private Context mContext = null;

	// 直方图的颜色值
	private int[] colors = { R.color.model_1, R.color.model_2, R.color.model_3,
			R.color.model_4, R.color.model_5, R.color.model_6, R.color.model_7,
			R.color.model_8, R.color.model_9, R.color.model_10,
			R.color.model_11, R.color.model_12 };

	private float scale = 1.0f;
	// 画笔

	private int data[];
	private int length;

	private String title;
	private Paint mPaint = null;
	private Paint textPaint, bTextPaint;
	private int textX, textY; // 小标签文字的宽度和高度

	private int bTextX, bTextY;// 底部月份或类型标签的宽度和高度

	private int rWidth; // 直方图色块的宽度
	private int mWidth, mHeight; // view的高度和宽度

	public GraphView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public GraphView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub

	}

	public GraphView(Context context, String title, int[] data) {
		this(context, null);
		this.title = title;
		this.data = data;
		this.length = data.length;
		Log.i("zlt", "data = " + Arrays.toString(data));
	}

	public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub

		mContext = context;

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(getResources().getColor(R.color.model_1));

		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(Color.parseColor("#646464"));
		textPaint.setTextSize(getResources().getDimension(
				R.dimen.graph_small_textsize));

		title = "";
		textY = (int) (textPaint.descent() - textPaint.ascent());

		bTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bTextPaint.setTextSize(getResources().getDimension(
				R.dimen.graph_title_textsize));
		bTextY = (int) (bTextPaint.descent() - bTextPaint.ascent());
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = getWidth();
		mHeight = getHeight();
		rWidth = mWidth / (length + 2);
		scale = mHeight * 7 / (8 * 100);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for (int i = 0; i < length; i++) {
			mPaint.setColor(i <= colors.length ? getResources().getColor(colors[i])
					: getRandomColor());
			textX = (int) textPaint.measureText(String.valueOf(data[i]));
			canvas.save();
			canvas.translate(rWidth * (i + 1), mHeight * 7 / 8 - dp2px(2)
					- data[i] * scale);
			canvas.drawRect(0, 0, rWidth, scale * data[i] + dp2px(2), mPaint);
			canvas.drawText(data[i] + "", rWidth / 2 - textX / 2, 10 - textY,
					textPaint);
			canvas.restore();
		}

		bTextX = (int) bTextPaint.measureText(String.valueOf(title));
		// canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(),
		// bTextPaint);
		canvas.drawLine(0, mHeight * 7 / 8, getWidth(), mHeight * 7 / 8,
				bTextPaint);
		canvas.drawText(title, getWidth() / 2 - bTextX / 2, mHeight * 7 / 8
				+ bTextY + 20, bTextPaint);

	}

	private int getRandomColor() {
		// TODO Auto-generated method stub
		return Color.rgb(new Random().nextInt(255), new Random().nextInt(255),
				new Random().nextInt(255));
	}

	/**
	 * dp2px.
	 */
	public int dp2px(float value) {
		float scale = mContext.getResources().getDisplayMetrics().densityDpi;
		return (int) (value * (scale / 160) + 0.5f);
	}
}
