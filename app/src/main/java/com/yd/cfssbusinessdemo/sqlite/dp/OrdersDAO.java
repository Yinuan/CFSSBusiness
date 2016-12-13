package com.yd.cfssbusinessdemo.sqlite.dp;

import java.util.ArrayList;
import java.util.List;

import com.yd.cfssbusinessdemo.entity.GsTaskInfo;
import com.yd.cfssbusinessdemo.util.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

/**
 * 数据库访问对象
 * 
 * @author poplar
 *
 */
public class OrdersDAO {

	private final Context context;
	private OrdersSQLiteOpenHelper helper;
	private long insert = 0;
	boolean succeed = false;
	private String key = "cfss";

	public OrdersDAO(Context context) {
		this.context = context;
		helper = new OrdersSQLiteOpenHelper(context);
		// 将SQLCipher所依赖的so库加载进来
		SQLiteDatabase.loadLibs(context);
	}

	/**
	 * 存储一条数据
	 * 
	 * @return
	 */
	public boolean addBean(GsTaskInfo infoBean) {
		// 可写数据库
		SQLiteDatabase db = helper.getWritableDatabase(key);

		ContentValues values = new ContentValues();
		values.put(OrdersDB.ordersList.COLUMN_NUM, infoBean.getOrdercode());
		values.put(OrdersDB.ordersList.COLUMN_STATE, infoBean.getStatus());
		values.put(OrdersDB.ordersList.COLUMN_CNAME, infoBean.getName());
		values.put(OrdersDB.ordersList.COLUMN_CPHONE, infoBean.getPhone());
		values.put(OrdersDB.ordersList.COLUMN_CTYPE, infoBean.getBilltype());
		values.put(OrdersDB.ordersList.COLUMN_ADDRESS, infoBean.getAddress());
		values.put(OrdersDB.ordersList.COLUMN_BANK, infoBean.getBankname());
		values.put(OrdersDB.ordersList.COLUMN_TIME, infoBean.getYuyuetime());
		values.put(OrdersDB.ordersList.COLUMN_TOA, infoBean.getYujitime());
		values.put(OrdersDB.ordersList.COLUMN_MPHONE, infoBean.getPaidanphone());
		values.put(OrdersDB.ordersList.COLUMN_REMARK, infoBean.getRemark());
		values.put(OrdersDB.ordersList.COLUMN_HINT, infoBean.getHint());
		values.put(OrdersDB.ordersList.COLUMN_IDCARD, infoBean.getidentno());

		insert = db.insert(OrdersDB.ordersList.TABLE_NAME, null, values);
		db.close();
		if (insert > 0) {
			succeed = true;
			return succeed;
		}
		return succeed;
	}

	/**
	 * 批量存储多条数据
	 * 
	 * @return
	 */
	public boolean batchAddBean(List<GsTaskInfo> infoBeans) {

		// 可写数据库
		SQLiteDatabase db = helper.getWritableDatabase(key);
		db.beginTransaction();// 开启事务
		for (GsTaskInfo infoBean : infoBeans) {
			System.out.println("遍历几次：" + insert);
			ContentValues values = new ContentValues();
			values.put(OrdersDB.ordersList.COLUMN_NUM, infoBean.getOrdercode());
			values.put(OrdersDB.ordersList.COLUMN_STATE, infoBean.getStatus());
			values.put(OrdersDB.ordersList.COLUMN_CNAME, infoBean.getName());
			values.put(OrdersDB.ordersList.COLUMN_CPHONE, infoBean.getPhone());
			values.put(OrdersDB.ordersList.COLUMN_CTYPE, infoBean.getBilltype());
			values.put(OrdersDB.ordersList.COLUMN_ADDRESS, infoBean.getAddress());
			values.put(OrdersDB.ordersList.COLUMN_BANK, infoBean.getBankname());
			values.put(OrdersDB.ordersList.COLUMN_TIME, infoBean.getYuyuetime());
			values.put(OrdersDB.ordersList.COLUMN_TOA, infoBean.getYujitime());
			values.put(OrdersDB.ordersList.COLUMN_MPHONE, infoBean.getPaidanphone());
			values.put(OrdersDB.ordersList.COLUMN_REMARK, infoBean.getRemark());
			values.put(OrdersDB.ordersList.COLUMN_HINT, infoBean.getHint());
			values.put(OrdersDB.ordersList.COLUMN_IDCARD, infoBean.getidentno());
			insert += db.insert(OrdersDB.ordersList.TABLE_NAME, null, values);
		}
		db.setTransactionSuccessful();// 提交事务
		db.endTransaction();// 结束事务
		db.close();

		if (insert > 0) {
			succeed = true;
			return succeed;
		}
		LogUtil.i(getClass().getName(), "insert批量添加了" + insert + "条数据");
		return succeed;
	}

