package com.yd.cfssbusinessdemo.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yd.cfssbusinessdemo.AlterPassActivity;
import com.yd.cfssbusinessdemo.ImageActivity;
import com.yd.cfssbusinessdemo.LoginActivity;
import com.yd.cfssbusinessdemo.MyApp;
import com.yd.cfssbusinessdemo.R;
import com.yd.cfssbusinessdemo.weight.CircleImageView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/****** 我的 ******/
public class Fragment_mine extends BaseFragment {

	private RelativeLayout reLayout;
	private Button btn_exit; // 退出登录按钮
	private TextView tv_name, tv_sex, tv_phone, tv_idcard; // 个人资料信息
	// 头像
	private CircleImageView image_tx;

	@Override
	protected int setLayoutResID() {

		return R.layout.fragment_two;
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {
		reLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout_pass);
		btn_exit = (Button) view.findViewById(R.id.button_exit);
		tv_idcard = (TextView) view.findViewById(R.id.textView_personal_id_card);
		tv_name = (TextView) view.findViewById(R.id.textView_personal_name);
		tv_sex = (TextView) view.findViewById(R.id.textView_personal_sex);
		tv_phone = (TextView) view.findViewById(R.id.textView_personal_phone);
		// 获取用户信息
		tv_idcard.setText(share.reStoreData("idcard"));
		tv_name.setText(share.reStoreData("name"));
		tv_phone.setText(share.reStoreData("telephone"));
		tv_sex.setText(share.reStoreData("sex"));
		// 显示用户头像
		showTouXiang();
	}

	private void showTouXiang() {
		image_tx = (CircleImageView) view.findViewById(R.id.touxiang);
		final String imageUrl = share.reStoreData("headportraiturl");
		System.out.println("头像地址是：" + imageUrl);
		
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					OkHttpClient client = new OkHttpClient();
//					Request request = new Request.Builder().url(imageUrl).build();
//					Response response = client.newCall(request).execute();
//					InputStream is = response.body().byteStream();
//					Bitmap bm = BitmapFactory.decodeStream(is);
//					// 1.图片保存的路径
////					FileOutputStream out = getActivity().openFileOutput("下载来啦.jpg", getActivity().MODE_PRIVATE);
//					String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/图片.jpg";
//					FileOutputStream out = new FileOutputStream(path);
//					bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
//					out.flush();
//					out.close();
//
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
////		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/图片.jpg";
//		String path = ";"
//				+ "file:///mnt/sdcard/图片.jpg";
//		System.out.println("--下载的图片路径是：" + path);
//		String url ="file:///mnt/sdcard/liangXian.jpg";;
		ImageLoader.getInstance().displayImage(imageUrl, image_tx, MyApp.getInstance().setImageOptionsConfig());

	}

	private void downImage(String imageUrl) {

	}

	@Override
	protected void setOnListener() {
		// 修改密码
		reLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AlterPassActivity.class);
				startActivity(intent);

			}
		});
		// 退出登录
		btn_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle("确认注销登录？")
						.setMessage("提示：请确认你的离线任务已提交并在联网状态下退出")
						.setPositiveButton("确认", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// 停止定位服务
								Intent in = new Intent("com.yd.cfssbusinessdemo.service");
								in.setPackage(getActivity().getPackageName());
								getActivity().stopService(in);
								// 清除数据库
								dao.deleteDB();
								// 清除用户信息
								share.clear();
								// 清除头像
								ImageLoader.getInstance().clearMemoryCache(); // 清除内存缓存
								ImageLoader.getInstance().clearDiskCache(); // 清除本地缓存
								Intent intent = new Intent(getActivity(), LoginActivity.class);
								startActivity(intent);

							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub

							}
						});
				builder.create().show();

			}
		});

		// 点击放大头像
		image_tx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ImageActivity.class);
				startActivity(intent);

			}
		});
	}

}
