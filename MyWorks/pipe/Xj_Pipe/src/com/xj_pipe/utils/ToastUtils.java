package com.xj_pipe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 弹出框工具类
 * @author Administrator
 *
 */
public class ToastUtils 
{
	/**
	 * 弹出消息
	 * 
	 * @param context
	 *            上下文对象
	 * @param message
	 *            消息
	 */
	public static void ShowMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

	}

	/**
	 * 弹出消息
	 * 
	 * @param context
	 *            上下文对象
	 * @param resourceId
	 *            资源地址
	 */
	public static void ShowMessage(Context context, int resourceId) {
		String message = context.getResources().getString(resourceId);
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

	}


}
