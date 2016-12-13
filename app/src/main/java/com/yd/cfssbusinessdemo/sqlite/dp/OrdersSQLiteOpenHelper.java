package com.yd.cfssbusinessdemo.sqlite.dp;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabase.CursorFactory;
import net.sqlcipher.database.SQLiteOpenHelper;
/**
 * 写一个类继承SQLiteOpenHelper 数据库创建辅助类
 */
public class OrdersSQLiteOpenHelper extends SQLiteOpenHelper {

	public OrdersSQLiteOpenHelper(Context context) {
		this(context, "orders.db", null, 1);
	}

	/**
	 * 创建一个数据库辅助类, 去 创建/打开/管理 数据库
	 * 
	 * @param context
	 *            上下文, 用于打开或创建数据库
	 * @param name
	 *            数据库的名称
	 * @param factory
	 *            CursorFactory 游标工厂, 如果我们想自定义结果集, 重写游标工厂. Cursor 游标(指针),
	 *            本身并不存储数据. 保存数据库的引用.
	 * @param version
	 *            数据库版本 必须 >=1
	 */
	public OrdersSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * 数据库第一次被创建时, 调用 适合做一些表的初始化操作
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("OrdersSQLiteOpenHelper: onCreate");

		// 执行创建表操作
		db.execSQL(OrdersDB.ordersList.SQL_CREATE_TABLE);
	}

	/**
	 * 数据库需要更新的时候, 调用此方法 执行表的更新操作.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out
				.println("PersonSQLiteOpenHelper: onUpgrade: oldVersion: " + oldVersion + " newVersion: " + newVersion);

		// if(oldVersion == 2 && newVersion == 3){
		// db.execSQL("alter table person add column salary integer;");
		// }
	}

	/**
	 * 删除数据库
	 * 
	 * @param context
	 * @return true 删除成功
	 */
	public boolean deleteDatabase(Context context) {
		return context.deleteDatabase(OrdersDB.DATABASE_NAME);
	}

}
