package com.yd.cfssbusinessdemo;

import java.util.List;

import com.amap.api.services.core.br;
import com.yd.cfssbusinessdemo.fragment.Fragment_volume_arrive;
import com.yd.cfssbusinessdemo.fragment.Fragment_volume_start;
import com.yd.cfssbusinessdemo.util.PagerSlidingTabStrip;
import com.yd.cfssbusinessdemo.util.SystemBarTintManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VolumeExecuteActivity extends FragmentActivity implements OnClickListener{

	private Fragment_volume_start start; // 碎片出发
	private Fragment_volume_arrive arrive;// 碎片到达
	private String titles[]={"出发","到达"};
	private ViewPager viewPager;
	private PagerSlidingTabStrip ps_my_order_tab;
	private ImageButton i_back;		//返回按钮
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_volume_execute);
		//把活动装箱
		MyApp.getInstance().addActivity(this);
		//设置顶部状态栏
		setTab();     
		initView();
	
	}
	//设置标题栏
	private void setTab() {
		 // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 自定义颜色
        tintManager.setTintColor(Color.parseColor("#f5821e"));
		
	}
	private void initView() {	
		 //设置标题栏
		ps_my_order_tab = (PagerSlidingTabStrip) findViewById(R.id.volume_tab_strip);
		ps_my_order_tab.setDividerColorResource(R.color.transparent);// 分割线设置成透明，因为不需要分割线
		ps_my_order_tab.setIndicatorColorResource(R.color.mycfss);// 滑动条颜色
		ps_my_order_tab.setTextColorResource(R.color.graytext1);// tab未被选中时字体颜色
		ps_my_order_tab.setSelectedTextColorResource(R.color.mycfss);// tab选中时字体颜色
		ps_my_order_tab.setTextSize(14);// 设置字体大小为14sp
		//设置返回按钮
		i_back =(ImageButton) findViewById(R.id.imageView_task_volume);		
		i_back.setOnClickListener(this);
		// 设置
		viewPager = (ViewPager) findViewById(R.id.volume_viewPager);
		viewPager.setAdapter(new ProSortAdapter(getSupportFragmentManager(), titles));
		ps_my_order_tab.setViewPager(viewPager);
		
	}
	//设置标题栏4.4	
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_task_volume:
			finish();
			break;

		default:
			break;
		}
		
	}
	class ProSortAdapter extends FragmentPagerAdapter
	{
		String[] mTitles;

		public ProSortAdapter(FragmentManager fm, String[] titles)
		{
			super(fm);
			mTitles = titles;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return mTitles[position];
		}

		@Override
		public Fragment getItem(int position)
		{
			switch (position)
			{
			case 0:
				if (start == null)
				{
					start = new Fragment_volume_start();
				}
				return start;

			case 1:
				if (arrive == null)
				{
					arrive = new Fragment_volume_arrive();
				}
				return arrive;
	

			}
			return null;
		}

		@Override
		public int getCount()
		{
			return mTitles.length;
		}

	}
	
	
}
