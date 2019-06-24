package com.kssj.frame.util;

import java.util.List;

/**
* @Description: 集合List工具类
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午02:28:12
* @version V1.0
*/
public class CoreListUtil {
	/**
	* @method: getOne
	* @Description: 取得集合里的第一个数据
	*
	* @param l
	* @return
	* @return Object
	*
	* @author: ChenYW
	* @date 2013-10-14 下午02:28:49
	*/
	public static Object getOne(List<Object> l ){
		if( (l!=null) && !l.isEmpty()){
			if(l.get(0)!=null){
				return l.get(0);
			}else{
				return null;
			}			
		}else{
			return null;
		}
	}
	
	/**
	* @method: isNotEmpty
	* @Description: 验证集合List不为空
	*
	* @param l
	* @return
	* @return boolean
	*
	* @author: ChenYW
	* @date 2013-10-14 下午02:29:08
	*/
	@SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(List l){
		return (l!=null && !l.isEmpty());
	}
}
