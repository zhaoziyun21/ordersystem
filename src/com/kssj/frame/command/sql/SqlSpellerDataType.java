package com.kssj.frame.command.sql;

/**
* @Description: data type
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-1-3 上午10:16:18
* @version V1.0
*/
public enum SqlSpellerDataType {
	
	S,	/** String */
	D,	/** date*/
	DL, /** date : yyyy MM dd 00:00:00*/
	DG,	/** date : yyyy MM dd 59:59:59*/
	N,	/** Integer */
	FT,	/** float */
	BD; /** BigDecimal */
	
	/**
	 * get the data type
	 * 
	 * @param type :data type
	 * @return 	data type
	 */
	public static SqlSpellerDataType getType(String type){
		if(type.toUpperCase().equals("S")){
			return SqlSpellerDataType.S;
		}else if(type.toUpperCase().equals("D")){
			return SqlSpellerDataType.D;
		}
		else if(type.toUpperCase().equals("DL")){
			return SqlSpellerDataType.DL;
		}
		else if(type.toUpperCase().equals("DG")){
			return SqlSpellerDataType.DG;
		}
		else if(type.toUpperCase().equals("N")){
			return SqlSpellerDataType.N;
		}else if(type.toUpperCase().equals("FT")){
			return SqlSpellerDataType.FT;
		}else if(type.toUpperCase().equals("BD")){
			return SqlSpellerDataType.BD;
		}else{
			throw new RuntimeException("错误的数据类型");
		}
	}
	
}
