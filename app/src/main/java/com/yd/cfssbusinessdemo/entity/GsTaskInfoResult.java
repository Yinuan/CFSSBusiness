package com.yd.cfssbusinessdemo.entity;

import java.util.ArrayList;
import java.util.List;



public class GsTaskInfoResult {

	private ArrayList<GsTaskInfo> data;	//订单数据
	private int ret;	//返回参数代码
	private int total;	//返回订单的数量
	private String msg;	//返回的消息提示
	private String sign;//返回的加密参数
	public GsTaskInfoResult(ArrayList<GsTaskInfo> data, int ret, int total, String msg, String sign) {
		super();
		this.data = data;
		this.ret = ret;
		this.total = total;
		this.msg = msg;
		this.sign = sign;
	}
	public ArrayList<GsTaskInfo> getData() {
		return data;
	}
	public void setData(ArrayList<GsTaskInfo> data) {
		this.data = data;
	}
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

	


	/*static class GsTaskInfo{
		private String ordercode;		//订单
		private String name;			//客户名字
		private String phone;			//客户电话
		private String address;			//地址	
		private String remark;			//备注
		private String status;			//订单状态
		private String billtype;		//业务类型
		private String bankname;		//行属
		private String yuyuetime;		//预约时间
		private String yujitime;		//预计到达时间
		private String paidanphone;		//派单员联系方式
		public GsTaskInfo(String ordercode, String name, String phone,
				String address, String remark, String status, String billtype,
				String bankname, String yuyuetime, String yujitime,
				String paidanphone) {
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
		}
		public GsTaskInfo(String ordercode, String name, String address,
				String status, String billtype, String yuyuetime) {
			super();
			this.ordercode = ordercode;
			this.name = name;
			this.address = address;
			this.status = status;
			this.billtype = billtype;
			this.yuyuetime = yuyuetime;
		}
		public String getOrdercode() {
			return ordercode;
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
		
		
		
	}*/
	
	
}
