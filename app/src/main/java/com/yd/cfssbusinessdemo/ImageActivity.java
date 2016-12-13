package com.yd.cfssbusinessdemo;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yd.cfssbusinessdemo.util.SharedPreferencesUnit;
import com.yd.cfssbusinessdemo.util.SystemBarTintManager;
import com.yd.cfssbusinessdemo.weight.TouchImageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class ImageActivity extends Activity {

	private TouchImageView image;// 头像

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image);
		// 4.4及以上版本开启
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		// 自定义颜色
		tintManager.setTintColor(Color.parseColor("#f5821e"));

		image = (TouchImageView) findViewById(R.id.iv_me_touxiang);

		findViewById(R.id.imageView_imageBig_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		// 下载头像
		showtouXiang();

	}

	private void showtouXiang() {
		SharedPreferencesUnit unit = SharedPreferencesUnit.instance(this);
		String imageUrl = unit.reStoreData("headportraiturl");
		ImageLoader.getInstance().displayImage(imageUrl, image, MyApp.getInstance().setImageOptionsConfig());
	}

	// 设置状态栏
	private void setTranslucentStatus(boolean b) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (b) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);

	}

}
