package com.xj_pipe.view;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPoint;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.Proximity2DResult;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.xj_pipe.adapter.CommonAdapter;
import com.xj_pipe.adapter.ViewHolder;
import com.xj_pipe.base.BaseActivity;
import com.xj_pipe.bean.InspectionBean;
import com.xj_pipe.bean.PipeDotBean;
import com.xj_pipe.common.CropPhotoUtil;
import com.xj_pipe.common.GeometryUtil;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;
import com.xj_pipe.utils.JsonUtils;
import com.xj_pipe.utils.MyUtils;

@SuppressWarnings("deprecation")
public class InspectionMainActivity extends BaseActivity {
	/**
	 * 发送坐标的时间间隔,每5秒为基本单元，所以times = 2,表示每2*5秒发送一次
	 */
	private final static int times = 2;
	/*** 巡检路线 */
	private Button inspRouteBtn = null;
	/*** 我的位置 */
	private ImageView inspLocationIV = null;
	/** 全屏切换 */
	private ImageView inspFullScreenIV = null;

	/** 地图控件 */
	private MapView inspMapView = null;
	/** 动态图层 **/
	private ArcGISDynamicMapServiceLayer inspDynamicLayer = null;
	private ArcGISTiledMapServiceLayer inspTiledLayer = null;
	private LocationDisplayManager locationManager;
	private GraphicsLayer graphicsLayer = new GraphicsLayer();
	private GraphicsLayer graphicsLayer2 = new GraphicsLayer();

	private GraphicsLayer traceLayer; // 地图轨迹图层

	/** 地图/列表布局切换 */
	private RadioGroup inspRadioGroup = null;
	/** 列表布局 */
	private RelativeLayout inspListModeLayout = null;
	/** 全屏状态 **/
	private boolean isFull = false;

	/** 任务详情菜单 **/
	private LinearLayout inspDetailLayout = null;
	/** 任务详情收起按钮 **/
	private ImageView inspShrinkBtn = null;

	private String filePath = "";

	private InspectionBean inspectionBean = null;

	private List<PipeDotBean> pipeDotBeans = new ArrayList<PipeDotBean>();
	/**
	 * 当前位置(默认坐标系,地图坐标系为4326)
	 */
	private Point curPoint = null;
	/**
	 * 当前位置在102100坐标系投影点
	 */
	private Point mapPoint;
	private PopupWindow pWindow = null;

	/** 巡检点签到对话框 */
	private Dialog dialog;

	private Timer timer;
	private TimerTask task;

	private boolean isWait = true; // 刚进入界面时，定时器处于等待状态,直到初始化成功才开始执行
	private boolean isShow = false; // 签到对话框状态
	private boolean isFirst = true; // 第一次进入界面标志自动定位标志
	private boolean isStart = false; // 巡检任务开始标记
	private int taskType;

	/**
	 * 巡检任务列表
	 */
	private ListView inspXlist = null;
	private CommonAdapter<PipeDotBean> mAdapter = null;

	private int count = 0; // 计数器
	private MultiPoint mPoints;

	private Proximity2DResult result[];

	private Point testPoint = new Point(0, 0);

	private Polyline mPolyLine; // 巡检轨迹折线

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/**
			 * 获取所有巡检点，根据状态画上标记
			 */
			case 0:
				int size = pipeDotBeans.size();
				Graphic[] graphics = new Graphic[size];
				Map<String, Object> map;
				for (int i = 0; i < size; i++) {
					PipeDotBean pipeDotBean = pipeDotBeans.get(i);
					map = new HashMap<String, Object>();
					map.put("name", pipeDotBean.getPointName());
					Point point = new Point(Double.parseDouble(pipeDotBean
							.getLongtitude()), Double.parseDouble(pipeDotBean
									.getLatitude()));
					if (pipeDotBean.getSign().equals("0")) {// 未签到
						PictureMarkerSymbol symbol = new PictureMarkerSymbol(
								getApplicationContext(), getResources()
								.getDrawable(R.drawable.map_no));
						Graphic graphic = new Graphic(point, symbol, map);
						graphics[i] = graphic;
					} else if (pipeDotBean.getSign().equals("1")) {// 已签到
						PictureMarkerSymbol symbol = new PictureMarkerSymbol(
								getApplicationContext(), getResources()
								.getDrawable(R.drawable.map_yes));
						Graphic graphic = new Graphic(point, symbol, map);
						graphics[i] = graphic;
					}
				}
				if (getGraphicLayer() != null) {
					graphicsLayer.removeAll();
				}

