package com.kssj.order.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kssj.auth.model.XUser;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.GenericService;
import com.kssj.order.model.MyAccount;
import com.kssj.order.model.MyFood;
import com.kssj.order.model.MyOrder;
import com.kssj.order.model.XAppointRelation;
import com.kssj.order.model.XOrders;

public interface XOrdersService extends GenericService<XOrders, String>{
	
	/**
	 * @method: getCurrentOrder
	 * @Description: 获取当前订单
	 * @param filter
	 * @return
	 * @author : lig
	 * @param endTime 
	 * @param startTime 
	 * @date 2017-2-23 上午11:53:14
	 */
	public List<Map> getCurrentOrder(SqlQueryFilter filter, String startTime, String endTime,XUser xuser);
	
	
	/**
	 * @method: getLedgerList
	 * @Description: 台账查询
	 * @param filter
	 * @return
	 * @author : lig
	 * @param endTime 
	 * @param startTime 
	 * @date 2017-2-27 下午2:39:34
	 */
	public List<Map> getLedgerList(SqlQueryFilter filter, String startTime, String endTime,XUser xuser);
	
	/**
	 * @method: getRechargeObj
	 * @Description: 查询充值对象
	 * @param filter
	 * @param flag	标识：员工or部门
	 * @param deptId 部门id
	 * @param str	员工姓名or用户名or电话
	 * @return
	 * @author : lig
	 * @date 2017-2-28 上午11:04:13
	 */
	public List<Map> getRechargeObj(SqlQueryFilter filter , String flag, String deptId,String companyId,String str,String otherCompanyId);
	
	/**
	 * @method: getDept
	 * @Description: 获取所有部门
	 * @return
	 * @author : lig
	 * @date 2017-2-28 上午11:29:51
	 */
	public List<Map> getDept();
	
	
	/**
	 * @method: getBalanceById
	 * @Description: 根据id查询余额
	 * @param flag
	 * @param id
	 * @return
	 * @author : lig
	 * @date 2017-2-28 下午4:48:42
	 */
	public String getBalanceById(String flag,String id);
	
	/**
	 * @method: updBalance
	 * @Description: 更新余额
	 * @param flag
	 * @param id
	 * @author : lig
	 * @date 2017-2-28 下午5:37:42
	 */
	public void updBalance(String flag,String id,String money);

	/**
	 * add dingzhj  at date 2017-03-02 订餐明细
	 * @param endTime 
	 * @param startTime 
	 * @param qf
	 * @return
	 */
	public List<MyFood> getDetailedOrder(String startTime, String endTime,XUser foodBusiness);
	
	/**
	 * 
	 * @method: getTreeData
	 * @Description: TODO
	 * @param companyId  所属公司id
	 * @param isHaveStaff 是否有员工
	 * @return
	 * @author : zhaoziyun
	 * @date 2017-3-8 上午10:06:07
	 */
	public List<Map> getTreeData(String companyId,boolean isHaveStaff,String name,String otherCompanyId);
	
	/**
	 * @method: getDeptTree
	 * @Description: 部门tree
	 * @param companyId
	 * @return
	 * @author : lig
	 * @date 2017-3-2 下午5:09:41
	 */
	public List<Map> getDeptTree(String companyId);

	/**
	 * add dingzhj at date 2017-03-04 台帐明细
	 * @param endTime 
	 * @param startTime 
	 * @return
	 */
	public List<MyAccount> getDetailedLedger(String startTime, String endTime,XUser xuser);

	/**
	 * add dingzhj at date 2017-03-04 保存指定关系
	 * @param xAppointRelation
	 */
	public XAppointRelation saveXAppointRelation(XAppointRelation xAppointRelation);

