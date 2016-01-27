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
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.cnooc.adapter.KnowledgeBaseListAdapter;
import com.xj.cnooc.biz.UserHttpBiz;
import com.xj.cnooc.common.BaseActivity;
import com.xj.cnooc.common.MyApp;
import com.xj.cnooc.common.XListView;
import com.xj.cnooc.common.XListView.IXListViewListener;
import com.xj.cnooc.https.HttpDataCallBack;
import com.xj.cnooc.model.AttachmentBean;
import com.xj.cnooc.model.FaultSupportModel;
import com.xj.cnooc.model.FeedBackModel;
import com.xj.cnooc.model.KnowledgeBaseModel;
import com.xj.cnooc.model.PingYiModel;
import com.xj.cnooc.view.R;

/**
 * 电网知识库界面
 * 
 * @author qinfan
 *
 *         2015-9-7
 */
public class KnowledgeBaseActivity extends BaseActivity implements
		IXListViewListener, OnClickListener {
	// private Button back_btn;
	// private Button btn_setting;
	// private RelativeLayout rellayout_back;
	// private RelativeLayout rellayout_setting;
	private ImageView iv_back;
	private ImageView iv_setting;
	private TextView tv_title;
	private EditText et_seacher;
	private XListView refreshListView;
	private KnowledgeBaseListAdapter adapter;
	private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	private List<KnowledgeBaseModel> knowledgeBaseModels = new ArrayList<KnowledgeBaseModel>();
	private List<FaultSupportModel> faultSupportModels = new ArrayList<FaultSupportModel>();
	private FaultSupportModel faultSupportModelDetail;
	private ProgressDialog progress;

	private int totalNum;// 从数据库中获取的数据总数
	private int num = 10;// 每页加载的数据数量
	private int curragePage = 1;// 当前页

	String knowledgeType = null;
	int knowledgeId = 0;

	Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				progress = ProgressDialog.show(KnowledgeBaseActivity.this, "",
						"正在加载数据,请稍候...");
				UserHttpBiz.getKnowledgeBseListData(curragePage, num, "",
						new HttpDataCallBack() {

							@Override
							public void HttpSuccess(String _result) {
								progress.dismiss();
								adapter = new KnowledgeBaseListAdapter(
										KnowledgeBaseActivity.this,
										getData(_result));
								if (totalNum <= 10) {
									refreshListView.setPullLoadEnable(false);
								}
								refreshListView.setAdapter(adapter);
							}

							@Override
							public void HttpFail(int ErrCode) {
								progress.dismiss();
								Toast.makeText(KnowledgeBaseActivity.this,
										"网络请求失败,请检查您的网络！", 1).show();
							}
						});
				break;

			case 1:
				UserHttpBiz.getKnowledgeBseListData(curragePage, num, "",
						new HttpDataCallBack() {

							@Override
							public void HttpSuccess(String _result) {
								getData(_result);
								adapter.notifyDataSetChanged();
								int pages = totalNum / num == 0 ? totalNum
										/ num : totalNum / num + 1;
								if (curragePage >= pages) {
									Toast.makeText(KnowledgeBaseActivity.this,
											"数据已全部加载完", 1).show();
								}
							}

							@Override
							public void HttpFail(int ErrCode) {
								progress.dismiss();
								Toast.makeText(KnowledgeBaseActivity.this,
										"网络请求失败,请检查您的网络！", 1).show();
							}
						});
				break;

			case 2:
				UserHttpBiz.getKnowledgeBaseDetailData(knowledgeType,
						knowledgeId, new HttpDataCallBack() {

							@Override
							public void HttpSuccess(String _result) {
								if (knowledgeType.equals("1")
										|| knowledgeType.equals("2")) {
									try {
										JSONObject jsonObject = new JSONObject(
												_result);

										faultSupportModelDetail = new FaultSupportModel();

										if (knowledgeType.equals("1")) {
											faultSupportModelDetail
													.setTitle(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString("title"));
											faultSupportModelDetail
													.setDescription(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString(
																	"description"));
											faultSupportModelDetail
													.setCreateTime(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString(
																	"createTime"));
											String type = jsonObject
													.getJSONObject(
															"hitchSupport")
													.getString("supportType");
											faultSupportModelDetail
													.setType(type);
											faultSupportModelDetail
													.setCreaterName(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString(
																	"createrName"));
											faultSupportModelDetail
													.setElectricNetName(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString(
																	"electricNetName"));

											faultSupportModelDetail
													.setElectricRunTypeName(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString(
																	"electricRunTypeName"));
											faultSupportModelDetail
													.setSupportSortName(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString(
																	"supportSortName"));
											faultSupportModelDetail
													.setFactoryUserName(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString(
																	"factoryUserName"));
											faultSupportModelDetail
													.setProficientName(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString(
																	"proficientName"));
											faultSupportModelDetail
													.setSuperName(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString(
																	"superName"));
											faultSupportModelDetail
													.setCompeten_advice(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString(
																	"competen_advice"));
											faultSupportModelDetail
													.setCompeten_grade(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString(
																	"competen_grade"));
											faultSupportModelDetail
													.setConclusion(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString(
																	"conclusion"));
											faultSupportModelDetail
													.setApprovalContent(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString(
																	"approvalContent"));
											faultSupportModelDetail
													.setApprovalUserName(jsonObject
															.getJSONObject(
																	"hitchSupport")
															.getString(
																	"approvalUserName"));

											if (type.equals("1")) {
												faultSupportModelDetail
														.setType("一般");
											}
											if (type.equals("2")) {
												faultSupportModelDetail
														.setType("严重");
											}
											if (type.equals("3")) {
												faultSupportModelDetail
														.setType("隐患");
											}
										}

										if (knowledgeType.equals("2")) {
											faultSupportModelDetail
													.setTitle(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString("title"));
											faultSupportModelDetail
													.setDescription(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString(
																	"description"));
											faultSupportModelDetail
													.setCreateTime(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString(
																	"createTime"));
											String type = jsonObject
													.getJSONObject(
															"assessmentSupport")
													.getString("supportType");
											faultSupportModelDetail
													.setType(type);
											faultSupportModelDetail
													.setCreaterName(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString(
																	"createrName"));
											faultSupportModelDetail
													.setElectricNetName(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString(
																	"electricNetName"));

											faultSupportModelDetail
													.setElectricRunTypeName(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString(
																	"electricRunTypeName"));
											faultSupportModelDetail
													.setSupportSortName(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString(
																	"supportSortName"));
											faultSupportModelDetail
													.setFactoryUserName(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString(
																	"factoryUserName"));
											faultSupportModelDetail
													.setProficientName(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString(
																	"proficientName"));
											faultSupportModelDetail
													.setSuperName(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString(
																	"superName"));
											faultSupportModelDetail
													.setCompeten_advice(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString(
																	"competen_advice"));
											faultSupportModelDetail
													.setCompeten_grade(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString(
																	"competen_grade"));
											faultSupportModelDetail
													.setConclusion(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString(
																	"conclusion"));
											faultSupportModelDetail
													.setApprovalContent(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString(
																	"approvalContent"));
											faultSupportModelDetail
													.setApprovalUserName(jsonObject
															.getJSONObject(
																	"assessmentSupport")
															.getString(
																	"approvalUserName"));

											if (type.equals("1")) {
												faultSupportModelDetail
														.setType("运行风险");
											}
											if (type.equals("2")) {
												faultSupportModelDetail
														.setType("操作风险");
											}
											if (type.equals("3")) {
												faultSupportModelDetail
														.setType("二次评估");
											}
										}

										JSONArray jsonArray2 = jsonObject
												.getJSONArray("attachmentList");
										List<AttachmentBean> attachmentBeans = new ArrayList<AttachmentBean>();
										for (int i = 0; i < jsonArray2.length(); i++) {
											AttachmentBean attachmentBean = new AttachmentBean();
											String path = jsonArray2
													.getJSONObject(i)
													.getString("path");
											String name = jsonArray2
													.getJSONObject(i)
													.getString("name");
											String parentId = jsonArray2
													.getJSONObject(i)
													.getString("parentId");
											String id = jsonArray2
													.getJSONObject(i)
													.getString("id");
											attachmentBean.setId(id);
											attachmentBean.setName(name);
											attachmentBean
													.setParentId(parentId);
											attachmentBean.setPath(path);
											attachmentBean.setType("1");
											attachmentBeans.add(attachmentBean);
										}

										JSONArray jsonArray = jsonObject
												.getJSONArray("reviewList");
										List<PingYiModel> list = new ArrayList<PingYiModel>();
										for (int i = 0; i < jsonArray.length(); i++) {
											PingYiModel pingYiModel = new PingYiModel();
											pingYiModel
													.setCreaterName(jsonArray
															.getJSONObject(i)
															.getString(
																	"createrName"));
											pingYiModel.setContent(jsonArray
													.getJSONObject(i)
													.getString("content"));
											pingYiModel.setCreateTime((jsonArray
													.getJSONObject(i)
													.getString("createTime")));
											pingYiModel.setIsAdopted((Integer
													.parseInt(jsonArray
															.getJSONObject(i)
															.getString(
																	"isAdopted"))));
											list.add(pingYiModel);
										}

										JSONArray jsonArray1 = jsonObject
												.getJSONArray("ticklingList");
										List<FeedBackModel> feedBackModels = new ArrayList<FeedBackModel>();
										for (int i = 0; i < jsonArray1.length(); i++) {
											FeedBackModel feedBackModel = new FeedBackModel();
											feedBackModel
													.setCreateName(jsonArray1
															.getJSONObject(i)
															.getString(
																	"createrName"));
											feedBackModel.setContent(jsonArray1
													.getJSONObject(i)
													.getString("content"));
											feedBackModel.setCreateTime((jsonArray1
													.getJSONObject(i)
													.getString("createTime")));
											feedBackModels.add(feedBackModel);
										}

										Intent intent = new Intent();
										intent.setClass(
												KnowledgeBaseActivity.this,
												KnowledgeBaseDetailActivity.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable(
												"falutSupportData",
												faultSupportModelDetail);
										bundle.putSerializable("pingyiList",
												(Serializable) list);
										bundle.putSerializable("feedbackList",
												(Serializable) feedBackModels);
										bundle.putSerializable(
												"attachmentList",
												(Serializable) attachmentBeans);
										bundle.putString("type", knowledgeType);
										intent.putExtras(bundle);
										KnowledgeBaseActivity.this
												.startActivity(intent);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}

								if (knowledgeType.equals("3")
										|| knowledgeType.equals("4")) {
									String videoTitle = null;
									String createName = null;
									String createTime = null;
									String attachmentPath = null;
									String attachmentName = null;
									String type = null;
									String parentId = null;
									String id = null;
									try {
										JSONObject jsonObject = new JSONObject(
												_result);
										videoTitle = jsonObject.getJSONObject(
												"record").getString("title");
										createTime = jsonObject.getJSONObject(
												"record").getString(
												"createTime");
										type = jsonObject.getJSONObject(
												"record").getString("type");

										JSONArray jsonArray = jsonObject
												.getJSONArray("attachmentList");
										for (int i = 0; i < jsonArray.length(); i++) {
											attachmentPath = jsonArray
													.getJSONObject(i)
													.getString("path");
											attachmentName = jsonArray
													.getJSONObject(i)
													.getString("name");
											parentId = jsonArray.getJSONObject(
													i).getString("parentId");
											id = jsonArray.getJSONObject(i)
													.getString("id");
											createName = jsonArray
													.getJSONObject(i)
													.getString("creatername");
										}

									} catch (JSONException e) {
										e.printStackTrace();
									}
									Intent intent = new Intent();
									intent.setClass(
											KnowledgeBaseActivity.this,
											VideoKnowledgeBaseDetailActivity.class);
									Bundle bundle = new Bundle();
									bundle.putString("videoTitle", videoTitle);
									bundle.putString("createName", createName);
									bundle.putString("createTime", createTime);
									bundle.putString("type", type);
									bundle.putString("attachmentPath",
											attachmentPath);
									bundle.putString("attachmentName",
											attachmentName);
									bundle.putString("id", id);
									bundle.putString("parentId", parentId);
									intent.putExtras(bundle);
									KnowledgeBaseActivity.this
											.startActivity(intent);
								}

							}

							@Override
							public void HttpFail(int ErrCode) {
								progress.dismiss();
								Toast.makeText(KnowledgeBaseActivity.this,
										"网络请求失败,请检查您的网络！", 1).show();
							}

						});
				break;
			case 3:
				UserHttpBiz.getKnowledgeBaseNum(knowledgeId,
						MyApp.globelUser.getAccountid(),
						MyApp.globelUser.getType(), new HttpDataCallBack() {
							@Override
							public void HttpSuccess(String _result) {
								// try
								// {
								// JSONObject object = new JSONObject(_result);
								// if (object.getBoolean("status") == true)
								// {
								// LoginActivity.recordtotal =
								// object.getInt("recordtotal");
								// }
								// }
								// catch (JSONException e)
								// {
								// e.printStackTrace();
								// }
								MyApp.recordtotal = 0;
							}

							@Override
							public void HttpFail(int ErrCode) {

							}
						});
				break;
			default:
				break;
			}
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
		setContentView(R.layout.activity_knowledge_base);
		init();
	}

	private void init() {
		// back_btn = (Button)findViewById(R.id.btn_back);
		//
		// btn_setting = (Button)findViewById(R.id.btn_setting);
		// btn_setting.setOnClickListener(this);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_setting = (ImageView) findViewById(R.id.iv_setting);

		tv_title = (TextView) findViewById(R.id.tv_center_title);
		tv_title.setText("电网知识库");

		et_seacher = (EditText) findViewById(R.id.et_seacher);

		// back_btn.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		iv_setting.setOnClickListener(this);
		et_seacher.setOnClickListener(this);

		refreshListView = (XListView) findViewById(R.id.knowledge_refresh_list);

		refreshListView.setPullLoadEnable(true);// 设置上拉加载
		refreshListView.setXListViewListener(this);

		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final KnowledgeBaseModel knowledgeBaseModel = knowledgeBaseModels
						.get(position - 1);
				knowledgeType = knowledgeBaseModel.getType();
				knowledgeId = knowledgeBaseModel.getId();
				handler.sendEmptyMessage(3);
				new Thread(new Runnable() {

					@Override
					public void run() {
						handler.sendEmptyMessage(2);
					}
				}).start();
			}
		});

		initListViewData();
	}

	// 初始化数据
	private void initListViewData() {
		thread.start();
	}

	Thread thread = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(0);
		}
	});

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		;
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		// case R.id.btn_back:
		// finish();
		// break;

		case R.id.et_seacher:
			intent.setClass(KnowledgeBaseActivity.this,
					KnowledgeSeachActivity.class);
			startActivity(intent);
			break;

		case R.id.iv_setting:
			showSettingActivity();
			break;

		// case R.id.btn_setting:
		// intent.setClass(KnowledgeBaseActivity.this, SettingActivity.class);
		// startActivity(intent);
		// break;

		default:
			break;
		}

	}

	/** 停止刷新， */
	private void onLoad() {
		refreshListView.stopRefresh();
		refreshListView.stopLoadMore();
		refreshListView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		// new AsyncTask<Void, Void, Void>() {
		//
		// @Override
		// protected Void doInBackground(Void... params) {
		// SystemClock.sleep(2000);
		//
		// Map<String, Object> map1=new HashMap<String, Object>();
		// map1.put("knowledgebase_title", "刷新的电网知识库标题1");
		// map1.put("knowledgebase_type", "刷新的电网知识库内容1");
		//
		// Map<String, Object> map2=new HashMap<String, Object>();
		// map2.put("knowledgebase_title", "刷新的电网知识库标题2");
		// map2.put("knowledgebase_type", "刷新的电网知识库内容2");
		//
		// listItems.add(0,map1);
		// listItems.add(0,map2);
		// return null;
		// }
		//
		// @Override
		// protected void onPostExecute(Void result) {
		// adapter.notifyDataSetChanged();
		onLoad();
		// }
		// }.execute(new Void[]{});

	}

	@Override
	public void onLoadMore() {
		if (totalNum < num) {
			Toast.makeText(KnowledgeBaseActivity.this, "数据已全部加载完", 1).show();
			onLoad();
		} else {
			int pages = totalNum / num == 0 ? totalNum / num : totalNum / num
					+ 1;
			if (pages >= curragePage) {
				curragePage++;
			}
			new Thread(new Runnable() {

				@Override
				public void run() {
					handler.sendEmptyMessage(1);
				}
			}).start();
			onLoad();
		}
	}

	// 从网络上获取数据进行解析
	private List<Map<String, Object>> getData(String _result) {
		JSONObject jsonObject;
		String title = null;
		String id = null;
		String createTime = null;
		String type = null;
		try {
			jsonObject = new JSONObject(_result);
			totalNum = Integer.parseInt(jsonObject.getString("total"));
			jsonObject.getString("status");

			JSONArray jsonArray = jsonObject.getJSONArray("recordList");
			for (int i = 0; i < jsonArray.length(); i++) {
				KnowledgeBaseModel knowledgeBaseModel = new KnowledgeBaseModel();
				title = jsonArray.getJSONObject(i).getString("title");
				id = jsonArray.getJSONObject(i).getString("id");
				createTime = "发布时间："
						+ jsonArray.getJSONObject(i).getString("createTime");
				String baseType = jsonArray.getJSONObject(i).getString("type");
				if (baseType.equals("1")) {
					type = "所属类型：故障知识库";
				}
				if (baseType.equals("2")) {
					type = "所属类型：评估知识库";
				}
				if (baseType.equals("3")) {
					type = "所属类型：视频知识库";
				}
				if (baseType.equals("4")) {
					type = "所属类型：资料知识库";
				}

				knowledgeBaseModel.setCreateTime(createTime);
				knowledgeBaseModel.setTitle(title);
				knowledgeBaseModel.setType(baseType);
				knowledgeBaseModel.setId(Integer.parseInt(jsonArray
						.getJSONObject(i).getString("id")));

				knowledgeBaseModels.add(knowledgeBaseModel);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("knowledgebase_title", title);
				map.put("knowledgebase_type", type);
				map.put("knowledgebase_date", createTime);
				// map.put("support_date", date);
				listItems.add(map);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listItems;
	}

}
