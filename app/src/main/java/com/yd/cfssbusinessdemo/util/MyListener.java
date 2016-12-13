package com.yd.cfssbusinessdemo.util;



import com.yd.cfssbusinessdemo.util.PullToRefreshLayout.OnRefreshListener;

import android.os.Handler;
import android.os.Message;



public class MyListener implements OnRefreshListener
{

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout)
	{
		// ����ˢ�²���
		new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				// ǧ������˸��߿ؼ�ˢ�������Ŷ��
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 1000);
	}

}
