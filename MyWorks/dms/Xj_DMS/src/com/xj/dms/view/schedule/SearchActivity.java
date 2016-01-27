package com.xj.dms.view.schedule;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xj.dms.adapter.CommonAdapter;
import com.xj.dms.adapter.ViewHolder;
import com.xj.dms.common.BaseActivity;
import com.xj.dms.common.MyApp;
import com.xj.dms.common.XListView;
import com.xj.dms.common.XListView.IXListViewListener;
import com.xj.dms.https.HttpDataCallBack;
import com.xj.dms.https.HttpJsonUtils;
import com.xj.dms.https.UserHttp;
import com.xj.dms.model.FaultTraceBean;
import com.xj.dms.model.KnowledgeBaseBean;
import com.xj.dms.model.TaskBean;
import com.xj.dms.view.R;
import com.xj.dms.view.faulttrace.FaultTraceDetailsActivity;
import com.xj.dms.view.knowledgebase.KnowledgeBaseDetailsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends BaseActivity implements OnClickListener,
		IXListViewListener {

	private int type;
	private EditText et_searchStr;
	private XListView xListView;

	private int totalNum = -1;;// 从数据库中获取的数据总数
	private int num = 10;// 每页加载的数据数量
	private int curragePage = 1;// 当前页

	private List<FaultTraceBean> faultList = new ArrayList<FaultTraceBean>();
	private List<TaskBean> taskList = new ArrayList<TaskBean>();
	private List<KnowledgeBaseBean> knowledgeBaseList = new ArrayList<KnowledgeBaseBean>();

	private String searchStr;
	private BaseAdapter mAdapter;
	String[] states;
	private TextView emptyText = null;

	private boolean isSetAdapter = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_search);
		states = getResources().getStringArray(R.array.states);

		type = getIntent().getIntExtra("type", 0);
		
		initViews();
		
		if (type == SubActivity.TYPE_LIBRARY_LIST)
		{
			et_searchStr.setHint("搜知识库关键词");
		}
		else if (type == SubActivity.TYPE_FAULT_TRACE) 
		{
			et_searchStr.setHint("搜故障标题");
		}
		else
		{
			et_searchStr.setHint("搜任务标题");
		}

	}

	private void initViews() {
		// TODO Auto-generated method stub
		et_searchStr = (EditText) findViewById(R.id.search_edit);
		xListView = (XListView) findViewById(R.id.schedule_refresh_list);
		emptyText = (TextView) findViewById(R.id.empty_view);

		findViewById(R.id.search_back).setOnClickListener(this);
		findViewById(R.id.search_start).setOnClickListener(this);

		xListView.setPullLoadEnable(true);// 设置上拉加载
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);

		xListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onListViewItemClick(position);
			}
		});
	}

	public void onListViewItemClick(int position) {
		Intent intent;
		if (type == SubActivity.TYPE_FAULT_TRACE) {
			intent = new Intent(this, FaultTraceDetailsActivity.class);
			FaultTraceBean fault_bean = faultList.get(position - 1);
			intent.putExtra("FaultTraceBean", fault_bean);
		} else if (type == SubActivity.TYPE_LIBRARY_LIST) {
			intent = new Intent(this, KnowledgeBaseDetailsActivity.class);
			KnowledgeBaseBean knowledgeBase_bean = knowledgeBaseList
					.get(position - 1);
			intent.putExtra("KnowledgeBaseBean", knowledgeBase_bean);
		} else {
			intent = new Intent(this, ScheduleDetailsActivity.class);
			TaskBean task_bean = taskList.get(position - 1);
			intent.putExtra("id", task_bean.getId());
			intent.putExtra("title_type", type);
		}

		startActivity(intent);
//		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_back:
			finish();
			break;
		case R.id.search_start:
			doSearchAndUpdate();
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		xListView.setRefreshTime("刚刚");
	}

	@Override
	public void onLoadMore() {
		curragePage++;
		handler.sendEmptyMessage(type);
	}

	/**
	 * 进行搜索查询操作并更新列表
	 */
	private void doSearchAndUpdate() {
		searchStr = et_searchStr.getText().toString().trim();
		// 获取到输入的内容后，隐藏输入法
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		if (searchStr.equals("")) {
			showToast("请输入要搜索的关键字!");
			return;
		}
		// 判断集合中是否有数据，清除后以便下次搜索
		if (type == SubActivity.TYPE_FAULT_TRACE) {
			faultList.clear();
		} else if (type == SubActivity.TYPE_LIBRARY_LIST) {
			knowledgeBaseList.clear();
		} else {
			taskList.clear();
		}
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
		
		curragePage = 1;
		
		handler.sendEmptyMessage(type);

	}

	private BaseAdapter getAdapter() {
		if (type == SubActivity.TYPE_FAULT_TRACE) {
			mAdapter = new CommonAdapter<FaultTraceBean>(this, faultList,
					R.layout.fault_trace_item) {

				@Override
				public void convert(ViewHolder viewHolder, FaultTraceBean item) {
					if (item.getStatus().equals("0")
							|| item.getStatus().equals("1")) {
						viewHolder.setText(R.id.tv_taskState, "已修复");
					} else {
						viewHolder.setText(R.id.tv_taskState, "未修复");
					}
					viewHolder.setText(R.id.tv_taskTitle,
							item.getBreakdownIndex());
					viewHolder.setText(R.id.tv_taskType,
							"位置 : " + item.getPlace());
					viewHolder.setText(R.id.tv_time, item.getHappenTime());
				}
			};
		} else if (type == SubActivity.TYPE_LIBRARY_LIST) {
			mAdapter = new CommonAdapter<KnowledgeBaseBean>(this,
					knowledgeBaseList, R.layout.schedule_library_item) {

				@Override
				public void convert(ViewHolder viewHolder,
						KnowledgeBaseBean item) {
					viewHolder.setText(R.id.tv_knowledgeTitle, item.getTitle());
					viewHolder.setText(R.id.tv_content, item.getContext());
					viewHolder
							.setText(R.id.tv_type, "所属装置 : " + item.getType());
				}
			};
		} else {
			mAdapter = new CommonAdapter<TaskBean>(this, taskList,
					R.layout.schedule_common_item) {
				@Override
				public void convert(ViewHolder viewHolder, TaskBean item) {

					viewHolder.setText(R.id.tv_taskTitle, item.getTname());
					viewHolder.setText(R.id.tv_taskType,
							"任务类型 : " + item.getTtype());
					viewHolder.setText(R.id.tv_time,
							"申请日期 : " + item.getTapplydate());
					if (type == SubActivity.TYPE_TASK_QUERY) {
						int i = Integer.parseInt(item.getTstate().toString());
						viewHolder.setText(R.id.tv_taskState, item.getFlag()
								+ states[i - 1]);
					} else {
						viewHolder
								.setText(
										R.id.tv_taskState,
										type == SubActivity.TYPE_TASK_UNAPPROVAL ? "未审批"
												: "已审批");
					}
				}
			};
		}
		return mAdapter;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SubActivity.TYPE_FAULT_TRACE:// 故障追溯搜索
				UserHttp.faultTraceQuery(curragePage, num, searchStr,
						new HttpDataCallBack() {
							@Override
							public void HttpSuccess(String _result) {

								getFaultListData(_result);

								handler.obtainMessage(5).sendToTarget();

							}

							@Override
							public void HttpFail(int ErrCode) {
								httpRequestFail();
							}
						});
				break;

			case SubActivity.TYPE_TASK_QUERY:// 任务查询搜索
				UserHttp.taskQuery(curragePage, num, searchStr,
						new HttpDataCallBack() {
							@Override
							public void HttpSuccess(String _result) {
								getTaskListData(_result);
								handler.obtainMessage(5).sendToTarget();
							}

							@Override
							public void HttpFail(int ErrCode) {
								httpRequestFail();
							}
						});
				break;

			case SubActivity.TYPE_TASK_APPROVAL:// 已审批任务搜索
				UserHttp.approvaledTaskQuery(curragePage, num, searchStr,
						MyApp.userBean.getUserId(), new HttpDataCallBack() {

							@Override
							public void HttpSuccess(String _result) {
								getTaskListData(_result);
								handler.obtainMessage(5).sendToTarget();
							}

							@Override
							public void HttpFail(int ErrCode) {
								httpRequestFail();
							}
						});
				break;

			case SubActivity.TYPE_TASK_UNAPPROVAL:// 待审批任务搜索
				UserHttp.unApprovalTaskQuery(curragePage, num, searchStr,
						MyApp.userBean.getUserId(), new HttpDataCallBack() {
							@Override
							public void HttpSuccess(String _result) {
								getTaskListData(_result);
								handler.obtainMessage(5).sendToTarget();
							}

							@Override
							public void HttpFail(int ErrCode) {
								httpRequestFail();
							}
						});
				break;

			case SubActivity.TYPE_LIBRARY_LIST:// 知识库列表搜索
				UserHttp.knowledgeBaseQuery(curragePage, num, searchStr,
						new HttpDataCallBack() {
							@Override
							public void HttpSuccess(String _result) {
								getKnowledgeBaseListData(_result);
								handler.obtainMessage(5).sendToTarget();
							}

							@Override
							public void HttpFail(int ErrCode) {
								httpRequestFail();
							}
						});
				break;
			case 5:
				et_searchStr.setHint(searchStr);
				if(!isSetAdapter){
					mAdapter = getAdapter();
				}
				int count = mAdapter.getCount();
				if (count > 0) {
					xListView.setVisibility(View.VISIBLE);
					emptyText.setVisibility(View.GONE);
					if(isSetAdapter){
						mAdapter.notifyDataSetChanged();
					}else{
						xListView.setAdapter(mAdapter);
						isSetAdapter = true;
					}
					xListView.setSelection(curragePage > 1 ? count - 9 : 0);
					if (count >= totalNum) {
						xListView.setPullLoadEnable(false);
						if (curragePage > 1) {
							showToast("全部加载完毕!");
						}
					} else {
						xListView.setPullLoadEnable(true);
					}
					xListView.stopLoadMore();
				} else {
					xListView.setVisibility(View.GONE);
					emptyText.setVisibility(View.VISIBLE);
				}
				
				break;
			default:
				break;
			}
		};
	};

	// 网络请求失败提示
	private void httpRequestFail() {
		showToast("网络请求失败，请检查您的网络!");
	}

	// 获取任务数据并解析
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

	// 获取故障数据并解析
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

	// 获取知识库数据并解析
	private List<KnowledgeBaseBean> getKnowledgeBaseListData(String result) {
		try {
			JSONObject object = new JSONObject(result);
			totalNum = object.getInt("total");
			JSONArray array = object.getJSONArray("knowledgeList");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				KnowledgeBaseBean bean = (KnowledgeBaseBean) HttpJsonUtils
						.putJsonToObject(object2,
								"com.xj.dms.model.KnowledgeBaseBean");

				knowledgeBaseList.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return knowledgeBaseList;

	}
}
