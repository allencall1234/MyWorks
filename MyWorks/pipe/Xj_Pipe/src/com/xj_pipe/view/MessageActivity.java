package com.xj_pipe.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.xj_pipe.adapter.CommonAdapter;
import com.xj_pipe.adapter.ViewHolder;
import com.xj_pipe.base.BaseActivity;
import com.xj_pipe.base.MyApp;
import com.xj_pipe.bean.MessageBean;
import com.xj_pipe.bean.UserBean;
import com.xj_pipe.common.CharacterParser;
import com.xj_pipe.common.ClearEditText;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;
import com.xj_pipe.utils.JsonUtils;
import com.xj_pipe.utils.MyUtils;
import com.xj_pipe.utils.ToastUtils;
import com.xj_pipe.xlistview.XListView;
import com.xj_pipe.xlistview.XListView.IXListViewListener;

public class MessageActivity extends BaseActivity implements IXListViewListener {
	private XListView message_listView;
	private CommonAdapter adapter;
	private List<MessageBean> messageBeans = new ArrayList<MessageBean>();
	private List<UserBean> userBeans;
	private ClearEditText clearEditText;

	private TextView tv_message, tv_linkman ,total_linkmans,load_msg_tips;

	private List<TextView> textViews;
	private int[] drawables = {R.drawable.msg_n,R.drawable.linkman_n};
	private int[] drawables_s = {R.drawable.msg_s,R.drawable.linkman_s};

	private int totalNum = 0;// 数据库中帖子的总数量
	private int num = 10;// 每页加载的帖子数量
	private int curragePage = 1;// 当前页

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;

