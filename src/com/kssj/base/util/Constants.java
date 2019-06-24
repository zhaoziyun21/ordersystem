package com.kssj.base.util;

import java.util.HashMap;
import java.util.Map;


/**
* @Description: 常量类
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-15 上午11:18:32
* @version V1.0
*/
public class Constants {
	//单价
	public static final String UNIT_PRICE="25";
	//收入
	public static final String INCOME="0";
	//支出
	public static final String PAY="1";
	
	//数据库一周菜谱
	public static final Map<String,String> billMap =  new HashMap();   
	static {  
		billMap.put("week_1", "1");  
		billMap.put("week_1-dinner", "2");  
		billMap.put("week_2", "3");  
		billMap.put("week_2-dinner", "4");  
		billMap.put("week_3", "5");  
		billMap.put("week_3-dinner", "6");  
		billMap.put("week_4", "7");  
		billMap.put("week_4-dinner", "8");  
		billMap.put("week_5", "9");  
		billMap.put("week_5-dinner", "10");  
		billMap.put("week_6", "11");  
		billMap.put("week_6-dinner", "12");  
		billMap.put("week_7", "13");  
		billMap.put("week_7-dinner", "14");  
	} 
	
	/**
	 * 产品
	 */
	public static final String PRO = "PRO";
	
	/**
	 * 午
	 */
	public static final String LUN = "LUN";
	/**
	 * 晚
	 */
	public static final String DIN = "DIN";
	
	/**
	 * 现场订餐
	 */
	//午餐开始时间
	public static final String LIVE_LUNSTART = "11:30:00";
	//午餐结束时间
	public static final String LIVE_LUNSEND = "13:30:00";
	//晚餐开始时间
	public static final String LIVE_DINSTART = "17:30:00";
	//晚餐结束时间
	public static final String LIVE_DINEND = "19:30:00";
	
	/**
	 * 正常和预定订餐
	 */
	//午餐开始时间
	public static final String LUNSTART = "00:00:00";
	//午餐结束时间
	public static final String LUNSEND = "12:00:00";
	//晚餐开始时间
	public static final String DINSTART = "12:00:01";
	//晚餐结束时间
	public static final String DINEND = "22:00:00";
	
	
	/*-------------add dingzhj at date 2017-03-22  订餐时间START----------------------------*/
	/**
	 * 上午订餐开始时间
	 */
	public static final String AMSTART = "00:00:00";
	/**
	 * 上午订餐结束时间
	 */
	public static final String AMEND = "10:20:00";
	/**
	 * 下午订餐开始时间
	 */
	public static final String PMSTART = "12:00:00";
	/**
	 * 下午订餐结束时间
	 */
	public static final String PMEND = "16:00:00";
	
	/*-------------add dingzhj at date 2017-03-22  订餐时间END----------------------------*/
	//cookie名
	public static final String USERNAME="username";
	public static final String PASSWORD="password";
	/**
	 * 安全级别
	 */
	public static final Integer SECLEVEL= 40;
	/**
	 * 员工type
	 */
	public static final String STAFF_TYPE ="1";
	/**
	 * 餐饮公司type
	 */
	public static final String FOOD_COMPANY_TYPE ="2";
	/**
	 * 自定义用户
	 */
	public static final String ADMIN_TYPE = "0";
	/**
	 * 部门联系人角色id
	 */
	public static final String DEPT_CONTACT_ID ="3";
	/**
	 * 部门专用角色id
	 */
	public static final String DEPT_ONLY_ID ="5";
	
	/**
	 * 餐饮公司角色
	 */
	public static final String FOOD_USER_ROLE ="4";
	
	/**
	 * 超级管理员角色
	 */
	public static final String ADMIN_ROLE ="1";
	
	// 获取access_token  API
	public static String ACCESS_TOKEN_API = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
	//CORPID
	public static String CORPID = "wxba9da6bca68c6581";
	//CORPSECRET 订餐应用的Secret
	public static String CORPSECRET = "qVyCt3e3RoH1yyP_in9v6ueyWTpnWOa6k1glmbKwUgk";
	
}

