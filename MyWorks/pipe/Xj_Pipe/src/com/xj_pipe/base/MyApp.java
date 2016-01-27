package com.xj_pipe.base;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.xj_pipe.bean.UserBean;
import com.xj_pipe.logcat.CrashHandler;
import com.xj_pipe.view.InspectionService;
import com.xj_pipe.view.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class MyApp extends Application {

	public static MyApp instance;
	public static LinkedList<Activity> activities = new LinkedList<Activity>();;

	/** 存放用户信息 */
	public static UserBean userInfo;
	public static int TYPE = -1;
	/** 本地未读应急消息总数量*/
	public static int localUnDealMsg = 0;
	
	/** 是否弹出对话框或者notification标记*/
	public static boolean isShow = false;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//初始化日志
		CrashHandler handler = CrashHandler.getInstance();
		handler.init(this);

		instance = this;

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				MyApp.this)
				// max width, max height，即保存的每个缓存文件的最大宽高
				// .memoryCacheExtraOptions(500,500)
				// 线程池内加载的数量
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				// 将保存的时候的URI名称用MD5 加密
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.discCacheFileCount(100)
				// 自定义缓存路径
				.discCache(
						new UnlimitedDiscCache(new File(Environment
								.getExternalStorageDirectory() + "")))
				.denyCacheImageMultipleSizesInMemory()
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// .defaultDisplayImageOptions(getDisplayOptions())
				.imageDownloader(
						new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
				// .writeDebugLogs()
				.build(); // 开始构建

		ImageLoader.getInstance().init(config);
		if (!isServiceWork(this, "com.xj_pipe.view.InspectionService")) {
			//启动应急任务监听服务
			startService(new Intent(this, InspectionService.class));
		}else {
			Log.i("zlt", "com.xj_pipe.view.InspectionService is live!");
		}
		
	}
	
	public static MyApp getInstance() {
		return instance;
	}

	/**
	 * 退出应用程序
	 */
	public static void exitApplication() {
		//关闭应急任务监听服务
		getInstance().stopService(new Intent(getInstance(),InspectionService.class));
		
		for (Activity activity : activities) {
			if (activity != null) {
				activity.finish();
			}
		}
	}

	public static DisplayImageOptions getDisplayOptions() {
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)// 设置图片Uri为空或是错误的时候显示的图片
				// .showImageOnFail(R.drawable.ic_launcher) //
				// 设置图片加载/解码过程中错误时候显示的图片
				// .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				// .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//
				// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				// .delayBeforeLoading(int delayInMillis)//int
				// delayInMillis为你设置的下载前的延迟时间
				// 设置图片加入缓存前，对bitmap进行设置
				// .preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
				.build();// 构建完成
		return options;
	}

	/** 
	 * 判断某个服务是否正在运行的方法 
	 *  
	 * @param mContext 
	 * @param serviceName 
	 *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService） 
	 * @return true代表正在运行，false代表服务没有正在运行 
	 */  
	public boolean isServiceWork(Context mContext, String serviceName) {  
	    boolean isWork = false;  
	    ActivityManager myAM = (ActivityManager) mContext  
	            .getSystemService(Context.ACTIVITY_SERVICE);  
	    List<RunningServiceInfo> myList = myAM.getRunningServices(40);  
	    if (myList.size() <= 0) {  
	        return false;  
	    }  
	    for (int i = 0; i < myList.size(); i++) {  
	        String mName = myList.get(i).service.getClassName().toString();  
	        if (mName.equals(serviceName)) {  
	            isWork = true;  
	            break;  
	        }  
	    }  
	    return isWork;  
	}  
}
