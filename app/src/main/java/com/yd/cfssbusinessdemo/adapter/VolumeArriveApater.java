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

public class VolumeArriveApater extends BaseAdapter {
	
	private Context context;
	private List<GsTaskInfo> list;
	private LayoutInflater inflater;
	private static HashMap<Integer, Boolean> isSelected_arr;
	
	

	public VolumeArriveApater(Context context, List<GsTaskInfo> list) {
		super();
		this.context = context;
		this.list = list;
		inflater =LayoutInflater.from(context);
		isSelected_arr =new HashMap<Integer, Boolean>();
		initData();
	}

	 public static HashMap<Integer, Boolean> getIsSelected() {  
	        return isSelected_arr;  
	    }  
	  
	 public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {  
	        VolumeArriveApater.isSelected_arr = isSelected;  
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View contentView, ViewGroup arg2) {
		GsTaskInfo infoBean =list.get(position);
		ViewHolder holder_arr =null;
		if(contentView ==null){
			holder_arr =new ViewHolder();
			contentView =inflater.inflate(R.layout.listview_volume_arrive, null);
			holder_arr.tv_num =(TextView) contentView.findViewById(R.id.textView_num_arrive);
			holder_arr.tv_name =(TextView) contentView.findViewById(R.id.textView_name_arrive);
			holder_arr.tv_tybe =(TextView) contentView.findViewById(R.id.textView_tybe_arrive);
			holder_arr.tv_time =(TextView) contentView.findViewById(R.id.textView_time_arrive);
			holder_arr.tv_address =(TextView) contentView.findViewById(R.id.textView_address_arrive);
			holder_arr.btn =(Button) contentView.findViewById(R.id.button_yuyue_list_arrive);
			holder_arr.checkBox =(CheckBox) contentView.findViewById(R.id.checkBox_arrive);
			contentView.setTag(holder_arr);
		}else{
			holder_arr =(ViewHolder) contentView.getTag();
		}
		holder_arr.tv_num.setText(infoBean.getOrdercode());
		holder_arr.tv_name.setText(infoBean.getName());
		holder_arr.tv_tybe.setText(infoBean.getBilltype());
		holder_arr.tv_time.setText(infoBean.getYuyuetime());
		holder_arr.tv_address.setText(infoBean.getAddress());
		holder_arr.btn.setText(infoBean.getStatus());
				
		holder_arr.checkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//监听checkBox并根据原来的状态来设置新的状态  
				  if (isSelected_arr.get(position)) {  
	                    isSelected_arr.put(position, false);  
	                    setIsSelected(isSelected_arr);  
	                } else {  
	                    isSelected_arr.put(position, true);  
	                    setIsSelected(isSelected_arr);  
	                }  
				
			}
		});
		// 根据isSelected来设置checkbox的选中状况  
		holder_arr.checkBox.setChecked(getIsSelected().get(position));
		
		return contentView;
	}
	
	

}
