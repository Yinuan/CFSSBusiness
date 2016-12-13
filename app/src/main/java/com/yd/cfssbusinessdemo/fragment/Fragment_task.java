package com.yd.cfssbusinessdemo.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yd.cfssbusinessdemo.ImageActivity;
import com.yd.cfssbusinessdemo.MyApp;
import com.yd.cfssbusinessdemo.R;
import com.yd.cfssbusinessdemo.util.PagerSlidingTabStrip;
import com.yd.cfssbusinessdemo.util.SharedPreferencesUnit;
import com.yd.cfssbusinessdemo.weight.CircleImageView;

/**
 * 指示器框架初始化
 * 
 * @author ouyonghua
 *
 */
public class Fragment_task extends BaseFragment {

	private PagerSlidingTabStrip ps_my_order_tab; // 导航栏
	private ViewPager viewPager;
	private String[] items = { "任务列表", "离线任务", "完成任务" };
	// 碎片
	private Fragment_task_list task_list; // 任务列表
	private Fragment_task_offline task_offline; // 离线任务
	private Fragment_task_perfrom task_perform; // 完成任务
	private CircleImageView iv_touxiang;// 头像

	@Override
	protected int setLayoutResID() {

		return R.layout.fragment_one;
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {

		ps_my_order_tab = (PagerSlidingTabStrip) view.findViewById(R.id.ps_my_order_tab);
		ps_my_order_tab.setDividerColorResource(R.color.transparent);// 分割线设置成透明，因为不需要分割线
		ps_my_order_tab.setIndicatorColorResource(R.color.mycfss);// 滑动条颜色
		ps_my_order_tab.setTextColorResource(R.color.graytext1);// tab未被选中时字体颜色
		ps_my_order_tab.setSelectedTextColorResource(R.color.mycfss);// tab选中时字体颜色
		ps_my_order_tab.setTextSize(14);// 设置字体大小为14sp
		// 设置viewPager
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		viewPager.setAdapter(new ProSortAdapter(getChildFragmentManager(), items));
		ps_my_order_tab.setViewPager(viewPager);
		// 显示头像
		showTouXiang();

	}

	private void showTouXiang() {
		iv_touxiang = (CircleImageView) view.findViewById(R.id.iv_touxiang);
		String imageUrl = share.reStoreData("headportraiturl");
		
		ImageLoader.getInstance().displayImage(imageUrl, iv_touxiang, MyApp.getInstance().setImageOptionsConfig());
	}

	@Override
	protected void setOnListener() {
		iv_touxiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ImageActivity.class);
				startActivity(intent);

			}
		});
	}

	private void sendBoard(String num) {
		Intent intent = new Intent();
		intent.setAction("com.yd.cfssbusinessdemo.Receiver");
		intent.putExtra("position", num);
		getActivity().sendBroadcast(intent);
	}

	/**
	 * 类 描 述:viewpager适配器 类 名 称:ProSortAdapter 所属包名 :com.yd.fragment.main 创 建
	 * 人:pchp 创建时间 :2016年4月27日 下午5:47:53
	 */
	class ProSortAdapter extends FragmentPagerAdapter {
		String[] mTitles;

		public ProSortAdapter(FragmentManager fm, String[] items) {
			super(fm);
			mTitles = items;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTitles[position];
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (task_list == null) {
					task_list = new Fragment_task_list();
				}
				return task_list;

			case 1:
				if (task_offline == null) {
					task_offline = new Fragment_task_offline();
				}
				return task_offline;
			case 2:
				if (task_perform == null) {
					task_perform = new Fragment_task_perfrom();
				}
				return task_perform;

			}
			return null;
		}

		@Override
		public int getCount() {
			return mTitles.length;
		}

	}

}
