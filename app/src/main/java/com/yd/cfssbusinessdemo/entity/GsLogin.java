package com.yd.cfssbusinessdemo.entity;

public class GsLogin {

	private String token;
	private String msg;
	private String ret;
	private LoginUser data;

	public GsLogin(String token, String msg, String ret, LoginUser data) {
		super();
		this.token = token;
		this.msg = msg;
		this.ret = ret;
		this.data = data;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public LoginUser getData() {
		return data;
	}

	public void setData(LoginUser data) {
		this.data = data;
	}

	public class LoginUser {
		private String name;
		private String telephone;
		private String idcard;
		private String sex;
		private String headportraiturl;
		
		public LoginUser(String name, String telephone, String idcard, String sex, String headportraiturl) {
			super();
			this.name = name;
			this.telephone = telephone;
			this.idcard = idcard;
			this.sex = sex;
			this.headportraiturl = headportraiturl;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

		public String getIdcard() {
			return idcard;
		}

		public void setIdcard(String idcard) {
			this.idcard = idcard;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public String getHeadportraiturl() {
			return headportraiturl;
		}

		public void setHeadportraiturl(String headportraiturl) {
			this.headportraiturl = headportraiturl;
		}

		
	}
}
