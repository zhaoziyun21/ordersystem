package com.kssj.product.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import com.google.gson.Gson;
import com.kssj.auth.model.XUser;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.command.sql.SqlSpellerDbType;
import com.kssj.frame.web.action.BaseAction;
import com.kssj.order.model.XFoodSendRegion;
import com.kssj.product.model.XProCategory;
import com.kssj.product.service.XProCategoryService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
public class XProCategoryAction extends BaseAction implements ModelDriven<XProCategory> {
	
	@Resource
	private XProCategoryService xProCategoryService;
	
	private XProCategory xProCategory = new XProCategory();
	
	@Override
	public XProCategory getModel() {
		return xProCategory;
	}
	
	/**
	 * 查询产品类别列表
	 * 
	 * @throws Exception
	 */
	public void getProCateList() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			String str = this.getRequest().getParameter("str");
			List<Map> proCategoryMap = xProCategoryService.getProCategoryList(getQf(),str);
			
			StringBuffer buff = new StringBuffer("{\"Total\":").append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson=new Gson();
			jsonString=gson.toJson(proCategoryMap);
			buff.append(jsonString);
			if(proCategoryMap==null){
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
	 * 跳转到添加产品类别页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toAddProCate() throws Exception{
		try {			
			this.setForwardPage("/jsp/product/productManage/productCategory_form.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("跳转到添加产品类别页面失败!");
		}
		
	}
	
	/**
	 * 跳转到编辑产品类别页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editProCate() throws Exception{
		try {			
			String xProCategoryId = this.getRequest().getParameter("xProCategoryId");
			XProCategory xProCategory = xProCategoryService.get(xProCategoryId);
			
			ActionContext.getContext().getValueStack().push(xProCategory);
			this.setForwardPage("/jsp/product/productManage/productCategory_edit.jsp");
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("跳转到编辑产品类别页面失败!");
		}
	}
	
	/**
	 * 添加或编辑产品类别操作
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
				XProCategory xProCategory2 = xProCategoryService.get(xProCategory.getId());
				xProCategory2.setDelFlag("0"); //启用
				xProCategory2.setProCateName(xProCategory.getProCateName());
				xProCategory2.setProCateDesc(xProCategory.getProCateDesc());
				xProCategory2.setUpdUser(loginUser.getUserName());
				xProCategory2.setUpdTime(new Date());
				xProCategoryService.save(xProCategory2);
			}else{ //插入操作
				xProCategory.setDelFlag("0"); //启用
				xProCategory.setInsUser(loginUser.getUserName());
				xProCategory.setInsTime(new Date());
				xProCategoryService.save(xProCategory);
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
	 * 启用禁用产品类别
	 * 
	 * @throws Exception
	 */
	public void delProCate() throws Exception{
		String result = "Y";
		String xProCategoryId = this.getRequest().getParameter("xProCategoryId");
		try {			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			
			//删除派送范围
			XProCategory xProCategory = xProCategoryService.get(xProCategoryId);
			if("0".equals(xProCategory.getDelFlag())){
				xProCategory.setDelFlag("1");
				xProCategoryService.save(xProCategory);
			}else if("1".equals(xProCategory.getDelFlag())){
				xProCategory.setDelFlag("0");
				xProCategoryService.save(xProCategory);
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
	 * 查询所有可用的产品类别列表
	 * 
	 * @throws Exception
	 */
	public void getProCateAll() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			List<XProCategory> xProCategoryList = xProCategoryService.getAllAvailable();
			JSONArray json = JSONArray.fromObject(xProCategoryList);
			
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
			throw new Exception("查询产品类别列表失败!");
		}
	}

}
