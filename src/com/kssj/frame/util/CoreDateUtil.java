package com.kssj.frame.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
* @Description: 日期公共函数
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 上午10:50:19
* @version V1.0
*/
public class CoreDateUtil {

	/**①
	 * 设置当前时间为当天的最初时间（即00时00分00秒）
	 * 
	 * @param 
	 * @return
	 */
	public static Calendar setStartDay(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal;
	}
	
	/**
	 * 设置当前时间为当天的最后时间（即23时59分59秒）
	 * 
	 * @param 
	 * @return
	 */
	public static Calendar setEndDay(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal;
	}

	/**
	 * 格式化日期为 中文时间格式,如： “yyyy-MM-dd HH:mm:ss”
	 * 
	 * @return
	 */
	public static String formatCnDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	
}
