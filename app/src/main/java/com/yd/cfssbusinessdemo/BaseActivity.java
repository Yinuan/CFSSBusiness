package com.yd.cfssbusinessdemo;


import com.yd.cfssbusinessdemo.util.SystemBarTintManager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		initData();
		initView();
		setOnclick();
	}
	protected abstract void initData();
		// TODO Auto-generated method stub
		
	
	protected abstract void initView();
		// TODO Auto-generated method stub
		
	protected abstract void setOnclick();
	
	//状态栏设置
	protected void initTint() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		// 自定义颜色
		tintManager.setTintColor(Color.parseColor("#f5821e"));
	}

	// 设置状态栏
	protected void setTranslucentStatus(boolean b) {
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
	protected void setTitleBar(){
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
}
