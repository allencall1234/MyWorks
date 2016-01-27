package com.xj_pipe.view;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

import org.codehaus.jackson.format.DataFormatMatcher;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.xj_pipe.base.BaseActivity;
import com.xj_pipe.base.MyApp;
import com.xj_pipe.common.ActivityForResultUtil;
import com.xj_pipe.common.CropPhotoUtil;
import com.xj_pipe.common.FactorModule;
import com.xj_pipe.common.PhotoModule;
import com.xj_pipe.common.PhotoPickDialog;
import com.xj_pipe.common.SubmitContentModule;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;
import com.xj_pipe.utils.MyUtils;
import com.xj_pipe.utils.ToastUtils;

/**
 * 事故报修、突发事件、施工上报界面
 * 根据MyApp.getType()判断调用哪用界面
 */
public class AccidentHandlingActivity extends BaseActivity implements OnTouchListener{
	//添加位置信息按钮触发状态
	private final static int STATE_SIMPLE_TAP = 1;
	private final static int STATE_ADD_TAP = 2;
	private Button addFactorButton = null; // 添加位置信息按钮

	private PhotoModule pModule = null;//选择照片
	private SubmitContentModule submitcontentmodule;

	private Button btn_save;// 确认提交

	private PopupWindow pWindow;

	private TextView menu_history_repair;// 历史报修
	private TextView menu_fault_repair;// 故障报修

	/**
	 * 地图相关控件
	 */
	private MapView mMapView = null;//地图控件
	private int width = -1, height = -1; // 地图控件的初始化宽度和高度
	private int scroll;
	private ArcGISTiledMapServiceLayer tiledLayer = null; // 地图切片服务图层
	private ArcGISDynamicMapServiceLayer dynamicLayer = null; // 地图动态服务图层
	private GraphicsLayer mGraphicsLayer = null; // 地图要素服务图层

	private ScrollView mScrollView = null;
	private ImageView tempView = null;
	private boolean isMove;

	private boolean isInitialed;
	private ProgressDialog mProgressDialog = null;
	//缩小按钮,点击按一定倍率缩小地图
	private ImageView zoomOutBtn = null;
	//全屏按钮，点击展开全屏或者最小化
	private ImageView fullScreenBtn = null;
	//全屏标志,判断当前地图状态是否是全屏
	private boolean isfull = false;
	// 全屏地图
	private int state = 1;
	
	//地图左右空白视图
	private View leftBlankView,rightBlankView;

	//施工上报是否动火布局
	private LinearLayout ll_submit_six = null;
	//事故报修现场是否处理
	private LinearLayout ll_dealwith = null;
	// 现场是否处理：是、否
	private RadioGroup rg_dealwith = null;
	private RadioButton rb_yes = null;
	private RadioButton rb_no= null;
	//巡检要素布局
	private FactorModule factormodule = null;
	//描述布局
	private RelativeLayout describe_layout = null;
	//巡检要素文字
	private TextView tv_inspection_elements;
	//描述内容标题
	private TextView tv_describe;
	//输入框内容
	private String repair,device_numbering,device_name,repair_time,title,occurrence_region,occurrence_time;
	//施工上报地图以上部分输入框内容
	private String construction_organization,construction_area,completion_date,start_time,end_time;
	// 内容描述布局
	private EditText et_describe;
	//是否动火单选框
	private RadioButton radio_yes;
	//是否动火
	private String isFire;
	
	//经纬度
	private TextView tv_longitude;
	private TextView tv_latitude;
	
	/**
	 * 上传照片布局
	 */
	private LinearLayout ll_photo;

	// 保存文件路径集合
	private List<String> filesPath = new ArrayList<String>();

	private String requestUrl = null;// 请求的网址和参数

	private double longitude;// 点击时获取的经度
	private double latitude;// 点击时获取的纬度

	private double locaitonLongitude;// 定位获取的经度
	private double locationLatitude;// 定位获取的纬度
	
	private boolean flag = false;

	/** 定位管理 */
	private LocationDisplayManager locationManager;

	private Point curPoint = null;

	// 现场是否处理
	private String isDealWith = null;
	
