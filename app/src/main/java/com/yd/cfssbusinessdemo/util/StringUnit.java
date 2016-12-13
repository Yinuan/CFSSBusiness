package com.yd.cfssbusinessdemo.util;
/**
 * 
* @ClassName: StringUnit 
* @Description:处理电话号码的类
* @author Yin_Juan
* @date 2016年6月24日 上午11:46:26 
*
 */
public class StringUnit {
	
	public static String symbolString(String number){
		if (number!=null||number.length()>0) {
			//判断电话号码是否正确
			if (number.length()==11) {
				String start =number.substring(0, 3);
				String end =number.substring(7, 11);
				String phone_number =start+"****"+end;
				return phone_number;
			} else {				
				return number;
			}
		}
		return number;

	}
}
