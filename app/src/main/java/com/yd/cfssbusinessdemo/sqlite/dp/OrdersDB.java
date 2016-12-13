package com.yd.cfssbusinessdemo.sqlite.dp;

/**
 * 存放与黑名单数据库相关的常量
 * 
 * @author 佚名
 */
public interface OrdersDB {
	String DATABASE_NAME = "orders.db"; // 数据库名
	int VERSION = 1;// 版本
	// 针对t_orders表的常量

	public interface ordersList {
		/************ 表名 *****************/
		String TABLE_NAME = "t_orders";
		/************ 主键 *****************/
		String COLUMN_ID = "_id";
		/************ 订单号 *****************/
		String COLUMN_NUM = "num";
		/************ 订单状态 *****************/
		String COLUMN_STATE = "state";
		/************ 客户姓名 *****************/
		String COLUMN_CNAME = "cname";
		/************ 客户电话 *****************/
		String COLUMN_CPHONE = "cphone";
		/************ 客户类型 *****************/
		String COLUMN_CTYPE = "ctype";
		/************ 地址 *****************/
		String COLUMN_ADDRESS = "address";
		/************ 行属 *****************/
		String COLUMN_BANK = "bank";
		/************ 时间 *****************/
		String COLUMN_TIME = "time";
		/************ 预计到达时间 ************/
		String COLUMN_TOA = "toa";
		/************ 派单员联系方式 *****************/
		String COLUMN_MPHONE = "mphone";
		/************ 提示信息 *****************/
		String COLUMN_HINT = "hint";
		/************ 备注 *****************/
		String COLUMN_REMARK = "remark";
		/************ 身份证号码 *****************/
		String COLUMN_IDCARD = "idCard";

		// 创建表的sql语句
		String SQL_CREATE_TABLE = "create table " + TABLE_NAME + " (" + " " + COLUMN_ID
				+ " integer primary key autoincrement, " + " " + COLUMN_NUM + " integer, " + // 订单号
				" " + COLUMN_STATE + " varchar(20), " + // 订单状态
				" " + COLUMN_CNAME + " varchar(20), " + // 客户姓名
				" " + COLUMN_IDCARD + " varchar(18), " + // 身份证号码
				" " + COLUMN_CPHONE + " varchar(20), " + // 客户电话
				" " + COLUMN_CTYPE + " varchar(20), " + // 客户类型
				" " + COLUMN_ADDRESS + " varchar(100), " + // 地址
				" " + COLUMN_BANK + " varchar(20), " + // 行属
				" " + COLUMN_TIME + " varchar(20), " + // 时间
				" " + COLUMN_TOA + " varchar(20), " + // 预计到达时间
				" " + COLUMN_MPHONE + " varchar(100), " + // 派单员联系方式
				" " + COLUMN_HINT + " varchar(100), " + // 提示信息
				" " + COLUMN_REMARK + " text);"; // 备注

	}

}
