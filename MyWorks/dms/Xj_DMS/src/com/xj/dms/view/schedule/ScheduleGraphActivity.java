package com.xj.dms.view.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.xj.dms.adapter.ViewPagerAdapter;
import com.xj.dms.common.BaseActivity;
import com.xj.dms.https.HttpDataCallBack;
import com.xj.dms.https.UserHttp;
import com.xj.dms.view.R;

public class ScheduleGraphActivity extends BaseActivity implements
		OnClickListener {
	public final static String TAG = "ScheduleGraphActivity。class";

	public final static int MONTH_STATISTICS = 0;
	public final static int TYPE_STATISTICS = 1;

	private ViewPager monthPager = null;
	private ViewPager typePager = null;

	private List<GraphView> monthList = new ArrayList<GraphView>();
	private List<GraphView> typeList = new ArrayList<GraphView>();

	private int num = 10;// 每页加载的数据数量
	private int curragePage = 1;// 当前页
	private JSONObject result;

	private int[][] monthArray;
	private int[][] typeArray;
	private int length;
	private int totalFlag; // 总计标示位,在获取的JSON数组中,得到的JSONArray对象顺序不固定。totalFlag用于标示"total"对应数组的位置

	private String[] titles;

	private String[] monthStr;
	private String[] typeStr;

	private RelativeLayout leftLayout, rightLayout;

	private ViewPagerAdapter<GraphView> monthAdapter;
	private ViewPagerAdapter<GraphView> typeAdapter;

	private TextView titleText;

	private RelativeLayout bottonContainer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.graph_layout);

		monthStr = getResources().getStringArray(R.array.month_titles);
		typeStr = getResources().getStringArray(R.array.task_types);

		initView();

		handler.sendEmptyMessage(0);

		monthAdapter = new ViewPagerAdapter<GraphView>(monthList);
		typeAdapter = new ViewPagerAdapter<GraphView>(typeList);

		monthPager = (ViewPager) findViewById(R.id.month_pager);
		monthPager.setCurrentItem(0);
		monthPager.setAdapter(monthAdapter);

		typePager = (ViewPager) findViewById(R.id.type_pager);
		typePager.setCurrentItem(0);
		typePager.setAdapter(typeAdapter);

		titleText = (TextView) findViewById(R.id.title_text);

		bottonContainer = (RelativeLayout) findViewById(R.id.bottomContainer);

		leftLayout = (RelativeLayout) findViewById(R.id.radio_left_layout);
		rightLayout = (RelativeLayout) findViewById(R.id.radio_right_layout);

		leftLayout.setSelected(true);

		leftLayout.setOnClickListener(this);
		rightLayout.setOnClickListener(this);

	}

	private void initView() {
		getbtn_back().setOnClickListener(this);
	}

	private void initTitleText() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int total = 0;
		for (int i = 0; i < monthArray[0].length; i++) {
			total += monthArray[0][i];
		}

		String title = String.format(
				getResources().getString(R.string.task_statistics_title), year,
				total);

		titleText.setText(title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.radio_left_layout:
			leftLayout.setSelected(true);
			rightLayout.setSelected(false);
			monthPager.setVisibility(View.VISIBLE);
			typePager.setVisibility(View.GONE);
			break;
		case R.id.radio_right_layout:
			leftLayout.setSelected(false);
			rightLayout.setSelected(true);
			monthPager.setVisibility(View.GONE);
			typePager.setVisibility(View.VISIBLE);
			break;
		case R.id.iv_back:// 返回
			finish();
			break;
		}
	}

	private void initListViews() {
		monthList.clear();
		typeList.clear();

		for (int i = 0; i < monthArray[0].length; i++) {
			monthList.add(new GraphView(this, monthStr[i], getData(
					MONTH_STATISTICS, i)));
		}

		for (int j = 0; j < typeArray[0].length; j++) {
			typeList.add(new GraphView(this, typeStr[j], getData(
					TYPE_STATISTICS, j)));
		}

		monthAdapter.notifyDataSetChanged();
	}

	private int[] getData(int type, int index) {
		int[] data = new int[length];

		for (int i = 0; i < length; i++) {

			data[i] = type == MONTH_STATISTICS ? monthArray[(i + 1 + totalFlag)
					% length][index]
					: typeArray[(i + 1 + totalFlag) % length][index];
		}

		return data;
	}
	
	private String[] sortTitle(){
		String[] data = new String[length];
		
		for (int i = 0; i < length; i++) {
			data[i] = titles[(i+1+totalFlag)%length];
		}
		return data;
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				UserHttp.taskStatisticalQuery(curragePage, num, "",
						new HttpDataCallBack() {
							@Override
							public void HttpSuccess(String _result) {
								getTaskCountListData(_result);
								initListViews();
								initTitleText();
								initBottomView();
							}

							@Override
							public void HttpFail(int ErrCode) {

							}
						});
				break;
			default:
				break;
			}
		};
	};

	private void initBottomView() {
		// TODO Auto-generated method stub
		
		bottonContainer.addView(new GraphBottomView(this, sortTitle()),
				new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
	}

	private void getTaskCountListData(String _result) {
		try {
			result = new JSONObject(_result);
			Log.i("length", "length = " + result.length());
			length = result.length();
			length = Math.max(length, 1);
			titles = new String[length];
			monthArray = new int[length][12];
			typeArray = new int[length][7];

			Iterator<?> it = result.keys();
			int index = 0;
			while (it.hasNext()) {
				// 迭代获取JSON对象里的每个JSON数组名称
				String name = it.next().toString();
				if ("total".equals(name)) {
					totalFlag = index;
				}
				titles[index] = name;
				// 通过数组名获取对应JSONArray的内容
				JSONArray jArray = result.getJSONArray(name);
				// 因为我们这里不是把每个JSONArray横向放到二维数组中,而是纵向放入.放入过程需要分开截图JSONArray的内容,放入到两个对应的二维数组中
				// 每个JSONArray的第一位是字符串,不要这个，所以从第一位开始。
				for (int i = 1; i < jArray.length(); i++) {
					if (i <= 12) {
						monthArray[index][i - 1] = jArray.getInt(i);
					} else {
						typeArray[index][i - 13] = jArray.getInt(i);
					}
				}
				index++;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
