package com.kssj.base.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
* @Description: reflact
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-15 下午12:59:14
* @version V1.0
*/
@SuppressWarnings("rawtypes")
public class ReflactUtil {
	
	/**
	 * 禁止实例化
	 */
	private ReflactUtil() {
	}

	
	/**
	* @method: getReflactClass
	* @Description: 根据类的全路径，获取对应的类
	*
	* @param classPath ：类的全路径
	* @return Object
	*
	* @author: ChenYW
	* @date 2014-6-6 下午2:59:48
	*/
	public static Object getReflactClass(String classPath) throws Exception {
		/*
		 * 实列化类 方法1
		 */
		Class cla = ReflactUtil.class.getClassLoader().loadClass(classPath);
		Object ob = cla.newInstance();
		
		return ob;
	}

	/**
	* @method: getReflactClass
	* @Description: 根据类对象，获取对应的属性和属性值
	*
	* @param classObj
	*
	* @author: ChenYW
	* @date 2014-6-6 下午3:03:12
	*/
	@SuppressWarnings("unchecked")
	public static Map getReflactClass(Object classObj) throws Exception {
		Map remap = new HashMap();
		//得到类对象
        Class userCla = (Class) classObj.getClass();
      
        //得到类中的所有属性集合
        Field[] fs = userCla.getDeclaredFields();
        for(int i = 0 ; i < fs.length; i++){
            //获取属性名
        	Field f = fs[i];
            //获取属性对应的值
            f.setAccessible(true); //设置些属性是可以访问的
            Object val = f.get(classObj);//得到此属性的值   
            
            System.out.println("name:"+f.getName()+"\t value = "+val);
            remap.put(f.getName(), val);
        }
      
		return remap;
	}
	
	/**
	* @method: getReflactMethod
	* @Description: 获取对应的method
	*
	* @param classObj     ：对象
	* @param fieldName	    ：该对象的属性名称
	* @param prefixMethod ：获取方法的前缀
	* @return
	* @return Method
	*
	* @author: ChenYW
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	* @date 2014-6-6 下午4:09:35
	*/
	public static Method getReflactMethod(Object classObj,String fieldName, String prefixMethod) throws Exception{
		//得到类对象
        Class Cla = (Class) classObj.getClass();
		
        //得到类中的方法
        Method[] methods = Cla.getMethods();
        for (Method method : methods) {
        	 if(method.getName().startsWith(prefixMethod)){
        		 
        	 }
		}
        
        for(int i = 0; i < methods.length; i++){
            Method method = methods[i];
            if(method.getName().startsWith(prefixMethod)){
               System.out.print("methodName:"+method.getName()+"\t");
               System.out.println("value:"+method.invoke(classObj));//得到get 方法的值
            }
        }
		
		return null;
	}
	
}
