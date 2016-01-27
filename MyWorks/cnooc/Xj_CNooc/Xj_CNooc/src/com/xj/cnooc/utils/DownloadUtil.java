package com.xj.cnooc.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.xj.cnooc.https.HttpURL;

import android.os.Environment;
import android.os.Handler;
import android.widget.ProgressBar;

public class DownloadUtil 
{
	private ProgressBar downProgress;
	private String localDownFolder;//要下载到本地的哪个文件夹中
	private String localFileFolder = "downloadFile";
	private long completeSize = 0;//已下载的文件大小
	private long totalDownSize = 0;//保存要下载的文件的总大小
	
	/** 下载过程中的各状态 */
	private static final int STATE = 0;//0 : 正在下载 ； 1：暂停 ； 2 ：下载完成
	
	public DownloadUtil(ProgressBar downProgress)
	{
		//此构造 方法，将界面中绑定的progressbar控件作为参数传递到此类中，那么在这个类中对这个引用的progressbar类型所作的操作相当于直接在操作绑定的界面上的那个控件
		this.downProgress = downProgress;
		localDownFolder = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+localFileFolder;
		createFolder();
	}
	
	/**
	 * 判断父级目录 是否存在，如果不存在，则在SD卡中创建这个文件夹，存在，则不进行任何操作
	 * @describe : TODO
	 */
	public void createFolder()
	{
		File file = new File(localDownFolder);
		if(!file.exists())
		{
			file.mkdirs();//如果父级目录不存在，则先创建父级目录
		}
	}
	
	/*
	 * 1、控制bar的进度，需要有几个参数：
	 * 				1、待下载的文件的总大小		2、当前下载的进度
	 * 2、进行下载时的操作，需要用到工具类： RandomAccessFile(随机分段下载文件）
	 * 
	 * 3、连接网络路径下的资源
	 * 		工具类：  HttpURLConnection
	 * 4、更新控件bar的进度，使用handler线程机制
	 * 
	 * 
	 * 
	 */
	
	/**
	 * 获取到当前要下载的文件资源的总大小
	 * @describe : TODO
	 */
	public long getDownFileSize(String fileUrl){
		long fileSize = -1;
		URL url;
		try 
		{
			url = new URL(fileUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			fileSize = conn.getContentLength();
			conn.disconnect();
		} 
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return fileSize;
	}
	/**
	 * 将文件的整个下载操作放在一个线程中来执行
	 * @describe : TODO
	 */
	public void asyncDownloadFile(final String fileUrl, final String fileName)
	{
		new Thread(){
			@Override
			public void run()
			{
				downloadFile(fileUrl, fileName);
			};
		}.start();
	}
	
	/**
	 * 开始下载指定资源的文件
	 * @describe : TODO
	 */
	private void downloadFile(String fileUrl, String fileName)
	{
		try {
			URL url = new URL(HttpURL.SERVICE_URL + fileUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			totalDownSize = conn.getContentLength();//将要下载的文件的总大小保存到全局变量 中
			
			InputStream is = conn.getInputStream();
			
			//开始边读取文件边进行文件的写入
			RandomAccessFile raf = new RandomAccessFile(new File(localDownFolder+"/" + fileName), "rwd");
			
			int length = 0 ;//读取流文件后的返回值
			byte[] b = new byte[1024];
			while((length = is.read(b))!= -1)
			{
				raf.write(b, 0, length);//将文件写入到指定的SD卡目录中
				//将已下载的文件长度保存到变量中
				completeSize += length;
				progressHandler.sendEmptyMessage(0);//下载的过程中不断将下载的最新进度情况发送给handler来更新进度条的进度
				
			}
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private Handler progressHandler = new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg) 
		{
			downProgress.setProgress((int)(completeSize * downProgress.getMax()
					/ totalDownSize));
		};
	};
}
