package com.xj.cnooc.common;


import com.xj.cnooc.view.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomDialog extends Dialog implements
		android.view.View.OnClickListener {

	int layoutRes;
	/** 布局文件 **/
	/** 上下文对象 **/
	Context context;
	/** 确定按钮 **/
	private Button confirmBtn;
	/** 取消按钮 **/
	private Button cancelBtn;
	
	private MyApp myApp;
	
	public CustomDialog(Context context) {
		this(context,0);
	}

	/**
	 * 自定义布局的构造方法
	 * 
	 * @param context
	 * @param resLayout
	 */
	public CustomDialog(Context context, int resLayout) {
		this(context,0,resLayout);
		
	}

	/**
	 * 自定义主题及布局的构造方法
	 * 
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public CustomDialog(Context context, int theme, int resLayout) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
		
		if (myApp == null) {
			myApp = MyApp.getInstance();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 指定布局
		this.setContentView(layoutRes);

		// 根据id在布局中找到控件对象
		confirmBtn = (Button) findViewById(R.id.confirm_btn);
		cancelBtn = (Button) findViewById(R.id.cancel_btn);

		// 设置按钮的文本颜色
		confirmBtn.setTextColor(0xff1E90FF);
		cancelBtn.setTextColor(0xff1E90FF);

		// 为按钮绑定点击事件监听器
		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.confirm_btn:
			// 点击了确认按钮
//			android.os.Process.killProcess(android.os.Process.myPid());// 杀死进程
			myApp.exitActivities();
			break;
		case R.id.cancel_btn:
			// 点击了取消按钮
			CustomDialog.this.dismiss();
			break;
		default:
			break;
		}
	}
}