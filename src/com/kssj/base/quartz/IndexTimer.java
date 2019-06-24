package com.kssj.base.quartz;

import org.apache.log4j.Logger;

/**
* @Description: solr inder Timer
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-6-3 上午11:35:15
* @version V1.0
*/
public class IndexTimer {
	private Logger logger = Logger.getLogger(IndexTimer.class);
	/*@Resource
	private SearchEngineService searchEngineService;*/
	
	public void createIndex(){
		try{
			logger.info("-----------start index ......");
			//调用方法
			System.out.println("================执行定时任务！");
			//searchEngineService.InderTimer();
			
			logger.info("-----------end index !!!");
		}catch(Exception e){
			e.printStackTrace();
			logger.log(null,"--------->>Exception: index ......");
		}		
	}
	
}
