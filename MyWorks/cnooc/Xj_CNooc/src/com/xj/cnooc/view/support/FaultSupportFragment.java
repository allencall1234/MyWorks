package com.xj.cnooc.view.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.xj.cnooc.adapter.SupportListAdapter;
import com.xj.cnooc.biz.UserHttpBiz;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.common.XListView;
import com.xj.cnooc.common.XListView.IXListViewListener;
import com.xj.cnooc.https.HttpDataCallBack;
import com.xj.cnooc.model.AttachmentBean;
import com.xj.cnooc.model.FaultSupportModel;
import com.xj.cnooc.view.R;
/**
 * 故障支持列表
 * @author qinfan
 *
 * 2015-9-18
 */
public class FaultSupportFragment extends Fragment implements IXListViewListener{
	private XListView refreshListView;
	private SupportListAdapter adapter;
	private static int REQUEST_CODE=1;

	private Button newsBtn;
	private View view;

	private int totalNum;//从数据库中获取的数据总数
	private int num=10;//每页加载的数据数量
	private int curragePage=1;//当前页
	private String fault_id=null;

	private ProgressDialog progress;

	List<Map<String, Object>> listItems=new ArrayList<Map<String,Object>>();
	private List<FaultSupportModel> faultSupportModels=new ArrayList<FaultSupportModel>();
	private FaultSupportModel faultSupportModelDetail;
	private int user_id;

