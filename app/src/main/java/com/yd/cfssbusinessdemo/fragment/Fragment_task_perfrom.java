package com.yd.cfssbusinessdemo.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Decoder.BASE64Decoder;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.yd.cfssbusinessdemo.R;
import com.yd.cfssbusinessdemo.adapter.MyTaskListApater;
import com.yd.cfssbusinessdemo.config.Config;
import com.yd.cfssbusinessdemo.entity.GsTaskInfo;
import com.yd.cfssbusinessdemo.entity.GsTaskInfoList;
import com.yd.cfssbusinessdemo.entity.GsTaskInfoResult;
import com.yd.cfssbusinessdemo.service.MyTaskReceiver;
import com.yd.cfssbusinessdemo.util.LogUtil;
import com.yd.cfssbusinessdemo.util.PullToRefreshLayout;
import com.yd.cfssbusinessdemo.util.PullToRefreshLayout.OnRefreshListener;
import com.yd.cfssbusinessdemo.util.PullableListView;
import com.yd.cfssbusinessdemo.util.PullableListView.OnLoadListener;
import com.yd.cfssbusinessdemo.util.SignTool;

public class Fragment_task_perfrom extends BaseFragment implements OnLoadListener {

	protected static final String TAG = Fragment_task_perfrom.class.getSimpleName();
	private PullableListView lv;
	private PullToRefreshLayout refreshLayout; // 下拉刷新
	private MyTaskListApater mApater;
	private ArrayList<GsTaskInfo> list;
	private LinearLayout layout; // 正常的视图
	private LinearLayout layout_null; // 没数据的视图
	private LinearLayout layout_loading;// 加载数据的视图
	private boolean b = false;// 判断网络连接是否正常
	private TextView tv_qipao;// 气泡数值
	private FrameLayout frameLayout_qipao;// 气泡帧布局

	@Override
	protected int setLayoutResID() {
		return R.layout.fragment_task_perform;
	}

	@Override
	protected void initData() {

	}

	private String arry;// 订单数据json
	private String sign;

	private void connWeb() {
		// 获取token
		String token = share.reStoreData("token");
		HttpParams params = new HttpParams();
		params.put("token", token);
		params.put("flag", "1");
		OkHttpUtils.post(Config.TASK_PERFORM_COMFRIM).tag(this).params(params).execute(new StringCallback() {
			@Override
			public void onResponse(boolean arg0, String arg1, Request arg2, Response arg3) {
				// 获取任务数据
				// System.out.println("------完成arg1-"+arg1);
				try {
					JSONObject obj = new JSONObject(arg1);
					arry = obj.optString("data");
					sign = obj.optString("ret");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				LogUtil.i(TAG, "--获取订单返回码："+sign);
				// 解密
				byte[] dec = null;
				String result = null;
				GsTaskInfoList result1 = null;
				if (arry != null) {
					BASE64Decoder decoder = new BASE64Decoder();
					// 拼接json数据
					JSONObject jsonObject = null;
					try {
						dec = decoder.decodeBuffer(arry);
						result = new String(dec, "utf-8");
						JSONArray jsonArray = new JSONArray(result);
						jsonObject = new JSONObject();
						jsonObject.put("data", jsonArray);
						result1 = new Gson().fromJson(jsonObject.toString(), GsTaskInfoList.class);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// 获取任务数据
				if (sign.equals("0")) {
					// 获取sign
					// String sign =result.getSign();
					b = true;
					if (result1 !=null) {
						// 获取数据显示
						list = result1.getData();
						mApater.clear();
						for (int i = 0; i < list.size(); i++) {
							mApater.addItem(list.get(i));
						}
						lv.setAdapter(mApater);
					}
					// 判断显示视图
					setView();
				} else if (sign.equals("2000")) {
					// 系统返回2000，报系统异常
					Toast.makeText(getActivity(), "系统异常", Toast.LENGTH_SHORT).show();
					layout_loading.setVisibility(View.GONE);
				} else if (sign.equals("1001")) {
					// token失效
					againLogin();
				}else if (sign.equals("") || sign == null) {
					// 如果后台忘记给返回码了
					Toast.makeText(getActivity(), "服务器异常，无返回码，请联系后台", Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onError(boolean isFromCache, Call call, Response response, Exception e) {
				super.onError(isFromCache, call, response, e);
				b = false;
				// 判断显示视图
				setView();
				System.out.println("----完成失败--");
			}
		});

	}

	// 判断显示视图
	private void setView() {
		// 进度条隐藏
		layout_loading.setVisibility(View.GONE);
		// 发送广播
		sendBoard(mApater.getCount() + "");
		if (mApater.getCount() == 0) {
			layout_null.setVisibility(View.VISIBLE);
			layout.setVisibility(View.GONE);
		} else {
			layout_null.setVisibility(View.GONE);
			layout.setVisibility(View.VISIBLE);

		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// 延迟一秒连接网络
			delayConnWeb();
		}
	}

	private void sendBoard(String num) {
		Intent intent = new Intent();
		intent.setAction("com.yd.cfssbusinessdemo.myReceiver");
		intent.putExtra("size", num);
		getActivity().sendBroadcast(intent);

	}

	@Override
	protected void initView() {

		refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view_perform);
		lv = (PullableListView) view.findViewById(R.id.listView_task_perform);
		layout = (LinearLayout) view.findViewById(R.id.linear_perform);

		frameLayout_qipao = (FrameLayout) view.findViewById(R.id.frameLayout_qipao);
		layout_null = (LinearLayout) view.findViewById(R.id.linear_perform_null);
		layout_loading = (LinearLayout) view.findViewById(R.id.linear_perform_progress);
		mApater = new MyTaskListApater(getActivity());

	}

	private void delayConnWeb() {
		new Handler() {
			@Override
			public void handleMessage(Message msg) {
				connWeb();
				super.handleMessage(msg);
			}
		}.sendEmptyMessageDelayed(0, 200);

	}

	@Override
	protected void setOnListener() {
		// 下拉刷新
		refreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
				new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						super.handleMessage(msg);
						// 请求网络
						connWeb();
						if (b) {
							// 刷新成功
							pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
						} else {
							pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
						}
					}
				}.sendEmptyMessageDelayed(0, 3000);

			}
		});
	}

	// 上拉加载的方法
	@Override
	public void onLoad(PullableListView pullableListView) {

		pullableListView.finishLoading();
		if (mApater.getCount() > list.size()) {
			// 上拉加载结束
			lv.setHasMoreData(false);
		}

	}

}
