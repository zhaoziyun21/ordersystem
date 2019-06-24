package com.kssj.frame.se.command;

import java.util.Calendar;

import org.apache.commons.lang.time.DateUtils;

import com.kssj.frame.util.CoreDateUtil;

/**
* @Description: SE: the speller of a single confition
* 
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-1-3 上午09:54:27
* @version V1.0
*/
public class SeConditionSpeller{
	
	//SO：front spell is "Q|属性名称|属性类型|操作类型|查询类型"，如：Q|userName|S|EQ|AND
	
	private String property;				/** property name */
	private Object value;					/** value 		  */
	private SeSpellerOperatorType operator; /** operator 	  */
	private SeSpellerDataType valueType;	/** value type 	  */
	private SeSpellerQueryType queryType;	/** query type 	  */
	
	/** speller type */
	private SeSpellerType spellType = SeSpellerType.CONDITION;
	

	public SeConditionSpeller(){}
	
	/**
	 * 	constructor
	 * 
	 * @param property 
	 * @param operator 
	 * @param value 
	 * @param valueType 
	 * @param dbType 
	 */
	public SeConditionSpeller(String property, SeSpellerOperatorType operator, Object value,SeSpellerDataType valueType,SeSpellerQueryType queryType) {
		this.property = property;
		this.operator = operator;
		this.value = value;
		this.valueType = valueType;
		this.queryType = queryType;
	}
	