	/**
	 * add dingzhj at date 2017-03-04 删除指定关系
	 * @param id
	 */
	public void delXAppointRelationById(String id);
	
	
	/**
	 * @method: chargeAll
	 * @Description: 全部充值
	 * @param ids
	 * @param flag
	 * @param balance
	 * @return
	 * @author : lig
	 * @date 2017-3-4 上午11:03:34
	 */
	public boolean chargeAll(String ids ,String flag ,String balance,String insUser);
	
	/**
	 * @method: getMyOrder
	 * @Description: 我的订单
	 * @param loginUserId
	 * @return
	 * @author : lig
	 * @date 2017-3-5 上午11:56:43
	 */
	public List<MyOrder> getMyOrder(String loginUserId) throws Exception;
	/**
	 * 
	 * @method: order
	 * @Description: TODO
	 * @param type
	 * @param xuser
	 * @param orderId
	 * @param map
	 * @return
	 * @author : zhaoziyun
	 * @param z_num 
	 * @param vistorName 
	 * @date 2017-3-5 下午3:53:38
	 */
	public Map<String,Object> order(String type ,XUser xuser ,String orderId,List<Map> map, int z_num, String vistorName,String recId);

	/**
	 * 取消订单
	 * @param flag
	 * @param orderId
	 * @param foodNum 
	 * @param orderType 
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> deleteOrder(String orderId, String foodNum,XUser user, String orderType) throws Exception;
	
	/**
	 * @method: isDeptSpecialRole
	 * @Description: 判断是否是部门专用角色
	 * @param userId
	 * @return
	 * @author : lig
	 * @date 2017-3-7 上午11:53:21
	 */
	public boolean isDeptSpecialRole(String userId);
	
	/**
	 * @method: getLeadByUserId
	 * @Description: 获取被指定人的领导列表
	 * @param userId
	 * @return
	 * @author : lig
	 * @date 2017-3-7 下午2:15:38
	 */
	public List<Map> getLeadByUserId(String userId);

	/**
	 * 根据套餐food_id查询当前时间库存
	 * @param foodId
	 * @return
	 */
	public String getFoodBillByFoodId(String foodId);
	
	
	/**
	 * @method: getMoney
	 * @Description:查询余额
	 * @param type
	 * @param id
	 * @return
	 * @author : lig
	 * @date 2017-3-8 下午3:02:18
	 */
	public Map getMoney(String type,String id);

	/**
	 * 统计
	 * @param sqlQueryFilter 
	 * @param qf
	 * @return
	 */
	public List<Map> getorderStatistics(SqlQueryFilter sqlQueryFilter, String startTime,String endTime,String dep_id,String flag,XUser xuser);

	
	/**
	 * 
	 * @method: getXAppointRelation
	 * @Description: 通过领导id获取记录
	 * @param leadId
	 * @return
	 * @author : zhaoziyun
	 * @date 2017-3-9 下午5:31:20
	 */
	public XAppointRelation getXAppointRelation(String leadId);

	/**
	 * 
	 * @param flag
	 * @param deptId
	 * @param companyId
	 * @param str
	 * @return
	 */
	public List<Map> getRechargeObjExcel(String flag, String deptId,
			String companyId, String str);
	
	/**
	 * 导出Excel查询方法（L）
	 * @param flag
	 * @param deptId
	 * @param companyId
	 * @param str
	 * @return
	 */
	public List<Map> getRechargeObjExcelL(String flag, String deptId,
			String companyId, String str, String startTime, String endTime);
	
	
	/**
	 * @method: clearZero
	 * @Description: 
	 * @param ids 员工id
	 * @param insUser 插入者
	 * @param insTime	插入 时间
	 * @return
	 * @author : lig
	 * @date 2017-3-11 上午11:27:18
	 */
	public boolean clearZero(String ids ,String insUser,Date insTime);
	
	/**
	 * @method: getCurrentFood
	 * @Description: 查询当前套餐
	 * @param week
	 * @param type
	 * @return
	 * @author : lig
	 * @date 2017-3-11 下午3:40:01
	 */
	public List<Map> getCurrentFood(String week,String type, String foodBusiness);

	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @param dep_id
	 * @return
	 */
	public List<Map> getorderStatisticsExcel(String startTime, String endTime,
			String dep_id,String flag,XUser xuser);

