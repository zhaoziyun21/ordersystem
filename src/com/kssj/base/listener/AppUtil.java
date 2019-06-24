package com.kssj.base.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
* @Description: 方便取得Spring容器，取得其他服务实例，但必须在Spring的配置文件里进行配置 如：
* 					<bean id="appUtil" class="...util.core.AppUtil"/> 也提供整个应用程序的相关配置获取方法
* 				
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 上午10:39:12
* @version V1.0
*/
@SuppressWarnings({"static-access","unchecked","rawtypes"})
public class AppUtil implements ApplicationContextAware {
	private static Logger logger = Logger.getLogger(AppUtil.class);
	
	/**① 
	 * @method: setApplicationContext
	 * @Description: overwritten “ApplicationContextAware”
	 *               ①function:get the Bean From the IOC container / Get the Bean from Spring IOC
	 *
	 * @param applicationContext
	 * @throws BeansException
	 *
	 * @author: ChenYW
	 * @date 2014-1-2 下午01:35:39
	 */
	private static ApplicationContext appContext;
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.appContext = applicationContext;
	}
	private static void checkApplicationContext() {
		if (appContext == null) {
			throw new IllegalStateException("applicaitonContext未注入,请在app-resource.xml中定义AppUtil");
		}
	}
	public static Object getBean(String beanId) {
		checkApplicationContext();
		return appContext.getBean(beanId);
	}

	//一、Application global object
	private static ServletContext servletContext = null;
	//get Absolute path of the Application
	public static String getAppAbsolutePath() {	
		return servletContext.getRealPath("/");
	}
	//二、Deposit the configuration of the "config.properties"（①Don`t need to use cache）
	private static Map configMap = new HashMap();
	/**② 
	 * @method: init
	 * @Description: Load static/system data when WEB application server startup
	 *
	 * @param in_servletContext
	 * @return void
	 *
	 * @author: ChenYW
	 * @date 2013-10-23 上午09:16:32
	 */
	public static void init(ServletContext in_servletContext) {
		servletContext = in_servletContext;

		// 1.读取来自config.properties文件的配置,并且放入configMap内,应用程序共同使用
//		String filePath = servletContext.getRealPath("/WEB-INF/classes/conf/");
//		String configFilePath = filePath + "/config.properties";
		
//		Properties props = new Properties();
//		try {
//			FileInputStream fis = new FileInputStream(configFilePath);
//			Reader r = new InputStreamReader(fis, "UTF-8");
//			props.load(r);
//			
//			Iterator it = props.keySet().iterator();
//			while (it.hasNext()) {
//				String key = (String) it.next();
//				configMap.put(key, props.get(key));
//			}
//		} catch (Exception ex) {
//			logger.error(ex.getMessage());
//		}
		
		//2.load :permission、dictionary and mune
		((AppUtil)getBean("appUtil")).load();
		
		//3.load solr url
//		((SolrServiceImpl)getBean("solrService")).init();
		
		//4.load Mahout parameter
//		((MahoutServiceImpl)getBean("mahoutService")).init();
	}
	
	
	
	
	//Loading menus/opration、systemData、data dictionary 
//	@Resource
//	private SysMenuService sysMenuService;//Loading the menus
	
//	@Resource
//	private SysParametersService sysParametersService;//Loading the system parameters
	
	/** ③
	* @method: load
	* @Description: load- menu, params, permission
	*
	* @return void
	*
	* @author ChenYW
	*/
	public void load()
	{
		try{
			//一、Loading permission(include:role--list[menu+operator]) 
//			sysMenuService.findLoad();	
			
			//二、Loading the system parameters
//			sysParametersService.findLoad();
			
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

//********************************Up: System start loading(save RAM)*********************************
	
	/**
	 * 获取系统配置MAP（/resource/conf/config.properties）
	 */
	public static Map getSysConfig() {
		return configMap;
	}

	/**
	 * 取得流程表单模板的目录的绝对路径
	 * 
	 * @return
	 */
	public static String getFlowFormAbsolutePath() {
		String path = (String) configMap.get("app.flowFormPath");
		if (path == null)
			path = "/WEB-INF/FlowForm/";
		return getAppAbsolutePath() + path;

	}
	
    /**
    * @method: getIpAddr
    * @Description: 探测访问机的真实IP
    *
    * @param request
    * @return
    * @return String
    *
    * @author: ChenYW
    * @date 2013-10-14 上午10:43:32
    */
    public static String getIpAddr(HttpServletRequest request) {  
    	       String ip = request.getHeader("x-forwarded-for");      
    	       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
    	           ip = request.getHeader("Proxy-Client-IP");  
    	       }  
    	       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
    	           ip = request.getHeader("WL-Proxy-Client-IP");  
    	       }  
    	       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
    	           ip = request.getHeader("HTTP_CLIENT_IP");  
    	       }  
    	       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
    	           ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
    	       }  
    	       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
    	           ip = request.getRemoteAddr();  
    	       } 
    	       if (ip == null || ip.length() == 0 || ip.split("\\.").length != 4)
    	       {
    	    	   ip = "127.0.0.1";
    	       }
    	     return ip;
    	} 

    
}
