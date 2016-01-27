package com.xj.dms.common;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

public class MyPagerOnClickListener implements OnClickListener
{
	private int index = 0;  
	private ViewPager viewPager;

	public MyPagerOnClickListener(int index,ViewPager viewPager) 
	{  
		this.index = index;  
		this.viewPager = viewPager;
	}  

	@Override
	public void onClick(View v) 
	{
		viewPager.setCurrentItem(index);  
	}

}
