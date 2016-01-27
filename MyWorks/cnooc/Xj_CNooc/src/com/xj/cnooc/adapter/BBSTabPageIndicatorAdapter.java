package com.xj.cnooc.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xj.cnooc.view.bbs.BBSFragment;
import com.xj.cnooc.view.bbs.CommentedPostFragment;
import com.xj.cnooc.view.bbs.MyCollectionFragment;

/**
 * ViewPager适配器
 * 滑动页面加载不同的fragment
 * @author len
 *
 */
public class BBSTabPageIndicatorAdapter extends FragmentPagerAdapter {
	String[] TITLE;
	public BBSTabPageIndicatorAdapter(FragmentManager fm,String[] TITLE) {
		super(fm);
		this.TITLE=TITLE;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment=null;
		Bundle args;
		switch (position) {
		case 0:
				fragment = new BBSFragment();
				args = new Bundle();
				args.putString("arg", TITLE[position]); 
				fragment.setArguments(args); 
			break;
		case 1:
				fragment = new CommentedPostFragment();  
				args = new Bundle();  
				args.putString("arg", TITLE[position]);  
				fragment.setArguments(args); 
			break;
		case 2:
				fragment = new MyCollectionFragment();  
				args = new Bundle();  
				args.putString("arg", TITLE[position]);  
				fragment.setArguments(args); 
			break;
		}
		return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLE[position % TITLE.length];
	}

	@Override
	public int getCount() {
		return TITLE.length;
	}
}