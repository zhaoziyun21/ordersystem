package com.kssj.frame.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
* @Description: bean工具类
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午02:38:13
* @version V1.0
*/
public class MyBeanUtils {
	
	/**
	* @method: copyNotNullProperties
	* @Description: Copy original object's properties that not null to destination object
	*
	* @param dest  destination objet
	* @param orig  original object 
	* @throws Exception
	* @return void
	*
	* @author: ChenYW
	* @date 2013-10-14 下午02:33:26
	*/
	public static void copyNotNullProperties(Object dest,Object orig) throws Exception{
		if(orig.getClass() != dest.getClass()){
			throw new Exception("源对象和目的对象不是同一类型!");
		}
		//①getFields()获得某个类的所有的公共（public）的字段，包括父类。
		//②getDeclaredFields()获得某个类的所有申明的字段，即包括public、private和proteced，但是不包括父类的申明字段。 
		//Field[] fields = dest.getClass().getDeclaredFields(); 

		/*for(int i = 0 , j = fields.length ; i < j ; i++){
			String fieldName = fields[i].getName();
			
			String setter = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1,fieldName.length());
			String getter = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1,fieldName.length());
			Method setterMethod = dest.getClass().getMethod(setter, fields[i].getType());
			Method getterMethod = orig.getClass().getMethod(getter, null);
			if(getterMethod.invoke(orig, null) != null){
				setterMethod.invoke(dest, getterMethod.invoke(orig, null));			
			}
		}*/
	}
	
	
	/**
	* @method: setObjNullProperties
	* @Description: 根据对应的需要设置为空的属性名称数组（properties）；把目标对象的相应属性设置为空。
	*
	* @param obj			需要设置的对象
	* @param properties		需要设置为空的属性名称
	* 
	* @throws Exception
	* @return void
	*
	* @author: ChenYW
	* @date 2014-1-2 下午04:56:32
	*/
	public static void setObjNullProperties(Object obj,Object[] properties) throws Exception{
		if(properties!= null && properties.length != 0){
			for(Object propertyObj : properties){
				String property = propertyObj.toString();
				property = property.trim();
				Field field = obj.getClass().getDeclaredField(property);
				String setter = "set" + property.substring(0,1).toUpperCase() + property.substring(1,property.length());
				Method setterMethod = obj.getClass().getMethod(setter, field.getType());
				setterMethod.invoke(obj,new Object[]{null});
			}
		}
	}
}
