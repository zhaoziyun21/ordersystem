package com.kssj.product.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;

import com.google.gson.Gson;
import com.kssj.auth.model.XUser;
import com.kssj.frame.web.action.BaseAction;
import com.kssj.product.model.XReceiverInfo;
import com.kssj.product.service.XReceiverInfoService;

@SuppressWarnings("serial")
public class XReceiverInfoAction  extends BaseAction {

	@Resource
	private XReceiverInfoService xReceiverInfoService;
	
	public void saveOrUpdateReceiverInfo(){
		//接收提交过来的数据
		String id = this.getRequest().getParameter("id");
		String recArea = this.getRequest().getParameter("recArea");
		String recName = this.getRequest().getParameter("recName");
		String recDetailAddress = this.getRequest().getParameter("recDetailAddress");
		String recPhone = this.getRequest().getParameter("recPhone");
		String recAddress = recArea+" "+recDetailAddress;
		
		XReceiverInfo xReceiverInfo = new XReceiverInfo();
		xReceiverInfo.setId(id);
		xReceiverInfo.setRecArea(recArea);
		xReceiverInfo.setRecName(recName);
		xReceiverInfo.setRecDetailAddress(recDetailAddress);
		xReceiverInfo.setRecAddress(recAddress);
		xReceiverInfo.setRecPhone(recPhone);
		//当前登录用户信息
		XUser orderUser = (XUser)this.getSession().getAttribute("orderLoginUser");
		xReceiverInfo.setUserId(orderUser.getUserId());
		
		Long count = xReceiverInfoService.findReceiverNum(orderUser.getUserId());
		if(id == null || id.length() == 0){ //添加收货人
			if(count == 0){
				xReceiverInfo.setRecDefaultStatus(0); //默认地址
				xReceiverInfo.setRecDelFlag(1); //非删除
			}else{
				xReceiverInfo.setRecDefaultStatus(1); //非默认地址
				xReceiverInfo.setRecDelFlag(1); //非删除
			}
		}else{ //编辑收货人
			if(count == 0){
				xReceiverInfo.setRecDefaultStatus(0); //默认地址
				xReceiverInfo.setRecDelFlag(1); //非删除
			}else{
				XReceiverInfo xReceiverInfo2 = xReceiverInfoService.findReceiverById(id);
				xReceiverInfo.setRecDefaultStatus(xReceiverInfo2.getRecDefaultStatus());
				xReceiverInfo.setRecDelFlag(1); //非删除
			}
		}
		
		//执行添加收货人信息操作
		XReceiverInfo xReceiverInfo2 = xReceiverInfoService.saveOrUpdateReceiverInfo(xReceiverInfo);
		String result = "true";
		if(xReceiverInfo2 == null){
			result = "false";
		}
		
		try {
			PrintWriter out = null;  
			this.getResponse().setContentType("text/json; charset=utf-8");
			out = this.getResponse().getWriter();
			out.write(result);
			if(out != null){
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
	}
	
	/**
	 * 查询添加收货人上限
	 */
	public void findReceiverNum(){
		//当前登录用户信息
		XUser orderUser = (XUser)this.getSession().getAttribute("orderLoginUser");
		//执行查询所有收货人
		Long count = xReceiverInfoService.findReceiverNum(orderUser.getUserId());
		String result = "true";
		if(count >= 5){
			result = "false";
		}
		
		try {
			PrintWriter out = null;  
			this.getResponse().setContentType("text/json; charset=utf-8");
			out = this.getResponse().getWriter();
			out.write(result);
			if(out != null){
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 通过收货人id查询此收货人地址信息
	 */
	public void findReceiverById(){
		//获取收货人id
		String id = this.getRequest().getParameter("id");
		XReceiverInfo xReceiverInfo = xReceiverInfoService.findReceiverById(id);
		
		Gson g = new Gson();
		try {
			PrintWriter out = null;  
			this.getResponse().setContentType("text/json; charset=utf-8");
			out = this.getResponse().getWriter();
			out.write(g.toJson(xReceiverInfo));
			if(out != null){
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置默认地址
	 */
	public void setUpRecDefaultStatus(){
		//获取收货人id
		String id = this.getRequest().getParameter("id");
		XReceiverInfo xReceiverInfo = xReceiverInfoService.setUpRecDefaultStatus(id);
		
		String result = "true";
		if(xReceiverInfo == null){
			result = "false";
		}
		
		try {
			PrintWriter out = null;  
			this.getResponse().setContentType("text/json; charset=utf-8");
			out = this.getResponse().getWriter();
			out.write(result);
			if(out != null){
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除收货人
	 */
	public void deleteReceiver(){
		//获取收货人id
		String id = this.getRequest().getParameter("id");
		XReceiverInfo xReceiverInfo = xReceiverInfoService.deleteReceiver(id);
		
		String result = "true";
		if(xReceiverInfo == null){
			result = "false";
		}
		
		try {
			PrintWriter out = null;  
			this.getResponse().setContentType("text/json; charset=utf-8");
			out = this.getResponse().getWriter();
			out.write(result);
			if(out != null){
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
