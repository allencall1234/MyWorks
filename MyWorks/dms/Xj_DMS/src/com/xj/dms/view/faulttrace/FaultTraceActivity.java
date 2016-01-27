package com.xj.dms.view.faulttrace;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.widget.BaseAdapter;

import com.xj.dms.adapter.CommonAdapter;
import com.xj.dms.adapter.ViewHolder;
import com.xj.dms.https.HttpDataCallBack;
import com.xj.dms.https.HttpJsonUtils;
import com.xj.dms.https.UserHttp;
import com.xj.dms.model.FaultTraceBean;
import com.xj.dms.view.R;
import com.xj.dms.view.schedule.SubActivity;

/**
 * 故障追溯
 * 
 */
public class FaultTraceActivity extends SubActivity {
	private int totalNum = -1;;// 从数据库中获取的数据总数
	private int num = 10;// 每页加载的数据数量

	private List<FaultTraceBean> faultList;

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_FAULT_TRACE;
	}

	@Override
	public BaseAdapter getAdapter() {
		CommonAdapter<FaultTraceBean> adapter = new CommonAdapter<FaultTraceBean>(
				this, faultList, R.layout.fault_trace_item) {
			@Override
			public void convert(ViewHolder viewHolder, FaultTraceBean item) {
				if (item.getStatus().equals("0")
						|| item.getStatus().equals("1")) {
					viewHolder.setText(R.id.tv_taskState, "已修复");
				} else {
					viewHolder.setText(R.id.tv_taskState, "未修复");
				}
				viewHolder.setText(R.id.tv_taskTitle, item.getBreakdownIndex());
				viewHolder.setText(R.id.tv_taskType, "位置 : " + item.getPlace());
				viewHolder.setText(R.id.tv_time, item.getHappenTime());
			}
		};
		return adapter;
	}

	@Override
	public int getTotalNumber() {
		return totalNum;
	}

	@Override
	public void initListData(final int page) {

		if (faultList == null) {
			faultList = new ArrayList<FaultTraceBean>();
		}
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				UserHttp.faultTraceQuery(page, num, "",
						new HttpDataCallBack() {
							@Override
							public void HttpSuccess(String _result) {
								getFaultListData(_result);
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

	// 获取数据并解析
	private List<FaultTraceBean> getFaultListData(String result) {
		try {
			JSONObject object = new JSONObject(result);
			totalNum = object.getInt("total");
			JSONArray array = object.getJSONArray("shutdownList");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				FaultTraceBean bean = (FaultTraceBean) HttpJsonUtils
						.putJsonToObject(object2,
								"com.xj.dms.model.FaultTraceBean");

				faultList.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return faultList;

	}

	@Override
	public void onListViewItemClick(int position) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(FaultTraceActivity.this,
				FaultTraceDetailsActivity.class);
		FaultTraceBean fault_bean = faultList.get(position - 2);
		intent.putExtra("FaultTraceBean", fault_bean);
		startActivity(intent);
	}
}
