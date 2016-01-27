package com.xj.dms.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.dms.view.MoreActivity;
import com.xj.dms.view.R;
/**
 * Activity基类
 * @author qinfan
 *
 * 2015-10-30
 */
public class BaseActivity extends FragmentActivity {
	private TextView tv_left_title,tv_center_title;  
	private ImageView btn_back, btn_more;  

	private LinearLayout ll_content;  
	private RelativeLayout tl_title;
	// 内容区域的布局  
	private View contentView;  

	private Toast mToast;
	private MyApp myApp = null;
	public static final String[] TAG = {"EXIT_USER_LOGIN_OR_APP","JUDGE_NET_CONN"};

	@Override 
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.public_title_view); 

		// 每打开一个activity界面，就将此activity添加到集合中
		if (myApp == null) {
			myApp = MyApp.getInstance();
		}
		myApp.contextList.add(this);

		ll_content = (LinearLayout) findViewById(R.id.ll_content);  
		tl_title = (RelativeLayout) findViewById(R.id.rl_title);
		tv_left_title = (TextView) findViewById(R.id.tv_left_title);
		tv_center_title = (TextView) findViewById(R.id.tv_center_title);
		btn_back = (ImageView) findViewById(R.id.iv_back);
		btn_more = (ImageView)findViewById(R.id.btn_more);

		btn_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);  
				intent.setClass(BaseActivity.this, MoreActivity.class);
				startActivity(intent);

			}
		});

	} 

	/*** 
	 * 设置内容区域 
	 *  
	 * @param resId 
	 * 资源文件ID 
	 */  
	public void setContentLayout(int resId) {  

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		contentView = inflater.inflate(resId, null);  
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,  
				LayoutParams.MATCH_PARENT);  
		contentView.setLayoutParams(layoutParams);  
		if (null != ll_content) {  
			ll_content.addView(contentView);  
		}  
	}  

	/** 
	 * 隐藏上方的标题栏 
	 */  
	public void hideTitleView() {  

		if (null != tl_title) {  
			tl_title.setVisibility(View.GONE);  
		}  
	}  

	/*** 
	 * 设置内容区域 
	 *  
	 * @param view 
	 *            View对象 
	 */  
	public void setContentLayout(View view) {  
		if (null != ll_content) {  
			ll_content.addView(view);  
		}  
	}  

	/** 
	 * 得到内容的View 
	 *  
	 * @return 
	 */  
	public View getLyContentView() {  

		return contentView;  
	}  

	/** 
	 * 得到返回按钮 
	 *  
	 * @return 
	 */  
	public ImageView getbtn_back() {  
		return btn_back;  
	}  

	/** 
	 * 得到更多按钮 
	 *  
	 * @return 
	 */  
	public ImageView getbtn_more() {  
		return btn_more;  
	}  

	/** 
	 * 设置左边标题 
	 *  
	 * @param title 
	 */  
	public void setLeftTitle(String title) {  

		if (null != tv_left_title) {  
			tv_left_title.setText(title);  
		}  
	}  

	/** 
	 * 设置中间标题 
	 *  
	 * @param title 
	 */  
	public void setCenterTitle(String title) {  

		if (null != tv_center_title) {  
			tv_center_title.setText(title);  
		}  
	}  

	/** 
	 * 隐藏左边的按钮 
	 */  
	public void hidebtn_back() {  

		if (null != btn_back) {  
			btn_back.setVisibility(View.GONE);  
		}  
	}  

	/*** 
	 * 隐藏右边的按钮 
	 */  
	public void hidebtn_more() {
		if (null != btn_more) {  
			btn_more.setVisibility(View.GONE);  
		}  
	}

	/*** 
	 * 隐藏左边标题
	 */  
	public void hidetv_left_title() {  
		if (null != tv_left_title) {  
			tv_left_title.setVisibility(View.GONE);  
		}  
	}

	/*** 
	 * 隐藏中间标题
	 */  
	public void hidetv_center_title() {  
		if (null != tv_center_title) {  
			tv_center_title.setVisibility(View.GONE);  
		}  
	}

	public void showToast(CharSequence s){
		if (mToast == null) {
			mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
		}
		mToast.setText(s);
		mToast.show();
	}

	
}
