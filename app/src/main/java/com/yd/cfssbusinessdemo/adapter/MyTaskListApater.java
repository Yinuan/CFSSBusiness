package com.yd.cfssbusinessdemo.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yd.cfssbusinessdemo.R;
import com.yd.cfssbusinessdemo.entity.GsTaskInfo;

public class MyTaskListApater extends MyAdapter<GsTaskInfo> {


	private LayoutInflater inflater;

	public MyTaskListApater(Context context) {
		super(context);

		inflater = LayoutInflater.from(context);

	}

	@Override
	public View getView(int arg0, View contentView, ViewGroup arg2) {

		ViewHolder holder = null;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = inflater.inflate(R.layout.listview_task_bg, null);
			holder.tv_num = (TextView) contentView.findViewById(R.id.textView_num);
			holder.tv_name = (TextView) contentView.findViewById(R.id.textView_name);
			holder.tv_tybe = (TextView) contentView.findViewById(R.id.textView_tybe);
			holder.tv_time = (TextView) contentView.findViewById(R.id.textView_time);
			holder.tv_address = (TextView) contentView.findViewById(R.id.textView_address);
			holder.btn = (Button) contentView.findViewById(R.id.button_yuyue_list);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}

		GsTaskInfo infoBean = getItem(arg0);
		String name = infoBean.getName();
		String num = infoBean.getOrdercode();
		String address = infoBean.getAddress();
		String userInput = infoBean.getUserInput();// 获取被搜索的关键词

			
			// 如果搜索关键词是姓名，则高亮
			if (!TextUtils.isEmpty(userInput) && name.contains(userInput)) {
				Spanned temp = setHeight(name, userInput);
				holder.tv_name.setText(temp);
				
			} else {			
				holder.tv_name.setText(infoBean.getName());		
			}
		
		
			
			// 如果搜索关键词是订单号，则高亮
			if (!TextUtils.isEmpty(userInput) && num.contains(userInput)) {
				Spanned temp = setHeight(num, userInput);
				holder.tv_num.setText(temp);
			} else {							
				holder.tv_num.setText(infoBean.getOrdercode());			
			}
				
			// 如果搜索关键词是地址，则高亮
			if (!TextUtils.isEmpty(userInput) && address.contains(userInput)) {
				Spanned temp = setHeight(address, userInput);
				holder.tv_address.setText(temp);
			} else {						
				holder.tv_address.setText(infoBean.getAddress());		
			}
		

		holder.tv_tybe.setText(infoBean.getBilltype());
		holder.tv_time.setText(infoBean.getYuyuetime());
		holder.btn.setText(infoBean.getStatus());

		/*
		 * holder.btn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new
		 * Intent(context, TaskMessageActivity.class);
		 * context.startActivity(intent); } });
		 */

		return contentView;
	}

	/********** 设置高亮 *************/
	private Spanned setHeight(String name, String userInput) {
		int index = name.indexOf(userInput);

		int len = userInput.length();

		Spanned temp = Html.fromHtml(name.substring(0, index) + "<u><font color=#78E3FE>"
				+ name.substring(index, index + len) + "</font></u>" + name.substring(index + len, name.length()));
		return temp;
	}

	class ViewHolder {
		TextView tv_num;
		TextView tv_name;
		TextView tv_tybe;
		TextView tv_time;
		TextView tv_address;
		Button btn;
	}

}
