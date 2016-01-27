package com.xj_pipe.view;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xj_pipe.base.MyApp;
import com.xj_pipe.common.ZoomImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

/**
 * 展示图片界面
 * 
 * @author Administrator
 * 
 */
public class PhotoActivity extends Activity {

	private ArrayList<String> list = null;
	private int position;

	private ViewPager viewPager = null;
	private TextView textView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photo_layout);
		Intent intent = getIntent();
		if (intent != null) {
			position = intent.getIntExtra("position", 0); // 获取当前页图片的页码
			list = intent.getStringArrayListExtra("bitmaps"); // 获取所有图片的路径
		}

		textView = (TextView) findViewById(R.id.view_count);
		textView.setText((position + 1) + "/" + list.size());

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		// viewPager.setAlwaysDrawnWithCacheEnabled(false);
		viewPager.setAdapter(new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// TODO Auto-generated method stub
				ZoomImageView imageView = new ZoomImageView(PhotoActivity.this);
				ImageLoader.getInstance().displayImage(list.get(position),
						imageView,MyApp.getDisplayOptions());
				container.addView(imageView);

				return imageView;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
				// Drawable drawable = images[position].getDrawable();
				// if (drawable != null && drawable instanceof BitmapDrawable) {
				// BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
				// Bitmap bitmap = bitmapDrawable.getBitmap();
				// if (bitmap != null && !bitmap.isRecycled()) {
				// bitmap.recycle();
				// }
				// }

				container.removeView((View) object);
			}
		});

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				textView.setText(String.format("%1$s/%2$s", arg0 + 1,
						list.size()));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		viewPager.setCurrentItem(position);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// for (int i = 0; i < total; i++) {
		// if (imageViews.get(i) == null) return;
		// Drawable drawable = imageViews.get(i).getDrawable();
		// if (drawable != null && drawable instanceof BitmapDrawable) {
		// BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		// Bitmap bitmap = bitmapDrawable.getBitmap();
		// if (bitmap != null && !bitmap.isRecycled()) {
		// bitmap.recycle();
		// }
		// }
		// }
		super.onDestroy();
	}
}
