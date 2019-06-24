package com.kssj.order.action;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.kssj.auth.model.XUser;
import com.kssj.auth.service.XUserService;
import com.kssj.base.datasource.DataSourceConst;
import com.kssj.base.datasource.DataSourceContextHolder;
import com.kssj.base.util.Constants;
import com.kssj.base.util.DateUtil;
import com.kssj.base.util.DateUtils2;
import com.kssj.base.util.DateWeek;
import com.kssj.base.util.ListUtil;
import com.kssj.base.util.Md5Security;
import com.kssj.base.util.StringUtil;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.command.sql.SqlSpellerDbType;
import com.kssj.frame.web.action.BaseAction;
import com.kssj.order.model.MyOrder;
import com.kssj.order.model.XAppointRelation;
import com.kssj.order.model.XDetailRecord;
import com.kssj.order.model.XFood;
import com.kssj.order.model.XFoodSendAddress;
import com.kssj.order.model.XFoodSendRegion;
import com.kssj.order.service.XDetailRecordService;
import com.kssj.order.service.XFoodSendAddressService;
import com.kssj.order.service.XFoodSendRegionService;
import com.kssj.order.service.XFoodService;
import com.kssj.order.service.XNoticeService;
import com.kssj.order.service.XOrdersService;

/**
 * @Description: 手机端action，所有手机端访问通过此action
 * @Company:KSSJ
 * @author : lig
 * @date: 2017-3-15 上午11:07:50
 * @version V1.0
 */
@SuppressWarnings("serial")
public class WeChatAction extends BaseAction {

	@Resource
	private XOrdersService xOrdersService;
	@Resource
	private XUserService xUserService;
	@Resource
	private XNoticeService xNoticeService;
	@Resource
	private XDetailRecordService xDetailRecordService;
	
	@Resource
	private XFoodSendAddressService xFoodSendAddressService;
	
	@Resource
	private XFoodSendRegionService xFoodSendRegionService;
	@Resource
	private XFoodService xFoodService;

	private XUser xuser;

	public void setXuser(XUser xuser) {
		this.xuser = xuser;
	}

	public XUser getXuser() {
		return xuser;
	}

	private List<XDetailRecord> detailRecordList;
	private List<MyOrder> myOrderList;

	public List<XDetailRecord> getDetailRecordList() {
		return detailRecordList;
	}

	public List<MyOrder> getMyOrderList() {
		return myOrderList;
	}

