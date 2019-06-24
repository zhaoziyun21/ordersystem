package com.kssj.frame.command.sql;

import java.util.Calendar;

import org.apache.commons.lang.time.DateUtils;

import com.kssj.frame.util.CoreDateUtil;

/**
* @Description: SQLSERVER : the speller of a single sql condition
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-1-3 上午11:41:04
* @version V1.0
*/
public class SqlConditionSpellerForSqlServer extends SqlConditionSpeller {
	
	/**
	 * Constructor
	 * 
	 * @param property 
	 * @param operator 
	 * @param value 
	 * @param valueType 
	 */
	public SqlConditionSpellerForSqlServer(String property, SqlSpellerOperatorType operator, Object value,
			SqlSpellerDataType valueType) {
		super(property, operator, value, valueType, SqlSpellerDbType.SQLSERVER);
	}
	/**
	 * Constructor
	 * 
	 * @param property 
	 * @param operator 
	 * @param value 
	 * @param valueType 
	 */
	public SqlConditionSpellerForSqlServer(String property, String operator, Object value,
			String valueType) {
		super(property, SqlSpellerOperatorType.getType(operator), value, SqlSpellerDataType.getType(valueType), SqlSpellerDbType.SQLSERVER);
	}
	
	public SqlConditionSpellerForSqlServer(){
		
	}
	
	
	/**
	* @method: spell
	* @Description: extends the speller of the parent class
	*
	* @return
	* @throws Exception
	*
	* @author: ChenYW
	* @date 2014-1-3 下午02:16:36
	*/
	public String spell() throws Exception{
		return super.spell();
	}
	
	/**
	* @method: valueToString
	* @Description: get the value of the condition
	*
	* @param type
	* @param value
	* @return
	* @throws Exception
	*
	* @author: ChenYW
	* @date 2014-1-3 下午02:14:50
	*/
	public String valueToString(SqlSpellerDataType type,Object value) throws Exception{
		String reValue = null;
		if(type.equals(SqlSpellerDataType.S)){
			return " '" + value + "' ";
		}else if(type.equals(SqlSpellerDataType.N)){
			return value.toString();
		}else if(type.equals(SqlSpellerDataType.BD)){
			return value.toString();
		}else if(type.equals(SqlSpellerDataType.FT)){
			return value.toString();
		}else if(type.equals(SqlSpellerDataType.D)){
			return " '" + value + "' ";
		}
		
		else if(type.equals(SqlSpellerDataType.DL)){//DATE
			Calendar cal=Calendar.getInstance();
			cal.setTime(DateUtils.parseDate((String) value,new String[]{"yyyy-MM-dd"}));
			reValue=CoreDateUtil.formatCnDate(CoreDateUtil.setStartDay(cal).getTime());
			return " '" + reValue + "' ";
		}
		else if(type.equals(SqlSpellerDataType.DG)){//DATE
			Calendar cal=Calendar.getInstance();
			cal.setTime(DateUtils.parseDate((String) value,new String[]{"yyyy-MM-dd"}));
			reValue=CoreDateUtil.formatCnDate(CoreDateUtil.setEndDay(cal).getTime());
			return " '" + reValue + "' ";
		}
		
		else{
			throw new RuntimeException("错误的类型!");
		}
	}
	
}
