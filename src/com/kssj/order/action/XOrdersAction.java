package com.kssj.order.action;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.kssj.auth.model.XUser;
import com.kssj.auth.service.XUserService;
import com.kssj.base.util.Constants;
import com.kssj.base.util.DateUtil;
import com.kssj.base.util.DateUtils2;
import com.kssj.base.util.DateWeek;
import com.kssj.base.util.ListUtil;
import com.kssj.base.util.StringUtil;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.command.sql.SqlSpellerDbType;
import com.kssj.frame.web.action.BaseAction;
import com.kssj.order.model.MyAccount;
import com.kssj.order.model.MyFood;
import com.kssj.order.model.MyOrder;
import com.kssj.order.model.XAppointRelation;
import com.kssj.order.model.XDetailRecord;
import com.kssj.order.model.XFood;
import com.kssj.order.model.XFoodSendAddress;
import com.kssj.order.model.XFoodSendRegion;
import com.kssj.order.model.XOrders;
import com.kssj.order.service.XDetailRecordService;
import com.kssj.order.service.XFoodSendAddressService;
import com.kssj.order.service.XFoodSendRegionService;
import com.kssj.order.service.XFoodService;
import com.kssj.order.service.XNoticeService;
import com.kssj.order.service.XOrdersService;
import com.kssj.product.model.XReceiverInfo;
import com.kssj.product.service.XReceiverInfoService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
public class XOrdersAction extends BaseAction implements ModelDriven<XOrders> {
	
	@Resource
	private XFoodSendAddressService xFoodSendAddressService;
	
	@Resource
	private XFoodSendRegionService xFoodSendRegionService;
	
	XOrders xOrders=new XOrders();
	
	@Override
	public XOrders getModel() {
		return xOrders;
	}
	
	@Resource
	private XOrdersService xOrdersService;
	@Resource
	private XUserService xUserService;
	@Resource
	private XDetailRecordService xDetailRecordService;
	@Resource
	private XNoticeService xNoticeService;
	
	@Resource
	private XFoodService xFoodService;
	
	
	/**
	 * 
	 * @method: orderFood
	 * @Description: 订餐
	 * @author : zhaoziyun
	 * @date 2017-3-3 下午4:45:55
	 */
	public void buyFood() throws Exception{
		String orderInfo = this.getRequest().getParameter("orderInfo");//套餐
		String type = this.getRequest().getParameter("type");//订餐类型
		String ldId = this.getRequest().getParameter("ldId");//领导ID
		String vistorName = this.getRequest().getParameter("vistorName");
		String recId = this.getRequest().getParameter("recId"); //收餐地址id
		List<Map> listMap =new ArrayList<Map>();
		JSONArray jsonArray = JSONArray.fromObject(orderInfo);  
        Collection java_collection=JSONArray.toCollection(jsonArray);  
        if(java_collection!=null && !java_collection.isEmpty())  
        {  
            Iterator it=java_collection.iterator();  
            while(it.hasNext())  
            {  
                JSONObject jsonObj=JSONObject.fromObject(it.next());  
                Map map = (Map)jsonObj;
                listMap.add(map);
            }  
        }
		 
		String result = order(listMap,type,ldId,vistorName,recId);
		try {
			PrintWriter out = null;  
			this.getResponse().setContentType("text/json; charset=utf-8");  
			out = this.getResponse().getWriter();  
			out.write(result);  
				if(out!=null){
					out.flush();
					out.close();
				}
		} catch (Exception e) {
			logger.error(e.getMessage());
 			throw new Exception("订餐失败");
		}
		
	}
	
	/**
	 * 订餐预定
	 * @method: buyReserveFood
	 * @Description: TODO
	 * @throws Exception
	 * @author : dingzhj
	 * @date 2017-3-22 下午4:53:36
	 */
	public void buyReserveFood() throws Exception{
		String orderInfo = this.getRequest().getParameter("orderInfo");//套餐
		String type = this.getRequest().getParameter("type");//订餐类型
		String ldId = this.getRequest().getParameter("ldId");//领导ID
		String vistorName = this.getRequest().getParameter("vistorName");
		String time = this.getRequest().getParameter("time");
		String recId = this.getRequest().getParameter("recId");
		List<Map> listMap =new ArrayList<Map>();
		JSONArray jsonArray = JSONArray.fromObject(orderInfo);  
        Collection java_collection=JSONArray.toCollection(jsonArray);  
        if(java_collection!=null && !java_collection.isEmpty())  
        {  
            Iterator it=java_collection.iterator();  
            while(it.hasNext())  
            {  
                JSONObject jsonObj=JSONObject.fromObject(it.next());  
                Map map = (Map)jsonObj;
                listMap.add(map);
            }  
        }
		 
		String result = orderReserve(listMap,type,ldId,vistorName,time,recId);
		try {
			PrintWriter out = null;  
			this.getResponse().setContentType("text/json; charset=utf-8");  
			out = this.getResponse().getWriter();  
			out.write(result);  
				if(out!=null){
					out.flush();
					out.close();
				}
		} catch (Exception e) {
			logger.error(e.getMessage());
 			throw new Exception("订餐失败");
		}
		
	}	
	