	/**
	 * 删除SQLite数据库
	 * 
	 * @return
	 */
	public boolean deleteDB() {
		boolean deleteDatabase = helper.deleteDatabase(context);
		return deleteDatabase;
	}

	/**
	 * 根据订单号来更新订单状态
	 * 
	 * @param ordercode
	 *            订单号
	 * @param status
	 *            订单状态
	 * @return
	 */
	public boolean update(String ordercode, String status) {
		boolean succeed = false;
		SQLiteDatabase db = helper.getWritableDatabase(key);
		ContentValues values = new ContentValues();
		values.put("state", status);

		// 根据订单号，对订单状态进行数据更新

		int update = db.update(OrdersDB.ordersList.TABLE_NAME, // 表名
				values, // set条件
				"num=?", // where条件
				new String[] { ordercode });// where条件的值
		LogUtil.i(getClass().getName(), "更新了" + update + "条数据");
		db.close();
		if (update > 0) {
			succeed = true;
			return succeed;
		}
		return succeed;

	}

	/**
	 * 批量更改订单状态。批量更改出发/到达
	 * 
	 * @param infoBeans多条数据
	 * @return true批量更改成功
	 */
	public boolean batchUpdateStatus(List<GsTaskInfo> infoBeans) {
		boolean succeed = false;// 更改成功标识
		int update = 0;// 更新了几条数据
		SQLiteDatabase db = helper.getWritableDatabase(key);
		ContentValues values = new ContentValues();

		db.beginTransaction();// 开启事务

		// 多条数据的订单状态，进行更改
		for (GsTaskInfo gsTaskInfo : infoBeans) {
			if ("已分配至核验人员".equals(gsTaskInfo.getStatus())) {
				// 判断集合里面每条数据的订单状态是否为"出发"状态
				values.put("state", "出发");
				update = db.update(OrdersDB.ordersList.TABLE_NAME, // 表名
						values, // set条件
						"num=?", // where条件
						new String[] { gsTaskInfo.getOrdercode() });// where条件的值
				LogUtil.i(getClass().getName(), "批量修改出发状态，更新了" + update + "条数据");

			} else if ("出发".equals(gsTaskInfo.getStatus())) {
				// 判断集合里面每条数据的订单状态是否为"到达"状态
				values.put("state", "到达");
				update = db.update(OrdersDB.ordersList.TABLE_NAME, // 表名
						values, // set条件
						"num=?", // where条件
						new String[] { gsTaskInfo.getOrdercode() });// where条件的值
				LogUtil.i(getClass().getName(), "批量修改到达状态，更新了" + update + "条数据");

			} else {
				Log.e("TAG", "批量更改订单异常：订单状态匹配不对，无法进行批量更新");
			}
		}
		db.setTransactionSuccessful();// 提交事务
		db.endTransaction();// 结束事务
		db.close();

		if (update > 0) {
			succeed = true;
			return succeed;
		}
		return succeed;

	}

