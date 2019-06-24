package com.kssj.quartz;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.kssj.order.service.XOrdersService;

/**
 * 定时进行“系统默认收货”操作
 * 
 * @author zhangxuejiao
 *
 */
public class UpdateOrderQuartz {
	
	private Logger logger = Logger.getLogger(UpdateOrderQuartz.class);

	@Resource
	private XOrdersService xOrdersService;
	
	public void updateOrderRecStatus(){
		try {
			logger.info(new Date()+" 执行《产品订单系统默认收货》的定时任务开始...");
			
			xOrdersService.updateOrderRecStatus();
			
			logger.info(new Date()+" 执行《产品订单系统默认收货》定时任务结束...");
		} catch (Exception e) {
			logger.error(new Date()+" 执行《产品订单系统默认收货》定时任务出现异常...");
		}
	}
    
}
