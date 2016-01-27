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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.cnooc.adapter.PostListViewAdapter;
import com.xj.cnooc.biz.UserHttpBiz;
import com.xj.cnooc.common.CustomPopupWindow;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.common.MyUtils;
import com.xj.cnooc.common.XListView;
import com.xj.cnooc.common.XListView.IXListViewListener;
import com.xj.cnooc.https.HttpDataCallBack;
import com.xj.cnooc.model.PostBean;
import com.xj.cnooc.view.R;

public class MyCollectionFragment extends Fragment implements
		IXListViewListener {
	private XListView refreshListView;
	private TextView tv_empty;
	private View view;

	private int totalNum = 0;// 已评论帖子的总数量
	private int num = 10;// 每页加载的帖子数量
	private int curragePage = 1;// 当前页my
	private int loginId;// 登录人id
	private int postId;

	private PostListViewAdapter adapter;
	public static List<PostBean> collection_list;
	private PostBean post_bean;

	private int longClickPosition;

	ProgressDialog dialog;

	private int flag_code;

	private static int REQUEST_CODE = 0x02;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.support_fragment, container, false);

		initView();// 绑定界面控件

		setRetainInstance(true);

		return view;
	}

	private void initView() {
		collection_list = new ArrayList<PostBean>();
		refreshListView = (XListView) view.findViewById(R.id.support_listView);
		tv_empty = (TextView) view.findViewById(R.id.tv_empty);

		loginId = MyApp.globelUser.getAccountid();

		// 设置上拉加载
		refreshListView.setPullLoadEnable(true);
		// refreshListView.setAdapter(new DiscussionListViewAdapter(list,
		// getActivity()));

		// 设置上拉加载事件监听
		refreshListView.setXListViewListener(this);
		refreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				Intent intent = new Intent(getActivity(),
						PostDetailsActivity.class);
				post_bean = collection_list.get(position - 1);
				int current = position - 1;
				Bundle bundle = new Bundle();
				bundle.putSerializable("PostBean", post_bean);
				bundle.putString("bbs_title", "我的收藏");
				bundle.putInt("position", current);
				intent.putExtras(bundle);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});

		refreshListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View view, final int position, long arg3) {
						longClickPosition = position - 1;
						post_bean = collection_list.get(longClickPosition);
						postId = post_bean.getId();
						new CustomPopupWindow(getActivity(), view) {
							@Override
							public void onRightClick()// 删除收藏
							{
								handler.sendEmptyMessage(2);
							}

							@Override
							public void onLeftClick()// 取消
							{

							}
						};

						return true;
						// View view1 =
						// LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog,
						// null);
						// TextView tv_cancel = (TextView)
						// view1.findViewById(R.id.tv_cancel);
						// TextView tv_delete = (TextView)
						// view1.findViewById(R.id.tv_delete);
						// //对话框
						// final Dialog dialog = new
						// AlertDialog.Builder(getActivity()).create();
						// dialog.show();
						// dialog.getWindow().setContentView(view1);
						// int[] location = new int[2];
						// // 获取当前view在屏幕中的绝对位置
						// // ,location[0]表示view的x坐标值,location[1]表示view的坐标值
						// view.getLocationOnScreen(location);
						// DisplayMetrics displayMetrics = new DisplayMetrics();
						// Display display =
						// getActivity().getWindowManager().getDefaultDisplay();
						// display.getMetrics(displayMetrics);
						// WindowManager.LayoutParams params =
						// dialog.getWindow().getAttributes();
						// params.gravity = Gravity.BOTTOM;
						// params.dimAmount = 0f;
						// params.x = display.getWidth() - location[0];
						// params.y = (int) (display.getHeight() - location[1] *
						// 1.6);
						// dialog.getWindow().setAttributes(params);
						// dialog.setCanceledOnTouchOutside(true);
						//
						// tv_delete.setOnClickListener(new OnClickListener()
						// {
						// @Override
						// public void onClick(View v)
						// {
						// dialog.dismiss();
						// new Thread()
						// {
						// public void run()
						// {
						// handler.sendEmptyMessage(2);
						// };
						// }.start();
						// }
						//
						// });
						//
						// tv_cancel.setOnClickListener(new OnClickListener()
						// {
						// @Override
						// public void onClick(View v)
						// {
						// dialog.dismiss();
						// }
						// });
					}
				});

		// 初始化数据
		initListViewData();
	}

	private void initListViewData() {
		handler.sendEmptyMessage(0);
	}

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dialog = ProgressDialog
						.show(getActivity(), "", "数据正在加载中......");
				dialog.setCancelable(true);
				UserHttpBiz.queryCollectionPostList(curragePage, num, loginId,
						new HttpDataCallBack() {
							@Override
							public void HttpSuccess(String _result) {
								dialog.dismiss();
								System.out.println(_result);
								adapter = new PostListViewAdapter(
										getCollectionPostData(_result),
										getActivity());
								if (totalNum < 10) {
									refreshListView.setPullLoadEnable(false);
								}
								if (adapter.getCount() <= 0) {
									refreshListView.setVisibility(View.GONE);
									tv_empty.setVisibility(View.VISIBLE);
								}
								refreshListView.setAdapter(adapter);
							}

							@Override
							public void HttpFail(int ErrCode) {
								dialog.dismiss();
								Toast.makeText(getActivity(),
										"网络请求失败,请检查您的网络!", Toast.LENGTH_SHORT)
										.show();
							}
						});
				break;
			case 1:
				UserHttpBiz.queryCollectionPostList(curragePage, num, loginId,
						new HttpDataCallBack() {
							@Override
							public void HttpSuccess(String _result) {
								getCollectionPostData(_result);
								adapter.notifyDataSetChanged();
								int pages = totalNum / num == 0 ? totalNum
										/ num : totalNum / num + 1;
								if (curragePage >= pages) {
									Toast.makeText(getActivity(), "数据已全部加载完",
											Toast.LENGTH_SHORT).show();
									onStopLoad();// 停止刷新
								}
							}

							@Override
							public void HttpFail(int ErrCode) {
								Toast.makeText(getActivity(),
										"网络请求失败,请检查您的网络!", Toast.LENGTH_SHORT)
										.show();
							}
						});
				break;
			case 2:
				UserHttpBiz.deleteCollectionPost(postId, loginId,
						new HttpDataCallBack() {
							@Override
							public void HttpSuccess(String _result) {
								try {
									JSONObject jsonObject = new JSONObject(
											_result);
									if (jsonObject.getBoolean("status") == true) {
										collection_list
												.remove(longClickPosition);
										adapter.notifyDataSetChanged();
										Toast.makeText(getActivity(), "删除成功",
												Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}

							@Override
							public void HttpFail(int ErrCode) {
								Toast.makeText(getActivity(),
										"网络请求失败,请检查您的网络!", Toast.LENGTH_SHORT)
										.show();
							}
						});
				break;
			}
			return false;
		}
	});

	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		onStopLoad();
	}

	/**
	 * 加载更多
	 */
	@Override
	public void onLoadMore() {
		int pages = totalNum / num == 0 ? totalNum / num : totalNum / num + 1;
		if (pages >= curragePage) {
			curragePage++;
		}

		handler.sendEmptyMessage(1);

		onStopLoad();
	}

	/**
	 * 停止刷新
	 */
	private void onStopLoad() {
		refreshListView.stopRefresh();
		refreshListView.stopLoadMore();
		refreshListView.setRefreshTime(MyUtils.getTime());
	}

	private List<PostBean> getCollectionPostData(String _result) {
		// if (collection_list.size() >0 && flagCode == 0x02)
		// {
		// collection_list.removeAll(collection_list);
		// }

		if (collection_list.size() > 0) {
			collection_list.remove(collection_list);
		}

		try {

			JSONObject jsonObject = new JSONObject(_result);
			totalNum = jsonObject.getInt("total");// 获取收藏帖子总数

			if (jsonObject.getBoolean("status") == true) {
				JSONArray jsonArray = jsonObject.getJSONArray("bbsStoreList");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					PostBean bean = (PostBean) MyUtils.putJsonToObject(object,
							"com.xj.cnooc.model.PostBean");
					collection_list.add(bean);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return collection_list;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
			if (resultCode == 0x02) {
				// flagCode = 0x02;
				initListViewData();
				collection_list.clear();
				adapter.notifyDataSetInvalidated();
				// Toast.makeText(getActivity(), "MyCollection返回结果",
				// Toast.LENGTH_SHORT).show();
			}
		}
	}

}
