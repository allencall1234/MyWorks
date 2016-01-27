package com.xj_pipe.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.xj_pipe.adapter.ArrayAdapter;
import com.xj_pipe.base.MyApp;
import com.xj_pipe.bean.EquipmentBean;
import com.xj_pipe.https.HttpDataCallBack;
import com.xj_pipe.https.UserHttp;
import com.xj_pipe.utils.JsonUtils;
import com.xj_pipe.utils.MyUtils;
import com.xj_pipe.utils.ToastUtils;
import com.xj_pipe.view.R;

/**
 * 事故保修、突发事件、施工上报内容填写模块 调用公共方法getContent()，返回输入的内容
 * 
 * @author qinfan
 *
 *         2015年12月30日
 */
public class SubmitContentModule extends LinearLayout implements
		OnClickListener {
	private Context mContext;
	/**
	 * 事故报修、突发事件、施工上报提交界面的布局、TextView以及输入框
	 */
	private LinearLayout ll_submit_three, ll_submit_four, ll_submit_five;
	private TextView tv_submit_one, tv_submit_two, tv_submit_three, tv_equipmentNumber;
	private EditText et_submit_one, et_submit_two, et_submit_three;

	private EditText start_time, end_time;// 施工上报开始时间、结束时间

	private EditText editText;

	public EditText getEditText() {
		return editText;
	}

	public void setEditText(EditText editText) {
		this.editText = editText;
	}

	private DatePickerDialog datePickerDialog;
	private String date = "";

	private List<String> spinnerArray = new ArrayList<String>();
	
	private List<EquipmentBean> equipList = new ArrayList<EquipmentBean>();;
	
	private AutoCompleteTextView autotv_equipmentNumber = null;// 报修设备编号

	private ArrayAdapter<String> adapter;

	public static final int TYPE_ACCIDENT_WARRANTY = 0;// 事故报修
	public static final int TYPE_EMERGENCY = 1; // 突发事件
	public static final int TYPE_CONSTRUCTION_REPORT = 2; // 施工上报

	public SubmitContentModule(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.map_module_layout, this);
		mContext = context;

		initViews();
	}

	private void initViews() {
		/**
		 * 事件报修、突发事件、施工上报提交界面地图以上内容每行布局
		 */
		ll_submit_three = (LinearLayout) findViewById(R.id.ll_submit_three);
		ll_submit_four = (LinearLayout) findViewById(R.id.ll_submit_four);
		ll_submit_five = (LinearLayout) findViewById(R.id.ll_submit_five);
		/**
		 * 事件报修、突发事件、施工上报提交界面地图以上内容每行textView
		 */
		tv_submit_one = (TextView) findViewById(R.id.tv_submit_one);
		tv_submit_two = (TextView) findViewById(R.id.tv_submit_two);
		tv_submit_three = (TextView) findViewById(R.id.tv_submit_three);
		tv_equipmentNumber = (TextView) findViewById(R.id.tv_equipmentNumber);
		/**
		 * 事件报修、突发事件、施工上报提交界面地图以上内容每行输入框
		 */
		et_submit_one = (EditText) findViewById(R.id.et_submit_one);
		et_submit_two = (EditText) findViewById(R.id.et_submit_two);
		et_submit_three = (EditText) findViewById(R.id.et_submit_three);
		/**
		 * 施工上报开始时间、结束时间
		 */
		start_time = (EditText) findViewById(R.id.statr_time);
		end_time = (EditText) findViewById(R.id.end_time);

		autotv_equipmentNumber = (AutoCompleteTextView) findViewById(R.id.autotv_equipmentNumber);
		autotv_equipmentNumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				autotv_equipmentNumber.showDropDown();
			}
		});
		
