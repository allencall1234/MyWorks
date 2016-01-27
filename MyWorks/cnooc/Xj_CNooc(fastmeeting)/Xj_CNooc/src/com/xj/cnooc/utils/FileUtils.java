package com.xj.cnooc.utils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xj.cnooc.https.HttpURL;

public class FileUtils {
	private ProgressBar downProgress;
	private Context context;
	public String localDownFolder;// 要下载到本地的哪个文件夹中
	private String localFileFolder = "downloadFile";
	private long completeSize = 0;// 已下载的文件大小
	private long totalDownSize = 0;// 保存要下载的文件的总大小
	private ImageButton ib_download;

	private int flag = -2;
	private File file = null;

	/** 下载过程中的各状态 */
	private static final int STATE = 0;// 0 : 正在下载 ； 1：暂停 ； 2 ：下载完成

	public FileUtils(ProgressBar downProgress, Context context,
			ImageButton ib_download) {
		// 此构造
		// 方法，将界面中绑定的progressbar控件作为参数传递到此类中，那么在这个类中对这个引用的progressbar类型所作的操作相当于直接在操作绑定的界面上的那个控件
		this.downProgress = downProgress;
		this.context = context;
		this.ib_download = ib_download;
		// 判断SD卡是否存在
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			localDownFolder = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/" + localFileFolder;
			createFolder();
		}
	}

	// public FileUtils()
	// {
	// //判断SD卡是否存在
	// boolean sdCardExist = Environment.getExternalStorageState()
	// .equals(android.os.Environment.MEDIA_MOUNTED);
	// if (sdCardExist)
	// {
	// localDownFolder =
	// Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+localFileFolder;
	// createFolder();
	// }
	// }

	/**
	 * 判断父级目录 是否存在，如果不存在，则在SD卡中创建这个文件夹，存在，则不进行任何操作
	 * 
	 * @describe : TODO
	 */
	public void createFolder() {
		File file = new File(localDownFolder);
		if (!file.exists()) {
			file.mkdirs();// 如果父级目录不存在，则先创建父级目录
		}
	}

	/*
	 * 1、控制bar的进度，需要有几个参数： 1、待下载的文件的总大小 2、当前下载的进度 2、进行下载时的操作，需要用到工具类：
	 * RandomAccessFile(随机分段下载文件）
	 * 
	 * 3、连接网络路径下的资源 工具类： HttpURLConnection 4、更新控件bar的进度，使用handler线程机制
	 */

	// /**
	// * 获取到当前要下载的文件资源的总大小
	// * @describe : TODO
	// */
	// public long getDownFileSize(String fileUrl)
	// {
	// long fileSize = -1;
	// URL url;
	// try
	// {
	// url = new URL(HttpURL.SERVICE_URL + fileUrl);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// fileSize = conn.getContentLength();
	// conn.disconnect();
	// }
	// catch (MalformedURLException e)
	// {
	// e.printStackTrace();
	// }
	// catch (IOException e)
	// {
	// e.printStackTrace();
	// }
	// return fileSize;
	// }

	/**
	 * 将文件的整个下载操作放在一个线程中来执行
	 * 
	 * @describe : TODO
	 */
	public void asyncDownloadFile(final String fileUrl, final String fileName,
			final String type) {
		new Thread() {
			@Override
			public void run() {
				downloadFile(fileUrl, fileName, type);
			};
		}.start();
	}

	// 开始下载指定资源文件
	public void downloadFile(String urlStr, String fileName, String type) {
		// 文件存在
		if (isFileExist(localDownFolder + "/" + fileName)) {
			flag = 1;
			// Toast.makeText(context, fileName + "已存在",
			// Toast.LENGTH_SHORT).show();
		} else {
			try {
				String str_url = URLEncoder.encode(urlStr, "UTF-8");
				String str_fileName = URLEncoder.encode(fileName, "UTF-8");
				URL url = new URL(HttpURL.SERVICE_URL
						+ "phoneDownload.html?downLoadPath=" + str_url
						+ "&fileName=" + str_fileName + "&type=" + type);
				System.out.println("============" + url);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				InputStream is = conn.getInputStream();
				// //设置请求方式为"GET"
				// conn.setRequestMethod("GET");
				// //超时响应时间为5秒
				// conn.setConnectTimeout(5 * 1000);
				// 通过输入流获取图片数据
				totalDownSize = conn.getContentLength();
				// conn.disconnect();

				// String[] fileUrl_array = (HttpURL.SERVICE_URL +
				// urlStr).split("\\/");
				// String file_name = fileUrl_array[fileUrl_array.length -
				// 1].split("\\?")[0];
				// File file = null;
				if (is != null && totalDownSize > 0) {
					file = new File(localDownFolder + "/" + fileName);
					file.createNewFile();
					RandomAccessFile raf = new RandomAccessFile(file, "rwd");
					int length = 0;
					byte[] b = new byte[1024];
					while ((length = is.read(b)) != -1) {
						raf.write(b, 0, length);
						completeSize += length;
						handler.sendEmptyMessage(2);
					}
					raf.close();

					flag = 0;
				} else {
					flag = -1;
				}

			} catch (Exception e) {
				e.printStackTrace();
				flag = -1;
			}

		}
		handler.sendEmptyMessage(flag);
		// Toast.makeText(context, fileName + "下载成功",
		// Toast.LENGTH_SHORT).show();
	}

	// /**
	// * 将一个InputStream里面的数据写入到SD卡中
	// */
	// public File writeSDFromInput(String fileName, InputStream input)
	// {
	// File file = null;
	// OutputStream output = null;
	// try
	// {
	// file = creatSDFile(localDownFolder + "/" + fileName); // 创建文件
	// output = new FileOutputStream(file);
	// byte buffer [] = new byte[1024];
	// int length = 0;
	// while((length = input.read(buffer)) != -1)
	// {
	// completeSize += length;
	// //将已下载的文件长度保存到变量中
	// completeSize += length;
	// progressHandler.sendEmptyMessage(0);//下载的过程中不断将下载的最新进度情况发送给handler来更新进度条的进度
	// output.write(buffer);
	// }
	// output.flush();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// finally
	// {
	// try
	// {
	// output.close();
	// input.close();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	// return file;
	// }

	// 由于得到一个InputStream对象是所有文件处理前必须的操作，所以将这个操作封装成了一个方法
	public InputStream getInputStream(String urlStr) {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			urlConn.setConnectTimeout(5 * 1000);
			urlConn.setRequestMethod("GET");
			if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				totalDownSize = urlConn.getContentLength();// 将要下载的文件的总大小保存到全局变量
															// 中
				return urlConn.getInputStream();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// /**
	// * Get data from stream
	// *
	// * @param inStream
	// * @return byte[]
	// * @throws Exception
	// */
	// public static byte[] readStream(InputStream inStream) throws Exception
	// {
	// ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	// byte[] buffer = new byte[1024];
	// int len = 0;
	// while ((len = inStream.read(buffer)) != -1)
	// {
	// outStream.write(buffer, 0, len);
	// }
	// outStream.close();
	// inStream.close();
	// return outStream.toByteArray();
	// }
	//
	// /**
	// * Get image from newwork
	// *
	// * @param path
	// * The path of image
	// * @return byte[]
	// * @throws Exception
	// */
	// public byte[] getImage(String path) throws Exception
	// {
	// URL url = new URL(path);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setConnectTimeout(5 * 1000);
	// conn.setRequestMethod("GET");
	// InputStream inStream = conn.getInputStream();
	// if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
	// {
	// return readStream(inStream);
	// }
	// return null;
	// }

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	// /**
	// * 在SD卡上创建文件
	// */
	// public File creatSDFile(String fileName)
	// {
	//
	// File file = new File(fileName);
	// try
	// {
	// file.createNewFile();
	// }
	// catch (IOException e)
	// {
	// e.printStackTrace();
	// }
	// return file;
	// }

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:
				downProgress.setProgress((int) (completeSize
						* downProgress.getMax() / totalDownSize));
				break;
			case 0:
				ib_download.setVisibility(View.GONE);
				Toast.makeText(context, "下载成功,请到downloadFile文件夹中查看下载文件!",
						Toast.LENGTH_SHORT).show();
				break;
			case -1:
				Toast.makeText(context, "下载失败,文件不存在或网络连接异常!",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				ib_download.setVisibility(View.GONE);
				downProgress.setProgress(downProgress.getMax());
				Toast.makeText(context, "文件已存在,请到downloadFile文件夹中查看下载文件!",
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

}
