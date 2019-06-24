package com.kssj.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.kssj.auth.dao.XStaffSumDao;
import com.kssj.auth.dao.XUserDao;
import com.kssj.auth.model.XStaffSum;
import com.kssj.auth.model.XUser;
import com.kssj.base.util.Constants;
import com.kssj.base.util.ListUtil;
import com.kssj.base.util.StringUtil;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.dao.GenericDao;
import com.kssj.frame.service.impl.GenericServiceImpl;
import com.kssj.order.dao.XDetailRecordDao;
import com.kssj.order.dao.XOnlineOrdersDao;
import com.kssj.order.model.XDetailRecord;
import com.kssj.order.model.XOnlineOrders;
import com.kssj.order.service.XOnlineOrdersService;

public class XOnlineOrdersServiceImpl extends GenericServiceImpl<XOnlineOrders, String>
		implements XOnlineOrdersService {

	@SuppressWarnings("unused")
	private XOnlineOrdersDao dao;

	private XStaffSumDao xStaffSumDao;

	private XDetailRecordDao xDetailRecordDao;
	
	private XUserDao xUserDao;

	
	public XOnlineOrdersServiceImpl(XOnlineOrdersDao dao, XStaffSumDao xStaffSumDao,
			XDetailRecordDao xDetailRecordDao,XUserDao xUserDao) {
		super(dao);
		this.dao = dao;
		this.xStaffSumDao = xStaffSumDao;
		this.xDetailRecordDao = xDetailRecordDao;
		this.xUserDao = xUserDao;
		// TODO Auto-generated constructor stub
	}

	@Override
	public XStaffSum getXStaffumByUserId(String id) {
		String hql = "from XStaffSum  where userId = '" + id + "'";
		List<XStaffSum> list = xStaffSumDao.findByHql(hql, null);
		if (ListUtil.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	@Transactional
	public boolean onlineOrders(String jine, String dianpu, XUser user,XStaffSum staffSum) {
		/**
		 * 1.先扣钱， 添加 x_detail_record 一条记录 2.生成订单 更新余额 添加余额操作记录 生成订单
		 */
		boolean b = false;
		Date date = new Date();
		try {
			// 扣钱操作
			int i = Integer.parseInt(staffSum.getBalance()) - Integer.parseInt(jine) ;
			staffSum.setBalance(i+"");
			staffSum.setUpdTime(date);
			staffSum.setUpdUser(user.getUserName());
			xStaffSumDao.save(staffSum); //付款
			// 添加一条记录付钱
			XDetailRecord xDetailRecord = new XDetailRecord();
			 xDetailRecord.setBalance(i+"");
			 xDetailRecord.setChangeMoney(jine+"");
			 xDetailRecord.setType("1");
			 xDetailRecord.setDeptId(user.getDeptId());
			 xDetailRecord.setUserId(user.getUserId());
			 xDetailRecord.setRemark("到店支付");
			 xDetailRecord.setInsTime(date);
			 xDetailRecord.setInsUser(user.getUserName());
			 xDetailRecordDao.save(xDetailRecord);//添加余额操作记录
			 XOnlineOrders onlineOrders = new XOnlineOrders();
			 onlineOrders.setUserId(user.getUserId());
			 onlineOrders.setOrderType("3");//定义3为到店支付
			 onlineOrders.setOrderMoney(jine);
			 onlineOrders.setRestaurantId(dianpu);
			 XUser xu = xUserDao.get(dianpu); //根据餐饮ID查询信息
			 if(xu != null){
				 onlineOrders.setFoodCompanyName(xu.getFoodCompanyName());
			 }
			 onlineOrders.setInsTime(date);
			 onlineOrders.setInsUser(user.getUserName());
			 onlineOrders.setDelFlag("0");
			 dao.save(onlineOrders);
			 b = true;
			} catch (Exception e) {
				b = false;
				e.printStackTrace();
		}
		return b;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Map> getorderStatistics(SqlQueryFilter filter,String startTime,
			String endTime,String dep_id,String cyId) {
		StringBuffer sql = new StringBuffer();
		if(dep_id != null && dep_id != ""){
			if(dep_id.indexOf("dept_")>=0){// 部门统计
				String u_id = dep_id.substring(5);
				sql.append(" SELECT");
				sql.append(" xc.company_name,");
				sql.append(" xd.dept_name,");
				sql.append(" xu.real_name,");
				sql.append(" xo.restaurant_id,");
				sql.append(" xo.food_company_name,");
				sql.append(" SUM(xo.order_money) AS numMoney");
				sql.append(" FROM");
				sql.append(" x_online_orders xo");
				sql.append(" LEFT JOIN x_user xu ON xu.user_id = xo.user_id");
				sql.append(" LEFT JOIN x_company xc ON xc.company_id = xu.company_id");
				sql.append(" LEFT JOIN x_dept xd ON xd.dept_id = xu.dept_id");
				sql.append(" WHERE 1=1");
				sql.append(" AND xd.dept_id = '"+u_id+"' or xd.supdepid = '"+u_id+"'");
				if(StringUtil.isNotEmpty(cyId)){
					sql.append(" and xo.restaurant_id = '"+cyId+"'");
				}
				if(startTime !=null && startTime !=""){
					sql.append(" and xod.ins_time >= '"+startTime+"'");
				}
				if(endTime !=null && endTime !=""){
					sql.append(" and xod.ins_time <= '"+endTime+"'");
				}
				
				sql.append(" GROUP BY 	xu.dept_id,xo.restaurant_id");
				sql.append(" ORDER BY  	xo.ins_time");
				 filter.setBaseSql(sql.toString());
				  List<Map> list = this.query(filter);
				  if(ListUtil.isNotEmpty(list)){
					  return list;	
				  }
			}else{//公司
				sql.append(" SELECT");
				sql.append(" xc.company_name,");
				sql.append(" xd.dept_name,");
				sql.append(" xu.real_name,");
				sql.append(" xo.restaurant_id,");
				sql.append(" xo.food_company_name,");
				sql.append(" SUM(xo.order_money) AS numMoney");
				sql.append(" FROM x_online_orders xo");
				sql.append(" LEFT JOIN x_user xu ON xu.user_id = xo.user_id");
				sql.append(" LEFT JOIN x_company xc ON xc.company_id = xu.company_id");
				sql.append(" LEFT JOIN x_dept xd ON xd.dept_id = xu.dept_id ");
				sql.append(" WHERE 1=1 " );
				if(StringUtil.isNotEmpty(cyId)){
					sql.append(" and xo.restaurant_id = '"+cyId+"'");
				}
				if(StringUtil.isNotEmpty(dep_id)){
					sql.append(" AND xu.company_id = '"+dep_id+"'");
				}
				if(StringUtil.isNotEmpty(startTime)){
					sql.append(" and xo.ins_time >= '"+startTime+"'");
				}
				if(StringUtil.isNotEmpty(endTime)){
					sql.append(" and xo.ins_time <= '"+endTime+"'");
				}
				sql.append(" GROUP BY xu.company_id,xo.restaurant_id");
				sql.append(" ORDER BY  xo.ins_time");
				 filter.setBaseSql(sql.toString());
				  List<Map> list = this.query(filter);
				  if(ListUtil.isNotEmpty(list)){
					  return list;	
				  }
			}	
				 
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> getLedgerList(SqlQueryFilter filter,String startTime, 
			String endTime,String dep_id,XUser user) {
		StringBuffer sql = new StringBuffer();
			if(dep_id.indexOf("dept_")>=0){// 部门统计
				String u_id = dep_id.substring(5);
				sql.append(" SELECT");
				sql.append(" xc.company_name,");
				sql.append(" xd.dept_name,");
				sql.append(" xu.real_name,");
				sql.append(" xo.restaurant_id,");
				sql.append(" xo.food_company_name,");
				sql.append(" SUM(xo.order_money) AS numMoney");
				sql.append(" FROM");
				sql.append(" x_online_orders xo");
				sql.append(" LEFT JOIN x_user xu ON xu.user_id = xo.user_id");
				sql.append(" LEFT JOIN x_company xc ON xc.company_id = xu.company_id");
				sql.append(" LEFT JOIN x_dept xd ON xd.dept_id = xu.dept_id");
				sql.append(" WHERE 1=1");
				if("2".equals(user.getType())){
					sql.append(" and xo.restaurant_id = '"+user.getUserId()+"'");
				}
				sql.append(" AND xd.dept_id = '"+u_id+"' or xd.supdepid = '"+u_id+"'");
				if(startTime !=null && startTime !=""){
					sql.append(" and xod.ins_time >= '"+startTime+"'");
				}
				if(endTime !=null && endTime !=""){
					sql.append(" and xod.ins_time <= '"+endTime+"'");
				}
				sql.append(" GROUP BY 	xu.dept_id,xo.restaurant_id");
				sql.append(" ORDER BY  	xo.ins_time");
				filter.setBaseSql(sql.toString());
				System.out.println("1111111====="+sql.toString());
				List<Map> list = this.query(filter);
				if(ListUtil.isNotEmpty(list)){
					return list;	
				}
			}else{//公司
				sql.append(" SELECT");
				sql.append(" xc.company_name,");
				sql.append(" xd.dept_name,");
				sql.append(" xu.real_name,");
				sql.append(" xo.restaurant_id,");
				sql.append(" xo.food_company_name,");
				sql.append(" SUM(xo.order_money) AS numMoney");
				sql.append(" FROM x_online_orders xo");
				sql.append(" LEFT JOIN x_user xu ON xu.user_id = xo.user_id");
				sql.append(" LEFT JOIN x_company xc ON xc.company_id = xu.company_id");
				sql.append(" LEFT JOIN x_dept xd ON xd.dept_id = xu.dept_id ");
				sql.append(" WHERE 1=1 " );
				if("2".equals(user.getType())){
					sql.append(" and xo.restaurant_id = '"+user.getId()+"'");  
				}
				if(StringUtil.isNotEmpty(dep_id)){
					sql.append(" AND xu.company_id = '"+dep_id+"'");
				}
				if(StringUtil.isNotEmpty(startTime)){
					sql.append(" and xo.ins_time >= '"+startTime+"'");
				}
				if(StringUtil.isNotEmpty(endTime)){
					sql.append(" and xo.ins_time <= '"+endTime+"'");
				}
				sql.append(" GROUP BY xu.company_id,xo.restaurant_id");
				sql.append(" ORDER BY  xo.ins_time");
				filter.setBaseSql(sql.toString());
				System.out.println("22222222====="+sql.toString());
				List<Map> list = this.query(filter);
				if(ListUtil.isNotEmpty(list)){
					return list;	
				}
				 
		}
		return null;
}

	@Override
	public List<XUser> getUserByCanYinAll() {
		String hql = " from XUser where type = '2' and delFlag = '0'";
		List<XUser> list = xUserDao.findByHql(hql, null);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}
}