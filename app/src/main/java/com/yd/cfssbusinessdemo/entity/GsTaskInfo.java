package com.yd.cfssbusinessdemo.entity;

import java.io.Serializable;

public class GsTaskInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/*	*//********* 单据号 **********/
	/*
	 * private String ordercode;
	 *//********* 客户姓名 **********/
	/*
	 * private String name;
	 *//********* 客户手机号码 **********/
	/*
	 * private String phone;
	 *//********* 客户地址 **********/
	/*
	 * private String address;
	 *//********* 备注 **********/
	/*
	 * private String remark;
	 *//********* 单据状态 **********/
	/*
	 * private String status;
	 *//********* 业务类型 **********/
	/*
	 * private String billtype;
	 *//********* 行属 **********/
	/*
	 * private String bankname;
	 *//********* 预约时间 **********/
	/*
	 * private String yuyuetime;
	 *//********* 预计到达时间 **********/
	/*
	 * private String yujitime;
	 *//********* 派单员联系方式 **********/
	/*
	 * private String paidanphone;
	 *//********* 时间段 **********/
	/*
	 * private String shijianduan;
	 *//********* 提示信息 **********/
	/*
	 * private String hint;//
	 *//********* 被搜索的关键词 **********/
	/*
	 * private String userInput;
	 *//********* 身份证号码 **********/
	/*
	 * private String identno;
	 */
	private String address;// 地址
	private String bankname;// 行属
	private String billtype;// 业务类型
	private String hint;// 提示信息
	private String identno;// 身份证号
	private String name;// 客户姓名
	private String ordercode;// 单据号
	private String paidanphone;// 派单员联系方式
	private String phone;// 客户电话
	private String remark;// 备注
	private String shijianduan;// 时间段
	private String status;// 单据状态
	private String userInput;//
	private String yujitime;// 预计到达时间
	private String yuyuetime;// 预约时间

	/********* 模糊查询 **********/
	public GsTaskInfo(String ordercode, String name, String address, String status, String billtype, String yuyuetime) {
		super();
		this.ordercode = ordercode;
		this.name = name;
		this.address = address;
		this.status = status;
		this.billtype = billtype;
		this.yuyuetime = yuyuetime;
	}

	/** 插入全部数据 **/
	public GsTaskInfo(String ordercode, String name, String phone, String address, String remark, String status,
			String billtype, String bankname, String yuyuetime, String yujitime, String paidanphone, String hint,
			String identno) {
		super();
		this.ordercode = ordercode;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.remark = remark;
		this.status = status;
		this.billtype = billtype;
		this.bankname = bankname;
		this.yuyuetime = yuyuetime;
		this.yujitime = yujitime;
		this.paidanphone = paidanphone;
		this.hint = hint;
		this.identno = identno;
	}
	

	public String getOrdercode() {
		return ordercode;
	}

	public GsTaskInfo(String address, String bankname, String billtype, String hint, String identno, String name,
			String ordercode, String paidanphone, String phone, String remark, String shijianduan, String status,
			String userInput, String yujitime, String yuyuetime) {
		super();
		this.address = address;
		this.bankname = bankname;
		this.billtype = billtype;
		this.hint = hint;
		this.identno = identno;
		this.name = name;
		this.ordercode = ordercode;
		this.paidanphone = paidanphone;
		this.phone = phone;
		this.remark = remark;
		this.shijianduan = shijianduan;
		this.status = status;
		this.userInput = userInput;
		this.yujitime = yujitime;
		this.yuyuetime = yuyuetime;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getYuyuetime() {
		return yuyuetime;
	}

	public void setYuyuetime(String yuyuetime) {
		this.yuyuetime = yuyuetime;
	}

	public String getYujitime() {
		return yujitime;
	}

	public void setYujitime(String yujitime) {
		this.yujitime = yujitime;
	}

	public String getPaidanphone() {
		return paidanphone;
	}

	public void setPaidanphone(String paidanphone) {
		this.paidanphone = paidanphone;
	}

	public String getShijianduan() {
		return shijianduan;
	}

	public void setShijianduan(String shijianduan) {
		this.shijianduan = shijianduan;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getUserInput() {
		return userInput;
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}

	public String getidentno() {
		return identno;
	}

	public void setidentno(String identno) {
		this.identno = identno;
	}

	@Override
	public String toString() {
		return "GsTaskInfo [ordercode=" + ordercode + ", name=" + name + ", phone=" + phone + ", address=" + address
				+ ", remark=" + remark + ", status=" + status + ", billtype=" + billtype + ", bankname=" + bankname
				+ ", yuyuetime=" + yuyuetime + ", yujitime=" + yujitime + ", paidanphone=" + paidanphone
				+ ", shijianduan=" + shijianduan + ", hint=" + hint + ", userInput=" + userInput + ", identno="
				+ identno + "]";
	}

}
