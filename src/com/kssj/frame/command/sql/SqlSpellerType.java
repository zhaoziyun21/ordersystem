package com.kssj.frame.command.sql;

/**
* @Description: sql : the speller type
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-1-3 上午09:57:43
* @version V1.0
*/
public enum SqlSpellerType {
	
	/** speller condition */
	CONDITION,
	/** speller sort */
	ORDER;
	
	/**
	 * get the speller type of the sql
	 * 
	 * @param type   : speller type(condition/order)
	 * @return 拼写器类型
	 */
	public static SqlSpellerType getType(String type){
		if(type.toUpperCase().equals("CONDITION")){
			return SqlSpellerType.CONDITION;
		}else if(type.toUpperCase().equals("ORDER")){
			return SqlSpellerType.ORDER;
		}else{
			throw new RuntimeException("错误的拼写器类型");
		}
	}
}
