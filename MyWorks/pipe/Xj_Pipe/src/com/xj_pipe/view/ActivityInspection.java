package com.xj_pipe.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xj_pipe.adapter.CommonAdapter;
import com.xj_pipe.adapter.ViewHolder;
import com.xj_pipe.base.MyApp;
import com.xj_pipe.bean.InspectionBean;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;
import com.xj_pipe.utils.JsonUtils;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 例行巡检和应急巡检界面 根据传入的数值taskType进行判断 taskType = 1表示是例行巡检;taskType=2表示应急巡检
 * 
 * @author Administrator
 * 
 */
public class ActivityInspection extends SubActivity {

	private int totalNum = -1;;// 从数据库中获取的数据总数
	private int num = 20;// 每页加载的数据数量
	private BaseAdapter mAdapter;
	private List<InspectionBean> list;
	private int inspectionState = 0;// 任务状态标识
	private PopupWindow pWindow;

	private TextView menu_forChecking;// 待巡检
	private TextView menu_inInspection;// 巡检中
	private TextView menu_hasInspection;// 已巡检

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/*
		 * fromService参数是从服务传递过来的，如果isShow = true，说明该Activity是通过InspectionService启动的
		 * 而InspectionService启动Activity的前提是检查到了新任务
		 */
		boolean isShow = getIntent().getBooleanExtra("fromService", false);
		if (isShow) {
			MyApp.isShow = false;
		}
	}
	
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_PAGE_INSPECTION;
	}

	@Override
	public BaseAdapter getAdapter() {
		// TODO Auto-generated method stub
		if (mAdapter == null) {
			mAdapter = new CommonAdapter<InspectionBean>(
					ActivityInspection.this, list,
					R.layout.item_emergency_inspection) {

				@Override
				public void convert(ViewHolder viewHolder, InspectionBean item) {
					// TODO Auto-generated method stub
					int state = item.getState();
					
					// 巡检未超时
					if (item.getDelayState().equals("0"))
					{
						viewHolder.setText(R.id.tv_inspectionState,
						textStates[state]);
						viewHolder.setTextBackground(R.id.tv_inspectionState,
								drawableIds[state]);
					}
					else
					{
						viewHolder.setText(R.id.tv_inspectionState,
								textStates[state] + "超时");
						viewHolder.setTextBackground(R.id.tv_inspectionState,
								R.drawable.state3);
					}
					viewHolder.setText(R.id.tv_title, item.getName());
					viewHolder.setText(R.id.tv_inspectionLine,
							item.getPointSetName());
					viewHolder.setText(
							R.id.tv_inspectionDate,
							item.getPlanStartTime() + "-"
									+ item.getPlanEndTime());
				}
			};
		}
		return mAdapter;
	}

	@Override
	public int getTotalNumber() {
		// TODO Auto-generated method stub
		return totalNum;
	}

	@Override
	public void initListData(final int page) {
		// TODO Auto-generated method stub
//		Log.i("zlt", "type = " + type + ",inspectionState = " + inspectionState);
		if (list == null) {
			list = new ArrayList<InspectionBean>();
		}
		// page值为1时，表明巡检状态可能发生了变化,所以数据需要进行初始化。当适配器mAapter非空时,表明有数据,所以需要通知列表刷新
		if (page == 1) {
			list.clear();
			if (mAdapter != null) { // 一定要做通知刷新处理,否则可能会报错
				mAdapter.notifyDataSetChanged();
			}
		}
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				UserHttp.routineInspectionTaskQuery("asc", page, num, "id",
						MyApp.userInfo.getStaffId(),
						String.valueOf(inspectionState), type + 1,
						new HttpDataCallBack() {

							@Override
							public void HttpSuccess(String _result) {
								getTaskListListData(_result);
								
								if (inspectionState == 0 && type == 1 ) {
									MyApp.localUnDealMsg = totalNum;
								}
								mHandler.obtainMessage(MESSAGE_SUCCESS)
										.sendToTarget();
							}

							@Override
							public void HttpFail(int ErrCode) {
								mHandler.obtainMessage(MESSAGE_FAILED)
										.sendToTarget();
							}
						});
			}
		});
	}

	/**
	 * 将Json格式数据格式化到taskList列表中
	 * 
	 * @param result
	 * @return
	 */
	private List<InspectionBean> getTaskListListData(String result) {
		try {
			JSONObject object = new JSONObject(result);

			totalNum = object.getInt("total");
			JSONArray array = object.getJSONArray("taskList");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				InspectionBean bean = (InspectionBean) JsonUtils
						.putJsonToObject(object2,
								"com.xj_pipe.bean.InspectionBean");
				
				if (bean.getState()<=2)
				{
					list.add(bean);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public void onListViewItemClick(int position) {
		// TODO Auto-generated method stub
		InspectionBean inspection_bean = list.get(position - 1);

		Intent intent = new Intent(ActivityInspection.this,
				InspectionMainActivity.class);

		Bundle bundle = new Bundle();
		bundle.putSerializable("InspectionBean", inspection_bean);
		intent.putExtras(bundle);
		intent.putExtra("taskType", type + 1);
		startActivity(intent);
	}

	@Override
	public void onMoreLisenter() {
		// TODO Auto-generated method stub
		if (pWindow == null) { // 当pWindow不存在时，创建popupWindow
			LayoutInflater inflater = LayoutInflater
					.from(ActivityInspection.this);
			View menu = inflater.inflate(
					R.layout.activity_inspection_state_menu, null);
			menu.setFocusableInTouchMode(true);
			menu.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (keyCode == KeyEvent.KEYCODE_MENU
							&& event.getAction() == KeyEvent.ACTION_DOWN
							&& pWindow.isShowing()) {
						pWindow.dismiss();
						return true;
					}
					return false;
				}
			});
			/** 待巡检 */
			menu_forChecking = (TextView) menu
					.findViewById(R.id.menu_forChecking);
			/** 巡检中 */
			menu_inInspection = (TextView) menu
					.findViewById(R.id.menu_inInspection);
			/** 已巡检 */
			menu_hasInspection = (TextView) menu
					.findViewById(R.id.menu_hasInspection);

			menu_forChecking.setOnClickListener(this);
			menu_inInspection.setOnClickListener(this);
			menu_hasInspection.setOnClickListener(this);
			pWindow = new PopupWindow(menu, android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		// 判断当pWindow处于显示状态，点击则取消,否则显示该popupWindow
		if (pWindow.isShowing()) {
			pWindow.dismiss();
		} else {
			pWindow.setFocusable(true);
			pWindow.setOutsideTouchable(true);
			pWindow.setBackgroundDrawable(new BitmapDrawable()); // 一定要添加这行，否则会影响其他的事件触发
			pWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
			pWindow.showAtLocation(getListView(), Gravity.RIGHT | Gravity.TOP,
					0, getTitleHeight() + getStatusHeight());
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.menu_forChecking:
			inspectionState = 0;
			menu_forChecking.setEnabled(false);
			menu_inInspection.setEnabled(true);
			menu_hasInspection.setEnabled(true);
			setPage(1);
			pWindow.dismiss();
			initListData(1);
			break;

		case R.id.menu_inInspection:
			inspectionState = 1;
			menu_forChecking.setEnabled(true);
			menu_inInspection.setEnabled(false);
			menu_hasInspection.setEnabled(true);
			pWindow.dismiss();
			setPage(1);
			initListData(1);
			break;
		case R.id.menu_hasInspection:
			inspectionState = 2;
			menu_forChecking.setEnabled(true);
			menu_inInspection.setEnabled(true);
			menu_hasInspection.setEnabled(false);
			pWindow.dismiss();
			setPage(1);
			initListData(1);
			break;
		default:
			break;
		}
		super.onClick(view);
	}

}