	/**
	 * 
	 * @method: orderLogin
	 * @Description: 订餐用户登录
	 * @throws Exception
	 * @author : zhaoziyun
	 * @date 2017-3-2 上午11:13:26
	 */
	public void orderLogin() throws Exception {
		String result = "Y";
		try {
			String str = Md5Security.getMD5(xuser.getPassword());
			xuser.setPassword(str.toUpperCase());
			DataSourceContextHolder holder = new DataSourceContextHolder();
			holder.setDataSourceType(DataSourceConst.SQLSERVER);
			boolean b = xUserService.orderLoginVerification(xuser);
			holder.setDataSourceType(DataSourceConst.MYSQL);
			if (!b) {
				result = "N";
			} else {
				XUser u = xUserService.getUserByUserName(xuser);
				if (u == null) {
					u = xUserService.addOaUser(xuser.getUserName());
				}else{
					  xUserService.updOaUser(u);
				}
				this.getSession().setAttribute("wxOrderLoginUser", u);
				// 设置cookie 记住密码
				Cookie userCookie = new Cookie("username-password",
						xuser.getUserName() + "-" + xuser.getPassword());
				// 设置Cookie的有效期为1年
				userCookie.setMaxAge(60 * 60 * 24 * 365);
				userCookie.setPath("/");
				this.getResponse().addCookie(userCookie);
			}
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try {
				out = this.getResponse().getWriter();
				out.write(result);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.flush();
					out.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("登录失败!");
		}
	}

	/**
	 * 
	 * @method: orderLogout
	 * @Description: TODO
	 * @return
	 * @throws Exception
	 * @author : zhaoziyun
	 * @date 2017-3-6 下午5:01:01
	 */
	public String orderLogout() throws Exception {
		try {
			// 清除session
			this.getSession().removeAttribute("wxOrderLoginUser");
			// 清除cookie
			Cookie cookie = new Cookie(Constants.USERNAME + "-"
					+ Constants.PASSWORD, null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			this.getResponse().addCookie(cookie);
			// 跳转登录页
			this.setForwardPage("/orders/orderLogin.jsp");

			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询详情失败!");
		}
	}

	/**
	 * @method: toMyOrder
	 * @Description: to我的订单page
	 * @return
	 * @throws Exception
	 * @author : lig
	 * @date 2017-3-15 上午11:09:13
	 */
	public String toMyOrder() throws Exception {
		try {
			String userId = this.getWxOrderUserBySession().getUserId();
			this.myOrderList = xOrdersService.getMyOrder(userId);
			this.setForwardPage("/wx_orders/myorder.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("我的订单查询失败!");
		}
	}
	
	/**
	 * 现场订购的订单
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toMyLiveOrder() throws Exception {
		try {
			String userId = this.getWxOrderUserBySession().getUserId();
			this.myOrderList = xOrdersService.getMyLiveOrder(userId);
			this.setForwardPage("/wx_orders/myLiveOrder.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("我的订单查询失败!");
		}
	}

	/**
	 * @method: toBalance
	 * @Description: to当前余额page
	 * @return
	 * @throws Exception
	 * @author : lig
	 * @date 2017-3-15 下午2:04:23
	 */
	public String toBalance() throws Exception {
		try {
			String userId = this.getWxOrderUserBySession().getUserId();
			String balance = xOrdersService.getBalanceById("2", userId);
			this.getRequest().setAttribute("balance", balance);
			this.setForwardPage("/wx_orders/balance.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("我的订单查询失败!");
		}
	}

	/**
	 * @method: toDetail
	 * @Description: to余额明细page
	 * @return
	 * @throws Exception
	 * @author : lig
	 * @date 2017-3-15 下午2:40:31
	 */
	public String toDetail() throws Exception {
		try {
			String userId = this.getWxOrderUserBySession().getUserId();
			this.detailRecordList = xDetailRecordService.getRecordById("2",
					userId);
			this.setForwardPage("/wx_orders/detail.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("我的订单查询失败!");
		}
	}

	/**
	 * 
	 * @method: toIndex
	 * @Description: 到首页
	 * @return
	 * @throws Exception
	 * @author : zhaoziyun
	 * @date 2017-3-16 下午1:48:54
	 */
	public String toIndex() throws Exception {
		try {
			// 判断订餐对象 isKR：客人 isLD：领导 isZD：指定人订餐
			boolean isKR = false;
			boolean isLD = false;
			boolean isZD = false;
			String userId = this.getWxOrderUserBySession().getUserId();
			XUser orderUser = xUserService.getByUserId(userId);
			if (orderUser != null) {
				if (orderUser.getSeclevel() != null) {
					// 安全级别大于40 领导,可以指定
					if (Integer.valueOf(orderUser.getSeclevel()) >= Constants.SECLEVEL) {
						isZD = true;
						XAppointRelation xa = xOrdersService
								.getXAppointRelation(orderUser.getUserId());
						if (xa != null) {
							XUser appointUser = xUserService.getByUserId(xa
									.getAppointId());
							this.getRequest().setAttribute("appointUser",
									appointUser);
						}
						List<Map> deptList = xUserService
								.getDeptByCompanyId(orderUser.getCompanyId());
						this.getRequest().setAttribute("deptList", deptList);
					}
				}
			}
			// 部门专用角色
			isKR = xOrdersService.isDeptSpecialRole(userId);
			// 是否被指定，如果被指定则显示领导
			List<Map> list = xOrdersService.getLeadByUserId(userId);
			if (ListUtil.isNotEmpty(list)) {
				isLD = true;
				this.getRequest().setAttribute("leadList", list);
			}

			this.getRequest().setAttribute("isKR", isKR);
			this.getRequest().setAttribute("isLD", isLD);
			this.getRequest().setAttribute("isZD", isZD);
			this.getSession().setAttribute("isZD", isZD);
			this.setForwardPage("/wx_orders/index.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			throw new Exception("进入首页失败!");
		}
	}

	/**
	 * @method: toSelfOrderPage
	 * @Description: to 订餐页面
	 * @return
	 * @throws Exception
	 * @author : zhaoziyun
	 * @date 2017-3-6 下午2:33:36
	 */
	@SuppressWarnings("rawtypes")
	public String toSelfOrderPage() throws Exception {
		try {
			String infos = this.getRequest().getParameter("infos");
			String orderType = this.getRequest().getParameter("orderType");
			
			this.getRequest().setAttribute("orderType", orderType);
			if (StringUtil.isNotEmpty(infos)) {
				this.getRequest().setAttribute("infos", infos);
			}
			
			//查询所有美食商家
			List<XUser> foodBusinessList = xUserService.findFoodBusiness();
			this.getRequest().setAttribute("foodBusinessList", foodBusinessList);
			
			String foodBusiness = this.getRequest().getParameter("foodBusiness");
			if(foodBusinessList != null && foodBusinessList.size() > 0 && StringUtils.isBlank(foodBusiness)){
				foodBusiness = foodBusinessList.get(0).getUserName();
			}
			this.getRequest().setAttribute("foodBusiness", foodBusiness);
			
			Map map = new HashMap();
			List<Map> noticeList = new ArrayList<Map>();
			if(StringUtils.isNotBlank(foodBusiness)){
				XUser xUser = xUserService.findFoodBusinessByUsername(foodBusiness);
				if("ZC".equals(orderType) || "YY".equals(orderType)){
					map = this.getCurrentFood(foodBusiness);
				}else if("XC".equals(orderType)){
					map = this.getLiveCurrentFood(foodBusiness);
				}
				this.getRequest().setAttribute("foodBusinessfirst", xUser);
				
				/**
				 * 今日公告
				 */
				if("XC".equals(orderType)){
					noticeList = this.getCurrentXCNotice(foodBusiness);
				}else{
					noticeList = this.getCurrentNotice(foodBusiness);
				}
				if(noticeList != null){
					this.getRequest().setAttribute("noticeList", noticeList);
					this.getRequest().setAttribute("notice", "yes");
				}else{
					this.getRequest().setAttribute("notice", "no");
				}
				
			}else{
				if(foodBusinessList != null && foodBusinessList.size() > 0){
					if("ZC".equals(orderType) || "YY".equals(orderType)){
						map = this.getCurrentFood(foodBusinessList.get(0).getUserName());
					}else if("XC".equals(orderType)){
						map = this.getLiveCurrentFood(foodBusinessList.get(0).getUserName());
					}
					
					this.getRequest().setAttribute("foodBusinessfirst", foodBusinessList.get(0));
					
					/**
					 * 今日公告
					 */
					if("XC".equals(orderType)){
						noticeList = this.getCurrentXCNotice(foodBusiness);
					}else{
						noticeList = this.getCurrentNotice(foodBusiness);
					}
					if(noticeList != null){
						this.getRequest().setAttribute("noticeList", noticeList);
						this.getRequest().setAttribute("notice", "yes");
					}else{
						this.getRequest().setAttribute("notice", "no");
					}
				}
			}
			this.getRequest().setAttribute("map", map);
			
			// 判断订餐对象 type:0、自己 1、客餐 2、领导
			String type = this.getRequest().getParameter("type");
			this.getRequest().setAttribute("type", type);
			if ("1".equals(type)) {
				String vistorName = this.getRequest()
						.getParameter("vistorName");
				String adv = java.net.URLDecoder.decode(vistorName, "utf-8");
				this.getRequest().setAttribute("vistorName",
						java.net.URLDecoder.decode(vistorName, "utf-8"));
			} else if ("2".equals(type)) {
				String leadId = this.getRequest().getParameter("leadId");
				this.getRequest().setAttribute("leadId", leadId);
			}
			
			if("ZC".equals(orderType)){
				this.setForwardPage("/wx_orders/self-order.jsp");
			}else if("YY".equals(orderType)){
				this.setForwardPage("/wx_orders/reserve.jsp");
			}else if("XC".equals(orderType)){
				this.setForwardPage("/wx_orders/live-order.jsp");
			}
			
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			throw new Exception("菜品查询失败!");
		}
	}
	
	/**
	 * 正常订餐的当前公告
	 * 
	 * @param foodBusiness
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	private List<Map> getCurrentNotice(String foodBusiness){
		Map m = new HashMap();
		
		Date now = new Date();
		try{
			String type = "";
			//周几
			String week = DateUtil.getWeek(now);
			Long currentTime = now.getTime();
			Long lunStart = DateUtil.getAppointDate(Constants.LUNSTART).getTime();
			Long lunEnd = DateUtil.getAppointDate(Constants.LUNSEND).getTime();
			Long dinStart = DateUtil.getAppointDate(Constants.DINSTART).getTime();
			Long dinEnd = DateUtil.getAppointDate(Constants.DINEND).getTime();
			
			if(lunStart <= currentTime && lunEnd >= currentTime){
				type =  Constants.LUN;
			}else if(dinStart <= currentTime && dinEnd >= currentTime){
				type =  Constants.DIN;
			}else{
				
			}
			
			List<Map> noticeList = xNoticeService.getCurrentNotice(week, type, foodBusiness);
			
			return noticeList;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 现场订餐的当前公告
	 * 
	 * @param foodBusiness
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unused"})
	private List<Map> getCurrentXCNotice(String foodBusiness){
		Map m = new HashMap();
		
		Date now = new Date();
		try{
			String type = "";
			//周几
			String week = DateUtil.getWeek(now);
			Long currentTime = now.getTime();
			Long lunStart = DateUtil.getAppointDate(Constants.LIVE_LUNSTART).getTime();
			Long lunEnd = DateUtil.getAppointDate(Constants.LIVE_LUNSEND).getTime();
			Long dinStart = DateUtil.getAppointDate(Constants.LIVE_DINSTART).getTime();
			Long dinEnd = DateUtil.getAppointDate(Constants.LIVE_DINEND).getTime();
			
			if(lunStart <= currentTime && lunEnd >= currentTime){
				type =  Constants.LUN;
			}else if(dinStart <= currentTime && dinEnd >= currentTime){
				type =  Constants.DIN;
			}else{
				
			}
			
			List<Map> noticeList = xNoticeService.getCurrentNotice(week, type, foodBusiness);
			
			return noticeList;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	/**
	 * 取消订单
	 * 
	 * @method: deleteOrder
	 * @Description: TODO
	 * @throws Exception
	 * @author : dingzhj
	 * @date 2017-3-19 上午10:53:56
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteOrder() throws Exception {
		Map result = new HashMap<String, String>();
		Gson g = new Gson();
		try {
			// 订餐类型
			// String flag = this.getRequest().getParameter("type");
			// 当前订单ID
			String orderId = this.getRequest().getParameter("orderId");
			// 取消订单的总套餐份数
			String foodNum = this.getRequest().getParameter("foodNum");
			String orderType = this.getRequest().getParameter("orderType");
			// 当前登录用户信息
			// XUser orderUser =
			// (XUser)this.getSession().getAttribute("orderLoginUser");
			XUser orderUser = getWxOrderUserBySession();
			Map<String, Object> msgMap;
			msgMap = xOrdersService.deleteOrder(orderId, foodNum, orderUser, orderType);
			// 取出一周的菜谱当天的菜谱
			/*
			 * Map m = new HashMap(); for(Iterator it =
			 * Global.getNumMap().keySet().iterator() ; it.hasNext();){ String
			 * key = it.next().toString(); m.put(key,
			 * Global.getNumMap().get(key)); }
			 */
			if ("true".equals(msgMap.get("status"))) {
				/*
				 * for(Iterator it = m.keySet().iterator() ; it.hasNext();){
				 * String key = it.next().toString();
				 * Global.getNumMap().put(key, m.get(key)); }
				 */
				result.put("success", "success");
				result.put("msg", "取消订餐成功");
			} else {
				result.put("success", "false");
				result.put("msg", "取消订餐失败");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("取消订餐失败");
		}
		PrintWriter out = null;
		this.getResponse().setContentType("text/json; charset=utf-8");
		out = this.getResponse().getWriter();
		out.write(g.toJson(result));
		if (out != null) {
			out.flush();
			out.close();
		}
	}

	/**
	 * 取消现场订单
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteLiveOrder() throws Exception {
		Map result = new HashMap<String, String>();
		Gson g = new Gson();
		try {
			// 订餐类型
			// String flag = this.getRequest().getParameter("type");
			// 当前订单ID
			String orderId = this.getRequest().getParameter("orderId");
			// 取消订单的总套餐份数
			String foodNum = this.getRequest().getParameter("foodNum");
			String orderType = this.getRequest().getParameter("orderType");
			// 当前登录用户信息
			// XUser orderUser =
			// (XUser)this.getSession().getAttribute("orderLoginUser");
			XUser orderUser = getWxOrderUserBySession();
			Map<String, Object> msgMap;
			msgMap = xOrdersService.deleteLiveOrder(orderId, foodNum, orderUser, orderType);
			
			if ("true".equals(msgMap.get("status"))) {
				result.put("success", "success");
				result.put("msg", "取消订餐成功");
			} else {
				result.put("success", "false");
				result.put("msg", "取消订餐失败");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("取消订餐失败");
		}
		PrintWriter out = null;
		this.getResponse().setContentType("text/json; charset=utf-8");
		out = this.getResponse().getWriter();
		out.write(g.toJson(result));
		if (out != null) {
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 根据订单ID
	 * 
	 * @method: againOrder
	 * @Description: TODO
	 * @author : dingzhj
	 * @date 2017-3-17 下午2:41:28
	 */
	public void againOrder() {
		try {
			String orderId = this.getRequest().getParameter("orderId");
			// List<MyOrder> myOrderLis1t = xOrdersService.getMyOrder(orderId);
			List<Map> myOrderList = xOrdersService.getMyOrderById(orderId);
			Gson gson = new Gson();
			jsonString = gson.toJson(myOrderList);
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try {
				out = this.getResponse().getWriter();
				out.write(jsonString);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * @method: getCurrentFood
	 * @Description: 获取当前时间段food
	 * @return
	 * @author : lig
	 * @date 2017-3-6 下午2:43:07
	 * 
	 *       9:00 - 14:00 午 14:00 - 22:00 晚 其他时间 暂无
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	private Map getCurrentFood(String foodBusiness) {
		Map m = new HashMap();
		// Map numMap = Global.getNumMap();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		try {
			// String weekKey = "";
			String type = "";
			// 周几
			String week = DateUtil.getWeek(now);
			Long currentTime = now.getTime();
			Long lunStart = DateUtil.getAppointDate(Constants.LUNSTART)
					.getTime();
			Long lunEnd = DateUtil.getAppointDate(Constants.LUNSEND).getTime();
			Long dinStart = DateUtil.getAppointDate(Constants.DINSTART)
					.getTime();
			Long dinEnd = DateUtil.getAppointDate(Constants.DINEND).getTime();

			if (lunStart <= currentTime && lunEnd >= currentTime) {
				type = Constants.LUN;
				System.out.println("==========type"+type);
				System.out.println("=========="+currentTime);
				System.out.println("==========lunStart"+lunStart);
				System.out.println("==========lunEnd"+lunEnd);
				System.out.println("==========dinStart"+dinStart);
				System.out.println("==========dinEnd"+dinEnd);
			} else if (dinStart <= currentTime && dinEnd >= currentTime) {
				type = Constants.DIN;
				System.out.println("==========type"+type);
				System.out.println("=========="+currentTime);
				System.out.println("==========lunStart"+lunStart);
				System.out.println("==========lunEnd"+lunEnd);
				System.out.println("==========dinStart"+dinStart);
				System.out.println("==========dinEnd"+dinEnd);
			} else {
				System.out.println("==========type"+type);
				System.out.println("=========="+currentTime);
				System.out.println("==========lunStart"+lunStart);
				System.out.println("==========lunEnd"+lunEnd);
				System.out.println("==========dinStart"+dinStart);
				System.out.println("==========dinEnd"+dinEnd);
				// 其他时间无
			}

			List<Map> foodFist = xOrdersService.getCurrentFood(week, type, foodBusiness);
			m.put("weekKey", week + "_" + type);
			m.put("foodInfos", foodFist);
			// if(!numMap.isEmpty() && numMap != null){
			// if(StringUtil.isNotEmpty(weekKey)){
			// m.put("weekKey",weekKey);
			// m.put("foodInfos", numMap.get(weekKey));
			// }
			// }
			return m;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * 现场订餐展示的套餐
	 * 
	 * @param foodBusiness
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	private Map getLiveCurrentFood(String foodBusiness) {
		Map m = new HashMap();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		try {
			String type = "";
			// 周几
			String week = DateUtil.getWeek(now);
			Long currentTime = now.getTime();
			Long lunStart = DateUtil.getAppointDate(Constants.LIVE_LUNSTART)
					.getTime();
			Long lunEnd = DateUtil.getAppointDate(Constants.LIVE_LUNSEND).getTime();
			Long dinStart = DateUtil.getAppointDate(Constants.LIVE_DINSTART)
					.getTime();
			Long dinEnd = DateUtil.getAppointDate(Constants.LIVE_DINEND).getTime();

			if (lunStart <= currentTime && lunEnd >= currentTime) {
				type = Constants.LUN;
			} else if (dinStart <= currentTime && dinEnd >= currentTime) {
				type = Constants.DIN;
			} else {
				// 其他时间无
			}

			List<Map> foodFist = xOrdersService.getCurrentFood(week, type, foodBusiness);
			m.put("weekKey", week + "_" + type);
			m.put("foodInfos", foodFist);
			
			return m;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @method: toOrderingPage
	 * @Description: to 订餐页面
	 * @return
	 * @throws Exception
	 * @author : lig
	 * @date 2017-3-6 下午2:33:36
	 */
	@SuppressWarnings("rawtypes")
	public String toOrderingPage() throws Exception {
		try {
			//切换不同的美食商家，展示不同的美食商家餐品，所需要带的其它值
			String typeOld = this.getRequest().getParameter("type");
			String leadIdOld = this.getRequest().getParameter("leadId");
			String orderTypeOld = this.getRequest().getParameter("orderType");
			String foodBusinessOld = this.getRequest().getParameter("foodBusiness");
			this.getRequest().setAttribute("type", typeOld);
			this.getRequest().setAttribute("leadId", leadIdOld);
			this.getRequest().setAttribute("orderType", orderTypeOld);
			this.getRequest().setAttribute("foodBusiness", foodBusinessOld);
			
			String infos = this.getRequest().getParameter("infos");
			if (StringUtil.isNotEmpty(infos)) {
				this.getRequest().setAttribute("infos", infos);
			}

			//查询所有美食商家
			List<XUser> foodBusinessList = xUserService.findFoodBusiness();
			this.getRequest().setAttribute("foodBusinessList", foodBusinessList);
			
			String foodBusiness = this.getRequest().getParameter("foodBusiness");
			Map map = new HashMap();
			if(StringUtils.isNotBlank(foodBusiness)){
				XUser xUser = xUserService.findFoodBusinessByUsername(foodBusiness);
				map = this.getCurrentFood(foodBusiness);
				this.getRequest().setAttribute("foodBusinessfirst", xUser);
			}else{
				if(foodBusinessList != null && foodBusinessList.size() > 0){
					map = this.getCurrentFood(foodBusinessList.get(0).getUserName());
					this.getRequest().setAttribute("foodBusinessfirst", foodBusinessList.get(0));
				}
			}
			this.getRequest().setAttribute("map", map);
			
			// 判断订餐对象 isKR：客人 isLD：领导 isZD：指定人订餐
			boolean isKR = false;
			boolean isLD = false;
			boolean isZD = false;
			XUser orderUser = this.getWxOrderUserBySession();
			String userId = orderUser.getUserId();
			if (orderUser != null) {
				if (orderUser.getSeclevel() != null) {
					// 安全级别大于40 领导,可以指定
					if (Integer.valueOf(orderUser.getSeclevel()) >= Constants.SECLEVEL) {
						isZD = true;
						XAppointRelation xa = xOrdersService
								.getXAppointRelation(orderUser.getUserId());
						if (xa != null) {
							XUser appointUser = xUserService.getByUserId(xa
									.getAppointId());
							this.getRequest().setAttribute("appointUser",
									appointUser);
						}
					}
				}
			}
			// 部门专用角色
			isKR = xOrdersService.isDeptSpecialRole(userId);
			// 是否被指定，如果被指定则显示领导
			List<Map> list = xOrdersService.getLeadByUserId(userId);
			if (ListUtil.isNotEmpty(list)) {
				isLD = true;
				this.getRequest().setAttribute("leadList", list);
			}

			this.getRequest().setAttribute("isKR", isKR);
			this.getRequest().setAttribute("isLD", isLD);
			this.getRequest().setAttribute("isZD", isZD);
			this.getSession().setAttribute("isZD", isZD);
			this.setForwardPage("/wx_orders/self-order.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("菜品查询失败!");
		}
	}

	/**
	 * goSum
	 * 
	 * @return
	 * @throws Exception
	 */
	public String goSum() throws Exception {
		//返回上一页所需要带的值
		String type = this.getRequest().getParameter("type");
		String leadId = this.getRequest().getParameter("leadId");
		String infos = this.getRequest().getParameter("infos");
		String orderType = this.getRequest().getParameter("orderType");
		String foodBusiness = this.getRequest().getParameter("foodBusiness");
		String checkedBusiness = this.getRequest().getParameter("checkedBusiness");
		this.getRequest().setAttribute("type", type);
		this.getRequest().setAttribute("leadId", leadId);
		this.getRequest().setAttribute("infos", infos);
		this.getRequest().setAttribute("orderType", orderType);
		this.getRequest().setAttribute("foodBusiness", foodBusiness);
		this.getRequest().setAttribute("checkedBusiness", checkedBusiness);
		
		String flag = this.getRequest().getParameter("flag");
		
		String orderInfo = null;
		String vistorName = null;
		if(StringUtils.isNotBlank(flag) && flag.equals("0")){
			String orderInfoOld = this.getRequest().getParameter("orderInfo");
			String vistorNameOld = this.getRequest().getParameter("vistorName");
			//解决乱码问题
			orderInfo = new String(orderInfoOld.getBytes("iso8859-1"),"utf-8");
			vistorName = new String(vistorNameOld.getBytes("iso8859-1"),"utf-8");
		}else{
			orderInfo = this.getRequest().getParameter("orderInfo");
			vistorName = this.getRequest().getParameter("vistorName");
		}
		
		JSONObject orderJson = null;
		if (StringUtil.isNotEmpty(orderInfo)) {
			orderJson = JSONObject.fromObject(orderInfo);
			// 获取订餐对象
			String orderObj = orderJson.getString("orderObj");
			// 订餐对象(ZJ:自己、KR：客人、LD：领导、ZD：指定)
			// 获取扣款对象姓名、账户余额
			Map map = new HashMap();
			if (orderObj.equals("ZJ")) {
				map = xOrdersService.getMoney("ZJ", this
						.getWxOrderUserBySession().getUserId()); // 查询出自己余额//this.getOrderUserBySession().getUserId();
			} else if (orderObj.equals("KR")) {
				map = xOrdersService.getMoney("KR", this
						.getWxOrderUserBySession().getDeptId()); // 查询出部门余额
																	// //this.getOrderUserBySession().getDeptId();
				this.getRequest().setAttribute("vistorName", vistorName);
			} else if (orderObj.equals("LD")) {
				String ldId = orderJson.getString("ldId");
				map = xOrdersService.getMoney("LD", ldId); // 查询出领导的余额//this.getOrderUserBySession().getUserId();
			}
			orderJson.put("obj", map);
		}
		
		//选择切换的地址
		String checkedAddress = this.getRequest().getParameter("checkedAddress");
		this.getRequest().setAttribute("checkedAddress", checkedAddress);
		
		//餐饮公司
		XUser foodCompany = xUserService.findFoodBusinessByUsername(checkedBusiness);
		this.getRequest().setAttribute("foodCompany", foodCompany);
		
		//收货人信息
		//当前登录用户信息
		XUser orderUser = (XUser)this.getSession().getAttribute("wxOrderLoginUser");
		setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
		//所有派送地址信息
		List<Map> addressList = xFoodSendAddressService.getAddressList2(getQf(),checkedBusiness);
		List<XFoodSendAddress> xFoodSendAddressList = new ArrayList<XFoodSendAddress>();
		for (Map addressMap : addressList) {
			if(StringUtils.isNotBlank(orderUser.getSendDefaultAddress())){
				if(orderUser.getSendDefaultAddress().equals(addressMap.get("id"))){ //默认地址
					XFoodSendAddress xFoodSendAddressDefault = new XFoodSendAddress();
					xFoodSendAddressDefault.setId(addressMap.get("id").toString());
					xFoodSendAddressDefault.setRegionId(addressMap.get("region_id").toString());
					xFoodSendAddressDefault.setRegionName(addressMap.get("region_name").toString());
					xFoodSendAddressDefault.setPark(addressMap.get("park").toString());
					xFoodSendAddressDefault.setHighBuilding(addressMap.get("high_building").toString());
					xFoodSendAddressDefault.setUnit(addressMap.get("unit").toString());
					//xFoodSendAddressDefault.setFloor(addressMap.get("floor").toString());
					xFoodSendAddressDefault.setRoomNum(addressMap.get("room_num").toString());
					
					this.getRequest().setAttribute("xFoodSendAddressDefault", xFoodSendAddressDefault);
				}else{
					XFoodSendAddress xFoodSendAddress = new XFoodSendAddress();
					xFoodSendAddress.setId(addressMap.get("id").toString());
					xFoodSendAddress.setRegionId(addressMap.get("region_id").toString());
					xFoodSendAddress.setRegionName(addressMap.get("region_name").toString());
					xFoodSendAddress.setPark(addressMap.get("park").toString());
					xFoodSendAddress.setHighBuilding(addressMap.get("high_building").toString());
					xFoodSendAddress.setUnit(addressMap.get("unit").toString());
					//xFoodSendAddress.setFloor(addressMap.get("floor").toString());
					xFoodSendAddress.setRoomNum(addressMap.get("room_num").toString());
					xFoodSendAddressList.add(xFoodSendAddress);
				}
			}else{
				XFoodSendAddress xFoodSendAddress = new XFoodSendAddress();
				xFoodSendAddress.setId(addressMap.get("id").toString());
				xFoodSendAddress.setRegionId(addressMap.get("region_id").toString());
				xFoodSendAddress.setRegionName(addressMap.get("region_name").toString());
				xFoodSendAddress.setPark(addressMap.get("park").toString());
				xFoodSendAddress.setHighBuilding(addressMap.get("high_building").toString());
				xFoodSendAddress.setUnit(addressMap.get("unit").toString());
				//xFoodSendAddress.setFloor(addressMap.get("floor").toString());
				xFoodSendAddress.setRoomNum(addressMap.get("room_num").toString());
				xFoodSendAddressList.add(xFoodSendAddress);
			}
		}
		this.getRequest().setAttribute("xFoodSendAddressList", xFoodSendAddressList);
		
		if(StringUtils.isNotBlank(checkedAddress)){
			XFoodSendAddress xFoodSendAddressChecked = xFoodSendAddressService.get(checkedAddress);
			XFoodSendRegion xFoodSendRegion = xFoodSendRegionService.get(xFoodSendAddressChecked.getRegionId());
			xFoodSendAddressChecked.setRegionName(xFoodSendRegion.getRegionName());
			this.getRequest().setAttribute("xFoodSendAddressChecked", xFoodSendAddressChecked);
		}
		
		this.getRequest().setAttribute("orderInfo", orderJson.toString());
		this.setForwardPage("/wx_orders/orderInfo.jsp");
		return SUCCESS;
	}
	
	/**
	 * 现场订购
	 * 
	 * @return
	 * @throws Exception
	 */
	public String goLiveSum() throws Exception {
		//返回上一页所需要带的值
		String type = this.getRequest().getParameter("type");
		String leadId = this.getRequest().getParameter("leadId");
		String infos = this.getRequest().getParameter("infos");
		String orderType = this.getRequest().getParameter("orderType");
		String foodBusiness = this.getRequest().getParameter("foodBusiness");
		String checkedBusiness = this.getRequest().getParameter("checkedBusiness");
		this.getRequest().setAttribute("type", type);
		this.getRequest().setAttribute("leadId", leadId);
		this.getRequest().setAttribute("infos", infos);
		this.getRequest().setAttribute("orderType", orderType);
		this.getRequest().setAttribute("foodBusiness", foodBusiness);
		this.getRequest().setAttribute("checkedBusiness", checkedBusiness);
		
		String flag = this.getRequest().getParameter("flag");
		
		String orderInfo = null;
		String vistorName = null;
		if(StringUtils.isNotBlank(flag) && flag.equals("0")){
			String orderInfoOld = this.getRequest().getParameter("orderInfo");
			String vistorNameOld = this.getRequest().getParameter("vistorName");
			//解决乱码问题
			orderInfo = new String(orderInfoOld.getBytes("iso8859-1"),"utf-8");
			vistorName = new String(vistorNameOld.getBytes("iso8859-1"),"utf-8");
		}else{
			orderInfo = this.getRequest().getParameter("orderInfo");
			vistorName = this.getRequest().getParameter("vistorName");
		}
		
		JSONObject orderJson = null;
		if (StringUtil.isNotEmpty(orderInfo)) {
			orderJson = JSONObject.fromObject(orderInfo);
			// 获取订餐对象
			String orderObj = orderJson.getString("orderObj");
			// 订餐对象(ZJ:自己、KR：客人、LD：领导、ZD：指定)
			// 获取扣款对象姓名、账户余额
			Map map = new HashMap();
			if (orderObj.equals("ZJ")) {
				map = xOrdersService.getMoney("ZJ", this
						.getWxOrderUserBySession().getUserId()); // 查询出自己余额//this.getOrderUserBySession().getUserId();
			} else if (orderObj.equals("KR")) {
				map = xOrdersService.getMoney("KR", this
						.getWxOrderUserBySession().getDeptId()); // 查询出部门余额
																	// //this.getOrderUserBySession().getDeptId();
				this.getRequest().setAttribute("vistorName", vistorName);
			} else if (orderObj.equals("LD")) {
				String ldId = orderJson.getString("ldId");
				map = xOrdersService.getMoney("LD", ldId); // 查询出领导的余额//this.getOrderUserBySession().getUserId();
			}
			orderJson.put("obj", map);
		}
		
		//餐饮公司
		XUser foodCompany = xUserService.findFoodBusinessByUsername(checkedBusiness);
		this.getRequest().setAttribute("foodCompany", foodCompany);
		
		this.getRequest().setAttribute("orderInfo", orderJson.toString());
		this.setForwardPage("/wx_orders/live_orderInfo.jsp");
		return SUCCESS;
	}

	/**
	 * 
	 * @method: orderFood
	 * @Description: 订餐
	 * @author : zhaoziyun
	 * @date 2017-3-3 下午4:45:55
	 */
	public void buyFood() throws Exception {
		String orderInfo = this.getRequest().getParameter("orderInfo");// 套餐
		String type = this.getRequest().getParameter("type");// 订餐类型
		String ldId = this.getRequest().getParameter("ldId");// 领导ID
		String vistorName = this.getRequest().getParameter("vistorName");
		String recId = this.getRequest().getParameter("recId");
		List<Map> listMap = new ArrayList<Map>();
		JSONArray jsonArray = JSONArray.fromObject(orderInfo);
		Collection java_collection = JSONArray.toCollection(jsonArray);
		if (java_collection != null && !java_collection.isEmpty()) {
			Iterator it = java_collection.iterator();
			while (it.hasNext()) {
				JSONObject jsonObj = JSONObject.fromObject(it.next());
				Map map = (Map) jsonObj;
				listMap.add(map);
			}
		}

		String result = order(listMap, type, ldId, vistorName,recId);
		try {
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			out = this.getResponse().getWriter();
			out.write(result);
			if (out != null) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("订餐失败");
		}

	}
	
	/**
	 * 现场订购
	 * 
	 * @throws Exception
	 */
	public void buyLiveFood() throws Exception {
		String orderInfo = this.getRequest().getParameter("orderInfo");// 套餐
		String type = this.getRequest().getParameter("type");// 订餐类型
		String ldId = this.getRequest().getParameter("ldId");// 领导ID
		String vistorName = this.getRequest().getParameter("vistorName");
		List<Map> listMap = new ArrayList<Map>();
		JSONArray jsonArray = JSONArray.fromObject(orderInfo);
		Collection java_collection = JSONArray.toCollection(jsonArray);
		if (java_collection != null && !java_collection.isEmpty()) {
			Iterator it = java_collection.iterator();
			while (it.hasNext()) {
				JSONObject jsonObj = JSONObject.fromObject(it.next());
				Map map = (Map) jsonObj;
				listMap.add(map);
			}
		}

		String result = orderLive(listMap, type, ldId, vistorName);
		try {
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			out = this.getResponse().getWriter();
			out.write(result);
			if (out != null) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("订餐失败");
		}

	}

	// type:0、自己 1、客餐 2、领导
	// userId 当前登录者的id
	// orderId 花钱买饭的id
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public String order(List<Map> ListMap, String type, String id,
			String vistorName,String recId) {
		synchronized (this) {
			boolean b = true;
			Map<String, Object> msgMap;
			Map result = new HashMap<String, String>();
			Map m = new HashMap();
			
			Gson g = new Gson();
			int z_num=0; //订单总价钱
			// 遍历订餐map
			for (Map map : ListMap) {
				String foodId = (String) map.get("id");// 套餐ID
				XFood xFood = xFoodService.get(foodId);
				String foodNum = map.get("num").toString();// 套餐数
				z_num +=Integer.parseInt(foodNum)*xFood.getSellPrice();
				// 查询当前库存
				String num = xOrdersService.getFoodBillByFoodId(foodId);
				if (num != null && num != "") {
					if (Integer.parseInt(num) < Integer.parseInt(foodNum)) {
						b = false;
						result.put("status", "false");
						result.put("msg", "套餐不足");
						break;
					} else {
						b = true;
					}
				} else {
					b = false;
					result.put("status", "false");
					result.put("msg", "套餐不足");
					return g.toJson(result);
				}

			}
			if (b) {
				// 扣除订餐人的余额
				// todomap
				XUser orderUser = getWxOrderUserBySession();
				String userId = orderUser.getUserId();
				// String type = this.getRequest().getParameter("type");
				String dcType = "0";
				if ("LD".equals(type)) {
					userId = id;
					dcType = "2";
					 XUser xUser = xUserService.getByUserId(userId);
					 vistorName  = xUser.getRealName();
				} else if ("KR".equals(type)) {
					dcType = "1";
				}else{
					 XUser xUser = xUserService.getByUserId(userId);
					 vistorName  = xUser.getRealName();
				}
				// String orderId =
				// this.getRequest().getParameter("orderId");//给谁订餐的ID
				// orderId = "8";
				msgMap = xOrdersService.order(dcType, orderUser, userId,
						ListMap, z_num, vistorName,recId);
				if ("true".equals(msgMap.get("status"))) {
					/*
					 * for(Iterator it = m.keySet().iterator() ; it.hasNext();){
					 * String key = it.next().toString();
					 * Global.getNumMap().put(key, m.get(key)); }
					 */
					result.put("status", "success");
					result.put("msg", "订餐成功");
				} else {
					result.put("status", "false");
					result.put("msg", msgMap.get("msg"));
				}
			}
			return g.toJson(result);
		}
	}
	
	// type:0、自己 1、客餐 2、领导
	// userId 当前登录者的id
	// orderId 花钱买饭的id
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String orderLive(List<Map> ListMap, String type, String id, String vistorName) {
		
		synchronized (this) {
			boolean b = true;
			Map<String, Object> msgMap;
			Map result = new HashMap<String, String>();
			Gson g = new Gson();
			int z_num = 0;
			
			//遍历订餐map
			for (Map map : ListMap) {
				String foodId = (String) map.get("id");// 套餐ID
				XFood xFood = xFoodService.get(foodId);
				String foodNum = map.get("num").toString();// 套餐数
				z_num +=Integer.parseInt(foodNum)*xFood.getSellPrice();
			}
			
			if (b) {
				// 扣除订餐人的余额
				XUser orderUser = getWxOrderUserBySession();
				String userId = orderUser.getUserId();
				// String type = this.getRequest().getParameter("type");
				String dcType = "0";
				if ("LD".equals(type)) {
					userId = id;
					dcType = "2";
					XUser xUser = xUserService.getByUserId(userId);
					vistorName  = xUser.getRealName();
				} else if ("KR".equals(type)) {
					dcType = "1";
				}else{
					XUser xUser = xUserService.getByUserId(userId);
					vistorName  = xUser.getRealName();
				}
				
				msgMap = xOrdersService.orderLive(dcType, orderUser, userId,
						ListMap, z_num, vistorName);
				
				if ("true".equals(msgMap.get("status"))) {
					result.put("status", "success");
					result.put("msg", "订餐成功");
				} else {
					result.put("status", "false");
					result.put("msg", msgMap.get("msg"));
				}
			}
			
			return g.toJson(result);
		}
	}

	/**
	 * 
	 * @method: getStaffInfo
	 * @Description: 通过部门id 获取公司员工信息
	 * @author : zhaoziyun
	 * @date 2017-3-21 下午1:26:34
	 */
	public void getStaffInfo() {
		String result = "\"{\"success\":\"false\"}\"";
		xuser = this.getWxOrderUserBySession();
		String deptId = this.getRequest().getParameter("deptId");
		List<Map> staffList = xOrdersService.getStaffInfo(xuser.getCompanyId(),
				deptId);
		PrintWriter out = null;
		this.getResponse().setContentType("text/json; charset=utf-8");
		if (staffList != null) {
			JSONArray jsonArray = JSONArray.fromObject(staffList);
			result = "{\"success\":\"true\",\"data\":" + jsonArray.toString()
					+ "}";
		}
		try {
			out = this.getResponse().getWriter();
			out.write(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	/**
	 * add dingzhj at date 2017-03-04 保存指定明细
	 * 
	 * @return
	 * @throws Exception
	 */
	public void saveXAppointRelation() throws Exception {
		String result = "N";
		try {
			// TODO 传值
			XUser loginUser = this.getWxOrderUserBySession();
			String appointId = this.getRequest().getParameter("appointId");
			String appointName = this.getRequest().getParameter("appointName");
			XAppointRelation xAppointRelation = new XAppointRelation();
			xAppointRelation.setLeadId(loginUser.getUserId());
			xAppointRelation.setLeadName(loginUser.getUserName());
			xAppointRelation.setAppointId(appointId);
			xAppointRelation.setAppointName(appointName);
			if (loginUser != null) {
				xAppointRelation.setInsUser(loginUser.getUserName());
			}
			XAppointRelation xar = xOrdersService
					.saveXAppointRelation(xAppointRelation);
			if (xar != null) {
				result = "Y";
			}
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try {
				out = this.getResponse().getWriter();
				out.write(result);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("指定关系保存失败!");
		}
	}

	/**
	 * 订餐预定
	 * 
	 * @method: buyReserveFood
	 * @Description: TODO
	 * @throws Exception
	 * @author : dingzhj
	 * @date 2017-3-22 下午4:53:36
	 */
	public void buyReserveFood() throws Exception {
		String orderInfo = this.getRequest().getParameter("orderInfo");// 套餐
		String type = this.getRequest().getParameter("type");// 订餐类型
		String ldId = this.getRequest().getParameter("ldId");// 领导ID
		String vistorName = this.getRequest().getParameter("vistorName");
		String time = this.getRequest().getParameter("time");
		String recId = this.getRequest().getParameter("recId");
		List<Map> listMap = new ArrayList<Map>();
		JSONArray jsonArray = JSONArray.fromObject(orderInfo);
		Collection java_collection = JSONArray.toCollection(jsonArray);
		if (java_collection != null && !java_collection.isEmpty()) {
			Iterator it = java_collection.iterator();
			while (it.hasNext()) {
				JSONObject jsonObj = JSONObject.fromObject(it.next());
				Map map = (Map) jsonObj;
				listMap.add(map);
			}
		}

		String result = orderReserve(listMap, type, ldId, vistorName,time,recId);
		try {
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			out = this.getResponse().getWriter();
			out.write(result);
			if (out != null) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("订餐失败");
		}

	}

	// type:0、自己 1、客餐 2、领导
	// userId 当前登录者的id
	// orderId 花钱买饭的id
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public String orderReserve(List<Map> ListMap, String type, String id,
			String vistorName,String time,String recId) throws ParseException {
		synchronized (this) {
			boolean b = true;
			Map<String, Object> msgMap;
			Map result = new HashMap<String, String>();
			Map m = new HashMap();
			Gson g = new Gson();
			int z_num = 0;
			// 遍历订餐map
			for (Map map : ListMap) {
				String foodId = (String) map.get("id");// 套餐ID
				XFood xFood = xFoodService.get(foodId);
				String foodNum = map.get("num").toString();// 套餐数
				z_num += Integer.parseInt(foodNum)*xFood.getSellPrice();
				// 查询当前库存
				//String num = xOrdersService.getFoodBillByFoodId(foodId);
				String weeknum =  map.get("weeknum").toString();
				String num = xOrdersService.getFoodBillReserveByFoodId(DateWeek.dinAndLun(weeknum),foodId);
				if (num != null && num != "") {
					if (Integer.parseInt(num) < Integer.parseInt(foodNum)) {
						b = false;
						result.put("status", "false");
						result.put("msg", "套餐不足");
						break;
					} else {
						b = true;
					}
				} else {
					b = false;
					result.put("status", "false");
					result.put("msg", "套餐不足");
					return g.toJson(result);
				}

			}
			if (b) {
				// 扣除订餐人的余额
				// todomap
				XUser orderUser = getWxOrderUserBySession();
				String userId = orderUser.getUserId();
				// String type = this.getRequest().getParameter("type");
				String dcType = "0";
				if ("LD".equals(type)) {
					userId = id;
					dcType = "2";
					 XUser xUser = xUserService.getByUserId(userId);
					  vistorName  = xUser.getRealName();
				} else if ("KR".equals(type)) {
					dcType = "1";
				}else{
					 XUser xUser = xUserService.getByUserId(userId);
					 vistorName  = xUser.getRealName();
				}
				// String orderId =
				// this.getRequest().getParameter("orderId");//给谁订餐的ID
				// orderId = "8";
				msgMap = xOrdersService.orderReserve(dcType, orderUser, userId,
						ListMap, z_num, vistorName,time,recId);
				if ("true".equals(msgMap.get("status"))) {
					/*
					 * for(Iterator it = m.keySet().iterator() ; it.hasNext();){
					 * String key = it.next().toString();
					 * Global.getNumMap().put(key, m.get(key)); }
					 */
					result.put("status", "success");
					result.put("msg", "订餐成功");
				} else {
					result.put("status", "false");
					result.put("msg", msgMap.get("msg"));
				}
			}
			return g.toJson(result);
		}
	}

	
	/**
	 * @method: getFoodList
	 * @Description: 查菜单
	 * @author : lig
	 * @date 2017-3-23 上午10:41:47
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getFoodList() {
		Map map = new HashMap();
		try {
			//周几
			String week = this.getRequest().getParameter("week");
			//午餐或晚餐
			String type = this.getRequest().getParameter("type");
			//选中的美食商家
			String checkedFoodBusiness = this.getRequest().getParameter("checkedFoodBusiness");
			
			//查询所有美食商家
			List<XUser> foodBusinessList = xUserService.findFoodBusiness();
			List<Map> foodList = new ArrayList<Map>();
			List<Map> noticeList = new ArrayList<Map>();
			if(StringUtils.isNotBlank(checkedFoodBusiness)){
				foodList = xOrdersService.getFoodByCondtion(week, type, checkedFoodBusiness);
				//公告
				noticeList = xNoticeService.getNoticeByCondtion(week, type, checkedFoodBusiness);
			}else{
				if(foodBusinessList != null && foodBusinessList.size() > 0){
					foodList = xOrdersService.getFoodByCondtion(week, type, foodBusinessList.get(0).getUserName());
					//公告
					noticeList = xNoticeService.getNoticeByCondtion(week, type, foodBusinessList.get(0).getUserName());
				}
			}
			
			map.put("foodList", foodList);
			if(noticeList != null){
				map.put("noticeList", noticeList);
			}else{
				map.put("noticeList", "");
			}
			
			Gson gson = new Gson();
			jsonString = gson.toJson(map);
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try {
				out = this.getResponse().getWriter();
				out.write(jsonString);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
				 
	/**
	 * 我的预定
	 * @method: toMyReserveOrder
	 * @Description: TODO
	 * @return
	 * @throws Exception
	 * @author : dingzhj
	 * @date 2017-3-21 下午3:52:48
	 */
	public String toMyReserveOrder() throws Exception{
		try {
			String userId = this.getWxOrderUserBySession().getUserId();
			this.myOrderList = xOrdersService.getReserveMyOrder(userId);
			//this.getRequest().setAttribute("myOrderList", myOrderList);
			this.setForwardPage("/wx_orders/myReserveOrder.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("我的订单查询失败!");
		}
	}
					
	/**
	 * @method: goReInfo
	 * @Description: 预算结算页
	 * @return
	 * @author : lig
	 * @throws Exception 
	 * @date 2017-3-24 上午11:22:36
	 */
	public String goReInfo() throws Exception{
		//返回上一页所需要带的值
		String type = this.getRequest().getParameter("type");
		String leadId = this.getRequest().getParameter("leadId");
		String infos = this.getRequest().getParameter("infos");
		String orderType = this.getRequest().getParameter("orderType");
		String foodBusiness = this.getRequest().getParameter("foodBusiness");
		String checkedBusiness = this.getRequest().getParameter("checkedBusiness");
		this.getRequest().setAttribute("type", type);
		this.getRequest().setAttribute("leadId", leadId);
		this.getRequest().setAttribute("infos", infos);
		this.getRequest().setAttribute("orderType", orderType);
		this.getRequest().setAttribute("foodBusiness", foodBusiness);
		this.getRequest().setAttribute("checkedBusiness", checkedBusiness);
		
		String flag = this.getRequest().getParameter("flag");
		String orderInfo = null;
		String vistorName = null;
		if(StringUtils.isNotBlank(flag) && flag.equals("0")){
			String orderInfoOld = this.getRequest().getParameter("orderInfo");
			String vistorNameOld = this.getRequest().getParameter("vistorName");
			//解决乱码问题
			orderInfo = new String(orderInfoOld.getBytes("iso8859-1"),"utf-8");
			vistorName = new String(vistorNameOld.getBytes("iso8859-1"),"utf-8");
		}else{
			orderInfo = this.getRequest().getParameter("orderInfo");
			vistorName = this.getRequest().getParameter("vistorName");
		}
		
		JSONObject orderJson = null;
		if(StringUtil.isNotEmpty(orderInfo)){
			orderJson = JSONObject.fromObject(orderInfo);
			//获取订餐对象
			 String orderObj = orderJson.getString("orderObj");
			//订餐对象(ZJ:自己、KR：客人、LD：领导、ZD：指定)
			 //获取扣款对象姓名、账户余额
			 Map map = new HashMap();
			 if(orderObj.equals("ZJ")){
				 map = xOrdersService.getMoney("ZJ", this.getWxOrderUserBySession().getUserId());	//查询出自己余额//this.getOrderUserBySession().getUserId();
			 }else if(orderObj.equals("KR")){
				 map = xOrdersService.getMoney("KR", this.getWxOrderUserBySession().getDeptId());	//查询出部门余额 //this.getOrderUserBySession().getDeptId();
			 }else if(orderObj.equals("LD")){
				 String ldId =  orderJson.getString("ldId");
				 map = xOrdersService.getMoney("LD", ldId);	//查询出领导的余额//this.getOrderUserBySession().getUserId();
			 }
			 orderJson.put("obj", map);
		}
		
		//选择切换的地址
		String checkedAddress = this.getRequest().getParameter("checkedAddress");
		
		//餐饮公司
		XUser foodCompany = xUserService.findFoodBusinessByUsername(checkedBusiness);
		this.getRequest().setAttribute("foodCompany", foodCompany);
		
		//收货人信息
		//当前登录用户信息
		XUser orderUser = (XUser)this.getSession().getAttribute("wxOrderLoginUser");
		setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
		//所有派送地址信息
		List<Map> addressList = xFoodSendAddressService.getAddressList2(getQf(),checkedBusiness);
		List<XFoodSendAddress> xFoodSendAddressList = new ArrayList<XFoodSendAddress>();
		if(addressList != null && addressList.size() > 0){
			for (Map addressMap : addressList) {
				if(StringUtils.isNotBlank(orderUser.getSendDefaultAddress())){
					if(orderUser.getSendDefaultAddress().equals(addressMap.get("id"))){ //默认地址
						XFoodSendAddress xFoodSendAddressDefault = new XFoodSendAddress();
						xFoodSendAddressDefault.setId(addressMap.get("id").toString());
						xFoodSendAddressDefault.setRegionId(addressMap.get("region_id").toString());
						xFoodSendAddressDefault.setRegionName(addressMap.get("region_name").toString());
						xFoodSendAddressDefault.setPark(addressMap.get("park").toString());
						xFoodSendAddressDefault.setHighBuilding(addressMap.get("high_building").toString());
						xFoodSendAddressDefault.setUnit(addressMap.get("unit").toString());
						//xFoodSendAddressDefault.setFloor(addressMap.get("floor").toString());
						xFoodSendAddressDefault.setRoomNum(addressMap.get("room_num").toString());
						
						this.getRequest().setAttribute("xFoodSendAddressDefault", xFoodSendAddressDefault);
					}else{
						XFoodSendAddress xFoodSendAddress = new XFoodSendAddress();
						xFoodSendAddress.setId(addressMap.get("id").toString());
						xFoodSendAddress.setRegionId(addressMap.get("region_id").toString());
						xFoodSendAddress.setRegionName(addressMap.get("region_name").toString());
						xFoodSendAddress.setPark(addressMap.get("park").toString());
						xFoodSendAddress.setHighBuilding(addressMap.get("high_building").toString());
						xFoodSendAddress.setUnit(addressMap.get("unit").toString());
						//xFoodSendAddress.setFloor(addressMap.get("floor").toString());
						xFoodSendAddress.setRoomNum(addressMap.get("room_num").toString());
						xFoodSendAddressList.add(xFoodSendAddress);
					}
				}else{
					XFoodSendAddress xFoodSendAddress = new XFoodSendAddress();
					xFoodSendAddress.setId(addressMap.get("id").toString());
					xFoodSendAddress.setRegionId(addressMap.get("region_id").toString());
					xFoodSendAddress.setRegionName(addressMap.get("region_name").toString());
					xFoodSendAddress.setPark(addressMap.get("park").toString());
					xFoodSendAddress.setHighBuilding(addressMap.get("high_building").toString());
					xFoodSendAddress.setUnit(addressMap.get("unit").toString());
					//xFoodSendAddress.setFloor(addressMap.get("floor").toString());
					xFoodSendAddress.setRoomNum(addressMap.get("room_num").toString());
					xFoodSendAddressList.add(xFoodSendAddress);
				}
			}
		}
		
		this.getRequest().setAttribute("xFoodSendAddressList", xFoodSendAddressList);
		
		if(StringUtils.isNotBlank(checkedAddress)){
			XFoodSendAddress xFoodSendAddressChecked = xFoodSendAddressService.get(checkedAddress);
			XFoodSendRegion xFoodSendRegion = xFoodSendRegionService.get(xFoodSendAddressChecked.getRegionId());
			xFoodSendAddressChecked.setRegionName(xFoodSendRegion.getRegionName());
			this.getRequest().setAttribute("xFoodSendAddressChecked", xFoodSendAddressChecked);
		}
		
		this.getRequest().setAttribute("orderInfo", orderJson.toString());
		this.getRequest().setAttribute("vistorName", vistorName);
		this.setForwardPage("/wx_orders/re-orderInfo.jsp");
		return SUCCESS;
	}
		
	
	/**
	 * 我的指定
	 * @method: toAppointMyOrder
	 * @Description: TODO
	 * @return
	 * @throws Exception
	 * @author : dingzhj
	 * @date 2017-3-31 下午3:57:18
	 */
	public String toAppointMyOrder() throws Exception{
		try {
			String userId = this.getWxOrderUserBySession().getUserId();
			this.myOrderList = xOrdersService.getMyOrderAppoint(userId);
			//this.getRequest().setAttribute("myOrderList", myOrderList);
			this.setForwardPage("/wx_orders/myOrderAppoint.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("我的订单查询失败!");
		}
	} 

	@SuppressWarnings("rawtypes")
	public void getUserList() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			String str = this.getRequest().getParameter("str");
			List<Map> userMap = xUserService.getXUser(getQf(),str);
			StringBuffer buff = new StringBuffer("{\"Total\":")
			.append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson=new Gson();
			jsonString=gson.toJson(userMap);
			buff.append(jsonString);
			if(userMap==null){
				buff.append("[]");
			}
			buff.append("}");
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try {
				out = this.getResponse().getWriter();
				out.write(buff.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(out!=null){
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("查询列表失败!");
		}
	}
	
	/**
	 * 根据订单id查询饭票所需要的信息
	 */
	@SuppressWarnings("unchecked")
	public void mealTicketSelect() {
		try {
			String orderId = this.getRequest().getParameter("orderId");
			List<Map> myOrderList = xOrdersService.getMyOrderById2(orderId);
			
			XUser foodBusiness = xUserService.findFoodBusinessByUsername(myOrderList.get(0).get("ins_user").toString());
			myOrderList.get(0).put("foodBusiness", foodBusiness.getFoodCompanyName());
			
			Gson gson = new Gson();
			jsonString = gson.toJson(myOrderList);
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try {
				out = this.getResponse().getWriter();
				out.write(jsonString);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
		}

	}
	
}