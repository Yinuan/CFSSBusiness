package com.yd.cfssbusinessdemo.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.yd.cfssbusinessdemo.R;
import com.yd.cfssbusinessdemo.adapter.MyVolumeTaskApater;
import com.yd.cfssbusinessdemo.adapter.VolumeArriveApater;
import com.yd.cfssbusinessdemo.config.Config;
import com.yd.cfssbusinessdemo.entity.GsLoginOut;
import com.yd.cfssbusinessdemo.entity.GsTaskInfo;
import com.yd.cfssbusinessdemo.util.SharedPreferencesID;
import com.yd.cfssbusinessdemo.util.ViewHolder;

/**
 * 
 * @ClassName: Fragment_volume_arrive
 * @Description: 批量到达
 * @author Yin_Juan
 * @date 2016年6月13日 下午2:16:31
 *
 */
public class Fragment_volume_arrive extends BaseFragment {

	private List<GsTaskInfo> list; // 数据源
	private ListView lv;
	private VolumeArriveApater mApater;
	private CheckBox rb; // 全选按钮
	private Button exe_btn; // 执行按钮
	private Button can_btn;// 取消按钮
	private List<GsTaskInfo> check_list; // 选中的数据源
	private LinearLayout layout;
	private LinearLayout layout_null; // 无数据显示的视图
	private SharedPreferencesID shared;// 用于缓存订单状态改变的时间

	@Override
	protected int setLayoutResID() {

		return R.layout.fragment_volume_arrive;
	}

	@Override
	protected void initData() {
		list = dao.queryArriveData();
		check_list = new ArrayList<GsTaskInfo>();
	}

	@Override
	protected void initView() {
		layout = (LinearLayout) view.findViewById(R.id.layout_listView_arr);
		layout_null = (LinearLayout) view.findViewById(R.id.layout_listView_arr_null);
		lv = (ListView) view.findViewById(R.id.listView_volume_arrive);
		rb = (CheckBox) view.findViewById(R.id.radioButton_all_arrive);
		can_btn = (Button) view.findViewById(R.id.button_volume_cancle_arrvie);
		exe_btn = (Button) view.findViewById(R.id.button_volume_execute_arrvie);
		mApater = new VolumeArriveApater(getActivity(), list);
		lv.setAdapter(mApater);
		// 设置显示的视图
		setView();
	}

	private void setView() {
		if (list.size() == 0 || list == null) {
			layout_null.setVisibility(View.VISIBLE);
			layout.setVisibility(View.GONE);
		} else {
			layout.setVisibility(View.VISIBLE);
			layout_null.setVisibility(View.GONE);
		}

	}

	@Override
	protected void setOnListener() {
		// listView记录单个点击状态
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				/*
				 * Intent intent =new
				 * Intent(getActivity(),TaskMessageActivity.class);
				 * startActivity(intent);
				 */
				// 获取实例
				ViewHolder holder_arr = (ViewHolder) view.getTag();
				// 改变状态
				holder_arr.checkBox.toggle();
				// 将状态记录下来
				VolumeArriveApater.getIsSelected().put(position, holder_arr.checkBox.isChecked());
				// 放数据
				if (holder_arr.checkBox.isChecked()) {
					check_list.add(list.get(position));
				} else {
					check_list.remove(list.get(position));
				}
			}
		});
		// 全选按钮
		rb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (rb.isChecked()) {
					// 遍历
					for (int i = 0; i < list.size(); i++) {
						VolumeArriveApater.getIsSelected().put(i, true);
					}
					check_list.clear();
					check_list.addAll(list);
					mApater.notifyDataSetChanged();
				} else {
					for (int i = 0; i < list.size(); i++) {
						VolumeArriveApater.getIsSelected().put(i, false);
						rb.setChecked(false);
					}
					check_list.clear();
					mApater.notifyDataSetChanged();
				}

			}
		});
		can_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		exe_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
						.setMessage("是否批量执行" + check_list.size() + "笔业务？")
						.setPositiveButton("确认", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {					
								// 对数据库操作
								if (check_list.size() != 0) {
									if (dao.batchUpdateStatus(check_list)) {
										//发送定位上传广播
										StringBuffer sb =new StringBuffer();
										for (int i = 0; i < check_list.size(); i++) {
											sb.append(check_list.get(i).getOrdercode());
											sb.append("&");
										}
										Intent intent = new Intent();
										intent.setAction("com.yd.cfssbusinessdemo.dingweiservice");
										intent.putExtra("code", sb.toString());
										getActivity().sendBroadcast(intent);
										// 通知适配器刷新
										mApater.notifyDataSetChanged();
										// 缓存订单状态改变的时间
										for (GsTaskInfo gsTaskInfo : check_list) {
											shared = new SharedPreferencesID(getActivity(), gsTaskInfo.getOrdercode().toString());
											savaTime("到达", getTime());
										}
										alterWebData();
									} else {
										System.out.println("-----批量到达更新数据失败----");
									}

								}

							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub

							}
						});
				builder.create().show();

			}
		});

	}

	protected void alterWebData() {
		// 获取token
		String token = share.reStoreData("token");
		// 获取订单号
		StringBuffer ordercode = new StringBuffer();
		for (GsTaskInfo gsTaskInfo : check_list) {
			ordercode.append(gsTaskInfo.getOrdercode());
			ordercode.append("&");
		}
		HttpParams params = new HttpParams();
		params.put("token", token);
		params.put("ordercode", ordercode.toString());
		params.put("updatetime",getStatusTime("到达"));
		System.out.println("-----到达-------"+getStatusTime("到达"));
		params.put("status", "2");
		OkHttpUtils.post(Config.TASK_ARRIVE).params(params).execute(new StringCallback() {

			@Override
			public void onResponse(boolean arg0, String arg1, Request arg2, Response arg3) {
				GsLoginOut data = new Gson().fromJson(arg1, GsLoginOut.class);
				// 判断请求是否成功
				if (data.getRet().equals("0")) {
					System.out.println("操作成功！");
					// 离开界面
					getActivity().finish();
				} else if (data.getRet().equals("2000")) {
					print("msg:系统异常" + "code:2000");
				} else if (data.getRet().equals("1001")) {
					print("msg:单号或状态为空" + "code:1001");
				} else if (data.getRet().equals("1002")) {
					print("msg:有单据不存在" + "code:1002");
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
				System.out.println("网络连接失败");
				// 离开界面
				getActivity().finish();
			}
		});

	}

	private void print(String message) {
		Toast.makeText(getActivity(), message, 0).show();
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
}
