package com.yd.cfssbusinessdemo;


import com.yd.cfssbusinessdemo.util.SharedPreferencesID;
import com.yd.cfssbusinessdemo.util.SystemBarTintManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MineMarkActivity extends Activity {

	private TextView tv_yuyue, tv_chufa, tv_daoda, tv_zhixing, tv_ordercode;
	private SharedPreferencesID shared;
	private String yuyue_tv;// 预约的备注
	private String ordercode;// 订单号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mine_mark);
		// 4.4及以上版本开启
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		// 自定义颜色
		tintManager.setTintColor(Color.parseColor("#f5821e"));
		initView();
		Intent intent = getIntent();
		yuyue_tv = intent.getStringExtra("beizhu_yuyue");
		ordercode = intent.getStringExtra("ordercode");
		initData();
		findViewById(R.id.imageView_mine_beizhu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		setOnclick();// 点击事件
	}

	private void setOnclick() {
		// 预约备注
		tv_yuyue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MineMarkActivity.this).setMessage(tv_yuyue
						.getText());
				builder.create().show();

			}
		});
		// 出发备注
		tv_chufa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MineMarkActivity.this).setMessage(tv_chufa
						.getText());
				builder.create().show();

			}
		});
		// 到达备注
		tv_daoda.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MineMarkActivity.this).setMessage(tv_daoda
						.getText());
				builder.create().show();

			}
		});
		// 执行备注
		tv_zhixing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MineMarkActivity.this).setMessage(tv_zhixing
						.getText());
				builder.create().show();

			}
		});

	}

	private void initData() {
		shared = new SharedPreferencesID(this, ordercode);
		tv_ordercode.setText(ordercode);
		tv_yuyue.setText(yuyue_tv);
		tv_chufa.setText(shared.reStoreData("出发备注"));
		tv_daoda.setText(shared.reStoreData("到达备注"));
		tv_zhixing.setText(shared.reStoreData("执行备注"));

	}

	private void initView() {
		tv_yuyue = (TextView) findViewById(R.id.beizhu_yuyue);
		tv_chufa = (TextView) findViewById(R.id.beizhu_chufa);
		tv_daoda = (TextView) findViewById(R.id.beizhu_daoda);
		tv_zhixing = (TextView) findViewById(R.id.beizhu_zhixing);
		tv_ordercode = (TextView) findViewById(R.id.tv_ordercode);
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