	/**
	 * 根据订单查询套餐
	 * @param orderId
	 * @return
	 */
	public List<Map> getMyOrderById(String orderId);
	/**
	 * 
	 * @method: getStaffInfo
	 * @Description: 通过公司、部门id获取员工信息
	 * @param companyId
	 * @param deptId
	 * @return
	 * @author : zhaoziyun
	 * @date 2017-3-21 下午1:29:10
	 */
	public List<Map> getStaffInfo(String companyId,String deptId);

	/**
	 * 我的预定
	 * @method: getReserveMyOrder
	 * @Description: TODO
	 * @param userId
	 * @return
	 * @author : dingzhj
	 * @throws Exception 
	 * @date 2017-3-21 下午3:53:53
	 */
	public List<MyOrder> getReserveMyOrder(String userId) throws Exception;
	
	/**
	 * @method: getFoodByCondtion
	 * @Description: 条件查询菜单
	 * @param week	周几（WEEK_1）
	 * @param type 类型（LUN：午餐、DIN：晚餐、null：午和晚）
	 * @return
	 * @author : lig
	 * @date 2017-3-22 下午3:01:42
	 */
	public List<Map> getFoodByCondtion(String week,String type,String foodBusiness);

	/**
	 * 订餐预定
	 * @method: orderReserve
	 * @Description: TODO
	 * @param dcType
	 * @param orderUser
	 * @param userId
	 * @param listMap
	 * @param z_num
	 * @param vistorName
	 * @return
	 * @author : dingzhj
	 * @param time 
	 * @throws ParseException 
	 * @date 2017-3-22 下午4:56:01
	 */
	public Map<String, Object> orderReserve(String dcType, XUser orderUser,
			String userId, List<Map> listMap, int z_num, String vistorName, String time, String recId) throws ParseException;

	/**
	 * 查询预定套餐
	 * @method: getFoodBillReserveByFoodId
	 * @Description: TODO
	 * @param weeknum
	 * @param foodId
	 * @return
	 * @author : dingzhj
	 * @date 2017-3-23 下午3:16:04
	 */
	public String getFoodBillReserveByFoodId(String weeknum, String foodId);

	/**
	 * 查询指定人订单
	 * @method: getMyOrderAppoint
	 * @Description: TODO
	 * @param userId
	 * @return
	 * @author : dingzhj
	 * @date 2017-3-31 下午3:42:13
	 */
	public List<MyOrder> getMyOrderAppoint(String userId) throws Exception;
	
	/**
	 * @method: getRechargeRecord
	 * @Description: 查询充值记录
	 * @param filter
	 * @param flag	标识：员工or部门
	 * @param deptId 部门id
	 * @param str	员工姓名or用户名or电话
	 * @return
	 * @author : zzy
	 * @date 2017-2-28 上午11:04:13
	 */
	public List<Map> getRechargeRecord(SqlQueryFilter filter , String flag, String deptId,String companyId,String str);
	
	public List<Map> getAllOrders(SqlQueryFilter qf, String flag, String deptId, String companyId, String str);

	public List<Map> getMyOrderById2(String orderId);


	public Map<String, Object> orderLive(String dcType, XUser orderUser,
			String userId, List<Map> listMap, int z_num, String vistorName);

	public List<MyOrder> getMyLiveOrder(String userId) throws Exception;

	public Map<String, Object> deleteLiveOrder(String orderId, String foodNum,
			XUser orderUser, String orderType) throws Exception;


	public List<Map> getCurrentXCOrder(SqlQueryFilter qf, String startTime,
			String endTime, XUser xuser);


	public void updateOrderRecStatus();


	public String getCurrentStaffCompanyId(XUser xuser);


}