	//type:0、自己 1、客餐 2、领导
		//userId 当前登录者的id
		//orderId 花钱买饭的id
		 @SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
		public  String orderReserve(List<Map> ListMap,String type,String id,String vistorName,String time,String recId) throws ParseException{
			 synchronized(this){
				 boolean b =true;
				 Map<String,Object> msgMap;
				 Map result = new HashMap<String, String>();
				 Map m = new HashMap();
				 Gson g = new Gson();
				 int z_num=0;
				 //遍历订餐map
				 for (Map map:ListMap){
					 	String foodId = (String) map.get("id");//套餐ID
					 	XFood xFood = xFoodService.get(foodId);
					 	String foodNum = map.get("num").toString();//套餐数
					 	z_num +=Integer.parseInt(foodNum)*xFood.getSellPrice();
					 	//查询当前库存
					 	//String num  = xOrdersService.getFoodBillByFoodId(foodId);
					 	String weeknum =  map.get("weeknum").toString();
						String num = xOrdersService.getFoodBillReserveByFoodId(DateWeek.dinAndLun(weeknum),foodId);
					 	if(num != null && num !=""){
					 		if(Integer.parseInt(num)<Integer.parseInt(foodNum)){
								 b= false;
								 result.put("status", "false");
								 result.put("msg", "套餐不足");
								 break;
						 	 }else{
						 		 b= true;
						 	 }
					 	}else{
					 		 b= false;
							 result.put("status", "false");
							 result.put("msg", "套餐不足");
							 return g.toJson(result);
					 	}
					 	 
				 }
				  if(b){
					//扣除订餐人的余额 
					  //todomap
					  XUser orderUser = getOrderUserBySession();
					  String userId=orderUser.getUserId();
					  //String type = this.getRequest().getParameter("type");
					  String dcType="0";
					  if("LD".equals(type)){
						  userId =id;
						  XUser xUser = xUserService.getByUserId(userId);
						  vistorName  = xUser.getRealName();
						  dcType= "2";
					  }else if("KR".equals(type)){
						  dcType= "1";
					  }else{
						  XUser xUser = xUserService.getByUserId(userId);
						  vistorName  = xUser.getRealName();
					  }
					  //String orderId = this.getRequest().getParameter("orderId");//给谁订餐的ID
					  //orderId = "8";
						msgMap = xOrdersService.orderReserve(dcType, orderUser, userId,
								ListMap, z_num, vistorName,time,recId);
					  if("true".equals(msgMap.get("status"))){
						 /* for(Iterator it = m.keySet().iterator() ; it.hasNext();){ 
								String key = it.next().toString();
								Global.getNumMap().put(key, m.get(key)); 
								} */
						  result.put("status", "success");
						  result.put("msg", "订餐成功");
					  }else{
						  result.put("status", "false");
						  result.put("msg", msgMap.get("msg"));
					  }
				  }
				  return g.toJson(result);
			 }
		    }
	
	
	//type:0、自己 1、客餐 2、领导
	//userId 当前登录者的id
	//orderId 花钱买饭的id
	 @SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public  String order(List<Map> ListMap,String type,String id,String vistorName,String recId){
		 synchronized(this){
			 boolean b =true;
			 Map<String,Object> msgMap;
			 Map result = new HashMap<String, String>();
			 Map m = new HashMap();
			
			 Gson g = new Gson();
			 int z_num=0; //订单总价钱
			 //遍历订餐map
			 for (Map map:ListMap){
				 	String foodId = (String) map.get("id");//套餐ID
				 	XFood xFood = xFoodService.get(foodId);
				 	String foodNum = map.get("num").toString();//套餐数
				 	z_num +=Integer.parseInt(foodNum)*xFood.getSellPrice();
				 	//查询当前库存
				 	String num  = xOrdersService.getFoodBillByFoodId(foodId);
				 	if(num != null && num !=""){
				 		if(Integer.parseInt(num)<Integer.parseInt(foodNum)){
							 b= false;
							 result.put("status", "false");
							 result.put("msg", "套餐不足");
							 break;
					 	 }else{
					 		 b= true;
					 	 }
				 	}else{
				 		 b= false;
						 result.put("status", "false");
						 result.put("msg", "套餐不足");
						 return g.toJson(result);
				 	}
				 	 
			 }
			  if(b){
				//扣除订餐人的余额 
				  //todomap
				  XUser orderUser = getOrderUserBySession();
				  String userId=orderUser.getUserId();
				  //String type = this.getRequest().getParameter("type");
				  String dcType="0";
				  // 根据用户ID查询 
				 
				  if("LD".equals(type)){
					  userId =id;
					  dcType= "2";
					  XUser xUser = xUserService.getByUserId(userId);
					  vistorName  = xUser.getRealName();
				  }else if("KR".equals(type)){
					  dcType= "1";
				  }else{
					  XUser xUser = xUserService.getByUserId(userId);
					  vistorName  = xUser.getRealName();
				  }
				  //String orderId = this.getRequest().getParameter("orderId");//给谁订餐的ID
				  //orderId = "8";
				  msgMap = xOrdersService.order(dcType,orderUser,userId,ListMap,z_num,vistorName,recId);
				  if("true".equals(msgMap.get("status"))){
					 /* for(Iterator it = m.keySet().iterator() ; it.hasNext();){ 
							String key = it.next().toString();
							Global.getNumMap().put(key, m.get(key)); 
							} */
					  result.put("status", "success");
					  result.put("msg", "订餐成功");
				  }else{
					  result.put("status", "false");
					  result.put("msg", msgMap.get("msg"));
				  }
			  }
			  return g.toJson(result);
		 }
	    }  
	 
