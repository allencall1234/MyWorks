package com.xj_pipe.view;

import com.xj_pipe.base.BaseActivity;
import com.xj_pipe.xlistview.XListView;
import com.xj_pipe.xlistview.XListView.IXListViewListener;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public abstract class SubActivity extends BaseActivity implements
		IXListViewListener {

	public static final int MESSAGE_FAILED = 0;
	public static final int MESSAGE_SUCCESS = 1;

	public static final int TYPE_PAGE_INSPECTION = 0; // 巡检界面
	public static final int TYPE_PAGE_INSPECTION_EMERGENCY = 1; // 应急巡检
	public static final int TYPE_PAGE_REPAIR = 2; // 事故报修
	public static final int TYPE_PAGE_EMERGENCY = 3; // 突发事件
	public static final int TYPE_PAGE_FEEDBACK = 4; // 施工上报

	private XListView mListView = null;

	private TextView emptyText = null;

	public int type = -1;

	private int curragePage = 0;// 当前页

	private BaseAdapter mAdapter = null;

	private ProgressDialog pDialog = null; // 创建一个进度条,但1000ms内无网络数据返回,则显示对话框

	protected String[] textStates;
	protected int[] drawableIds = {R.drawable.state0,R.drawable.state1,R.drawable.state2};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_sub_main);
		
		type = getType();

		if (getIntent() != null) {
			int taskType = getIntent().getIntExtra("taskType", 0);
			if (taskType == 1) {
				type = TYPE_PAGE_INSPECTION;
			}else if (taskType == 2) {
				type = TYPE_PAGE_INSPECTION_EMERGENCY;
			}
		}

		initViews(); // 初始化视图控件
		// 从网络获取列表数据并更新
		initDatas(); // 初始化类型界面
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		curragePage = 1;
		initListData(curragePage); 
	}
	
	private void initDatas() {
		// TODO Auto-generated method stub
		setCenterTitle(getTitleByType(type));
		hideLeftLogo();
		
		if (type == TYPE_PAGE_FEEDBACK || type == TYPE_PAGE_EMERGENCY) {
			setRightTitle("上报");
			textStates = getResources().getStringArray(R.array.emergency_state);
		} else if (type == TYPE_PAGE_REPAIR) {
			setRightTitle("报修");
			textStates = getResources().getStringArray(R.array.repair_state);
		} else if (type == TYPE_PAGE_INSPECTION
				|| type == TYPE_PAGE_INSPECTION_EMERGENCY) {
			setRightTitle("切换");
			textStates = getResources().getStringArray(R.array.inspection_state);
		}
	}

	private String getTitleByType(int type) {
		switch (type) {
		case TYPE_PAGE_INSPECTION:
			return getResources().getString(R.string.type_page_inspection);
		case TYPE_PAGE_INSPECTION_EMERGENCY:
			return getResources().getString(
					R.string.type_page_inspection_emergency);
		case TYPE_PAGE_REPAIR:
			return getResources().getString(R.string.type_page_repair);
		case TYPE_PAGE_EMERGENCY:
			return getResources().getString(R.string.type_page_emergency);
		case TYPE_PAGE_FEEDBACK:
		default:
			return getResources().getString(R.string.type_page_feedback);
		}
	}

	private void initViews() {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("正在请求数据...");
		pDialog.setCanceledOnTouchOutside(false);

		mHandler.postDelayed(runnable, 1000);

		emptyText = (TextView) findViewById(R.id.main_empty_view);
		mListView = (XListView) findViewById(R.id.main_list);

		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		if (getTotalNumber() <= 20) {
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
		@Override
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
				} else {
					mHandler.removeCallbacks(runnable);
				}

				if (mAdapter == null) {
					mListView.setAdapter(mAdapter = getAdapter());
				}
				int count = mAdapter.getCount();
				if (count > 0) {
					mListView.setVisibility(View.VISIBLE);
					emptyText.setVisibility(View.GONE);
					mAdapter.notifyDataSetChanged();

					if (count >= getTotalNumber()) { // 数据全部加载完毕,弹出显示,隐藏加载更多按钮
						mListView.setPullLoadEnable(false);
						if (curragePage > 1) {
							showToast("全部加载完毕!");
						}
					} else {
						mListView.setPullLoadEnable(true);
					}
					mListView.stopLoadMore();
				} else {
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

	@Override
	public void onBackLisenter() {
		// TODO Auto-generated method stub
		super.onBackLisenter();
		finish();
	}

	public XListView getListView() {
		return mListView;
	}

	public int getPage() {
		// TODO Auto-generated method stub
		return curragePage;
	}

	public void setPage(int page) {
		// TODO Auto-generated method stub
		curragePage = page;
	}

	/**
	 * 返回当前的页面类型
	 * 
	 * @return TYPE_PAGE_INSPECTION = 0; // 巡检界面
	 *         <p>
	 *         TYPE_PAGE_INSPECTION_EMERGENCY = 1; //应急巡检界面
	 *         <p>
	 *         TYPE_PAGE_REPAIR = 2; // 事故报修界面
	 *         <p>
	 *         TYPE_PAGE_EMERGENCY = 3; //突发事件界面
	 *         <p>
	 *         TYPE_PAGE_FEEDBACK = 4; // 施工上报界面
	 */
	public abstract int getType();

	/**
	 * 获取列表适配器
	 * 
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
