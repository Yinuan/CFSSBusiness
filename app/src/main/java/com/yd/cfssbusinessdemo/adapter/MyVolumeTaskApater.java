package com.yd.cfssbusinessdemo.adapter;

import java.util.HashMap;
import java.util.List;

import com.yd.cfssbusinessdemo.R;
import com.yd.cfssbusinessdemo.TaskMessageActivity;
import com.yd.cfssbusinessdemo.entity.GsTaskInfo;
import com.yd.cfssbusinessdemo.util.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MyVolumeTaskApater extends BaseAdapter {
	
	private Context context;
	private List<GsTaskInfo> list;
	private LayoutInflater inflater;
	private static HashMap<Integer, Boolean> isSelected;
	
	

	public MyVolumeTaskApater(Context context, List<GsTaskInfo> list) {
		super();
		this.context = context;
		this.list = list;
		inflater =LayoutInflater.from(context);
		isSelected =new HashMap<Integer, Boolean>();
		initData();
	}

	 public static HashMap<Integer, Boolean> getIsSelected() {  
	        return isSelected;  
	    }  
	  
	 public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {  
	        MyVolumeTaskApater.isSelected = isSelected;  
	    }  
	
	//初始化数据
	private void initData() {
		for(int i=0;i<list.size();i++){
			getIsSelected().put(i, false);
		}
		
	}


	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public GsTaskInfo getItem(int arg0) {
		
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View contentView, ViewGroup arg2) {
		GsTaskInfo infoBean =list.get(position);
		ViewHolder holder =null;
		if(contentView ==null){
			holder =new ViewHolder();
			contentView =inflater.inflate(R.layout.listview_volume_bg, null);
			holder.tv_num =(TextView) contentView.findViewById(R.id.textView_num_vm);
			holder.tv_name =(TextView) contentView.findViewById(R.id.textView_name_vm);
			holder.tv_tybe =(TextView) contentView.findViewById(R.id.textView_tybe_vm);
			holder.tv_time =(TextView) contentView.findViewById(R.id.textView_time_vm);
			holder.tv_address =(TextView) contentView.findViewById(R.id.textView_address_vm);
			holder.btn =(Button) contentView.findViewById(R.id.button_yuyue_list_vm);
			holder.checkBox =(CheckBox) contentView.findViewById(R.id.checkBox);
			contentView.setTag(holder);
		}else{
			holder =(ViewHolder) contentView.getTag();
		}
		holder.tv_num.setText(infoBean.getOrdercode());
		holder.tv_name.setText(infoBean.getName());
		holder.tv_tybe.setText(infoBean.getBilltype());
		holder.tv_time.setText(infoBean.getYuyuetime());
		holder.tv_address.setText(infoBean.getAddress());
		holder.btn.setText(infoBean.getStatus());						
		holder.checkBox.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				//监听checkBox并根据原来的状态来设置新的状态  
				  if (isSelected.get(position)) {  
	                    isSelected.put(position, false);  
	                    setIsSelected(isSelected);  
	                } else {  
	                    isSelected.put(position, true);  
	                    setIsSelected(isSelected);  
	                }  
				
			}
		});
		// 根据isSelected来设置checkbox的选中状况  
		holder.checkBox.setChecked(getIsSelected().get(position));
		
		return contentView;
	}
	
	

}