	/**
	 * 第一次进入界面时自动定位
	 */
	private boolean isFristIn = true;

	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what) 
			{
			case 0:
				mMapView.setVisibility(View.VISIBLE);
				tempView.setVisibility(View.GONE);
				break;

			case 4: 
				UserHttp.buildFeedBackSubmit(
						construction_organization, 
						construction_area, 
						start_time,
						end_time,
						isFire, 
						String.valueOf(longitude), 
						String.valueOf(latitude), 
						MyApp.userInfo.getStaffId(),
						new HttpDataCallBack() {

							@Override
							public void HttpSuccess(String _result) {
								// TODO Auto-generated method stub
								mProgressDialog.dismiss();
								try {
									JSONObject jsonObject = new JSONObject(_result);
									if(jsonObject.getBoolean("status")== true){
										showToast("上报成功！");
										finish();
									}else{
										showToast("上报失败！");
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}

							@Override
							public void HttpFail(int ErrCode) {

							}
						});
				break; 

			case 5:
				mProgressDialog.dismiss();
				if (flag)
				{
					finish();
					ToastUtils.ShowMessage(getApplicationContext(), "提交成功!");
				}
				else
				{
					ToastUtils.ShowMessage(getApplicationContext(), "提交失败!");
				}
				break;

			default:
				break;
			}	
		};
	};

	//网络请求需要开启新的线程，不能在主线程中操作——————上传文件所需线程
	Runnable multiThread = new Runnable() 
	{
		@Override
		public void run() 
		{
			try 
			{
				// 上传图片及传递参数
				flag = MyUtils.multiUploadFile1(requestUrl, filesPath, true);
				mHandler.sendEmptyMessage(5);
			} 
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_accident_handling_layout);

		setRightTitle("提交");
		hideLeftLogo();

		//是否动火布局
		ll_submit_six = (LinearLayout) findViewById(R.id.ll_submit_six);
		//现场是否处理
		ll_dealwith = (LinearLayout) findViewById(R.id.ll_dealwith);
		rg_dealwith = (RadioGroup) findViewById(R.id.rg_dealwith);
		rb_yes = (RadioButton) findViewById(R.id.rb_yes);
		rb_no = (RadioButton) findViewById(R.id.rb_no);
		
		//经纬度
		tv_longitude = (TextView) findViewById(R.id.tv_longitude);
		tv_latitude = (TextView) findViewById(R.id.tv_latitude);
		
		//巡检要素布局
		factormodule = (FactorModule) findViewById(R.id.factormodule);
		//巡检要素文字
		tv_inspection_elements = (TextView) findViewById(R.id.tv_inspection_elements);
		ll_photo = (LinearLayout) findViewById(R.id.ll_photo);
		// 故障描述标题
		tv_describe = (TextView) findViewById(R.id.tv_describe);
		describe_layout = (RelativeLayout) findViewById(R.id.describe_layout);
		//填写内容
		et_describe = (EditText) findViewById(R.id.et_describe);
		radio_yes = (RadioButton) findViewById(R.id.radio_yes);
		switch (MyApp.TYPE) {
		case 0:
			setCenterTitle("事故报修");
			ll_dealwith.setVisibility(View.VISIBLE);
			factormodule.setVisibility(View.VISIBLE);
			tv_inspection_elements.setText("巡检要素");
			tv_describe.setText("报修描述");
			break;
		case 1:
			setCenterTitle("突发事件");
			tv_inspection_elements.setVisibility(View.GONE);
			tv_describe.setText("事件描述");
			break;
		case 2:
			setCenterTitle("施工上报");
			ll_submit_six.setVisibility(View.VISIBLE);
			ll_photo.setVisibility(View.GONE);
			tv_inspection_elements.setVisibility(View.GONE);
			describe_layout.setVisibility(View.GONE);
			tv_describe.setVisibility(View.GONE);
			break;
		}

		pModule = (PhotoModule) findViewById(R.id.photo_module);

		submitcontentmodule = (SubmitContentModule) findViewById(R.id.submitcontentmodule);

		btn_save = (Button) findViewById(R.id.btn_save);

		btn_save.setOnClickListener(this);

		isDealWith = "Y";
		rg_dealwith.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				switch (checkedId)
				{
				case R.id.rb_yes:
					rb_yes.setChecked(true);
					isDealWith = "Y";
					break;

				case R.id.rb_no:
					rb_no.setChecked(true);
					isDealWith = "N";
					break;

				default:
					break;
				}
			}
		});

		// 初始化添加要素按钮
		addFactorButton = (Button) findViewById(R.id.add_location);
		addFactorButton.setOnClickListener(this);
		addFactorButton.setOnTouchListener(this);

		// 初始化缩小按钮
		zoomOutBtn = (ImageView) findViewById(R.id.zoomout_button);
		zoomOutBtn.setOnClickListener(this);
		zoomOutBtn.setOnTouchListener(this);

		// 初始化全屏按钮
		fullScreenBtn = (ImageView) findViewById(R.id.fullscreen_button);
		fullScreenBtn.setOnClickListener(this);
		fullScreenBtn.setOnTouchListener(this);

		leftBlankView = findViewById(R.id.left_blank_view);
		rightBlankView = findViewById(R.id.right_blank_view);

		mScrollView = (ScrollView) findViewById(R.id.external_scrollview);
		tempView = (ImageView) findViewById(R.id.temp_view);
		initMapViews();

		initLocationManager();

		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("正在加载地图...");

		mProgressDialog.setOnCancelListener(new OnCancelListener() 
		{
			@Override
			public void onCancel(DialogInterface dialog) 
			{
				isInitialed = true;
			}
		});

		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();
		new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				while (!isInitialed) 
				{
					if (mMapView.isLoaded())
					{
						isInitialed = true;
						mProgressDialog.dismiss();
					}
				}
			}
		}).start();
	}

	private void initLocationManager() 
	{
		locationManager = mMapView.getLocationDisplayManager();
		locationManager
		.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
		locationManager.setAccuracyCircleOn(false);
		locationManager.setLocationListener(new LocationListener() 
		{
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
				// TODO Auto-generated method stub
				locaitonLongitude = location.getLongitude();
				locationLatitude = location.getLatitude();
				Point point = new Point(locaitonLongitude, locationLatitude);
//				Point point1 = (Point)GeometryEngine.project(point, mMapView.getSpatialReference(), SpatialReference.create(4326));

				if (isFristIn) {
					isFristIn = false;
					mMapView.zoomToScale(point, 5326f);
				}
			}
		});
		locationManager.start();
	}

	private void initMapViews() 
	{
		ArcGISRuntime.setClientId("uK0DxqYT0om1UXa9");
		tiledLayer = new ArcGISTiledMapServiceLayer("http://218.17.162.229:8107/arcgis/rest/services/CACH/MapServer");

		dynamicLayer = new ArcGISDynamicMapServiceLayer("http://218.17.162.229:8107/arcgis/rest/services/shenzhen/MapServer");

		mMapView = (MapView) findViewById(R.id.map_view);

		/*
		 * 动态图层一定要在切片图层前面添加,不然定位中心会不对
		 * 没有为什么
		 */
		mMapView.addLayer(dynamicLayer);		
		mMapView.addLayer(tiledLayer); 
		
		MyUtils.setMapViewBackground(mMapView);

		isMove = false;
		mMapView.getChildAt(0).setOnTouchListener(this);

		mMapView.setOnSingleTapListener(new OnSingleTapListener() 
		{
			@Override
			public void onSingleTap(float x, float y)
			{
				if (state == STATE_ADD_TAP)
				{
					Point point = mMapView.toMapPoint(x, y);
					Point point1 = (Point)GeometryEngine.project(point,mMapView.getSpatialReference(), SpatialReference.create(4326));
					longitude = point1.getX();
					latitude = point1.getY();
					DecimalFormat df = new DecimalFormat("###.000000");
					tv_longitude.setText("经度 : " + df.format(longitude));
					tv_latitude.setText("纬度 : " + df.format(latitude));
//					Toast.makeText(getApplicationContext(),"X:"+point1.getX()+",Y:"+point1.getY(),Toast.LENGTH_SHORT).show();
					addNewGraphic(x, y);
					state = STATE_SIMPLE_TAP;
				}
			}

			private void addNewGraphic(float x, float y) 
			{
				GraphicsLayer layer = getGraphicLayer();
				if (layer != null && layer.isInitialized() && layer.isVisible()) 
				{
					// 转换坐标
					Point pt = mMapView.toMapPoint(new Point(x, y));
					// 附加特别的属性
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("tag", "A");
					// 创建 graphic对象
					Graphic gp1 = CreateGraphic(pt, map);
					if(layer != null){
						layer.removeAll();
					}
					// 添加 Graphics 到图层
					layer.addGraphic(gp1);
				}
			}

		});

		mScrollView.setOnTouchListener(new OnTouchListener() 
		{

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				if (event.getAction() == MotionEvent.ACTION_MOVE) 
				{
					if (!isMove)
					{
						isMove = true;
						Bitmap cacheBitmap = null;
						cacheBitmap = mMapView.getDrawingMapCache(0, 0,
								mMapView.getWidth(), mMapView.getHeight());

						Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

						mMapView.setDrawingCacheEnabled(true);
						tempView.setImageBitmap(bitmap);
						mMapView.setVisibility(View.GONE);
						tempView.setVisibility(View.VISIBLE);
					}
				} 
				else 
				{
					if (isMove) 
					{
						isMove = false;
						mHandler.sendEmptyMessageDelayed(0, 1500);
					}
				}
				return false;
			}
		});
	}

	/**
	 * 添加一个要素图标
	 * @param pt
	 * @param map
	 * @return
	 */
	protected Graphic CreateGraphic(Point pt, Map<String, Object> map) 
	{
		GraphicsLayer layer = getGraphicLayer();// 获得图层
		Drawable image = getResources().getDrawable(R.drawable.map_yes);
		PictureMarkerSymbol symbol = new PictureMarkerSymbol(image);
		// 构建graphic
		// Graphic g = new Graphic(geometry, symbol);
		Graphic g = new Graphic(pt, symbol, map);
		return g;
	}

	/**
	 * 获取要素图层
	 * @return
	 */
	protected GraphicsLayer getGraphicLayer() 
	{
		if (mGraphicsLayer == null) 
		{
			mGraphicsLayer = new GraphicsLayer();
			mMapView.addLayer(mGraphicsLayer);
		}
		return mGraphicsLayer;
	}

	@Override
	public void onBackLisenter() 
	{
		super.onBackLisenter();
		finish();
	}

	@Override
	public void onClick(View view) 
	{
		super.onClick(view);
		switch (view.getId())
		{
		case R.id.add_location:
			state = STATE_ADD_TAP;
			break;

		case R.id.zoomout_button:
			mMapView.zoomout();
			break;

		case R.id.fullscreen_button:
			if (isfull) 
			{ // 如果当前是全屏状态,那么就最小化处理
				isfull = false;
				minMapView();
			}
			else 
			{
				// 判断输入法是否打开,如果打开了就关闭输入法，否则地图全屏
				if (!hideSoftInput())
				{
					// 清除输入焦点
					isfull = true;
					maxMapView();
				}
			};

			break;

		default:
			break;
		}
	}

	/**
	 * 最小化地图视图
	 */
	private void minMapView()
	{
		LayoutParams layoutParams = mMapView.getLayoutParams();
		layoutParams.height = height;
		layoutParams.width = width;

		mMapView.setLayoutParams(layoutParams);

		mScrollView.smoothScrollBy(0, -scroll);

		leftBlankView.setVisibility(View.VISIBLE);
		rightBlankView.setVisibility(View.VISIBLE);
		
		fullScreenBtn.setImageResource(R.drawable.fullscreen_btn_selector);
		
		showTitleBanner();
	}

	/**
	 * 最大化地图视图
	 */
	private void maxMapView() 
	{
		// 获取当前地图MapView的坐标和宽高
		hideTitleBanner();

		int pos[] = new int[2];
		mMapView.getLocationOnScreen(pos);

		// 获取MpaView的初始宽度和高度
		if (width == -1 || height == -1) 
		{
			width = mMapView.getWidth();
			height = mMapView.getHeight();
		}

		leftBlankView.setVisibility(View.GONE);
		rightBlankView.setVisibility(View.GONE);

		LayoutParams layoutParams = mMapView.getLayoutParams();
		layoutParams.height = getWindowSize()[1] - getStatusHeight();
		layoutParams.width = getWindowSize()[0];
		mMapView.setLayoutParams(layoutParams);

		scroll = pos[1] - getStatusHeight() - getTitleHeight();
		mScrollView.smoothScrollBy(0, scroll);
		
		fullScreenBtn.setImageResource(R.drawable.exit_fullscreen_btn_selector);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode) 
		{
		/**
		 * 照相返回的图片
		 */
		case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CAMERA:
			if (resultCode == RESULT_OK) 
			{
				if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					Toast.makeText(this, "SD不可用", Toast.LENGTH_SHORT).show();
					return;
				}
				File file = new File(PhotoPickDialog.mUploadPhotoPath);

				// 将拍照的照片路径添加到集合中
				filesPath.add(PhotoPickDialog.mUploadPhotoPath);
				pModule.addBitmap("file://" + PhotoPickDialog.mUploadPhotoPath);
				//					new CropPhotoUtil(this).startPhotoZoom(Uri.fromFile(file));
				//					CropPhotoUtil cropPhoto = new CropPhotoUtil(this);
				//					cropPhoto.startPhotoZoom(Uri.fromFile(file));
			} 
			else 
			{
				Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
			}

			break;

			/**
			 * 本地返回的图片
			 */
		case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION:
			Uri uri = null;
			if (data == null) 
			{
				Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
				return;
			}
			if (resultCode == RESULT_OK) 
			{
				if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) 
				{
					Toast.makeText(this, "SD不可用", Toast.LENGTH_SHORT).show();
					return;
				}

				uri = data.getData();

				filesPath.add(MyUtils.getPathByUri(this, uri));

				System.out.println("cdy_uri" + uri);
				Log.i("zlt", "getPathByUri = " + MyUtils.getPathByUri(this, uri)+" : uri = " + uri);
				pModule.addBitmap(uri.toString());
				//					new CropPhotoUtil(this).startPhotoZoom(uri);
			} 
			else 
			{
				Toast.makeText(this, "照片获取失败", Toast.LENGTH_SHORT).show();
			}
			break;

			/**
			 * 裁剪修改的头像
			 */
		case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CROP:
			if (data == null)
			{
				Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
				return;
			} 
			else 
			{
				//					Bitmap bitmap = new CropPhotoUtil(this).saveCropPhoto(data);

				CropPhotoUtil cropPhotoUtil = new CropPhotoUtil(this);
				Bitmap bitmap = cropPhotoUtil.saveCropPhoto(data);
				//					Log.i("zlt", "crop = " + cropPhotoUtil.getPhotoPath(data));
				//					pModule.addBitmap(bitmap);
			}
			break;

		case ActivityForResultUtil.INTENT:
			// resultPosition = data.getIntExtra("position", 0);
			// if (resultPosition != -1) {
			// historyBeans = (List<AccidentHistoryBean>) data
			// .getSerializableExtra("combackList");
			//
			// LogUtil.e("返回回来的列表长度:" + historyBeans.size());
			// bindResultView();
			// llAdd.setVisibility(View.GONE);
			// llSelect.setVisibility(View.VISIBLE);
			// }
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.unpause();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.pause();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mMapView.recycle();

		super.onDestroy();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		// 处理地图(及地图上面的控件)拖动与外侧ScrollView产生冲突,当手势在地图以及以上控件滑动时，scrollView不接受滑动事件
		if (event.getAction() == MotionEvent.ACTION_UP) {
			mScrollView.requestDisallowInterceptTouchEvent(false);
		} else {
			mScrollView.requestDisallowInterceptTouchEvent(true);
		}
		return false;
	}

	/**
	 * 监听更多
	 */
	@Override
	public void onMoreLisenter()
	{
		Map<String, Object> contentMap = submitcontentmodule.getContent();
		
		/**
		 * 根据Type判断调用当前是哪个界面
		 * 0 事故报修
		 * 1 突发事件
		 * 2 施工上报
		 */
		switch (MyApp.TYPE) {
		
		case 0:
			repair = (String) contentMap.get("repair");
			device_numbering = (String) contentMap.get("device_numbering");
			device_name = (String) contentMap.get("device_name");
			repair_time = (String) contentMap.get("repair_time");

			if(TextUtils.isEmpty(repair)){
				showToast("报修区域不能为空！");
				return;
			}

			if (TextUtils.isEmpty(repair_time)) 
			{
				showToast("报修时间不能为空！");
				return;
			}

//			if (longitude == 0|| latitude == 0)
//			{
//				showToast("经纬度不能为空！");
//				return;
//			}
			
			if (TextUtils.isEmpty(String.valueOf(factormodule.getFaultType()))) 
			{
				showToast("故障类型不能为空！");
				return;
			}

			if (TextUtils.isEmpty(factormodule.getFaultContent()))
			{
				showToast("存在问题不能为空！");
				return;
			}

			if (TextUtils.isEmpty(et_describe.getText().toString().trim()))
			{
				showToast("故障描述不能为空！");
				return;
			}

			if (TextUtils.isEmpty(isDealWith))
			{
				showToast("现场是否处理不能为空！");
				return;
			}
			
			if (longitude == 0 || latitude == 0)
			{
				showDialog();
			}
			else
			{
				submit();
			}
			
			break;

		case 1:
			title = (String) contentMap.get("title");
			occurrence_region = (String) contentMap.get("occurrence_region");
			occurrence_time = (String) contentMap.get("occurrence_time");
			if(TextUtils.isEmpty(title)){
				showToast("事件标题不能为空！");
				return;
			}
			if(TextUtils.isEmpty(occurrence_region)){
				showToast("发生区域不能为空！");
				return;
			}
			if(TextUtils.isEmpty(occurrence_time)){
				showToast("发生时间不能为空！");
				return;
			}

//			if (longitude == 0|| latitude == 0)
//			{
//				showToast("经纬度不能为空！");
//				return;
//			}

			if (TextUtils.isEmpty(et_describe.getText().toString().trim()))
			{
				showToast("突发事件描述不能为空！");
				return;
			}
			
			if (longitude == 0 || latitude == 0)
			{
				showDialog();
			}
			else
			{
				submit();
			}
			
			break;

		case 2:
			construction_organization = (String) contentMap.get("construction_organization");
			construction_area = (String) contentMap.get("construction_area");
			//			completion_date = (String) contentMap.get("completion_date");
			start_time =  (String) contentMap.get("start_time");
			end_time = (String) contentMap.get("end_time");
			if(radio_yes.isChecked()){
				isFire = "1";
			}else{
				isFire = "0";
			}

			if(TextUtils.isEmpty(construction_organization)){
				showToast("施工单位不能为空！");
				return;
			}
			if(TextUtils.isEmpty(construction_area)){
				showToast("施工区域不能为空！");
				return;
			}
			//			if(TextUtils.isEmpty(completion_date)){
			//				showToast("完工日期不能为空！");
			//				return;
			//			}
			if(TextUtils.isEmpty(start_time)){
				showToast("开始时间不能为空！");
				return;
			}
			if(TextUtils.isEmpty(end_time)){
				showToast("结束时间不能为空！");
				return;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA);
			Date start_date = null;
			Date end_date = null;
			try 
			{
				start_date = (Date) sdf.parse(start_time);
				end_date = (Date) sdf.parse(end_time);
			} 
			catch (ParseException e)
			{
				e.printStackTrace();
			}

			if (start_date.compareTo(end_date) > 0) 
			{
				showToast("结束时间不能小于开始时间！");
				return;
			}
			
			if (longitude == 0 || latitude == 0)
			{
				showDialog();
			}
			else
			{
				submit();
			}
				
			break;
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 屏蔽任务键直接提交任务
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isfull) {
				isfull = false;
				minMapView();
			}else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void showDialog()
	{
		LayoutInflater inflater = LayoutInflater.from(this);
		final Dialog dialog = new Dialog(this, R.style.CommonDialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);

		View view2 = inflater.inflate(R.layout.dialog_version, null);
		TextView dialog_content = (TextView) view2
				.findViewById(R.id.no_app_title);
		Button ok = (Button) view2.findViewById(R.id.dialog_view_ok);
		ok.setText("确定");
		dialog_content.setText("您未选择报修点，是否以当前位置提交?");
		view2.findViewById(R.id.dialog_view_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						showToast("请选择报修位置!");
					}
				});
		view2.findViewById(R.id.dialog_view_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialog.dismiss();
						longitude = locaitonLongitude;
						latitude = locationLatitude;
						
						submit();
					}
				});
		dialog.setContentView(view2);
		dialog.show();
	}
	
	private void submit()
	{
		switch (MyApp.TYPE)
		{
			case 0:
				requestUrl = UserHttp.accidentHandlingSubmit(repair, device_numbering, factormodule.getFaultContent(), factormodule.getFaultType(), repair_time, MyApp.userInfo.getStaffId(), et_describe.getText().toString().trim(), isDealWith, String.valueOf(longitude), String.valueOf(latitude));
				new Thread(multiThread).start();
				break;
				
			case 1:
				requestUrl = UserHttp.emergencySubmit(title, occurrence_region, occurrence_time, String.valueOf(longitude), String.valueOf(latitude), et_describe.getText().toString().trim(), MyApp.userInfo.getStaffId());
				new Thread(multiThread).start();
				break;
				
			case 2:
				mHandler.sendEmptyMessage(4);
				break;
	
			default:
				break;
		}
		
		mProgressDialog = new ProgressDialog(AccidentHandlingActivity.this);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setMessage("正在上报中，请稍后...");
		mProgressDialog.show();
	}
	
	
}
