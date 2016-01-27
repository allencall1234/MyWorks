package com.xj.cnooc.common;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;

import com.xj.cnooc.view.R;

/**
 * 自定义弹出窗口，用于ListView的长按事件处理。 默认弹出窗口只支持删除和取消事件
 * 
 * @author zhulanting 2015/9/23
 *
 */
public abstract class CustomPopupWindow implements OnClickListener {
	private PopupWindow mWindow;

	public CustomPopupWindow(Context context, View view) {

		View mainView = LayoutInflater.from(context).inflate(
				R.layout.layout_dialog, null);
		View leftView = mainView.findViewById(R.id.tv_cancel);
		View rightView = mainView.findViewById(R.id.tv_delete);

		leftView.setOnClickListener(this);
		rightView.setOnClickListener(this);

		mWindow = new PopupWindow(mainView,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
		mWindow.setOutsideTouchable(true);
		mWindow.setBackgroundDrawable(new ColorDrawable(0));
		mWindow.setTouchable(true);

		mWindow.showAsDropDown(view, view.getWidth() / 2 - 40,
				-40 - view.getHeight());
	}

	public abstract void onLeftClick();

	public abstract void onRightClick();

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_cancel:
			onLeftClick();
			break;
		case R.id.tv_delete:
			onRightClick();
		default:
			break;
		}
		mWindow.dismiss();
	}
}