//		autotv_equipmentNumber.addTextChangedListener(new TextWatcher() 
//		{
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				// TODO Auto-generated method stub
//				ToastUtils.ShowMessage(mContext, "onTextChanged");
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//				ToastUtils.ShowMessage(mContext, "beforeTextChanged");
//			}
//			
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				ToastUtils.ShowMessage(mContext, "afterTextChanged" + autotv_equipmentNumber.getText().toString().trim());
//				handler.sendEmptyMessage(0);
//			}
//		});
		
		et_submit_three.setFocusable(false);
		start_time.setFocusable(false);
		end_time.setFocusable(false);

		et_submit_three.setOnClickListener(this);
		start_time.setOnClickListener(this);
		end_time.setOnClickListener(this);

		datePickerDialog = new DatePickerDialog(mContext,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthofYear, int dayofMonth) {
						date = year + "-" + (monthofYear + 1) + "-"
								+ dayofMonth;
						getEditText().setText(date);
					}
				}, MyUtils.getmYear(), MyUtils.getmMonth(), MyUtils.getmDay());

		adapter = new ArrayAdapter<String>(mContext,
				R.layout.custom_spinner_layout, spinnerArray);

		switch (getType()) {
		case TYPE_ACCIDENT_WARRANTY:// 事故报修
			// ll_submit_three.setVisibility(View.GONE);
			ll_submit_five.setVisibility(View.GONE);

			tv_submit_one.setText("报修区域：");
			tv_submit_two.setText("设备名称：");
			tv_submit_three.setText("报修时间：");
			tv_equipmentNumber.setText("设备编号：");

			et_submit_one.setHint("填写报修区域");
			et_submit_two.setHint("填写设备名称/可不填");
			et_submit_three.setHint("选择发生时间");
			
			autotv_equipmentNumber.setAdapter(adapter);
			
			autotv_equipmentNumber.setOnItemClickListener(new OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id)
				{
					et_submit_two.setText(equipList.get(position).getName());
					autotv_equipmentNumber.setText(equipList.get(position).getNo());
				}
			});
			
			break;

		case TYPE_CONSTRUCTION_REPORT:// 施工上报
			ll_submit_four.setVisibility(View.GONE);
			ll_submit_three.setVisibility(View.GONE);

			tv_submit_one.setText("施工单位：");
			tv_submit_two.setText("施工区域：");
			// tv_submit_three.setText("完成工期：");

			et_submit_one.setHint("填写施工单位");
			et_submit_two.setHint("填写施工区域");
			// et_submit_three.setHint("选择日期");
			break;

		case TYPE_EMERGENCY:// 突发事件
			ll_submit_four.setVisibility(View.GONE);
			ll_submit_five.setVisibility(View.GONE);

			tv_submit_one.setText("事件标题：");
			tv_submit_two.setText("发生区域：");
			tv_submit_three.setText("发生时间：");

			et_submit_one.setHint("填写事件标题");
			et_submit_two.setHint("填写发生区域");
			et_submit_three.setHint("选择发生时间");
			break;
		}
		
		handler.sendEmptyMessage(0);

	}

	/**
	 * 获取当前处理类型
	 * 
	 * @return
	 */
	public int getType() {
		return MyApp.TYPE;
	}

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				UserHttp.equipmentNumberQuery(autotv_equipmentNumber.getText().toString().trim(), new HttpDataCallBack() {
					@Override
					public void HttpSuccess(String _result) {
						
						getEquipmentNumber(_result);
						adapter.notifyDataSetChanged();
						System.out.println("cdy" + _result);
					}

					@Override
					public void HttpFail(int ErrCode) {
						ToastUtils.ShowMessage(mContext, "网络请求失败，请检查您的网络!");
					}
				});

			default:
				break;
			}
			return false;
		}
	});

	/**
	 * 获取输入框的内容
	 * 
	 * @return
	 */
	public Map<String, Object> getContent() {
		Map<String, Object> contentMap = new HashMap<String, Object>();

		switch (getType()) {
		case TYPE_ACCIDENT_WARRANTY:// 事件报修
			contentMap.put("repair", et_submit_one.getText().toString());// 报修区域
			contentMap.put("device_name", et_submit_two.getText().toString());// 设备名称
			contentMap.put("repair_time", et_submit_three.getText().toString());// 报修时间
			// spinnerArray =
			// getResources().getStringArray(R.array.construct_area);
			contentMap.put("device_numbering", autotv_equipmentNumber.getText()
					.toString());// 设备编号
			break;

		case TYPE_CONSTRUCTION_REPORT:// 施工上报
			contentMap.put("construction_organization", et_submit_one.getText()
					.toString());// 施工单位
			contentMap.put("construction_area", et_submit_two.getText()
					.toString());// 施工区域
			// contentMap.put("completion_date",
			// et_submit_three.getText().toString());//完工日期
			contentMap.put("start_time", start_time.getText().toString());// 开始时间
			contentMap.put("end_time", end_time.getText().toString());// 结束时间
			break;

		case TYPE_EMERGENCY:// 突发事件
			contentMap.put("title", et_submit_one.getText().toString());// 事件标题
			contentMap.put("occurrence_region", et_submit_two.getText()
					.toString());// 发生区域
			contentMap.put("occurrence_time", et_submit_three.getText()
					.toString());// 发生时间
			break;
		}

		return contentMap;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.et_submit_three:
			setEditText(et_submit_three);
			datePickerDialog.show();
			break;

		case R.id.statr_time:
			setEditText(start_time);
			datePickerDialog.show();
			break;

		case R.id.end_time:
			setEditText(end_time);
			datePickerDialog.show();
			break;
		}
	}

	private void getEquipmentNumber(String result) {

		spinnerArray.clear();
		equipList.clear();
		
		try {
			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("equipList");
			if (array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					spinnerArray.add(array.getJSONObject(i).getString("no"));
					EquipmentBean bean = (EquipmentBean) JsonUtils.putJsonToObject(array.getJSONObject(i), "com.xj_pipe.bean.EquipmentBean");
					equipList.add(bean);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
