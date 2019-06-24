package com.kssj.product.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.kssj.auth.model.XUser;
import com.kssj.auth.service.XUserService;
import com.kssj.base.util.Constants;
import com.kssj.base.util.ListUtil;
import com.kssj.base.util.StringUtil;
import com.kssj.frame.web.action.BaseAction;
import com.kssj.order.model.XAppointRelation;
import com.kssj.order.model.XOrders;
import com.kssj.order.service.XOrdersService;
import com.kssj.product.model.MyProOrder;
import com.kssj.product.model.XProCategory;
import com.kssj.product.model.XReceiverInfo;
import com.kssj.product.service.XProCategoryService;
import com.kssj.product.service.XProductsService;
import com.kssj.product.service.XReceiverInfoService;

/**
 * 产品
 * 
 * @author zhangxuejiao
 * 
 */
@SuppressWarnings("serial")
public class XProductsAction extends BaseAction {

	@Resource
	private XOrdersService xOrdersService;

	@Resource
	private XUserService xUserService;

	@Resource
	private XProductsService xProductsService;
	
	@Resource
	private XReceiverInfoService xReceiverInfoService;
	
	@Resource
	private XProCategoryService xProCategoryService;

	/**
	 * 去往产品列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String toOrderingPage() throws Exception {
		try {
			String infos = this.getRequest().getParameter("infos");
			if (StringUtil.isNotEmpty(infos)) {
				this.getRequest().setAttribute("infos", infos);
			}

			// 判断订购产品对象 isKR：客人 isLD：领导 isZD：指定人订购产品
			boolean isKR = false;
			boolean isLD = false;
			boolean isZD = false;
			XUser orderUser = this.getOrderUserBySession();
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
			
			String proName = this.getRequest().getParameter("proName");
			this.getSession().setAttribute("proName", proName);
			
			//供产品类别下拉选使用
			List<XProCategory> proCategoryList = xProCategoryService.getAllAvailable();
			this.getRequest().setAttribute("proCategoryList", proCategoryList);
			
			this.setForwardPage("/orders/ordering.jsp");
			String ISBP = this.getRequest().getParameter("type");
			if (StringUtil.isNotEmpty(ISBP) && ISBP.equals("BP")) {
				this.setForwardPage("/products/reserve.jsp");
			}
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("产品查询失败!");
		}
	}

	/**
	 * 加载产品列表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void toBPPage() {
		Map map = new HashMap();
		try {
			// 查询条件
			String proName = this.getRequest().getParameter("proName");
			String proCateId = this.getRequest().getParameter("proCateId");
			List<Map> productList = xProductsService.getProductsByCondtion(proName, proCateId);
			map.put("productList", productList);

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

		}
	}

	/**
	 * 订单信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	public String goReSum() {
		String orderInfo = this.getRequest().getParameter("orderInfo");
		String vistorName = this.getRequest().getParameter("vistorName");
		JSONObject orderJson = null;
		if (StringUtil.isNotEmpty(orderInfo)) {
			orderJson = JSONObject.fromObject(orderInfo);
			// 获取订购产品对象
			String orderObj = orderJson.getString("orderObj");
			// 订购产品对象(ZJ:自己、KR：客人、LD：领导、ZD：指定)
			// 获取扣款对象姓名、账户余额
			Map map = new HashMap();
			if (orderObj.equals("ZJ")) {
				map = xOrdersService.getMoney("ZJ", this
						.getOrderUserBySession().getUserId()); // 查询出自己余额
			} else if (orderObj.equals("KR")) {
				map = xOrdersService.getMoney("KR", this
						.getOrderUserBySession().getDeptId()); // 查询出部门余额
																
			} else if (orderObj.equals("LD")) {
				String ldId = orderJson.getString("ldId");
				map = xOrdersService.getMoney("LD", ldId); // 查询出领导的余额
			}
			orderJson.put("obj", map);
		}
		
		//收货人信息
		//当前登录用户信息
		XUser orderUser = (XUser)this.getSession().getAttribute("orderLoginUser");
		//查询当前登录用户的所有收货人信息
		List<XReceiverInfo> xReceiverInfoList = xReceiverInfoService.findAllReceiversInfo(orderUser.getUserId());
		List<XReceiverInfo> xReceiverInfoListNew = new ArrayList<XReceiverInfo>();
		if(xReceiverInfoList != null && xReceiverInfoList.size() > 0){
			for (XReceiverInfo xReceiverInfo : xReceiverInfoList) {
				xReceiverInfo.setRecArea(xReceiverInfo.getRecArea().replaceAll("/", " "));
				if(xReceiverInfo.getRecDefaultStatus() == 0){ //默认地址
					this.getRequest().setAttribute("xReceiverInfoDefault", xReceiverInfo);
				}
				if(xReceiverInfo.getRecDefaultStatus() == 1){
					xReceiverInfoListNew.add(xReceiverInfo);
				}
			}
		}
		this.getRequest().setAttribute("xReceiverInfoList", xReceiverInfoListNew);

		this.getRequest().setAttribute("orderInfo", orderJson.toString());
		this.getRequest().setAttribute("vistorName", vistorName);
		this.setForwardPage("/products/re-orderInfo.jsp");
		return SUCCESS;
	}

	/**
	 * 产品预订
	 * 
	 * @throws Exception
	 */
	public void buyReserveProducts() throws Exception {
		String orderInfo = this.getRequest().getParameter("orderInfo"); //套餐
		String type = this.getRequest().getParameter("type"); //订购产品人的类型
		String ldId = this.getRequest().getParameter("ldId"); //领导ID
		String vistorName = this.getRequest().getParameter("vistorName"); //客人
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

		String result = orderReserve(listMap, type, ldId, vistorName, recId);
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
			throw new Exception("订购产品失败");
		}

	}

	// type:0、自己 1、客餐 2、领导
	// userId 当前登录者的id
	// orderId 花钱买产品的id
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public String orderReserve(List<Map> ListMap, String type, String id,
			String vistorName, String recId) throws Exception {
		synchronized (this) {
			boolean b = true;
			//Map<String, Object> msgMap = new HashMap<String, Object>();
			Map result = new HashMap<String, String>();
			Map m = new HashMap();
			Gson g = new Gson();
			int z_num = 0;
			//遍历订购产品map
			for (Map map : ListMap) {
				String productId = (String) map.get("id");// 产品ID
				String productNum = map.get("num").toString();// 产品数
				String total = map.get("total").toString(); //产品小计
				z_num += Integer.parseInt(total);
				//当前库存
				String num = xProductsService.getProRemainReserveByProductId(productId);
				if (num != null && num != "") {
					if (Integer.parseInt(num) < Integer.parseInt(productNum)) {
						b = false;
						result.put("status", "false");
						result.put("msg", "该产品数量不足，您可以通过外链地址进行购买");
						break;
					} else {
						b = true;
					}
				} else {
					b = false;
					result.put("status", "false");
					result.put("msg", "该产品数量不足，您可以通过外链地址进行购买");
					return g.toJson(result);
				}

			}
			if (b) {
				// 扣除订购产品人的余额
				XUser orderUser = getOrderUserBySession();
				String userId = orderUser.getUserId();
				String dcType = "0";
				if ("LD".equals(type)) {
					userId = id;
					XUser xUser = xUserService.getByUserId(userId);
					vistorName = xUser.getRealName();
					dcType = "2";
				} else if ("KR".equals(type)) {
					dcType = "1";
				} else {
					XUser xUser = xUserService.getByUserId(userId);
					vistorName = xUser.getRealName();
				}
				
				Map<String, Object> msgMap = xProductsService.orderReserve(dcType, orderUser, userId, ListMap, z_num, vistorName, recId);
				if ("true".equals(msgMap.get("status"))) {
					result.put("status", "success");
					result.put("msg", "订购产品成功");
				} else {
					result.put("status", "false");
					result.put("msg", msgMap.get("msg"));
				}
			}
			return g.toJson(result);
		}
	}
	
	/**
	 * 产品订单详情
	 * @return
	 * @throws Exception
	 */
	public String toMyOrder() throws Exception{
		try {
			String userId = this.getOrderUserBySession().getUserId();
			List<MyProOrder> myOrderList = xProductsService.getMyOrder(userId);
			this.getRequest().setAttribute("myOrderList", myOrderList);
			this.setForwardPage("/products/myOrder.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("我的订单查询失败!");
		}
	}
	
	/**
	 * 产品预订信息
	 * @return
	 * @throws Exception
	 */
	public String toMyReserveOrder() throws Exception{
		try {
			String userId = this.getOrderUserBySession().getUserId();
			List<MyProOrder> myOrderList = xProductsService.getReserveMyOrder(userId);
			this.getRequest().setAttribute("myOrderList", myOrderList);
			this.setForwardPage("/products/myReserveOrder.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("我的订单查询失败!");
		}
	}
	
	/**
	 * 取消订购产品
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteOrder() throws Exception{
		 Map result = new HashMap<String, String>();
		 Gson g = new Gson();
		 try {
			 //当前订单ID
			 String orderId = this.getRequest().getParameter("orderId");
			 //取消订单的总money
			 String moneyTotal = this.getRequest().getParameter("moneyTotal");
			 //当前登录用户信息
			 XUser orderUser = (XUser)this.getSession().getAttribute("orderLoginUser");
			 Map<String,Object> msgMap = new HashMap<String, Object>();
			 msgMap = xProductsService.deleteOrder(orderId,moneyTotal,orderUser);
			 if("true".equals(msgMap.get("status"))){
				 result.put("success", "success");
				 result.put("msg", "取消订购产品成功");
			 }else{
				 result.put("success", "false");
				 result.put("msg","取消订购产品失败");
			 }
		} catch (Exception e) {
			logger.error(e.getMessage());
 			throw new Exception("取消订购产品失败");
		}
		 
	 	PrintWriter out = null;  
		this.getResponse().setContentType("text/json; charset=utf-8");  
		out = this.getResponse().getWriter();  
		out.write( g.toJson(result));
		if(out!=null){
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 指定人查询产品订单信息
	 * @return
	 * @throws Exception
	 */
	public String toAppointMyOrder() throws Exception{
		try {
			String userId = this.getOrderUserBySession().getUserId();
			List<MyProOrder> myOrderList = xProductsService.getMyOrderAppoint(userId);
			this.getRequest().setAttribute("myOrderList", myOrderList);
			this.setForwardPage("/products/myOrderAppoint.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("我的订单查询失败!");
		}
	}
	
	/**
	 * 确认收货
	 * 
	 * @return
	 * @throws Exception
	 */
	public String upDateRecStatus() throws Exception{
		String result="Y";
		try {
			String orderId = this.getRequest().getParameter("orderId");
			XOrders xOrders = xProductsService.getResOrder(orderId);
			xOrders.setRecFlag(0); //手动确认收货
			xOrdersService.save(xOrders);
			
			this.getResponse().setContentType("text/json; charset=utf-8");
			this.getResponse().getWriter().print(result);
		} catch (Exception e) {
			result="N";
			logger.error(e.getMessage());
			throw new Exception("修改失败!!!");
		}
		return NONE;
	}
	
	
}