	public Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if(msg.what == 0){
				UserHttp.messageQuery("desc", curragePage, num, "id", MyApp.userInfo.getStaffId(), new HttpDataCallBack() {

					@Override
					public void HttpSuccess(String _result) {
						messageBeans.clear();
						getMessageListData(_result);
						if (totalNum <= num) {
							// 隐藏加载更多
							message_listView.setPullLoadEnable(false);
						}

						if (totalNum <= 0) {
							message_listView.setVisibility(View.GONE);
							load_msg_tips.setVisibility(View.VISIBLE);
						} else {
							message_listView.setVisibility(View.VISIBLE);
							load_msg_tips.setVisibility(View.GONE);
						}
						adapter = new CommonAdapter<MessageBean>(
								getApplicationContext(), messageBeans, R.layout.message_listview_item_detail) {

							@Override
							public void convert(ViewHolder viewHolder,
									MessageBean item) {
								viewHolder.setText(R.id.send_message_name,item.getCreaterName()); 
								viewHolder.setText(R.id.message_time,item.getCreatetime());
								viewHolder.setText(R.id.message_content, item.getContent());
								TextView message_flag;
								message_flag = viewHolder.getView(R.id.message_flag);
								if(item.getState()==1){
									message_flag.setBackgroundResource(R.drawable.state1);
									message_flag.setText("已读");
								}else{
									message_flag.setBackgroundResource(R.drawable.state0);
									message_flag.setText("未读");
								}
							}
						};
						message_listView.setAdapter(adapter);
					}

					@Override
					public void HttpFail(int ErrCode) {
						showToast("网络连接失败，请检查您的网络！");
					}
				});

			}else if(msg.what == 1){
				message_listView.setVisibility(View.VISIBLE);
				UserHttp.contectPersonQuery(MyApp.userInfo.getStaffId(), new HttpDataCallBack() {

					@Override
					public void HttpSuccess(String _result) {
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(_result);
							boolean status = jsonObject.getBoolean("status");
							if(status){
								int staffListNum = jsonObject.getInt("staffListNum");
								total_linkmans.setText("共"+staffListNum+"个联系人");
								JSONArray linkmans = jsonObject.getJSONArray("staffList");
								userBeans  = new ArrayList<UserBean>();
								for (int i = 0; i < linkmans.length(); i++) {
									JSONObject object2 = linkmans.getJSONObject(i);
									UserBean bean = (UserBean) JsonUtils
											.putJsonToObject(object2,
													"com.xj_pipe.bean.UserBean");

									userBeans.add(bean);
								}
								adapter = new CommonAdapter<UserBean>(getApplicationContext(),
										userBeans, R.layout.linkman_listview_item_detail) {

									@Override
									public void convert(final ViewHolder viewHolder, final UserBean item) {
										// TODO Auto-generated method stub
										viewHolder.setText(R.id.linkman_name, item.getName());
										viewHolder.setText(R.id.linkman_position, item.getRoleName());
										viewHolder.setChecked(R.id.linkman_flag, item.isFlag());
										viewHolder.setListener(R.id.linkman_item, new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method stub
												boolean flag = item.isFlag();
												item.setFlag(!flag);
												viewHolder.setChecked(R.id.linkman_flag, !flag);
											}
										});
									}
								};
								load_msg_tips.setVisibility(View.GONE);
								message_listView.setAdapter(adapter);

							}else{
								adapter = null;
								message_listView.setAdapter(adapter);
								adapter.notifyDataSetChanged();
								showToast("获取联系人异常！");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void HttpFail(int ErrCode) {
						adapter = null;
						message_listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						showToast("网络请求失败！");
					}
				});
			}else if(msg.what == 2){
				UserHttp.messageQuery("desc", curragePage, num, "id", MyApp.userInfo.getStaffId(), new HttpDataCallBack() {

					@Override
					public void HttpSuccess(String _result) {
						getMessageListData(_result);
						adapter.notifyDataSetChanged();
						
						int pages = totalNum / num == 0 ? totalNum
								/ num : totalNum / num + 1;

						if (curragePage >= pages) {
							// 停止刷新
							onStopLoad();
							// 隐藏加载更多
							message_listView.setPullLoadEnable(false);
							ToastUtils.ShowMessage(
									MessageActivity.this,
									"加载完成，没有更多内容!");

						}
					}

					@Override
					public void HttpFail(int ErrCode) {
						showToast("网络连接失败，请检查您的网络！");
					}
				});
			}
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_message);
		initViews();
	}

	private void initViews() {
		setCenterTitle("消息");
		hideLeftLogo();

		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		message_listView = (XListView) findViewById(R.id.message_listView);
		tv_message = (TextView) findViewById(R.id.tv_message);
		tv_linkman = (TextView) findViewById(R.id.tv_linkman);
		total_linkmans = (TextView) findViewById(R.id.total_linkmans);

		textViews = new ArrayList<TextView>();
		textViews.add(tv_message);
		textViews.add(tv_linkman);

		clearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		load_msg_tips = (TextView) findViewById(R.id.load_msg_tips);

		tv_message.setOnClickListener(this);
		tv_linkman.setOnClickListener(this);

		message_listView.setPullLoadEnable(true);
		message_listView.setXListViewListener(this);

		message_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (getRightTitle().getText().equals("更多")) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							MessageDetailActivity.class);
					intent.putExtra("messageBeans", messageBeans.get(arg2 - 1));
					startActivity(intent);
				}
			}
		});

		//根据输入框输入值的改变来过滤搜索
		clearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	@SuppressWarnings("unchecked")
	private void filterData(String filterStr){
		List<UserBean> filterDateList = new ArrayList<UserBean>();

		if(TextUtils.isEmpty(filterStr)){
			filterDateList = userBeans;
		}else{
			filterDateList.clear();
			for(UserBean sortModel : userBeans){
				String name = sortModel.getName();
				String position = sortModel.getRoleName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString()) || position.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(position).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		//		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (getRightTitle().getText().equals("更多")) {
			handler.sendEmptyMessage(0);
		}
	}

	@Override
	public void onBackLisenter() {
		super.onBackLisenter();
		finish();
	}

	@Override
	public void onMoreLisenter() {
		if (getRightTitle().getText().equals("发消息")) {
			String str_send_message_person = "";
			String receiver = "";
			for (UserBean linkmanBean : userBeans) {
				if (linkmanBean.isFlag()) {
					str_send_message_person += linkmanBean.getName()
							+ ",";
					receiver += linkmanBean.getStaffId() + ",";
				}
			}
			if (TextUtils.isEmpty(str_send_message_person)) {
				showToast("请选择联系人！");
			} else {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						SendMessageActivity.class);
				intent.putExtra("person", str_send_message_person);
				intent.putExtra("receiver", receiver);
				startActivity(intent);
			}
		} else {
			super.onMoreLisenter();
		}
	}

	@Override
	public void onRefresh() {
		onStopLoad();
	}

	@Override
	public void onLoadMore() {
		int pages = totalNum / num == 0 ? totalNum / num : totalNum / num + 1;
		if (pages >= curragePage) {
			curragePage++;
		}
		handler.sendEmptyMessage(2);
		onStopLoad();
	}

	/** 停止刷新 */
	private void onStopLoad() {
		message_listView.stopRefresh();
		message_listView.stopLoadMore();
		message_listView.setRefreshTime(MyUtils.getTime());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_message:
			setRightTitle("更多");
			changeColor(tv_message);
			clearEditText.setVisibility(View.GONE);
			total_linkmans.setVisibility(View.GONE);
			messageBeans.clear();
			curragePage = 1;
			message_listView.setPullLoadEnable(true);
			handler.sendEmptyMessage(0);
			break;
		case R.id.tv_linkman:
			setRightTitle("发消息");
			changeColor(tv_linkman);
			clearEditText.setVisibility(View.VISIBLE);
			total_linkmans.setVisibility(View.VISIBLE);
			message_listView.setPullLoadEnable(false);
			handler.sendEmptyMessage(1);
			break;
		default:
			break;
		}
		super.onClick(view);
	}

	private List<MessageBean> getMessageListData(String result) {

//		System.out.println("result = " + result);
//		messageBeans.clear();
		try {
			JSONObject object = new JSONObject(result);

			totalNum = Integer.parseInt(object.getString("total"));

			JSONArray array = object.getJSONArray("messageList");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				MessageBean bean = (MessageBean) JsonUtils
						.putJsonToObject(object2,
								"com.xj_pipe.bean.MessageBean");

				messageBeans.add(bean);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return messageBeans;
	}

	/**改变底部banner按钮背景*/
	private void changeColor(TextView textView){
		Drawable drawable = null ;
		for (int i = 0; i < 2; i++) {
			if(textView.getId() == textViews.get(i).getId()){
				drawable = getResources().getDrawable(drawables_s[i]); 
				textView.setTextColor(Color.parseColor("#0B52AE"));
			}else{
				drawable = getResources()
						.getDrawable(drawables[i]); 
				textViews.get(i).setTextColor(
						Color.parseColor("#A2A5A8")
						);
			}
			drawable.setBounds(
					0, 0, 
					drawable.getMinimumWidth(), 
					drawable.getMinimumHeight()
					);
			textViews.get(i).setCompoundDrawables(
					null, drawable, null, null
					);
		}
	}

	//	private String getCreaterName(int id){
	//		return null;
	//	}
}
