package com.xj.cnooc.view.bbs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.cnooc.adapter.PostListViewAdapter;
import com.xj.cnooc.biz.UserHttpBiz;
import com.xj.cnooc.common.BaseActivity;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.common.MyUtils;
import com.xj.cnooc.common.XListView;
import com.xj.cnooc.common.XListView.IXListViewListener;
import com.xj.cnooc.https.HttpDataCallBack;
import com.xj.cnooc.model.BBSBean;
import com.xj.cnooc.model.PostBean;
import com.xj.cnooc.view.R;

/**
 * 
 * @author Administrator 技术区版块Activity:综保、变压器技术讨论区等
 *
 */
public class PostListViewActivity extends BaseActivity implements
		OnClickListener, IXListViewListener {
	private XListView refreshListView;
	private TextView tv_empty;
	private Context context;
	// private int bbs_position;// 上一个界面的点击的论坛下标
	private BBSBean bbsBean;

	private int totalNum = 0;// 数据库中帖子的总数量
	private int num = 10;// 每页加载的帖子数量
	private int curragePage = 1;// 当前页
	public static int bbsModuleId = -1;// 版块id
	private int bbs_count;// 版块帖子数量

	private int flagCode;
	private int postId;// 点击帖子的id

	// 标题栏
	// private Button btn_back;
	// private RelativeLayout rellayout_back;
	// private RelativeLayout rellayout_setting;
	private ImageView iv_back;
	private ImageView iv_setting;
	private TextView tv_center_title;// 中间标题
	// private Button btn_setting;// 设置按钮

	private PostListViewAdapter adapter;
	private List<PostBean> post_list;

	private static int REQUEST_CODE = 0x01;

	public static String bbs_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void initView() {
		// 获取传递过来的对象
		bbsBean = (BBSBean) getIntent().getSerializableExtra("BBSBean");
		// bbs_position = getIntent().getIntExtra("bbs_position", bbs_position);

		setContentView(R.layout.discuss_public_view);

		context = PostListViewActivity.this;

		post_list = new ArrayList<PostBean>();

		refreshListView = (XListView) findViewById(R.id.xlv_post_list);
		tv_empty = (TextView) findViewById(R.id.tv_empty);

		// btn_back = (Button) findViewById(R.id.btn_back);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_setting = (ImageView) findViewById(R.id.iv_setting);
		tv_center_title = (TextView) findViewById(R.id.tv_center_title);
		// btn_setting = (Button) findViewById(R.id.btn_setting);

		// for (int i = 0; i < MyApp.bbsTitle.size(); i++)
		// {
		// if (bbs_position == i)
		// {
		// BBSBean bean = MyApp.bbsTitle.get(i);
		// bbsModuleId = bean.getId();// 版块id
		// totalNum = bean.getCount();// 帖子总的数量
		// tv_center_title.setText(bean.getName());
		// break;
		// }
		// }

		bbsModuleId = bbsBean.getId();// 版块id
		totalNum = bbsBean.getCount();// 帖子总的数量
		bbs_title = bbsBean.getName();
		tv_center_title.setText(bbsBean.getName());

		// btn_back.setOnClickListener(this);
		// btn_setting.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		iv_setting.setOnClickListener(this);

		refreshListView.setPullLoadEnable(true);// 设置上拉加载
		refreshListView.setXListViewListener(this);
		refreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				handler.sendEmptyMessage(2);

				Intent intent = new Intent(context, PostDetailsActivity.class);
				postId = position - 1;
				PostBean post_bean = post_list.get(postId);
				Bundle bundle = new Bundle();
				bundle.putSerializable("PostBean", post_bean);
				bundle.putString("bbs_title", bbsBean.getName());
				intent.putExtras(bundle);
				startActivityForResult(intent, REQUEST_CODE);
				// startActivity(intent);

			}

		});

		initListViewData();// 初始化数据

	}

	private void initListViewData() {
		handler.sendEmptyMessage(0);
	}

	// 按钮事件监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			setResult(0x07);
			finish();
			break;
		case R.id.iv_setting:
			showSettingActivity();
			break;
		// case R.id.btn_back:
		// finish();
		// break;
		// case R.id.btn_setting:
		// startActivity(new Intent(context, SettingActivity.class));
		// break;
		}
	}

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(final Message msg) {
			switch (msg.what) {
			case 0:
				UserHttpBiz.queryPostList(curragePage, num, bbsModuleId, "",
						"", new HttpDataCallBack() {
							@Override
							public void HttpSuccess(String _result) {
								if (totalNum < num) {
									refreshListView.setPullLoadEnable(false);
								}
								// 设置适配器
								adapter = new PostListViewAdapter(
										getPostListData(_result), context);
								int count = adapter.getCount();
								if (count <= 0) {
									refreshListView.setVisibility(View.GONE);
									tv_empty.setVisibility(View.VISIBLE);
								}
								refreshListView.setAdapter(adapter);
							}

							@Override
							public void HttpFail(int ErrCode) {
								Toast.makeText(context, "网络请求失败,请检查您的网络!",
										Toast.LENGTH_SHORT).show();
							}
						});
				break;
			case 1:
				UserHttpBiz.queryPostList(curragePage, num, bbsModuleId, "",
						"", new HttpDataCallBack() {
							@Override
							public void HttpSuccess(String _result) {
								getPostListData(_result);
								adapter.notifyDataSetChanged();
								int pages = totalNum / num == 0 ? totalNum
										/ num : totalNum / num + 1;
								if (curragePage >= pages) {
									Toast.makeText(context, "数据已全部加载完",
											Toast.LENGTH_SHORT).show();
									onStopLoad();// 停止刷新
								}
							}

							@Override
							public void HttpFail(int ErrCode) {
								Toast.makeText(context, "网络请求失败,请检查您的网络!",
										Toast.LENGTH_SHORT).show();
							}
						});
				break;
			case 2:
				UserHttpBiz.getBBSNum(MyApp.globelUser.getAccountid(), postId,
						new HttpDataCallBack() {
							@Override
							public void HttpSuccess(String _result) {
								// try
								// {
								// JSONObject object = new JSONObject(_result);
								// if (object.getBoolean("status") == true)
								// {
								// LoginActivity.bbsTotal =
								// object.getInt("bbsTotal");
								// // Intent intent = new Intent();
								// //
								// intent.setAction("MainGridViewActivityUpdate");
								// // intent.putExtra("position", 4);
								// // intent.putExtra("bbsTotal",
								// LoginActivity.bbsTotal);
								// // sendBroadcast(intent);
								// }
								// }
								// catch (JSONException e)
								// {
								// e.printStackTrace();
								// }
								MyApp.bbsTotal = 0;
								Log.i("msg", "PostListViewActivity : bbsTotal="
										+ MyApp.bbsTotal);

							}

							@Override
							public void HttpFail(int ErrCode) {
								Toast.makeText(context, "网络请求失败,请检查您的网络!",
										Toast.LENGTH_SHORT).show();
							}
						});
				break;
			default:
				break;
			}
			return false;
		}

	});

	// 下拉刷新
	@Override
	public void onRefresh() {
		onStopLoad();
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		int pages = totalNum / num == 0 ? totalNum / num : totalNum / num + 1;
		if (pages >= curragePage) {
			curragePage++;
		}

		handler.sendEmptyMessage(1);

		onStopLoad();
	}

	/** 停止刷新 */
	private void onStopLoad() {
		refreshListView.stopRefresh();
		refreshListView.stopLoadMore();
		refreshListView.setRefreshTime(MyUtils.getTime());
	}

	// 将获取的帖子列表的json数据解析保存到集合中
	private List<PostBean> getPostListData(String result) {
		// 防止界面数据更新时多次加载数据
		// if (post_list.size() > 0 && flagCode == 0x01)
		// {
		// post_list.removeAll(post_list);
		// }
		if (post_list.size() > 0) {
			post_list.remove(post_list);
		}

		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.getBoolean("status") == true) {
				JSONArray jsonArray = jsonObject.getJSONArray("listBbsPost");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					PostBean bean = (PostBean) MyUtils.putJsonToObject(object,
							"com.xj.cnooc.model.PostBean");
					post_list.add(bean);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		MyApp.post_list = post_list;
		return post_list;
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// startActivity(new Intent(PostListViewActivity.this,
			// BBSFragmentActivity.class));
			setResult(0x07);
			this.finish();
		}

		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
			if (resultCode == 0x01) {
				// flagCode = 0x01;
				initListViewData();
				post_list.clear();
				;
				adapter.notifyDataSetInvalidated();
				// Toast.makeText(context, "PostListView返回结果",
				// Toast.LENGTH_SHORT).show();
			}
		}
	}

}
