package com.xj.cnooc.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class GuideActivity extends Activity
{
	private ViewPager mViewPager;
	private Button btn_start;
//	private PagerTitleStrip mPagerTitleStrip;
//	private ImageView mPageImg;// 动画图片
	private int currIndex = 0;
	private ImageView mPage0;
	private ImageView mPage1;
	private ImageView mPage2;
	private ImageView mPage3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_view);
		// 初始化界面
		initView();
	}
	
	private void initView()
	{
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
//		mPagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pager_title);
		
//        mPageImg = (ImageView) findViewById(R.id.iv_page_now);    
        mPage0 = (ImageView)findViewById(R.id.iv_page0);
        mPage1 = (ImageView)findViewById(R.id.iv_page1);
        mPage2 = (ImageView)findViewById(R.id.iv_page2);
        mPage3 = (ImageView)findViewById(R.id.iv_page3);
        
        // ViewPager的滑动监听
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.guide_view1, null);
        View view2 = mLi.inflate(R.layout.guide_view2, null);
        View view3 = mLi.inflate(R.layout.guide_view3, null);
        View view4 = mLi.inflate(R.layout.guide_view4, null);
        
//        btn_start = (Button) view4.findViewById(R.id.btn_start);
        
//        btn_start.setOnClickListener(this);
        
        //每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        
//        final ArrayList<String> titles = new ArrayList<String>();
//        titles.add("①");
//        titles.add("②");
//        titles.add("③");
//        titles.add("④");
        
        //填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() 
        {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
//			@Override
//			public CharSequence getPageTitle(int position) {
////				return titles.get(position);
//			}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		mViewPager.setAdapter(mPagerAdapter);
	}
	
	 public class MyOnPageChangeListener implements OnPageChangeListener 
	 {
			//int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
			//int two = one * 2;// 页卡1 -> 页卡3 偏移量
	    	//int move = 20;

			@Override
			public void onPageSelected(int arg0)
			{
				Animation animation = null;
				switch (arg0) 
				{
					case 0:				
						mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
						mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
						if (currIndex == arg0 + 1)
						{
							animation = new TranslateAnimation(20*(arg0+1), 20*arg0, 0, 0);
						} 
						break;
					case 1:
						mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
						mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page));
						mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
						if (currIndex == arg0-1)
						{
							animation = new TranslateAnimation(20*(arg0-1), 20*arg0, 0, 0);	
							
						} 
						else if (currIndex == arg0 + 1) 
						{
							animation = new TranslateAnimation(20*(arg0+1), 20*arg0, 0, 0);
						}
						break;
					case 2:
						mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
						mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
						mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page));
						if (currIndex == arg0 - 1)
						{
							animation = new TranslateAnimation(20*(arg0-1), 20*arg0, 0, 0);
						} 
						else if (currIndex == arg0 + 1) 
						{
							animation = new TranslateAnimation(20*(arg0+1), 20*arg0, 0, 0);
						}
						break;
					case 3:
						mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
						mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
						if (currIndex == arg0 - 1)
						{
							animation = new TranslateAnimation(20*(arg0-1), 20*arg0, 0, 0);
							
						} 
						else if (currIndex == arg0 + 1)
						{
							animation = new TranslateAnimation(20*(arg0+1), 20*arg0, 0, 0);
						}
						break;
				}
				currIndex = arg0;
				animation.setFillAfter(true);// True:图片停在动画结束位置
				animation.setDuration(300);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) 
			{
				
			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				
			}
		}

	   public void startbutton(View v) 
	   {  
	      	Intent intent = new Intent();
			intent.setClass(GuideActivity.this, LoginActivity.class);
			GuideActivity.this.startActivity(intent);
			System.gc();
			GuideActivity.this.finish();
	   }

//	@Override
//	public void onClick(View v) 
//	{
//		Intent intent = new Intent();
//		intent.setClass(GuideActivity.this, LoginActivity.class);
//		startActivity(intent);
//		this.finish();
//	}

}
