package com.kssj.order.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import com.google.gson.Gson;
import com.kssj.auth.model.XMenu;
import com.kssj.auth.model.XUser;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.command.sql.SqlSpellerDbType;
import com.kssj.frame.web.action.BaseAction;
import com.kssj.order.model.XFoodSendRegion;
import com.kssj.order.service.XFoodSendRegionService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
public class XFoodSendRegionAction extends BaseAction implements ModelDriven<XFoodSendRegion> {
	
	@Resource
	private XFoodSendRegionService xFoodSendRegionService;
	
	private XFoodSendRegion xFoodSendRegion = new XFoodSendRegion();
	
	@Override
	public XFoodSendRegion getModel() {
		return xFoodSendRegion;
	}
	
	/**
	 * 查询派送范围列表
	 * 
	 * @throws Exception
	 */
	public void getRegionList() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			String str = this.getRequest().getParameter("str");
			List<Map> redgionMap = xFoodSendRegionService.getRegionList(getQf(),str);
			
			StringBuffer buff = new StringBuffer("{\"Total\":").append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson=new Gson();
			jsonString=gson.toJson(redgionMap);
			buff.append(jsonString);
			if(redgionMap==null){
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
	 * 跳转到添加派送范围页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toAddRegion() throws Exception{
		try {			
			this.setForwardPage("/jsp/auth/xfooduser/sendregion_form.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("跳转到添加派送范围页面失败!");
		}
		
	}
	
	/**
	 * 跳转到编辑派送范围页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editRegion() throws Exception{
		try {			
			String regionId = this.getRequest().getParameter("regionId");
			XFoodSendRegion xFoodSendRegion = xFoodSendRegionService.get(regionId);
			
			ActionContext.getContext().getValueStack().push(xFoodSendRegion);
			this.setForwardPage("/jsp/auth/xfooduser/sendregion_edit.jsp");
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("跳转到编辑派送范围页面失败!");
		}
	}
	
	/**
	 * 添加或编辑派送范围操作
	 * 
	 * @throws Exception
	 */
	public void doAddOrUpdate() throws Exception{
		String result = "Y";
		String type = this.getRequest().getParameter("type");
		try {			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			XUser loginUser = (XUser)this.getRequest().getSession().getAttribute("loginUser");
			
			//更新操作
			if(type.equals("update")){
				XFoodSendRegion xFoodSendRegion2 = xFoodSendRegionService.get(xFoodSendRegion.getId());
				xFoodSendRegion2.setDelFlag("0"); //启用
				xFoodSendRegion2.setRegionName(xFoodSendRegion.getRegionName());
				xFoodSendRegion2.setRegionDesc(xFoodSendRegion.getRegionDesc());
				xFoodSendRegion2.setUpdUser(loginUser.getUserName());
				xFoodSendRegion2.setUpdTime(new Date());
				xFoodSendRegionService.save(xFoodSendRegion2);
			}else{ //插入操作
				xFoodSendRegion.setDelFlag("0"); //启用
				xFoodSendRegion.setInsUser(loginUser.getUserName());
				xFoodSendRegion.setInsTime(new Date());
				xFoodSendRegionService.save(xFoodSendRegion);
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
			result = "N";
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("操作失败!");
		}
	}
	
	/**
	 * 启用禁用餐饮派送范围
	 * 
	 * @throws Exception
	 */
	public void delRegion() throws Exception{
		String result = "Y";
		String regionId = this.getRequest().getParameter("regionId");
		try {			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			
			//删除派送范围
			XFoodSendRegion xFoodSendRegion = xFoodSendRegionService.get(regionId);
			if("0".equals(xFoodSendRegion.getDelFlag())){
				xFoodSendRegion.setDelFlag("1");
				xFoodSendRegionService.save(xFoodSendRegion);
			}else if("1".equals(xFoodSendRegion.getDelFlag())){
				xFoodSendRegion.setDelFlag("0");
				xFoodSendRegionService.save(xFoodSendRegion);
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
	 * 查询所有可用的派送范围列表
	 * 
	 * @throws Exception
	 */
	public void getRegionAll() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			List<XFoodSendRegion> regionList = xFoodSendRegionService.getAllAvailable();
			JSONArray json = JSONArray.fromObject(regionList);
			
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
			throw new Exception("查询派送范围列表失败!");
		}
	}

}
