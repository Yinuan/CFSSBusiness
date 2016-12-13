package com.yd.cfssbusinessdemo.config;

public class Config {
	public static final String URL_T="http://192.168.1.114:2000/yyerp";
	public static final String URL="http://demo.yuendong.com:2000/yyerp";
	public static final String URL_CFSS="http://220.231.140.34:9889/yyerp";
	public static final String URL_CFSS_TEST="http://220.231.140.4:9887/yyerp";
	public static final String URL_A="/yd";
	
	
	public static final String URL_TEST=URL_CFSS_TEST+URL_A;
	/**登录地址**/
	public static final String LOGIN_URL=URL_TEST+"/logic/user/login.action";
	/**注销登录地址**/
	public static final String LOGIN_OUT=URL_TEST+"/logic/user/logout.action";
	/**获取任务订单地址**/
	public static final String TASK_LIST=URL_TEST+"/logic/order/listOrder.action";
	/**获取已完成任务订单地址**/
	public static final String TASK_PERFORM_COMFRIM=URL_TEST+"/logic/order/listOrder.action";
	/**出发**/
	public static final String TASK_START=URL_TEST+"/logic/order/sendStatus.action";
	/**到达**/
	public static final String TASK_ARRIVE=URL_TEST+"/logic/order/sendStatus.action";
	/**执行**/
	public static final String TASK_EXECUTE=URL_TEST+"/logic/order/sendStatus.action";
	/**完成**/
	public static final String TASK_PERFORM=URL_TEST+"/logic/order/sendStatus.action";
	/**取消**/
	public static final String TASK_CANCLE=URL_TEST+"/logic/order/sendStatus.action";
	/**密码修改**/
	public static final String PASS_ALTER=URL_TEST+"/logic/user/updatepassword.action";
	/**定位**/
	public static final String LOCATION_URL=URL_TEST+"/logic/position/dingwei.action";
	/**版本更新地址**/
	public static final String UPDATE_URL=URL_TEST+"/basic/version/show.action";
	
	
}
