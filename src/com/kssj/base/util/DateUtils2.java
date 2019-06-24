package com.kssj.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils2 {

	/**
	* 日期转换成字符串
	* @param date 
	* @return str
	*/
	public static String dateToStr(Date date) {
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String str = format.format(date);
	   return str;
	} 
	
	public static String dateToStr2(Date date) {
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	   String str = format.format(date);
	   return str;
	}
	
	public static String dateToStr3(Date date) {
	   SimpleDateFormat format = new SimpleDateFormat("MM-dd");
	   String str = format.format(date);
	   return str;
	}

	/**
	* 字符串转换成日期
	* @param str
	* @return date
	*/
	public static Date strToDate(String str) {
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (Exception e) {
	    e.printStackTrace();
	   }
	   return date;
	}
	
	public static Date strToDate2(String str) {
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (Exception e) {
	    e.printStackTrace();
	   }
	   return date;
	}
	
}
