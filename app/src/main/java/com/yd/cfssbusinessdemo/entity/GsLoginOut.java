package com.yd.cfssbusinessdemo.entity;

public class GsLoginOut {

	private String ret;
	private String data;
	private String msg;
	
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public GsLoginOut(String ret, String data, String msg) {
		super();
		this.ret = ret;
		this.data = data;
		this.msg = msg;
	}
	public GsLoginOut(String ret, String msg) {
		super();
		this.ret = ret;
		this.msg = msg;
	}
	
	
	
	
}
