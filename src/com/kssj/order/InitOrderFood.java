package com.kssj.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletConfigAware;

import com.kssj.base.util.Global;
import com.kssj.order.service.XOrdersService;

public class InitOrderFood implements InitializingBean,ServletConfigAware{

	public XOrdersService getxOrdersService() {
		return xOrdersService;
	}

	public void setxOrdersService(XOrdersService xOrdersService) {
		this.xOrdersService = xOrdersService;
	}

	private XOrdersService xOrdersService;
	
	@Override
	public void setServletConfig(ServletConfig arg0) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("--[start 进入初始化方法]--");
		
		Map map = new HashMap();
		List<Map> listMap = null;
		String nameArr[] = {"A","B","C"}; 
		String weekArr[] = {
					"WEEK_1_LUN","WEEK_1_DIN",
					"WEEK_2_LUN","WEEK_2_DIN",
					"WEEK_3_LUN","WEEK_3_DIN",
					"WEEK_4_LUN","WEEK_4_DIN",
					"WEEK_5_LUN","WEEK_5_DIN",
					};
		
		Map m = null;
		for (int j = 0; j < weekArr.length; j++) {
			listMap = new ArrayList<Map>();
			for (int i = 0; i < nameArr.length; i++) {
				m = new HashMap();
				m.put("food_name", "套餐"+nameArr[i]);
				m.put("food_num", i+1);
				m.put("food_desc", "描述"+(i+1));
				m.put("food_img", "upload/1488781465361.jpg");
				m.put("food_id", i);
				listMap.add(m);
			}
			map.put(weekArr[j], listMap);
		}
		Global.setNumMap(map);
		System.out.println("[初始化 Global.numMap]:"+Global.getNumMap().toString());
		System.out.println("--[end  进入初始化方法]--");
	}

}
