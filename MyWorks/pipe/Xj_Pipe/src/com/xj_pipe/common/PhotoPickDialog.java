package com.xj_pipe.common;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.xj_pipe.view.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 照片选取对话框
 */
public class PhotoPickDialog {
	/** 存放拍照上传的照片路径 */
	public static String mUploadPhotoPath;
	public static Dialog createDialog(final Activity activity) {
		final Dialog dialog = new Dialog(activity,R.style.CommonDialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		
		View view = LayoutInflater.from(activity).inflate(R.layout.dialog_photo_picker, null);
		
		view.findViewById(R.id.dialog_pickfrom_camera).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				Intent intent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				File dir = new CropPhotoUtil(activity)
						.getCachePath();
				Log.e("拍照图片路径", "" + dir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				
				mUploadPhotoPath = new CropPhotoUtil(
						activity).getCachePath()
						+ "/"
						+ UUID.randomUUID().toString()
						+ ".png";
				File file = new File(mUploadPhotoPath);
				System.out.println(mUploadPhotoPath);
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {

					}
				}
				
				
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(file));

				activity.startActivityForResult(
						intent,
						ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CAMERA);
			}
		});
		
		view.findViewById(R.id.dialog_pickfrom_gallery).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_PICK,
						null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						"image/*");

				activity.startActivityForResult(
						intent,
						ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION);
			}
		});
		
		view.findViewById(R.id.dialog_view_cancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		dialog.setContentView(view);
		
		return dialog;
		
	}

}
