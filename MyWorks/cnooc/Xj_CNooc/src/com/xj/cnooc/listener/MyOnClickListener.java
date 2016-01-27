package com.xj.cnooc.listener;

import android.support.v4.view.ViewPager;
import android.view.View;

/** 
 * 头标点击监听 
 */  
public class MyOnClickListener implements View.OnClickListener {  
	private int index = 0;  
	private ViewPager viewPager;

	public MyOnClickListener(int i,ViewPager viewPager) {  
		index = i;  
		this.viewPager=viewPager;
	}  

	@Override  
	public void onClick(View v) {  
		viewPager.setCurrentItem(index);  
	}  
};  