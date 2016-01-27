package com.xj_pipe.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xj_pipe.adapter.CommonAdapter;
import com.xj_pipe.adapter.ViewHolder;
import com.xj_pipe.bean.InspectionFactor;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;
import com.xj_pipe.utils.JsonUtils;
import com.xj_pipe.view.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 巡检要素功能模块,可以直接复用
 * <p>
 * 输入公共方法为getSelectedIds(),返回选中的点
 * 
 * @author Administrator
 * 
 */
public class FactorModule extends LinearLayout {
	// 更新UI消息,当正确接收到网络收据时发送
	private final static int MSG_UPDATE_UI = 100;
	// 所有FactorBean对象的预设id
	private Context mContext;

	private RadioGroup factorTitle = null;
	private GridView factorView = null;

	private CommonAdapter<InspectionFactor> adapter = null; // 适配器

	/** 获取所有的巡检要素 */
	private List<InspectionFactor> mlist;
	/** 巡检父要素列表 */
	private List<InspectionFactor> titleList;
	/** 巡检子要素列表 */
	private List<InspectionFactor> itemList;

	/** 父要素ID */
	private String titleId = "-1";

	/** 选中的子要素ID */
	private String lastId = "-1";

	/** 选中的子要素的具体内容 */
	private String lastContent = "";

	private Handler mHandler = new Handler() {
		@Override
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_UPDATE_UI:

				for (int i = factorTitle.getChildCount() - 1; i >= 0; i--) {
					if (i > titleList.size() - 1) {
						factorTitle.getChildAt(i).setVisibility(View.GONE); // 预设4个小标题，当实际网络获取标题数目不够4个时,隐藏多出部分
					} else {
						RadioButton radio = (RadioButton) factorTitle
								.getChildAt(i);
						radio.setText(titleList.get(i).getBaseName());
//						radio.setTag(titleList.get(i).getBaseId());
					}
				}

				factorTitle.check(R.id.radio1);

				break;

			default:
				break;
			}
		};
	};

	public FactorModule(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.factor_module_layout,
				this);

		mContext = context;

		factorTitle = (RadioGroup) findViewById(R.id.factor_title);
		factorView = (GridView) findViewById(R.id.factor_view);

		titleList = new ArrayList<InspectionFactor>();
		itemList = new ArrayList<InspectionFactor>();
		mlist = new ArrayList<InspectionFactor>();
		
		factorView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if (!itemList.get(position).getBaseId().equals(lastId)) {
					lastId = itemList.get(position).getBaseId();
					lastContent = itemList.get(position).getBaseName();
					adapter.notifyDataSetChanged();
				}
				// System.out.println("IDS = " + getSelectedIds());
			}
		});

		factorView.setAdapter(adapter = new CommonAdapter<InspectionFactor>(
				mContext, itemList, R.layout.factor_item) {
			@Override
			public void convert(ViewHolder viewHolder, InspectionFactor item) {

				viewHolder.setText(R.id.factor_check, item.getBaseName());
				viewHolder.setChecked(R.id.factor_check, item.getBaseId()
						.equals(lastId));
			}
		});

		factorTitle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.radio1:
					titleId = titleList.get(0).getBaseId();
					break;
				case R.id.radio2:
					titleId = titleList.get(1).getBaseId();
					break;
				case R.id.radio3:
					titleId = titleList.get(2).getBaseId();
					break;
				case R.id.radio4:
					titleId = titleList.get(3).getBaseId();
					break;
				default:
					break;
				}
				getItemList();
			}
		});

	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();

		UserHttp.getInspectionFactor(new HttpDataCallBack() {

			@Override
			public void HttpSuccess(String _result) {
				// TODO Auto-generated method stub
				getTaskListListData(_result);

				mHandler.sendEmptyMessage(MSG_UPDATE_UI);
			}

			@Override
			public void HttpFail(int ErrCode) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void getItemList() {

		itemList.clear();

		for (InspectionFactor factor : mlist) {
			if (titleId.equals(factor.getParentBaseId())) {
				itemList.add(factor);
			}
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 从网络获取巡检要素点内容
	 * 
	 * @param result
	 */
	private void getTaskListListData(String result) {

		System.out.println("result = " + result);

		mlist.clear();

		try {
			JSONObject object = new JSONObject(result);
			
			/*
			 * 初始化mList,装载所有的子巡检点
			 */
			JSONObject son = object.getJSONObject("sonBaseData");
			
			Iterator<String> keys = son.keys();
			String key;
			
			while (keys.hasNext()) {
				key = keys.next();
				JSONArray tempArray = son.getJSONArray(key);
				
				for(int i = 0;i<tempArray.length();i++){
					JSONObject tempObject = tempArray.getJSONObject(i);
					InspectionFactor tempFactor = JsonUtils.putJsonToObject(tempObject, InspectionFactor.class);
					mlist.add(tempFactor);
				}
			}
			
			
			/*
			 * 初始化titleList
			 */
			titleList.clear();
			JSONArray pArray = object.getJSONArray("parentBaseData");
			for (int j = 0; j < pArray.length(); j++) {
				JSONObject pObject = pArray.getJSONObject(j);
				InspectionFactor pFactor = JsonUtils.putJsonToObject(pObject,
						InspectionFactor.class);
				titleList.add(pFactor);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/** 缺陷类型 */
	public int getFaultType() {
		// TODO Auto-generated method stub
		return Integer.valueOf(titleId);
	}

	/** 存在问题 */
	public String getFaultContent() {
		return lastContent;
	}

}
