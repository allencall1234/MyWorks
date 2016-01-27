package com.xj_pipe.view;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.xj_pipe.adapter.CommonAdapter;
import com.xj_pipe.base.BaseActivity;
import com.xj_pipe.common.ActivityForResultUtil;
import com.xj_pipe.common.CropPhotoUtil;
import com.xj_pipe.common.PhotoModule;
import com.xj_pipe.common.PhotoPickDialog;

/**
 * 巡检点签到界面
 * @author Administrator
 *
 */
public class InspectionSignInActivity extends BaseActivity 
{
	private TextView tv_inspectionPerson;// 巡检员
	private TextView tv_inspectionArea;// 巡检区域
	private TextView tv_inspectionPoint;// 巡检点
	private TextView tv_inspectionDate;// 巡检日期
	
	private RadioGroup rg_inspectionState;// 巡检状态
	private RadioButton rb_pointNormal, rb_pointZhengGai;
	
	private EditText et_cause;// 不合格原因说明
	
	private Button btn_cancel;
	private Button btn_confirm;
	
	private CommonAdapter<Bitmap> glList;
	
	private PhotoModule pModule = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_inspection_signin);
		
		initView();
	}

	private void initView() 
	{
		setCenterTitle("巡检签到");
		
		tv_inspectionPerson = (TextView) findViewById(R.id.tv_inspectionPerson);
		tv_inspectionArea = (TextView) findViewById(R.id.tv_inspectionArea);
		tv_inspectionPoint = (TextView) findViewById(R.id.tv_inspectionPoint);
		tv_inspectionDate = (TextView) findViewById(R.id.tv_inspectionDate);
		
		rg_inspectionState = (RadioGroup) findViewById(R.id.rg_inspectionState);
		
		rb_pointNormal = (RadioButton) findViewById(R.id.rb_pointNormal);
		rb_pointZhengGai = (RadioButton) findViewById(R.id.rb_pointZhengGai);
		
		et_cause = (EditText) findViewById(R.id.et_cause);
		
		pModule = (PhotoModule) findViewById(R.id.photo_module);
		
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		
		btn_cancel.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		
		rg_inspectionState.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				switch (checkedId)
				{
					case R.id.rb_pointNormal:
					     rb_pointNormal.setChecked(true);
						break;
						
					case R.id.rb_pointZhengGai:
					     rb_pointZhengGai.setChecked(true);
						break;
	
					default:
						break;
				}
				
			}
		});
		
	}
	
	@Override
	public void onClick(View v) 
	{
		super.onClick(v);
		switch (v.getId()) 
		{
			case R.id.btn_cancel:
				
				break;
				
			case R.id.btn_confirm:
				
				break;
			default:
				break;
		}
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
				if (resultCode == RESULT_OK) {
					if (!Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						Toast.makeText(this, "SD不可用", Toast.LENGTH_SHORT).show();
						return;
					}
					File file = new File(PhotoPickDialog.mUploadPhotoPath);
					new CropPhotoUtil(this).startPhotoZoom(Uri.fromFile(file));
				} else {
					Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
				}
				break;
			/**
			 * 本地返回的图片
			 */
			case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION:
				Uri uri = null;
				if (data == null) {
					Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
					return;
				}
				if (resultCode == RESULT_OK) {
					if (!Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						Toast.makeText(this, "SD不可用", Toast.LENGTH_SHORT).show();
						return;
					}
					uri = data.getData();
					new CropPhotoUtil(this).startPhotoZoom(uri);
				} else {
					Toast.makeText(this, "照片获取失败", Toast.LENGTH_SHORT).show();
				}
				break;
			/**
			 * 裁剪修改的头像
			 */
			case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CROP:
				if (data == null) {
					Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
					return;
				} else {
					Bitmap bitmap = new CropPhotoUtil(this).saveCropPhoto(data);
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

}
