package com.kssj.base.util;

import java.util.HashMap;
import java.util.Map;

public class Global {
	//菜谱
	private static Map numMap = new HashMap();
	
	
	
	public static Map getNumMap() {
		return numMap;
	}

	public static void setNumMap(Map numMap) {
		Global.numMap = numMap;
	}

	public Global(){}
	
	public Global(Map map){
		this.numMap = map;
	}
	
	
	
}
