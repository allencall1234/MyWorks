package com.xj.dms.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.dms.view.MainActivity;
import com.xj.dms.view.R;
/**
 * 自定义弹出框
 * @author qinfan
 *
 * 2015-11-6
 */
public class MyDialog implements OnClickListener{
	private Activity activity;
	private AlertDialog dialog;
	private String title;
	private String tag;

	public MyDialog(Activity activity,String title,String tag) {
		this.activity = activity;
		this.title = title;
		this.tag = tag;
	}

	public void showDialog(){
		dialog=new AlertDialog.Builder(activity).create();
		//点击外部区域不能取消dialog 
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener(keylistener);
		dialog.show();

		Window window = dialog.getWindow();
		
		if(tag == BaseActivity.TAG[0]){
			window.setContentView(R.layout.my_dialog);
			TextView tv_title = (TextView) window.findViewById(R.id.dialog_title);
			TextView tv_confirm = (TextView) window.findViewById(R.id.tv_confirm);
			TextView tv_cancel = (TextView) window.findViewById(R.id.tv_cancel);
			
			tv_title.setText(title);
			tv_confirm.setOnClickListener(this);
			tv_cancel.setOnClickListener(this);
		}
		
		if(tag == BaseActivity.TAG[1]){
			window.setContentView(R.layout.judge_net_dialog);
			Button dialog_btn_confirm = (Button) window.findViewById(R.id.dialog_btn_confirm);
			dialog_btn_confirm.setOnClickListener(this);
		}

		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_confirm:
			if(title.equals("确定退出系统")){
				activity.finish();
				dialog.dismiss();
				MyApp.exitActivities();
				System.exit(0);
			}
			if(title.equals("确定退出用户登录")){
				dialog.dismiss();
				MyApp.isLogin = false;
				Intent intent = new Intent();
				intent.setClass(activity, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				activity.startActivity(intent);
			}
			break;
			
		case R.id.tv_cancel:
			dialog.dismiss();
			break;
			
		case R.id.dialog_btn_confirm:
			activity.finish();
			break;
		}
	}

	public static OnKeyListener keylistener = 
			new DialogInterface.OnKeyListener(){
				public boolean onKey(
						DialogInterface dialog, 
						int keyCode,
						KeyEvent event) 
						{
							if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0)
							{
								return true;
							}
							else
							{
								return false;
							}
						}
				} ;
}
