package com.kssj.order.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.google.gson.Gson;
import com.kssj.auth.model.XStaffSum;
import com.kssj.auth.model.XUser;
import com.kssj.base.util.ListUtil;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.command.sql.SqlSpellerDbType;
import com.kssj.frame.web.action.BaseAction;
import com.kssj.order.model.XDetailRecord;
import com.kssj.order.service.XOnlineOrdersService;
import com.kssj.order.service.XOrdersService;

@SuppressWarnings("serial")
public class OnlinePayAction extends BaseAction {
	
	@Resource
	private XOnlineOrdersService onlineOrdersService;
	
	
	/**
	 * 跳转
	 * @return
	 * @throws Exception
	 */
	public String togetonli() throws Exception{
		try {		
			//查询公司列表
			List<XUser> listUser = onlineOrdersService.getUserByCanYinAll();
			this.getRequest().setAttribute("listUser", listUser);
			this.setForwardPage("/wx_orders/online_pay.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("角色查询详情失败!");
		}
	}
	
	
	
	
	/**
	 * 提交订单
	 * @method: doCharge
	 * @Description: TODO
	 * @throws Exception
	 * @author : dingzhj
	 * @return 
	 * @date 2017-12-14 上午11:58:57
	 */
	public String doCharge() throws Exception{
		synchronized(this){
			String result = "Y";
			String jine = this.getRequest().getParameter("jine");//金额
			String dianpu = this.getRequest().getParameter("dianpu"); //餐饮公司ID
			XUser user = (XUser) this.getSession().getAttribute("wxOrderLoginUser");
			System.out.println(user.toString());
			try {			
				PrintWriter out = null;
				this.getResponse().setContentType("text/json; charset=utf-8");
				XStaffSum staffSum = onlineOrdersService.getXStaffumByUserId(user.getUserId());
				String s = staffSum.getBalance();
				int payments = Integer.parseInt(jine);
				int balance = Integer.parseInt(s);
				if(staffSum != null && balance-payments>=0 ){//可用余额足够支付
					//付款
					boolean bon = onlineOrdersService.onlineOrders(jine,dianpu,user,staffSum);
					if(!bon){
						result = "N";
					}
				}else{
					result = "S";
				}
				out = this.getResponse().getWriter();
				out.write(result);
				if(out!=null){
					out.flush();
					out.close();
				}
				this.setForwardPage("/wx_orders/success.jsp");
			} catch (Exception e) {
				result = "N";
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new Exception("付款未成功");
			}
		}
		return SUCCESS;
	}
	
/**********************************后台统计***********************************************/
	
	/**
	 * 跳转后台统计
	 * @method: toxordersTopay
	 * @Description: TODO
	 * @return
	 * @throws Exception
	 * @author : dingzhj
	 * @date 2017-12-23 下午2:56:09
	 */
	public String toxordersTopay() throws Exception{
		try {		
			//查询公司列表
			List<XUser> listUser = onlineOrdersService.getUserByCanYinAll();
			this.getRequest().setAttribute("listUser", listUser);
			this.setForwardPage("/jsp/order/orders/xorders_topay.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("角色查询详情失败!");
		}
	}
	
	
	
	/**
	 * 后台统计	
	 * @method: orderStatistics
	 * @Description: TODO
	 * @throws Exception
	 * @author : dingzhj
	 * @date 2017-12-23 下午2:33:27
	 */
	public void orderStatistics()throws Exception{
		try {
			String startTime = this.getRequest().getParameter("startTime");
			String endTime = this.getRequest().getParameter("endTime");
			String dep_id = this.getRequest().getParameter("dep_id");
			String cyId = this.getRequest().getParameter("cyId");
			XUser xUser = getSysUserBySession();
			if(dep_id == null || dep_id ==""){
				dep_id = xUser.getCompanyId();
			}
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			List<Map> orderList = onlineOrdersService.getorderStatistics(getQf(),startTime,endTime,dep_id,cyId);
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
	 * @method: getLedgerList
	 * @Description: 到付台账list
	 * @throws Exception
	 * @author : lig
	 * @date 2017-2-27 下午2:38:31
	 */
	public void getLedgerList() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			// modify by dingzhj at date 2017-03-06 
			String startTime =this.getRequest().getParameter("startTime");
			String endTime =this.getRequest().getParameter("endTime");
			String dep_id = this.getRequest().getParameter("dep_id");
			XUser user = getSysUserBySession();
			List<Map> orderList = onlineOrdersService.getLedgerList(getQf(),startTime,endTime,dep_id,user);
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
	
}
