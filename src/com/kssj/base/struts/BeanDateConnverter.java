package com.kssj.base.struts;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
* @Description: 用于进行Bean的日期属性类型转化
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午06:34:44
* @version V1.0
*/
@SuppressWarnings("rawtypes")
public class BeanDateConnverter implements Converter {
	private static final Log logger = LogFactory.getLog(BeanDateConnverter.class);
	public static final String[] ACCEPT_DATE_FORMATS = {
		"yyyy-MM-dd HH:mm:ss",
		"yyyy-MM-dd"
	};

	public BeanDateConnverter() {
	}

	public Object convert(Class arg0, Object value) {
		logger.debug("conver " + value + " to date object");
		String dateStr=value.toString();
		dateStr=dateStr.replace("T", " ");
		try{
			return DateUtils.parseDate(dateStr, ACCEPT_DATE_FORMATS);
		}catch(Exception ex){
			logger.debug("parse date error:"+ex.getMessage());
		}
		return null;
	}
}