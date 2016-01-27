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
import com.xj.dms.common.MyApp;
import com.xj.dms.https.HttpDataCallBack;
import com.xj.dms.https.HttpJsonUtils;
import com.xj.dms.https.UserHttp;
import com.xj.dms.model.TaskBean;
import com.xj.dms.view.R;

/**
 * 任务已审批和待审批
 * 
 * @author zlt
 * 
 */
public class ScheduleApprovalActivity extends SubActivity {

	private int totalNum = -1;;// 从数据库中获取的数据总数
	private int num = 10;// 每页加载的数据数量

	private List<TaskBean> approvalList;

	@Override
	public int getType() {
		return TYPE_TASK_UNAPPROVAL;
	}

	@Override
	public BaseAdapter getAdapter() {
		CommonAdapter<TaskBean> adapter = new CommonAdapter<TaskBean>(this,
				approvalList, R.layout.schedule_common_item) {
			@Override
			public void convert(ViewHolder viewHolder, TaskBean item) {

				viewHolder.setText(R.id.tv_taskTitle, item.getTname());
				viewHolder.setText(R.id.tv_taskType,
						"任务类型 : " + item.getTtype());
				viewHolder.setText(R.id.tv_time,
						"申请日期 : " + item.getTapplydate());
				viewHolder.setText(R.id.tv_taskState,
						type == TYPE_TASK_UNAPPROVAL ? "未审批" : "已审批");
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
		if (page <= 1) {
			approvalList = new ArrayList<TaskBean>();
		}

		if (type == TYPE_TASK_APPROVAL) {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					UserHttp.approvaledTaskQuery(page, num, "",
							MyApp.userBean.getUserId(), new HttpDataCallBack() {
								@Override
								public void HttpSuccess(String _result) {
									getApprovalListData(_result);
									mHandler.obtainMessage(MESSAGE_SUCCESS).sendToTarget();
								}

								@Override
								public void HttpFail(int ErrCode) {
									mHandler.obtainMessage(MESSAGE_FAILED).sendToTarget();
								}
							});
				}
			});
		} else if (type == TYPE_TASK_UNAPPROVAL) {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					UserHttp.unApprovalTaskQuery(page, num, "",
							MyApp.userBean.getUserId(), new HttpDataCallBack() {
								@Override
								public void HttpSuccess(String _result) {
									getApprovalListData(_result);
									mHandler.obtainMessage(MESSAGE_SUCCESS).sendToTarget();
								}

								@Override
								public void HttpFail(int ErrCode) {
									mHandler.obtainMessage(MESSAGE_FAILED).sendToTarget();
								}
							});
				}
			});
		}
	}

	// 获取数据并解析
	private List<TaskBean> getApprovalListData(String result) {
		try {
			JSONObject object = new JSONObject(result);
			totalNum = object.getInt("total");
			JSONArray array = object.getJSONArray("taskList");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				TaskBean bean = (TaskBean) HttpJsonUtils.putJsonToObject(
						object2, "com.xj.dms.model.TaskBean");

				approvalList.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return approvalList;

	}

	@Override
	public void onListViewItemClick(int position) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(ScheduleApprovalActivity.this,
				ScheduleDetailsActivity.class);
		TaskBean bean = approvalList.get(position - 2);
		intent.putExtra("title_type", type);
		intent.putExtra("id", bean.getId());
		startActivity(intent);
	}
}
