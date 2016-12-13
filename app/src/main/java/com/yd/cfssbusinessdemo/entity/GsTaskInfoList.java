package com.yd.cfssbusinessdemo.entity;

import java.util.ArrayList;

public class GsTaskInfoList {

	private ArrayList<GsTaskInfo> data;	//订单数据
	
	

	public GsTaskInfoList(ArrayList<GsTaskInfo> data) {
		super();
		this.data = data;
	}

	public ArrayList<GsTaskInfo> getData() {
		return data;
	}

	public void setData(ArrayList<GsTaskInfo> data) {
		this.data = data;
	}
	
	
}
