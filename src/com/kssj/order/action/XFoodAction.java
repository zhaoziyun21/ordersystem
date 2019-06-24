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
import com.kssj.order.model.XFood;
import com.kssj.order.model.XFoodSendAddress;
import com.kssj.order.model.XFoodSendRegion;
import com.kssj.order.service.XFoodService;

@SuppressWarnings("serial")
public class XFoodAction extends BaseAction {
	
	@Resource
	private XFoodService xFoodService;
	
	private XFood xfood;
	
	public XFood getXfood() {
		return xfood;
	}

	public void setXfood(XFood xfood) {
		this.xfood = xfood;
	}
	
	private XFoodSendRegion xFoodSendRegion;
	
	private XFoodSendAddress xFoodSendAddress;

	public XFoodSendRegion getxFoodSendRegion() {
		return xFoodSendRegion;
	}

	public void setxFoodSendRegion(XFoodSendRegion xFoodSendRegion) {
		this.xFoodSendRegion = xFoodSendRegion;
	}

	public XFoodSendAddress getxFoodSendAddress() {
		return xFoodSendAddress;
	}

	public void setxFoodSendAddress(XFoodSendAddress xFoodSendAddress) {
		this.xFoodSendAddress = xFoodSendAddress;
	}

	/**
	 * @method: getAllFoods
	 * @Description: 获取列表
	 * @throws Exception
	 * @author : lig
	 * @date 2017-2-22 下午4:41:43
	 */
	@SuppressWarnings("rawtypes")
	public void getAllFoods() throws Exception{
		try {			
			XUser xuser = this.getSysUserBySession();
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
//			SqlConditionSpeller spell = new SqlConditionSpellerForMySql("ins_user", SqlSpellerOperatorType.EQ,xuser.getUserName(), SqlSpellerDataType.S);

//			getQf().addCondition(spell);
			List<Map> foodList = xFoodService.getAllFoods(getQf(), xuser);
			StringBuffer buff = new StringBuffer("{\"Total\":")
			.append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson = new Gson();
			jsonString = gson.toJson(foodList);
			buff.append(jsonString);
			if(!ListUtil.isNotEmpty(foodList)){
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
	 * @method: saveFood
	 * @Description: 添加菜谱
	 * @throws Exception
	 * @author : lig
	 * @date 2017-2-22 下午4:41:06
	 */
	public void saveFood() throws Exception{
		String result = "Y";
		String type = this.getRequest().getParameter("type");
		try {			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			//当前登录用户
			XUser xuser = (XUser)this.getSession().getAttribute("loginUser");
			XFood foo =null;
			if(type.equals("add")){
				xfood.setInsUser(xuser.getUserName());
				xfood.setInsTime(new Date());
				xfood.setUserId(xuser.getUserId());
				  foo= xFoodService.save(xfood);
			}else if(type.equals("update")){
				xfood.setUpdUser(xuser.getUserName());
				xfood.setUpdTime(new Date());
				XFood xf = xFoodService.get(xfood.getId());
				BeanUtil.copyNotNullProperties(xf, xfood);
				xfood.setUserId(xuser.getUserId());
				  foo= xFoodService.save(xf);
			}
			if(foo == null){
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
	 * @method: toEditFood
	 * @Description: to编辑页
	 * @return
	 * @throws Exception
	 * @author : lig
	 * @date 2017-2-22 下午5:32:10
	 */
	public String toEditFood() throws Exception{
		try {			
			String foodid = this.getRequest().getParameter("foodid");
			xfood = xFoodService.get(foodid);
			this.setForwardPage("/jsp/order/xfood/xfood_view.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询菜谱失败!");
		}
	}
	
	
	
	/**
	 * @method: delFood
	 * @Description: 删除food
	 * @throws Exception
	 * @author : lig
	 * @date 2017-2-22 下午5:24:41
	 */
	public void delFood() throws Exception{
		String result = "Y";
		String foodid = this.getRequest().getParameter("foodid");
		XUser xu  = this.getSysUserBySession();
		try {			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try{
				XFood xf= xFoodService.get(foodid);
				xf.setDelFlag(1);
				xf.setUpdTime(new Date());
				xf.setUpdUser(xu.getUserName());
				xFoodService.save(xf);
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
	 * 
	 * @method: checkFoodName
	 * @Description: 验证菜单名唯一
	 * @author : zhaoziyun
	 * @date 2017-3-8 下午2:54:59
	 */
	 public void checkFoodName(){
		 String result ="Y";
		 String foodName =this.getRequest().getParameter("foodName");
		 List<XFood> xfoodlist = xFoodService.getFood(foodName);
		 if(ListUtil.isNotEmpty(xfoodlist)){
			 result =  "N";
			
		 }else{
			 result="Y";
		 }
		 ajaxJson(result);
	 }
	/**
	 * 
	 * @method: saveFoodbill
	 * @Description: 保存一周菜谱
	 * @author : zhaoziyun
	 * @date 2017-3-8 下午2:49:46
	 */
	public void saveFoodbill(){
		String foodbillInfo=this.getRequest().getParameter("foodbillInfo");
		//add dingzhj at date 2017-03-10 
		String userName  = this.getSysUserBySession().getUserName();
//		String foodbillInfo="[{\"week_1-din\": [{\"food_name\": \"套餐A\",\"food_num\": \"12\",\"food_id\": \"1\"},{\"food_name\": \"套餐B\",\"food_num\": \"13\",\"food_id\": \"2\"}]}]";
		Map<String, String> billMap  = (Map)JSONObject.fromObject(foodbillInfo);
		xFoodService.saveBillMap(billMap,userName);
	}
	/**
	 * 
	 * @method: goFoodbillForm
	 * @Description: 跳转到编辑一周菜谱
	 * @return
	 * @author : zhaoziyun
	 * @date 2017-3-8 下午2:51:42
	 */
	public String goFoodbillForm(){
		XUser xuser = this.getSysUserBySession();
		
		String abc = "{\"week_1\":{\"lunch\":[{\"foodName\":\"套餐B\",\"foodNum\":\"1\",\"foodid\":\"G\"},{\"foodName\":\"套餐C\",\"foodNum\":\"1\",\"foodid\":\"G\"},{\"foodName\":\"套餐A\",\"foodNum\":\"1\"}],\"dinner\":[{\"foodName\":\"套餐E\",\"foodNum\":\"1\"},{\"foodName\":\"套餐D\",\"foodNum\":\"1\"}]},\"week_2\":{\"lunch\":[{\"foodName\":\"套餐B\",\"foodNum\":\"1\",\"foodid\":\"G\"},{\"foodName\":\"套餐C\",\"foodNum\":\"1\",\"foodid\":\"G\"},{\"foodName\":\"套餐A\",\"foodNum\":\"1\"}],\"dinner\":[{\"foodName\":\"套餐C\",\"foodNum\":\"1\"},{\"foodName\":\"套餐D\",\"foodNum\":\"1\"}]},\"week_3\":{\"lunch\":[{\"foodName\":\"套餐B\",\"foodNum\":\"1\",\"foodid\":\"G\"},{\"foodName\":\"套餐C\",\"foodNum\":\"1\",\"foodid\":\"G\"},{\"foodName\":\"套餐A\",\"foodNum\":\"1\"}],\"dinner\":[{\"foodName\":\"套餐C\",\"foodNum\":\"1\"},{\"foodName\":\"套餐D\",\"foodNum\":\"1\"}]},\"week_4\":{\"lunch\":[],\"dinner\":[{\"foodName\":\"套餐C\",\"foodNum\":\"1\"},{\"foodName\":\"套餐D\",\"foodNum\":\"1\"}]},\"week_5\":{\"lunch\":[],\"dinner\":[{\"foodName\":\"套餐C\",\"foodNum\":\"1\"}]}}";
		List<Map> list = xFoodService.getBillMap(xuser);
//		Map map = new HashMap<String, Object>();
//		for (Map.Entry<String, String> entry : Constants.billMap.entrySet()){  
//			map.put("", value);
//		}
//		switch (key) {
//		case value:
//			
//			break;
//
//		default:
//			break;
//		}
//		for (int i = 0; i < list.size(); i++) {
//			map.put(list.get(i).get("week"),);
//		}
		JSONArray json = JSONArray.fromObject(list);
		this.getRequest().setAttribute("foodbillInfo", json.toString());
		this.setForwardPage("/jsp/order/xfood/xfoodbill_form.jsp");
		return SUCCESS;
	}
	
	
	/**
	 * @method: getFoodList
	 * @Description: 编辑菜谱时获取所有套餐
	 * @throws Exception
	 * @author : lig
	 * @date 2017-3-9 下午5:31:41
	 */
	public void getFoodList() throws Exception{
//		JSONArray jsonArr = new JSONArray();
		JSONObject j =new JSONObject();
		XUser xuser = this.getSysUserBySession();
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			List<Map> foodList = xFoodService.getAllFoods(getQf(), xuser);
			String  type = this.getRequest().getParameter("type");
			String billId = Constants.billMap.get(type).toString();
			List<Map> weekFoodList = xFoodService.getFoodbillByWeekId(billId,xuser);
			if(ListUtil.isNotEmpty(foodList)){
				if(ListUtil.isNotEmpty(weekFoodList)){
					foodList.removeAll(weekFoodList);
				}
				Map map = new HashMap<String, String>();
				map.put("foodList", foodList);
				map.put("weekFoodList", weekFoodList);
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
	

}
