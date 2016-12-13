package com.yd.cfssbusinessdemo.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.yd.cfssbusinessdemo.R;
import com.yd.cfssbusinessdemo.TaskMessageActivity;
import com.yd.cfssbusinessdemo.adapter.MyTaskListApater;
import com.yd.cfssbusinessdemo.config.Config;
import com.yd.cfssbusinessdemo.entity.GsTaskInfo;
import com.yd.cfssbusinessdemo.entity.GsTaskInfoList;
import com.yd.cfssbusinessdemo.util.LogUtil;
import com.yd.cfssbusinessdemo.util.PullToRefreshLayout;
import com.yd.cfssbusinessdemo.util.PullToRefreshLayout.OnRefreshListener;
import com.yd.cfssbusinessdemo.util.PullableListView;
import com.yd.cfssbusinessdemo.util.PullableListView.OnLoadListener;

import Decoder.BASE64Decoder;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 任务列表
 * 
 * @author ouyonghua
 *
 */
public class Fragment_task_list extends BaseFragment{

	protected static final String TAG = Fragment_task_list.class.getSimpleName();
	private PullableListView lv; // 下拉上载的listView
	private PullToRefreshLayout refreshLayout;// 下拉刷新的视图
	private MyTaskListApater mApater;
	private List<GsTaskInfo> list; // 从数据库获取展示的数据
	private EditText et_search; // 搜索输入框
	private Button btn_search_all;// 所有订单 按钮
	private Button button_search_today; // 当天订单 按钮
	private Button btn_search_date; // 日期搜索 按钮
	private List<GsTaskInfo> arrayList;// 从数据库获取与服务器做比较的数据
	private InputMethodManager inputMethodManager; // 控制输入框显示隐藏的代码
	private List<GsTaskInfo> searchs; // 搜索框结果集合
	private String message;// 搜索框内容
	private List<GsTaskInfo> data;// 网络下载的数据
	private LinearLayout layout_loading; // 加载网络数据时的进度条
	private LinearLayout layout_loading_good;// 加载网络完毕后
	private LinearLayout layout_loading_null;// 无订单的显示
	private boolean b = false;// 判断网络请求是否成功！
	private boolean isTag = false;// 判断是否处在用户可见的界面

	@Override
	protected int setLayoutResID() {
		return R.layout.fragment_task_list;
	}

	@Override
	protected void initData() {
		System.out.println("--Tasklist------create----");
		data = new ArrayList<GsTaskInfo>();
		// 连接网络

	}

	private String arry;// 获取的数据源
	private String code;// 获取的返回代码

