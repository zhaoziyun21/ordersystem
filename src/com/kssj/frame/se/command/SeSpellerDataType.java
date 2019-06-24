package com.kssj.frame.se.command;

/**
* @Description: data type
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-1-3 上午10:16:18
* @version V1.0
*/
public enum SeSpellerDataType {
	
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
	public static SeSpellerDataType getType(String type){
		if(type.toUpperCase().equals("S")){
			return SeSpellerDataType.S;
		}else if(type.toUpperCase().equals("D")){
			return SeSpellerDataType.D;
		}
		else if(type.toUpperCase().equals("DL")){
			return SeSpellerDataType.DL;
		}
		else if(type.toUpperCase().equals("DG")){
			return SeSpellerDataType.DG;
		}
		else if(type.toUpperCase().equals("N")){
			return SeSpellerDataType.N;
		}else if(type.toUpperCase().equals("FT")){
			return SeSpellerDataType.FT;
		}else if(type.toUpperCase().equals("BD")){
			return SeSpellerDataType.BD;
		}else{
			throw new RuntimeException("错误的数据类型");
		}
	}
	
}
