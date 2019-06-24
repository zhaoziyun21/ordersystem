package com.kssj.base.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

/**
* @Description: 日期公共函数
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 上午10:50:19
* @version V1.0
*/
@SuppressWarnings("deprecation")
public class DateUtil {
	private static final Logger logger = Logger.getLogger(DateUtil.class);

	/**①
	 * 设置当前时间为当天的最初时间（即00时00分00秒）
	 * 
	 * @param 
	 * @return
	 */
	public static Date setDateStartDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}
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
	public static Date setDateEndDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}
	public static Calendar setEndDay(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal;
	}

	/**
	 * 获取指定格式的当前月的第一天
	 * 
	 * @return String
	 */
	public static String getDateStringFirst(String pattern) {
		// 日期格式
		DateFormat dfmt = new SimpleDateFormat(pattern);
		String dates = dfmt.format(new Date());
		return dates.substring(0, 8) + "01";
	}
	
	/**
	* @method: getLastMonth
	* @Description: 取得本月的上一个月，如：“2013/10”
	*
	* @return
	* @return String
	*
	* @author: ChenYW
	* @date 2013-11-5 下午06:12:33
	*/
	public static String getLastMonth() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		if (month == 1) {
			year = year - 1;
			month = month + 11;
		}else{
			month = month - 1;
		}

		return year + "/" + month;

	}

	/**
	* @method: getLastMonthByPattern
	* @Description: 取得本月的上一个月，如：“201310”
	*
	* @return
	* @return String
	*
	* @author: ChenYW
	* @date 2013-11-5 下午06:11:03
	*/
	public static String getLastMonthByPattern() {
		Calendar calendar = Calendar.getInstance();
		String lastMonth = "";
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		if (month == 1) {
			year = year - 1;
			month = month + 11;
		}else{
			month = month - 1;
		}
		
		if (month < 10) {
			lastMonth = year + "0" + month;

		} else {
			lastMonth = year + "" + month;
		}
		return lastMonth;

	}

	/**
	 * 获取指定格式的当前月的最后一天
	 * 
	 * @return String 日期格式
	 */
	public static String getLastDayOfMonth(String pattern) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		DateFormat dfmt = new SimpleDateFormat(pattern);
		return dfmt.format(calendar.getTime());
	}

	/**
	 * 获取指定格式的当前月的第一天
	 * 
	 * @return String
	 */
	public static String getFirstDayOfMonth(String pattern) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
		DateFormat dfmt = new SimpleDateFormat(pattern);
		return dfmt.format(calendar.getTime());
	}
	
	/**②
	 * 获得两个时间差，返回值为“秒”数
	 * @param d1
	 * @param d2
	 * @return 
	 */
	public static Long getDateDifferenceSeconds(Date d1, Date d2) {
		try {
			long diff = d1.getTime() - d2.getTime();
			long days = diff / (1000);
			return days;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获得两个时间差，返回值为“分钟”数
	 * @param d1
	 * @param d2
	 * @return 
	 */
	public static Long getDateDifferenceMins(Date d1, Date d2) {
		try {
			long diff = d1.getTime() - d2.getTime();
			long days = diff / (1000 * 60);
			return days;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获得两个时间差，返回值为“小时”数
	 * @param d1
	 * @param d2
	 * @return 
	 */
	public static Double getDateDifferenceHours(Date d1, Date d2) {
		try {
			Double diff = (double) d1.getTime() - d2.getTime();
			Double days =  diff / (1000 * 60 * 60.0);
			
			if(days <0.5 || days==null ){
				return 0.0;
			}
			Double oldtime = Math.floor(days);
			Double newtime = Math.floor(days-0.5);
			if(oldtime - newtime ==0){
				newtime+=0.5;
			}else{
				newtime+=1.0;
			}
			int i = 0;
			Double a = newtime;
			while(a-5 >0.5){
				a-=4;
				i++;
			}
			return newtime-i;
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	/**
	 * 把“源日历”的年月日设置到“目标日历”对象中
	 * 
	 * @param destCal
	 * @param sourceCal
	 */
	public static void copyYearMonthDay(Calendar destCal, Calendar sourceCal) {
		destCal.set(Calendar.YEAR, sourceCal.get(Calendar.YEAR));
		destCal.set(Calendar.MONTH, sourceCal.get(Calendar.MONTH));
		destCal.set(Calendar.DAY_OF_MONTH, sourceCal.get(Calendar.DAY_OF_MONTH));
	}

	/**③
	* @method: getDateString
	* @Description: 返回“指定日期”的“指定格式”的时间字符串
	*
	* @param date
	* @param pattern
	* @return
	* @return String
	*
	* @author ChenYW
	* @date 2013-5-22 上午10:19:14
	*/
	public static String getDateString(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		DateFormat vdfmt1 = new SimpleDateFormat(pattern);
		return vdfmt1.format(date);
	}
	/**
	 * 格式化日期为 英语时间格式 “月/日/年  时:分:秒  AM” ,
	 * 					      如：“10/14/2013 10:54:20 AM”
	 * 
	 * @return
	 */
	public static String formatEnDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

		return sdf.format(date).replaceAll("上午", "AM").replaceAll("下午", "PM");
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

	/**
	 * 根据日期字符串 格式化 对应日期值
	 * @param dateString
	 * @return
	 */
	public static Date parseDate(String dateString) {
		Date date = null;
		try {
			date = DateUtils.parseDate(dateString, new String[] {
					"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"});
		} catch (Exception ex) {
			logger.error("Pase the Date(" + dateString + ") occur errors:"
					+ ex.getMessage());
		}
		return date;
	}
	/**
	 * 将”指定时间“的字串转换成”指定格式“日期
	 * 
	 * @param 	  strDate  ”指定时间“的字串
	 *            String   ”指定格式“日期
	 * @return Date
	 * @throws ParseException
	 */
	public static Date getDate(String strDate, String pattern)
			throws ParseException {
		if(strDate==null||strDate.equals(""))return null;
		// 日期格式
		DateFormat dfmt = new SimpleDateFormat(pattern);
		return dfmt.parse(strDate);
	}
	/**
	 * 根据字符串“yyyy-mm-dd”，获取形如yyyyMMddHHmmss的当前日期字串
	 * 
	 * @return String
	 */
	public static String getYYYYMMDDHH24MISS(String strYYYY_MM_DD) {
		String tmp = "";
		try {
			Date d = getDate(strYYYY_MM_DD, "yyyy-MM-dd");
			DateFormat dfmt = new SimpleDateFormat("yyyyMMddHHmmss");
			return dfmt.format(d);
		} catch (ParseException e) {
			;
		}

		return tmp;
	}

	/**
	* @method: getYYYYMMDDHH24MISS
	* @Description: 根据日期，获取形如yyyyMMddHH24mmss的当前日期字串
	*
	* @param date
	* @return
	* @date 2014-3-10 下午04:48:14
	*/
	public static String getYYYYMMDDHH24MISS(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}
	
	/**
	* @method: getStringDate
	* @Description: 取得“时间字符串”转化为“指定格式”的时间字符串
	* 				如：getStringDate("2014-8-10 12:10:24","yyyy-MM-dd HH:mm:ss","yyyy年MM月dd日 HH:mm:ss")
	*
	* @param stringdate   时间字符串
	* @param fpattern	     时间字符串的格式
	* @param tpattern     要转化的指定格式
	* @return
	* @return String
	*
	* @author: ChenYW
	* @date 2013-11-5 下午06:31:02
	*/
	public static String getStringDate(String stringdate, String fpattern,
			String tpattern) {
		if (stringdate == null)
			return null;

		SimpleDateFormat formatter1 = new SimpleDateFormat(fpattern);
		SimpleDateFormat formatter2 = new SimpleDateFormat(tpattern);
		String dateString = "";
		try {
			Date date = formatter1.parse(stringdate.trim());
			dateString = formatter2.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateString;
	}
	
	/**④
	* @method: getMothsDate
	* @Description: 获取“指定日期”的“指定月数”后的时间
	*
	* @param date
	* @param moths
	* @return
	* @return Date
	*
	* @author: ChenYW
	* @date 2014-1-2 下午05:27:15
	*/
	public static Date getMothsDate(Date date,int moths) {

		Calendar cal = Calendar.getInstance(); // 取得今天的时间
		cal.setTime(date);
		cal.add(Calendar.MONTH, moths); // 算出moths个月以后的
		cal.get(Calendar.YEAR); // moths个月以前的年
		cal.get(Calendar.MONTH); // moths个月以前的月
		cal.get(Calendar.DATE);

		Date dateaftersixmonth = cal.getTime();
		
		return dateaftersixmonth;
	}
	/**
	* @method: getMonthAfter
	* @Description: 取指定日期的指定月数后的日期
	*/
	public static Date getMonthAfter(Date date, int months){
		try {
			Calendar now =Calendar.getInstance();  
			now.setTime(date);
			now.set(Calendar.MONTH, now.get(Calendar.MONTH)+months);
			
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			String[] ACCEPT_DATE_FORMATS = {
					"yyyy-MM-dd HH:mm:ss",
					"yyyy-MM-dd"
			};
			Date endDate;
			endDate = DateUtils.parseDate(sd.format(now.getTime()), ACCEPT_DATE_FORMATS);
			return endDate;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	 /** 
	  * 得到某天的几天后的时间 
	  * @param date 
	  * @param day 
	  * @return 
	  */  
	  public static Date getDateAfter(Date date,int day){  
	   Calendar now =Calendar.getInstance();  
	   now.setTime(date);  
	   now.set(Calendar.DATE,now.get(Calendar.DATE)+day);  
	   return now.getTime();  
	  }

	/**
	 * 根据是定天数，取指定天数后的日期字符串
	 * 
	 * @param days
	 *            和当前日期的差值（单位:天）
	 * @param pattern 返回日期格式
	 * @return
	 */
	public static String getDateString(int days, String pattern) {
		DateFormat dfmt = new SimpleDateFormat(pattern);
		long days2 = (long) days;
		return dfmt.format(new java.util.Date((new Date()).getTime() + 1000
				* 60 * 60 * 24 * days2));
	}
	  
  	/**
	 * 字符串 时间相减得到天数
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 */
	 public static long getDaySub(String beginDateStr,String endDateStr){
		 long day=0;
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		 Date beginDate;
		 Date endDate;
		 try{
			 beginDate = format.parse(beginDateStr);
			 endDate = format.parse(endDateStr); 
			 day = (endDate.getTime()-beginDate.getTime())/(24*60*60*1000); 
		 } catch (ParseException e){
			 e.printStackTrace();
		 } 
		 return day;
	 }

	/**
	 * 对给定时间加上相差时间后的，时间值
	 * 
	 * @param 输入的时间
	 * @param 与输入时间相差的天数
	 * @return Date
	 */
	public static Date appendDate(Date time, int n) throws ParseException {

		DateFormat mediumDateFormat = DateFormat.getDateTimeInstance();
		Date time1 = mediumDateFormat.parse(mediumDateFormat.format(time));
		time1.setDate(time1.getDate() + n);
		return time1;
	}

	/**
	* @method: appendDate
	* @Description: 获取“指定格式”的“指定时间”后“指定天数”的时间字符串
	*
	* @param beginvalue   输入的时间
	* @param pattern	     日期的格式
	* @param n			     与输入时间相差的天数
	* 
	* @throws ParseException
	* @return String
	*
	* @author: ChenYW
	* @date 2013-11-5 下午06:39:34
	*/
	public static String appendDate(String beginvalue, String pattern, int n)
			throws ParseException {

		DateFormat mediumDateFormat = DateFormat.getDateTimeInstance();
		Date time = getDate(beginvalue, pattern);
		Date time1 = mediumDateFormat.parse(mediumDateFormat.format(time));
		time1.setDate(time1.getDate() + n);
		return getDateString(time1, pattern);
	}

	/**
	 * 得到当前月的后month_num个月的帐期 例如当前为2005-09，返回上个月的帐期，则设置month_num为-1,返回为200508
	 * 
	 * 例如当前为2005-09，返回下个月的帐期，则设置month_num为1,返回为200510
	 * 例如当前为2006-1，返回上个月的帐期，则设置month_num为1,返回为200512
	 */
	public static String getAfterMonByMons(int month_num) {
		Calendar c1 = Calendar.getInstance();
		String result = "";
		c1.add(2, month_num);
		result = String.valueOf(c1.get(1));
		if ((c1.get(2) + 1) >= 10) {
			result = result + String.valueOf(c1.get(2) + 1);
		} else {
			result = result + "0" + String.valueOf(c1.get(2) + 1);
		}
		return result;
	}

	/**
	 * 获取当前日期的年数，如：“2013”
	 * 
	 */
	public static int getSysYear() {
		Calendar calendar = new GregorianCalendar();
		int iyear = calendar.get(Calendar.YEAR);
		return iyear;
	}
	
	/**
	* 取得当前月份数
	*/
	public static int getSysMonth() {
		Calendar calendar = new GregorianCalendar();
		int imonth = calendar.get(Calendar.MONTH) + 1;
		return imonth;
	}

	/**
	*取得当前月的第几天
	*/
	public static int getSysDay() {
		Calendar calendar = new GregorianCalendar();
		int idate = calendar.get(Calendar.DAY_OF_MONTH);
		return idate;
	}

	/**
	* @method: getTwoMonthNum
	* @Description: 取得两个字符串日期的相差的月份数
	*/
	public static int getTwoMonthNum(String startDate, String endDate) {
		int year1 = Integer.parseInt(startDate.substring(0, 4));
		int year2 = Integer.parseInt(endDate.substring(0, 4));
		int month1 = Integer.parseInt(startDate.substring(4, 6));
		int month2 = Integer.parseInt(endDate.substring(4, 6));
		return Math.abs(year1 - year2) * 12 - (month1 - month2) + 1;
	}

	/**
	* @method: isDateStr
	* @Description: 判断字符串是否是日期格式
	*
	* @param strDate   源日期字符串
	* @param pattern   日期转换格式
	* @return
	* @return boolean
	*
	* @author: ChenYW
	* @date 2013-11-5 下午06:21:59
	*/
	public static boolean isDateStr(String strDate, String pattern) {
		boolean tmp = true;

		try {
			getDate(strDate, pattern);
		} catch (ParseException e) {
			tmp = false;
		}

		return tmp;
	}
	/**
	* @method: getHourAfter
	* @Description: 得到某天的几小时后的时间 （24小时制）
	*/
	public static Date getHourAfter(Date date,int hours){  
	   Calendar now =Calendar.getInstance();  
	   now.setTime(date);  
	   now.set(Calendar.HOUR_OF_DAY,now.get(Calendar.HOUR_OF_DAY)+hours);  
	   return now.getTime();  
	}
	/**
	* @method: getWeekAfter
	* @Description: 得到某天的几个星期后的时间 
	*
	*/
	public static Date getWeekAfter(Date date,int day){  
	   Calendar now =Calendar.getInstance();  
	   now.setTime(date);  
	   now.set(Calendar.WEEK_OF_YEAR,now.get(Calendar.WEEK_OF_YEAR)+day);  
	   return now.getTime();  
	}
	
	/**
	* @method: getYearAfter
	* @Description: 得到某天的几年后的时间 
	*
	*/
	public static Date getYearAfter(Date date,int day){  
	   Calendar now =Calendar.getInstance();  
	   now.setTime(date);  
	   now.set(Calendar.YEAR,now.get(Calendar.YEAR)+day);  
	   return now.getTime();  
	}
	
	/**
	 * 格式化日期为 中文时间格式,如： “yyyy/MM”
	 * 
	 * @return
	 */
	public static String formatCnYearMonth(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		return sdf.format(date);
	}
	
	/**
	 * 根据指定时间，获取形如yyyyMMdd的日期字串
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	public static String getDateStringYYYYMMDD(Date date) {
		
		if(null == date){
			return "";
		}
		java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String strs = format1.format(date);
		return strs;
	}
	/**
	* @Description: 根据日期取星期
	* @Company:TGRF
	* @author:ChenYW
	* 
	* @date: 2013-11-5 上午09:27:35
	* @version V1.0
	*/
	public static String dayForWeek(Date pTime){  
		Calendar c = Calendar.getInstance();
		String week = null;
		c.setTime(pTime);
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			week = "[周日]";
		} else if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			week = "[周一]";
		} else if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			week = "[周二]";
		} else if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			week = "[周三]";
		} else if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			week = "[周四]";
		} else if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			week = "[周五]";
		} else if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			week = "[周六]";
		} else {
			week = "";
		}
		return week;
	}  
	
	/**
	* @method: compareDate
	* @Description: 比较两个日期的大小，只精确到日（前面日期大于等于后面日期，返回true；否则返回false）
	*
	* @param beforeDate
	* @param afterDate
	* @return
	* @return boolean
	*
	* @author: ChenYW
	* @date 2014-2-11 下午03:28:16
	*/
	public static boolean compareDate(Date beforeDate, Date afterDate){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		
		cal1.setTime(beforeDate);
		cal2.setTime(afterDate);
		
		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		int month1 = cal1.get(Calendar.MONTH);
		int month2 = cal2.get(Calendar.MONTH);
		int day1 = cal1.get(Calendar.DAY_OF_MONTH);
		int day2 = cal2.get(Calendar.DAY_OF_MONTH);
		
		if (year1 - year2 > 0) {
			return true;
		}else if (year1 - year2 == 0) {
			if (month1 - month2 > 0) {
				return true;
			}else if(month1 - month2 == 0){
				if (day1 - day2 >= 0) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * @method: getWeek
	 * @Description: 获取星期
	 * @param pTime
	 * @return
	 * @author : lig
	 * @date 2017-3-6 下午2:46:29
	 */
	public static String getWeek(Date pTime){  
		Calendar c = Calendar.getInstance();
		String week = null;
		c.setTime(pTime);
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			week = "WEEK_7";
		} else if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			week = "WEEK_1";
		} else if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			week = "WEEK_2";
		} else if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			week = "WEEK_3";
		} else if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			week = "WEEK_4";
		} else if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			week = "WEEK_5";
		} else if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			week = "WEEK_6";
		} else {
			week = "";
		}
		return week;
	}  
	
	/**
	 * @method: getAppointDate
	 * @Description: 获取当天指定时间
	 * @param dateStr 例如：当天9点-》09:00:00
	 * @return 2017-03-06 09:00:00
	 * @author : lig
	 * @date 2017-3-6 下午3:09:16
	 */
	public static Date getAppointDate(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = new Date();
			String ymd = sdf.format(d);
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.parse(ymd+" "+dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	} 
	
	public static String getLastMonth2() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		if (month == 1) {
			year = year - 1;
			month = month + 11;
		}else{
			month = month - 1;
		}

		return year + "-" + month + "-1";

	}
	
	public static void main(String[] args) throws ParseException {
		/*Date date2 = getDate("2014-02-11","yyyy-MM-dd");
		Date date1 = new Date();*/
		
		System.out.println(getLastMonth2());
	}
}
