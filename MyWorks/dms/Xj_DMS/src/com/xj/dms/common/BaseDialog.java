package com.xj.dms.common;

import android.app.Dialog;
import android.content.Context;

public class BaseDialog extends Dialog {

	public BaseDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 监听驳回
	 */
	public abstract interface OnRejectedListener {

		public abstract void  OnRejected(String result);
	}

	/**
	 * 监听同意
	 */
	public abstract interface OnAgreeListener {

		public abstract void OnAgree(String result);
	}
	
	/**
	 * 监听删除
	 */
	public abstract interface OnCloseListener {

		public abstract void onClose(String result);
	}

}
