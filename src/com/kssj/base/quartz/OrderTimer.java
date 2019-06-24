package com.kssj.base.quartz;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.kssj.base.servlet.AccessTokenUtil;
import com.kssj.base.servlet.WebUtils;

/**
* @Description: solr inder Timer
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-6-3 上午11:35:15
* @version V1.0
*/
public class OrderTimer {
	private Logger logger = Logger.getLogger(OrderTimer.class);
	/*@Resource
	private SearchEngineService searchEngineService;*/
	
	private static String URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";
	/**
	 * 部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
	 * 
	 * 	id : 3    上海三盛宏业投资(集团)有限责任公司
		id : 895  造价公司
		id : 1146 中昌大数据股份有限公司
		id : 915  上海铭慈投资管理有限公司
		id : 902  经贸公司
		id : 921  上海九厘金融信息服务有限公司
	 */
	private static String TOPARTY = "3|895|1146|915|902|921";
	/**
	 * 
	 * @method: initNumMap
	 * @Description: 微信提醒 10:00
	 * @author : zhaoziyun
	 * @date 2017-3-3 下午4:34:12
	 */
	public static  void initNumMap(String accesstoken){
		Gson gson = new Gson();
		String url = URL+accesstoken; 
		Map<String, Object> map = new HashMap<String, Object>();
		/**
		 * 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向该企业应用的全部成员发送
		 */
		map.put("touser", "zhaoziyun|dingzhenjiao|anqin|liushengjie");
		/**
		 * 部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
		 */
		//map.put("toparty", "584");
		/**
		 * 消息类型，此时固定为：text
		 */
		map.put("msgtype", "text");
		/**
		 * 企业应用的id，整型。可在应用的设置页面查看
		 */
		map.put("agentid",31);
		Map<String, Object> map1 = new HashMap<String, Object>();
		String text = "距离订餐结束时间还有20分钟，请您不要忘记订餐！<a href=\"http://dc.sshy.cn:8081/ordersystem/wx_orders/wx_orderLogin.jsp\">前往微信订餐</a>";
		/**
		 * 消息内容，最长不超过2048个字节
		 */
		map1.put("content", text);
		map.put("text",map1 );
		System.out.println(gson.toJson(map));
		String json = gson.toJson(map);
		try {
			String str = WebUtils.postData(url, json, "UTF-8", "UTF-8", null);
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void createIndex(){
		try{
			String accesstoken =AccessTokenUtil.accessToken();
			System.out.println(accesstoken);
			initNumMap(accesstoken);
			logger.info("-----------start index ......");
			//调用方法
			System.out.println("================执行定时任务！");
			logger.info("-----------end index !!!");
		}catch(Exception e){
			e.printStackTrace();
			logger.log(null,"--------->>Exception: index ......");
		}		
	}
	
	
	
	/**
	 * 提醒订餐9：00
	 * @method: initNumMap_2
	 * @Description: TODO
	 * @param accesstoken
	 * @author : dingzhj
	 * @date 2017-6-27 下午12:18:41
	 */
	public static  void initNumMap_2(String accesstoken){
		Gson gson = new Gson();
		String url = URL+accesstoken; 
		Map<String, Object> map = new HashMap<String, Object>();
		/**
		 * 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向该企业应用的全部成员发送
		 */
		map.put("touser", "zhaoziyun|dingzhenjiao|anqin|liushengjie");
		/**
		 * 部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
		 */
		//map.put("toparty", "584");
		/**
		 * 消息类型，此时固定为：text
		 */
		map.put("msgtype", "text");
		/**
		 * 企业应用的id，整型。可在应用的设置页面查看
		 */
		map.put("agentid",31);
		Map<String, Object> map1 = new HashMap<String, Object>();
		String text = "新的一天工作开始，请不要忘记订餐！<a href=\"http://dc.sshy.cn:8081/ordersystem/wx_orders/wx_orderLogin.jsp\">前往微信订餐</a>";
		/**
		 * 消息内容，最长不超过2048个字节
		 */
		map1.put("content", text);
		map.put("text",map1 );
		System.out.println(gson.toJson(map));
		String json = gson.toJson(map);
		try {
			String str = WebUtils.postData(url, json, "UTF-8", "UTF-8", null);
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void createIndex_2(){
		try{
			String accesstoken =AccessTokenUtil.accessToken();
			System.out.println(accesstoken);
			initNumMap_2(accesstoken);
			logger.info("-----------start index2 ......");
			//调用方法
			System.out.println("================执行定时任务2！");
			logger.info("-----------end index !!!");
		}catch(Exception e){
			e.printStackTrace();
			logger.log(null,"--------->>Exception: index2 ......");
		}		
	}
	
	public static void main(String[] args) {
		String accesstoken =AccessTokenUtil.accessToken();
		String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" +
				accesstoken;
		String url2 = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=" +
				accesstoken +
				"&department_id="+1;
				//+"&fetch_child=1";
		//String s = WebUtils.httpsGet(url);
		//String s = WebUtils.httpsGet(url);
		//System.out.println(s);
		//initNumMap(accesstoken);
	}
}
