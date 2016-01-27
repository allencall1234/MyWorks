package com.xj_pipe.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xj_pipe.adapter.CommonAdapter;
import com.xj_pipe.adapter.ViewHolder;
import com.xj_pipe.base.MyApp;
import com.xj_pipe.bean.EmergencyBean;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;
import com.xj_pipe.utils.JsonUtils;

import android.content.Intent;
import android.os.Bundle;
import android.widget.BaseAdapter;

/**
 * 突发事件界面
 * @author Administrator
 *
 */
public class ActivityEmergency extends SubActivity {

	private int totalNum = -1;;// 从数据库中获取的数据总数
	private int num = 20;// 每页加载的数据数量
	private BaseAdapter mAdapter;
	private List<EmergencyBean> list;
	
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_PAGE_EMERGENCY;
	}

	@Override
	public BaseAdapter getAdapter() {
		// TODO Auto-generated method stub
		if (mAdapter == null) {
			mAdapter = new CommonAdapter<EmergencyBean>(getApplicationContext(), list, R.layout.item_accident_repair_history) 
			{
				
				@Override
				public void convert(ViewHolder viewHolder, EmergencyBean item)
				{
					int state = Integer.valueOf(item.getState());
					viewHolder.setText(R.id.tv_inspectionState, textStates[state]);
					viewHolder.setTextBackground(R.id.tv_inspectionState, drawableIds[state]);
					
					viewHolder.setText(R.id.tv_repair_area, item.getTitle());//突发事件标题
					viewHolder.setText(R.id.tv_accident_discribe, item.getDescription());
					viewHolder.setText(R.id.tv_repair_time, "发生时间 : " + item.getHappenDate());
					
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
		if (list == null) {
			list = new ArrayList<EmergencyBean>();
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
				UserHttp.emergencyHistoryQuery(page, num, "createTime", "desc", MyApp.userInfo.getStaffId(), new HttpDataCallBack(){

							@Override
							public void HttpSuccess(String _result) {
								getEmergencyListData(_result);
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

	@Override
	public void onListViewItemClick(int position) {
		// TODO Auto-generated method stub
		EmergencyBean emergency_bean = list.get(position - 1);
		Intent intent = new Intent(ActivityEmergency.this, AccidentHistoryDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("EmergencyBean", emergency_bean);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private List<EmergencyBean> getEmergencyListData(String result)
	{
		try
		{
			JSONObject object = new JSONObject(result);
			if (object.getBoolean("status") == true)
			{
				totalNum = object.getInt("total");
				JSONArray array = object.getJSONArray("eventOrderList");
				for (int i = 0; i < array.length(); i++) 
				{
					JSONObject object2 = array.getJSONObject(i);
					EmergencyBean bean = (EmergencyBean) JsonUtils.putJsonToObject(object2, "com.xj_pipe.bean.EmergencyBean");
					list.add(bean);
				}
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return list;
	}
	
	@Override
	public void onMoreLisenter() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, AccidentHandlingActivity.class));
	}
}
