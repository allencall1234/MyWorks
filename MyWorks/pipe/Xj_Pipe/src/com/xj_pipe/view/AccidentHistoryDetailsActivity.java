package com.xj_pipe.view;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.xj_pipe.adapter.PhotoGridViewAdapter;
import com.xj_pipe.base.BaseActivity;
import com.xj_pipe.base.MyApp;
import com.xj_pipe.bean.BuildBean;
import com.xj_pipe.bean.EmergencyBean;
import com.xj_pipe.bean.FaultBean;
import com.xj_pipe.common.MyGridView;
import com.xj_pipe.https.HttpURL;
import com.xj_pipe.utils.MyUtils;

/**
 * 历史报修详情界面
 * @author Administrator
 *
 */
public class AccidentHistoryDetailsActivity extends BaseActivity implements OnTouchListener
{
	private ScrollView scrollView;
	// 报修区域
	private TextView tv_repairArea;
	private TextView tv_repair_area;
	// 设备编号
	private TextView tv_equipmentNumber;
	private TextView tv_equipment_number;
	// 设备名称
	private TextView tv_equipmentName;
	private TextView tv_equipment_name;
	// 施工期限
	private LinearLayout ll_buildDate;
	private TextView tv_build_date;
	// 现场是否处理
	private LinearLayout ll_isDealwith;
	private TextView tv_isDealwith;
	private TextView tv_is_dealwith;
	// 地理位置(经纬度)
	private TextView tv_longitude;
	private TextView tv_latitude;
	// 巡检要素
	private LinearLayout ll_inspectionElements, fault_description_layout,queipmentName_layout;
	// 故障类型
	private TextView tv_accident_type;
	// 存在问题
	private TextView tv_problem;
	// 故障描述
	private TextView tv_fault_describe;
	
	private MapView mMapView;
	private ImageView tempView;
	
	private ArcGISDynamicMapServiceLayer dynamicLayer = null; // 地图动态服务图层
	private ArcGISTiledMapServiceLayer tiledLayer = null; // 地图切片服务图层
	private GraphicsLayer mGraphicsLayer = null; // 地图要素服务图层
	
	private LinearLayout ll_image;
	private MyGridView gv_scenePhoto;// 现场照片
	
	// 事故报修
	private  FaultBean faultBean = null;
	
	// 突发事件
	private EmergencyBean emergencyBean = null;
	
	// 施工反馈
	private BuildBean buildBean = null;
	
	private PhotoGridViewAdapter adapter = null;
	
	private String filePath = null;
	
	private List<String> imgUrlList = null;
	
	private String text1, text2, text3, text4, text5, text6, text7, text8, longitude, latitude, describe;
	
	private ProgressDialog mProgressDialog = null;
	private boolean isInitialed;
	private boolean isMove;
	
	private Point curPoint = null;
	
	private boolean isFirstIn = false;
	
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
				
			case 1:
//				curPoint = new Point();
//				curPoint.setXY(113.931776, 22.548388);
				mMapView.zoomToScale(curPoint, 5326);
				break;

