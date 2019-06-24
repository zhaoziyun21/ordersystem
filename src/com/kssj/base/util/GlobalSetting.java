package com.kssj.base.util;


import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
 


public class GlobalSetting {

	private static Logger log = Logger.getLogger(GlobalSetting.class);

	private static Map<String,String> forgetMap = new HashMap<String,String>();

	public static Map<String, String> getForgetMap() {
		return forgetMap;
	}

	public static void setForgetMap(Map<String, String> forgetMap) {
		GlobalSetting.forgetMap = forgetMap;
	}

}
