package com.xj.cnooc.view.support;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xj.cnooc.common.BaseFragmentActivity;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.listener.MyOnClickListener;
import com.xj.cnooc.view.R;

/**
 * 故障支持界面
 * 
 * @author qinfan 2015-9-6
 */
public class FaultEvaluateActivity extends BaseFragmentActivity implements
		OnClickListener {
	private static final String[] TITLE = new String[] { "故障评估列表", "已审批列表" };
	private ViewPager viewPager;// 页卡内容
	private ImageView cursor;// 动画图片
	private RelativeLayout fault_support_text_layout,
			finished_fault_support_text_layout;
	@SuppressWarnings("unused")
	private TextView fault_support_text, finished_fault_support_text;
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private TextView title;
	// private Button btn_back;
	// private Button btn_setting;
	// private RelativeLayout rellayout_back;
	// private RelativeLayout rellayout_setting;
	private ImageView iv_back;
	private ImageView iv_setting;

	private MyApp myApp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initData();

		// 每打开一个activity界面，就将此activity添加到集合中
		if (myApp == null) {
			myApp = (MyApp) this.getApplicationContext();
		}
		myApp.contextList.add(this);
	}

	protected void initData() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
		setContentView(R.layout.activity_fault_support);
		Intent intent = getIntent();
		String supportType = intent.getStringExtra("supportType");

		viewPager = (ViewPager) findViewById(R.id.viewPager);

		fault_support_text_layout = (RelativeLayout) findViewById(R.id.fault_support_text_layout);
		finished_fault_support_text_layout = (RelativeLayout) findViewById(R.id.finished_fault_support_text_layout);

		fault_support_text = (TextView) findViewById(R.id.fault_support_text);
		fault_support_text.setTextColor(Color.parseColor("#0099ff"));
		finished_fault_support_text = (TextView) findViewById(R.id.finished_fault_support_text);

		fault_support_text_layout.setOnClickListener(new MyOnClickListener(0,
				viewPager));
		finished_fault_support_text_layout
				.setOnClickListener(new MyOnClickListener(1, viewPager));

		title = (TextView) findViewById(R.id.tv_center_title);

		title.setText("电网评估支持");
		fault_support_text.setText("评估支持列表");

		// btn_back = (Button)findViewById(R.id.btn_back);
		// btn_back.setOnClickListener(this);

		// btn_setting = (Button)findViewById(R.id.btn_setting);
		// btn_setting.setOnClickListener(this);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_setting = (ImageView) findViewById(R.id.iv_setting);

		iv_back.setOnClickListener(this);
		iv_setting.setOnClickListener(this);

		InitImageView();

		FragmentPagerAdapter adapter = new TabPageAdapter(
				getSupportFragmentManager(), TITLE);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fault_support, menu);
		return true;
	}

	/**
	 * 初始化动画
	 */
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.aa)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				changColor(fault_support_text);
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				changColor(finished_fault_support_text);
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			}
			//
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	class TabPageAdapter extends FragmentPagerAdapter {
		public TabPageAdapter(FragmentManager fm, String[] TITLE) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			Bundle args;
			switch (position) {
			case 0:
				fragment = new FaultEvaluateFragment();
				args = new Bundle();
				args.putString("arg", TITLE[position]);
				fragment.setArguments(args);
				break;
			case 1:
				fragment = new FinishEvaluateFragment();
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

	// 改变字体颜色
	public void changColor(TextView textView) {
		fault_support_text.setTextColor(Color.parseColor("#000000"));
		finished_fault_support_text.setTextColor(Color.parseColor("#000000"));
		textView.setTextColor(Color.parseColor("#0099ff"));
	}

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
		// this.setResult(0x02);
		// this.finish();
		// break;
		//
		// case R.id.btn_setting:
		// Intent intent=new Intent();
		// intent.setClass(FaultEvaluateActivity.this, SettingActivity.class);
		// startActivity(intent);
		// break;

		default:
			break;
		}
	}

}