			default:
				break;
			}	
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_accident_history_details);
		
		initView();
	}
	
	private void initView() 
	{
		faultBean = (FaultBean) getIntent().getSerializableExtra("FaultBean");
		emergencyBean = (EmergencyBean) getIntent().getSerializableExtra("EmergencyBean");
		buildBean = (BuildBean) getIntent().getSerializableExtra("BuildBean");
		
		hideLeftLogo();
		
		imgUrlList = new ArrayList<String>();
		
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		
		// 报修区域
		tv_repairArea = (TextView) findViewById(R.id.tv_repairArea);
		tv_repair_area = (TextView) findViewById(R.id.tv_repair_area);
		// 设备编号
		tv_equipmentNumber = (TextView) findViewById(R.id.tv_equipmentNumber);
		tv_equipment_number = (TextView) findViewById(R.id.tv_equipment_number);
		// 设备名称
		tv_equipmentName = (TextView) findViewById(R.id.tv_equipmentName);
		tv_equipment_name = (TextView) findViewById(R.id.tv_equipment_name);
		// 施工期限
		ll_buildDate = (LinearLayout) findViewById(R.id.ll_buildDate);
		tv_build_date = (TextView) findViewById(R.id.tv_build_date);
		// 现场是否处理
		ll_isDealwith = (LinearLayout) findViewById(R.id.ll_isDealwith);
		tv_isDealwith = (TextView) findViewById(R.id.tv_isDealwith);
		tv_is_dealwith = (TextView) findViewById(R.id.tv_is_dealwith);
		
		// 经纬度
		tv_longitude = (TextView) findViewById(R.id.tv_longitude);
		tv_latitude = (TextView) findViewById(R.id.tv_latitude);
		// 巡检要素
		ll_inspectionElements = (LinearLayout) findViewById(R.id.ll_inspectionElements);
		tv_accident_type = (TextView) findViewById(R.id.tv_accident_type);
		tv_problem = (TextView) findViewById(R.id.tv_problem);
		tv_fault_describe = (TextView) findViewById(R.id.tv_fault_describe);
		
		mMapView = (MapView) findViewById(R.id.map_view);
		
		ll_image = (LinearLayout) findViewById(R.id.ll_image);
		gv_scenePhoto = (MyGridView) findViewById(R.id.gv_scenePhoto);
		
		fault_description_layout = (LinearLayout) findViewById(R.id.fault_description_layout);
		queipmentName_layout = (LinearLayout) findViewById(R.id.queipmentName_layout);
		
//		scrollView.smoothScrollTo(0, 20);
		
		ArcGISRuntime.setClientId("uK0DxqYT0om1UXa9");
//		tiledLayer = new ArcGISTiledMapServiceLayer("http://192.168.1.158:6080/arcgis/rest/services/CACH/MapServer");
//		tiledLayer = new ArcGISTiledMapServiceLayer("http://192.168.1.158:6080/arcgis/rest/services/shenzhen/MapServer");
		tiledLayer = new ArcGISTiledMapServiceLayer("http://218.17.162.229:8107/arcgis/rest/services/CACH/MapServer");

		dynamicLayer = new ArcGISDynamicMapServiceLayer("http://218.17.162.229:8107/arcgis/rest/services/shenzhen/MapServer");

		mMapView = (MapView) findViewById(R.id.map_view);
		tempView = (ImageView) findViewById(R.id.temp_view);
		
		mMapView.addLayer(dynamicLayer);
		mMapView.addLayer(tiledLayer);
		
		MyUtils.setMapViewBackground(mMapView);
		
		isMove = false;
		mMapView.getChildAt(0).setOnTouchListener(this);
		
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
		
		scrollView.setOnTouchListener(new OnTouchListener() 
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
		
		// 设置详情内容
		setDetailsContent();
		
		tv_repairArea.setText(text1);
		tv_repair_area.setText(text2);
		
		tv_equipmentNumber.setText(text3);
		tv_equipment_number.setText(text4);
		
		tv_equipmentName.setText(text5);
		tv_equipment_name.setText(text6);
		
		tv_isDealwith.setText(text7);
		tv_is_dealwith.setText(text8);
		
		tv_longitude.setText("经度 : " + longitude);
		tv_latitude.setText("纬度 : " + latitude);
		
		tv_fault_describe.setText("  " + describe);
		
		if (!TextUtils.isEmpty(longitude) && !TextUtils.isEmpty(latitude)) 
		{
			
			curPoint = new Point(Double.parseDouble(longitude), Double.parseDouble(latitude));
			Log.i("ppp", "longtitude = " + curPoint.getX() + ",latidude = " + curPoint.getY());
			addNewGraphic(Double.parseDouble(longitude), Double.parseDouble(latitude));
//			mMapView.zoomToScale((Point) GeometryEngine.project(curPoint, mMapView.getSpatialReference(), SpatialReference.create(4326)), 10326f);
//			mMapView.zoomToScale(curPoint, 5326f); 
			isFirstIn = true;
			
		}
		
		adapter = new PhotoGridViewAdapter(this, getImageUrl());
		gv_scenePhoto.setAdapter(adapter);
		
		gv_scenePhoto.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{
				Intent intent = new Intent(AccidentHistoryDetailsActivity.this, PhotoActivity.class);
				intent.putExtra("position", position);
				intent.putStringArrayListExtra("bitmaps", (ArrayList<String>) imgUrlList);
				startActivity(intent);
				
			}
		});
		if (isFirstIn) {
			mHandler.sendEmptyMessageDelayed(1,500);
			isFirstIn = false;
		}
	}

	private void setDetailsContent() 
	{
		if (MyApp.TYPE == 0)
		{
			setCenterTitle("事故报修历史详情");
			text1 = "报修区域 : ";
			text2 = faultBean.getPlace();
			text3 = "设备编号 : ";
			text4 = faultBean.getLineId();
			text5 = "设备名称 : ";
			text6 = faultBean.getEquipName();
			text7 = "现场是否处理完成 : ";
			
			if (faultBean.getFlag().equals("N")) 
			{
				text8 = "否";
			}
			else
			{
				text8 = "是";
			}
			
			longitude = faultBean.getLongitude();
			latitude = faultBean.getLatitude();
			describe = faultBean.getContent();
			ll_inspectionElements.setVisibility(View.VISIBLE);
			tv_accident_type.setText(faultBean.getTypeName());
			tv_problem.setText(faultBean.getExistProblem());
			
			filePath = faultBean.getReportStageFilePath();
		}
		else if (MyApp.TYPE == 1)
		{
			setCenterTitle("突发事件历史详情");
			ll_isDealwith.setVisibility(View.GONE);
			text1 = "事件标题 : ";
			text2 = emergencyBean.getTitle();
			text3 = "发生区域 : ";
			text4 = emergencyBean.getArea();
			text5 = "发生时间 : ";
			text6 = emergencyBean.getHappenDate();
			longitude = emergencyBean.getLongitude();
			latitude = emergencyBean.getLatitude();
			describe = emergencyBean.getDescription();
			
			filePath = emergencyBean.getFilePath();
		}
		else
		{
			setCenterTitle("施工反馈历史详情");
			text1 = "施工单位 : ";
			text2 = buildBean.getUnit();
			text3 = "施工区域 : ";
			text4 = buildBean.getArea();
//			text5 = "完工日期 : ";
//			text6 = buildBean.getFinishDate();
			queipmentName_layout.setVisibility(View.GONE);
			text7 = "是否动火 : ";
			if (buildBean.getIsFire().equals("0"))
			{
				text8 = "否";
			}
			else
			{
				text8 = "是";
			}
			ll_buildDate.setVisibility(View.VISIBLE);
			tv_build_date.setText(buildBean.getBeginTime() + "——" + buildBean.getEndTime());
			ll_image.setVisibility(View.GONE);
			fault_description_layout.setVisibility(View.GONE);
			longitude = buildBean.getLongitude();
			latitude = buildBean.getLatitude();
			describe = buildBean.getContent();
		}
	}

	@Override
	public void onBackLisenter()
	{
		super.onBackLisenter();
		finish();
	}
	
	private List<String> getImageUrl()
	{
		if(!TextUtils.isEmpty(filePath))
		{
			String[] paths = filePath.split(",");
			for(String path : paths)
			{
//				String[] mm = path.split("\\/");
				String[] imgUrl = path.split("pis");
//				PhotoBean photoBean = new PhotoBean();
//				photoBean.setImgName(mm[mm.length-1]);
//				photoBean.setImgUrl(path);
				imgUrlList.add(HttpURL.SERVICE_URL + imgUrl[imgUrl.length - 1]);
			}
		}
		 
		return imgUrlList;
		
	}
	
	private void addNewGraphic(double x, double y) 
	{
		GraphicsLayer layer = getGraphicLayer();
		if (layer != null && layer.isInitialized() && layer.isVisible()) 
		{
			// 转换坐标
			Point pt = new Point(x, y);
//			Point point1 = (Point)GeometryEngine.project(new Point(x, y), mMapView.getSpatialReference(), SpatialReference.create(4326));
			// 附加特别的属性
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tag", "A");
			// 创建 graphic对象
			Graphic gp1 = CreateGraphic(pt, map);
			if(layer != null)
			{
				layer.removeAll();
			}
			// 添加 Graphics 到图层
			layer.addGraphic(gp1);
		}
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
	public boolean onTouch(View v, MotionEvent event) {

		// 处理地图(及地图上面的控件)拖动与外侧ScrollView产生冲突,当手势在地图以及以上控件滑动时，scrollView不接受滑动事件
		if (event.getAction() == MotionEvent.ACTION_UP) 
		{
			scrollView.requestDisallowInterceptTouchEvent(false);
		} 
		else
		{
			scrollView.requestDisallowInterceptTouchEvent(true);
		}
		return false;
	}

}