	/**
	 * ①.spell a confitional statements of the SE--Q|userName|S|EQ|AND	
	 * 
	 * "(title:"+content+" OR content:"+content+")"
	 */
	public String spell() throws Exception{
		StringBuffer sb = new StringBuffer();
		if (queryType.equals(SeSpellerQueryType.AND)) {
			String[] valueArr = value.toString().split("__");
			
			sb.append(" AND " + property);
			
			if(operator.equals(SeSpellerOperatorType.LK))
			{
				sb.append(":*" + valueToString(valueType,valueArr[0]) + "*");
			}
			else if(operator.equals(SeSpellerOperatorType.EQ))
			{
				sb.append(":" + valueToString(valueType,valueArr[0]) );
			}
			else if(operator.equals(SeSpellerOperatorType.GLT))
			{
				if (valueArr[0].toString().trim().startsWith("_")) {
					String[] values = valueArr[0].toString().split("_");
					sb.append(":{* TO " + valueToString(valueType,values[1]) +"}");
				}else if (valueArr[0].toString().trim().endsWith("_")) {
					String[] values = valueArr[0].toString().split("_");
					sb.append(":{" + valueToString(valueType,values[0]) + " TO NOW }");
				}else {
					String[] values = valueArr[0].toString().split("_");
					sb.append(":{" + valueToString(valueType,values[0]) + " TO " + valueToString(valueType,values[1]) +"}");
				}
			}
			else if(operator.equals(SeSpellerOperatorType.GLE))
			{
				if (valueArr[0].toString().trim().startsWith("_")) {
					String[] values = valueArr[0].toString().split("_");
					sb.append(":[* TO " + valueToString(valueType,values[1]) +"]");
				}else if (valueArr[0].toString().trim().endsWith("_")) {
					String[] values = valueArr[0].toString().split("_");
					sb.append(":[" + valueToString(valueType,values[0]) + " TO NOW ]");
				}else {
					String[] values = valueArr[0].toString().split("_");
					sb.append(":[" + valueToString(valueType,values[0]) + " TO " + valueToString(valueType,values[1]) +"]");
				}
			}
			else
			{
				throw new RuntimeException("操作类型错误!");
			}
			
			if (value.toString().contains("__")) {
				sb.append(" AND " + property);
				
				if(operator.equals(SeSpellerOperatorType.LK))
				{
					sb.append(":*" + valueToString(valueType,valueArr[1]) + "*");
				}
				else if(operator.equals(SeSpellerOperatorType.EQ))
				{
					sb.append(":" + valueToString(valueType,valueArr[1]) );
				}
				else if(operator.equals(SeSpellerOperatorType.GLT))
				{
					if (valueArr[1].toString().trim().startsWith("_")) {
						String[] values = valueArr[1].toString().split("_");
						sb.append(":{* TO " + valueToString(valueType,values[1]) +"}");
					}else if (valueArr[1].toString().trim().endsWith("_")) {
						String[] values = valueArr[1].toString().split("_");
						sb.append(":{" + valueToString(valueType,values[0]) + " TO NOW }");
					}else {
						String[] values = valueArr[1].toString().split("_");
						sb.append(":{" + valueToString(valueType,values[0]) + " TO " + valueToString(valueType,values[1]) +"}");
					}
				}
				else if(operator.equals(SeSpellerOperatorType.GLE))
				{
					if (valueArr[1].toString().trim().startsWith("_")) {
						String[] values = valueArr[1].toString().split("_");
						sb.append(":[* TO " + valueToString(valueType,values[1]) +"]");
					}else if (valueArr[1].toString().trim().endsWith("_")) {
						String[] values = valueArr[1].toString().split("_");
						sb.append(":[" + valueToString(valueType,values[0]) + " TO NOW ]");
					}else {
						String[] values = valueArr[1].toString().split("_");
						sb.append(":[" + valueToString(valueType,values[0]) + " TO " + valueToString(valueType,values[1]) +"]");
					}
				}
				else{
					throw new RuntimeException("操作类型错误!");
				}
			}
		}else if (queryType.equals(SeSpellerQueryType.OR)) {
			String[] valueArr = value.toString().split("__");
			
			sb.append(" AND (");
			sb.append(property);
			
			if(operator.equals(SeSpellerOperatorType.LK))
			{
				sb.append(":*" + valueToString(valueType,valueArr[0]) + "*");
			}
			else if(operator.equals(SeSpellerOperatorType.EQ))
			{
				sb.append(":" + valueToString(valueType,valueArr[0]) );
			}
			else if(operator.equals(SeSpellerOperatorType.GLT))
			{
				if (valueArr[0].toString().trim().startsWith("_")) {
					String[] values = valueArr[0].toString().split("_");
					sb.append(":{* TO " + valueToString(valueType,values[1]) +"}");
				}else if (valueArr[0].toString().trim().endsWith("_")) {
					String[] values = valueArr[0].toString().split("_");
					sb.append(":{" + valueToString(valueType,values[0]) + " TO NOW }");
				}else {
					String[] values = valueArr[0].toString().split("_");
					sb.append(":{" + valueToString(valueType,values[0]) + " TO " + valueToString(valueType,values[1]) +"}");
				}
			}
			else if(operator.equals(SeSpellerOperatorType.GLE))
			{
				if (valueArr[0].toString().trim().startsWith("_")) {
					String[] values = valueArr[0].toString().split("_");
					sb.append(":[* TO " + valueToString(valueType,values[1]) +"]");
				}else if (valueArr[0].toString().trim().endsWith("_")) {
					String[] values = valueArr[0].toString().split("_");
					sb.append(":[" + valueToString(valueType,values[0]) + " TO NOW ]");
				}else {
					String[] values = valueArr[0].toString().split("_");
					sb.append(":[" + valueToString(valueType,values[0]) + " TO " + valueToString(valueType,values[1]) +"]");
				}
			}
			else{
				throw new RuntimeException("操作类型错误!");
			}
			
			sb.append(" OR " + property);
			
			if(operator.equals(SeSpellerOperatorType.LK))
			{
				sb.append(":*" + valueToString(valueType,valueArr[1]) + "*");
			}
			else if(operator.equals(SeSpellerOperatorType.EQ))
			{
				sb.append(":" + valueToString(valueType,valueArr[1]) );
			}
			else if(operator.equals(SeSpellerOperatorType.GLT))
			{
				if (valueArr[1].toString().trim().startsWith("_")) {
					String[] values = valueArr[1].toString().split("_");
					sb.append(":{* TO " + valueToString(valueType,values[1]) +"}");
				}else if (valueArr[1].toString().trim().endsWith("_")) {
					String[] values = valueArr[1].toString().split("_");
					sb.append(":{" + valueToString(valueType,values[0]) + " TO NOW }");
				}else {
					String[] values = valueArr[1].toString().split("_");
					sb.append(":{" + valueToString(valueType,values[0]) + " TO " + valueToString(valueType,values[1]) +"}");
				}
			}
			else if(operator.equals(SeSpellerOperatorType.GLE))
			{
				if (valueArr[1].toString().trim().startsWith("_")) {
					String[] values = valueArr[1].toString().split("_");
					sb.append(":[* TO " + valueToString(valueType,values[1]) +"]");
				}else if (valueArr[1].toString().trim().endsWith("_")) {
					String[] values = valueArr[1].toString().split("_");
					sb.append(":[" + valueToString(valueType,values[0]) + " TO NOW ]");
				}else {
					String[] values = valueArr[1].toString().split("_");
					sb.append(":[" + valueToString(valueType,values[0]) + " TO " + valueToString(valueType,values[1]) +"]");
				}
			}
			else{
				throw new RuntimeException("操作类型错误!");
			}
			sb.append(")");
		}else if (queryType.equals(SeSpellerQueryType.NOT)) {//category:研究成果类 AND (NOT name:系统) AND type:文档
			if (value.toString().contains("__")) {
				String[] valueArr = value.toString().split("__");
				
				sb.append(" AND " + property);
				if(operator.equals(SeSpellerOperatorType.LK))
				{
					sb.append(":*" + valueToString(valueType,valueArr[0]) + "*");
				}
				else if(operator.equals(SeSpellerOperatorType.EQ))
				{
					sb.append(":" + valueToString(valueType,valueArr[0]) );
				}
				else if(operator.equals(SeSpellerOperatorType.GLT))
				{
					if (valueArr[0].toString().trim().startsWith("_")) {
						String[] values = valueArr[0].toString().split("_");
						sb.append(":{* TO " + valueToString(valueType,values[1]) +"}");
					}else if (valueArr[0].toString().trim().endsWith("_")) {
						String[] values = valueArr[0].toString().split("_");
						sb.append(":{" + valueToString(valueType,values[0]) + " TO NOW }");
					}else {
						String[] values = valueArr[0].toString().split("_");
						sb.append(":{" + valueToString(valueType,values[0]) + " TO " + valueToString(valueType,values[1]) +"}");
					}
				}
				else if(operator.equals(SeSpellerOperatorType.GLE))
				{
					if (valueArr[0].toString().trim().startsWith("_")) {
						String[] values = valueArr[0].toString().split("_");
						sb.append(":[* TO " + valueToString(valueType,values[1]) +"]");
					}else if (valueArr[0].toString().trim().endsWith("_")) {
						String[] values = valueArr[0].toString().split("_");
						sb.append(":[" + valueToString(valueType,values[0]) + " TO NOW ]");
					}else {
						String[] values = valueArr[0].toString().split("_");
						sb.append(":[" + valueToString(valueType,values[0]) + " TO " + valueToString(valueType,values[1]) +"]");
					}
				}
				else{
					throw new RuntimeException("操作类型错误!");
				}
				
				sb.append(" NOT " + property);
				if(operator.equals(SeSpellerOperatorType.LK))
				{
					sb.append(":*" + valueToString(valueType,valueArr[1]) + "*");
				}
				else if(operator.equals(SeSpellerOperatorType.EQ))
				{
					sb.append(":" + valueToString(valueType,valueArr[1]) );
				}
				else if(operator.equals(SeSpellerOperatorType.GLT))
				{
					if (valueArr[1].toString().trim().startsWith("_")) {
						String[] values = valueArr[1].toString().split("_");
						sb.append(":{* TO " + valueToString(valueType,values[1]) +"}");
					}else if (valueArr[1].toString().trim().endsWith("_")) {
						String[] values = valueArr[1].toString().split("_");
						sb.append(":{" + valueToString(valueType,values[0]) + " TO NOW }");
					}else {
						String[] values = valueArr[1].toString().split("_");
						sb.append(":{" + valueToString(valueType,values[0]) + " TO " + valueToString(valueType,values[1]) +"}");
					}
				}
				else if(operator.equals(SeSpellerOperatorType.GLE))
				{
					if (valueArr[1].toString().trim().startsWith("_")) {
						String[] values = valueArr[1].toString().split("_");
						sb.append(":[* TO " + valueToString(valueType,values[1]) +"]");
					}else if (valueArr[1].toString().trim().endsWith("_")) {
						String[] values = valueArr[1].toString().split("_");
						sb.append(":[" + valueToString(valueType,values[0]) + " TO NOW ]");
					}else {
						String[] values = valueArr[1].toString().split("_");
						sb.append(":[" + valueToString(valueType,values[0]) + " TO " + valueToString(valueType,values[1]) +"]");
					}
				}
				else{
					throw new RuntimeException("操作类型错误!");
				}
			}else {
				sb.append(" AND (NOT " + property);
				
				if(operator.equals(SeSpellerOperatorType.LK))
				{
					sb.append(":*" + valueToString(valueType,value) + "*");
				}
				else if(operator.equals(SeSpellerOperatorType.EQ))
				{
					sb.append(":" + valueToString(valueType,value) );
				}
				else if(operator.equals(SeSpellerOperatorType.GLT))
				{
					if (value.toString().trim().startsWith("_")) {
						String[] values = value.toString().split("_");
						sb.append(":{* TO " + valueToString(valueType,values[1]) +"}");
					}else if (value.toString().trim().endsWith("_")) {
						String[] values = value.toString().split("_");
						sb.append(":{" + valueToString(valueType,values[0]) + " TO NOW }");
					}else {
						String[] values = value.toString().split("_");
						sb.append(":{" + valueToString(valueType,values[0]) + " TO " + valueToString(valueType,values[1]) +"}");
					}
				}
				else if(operator.equals(SeSpellerOperatorType.GLE))
				{
					if (value.toString().trim().startsWith("_")) {
						String[] values = value.toString().split("_");
						sb.append(":[* TO " + valueToString(valueType,values[1]) +"]");
					}else if (value.toString().trim().endsWith("_")) {
						String[] values = value.toString().split("_");
						sb.append(":[" + valueToString(valueType,values[0]) + " TO NOW ]");
					}else {
						String[] values = value.toString().split("_");
						sb.append(":[" + valueToString(valueType,values[0]) + " TO " + valueToString(valueType,values[1]) +"]");
					}
				}
				else{
					throw new RuntimeException("操作类型错误!");
				}
				sb.append(")");
			}
			
		}else {
			throw new RuntimeException("关联操作错误!");
		}
		
		return sb.toString();
	}
	/**
	 * 把值转换成sql语句
	 * @param type 值类型
	 * @param value 值
	 * @return 
	 * @throws Exception
	 */
    public String valueToString(SeSpellerDataType type,Object value) throws Exception{
		String reValue = null;
		if(type.equals(SeSpellerDataType.S)){
			return value.toString();
		}else if(type.equals(SeSpellerDataType.N)){
			return value.toString();
		}else if(type.equals(SeSpellerDataType.BD)){
			return value.toString();
		}else if(type.equals(SeSpellerDataType.FT)){
			return value.toString();
		}else if(type.equals(SeSpellerDataType.D)){
			return " '" + value + "' ";
		}
		
		else if(type.equals(SeSpellerDataType.DL)){//DATE
			Calendar cal=Calendar.getInstance();
			cal.setTime(DateUtils.parseDate((String) value,new String[]{"yyyy-MM-dd"}));
			reValue=CoreDateUtil.formatCnDate(CoreDateUtil.setStartDay(cal).getTime());
			return " '" + reValue + "' ";
		}
		else if(type.equals(SeSpellerDataType.DG)){//DATE
			Calendar cal=Calendar.getInstance();
			cal.setTime(DateUtils.parseDate((String) value,new String[]{"yyyy-MM-dd"}));
			reValue=CoreDateUtil.formatCnDate(CoreDateUtil.setEndDay(cal).getTime());
			return " '" + reValue + "' ";
		}
		
		else{
			throw new RuntimeException("错误的类型!");
		}
	}
	
	
	/**
	 * 2.获取拼写器类型
	 * @return 拼写器类型
	 */
	public SeSpellerType getSpellType() {
		return spellType;
	}
	
	
	
	/**====================================================================
	 * 3.
	 */
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public SeSpellerOperatorType getOperator() {
		return operator;
	}
	public void setOperator(SeSpellerOperatorType operator) {
		this.operator = operator;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public SeSpellerDataType getValueType() {
		return valueType;
	}
	public void setValueType(SeSpellerDataType valueType) {
		this.valueType = valueType;
	}
}
