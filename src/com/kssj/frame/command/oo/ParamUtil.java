package com.kssj.frame.command.oo;

import java.math.BigDecimal;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kssj.frame.util.CoreDateUtil;

/**
* @Description: (#FrameWork#)CORE -- get the type of the parameter
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午02:39:16
* @version V1.0
*/
public class ParamUtil {
	private static Log logger=LogFactory.getLog(ParamUtil.class);
	
	 public static Object convertObject(String type,String paramValue){
	    	if(StringUtils.isEmpty(paramValue))return null;
	    	Object value=null;
	    	try{
				if("S".equals(type)){
					value=paramValue;
				}else if("L".equals(type)){
					value=new Long(paramValue);
				}else if("N".equals(type)){
					value=new Integer(paramValue);
				}else if("BD".equals(type)){
					value=new BigDecimal(paramValue);
				}else if("FT".equals(type)){
					value=new Float(paramValue);
				}else if("SN".equals(type)){
					value=new Short(paramValue);
				}else if("D".equals(type)){
					value=DateUtils.parseDate(paramValue,new String[]{"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss"});
				}else if("DL".equals(type)){
					Calendar cal=Calendar.getInstance();
					cal.setTime(DateUtils.parseDate(paramValue,new String[]{"yyyy-MM-dd"}));
					value=CoreDateUtil.setStartDay(cal).getTime();
				}else if("DG".equals(type)){
					Calendar cal=Calendar.getInstance();
					cal.setTime(DateUtils.parseDate(paramValue,new String[]{"yyyy-MM-dd"}));
					value=CoreDateUtil.setEndDay(cal).getTime();
				}else{
					value=paramValue;
				}
			}catch(Exception ex){
				logger.error("the data value is not right for the query filed type:"+ex.getMessage());
			}
			return value;
	    }
}
