package com.kssj.frame.se.command;

/**
* @Description: SE -- the type of the operator
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-1-3 上午10:24:15
* @version V1.0
*/
public enum SeSpellerOperatorType {
	
	LK,		/** like 		 */
	EQ,		/** = 			 */
	GLT,	/** > <		此时：参数名为一个，对应的值也是两个（前后）--以下滑线分隔 */
	GLE;	/** >= <= 	此时：参数名为一个，对应的值也是两个（前后）--以下滑线分隔  */
	
	/** 	
	 * get operator type of the sql
	 * 
	 * @param type 
	 * @return 
	 */
	public static SeSpellerOperatorType getType(String type){
		if(type.toUpperCase().equals("LK")){
			return SeSpellerOperatorType.LK;
		}else if(type.toUpperCase().equals("EQ")){
			return SeSpellerOperatorType.EQ;
		}else if(type.toUpperCase().equals("GLT")){
			return SeSpellerOperatorType.GLT;
		}else if(type.toUpperCase().equals("GLE")){
			return SeSpellerOperatorType.GLE;
		}else{
			throw new RuntimeException("错误的操作类型");
		}
	}
	
	
}
