package com.xj.cnooc.view.bbs;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.Toast;

import com.xj.cnooc.adapter.PostListViewAdapter;
import com.xj.cnooc.biz.UserHttpBiz;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.common.MyUtils;
import com.xj.cnooc.common.XListView;
import com.xj.cnooc.common.XListView.IXListViewListener;
import com.xj.cnooc.https.HttpDataCallBack;
import com.xj.cnooc.model.PostBean;
import com.xj.cnooc.view.LoginActivity;
import com.xj.cnooc.view.R;

/**
 * 已评论帖子界面
 * @author Administrator
 *
 */
public class CommentedPostFragment extends Fragment implements IXListViewListener
{
	private XListView refreshListView;
	private PostListViewAdapter adapter;
	private View view;
	
	private int totalNum = 0;//已评论帖子的总数量
	private int num = 10;//每页加载的帖子数量
	private int curragePage = 1;//当前页
	private int loginId;// 登录人id
	
	public static List<PostBean> commented_pList;
	
	private ProgressDialog dialog; 
	
	private static int REQUEST_CODE = 0x03;
	
	private int flagCode;
	private int postId;
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		view = inflater.inflate(R.layout.support_fragment, container, false);
		
		initView();
		
		setRetainInstance(true);
		
		return view;
	}

	private void initView() 
	{
		commented_pList = new ArrayList<PostBean>();
		refreshListView = (XListView) view.findViewById(R.id.support_listView);
		
		loginId = MyApp.globelUser.getAccountid();
		
		refreshListView.setPullLoadEnable(true);// 设置上拉加载
		refreshListView.setAdapter(adapter);
		refreshListView.setXListViewListener(this);// 上拉加载监听事件
		refreshListView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) 
			{
				handler.sendEmptyMessage(2);// 点击帖子后查询论坛首页数量
				postId = position - 1;
				PostBean post_bean = commented_pList.get(postId);
				Intent intent = new Intent(new Intent(getActivity(), PostDetailsActivity.class));
				Bundle bundle = new Bundle();
				bundle.putSerializable("PostBean", post_bean);
				bundle.putString("bbs_title", "已评论贴");
				intent.putExtras(bundle);
				startActivityForResult(intent, REQUEST_CODE);
			}
			
		});
		
		initListViewData();// 数据的初始化
	}
	
	private void initListViewData() 
	{
		new Thread()
		{
			@Override
			public void run() 
			{
				super.run();
				handler.sendEmptyMessage(0);// 查询前十条数据
			}
		}.start();
	}
	
	private Handler handler = new Handler(new Handler.Callback() 
	{
		@Override
		public boolean handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case 0:
					dialog = ProgressDialog.show(getActivity(), "", "数据正在加载中......");
					dialog.setCancelable(true);
					UserHttpBiz.queryCommentedPostList(curragePage, num, loginId, new HttpDataCallBack() 
					{
						@Override
						public void HttpSuccess(String _result)
						{
							dialog.dismiss();
							// 设置适配器
							adapter = new PostListViewAdapter(getCommentedPostData(_result), getActivity());
							if (totalNum < 10) 
							{
								refreshListView.setPullLoadEnable(false);
							}
							refreshListView.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}
						
						@Override
						public void HttpFail(int ErrCode) 
						{
							dialog.dismiss();
							Toast.makeText(getActivity(), "网络请求失败,请检查您的网络!", Toast.LENGTH_SHORT).show();
						}
					});
					break;
				case 1:
					UserHttpBiz.queryCommentedPostList(curragePage, num, loginId, new HttpDataCallBack() 
					{
						@Override
						public void HttpSuccess(String _result)
						{
							getCommentedPostData(_result);
							adapter.notifyDataSetChanged();
							int pages = totalNum / num == 0 ? totalNum / num : totalNum / num + 1;
							if(curragePage >= pages)
							{
								Toast.makeText(getActivity(), "数据已全部加载完", Toast.LENGTH_SHORT).show();
								onStopLoad();// 停止刷新
							}
						}
						
						@Override
						public void HttpFail(int ErrCode) 
						{
							Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
						}
					});
					break;
				case 2:
					UserHttpBiz.getBBSNum(MyApp.globelUser.getAccountid(), postId, new HttpDataCallBack()
					{
						@Override
						public void HttpSuccess(String _result) 
						{
							try
							{
								JSONObject object = new JSONObject(_result);
								if (object.getBoolean("status") == true) 
								{
									LoginActivity.bbsTotal = object.getInt("bbsTotal");
								}
							} 
							catch (JSONException e) 
							{
								e.printStackTrace();
							}
						}
						
						@Override
						public void HttpFail(int ErrCode) 
						{
							Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
						}
					});
					break;
	
			}
			return false;
		}
	});

	/**
	 * 停止刷新
	 */
	private void onStopLoad()
	{
		refreshListView.stopRefresh();
		refreshListView.stopLoadMore();
		refreshListView.setRefreshTime(MyUtils.getTime());
	}

	/** 下拉刷新 */
	@Override
	public void onRefresh() 
	{
		onStopLoad();
	}

	/** 加载更多 **/
	@Override
	public void onLoadMore() 
	{
		int pages = totalNum / num == 0 ? totalNum / num : totalNum / num + 1;
		if(pages >= curragePage)
		{
			curragePage++;
		}
		
		new Thread()
		{
			@Override
			public void run() 
			{
				super.run();
				handler.sendEmptyMessage(1);// 点击加载更多刷新加载数据
			}
		}.start();
		
		onStopLoad();
		
	}
	
	
	
	/** 解析josn将数据加到已评论的帖子集合中 */
	private List<PostBean> getCommentedPostData(String _result) 
	{
//		if (commented_pList.size() >0 && flagCode == 0x02) 
//		{
//			commented_pList.removeAll(commented_pList);  
//		}
		
		if (commented_pList.size() > 0) 
		{
			commented_pList.remove(commented_pList);  
		}
		
		try
		{
			JSONObject jsonObject = new JSONObject(_result);
			totalNum = jsonObject.getInt("total");
			if (jsonObject.getBoolean("status") == true) 
			{
				JSONArray jsonArray = jsonObject.getJSONArray("listBbsPost");
				for (int i = 0; i < jsonArray.length(); i++)
				{
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					PostBean bean = (PostBean) MyUtils.putJsonToObject(jsonObj, "com.xj.cnooc.model.PostBean");
					commented_pList.add(bean);
				}
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return commented_pList;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE)
		{
			if(resultCode == 0x03)
			{
//				flagCode = 0x03;
				initListViewData();
//				Toast.makeText(getActivity(), "CommentedPost返回结果", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
}
