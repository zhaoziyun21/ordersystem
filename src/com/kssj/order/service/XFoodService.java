package com.kssj.order.service;

import java.util.List;
import java.util.Map;

import com.kssj.auth.model.XUser;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.GenericService;
import com.kssj.order.model.XFood;

public interface XFoodService extends GenericService<XFood, String>{
	
	/**
	 * @method: getAll
	 * @Description: 查询所有菜谱
	 * @return
	 * @author : lig
	 * @date 2017-2-22 下午2:45:54
	 */
	public List<Map> getAllFoods(SqlQueryFilter filter,XUser xuser); 
	
	/**
	 * 
	 * @method: getFoodbillByWeekId
	 * @Description: 查询周X的套餐
	 * @param id
	 * @return
	 * @author : zhaoziyun
	 * @date 2017-3-10 上午11:07:32
	 */
	public List<Map> getFoodbillByWeekId(String id,XUser xuser);
	/**
	 * 
	 * @method: getFood
	 * @Description: 通过套餐名获取套餐
	 * @param foodName
	 * @return
	 * @author : zhaoziyun
	 * @date 2017-3-8 下午3:02:50
	 */
	public List<XFood> getFood(String foodName); 
	
	/**
	 * 
	 * @method: saveBillMap
	 * @Description: 保存一周菜谱
	 * @param billMap
	 * @return
	 * @author : zhaoziyun
	 * @param userName 
	 * @date 2017-3-10 下午2:03:58
	 */
	public boolean saveBillMap(Map<String, String> billMap, String userName);
	
	public List<Map> getBillMap(XUser xuser);
}