	private void connWeb() {

		// 获取token
		String token = share.reStoreData("token");
		HttpParams params = new HttpParams();
		params.put("token", token);
		LogUtil.i(TAG, "post提交的参数是：" + params.toString());
		OkHttpUtils.post(Config.TASK_LIST).tag(this).params(params).execute(new StringCallback() {
			@Override
			public void onResponse(boolean arg0, String arg1, Request arg2, Response arg3) {
				// 获取返回码和返回的加密数据
				try {
					JSONObject obj = new JSONObject(arg1);
					arry = obj.optString("data");
					code = obj.optString("ret");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				LogUtil.i(TAG, "--获取订单返回码：" + code);
				// 对加密数据进行解析
				byte[] dec = null;
				String result = null;
				GsTaskInfoList result1 = null;
				if (arry != null) {
					BASE64Decoder decoder = new BASE64Decoder();
					JSONObject jsonObject = null;
					try {
						// 得到解密后的字符串
						dec = decoder.decodeBuffer(arry);
						result = new String(dec, "utf-8");
						JSONArray jsonArray = new JSONArray(result);
						// 将解密后的字符串转成json
						jsonObject = new JSONObject();
						jsonObject.put("data", jsonArray);
						result1 = new Gson().fromJson(jsonObject.toString(), GsTaskInfoList.class);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				if (code.equals("0")) {
					// 请求成功！
					b = true;
					// 网络获取的数据源
					if (result1 !=null) {
						data = result1.getData();
						// 从数据库获取数据
						arrayList = dao.queryAllData();
						// 判断数据库是否存在离线任务,有就清掉相应的网络订单
						// offlineTask();
						// 比较，数据是否刷新，刷新了即存储，没有不处理
						if (null == arrayList || arrayList.size() == 0) {
							// 数据为空，直接使用服务器数据
							dao.batchAddBean(result1.getData());
						} else {
							// 服务器与数据库同步的操作
							getUncontain(data, arrayList);
						}
					}else {
						//数据库有数据时，清空数据库						
						arrayList = dao.queryAllData();
						if (arrayList !=null) {	
							List<String> list3 = new ArrayList<String>(); // 数据库订单号的集合
							for (int j = 0; j < arrayList.size(); j++) {
								list3.add(arrayList.get(j).getOrdercode());
							}
							for (int i = 0; i < list3.size(); i++) {
								dao.deleteAData(list3.get(i));
							}
						}
					}
					showData();
				} else if (code.equals("2000")) {
					// 系统异常 从数据库获取数据
					Toast.makeText(getActivity(), "系统异常", 0).show();
					// 从数据库拿数据
					showData();
				} else if (code.equals("1001")) {
					// token失效,重新登录
					againLogin();
				} else if (code.equals("") || code == null) {
					// 如果后台忘记给返回码了
					Toast.makeText(getActivity(), "服务器异常，无返回码，请联系后台", Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onError(boolean isFromCache, Call call, Response response, Exception e) {
				super.onError(isFromCache, call, response, e);
				b = false;
				// 网络连接失败从数据库获取数据展示
				System.out.println("------shibai-----");
				showData();
				LogUtil.e(TAG, "联网失败了！");
				LogUtil.e(TAG, "错误：isFromCache" + isFromCache);
				LogUtil.e(TAG, "错误：call" + call);
				LogUtil.e(TAG, "错误：response" + response);
				LogUtil.e(TAG, "错误：e" + e);
			}
		});

	}

	// 同步离线数据,去除掉数据库的离线任务
	protected void offlineTask() {
		List<GsTaskInfo> offline_data = dao.queryAllData();
		for (int i = 0; i < offline_data.size(); i++) {
			if (offline_data.get(i).getStatus().equals("离线")) {
				// 删除网络数据中该订单，用订单号删
				String order = offline_data.get(i).getOrdercode();
				for (int j = 0; j < data.size(); j++) {
					if (data.get(j).getOrdercode().equals(order)) {
						data.remove(j);
					}
				}

			}
		}

	}

	protected void getUncontain(List<GsTaskInfo> data, List<GsTaskInfo> arrayList) {
		// 对他们的订单号遍历
		List<String> list1 = new ArrayList<String>(); // 网络数据的订单号集合
		for (int i = 0; i < data.size(); i++) {
			// 网络订单的单号
			list1.add(data.get(i).getOrdercode());
		}
		List<String> list2 = new ArrayList<String>(); // 数据库订单号的集合
		for (int j = 0; j < arrayList.size(); j++) {
			list2.add(arrayList.get(j).getOrdercode());
		}
		// 对数据库操作
		for (String str2 : list2) {
			if (!list1.contains(str2)) {// 删除数据库中多余的订单
				dao.deleteAData(str2);
			}
		}
		/*
		 * for (String str1 : list1) { if (!list2.contains(str1)) {//
		 * 增加数据库中没有的订单 //dao.deleteAData(str1); //根据订单号得到对象
		 * 
		 * } }
		 */
		for (int i = 0; i < list1.size(); i++) {// 增加数据库中没有的订单
			if (!list2.contains(list1.get(i))) {
				dao.addBean(data.get(i));
			}
		}

	}

	@Override
	protected void initView() {
		layout_loading_good = (LinearLayout) view.findViewById(R.id.layout_loading_good);
		layout_loading = (LinearLayout) view.findViewById(R.id.layout_loading);
		layout_loading_null = (LinearLayout) view.findViewById(R.id.layout_loading_null);
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		lv = (PullableListView) view.findViewById(R.id.listView_task_list);
		et_search = (EditText) view.findViewById(R.id.editText_search);
		btn_search_all = (Button) view.findViewById(R.id.btn_search_all);
		button_search_today = (Button) view.findViewById(R.id.button_search_today);
		btn_search_date = (Button) view.findViewById(R.id.btn_search_date);
		mApater = new MyTaskListApater(getActivity());
		//lv.setOnLoadListener(this);
		//lv.setHasMoreData(false);
		layout_loading.setVisibility(View.VISIBLE);
		layout_loading_good.setVisibility(View.GONE);
	}

	// 延迟一秒加载网络数据
	private void deLayConnWeb() {
		new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 请求网络
				connWeb();
				super.handleMessage(msg);
			}
		}.sendEmptyMessageDelayed(0, 1000);

	}

	// 拉取数据显示
	private void showData() {
		list = dao.queryNoCompleteData();
		LogUtil.i(TAG, "拉取数据：" + list.toString());
		if (list != null && list.size() != 0) {
			mApater.clear();
			for (int i = 0; i < list.size(); i++) {
				mApater.addItem(list.get(i));
			}
			lv.setAdapter(mApater);
			layout_loading.setVisibility(View.GONE);
			layout_loading_good.setVisibility(View.VISIBLE);
			// 将没数据视图隐藏出来
			layout_loading_null.setVisibility(View.GONE);
			// 发送广播
			sendBoard(list.size() + "");
		} else {
			// 没数据时的显示
			mApater.clear();
			sendBoard(list.size() + "");
			layout_loading.setVisibility(View.GONE);
			// 将没数据视图显示出来
			layout_loading_null.setVisibility(View.VISIBLE);
		}
	}

	private void sendBoard(String num) {
		Intent intent = new Intent();
		intent.setAction("com.yd.cfssbusinessdemo.myReceiver");
		intent.putExtra("size", num);
		if(getActivity() !=null){
			getActivity().sendBroadcast(intent);
		}
		
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		LogUtil.i(TAG, "执行了setUserVisibleHint");
		// 执行了此操作，将标记变true
		isTag = true;
		// 延迟一秒加载网络数据
		if (isVisibleToUser) {
			deLayConnWeb();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// 判断标记
		if (!isTag) {
			deLayConnWeb();
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		isTag = false;
	}

	@Override
	protected void setOnListener() {
		// 点击进入详情页
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// 得到listView点击位置的对象
				GsTaskInfo info = list.get(position);
				Intent intent = new Intent(getActivity(), TaskMessageActivity.class);
				// 得到元素在数据库中的值，传过去
				Bundle bundle = new Bundle();
				bundle.putSerializable("info", info);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
		// 下拉刷新
		refreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
				new Handler() {
					@Override
					public void handleMessage(Message msg) {
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
		// 无数据显示 点击刷新事件
		layout_loading_null.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 切换到加载视图
				layout_loading_null.setVisibility(View.GONE);
				layout_loading.setVisibility(View.VISIBLE);
				// 请求网络
				deLayConnWeb();
			}
		});

		// 所有订单
		btn_search_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showData();
			}
		});

		// 搜索框自动查询
		et_search.addTextChangedListener(watcher);

		/******************* 日期搜索 ********************/
		btn_search_date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 日期搜索逻辑
				Calendar d = Calendar.getInstance(Locale.CHINA);
				// 创建一个日历引用d，通过静态方法getInstance() 从指定时区 Locale.CHINA 获得一个日期实例
				Date myDate = new Date();
				// 创建一个Date实例
				d.setTime(myDate);
				// 设置日历的时间，把一个新建Date实例myDate传入
				int year = d.get(Calendar.YEAR);
				int monthOfYear = d.get(Calendar.MONTH);
				int dayOfMonth = d.get(Calendar.DAY_OF_MONTH);
				// LogUtil.i(TAG, "初始化日期为：" + year + "年" + monthOfYear + "月" +
				// dayOfMonth + "日");

				// 选择日历监听器
				OnDateSetListener callBack = new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						++monthOfYear;// 老外的1月是0月，所以要+1
						String select_date = null;
						String month = null;
						String day = null;
						// 如果 月分<10,则把 9月拼成09月，
						if (monthOfYear < 10) {
							month = "0" + monthOfYear;
						} else {
							month = "" + monthOfYear;
						}
						// 如果 日<10,则把 9日拼成09日
						if (dayOfMonth < 10) {
							day = "0" + dayOfMonth;
						} else {
							day = "" + dayOfMonth;
						}

						// 拼接日期 2016-08-23
						select_date = new StringBuffer().append(year).append("-").append(month).append("-").append(day)
								.toString();

						LogUtil.i(TAG, "选择了日期为：" + select_date);
						list = dao.queryToDayData(select_date);
						LogUtil.i(TAG, "查询指定日期订单：" + list.toString());
						mApater.setList(list);
						// 发送广播
						sendBoard(list.size() + "");
					}
				};
				// 日历对话框，主题可更换：日历式的，带滚轮的，带+、-选择日期的
				DatePickerDialog dialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
						callBack, year, monthOfYear, dayOfMonth);
				dialog.setTitle("请选择需要查询的日期");
				dialog.setCancelable(true);
				dialog.show();
			}
		});

		// 当天订单 按钮
		button_search_today.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard();

				// 获取当前时间
				String today = getTimeStamp();
				// String today = "2016-08-22";
				LogUtil.i(TAG, "获取当前时间为：" + today);
				list = dao.queryToDayData(today);
				LogUtil.i(TAG, "查询当天订单：" + list.toString());
				mApater.setList(list);
				// 发送广播
				sendBoard(list.size() + "");
			}
		});
	}
	
	/*********** 监听文本内容 *************/
	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

			int length = s.length();
			// 搜索框内容发生改变则实时查询
			String likeText = String.valueOf(s);
			searchs = dao.LikeSearchDB(likeText);// 模糊查询得到的集合
			if (length != 0) {
				for (GsTaskInfo gsTaskInfo : searchs) {
					gsTaskInfo.setUserInput(likeText);
				}
				list = searchs;
				mApater.setList(list);
				// 发送广播
				sendBoard(list.size() + "");
			} else {
				list = searchs;
				mApater.setList(list);
				// 发送广播
				sendBoard(list.size() + "");
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	// 隐藏键盘
	void hideSoftKeyboard() {
		((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(et_search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/*// 上啦加载
	@Override
	public void onLoad(final PullableListView pullableListView) {
		new Handler() {
			@Override
			public void handleMessage(Message msg) {
				
				 * if (mApater.getCount() >= list.size()) { // 上拉加载结束
				 * lv.setHasMoreData(false); }
				 
				pullableListView.finishLoading();
			}
		}.sendEmptyMessageDelayed(0, 2000);
		lv.setHasMoreData(false);
	}*/

	// 废弃的MD5加密方法
	private void mdEncry() {
		/*
		 * try { JSONObject obj = new JSONObject(arg1); arry =
		 * obj.optString("data");
		 * 
		 * } catch (JSONException e1) { e1.printStackTrace(); } // 获取sign String
		 * sign = result1.getSign(); String signApp = ""; if (result1.getRet()
		 * == 0) { // 请求成功！ b = true; // 网络获取的数据源 data = result1.getData(); //
		 * 从数据库获取数据 arrayList = dao.queryAllData(); // 判断数据库是否存在离线任务,有就清掉相应的网络订单
		 * // offlineTask(); // 比较，数据是否刷新，刷新了即存储，没有不处理 if (null == arrayList ||
		 * arrayList.size() == 0) { // 数据为空，直接使用服务器数据
		 * dao.batchAddBean(result1.getData()); } else { // 服务器与数据库同步的操作
		 * getUncontain(data, arrayList); try { // 生成自己的sign Map<String, Object>
		 * map1 = new HashMap<String, Object>(); map1.put("data", arry);
		 * Map<String, Object> map2 = SignTool.paramFilter(map1); signApp =
		 * SignTool.md5App(SignTool.createLinkString(map2)); } catch (Exception
		 * e) { e.printStackTrace(); } // 判断数据是否完整 if (sign == null ||
		 * sign.length() <= 0) {
		 * 
		 * } else { if (sign.equals(signApp)) { // 从数据库获取数据 arrayList =
		 * dao.queryAllData(); // 判断数据库是否存在离线任务,有就清掉相应的网络订单 // offlineTask(); //
		 * 比较，数据是否刷新，刷新了即存储，没有不处理 if (null == arrayList || arrayList.size() ==
		 * 0) { // 数据为空，直接使用服务器数据 dao.batchAddBean(result1.getData()); } else {
		 * // 服务器与数据库同步的操作 getUncontain(data, arrayList); } } else { //
		 * 若果数据被改，怎么操作？ Toast.makeText(getActivity(), "数据不合法，请报告后台系统",
		 * 0).show(); } } }
		 */
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 2016-08-23
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimeStamp() {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return timeStamp;
	}
}