				graphicsLayer.addGraphics(graphics);

				if (mPoints == null) {
					mPoints = new MultiPoint();
				} else {
					mPoints.setEmpty();
				}
				for (PipeDotBean bean : pipeDotBeans) {
					// Log.i("zlt", "pipedotbean : [ " + bean.getLongtitude() +
					// "," + bean.getLatitude() + "]");
					mPoints.add(GeometryEngine.project(
							Double.valueOf(bean.getLongtitude()),
							Double.valueOf(bean.getLatitude()),
							SpatialReference.create(102100)));
				}

				isWait = false; // 开始定时
				break;
			case 1:
				/**
				 * 测量当前位置到巡检点的距离，到达10米范围内弹出签到框进行签到
				 */
				result = GeometryEngine.getNearestVertices(mPoints, mapPoint,
						10, 1);
				// result isn't null refers that some valued points is returned
				if (result != null) {
					int index = result[0].getVertexIndex();
					// sign值为0表示未签到，这个时候需要弹签到对话框;已经签到过的就不要管了,直接跳过
					if (!isShow
							&& pipeDotBeans.get(index).getSign().equals("0")) {
						// testPoint.setXY(0, 0);
						isShow = true;
						showSignInDialog(
								pipeDotBeans.get(index).getPointName(),
								result[0].getDistance(), pipeDotBeans
								.get(index).getId());
					}
				}

