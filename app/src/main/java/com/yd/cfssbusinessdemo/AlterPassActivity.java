package com.yd.cfssbusinessdemo;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpParams;
import com.yd.cfssbusinessdemo.config.Config;
import com.yd.cfssbusinessdemo.entity.GsLoginOut;
import com.yd.cfssbusinessdemo.util.SharedPreferencesUnit;
import com.yd.cfssbusinessdemo.util.SystemBarTintManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class AlterPassActivity extends Activity {

	private ImageButton i_back; // 返回按钮
	private Button btn_save; // 修改按钮
	private EditText et_old_pass, et_new_pass, et_confrim_pass; // 密码输入框
	private InputMethodManager inputMethodManager; // 状态条
	private SharedPreferencesUnit share ;//文件读取


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//把活动装箱
		MyApp.getInstance().addActivity(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alter_pass);
		// 4.4及以上版本开启
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		// 自定义颜色
		tintManager.setTintColor(Color.parseColor("#f5821e"));
		initView();
		setOnclick();

	}

	private void initView() {
		inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		share =SharedPreferencesUnit.instance(this);//文件管理
		i_back = (ImageButton) findViewById(R.id.imageView_alter_pass);
		btn_save = (Button) findViewById(R.id.button_exit);		// 修改密码按钮
		et_old_pass =(EditText) findViewById(R.id.editView_old_pass);
		et_new_pass =(EditText) findViewById(R.id.editView_new_pass);
		et_confrim_pass =(EditText) findViewById(R.id.editView_confrim_pass);
	}
	//返回按钮
	private void setOnclick() {
		i_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				finish();

			}
		});
		//保存密码
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard();	//隐藏键盘
				// 后台修改
				alterPassword();

			}
		});			

	}

	// 修改密码
	protected void alterPassword() {
		//获取token
		String token =share.reStoreData("token");
		//获取密码
		String password =et_old_pass.getText().toString();	//旧密码
		String npd=et_new_pass.getText().toString();	//新密码
		String confrim_pass=et_confrim_pass.getText().toString();//确认密码
		if(!npd.equals(confrim_pass)){			
			Toast.makeText(AlterPassActivity.this, "两次新密码输入不一致", 0).show();
		}else{
			HttpParams params =new HttpParams();
			params.put("token", token);
			params.put("password", password);
			params.put("npd", npd);
			OkHttpUtils.post(Config.PASS_ALTER)
			.params(params)
			.execute(new StringCallback() {
				
				@Override
				public void onResponse(boolean arg0, String arg1, Request arg2,
						Response arg3) {
					System.out.println("--------"+arg1);
					GsLoginOut data =new Gson().fromJson(arg1, GsLoginOut.class);
					if (data.getRet().equals("0")) {
						Intent intent =new Intent(AlterPassActivity.this,LoginActivity.class);
						startActivity(intent);
					}else if (data.getRet().equals("1004")) {
						print("msg:原有密码错误"+"code:1004");
					}else if (data.getRet().equals("2000")) {
						print("msg:系统异常"+"code:2000");
					}else if (data.getRet().equals("1002")) {
						print("msg:该用户不存在"+"code:1002");
					}else if (data.getRet().equals("1001")) {
						print("msg:用户未登录"+"code:1001");
					}else if (data.getRet().equals("1003")) {
						print("msg:密码为空"+"code:1003");
					}
					//清空
					et_confrim_pass.getText().clear();
					et_new_pass.getText().clear();
					et_old_pass.getText().clear();
					
				}
				@Override
				public void onError(boolean isFromCache, Call call,
						Response response, Exception e) {
					super.onError(isFromCache, call, response, e);
					print("网络连接失败，修改无效！");
				}
			});
		}
	}

	// 设置状态栏
	private void setTranslucentStatus(boolean b) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (b) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);

	}
	// 隐藏键盘
	void hideSoftKeyboard() {
		((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).
		hideSoftInputFromWindow(getCurrentFocus().
				getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); }
	private void print(String message){
		Toast.makeText(AlterPassActivity.this, message, 0).show();
	}
		
}
