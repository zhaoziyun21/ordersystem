package com.kssj.product.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.google.gson.Gson;
import com.kssj.auth.model.XUser;
import com.kssj.base.util.ListUtil;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.command.sql.SqlSpellerDbType;
import com.kssj.frame.web.action.BaseAction;
import com.kssj.order.model.XFoodSendRegion;
import com.kssj.product.model.MyProOrder;
import com.kssj.product.model.XProCategory;
import com.kssj.product.model.XProExamine;
import com.kssj.product.model.XProducts;
import com.kssj.product.service.XProCategoryService;
import com.kssj.product.service.XProExamineService;
import com.kssj.product.service.XProductsManageService;
import com.kssj.product.service.XProductsService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
public class XProductsManageAction extends BaseAction implements ModelDriven<XProducts> {

	private XProducts xProducts = new XProducts();

	@Resource
	private XProductsManageService xProductManageService;
	
	@Resource
	private XProCategoryService xProCategoryService;
	
	@Resource
	private XProExamineService xProExamineService;

	@Override
	public XProducts getModel() {
		return xProducts;
	}

	@SuppressWarnings("rawtypes")
	public void getAll() throws Exception {
		try {
			String startTime = this.getRequest().getParameter("startTime");
			String endTime = this.getRequest().getParameter("endTime");
			String flag = this.getRequest().getParameter("flag");
			setQf(new SqlQueryFilter(this.getRequest(), SqlSpellerDbType.MYSQL));

			List<Map> allProducts = xProductManageService.getAllProducts(getQf(), startTime, endTime, flag);
			StringBuffer buff = new StringBuffer("{\"Total\":").append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson = new Gson();
			jsonString = gson.toJson(allProducts);
			buff.append(jsonString);
			if (!ListUtil.isNotEmpty(allProducts)) {
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
			} finally {
				if (out != null) {
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
	 * 去添加产品类别页面
	 * @return
	 * @throws Exception
	 */
	public String toAddProCate() throws Exception{
		try {		
			List<XProCategory> proCategoryList = xProCategoryService.getAllAvailable();
			this.getRequest().setAttribute("proCategoryList", proCategoryList);
			
			this.setForwardPage("/jsp/product/productManage/productManage_form.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("跳转到添加产品类别页面失败!");
		}
		
	}
	
	// 添加商品
	public void addProduct() {
		String result = "Y";
		XUser user = (XUser) this.getRequest().getSession().getAttribute("loginUser");
		String type = this.getRequest().getParameter("type");
		try {
			if (type.equals("add")) {
				if (xProducts != null) {
					//添加的产品
					xProducts.setStatus("2"); //上架中
					xProducts.setInsTime(new Date());
					xProducts.setInsUser(user.getUserName());
					xProducts.setUpdTime(new Date());
					xProducts.setUpdUser(user.getUserName());
					xProducts.setProCode(Long.toString(new Date().getTime()));
					//审批实体
					XProExamine xProExamine = new XProExamine();
					xProExamine.setProId(xProducts.getId());
					xProExamine.setExStatus("0"); //审批中
					
					xProductManageService.saveProductAndProExamine(xProducts, xProExamine);
				}
			}
			if (type.equals("update")) {
				if (xProducts != null) {
					xProducts.setUpdTime(new Date());
					xProducts.setUpdUser(user.getUserName());
					xProductManageService.save(xProducts);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "N";
		}
		
		try {
			this.getResponse().setContentType("text/json; charset=utf-8");
			this.getResponse().getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 得到产品详情信息
	public String getDesc() throws Exception {
		try {
			//供产品类别下拉选使用
			List<XProCategory> proCategoryList = xProCategoryService.getAllAvailable();
			this.getRequest().setAttribute("proCategoryList", proCategoryList);
			
			String id = this.getRequest().getParameter("id");
			if (id != null && !"".equals(id)) {
				//产品
				xProducts = xProductManageService.get(id);
				ActionContext.getContext().getValueStack().push(xProducts);
				//产品类别
				XProCategory xProCategory = xProCategoryService.get(xProducts.getProCateId());
				this.getRequest().setAttribute("xProCategory", xProCategory);
				
				this.setForwardPage("/jsp/product/productManage/productManage_view.jsp");
			}
			return SUCCESS;

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询失败");
		}
	}

	// 根据ID删除数据
	public String delById() throws Exception {
		String result = "Y";
		String id = this.getRequest().getParameter("id");
		try {
			if (id != null && !"".equals(id)) {
				xProductManageService.remove(id);
			}
			this.getResponse().setContentType("text/json; charset=utf-8");
			PrintWriter writer = this.getResponse().getWriter();
			writer.print(result);
		} catch (Exception e) {
			result = "N";
			logger.error(e.getMessage());
			throw new Exception("删除失败");
		}

		return NONE;
	}

	// 修改产品状态
	public String uStatus() throws Exception {
		XUser user = (XUser) this.getRequest().getSession().getAttribute("loginUser");
		String result = "Y";
		try {
			String id = this.getRequest().getParameter("id");
			String status = this.getRequest().getParameter("status");
			XProducts xProducts2 = xProductManageService.get(id);
			xProducts2.setStatus(status);
			xProducts2.setUpdTime(new Date());
			xProducts2.setUpdUser(user.getUserName());
			xProductManageService.save(xProducts2);
			
			//如果“下架”后要“上架中”，修改审批这条信息的状态为“0”，即“审批中”的状态
			if(status.equals("2")){
				XProExamine xProExamine = xProExamineService.getExamineDetail(id);
				XProExamine xProExamineNew = new XProExamine();
				xProExamineNew.setId(xProExamine.getId());
				xProExamineNew.setProId(xProExamine.getProId());
				xProExamineNew.setExStatus("0");
				
				xProExamineService.save(xProExamineNew);
			}
			
			// xProductManageService.updateStatus(id,status);
			this.getResponse().setContentType("text/json; charset=utf-8");
			this.getResponse().getWriter().print(result);
		} catch (Exception e) {
			result = "N";
			logger.error(e.getMessage());
			throw new Exception("修改失败");
		}
		return NONE;
	}

	/**
	 * 产品订单详情
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String getOrderDetail() throws Exception {
		try {
			String id = this.getRequest().getParameter("id");
			if (id != null && !"".equals(id)) {
				List<Map> detailList = xProductManageService.queryMyProOrder(id);
				this.getRequest().setAttribute("detailList", detailList);
				this.setForwardPage("/jsp/product/orderManage/orderManage_detail.jsp");
			}
			return SUCCESS;

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询失败");
		}
	}

//.....................................产品审批......................................
	/**
	 * 审批产品功能---产品详情之查询
	 * @return
	 * @throws Exception
	 */
	public String getExamineProDesc() throws Exception {
		try {
			String id = this.getRequest().getParameter("id");
			if (id != null && !"".equals(id)) {
				xProducts = xProductManageService.get(id);
				ActionContext.getContext().getValueStack().push(xProducts);
				this.setForwardPage("/jsp/product/productManage/productExamine_view.jsp");
			}
			return SUCCESS;

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询失败");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void getExamineAll() throws Exception {
		try {
			String startTime = this.getRequest().getParameter("startTime");
			String endTime = this.getRequest().getParameter("endTime");
			String flag = this.getRequest().getParameter("flag");
			setQf(new SqlQueryFilter(this.getRequest(), SqlSpellerDbType.MYSQL));

			List<Map> allProducts = xProductManageService.getExamineAllProducts(getQf(), startTime, endTime, flag);
			StringBuffer buff = new StringBuffer("{\"Total\":").append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson = new Gson();
			jsonString = gson.toJson(allProducts);
			buff.append(jsonString);
			if (!ListUtil.isNotEmpty(allProducts)) {
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
			} finally {
				if (out != null) {
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
	
	//得到审批信息
	public String getExamineDesc() throws Exception {
		try {
			String id = this.getRequest().getParameter("id");
			if (id != null && !"".equals(id)) {
				XProExamine xProExamine = xProExamineService.getExamineDetail(id);
				ActionContext.getContext().getValueStack().push(xProExamine);
				this.setForwardPage("/jsp/product/productManage/examine_view.jsp");
			}
			return SUCCESS;

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询失败");
		}
	}
	
	/**
	 * 得到审批结果信息
	 * @return
	 * @throws Exception
	 */
	public String getExamineDetail() throws Exception {
		try {
			String id = this.getRequest().getParameter("id");
			if (id != null && !"".equals(id)) {
				XProExamine xProExamine = xProExamineService.getExamineDetail(id);
				ActionContext.getContext().getValueStack().push(xProExamine);
				this.setForwardPage("/jsp/product/productManage/pro_examine_detail.jsp");
			}
			return SUCCESS;

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询失败");
		}
	}
	
	/**
	 * 审批结果插入或者更新
	 * @return
	 * @throws Exception
	 */
	public void getExamineResultInsertOrUpdate() throws Exception {
		String result = "Y";
		XProExamine xProExamine = new XProExamine();
		XUser user = (XUser) this.getRequest().getSession().getAttribute("loginUser");
		try {
			String id = this.getRequest().getParameter("id");
			String exStatus = this.getRequest().getParameter("exStatus");
			String exDesc = this.getRequest().getParameter("exDesc");
			String proId = this.getRequest().getParameter("proId");
			if (id != null && !"".equals(id)) {
				xProExamine.setId(id);
				xProExamine.setProId(proId);
				xProExamine.setExStatus(exStatus);
				xProExamine.setExDesc(exDesc);
				xProExamine.setExInsUser(user.getUserName());
				xProExamine.setExInsTime(new Date());
				xProExamine.setExUpdTime(new Date());
				
				xProExamineService.save(xProExamine);
				
				if(exStatus.equals("1")){ //审批通过
					XProducts xProducts2 = xProductManageService.get(proId);
					xProducts2.setStatus("0"); //产品状态改变为“已上架”
					xProductManageService.save(xProducts2);
				}
				
			}

		} catch (Exception e) {
			result = "N";
			logger.error(e.getMessage());
			throw new Exception("查询失败");
		}
		
		try {
			this.getResponse().setContentType("text/json; charset=utf-8");
			this.getResponse().getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
