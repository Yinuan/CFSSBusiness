package com.yd.cfssbusinessdemo.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.yd.cfssbusinessdemo.TaskMessageActivity;
import com.yd.cfssbusinessdemo.config.Config;
import com.yd.cfssbusinessdemo.entity.GsLoginOut;
import com.yd.cfssbusinessdemo.util.SharedPreferencesID;
import com.yd.cfssbusinessdemo.util.SharedPreferencesUnit;

import android.R.integer;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service implements AMapLocationListener {

	private SharedPreferencesUnit share;// 缓存token，用户信息
	private static String ordercode;// 订单号
	private static String ordercode_code;// 取消的订单号
	private static List<String> list = new ArrayList<String>();// 装订单号的集合
	private SharedPreferencesID shard;
	private String ordercode_net;// 上传的定位数据
	// 声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient = null;
	// 定位参数
	private AMapLocationClientOption mLocationOption = null;

	private Handler mhandle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				ordercode_net = shard.reStoreData("codeList");
				if (ordercode_net.isEmpty() || ordercode_net == null) {

				} else if (getTime() >= 8 && getTime() <= 22) {
					// 工作时间段才记录定位位置,确保用户隐私
					locationMsg();
				}
				break;
			case 2:
				// print("定位失败");
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onCreate() {
		super.onCreate();

		initData();
	}

	private void initData() {
		// 文件读取实例
		share = SharedPreferencesUnit.instance(MyService.this);
		// 缓存保存实例
		shard = new SharedPreferencesID(MyService.this, "myService");
		// 初始化定位
		mLocationClient = new AMapLocationClient(getApplicationContext());
		// 初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		mLocationClient.setLocationListener(this);
		// 设置定位的参数
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		// 设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(false);
		// 设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		// 设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		// 设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(10000);
		// 开启定位
		mLocationClient.setLocationOption(mLocationOption);
		mLocationClient.startLocation();

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("-----onStartCommand--------");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("--------onDestroy------");
		mLocationClient.onDestroy();// 销毁定位客户端
		shard.clear();// 清除此订单字符串
	}

	// 向服务器发送定位数据
	protected void locationMsg() {
		// 获取token
		String token = share.reStoreData("token");
		// 参数
		HttpParams params = new HttpParams();
		params.put("ordercode", ordercode_net);
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
				print("定位信息提交失败");
			}
		});
	}

	private void print(String message) {
		Toast.makeText(MyService.this, message, 0).show();
	}

	public static double lat, lon; // 定位经纬度

	// 定位改变的结果
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null) {
			System.out.println("---定位返回代码----" + amapLocation.getErrorCode());
			if (amapLocation.getErrorCode() == 0) {
				// 定位成功回调信息，设置相关消息
				lat = amapLocation.getLatitude();// 获取纬度
				lon = amapLocation.getLongitude();// 获取经度
				saveCode();
				// 发送定位数据
				mhandle.sendEmptyMessage(1);
			} else {
				mhandle.sendEmptyMessage(2);
			}
		}
	}

	// 出发、到达广播接收器
	public static class MyBoradcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.yd.cfssbusinessdemo.dingweiservice")) {
				System.out.println("--------接受到了静态广播！----------");
				ordercode = intent.getStringExtra("code");
				System.out.println("-----ordercode----" + ordercode);
				handOrdercode();
			}

		}

	}

	// 取消订单的广播接收器
	public static class MyCancleBoradcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.yd.cfssbusinessdemo.cancledingweiservice")) {
				System.out.println("--------接受到了静态广播！----------");
				ordercode_code = intent.getStringExtra("cancle_code");
				System.out.println("-----ordercode----" + ordercode_code);
				if (list.contains(ordercode_code)) {
					list.remove(ordercode_code);
				}

			}

		}

	}

	// 订单号处理
	public static void handOrdercode() {
		// 如果原有的订单号中有此订单，则删除此订单
		// 如果原有的订单号中没有此订单，则加人此订单
		if (ordercode == null || ordercode.isEmpty()) {

		} else {
			String[] code = ordercode.split("&");
			if (list.size() == 0 || list == null) {
				for (int i = 0; i < code.length; i++) {
					list.add(code[i]);
				}
			} else {
				// 将数组变成集合
				List<String> code_list = new ArrayList<String>();
				for (int i = 0; i < code.length; i++) {
					code_list.add(code[i]);
				}
				// 两个集合进行比较
				for (String str : code_list) {
					if (list.contains(str)) {
						// 有这订单时，删除
						list.remove(str);
					} else {
						// 没有这个订单时，加入
						list.add(str);
					}
				}
			}
			System.out.println("-----订单集合的大小-------" + list.size());
		}

	}

	// 遍历集合，得到特殊的字符串，传给后台
	public String getCode() {
		if (list.size() == 0 || list == null) {
			return null;
		} else {
			StringBuffer svn = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				svn.append(list.get(i));
				svn.append("&");
			}
			return svn.toString();
		}

	}

	// 缓存保存订单集合号
	private void saveCode() {
		shard.savaData("codeList", getCode());
	}

	// 获取时间的方法
	private int getTime() {
		// 获取系统时间
		Calendar cal = Calendar.getInstance();
		int data = cal.get(Calendar.HOUR_OF_DAY);
		return data;
	}

}
