package com.xj_pipe.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xj_pipe.adapter.CommonAdapter;
import com.xj_pipe.adapter.ViewHolder;
import com.xj_pipe.base.MyApp;
import com.xj_pipe.bean.BuildBean;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;
import com.xj_pipe.utils.JsonUtils;

import android.content.Intent;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 施工上报界面
 * @author Administrator
 *
 */
public class ActivityFeedBack extends SubActivity {

	private int totalNum = -1;;// 从数据库中获取的数据总数
	private int num = 20;// 每页加载的数据数量
	private BaseAdapter mAdapter;
	private List<BuildBean> list;

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_PAGE_FEEDBACK;
	}

	@Override
	public BaseAdapter getAdapter() {
		// TODO Auto-generated method stub
		if (mAdapter == null) {
			mAdapter = new CommonAdapter<BuildBean>(getApplicationContext(),
					list, R.layout.item_accident_repair_history) {

				@Override
				public void convert(ViewHolder viewHolder, BuildBean item) {
					// TODO Auto-generated method stub
					TextView tv_bg = viewHolder
							.getView(R.id.tv_inspectionState);

					if (item.getState().equals("1")) {
						viewHolder.setText(R.id.tv_inspectionState, "待处理");
						tv_bg.setBackgroundResource(R.drawable.state0);
					} else if (item.getState().equals("2")) {
						viewHolder.setText(R.id.tv_inspectionState, "完成");
						tv_bg.setBackgroundResource(R.drawable.state2);
					}
					viewHolder.setText(R.id.tv_repair_area, item.getUnit());// 施工单位
					viewHolder.setText(R.id.tv_accident_discribe,
							item.getContent());
					viewHolder.setText(R.id.tv_repair_time,
							"施工期限 : " + item.getBeginTime()+"-"+ item.getEndTime());
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
			list = new ArrayList<BuildBean>();
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
				// TODO Auto-generated method stub
				UserHttp.buildFeedBackHistoryQuery(page, num, "createTime",
						"desc", MyApp.userInfo.getStaffId(),
						new HttpDataCallBack() {

							@Override
							public void HttpSuccess(String _result) {
								getData(_result);
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
		BuildBean build_bean = list.get(position - 1);
		Intent intent = new Intent(ActivityFeedBack.this,
				AccidentHistoryDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("BuildBean", build_bean);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private List<BuildBean> getData(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			boolean status = jsonObject.getBoolean("status");
			if (status == true) {
				totalNum = jsonObject.getInt("total");
				JSONArray jsonArray = jsonObject
						.getJSONArray("conFeedbackList");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object2 = jsonArray.getJSONObject(i);
					BuildBean bean = (BuildBean) JsonUtils.putJsonToObject(
							object2, "com.xj_pipe.bean.BuildBean");
					list.add(bean);
				}
			} else {

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
