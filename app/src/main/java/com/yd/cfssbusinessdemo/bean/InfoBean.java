package com.yd.cfssbusinessdemo.bean;

import java.io.Serializable;

public class InfoBean implements Serializable{

	private String num;				//订单编号
	private String clientName;		//客户姓名
	private String clientPhone;		//客户电话
	private String bTybe;			//业务
	private String bank;			//行属
	private String time;			//时间
	private String address;			//地址	
	private String arriveTime;		//送达时间
	private String courierPhone;	//快递员电话
	private String remark;			//备注
	
	
	public InfoBean() {
		super();
	}

	public InfoBean(String num, String clientName, String bTybe, String time,
			String address) {
		super();
		this.num = num;
		this.clientName = clientName;
		this.bTybe = bTybe;
		this.time = time;
		this.address = address;
	}

	public InfoBean(String num, String clientName, String clientPhone,
			String bTybe, String bank, String time, String address,
			String arriveTime, String courierPhone, String remark) {
		super();
		this.num = num;
		this.clientName = clientName;
		this.clientPhone = clientPhone;
		this.bTybe = bTybe;
		this.bank = bank;
		this.time = time;
		this.address = address;
		this.arriveTime = arriveTime;
		this.courierPhone = courierPhone;
		this.remark = remark;
	}
	
	

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientPhone() {
		return clientPhone;
	}

	public void setClientPhone(String clientPhone) {
		this.clientPhone = clientPhone;
	}

	public String getbTybe() {
		return bTybe;
	}

	public void setbTybe(String bTybe) {
		this.bTybe = bTybe;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getCourierPhone() {
		return courierPhone;
	}

	public void setCourierPhone(String courierPhone) {
		this.courierPhone = courierPhone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "InfoBean [num=" + num + ", clientName=" + clientName
				+ ", clientPhone=" + clientPhone + ", bTybe=" + bTybe
				+ ", bank=" + bank + ", time=" + time + ", address=" + address
				+ ", arriveTime=" + arriveTime + ", courierPhone="
				+ courierPhone + ", remark=" + remark + "]";
	}
	
	
}
