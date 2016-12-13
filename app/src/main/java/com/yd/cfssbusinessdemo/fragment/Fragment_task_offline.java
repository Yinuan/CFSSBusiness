package com.yd.cfssbusinessdemo.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.yd.cfssbusinessdemo.R;
import com.yd.cfssbusinessdemo.adapter.MyTaskListApater;
import com.yd.cfssbusinessdemo.config.Config;
import com.yd.cfssbusinessdemo.entity.GsLoginOut;
import com.yd.cfssbusinessdemo.entity.GsTaskInfo;
import com.yd.cfssbusinessdemo.service.MyTaskReceiver;
import com.yd.cfssbusinessdemo.util.SharedPreferencesID;

public class Fragment_task_offline extends BaseFragment {

	private ListView lv;
	private MyTaskListApater mApater;
	private List<GsTaskInfo> list_offline;// 数据源
	private LinearLayout layout;
	private LinearLayout layout_null;
	private String ordercode;// 订单号
	private ProgressBar btn_commit;// 提交任务动画
	private Button btn_null; // 刷新
	private LinearLayout layout_commit;// 点击提交
	private SharedPreferencesID shared;// 用于缓存订单状态改变的时间

	@Override
	protected int setLayoutResID() {

		return R.layout.fragment_task_offline;
	}

	@Override
	protected void initData() {

	}

	@Override
	public void onStart() {
		super.onStart();

	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {		
			showView();
			if (null != list_offline && list_offline.size() != 0) {
				// 离线请求
				askWeb();
			}			
		}
	}

	// 请求网络
	private void askWeb() {
		for (int i = 0; i < list_offline.size(); i++) {
			ordercode = list_offline.get(i).getOrdercode();
			shared = new SharedPreferencesID(getActivity(), ordercode);
			offlineAsk();
		}	
	}

	private void offlineAsk() {
		// 上传服务器进行更改
		String token = share.reStoreData("token");
		HttpParams params = new HttpParams();
		params.put("ordercode", ordercode);
		params.put("updatetime", getStatusTime("完成"));
		System.out.println("---离线----" + getStatusTime("完成"));
		params.put("token", token);
		params.put("status", "4");
		OkHttpUtils.post(Config.TASK_PERFORM).params(params).tag(this).execute(new StringCallback() {
			@Override
			public void onResponse(boolean arg0, String arg1, Request arg2, Response arg3) {
				// 判断是否修改成功
				GsLoginOut data = new Gson().fromJson(arg1, GsLoginOut.class);
				if (data.getRet().equals("0")) {
					// 更新成功即删除数据库里面完成状态的任务
					for (GsTaskInfo gsTaskInfo : list_offline) {
						dao.deleteAData(gsTaskInfo.getOrdercode());
					}				
					// 清除缓存
					clearNotes();
					//显示即时的数据
					showView();
					//判断手动提交是否完成
					if (mApater!=null||mApater.getCount()!=0) {
						// 更新失败显示
						btn_null.setVisibility(View.VISIBLE);
						btn_commit.setVisibility(View.GONE);
					}else {
						//全部提交成功
						layout_commit.setVisibility(View.GONE);
					}
				} else if (data.getRet().equals("2000")) {

				} else if (data.getRet().equals("1001")) {

				} else if (data.getRet().equals("1002")) {

				} else if (data.getRet().equals("1003")) {

				} else if (data.getRet().equals("1004")) {
					againLogin();
				}
			}

			@Override
			public void onError(boolean isFromCache, Call call, Response response, Exception e) {
				super.onError(isFromCache, call, response, e);
				//判断提交是否成功
				if (mApater!=null||mApater.getCount()!=0) {
					// 更新失败显示
					btn_null.setVisibility(View.VISIBLE);
					btn_commit.setVisibility(View.GONE);
				}else {
					layout_commit.setVisibility(View.GONE);
				}
				//发送一个广播
				sendBoard(mApater.getCount()+"");
			}
		});

	}

	

	private void sendBoard(String num) {
		Intent intent = new Intent();
		intent.setAction("com.yd.cfssbusinessdemo.myReceiver");
		intent.putExtra("size", num);
		getActivity().sendBroadcast(intent);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("--off------onStop----");
	}

	private void showView() {
		//实时查询数据库,获取订单
		list_offline = dao.queryCompleteData();
		mApater.setList(list_offline);
		lv.setAdapter(mApater);
		if (mApater.getCount() == 0) {
			layout_null.setVisibility(View.VISIBLE);
			layout.setVisibility(View.GONE);
		} else {
			layout_null.setVisibility(View.GONE);
			layout.setVisibility(View.VISIBLE);
		}
		//发送广播
		sendBoard(mApater.getCount()+"");
	}

	private void print(String message) {
		Toast.makeText(getActivity(), message, 0).show();
	}

	@Override
	protected void initView() {
		layout_commit = (LinearLayout) view.findViewById(R.id.layout_commit);
		btn_null = (Button) view.findViewById(R.id.btn_anim);
		btn_commit = (ProgressBar) view.findViewById(R.id.progressBar2);
		lv = (ListView) view.findViewById(R.id.listView_task_offline);
		layout = (LinearLayout) view.findViewById(R.id.linear_offline);
		layout_null = (LinearLayout) view.findViewById(R.id.linear_offline_null);
		mApater = new MyTaskListApater(getActivity());

	}

	@Override
	protected void setOnListener() {
		// 点击提交离线
		layout_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_null.setVisibility(View.GONE);
				btn_commit.setVisibility(View.VISIBLE);
				new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						offlineAsk();					
					}
				}.sendEmptyMessageDelayed(0, 1000);

			}
		});
	}

	// 获取时间的方法
	private String getTime() {
		// 获取系统时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	// 根据订单号保存订单状态改变的时间
	private void savaTime(String key, String value) {
		shared.savaData(key, value);
	}

	// 根据订单号以及订单号状态取出时间
	private String getStatusTime(String key) {
		return shared.reStoreData(key);
	}

	// 清除
	private void clearNotes() {
		shared.clear();
	}

}
