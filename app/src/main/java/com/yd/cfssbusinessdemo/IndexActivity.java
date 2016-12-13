package com.yd.cfssbusinessdemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.yd.cfssbusinessdemo.config.Config;
import com.yd.cfssbusinessdemo.entity.GsTaskInfo;
import com.yd.cfssbusinessdemo.entity.GsUpdata;
import com.yd.cfssbusinessdemo.entity.UpdataInfo;
import com.yd.cfssbusinessdemo.sqlite.dp.OrdersDAO;
import com.yd.cfssbusinessdemo.util.SharedPreferencesUnit;
import com.yd.cfssbusinessdemo.util.ShortcutUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class IndexActivity extends Activity {

	private SharedPreferencesUnit share;
	private UpdataInfo info;// 版本更新信息
	private ProgressDialog pBar;// 更新进度条
	private OrdersDAO dao;
	private Handler mHandle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			/*
			 * // 如果有更新就提示 if (isNeedUpdate()) { //在下面的代码段 showUpdateDialog();
			 * //下面的代码段 }
			 */
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Activity context = this;
		// 把活动装箱
		MyApp.getInstance().addActivity(context);
		// 去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_index);

		// 创建快捷方式
		if (!ShortcutUtils.hasInstallShortcut(context)) {
			sendBroadcast(ShortcutUtils.getShortcutToDesktopIntent(context));
		}

		share = SharedPreferencesUnit.instance(this);
		dao = new OrdersDAO(this);
		// 版本检测的方法(别人的
		// checkVersion();
		// 版本检测的方法(自己的)
		checkVersionByMySelf();
		// skipFace();
	}

	private void checkVersionByMySelf() {
		OkHttpUtils.post(Config.UPDATE_URL).execute(new StringCallback() {

			@Override
			public void onResponse(boolean arg0, String arg1, Request arg2, Response arg3) {
				// 解析
				final GsUpdata data = new Gson().fromJson(arg1, GsUpdata.class);
				if (data.getRet() == 0) {
					// 版本补不要更新
					if (data.getData().getVersion().equals(getVersion())) {
						skipFace();
					} else {
						// 版本更新
						info = data.getData();
						showUpdateDialog();

					}
				} else if (data.getRet() == 2000) {
					// 系统异常什么鬼的
					skipFace();
					System.out.println("----系统异常什么鬼的-------");
				}

			}

			@Override
			public void onError(boolean isFromCache, Call call, Response response, Exception e) {
				// 网络连接失败的话直接进入
				super.onError(isFromCache, call, response, e);
				System.out.println("--日了狗了------------");
				skipFace();
			}
		});

	}

	private void skipFace() {
		new Handler() {
			public void handleMessage(android.os.Message msg) {
				String token = share.reStoreData("token");
				if (token != null && token.length() != 0) {
					Intent intent = new Intent(IndexActivity.this, TaskListActivity.class);
					startActivity(intent);
				} else {
					Intent intent2 = new Intent(IndexActivity.this, LoginActivity.class);
					startActivity(intent2);
				}
			};
		}.sendEmptyMessageDelayed(0, 1000);

	}

	// 更新的提示
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("检测到新版本v" + info.getVersion());
		// builder.setMessage("0000000000000000000"); //info.getDescription()
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 判断是否有离线任务，有的话，不更新
				// 检查离线任务是否完成
				ArrayList<GsTaskInfo> list = dao.queryCompleteData();
				if (list == null || list.size() == 0) {
					// 没有离线任务时 直接下载更新
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
						downFile(info.getUrl()); // 在下面的代码段
					} else {
						Toast.makeText(IndexActivity.this, "SD卡不可用，请插入SD卡", Toast.LENGTH_SHORT).show();
					}
				} else {
					// 有离线任务，将离线任务提交再更新
					Toast.makeText(IndexActivity.this, "有未提交的离线任务，请及时提交", 0).show();
					System.out.println("---离线任务的集合--" + list);
					skipFace();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}

		});
		builder.create().show();
	}

	protected void downFile(final String url) {
		pBar = new ProgressDialog(IndexActivity.this); // 进度条，在下载的时候实时更新进度，提高用户友好度
		pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pBar.setTitle("正在下载");
		pBar.setMessage("请稍候...");
		pBar.setProgress(0);
		pBar.setCanceledOnTouchOutside(false);
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					int length = (int) entity.getContentLength(); // 获取文件大小
					pBar.setMax(length); // 设置进度条的总长度
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(Environment.getExternalStorageDirectory(), "CFSSBusinessDemo.apk");
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024]; // 这个是缓冲区，即一次读取1024个比特
						int ch = -1;
						int process = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							process += ch;
							pBar.setProgress(process); // 这里就是关键的实时更新进度了！
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();

	}

	protected void down() {
		mHandle.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});

	}

	protected void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "CFSSBusinessDemo.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
		// 打开新版本
		// openNewVersion();

	}

	private void openNewVersion() {
		System.out.println("--走没走--------------");
		String packageName = "com.yd.cfssbusinessdemo";
		try {
			Intent intent = this.getPackageManager().getLaunchIntentForPackage(packageName);
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(this, "打开失败", 0).show();
		}

	}

	/*
	 * //判断版本是否需要更新 protected boolean isNeedUpdate() {
	 * 
	 * String v = info.getVersion(); // 最新版本的版本号 Log.i("update",v);
	 * Toast.makeText(IndexActivity.this, v, Toast.LENGTH_SHORT).show(); if
	 * (v.equals(getVersion())) { return false; } else { return true; } }
	 */
	// 获取当前版本的版本号
	private String getVersion() {
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "版本号未知";
		}
	}

	// 离线任务提交
	private void checkOfflineTask() {
		// 检查离任务是否完成
		ArrayList<GsTaskInfo> list = dao.queryCompleteData();
		if (list != null || list.size() >= 0) {
			// 有离线任务，将离线任务提交再更新
			AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this).setTitle("检测到离线任务")
					.setMessage("系统发现你有未提交的离线任务,请提交完后更新")
					.setPositiveButton("确认", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							skipFace();

						}
					});
			builder.create().show();
		}
	}

}
