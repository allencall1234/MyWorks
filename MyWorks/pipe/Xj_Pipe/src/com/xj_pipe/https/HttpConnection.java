package com.xj_pipe.https;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;

public class HttpConnection 
{
	/**
	 * 从网上获取内容
	 */
	public static void getStringFromUrl(final String url,final HttpDataCallBack callBack)
	{
		Thread thread = new Thread(new Runnable() 
		{  
			@Override
			public void run() 
			{
				try
				{
					HttpGet get = new HttpGet(url);
					System.out.println("http========"+url);
					HttpClient client = new DefaultHttpClient();

					//超时等待时间60000ms太长了,改为6000ms
					HttpConnectionParams.setConnectionTimeout(client.getParams(), 6000);
					HttpConnectionParams.setSoTimeout(client.getParams(), 6000);
					HttpResponse response = client.execute(get);
					//					response.setHeader("", "");
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
					try 
					{
						mHandler.obtainMessage(1,new HttpDataCallBackEntity("-404", callBack)).sendToTarget();
					} 
					catch (Exception e2)
					{
						e.printStackTrace();
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
	public static byte[] readStream(InputStream inputStream) throws Exception 
	{  
		ByteArrayOutputStream bout = new ByteArrayOutputStream();  
		byte[] buffer = new byte[1024];  
		int len = 0;  
		while ((len = inputStream.read(buffer)) != -1) 
		{  
			bout.write(buffer, 0, len);  
		}  
		bout.close();  
		inputStream.close();  

		return bout.toByteArray();  
	} 

	public static InputStream getInputStream(String Url)
	{
		InputStream is = null;
		try 
		{
			URL url = new URL(Url);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setReadTimeout(5000);
			is = connection.getInputStream();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return is;
	}

	private static Handler mHandler = new Handler() 
	{  
		@Override  
		public void handleMessage(final Message msg)
		{  
			switch(msg.what)
			{  
			case 0:  
				try 
				{
					HttpDataCallBackEntity entity=(HttpDataCallBackEntity)msg.obj;
					entity.HttpSuccessCallBack();
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
				break;
			case 1:
				try 
				{
					HttpDataCallBackEntity entity=(HttpDataCallBackEntity)msg.obj;
					entity.HttpFailCallBack();
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				break;
			}	
		}			
	};
	
	/**  
	 * 向指定URL发送POST方法的请求  
	 *   
	 * @param url  
	 *            发送请求的URL  
	 * @param params  
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。  
	 * @return URL所代表远程资源的响应  
	 */  
	public static void sendPost(final String url, final String params , final HttpDataCallBack callBack)  
	{  
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				PrintWriter out = null;  
				BufferedReader in = null;  
				String result = "";  
				try  
				{  
					URL realUrl = new URL(url);  
					// 打开和URL之间的连接  
					URLConnection conn = realUrl.openConnection();  
					// 设置通用的请求属性  
					conn.setRequestProperty("accept", "*/*");  
					conn.setRequestProperty("connection", "Keep-Alive");  
					conn.setRequestProperty("user-agent",  
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");  
					// 发送POST请求必须设置如下两行  
					conn.setDoOutput(true);  
					conn.setDoInput(true);  
					// 获取URLConnection对象对应的输出流  
					out = new PrintWriter(conn.getOutputStream());  
					// 发送请求参数  
					out.print(params);  
					// flush输出流的缓冲  
					out.flush();  
					// 定义BufferedReader输入流来读取URL的响应  
					in = new BufferedReader(  
							new InputStreamReader(conn.getInputStream()));  
					String line;  
					while ((line = in.readLine()) != null)  
					{  
						result += "\n" + line;  
						mHandler.obtainMessage(0,new HttpDataCallBackEntity(result, callBack)).sendToTarget();
					}  
				}  
				catch (Exception e)  
				{  
					mHandler.obtainMessage(1,new HttpDataCallBackEntity("-404", callBack)).sendToTarget();
					e.printStackTrace();  
				}  
				// 使用finally块来关闭输出流、输入流  
				finally  
				{  
					try  
					{  
						if (out != null)  
						{  
							out.close();  
						}  
						if (in != null)  
						{  
							in.close();  
						}  
					}  
					catch (IOException ex)  
					{  
						ex.printStackTrace();  
					}  
				}  
			}
		});
		thread.start();
	}  
}
/*回调数据类*/
final class HttpDataCallBackEntity
{
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
