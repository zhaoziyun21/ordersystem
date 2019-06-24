package com.kssj.base.listener;


import javax.servlet.ServletContextEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoaderListener;


/**
* @Description: extends “ContextLoaderListener”
* 				loading...
* 					1.config.properties
* 					2.permission and mnue
* 
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午06:30:53
* @version V1.0
*/
public class StartupListener extends ContextLoaderListener {
	private static Log logger=LogFactory.getLog(StartupListener.class);
	
	public void contextInitialized(ServletContextEvent event) {

		super.contextInitialized(event);
		//Initialize the implemental Bean of the Application
		AppUtil.init(event.getServletContext());
		
		logger.info("-------ApplicationContext.xml is loaded!!!");
	}
}
