package com.kssj.order.service;

import java.util.List;
import java.util.Map;

import com.kssj.auth.model.XStaffSum;
import com.kssj.auth.model.XUser;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.GenericService;
import com.kssj.order.model.XOnlineOrders;

public interface XOnlineOrdersService extends GenericService<XOnlineOrders,String>{
	/**
	 * 根据userID余额
	 * @method: getXStaffumByUserId
	 * @Description: TODO
	 * @param id
	 * @return
	 * @author : dingzhj
	 * @date 2017-12-14 下午12:07:31
	 */
	public XStaffSum getXStaffumByUserId(String id);
	/**
	 * 付款
	 * @method: onlineOrders
	 * @Description: TODO
	 * @param flag
	 * @param id
	 * @param user
	 * @return
	 * @author : dingzhj
	 * @param staffSum 
	 * @date 2017-12-14 下午12:14:33
	 */
	public boolean onlineOrders(String flag, String id, XUser user, XStaffSum staffSum);
		
	/**
	 *	到付统计
	 * @param sqlQueryFilter
	 * @param startTime
	 * @param endTime
	 * @param dep_id
	 * @param cyId 
	 * @return
	 */
	public List<Map> getorderStatistics(SqlQueryFilter sqlQueryFilter, String startTime,String endTime,String dep_id, String cyId);
	
	/**
	 * @method: getLedgerList
	 * @Description: 到付台账查询
	 * @param filter
	 * @return
	 * @author : lig
	 * @param endTime 
	 * @param startTime 
	 * @param user 
	 * @date 2017-2-27 下午2:39:34
	 */
	public List<Map> getLedgerList(SqlQueryFilter filter, String startTime, String endTime,String dept_id, XUser user);
	/**
	 * 查询餐饮公司List
	 * @return
	 */
	public List<XUser> getUserByCanYinAll();
}