				break;
			}
			super.handleMessage(msg);

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		if (intent != null) {
			inspectionBean = (InspectionBean) intent.getExtras()
					.getSerializable("InspectionBean");

			// Log.i("zlt", "inspectionBean = " + inspectionBean.getId());
			taskType = intent.getExtras().getInt("taskType");

			/*
			 * 应急任务进入之后自动开始
			 */
			if (taskType == 2 && inspectionBean.getState() < 2) {
				startTask();
			}
		}

		setContentLayout(R.layout.activity_inspection_main);

		setCenterTitle(taskType == 1 ? "例行巡检任务" : "应急巡检任务"); // 设置标题

		initViews();
		initLocationManager();

		task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (isStart) {
					if (!isWait) {
						handler.sendEmptyMessage(1);
					}

					if ((count = count++ % times) == 0 && curPoint != null) {
						sendLocationToServer();
					}
				}
			}
		};
		timer = new Timer();
		timer.schedule(task, 2000, 5000);
	}

	private void initLocationManager() {

		locationManager = inspMapView.getLocationDisplayManager();
		locationManager.setAutoPanMode(
				// LocationDisplayManager.AutoPanMode.LOCATION
				LocationDisplayManager.AutoPanMode.OFF);
		locationManager.setAccuracyCircleOn(false);
		locationManager.setLocationListener(new LocationListener() {

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				if (curPoint == null) {
					curPoint = new Point();
				}

				curPoint.setXY(location.getLongitude(), location.getLatitude());

				mapPoint = (Point) GeometryEngine.project(curPoint,
						inspMapView.getSpatialReference(),
						SpatialReference.create(102100));
				// 是否开始绘制轨迹
				if (isStart) {
					if (mPolyLine == null) {
						mPolyLine = new Polyline();
						mPolyLine.startPath(curPoint);
					} else {
						mPolyLine.lineTo(curPoint);
						drawTraceOnMap(mPolyLine, getTraceLayer(), Color.CYAN);
					}
				}
				// 是否开启自动定位
				if (isFirst) {
					isFirst = false;
					doAutoLocating();
				}
			}
		});
		locationManager.start();
	}

	/**
	 * 自动定位
	 */
	protected void doAutoLocating() {
		// isFirst values false indicate that locationManager works
		// normally,then
		// we can locate manually
		if (!isFirst) {
			inspMapView.zoomToScale(curPoint, 5726);
		}
	}

	private void initViews() {
		// TODO Auto-generated method stub
		hideLeftLogo();
		inspLocationIV = (ImageView) findViewById(R.id.inspection_mylocation);
		inspFullScreenIV = (ImageView) findViewById(R.id.inspection_screen_mode);
		inspRouteBtn = (Button) findViewById(R.id.inspection_route);

		inspMapView = (MapView) findViewById(R.id.inspection_mapview);

		inspRadioGroup = (RadioGroup) findViewById(R.id.inspection_bottom_radiogroup);

		inspListModeLayout = (RelativeLayout) findViewById(R.id.inspection_list_mode_layout);
		inspListModeLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});

		inspFullScreenIV.setOnClickListener(this);
		inspLocationIV.setOnClickListener(this);
		inspRouteBtn.setOnClickListener(this);

		inspRadioGroup
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.inspection_radio_1:
					if (inspListModeLayout.getVisibility() != View.GONE) {
						inspListModeLayout.setVisibility(View.GONE);
					}
					break;
				case R.id.inspection_radio_2:
					if (inspListModeLayout.getVisibility() != View.VISIBLE) {
						inspListModeLayout.setVisibility(View.VISIBLE);
					}
					break;

				default:
					break;
				}
			}
		});

		ArcGISRuntime.setClientId("uK0DxqYT0om1UXa9");// 去掉地图水印
		inspDynamicLayer = new ArcGISDynamicMapServiceLayer( // 深圳动态图层
				"http://218.17.162.229:8107/arcgis/rest/services/shenzhen/MapServer");

		inspTiledLayer = new ArcGISTiledMapServiceLayer( // 海南省切片图层
				"http://218.17.162.229:8107/arcgis/rest/services/CACH/MapServer");
		// inspDynamicLayer = new ArcGISDynamicMapServiceLayer(
		// "http://192.168.1.34:8097/arcgis/rest/services/CACH/MapServer");
		inspMapView.addLayer(inspDynamicLayer);
		inspMapView.addLayer(inspTiledLayer);

		ArcGISFeatureLayer pointFeatureLayer = new ArcGISFeatureLayer(
				"http://218.17.162.229:8107/arcgis/rest/services/PIPETEST/FeatureServer/0",
				ArcGISFeatureLayer.MODE.SNAPSHOT);
		inspMapView.addLayer(pointFeatureLayer);
		// ArcGISFeatureLayer pointFeatureLayer = new ArcGISFeatureLayer(
		// "http://218.17.162.229:8107/arcgis/rest/services/FEATURE/FeatureServer/4",
		// ArcGISFeatureLayer.MODE.SNAPSHOT);
		// inspMapView.addLayer(pointFeatureLayer);

		ArcGISFeatureLayer lineFeatureLayer = new ArcGISFeatureLayer(
				"http://218.17.162.229:8107/arcgis/rest/services/PIPETEST/FeatureServer/1",
				ArcGISFeatureLayer.MODE.SNAPSHOT);
		inspMapView.addLayer(lineFeatureLayer);
		// ArcGISFeatureLayer lineFeatureLayer = new ArcGISFeatureLayer(
		// "http://218.17.162.229:8107/arcgis/rest/services/FEATURE/FeatureServer/6",
		// ArcGISFeatureLayer.MODE.SNAPSHOT);
		// inspMapView.addLayer(lineFeatureLayer);

		inspMapView.addLayer(graphicsLayer);
		inspMapView.addLayer(graphicsLayer2);

		MyUtils.setMapViewBackground(inspMapView);

		inspMapView.setOnSingleTapListener(new OnSingleTapListener() {

			@SuppressWarnings("unused")
			@Override
			public void onSingleTap(float x, float y) {
				int[] graphicIDs = graphicsLayer.getGraphicIDs(x, y, 25);

				Callout callout = inspMapView.getCallout();// 通过MapView获取Callout实例对象
				if (graphicIDs != null && graphicIDs.length > 0) {
					Graphic gr = graphicsLayer.getGraphic(graphicIDs[0]);
					com.esri.core.geometry.Point location = (Point) gr
							.getGeometry();
					LayoutInflater li = getLayoutInflater();
					View content = li.inflate(R.layout.calloutlayout, null);
					TextView message = (TextView) content
							.findViewById(R.id.message);

					message.setText("里程桩名称：" + gr.getAttributeValue("name"));
					callout.setContent(content);// 弹出窗口布局文件对象
					// callout. setOffset(0, -15);
					callout.show(location);
				} else {
					callout.hide();
				}

				/****************************************
				 * 
				 * 以下代码用于位置测试，勿删
				 * 
				 * ************************************
				 */
				// testPoint = inspMapView.toMapPoint(x, y);
				//
				// mapPoint = (Point) GeometryEngine.project(testPoint,
				// inspMapView.getSpatialReference(),
				// SpatialReference.create(102100));
			}
		});

