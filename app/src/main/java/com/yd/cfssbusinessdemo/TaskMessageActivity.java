package com.yd.cfssbusinessdemo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.yd.cfssbusinessdemo.config.Config;
import com.yd.cfssbusinessdemo.entity.GsLoginOut;
import com.yd.cfssbusinessdemo.entity.GsTaskInfo;
import com.yd.cfssbusinessdemo.service.MyService;
import com.yd.cfssbusinessdemo.sqlite.dp.OrdersDAO;
import com.yd.cfssbusinessdemo.util.AMapUtil;
import com.yd.cfssbusinessdemo.util.LogUtil;
import com.yd.cfssbusinessdemo.util.SharedPreferencesID;
import com.yd.cfssbusinessdemo.util.SharedPreferencesUnit;
import com.yd.cfssbusinessdemo.util.StringUnit;
import com.yd.cfssbusinessdemo.util.SystemBarTintManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class TaskMessageActivity extends Activity implements OnGeocodeSearchListener, OnRouteSearchListener {
	/********** DECLARES *************/
	private TextView textView_num_mess;// 订单号·
	private TextView textView_name_mess;// 客户姓名
	private TextView textView_idcard;// 客户身份证
	private TextView textView_address;// 客户地址
	private TextView textView_tybe_mess;// 业务类型
	private TextView textView_bank_mess;// 银行名称
	private TextView textView_time_mess;// 预约时间
	private TextView textView_arrtime_mess;// 到达时间
	private TextView textView_cphone_mess; // 派单员电话
	private TextView textView_client_phone_mess;// 客户电话
	private EditText textView_remark_mess;// 我的备注
	private TextView textView_msg_mess;// 提示信息
	private TextView textView_progress_yuyue;// 预约
	private TextView textView_progress_chufa;// 出发
	private TextView textView_progress_daoda;// 到达
	private TextView textView_progress_zhixing;// 执行
	private TextView textView_progress_wancheng;// 完成
	private ProgressBar progressBar1;// 动画快递员1
	private ProgressBar progressBar2;// 动画快递员1
	private ProgressBar progressBar3;// 动画快递员1
	private ProgressBar progressBar4;// 动画快递员1
	private ProgressBar progressBar5;// 动画快递员1
	private GsTaskInfo info; // listView传递过来的订单信息
	private InputMethodManager inputMethodManager; // 状态条
	private SharedPreferencesID shared;// 缓存订单状态时间改变
	LinearLayout layout;
	TextView btn_firm; // 按钮，出发，到达..文字的设置
	ImageButton btn_navi; // 导航按钮
	EditText et; // 取消原因
	ImageView progress; // 进度条
	ImageView status_frim;// 订单状态改变
	private String targetName; // 目标地址
	private double targetLat; // 目标纬度
	private double targetLon; // 目标经度
	private double lat; // 定位到的纬度
	private double lon; // 定位到的经度
	private GeocodeSearch search; // 高德POI搜索
	private ImageButton i_back; // 返回按钮
	private ImageButton btn_call; // 客户电话
	private ImageButton btn_boss_call;// 派单员电话
	private LinearLayout ll_btn_message;// 资料管理
	private LinearLayout ll_btn_message_2;// 我的备注
	private ImageButton textView_phone_mess_2;// 业务介绍
	private boolean startTag = false;// 判断目标是否出发

	/** 取消订单原因 */
	String items[] = { "无网络需再次核身", "设备问题需再次核身", "无法联系客户", "客户爽约（或迟到）", "不可抗因素无法抵达预约地点", "客户临时放弃需再次核身", "客户中途放弃需再次核身",
			"预约地址与上门地址不符", "客户提出改期" };
	/** 完成订单原因 */
	String completeItems[] = { "不在核身名单中" };

	private List<Integer> mSelectedItems;// 订单取消原因列表编号
	private List<Integer> mSelectedCompleteItems;// 完成订单原因列表编号
	StringBuffer sbComplete = new StringBuffer();// 拼接完成订单
	// 数据库应用
	OrdersDAO ordersDAO;
	// 文件读取
	SharedPreferencesUnit share;// 缓存token，用户信息
	// 声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient = null;
	// 定位参数
	private AMapLocationClientOption mLocationOption = null;
	// 路径规划预计到达时间
	private RouteSearch mRouteSearch;
	private DriveRouteResult mDriveRouteResult;
	private final int ROUTE_TYPE_DRIVE = 2;
	private LatLonPoint mStartPoint;// 起点，
	private LatLonPoint mEndPoint;// 终点，
	/*
	 * // handle用于处理定位数据，提交后台 Handler mHandle = new Handler() { public void
	 * handleMessage(android.os.Message msg) { switch (msg.what) { case 1:
	 * locationMsg(); break; case 2: print("网络连接中断"); break;
	 * 
	 * default: break; } }; };
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_task_message);
		// 把活动装箱
		MyApp.getInstance().addActivity(this);
		// 4.4及以上版本开启
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(Color.parseColor("#f5821e"));
		initView();
		initData();
		setOnclick();
		/*
		 * // 给定位客户端对象设置定位参数 // 开启定位
		 * mLocationClient.setLocationOption(mLocationOption);
		 * mLocationClient.startLocation();
		 */

	}

	private void setOnclick() {
		// 点击显示业务类型全数据
		textView_tybe_mess.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(TaskMessageActivity.this)
						.setMessage(textView_tybe_mess.getText());
				builder.create().show();

			}
		});
		// 点击显示全地址
		textView_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(TaskMessageActivity.this)
						.setMessage(textView_address.getText());
				builder.create().show();

			}
		});
		// 点击显示全提示信息
		textView_msg_mess.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(TaskMessageActivity.this)
						.setMessage(textView_msg_mess.getText());
				builder.create().show();

			}
		});
		// 点击改变状态按钮
		status_frim.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("出发".equals(btn_firm.getText())) {
					firmStart();

				} else if ("到达".equals(btn_firm.getText())) {
					firmArrive();

				} else if ("执行".equals(btn_firm.getText())) {
					firmWorking();

				} else if ("完成".equals(btn_firm.getText())) {
					// 完成按钮。
					firmPerform();

				}
			}
		});
		// 打开导航
		btn_navi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("------targetLat------" + targetLat);
				try {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.setPackage("com.autonavi.minimap");
					intent.setData(Uri.parse("androidamap://viewMap?sourceApplication=appname&poiname=" + targetName
							+ "&lat=" + targetLat + "&lon=" + targetLon + "&dev=0"));
					startActivity(intent); // 启动调用
				} catch (Exception e) {
					print("请下载安装高德地图");
				}

			}
		});
		// 返回
		i_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		// 调起客户电话
		btn_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断电话号码
				String phoneNum = info.getPhone();

				if (phoneNum != null) {
					// 广播
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
					startActivity(intent);
				}

			}
		});
		// 调起派单员电话
		btn_boss_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断电话号码
				String phoneNum = info.getPaidanphone();

				if (phoneNum != null) {
					// 广播
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
					startActivity(intent);
				}

			}
		});

		// 资料管理
		ll_btn_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 此功能暂未开放
				AlertDialog.Builder builder = new AlertDialog.Builder(TaskMessageActivity.this).setTitle("资料管理")
						.setMessage("暂未开放，敬请期待");
				builder.create().show();
			}
		});

		// 跳转到我的备注
		ll_btn_message_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TaskMessageActivity.this, MineMarkActivity.class);
				intent.putExtra("beizhu_yuyue", textView_msg_mess.getText().toString());
				intent.putExtra("ordercode", info.getOrdercode());
				startActivity(intent);
			}
		});
		// 业务介绍
		textView_phone_mess_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(TaskMessageActivity.this).setTitle("业务类型")
						.setMessage("暂未开放，敬请期待");
				builder.create().show();

			}
		});

	}

	/*
	 * //判断地图是否存在 private boolean isInstallByread(String packageName) { return
	 * new File("/data/data/" + packageName).exists(); }
	 */

	private void initView() {
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		layout = (LinearLayout) findViewById(R.id.layout_mess);
		btn_firm = (TextView) findViewById(R.id.textview_point_feiji_mess);
		btn_navi = (ImageButton) findViewById(R.id.button_open_navi);
		i_back = (ImageButton) findViewById(R.id.imageView_task_back);
		btn_call = (ImageButton) findViewById(R.id.textView_phone_mess);
		btn_boss_call = (ImageButton) findViewById(R.id.textView_phone_boss_mess);
		progress = (ImageView) findViewById(R.id.image_progress);

		/********** INITIALIZES *************/
		textView_num_mess = (TextView) findViewById(R.id.textView_num_mess);
		textView_name_mess = (TextView) findViewById(R.id.textView_name_mess);
		textView_idcard = (TextView) findViewById(R.id.textView_idcard_mess);
		textView_address = (TextView) findViewById(R.id.textView_address);
		textView_tybe_mess = (TextView) findViewById(R.id.textView_tybe_mess);
		textView_bank_mess = (TextView) findViewById(R.id.textView_bank_mess);
		textView_time_mess = (TextView) findViewById(R.id.textView_time_mess);
		textView_arrtime_mess = (TextView) findViewById(R.id.textView_arrtime_mess);
		textView_cphone_mess = (TextView) findViewById(R.id.textView_cphone_mess);// 派单员电话
		textView_client_phone_mess = (TextView) findViewById(R.id.textView_client_phone_mess);// 客户电话
		textView_msg_mess = (TextView) findViewById(R.id.textView_hint_mess);// 提示信息
		textView_remark_mess = (EditText) findViewById(R.id.textView_remark_mess);// 我的备注
		/********** 进图条时间文字 *************/
		textView_progress_yuyue = (TextView) findViewById(R.id.textView_progress_yuyue);
		textView_progress_chufa = (TextView) findViewById(R.id.textView_progress_chufa);
		textView_progress_daoda = (TextView) findViewById(R.id.textView_progress_daoda);
		textView_progress_zhixing = (TextView) findViewById(R.id.textView_progress_zhixing);
		textView_progress_wancheng = (TextView) findViewById(R.id.textView_progress_wancheng);
		// 点击进度条改变的按钮
		status_frim = (ImageView) findViewById(R.id.button_message_firm);
		// 资料管理
		ll_btn_message = (LinearLayout) findViewById(R.id.ll_btn_message);
		// 我的备注
		ll_btn_message_2 = (LinearLayout) findViewById(R.id.ll_btn_message_2);
		// 业务介绍
		textView_phone_mess_2 = (ImageButton) findViewById(R.id.textView_phone_mess_2);
		/********** 快递员动画 *************/
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
		progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
		progressBar4 = (ProgressBar) findViewById(R.id.progressBar4);
		progressBar5 = (ProgressBar) findViewById(R.id.progressBar5);
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
	 * 点击取消弹出窗口
	 * 
	 * @param view
	 */
	public void onCancle(View view) {
		// popup实现方式
		/*
		 * WindowManager.LayoutParams lp = getWindow().getAttributes(); lp.alpha
		 * = 0.7f; getWindow().setAttributes(lp); LayoutInflater layoutInflater
		 * =(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 * View conteView =layoutInflater.inflate(R.layout.popup_input_layout,
		 * null); conteView.setFocusable(true); // 这个很重要
		 * conteView.setFocusableInTouchMode(true); final PopupWindow
		 * popupWindow =new PopupWindow(conteView,LayoutParams.WRAP_CONTENT,
		 * LayoutParams.WRAP_CONTENT);
		 * 
		 * popupWindow.setFocusable(true);
		 * popupWindow.setOutsideTouchable(false);
		 * popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
		 * conteView.setOnKeyListener(new OnKeyListener() {
		 * 
		 * @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
		 * if(keyCode ==KeyEvent.KEYCODE_BACK){ popupWindow.dismiss();
		 * 
		 * 
		 * return true; } return false; } });
		 * popupWindow.setOnDismissListener(new OnDismissListener() {
		 * 
		 * @Override public void onDismiss() { WindowManager.LayoutParams lp =
		 * getWindow().getAttributes(); lp.alpha = 1f;
		 * getWindow().setAttributes(lp);
		 * 
		 * } }); popupWindow.showAtLocation(view,
		 * Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
		 */

		// dialog实现方式
		cancleTask();
	}

	// 取消订单对话框
	private void cancleTask() {

		mSelectedItems = new ArrayList<Integer>();// 声明集合
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		builder.setTitle("取消本订单?").setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if (isChecked) {
					mSelectedItems.add(which);
				} else if (mSelectedItems.contains(which)) {
					mSelectedItems.remove(Integer.valueOf(which));
				}

			}
		});
		// 获取自定义原因
		View view = inflater.inflate(R.layout.bg_cancle_layout, null);
		builder.setView(view);
		et = (EditText) view.findViewById(R.id.editText_cancle_reason);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 发送取消订单的广播
				sendCancle();
				// 修改后台(带原因)
				// 获取token
				String token = share.reStoreData("token");
				// 获取原因列表值
				StringBuffer sb = new StringBuffer();
				for (Integer i : mSelectedItems) {
					sb.append(items[i]);
					sb.append("&");
				}
				// 获取取消原因
				String remark = sb + et.getText().toString().trim();
				if (remark.length() == 0) {
					print("操作失误，取消订单请先填选原因！");
					return;
				}
				String time = getTime();
				HttpParams params = new HttpParams();
				params.put("ordercode", info.getOrdercode());
				params.put("token", token);
				params.put("remark", remark);
				params.put("status", "5");
				params.put("updatetime", time);
				OkHttpUtils.post(Config.TASK_CANCLE).params(params).execute(new StringCallback() {

					@Override
					public void onResponse(boolean arg0, String arg1, Request arg2, Response arg3) {
						// 取消成功，更新数据库
						GsLoginOut data = new Gson().fromJson(arg1, GsLoginOut.class);

						if (data.getRet().equals("0")) {
							boolean update = ordersDAO.deleteAData(info.getOrdercode());
							if (update) {
								finish();
							}
						} else if (data.getRet().equals("1004")) {
							// token失效
							againLogin();
							LogUtil.i("TAG", "取消订单失败，返回码1004");
						} else if (data.getRet().equals("1003")) {
							LogUtil.i("TAG", "取消订单失败，返回码1003");

						} else if (data.getRet().equals("1002")) {
							LogUtil.i("TAG", "取消订单失败，返回码1002");

						} else if (data.getRet().equals("1001")) {
							LogUtil.i("TAG", "取消订单失败，返回码1001");

						} else if (data.getRet().equals("2000")) {
							LogUtil.i("TAG", "取消订单失败，返回码2000");

						}
					}

					@Override
					public void onError(boolean isFromCache, Call call, Response response, Exception e) {
						super.onError(isFromCache, call, response, e);
						// 订单取消失败
						print("取消失败，请确保网络连接正常！");
					}
				});

			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		AlertDialog tempDialog = builder.create();
		tempDialog.setOnShowListener(new OnShowListener() {
			public void onShow(DialogInterface dialog) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(et, InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		});

		tempDialog.show();

	}

	// 导航开始
	@Override
	public void onGeocodeSearched(GeocodeResult result, int code) {
		if (code == 1000) {
			if (result != null && result.getGeocodeAddressList() != null && result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				// 目标位置
				targetLat = address.getLatLonPoint().getLatitude();
				targetLon = address.getLatLonPoint().getLongitude();
				mStartPoint = new LatLonPoint(lat, lon);// 起点，
				mEndPoint = new LatLonPoint(targetLat, targetLon);// 终点
				System.out.println("----纬度------" + targetLat);
				System.out.println("----经度------" + targetLon);
				// 发起算路
				searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);

			} else {
				print("地理位置信息错误");
			}
		} else {
			print("网络连接中断，无法打开导航");
		}

	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {

	}

	// dialog提示
	// 出发
	private void firmStart() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("是否确认出发").setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 出发广播，开始上传定位
				sendLocation();
				// 更新数据库里的订单状态
				boolean update = ordersDAO.update(info.getOrdercode(), "出发");
				if (update == false) {
					LogUtil.e(getClass().getName(), "到达更新数据失败");
				} else {
					// 取出预约的时间
					// getStatusTime("预约");
					// 设置出发时间，保存
					String time = getTime();
					textView_progress_chufa.setText("出发" + "(" + time + ")");
					textView_progress_chufa
							.setTextColor(TaskMessageActivity.this.getResources().getColor(R.color.mycfss));
					btn_firm.setText("到达");
					savaTime("出发", time);
					progress.setBackgroundResource(R.drawable.two);
					// 快递员动画
					progressBar1.setVisibility(View.GONE);
					progressBar2.setVisibility(View.VISIBLE);
					// 填写出发的备注
					if (textView_remark_mess.getText().length() != 0) {
						savaBeizhu("出发备注", textView_remark_mess.getText().toString());
						textView_remark_mess.setText("");
					}
					// 网络修改
					alterStartData();
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		builder.create().show();
	}

	protected void sendLocation() {
		Intent intent = new Intent();
		intent.setAction("com.yd.cfssbusinessdemo.dingweiservice");
		intent.putExtra("code", info.getOrdercode());
		this.sendBroadcast(intent);

	}

	protected void sendCancle() {
		Intent intent = new Intent();
		intent.setAction("com.yd.cfssbusinessdemo.cancledingweiservice");
		intent.putExtra("cancle_code", info.getOrdercode());
		this.sendBroadcast(intent);
	}

	// 后台出发
	protected void alterStartData() {
		String token = share.reStoreData("token");
		HttpParams params = new HttpParams();
		params.put("ordercode", info.getOrdercode());
		params.put("updatetime", getStatusTime("出发"));
		params.put("token", token);
		params.put("status", "1");
		OkHttpUtils.post(Config.TASK_START).params(params).execute(new StringCallback() {

			@Override
			public void onResponse(boolean arg0, String arg1, Request arg2, Response arg3) {
				// 判断是否修改成功
				// 判断是否修改成功
				GsLoginOut data = new Gson().fromJson(arg1, GsLoginOut.class);
				if (data.getRet().equals("0")) {

				} else if (data.getRet().equals("2000")) {
					print("msg:系统异常" + "code:2000");
				} else if (data.getRet().equals("1001")) {
					print("msg:单号或状态为空" + "code:1001");
				} else if (data.getRet().equals("1002")) {
					print("msg:该单据不存在" + "code:1002");
				} else if (data.getRet().equals("1003")) {
					print("msg:数据更新失败" + "code:1003");
				} else if (data.getRet().equals("1004")) {
					// token失效
					againLogin();
				}

			}

			@Override
			public void onError(boolean isFromCache, Call call, Response response, Exception e) {
				super.onError(isFromCache, call, response, e);
			}
		});
	}

	// 到达
	private void firmArrive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("是否确认到达").setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 到达，结束上传定位
				sendLocation();
				// 更新数据库里的订单状态
				boolean update = ordersDAO.update(info.getOrdercode(), "到达");
				if (update == false) {
				} else {
					// 保存到达的时间
					String time = getTime();
					textView_progress_daoda.setText("到达" + "(" + time + ")");
					savaTime("到达", time);
					progress.setBackgroundResource(R.drawable.three);
					// 设置字体颜色
					textView_progress_daoda
							.setTextColor(TaskMessageActivity.this.getResources().getColor(R.color.mycfss));
					btn_firm.setText("执行");
					progressBar3.setVisibility(View.VISIBLE);
					progressBar1.setVisibility(View.GONE);
					progressBar2.setVisibility(View.GONE);
					// 填写到达的备注
					if (textView_remark_mess.getText().length() != 0) {
						savaBeizhu("到达备注", textView_remark_mess.getText().toString());
						textView_remark_mess.setText("");
					}
					// 后台到达
					alterArriveData();
				}

			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show();
	}

	// 后台到达
	protected void alterArriveData() {
		String token = share.reStoreData("token");
		HttpParams params = new HttpParams();
		params.put("ordercode", info.getOrdercode());
		params.put("updatetime", getStatusTime("到达"));
		params.put("token", token);
		params.put("status", "2");
		OkHttpUtils.post(Config.TASK_ARRIVE).params(params).execute(new StringCallback() {

			@Override
			public void onResponse(boolean arg0, String arg1, Request arg2, Response arg3) {
				// 判断是否修改成功
				// 判断是否修改成功
				GsLoginOut data = new Gson().fromJson(arg1, GsLoginOut.class);
				if (data.getRet().equals("0")) {

				} else if (data.getRet().equals("2000")) {
					print("msg:系统异常" + "code:2000");
				} else if (data.getRet().equals("1001")) {
					print("msg:单号或状态为空" + "code:1001");
				} else if (data.getRet().equals("1002")) {
					print("msg:该单据不存在" + "code:1002");
				} else if (data.getRet().equals("1003")) {
					print("msg:数据更新失败" + "code:1003");
				} else if (data.getRet().equals("1004")) {
					// token失效
					againLogin();
				}

			}

			@Override
			public void onError(boolean isFromCache, Call call, Response response, Exception e) {

				super.onError(isFromCache, call, response, e);
			}
		});

	}

	// 执行
	private void firmWorking() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("是否确认执行").setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 更新数据库里的订单状态
				boolean update = ordersDAO.update(info.getOrdercode(), "执行");
				if (update == false) {

				} else {
					// 取消预约，出发，到达的时间
					// getStatusTime("预约");

					// 保存执行的时间
					String time = getTime();
					textView_progress_zhixing.setText("执行" + "(" + time + ")");
					savaTime("执行", time);
					progress.setBackgroundResource(R.drawable.four);
					textView_progress_zhixing
							.setTextColor(TaskMessageActivity.this.getResources().getColor(R.color.mycfss));
					btn_firm.setText("完成");
					progressBar4.setVisibility(View.VISIBLE);
					progressBar3.setVisibility(View.GONE);
					progressBar1.setVisibility(View.GONE);
					progressBar2.setVisibility(View.GONE);
					// 填写执行的备注
					if (textView_remark_mess.getText().length() != 0) {
						savaBeizhu("执行备注", textView_remark_mess.getText().toString());
						textView_remark_mess.setText("");
					}
					// 后台执行刷新
					alterExecuteData();

				}

			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		builder.create().show();
	}

	// 启动微核身app
	protected void startApp() {
		String packageName = "com.webank.accountupgrade";
		try {
			Intent intent = TaskMessageActivity.this.getPackageManager().getLaunchIntentForPackage(packageName);
			startActivity(intent);
		} catch (Exception e) {
			print("没有安装此核身App");
		}

	}

	// 后台执行
	protected void alterExecuteData() {
		String token = share.reStoreData("token");
		HttpParams params = new HttpParams();
		params.put("ordercode", info.getOrdercode());
		params.put("updatetime", getStatusTime("执行"));
		params.put("token", token);
		params.put("status", "3");
		OkHttpUtils.post(Config.TASK_ARRIVE).params(params).execute(new StringCallback() {

			@Override
			public void onResponse(boolean arg0, String arg1, Request arg2, Response arg3) {
				// 判断是否修改成功
				GsLoginOut data = new Gson().fromJson(arg1, GsLoginOut.class);
				if (data.getRet().equals("0")) {
					// 打开银行APP
					startApp();
				} else if (data.getRet().equals("2000")) {
					print("msg:系统异常" + "code:2000");
				} else if (data.getRet().equals("1001")) {
					print("msg:单号或状态为空" + "code:1001");
				} else if (data.getRet().equals("1002")) {
					print("msg:该单据不存在" + "code:1002");
				} else if (data.getRet().equals("1003")) {
					print("msg:数据更新失败" + "code:1003");
				} else if (data.getRet().equals("1004")) {
					// token失效
					againLogin();
				}
			}

			@Override
			public void onError(boolean isFromCache, Call call, Response response, Exception e) {
				// TODO Auto-generated method stub
				super.onError(isFromCache, call, response, e);
				System.out.println("----------日了你大爷的了--------");
				// 打开银行APP
				startApp();
			}
		});

	}

	// 完成
	private void firmPerform() {

		// 点击完成的时候，弹出多选框，多选框一条内容是：未在核身名单中

		mSelectedCompleteItems = new ArrayList<Integer>();// 声明集合
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// 设置标题，设置多选内容
		builder.setTitle("完成本订单?").setMultiChoiceItems(completeItems, null,
				new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						// 多选点击判断逻辑，选择原因值
						if (isChecked) {
							mSelectedCompleteItems.add(which);
						} else if (mSelectedCompleteItems.contains(which)) {
							mSelectedCompleteItems.remove(Integer.valueOf(which));
						}

					}
				});

		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 保存完成的时间
				String time = getTime();
				savaTime("完成", time);
				boolean update = ordersDAO.update(info.getOrdercode(), "完成");
				if (update == false) {
				} else {
					// 获取原因列表值
					for (Integer i : mSelectedCompleteItems) {
						sbComplete.append(completeItems[i]);
						// sbComplete.append("&");
					}
					// 后台完成刷新
					String token = share.reStoreData("token");
					String completeStr = sbComplete.toString();
					HttpParams params = new HttpParams();
					params.put("ordercode", info.getOrdercode());
					params.put("updatetime", getStatusTime("完成"));
					params.put("token", token);
					params.put("status", "4");
					params.put("remark", completeStr);
					OkHttpUtils.post(Config.TASK_ARRIVE).params(params).execute(new StringCallback() {

						@Override
						public void onResponse(boolean arg0, String arg1, Request arg2, Response arg3) {
							// 判断是否修改成功
							System.out.println("------perform------");
							GsLoginOut data = new Gson().fromJson(arg1, GsLoginOut.class);
							if (data.getRet().equals("0")) {
								// 修改成功，数据库删除完成订单的任务
								ordersDAO.deleteAData(info.getOrdercode());
								clearNotes();
								// 修改成功返回界面
								finish();
							} else if (data.getRet().equals("2000")) {
								print("msg:系统异常" + "code:2000");
							} else if (data.getRet().equals("1001")) {
								print("msg:单号或状态为空" + "code:1001");
							} else if (data.getRet().equals("1002")) {
								print("msg:该单据不存在" + "code:1002");
							} else if (data.getRet().equals("1003")) {
								print("msg:数据更新失败" + "code:1003");
							} else if (data.getRet().equals("1004")) {
								// token失效
								againLogin();
							}
						}

						@Override
						public void onError(boolean isFromCache, Call call, Response response, Exception e) {
							super.onError(isFromCache, call, response, e);
							finish();

						}
					});
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				LogUtil.i("TAG", "取消了完成订单对话框");
			}
		});

		builder.create().show();

	}

	private void initData() {
		// 获取被点击的item数据
		Intent intent = getIntent();
		info = (GsTaskInfo) intent.getSerializableExtra("info");
		Log.i("TAG", "任务详情数据" + info.toString());
		// 数据库实例
		ordersDAO = new OrdersDAO(TaskMessageActivity.this);
		// 文件读取实例
		share = SharedPreferencesUnit.instance(TaskMessageActivity.this);
		// 根据订单号缓存
		shared = new SharedPreferencesID(TaskMessageActivity.this, info.getOrdercode());
		// 发起导航服务
		targetName = info.getAddress();// 获取目标地址
		textView_num_mess.setText(info.getOrdercode());// 订单号
		textView_name_mess.setText(info.getName());// 客户姓名
		textView_idcard.setText(info.getidentno());// 客户ID
		textView_address.setText(info.getAddress());// 客户地址
		textView_tybe_mess.setText(info.getBilltype());// 业务类型
		textView_bank_mess.setText(info.getBankname());// 行属
		textView_time_mess.setText(info.getYuyuetime());// 时间
		// textView_arrtime_mess.setText(info.getYujitime());// 预计到达时间
		textView_cphone_mess.setText(info.getPaidanphone());// 派单员联系方式
		textView_client_phone_mess.setText(StringUnit.symbolString(info.getPhone()));// 客户电话
		textView_msg_mess.setText(info.getRemark()); // 提示信息
		// 根据订单状态更改进度条状态、按钮状态
		String status = info.getStatus();
		if ("已分配至核验人员".equals(status) || "已分配至二次核身人员".equals(status)) {
			textView_progress_yuyue.setText("预约" + "(" + info.getYuyuetime() + ")");
			textView_progress_yuyue.setTextColor(this.getResources().getColor(R.color.mycfss));
			btn_firm.setText("出发");
			progressBar1.setVisibility(View.VISIBLE);
			progress.setBackgroundResource(R.drawable.one);
		} /*
			 * else if ("预约".equals(status)) {
			 * textView_progress_yuyue.setText("预约"+"("+getTime()+")");
			 * textView_progress_chufa.setText("出发"+"("+getTime()+")");
			 * textView_progress_chufa
			 * .setTextColor(this.getResources().getColor(R.color.mycfss));
			 * textView_progress_yuyue
			 * .setTextColor(this.getResources().getColor(R.color.graybg));
			 * btn_firm.setText("出发");
			 * progress.setBackgroundResource(R.drawable.one);
			 */
		else if ("出发".equals(status)) {
			textView_progress_yuyue.setText("预约" + "(" + info.getYuyuetime() + ")");
			textView_progress_chufa.setText("出发" + "(" + getStatusTime("出发") + ")");
			textView_progress_chufa.setTextColor(this.getResources().getColor(R.color.mycfss));
			btn_firm.setText("到达");
			progressBar2.setVisibility(View.VISIBLE);
			progress.setBackgroundResource(R.drawable.two);
			startTag = true;
			// 出发状态显示预计到达时间
			textView_arrtime_mess.setText(getStatusTime("预计到达"));
		} else if ("到达".equals(status)) {
			textView_progress_yuyue.setText("预约" + "(" + info.getYuyuetime() + ")");
			textView_progress_chufa.setText("出发" + "(" + getStatusTime("出发") + ")");
			textView_progress_daoda.setText("到达" + "(" + getStatusTime("到达") + ")");
			textView_progress_daoda.setTextColor(this.getResources().getColor(R.color.mycfss));
			btn_firm.setText("执行");
			progressBar3.setVisibility(View.VISIBLE);
			progress.setBackgroundResource(R.drawable.three);
		} else if ("执行".equals(status)) {
			textView_progress_yuyue.setText("预约" + "(" + info.getYuyuetime() + ")");
			textView_progress_zhixing.setText("执行" + "(" + getStatusTime("执行") + ")");
			textView_progress_chufa.setText("出发" + "(" + getStatusTime("出发") + ")");
			textView_progress_daoda.setText("到达" + "(" + getStatusTime("到达") + ")");
			textView_progress_zhixing.setTextColor(this.getResources().getColor(R.color.mycfss));
			btn_firm.setText("完成");
			progressBar4.setVisibility(View.VISIBLE);
			progress.setBackgroundResource(R.drawable.four);
		}
		// 读取预计到达时间的缓存
		/*
		 * // 初始化定位 mLocationClient = new
		 * AMapLocationClient(getApplicationContext()); // 初始化定位参数
		 * mLocationOption = new AMapLocationClientOption();
		 * mLocationClient.setLocationListener(this);
		 */
		// 导航
		search = new GeocodeSearch(this);
		search.setOnGeocodeSearchListener(this);
		/*
		 * // 设置定位的参数
		 * mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy); //
		 * 设置是否返回地址信息（默认返回地址信息） mLocationOption.setNeedAddress(true); //
		 * 设置是否只定位一次,默认为false mLocationOption.setOnceLocation(false); //
		 * 设置是否强制刷新WIFI，默认为强制刷新 mLocationOption.setWifiActiveScan(true); //
		 * 设置是否允许模拟位置,默认为false，不允许模拟位置 mLocationOption.setMockEnable(false); //
		 * 设置定位间隔,单位毫秒,默认为2000ms mLocationOption.setInterval(10000);
		 */
		// 初始化路径规划
		mRouteSearch = new RouteSearch(this);
		mRouteSearch.setRouteSearchListener(this);
		// 计算路程时间
		calculateTime();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// mLocationClient.onDestroy();// 销毁定位客户端。
		System.out.println("---------destory");
	}

	/*
	 * // 定位回调的结果
	 * 
	 * @Override public void onLocationChanged(AMapLocation amapLocation) { if
	 * (amapLocation != null) { System.out.println("---定位返回代码----" +
	 * amapLocation.getErrorCode()); if (amapLocation.getErrorCode() == 0) { //
	 * 定位成功回调信息，设置相关消息 lat = amapLocation.getLatitude();// 获取纬度 lon =
	 * amapLocation.getLongitude();// 获取经度
	 * System.out.println("--单一定位--------"+lat+"&"+lon); //
	 * amapLocation.getAccuracy();// 获取精度信息 // 判断目标是否出发，如果出发了才发送定位数据
	 * System.out.println("---判断是否上传的标记----"+startTag); if (startTag) { //
	 * 开启定位数据发送后台线程 mHandle.sendEmptyMessage(1); } // 获取到当前的定位地址后，即可开始计算路程
	 * calculateTime(); } else { mHandle.sendEmptyMessage(2); } }
	 * 
	 * }
	 */

	// 预计到达时间
	private void calculateTime() {
		// 得到当前的经纬度
		lat = MyService.lat;
		lon = MyService.lon;
		// 发起获取
		GeocodeQuery query = new GeocodeQuery(targetName, "");
		search.getFromLocationNameAsyn(query);

	}

	private void searchRouteResult(int routeType, int mode) {
		if (mStartPoint == null) {
			return;
		}
		if (mEndPoint == null) {
			return;
		}

		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
		if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
			DriveRouteQuery query = new DriveRouteQuery(fromAndTo, mode, null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
			mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
		}

	}

	// 向服务器发送定位数据
	protected void locationMsg() {
		// 获取token
		String token = share.reStoreData("token");
		// 参数
		HttpParams params = new HttpParams();
		params.put("ordercode", info.getOrdercode());
		params.put("lon", String.valueOf(lon));
		params.put("lat", String.valueOf(lat));
		params.put("token", token);
		// 网络
		OkHttpUtils.post(Config.LOCATION_URL).params(params).execute(new StringCallback() {

			@Override
			public void onResponse(boolean arg0, String arg1, Request arg2, Response arg3) {
				GsLoginOut data = new Gson().fromJson(arg1, GsLoginOut.class);
				if (data.getRet().equals("0")) {
					System.out.println("-----上传lon-----" + String.valueOf(lon));
					System.out.println("-----上传-lat----" + String.valueOf(lat));
				} else if (data.getRet().equals("2000")) {
					print("错误码：2000  后台系统异常，定位上传失败");
				} else if (data.getRet().equals("1003")) {
					print("错误码：1003 坐标格式错误");
				} else if (data.getRet().equals("1002")) {
					print("错误码：1002 该单不存在");
				} else if (data.getRet().equals("1001")) {
					print("错误码：1003  用户未登录");
				}

			}

			@Override
			public void onError(boolean isFromCache, Call call, Response response, Exception e) {
				super.onError(isFromCache, call, response, e);
				print("定位失败");
			}
		});

	}

	// 隐藏键盘

	void hideSoftKeyboard() {
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void print(String message) {
		Toast.makeText(TaskMessageActivity.this, message, 0).show();
	}

	/**
	 * 监听Back键按下事件,方法1: 注意: super.onBackPressed()会自动调用finish()方法,关闭 当前Activity.
	 * 若要屏蔽Back键盘,注释该行代码即可
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	// token失效，弹出界面
	private void againLogin() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage("你的账号已失效，请重新登录").setCancelable(false)
				.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 请出用户信息，回到登录界面
						share.clear();
						Intent intent = new Intent(TaskMessageActivity.this, LoginActivity.class);
						startActivity(intent);

					}
				}).setNegativeButton("退出", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 请出用户信息，退出App
						share.clear();
						MyApp.getInstance().exit();

					}
				});
		builder.create().show();
	}

	// 获取时间的方法
	private String getTime() {
		// 获取系统时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	// 根据订单号以及订单号状态保存订单状态改变的时间
	private void savaTime(String key, String value) {
		shared.savaData(key, value);
	}

	// 根据订单号保存订单的备注
	private void savaBeizhu(String key, String value) {
		shared.savaData(key, value);
	}

	// 根据订单号保存订单的备注
	private void savaLat(String key, String value) {
		shared.savaData(key, value);
	}

	// 取出时间
	private String getLat(String key) {
		return shared.reStoreData(key);
	}

	// 取出时间
	private String getStatusTime(String key) {
		return shared.reStoreData(key);
	}

	// 清除
	private void clearNotes() {
		shared.clear();
	}

	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
		System.out.println("------走没走-------" + errorCode);
		if (errorCode == 1000) {
			if (result != null && result.getPaths() != null) {
				if (result.getPaths().size() > 0) {
					mDriveRouteResult = result;
					final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
					int dis = (int) drivePath.getDistance();
					int dur = (int) drivePath.getDuration();
					String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
					textView_arrtime_mess.setText(des);
					// 缓存预计到达时间
					savaTime("预计到达", des);
					/*
					 * mRouteDetailDes.setVisibility(View.VISIBLE); int taxiCost
					 * = (int) mDriveRouteResult.getTaxiCost();
					 * mRouteDetailDes.setText("打车约"+taxiCost+"元");
					 */
				} else if (result != null && result.getPaths() == null) {
					// ToastUtil.show(mContext, R.string.no_result);
				}

			} else {
				// ToastUtil.show(mContext, R.string.no_result);
			}
		} else {
			// ToastUtil.showerror(this.getApplicationContext(), errorCode);
		}

	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

}