	Handler mhaHandler=new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(final Message msg) {
			String rolename=MyApp.globelUser.getRolename();
			if(rolename.equals("系统专家")){
				user_id = MyApp.globelUser.getAccountid();
			}else if(rolename.equals("厂家支持人员")){
				user_id = MyApp.globelUser.getUnit();
			}
			if(msg.what==0){
				progress=ProgressDialog.show(getActivity(),"", "正在加载数据,请稍候...");
				UserHttpBiz.getFaultSupportListData(curragePage, num, user_id,2,new HttpDataCallBack() {

					@Override
					public void HttpSuccess(String _result) {
						progress.dismiss();
						adapter = new SupportListAdapter(getActivity(), getData(_result));
						if(totalNum <= 10){
							refreshListView.setPullLoadEnable(false);
						}
						refreshListView.setAdapter(adapter);
					}

					@Override
					public void HttpFail(int ErrCode) {
						progress.dismiss();
						Toast.makeText(getActivity(), "网络请求失败,请检查您的网络！", 1).show();
					}
				});

			}

			if(msg.what==1){
				UserHttpBiz.getFaultSupportListData(curragePage, num, user_id,2, new HttpDataCallBack() {

					@Override
					public void HttpSuccess(String _result) {
						getData(_result);
						adapter.notifyDataSetChanged(); 
						int pages=totalNum/num==0?totalNum/num:totalNum/num+1;
						if(curragePage>=pages){
							Toast.makeText(getActivity(), "数据已全部加载完", 1).show();
						}
					}

					@Override
					public void HttpFail(int ErrCode) {
						progress.dismiss();
						Toast.makeText(getActivity(), "网络请求失败,请检查您的网络！", 1).show();
					}
				});

			}

			if(msg.what==2){
				UserHttpBiz.getFinishFaultDetailData(fault_id,new HttpDataCallBack(){

					@Override
					public void HttpSuccess(String _result) {
						try {
							JSONObject jsonObject=new JSONObject(_result);

							faultSupportModelDetail=new FaultSupportModel();
							faultSupportModelDetail.setId(jsonObject.getJSONObject("hitchSupport").getString("id"));
							faultSupportModelDetail.setTitle(jsonObject.getJSONObject("hitchSupport").getString("title"));
							faultSupportModelDetail.setDescription(jsonObject.getJSONObject("hitchSupport").getString("description"));
							faultSupportModelDetail.setCreateTime(jsonObject.getJSONObject("hitchSupport").getString("createTime"));
							String type=jsonObject.getJSONObject("hitchSupport").getString("supportType");
							faultSupportModelDetail.setType(type);
							faultSupportModelDetail.setCreaterName(jsonObject.getJSONObject("hitchSupport").getString("createrName"));
							faultSupportModelDetail.setElectricNetName(jsonObject.getJSONObject("hitchSupport").getString("electricNetName"));

							faultSupportModelDetail.setElectricRunTypeName(jsonObject.getJSONObject("hitchSupport").getString("electricRunTypeName"));
							faultSupportModelDetail.setSupportSortName(jsonObject.getJSONObject("hitchSupport").getString("supportSortName"));
							faultSupportModelDetail.setFactoryUserName(jsonObject.getJSONObject("hitchSupport").getString("factoryUserName"));
							faultSupportModelDetail.setProficientName(jsonObject.getJSONObject("hitchSupport").getString("proficientName"));

							if(type.equals("1")){
								faultSupportModelDetail.setType("一般");
							}
							if(type.equals("2")){
								faultSupportModelDetail.setType("严重");
							}
							if(type.equals("3")){
								faultSupportModelDetail.setType("隐患");
							}

							JSONArray jsonArray2=jsonObject.getJSONArray("attachmentList");
							List<AttachmentBean> attachmentBeans=new ArrayList<AttachmentBean>();
							for (int i = 0; i < jsonArray2.length(); i++) {
								AttachmentBean attachmentBean=new AttachmentBean();
								String path=jsonArray2.getJSONObject(i).getString("path");
								String name=jsonArray2.getJSONObject(i).getString("name");
								String parentId=jsonArray2.getJSONObject(i).getString("parentId");
								String id=jsonArray2.getJSONObject(i).getString("id");
								attachmentBean.setId(id);
								attachmentBean.setName(name);
								attachmentBean.setParentId(parentId);
								attachmentBean.setPath(path);
								attachmentBean.setType("1");
								attachmentBeans.add(attachmentBean);
							}

							Intent intent=new Intent();
							intent.setClass(getActivity(), FaultDetailActivity.class);
							Bundle bundle=new Bundle();
							bundle.putSerializable("falutSupportData", faultSupportModelDetail);
							bundle.putSerializable("attachmentList", (Serializable)attachmentBeans);
							bundle.putString("type", "故障详情");
							intent.putExtras(bundle);
							getActivity().startActivity(intent);
							getActivity().finish();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void HttpFail(int ErrCode) {
						progress.dismiss();
						Toast.makeText(getActivity(), "网络请求失败,请检查您的网络！", 1).show();
					}

				});
			}
			return false;
		}
	});

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.support_fragment, container, false);
		init();
		return view;
	}

	private void init(){
		refreshListView = (XListView)view.findViewById(R.id.support_listView); 
		newsBtn = (Button)view.findViewById(R.id.support_list_news_tips);

		refreshListView.setPullLoadEnable(true);//设置上拉加载
		refreshListView.setXListViewListener(this);
		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final FaultSupportModel faultSupportModel=faultSupportModels.get(position-1);
				fault_id=faultSupportModel.getId();
				new Thread(new Runnable() {

					@Override
					public void run() {
						mhaHandler.sendEmptyMessage(2);
					}
				}).start();
			}
		});
		new Thread(new Runnable() {

			@Override
			public void run() {
				mhaHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	/** 停止刷新， */
	private void onLoad() {
		refreshListView.stopRefresh();
		refreshListView.stopLoadMore();
		refreshListView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		onLoad();
	}

	@Override
	public void onLoadMore() {
		if(totalNum<num){
			Toast.makeText(getActivity(), "数据已全部加载完", 1).show();
			onLoad();
		}else{
			int pages=totalNum/num==0?totalNum/num:totalNum/num+1;
			if(pages>=curragePage){
				curragePage++;
			}
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mhaHandler.sendEmptyMessage(1);
				}
			}).start();
			onLoad();
		}
	}

	//从网络上获取数据进行解析
	private List<Map<String, Object>> getData(String _result){
		JSONObject jsonObject;
		String title = null;
		String description=null;
		String date=null;
		try {
			jsonObject = new JSONObject(_result);
			totalNum=Integer.parseInt(jsonObject.getString("total"));
			jsonObject.getString("status");
			System.out.println(_result);

			JSONArray jsonArray=jsonObject.getJSONArray("resultList");
			for(int j = 0, length = jsonArray.length(); j < length; j++){
				FaultSupportModel faultSupportModel=new FaultSupportModel();
				JSONObject jsonArray1=jsonArray.getJSONObject(j).getJSONObject("hitchSupport");
				title = jsonArray1.getString("title");
				description = jsonArray1.getString("description");
				date = jsonArray1.getString("createTime");
				String type = jsonArray1.getString("supportType");
				//设置故障支持信息
				faultSupportModel.setCreateTime(date);
				faultSupportModel.setDescription(description);
				faultSupportModel.setTitle(title);
				faultSupportModel.setId(jsonArray1.getString("id"));
				faultSupportModel.setCreaterName(jsonArray1.getString("createrName"));
				faultSupportModel.setElectricNetName(jsonArray1.getString("electricNetName"));
				faultSupportModel.setElectricRunTypeName(jsonArray1.getString("electricRunTypeName"));
				faultSupportModel.setSupportSortName(jsonArray1.getString("supportSortName"));
				faultSupportModel.setFactoryUserName(jsonArray1.getString("factoryUserName"));
				faultSupportModel.setProficientName(jsonArray1.getString("proficientName"));
				if(type.equals("1")){
					faultSupportModel.setType("一般");
				}
				if(type.equals("2")){
					faultSupportModel.setType("严重");
				}
				if(type.equals("3")){
					faultSupportModel.setType("隐患");
				}

				//				JSONArray jsonArray2=jsonArray.getJSONObject(j).getJSONArray("attachmentList");
				//				for (int i = 0; i < jsonArray2.length(); i++) {
				//					String path=jsonArray2.getJSONObject(i).getString("path");
				//					String name=jsonArray2.getJSONObject(i).getString("name");
				////					faultSupportModel.setAttachmentName(name);
				//				}

				faultSupportModels.add(faultSupportModel);

				Map<String, Object> map=new HashMap<String, Object>();
				map.put("support_title", title);
				map.put("support_content", description); 
				map.put("support_date", date); 
				listItems.add(map);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listItems;
	}

}
