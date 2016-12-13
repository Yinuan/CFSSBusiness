package com.yd.cfssbusinessdemo.fragment;

import com.lzy.okhttputils.OkHttpUtils;
import com.yd.cfssbusinessdemo.LoginActivity;
import com.yd.cfssbusinessdemo.MyApp;
import com.yd.cfssbusinessdemo.R;
import com.yd.cfssbusinessdemo.sqlite.dp.OrdersDAO;
import com.yd.cfssbusinessdemo.util.SharedPreferencesUnit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * 
 * 类 描 述:碎片基类 类 名 称:BaseFragment 所属包名 :com.yd.fragment.main 创 建 人:pchp 创建时间
 * :2016年4月18日 下午6:25:18
 */
public abstract class BaseFragment extends Fragment {
	protected FrameLayout fl_baseFragment_bottom;// 帧布局控件

	RelativeLayout rl_baseFragment_title;// 相对布局控件

	protected View view;// View控件
	protected SharedPreferencesUnit share;// 文件缓存
	protected OrdersDAO dao;// sqlite数据库

	public Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_base, null); // 找到控件fragment的空地方
		context = getActivity();
		fl_baseFragment_bottom = (FrameLayout) view.findViewById(R.id.fl_baseFragment_bottom);

		inflater.inflate(setLayoutResID(), fl_baseFragment_bottom, true);// 添加一个布局进来
		// 缓存文件管理
		share = SharedPreferencesUnit.instance(getActivity());
		// sqlite数据库
		dao = new OrdersDAO(getActivity());
		initData();

		initView();

		setOnListener();

		return view;
	}

	/** 设置布局资源的视图控件ID抽象方法(当前Fragment绑定的布局),继承它的子类去实现 **/
	protected abstract int setLayoutResID();

	/** Fragment初始化视图的数据的抽象方法,继承它的子类去实现 **/
	protected abstract void initData();

	/** Fragment初始化视图的抽象方法,继承它的子类去实现 **/
	protected abstract void initView();

	/** 设置监听方法,继承它的子类去实现 **/
	protected abstract void setOnListener();

	@Override
	public void onDestroy() {

		super.onDestroy();
		OkHttpUtils.getInstance().cancelTag(this);
	}

	protected void againLogin() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("你的账号已失效，请重新登录")
				.setCancelable(false).setPositiveButton("重新登录", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 请出用户信息，回到登录界面
						share.clear();
						Intent intent = new Intent(getActivity(), LoginActivity.class);
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
}