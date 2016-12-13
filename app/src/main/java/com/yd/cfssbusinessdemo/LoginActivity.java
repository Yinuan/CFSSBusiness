package com.yd.cfssbusinessdemo;

import java.io.File;
import java.io.FileOutputStream;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.yd.cfssbusinessdemo.config.Config;
import com.yd.cfssbusinessdemo.entity.GsLogin;
import com.yd.cfssbusinessdemo.util.SharedPreferencesID;
import com.yd.cfssbusinessdemo.util.SharedPreferencesUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity {

	EditText userName_et; // 用户名
	EditText Password_et; // 密码
	Button loginBtn; // 登录按钮
	String Token; // 服务器返回的token
	private SharedPreferencesUnit sharedUnit;// 保存用户信息
	private SharedPreferencesID sharedID;// 保存用户信息
	private LinearLayout ll;// 布局滚动
	private CheckBox checkBox;// 点击记住账号
	private String ID;// 用户账号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		sharedUnit = SharedPreferencesUnit.instance(this);
		sharedID = new SharedPreferencesID(this, "MySharedID");
		// 把活动装箱
		MyApp.getInstance().addActivity(this);
		findView();
		// 设置账号
		String name = sharedID.reStoreData("ID");
		if (name.length() != 0 && name != null) {
			userName_et.setText(name);
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}

		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 登陆
				login();

			}
		});

		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});
		// 点击记住账号,只有登录成功的账号才记住
		checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkBox.isChecked()) {
					ID = userName_et.getText().toString().trim();
				} else {
					ID = "";
				}

			}
		});
	}

	// 登录
	protected void login() {
		final String userName = userName_et.getText().toString().trim();
		final String password = Password_et.getText().toString().trim();
		if (userName.length() == 0 || password.length() == 0) {
			print("用户名或密码不能为空");
			return;
		}
		// 获取手机的imel
		String Imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		// String Imei = "A100004A80B841";//小欧测试
		saveFile("当前设备号是："+Imei);// 保存MEID、IMEI码到SD卡上，方便查看验证
		System.out.println("----Imei----" + Imei);
		HttpParams params = new HttpParams();
		params.put("number", userName);
		params.put("password", password);
		params.put("imei", Imei);
		OkHttpUtils.post(Config.LOGIN_URL).tag(this).params(params).execute(new StringCallback() {

			@Override
			public void onResponse(boolean arg0, String arg1, Request arg2, Response arg3) {
				System.out.println("---login-----1----" + arg1);
				if (arg1 != null || arg1.length() > 0) {
					GsLogin fromJson = new Gson().fromJson(arg1, GsLogin.class);
					if (fromJson.getRet().equals("0")) {
						// 验证成功，获取token及个人资料
						String token = fromJson.getToken();
						// 获取用户信息保存
						String idCard = fromJson.getData().getIdcard();
						String name = fromJson.getData().getName();
						String sex = fromJson.getData().getSex();
						String telephone = fromJson.getData().getTelephone();
						// 保存token
						sharedUnit.savaData("token", token);
						// 保存用户信息
						sharedUnit.savaData("idcard", idCard);
						sharedUnit.savaData("sex", sex);
						sharedUnit.savaData("name", name);
						sharedUnit.savaData("telephone", telephone);
						// 保存头像路径
						String image_touxiang = fromJson.getData().getHeadportraiturl();
						sharedUnit.savaData("headportraiturl", image_touxiang);
						// 保存账号
						sharedID.savaData("ID", ID);
						// 跳转界面
						Intent i = new Intent(LoginActivity.this, TaskListActivity.class);
						startActivity(i);
						finish();
					} else if (fromJson.getRet().equals("1000")) {
						print("该用户名不存在...");
					} else if (fromJson.getRet().equals("1001")) {
						print("你的密码错误，请重试...");
					} else if (fromJson.getRet().equals("1003")) {
						print("MEID不匹配，请联系后台管理员");
					} else {
						print("系统异常，错误码：2000");
					}
				} else {
					System.out.println("-----日你大爷的---------");
				}
			}

			@Override
			public void onError(boolean isFromCache, Call call, Response response, Exception e) {
				super.onError(isFromCache, call, response, e);
				Toast.makeText(LoginActivity.this, "登录失败，请检查网络" + e.getMessage(), 0).show();
				// Toast.makeText(LoginActivity.this, "连接失败，请检查网络", 0).show();

			}
		});
	}

	private void findView() {
		userName_et = (EditText) findViewById(R.id.editText_userName);
		Password_et = (EditText) findViewById(R.id.editText_password);
		loginBtn = (Button) findViewById(R.id.button_login);
		ll = (LinearLayout) findViewById(R.id.ll_login);
		checkBox = (CheckBox) findViewById(R.id.checkBox_remember);

	}

	private void print(String message) {
		Toast.makeText(LoginActivity.this, message, 0).show();
	}

	long mExitTime = 0;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyApp.getInstance().exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 保存一个字符串内容到SD卡文件上
	 * 
	 * @param str
	 */
	public static void saveFile(String str) {
		String filePath = null;
		boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (hasSDCard) {
			filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "cfss.txt";
		} else {
			filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "cfss.txt";
		}
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				File dir = new File(file.getParent());
				dir.mkdirs();
				file.createNewFile();
			}
			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(str.getBytes());
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
