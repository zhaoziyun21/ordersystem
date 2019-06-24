package com.kssj.frame.command.sql;

/**
* @Description: SQL : a speller interface of the single
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-1-3 下午04:23:41
* @version V1.0
*/
public interface SqlSpeller {
	/**
	 * SQL : spell order statement
	 * 
	 * @return  the statement of the after "order by"
	 * @throws Exception 
	 */
	public String spell()  throws Exception;
	
	/**
	 * SQL : get the type of the speller 
	 * 
	 * @return sql speller type
	 */
	public SqlSpellerType getSpellType();
	/**
	 * 获取属性名称
	 * @return 属性名称
	 */
	public String getProperty();
}
