package com.yd.cfssbusinessdemo;

import com.yd.cfssbusinessdemo.fragment.Fragment_mine;
import com.yd.cfssbusinessdemo.fragment.Fragment_task;
import com.yd.cfssbusinessdemo.service.MyReceiver;
import com.yd.cfssbusinessdemo.util.ServiceUnit;
import com.yd.cfssbusinessdemo.util.SystemBarTintManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TaskListActivity extends FragmentActivity implements OnClickListener {

	private RelativeLayout ll_btn_message;
	private LinearLayout ll_btn_message_2;
	private ImageView iv_order_message, iv_order_message_2;
	private TextView tv_order_message_2, tv_order_message;
	private FragmentManager fragmentManager;// 碎片管理器
	private int pagerPostion = 0; // 保存当前显示的是第几页
	private Fragment_task one; // 碎片1
	private Fragment_mine two; // 碎片2
	private ImageView iv_plane; // 打飞机
	private static Context context;
	private TextView tv_qipao;// 气泡数值
	private FrameLayout frameLayout_qipao;// 气泡帧布局
	public NotificationManager manager;
	private Intent intent;
	private Handler mhandler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			String tv_size =msg.getData().getString("data");
			System.out.println("----tv_size-----"+tv_size);
			if (tv_size.length()>=0) {				
				frameLayout_qipao.setVisibility(View.VISIBLE);
				if (tv_size.length()==1) {
					if (!tv_size.equals("0")) {
						tv_qipao.setText(tv_size);	
					}else {
						frameLayout_qipao.setVisibility(View.GONE);
					}			
				} else if(tv_size.length()==2){
					tv_qipao.setText(tv_size);
				}else if (tv_size.length()>=3) {
					tv_qipao.setText("...");
				}else if (tv_size.length()==0) {
					frameLayout_qipao.setVisibility(View.GONE);
				}
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_task_list);
		// 把活动装箱
		MyApp.getInstance().addActivity(this);
		// 4.4及以上版本开启
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		// 自定义颜色
		tintManager.setTintColor(Color.parseColor("#f5821e"));
		// 如果当前碎片页面不等于空就复用当前碎片页面
		if (null != savedInstanceState) {
			pagerPostion = savedInstanceState.getInt("mdb");
		}
		initView();
	/*	//开启服务
		intent =new Intent();
		intent.setAction("com.yd.cfssbusinessdemo.MyService");
		startService(intent);*/
		//动态注册广播
		regReceiver();
		//前台消息初始化
		//manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
		//notifyShow();
		// 开启定位服务系统
		if (!ServiceUnit.isServiceWork(this, "com.yd.cfssbusinessdemo.service.MyService")) {
			Intent intent = new Intent("com.yd.cfssbusinessdemo.service");
			intent.setPackage(this.getPackageName());
			this.startService(intent);
		}
	}
	private void regReceiver() {
		IntentFilter intentFilter =new IntentFilter("com.yd.cfssbusinessdemo.myReceiver");
		MyReceiver myReceiver =new MyReceiver(mhandler);
		registerReceiver(myReceiver, intentFilter);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
	/**
	 * 获取Activity的上下文
	 * 
	 * @return
	 */
	public static Context getContext() {
		return context;
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

	/**
	 * 重写onSaveInstanceState方法，保存状态
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("mdb", pagerPostion);
		super.onSaveInstanceState(outState);
		// Log.i("mdb", "onSaveInstanceState");
	}

	private void initView() {

		ll_btn_message = (RelativeLayout) findViewById(R.id.ll_btn_message);
		ll_btn_message_2 = (LinearLayout) findViewById(R.id.ll_btn_message_2);

		iv_order_message = (ImageView) findViewById(R.id.iv_order_message);
		iv_order_message_2 = (ImageView) findViewById(R.id.iv_order_message_2);

		tv_order_message_2 = (TextView) findViewById(R.id.tv_order_message_2);
		tv_order_message = (TextView) findViewById(R.id.tv_order_message);
		iv_plane = (ImageView) findViewById(R.id.imageView_point);

		context = this;
		tv_qipao = (TextView) findViewById(R.id.textView_list_size);
		frameLayout_qipao = (FrameLayout) findViewById(R.id.frameLayout_qipao);
		ll_btn_message.setOnClickListener(this);
		ll_btn_message_2.setOnClickListener(this);
		iv_plane.setOnClickListener(this);
		fragmentManager = getSupportFragmentManager();// 碎片管理器

		// 在FragmentManager里面根据Tag去找相应的fragment. 用户屏幕发生旋转，重新调用onCreate方法。否则会发生重叠
		one = (Fragment_task) fragmentManager.findFragmentByTag("fm_one");
		two = (Fragment_mine) fragmentManager.findFragmentByTag("fm_two");

		setTabSelection(pagerPostion);// 第一次启动时选中第0个tab

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_btn_message:

			getCheck(true, false);
			setTabSelection(0);
			pagerPostion = 0;
			break;

		case R.id.ll_btn_message_2:
			getCheck(false, true);
			setTabSelection(1);
			pagerPostion = 1;
			break;
		case R.id.imageView_point:
			Intent intent = new Intent(TaskListActivity.this, VolumeExecuteActivity.class);
			startActivity(intent);
			break;
		}

	}

	// 点击事件，图文替换
	private void getCheck(boolean f1, boolean f2) {
		iv_order_message.setSelected(f1);
		iv_order_message_2.setSelected(f2);

		tv_order_message.setSelected(f1);
		tv_order_message_2.setSelected(f2);
	}

	/**
	 * 构造方法描述:根据传入的index参数来设置选中的tab页
	 * 
	 * @param index
	 *            每个tab页对应的下标。0表示首页，1表示活动，2表示购物车，3表示我的 返 回 类 型:void
	 */
	private void setTabSelection(int index) {
		getCheck(false, false);// 每次选中之前先清楚掉上次的选中状态

		FragmentTransaction transaction = fragmentManager.beginTransaction();// 开启一个Fragment事务

		hideFragments(transaction);// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况

		switch (index) {
		case 0:
			getCheck(true, false);// 当点击了首页tab时，改变控件的图片和文字颜色

			if (one == null) {
				one = new Fragment_task();// 如果首页为空，则创建一个并添加到界面上
				transaction.add(R.id.fl_fragment, one, "fm_one");
			} else {
				transaction.show(one);// 如果首页不为空，则直接将它显示出来
			}
			break;

		case 1:
			getCheck(false, true);// 当点击了活动tab时，改变控件的图片和文字颜色

			if (two == null) {
				two = new Fragment_mine();// 如果活动页面为空，则创建一个并添加到界面上
				transaction.add(R.id.fl_fragment, two, "fm_two");
			} else {
				transaction.show(two);// 如果活动页面不为空，则直接将它显示出来
			}
			break;

		}
		transaction.commit();// 提交事务
	}

	/**
	 * 构造方法描述:将所有的Fragment都置为隐藏状态
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务 返 回 类 型:void
	 */
	private void hideFragments(FragmentTransaction transaction) {

		if (one != null) {
			transaction.hide(one);
		}
		if (two != null) {
			transaction.hide(two);
		}

	}

	// 通知栏
	private void notifyShow() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
		//图片
		Bitmap bitmap =BitmapFactory.decodeResource(TaskListActivity.this.getContext().getResources(), R.drawable.logo_h);
		// 必需的通知内容
		builder.setContentTitle("content title")
		.setContentText("content describe")
		
		//.setLargeIcon(bitmap);
		
		.setSmallIcon(R.drawable.person_1);
		

		
		/* Intent notifyIntent = new Intent(this, AtyService.class); 
		 PendingIntent notifyPendingIntent =PendingIntent.getActivity(this, 0, notifyIntent,
		 PendingIntent.FLAG_UPDATE_CURRENT);
		  builder.setContentIntent(notifyPendingIntent);*/
		 

		Notification notification = builder.build();
		
		manager.notify(1, notification);

	}

	/**
	 * 重写系统返回键
	 */
	long mExitTime = 0;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("------hello--2----");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Object mHelperUtils;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				//manager.cancel(1);
				MyApp.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
