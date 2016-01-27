package com.xj_pipe.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.esri.android.map.MapView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class MyUtils {
	private static final Calendar rightNow = Calendar.getInstance();

	public static int getmYear() {
		return  rightNow.get(Calendar.YEAR);
	}

	public static int getmMonth() {
		return  rightNow.get(Calendar.MONTH);
	}

	public static int getmDay() {
		return rightNow.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 时间格式转换
	 */
	public static String getTime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");       
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
		String send_time = sdf.format(curDate); 
		
		return send_time;
	}
	
	/**
	 * 检查当前网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Activity activity)
	{
		Context context = activity.getApplicationContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) 
		{
			return false;
		} 
		else 
		{
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0)
			{
				for (int i = 0; i < networkInfo.length; i++) 
				{
					System.out.println(i + "===状态==="
							+ networkInfo[i].getState());
					System.out.println(i + "===类型==="
							+ networkInfo[i].getTypeName());
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) 
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断手机GPS是否打开
	 */
	public static boolean isGPSOpen(Activity activity)
	{
		LocationManager alm = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
		
		if( alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 上传图片的方法
	 * @throws UnsupportedEncodingException
	 */
	public static boolean multiUploadFile1 (String url, List<String> filesPath, boolean flag) throws UnsupportedEncodingException 
	{
		//HttpClient对象
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 6000);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 60000);
		//采用POST的请求方式
		//这是上传服务地址http://10.0.2.2:8080/WebAppProject/main.do?method=upload2
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
//		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
//		HttpPost httpPost = new HttpPost("http://192.168.1.24:8080/WebAppProject/main.do?method=upload");
//		HttpPost httpPost = new HttpPost("http://192.168.1.52:8081/pis/taskForMobil/uploadPicture1.action?title=发生区域&id=1");
//		HttpPost httpPost = new HttpPost("http://192.168.1.52:8081/pis/appAddEventOrder.action?");
		
	    MultipartEntity multipartEntity = new MultipartEntity();
		
		//StringBody对象，参数--------->参加后请求地址有多个参数请求失败
		StringBody param = new StringBody("参数内容");
		multipartEntity.addPart("param1", param);
	    
		//filesPath为List<String>对象，里面存放的是需要上传的文件的地址
	    
	    if (filesPath.size() > 0) 
	    {
	    	for (String path: filesPath) 
	    	{
	    		ContentBody fileBody = new FileBody( new File(path));
	    		multipartEntity.addPart("file", fileBody);
	    		
//			file.delete(); 
	    	}
		}
		
		
		//将MultipartEntity对象赋值给HttpPost
		httpPost.setEntity(multipartEntity);
		HttpResponse response = null;
		
		try
		{
			//执行请求，并返回结果HttpResponse
			response = httpClient.execute(httpPost);
		} 
		catch (ClientProtocolException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		//上传成功后返回
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
		{
			try
			{
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity, "UTF-8");
				try
				{
					JSONObject object = new JSONObject(result);
					if (flag)
					{
						if (object.getBoolean("status") == true)
						{
							filesPath.clear();;
							return true;
						}
					}
					else
					{
						if (object.getString("result").equals("0")) 
						{
							filesPath.clear();;
							return true;
						}
					}
				
				} 
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
		}
		else
		{
			System.out.println("-->failure");
			filesPath.clear();
		}
		
		return false;

	}
	
	@SuppressLint("NewApi")
	public static String getPathByUri(Context cxt,Uri uri)
	{
		//判断手机系统是否是4.4或以上的sdk
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		
		//如果是4.4以上的系统并且选择的文件是4.4专有的最近的文件
		if (isKitKat && DocumentsContract.isDocumentUri(cxt, uri)) {
			// 如果是从外部储存卡选择的文件
			if (isExternalStorageDocument(uri)) 
			{
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) 
				{
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

			}
			
			//如果是下载返回的路径
			else if (isDownloadsDocument(uri))
			{
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(cxt, contentUri, null, null);
			}
			
			//如果是选择的媒体的文件
			else if (isMediaDocument(uri)) 
			{
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) 
				{  //图片
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				}
				else if ("video".equals(type)) 
				{  //视频
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} 
				else if ("audio".equals(type)) 
				{  //音频
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(cxt, contentUri, selection,
						selectionArgs);
			}
		}
		else if ("content".equalsIgnoreCase(uri.getScheme()))
		{   //如果是低端4.2以下的手机文件uri格式
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(cxt, uri, null, null);
		}
		else if ("file".equalsIgnoreCase(uri.getScheme()))
		{   //如果是通过file转成的uri的格式
			return uri.getPath();
		}

		return null;
	}
	
	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
			String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}
	
	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}
	
	/**
	 * 设置地图背景
	 */
	public static void setMapViewBackground(MapView mapView)
	{
		mapView.setMapBackground(0x7ab6f5, 0xffffff, 0, 0);
	}

}
