package com.kssj.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.kssj.auth.model.XUser;
import com.kssj.base.util.Constants;
import com.kssj.base.util.ListUtil;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.impl.GenericServiceImpl;
import com.kssj.order.dao.XFoodBillDao;
import com.kssj.order.dao.XFoodDao;
import com.kssj.order.model.XFood;
import com.kssj.order.model.XFoodBill;
import com.kssj.order.service.XFoodService;

public class XFoodServiceImpl extends GenericServiceImpl<XFood, String> implements XFoodService{

	@SuppressWarnings("unused")
	private XFoodDao dao;
	private XFoodBillDao xFoodBillDao;
	
	public XFoodServiceImpl(XFoodDao dao,XFoodBillDao xFoodBillDao) {
		super(dao);
		this.dao = dao;
		this.xFoodBillDao =  xFoodBillDao;
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Map> getAllFoods(SqlQueryFilter filter,XUser xuser) {
		String sql = "select id,food_name,food_desc,cost_price,sell_price,food_img,user_id,ins_time,ins_user,upd_time,upd_user,food_type  from x_food where del_flag='0' and ins_user='"+xuser.getUserName()+"'";
		filter.setBaseSql(sql);
		List<Map> list = this.query(filter);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}
	@Override
	public List<Map> getFoodbillByWeekId(String id,XUser xuser) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select xf.id,xf.food_name,xf.food_desc,xf.cost_price,xf.sell_price,xf.food_img,xf.user_id,xf.ins_time,xf.ins_user,xf.upd_time,xf.upd_user,xf.food_type  from x_food xf left join x_food_bill  xfb  ");
		sql.append(" on xfb.food_id = xf.id where xf.del_flag='0' and xfb.dictionary_id =? and xf.ins_user=?");
		List<Map> list = this.dao.findBySqlToMap(sql.toString(), new Object[]{id,xuser.getUserName()}, 0, 0);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
		
	}
	@Override
	public List<XFood> getFood(String foodName) {
		String hql ="from XFood where foodName =? and delFlag = '0' ";
		Object[] o ={foodName};
		List<XFood> list = this.dao.findByHql(hql,o);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}

	@Override
	public boolean saveBillMap(Map<String, String> billMap,String userName) {
		try {
			//add dingzhj 获取当前登陆人姓名  根据姓名 删除想foodBill 数据
			String sql = "delete from x_food_bill where ins_user='"+userName+"'";
			xFoodBillDao.executeSql(sql);
			
			for (Map.Entry<String, String> entry : billMap.entrySet()){  
				  String weekDay = entry.getKey().toString();
				  Map<String, String> foodTypeMap  = (Map)JSONObject.fromObject(entry.getValue());
					  JSONArray lunchArray = JSONArray.fromObject(foodTypeMap.get("lunch"));
					  JSONArray dinnerArray = JSONArray.fromObject(foodTypeMap.get("dinner"));
					  String lunchDictionaryId = Constants.billMap.get(weekDay).toString();
					  String dinnerDictionaryId = Constants.billMap.get(weekDay+"-dinner").toString();
					  System.out.println(lunchDictionaryId+"-----"+dinnerDictionaryId);
					  XFoodBill xFoodBill = new XFoodBill();
					  
					  for (int j = 0; j < lunchArray.size(); j++) {
						  Map m = (Map)lunchArray.get(j);
						  xFoodBill.setId(null);
						  xFoodBill.setDictionaryId(lunchDictionaryId);
						  xFoodBill.setFoodId(m.get("foodid").toString());
						  xFoodBill.setFoodName(m.get("foodName").toString());
						  xFoodBill.setFoodNum(Integer.parseInt(m.get("foodNum").toString()));
						  xFoodBill.setFoodRemain(m.get("foodNum").toString());
						  xFoodBill.setInsTime(new Date());
						  xFoodBill.setInsUser(userName);
						  xFoodBillDao.save(xFoodBill);
					}
					  for (int j = 0; j < dinnerArray.size(); j++) {
						  Map m = (Map)dinnerArray.get(j);
						  xFoodBill.setId(null);
						  xFoodBill.setDictionaryId(dinnerDictionaryId);
						  xFoodBill.setFoodId(m.get("foodid").toString());
						  xFoodBill.setFoodName(m.get("foodName").toString());
						  xFoodBill.setFoodNum(Integer.parseInt(m.get("foodNum").toString()));
						  xFoodBill.setFoodRemain(m.get("foodNum").toString());
						  xFoodBill.setInsTime(new Date());
						  xFoodBill.setInsUser(userName);
						  xFoodBillDao.save(xFoodBill);
					  }
			  
			}  
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		}
		
	}

	@Override
	public List<Map> getBillMap(XUser xuser) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select xd.week,xd.food_type,xfb.dictionary_id,xfb.food_id,xfb.food_name,xfb.food_num,xfb.ins_user,xf.food_desc,xf.sell_price from x_food_bill xfb left join  x_dictionary xd on xd.id = xfb.dictionary_id  ");
		sb.append(" left join x_food xf on xf.id = xfb.food_id  where xf.del_flag ='0' and xfb.ins_user='"+xuser.getUserName()+"'");
		List<Map> list = this.dao.findBySqlToMap(sb.toString(), null, 0, 0);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}

}
