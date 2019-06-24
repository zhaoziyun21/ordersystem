package com.kssj.base.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateWeek {
	 public static void main(String[] args) {
		System.out.println( printWeekdays("31"));;
	    }
	 
	    private static final int FIRST_DAY = Calendar.SUNDAY;
	 
	    public static String  printWeekdays(String type) {
	        Calendar calendar = Calendar.getInstance();
	        setToFirstDay(calendar);
	        SimpleDateFormat dateFormat = new SimpleDateFormat("EE");
	        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	        String weekDate = "";
	        if("11".equals(type)){//星期一上午
	        	 for (int i = 0; i < 7; i++) {
	  	           // printDay(calendar);
	  	        	String week = dateFormat.format(calendar.getTime());
	  	        	if("星期一".equals(week)){
	  	        		weekDate = dateFormat1.format(calendar.getTime())+" 04:10:00";
	  	        		break;
	  	        	}
	  	           calendar.add(Calendar.DATE, 1);
	  	        }
	        }else if("12".equals(type)){//星期一下午
	        	 for (int i = 0; i < 7; i++) {
		  	           // printDay(calendar);
		  	        	String week = dateFormat.format(calendar.getTime());
		  	        	if("星期一".equals(week)){
		  	        		weekDate = dateFormat1.format(calendar.getTime())+" 15:10:00";
		  	        		break;
		  	        	}
		  	           calendar.add(Calendar.DATE, 1);
		  	        }
	        }else if("21".equals(type)){//星期二上午
	        	 for (int i = 0; i < 7; i++) {
		  	           // printDay(calendar);
		  	        	String week = dateFormat.format(calendar.getTime());
		  	        	if("星期二".equals(week)){
		  	        		weekDate = dateFormat1.format(calendar.getTime())+" 04:10:00";
		  	        		break;
		  	        	}
		  	           calendar.add(Calendar.DATE, 1);
		  	        }
	        }else if("22".equals(type)){//星期二下午
	        	 for (int i = 0; i < 7; i++) {
		  	           // printDay(calendar);
		  	        	String week = dateFormat.format(calendar.getTime());
		  	        	if("星期二".equals(week)){
		  	        		weekDate = dateFormat1.format(calendar.getTime())+" 15:10:00";
		  	        		break;
		  	        	}
		  	           calendar.add(Calendar.DATE, 1);
		  	        }
	        }else if("31".equals(type)){//星期三上午
	        	 for (int i = 0; i < 7; i++) {
		  	           // printDay(calendar);
		  	        	String week = dateFormat.format(calendar.getTime());
		  	        	if("星期三".equals(week)){
		  	        		weekDate = dateFormat1.format(calendar.getTime())+" 04:10:00";
		  	        		break;
		  	        	}
		  	           calendar.add(Calendar.DATE, 1);
		  	        }
	        }else if("32".equals(type)){//星期三下午
	        	 for (int i = 0; i < 7; i++) {
		  	           // printDay(calendar);
		  	        	String week = dateFormat.format(calendar.getTime());
		  	        	if("星期三".equals(week)){
		  	        		weekDate = dateFormat1.format(calendar.getTime())+" 15:10:00";
		  	        		break;
		  	        	}
		  	           calendar.add(Calendar.DATE, 1);
		  	        }
	        }else if("41".equals(type)){//星期四上午
	        	 for (int i = 0; i < 7; i++) {
		  	           // printDay(calendar);
		  	        	String week = dateFormat.format(calendar.getTime());
		  	        	if("星期四".equals(week)){
		  	        		weekDate = dateFormat1.format(calendar.getTime())+" 04:10:00";
		  	        		break;
		  	        	}
		  	           calendar.add(Calendar.DATE, 1);
		  	        }
	        }else if("42".equals(type)){//星期四下午
	        	 for (int i = 0; i < 7; i++) {
		  	           // printDay(calendar);
		  	        	String week = dateFormat.format(calendar.getTime());
		  	        	if("星期四".equals(week)){
		  	        		weekDate = dateFormat1.format(calendar.getTime())+" 15:10:00";
		  	        		break;
		  	        	}
		  	           calendar.add(Calendar.DATE, 1);
		  	        }
	        }else if("51".equals(type)){//星期五上午
	        	 for (int i = 0; i < 7; i++) {
		  	           // printDay(calendar);
		  	        	String week = dateFormat.format(calendar.getTime());
		  	        	if("星期五".equals(week)){
		  	        		weekDate = dateFormat1.format(calendar.getTime())+" 04:10:00";
		  	        		break;
		  	        	}
		  	           calendar.add(Calendar.DATE, 1);
		  	        }
	        }else if("52".equals(type)){//星期五下午
	        	 for (int i = 0; i < 7; i++) {
		  	           // printDay(calendar);
		  	        	String week = dateFormat.format(calendar.getTime());
		  	        	if("星期五".equals(week)){
		  	        		weekDate = dateFormat1.format(calendar.getTime())+" 15:10:00";
		  	        		break;
		  	        	}
		  	           calendar.add(Calendar.DATE, 1);
		  	        }
	        }else if("61".equals(type)){//星期六上午
	        	 for (int i = 0; i < 7; i++) {
		  	           // printDay(calendar);
		  	        	String week = dateFormat.format(calendar.getTime());
		  	        	if("星期六".equals(week)){
		  	        		weekDate = dateFormat1.format(calendar.getTime())+" 04:10:00";
		  	        		break;
		  	        	}
		  	           calendar.add(Calendar.DATE, 1);
		  	        }
	        }else if("62".equals(type)){//星期六下午
	        	 for (int i = 0; i < 7; i++) {
		  	           // printDay(calendar);
		  	        	String week = dateFormat.format(calendar.getTime());
		  	        	if("星期六".equals(week)){
		  	        		weekDate = dateFormat1.format(calendar.getTime())+" 15:10:00";
		  	        		break;
		  	        	}
		  	           calendar.add(Calendar.DATE, 1);
		  	        }
	        }else if("71".equals(type)){//星期日上午
	        	 for (int i = 0; i < 7; i++) {
		  	           // printDay(calendar);
		  	        	String week = dateFormat.format(calendar.getTime());
		  	        	if("星期日".equals(week)){
		  	        		weekDate = dateFormat1.format(calendar.getTime())+" 04:10:00";
		  	        		break;
		  	        	}
		  	           calendar.add(Calendar.DATE, 1);
		  	        }
	        }else if("72".equals(type)){//星期日下午
	        	 for (int i = 0; i < 7; i++) {
		  	           // printDay(calendar);
		  	        	String week = dateFormat.format(calendar.getTime());
		  	        	if("星期日".equals(week)){
		  	        		weekDate = dateFormat1.format(calendar.getTime())+" 15:10:00";
		  	        		break;
		  	        	}
		  	           calendar.add(Calendar.DATE, 1);
		  	        }
	        }
	        
	        
			return weekDate;
	    }
	 
	    private static void setToFirstDay(Calendar calendar) {
	        while (calendar.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {
	            calendar.add(Calendar.DATE, -1);
	        }
	    }
	 
	/*    private static String printDay(Calendar calendar) {
	        SimpleDateFormat weekDate = new SimpleDateFormat("EE");
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd EE");
	        System.out.println(dateFormat.format(calendar.getTime()));
	        String week = weekDate.format(calendar.getTime());
			return null;
	    }*/
	 
	    public static String dinAndLun(String type){
	    	String dinAndLun ="";
	    	if("11".equals(type)){
	    		dinAndLun =  "WEEK_1_LUN";
	    	}else if("12".equals(type)){
	    		dinAndLun = "WEEK_1_DIN";
	    	}else if("21".equals(type)){
	    		dinAndLun = "WEEK_2_LUN";
	    	}else if("22".equals(type)){
	    		dinAndLun = "WEEK_2_DIN";
	    	}else if("31".equals(type)){
	    		dinAndLun = "WEEK_3_LUN";
	    	}else if("32".equals(type)){
	    		dinAndLun = "WEEK_3_DIN";
	    	}else if("41".equals(type)){
	    		dinAndLun = "WEEK_4_LUN";
	    	}else if("42".equals(type)){
	    		dinAndLun = "WEEK_4_DIN";
	    	}else if("51".equals(type)){
	    		dinAndLun = "WEEK_5_LUN";
	    	}else if("52".equals(type)){
	    		dinAndLun = "WEEK_5_DIN";
	    	}else if("61".equals(type)){
	    		dinAndLun = "WEEK_6_LUN";
	    	}else if("62".equals(type)){
	    		dinAndLun = "WEEK_6_DIN";
	    	}else if("71".equals(type)){
	    		dinAndLun = "WEEK_7_LUN";
	    	}else if("72".equals(type)){
	    		dinAndLun = "WEEK_7_DIN";
	    	}
			return dinAndLun;
	    }
	    
	    
}
