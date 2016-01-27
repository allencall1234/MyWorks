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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xj.cnooc.adapter.KnowledgeBaseListAdapter;
import com.xj.cnooc.biz.UserHttpBiz;
import com.xj.cnooc.common.BaseActivity;
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
 * 知识库搜索界面
 * 
 * @author qinfan
 *
 *         2015-9-24
 */
public class KnowledgeSeachActivity extends BaseActivity implements
		IXListViewListener, OnClickListener {
	private Button btn_back;
	// private RelativeLayout rellayout_back;
	private ImageView iv_back;
	private XListView refreshListView;
	private KnowledgeBaseListAdapter adapter;
	private Button btn_seach;
	private EditText et_seach;

	private List<KnowledgeBaseModel> knowledgeBaseModels = new ArrayList<KnowledgeBaseModel>();
	private List<FaultSupportModel> faultSupportModels = new ArrayList<FaultSupportModel>();
	private FaultSupportModel faultSupportModelDetail;
	List<Map<String, Object>> listItems2 = new ArrayList<Map<String, Object>>();

	String knowledgeType = null;
	int knowledgeId = 0;

	private int totalNum = 0;// 从数据库中获取的数据总数
	private int num = 10;// 每页加载的数据数量
	private int curragePage = 1;// 当前页

	private ProgressDialog progress;

	Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				progress = ProgressDialog.show(KnowledgeSeachActivity.this, "",
						"正在加载数据,请稍候...");
				UserHttpBiz.getKnowledgeBseListData(curragePage, num, et_seach
						.getText().toString(), new HttpDataCallBack() {

					@Override
					public void HttpSuccess(String _result) {
						progress.dismiss();
						getData(_result);
						if (totalNum == 0) {
							Toast.makeText(KnowledgeSeachActivity.this,
									"没有找到与关键词相关的信息", 1).show();
						}
						if (totalNum <= 10) {
							refreshListView.setPullLoadEnable(false);
						} else {
							refreshListView.setPullLoadEnable(true);
						}
						adapter.notifyDataSetChanged();
					}

					@Override
					public void HttpFail(int ErrCode) {
						progress.dismiss();
						Toast.makeText(KnowledgeSeachActivity.this,
								"网络请求失败,请检查您的网络！", 1).show();
					}
				});
				break;

			case 1:
				UserHttpBiz.getKnowledgeBseListData(curragePage, num, et_seach
						.getText().toString(), new HttpDataCallBack() {

					@Override
					public void HttpSuccess(String _result) {
						// TODO Auto-generated method stub
						getData(_result);
						adapter.notifyDataSetChanged();
						int pages = totalNum / num == 0 ? totalNum / num
								: totalNum / num + 1;
						if (curragePage >= pages) {
							Toast.makeText(KnowledgeSeachActivity.this,
									"数据已全部加载完", 1).show();
						}
					}

					@Override
					public void HttpFail(int ErrCode) {
						progress.dismiss();
						Toast.makeText(KnowledgeSeachActivity.this,
								"网络请求失败,请检查您的网络！", 1).show();
					}
				});
				break;

			case 2:
				UserHttpBiz.getKnowledgeBaseDetailData(knowledgeType,
						knowledgeId, new HttpDataCallBack() {

							@Override
							public void HttpSuccess(String _result) {
								// TODO Auto-generated method stub
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
												KnowledgeSeachActivity.this,
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
										KnowledgeSeachActivity.this
												.startActivity(intent);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}

								if (knowledgeType.equals("3")
										|| knowledgeType.equals("4")) {
									// Toast.makeText(KnowledgeBaseActivity.this,
									// "暂不支持该类型！", 1).show();
									String videoTitle = null;
									String createName = null;
									String createTime = null;
									String attachmentPath = null;
									String attachmentName = null;
									String type = null;
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
											createName = jsonArray
													.getJSONObject(i)
													.getString("creatername");
										}

									} catch (JSONException e) {
										e.printStackTrace();
									}
									Intent intent = new Intent();
									intent.setClass(
											KnowledgeSeachActivity.this,
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
									intent.putExtras(bundle);
									KnowledgeSeachActivity.this
											.startActivity(intent);
								}

							}

							@Override
							public void HttpFail(int ErrCode) {
								progress.dismiss();
								Toast.makeText(KnowledgeSeachActivity.this,
										"网络请求失败,请检查您的网络！", 1).show();
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
		setContentView(R.layout.activity_knowledge_seach);
		init();
	}

	private void init() {
		btn_back = (Button) findViewById(R.id.btn_seach_back);
		btn_back.setOnClickListener(this);

		btn_seach = (Button) findViewById(R.id.btn_seach);
		btn_seach.setOnClickListener(this);

		et_seach = (EditText) findViewById(R.id.et_seach);
		refreshListView = (XListView) findViewById(R.id.seach_refresh_list);

		refreshListView.setPullLoadEnable(false);// 设置上拉加载
		refreshListView.setXListViewListener(this);
		adapter = new KnowledgeBaseListAdapter(KnowledgeSeachActivity.this,
				listItems2);
		refreshListView.setAdapter(adapter);

		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final KnowledgeBaseModel knowledgeBaseModel = knowledgeBaseModels
						.get(position - 1);
				knowledgeType = knowledgeBaseModel.getType();
				knowledgeId = knowledgeBaseModel.getId();
				new Thread(new Runnable() {

					@Override
					public void run() {
						handler.sendEmptyMessage(2);
					}
				}).start();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.knowledge_seach, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.iv_back:
		// finish();
		// break;

		case R.id.btn_seach_back:
			finish();
			break;

		case R.id.btn_seach:
			if (et_seach.getText().toString() == null
					|| et_seach.getText().toString().equals("")) {
				Toast.makeText(KnowledgeSeachActivity.this, "请输入查询关键字", 0)
						.show();
			} else {
				new Thread(new Runnable() {

					@Override
					public void run() {
						listItems2.clear();
						knowledgeBaseModels.clear();
						curragePage = 1;
						handler.sendEmptyMessage(0);
					}
				}).start();
			}
			break;

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
			Toast.makeText(KnowledgeSeachActivity.this, "数据已全部加载完", 1).show();
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
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
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
				listItems2.add(map);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (listItems2.size() > 0) {
			return listItems2;
		}
		return listItems;
	}

}
