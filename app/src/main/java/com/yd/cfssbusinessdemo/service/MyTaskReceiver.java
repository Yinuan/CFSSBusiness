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
/**
 * 
* @ClassName: MyTaskReceiver 
* @Description: 用来判断当前是哪个碎片
* @author Yin_Juan
* @date 2016年7月4日 下午4:54:11 
*
 */
public class MyTaskReceiver extends BroadcastReceiver{
	
	private Handler handler;
	

	public MyTaskReceiver(Handler handler) {
		super();
		this.handler = handler;
	}



	@Override
	public void onReceive(Context context, Intent intent) {
		//收到指定的广播时
		if (intent.getAction().equals("com.yd.cfssbusinessdemo.Receiver")) {
			if (intent.getStringExtra("position").equals("0")) {
				System.out.println("------得到了位置0----");
				handler.sendEmptyMessage(0);
			}else if (intent.getStringExtra("position").equals("1")) {
				System.out.println("------得到了位置1----");
				handler.sendEmptyMessage(1);
			}else if (intent.getStringExtra("position").equals("2")) {
				System.out.println("------得到了位置2----");
				handler.sendEmptyMessage(2);
			}
					
		}
		
	}

}
