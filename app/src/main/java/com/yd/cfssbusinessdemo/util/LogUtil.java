package com.yd.cfssbusinessdemo.util;

import android.util.Log;

/** Log日志打印工具 上线的时候，修改isShowLog=false，可统一关闭Log，提高app运行的流畅 */
public class LogUtil {

	public static final boolean DEBUG = false;

	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}

	public static void i(Object o, String msg) {
		if (DEBUG) {
			Log.i(o.getClass().getSimpleName(), msg);
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, msg);
		}
	}

	public static void e(Object o, String msg) {
		if (DEBUG) {
			Log.e(o.getClass().getSimpleName(), msg);
		}
	}

}
