package com.xj.cnooc.https;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;

/**
 * 网络
 * @author qinfan
 *
 * 2015-9-11
 */
public class HttpUntilImpl {
	// 检测网络连接
	//	public static boolean checkConnection(Context context) {
	//		ConnectivityManager connectivityManager = (ConnectivityManager) context
	//				.getSystemService(Context.CONNECTIVITY_SERVICE);
	//		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
	//		if (networkInfo != null) {
	//			return networkInfo.isAvailable();
	//		}
	//		return false;
	//	}
	
	// public static boolean isWifi(Context mContext) {
	//		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
	//				.getSystemService(Context.CONNECTIVITY_SERVICE);
	//		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
	//		if (activeNetInfo != null && activeNetInfo.getTypeName().equals("WIFI")) {
	//			return true;
	//		}
	//		return false;
	//	}

	/**
	 * 从网上获取内容
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static void getStringFromUrl(final String url,final HttpDataCallBack callBack){
		Thread thread = new Thread(new Runnable() {  
			@Override
			public void run() {
				try
				{
					HttpGet get = new HttpGet(url);
					System.out.println("http========"+url);
					HttpClient client = new DefaultHttpClient();
					HttpConnectionParams.setConnectionTimeout(client.getParams(), 60000);
					HttpConnectionParams.setSoTimeout(client.getParams(), 60000);
					HttpResponse response = client.execute(get);
					int status=response.getStatusLine().getStatusCode();
					if(status==200)
					{
						HttpEntity entity = response.getEntity();
						String result=EntityUtils.toString(entity, "UTF-8");
						mHandler.obtainMessage(0,new HttpDataCallBackEntity(result, callBack)).sendToTarget();
						return;
					}
					mHandler.obtainMessage(1,new HttpDataCallBackEntity(String.valueOf(status), callBack)).sendToTarget();
				}
				catch(Exception e)
				{
					try {
						mHandler.obtainMessage(1,new HttpDataCallBackEntity("-404", callBack)).sendToTarget();
					} 
					catch (Exception e2) {

					}
					e.printStackTrace();

				}
			}
		}); 
		thread.start();
	}

	/** 
	 * 把输入流转换成字符数组 
	 * @param inputStream   输入流 
	 * @return  字符数组 
	 * @throws Exception 
	 */  
	public static byte[] readStream(InputStream inputStream) throws Exception {  
		ByteArrayOutputStream bout = new ByteArrayOutputStream();  
		byte[] buffer = new byte[1024];  
		int len = 0;  
		while ((len = inputStream.read(buffer)) != -1) {  
			bout.write(buffer, 0, len);  
		}  
		bout.close();  
		inputStream.close();  

		return bout.toByteArray();  
	} 

	public static InputStream getInputStream(String Url){
		InputStream is = null;
		try {
			URL url = new URL(Url);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setReadTimeout(5000);
			is = connection.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}

	private static Handler mHandler = new Handler() {  
		@Override  
		public void handleMessage(final Message msg) {  
			// TODO Auto-generated method stub 
			//Looper.prepare();
			switch(msg.what){  
			case 0:  
				try 
				{
					HttpDataCallBackEntity entity=(HttpDataCallBackEntity)msg.obj;
					entity.HttpSuccessCallBack();
				} 
				catch (Exception e) {

				}
				break;
			case 1:
				try {
					HttpDataCallBackEntity entity=(HttpDataCallBackEntity)msg.obj;
					entity.HttpFailCallBack();
				} catch (Exception e)  {
					// TODO: handle exception
				}
				break;
			}	
			//Looper.loop();
		}			
	};
}
/*回调数据类*/
final class HttpDataCallBackEntity{
	
	String HttpResult;
	
	HttpDataCallBack CallBack;
	
	public HttpDataCallBackEntity(String _HttpResult,HttpDataCallBack _CallBack)
	{
		this.HttpResult=_HttpResult;
		this.CallBack=_CallBack;
	}
	public void HttpSuccessCallBack()
	{
		if(CallBack!=null)
		{
			CallBack.HttpSuccess(HttpResult);
		}
	}
	public void HttpFailCallBack()
	{
		if(CallBack!=null)
		{
			CallBack.HttpFail(Integer.valueOf(HttpResult));
		}
	}

}