	 /**
	  * 取消订单
	  * @return
	  * @throws Exception
	  */
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteOrder() throws Exception{
		 Map result = new HashMap<String, String>();
		 Gson g = new Gson();
		 try {
			 // 订餐类型
			// String flag = this.getRequest().getParameter("type");
			 //当前订单ID
			 String orderId = this.getRequest().getParameter("orderId");
			 //取消订单的总套餐份数
			 String foodNum = this.getRequest().getParameter("foodNum");
			 String orderType = this.getRequest().getParameter("orderType");
			 //当前登录用户信息
			 XUser orderUser = (XUser)this.getSession().getAttribute("orderLoginUser");
			 Map<String,Object> msgMap;
			 msgMap = xOrdersService.deleteOrder(orderId,foodNum,orderUser,orderType);
			 // 取出一周的菜谱当天的菜谱
			/* Map m = new HashMap();
			for(Iterator it = Global.getNumMap().keySet().iterator() ; it.hasNext();){ 
				String key = it.next().toString();
				m.put(key, Global.getNumMap().get(key));
			 } */
			 if("true".equals(msgMap.get("status"))){
				 /* for(Iterator it = m.keySet().iterator() ; it.hasNext();){
						String key = it.next().toString();
						Global.getNumMap().put(key, m.get(key)); 
						} */
				 result.put("success", "success");
				 result.put("msg", "取消订餐成功");
			 }else{
				 result.put("success", "false");
				 result.put("msg","取消订餐失败");
			 }
		} catch (Exception e) {
			logger.error(e.getMessage());
 			throw new Exception("取消订餐失败");
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
	 * @method: getCurrentOrder
	 * @Description:查询当前 订单
	 * @throws Exception
	 * @author : lig
	 * @date 2017-2-23 上午11:51:42
	 */
	@SuppressWarnings("rawtypes")
	public void getCurrentOrder() throws Exception{
		try {
			XUser xuser = this.getSysUserBySession();
			
			// add dingzhj at date 2017-03-03 
			String startTime =this.getRequest().getParameter("startTime");
			String endTime =this.getRequest().getParameter("endTime");
			System.out.println(startTime+"~~~"+endTime);
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			List<Map> orderList = xOrdersService.getCurrentOrder(getQf(),startTime,endTime,xuser);
			StringBuffer buff = new StringBuffer("{\"Total\":")
			.append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson = new Gson();
			jsonString = gson.toJson(orderList);
			buff.append(jsonString);
			if(!ListUtil.isNotEmpty(orderList)){
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
	 * 现场当前订单
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void getCurrentXCOrder() throws Exception{
		try {
			XUser xuser = this.getSysUserBySession();
			String startTime =this.getRequest().getParameter("startTime");
			String endTime =this.getRequest().getParameter("endTime");
			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			List<Map> orderList = xOrdersService.getCurrentXCOrder(getQf(),startTime,endTime,xuser);
			
			StringBuffer buff = new StringBuffer("{\"Total\":").append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson = new Gson();
			jsonString = gson.toJson(orderList);
			buff.append(jsonString);
			if(!ListUtil.isNotEmpty(orderList)){
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
	 * @method: getLedgerList
	 * @Description: 台账list
	 * @throws Exception
	 * @author : lig
	 * @date 2017-2-27 下午2:38:31
	 */
	public void getLedgerList() throws Exception{
		try {			
			XUser xuser = this.getSysUserBySession();
			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			// modify by dingzhj at date 2017-03-06 
			String startTime =this.getRequest().getParameter("startTime");
			String endTime =this.getRequest().getParameter("endTime");
			List<Map> orderList = xOrdersService.getLedgerList(getQf(),startTime,endTime,xuser);
			StringBuffer buff = new StringBuffer("{\"Total\":")
			.append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson = new Gson();
			jsonString = gson.toJson(orderList);
			buff.append(jsonString);
			if(!ListUtil.isNotEmpty(orderList)){
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
	}//(SqlQueryFilter filter , String flag, String deptId,String str);
	
	
	private SqlQueryFilter setQf(int i) {
		// TODO Auto-generated method stub
		return null;
	}






	/**
	 * @method: getRechargeObj
	 * @Description: 获取充值对象
	 * @throws Exception
	 * @author : lig
	 * @date 2017-2-28 上午11:17:52
	 */
	@SuppressWarnings("rawtypes")
	public void getRechargeObj() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			
			String flag = this.getRequest().getParameter("flag");
			String deptId = this.getRequest().getParameter("deptId");
			String str = this.getRequest().getParameter("str");
			String companyId = this.getRequest().getParameter("companyId");
			String otherCompanyId = "";
			XUser user = getSysUserBySession();
			if(companyId == null || companyId == ""){
				companyId = user.getCompanyId();
				//zhangxuejiao编写
				if(user.getOtherCompanyId() != null && user.getOtherCompanyId() != ""){
					otherCompanyId = user.getOtherCompanyId();
				}
			}
			
			List<Map> orderList = xOrdersService.getRechargeObj(getQf(), flag, deptId,companyId, str, otherCompanyId);
			StringBuffer buff = new StringBuffer("{\"Total\":")
			.append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson = new Gson();
			jsonString = gson.toJson(orderList);
			buff.append(jsonString);
			if(!ListUtil.isNotEmpty(orderList)){
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
	 * 
	 * @throws Exception
	 */
	public void getRechargeObjExcel() throws Exception{
		try {	
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			String flag = this.getRequest().getParameter("flag");
			String deptId = this.getRequest().getParameter("deptId");
			String str = this.getRequest().getParameter("str");
			String companyId = this.getRequest().getParameter("companyId");
			if(StringUtil.isEmpty(companyId)){
				XUser user = getSysUserBySession();
				companyId = user.getCompanyId();
			}
			List<Map> orderList = xOrdersService.getRechargeObjExcel( flag, deptId,companyId, str);
			this.getSession().setAttribute("orderList", orderList);
			StringBuffer buff = new StringBuffer("{\"Total\":")
			.append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson = new Gson();
			jsonString = gson.toJson(orderList);
			buff.append(jsonString);
			if(!ListUtil.isNotEmpty(orderList)){
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
	 * 充值记录导出Excel
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void getRechargeObjExcelL() throws Exception{
		try {	
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			String flag = this.getRequest().getParameter("flag");
			String deptId = this.getRequest().getParameter("deptId");
			String str = this.getRequest().getParameter("str");
			String companyId = this.getRequest().getParameter("companyId");
			String startTime = this.getRequest().getParameter("startTime");
			String endTime = this.getRequest().getParameter("endTime");
			if(StringUtil.isEmpty(companyId)){
				XUser user = getSysUserBySession();
				companyId = user.getCompanyId();
			}
			List<Map> chargeObjList = xOrdersService.getRechargeObjExcelL( flag, deptId,companyId, str, startTime, endTime);
			this.getSession().setAttribute("chargeObjList", chargeObjList);
			StringBuffer buff = new StringBuffer("{\"Total\":")
			.append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson = new Gson();
			jsonString = gson.toJson(chargeObjList);
			buff.append(jsonString);
			if(!ListUtil.isNotEmpty(chargeObjList)){
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
	 * @method: toRechargeList
	 * @Description: to充值页面
	 * @return
	 * @throws Exception
	 * @author : lig
	 * @date 2017-2-28 上午11:32:28
	 */
	public String toRechargeList() throws Exception{
		try {		
//			XUser user = getSysUserBySession();
//			List<Map> deptList = xOrdersService.getTreeData(user.getCompanyId(), false,"");
//			JSONArray jsonArray = JSONArray.fromObject(deptList);
//			this.getRequest().setAttribute("deptList", jsonArray.toString());
			this.setForwardPage("/jsp/order/orders/recharge_list.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("角色查询详情失败!");
		}
	}
	
	
	
	
	/**
	 * @method: doCharge
	 * @Description: 充值
	 * @throws Exception
	 * @author : lig
	 * @date 2017-2-28 下午4:02:49
	 */
	public void doCharge() throws Exception{
		String result = "Y";
		//标识 flag ： 1-》部门充值 2-》员工充值
		String flag = this.getRequest().getParameter("flag");
		String id = this.getRequest().getParameter("id");
		String balance = this.getRequest().getParameter("balance");
		try {			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			
			XDetailRecord XDetailRecord = new XDetailRecord();
			if(Integer.parseInt(balance)>=0){
				XDetailRecord.setType("0"); //0：收入 1：支出
			}else{
				XDetailRecord.setType("1");
			}
						
			XDetailRecord.setChangeMoney(balance);	//交易金额
			XDetailRecord.setRemark("充值");			//备注
			XDetailRecord.setInsTime(new Date()); 	//插入时间
			XDetailRecord.setInsUser(((XUser)this.getSession().getAttribute("loginUser")).getUserName());//插入者
			//获取当前余额
			String currentBalance = xOrdersService.getBalanceById(flag, id);
			//充值后余额
			//Double afterCharge = Double.valueOf(currentBalance==null?"0":currentBalance) + Double.valueOf(balance);
			int afterCharge = Integer.parseInt(currentBalance==null?"0":currentBalance) + Integer.parseInt(balance);
			XDetailRecord.setBalance(afterCharge+"");//余额
			if(flag.equals("1")){
				XDetailRecord.setDeptId(id);
			}else if(flag.equals("2")){
				XDetailRecord.setUserId(id);
			}
			//添加记录
			xDetailRecordService.save(XDetailRecord);
			//更新余额
			xOrdersService.updBalance(flag, id, (afterCharge+""));
			out = this.getResponse().getWriter();
			out.write(result);
				if(out!=null){
					out.flush();
					out.close();
				}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("充值失败!");
		}
	}
	
	
	/**
	 * @method: getTreeData
	 * @Description: tree数据
	 * @throws Exception
	 * @author : lig
	 * @date 2017-3-1 下午4:09:44
	 */
	public void getTreeData() throws Exception{
		try {		
			String companyId = this.getRequest().getParameter("companyId");
			XUser xUser = getSysUserBySession();
			String otherCompanyId = "";
			if(companyId == null || companyId ==""){
				if("1".equals(companyId)){
					companyId ="";
				}else{
					companyId = xUser.getCompanyId();
					if(xUser.getOtherCompanyId() != null && xUser.getOtherCompanyId() != ""){
						otherCompanyId = xUser.getOtherCompanyId();
					}
				}
			}
			List<Map> list = xOrdersService.getTreeData(companyId,false,"",otherCompanyId);
			JSONArray json = JSONArray.fromObject(list);
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try {
				out = this.getResponse().getWriter();
				out.write(json.toString());
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
	  * @method: getDeptTree
	  * @Description: 获取部门tree
	  * @throws Exception
	  * @author : lig
	  * @date 2017-3-2 下午5:11:29
	  */
	 public void getDeptTree() throws Exception{
			try {		
				
				XUser xuser = (XUser)this.getSession().getAttribute("loginUser");
				List<Map> list = xOrdersService.getDeptTree(xuser.getCompanyId());
				JSONArray json = JSONArray.fromObject(list);
				PrintWriter out = null;
				this.getResponse().setContentType("text/json; charset=utf-8");
				try {
					out = this.getResponse().getWriter();
					out.write(json.toString());
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
	  * @method: toRechargeAll
	  * @Description: to全部充值页面
	  * @return
	  * @throws Exception
	  * @author : lig
	  * @date 2017-3-4 上午10:46:41
	  */
	 public String  toRechargeAll() throws Exception{
		 	setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
		 	//标识 flag ： 1-》部门充值 2-》员工充值
			String flag = this.getRequest().getParameter("flag");
			String deptId = this.getRequest().getParameter("deptId");	//部门id
			String str = this.getRequest().getParameter("str"); //员工模糊查询
			str = new String(str.getBytes("ISO8859-1"),"utf-8");
			String companyId = this.getRequest().getParameter("companyId");
			String otherCompanyId = "";
			if(StringUtil.isEmpty(companyId)){
				XUser user = getSysUserBySession();
				companyId = user.getCompanyId();
				if(user.getOtherCompanyId() != null && user.getOtherCompanyId() != ""){
					otherCompanyId = user.getOtherCompanyId();
				}
			}
			try {		
				
				List<Map> orderList = xOrdersService.getRechargeObj(getQf(), flag, deptId,companyId ,str, otherCompanyId);
				
				String idsStr = this.getIds(orderList);
				this.getRequest().setAttribute("idsStr", idsStr);
				this.getRequest().setAttribute("flag", flag);
				this.setForwardPage("/jsp/order/orders/recharge_formAll.jsp");
				
				return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new Exception("角色查询详情失败!");
			}
		}
	 private String getIds(List<Map> lMap){
		 StringBuffer ids = new StringBuffer();
		 if(ListUtil.isNotEmpty(lMap)){
			 for (int i = 0; i < lMap.size(); i++) {
				ids.append(lMap.get(i).get("id"));
				ids.append(",");
			}
		 }
		 return ids.toString();
	 }
	 
	 
	 /**
		 * add dingzhj at date 2017-03-02 订餐管理明细
		 * @return
		 * @throws Exception
		 */
		public String toDetailed() throws Exception{
			try {
				//当前登录用户信息
				XUser foodBusiness = getSysUserBySession();
				this.getRequest().setAttribute("foodBusiness", foodBusiness);
				
				//setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
				String startTime = this.getRequest().getParameter("startTime");
				String endTime = this.getRequest().getParameter("endTime");
				this.getRequest().setAttribute("startTime", startTime);
				this.getRequest().setAttribute("endTime", endTime);
				
				List<MyFood> detaileList = xOrdersService.getDetailedOrder(startTime,endTime,foodBusiness);
				
				//JSONArray json = JSONArray.fromObject(detaileList);
				//logger.info("====显示不全数据bug===="+json.toString());
				
				this.getRequest().setAttribute("detaileList", detaileList);
				this.setForwardPage("/jsp/order/orders/xorders_detailed.jsp");
				return SUCCESS;
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new Exception("订餐管理明细!");
			}
		}
		
		/**
		 * 台帐明细
		 * @return
		 * @throws Exception
		 */
		@SuppressWarnings("rawtypes")
		public String toLedgerDetailed() throws Exception{
			try {
				
				//setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
				//当前登录用户信息
				XUser xuser = getSysUserBySession();
				this.getRequest().setAttribute("foodBusiness", xuser);
				
				String startTime = this.getRequest().getParameter("startTime");
				String endTime = this.getRequest().getParameter("endTime");
				this.getRequest().setAttribute("startTime", startTime);
				this.getRequest().setAttribute("endTime", endTime);
				
				List<MyAccount> detaileList = xOrdersService.getDetailedLedger(startTime,endTime,xuser);
				this.getRequest().setAttribute("detaileList", detaileList);
				this.setForwardPage("/jsp/order/orders/ledger_detailed.jsp");
				return SUCCESS;
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new Exception("订餐管理明细!");
			}
		}
	
	 
	 
	 public void doChargeAll() throws Exception{
			String result = "Y";
			//标识 flag ： 1-》部门充值 2-》员工充值
			String flag = this.getRequest().getParameter("flag");
			String ids = this.getRequest().getParameter("ids");
			String balance = this.getRequest().getParameter("balance");
			try {			
				PrintWriter out = null;
				this.getResponse().setContentType("text/json; charset=utf-8");
				boolean boo = xOrdersService.chargeAll(ids,flag,balance,((XUser)this.getSession().getAttribute("loginUser")).getUserName());
				if(!boo){
					result = "N";
				}
				out = this.getResponse().getWriter();
				out.write(result);
					if(out!=null){
						out.flush();
						out.close();
					}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new Exception("充值失败!");
			}
		}
		
	 /**
		 * add dingzhj at date 2017-03-04 保存指定明细
		 * @return
		 * @throws Exception
		 */
		public void saveXAppointRelation()throws Exception{
			String result = "N";
			try {
				// TODO 传值
				XUser loginUser = this.getOrderUserBySession();
				String appointId = this.getRequest().getParameter("appointId");
				String appointName = this.getRequest().getParameter("appointName");
				XAppointRelation xAppointRelation = new XAppointRelation();
				xAppointRelation.setLeadId(loginUser.getUserId());
				xAppointRelation.setLeadName(loginUser.getUserName());
				xAppointRelation.setAppointId(appointId);
				xAppointRelation.setAppointName(appointName);
				if(loginUser!=null){
					xAppointRelation.setInsUser(loginUser.getUserName());
				}
				XAppointRelation xar = xOrdersService.saveXAppointRelation(xAppointRelation);
				if(xar != null){
					result = "Y";
				}
				PrintWriter out = null;
				this.getResponse().setContentType("text/json; charset=utf-8");
				try {
					out = this.getResponse().getWriter();
					out.write(result);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(out!=null){
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
		 * add dingzhj at date 2017-03-04 删除指定关系
		 * @throws Exception
		 */
		public void delXAppointRelation() throws Exception{
			try {
			//TODO 根据ID删除
			String id = this.getRequest().getParameter("id");
			xOrdersService.delXAppointRelationById(id);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new Exception("指定关系删除失败!");
			}
		}
		
		
		
		/**
		 * @method: toMyOrder
		 * @Description: 我的订单
		 * @return
		 * @throws Exception
		 * @author : lig
		 * @date 2017-3-5 下午2:41:02
		 */
		public String toMyOrder() throws Exception{
			try {
				String userId = this.getOrderUserBySession().getUserId();
				List<MyOrder> myOrderList = xOrdersService.getMyOrder(userId);
				this.getRequest().setAttribute("myOrderList", myOrderList);
				this.setForwardPage("/orders/myOrder.jsp");
				return SUCCESS;
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new Exception("我的订单查询失败!");
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
				String userId = this.getOrderUserBySession().getUserId();
				List<MyOrder> myOrderList = xOrdersService.getReserveMyOrder(userId);
				this.getRequest().setAttribute("myOrderList", myOrderList);
				this.setForwardPage("/orders/myReserveOrder.jsp");
				return SUCCESS;
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new Exception("我的订单查询失败!");
			}
		}
		
		/**
		 * 现场订单
		 * 
		 * @return
		 * @throws Exception
		 */
		public String toMyLiveOrder() throws Exception{
			try {
				String userId = this.getOrderUserBySession().getUserId();
				List<MyOrder> myOrderList = xOrdersService.getMyLiveOrder(userId);
				this.getRequest().setAttribute("myOrderList", myOrderList);
				this.setForwardPage("/orders/myLiveOrder.jsp");
				return SUCCESS;
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new Exception("我的订单查询失败!");
			}
		}
		
		/**
		 * 查询所有美食商家
		 * @return
		 * @throws Exception
		 */
		/*public void findFoodBusiness() throws Exception{
			try{
				//查询所有美食商家
				List<XUser> foodBusinessList = xUserService.findFoodBusiness();
				
				JSONArray json = JSONArray.fromObject(foodBusinessList);
				PrintWriter out = null;
				this.getResponse().setContentType("text/json; charset=utf-8");
				try {
					out = this.getResponse().getWriter();
					out.write(json.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(out!=null){
						out.flush();
						out.close();
					}
				}
			}catch (Exception e) {
				logger.error(e.getMessage());
				throw new Exception("查询所有美食商家失败!");
			}
		}*/
		
		/**
		 * @method: toOrderingPage
		 * @Description: to 订餐页面
		 * @return
		 * @throws Exception
		 * @author : lig
		 * @date 2017-3-6 下午2:33:36
		 */
		@SuppressWarnings({ "rawtypes"})
		public String toOrderingPage() throws Exception{
			try {
				//订餐类型：正常订餐或预定  或  订购产品类型
				String ISYD = this.getRequest().getParameter("type");
				
				String infos = this.getRequest().getParameter("infos");
				if(StringUtil.isNotEmpty(infos)){
					this.getRequest().setAttribute("infos", infos);
				}
				
				//查询所有美食商家
				List<XUser> foodBusinessList = xUserService.findFoodBusiness();
				this.getRequest().setAttribute("foodBusinessList", foodBusinessList);
				
				String foodBusiness = this.getRequest().getParameter("foodBusiness");
				Map map = new HashMap();
				List<Map> noticeList = new ArrayList<Map>();
				if(StringUtils.isNotBlank(foodBusiness)){
					XUser xUser = xUserService.findFoodBusinessByUsername(foodBusiness);
					map = this.getCurrentFood(foodBusiness);
					this.getRequest().setAttribute("foodBusinessfirst", xUser);
					
					/**
					 * 今日公告
					 */
					noticeList = this.getCurrentNotice(foodBusiness);
					if(noticeList != null){
						this.getRequest().setAttribute("noticeList", noticeList);
						this.getRequest().setAttribute("notice", "yes");
					}else{
						this.getRequest().setAttribute("notice", "no");
					}
					
				}else{
					if(foodBusinessList != null && foodBusinessList.size() > 0){
						map = this.getCurrentFood(foodBusinessList.get(0).getUserName());
						this.getRequest().setAttribute("foodBusinessfirst", foodBusinessList.get(0));
						
						/**
						 * 今日公告
						 */
						noticeList = this.getCurrentNotice(foodBusinessList.get(0).getUserName());
						if(noticeList != null){
							this.getRequest().setAttribute("noticeList", noticeList);
							this.getRequest().setAttribute("notice", "yes");
						}else{
							this.getRequest().setAttribute("notice", "no");
						}
						
					}
				}
				this.getRequest().setAttribute("map", map);
				
				//判断订餐对象   isKR：客人  isLD：领导 isZD：指定人订餐
				boolean isKR = false;
				boolean isLD = false;
				boolean isZD = false;
				XUser orderUser = this.getOrderUserBySession();
				String userId = orderUser.getUserId();
				if(orderUser != null){
					if(orderUser.getSeclevel() != null){
						//安全级别大于40  领导,可以指定
						if(Integer.valueOf(orderUser.getSeclevel()) >=Constants.SECLEVEL){
							isZD = true;
							XAppointRelation xa = xOrdersService.getXAppointRelation(orderUser.getUserId());
							if(xa!=null){
								XUser appointUser = xUserService.getByUserId(xa.getAppointId());
								this.getRequest().setAttribute("appointUser", appointUser);
							}
							List<Map> deptList = xUserService
									.getDeptByCompanyId(orderUser.getCompanyId());
							this.getRequest().setAttribute("deptList", deptList);
						}
					}
					
				}
				//部门专用角色
				isKR = xOrdersService.isDeptSpecialRole(userId);
				//是否被指定，如果被指定则显示领导
				List<Map> list = xOrdersService.getLeadByUserId(userId);
				if(ListUtil.isNotEmpty(list)){
					isLD = true;
					this.getRequest().setAttribute("leadList", list);
				}
				
				this.getRequest().setAttribute("isKR", isKR);
				this.getRequest().setAttribute("isLD", isLD);
				this.getRequest().setAttribute("isZD", isZD);
				this.getSession().setAttribute("isZD", isZD);
				this.setForwardPage("/orders/ordering.jsp");
				
				if(StringUtil.isNotEmpty(ISYD) && ISYD.equals("YD")){
					this.setForwardPage("/orders/reserve.jsp");
				}else if(StringUtil.isNotEmpty(ISYD) && ISYD.equals("BP")){
					this.setForwardPage("/products/reserve.jsp");
				}
				return SUCCESS;
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new Exception("菜品查询失败!");
			}
		}
		
		/**
		 * 当前公告
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
		 * 
		 * @method: getStaffInfo
		 * @Description: 通过部门id 获取公司员工信息
		 * @author : zhaoziyun
		 * @date 2017-3-21 下午1:26:34
		 */
		public void getStaffInfo() {
			String result = "\"{\"success\":\"false\"}\"";
			XUser xuser = this.getOrderUserBySession();
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
		 * @method: getCurrentFood
		 * @Description: 获取当前时间段food
		 * @return
		 * @author : lig
		 * @date 2017-3-6 下午2:43:07
		 * 
		 * 9:00 - 14:00  	午
		 * 14:00 - 22:00	晚
		 * 其他时间			暂无
		 */
		@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
		private Map getCurrentFood(String foodBusiness){
			Map m = new HashMap();
//			Map numMap = Global.getNumMap();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now = new Date();
			try{
//				String weekKey = "";
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
					System.out.println("==========type"+type);
					System.out.println("=========="+currentTime);
					System.out.println("==========lunStart"+lunStart);
					System.out.println("==========lunEnd"+lunEnd);
					System.out.println("==========dinStart"+dinStart);
					System.out.println("==========dinEnd"+dinEnd);
				}else if(dinStart <= currentTime && dinEnd >= currentTime){
					type =  Constants.DIN;
					System.out.println("=========="+type);
					System.out.println("==========currentTime"+currentTime);
					System.out.println("==========lunStart"+lunStart);
					System.out.println("==========lunEnd"+lunEnd);
					System.out.println("==========dinStart"+dinStart);
					System.out.println("==========dinEnd"+dinEnd);
				}else{
					//其他时间无
					System.out.println("=========="+week);
					System.out.println("==========currentTime"+currentTime);
					System.out.println("==========lunStart"+lunStart);
					System.out.println("==========lunEnd"+lunEnd);
					System.out.println("==========dinStart"+dinStart);
					System.out.println("==========dinEnd"+dinEnd);
				}
				
				List<Map> foodFist = xOrdersService.getCurrentFood(week, type, foodBusiness);
				m.put("weekKey", week+"_"+type);
				m.put("foodInfos", foodFist);
//				if(!numMap.isEmpty() && numMap != null){
//					if(StringUtil.isNotEmpty(weekKey)){
//						m.put("weekKey",weekKey);
//						m.put("foodInfos", numMap.get(weekKey));
//					}
//				}
				return m;
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
		
		/**
		 * 
		 * @method: goSum
		 * @Description: TODO
		 * @author : zhaoziyun
		 * @throws Exception 
		 * @date 2017-3-7 下午2:07:38
		 */
		public String goSum() throws Exception{
			String orderInfo = this.getRequest().getParameter("orderInfo");
			String vistorName = this.getRequest().getParameter("vistorName");
			String foodBusiness = this.getRequest().getParameter("foodBusiness");
			JSONObject orderJson = null;
			if(StringUtil.isNotEmpty(orderInfo)){
				orderJson = JSONObject.fromObject(orderInfo);
				//获取订餐对象
				 String orderObj = orderJson.getString("orderObj");
				//订餐对象(ZJ:自己、KR：客人、LD：领导、ZD：指定)
				 //获取扣款对象姓名、账户余额
				 Map map = new HashMap();
				 if(orderObj.equals("ZJ")){
					 map = xOrdersService.getMoney("ZJ", this.getOrderUserBySession().getUserId());	//查询出自己余额//this.getOrderUserBySession().getUserId();
				 }else if(orderObj.equals("KR")){
					 map = xOrdersService.getMoney("KR", this.getOrderUserBySession().getDeptId());	//查询出部门余额 //this.getOrderUserBySession().getDeptId();
				 }else if(orderObj.equals("LD")){
					 String ldId =  orderJson.getString("ldId");
					 map = xOrdersService.getMoney("LD", ldId);	//查询出领导的余额//this.getOrderUserBySession().getUserId();
				 }
				 orderJson.put("obj", map);
			}
			
			//餐饮公司
			XUser foodCompany = xUserService.findFoodBusinessByUsername(foodBusiness);
			this.getRequest().setAttribute("foodCompany", foodCompany);
			
			//收货人信息
			//当前登录用户信息
			XUser orderUser = (XUser)this.getSession().getAttribute("orderLoginUser");
			//所有派送地址信息
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			List<Map> addressList = xFoodSendAddressService.getAddressList2(getQf(),foodBusiness);
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
			
			List<XFoodSendRegion> allAvailableRegion = xFoodSendRegionService.getAllAvailable();
			if(allAvailableRegion != null && allAvailableRegion.size() > 0){
				this.getRequest().setAttribute("allAvailableRegion", allAvailableRegion);
			}
			
			this.getRequest().setAttribute("orderInfo", orderJson.toString());
			this.getRequest().setAttribute("vistorName", vistorName);
			this.setForwardPage("/orders/orderinformation.jsp");
			return SUCCESS;
		}
		/**
		 * 
		 * @method: goReSum
		 * @Description: TODO
		 * @author : zhaoziyun
		 * @throws Exception 
		 * @date 2017-3-7 下午2:07:38
		 */
		public String goReSum() throws Exception{
			String orderInfo = this.getRequest().getParameter("orderInfo");
			String vistorName = this.getRequest().getParameter("vistorName");
			String foodBusiness = this.getRequest().getParameter("foodBusiness");
			JSONObject orderJson = null;
			if(StringUtil.isNotEmpty(orderInfo)){
				orderJson = JSONObject.fromObject(orderInfo);
				//获取订餐对象
				 String orderObj = orderJson.getString("orderObj");
				//订餐对象(ZJ:自己、KR：客人、LD：领导、ZD：指定)
				 //获取扣款对象姓名、账户余额
				 Map map = new HashMap();
				 if(orderObj.equals("ZJ")){
					 map = xOrdersService.getMoney("ZJ", this.getOrderUserBySession().getUserId());	//查询出自己余额//this.getOrderUserBySession().getUserId();
				 }else if(orderObj.equals("KR")){
					 map = xOrdersService.getMoney("KR", this.getOrderUserBySession().getDeptId());	//查询出部门余额 //this.getOrderUserBySession().getDeptId();
				 }else if(orderObj.equals("LD")){
					 String ldId =  orderJson.getString("ldId");
					 map = xOrdersService.getMoney("LD", ldId);	//查询出领导的余额//this.getOrderUserBySession().getUserId();
				 }
				 orderJson.put("obj", map);
			}
			
			//餐饮公司
			XUser foodCompany = xUserService.findFoodBusinessByUsername(foodBusiness);
			this.getRequest().setAttribute("foodCompany", foodCompany);
			
			//收货人信息
			//当前登录用户信息
			XUser orderUser = (XUser)this.getSession().getAttribute("orderLoginUser");
			//所有派送地址信息
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			List<Map> addressList = xFoodSendAddressService.getAddressList2(getQf(),foodBusiness);
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
			
			List<XFoodSendRegion> allAvailableRegion = xFoodSendRegionService.getAllAvailable();
			if(allAvailableRegion != null && allAvailableRegion.size() > 0){
				this.getRequest().setAttribute("allAvailableRegion", allAvailableRegion);
			}
			
			this.getRequest().setAttribute("orderInfo", orderJson.toString());
			this.getRequest().setAttribute("vistorName", vistorName);
			this.setForwardPage("/orders/re-orderInfo.jsp");
			return SUCCESS;
		}
		/**
		 * @method: getStaffTreeData
		 * @Description: 指定人tree数据
		 * @throws Exception
		 * @author : lig
		 * @date 2017-3-9 下午12:05:37
		 */
		public void getStaffTreeData() throws Exception{
			try {		
				String companyId = this.getOrderUserBySession().getCompanyId();
				String staffName = this.getRequest().getParameter("staffName");
				List<Map> list = xOrdersService.getTreeData(companyId,true,staffName,"");
				JSONArray json = JSONArray.fromObject(list);
				PrintWriter out = null;
				this.getResponse().setContentType("text/json; charset=utf-8");
				try {
					out = this.getResponse().getWriter();
					out.write(json.toString());
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
		 * 后台统计
		 * @throws Exception
		 */
		public void orderStatistics()throws Exception{
			try {
				//当前登录用户信息
				XUser xuser = getSysUserBySession();
				
				String flag = this.getRequest().getParameter("flag");
				String companyId = this.getRequest().getParameter("companyId");
				XUser user = getSysUserBySession();
				if(companyId == null || companyId == ""){
					companyId = user.getCompanyId();
				}
				String startTime = this.getRequest().getParameter("startTime");
				String endTime = this.getRequest().getParameter("endTime");
				String dep_id = this.getRequest().getParameter("dep_id");
				XUser xUser = getSysUserBySession();
				if(dep_id == null || dep_id ==""){
					
					dep_id = xUser.getCompanyId();
				}
				setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
				List<Map> orderList = xOrdersService.getorderStatistics(getQf(),startTime,endTime,dep_id,flag,xuser);
				StringBuffer buff = new StringBuffer("{\"Total\":")
				.append(getQf().getTotal()).append(",\"Rows\":");
				Gson gson = new Gson();
				if(!ListUtil.isNotEmpty(orderList)){
					buff.append("[]");
				}else{
					jsonString = gson.toJson(orderList);
					buff.append(jsonString);
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
				throw new Exception("后台统计!");
			}
		} 
		
		
		/**
		 * @method: doClearZero
		 * @Description: 年底清零
		 * @throws Exception
		 * @author : lig
		 * @date 2017-3-11 上午11:15:03
		 */
		 @SuppressWarnings("rawtypes")
		public void  doClearZero() throws Exception{
			 	String result = "N";
			 	boolean boo = false;
			 	setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			 	//标识 flag ：员工充值（flag 只有2）
				String flag = this.getRequest().getParameter("flag");
				String deptId = this.getRequest().getParameter("deptId");	//部门id
				String str = this.getRequest().getParameter("str"); 		//员工模糊查询
				String companyId = this.getRequest().getParameter("companyId");
				String otherCompanyId = "";
				try {		
					List<Map> orderList = xOrdersService.getRechargeObj(getQf(), flag, deptId,companyId ,str,otherCompanyId);
					String idsStr = this.getIds(orderList);
					idsStr = idsStr.substring(0,idsStr.lastIndexOf(","));//去掉最后一个逗号
					String userName = this.getSysUserBySession().getUserName();
					
					boo = xOrdersService.clearZero(idsStr,userName,new Date());
					
					if(boo){
						result = "Y";
					}
					PrintWriter out = null;
					this.getResponse().setContentType("text/json; charset=utf-8");
					try {
						out = this.getResponse().getWriter();
						out.write(result);
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						if(out!=null){
							out.flush();
							out.close();
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
					throw new Exception("角色查询详情失败!");
				}
			}
		 
		 /**
		  * 
		  * @throws Exception
		  */
		 public void orderStatisticsExcel()throws Exception{
			 	//当前登录用户信息
				XUser xuser = getSysUserBySession();
			 
				try {
					String flag = this.getRequest().getParameter("flag");
					String startTime = this.getRequest().getParameter("startTime");
					String endTime = this.getRequest().getParameter("endTime");
					String dep_id = this.getRequest().getParameter("dep_id");
					XUser xUser = getSysUserBySession();
					if(dep_id == null || dep_id ==""){
						
						dep_id = xUser.getCompanyId();
					}
					setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
					List<Map> tjList = xOrdersService.getorderStatisticsExcel(startTime,endTime,dep_id,flag,xuser);
					this.getSession().setAttribute("tjList", tjList);
					StringBuffer buff = new StringBuffer("{\"Total\":")
					.append(getQf().getTotal()).append(",\"Rows\":");
					Gson gson = new Gson();
					if(!ListUtil.isNotEmpty(tjList)){
						buff.append("[]");
					}else{
						jsonString = gson.toJson(tjList);
						buff.append(jsonString);
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
					throw new Exception("后台统计!");
				}
			}
		 
		 /**
		  * 根据订单ID
		  * @method: againOrder
		  * @Description: TODO
		  * @author : dingzhj
		  * @date 2017-3-17 下午2:41:28
		  */
		 public void againOrder(){
			 try {
				 String orderId = this.getRequest().getParameter("orderId");
				 //List<MyOrder> myOrderLis1t = xOrdersService.getMyOrder(orderId);
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
					} finally{
						if(out!=null){
							out.flush();
							out.close();
						}
					}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		 }
		 
		 
		 /**
		  * @method: toYDPage
		  * @Description: 预订页
		  * @return
		  * @throws Exception
		  * @author : lig
		  * @date 2017-3-22 下午3:09:57
		  */
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void toYDPage() {
				Map map = new HashMap();
				try {
					//周几
					String week = this.getRequest().getParameter("week");
					//美食商家
					String foodBusiness = this.getRequest().getParameter("foodBusiness");
					
					//查询所有美食商家
					List<XUser> foodBusinessList = xUserService.findFoodBusiness();
					this.getRequest().setAttribute("foodBusinessList", foodBusinessList);
					
					List<Map> foodList = new ArrayList<Map>();
					List<Map> noticeList = new ArrayList<Map>();
					if(StringUtils.isNotBlank(foodBusiness)){
						XUser xUser = xUserService.findFoodBusinessByUsername(foodBusiness);
						//套餐
						foodList = xOrdersService.getFoodByCondtion(week, null, foodBusiness);
						//公告
						noticeList = xNoticeService.getNoticeByCondtion(week, null, foodBusiness);
						this.getRequest().setAttribute("foodBusinessfirst", xUser);
					}else{
						if(foodBusinessList != null && foodBusinessList.size() > 0){
							//套餐
							foodList = xOrdersService.getFoodByCondtion(week, null, foodBusinessList.get(0).getUserName());
							//公告
							noticeList = xNoticeService.getNoticeByCondtion(week, null, foodBusinessList.get(0).getUserName());
							this.getRequest().setAttribute("foodBusinessfirst", foodBusinessList.get(0));
						}
					}
					
					List<Map> LUNList = new ArrayList<Map>();//午餐集合
					List<Map> DINList = new ArrayList<Map>();//晚餐集合
					List<Map> LUNNoticeList = new ArrayList<Map>();//午餐公告集合
					List<Map> DINNoticeList = new ArrayList<Map>();//晚餐公告集合
					
					if(ListUtil.isNotEmpty(foodList)){
						for (int i = 0; i < foodList.size(); i++) {
							if(Constants.LUN.equals(foodList.get(i).get("ft"))){
								LUNList.add(foodList.get(i));
							}else if(Constants.DIN.equals(foodList.get(i).get("ft"))){
								DINList.add(foodList.get(i));
							}
						}
					}
					//公告
					if(ListUtil.isNotEmpty(noticeList)){
						for (int i = 0; i < noticeList.size(); i++) {
							if(Constants.LUN.equals(noticeList.get(i).get("ft"))){
								LUNNoticeList.add(noticeList.get(i));
							}else if(Constants.DIN.equals(noticeList.get(i).get("ft"))){
								DINNoticeList.add(noticeList.get(i));
							}
						}
					}
					
					map.put("LUNList", LUNList);
					map.put("DINList", DINList);
					//公告
					map.put("LUNNoticeList", LUNNoticeList);
					map.put("DINNoticeList", DINNoticeList);
					
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
	 * 指定人查询被指定人指定指定人的订单
	 * @method: toAppointMyOrder
	 * @Description: TODO
	 * @return
	 * @throws Exception
	 * @author : dingzhj
	 * @date 2017-3-31 下午3:39:37
	 */
	public String toAppointMyOrder() throws Exception{
		try {
			String userId = this.getOrderUserBySession().getUserId();
			List<MyOrder> myOrderList = xOrdersService.getMyOrderAppoint(userId);
			this.getRequest().setAttribute("myOrderList", myOrderList);
			this.setForwardPage("/orders/myOrderAppoint.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("我的订单查询失败!");
		}
	} 
	
	/**
	 * @method: toRechargeRecordList
	 * @Description: to充值记录页面
	 * @return
	 * @throws Exception
	 * @author : zzy
	 * @date 2017-2-28 上午11:32:28
	 */
	public String toRechargeRecordList() throws Exception{
		try {		
			this.setForwardPage("/jsp/order/orders/recharge_record_list.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("充值记录跳转失败!");
		}
	}
	
	/**
	 * @method: getRechargeRecord
	 * @Description: 获取充值记录
	 * @throws Exception
	 * @author : zzy
	 * @date 2017-2-28 上午11:17:52
	 */
	@SuppressWarnings("rawtypes")
	public void getRechargeRecord() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			String flag = this.getRequest().getParameter("flag");
			String deptId = this.getRequest().getParameter("deptId");
			String str = this.getRequest().getParameter("str");
			String companyId = this.getRequest().getParameter("companyId");
			XUser user = getSysUserBySession();
			if(companyId == null || companyId == ""){
				companyId = user.getCompanyId();
			}
			
			List<Map> orderList = xOrdersService.getRechargeRecord(getQf(), flag, deptId,companyId, str);
			StringBuffer buff = new StringBuffer("{\"Total\":")
			.append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson = new Gson();
			jsonString = gson.toJson(orderList);
			buff.append(jsonString);
			if(!ListUtil.isNotEmpty(orderList)){
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
	 * 获取当前登录用户所在公司的id
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void getCurrentStaffCompanyId() throws Exception{
		Gson g = new Gson();
		String result = null;
		try {
			//当前登录用户信息
			XUser xuser = getSysUserBySession();
			result = xOrdersService.getCurrentStaffCompanyId(xuser);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("获取当前登录的用户所在的公司id失败！");
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
	  * 取消现场订单
	  * @return
	  * @throws Exception
	  */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteLiveOrder() throws Exception{
		 Map result = new HashMap<String, String>();
		 Gson g = new Gson();
		 try {
			 //当前订单ID
			 String orderId = this.getRequest().getParameter("orderId");
			 //取消订单的总套餐份数
			 String foodNum = this.getRequest().getParameter("foodNum");
			 String orderType = this.getRequest().getParameter("orderType");
			 String userId = this.getRequest().getParameter("userId");
			 
			 //获取取消订单的用户信息
			 XUser orderUser = xUserService.getByUserId(userId);
			 Map<String,Object> msgMap;
			 msgMap = xOrdersService.deleteLiveOrder(orderId,foodNum,orderUser,orderType);
			 
			 if("true".equals(msgMap.get("status"))){
				 result.put("success", "success");
				 result.put("msg", "取消订餐成功");
			 }else{
				 result.put("success", "false");
				 result.put("msg","取消订餐失败");
			 }
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("取消订餐失败");
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
	
	
	//后台系统产品订单...........................start..........................................	
	//获取到所有的订单
	@SuppressWarnings("rawtypes")
	public void getAll(){
		try {
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			String flag = this.getRequest().getParameter("flag");//判断呢是部门还是员工
			String deptId = this.getRequest().getParameter("deptId");
			String companyId = this.getRequest().getParameter("companyId");
			String str = this.getRequest().getParameter("str");
			List<Map> allOrders=xOrdersService.getAllOrders(getQf(), flag, deptId, companyId, str);
			StringBuffer buff = null;
			if(!ListUtil.isNotEmpty(allOrders)){
				buff = new StringBuffer("{\"Total\":").append(0).append(",\"Rows\":");
			}else{
				buff = new StringBuffer("{\"Total\":").append(getQf().getTotal()).append(",\"Rows\":");
			}
			Gson gson = new Gson();
			jsonString = gson.toJson(allOrders);
			buff.append(jsonString);
			buff.append("}");
			//PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			this.getResponse().getWriter().print(buff.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 跳转到产品发货页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getProFaHuoInfo() throws Exception {
		try {
			String id = this.getRequest().getParameter("id");
			if (id != null && !"".equals(id)) {
				XOrders order = xOrdersService.get(id);
				ActionContext.getContext().getValueStack().push(order);
				this.setForwardPage("/jsp/product/orderManage/productFaHuo_view.jsp");
			}
			return SUCCESS;

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询失败");
		}
	}
	
	/**
	 * 修改发货状态
	 * 
	 * @return
	 * @throws Exception
	 */
	public String upDateStatus() throws Exception{
		String result="Y";
		try {
			String id = this.getRequest().getParameter("id");
			String sendOutFlag = this.getRequest().getParameter("sendOutFlag");
			String expressNum = this.getRequest().getParameter("expressNum");
			
			if(sendOutFlag.endsWith("0")){ //发货状态
				XOrders order = xOrdersService.get(id);
				order.setExpressNum(expressNum); //快递单号
				order.setSendOutFlag(Integer.valueOf(sendOutFlag)); //发货状态
				order.setSendOutTime(new Date());
				xOrdersService.save(order);
			}else if(sendOutFlag.endsWith("1")){ //未发货
				XOrders order = xOrdersService.get(id);
				order.setExpressNum(expressNum); //快递单号
				order.setSendOutFlag(Integer.valueOf(sendOutFlag)); //发货状态
				order.setSendOutTime(null);
				xOrdersService.save(order);
			}
			
			this.getResponse().setContentType("text/json; charset=utf-8");
			this.getResponse().getWriter().print(result);
		} catch (Exception e) {
			result="N";
			logger.error(e.getMessage());
			throw new Exception("修改失败");
		}
		return NONE;
	}
	
	//后台系统产品订单...........................end..........................................	
	
}
