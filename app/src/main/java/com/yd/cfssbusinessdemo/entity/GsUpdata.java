package com.yd.cfssbusinessdemo.entity;

import android.R.integer;

public class GsUpdata {

	private int ret;
	private String msg;
	private UpdataInfo data;
	
	public GsUpdata() {
		super();
	}

	public GsUpdata(int ret, String msg, UpdataInfo data) {
		super();
		this.ret = ret;
		this.msg = msg;
		this.data = data;
	}

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public UpdataInfo getData() {
		return data;
	}

	public void setData(UpdataInfo data) {
		this.data = data;
	}
	
	
}
