package com.xj_pipe.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xj_pipe.adapter.CommonAdapter;
import com.xj_pipe.adapter.ViewHolder;
import com.xj_pipe.base.MyApp;
import com.xj_pipe.bean.FaultBean;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;
import com.xj_pipe.utils.JsonUtils;

import android.content.Intent;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 事故报修界面
 * 
 * @author Administrator
 * 
 */
public class ActivityRepair extends SubActivity {
	private int totalNum = -1;;// 从数据库中获取的数据总数
	private int num = 20;// 每页加载的数据数量
	private BaseAdapter mAdapter;
	private List<FaultBean> list;

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_PAGE_REPAIR;
	}

	@Override
	public BaseAdapter getAdapter() {
		// TODO Auto-generated method stub
		if (mAdapter == null) {
			mAdapter = new CommonAdapter<FaultBean>(getApplicationContext(),
					list, R.layout.item_accident_repair_history) {
				@Override
				public void convert(ViewHolder viewHolder, FaultBean item) {
					TextView tv_bg = viewHolder
							.getView(R.id.tv_inspectionState);

					if (item.getFlag().equals("N")) {
						viewHolder.setText(R.id.tv_inspectionState, "未维修");
						tv_bg.setBackgroundResource(R.drawable.state0);
					} else {
						viewHolder.setText(R.id.tv_inspectionState, "已维修");
						tv_bg.setBackgroundResource(R.drawable.state2);
					}

					viewHolder.setText(R.id.tv_repair_area, item.getPlace());
					viewHolder.setText(R.id.tv_accident_discribe,
							item.getContent());
					viewHolder.setText(R.id.tv_repair_time,
							"发生时间 : " + item.getDefectTime());
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
			list = new ArrayList<FaultBean>();
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
				UserHttp.accidentHandlingHistoryQuery(page, num, "defectTime",
						"desc", MyApp.userInfo.getStaffId(), new HttpDataCallBack() {

							@Override
							public void HttpSuccess(String _result) {
								getAccidentRepairListData(_result);
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
		FaultBean fault_bean = list.get(position - 1);
		Intent intent = new Intent(ActivityRepair.this,
				AccidentHistoryDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("FaultBean", fault_bean);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private List<FaultBean> getAccidentRepairListData(String result) {
		try {
			JSONObject object = new JSONObject(result);
			if (object.getBoolean("status") == true) {
				totalNum = object.getInt("total");

				JSONArray array = object.getJSONArray("repairList");
				for (int i = 0; i < array.length(); i++) {
					JSONObject object2 = array.getJSONObject(i);
					FaultBean bean = (FaultBean) JsonUtils.putJsonToObject(
							object2, "com.xj_pipe.bean.FaultBean");
					list.add(bean);
				}
			}
		} catch (JSONException e) {
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
