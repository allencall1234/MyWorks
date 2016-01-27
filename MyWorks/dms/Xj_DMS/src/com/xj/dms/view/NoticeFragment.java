package com.xj.dms.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.dms.adapter.CommonAdapter;
import com.xj.dms.adapter.ViewHolder;
import com.xj.dms.common.XListView;
import com.xj.dms.common.XListView.IXListViewListener;
import com.xj.dms.https.HttpDataCallBack;
import com.xj.dms.https.HttpJsonUtils;
import com.xj.dms.https.UserHttp;
import com.xj.dms.model.NoticePlanBean;
/**
 * 通知公告
 * @author qinfan
 *
 * 2015-11-6
 */
public class NoticeFragment extends Fragment implements IXListViewListener{
	private XListView listView; 
	private View view;
	private TextView empty;
	private CommonAdapter<NoticePlanBean>  adapter;
	private ArrayList<NoticePlanBean> noticePlanBeans = new ArrayList<NoticePlanBean>();
	
	private int totalNum = 0;//从数据库中获取的数据总数
	private int curragePage=1;//当前页
	private int rows = 10;//每页获取数据数量

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.notice_fragment, container, false);

		init();
		return view;
	}

	private void init(){
		empty = (TextView) view.findViewById(R.id.empty);
		listView = (XListView) view.findViewById(R.id.notice_list);
		listView.setPullLoadEnable(false);
		listView.setXListViewListener(this);
		adapter = new CommonAdapter<NoticePlanBean>(
				getActivity(), noticePlanBeans, R.layout.notice_list_detail) {
			
			@Override
			public void convert(ViewHolder viewHolder,
					NoticePlanBean item) {
				viewHolder.setText(R.id.tv_usable_work,item.getServiceName());  
				viewHolder.setText(R.id.tv_notice_date,item.getDateTime());
				viewHolder.setImageResource(R.id.iv_notice_icon, R.drawable.icon_notice_list);
			}
		};
		listView.setAdapter(adapter);
		connectNet();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), NoticePlanDetailActivity.class);
				intent.putExtra("type", "notice");
				intent.putExtra("position", position-1);
				intent.putParcelableArrayListExtra(
						"noticePlanBeans", noticePlanBeans);
				getActivity().startActivity(intent);
			}
		});
	}

	private List<NoticePlanBean> getData(String json){
		try {
			JSONObject jsonObject = new JSONObject(json);
			totalNum = jsonObject.getInt("total");
				JSONArray jsonArray = jsonObject.getJSONArray("publicServiceList");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object2 = jsonArray.getJSONObject(i);
						NoticePlanBean bean = (NoticePlanBean) HttpJsonUtils
								.putJsonToObject(object2,
										"com.xj.dms.model.NoticePlanBean");
		
						noticePlanBeans.add(bean);
					}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return noticePlanBeans;
	}

	/** 停止刷新  */
	private void onLoad() 
	{
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(HttpJsonUtils.getTime());
	}

	@Override
	public void onRefresh() {
		onLoad();
	}

	@Override
	public void onLoadMore() {
			curragePage++;
			connectNet();
			onLoad();
	} 
	
	private void connectNet(){
		UserHttp.getNetPowerNotice(
				curragePage,
				rows,
				new HttpDataCallBack() {
					@Override
					public void HttpSuccess(String _result) {
						getData(_result);
						if(totalNum==0){
							empty.setVisibility(View.VISIBLE);
							listView.setVisibility(View.GONE);
						}else 
							if(totalNum <= 10){
							listView.setPullLoadEnable(false);
						}else{
							listView.setPullLoadEnable(true);
							
						}
						adapter.notifyDataSetChanged();
						int pages=totalNum / rows == 0 ? totalNum / rows:totalNum / rows + 1; 
						if(curragePage >= pages && pages > 1){
							listView.setPullLoadEnable(false);
							Toast.makeText(getActivity(), "数据已全部加载完", 1).show();
						}
					}
		
					@Override
					public void HttpFail(int ErrCode) {
						
					}
				});
	}
	
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		view = null;
	}
}
