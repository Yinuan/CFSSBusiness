package com.yd.cfssbusinessdemo.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 
* @ClassName: ServiceUnit 
* @Description: 判断服务是否在运行的类
* @author Yin_Juan
* @date 2016年7月22日 下午4:51:22 
*
 */
public class ServiceUnit {

	public static boolean isServiceWork(Context mContext, String serviceName) {  
	    boolean isWork = false;  
	    ActivityManager myAM = (ActivityManager) mContext  
	            .getSystemService(Context.ACTIVITY_SERVICE);  
	    List<RunningServiceInfo> myList = myAM.getRunningServices(40);  
	    if (myList.size() <= 0) {  
	        return false;  
	    }  
	    for (int i = 0; i < myList.size(); i++) {  
	        String mName = myList.get(i).service.getClassName().toString();  
	        if (mName.equals(serviceName)) {  
	            isWork = true;  
	            break;  
	        }  
	    }  
	    return isWork;  
	}  
}
