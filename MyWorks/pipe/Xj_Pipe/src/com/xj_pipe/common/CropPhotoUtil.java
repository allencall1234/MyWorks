package com.xj_pipe.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

/**
 * 
 * 裁剪图片帮助�?
 * 
 */
public class CropPhotoUtil {
	private Activity mActivity;
	private File cacheDir;

	public CropPhotoUtil(Activity activity) {
		mActivity = activity;
	}

	public File getCachePath() {
		if (sdCardIsAvailable()) { // sdcard not available
			cacheDir = new File(Environment.getExternalStorageDirectory()
					+ "/DCIM/Camera/");
		}
		return cacheDir;
	}

	/**
	 * 检测sdcard是否可用
	 * 
	 * @return true为可用，否则为不可用
	 */
	public static boolean sdCardIsAvailable() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}

	/**
	 * 系统裁剪照片
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("scale", true);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		mActivity.startActivityForResult(intent, 9);

	}

	/**
	 * 裁剪后的图片路径
	 * 
	 * @return
	 */
	public String getCropPath(Intent data) {
		Bundle extras = data.getExtras();
		Bitmap myBitmap = (Bitmap) extras.get("data");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(getmCurrentPhotoFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		myBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
		return getmCurrentPhotoFile().toString();
	}

	/**
	 * 得到本地图片路径
	 * 
	 * @return
	 */
	private File mCurrentPhotoFile;// 照相机裁剪之后得到的图片

	public File getmCurrentPhotoFile() {
		if (mCurrentPhotoFile == null) {
			if (!getCachePath().exists())
				getCachePath().mkdirs();// 创建照片的存储目�?
			mCurrentPhotoFile = new File(getCachePath(), UUID.randomUUID()
					.toString() + ".png");
			if (!mCurrentPhotoFile.exists())
				try {
					mCurrentPhotoFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		return mCurrentPhotoFile;
	}

	/**
	 * 获取裁剪之后的图片File路径
	 * 
	 * @param data
	 * @return
	 */
	public File getPhotoPath(Intent data) {
		File file = new File(getCropPath(data));
		return file;
	}

	/**
	 * 保存裁剪的照�?
	 * 
	 * @param data
	 */
	private Bitmap bitmap;

	public Bitmap saveCropPhoto(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			// Log.e("datePicPath=", ""+file);
			bitmap = extras.getParcelable("data");
		} else {
			Toast.makeText(mActivity, "获取裁剪照片错误", Toast.LENGTH_SHORT).show();
		}
		return bitmap;
	}

}
