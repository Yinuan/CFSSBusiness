package com.yd.cfssbusinessdemo.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;



/**
 * md5加签及相关sign验证方法
 * @author clxsui
 *
 */
public class SignTool {
	/**
	 * 加签
	 * @param param	参数
	 * @param contrId	协议号
	 * @return
	 * @throws Exception
	 */
	public String sign(Map<String, Object> param, String contrId)
			throws Exception {
		Map<String,Object> tmp=paramFilter(param);
		String sign=md5(createLinkString(tmp), contrId);
		tmp.put("sign", sign);
		
		return createLinkString(tmp);
	}
	
	/**
	 * 除去数组中的空值和签名参数
	 * @param sArray
	 * @return
	 */
	public static Map<String,Object> paramFilter(Map<String,Object> sArray){
		Map<String,Object> result = new HashMap<String,Object>();
		if(sArray==null||sArray.size()<=0){
			return result;
		}
		for(String key:sArray.keySet()){
			String value=String.valueOf(sArray.get(key));
			if(key.equalsIgnoreCase("sign")||value==null||value.equals("")){
				continue;
			}
			result.put(key, value);
		}
		return result;
	}
	/**
	 * 把map拼接成  "参数=参数值"的模式并用"&"进行拼接
	 * @param params
	 * @return
	 */
	public static String createLinkString(Map<String,Object> params){
		List<String> keys=new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr="";
		for(int i=0;i<keys.size();i++){
			String key=keys.get(i);
			Object value=params.get(key);
			if(i==keys.size()-1){
				prestr=prestr+key+"="+value;
			}else{
				prestr=prestr+key+"="+value+"&";
			}
		}
		return prestr;
	}
	/**
	 * 使用协议号的md5签名算法
	 * @param text
	 * @param salt
	 * @return
	 * @throws Exception
	 */
	public static String md5(String text,String salt) throws Exception{
		//转码，如果getBytes()里面不写，windows7默认转成utf8，而windows server2008会转成GBK，所以导致加密sign不一样，所以需要写上UTF-8
		byte[] en=(text+salt).getBytes("UTF-8");
		MessageDigest messageDigest=MessageDigest.getInstance("MD5");
		messageDigest.update(en);
		byte[] bytes=messageDigest.digest();
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<bytes.length;i++){
			if ((bytes[i] & 0xff) < 0x10) {
				sb.append("0");
			}
			sb.append(Long.toString(bytes[i] & 0xff, 16));
		}
		return sb.toString().toLowerCase();
	}
	
	/**
	 * 普通md5签名算法
	 * @param text
	 * @param salt
	 * @return
	 * @throws Exception
	 */
	public static String md5App(String text) throws Exception{
		//转码，如果getBytes()里面不写，windows7默认转成utf8，而windows server2008会转成GBK，所以导致加密sign不一样，所以需要写上UTF-8
		byte[] en=(text).getBytes("UTF-8");
		MessageDigest messageDigest=MessageDigest.getInstance("MD5");
		messageDigest.update(en);
		byte[] bytes=messageDigest.digest();
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<bytes.length;i++){
			if ((bytes[i] & 0xff) < 0x10) {
				sb.append("0");
			}
			sb.append(Long.toString(bytes[i] & 0xff, 16));
		}
		return sb.toString().toLowerCase();
	}
	
/*	public static void main(String[] args) throws Exception {
		

	}*/
}
