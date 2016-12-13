package com.yd.cfssbusinessdemo.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MyReceiver extends BroadcastReceiver {

	private Handler handler;
	private Message msg1;


	public MyReceiver(Handler handler) {
		super();
		this.handler = handler;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// 收到指定的广播时
		if (intent.getAction().equals("com.yd.cfssbusinessdemo.myReceiver")) {
			msg1 = new Message();
			msg1.what = 0;
			Bundle bundle = new Bundle();
			bundle.putString("data", intent.getStringExtra("size"));
			msg1.setData(bundle);
			handler.sendMessage(msg1);

		}
	}

}
