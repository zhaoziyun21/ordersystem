package com.kssj.base.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.kssj.base.listener.AppUtil;
import com.kssj.base.struts.BeanDateConnverter;
import com.kssj.frame.dao.impl.DynamicDaoImpl;
import com.kssj.frame.service.DynamicService;
import com.kssj.frame.service.impl.DynamicServiceImpl;


/**
* @Description: #FrameWork# 对实体类操作用
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-24 下午05:12:55
* @version V1.0
*/
@SuppressWarnings("rawtypes")
public class BeanUtil {
	private static Log logger = LogFactory.getLog(BeanUtil.class);
	/**
	 * BeanUtil类型转换器
	 */
	public static ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
	
	static{
		convertUtilsBean.register(new BeanDateConnverter(), Date.class);
		convertUtilsBean.register(new LongConverter(null), Long.class);
	}
	
	/**
	* @method: copyNotNullProperties
	* @Description: 拷贝一个bean中的非空属性于另一个bean中(同类型的Object: bean/Map)
	*
	* @param dest ： 被赋值的bean
	* @param orig ： 用于赋值的bean
	* 
	* @throws IllegalAccessException
	* @throws InvocationTargetException
	* @return void
	*
	* @author: ChenYW
	* @date 2014-3-5 上午09:18:53
	*/
	public static void copyNotNullProperties(Object dest, Object orig) throws IllegalAccessException, InvocationTargetException {
		BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
		// Validate existence of the specified beans
		if (dest == null) {
			throw new IllegalArgumentException("No destination bean specified");
		}
		if (orig == null) {
			throw new IllegalArgumentException("No origin bean specified");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("BeanUtils.copyProperties(" + dest + ", " + orig+ ")");
		}

		// Copy the properties, converting as necessary
		if (orig instanceof DynaBean) {
			DynaProperty[] origDescriptors = ((DynaBean) orig).getDynaClass()
															  .getDynaProperties();
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				// Need to check isReadable() for WrapDynaBean
				// (see Jira issue# BEANUTILS-61)
				if (beanUtils.getPropertyUtils().isReadable(orig, name)
						&& beanUtils.getPropertyUtils().isWriteable(dest, name)) {
					Object value = ((DynaBean) orig).get(name);
					beanUtils.copyProperty(dest, name, value);
				}
			}
		} else if (orig instanceof Map) {
			Iterator entries = ((Map) orig).entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String name = (String) entry.getKey();
				if (beanUtils.getPropertyUtils().isWriteable(dest, name)) {
					beanUtils.copyProperty(dest, name, entry.getValue());
				}
			}
		} else /* if (orig is a standard JavaBean) */{
			PropertyDescriptor[] origDescriptors = beanUtils.getPropertyUtils()
															.getPropertyDescriptors(orig);
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if ("class".equals(name)) {
					continue; // No point in trying to set an object's class
				}
				if (beanUtils.getPropertyUtils().isReadable(orig, name)
						&& beanUtils.getPropertyUtils().isWriteable(dest, name)) {
					try {
						Object value = beanUtils.getPropertyUtils()
								.getSimpleProperty(orig, name);
						if (value != null) {
							beanUtils.copyProperty(dest, name, value);
						}
					} catch (NoSuchMethodException e) {
						// Should not happen
					}
				}
			}
		}

	}
	
	/**
	 * 通过Request构造实体
	 * 
	 * @param request 请求对象
	 * @param entity 实体对象
	 * @param preName 请求中的前缀变量
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	public static Object populateEntity(HttpServletRequest request,Object entity,String preName)
	throws IllegalAccessException, InvocationTargetException{
	    HashMap map = new HashMap();
	    Enumeration<String> names = request.getParameterNames();
	    while (names.hasMoreElements()) {
	      String name = (String) names.nextElement();
	      //属性名称
	      String propetyName=name;
	      if(StringUtils.isNotEmpty(preName)){
	    	  propetyName=propetyName.replace(preName+".", "");
	      }
	      map.put(propetyName, request.getParameterValues(name));
	    }
	    getBeanUtils().populate(entity, map);
		return entity;
	}

	// 取得动态DynamicService
	public static DynamicService getDynamicServiceBean(String entityName){
		org.springframework.orm.hibernate3.LocalSessionFactoryBean sessionFactory=(LocalSessionFactoryBean) AppUtil.getBean("&sessionFactory");
		DynamicDaoImpl dao = new DynamicDaoImpl(entityName);
		dao.setSessionFactory((SessionFactory)sessionFactory.getObject());
		dao.setEntityClassName(entityName);
		DynamicServiceImpl service = new DynamicServiceImpl(dao);
		return service;
	}

	/**
	 * 转化类型
	 * @param convertUtil
	 * @param value
	 * @param type
	 * @return
	 */
	public static Object convertValue(ConvertUtilsBean convertUtil,Object value,Class type){
		
		Converter converter = convertUtil.lookup(type);
		if(converter==null) return value;
		
		Object newValue=null;
		if (value instanceof String) {
		    newValue = converter.convert(type,(String) value);
		} else if (value instanceof String[]) {
		    newValue = converter.convert(type,((String[]) value)[0]);
		} else {
		    newValue = converter.convert(type,value);
		}
		return newValue;
	}

	/**
	 * 取得能转化类型的bean
	 * 
	 * @return
	 */
	public static BeanUtilsBean getBeanUtils() {
		BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean,new PropertyUtilsBean());
		return beanUtilsBean;
	}
	
	/**
	 * 返回请求中的所有的对应的Map值
	 * @param request
	 * @return
	 */
	public static Map getMapFromRequest(HttpServletRequest request){
	    	Map reqMap=request.getParameterMap();
	   	
	    	HashMap<String,Object> datas=new HashMap<String,Object>();
		Iterator it = reqMap.entrySet().iterator();
		
		while(it.hasNext()){
			Map.Entry entry = (Map.Entry)it.next();
			String key=(String)entry.getKey();
			String[] val=(String[])entry.getValue();
			if(val.length==1){
			    datas.put(key, val[0]);
			}else{
			    datas.put(key,val);
			}
		}
		return datas;
	}
	
