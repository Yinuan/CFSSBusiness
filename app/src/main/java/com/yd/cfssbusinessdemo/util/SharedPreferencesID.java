package com.yd.cfssbusinessdemo.util;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 
* @ClassName: SharedPreferencesUnit 
* @Description: 保存用户账号
* @author Yin_Juan
* @date 2016年6月23日 下午5:56:19 
*
 */
public class SharedPreferencesID {
	private static SharedPreferences sharedPreferences;
    private static SharedPreferencesID sharedPreferencesUnit;
    private static SharedPreferences.Editor editor;
    private static Context context;

    public SharedPreferencesID(Context context,String code) {
       this.context =context;
        sharedPreferences =context.getSharedPreferences(code,Context.MODE_PRIVATE);
        editor =sharedPreferences.edit();
    }
  /*  //单列模式。通过单列实例化，其他功能方法可以不设置静态
    public static SharedPreferencesID instance(Context context){
        if(sharedPreferencesUnit ==null){
            sharedPreferencesUnit =new SharedPreferencesID(context);
        }
        return sharedPreferencesUnit;
    }*/

    //保存数据
    public  void savaData(String key,String value){       
        editor.putString(key,value);
        editor.commit();

    }

    //提取数据
    public  String reStoreData(String key){

        return sharedPreferences.getString(key,"");
    }

    //清空数据
    public  void clear(){
        editor.clear();
        editor.commit();
    }
}
