package com.xj_pipe.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.xj_pipe.adapter.MenuGridViewAdapter;
import com.xj_pipe.base.BaseActivity;
import com.xj_pipe.base.MyApp;
import com.xj_pipe.bean.MainNumBean;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;

/**
 * 主功能界面
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends BaseActivity {

	private String[] itemData;
	private List<Intent> intentList;

	private GridView gridView;
	private MenuGridViewAdapter myAdapter;

	private MainNumBean mainNumBean = new MainNumBean();

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				UserHttp.getMainDot(MyApp.userInfo.getStaffId(),
						new HttpDataCallBack() {

							@Override
							public void HttpSuccess(String _result) {
								Log.i("result", "" + _result);
								try {
									JSONObject jsonObject = new JSONObject(
											_result);
									if (jsonObject.getBoolean("status")) {
										int unMessageNum = jsonObject
												.getInt("unMessageNum");// 未读消息数量
										mainNumBean.setMsgCount(""
												+ unMessageNum);

										int emergencyTaskCount = jsonObject
												.getInt("emergencyTaskCount");// 应急任务待处理数量
										
										mainNumBean.setYjCount(""
												+ emergencyTaskCount);

										int taskCount = jsonObject
												.getInt("taskCount");// 例行任务待处理数量
										mainNumBean.setLxCount("" + taskCount);
									}
									myAdapter = new MenuGridViewAdapter(
											MainActivity.this, 0, itemData,
											mainNumBean);
									gridView.setAdapter(myAdapter);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void HttpFail(int ErrCode) {
								// Toast.makeText(MyApp.instance, ErrCode,
								// 1).show();
							}
						});
				break;
			}
		};
	};
	private long mExitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_main);
		addData();
		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getMain();
	}

	private void initViews() {

		hideLeftTitle();
		
		itemData = getResources().getStringArray(R.array.item_main);
		gridView = (GridView) findViewById(R.id.main_menu_gridView);
		gridView.setOnItemClickListener(new OnItemClick());
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		myAdapter = new MenuGridViewAdapter(this, 0, itemData, mainNumBean);
		gridView.setAdapter(myAdapter);
	}

	private void addData() {
		intentList = new ArrayList<Intent>();
		intentList.add(new Intent(this, ActivityInspection.class));
		intentList.add(new Intent(this, ActivityInspection.class));
		intentList.add(new Intent(this, ActivityRepair.class));
		intentList.add(new Intent(this, ActivityEmergency.class));
		intentList.add(new Intent(this, ActivityFeedBack.class));
		intentList.add(new Intent(this, MessageActivity.class));
	}

	private void getMain() {
		handler.sendEmptyMessage(1);
	}

	class OnItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = intentList.get(arg2);
			// 当指定的Intent是巡检任务(顺序0为例行巡检任务,1为应急巡检任务)时,需要传递参数taskType进行识别
			// 例行巡检taskType = 1,应急巡检taskType = 2
			if (arg2 == 0 || arg2 == 1) {
				intent.putExtra("taskType", arg2 + 1);
			}
			if (arg2 >=2 && arg2 <= 4) {
				MyApp.TYPE = arg2 - 2;
			}
			startActivity(intent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// TODO Auto-generated method stub
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				showToast("再按一次退出应用");
				mExitTime = System.currentTimeMillis();
			} else {
				MyApp.exitApplication();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
