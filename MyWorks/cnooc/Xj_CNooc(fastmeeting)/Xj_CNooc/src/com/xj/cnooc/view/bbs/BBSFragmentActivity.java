package com.xj.cnooc.view.bbs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.xj.cnooc.adapter.BBSTabPageIndicatorAdapter;
import com.xj.cnooc.common.BaseFragmentActivity;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.listener.MyOnClickListener;
import com.xj.cnooc.view.R;

/**
 * 
 * @author Administrator 三个滑动界面：帖子、已评论帖、我的收藏
 *
 */
public class BBSFragmentActivity extends BaseFragmentActivity implements
		OnClickListener {
	private static final String[] TITLE = new String[] { "帖子", "已评论贴", "我的收藏" };

	private ViewPager viewPager = null;// 页卡内容
	private TextView tv_tiezi;// 帖子
	private TextView tv_commented;// 已评论贴
	private TextView tv_myCollection;// 我的收藏

	private ImageView iv_cursor;// 动画图片

	// 公共标题组件
	// private Button btn_back;// 返回按钮
	// private RelativeLayout rellayout_back;
	// private RelativeLayout rellayout_setting;
	private ImageView iv_back;
	private ImageView iv_setting;
	private TextView tv_center_title;// 标题
	// private Button btn_setting;// 设置按钮

	private int currIndex = 0;// 当前页卡编号
	private int screeWidth;// 屏幕宽度

	private MyApp myApp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_view);

		// 每打开一个activity界面，就将此activity添加到集合中
		if (myApp == null) {
			// myApp = (MyApp) this.getApplicationContext();
			myApp = MyApp.getInstance();
		}
		myApp.contextList.add(this);

		initView();// 绑定界面UI

		initAnimation();// 初始化动画

		initViewPager();// 初始化viewpager
	}

	private void initView() {
		tv_tiezi = (TextView) findViewById(R.id.bbs_tv_tiezi);
		tv_commented = (TextView) findViewById(R.id.bbs_tv_commented);
		tv_myCollection = (TextView) findViewById(R.id.bbs_tv_myCollection);

		// btn_back = (Button) findViewById(R.id.btn_back);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_setting = (ImageView) findViewById(R.id.iv_setting);
		tv_center_title = (TextView) findViewById(R.id.tv_center_title);
		// btn_setting = (Button) findViewById(R.id.btn_setting);

		viewPager = (ViewPager) findViewById(R.id.bbs_viewpager);
		// viewPager.setPageTransformer(true, new
		// TransformEffect(TransitionEffect.RotateDown));
		tv_center_title.setText("论坛交流");

		// btn_back.setOnClickListener(this);
		// btn_setting.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		iv_setting.setOnClickListener(this);

		tv_tiezi.setOnClickListener(new MyOnClickListener(0, viewPager));
		tv_commented.setOnClickListener(new MyOnClickListener(1, viewPager));
		tv_myCollection.setOnClickListener(new MyOnClickListener(2, viewPager));

	}

	private void initAnimation() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screeWidth = dm.widthPixels;
		iv_cursor = (ImageView) findViewById(R.id.iv_cursor);
		LayoutParams lp = new LayoutParams(screeWidth / 3, 5);
		iv_cursor.setLayoutParams(lp);
		iv_cursor.setBackgroundColor(Color.parseColor("#0099ff"));
	}

	private void initViewPager() {
		FragmentPagerAdapter adapter = new BBSTabPageIndicatorAdapter(
				getSupportFragmentManager(), TITLE);

		// 为viewpager加载适配器
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(2);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		int one = screeWidth / 3;// // 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int index) {
			Animation animation = null;
			switch (index) {
			case 0:
				changColor(tv_tiezi);
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;

			case 1:
				changColor(tv_commented);
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;

			case 2:
				changColor(tv_myCollection);
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;

			}
			currIndex = index;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			iv_cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

	}

	// /**
	// * 头标点击监听
	// */
	// public class MyOnClickListener implements View.OnClickListener
	// {
	// private int index = 0;
	//
	// public MyOnClickListener(int i)
	// {
	// index = i;
	// }
	//
	// @Override
	// public void onClick(View v)
	// {
	// viewPager.setCurrentItem(index);
	// }
	// }

	// 按钮事件监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.iv_setting:
			showSettingActivity();
			break;
		// case R.id.btn_back:
		// this.setResult(0x05);
		// finish();
		// break;
		// case R.id.btn_setting:
		// startActivity(new Intent(BBSFragmentActivity.this,
		// SettingActivity.class));
		// break;
		}
	}

	// 改变字体颜色
	public void changColor(TextView textView) {
		tv_tiezi.setTextColor(Color.parseColor("#000000"));
		tv_commented.setTextColor(Color.parseColor("#000000"));
		tv_myCollection.setTextColor(Color.parseColor("#000000"));
		textView.setTextColor(Color.parseColor("#0099ff"));
	}
}