	/**
	 * 查询所有数据
	 * 
	 * @return
	 */
	public ArrayList<GsTaskInfo> queryAllData() {
		ArrayList<GsTaskInfo> infoList = new ArrayList<GsTaskInfo>();
		SQLiteDatabase db = helper.getReadableDatabase(key);

		Cursor cursor = db.query(OrdersDB.ordersList.TABLE_NAME, // 表名
				null, // 要查询列名 new String[]{name,age}
				null, // 查询条件
				null, // 条件参数
				null, // 分组
				null, // 分组
				null); // 排序

		if (cursor != null && cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				String ordercode = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_NUM));
				String name = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CNAME));
				String phone = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CPHONE));
				String address = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_ADDRESS));
				String remark = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_REMARK));
				String status = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_STATE));
				String billtype = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CTYPE));
				String bankname = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_BANK));
				String yuyuetime = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_TIME));
				String yujitime = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_TOA));
				String paidanphone = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_MPHONE));
				String hint = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_HINT));
				String identno = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_IDCARD));
				GsTaskInfo gsTaskInfo = new GsTaskInfo(ordercode, name, phone, address, remark, status, billtype,
						bankname, yuyuetime, yujitime, paidanphone, hint, identno);

				infoList.add(gsTaskInfo);
			}

			cursor.close();
		}
		db.close();
		return infoList;
	}

	/**
	 * 查询订单状态为"完成"的所有数据
	 * 
	 * @return
	 */
	public ArrayList<GsTaskInfo> queryCompleteData() {
		ArrayList<GsTaskInfo> infoList = new ArrayList<GsTaskInfo>();
		SQLiteDatabase db = helper.getReadableDatabase(key);

		String sql = "select * from t_orders  where state in(?);";

		Cursor cursor = db.rawQuery(sql, new String[] { "完成" });

		if (cursor != null && cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				String ordercode = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_NUM));
				String name = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CNAME));
				String phone = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CPHONE));
				String address = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_ADDRESS));
				String remark = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_REMARK));
				String status = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_STATE));
				String billtype = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CTYPE));
				String bankname = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_BANK));
				String yuyuetime = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_TIME));
				String yujitime = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_TOA));
				String paidanphone = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_MPHONE));
				String hint = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_HINT));
				String identno = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_IDCARD));

				GsTaskInfo gsTaskInfo = new GsTaskInfo(ordercode, name, phone, address, remark, status, billtype,
						bankname, yuyuetime, yujitime, paidanphone, hint, identno);

				infoList.add(gsTaskInfo);
			}

			cursor.close();
		}
		db.close();
		return infoList;
	}

	/**
	 * 删除一条数据，根据订单号
	 * 
	 * @return
	 */
	public boolean deleteAData(String ordercode) {
		SQLiteDatabase db = helper.getWritableDatabase(key);
		int delete = db.delete(OrdersDB.ordersList.TABLE_NAME, OrdersDB.ordersList.COLUMN_NUM + "=?",
				new String[] { ordercode });
		if (delete > 0) {
			return true;
		}
		db.close();
		return false;
	}

	/**
	 * 查询非完成状态的订单
	 * 
	 * @return
	 */
	public ArrayList<GsTaskInfo> queryNoCompleteData() {
		ArrayList<GsTaskInfo> infoList = new ArrayList<GsTaskInfo>();
		SQLiteDatabase db = helper.getReadableDatabase(key);
		String sql = "select * from t_orders  where state in(?,?,?,?,?);";

		Cursor cursor = db.rawQuery(sql, new String[] { "已分配至核验人员", "已分配至二次核身人员", "出发", "到达", "执行", });

		if (cursor != null && cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				String ordercode = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_NUM));
				String name = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CNAME));
				String phone = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CPHONE));
				String address = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_ADDRESS));
				String remark = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_REMARK));
				String status = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_STATE));
				String billtype = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CTYPE));
				String bankname = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_BANK));
				String yuyuetime = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_TIME));
				String yujitime = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_TOA));
				String paidanphone = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_MPHONE));
				String hint = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_HINT));
				String identno = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_IDCARD));

				GsTaskInfo gsTaskInfo = new GsTaskInfo(ordercode, name, phone, address, remark, status, billtype,
						bankname, yuyuetime, yujitime, paidanphone, hint, identno);

				infoList.add(gsTaskInfo);
			}

			cursor.close();
		}
		db.close();
		return infoList;
	}

	/**
	 * 查询预约和出发状态的订单
	 * 
	 * @return
	 */
	public ArrayList<GsTaskInfo> queryAppointAndGoData() {
		ArrayList<GsTaskInfo> infoList = new ArrayList<GsTaskInfo>();
		SQLiteDatabase db = helper.getReadableDatabase(key);
		String sql = "select * from t_orders  where state in(?);";

		Cursor cursor = db.rawQuery(sql, new String[] { "已分配至核验人员" });

		if (cursor != null && cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				String ordercode = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_NUM));
				String name = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CNAME));
				String phone = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CPHONE));
				String address = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_ADDRESS));
				String remark = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_REMARK));
				String status = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_STATE));
				String billtype = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CTYPE));
				String bankname = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_BANK));
				String yuyuetime = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_TIME));
				String yujitime = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_TOA));
				String paidanphone = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_MPHONE));
				String hint = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_HINT));
				String identno = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_IDCARD));

				GsTaskInfo gsTaskInfo = new GsTaskInfo(ordercode, name, phone, address, remark, status, billtype,
						bankname, yuyuetime, yujitime, paidanphone, hint, identno);

				infoList.add(gsTaskInfo);
			}

			cursor.close();
		}
		db.close();
		return infoList;
	}

	/******************* 查询到达的订单 ************************/
	public ArrayList<GsTaskInfo> queryArriveData() {
		ArrayList<GsTaskInfo> infoList = new ArrayList<GsTaskInfo>();
		SQLiteDatabase db = helper.getReadableDatabase(key);
		String sql = "select * from t_orders  where state in(?);";

		Cursor cursor = db.rawQuery(sql, new String[] { "出发" });

		if (cursor != null && cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				String ordercode = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_NUM));
				String name = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CNAME));
				String phone = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CPHONE));
				String address = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_ADDRESS));
				String remark = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_REMARK));
				String status = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_STATE));
				String billtype = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CTYPE));
				String bankname = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_BANK));
				String yuyuetime = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_TIME));
				String yujitime = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_TOA));
				String paidanphone = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_MPHONE));
				String hint = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_HINT));
				String identno = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_IDCARD));

				GsTaskInfo gsTaskInfo = new GsTaskInfo(ordercode, name, phone, address, remark, status, billtype,
						bankname, yuyuetime, yujitime, paidanphone, hint, identno);

				infoList.add(gsTaskInfo);
			}

			cursor.close();
		}
		db.close();
		return infoList;
	}

	/**
	 * 模糊查询 可根据订单号、客户姓名、地址来模糊查询
	 * 
	 * @param like
	 *            订单号\客户姓名\地址的搜索关键词
	 * @return 订单数据
	 */
	public ArrayList<GsTaskInfo> LikeSearchDB(String like) {
		ArrayList<GsTaskInfo> infoList = new ArrayList<GsTaskInfo>();
		SQLiteDatabase db = helper.getReadableDatabase(key);
		Cursor cursor = null;

		// 模糊查询客户姓名
		String sql = "SELECT  * FROM " + OrdersDB.ordersList.TABLE_NAME + " where " + OrdersDB.ordersList.COLUMN_CNAME
				+ " like '%" + like + "%'";
		// 模糊查询订单号
		String sql2 = "SELECT  * FROM " + OrdersDB.ordersList.TABLE_NAME + " where " + OrdersDB.ordersList.COLUMN_NUM
				+ " like '%" + like + "%'";
		// 模糊查询地址
		String sql3 = "SELECT  * FROM " + OrdersDB.ordersList.TABLE_NAME + " where "
				+ OrdersDB.ordersList.COLUMN_ADDRESS + " like '%" + like + "%'";

		Cursor result1 = db.rawQuery(sql, null);
		Cursor result2 = db.rawQuery(sql2, null);
		Cursor result3 = db.rawQuery(sql3, null);

		// 判断三个查询结果，如果cursor.getCount()>0，则执行操作
		if (result1.getCount() > 0) {
			cursor = result1;
			// result1.close();
		} else if (result2.getCount() > 0) {
			cursor = result2;
			// result2.close();
		} else if (result3.getCount() > 0) {
			cursor = result3;
			// result3.close();
		}

		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {

				String ordercode = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_NUM));
				String name = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CNAME));
				String phone = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CPHONE));
				String address = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_ADDRESS));
				String remark = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_REMARK));
				String status = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_STATE));
				String billtype = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CTYPE));
				String bankname = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_BANK));
				String yuyuetime = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_TIME));
				String yujitime = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_TOA));
				String paidanphone = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_MPHONE));
				String hint = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_HINT));
				String identno = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_IDCARD));

				GsTaskInfo infoBean = new GsTaskInfo(ordercode, name, phone, address, remark, status, billtype,
						bankname, yuyuetime, yujitime, paidanphone, hint, identno);

				infoList.add(infoBean);
			}
			cursor.close();
		}
		if (result2 != null) {
			result2.close();
		}
		if (result3 != null) {
			result3.close();
		}
		db.close();
		return infoList;
	}

	/**
	 * 查询当天没完成的订单
	 * 
	 * @param today
	 *            当前时间，如：2016-08-23
	 * @return 非完成状态的订单
	 */
	public List<GsTaskInfo> queryToDayData(String today) {

		// 查询条件：日期 and 非完成状态

		ArrayList<GsTaskInfo> infoList = new ArrayList<GsTaskInfo>();
		SQLiteDatabase db = helper.getReadableDatabase(key);
		Cursor cursor = null;

		// 模糊查询客户姓名
		String sql = "SELECT  * FROM " + OrdersDB.ordersList.TABLE_NAME + " where " + OrdersDB.ordersList.COLUMN_TIME
				+ " like '%" + today + "%'";
		cursor = db.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {

				String ordercode = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_NUM));
				String name = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CNAME));
				String phone = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CPHONE));
				String address = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_ADDRESS));
				String remark = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_REMARK));
				String status = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_STATE));
				String billtype = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_CTYPE));
				String bankname = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_BANK));
				String yuyuetime = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_TIME));
				String yujitime = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_TOA));
				String paidanphone = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_MPHONE));
				String hint = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_HINT));
				String identno = cursor.getString(cursor.getColumnIndex(OrdersDB.ordersList.COLUMN_IDCARD));

				GsTaskInfo infoBean = new GsTaskInfo(ordercode, name, phone, address, remark, status, billtype,
						bankname, yuyuetime, yujitime, paidanphone, hint, identno);

				infoList.add(infoBean);
			}
			cursor.close();
		}
		db.close();
		return infoList;
	}

}
