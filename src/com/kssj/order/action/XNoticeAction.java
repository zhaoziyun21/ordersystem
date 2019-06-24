package com.kssj.order.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.kssj.auth.model.XUser;
import com.kssj.base.util.BeanUtil;
import com.kssj.base.util.Constants;
import com.kssj.base.util.ListUtil;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.command.sql.SqlSpellerDbType;
import com.kssj.frame.web.action.BaseAction;
import com.kssj.order.model.XNotice;
import com.kssj.order.service.XNoticeService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
public class XNoticeAction extends BaseAction implements ModelDriven<XNotice> {
	
	@Resource
	private XNoticeService xNoticeService;
	
	private XNotice xNotice = new XNotice();
	
	@Override
	public XNotice getModel() {
		return xNotice;
	}

//==============================菜谱公告===========================================================	
	
	/**
	 * 获得公告列表
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void getAllNotices() throws Exception{
		try {			
			XUser xuser = this.getSysUserBySession();
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			
			List<Map> noticeList = xNoticeService.getAllNotices(getQf(), xuser);
			
			StringBuffer buff = new StringBuffer("{\"Total\":")
			.append(getQf().getTotal()).append(",\"Rows\":");
			
			Gson gson = new Gson();
			jsonString = gson.toJson(noticeList);
			buff.append(jsonString);
			if(!ListUtil.isNotEmpty(noticeList)){
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
			throw new Exception("查询公告列表失败!");
		}
	}
	
	/**
	 * 验证公告名唯一
	 */
	 public void checkNoticeName(){
		 String result ="Y";
		 String noticeName =this.getRequest().getParameter("noticeName");
		 List<XNotice> xNoticelist = xNoticeService.getNotice(noticeName);
		 if(ListUtil.isNotEmpty(xNoticelist)){
			 result = "N";
		 }else{
			 result = "Y";
		 }
		 ajaxJson(result);
	 }
	 
	/**
	 * 去编辑公告页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toEditNotice() throws Exception{
		try {			
			String noticeId = this.getRequest().getParameter("noticeId");
			xNotice = xNoticeService.get(noticeId);
			ActionContext.getContext().getValueStack().push(xNotice);
			this.setForwardPage("/jsp/order/xfood/notice_view.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询公告失败!");
		}
	}
	 
	/**
	 * 新增和编辑公告
	 * 
	 * @throws Exception
	 */
	public void saveNotice() throws Exception{
		String result = "Y";
		String type = this.getRequest().getParameter("type");
		try {			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			//当前登录用户
			XUser xuser = (XUser)this.getSession().getAttribute("loginUser");
			XNotice xNot = null;
			if(type.equals("add")){
				xNotice.setInsUser(xuser.getUserName());
				xNotice.setInsTime(new Date());
				xNot = xNoticeService.save(xNotice);
			}else if(type.equals("update")){
				xNotice.setUpdUser(xuser.getUserName());
				xNotice.setUpdTime(new Date());
				XNotice xN = xNoticeService.get(xNotice.getId());
				BeanUtil.copyNotNullProperties(xN, xNotice);
				xNot = xNoticeService.save(xN);
			}
			if(xNot == null){
				result = "N";
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
	 * 删除公告
	 * 
	 * @throws Exception
	 */
	public void delNotice() throws Exception{
		String result = "Y";
		String noticeId = this.getRequest().getParameter("noticeId");
		XUser xu  = this.getSysUserBySession();
		
		try {			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try{
				XNotice xN= xNoticeService.get(noticeId);
				xN.setDelFlag(1);
				xN.setUpdTime(new Date());
				xN.setUpdUser(xu.getUserName());
				xNoticeService.save(xN);
			}catch(Exception e){
				result = "N";
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
	 * 跳转到编辑一周公告
	 * 
	 * @return
	 */
	public String goNoticeBillForm(){
		XUser xuser = this.getSysUserBySession();
		
		List<Map> list = xNoticeService.getBillMap(xuser);
		JSONArray json = JSONArray.fromObject(list);
		
		this.getRequest().setAttribute("noticeBillInfo", json.toString());
		this.setForwardPage("/jsp/order/xfood/noticebill_form.jsp");
		return SUCCESS;
	}
	
	/**
	 * 编辑公告时获取所有公告
	 * 
	 * @throws Exception
	 */
	public void getNoticeList() throws Exception{
		JSONObject j =new JSONObject();
		XUser xuser = this.getSysUserBySession();
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			List<Map> noticeList = xNoticeService.getAllNotice(getQf(), xuser);
			
			String type = this.getRequest().getParameter("type");
			String billId = Constants.billMap.get(type).toString();
			List<Map> weekNoticeList = xNoticeService.getNoticebillByWeekId(billId,xuser);
			if(ListUtil.isNotEmpty(noticeList)){
				if(ListUtil.isNotEmpty(weekNoticeList)){
					noticeList.removeAll(weekNoticeList);
				}
				Map map = new HashMap<String, String>();
				map.put("noticeList", noticeList);
				map.put("weekNoticeList", weekNoticeList);
				j = JSONObject.fromObject(map);
			}
			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try {
				out = this.getResponse().getWriter();
				out.write(j.toString());
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
	 * @method: saveFoodbill
	 * @Description: 保存一周菜谱
	 * @author : zhaoziyun
	 * @date 2017-3-8 下午2:49:46
	 */
	public void saveNoticebill(){
		String noticeBillInfo=this.getRequest().getParameter("noticeBillInfo");
		String userName  = this.getSysUserBySession().getUserName();
		Map<String, String> billMap  = (Map)JSONObject.fromObject(noticeBillInfo);
		
		xNoticeService.saveBillMap(billMap,userName);
	}

}
