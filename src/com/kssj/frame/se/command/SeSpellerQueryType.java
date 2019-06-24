package com.kssj.frame.se.command;

/**
* @Description: SE -- the type of the operator
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-1-3 上午10:24:15
* @version V1.0
*/
public enum SeSpellerQueryType {
	
	AND,	/** and 	 */
	OR,		/** or		 */
	NOT;	/** is not	 */
	
	/** 	
	 * get operator type of the sql
	 * 
	 * @param type 
	 * @return 
	 */
	public static SeSpellerQueryType getType(String type){
		if(type.toUpperCase().equals("AND")){
			return SeSpellerQueryType.AND;
		}else if(type.toUpperCase().equals("OR")){
			return SeSpellerQueryType.OR;
		}else if(type.toUpperCase().equals("NOT")){
			return SeSpellerQueryType.NOT;
		}else{
			throw new RuntimeException("错误的关联查询类型");
		}
	}
	
	
}
