package com.yd.cfssbusinessdemo.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import android.R.integer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.yd.cfssbusinessdemo.AlterPassActivity;
import com.yd.cfssbusinessdemo.R;
import com.yd.cfssbusinessdemo.TaskMessageActivity;
import com.yd.cfssbusinessdemo.adapter.MyVolumeTaskApater;
import com.yd.cfssbusinessdemo.config.Config;
import com.yd.cfssbusinessdemo.entity.GsLoginOut;
import com.yd.cfssbusinessdemo.entity.GsTaskInfo;
import com.yd.cfssbusinessdemo.service.MyService;
import com.yd.cfssbusinessdemo.util.LogUtil;
import com.yd.cfssbusinessdemo.util.SharedPreferencesID;
import com.yd.cfssbusinessdemo.util.ViewHolder;

/**
 * 
 * @ClassName: Fragment_volume_start
 * @Description: 执行批量出发
 * @author Yin_Juan
 * @date 2016年6月13日 下午2:15:48
 *
 */
public class Fragment_volume_start extends BaseFragment {

	private ListView lv;
	private MyVolumeTaskApater mApater;
	private List<GsTaskInfo> list; // 数据源
	private CheckBox rb; // 全选按钮
	private Button exe_btn; // 执行按钮
	private Button can_btn; // 取消按钮
	private List<GsTaskInfo> checkList; // 选中的数据源
	private LinearLayout layout;
	private LinearLayout layout_null; // 无数据显示的视图
	private SharedPreferencesID shared;// 用于缓存订单状态改变的时间
	private String startTime;// 批量出发的时间

	@Override
	protected int setLayoutResID() {

		return R.layout.fragment_volume_start;
	}

	@Override
	protected void initData() {
		checkList = new ArrayList<GsTaskInfo>();
		// 根据订单号是出发来获取数据
		list = dao.queryAppointAndGoData();

	}

	@Override
	protected void initView() {
		lv = (ListView) view.findViewById(R.id.listView_volume_start);
		rb = (CheckBox) view.findViewById(R.id.radioButton_all);
		exe_btn = (Button) view.findViewById(R.id.button_volume_execute);
		can_btn = (Button) view.findViewById(R.id.button_volume_cancle);
		layout = (LinearLayout) view.findViewById(R.id.layout_listView);
		layout_null = (LinearLayout) view.findViewById(R.id.layout_listView_null);
		mApater = new MyVolumeTaskApater(getActivity(), list);
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
				// 获取实例
				ViewHolder holder = (ViewHolder) view.getTag();
				// 改变状态
				holder.checkBox.toggle();
				// 将状态记录下来
				MyVolumeTaskApater.getIsSelected().put(position, holder.checkBox.isChecked());
				if (holder.checkBox.isChecked()) {
					checkList.add(list.get(position));
					System.out.println("----单个点击增加-----" + checkList.size());
				} else {
					checkList.remove(list.get(position));
					System.out.println("----单个点击取消-----" + checkList.size());
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
						MyVolumeTaskApater.getIsSelected().put(i, true);
					}
					checkList.clear();
					checkList.addAll(list);
					mApater.notifyDataSetChanged();
				} else {
					for (int i = 0; i < list.size(); i++) {
						MyVolumeTaskApater.getIsSelected().put(i, false);
						rb.setChecked(false);
					}
					checkList.clear();
					mApater.notifyDataSetChanged();
				}

			}
		});
		exe_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
						.setMessage("是否批量执行 " + checkList.size() + "笔业务？")
						.setPositiveButton("确认", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// 对数据库操作
								if (checkList.size() != 0) {									
									if (dao.batchUpdateStatus(checkList)) {
										// 发送定位上传广播
										StringBuffer sb = new StringBuffer();
										for (int i = 0; i < checkList.size(); i++) {
											sb.append(checkList.get(i).getOrdercode());
											sb.append("&");
										}
										Intent intent = new Intent();
										intent.setAction("com.yd.cfssbusinessdemo.dingweiservice");
										intent.putExtra("code", sb.toString());
										getActivity().sendBroadcast(intent);
										// 缓存订单状态改变的时间
										for (GsTaskInfo gsTaskInfo : checkList) {
											shared = new SharedPreferencesID(getActivity(), gsTaskInfo.getOrdercode()); // 分别得到不同订单的时间
											savaTime("出发", getTime());
										} // 通知适配器刷新
										mApater.notifyDataSetChanged(); // 通知后台刷新
									
										alterWebData();

									} else {

									}
								}

							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						});
				builder.create().show();

			}
		});
		can_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();

			}
		});

	}

	protected void alterWebData() {
		// 获取token
		String token = share.reStoreData("token");
		// 获取订单号
		StringBuffer ordercode = new StringBuffer();
		for (GsTaskInfo gsTaskInfo : checkList) {
			ordercode.append(gsTaskInfo.getOrdercode());
			ordercode.append("&");
		}
		System.out.println("-----147-----" + ordercode.toString());
		HttpParams params = new HttpParams();
		params.put("token", token);
		params.put("ordercode", ordercode.toString());
		params.put("updatetime", getStatusTime("出发"));
		System.out.println("------出发----" + getStatusTime("出发"));
		params.put("status", "1");
		OkHttpUtils.post(Config.TASK_START).params(params).execute(new StringCallback() {

			@Override
			public void onResponse(boolean arg0, String arg1, Request arg2, Response arg3) {
				GsLoginOut data = new Gson().fromJson(arg1, GsLoginOut.class);
				// 判断请求是否成功
				if (data.getRet().equals("0")) {
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

	// 根据订单号以及订单号状态保存订单状态改变的时间
	private void savaTime(String key, String value) {
		shared.savaData(key, value);
	}

	// 根据订单号以及订单号状态取出时间
	private String getStatusTime(String key) {
		return shared.reStoreData(key);
	}

}
