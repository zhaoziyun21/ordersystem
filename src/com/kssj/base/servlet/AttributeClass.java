package com.kssj.base.servlet;

import java.util.Date;

public class AttributeClass {
	// jsApi 动态获取，因每日有限制，做全局缓存
	public static String JSAPI_TICKET = "";
	// access_token 获取
	public static String ACCESS_TOKEN = "";
	// 最后更新时间
	public static Date UPD_TIME = null;
	
}
