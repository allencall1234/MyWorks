package com.xj.dms.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xj.dms.common.BaseDialog.OnAgreeListener;
import com.xj.dms.common.BaseDialog.OnCloseListener;
import com.xj.dms.common.BaseDialog.OnRejectedListener;
import com.xj.dms.view.R;

/**
 * 选择对话框
 * 
 */
public class SelectionDialog extends Dialog {

	private ImageView iv_close;
	private Button btnAgree, btnRejected;
	private Button btnConfirm, btnCancel;
	private LinearLayout linearLayout1, linearLayout2;
	private RelativeLayout rellayout1;
	
	private OnAgreeListener mOnAgreeListener;// 确定同意
	private OnRejectedListener mOnRejectedListener;// 取消监听
	private OnCloseListener mOnCloseListener;// 关闭监听
	
	private String rejectedText;
	private boolean flag = false;
	private int sign = -1;
	private EditText et_rejectedOpinion;// 驳回意见

	public SelectionDialog(Context context) 
	{
		super(context, R.style.Theme_Light_FullScreenDialogAct);
	}
	
	public SelectionDialog(Context context, int sign) 
	{
		super(context, R.style.Theme_Light_FullScreenDialogAct);
		this.sign = sign;
	}
	
	public SelectionDialog(Context context, String rejectedText, boolean flag) 
	{
		super(context, R.style.Theme_Light_FullScreenDialogAct);
		this.rejectedText = rejectedText;
		this.flag = flag;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.approval_dialog);
		initView();
		initListener();
	}

	private void initView() {
		iv_close = (ImageView) findViewById(R.id.iv_close);
		btnAgree = (Button) findViewById(R.id.btn_agree);
		btnRejected = (Button) findViewById(R.id.btn_rejected);
		
		btnConfirm = (Button) findViewById(R.id.btn_confirm);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		
		linearLayout1 = (LinearLayout) findViewById(R.id.linlayout1);
		linearLayout2 = (LinearLayout) findViewById(R.id.linlayout2);
		rellayout1 = (RelativeLayout) findViewById(R.id.rellayout1);
		
		et_rejectedOpinion = (EditText) findViewById(R.id.et_rejectedOpinion);
		
		if (flag == true)
		{
			btnRejected.setText(rejectedText);
			btnAgree.setVisibility(View.INVISIBLE);
			et_rejectedOpinion.setVisibility(View.VISIBLE);
		}
		
		if (sign == 1) 
		{
			linearLayout1.setVisibility(View.VISIBLE);
			linearLayout2.setVisibility(View.GONE);
			rellayout1.setVisibility(View.GONE);
		}
		
	}

	private void initListener() {
		
		// 关闭弹出框
		iv_close.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				dismiss();

				if (mOnCloseListener != null) 
				{
					mOnCloseListener.onClose(null);
				}
			}

		});

		// 驳回按钮
		btnRejected.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();

				if (mOnRejectedListener != null && flag == false) {
					mOnRejectedListener.OnRejected(null);
				}
				else if (mOnRejectedListener != null && flag == true) 
				{
					mOnRejectedListener.OnRejected(et_rejectedOpinion.getText().toString().trim());
				}
			}
		});

		// 同意按钮
		btnAgree.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();

				if (mOnAgreeListener != null && sign == -1) 
				{
					mOnAgreeListener.OnAgree(null);
				}
				else if (mOnAgreeListener != null && sign == 1)
				{
					
				}
			}
		});
		
		// 确认按钮
		btnConfirm.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dismiss();
				
				if (mOnAgreeListener != null && sign == 1) 
				{
					mOnAgreeListener.OnAgree(null);
				}
			}
		});
		
		// 取消按钮
		btnCancel.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dismiss();
			}
		});
	}

	/**
	 * 外部方法，设置同意监听器
	 * 
	 * @param dialogListener
	 */
	public void setOnAgreeListener(OnAgreeListener onAgreeListener) {
		this.mOnAgreeListener = onAgreeListener;
	}

	/**
	 * 外部方法，设置驳回监听器
	 * 
	 * @param dialogListener
	 */
	public void setOnRejectedListener(OnRejectedListener onRejectedListener) {
		this.mOnRejectedListener = onRejectedListener;
	}

	/**
	 * 外部方法，显示对话框
	 */
	@Override
	public void show() {
		// TODO Auto-generated method stub
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Point size = new Point();
		wm.getDefaultDisplay().getSize(size);

		super.show();
		getWindow().setLayout((int) (size.x * 9 / 10), LayoutParams.WRAP_CONTENT);
	}

}
