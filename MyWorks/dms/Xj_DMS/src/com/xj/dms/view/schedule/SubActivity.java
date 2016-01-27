package com.xj.dms.view.schedule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.xj.dms.common.BaseActivity;
import com.xj.dms.common.XListView;
import com.xj.dms.common.XListView.IXListViewListener;
import com.xj.dms.view.R;

public abstract class SubActivity extends BaseActivity implements
		OnClickListener, IXListViewListener {

	public static final int MESSAGE_FAILED = 0;
	public static final int MESSAGE_SUCCESS = 1;
	
	public static final int TYPE_FAULT_TRACE = 0; // 故障追溯
	public static final int TYPE_TASK_QUERY = 1; // 任务查询
	public static final int TYPE_TASK_APPROVAL = 2; // 任务已审批
	public static final int TYPE_TASK_UNAPPROVAL = 3; // 任务待审
	public static final int TYPE_LIBRARY_LIST = 4; // 知识库列表

	private XListView mListView = null;
	private View searchLayout = null;

	private RadioGroup mRadioGroup = null;
	private TextView emptyText = null;
	
	public int type = -1;

	private int curragePage = 0;// 当前页

	private BaseAdapter mAdapter = null;
	
	private ProgressDialog pDialog = null;  //创建一个进度条,但1000ms内无网络数据返回,则显示对话框
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.schedule_main);
		type = getType();
		initViews(); // 初始化视图控件
		initListData(++curragePage); // 从网络获取列表数据并更新
		initDatas(); // 初始化类型界面
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		if (type != TYPE_TASK_APPROVAL && type != TYPE_TASK_UNAPPROVAL) {
			mRadioGroup.setVisibility(View.GONE);
		}

		if (type == TYPE_LIBRARY_LIST || type == TYPE_FAULT_TRACE) {
			hideTitleView();
		} else {
			setCenterTitle(getTitleByType(type));
		}
	}

	private String getTitleByType(int type) {
		switch (type) {
		case TYPE_FAULT_TRACE:
			return getResources().getString(R.string.fault_trace);
		case TYPE_TASK_QUERY:
			return getResources().getString(R.string.task_query);
		case TYPE_TASK_APPROVAL:
		case TYPE_TASK_UNAPPROVAL:
			return getResources().getString(R.string.task_approval);
		case TYPE_LIBRARY_LIST:
		default:
			return getResources().getString(R.string.knowledge_base);
		}
	}

	private void initViews() {
		// TODO Auto-generated method stub
		searchLayout = LayoutInflater.from(this).inflate(
				R.layout.search_textview_layout, null);
		searchLayout.setOnClickListener(this);

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("正在请求数据...");
		pDialog.setCanceledOnTouchOutside(false);
		
		mHandler.postDelayed(runnable, 1000);
		
		
		emptyText = (TextView) findViewById(R.id.empty_view);
		
		mRadioGroup = (RadioGroup) findViewById(R.id.sub_title);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				curragePage = 0;
				switch (checkedId) {
				case R.id.radio_left:
					type = TYPE_TASK_UNAPPROVAL;
					break;
				case R.id.radio_right:
					type = TYPE_TASK_APPROVAL;
					break;
				default:
					break;
				}
				initListData(++curragePage);
			}
		});

		mListView = (XListView) findViewById(R.id.schedule_refresh_list);

		mListView.addHeaderView(searchLayout);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		if (getTotalNumber() <= 10) {
			mListView.setPullLoadEnable(false);// 设置上拉加载
		} else {
			mListView.setPullLoadEnable(true);
		}

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				onListViewItemClick(position);
			}
		});

		getbtn_back().setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.seacher_layout: // 根据type内容进行相应查询
			Intent intent = new Intent();
			intent.setClass(SubActivity.this, SearchActivity.class);
			intent.putExtra("type", type);
			startActivity(intent);
			break;
		case R.id.iv_back:
			finish();
			break;
		default:
			break;
		}
	}

	/*
	 * 延时一秒显示加载对话框
	 */
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			pDialog.show();
		}
	};
	
	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_FAILED:
				if (pDialog.isShowing()) {
					pDialog.dismiss();
				}
				mHandler.removeMessages(0);
				showToast("网络请求失败，请您检查网络!");
				break;
			case MESSAGE_SUCCESS:
				if (pDialog.isShowing()) {
					pDialog.dismiss();
				}else {
					mHandler.removeCallbacks(runnable);
				}
				
				mAdapter = getAdapter();
				int count = mAdapter.getCount();
				if (count > 0) {
					mListView.setVisibility(View.VISIBLE);	
					emptyText.setVisibility(View.GONE);
					mListView.setAdapter(mAdapter);
					mListView.setSelection(curragePage > 1 ? count- 8 : 0);
					
					if (count >= getTotalNumber()) {
						mListView.setPullLoadEnable(false);
						if (curragePage > 1) {
							showToast("全部加载完毕!");
						}
					}else {
						mListView.setPullLoadEnable(true);
					}
					mListView.stopLoadMore();
				}else {
					mListView.setVisibility(View.GONE);
					emptyText.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		initListData(++curragePage);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
	}
	
	/**
	 * 返回当前的页面类型
	 * 
	 * @return TYPE_FAULT_TRACE 显示故障追溯相关界面
	 *         <p>
	 *         TYPE_TASK_QUERY 显示任务查询界面
	 *         <p>
	 *         TYPE_TASK_APPROVAL,TYPE_TASK_UNAPPROVAL 显示任务审批界面
	 *         <p>
	 *         TYPE_LIBRARY_LIST 显示知识库界面
	 */
	public abstract int getType();


	/**
	 * 获取列表适配器
	 * @return
	 */
	public abstract BaseAdapter getAdapter();

	/**
	 * 获取页面总条数
	 * 
	 * @return
	 */
	public abstract int getTotalNumber();

	/**
	 * 初始化列表数据
	 */
	public abstract void initListData(int page);

	/**
	 * 列表点击事件处理
	 * 
	 * @param position
	 *            列表点击位置
	 */
	public abstract void onListViewItemClick(int position);

}
