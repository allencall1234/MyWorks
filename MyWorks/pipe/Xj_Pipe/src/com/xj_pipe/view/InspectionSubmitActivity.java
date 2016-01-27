package com.xj_pipe.view;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xj_pipe.base.BaseActivity;
import com.xj_pipe.common.ActivityForResultUtil;
import com.xj_pipe.common.CropPhotoUtil;
import com.xj_pipe.common.PhotoModule;
import com.xj_pipe.common.PhotoPickDialog;
import com.xj_pipe.https.UserHttp;
import com.xj_pipe.utils.MyUtils;
import com.xj_pipe.utils.ToastUtils;

/**
 * 任务提交页
 * @author Administrator
 *
 */
public class InspectionSubmitActivity extends BaseActivity {

	private Button inspSubmitBtn = null;
	private EditText et_inspection_describe = null;
	private String taskId = "";
	
	// 保存文件路径集合
	private List<String> filesPath = new ArrayList<String>();
	
	private String requestUrl = null;// 请求的网址和参数
	
	private PhotoModule pModule = null;//选择照片
	
	private boolean flag = false;// 标识操作结果
	
	private ProgressDialog mProgressDialog = null;
	
	private String feedBack = "";
	
	private boolean isFinish = false;
	
	private Handler mHandler = new Handler(new Handler.Callback()
	{
		@Override
		public boolean handleMessage(Message msg) 
		{
			switch (msg.what)
			{
				case 0:
					mProgressDialog.dismiss();
					if (flag)
					{
						ToastUtils.ShowMessage(getApplicationContext(), "提交成功!");
						Intent intent = new Intent(InspectionSubmitActivity.this,ActivityInspection.class);
						intent.putExtra("taskType", taskType);
//						intent.putExtra("taskState", taskState);	//指定跳转到ActivityIspection的指定界面
						startActivity(intent);
					}
					else
					{
						ToastUtils.ShowMessage(getApplicationContext(), "提交失败!");
					}

					break;
	
				default:
					break;
			}
			return false;
		}
	});
	
	//网络请求需要开启新的线程，不能在主线程中操作——————上传文件所需线程
	Runnable multiThread = new Runnable() 
	{
		@Override
		public void run() 
		{
			try 
			{
				// 上传图片及传递参数
				flag = MyUtils.multiUploadFile1(requestUrl, filesPath, false);
				mHandler.sendEmptyMessage(0);
			} 
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
	};
	private int taskType;
	private int taskState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_inpsection_form);
		
		taskId = getIntent().getStringExtra("TaskId");
		taskType = getIntent().getIntExtra("taskType", 0);
		taskState = getIntent().getIntExtra("taskState", 0);
		isFinish = getIntent().getBooleanExtra("isFinish", false);
		
		setCenterTitle("本次巡检单");
		hideRightTitle();
		hideLeftLogo();
		
		initViews();
	}
	private void initViews(){
		
		et_inspection_describe = (EditText) findViewById(R.id.et_inspection_describe);
		inspSubmitBtn = (Button) findViewById(R.id.inspection_button_submit);
		
		pModule = (PhotoModule) findViewById(R.id.photo_module);
		inspSubmitBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.inspection_button_submit:
			feedBack = et_inspection_describe.getText().toString().trim();
			
			if (TextUtils.isEmpty(feedBack))
			{
				ToastUtils.ShowMessage(getApplicationContext(), "巡检描述内容不能为空!");
				return;
			}

			//如果isFinish = true,表明任务全部巡检完毕,可以直接提交
			//否则弹出对话框提示
			if (isFinish) {
				submitTask();
			}else {
				showDialog();
			}
			break;

		default:
			break;
		}
		
		super.onClick(view);
	}
	
	private void showDialog() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(this);
		
		final Dialog dialog = new Dialog(this, R.style.CommonDialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);

		View view = inflater.inflate(R.layout.dialog_task_submit, null);
		
		final EditText dialog_description = (EditText) view.findViewById(R.id.dialog_description);
		
		view.findViewById(R.id.dialog_view_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						dialog.dismiss();
					}
				});
		view.findViewById(R.id.dialog_view_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						feedBack = feedBack + "," + dialog_description.getText().toString().trim();
						submitTask();
						dialog.dismiss();
					}
				});
		dialog.setContentView(view);
		dialog.show();
	}
	
	private void submitTask() {
		requestUrl = UserHttp.inspectionTaskSubmit(taskId, feedBack);
		new Thread(multiThread).start();
		mProgressDialog = new ProgressDialog(InspectionSubmitActivity.this);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setMessage("正在上报中，请稍后...");
		mProgressDialog.show();
	}
	
	@Override
	public void onBackLisenter()
	{
		super.onBackLisenter();
		finish();
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
				Toast.makeText(this, "取消图片选择", Toast.LENGTH_SHORT).show();
			}

			break;

			/**
			 * 本地返回的图片
			 */
		case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION:
			Uri uri = null;
			if (data == null) 
			{
				Toast.makeText(this, "取消选择图片", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(this, "取消图片选择", Toast.LENGTH_SHORT).show();
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
	
	
}
