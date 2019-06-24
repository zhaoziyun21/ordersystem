package com.kssj.order.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.kssj.auth.model.XUser;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.command.sql.SqlSpellerDbType;
import com.kssj.frame.web.action.BaseAction;
import com.kssj.order.model.XFoodSendAddress;
import com.kssj.order.model.XFoodSendRegion;
import com.kssj.order.service.XFoodSendAddressService;
import com.kssj.order.service.XFoodSendRegionService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
public class XFoodSendAddressAction extends BaseAction implements ModelDriven<XFoodSendAddress> {
	
	@Resource
	private XFoodSendAddressService xFoodSendAddressService;
	
	@Resource
	private XFoodSendRegionService xFoodSendRegionService;
	
	private XFoodSendAddress xFoodSendAddress = new XFoodSendAddress();
	
	@Override
	public XFoodSendAddress getModel() {
		return xFoodSendAddress;
	}
	
	/**
	 * 查询派送地址列表
	 * 
	 * @throws Exception
	 */
	public void getAddressList() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			String str = this.getRequest().getParameter("str");
			List<Map> addressMap = xFoodSendAddressService.getAddressList(getQf(),str);
			
			StringBuffer buff = new StringBuffer("{\"Total\":").append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson=new Gson();
			jsonString=gson.toJson(addressMap);
			buff.append(jsonString);
			if(addressMap==null){
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
	 * 跳转到添加派送地址页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toAddAddress() throws Exception{
		try {		
			List<XFoodSendRegion> regionList = xFoodSendRegionService.getAllAvailable();
			this.getRequest().setAttribute("regionList", regionList);
			this.setForwardPage("/jsp/auth/xfooduser/sendaddress_form.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("跳转到添加派送地址页面失败!");
		}
		
	}
	
	/**
	 * 跳转到编辑派送地址页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editAddress() throws Exception{
		try {			
			String addressId = this.getRequest().getParameter("addressId");
			XFoodSendAddress xFoodSendAddress = xFoodSendAddressService.get(addressId);
			
			List<XFoodSendRegion> regionList = xFoodSendRegionService.getAllAvailable();
			this.getRequest().setAttribute("regionList", regionList);
			
			ActionContext.getContext().getValueStack().push(xFoodSendAddress);
			this.setForwardPage("/jsp/auth/xfooduser/sendaddress_edit.jsp");
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("跳转到编辑派送地址页面失败!");
		}
	}
	
	/**
	 * 添加或编辑派送地址操作
	 * 
	 * @throws Exception
	 */
	public void doAddOrUpdate() throws Exception{
		String result = "Y";
		String type = this.getRequest().getParameter("type");
		String regionId = this.getRequest().getParameter("regionId");
		try {			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			XUser loginUser = (XUser)this.getRequest().getSession().getAttribute("loginUser");
			
			//更新操作
			if(type.equals("update")){
				XFoodSendAddress xFoodSendAddress2 = xFoodSendAddressService.get(xFoodSendAddress.getId());
				xFoodSendAddress2.setPark(xFoodSendAddress.getPark());
				xFoodSendAddress2.setHighBuilding(xFoodSendAddress.getHighBuilding());
				xFoodSendAddress2.setUnit(xFoodSendAddress.getUnit());
				//xFoodSendAddress2.setFloor(xFoodSendAddress.getFloor());
				xFoodSendAddress2.setRoomNum(xFoodSendAddress.getRoomNum());
				xFoodSendAddress2.setRegionId(xFoodSendAddress.getRegionId());
				
				xFoodSendAddress2.setRegionId(regionId);
				xFoodSendAddress2.setUpdUser(loginUser.getUserName());
				xFoodSendAddress2.setUpdTime(new Date());
				xFoodSendAddressService.save(xFoodSendAddress2);
			}else{ //插入操作
				xFoodSendAddress.setDelFlag("0"); //启用
				xFoodSendAddress.setRegionId(regionId);
				xFoodSendAddress.setInsUser(loginUser.getUserName());
				xFoodSendAddress.setInsTime(new Date());
				xFoodSendAddressService.save(xFoodSendAddress);
			}
			
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
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("操作失败!");
		}
	}
	
	/**
	 * 启用禁用餐饮派送地址
	 * 
	 * @throws Exception
	 */
	public void delAddress() throws Exception{
		String result = "Y";
		String addressId = this.getRequest().getParameter("addressId");
		try {			
			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			
			//删除派送地址
			XFoodSendAddress xFoodSendAddress = xFoodSendAddressService.get(addressId);
			if("0".equals(xFoodSendAddress.getDelFlag())){
				xFoodSendAddress.setDelFlag("1");
				xFoodSendAddressService.save(xFoodSendAddress);
			}else if("1".equals(xFoodSendAddress.getDelFlag())){
				xFoodSendAddress.setDelFlag("0");
				xFoodSendAddressService.save(xFoodSendAddress);
			}
			
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
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 微信端收餐地址查询--正常订餐
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String getAddressListWx() throws Exception{
		try {
			String type = this.getRequest().getParameter("type");
			String leadId = this.getRequest().getParameter("leadId");
			String orderType = this.getRequest().getParameter("orderType");
			String infos = this.getRequest().getParameter("infos");
			String foodBusiness = this.getRequest().getParameter("foodBusiness");
			this.getRequest().setAttribute("type", type);
			this.getRequest().setAttribute("leadId", leadId);
			this.getRequest().setAttribute("infos", infos);
			this.getRequest().setAttribute("orderType", orderType);
			this.getRequest().setAttribute("foodBusiness", foodBusiness);
			
			String checkedBusiness = this.getRequest().getParameter("checkedBusiness");
			this.getRequest().setAttribute("checkedBusiness", checkedBusiness);
			
			String orderInfoOld = this.getRequest().getParameter("orderInfo");
			String vistorNameOld = this.getRequest().getParameter("vistorName");
			//解决乱码问题
			String orderInfo = new String(orderInfoOld.getBytes("iso8859-1"),"utf-8");
			String vistorName = new String(vistorNameOld.getBytes("iso8859-1"),"utf-8");
			
			this.getRequest().setAttribute("orderInfo", orderInfo);
			this.getRequest().setAttribute("vistorName", vistorName);
			
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
			
			List<XFoodSendRegion> allAvailableRegion = xFoodSendRegionService.getAllAvailable();
			if(allAvailableRegion != null && allAvailableRegion.size() > 0){
				this.getRequest().setAttribute("allAvailableRegion", allAvailableRegion);
			}
			
			this.setForwardPage("/wx_orders/Management_address.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询收餐地址失败!");
		}
	} 
	
	/**
	 * 微信端收餐地址查询--预定订餐
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String getAddressListWx_YD() throws Exception{
		try {
			String type = this.getRequest().getParameter("type");
			String leadId = this.getRequest().getParameter("leadId");
			String orderType = this.getRequest().getParameter("orderType");
			String infos = this.getRequest().getParameter("infos");
			String foodBusiness = this.getRequest().getParameter("foodBusiness");
			this.getRequest().setAttribute("type", type);
			this.getRequest().setAttribute("leadId", leadId);
			this.getRequest().setAttribute("infos", infos);
			this.getRequest().setAttribute("orderType", orderType);
			this.getRequest().setAttribute("foodBusiness", foodBusiness);
			
			String checkedBusiness = this.getRequest().getParameter("checkedBusiness");
			this.getRequest().setAttribute("checkedBusiness", checkedBusiness);
			
			String orderInfoOld = this.getRequest().getParameter("orderInfo");
			String vistorNameOld = this.getRequest().getParameter("vistorName");
			//解决乱码问题
			String orderInfo = new String(orderInfoOld.getBytes("iso8859-1"),"utf-8");
			String vistorName = new String(vistorNameOld.getBytes("iso8859-1"),"utf-8");
			
			this.getRequest().setAttribute("orderInfo", orderInfo);
			this.getRequest().setAttribute("vistorName", vistorName);
			
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
			
			List<XFoodSendRegion> allAvailableRegion = xFoodSendRegionService.getAllAvailable();
			if(allAvailableRegion != null && allAvailableRegion.size() > 0){
				this.getRequest().setAttribute("allAvailableRegion", allAvailableRegion);
			}
			
			this.setForwardPage("/wx_orders/Management_address_yd.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询收餐地址失败!");
		}
	} 

}
