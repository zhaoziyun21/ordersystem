package com.kssj.base.servlet;

import java.util.Date;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.kssj.base.util.Constants;

public class AccessTokenUtil {
	private static Logger logger = Logger.getLogger(SessionFilter.class);
	
	public static String accessToken(){
		
		/****************************获取access_token**************************************/
		// 是否需要更新 access_token
		boolean boo = false;
		Date newDate = new Date();
		Date oldDate = null;
		if(AttributeClass.UPD_TIME != null){
			try {
				oldDate = AttributeClass.UPD_TIME;
				long diff = newDate.getTime() - oldDate.getTime();
				long second = diff / 1000;
				logger.info("[SessionFilter.doFilter]-----已消耗：" + second + "秒");
				System.out.println("已消耗：" + second + "秒");
				if(second >= 7000){
					boo = true;
				}
			} catch (Exception e) {
				logger.info("[SessionFilter.doFilter]：error:" + e);
				e.printStackTrace();
			}
		}else{
			boo = true;
		}
		//获取access_token
		if(boo){
			logger.info("[SessionFilter.doFilter]：-- 开始获取access_token");
			System.out.println("开始获取    access_token");
			String api_url = Constants.ACCESS_TOKEN_API
    				+ "?corpid=" + Constants.CORPID
    				+ "&corpsecret=" +Constants.CORPSECRET;
    		String access_token_resource = WebUtils.httpsGet(api_url);
    		JSONObject accessTokenJson = JSONObject.fromObject(access_token_resource);
    		String access_token = accessTokenJson.getString("access_token");
    		logger.info("[SessionFilter.doFilter]：-- access_token = " + access_token);
			System.out.println("access_token = " + access_token);
			AttributeClass.ACCESS_TOKEN = access_token;
			AttributeClass.UPD_TIME = newDate;
			System.out.println("从新获取的ACCESS_TOKEN"+access_token);
		}
		
		/****************************获取access_token END**************************************/
		
		
		return AttributeClass.ACCESS_TOKEN;
	}
}
