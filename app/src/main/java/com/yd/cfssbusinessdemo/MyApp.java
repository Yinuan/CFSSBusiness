package com.yd.cfssbusinessdemo;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.lzy.okhttputils.OkHttpUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;

public class MyApp extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static MyApp instance;

	public MyApp() {

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("-----------初始化APP");
		// 必须调用初始化
		OkHttpUtils.init(this);
		// 以下都不是必须的，根据需要自行选择
		OkHttpUtils.getInstance()//
				// .debug("OkHttpUtils") //是否打开调试
				.setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS) // 全局的连接超时时间
				.setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS) // 全局的读取超时时间
				.setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS); // 全局的写入超时时间
		// 创建通知栏

		// notifyShow();
		// imageLoad初始化
		// 创建默认的ImageLoader配置参数
		initConfig();

	}

	/**
	 * 配置ImageLoader基本属性,最好放在Application中(只能配置一次,如多次配置,则会默认第一次的配置参数)
	 */
	private void initConfig() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)// 设置线程优先级
				.threadPoolSize(4)// 线程池内加载的数量,推荐范围1-5内。
				.denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片缓存到内存中时只缓存一个。不设置的话默认会缓存多个不同大小的图片
				.memoryCacheExtraOptions(480, 800)// 内存缓存文件的最大长度
				.memoryCache(new LruMemoryCache(10 * 1024 * 1024))// 内存缓存方式,这里可以换成自己的内存缓存实现。(推荐LruMemoryCache,道理自己懂的)
				.memoryCacheSize(10 * 1024 * 1024)// 内存缓存的最大值
				.diskCache(new UnlimitedDiskCache(createSavePath()))// 可以自定义缓存路径
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())// 对保存的URL进行加密保存
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000))// 设置连接时间5s,超时时间30s
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 创建存储缓存的文件夹路径
	 *
	 * @return
	 */
	private File createSavePath() {
		return getFilesDir();
	}

	/**
	 * 配置图片加载时候的配置,在实际开发中可以对这些参数进行一次封装。
	 */
	public DisplayImageOptions setImageOptionsConfig() {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.touxiang_big)// 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.logo_denglu)// 设置图片Uri为null或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_shuju)// 设置图片加载/解码过程中错误时显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true)// 是否考虑JPEG图像的旋转,翻转
				.imageScaleType(ImageScaleType.NONE_SAFE)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置和复位
				.displayer(new SimpleBitmapDisplayer())// 不设置的时候是默认的
				// .displayer(new RoundedBitmapDisplayer(20))//是否为圆角,弧度是多少
				// displayer()还可以设置渐入动画
				.build();
		return options;
	}

	public static MyApp getInstance() {
		if (null == instance) {
			instance = new MyApp();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		// manager.cancel(1);
	}

}
