package com.kssj.frame.command.sql;

/**
* @Description: DB -- the type of the operator
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-1-3 上午10:24:15
* @version V1.0
*/
public enum SqlSpellerOperatorType {
	
	LK,		/** like '%...%' */
	EQ,		/** = 			 */
	GT,		/** > 			 */
	GE,		/** >= 			 */
	LT,		/** < 			 */
	LE,		/** <= 			 */
	IN,
	NEQ,	/** <> 			 */
//	EMP,	/** empty 		 */
//	NOTEMP, /** not empty 	 */
	NULL, 	/** null 		 */
	NOTNULL;/** not null	 */
	
	/** 	
	 * get operator type of the sql
	 * 
	 *		   LT, GT, EQ, LE, GE, NEQ,   LK,   EMP,    NOTEMP, NULL, NOTNULL
	 * 分别代表    <,  >,  =, <=, >=,  <>, like, empty, not empty, null, not null的条件查询
	 *
	 * @param type 
	 * @return 
	 */
	public static SqlSpellerOperatorType getType(String type){
		if(type.toUpperCase().equals("LK")){
			return SqlSpellerOperatorType.LK;
		}else if(type.toUpperCase().equals("EQ")){
			return SqlSpellerOperatorType.EQ;
		}else if(type.toUpperCase().equals("GT")){
			return SqlSpellerOperatorType.GT;
		}else if(type.toUpperCase().equals("GE")){
			return SqlSpellerOperatorType.GE;
		}else if(type.toUpperCase().equals("LT")){
			return SqlSpellerOperatorType.LT;
		}else if(type.toUpperCase().equals("LE")){
			return SqlSpellerOperatorType.LE;
		}else if(type.toUpperCase().equals("IN")){
			return SqlSpellerOperatorType.IN;
		}
		
		else if(type.toUpperCase().equals("NEQ")){
			return SqlSpellerOperatorType.NEQ;
		}
//		else if(type.toUpperCase().equals("EMP")){
//			return SqlSpellerOperatorType.EMP;
//		}else if(type.toUpperCase().equals("NOTEMP")){
//			return SqlSpellerOperatorType.NOTEMP;
//		}
		else if(type.toUpperCase().equals("NULL")){
			return SqlSpellerOperatorType.NULL;
		}else if(type.toUpperCase().equals("NOTNULL")){
			return SqlSpellerOperatorType.NOTNULL;
		}
		
		else{
			throw new RuntimeException("错误的数据类型");
		}
	}
	
	
}
