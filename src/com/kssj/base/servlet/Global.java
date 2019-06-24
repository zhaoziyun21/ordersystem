package com.kssj.base.servlet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minshenglife.commons.utils.PropertiesLoader;


/**
 * 全局配置类
 * @version 2013-03-23
 */
public class Global {
	private static Logger logger=LoggerFactory.getLogger(Global.class);
	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader propertiesLoader;
	
	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		if (propertiesLoader == null){
			propertiesLoader = new PropertiesLoader("application.properties","project.properties");
		}
		System.out.println("==========propertiesLoader="+propertiesLoader.getProperty(key));
		return propertiesLoader.getProperty(key);
	}
	/**
	 * 获取整数配置
	 * @param key
	 * @return
	 */
	public static int getInteger(String key){
		String value=getConfig(key);
		if(StringUtils.isBlank(value)){
			return 0;
		}else{
			try{
				int ret= Integer.parseInt(value);
				return ret;
			}catch(NumberFormatException formatException){
				logger.error("从配置中获取integer失败");
				return 0;
			}
		}
	}
	/**
	 * 获取布尔型配置
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key){
		String value=getConfig(key);
		if(StringUtils.isBlank(value)){
			return false;
		}else{
			boolean ret=Boolean.parseBoolean(value);
			return ret;
		}
	}
}
