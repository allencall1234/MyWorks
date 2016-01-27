package com.xj_pipe.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.TextView;

import com.xj_pipe.adapter.CommonAdapter;
import com.xj_pipe.adapter.ViewHolder;
import com.xj_pipe.base.BaseActivity;
import com.xj_pipe.bean.InspectionBean;
import com.xj_pipe.xlistview.XListView;
import com.xj_pipe.xlistview.XListView.IXListViewListener;

/**
 * 巡检详情界面
 * @author Administrator
 *
 */
public class InspectionDetailsActivity extends BaseActivity implements IXListViewListener
{
	private XListView lv_inspectionPoint;// 巡检路线
	
	private CommonAdapter<InspectionBean> adapter;
	private List<InspectionBean> inspectionPointList;
	
	private TextView tv_currentInspectionTask;// 当前巡检任务
	private TextView tv_timeInterval;// 巡检时间区间
	private TextView tv_taskType;// 任务类型
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_inspection_details);
		
		initView();
	}

	private void initView()
	{
		tv_currentInspectionTask = (TextView) findViewById(R.id.tv_currentInspectionTask);
		tv_timeInterval = (TextView) findViewById(R.id.tv_timeInterval);
		tv_taskType = (TextView) findViewById(R.id.tv_taskType);
		
		lv_inspectionPoint = (XListView) findViewById(R.id.lv_inspectionPoint);
		
		inspectionPointList = new ArrayList<InspectionBean>();
		
		lv_inspectionPoint.setPullLoadEnable(false);
		lv_inspectionPoint.setXListViewListener(this);
		
		adapter = new CommonAdapter<InspectionBean>(this,
				getInspectionPointListData(), R.layout.item_inspection_details) {
			
			@Override
			public void convert(ViewHolder viewHolder, InspectionBean item) 
			{
//				if (item.getInspectionState() == 0) 
//				{
//					viewHolder.setText(R.id.tv_inspectionState, "未巡检");
//				}
//				else
//				{
//					viewHolder.setText(R.id.tv_inspectionState, "已巡检");
//				}
//				int position = item.getPointCount();
//				
//				int state = position > 1 ? 2:1;
//				if(position == adapter.getCount()){
//					state = 3;
//				}
				
//				viewHolder.setText(R.id.tv_count, String.valueOf(item.getPointCount()));
//				viewHolder.setText(R.id.tv_count, String.valueOf(item.getPointCount()));
//				viewHolder.setTimeViewState(R.id.tv_count, state);
//				viewHolder.setTimeViewNum(R.id.tv_count, position);
//				viewHolder.setText(R.id.tv_inspectionPoint, item.getInspectionPoint());
			}
		};
		
		lv_inspectionPoint.setAdapter(adapter);
	}
	
	@Override
	public void onBackLisenter()
	{
		super.onBackLisenter();
		finish();
	}
	
	private List<InspectionBean> getInspectionPointListData()
	{
		
		return inspectionPointList;
	}

	@Override
	public void onRefresh() 
	{
		
	}

	@Override
	public void onLoadMore()
	{
		
	}
	
}