//		initDetailLayout();
		initInspectionListLayout();

		initPipeDotList();
	}

	/**
	 * 初始化巡检任务详情模块
	 */
	private void initDetailLayout() {
		// TODO Auto-generated method stub
		inspDetailLayout = (LinearLayout) findViewById(R.id.inspection_task_detail);
		inspShrinkBtn = (ImageView) findViewById(R.id.inspection_shrink);
		inspShrinkBtn.setOnClickListener(this);

		TextView inspDetailName = (TextView) findViewById(R.id.inspection_detail_name);
		// TextView inspDetailType = (TextView)
		// findViewById(R.id.inspection_detail_type);
		TextView inspDetailEndTime = (TextView) findViewById(R.id.inspection_detail_endtime);
		TextView inspDetailBeginTime = (TextView) findViewById(R.id.inspection_detail_begintime);
		TextView inspDetailDescription = (TextView) findViewById(R.id.inspection_detail_description);

		if (inspectionBean != null) {
			inspDetailName.setText(inspectionBean.getName());
			// inspDetailType.setText(inspectionBean.getTaskType());
			inspDetailEndTime.setText(inspectionBean.getPlanEndTime());
			inspDetailBeginTime.setText(inspectionBean.getStartTime());
			inspDetailDescription.setText("任务描述:\n"
					+ inspectionBean.getContent());
		}

	}

	/**
	 * 初始化巡检任务列表页面模块
	 */
	private void initInspectionListLayout() {

		inspXlist = (ListView) findViewById(R.id.inspection_xlist);
		inspXlist.setAdapter(mAdapter = new CommonAdapter<PipeDotBean>(this,
				pipeDotBeans, R.layout.inspection_list_item_layout) {

			@Override
			public void convert(ViewHolder viewHolder, PipeDotBean item) {
				// TODO Auto-generated method stub
				viewHolder.setText(R.id.item_log, new DecimalFormat("00")
				.format(pipeDotBeans.indexOf(item) + 1));

				viewHolder
				.setTextBackground(
						R.id.item_log,
						item.getSign().equals("0") ? R.drawable.inspection_item_state_uninspect
								: R.drawable.inspection_item_state_inspected);

				viewHolder.setText(
						R.id.item_name,
						String.format("%1$s桩", item.getPointName()),
						item.getSign().equals("0") ? Color
								.parseColor("#ffaa22") : Color
								.parseColor("#18d55a"));

				viewHolder.setText(
						R.id.item_state,
						item.getSign().equals("0") ? "未巡检" : "已巡检",
								item.getSign().equals("0") ? Color
										.parseColor("#ffaa22") : Color
										.parseColor("#18d55a"));
			}
		});

		inspXlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});

		if (inspectionBean != null) {
			((TextView) findViewById(R.id.tv_currentInspectionTask))
			.setText(inspectionBean.getName());
			((TextView) findViewById(R.id.tv_endtime)).setText(inspectionBean
					.getPlanEndTime());
			((TextView) findViewById(R.id.tv_starttime)).setText(inspectionBean
					.getPlanStartTime());
			String description = inspectionBean.getContent();
			if (description.trim().length() <= 0) {
				description = "暂无相关任务描述";
			}
			((TextView) findViewById(R.id.tv_description)).setText(description);
		}
		
		descriptionLayout  = (LinearLayout) findViewById(R.id.ll_4);
		inspShrinkBtn = (ImageView) findViewById(R.id.inspection_shrink);
		inspShrinkBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (descriptionLayout.getVisibility() == View.GONE) {
					descriptionLayout.setVisibility(View.VISIBLE);
					inspShrinkBtn.setImageResource(R.drawable.task_details_shouqi);
				}else {
					descriptionLayout.setVisibility(View.GONE);
					inspShrinkBtn.setImageResource(R.drawable.task_details_zhankai);
				}
			}
		});
	}
	
	private LinearLayout descriptionLayout;
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.inspection_screen_mode: // 全屏切换

			isFull = !isFull;
			fullMapView(isFull);

			break;
		case R.id.inspection_mylocation: // 地图定位
			doAutoLocating();
			break;

		case R.id.inspection_route: // 查看任务路线
			break;

		case R.id.menu_takephoto: // 拍照
			pWindow.dismiss();
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			filePath = new CropPhotoUtil(this).getCachePath() + "/"
					+ UUID.randomUUID().toString() + ".png";

			File file = new File(filePath);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {

				}
			}
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			startActivityForResult(intent, 1);

			break;
		case R.id.inspection_shrink:
		case R.id.menu_showdetail: // 显示任务详情
			pWindow.dismiss();
			inspMapView.scrollBy(0, 1);
			final boolean isShow = inspDetailLayout.getVisibility() == View.VISIBLE ? true
					: false;

			int fromY = isShow ? 0 : -inspDetailLayout.getHeight();
			int toY = isShow ? -inspDetailLayout.getHeight() : 0;
			TranslateAnimation animation = new TranslateAnimation(0, 0, fromY,
					toY);
			animation.setDuration(500);
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					inspDetailLayout.setVisibility(isShow ? View.GONE
							: View.VISIBLE);
				}
			});
			inspDetailLayout.startAnimation(animation);

			break;
		case R.id.menu_submittask: // 提交任务
			pWindow.dismiss();

	        boolean isFinish = true;
			
			for (PipeDotBean bean:pipeDotBeans) {
				if (bean.getSign().equals("0")) {
					isFinish = false;
					break;
				}
			}
			
			Intent intent2 = new Intent(InspectionMainActivity.this,
					InspectionSubmitActivity.class);
			intent2.putExtra("TaskId", inspectionBean.getId());
			intent2.putExtra("taskType", taskType);
			intent2.putExtra("taskState", inspectionBean.getState());
			intent2.putExtra("isFinish", isFinish);
			startActivity(intent2);
			break;

		case R.id.menu_starttask:// 开始巡检
			pWindow.dismiss();
			if (isStart) {
				showToast("巡检任务已经开始!");
				return;
			}

			LayoutInflater inflater = LayoutInflater.from(this);
			final Dialog dialog = new Dialog(this, R.style.CommonDialog);
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);

			View view2 = inflater.inflate(R.layout.dialog_exit, null);
			TextView title = (TextView) view2
					.findViewById(R.id.dialog_view_title);
			Button ok = (Button) view2.findViewById(R.id.dialog_view_ok);
			ok.setText("开始");
			title.setText("是否开始巡检?");
			view2.findViewById(R.id.dialog_view_cancel).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
			view2.findViewById(R.id.dialog_view_ok).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {

							dialog.dismiss();

							if (!isStart) {
								startTask();
							}
						}
					});
			dialog.setContentView(view2);
			dialog.show();
			break;

		default:
			break;
		}
		super.onClick(view);

	}

	// 任务开始了
	private void startTask() {
		// 发送消息，告诉后台任务开始了
		// 这里添加接口事件
		if (inspectionBean.getState() == 1) {
			isStart = true;
			showToast("巡检任务开始!");
			return;
		}
		
		UserHttp.startCheckPoint(inspectionBean.getId(),new HttpDataCallBack() {

			@Override
			public void HttpSuccess(String _result) {
				// TODO Auto-generated method stub
				try {
					int flag = new JSONObject(_result).getInt("result");
					if(flag == 0){
						isStart = true;
						showToast("巡检任务开始!");
					}else{
						showToast("失败，暂不能开始!");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void HttpFail(int ErrCode) {
				showToast("网络连接失败，请检查您的网络!");
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (isFull) {
				isFull = false;
				fullMapView(isFull);
			} else {
				finish();
			}
			return true;
		} else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			if (isFull) {
				isFull = false;
				fullMapView(isFull);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onMoreLisenter() {
		// TODO Auto-generated method stub
		if (pWindow == null) { // 当pWindow不存在时，创建popupWindow
			LayoutInflater inflater = LayoutInflater.from(this);
			View menu = inflater.inflate(
					R.layout.activity_inspection_main_menu, null);
			menu.setFocusableInTouchMode(true);
			menu.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (keyCode == KeyEvent.KEYCODE_MENU
							&& event.getAction() == KeyEvent.ACTION_DOWN
							&& pWindow.isShowing()) {
						pWindow.dismiss();
						return true;
					}
					return false;
				}
			});
			menu.findViewById(R.id.menu_takephoto).setOnClickListener(this);
			menu.findViewById(R.id.menu_submittask).setOnClickListener(this);
			menu.findViewById(R.id.menu_showdetail).setOnClickListener(this);
			menu.findViewById(R.id.menu_starttask).setOnClickListener(this);
			
			if (inspectionBean.getState() >= 2) {	//假如任务已完成,那么不能进行提交任务和开始任务操作
				((View) menu.findViewById(R.id.menu_submittask).getParent()).setVisibility(View.GONE);
				((View) menu.findViewById(R.id.menu_starttask).getParent()).setVisibility(View.GONE);
			}
			
			if (taskType == 2) {	//如果是应急巡检任务,那么不用显示开始任务按钮
				((View) menu.findViewById(R.id.menu_starttask).getParent()).setVisibility(View.GONE);
			}
			
			pWindow = new PopupWindow(menu, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		// 判断当pWindow处于显示状态，点击则取消,否则显示该popupWindow
		if (pWindow.isShowing()) {
			pWindow.dismiss();
		} else {
			pWindow.setFocusable(true);
			pWindow.setOutsideTouchable(true);
			pWindow.setBackgroundDrawable(new BitmapDrawable()); // 一定要添加这行，否则会影响其他的事件触发
			pWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
			pWindow.showAtLocation(getLyContentView(), Gravity.RIGHT
					| Gravity.TOP, 0, getTitleHeight() + getStatusHeight());
		}
	}

	private void fullMapView(final boolean flag) {
		if (!flag) {
			showTitleBanner();
			inspFullScreenIV
			.setImageResource(R.drawable.fullscreen_btn_selector);
		} else {
			hideTitleBanner();
			inspFullScreenIV
			.setImageResource(R.drawable.exit_fullscreen_btn_selector);
		}
		inspRadioGroup.setVisibility(flag ? View.GONE : View.VISIBLE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			showToast("照片存储路径" + filePath);
		}
	}

	/**
	 * 获取所有点的数据
	 */
	private void initPipeDotList() {

		if (pipeDotBeans == null) {
			pipeDotBeans = new ArrayList<PipeDotBean>();
		} else {

			// 情况pipeDotBeans列表，刷新适配器
			pipeDotBeans.clear();
			if (mAdapter != null) {
				mAdapter.notifyDataSetChanged();
			}
		}

		UserHttp.getPipeDotInfo(inspectionBean.getId(), new HttpDataCallBack() {

			@Override
			public void HttpSuccess(String _result) {
				try {
					System.out.println(_result);
					JSONObject jsonObject = new JSONObject(_result);
					JSONArray jsonArray = jsonObject
							.getJSONArray("templatePointSetList");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object2 = jsonArray.getJSONObject(i);
						PipeDotBean bean = (PipeDotBean) JsonUtils
								.putJsonToObject(object2,
										"com.xj_pipe.bean.PipeDotBean");

						pipeDotBeans.add(bean);
					}
					if (mAdapter != null) {
						mAdapter.notifyDataSetChanged();
					}
					handler.sendEmptyMessage(0);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void HttpFail(int ErrCode) {

			}
		});
	}

	/**
	 * 显示巡检点签到对话框
	 * 
	 * @param pointName
	 *            所在点的名称
	 * @param distance
	 *            当前位置与签到点所在的距离
	 */
	private void showSignInDialog(String pointName, double distance,
			final String pointId) {
		// TODO Auto-generated method stub
		dialog = new Dialog(this, R.style.CommonDialog);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);

		View view = getLayoutInflater().inflate(R.layout.dialog_sign_in, null);
		view.findViewById(R.id.dialog_view_ok).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						inspectionPointSignIn(pointId);
					}
				});
		Spanned text = Html.fromHtml(String.format(
				"你已到达巡检点<font color='green'>%s</font>处%.2f米", pointName,
				distance));

		((TextView) view.findViewById(R.id.dialog_view_text)).setText(text);

		dialog.setContentView(view);
		dialog.show();
	}

	/**
	 * 巡检点签到
	 */
	private void inspectionPointSignIn(String pointId) {
		UserHttp.inspectionPointSign(pointId, "0", new HttpDataCallBack() {

			@Override
			public void HttpSuccess(String _result) {
				dialog.dismiss();
				/*
				 * 获取到网络数据后,还未进行界面更新,这个时候先暂停定时器,故isWait处于等待状态
				 */
				isWait = true;
				/*
				 * 对话框消失,说明事件被处理,所以重置isShow状态
				 */
				isShow = false;
				showToast("签到成功!");
				initPipeDotList();
			}

			@Override
			public void HttpFail(int ErrCode) {
				showToast("签到失败!");
			}
		});
	}

	/**
	 * 在地图上绘制轨迹
	 * 
	 * @param mPolyLine
	 *            几何图形
	 * @param layer
	 *            轨迹绘制所在图层
	 * @param color
	 *            轨迹颜色值
	 */
	private void drawTraceOnMap(Polyline mPolyLine, GraphicsLayer layer,
			int color) {

		Geometry[] geoms = new Geometry[1];
		geoms[0] = mPolyLine;

		try {
			layer.removeAll();
			GeometryUtil.highlightGeometriesWithColor(geoms, layer, color);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送当前位置
	 */
	protected void sendLocationToServer() {
		// TODO Auto-generated method stub

		Log.i("pipe", "send : id = " + inspectionBean.getId() + "," + curPoint);

		UserHttp.submitTrackPoint(inspectionBean.getId(),
				String.valueOf(curPoint.getX()),
				String.valueOf(curPoint.getY()), new HttpDataCallBack() {

			@Override
			public void HttpSuccess(String _result) {
				// TODO Auto-generated method stub
				boolean flag = false;
				try {
					flag = new JSONObject(_result).getBoolean("status");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 如果发送失败,继续发送
				if (!flag) {
					sendLocationToServer();
				}

			}

			@Override
			public void HttpFail(int ErrCode) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 获取要素图层
	 * 
	 * @return
	 */
	protected GraphicsLayer getGraphicLayer() {
		if (graphicsLayer == null) {
			graphicsLayer = new GraphicsLayer();
			inspMapView.addLayer(graphicsLayer);
		}
		return graphicsLayer;
	}

	/**
	 * 获取地图轨迹图层
	 * 
	 * @return
	 */
	public GraphicsLayer getTraceLayer() {
		if (traceLayer == null) {
			traceLayer = new GraphicsLayer();
			inspMapView.addLayer(traceLayer);
		}
		return traceLayer;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
		inspMapView.removeAll();
	}

	@Override
	public void onBackLisenter() {
		super.onBackLisenter();
		finish();
	}
}
