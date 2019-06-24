package com.kssj.frame.command.sql;

import java.util.Calendar;

import org.apache.commons.lang.time.DateUtils;

import com.kssj.frame.util.CoreDateUtil;

/**
* @Description: ORACLE : the speller of a single sql confition
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-1-3 上午10:20:41
* @version V1.0
*/
public class SqlConditionSpellerForOracle extends SqlConditionSpeller {
	
	/**
	 * Constructor
	 * 
	 * @param property 
	 * @param operator 
	 * @param value 
	 * @param valueType 
	 */
	public SqlConditionSpellerForOracle(String property, SqlSpellerOperatorType operator, Object value,
			SqlSpellerDataType valueType) {
		super(property, operator, value, valueType, SqlSpellerDbType.ORACLE);
	}
	
	/**
	 * Constructor
	 * 
	 * @param property 
	 * @param operator 
	 * @param value 
	 * @param valueType 
	 */
	public SqlConditionSpellerForOracle(String property, String operator, Object value, String valueType) {
		super(property, SqlSpellerOperatorType.getType(operator), value, SqlSpellerDataType.getType(valueType), SqlSpellerDbType.ORACLE);
	}
	
	public SqlConditionSpellerForOracle(){
		
	}
	
	/*
	 * @see com.thd.util.queryfilter.SqlConditionSpeller#spell()
	 */
	public String spell() throws Exception{
		return super.spell();
	}
	
	/*
	 * @see com.thd.util.queryfilter.SqlConditionSpeller#valueToString(com.thd.util.queryfilter.SqlSpellerDataType, java.lang.Object)
	 */
	public String valueToString(SqlSpellerDataType type,Object value) throws Exception{
		String reValue = null;
		if(type.equals(SqlSpellerDataType.S)){//STRING
			return " '" + value + "' ";
		}else if(type.equals(SqlSpellerDataType.N)){//NUMBER
			return value.toString();
		}else if(type.equals(SqlSpellerDataType.BD)){
			return value.toString();
		}else if(type.equals(SqlSpellerDataType.FT)){
			return value.toString();
		}else if(type.equals(SqlSpellerDataType.D)){//DATE
			return " to_date('" + value + "','yyyy-mm-dd hh24:mi:ss') ";
		}
		
		else if(type.equals(SqlSpellerDataType.DL)){//DATE
			Calendar cal=Calendar.getInstance();
			cal.setTime(DateUtils.parseDate((String) value,new String[]{"yyyy-MM-dd"}));
			reValue=CoreDateUtil.formatCnDate(CoreDateUtil.setStartDay(cal).getTime());
			return " to_date('" + reValue + "','yyyy-mm-dd hh24:mi:ss') ";
		}
		else if(type.equals(SqlSpellerDataType.DG)){//DATE
			Calendar cal=Calendar.getInstance();
			cal.setTime(DateUtils.parseDate((String) value,new String[]{"yyyy-MM-dd"}));
			reValue=CoreDateUtil.formatCnDate(CoreDateUtil.setEndDay(cal).getTime());
			return " to_date('" + reValue + "','yyyy-mm-dd hh24:mi:ss') ";
		}
		
		else{
			throw new RuntimeException("错误的类型!");
		}
	}
}
