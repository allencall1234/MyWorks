package com.xj.dms.view.schedule;

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
import com.xj.dms.model.TaskBean;
import com.xj.dms.view.R;

/**
 * 任务查询
 * 
 */
public class ScheduleActivity extends SubActivity {

	private int totalNum = -1;;// 从数据库中获取的数据总数
	private int num = 10;// 每页加载的数据数量

	private List<TaskBean> taskList;

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_TASK_QUERY;
	}

	@Override
	public BaseAdapter getAdapter() {
		final String[] states = getResources().getStringArray(R.array.states);

		CommonAdapter<TaskBean> adapter = new CommonAdapter<TaskBean>(this,
				taskList, R.layout.schedule_common_item) {

			@Override
			public void convert(ViewHolder viewHolder, TaskBean item) {

				int i = Integer.parseInt(item.getTstate().toString());
				if (i == 2) 
				{
					viewHolder.setText(R.id.tv_taskState, item.getFlag() + states[i - 1]);
				}
				else
				{
					viewHolder.setText(R.id.tv_taskState, states[i - 1]);
				}
				viewHolder.setText(R.id.tv_taskTitle, item.getTname());
				viewHolder.setText(R.id.tv_taskType, "任务类型 : " + item.getTtype());
				viewHolder.setText(R.id.tv_time, "申请日期 : " + item.getTapplydate());
			}
		};
		return adapter;
	}

	@Override
	public int getTotalNumber() {
		return totalNum;
	}

	// 获取数据并解析
	private List<TaskBean> getTaskListData(String result) {
		try {
			JSONObject object = new JSONObject(result);
			totalNum = object.getInt("total");
			JSONArray array = object.getJSONArray("taskList");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				TaskBean bean = (TaskBean) HttpJsonUtils.putJsonToObject(
						object2, "com.xj.dms.model.TaskBean");

				taskList.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return taskList;

	}

	@Override
	public void initListData(final int page) {
		// TODO Auto-generated method stub 
		if (taskList == null) {
			taskList = new ArrayList<TaskBean>();
		}
		
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				UserHttp.taskQuery(page, num, "",
						new HttpDataCallBack() {

							@Override
							public void HttpSuccess(String _result) {
								getTaskListData(_result);
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
		Intent intent = new Intent(ScheduleActivity.this,
				ScheduleDetailsActivity.class);

		TaskBean task_bean = taskList.get(position - 2);
		intent.putExtra("id", task_bean.getId());
		intent.putExtra("title_type", TYPE_TASK_QUERY);

		startActivity(intent);
	}
}
