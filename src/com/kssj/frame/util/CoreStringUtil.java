package com.kssj.frame.util;

/**
* @Description: 字符转换类
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午02:54:20
* @version V1.0
*/
public class CoreStringUtil {
	public static final String numberChar = "0123456789";
	
	
	//字符串是否空
	public static boolean isEmpty(String str){
		return (str == null || str.toString().trim().equals(""));
	}
	//字符串是否不空
	public static boolean isNotEmpty(String str){
		return (str !=null && !str.toString().trim().equals(""));
	}
	
	
}