//-----------------------------TMS:new--------------------------------
	
	//TODO 执行某类的某方法
	@SuppressWarnings("unchecked")
	public static Object invokePrivateMethod(Object object, String methodName, Object[] params) throws NoSuchMethodException{
	     Assert.notNull(object);
	     Assert.hasText(methodName);
	     Class[] types = new Class[params.length];
	     for (int i = 0; i < params.length; i++) {
	     	types[i] = params[i].getClass();
	     }
	 
	     Class clazz = object.getClass();
	     Method method = null;
	     for (Class superClass = clazz; superClass != Object.class; ) {
	     	try{
	        	method = superClass.getDeclaredMethod(methodName, types);
	     	}catch (NoSuchMethodException localNoSuchMethodException){
	     		superClass = superClass.getSuperclass();
	     	}
	     }
	 
	     if (method == null) {
	     	throw new NoSuchMethodException("No Such Method:" + 
	     	clazz.getSimpleName() + methodName);
	     }
	     boolean accessible = method.isAccessible();
	     method.setAccessible(true);
	     Object result = null;
	     try {
	     	result = method.invoke(object, params);
	     } catch (Exception e) {
	     	ReflectionUtils.handleReflectionException(e);
	     }
	     method.setAccessible(accessible);
	     return result;
	}
	
	//TODO 
	public static String[] getPropertyNames(Object obj)
	{
	  try
	  {
	    BeanInfo info = Introspector.getBeanInfo(obj.getClass());
	    PropertyDescriptor[] properties = info.getPropertyDescriptors();
	    String[] result = new String[properties.length];
	
	    for (int i = 0; i < properties.length; i++) {
	      result[i] = properties[i].getName();
	    }
	
	    return result; } catch (IntrospectionException e) {
	  }
	  return null;
	}
	//TODO 
	public static Object forceGetProperty(Object object, String propertyName)
	  throws NoSuchFieldException
	{
	  Assert.notNull(object);
	  Assert.hasText(propertyName);
	  Field field = getDeclaredField(object, propertyName);
	  boolean accessible = field.isAccessible();
	  field.setAccessible(true);
	  Object result = null;
	  try {
	    result = field.get(object);
	  } catch (IllegalAccessException e) {
	    logger.info("error wont' happen");
	  }
	  field.setAccessible(accessible);
	  return result;
	}
	//TODO 
	public static Field getDeclaredField(Object object, String propertyName)
	  throws NoSuchFieldException
	{
	  Assert.notNull(object);
	  Assert.hasText(propertyName);
	  return getDeclaredField(object.getClass(), propertyName);
	}
	
	
	
}
