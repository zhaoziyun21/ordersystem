package com.kssj.order.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.kssj.auth.dao.XDeptDao;
import com.kssj.auth.dao.XDeptSumDao;
import com.kssj.auth.dao.XStaffSumDao;
import com.kssj.auth.dao.XUserDao;
import com.kssj.auth.model.XDept;
import com.kssj.auth.model.XDeptSum;
import com.kssj.auth.model.XStaffSum;
import com.kssj.auth.model.XUser;
import com.kssj.auth.service.XStaffSumService;
import com.kssj.auth.service.XUserService;
import com.kssj.base.util.BeanUtil;
import com.kssj.base.util.Constants;
import com.kssj.base.util.DateUtil;
import com.kssj.base.util.DateWeek;
import com.kssj.base.util.ListUtil;
import com.kssj.base.util.StringUtil;
import com.kssj.frame.command.sql.SqlConditionSpeller;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.impl.GenericServiceImpl;
import com.kssj.order.dao.XAppointRelationDao;
import com.kssj.order.dao.XDetailRecordDao;
import com.kssj.order.dao.XFoodBillDao;
import com.kssj.order.dao.XOrderDetailDao;
import com.kssj.order.dao.XOrdersDao;
import com.kssj.order.model.MyAccount;
import com.kssj.order.model.MyFood;
import com.kssj.order.model.MyFoodUser;
import com.kssj.order.model.MyOrder;
import com.kssj.order.model.XAppointRelation;
import com.kssj.order.model.XDetailRecord;
import com.kssj.order.model.XFood;
import com.kssj.order.model.XFoodSendAddress;
import com.kssj.order.model.XFoodSendRegion;
import com.kssj.order.model.XOrderDetail;
import com.kssj.order.model.XOrders;
import com.kssj.order.service.XDetailRecordService;
import com.kssj.order.service.XFoodSendAddressService;
import com.kssj.order.service.XFoodSendRegionService;
import com.kssj.order.service.XFoodService;
import com.kssj.order.service.XOrdersService;
import com.kssj.product.model.XReceiverInfo;

public class XOrdersServiceImpl extends GenericServiceImpl<XOrders, String> implements XOrdersService{

	private XOrdersDao dao;
	private XAppointRelationDao relationDao;
	
	@Autowired
	private XUserDao xUserDao;
	
	private XStaffSumDao xStaffSumDao;
	private XDetailRecordDao xDetailRecordDao;
	private XOrderDetailDao xOrderDetailDao;
	private XDeptSumDao xDeptSumDao;
	private XDeptDao xDeptDao;
	private XFoodBillDao xFoodBillDao;
	
	@Resource
	private XDetailRecordService xDetailRecordService;
	@Resource
	private XStaffSumService xStaffSumService;
	
	@Resource
	private XFoodService xFoodService;
	
	@Resource
	private XUserService xUserService;
	
	@Resource
	private XFoodSendAddressService xFoodSendAddressService;
	
	@Resource
	private XFoodSendRegionService xFoodSendRegionService;
	
	public XOrdersServiceImpl(XOrdersDao dao,XAppointRelationDao relationDao,XStaffSumDao xStaffSumDao,XDetailRecordDao xDetailRecordDao,XOrderDetailDao xOrderDetailDao,XDeptSumDao xDeptSumDao,XFoodBillDao xFoodBillDao,XDeptDao xDeptDao) {
		super(dao);
		this.dao = dao;
		this.relationDao = relationDao;
		this.xStaffSumDao = xStaffSumDao;
		this.xDetailRecordDao = xDetailRecordDao;
		this.xOrderDetailDao = xOrderDetailDao;
		this.xDeptSumDao = xDeptSumDao;
		this.xFoodBillDao = xFoodBillDao;
		this.xDeptDao = xDeptDao;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> getCurrentOrder(SqlQueryFilter filter,String startTime, String endTime, XUser xuser) {
		// modify by dingzhj at date 2017-03-03  加查询条件
		
		/*String sql = "SELECT xf.food_name,food_desc,food_num FROM (SELECT od.food_id ,SUM(food_num) AS food_num   FROM x_order_detail od WHERE od.order_id IN " 
					+"( SELECT o.id FROM x_orders  o WHERE ins_time < DATE_FORMAT(NOW(),'%Y-%m-%d 10:30:00')) GROUP BY od.food_id) tab LEFT JOIN x_food xf  "
					+"ON tab.food_id = xf.id ";*/
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT");
		sql.append(" xf.food_name,");
		sql.append(" food_desc,");
		sql.append(" food_num");
		sql.append(" FROM");
		sql.append(" (");
		sql.append(" SELECT");
		sql.append(" od.food_id,");
		sql.append(" SUM(food_num) AS food_num");
		sql.append(" FROM");
		sql.append(" x_order_detail od");
		sql.append(" WHERE");
		sql.append(" od.order_id IN (");
		sql.append(" SELECT");
		sql.append(" o.id");
		sql.append(" FROM");
		sql.append(" x_orders o");
		sql.append(" WHERE 1=1 AND o.del_flag = '0' and o.order_category != 'XC' "); //非现场订单
		if(startTime != null && startTime != ""){
			//sql.append(" and ins_time < DATE_FORMAT('"+startTime+"','%Y-%m-%d %h-%i-%s')");
			sql.append(" and od.ins_time >='"+startTime+"'");
		}
		if(endTime != null && endTime != ""){
			//sql.append(" and ins_time > DATE_FORMAT('"+endTime+"','%Y-%m-%d %h-%i-%s')");		
			sql.append(" and od.ins_time <= '"+endTime+"'");		
		}
		sql.append(" )");
		sql.append(" GROUP BY");
		sql.append(" od.food_id");
		sql.append(" ) tab");
		sql.append(" LEFT JOIN x_food xf ON tab.food_id = xf.id where xf.ins_user='"+xuser.getUserName()+"'");
		
		filter.setBaseSql(sql.toString());
		List<Map> list = this.query(filter);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> getLedgerList(SqlQueryFilter filter,String startTime, String endTime,XUser xuser) {
		/*String sql = "SELECT SUM(xod.food_num) food_num,SUM(xod.food_num)*25 total,xod.food_id,xod.ins_time,xf.food_name FROM x_order_detail xod " 
					+" LEFT JOIN x_food xf   ON xod.food_id = xf.id "
					+" WHERE  1 = 1   GROUP BY food_id ";*/
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT");
		sql.append(" SUM(xod.food_num) food_num,");
		sql.append(" SUM(xod.food_num*xf.sell_price) total,");
		sql.append(" xod.food_id, ");
		sql.append(" xod.pay_price, ");
		sql.append(" xod.ins_time, ");
		sql.append(" xf.food_name, ");
		sql.append(" xf.sell_price ");
		sql.append(" FROM");
		sql.append(" x_order_detail xod");
		sql.append(" LEFT JOIN x_food xf ON xod.food_id = xf.id");
		sql.append(" LEFT JOIN x_orders xs ON xs.id = xod.order_id");
		sql.append(" WHERE 1=1");
		sql.append(" and xs.del_flag = '0' and xf.ins_user='"+xuser.getUserName()+"'");
		if(startTime !=null && startTime !=""){
			//sql.append(" and xod.ins_time < DATE_FORMAT('"+startTime+"','%Y-%m-%d %h-%i-%s')");
			sql.append(" and xod.ins_time >= '"+startTime+"'");
		}
		if(endTime !=null && endTime != ""){
			sql.append(" and  xod.ins_time <= '"+endTime+"'");
		}
		sql.append(" GROUP BY food_id");
		System.out.println(sql.toString());
		filter.setBaseSql(sql.toString());
		List<Map> list = this.query(filter);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> getRechargeObj(SqlQueryFilter filter, String flag,
			String deptId,String companyId, String str, String otherCompanyId) {
		
		StringBuffer sql = new StringBuffer();
		
		//部门
		if(flag.equals("1")){
			sql.append("select 1 flag,xd.dept_id id,xd.dept_name,xds.dept_sum,xy.company_name " +
					"from x_dept xd left join x_dept_sum xds on xd.dept_id = xds.dept_id  " +
					" left join x_company xy on  xy.company_id=  xd.company_id" +
					" where 1 = 1 ");
			
			if(StringUtil.isNotEmpty(deptId)){
				sql.append(" and xd.dept_id = '"+deptId+"' ");
			}
			
			if(StringUtil.isNotEmpty(companyId)){
				sql.append(" and (xd.company_id = '"+companyId+"' ");
				//zhangxuejiao编写
				if(StringUtil.isNotEmpty(otherCompanyId)){
					sql.append(" or xd.company_id = '"+otherCompanyId+"') ");
				}else{
					sql.append(")");
				}
			}
			
		}else if(flag.equals("2")){
			  
			sql.append(" SELECT  2 flag,xu.user_id id,xd.dept_name,xu.real_name,xu.jobtitle,balance,xy.company_name");
			sql.append(" FROM x_user xu  ");
			sql.append(" LEFT JOIN x_dept xd ON xu.dept_id = xd.dept_id  ");
			sql.append(" LEFT JOIN x_staff_sum xs ON xu.user_id = xs.user_id ");
			sql.append("  left join x_company xy on  xy.company_id=  xd.company_id    ");
			sql.append(" WHERE  xu.type='1'  AND xu.del_flag = '0' ");

			if(StringUtil.isNotEmpty(deptId)){
				sql.append(" AND xd.dept_id = '"+deptId+"' ");
			}
			
			if(StringUtil.isNotEmpty(companyId)){
				sql.append(" and (xd.company_id = '"+companyId+"' ");
				//zhangxuejiao编写
				if(StringUtil.isNotEmpty(otherCompanyId)){
					sql.append(" or xd.company_id = '"+otherCompanyId+"') ");
				}else{
					sql.append(")");
				}
			}
			
			if(StringUtil.isNotEmpty(str)){
				sql.append("   AND (user_name LIKE '%"+str+"%' OR real_name LIKE '%"+str+"%' OR tel LIKE '%"+str+"%') ");
			}
			
		}
		
		filter.setBaseSql(sql.toString());
		List<Map> list = this.query(filter);
		if(ListUtil.isNotEmpty(list)){
			if(flag.equals("1")){
				String deptSpecSql = "";
				String ids = "";
				for (Map map : list) {
					if(map.get("id") != null){
						ids += map.get("id")+",";
					}
				}
				if(StringUtil.isNotEmpty(ids)){
					ids =  ids.substring(0,ids.lastIndexOf(","));
					deptSpecSql  = "SELECT xu.dept_id,xu.real_name FROM x_user xu  LEFT JOIN x_user_role xur ON xur.user_id = xu.user_id   WHERE xu.dept_id IN ("+ids+") AND xur.role_id = '"+Constants.DEPT_ONLY_ID+"'";
					List<Map> mapList = this.getPubDao().findBySqlToMap(deptSpecSql);
					
					for (Map map : list) {
						if(ListUtil.isNotEmpty(mapList)){
							for (Map m : mapList) {
								if(map.get("id").equals(m.get("dept_id"))){
									map.put("specname", m.get("real_name"));
								}
							}
						}
					}
					
				}
			}
			return list;
		}
		return null;
	}

	@Override
	public List<Map> getDept() {
		String sql = "select * from  x_dept";
		List<Map> list = this.getPubDao().findBySqlToMap(sql);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}

	@Override
	public String getBalanceById(String flag, String id) {
		String sql = "";
		if(flag.equals("1")){
			sql += "SELECT dept_sum money FROM x_dept_sum WHERE dept_id = '"+id+"'";
		}else{
			sql += "select balance  money from x_staff_sum where user_id='"+id+"'";
		}
		List<Map> list = this.getPubDao().findBySqlToMap(sql);
		if(ListUtil.isNotEmpty(list)){
			return list.get(0).get("money").toString();
		}
		return null;
	}

	@Override
	public void updBalance(String flag, String id,String money) {
		String sql = "";
		if(flag.equals("1")){  
			sql += " UPDATE x_dept_sum SET dept_sum = '"+money+"' WHERE dept_id = '"+id+"'";
		}else{
			sql += "update x_staff_sum set balance = '"+money+"' where user_id = '"+id+"'";
			
		}
		this.dao.executeSql(sql);
	}

	@Override
	public List<Map> getTreeData(String companyId,boolean isHaveStaff,String name,String otherCompanyId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select xc.company_id id,xc.company_name name,   parent_id   from x_company xc   ");
		if(StringUtil.isNotEmpty(companyId)){
			sb.append(" where xc.company_id = '"+companyId+"' or xc.parent_id ='"+companyId+"'");
			//zhangxuejiao编写--->下拉选显示其他的公司
			if(StringUtil.isNotEmpty(otherCompanyId)){
				sb.append(" or xc.company_id = '"+otherCompanyId+"'");
			}
		}
		List<Map> companylist = this.getPubDao().findBySqlToMap(sb.toString());
//		StringBuffer sb1 = new StringBuffer();
//		sb1.append("select xd.company_id parent_id ,xd.dept_name name ,xd.dept_id id from x_dept xd where xd.supdepid = '0'");
//		if(StringUtil.isNotEmpty(companyId)){
//			sb1.append(" and xd.company_id ='"+companyId+"'");
//		}
//		List<Map> deptList = this.getPubDao().findBySqlToMap(sb1.toString());
		StringBuffer sb2 = new StringBuffer();
		sb2.append("select xd.dept_name name,xd.supdepid parent_id ,xd.dept_id id from x_dept xd ");
		sb2.append("  where xd.supdepid != '0' ");
		if(StringUtil.isNotEmpty(companyId)){
			sb2.append(" and xd.company_id ='"+companyId+"'");
			
		}
		List<Map> childDeptList = this.getPubDao().findBySqlToMap(sb2.toString());
		StringBuffer sb4 = new StringBuffer();
		sb4.append("select xd.company_id parent_id ,xd.dept_name name ,xd.dept_id id from x_dept xd ");
		sb4.append(" left join x_company xc on xc.company_id = xd.company_id");
		sb4.append(" where xd.supdepid = '0' ");
		if(StringUtil.isNotEmpty(companyId)){
			sb4.append(" and xc.company_id='"+companyId+"' or xc.parent_id ='"+companyId+"'");
			//zhangxuejiao编写--->下拉选显示其他的公司
			if(StringUtil.isNotEmpty(otherCompanyId)){
				sb4.append(" or xc.company_id = '"+otherCompanyId+"' or xc.parent_id ='"+otherCompanyId+"'");
			}
		}
		List<Map> childComapnyDeptList = this.getPubDao().findBySqlToMap(sb4.toString());
		if(ListUtil.isNotEmpty(childComapnyDeptList)){
			for (int i = 0; i < childComapnyDeptList.size(); i++) {
				String val = childComapnyDeptList.get(i).get("id").toString();
				String valParent = childComapnyDeptList.get(i).get("parent_id").toString();
				childComapnyDeptList.get(i).put("id", "dept_"+val);
				childComapnyDeptList.get(i).put("parent_id", valParent);
			}	
		}
//		if(ListUtil.isNotEmpty(deptList)){
//			for (int i = 0; i < deptList.size(); i++) {
//				String val = deptList.get(i).get("id").toString();
//				deptList.get(i).put("id", "dept_"+val);
//			}
//		}
		if(ListUtil.isNotEmpty(childDeptList)){
			for (int i = 0; i < childDeptList.size(); i++) {
				String val = childDeptList.get(i).get("id").toString();
				String valParent = childDeptList.get(i).get("parent_id").toString();
				childDeptList.get(i).put("id", "dept_"+val);
				childDeptList.get(i).put("parent_id", "dept_"+valParent);
			}
		}
		if(isHaveStaff){
			StringBuffer sb3 = new StringBuffer();
			sb3.append(" select real_name name,user_id id,dept_id parent_id from x_user where type='1' ");
			if(StringUtil.isNotEmpty(companyId)){
				sb3.append(" and company_id ='"+companyId+"'");
				
			}
			if(StringUtil.isNotEmpty(name)){
				sb3.append(" and real_name  like '%"+companyId+"'%");
				
			}
			
			List<Map> staffList = this.getPubDao().findBySqlToMap(sb3.toString());
			if(ListUtil.isNotEmpty(staffList)){
				for (int i = 0; i < staffList.size(); i++) {
					String val = staffList.get(i).get("id").toString();
					String valParent = staffList.get(i).get("parent_id").toString();
					staffList.get(i).put("id", "staff_"+val);
					staffList.get(i).put("parent_id", "dept_"+valParent);
				}
			}
			companylist.addAll(staffList);
		}
//		companylist.addAll(deptList);
		companylist.addAll(childDeptList);
		companylist.addAll(childComapnyDeptList);
		return companylist;
	}

	@Override
	public List<Map> getDeptTree(String companyId) {
//		String sql =  " SELECT  xc.company_id,xc.company_name, 0 pid,1 isCompany "
//					  +" FROM x_company xc WHERE xc.parent_id = '"+companyId+"'"
//					  +" UNION ALL"
// 					  +" SELECT xd.dept_id,xd.dept_name,0 parent_id ,0 isCompany FROM x_dept xd WHERE xd.supdepid = '0' AND xd.company_id = '"+companyId+"'";
		String sql = "SELECT xd.dept_id,xd.dept_name,xd.supdepid FROM x_dept xd WHERE xd.company_id = '"+companyId+"'";
		List<Map> list = this.getPubDao().findBySqlToMap(sql);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}
	
	@Override
	public List<MyFood> getDetailedOrder(String startTime, String endTime, XUser foodBusiness) {
		List<MyFood> listAll = new ArrayList<MyFood>();
		StringBuffer sql  = new StringBuffer();
		//查询当前套餐
		sql.append(" select xs.for_who_id, xt.dept_id, xs.user_id, xs.ins_time as effective_date, xs.rec_id,xf.food_name,xf.ins_user,xf.food_desc,sum(xd.food_num) as food_num1,xf.id,xt.dept_name,xy.company_name, ");
		
		sql.append("  case when xu.temp_room_num is not null then xu.temp_room_num else xt.room_num end room_num ");
		
		sql.append("  from x_orders xs ");
		sql.append(" LEFT JOIN x_order_detail xd on xs.id = xd.order_id");
		sql.append(" LEFT JOIN x_orders x ON x.id = xd.order_id");
		sql.append(" LEFT JOIN x_food xf on xd.food_id =xf.id");
		
		sql.append(" LEFT JOIN x_user xu ON xu.user_id = x.user_id");
		sql.append(" LEFT JOIN x_dept xt ON xt.dept_id = xu.dept_id");
		
		sql.append(" LEFT JOIN x_company xy ON xy.company_id = xt.company_id");
		sql.append(" where 1=1");
		sql.append(" AND x.del_flag = '0'");
		sql.append(" AND x.order_category != 'XC'"); //非现场订单
		sql.append(" AND xf.ins_user = '"+foodBusiness.getUserName()+"'");
		//sql.append("#xf.user_id = '1'");
		if(startTime !=null && startTime !=""){
			sql.append(" and xd.ins_time >='"+startTime+"' ");
		}
		if(endTime !=null && endTime !=""){
			
			sql.append(" and xd.ins_time <='"+endTime+"'");
		}
		//sql.append(" GROUP BY xt.company_id,xt.dept_id,xf.id ,xs.rec_id");
		if(foodBusiness.getWhetherSendStatus().equals("0")){ //支持派送
			sql.append(" GROUP BY xt.company_id,xt.dept_id,xf.id,xs.rec_id");
		}else{
			sql.append(" GROUP BY xt.company_id,xt.dept_id,xf.id");
		}
		
		//System.out.println("===="+sql.toString());
		List<Map> list = this.getPubDao().findBySqlToMap(sql.toString());
		if(ListUtil.isNotEmpty(list)){
			for (Map map : list) {
				MyFood myFood = new MyFood();
				myFood.setFoodName((String) map.get("food_name"));
				myFood.setFoodDesc((String) map.get("food_desc"));
				
				if(map.get("dept_name") != null){
					myFood.setDeptName((String) map.get("dept_name"));
				}else{
					String hql = "from XDept where deptId = ?";
					Object[] objs = {(String) map.get("for_who_id")};
					List<XDept> xDeptList = xDeptDao.findByHql(hql, objs);
					if(xDeptList != null && xDeptList.size() > 0){
						myFood.setDeptName(xDeptList.get(0).getDeptName());
					}
				}
				myFood.setEffectiveDate(map.get("effective_date").toString().split(" ")[0]);
				int num =  Integer.parseInt(map.get("food_num1").toString());
				myFood.setFoodNum(num);
				myFood.setCompanyName((String) map.get("company_name"));
				if(StringUtils.isNotBlank((String)map.get("rec_id"))){
					XFoodSendAddress xFoodSendAddress = xFoodSendAddressService.get((String) map.get("rec_id"));
					XFoodSendRegion xFoodSendRegion = xFoodSendRegionService.get(xFoodSendAddress.getRegionId());
					myFood.setRoomNum(xFoodSendRegion.getRegionName()+" "+xFoodSendAddress.getPark()+" "+xFoodSendAddress.getHighBuilding()+" "+xFoodSendAddress.getUnit()+" "+xFoodSendAddress.getRoomNum());
				}
				
				//String userId = (String) map.get("for_who_id");
				String id = (String) map.get("id");
				String deptId = (String) map.get("dept_id");
				String recId = (String)map.get("rec_id");
				
				
				String su = "select xs.rec_id, xs.vistor_name, xs.for_who_id, xs.user_id, xu.real_name, " +
							" xt.dept_name from  x_food xf " +
							" LEFT JOIN x_order_detail xd ON xd.food_id = xf .id" +
							" LEFT JOIN x_orders xs on xs.id = xd.order_id" +
							" LEFT JOIN x_user xu on xu.user_id = xs.user_id" +
							" LEFT JOIN x_dept xt ON xu.dept_id = xt.dept_id" +
							" where  xf.id ='"+id+"'" +
							" and xs.del_flag='0'"+
							" and xt.dept_id = '"+deptId+"'" ;
				
				if(foodBusiness.getWhetherSendStatus().equals("0")){ //支持派送
					//收货地址
					if(StringUtils.isNotBlank(recId)){
						su+=" and xs.rec_id ='"+recId+"'";
					}
				}
										
				if(startTime !=null && startTime !=""){
					su+=" and xd.ins_time >='"+startTime+"' ";
				}
				if(endTime !=null && endTime !=""){
					
					su+=" and xd.ins_time <='"+endTime+"'";
				}
				su+=" GROUP BY xs.vistor_name" ;
				//System.out.println("===="+su);
				List<Map> listUser = this.getPubDao().findBySqlToMap(su);
				//String depName = null;
				if(ListUtil.isNotEmpty(listUser)){
					List<MyFoodUser> myFoodUsers = new ArrayList<MyFoodUser>();
					for (Map map3 : listUser) {
						MyFoodUser myFoodUser = new MyFoodUser();
						if(!(map3.get("for_who_id").toString()).equals(map3.get("user_id").toString())){
							myFoodUser.setRealName((String) map3.get("real_name")+"(为"+(String) map3.get("vistor_name")+")");
						}else{
							myFoodUser.setRealName((String) map3.get("vistor_name"));
						}
						myFoodUsers.add(myFoodUser);
						myFood.setListUser(myFoodUsers);
					}
				}
			/*	String sw = " select xr.id, xl.user_id ,xr.role_name from  x_user_role  xl" +
					    " LEFT JOIN x_role xr on xr.id = role_id" +
					    " LEFT JOIN x_user xu ON xu.user_id = xl.user_id" +
					    " where xu.dept_id = '"+deptId+"'";*/
				String sw =" select xl.user_id from x_role xr " +
						"  LEFT JOIN x_user_role xl ON xr.id = role_id" +
						" LEFT JOIN x_user xu ON xl.user_id = xu.user_id" +
						" 	where xr.id = '3'" +
						"  and xu.dept_id = '"+deptId+"' ";
				List<Map> list2 = this.getPubDao().findBySqlToMap(sw);
				
				if(ListUtil.isNotEmpty(list2)){
					for (Map map2 : list2) {
						String userId2 = (String) map2.get("user_id");
							String shql = "from XUser where userId='"+userId2+"'";
							List<XUser> xdList = this.getPubDao().findByHql(shql);
							if(ListUtil.isNotEmpty(xdList)){
								XUser user = xdList.get(0);
								myFood.setDeptRoleName(user.getRealName());
								myFood.setTel(user.getTel());
							}
					}
				}
				listAll.add(myFood);
			}
			
		}
		return listAll;	
	}

	@Override
	public boolean chargeAll(String ids, String flag, String balance,String insUser) {
		boolean boo = true;
		try{
			if(StringUtil.isNotEmpty(ids) && StringUtil.isNotEmpty(flag) ){
				String allIds [] = ids.split(",");
				XDetailRecord xDetailRecord;
				for (int i = 0; i < allIds.length; i++) {
					String	id = allIds[i];
					xDetailRecord = new XDetailRecord();
					
					if(Double.valueOf(balance)>=0){
						xDetailRecord.setType("0"); //0：收入 1：支出
					}else{
						xDetailRecord.setType("1");
					}
								
					xDetailRecord.setChangeMoney(balance);	//交易金额
					xDetailRecord.setRemark("全部充值");			//备注
					xDetailRecord.setInsTime(new Date()); 	//插入时间
					xDetailRecord.setInsUser(insUser);//插入者
					//获取当前余额
					String currentBalance = this.getBalanceById(flag, id);
					//充值后余额
					int afterCharge = Integer.parseInt(currentBalance==null?"0":currentBalance) + Integer.parseInt((balance));
					xDetailRecord.setBalance(afterCharge+"");//余额
					if(flag.equals("1")){
						xDetailRecord.setDeptId(id);
					}else if(flag.equals("2")){
						xDetailRecord.setUserId(id);
					}
					//添加记录
					xDetailRecordService.save(xDetailRecord);
					//更新余额
					this.updBalance(flag, id, afterCharge+"");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			boo = false;
			// TODO: handle exception
		}
		
		return boo;
	}
	
	@Override
	public List<MyAccount> getDetailedLedger(String startTime, String endTime,XUser xuser) {
		List<MyAccount> listAll = new ArrayList<MyAccount>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT");
		sql.append(" SUM(xod.food_num) food_num,");
		sql.append(" SUM(xod.food_num*xf.sell_price) total,");
		sql.append(" xod.food_id,");
		sql.append(" xod.ins_time,");
		sql.append(" xf.food_name,");
		sql.append(" xf.sell_price,");
		sql.append(" xd.dept_name,");
		sql.append(" xd.dept_id,");
		sql.append(" xo.for_who_id,");
		sql.append(" xf.id,xy.company_name");
		sql.append(" FROM");
		sql.append(" x_order_detail xod");
		sql.append(" LEFT JOIN x_food xf ON xod.food_id = xf.id");
		sql.append(" LEFT JOIN x_orders xo on xo.id = xod.order_id");
		sql.append(" LEFT JOIN x_user xu on xu.user_id = xo.for_who_id");
		sql.append(" LEFT JOIN x_dept xd on xd.dept_id = xu.dept_id");
		sql.append(" LEFT JOIN x_company xy ON xy.company_id = xd.company_id");
		sql.append(" WHERE 1=1");
		sql.append(" and xo.del_flag = '0' and xf.ins_user='"+xuser.getUserName()+"'");
		if(startTime !="" && startTime !=null){
			sql.append(" AND xod.ins_time >= '"+startTime+"'");
		}
		if(endTime !="" && endTime !=null){
			
			sql.append(" AND xod.ins_time <= '"+endTime+"'");
		}
		sql.append(" GROUP BY xy.company_id,xf.id,xd.dept_id");
		sql.append(" order by xf.id,xy.company_id,xd.dept_id");
		
		List<Map> list = this.getPubDao().findBySqlToMap(sql.toString());
		int sumTotal = 0;
		int sumNum = 0;
		//int bNum =0;
		int deTotal =0;
		if(ListUtil.isNotEmpty(list)){
			if(ListUtil.isNotEmpty(list)){
					for (Map map4 : list) {
						int num = Integer.parseInt(map4.get("food_num").toString());
						sumNum+=num;
						int  total  = Integer.parseInt(map4.get("total").toString());
						sumTotal +=total;
					}
					
					for (Map map : list) {
						MyAccount myAccount = new MyAccount();
						int num = Integer.parseInt(map.get("food_num").toString());
						int  total  = Integer.parseInt(map.get("total").toString());
						String deptId = (String) map.get("dept_id");
						String id = (String) map.get("id");
						//sumNum+=num;
						//sumTotal+=total;
						myAccount.setFoodName((String) map.get("food_name"));//套餐名称
						if(map.get("dept_name") != null){
							myAccount.setDeptName((String) map.get("dept_name"));
						}else{
							String hql = "from XDept where deptId = ?";
							Object[] objs = {(String) map.get("for_who_id")};
							List<XDept> xDeptList = xDeptDao.findByHql(hql, objs);
							if(xDeptList != null && xDeptList.size() > 0){
								myAccount.setDeptName(xDeptList.get(0).getDeptName());
							}
						}
						myAccount.setFoodNum(num);//套餐数
						myAccount.setCompanyName((String) map.get("company_name"));
						myAccount.setTotal(total);
						//myAccount.setSumNum(sumNum);//总
						//myAccount.setDeptTotal(deTotal);
						myAccount.setSumTotal(sumTotal);
						String sql2 = "select  SUM(xod.food_num) deptfood_num," +
								" SUM(xod.food_num*xf.sell_price) depttotal" +
								" from x_order_detail xod " +
								" LEFT JOIN x_food xf ON xod.food_id = xf.id" +
								" LEFT JOIN x_orders xo ON xo.id = xod.order_id" +
								" LEFT JOIN x_user xu ON xu.user_id = xo.user_id" +
								" LEFT JOIN x_dept xd ON xd.dept_id = xu.dept_id" +
								" where xf.id  = '"+id+"' " +
								" and xo.del_flag = '0'  and xf.ins_user='"+xuser.getUserName()+"' ";
						if(startTime !="" && startTime !=null){
							sql2 += " AND xod.ins_time >= '"+startTime+"'";
						}
						if(endTime !="" && endTime !=null){
							
							sql2 += " AND xod.ins_time <= '"+endTime+"'";
						}
						sql2 += " GROUP BY xf.id";
						
						List<Map> list2 = this.getPubDao().findBySqlToMap(sql2.toString());
						if(ListUtil.isNotEmpty(list2)){
							for (Map map3 : list2) {
								int bNum  = Integer.parseInt(map3.get("depttotal").toString());
								int foodNum  = Integer.parseInt(map3.get("deptfood_num").toString());
								myAccount.setSumNum(foodNum);//总
								myAccount.setDeptTotal(bNum);
							}
						}
						listAll.add(myAccount);
					}
			}
		}
		return listAll;	
		
	}

	@Override
	public XAppointRelation saveXAppointRelation(XAppointRelation xAppointRelation) {
		String hql = " from XAppointRelation xar where xar.leadId ='"+xAppointRelation.getLeadId()+"'";
		List<XAppointRelation> list = this.getPubDao().findByHql(hql);
		if(ListUtil.isNotEmpty(list)){
			XAppointRelation xapRelation = list.get(0);
			xAppointRelation.setUpdTime(new Date());
			xAppointRelation.setUpdUser(xAppointRelation.getInsUser());
			try {
				BeanUtil.copyNotNullProperties(xapRelation, xAppointRelation);
				return relationDao.save(xapRelation);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		xAppointRelation.setInsTime(new Date());
		return relationDao.save(xAppointRelation);
	}

	@Override
	public void delXAppointRelationById(String id) {
		relationDao.remove(id);
	}

	@Override
	public List<MyOrder> getMyOrder(String loginUserId) throws Exception{
		List<MyOrder> orderList = null;
		MyOrder myOrder = null;
		try{
			
			StringBuffer orderSql = new StringBuffer();
			orderSql.append("  SELECT xo.id as order_id, xo.order_type, xo.for_who_id,xo.id,xo.flag, xo.rec_id, date_format(xo.ins_time, '%Y-%m-%d %H:%i:%s' )  ordertime,  ");
			orderSql.append("  CASE flag   ");
			orderSql.append("  WHEN '1'  THEN (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '3' THEN (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '2' THEN (SELECT xd.dept_name FROM x_dept xd WHERE xd.dept_id = xo.for_who_id )  ");
			orderSql.append("  END orderobj,  ");
			orderSql.append("  CASE flag   ");
			orderSql.append("  WHEN '1'  THEN (SELECT xss.balance FROM x_staff_sum  xss WHERE xss.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '3' THEN (SELECT xss.balance FROM x_staff_sum  xss WHERE xss.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '2' THEN (SELECT xds.dept_sum FROM x_dept_sum xds WHERE xds.dept_id = xo.for_who_id )  ");
			orderSql.append("  END objbalance  ");
			orderSql.append("  FROM x_orders xo   ");
			orderSql.append("  WHERE xo.user_id = '"+loginUserId+"'  ");
			orderSql.append("  and xo.ins_time <= now()");
			orderSql.append("  and xo.ins_time >= '"+DateUtil.getLastMonth2()+"'");
			//查询条件：非产品
			orderSql.append("  and xo.order_type != 'PRO'");
			orderSql.append("  and xo.order_category = 'ZC'"); //正常订餐
			orderSql.append("  and xo.del_flag ='0' order by ordertime desc");
			
			List<Map> l = this.getPubDao().findBySqlToMap(orderSql.toString());
			if(ListUtil.isNotEmpty(l)){
				orderList = new ArrayList<MyOrder>();
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar now = Calendar.getInstance();  
			    String dayTime =  now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH);
			    Date aMStart = formatter.parse((dayTime+" "+Constants.AMSTART)); //上开
			    Date aMSEnd = formatter.parse((dayTime+" "+Constants.AMEND)); //上结
			    Date pMStart = formatter.parse((dayTime+" "+Constants.PMSTART)); //下开
			    Date pMEnd= formatter.parse((dayTime+" "+Constants.PMEND)); //下结
				for (Map map : l) {
					myOrder = new MyOrder();
					//add dingzhj at date 2017-02-22  添加时间判断  START
					//获取订单时间
					String insTime =map.get("ordertime").toString();
					Date insDate = formatter.parse(insTime);
					String  orderType = (String) map.get("order_type");
					//判断当前时间类型（上午，下午）
					if(orderType.equals(Constants.LUN)){//上午
						if(aMStart.getTime() <=insDate.getTime() && insDate.getTime() <= aMSEnd.getTime()){
							if(aMStart.getTime() <=date.getTime() && date.getTime() <= aMSEnd.getTime()){
								myOrder.setIsShow("ON");
							}
						}else{
							//myOrder.setIsShow("OFF");
						}
					}else if(orderType.equals(Constants.DIN)){//下午
						if(pMStart.getTime() <=insDate.getTime() && insDate.getTime() <= pMEnd.getTime()){
							if(pMStart.getTime() <=date.getTime() && date.getTime() <= pMEnd.getTime()){
								myOrder.setIsShow("ON");
							}
						}else{
							//myOrder.setIsShow("OFF");
						}
					}
					
					myOrder.setBalance((String) map.get("objbalance"));
					myOrder.setOrderObj((String) map.get("orderobj"));
					myOrder.setOrderTime(map.get("ordertime").toString());
					myOrder.setId((String) map.get("id"));
					myOrder.setFlag((String) map.get("flag"));
					myOrder.setId((String) map.get("id"));
					myOrder.setOrderId((String) map.get("order_id"));
					myOrder.setForWhoId((String) map.get("for_who_id"));
					myOrder.setOrderType((String) map.get("order_type"));
					String setFoodNameStr = "";
					Integer setFoodTypeNum = 0;
					Integer orderNum=0;
					String id = (String) map.get("id");
					StringBuffer detailSql = new StringBuffer();
					detailSql.append("  SELECT xod.food_num,xod.pay_price,xf.food_name,xf.food_desc,xf.sell_price,xf.food_img,xf.id FROM x_order_detail xod  ");
					detailSql.append("  LEFT JOIN x_food xf ON xf.id = xod.food_id   ");
					detailSql.append("  WHERE order_id = '"+map.get("id").toString()+"'"    );
					List<Map> detailMap = this.getPubDao().findBySqlToMap(detailSql.toString());
					
					//订单总价
					Integer total = 0;
					if(ListUtil.isNotEmpty(detailMap)){
						for (Map m : detailMap) {
							setFoodNameStr += m.get("food_name") + ",";
							setFoodTypeNum +=Integer.valueOf(m.get("food_num").toString());
							orderNum +=setFoodTypeNum;
							
							total += Integer.parseInt(m.get("food_num").toString()) * Integer.parseInt(m.get("pay_price").toString());
							
							//获取餐饮所属的餐饮公司
							XFood xFood = xFoodService.get(m.get("id").toString());
							if(xFood != null){
								XUser foodBusiness = xUserService.findFoodBusinessByUsername(xFood.getInsUser());
								myOrder.setxUser(foodBusiness);
							}
							
						}
					}
					
					myOrder.setDetailList(detailMap);
					myOrder.setFoodNameStr(setFoodNameStr==""?"":setFoodNameStr.substring(0, setFoodNameStr.lastIndexOf(",")));
					myOrder.setFoodTypeNum(setFoodTypeNum.toString());
					myOrder.setMoenyTotal(total.toString());
					myOrder.setOrdeNum(orderNum.toString());
					
					if(map.get("rec_id") != null){
						//收货人信息
						XFoodSendAddress xFoodSendAddress = xFoodSendAddressService.get((String)map.get("rec_id"));
						if(xFoodSendAddress != null){
							XFoodSendRegion xFoodSendRegion = xFoodSendRegionService.get(xFoodSendAddress.getRegionId());
							xFoodSendAddress.setRegionName(xFoodSendRegion.getRegionName());
							
							myOrder.setxFoodSendAddress(xFoodSendAddress);
						}
					}
					
					orderList.add(myOrder);
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("获取我的订单失败！");
		}
		if(ListUtil.isNotEmpty(orderList)){
			return orderList;
		}
		
		return null;
	}
	
	@Override
	public List<MyOrder> getMyLiveOrder(String loginUserId) throws Exception{
		List<MyOrder> orderList = null;
		MyOrder myOrder = null;
		try{
			
			StringBuffer orderSql = new StringBuffer();
			orderSql.append("  SELECT xo.id as order_id, xo.order_type, xo.for_who_id,xo.id,xo.flag, xo.rec_id, date_format(xo.ins_time, '%Y-%m-%d %H:%i:%s' )  ordertime,  ");
			orderSql.append("  CASE flag   ");
			orderSql.append("  WHEN '1'  THEN (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '3' THEN (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '2' THEN (SELECT xd.dept_name FROM x_dept xd WHERE xd.dept_id = xo.for_who_id )  ");
			orderSql.append("  END orderobj,  ");
			orderSql.append("  CASE flag   ");
			orderSql.append("  WHEN '1'  THEN (SELECT xss.balance FROM x_staff_sum  xss WHERE xss.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '3' THEN (SELECT xss.balance FROM x_staff_sum  xss WHERE xss.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '2' THEN (SELECT xds.dept_sum FROM x_dept_sum xds WHERE xds.dept_id = xo.for_who_id )  ");
			orderSql.append("  END objbalance  ");
			orderSql.append("  FROM x_orders xo   ");
			orderSql.append("  WHERE xo.user_id = '"+loginUserId+"'  ");
			orderSql.append("  and xo.ins_time <= now()");
			orderSql.append("  and xo.ins_time >= '"+DateUtil.getLastMonth2()+"'");
			//查询条件：非产品
			orderSql.append("  and xo.order_type != 'PRO'");
			orderSql.append("  and xo.order_category = 'XC'"); //现场订餐
			orderSql.append("  and xo.del_flag ='0' order by ordertime desc");
			List<Map> l = this.getPubDao().findBySqlToMap(orderSql.toString());
			if(ListUtil.isNotEmpty(l)){
				orderList = new ArrayList<MyOrder>();
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar now = Calendar.getInstance();  
			    String dayTime =  now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH);
			    Date aMStart = formatter.parse((dayTime+" "+Constants.LIVE_LUNSTART)); //上开
			    Date aMSEnd = formatter.parse((dayTime+" "+Constants.LIVE_LUNSEND)); //上结
			    Date pMStart = formatter.parse((dayTime+" "+Constants.LIVE_DINSTART)); //下开
			    Date pMEnd= formatter.parse((dayTime+" "+Constants.LIVE_DINEND)); //下结
				for (Map map : l) {
					myOrder = new MyOrder();
					//add dingzhj at date 2017-02-22  添加时间判断  START
					//获取订单时间
					String insTime =map.get("ordertime").toString();
					Date insDate = formatter.parse(insTime);
					String  orderType = (String) map.get("order_type");
					//判断当前时间类型（上午，下午）
					if(orderType.equals(Constants.LUN)){//上午
						if(aMStart.getTime() <=insDate.getTime() && insDate.getTime() <= aMSEnd.getTime()){
							if(aMStart.getTime() <=date.getTime() && date.getTime() <= aMSEnd.getTime()){
								myOrder.setIsShow("ON");
							}
						}else{
							//myOrder.setIsShow("OFF");
						}
					}else if(orderType.equals(Constants.DIN)){//下午
						if(pMStart.getTime() <=insDate.getTime() && insDate.getTime() <= pMEnd.getTime()){
							if(pMStart.getTime() <=date.getTime() && date.getTime() <= pMEnd.getTime()){
								myOrder.setIsShow("ON");
							}
						}else{
							//myOrder.setIsShow("OFF");
						}
					}
					//add dingzhj at date 2017-02-22  添加时间判断  END
					myOrder.setBalance((String) map.get("objbalance"));
					myOrder.setOrderObj((String) map.get("orderobj"));
					myOrder.setOrderTime(map.get("ordertime").toString());
					myOrder.setId((String) map.get("id"));
					myOrder.setFlag((String) map.get("flag"));
					myOrder.setId((String) map.get("id"));
					myOrder.setOrderId((String) map.get("order_id"));
					myOrder.setForWhoId((String) map.get("for_who_id"));
					myOrder.setOrderType((String) map.get("order_type"));
					String setFoodNameStr = "";
					Integer setFoodTypeNum = 0;
					Integer orderNum=0;
					String id = (String) map.get("id");
					StringBuffer detailSql = new StringBuffer();
					detailSql.append("  SELECT xod.food_num,xod.pay_price,xf.food_name,xf.sell_price,xf.food_desc,xf.food_img,xf.id FROM x_order_detail xod  ");
					detailSql.append("  LEFT JOIN x_food xf ON xf.id = xod.food_id   ");
					detailSql.append("  WHERE order_id = '"+map.get("id").toString()+"'"    );
					List<Map> detailMap = this.getPubDao().findBySqlToMap(detailSql.toString());
					//订单总价
					Integer total = 0;
					if(ListUtil.isNotEmpty(detailMap)){
						for (Map m : detailMap) {
							setFoodNameStr += m.get("food_name") + ",";
							setFoodTypeNum +=Integer.valueOf(m.get("food_num").toString());
							orderNum +=setFoodTypeNum;
							
							total += Integer.parseInt(m.get("food_num").toString()) * Integer.parseInt(m.get("pay_price").toString());
							
							//获取餐饮所属的餐饮公司
							XFood xFood = xFoodService.get(m.get("id").toString());
							if(xFood != null){
								XUser foodBusiness = xUserService.findFoodBusinessByUsername(xFood.getInsUser());
								myOrder.setxUser(foodBusiness);
							}
							
						}
					}
					
					myOrder.setDetailList(detailMap);
					myOrder.setFoodNameStr(setFoodNameStr==""?"":setFoodNameStr.substring(0, setFoodNameStr.lastIndexOf(",")));
					myOrder.setFoodTypeNum(setFoodTypeNum.toString());
					myOrder.setMoenyTotal(total.toString());
					myOrder.setOrdeNum(orderNum.toString());
					
					orderList.add(myOrder);
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("获取我的订单失败！");
		}
		if(ListUtil.isNotEmpty(orderList)){
			return orderList;
		}
		
		return null;
	}

	@Override
	@Transactional 
	public Map<String, Object> order(String type, XUser xuser,
			String orderId, List<Map> map, int z_num,String vistorName,String recId) {
		 Map<String, Object> msgMap = new HashMap<String, Object>();
		 int sum = z_num;
		 
		 //modify by dingzhj at date 2017-0317
		 SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date now = new Date();
		//周几
		String weekKey = "";
		//订餐类型
		String foodType = "";
		String week = DateUtil.getWeek(now);
		Long currentTime = now.getTime();
		Long lunStart = DateUtil.getAppointDate(Constants.LUNSTART).getTime();
		Long lunEnd = DateUtil.getAppointDate(Constants.LUNSEND).getTime();
		Long dinStart = DateUtil.getAppointDate(Constants.DINSTART).getTime();
		Long dinEnd = DateUtil.getAppointDate(Constants.DINEND).getTime();
		if(lunStart <= currentTime && lunEnd >= currentTime){
			weekKey = week;
			foodType =  Constants.LUN;
		}else if(dinStart <= currentTime && dinEnd >= currentTime){
			weekKey = week;
			foodType =  Constants.DIN;
		}
		 if("0".equals(type)||"2".equals(type)){
			// XStaffSum xStaffSum = xStaffSumDao.get(orderId);
			 XStaffSum xStaffSum =null;
			 String hql = "from XStaffSum where userId = '"+orderId+"'";
			 List<XStaffSum> list = this.getPubDao().findByHql(hql);
			 if(ListUtil.isNotEmpty(list)){
				 xStaffSum = list.get(0);
			 }
			 if(xStaffSum!=null){
				 XDetailRecord xDetailRecord = new XDetailRecord();
				 int balance = Integer.parseInt(xStaffSum.getBalance());//当前
				 if(balance>=sum){
					 balance = balance-sum;
					 xStaffSum.setBalance(balance+"");
					 xStaffSum.setUpdTime(now);
					 xStaffSum.setUpdUser(xuser.getUserName());
					 xStaffSumDao.save(xStaffSum);//更新余额
					 xDetailRecord.setBalance(balance+"");
					 xDetailRecord.setChangeMoney(sum+"");
					 xDetailRecord.setType(Constants.PAY);
					 xDetailRecord.setDeptId(xuser.getDeptId());
					 xDetailRecord.setUserId(orderId);
					 //int i = 5/0;
					 xDetailRecord.setRemark("订餐消费");
					 xDetailRecord.setInsTime(now);
					 xDetailRecord.setInsUser(xuser.getUserName());
					 xDetailRecordDao.save(xDetailRecord);//添加余额操作记录
					 XOrders xOrders =new XOrders();
					 if("0".equals(type)){
						 xOrders.setFlag("1");
					 }else if("2".equals(type)){
						 xOrders.setFlag("3");
					 }
					 if(vistorName != null && vistorName!=""){
						 xOrders.setVistorName(vistorName);
					 }
					 xOrders.setOrderType(foodType); //午餐还是晚餐
					 xOrders.setOrderCategory("ZC"); //订餐种类：正常订餐（ZC）预约订餐（YY）现场订餐（XC）
					 xOrders.setForWhoId(orderId);
					 xOrders.setUserId(xuser.getUserId());
					 xOrders.setInsTime(now);
					 xOrders.setInsUser(xuser.getUserName());
					 xOrders.setRecId(recId); //收餐地址id
					 dao.save(xOrders);//生成订单
					  for (Map map1:map) {
						  XOrderDetail xOrderDetail =new XOrderDetail();
						  xOrderDetail.setOrderId(xOrders.getId());
						  xOrderDetail.setInsTime(now);
						  xOrderDetail.setInsUser(xuser.getUserName());
						  xOrderDetail.setFoodId(map1.get("id").toString());
						  xOrderDetail.setFoodNum(Integer.parseInt(map1.get("num").toString()));
						  xOrderDetail.setPayPrice(Integer.parseInt(map1.get("price").toString())); //实付套餐价格
						  xOrderDetailDao.save(xOrderDetail);//保存订餐详情
						  //查询套餐库存
						  String num = getFoodBillByFoodId(map1.get("id").toString());
						  int food_remain = Integer.parseInt(num) -Integer.parseInt(map1.get("num").toString());
						  String sl = "UPDATE x_food_bill SET food_remain ='"+food_remain+"' where food_id = '"+map1.get("id").toString()+"'";
						  xFoodBillDao.executeSql(sl);//更新套餐库存
					   }
					  msgMap.put("status", "true"); 
					  msgMap.put("msg", "订餐成功"); 
				 }else{
					 msgMap.put("status", "false"); 
					 msgMap.put("msg", "账户余额不足,请联系管理员"); 
				 }
			 }else{
				 msgMap.put("status", "false");
				 msgMap.put("msg", "该账户余额不存在，请联系系统管理员");
			 }
		 }else if("1".equals(type)){
			 XDeptSum xDeptSum = null;
			 String hql = "from XDeptSum where deptId = '"+xuser.getDeptId()+"'";
			 List<XDeptSum> list = this.getPubDao().findByHql(hql);
			 if(ListUtil.isNotEmpty(list)){
				 xDeptSum = list.get(0);
				}
			 
			 if(xDeptSum!=null){
				 XDetailRecord xDetailRecord = new XDetailRecord();
				 int balance = xDeptSum.getDeptSum();
				 if(balance>=sum){
					 balance = balance-sum;
					 xDeptSum.setDeptSum(balance);
					 xDeptSum.setUpdTime(now);
					 xDeptSum.setUpdUser(xuser.getUserName());
					 xDeptSumDao.save(xDeptSum);//更新余额
					 xDetailRecord.setBalance(balance+"");
					 xDetailRecord.setChangeMoney(sum+"");
					 xDetailRecord.setType(Constants.PAY);
					 xDetailRecord.setDeptId(xDeptSum.getDeptId());
					 xDetailRecord.setRemark("订餐消费");
					 xDetailRecord.setInsTime(now);
					 xDetailRecord.setInsUser(xuser.getUserName());
					 xDetailRecordDao.save(xDetailRecord);//添加余额操作记录
					 XOrders xOrders =new XOrders();
					 xOrders.setFlag("2");
					 if(vistorName != null && vistorName!=""){
						 xOrders.setVistorName(vistorName);
					 }
					 xOrders.setOrderType(foodType); //午餐还是晚餐
					 xOrders.setOrderCategory("ZC"); //订餐种类：正常订餐（ZC）预约订餐（YY）现场订餐（XC）
					 xOrders.setForWhoId(xDeptSum.getDeptId());
					 xOrders.setUserId(xuser.getUserId());
					 xOrders.setInsTime(now);
					 xOrders.setInsUser(xuser.getUserName());
					 xOrders.setRecId(recId); //收餐地址id
					 dao.save(xOrders);//生成订单
					 for (Map map1:map) {
						  XOrderDetail xOrderDetail =new XOrderDetail();
						  xOrderDetail.setOrderId(xOrders.getId());
						  xOrderDetail.setInsTime(new Date());
						  xOrderDetail.setInsUser(xuser.getUserName());
						  xOrderDetail.setFoodId(map1.get("id").toString());
						  xOrderDetail.setFoodNum(Integer.parseInt(map1.get("num").toString()));
						  xOrderDetail.setPayPrice(Integer.parseInt(map1.get("price").toString())); //实付套餐价格
						  xOrderDetailDao.save(xOrderDetail);//保存订餐详情
						//查询套餐库存
						  String num = getFoodBillByFoodId(map1.get("id").toString());
						  int food_remain = Integer.parseInt(num) -Integer.parseInt(map1.get("num").toString());
						  String sl = "UPDATE x_food_bill SET food_remain ='"+food_remain+"' where food_id = '"+map1.get("id").toString()+"'";
						  xFoodBillDao.executeSql(sl);//更新套餐库存
					   }
					 msgMap.put("status", "true"); 
					  msgMap.put("msg", "订餐成功"); 
				 }else{
					 msgMap.put("status", "false"); 
					 msgMap.put("msg", "部门账户余额不足,请联系管理员"); 
				 }
				 
			 }
		 }
		 
		return msgMap;
	}
	
	@SuppressWarnings("unused")
	@Override
	@Transactional 
	public Map<String, Object> orderLive(String type, XUser xuser, String orderId, List<Map> map, int z_num,String vistorName) {
		
		Map<String, Object> msgMap = new HashMap<String, Object>();
		int sum = z_num;
		
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		//周几
		String weekKey = "";
		//订餐类型
		String foodType = "";
		String week = DateUtil.getWeek(now);
		Long currentTime = now.getTime();
		Long lunStart = DateUtil.getAppointDate(Constants.LIVE_LUNSTART).getTime();
		Long lunEnd = DateUtil.getAppointDate(Constants.LIVE_LUNSEND).getTime();
		Long dinStart = DateUtil.getAppointDate(Constants.LIVE_DINSTART).getTime();
		Long dinEnd = DateUtil.getAppointDate(Constants.LIVE_DINEND).getTime();
		if(lunStart <= currentTime && lunEnd >= currentTime){
			weekKey = week;
			foodType =  Constants.LUN;
		}else if(dinStart <= currentTime && dinEnd >= currentTime){
			weekKey = week;
			foodType =  Constants.DIN;
		}
		
		if("0".equals(type)||"2".equals(type)){
			// XStaffSum xStaffSum = xStaffSumDao.get(orderId);
			XStaffSum xStaffSum =null;
			String hql = "from XStaffSum where userId = '"+orderId+"'";
			List<XStaffSum> list = this.getPubDao().findByHql(hql);
			if(ListUtil.isNotEmpty(list)){
				xStaffSum = list.get(0);
			}
			 if(xStaffSum!=null){
				 XDetailRecord xDetailRecord = new XDetailRecord();
				 int balance = Integer.parseInt(xStaffSum.getBalance());//当前
				 if(balance>=sum){
					 balance = balance-sum;
					 xStaffSum.setBalance(balance+"");
					 xStaffSum.setUpdTime(now);
					 xStaffSum.setUpdUser(xuser.getUserName());
					 xStaffSumDao.save(xStaffSum);//更新余额
					 xDetailRecord.setBalance(balance+"");
					 xDetailRecord.setChangeMoney(sum+"");
					 xDetailRecord.setType(Constants.PAY);
					 xDetailRecord.setDeptId(xuser.getDeptId());
					 xDetailRecord.setUserId(orderId);
					 //int i = 5/0;
					 xDetailRecord.setRemark("订餐消费");
					 xDetailRecord.setInsTime(now);
					 xDetailRecord.setInsUser(xuser.getUserName());
					 xDetailRecordDao.save(xDetailRecord);//添加余额操作记录
					 XOrders xOrders =new XOrders();
					 if("0".equals(type)){
					 xOrders.setFlag("1");
					 }else if("2".equals(type)){
					 xOrders.setFlag("3");
					 }
					 if(vistorName != null && vistorName!=""){
						 xOrders.setVistorName(vistorName);
					 }
					 xOrders.setOrderType(foodType); //午餐还是晚餐
					 xOrders.setOrderCategory("XC"); //订餐种类：正常订餐（ZC）预约订餐（YY）现场订餐（XC）
					 xOrders.setForWhoId(orderId);
					 xOrders.setUserId(xuser.getUserId());
					 xOrders.setInsTime(now);
					 xOrders.setInsUser(xuser.getUserName());
					 dao.save(xOrders);//生成订单
					 for (Map map1:map) {
						 XOrderDetail xOrderDetail =new XOrderDetail();
						 xOrderDetail.setOrderId(xOrders.getId());
						 xOrderDetail.setInsTime(now);
						 xOrderDetail.setInsUser(xuser.getUserName());
						 xOrderDetail.setFoodId(map1.get("id").toString());
						 xOrderDetail.setFoodNum(Integer.parseInt(map1.get("num").toString()));
						 xOrderDetail.setPayPrice(Integer.parseInt(map1.get("price").toString())); //实付套餐价格
						 xOrderDetailDao.save(xOrderDetail);//保存订餐详情
					 }
					 msgMap.put("status", "true"); 
					 msgMap.put("msg", "订餐成功"); 
				 }else{
					 msgMap.put("status", "false"); 
					 msgMap.put("msg", "账户余额不足,请联系管理员"); 
				 }
			 }else{
				 msgMap.put("status", "false");
				 msgMap.put("msg", "该账户余额不存在，请联系系统管理员");
			 }
		 }else if("1".equals(type)){
			 XDeptSum xDeptSum = null;
			 String hql = "from XDeptSum where deptId = '"+xuser.getDeptId()+"'";
			 List<XDeptSum> list = this.getPubDao().findByHql(hql);
			 if(ListUtil.isNotEmpty(list)){
				xDeptSum = list.get(0);
			 }
			 
			 if(xDeptSum!=null){
				 XDetailRecord xDetailRecord = new XDetailRecord();
				 int balance = xDeptSum.getDeptSum();
				 if(balance>=sum){
					 balance = balance-sum;
					 xDeptSum.setDeptSum(balance);
					 xDeptSum.setUpdTime(now);
					 xDeptSum.setUpdUser(xuser.getUserName());
					 xDeptSumDao.save(xDeptSum);//更新余额
					 xDetailRecord.setBalance(balance+"");
					 xDetailRecord.setChangeMoney(sum+"");
					 xDetailRecord.setType(Constants.PAY);
					 xDetailRecord.setDeptId(xDeptSum.getDeptId());
					 xDetailRecord.setRemark("订餐消费");
					 xDetailRecord.setInsTime(now);
					 xDetailRecord.setInsUser(xuser.getUserName());
					 xDetailRecordDao.save(xDetailRecord);//添加余额操作记录
					 XOrders xOrders =new XOrders();
					 xOrders.setFlag("2");
					 if(vistorName != null && vistorName!=""){
						 xOrders.setVistorName(vistorName);
					 }
					 xOrders.setOrderType(foodType); //午餐还是晚餐
					 xOrders.setOrderCategory("XC"); //订餐种类：正常订餐（ZC）预约订餐（YY）现场订餐（XC）
					 xOrders.setForWhoId(xDeptSum.getDeptId());
					 xOrders.setUserId(xuser.getUserId());
					 xOrders.setInsTime(now);
					 xOrders.setInsUser(xuser.getUserName());
					 dao.save(xOrders);//生成订单
					 for (Map map1:map) {
						 XOrderDetail xOrderDetail =new XOrderDetail();
						 xOrderDetail.setOrderId(xOrders.getId());
						 xOrderDetail.setInsTime(new Date());
						 xOrderDetail.setInsUser(xuser.getUserName());
						 xOrderDetail.setFoodId(map1.get("id").toString());
						 xOrderDetail.setFoodNum(Integer.parseInt(map1.get("num").toString()));
						 xOrderDetail.setPayPrice(Integer.parseInt(map1.get("price").toString())); //实付套餐价格
						 xOrderDetailDao.save(xOrderDetail);//保存订餐详情
					 }
					 msgMap.put("status", "true"); 
					 msgMap.put("msg", "订餐成功"); 
				 }else{
					 msgMap.put("status", "false"); 
					 msgMap.put("msg", "部门账户余额不足,请联系管理员"); 
				 }
				 
			 }
		 }
		 
		return msgMap;
	}
	
	@Override
	public boolean isDeptSpecialRole(String userId) {
		String sql = "SELECT xu.user_id FROM x_user xu LEFT JOIN x_user_role xur ON xu.user_id = xur.user_id WHERE xur.role_id = '"+Constants.DEPT_ONLY_ID+"' AND xu.user_id = '"+userId+"'";
		List list = this.getPubDao().findBySql(sql);
		if(ListUtil.isNotEmpty(list)){
			return true;
		}
		return false;
	}

	@Override
	public List<Map> getLeadByUserId(String userId) {
		String sql = "SELECT xar.lead_id,xu.real_name as lead_name FROM x_appoint_relation xar left join  x_user xu on xu.user_id = xar.lead_id WHERE xar.appoint_id = '"+userId+"'";
		List<Map> list = this.getPubDao().findBySqlToMap(sql);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}
	
	@Override
	public Map<String, Object> deleteOrder( String orderId,String foodNum,XUser user, String orderType) throws Exception {
		 Map<String, Object> msgMap = new HashMap<String, Object>();
		 SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date now = new Date();
		//周几
		String weekKey = "";
		//订餐类型
		String foodType = "";
		String week = DateUtil.getWeek(now);
		Long currentTime = now.getTime();
		Long lunStart = DateUtil.getAppointDate(Constants.LUNSTART).getTime();
		Long lunEnd = DateUtil.getAppointDate(Constants.LUNSEND).getTime();
		Long dinStart = DateUtil.getAppointDate(Constants.DINSTART).getTime();
		Long dinEnd = DateUtil.getAppointDate(Constants.DINEND).getTime();
		if(lunStart <= currentTime && lunEnd >= currentTime){
			weekKey = week;
			foodType =  Constants.LUN;
		}else if(dinStart <= currentTime && dinEnd >= currentTime){
			weekKey = week;
			foodType =  Constants.DIN;
		}
		
		//根据订单ID取出订单信息
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar now2 = Calendar.getInstance();  
	    String dayTime =  now2.get(Calendar.YEAR)+"-"+(now2.get(Calendar.MONTH)+1)+"-"+now2.get(Calendar.DAY_OF_MONTH);
	    Date aMStart = formatter.parse((dayTime+" "+Constants.AMSTART)); //上开
	    Date aMSEnd = formatter.parse((dayTime+" "+Constants.AMEND)); //上结
	    Date pMStart = formatter.parse((dayTime+" "+Constants.PMSTART)); //下开
	    Date pMEnd= formatter.parse((dayTime+" "+Constants.PMEND)); //下结
		XOrders xOrders =dao.get(orderId);
		//判断当前时间类型（上午，下午）
		boolean b = true;
		if(foodType.equals(Constants.LUN)){//上午
			if("1".equals(orderType)){
				foodType = xOrders.getOrderType();
				weekKey = DateUtil.getWeek(xOrders.getInsTime());
				if(xOrders.getInsTime().getTime() > now.getTime() ){
					b = true;
				}else{
					b = false;
				}
			}else{
				if(aMStart.getTime() <=xOrders.getInsTime().getTime() && xOrders.getInsTime().getTime() <= aMSEnd.getTime()){
					if(aMStart.getTime() <=now.getTime() && now.getTime() <= aMSEnd.getTime()){
						b = true;
					}
				}else{
					b = false;
				}
			}
			
		}else if(foodType.equals(Constants.DIN)){//下午
			if("1".equals(orderType)){
				foodType = xOrders.getOrderType();
				weekKey = DateUtil.getWeek(xOrders.getInsTime());
				if(xOrders.getInsTime().getTime() > now.getTime() ){
					b = true;
				}else{
					b = false;
				}
			}else{
				if(pMStart.getTime() <=xOrders.getInsTime().getTime() && xOrders.getInsTime().getTime() <= pMEnd.getTime()){
					if(pMStart.getTime() <=now.getTime() && now.getTime() <= pMEnd.getTime()){
						b = true;
					}
				}else{
					b = false;
				}
			}
		}
		if(!b){
			msgMap.put("status", "false"); 
			msgMap.put("msg", "已完成不能取消订单"); 
		}else{
			//1：自己的2：客人的3：领导的
			if("1".equals(xOrders.getFlag()) || "3".equals(xOrders.getFlag())){
					 XStaffSum xStaffSum =null;
					 String hql = "from XStaffSum where userId = '"+xOrders.getForWhoId()+"'";
					 List<XStaffSum> list = this.getPubDao().findByHql(hql);
					 if(ListUtil.isNotEmpty(list)){
						 xStaffSum = list.get(0);
					 }
					 if(xStaffSum != null){
						 //返回金额
						 int f_balance = 0;
						 
						 /**
						  * 更新套餐库存数量
						  */
						 //根据订单ID取出套餐份数
						 String shql = "from XOrderDetail where orderId='"+orderId+"'";
						 List<XOrderDetail> xdList = this.getPubDao().findByHql(shql);
						 if(ListUtil.isNotEmpty(xdList)){
							 for (XOrderDetail xOrderDetail : xdList) {
								//根据套餐ID取出 套餐剩余份数
								 String food_remain ="";
								 String sql2 = "select xf.food_remain food_remain, xff.sell_price from x_food_bill xf " +
								 		" LEFT JOIN x_dictionary xd ON xf.dictionary_id = xd.id " +
								 		" LEFT JOIN x_food xff ON xff.id = xf.food_id " +
								 		" where xd.food_type='"+foodType+"'" +
								 		" and xd.week = '"+weekKey+"'" +
								 		" and xf.food_id = '"+xOrderDetail.getFoodId()+"'";
								 List<Map> list2 = this.getPubDao().findBySqlToMap(sql2);
								 if(ListUtil.isNotEmpty(list2)){
									 food_remain=  (String) list2.get(0).get("food_remain");
									 Integer sell_price = Integer.parseInt(list2.get(0).get("sell_price").toString());
									 int num_food = Integer.parseInt(food_remain) + xOrderDetail.getFoodNum();
									 String sl = "UPDATE x_food_bill SET food_remain ='"+num_food+"' where food_id = '"+xOrderDetail.getFoodId()+"'";
									 xFoodBillDao.executeSql(sl);//更新套餐库存
									 
									 //f_balance += xOrderDetail.getFoodNum() * sell_price;
								 }
								 
								 f_balance += xOrderDetail.getFoodNum() * xOrderDetail.getPayPrice();
							}
						 }
						 
						 /**
						  * 更新员工账户余额
						  */
						 //原有金额
						 int balance = Integer.parseInt(xStaffSum.getBalance()); 
						 
						 balance = balance + f_balance;
						 xStaffSum.setBalance(balance+"");
						 xStaffSum.setUpdTime(new Date());
						 //更新余额表
						 xStaffSumService.update(xStaffSum);
						 if(xOrders !=null){
							 xOrders.setUpdUser(user.getUserName()); 
							 xOrders.setUpdTime(new Date());
							 //0 : 未取消订单 1：取消订单
							 xOrders.setDelFlag(1);
							 String sql ="UPDATE x_orders SET upd_user='"+xOrders.getUpdUser()+"',upd_time='"+f.format(xOrders.getUpdTime())+"',del_flag='"+xOrders.getDelFlag()+"' where id ='"+orderId+"'";
							 dao.executeSql(sql);//更新订单信息
						 }
						 
						 /**
						  * 插入记录表信息
						  */
						 XDetailRecord xDetailRecord = new XDetailRecord();
						 // 0：收入 1：支出
						 xDetailRecord.setType("0");
						 if(xOrders.getFlag().endsWith("3")){ //领导
							 xDetailRecord.setUserId(xOrders.getForWhoId());
						 }else{
							 xDetailRecord.setUserId(user.getUserId()); //自己
						 }
						 xDetailRecord.setBalance(balance+"");
						 xDetailRecord.setInsTime(new Date());
						 xDetailRecord.setInsUser(user.getUserName());
						 xDetailRecord.setRemark("取消订单返回金额");
						 xDetailRecord.setChangeMoney(f_balance+"");
						 xDetailRecordDao.save(xDetailRecord);//返回余额操纵记录
						 msgMap.put("status", "true"); 
						 msgMap.put("msg", "取消订单成功"); 
					 }
				 }else if("2".equals(xOrders.getFlag())){
					 //返回金额
					 int f_balance = 0;
					 
					 XDeptSum xDeptSum = null;
					 String hql = "from XDeptSum where deptId = '"+user.getDeptId()+"'";
					 List<XDeptSum> list = this.getPubDao().findByHql(hql);
					 if(ListUtil.isNotEmpty(list)){
						 xDeptSum = list.get(0);
					 }
					 if(xDeptSum != null){
						 /**
						  * 更新套餐库存数量
						  */
						 //根据订单ID取出套餐份数
						 String shql = "from XOrderDetail where orderId='"+orderId+"'";
						 List<XOrderDetail> xdList = this.getPubDao().findByHql(shql);
						 if(ListUtil.isNotEmpty(xdList)){
							 for (XOrderDetail xOrderDetail : xdList) {
								//根据套餐ID取出 套餐剩余份数
								 String food_remain ="";
								 String sql2 = "select xf.food_remain food_remain, xff.sell_price from x_food_bill xf " +
								 		" LEFT JOIN x_dictionary xd ON xf.dictionary_id = xd.id " +
								 		" LEFT JOIN x_food xff ON xff.id = xf.food_id " +
								 		" where xd.food_type='"+foodType+"'" +
								 		" and xd.week = '"+weekKey+"'" +
								 		" and xf.food_id = '"+xOrderDetail.getFoodId()+"'";
								 List<Map> list2 = this.getPubDao().findBySqlToMap(sql2);
								 if(ListUtil.isNotEmpty(list2)){
									 food_remain=  (String) list2.get(0).get("food_remain");
									 Integer sell_price = Integer.parseInt(list2.get(0).get("sell_price").toString());
									 int num_food = Integer.parseInt(food_remain) + xOrderDetail.getFoodNum();
									 String sl = "UPDATE x_food_bill SET food_remain ='"+num_food+"' where food_id = '"+xOrderDetail.getFoodId()+"'";
									 xFoodBillDao.executeSql(sl);//更新套餐库存
									 
									 //f_balance += xOrderDetail.getFoodNum() * sell_price;
								 }
								 
								 f_balance += xOrderDetail.getFoodNum() * xOrderDetail.getPayPrice();
							}
						 }
						 
						 /**
						  * 更新员工账户余额
						  */
						 //获取原有部门金额
						 int balance = xDeptSum.getDeptSum();
						 
						 xDeptSum.setDeptSum(f_balance+balance);
						 xDeptSum.setUpdTime(new Date());
						 xDeptSum.setUpdUser(user.getUserName());
						 String sql = "UPDATE x_dept_sum SET dept_sum='"+xDeptSum.getDeptSum()+"',upd_time='"+f.format(xDeptSum.getUpdTime())+"',upd_user='"+xDeptSum.getUpdUser()+"' where dept_id='"+user.getDeptId()+"'";
						 xDeptSumDao.executeSql(sql);//更新部门金额
						 if(xOrders !=null){
							 xOrders.setUpdUser(user.getUserName()); 
							 xOrders.setUpdTime(new Date());
							 //0 : 未取消订单 1：取消订单
							 xOrders.setDelFlag(1);
							 String sqlX ="UPDATE x_orders SET upd_user='"+xOrders.getUpdUser()+"',upd_time='"+f.format(xOrders.getUpdTime())+"',del_flag='"+xOrders.getDelFlag()+"' where id ='"+orderId+"'";
							 dao.executeSql(sqlX);//更新订单信息
						 }
						 
						 /**
						  * 插入记录表信息
						  */
						 XDetailRecord xDetailRecord = new XDetailRecord();
						 // 0：收入 1：支出
						 xDetailRecord.setType("0");
						 xDetailRecord.setBalance(balance+"");
						 xDetailRecord.setInsTime(new Date());
						 xDetailRecord.setUpdUser(user.getUserName());
						 xDetailRecord.setDeptId(user.getDeptId());
						 xDetailRecord.setRemark("取消部门订单返回金额");
						 xDetailRecord.setChangeMoney(f_balance+"");
						 xDetailRecordDao.save(xDetailRecord);//返回余额操纵记录
						 msgMap.put("status", "true"); 
						 msgMap.put("msg", "取消订单成功"); 
						 
					 }
				 }
			}
		return msgMap;
	}
	
	@Override
	public Map<String, Object> deleteLiveOrder( String orderId,String foodNum,XUser user, String orderType) throws Exception {
		Map<String, Object> msgMap = new HashMap<String, Object>();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		//周几
		String weekKey = "";
		//订餐类型
		String foodType = "";
		String week = DateUtil.getWeek(now);
		Long currentTime = now.getTime();
		Long lunStart = DateUtil.getAppointDate(Constants.LIVE_LUNSTART).getTime();
		Long lunEnd = DateUtil.getAppointDate(Constants.LIVE_LUNSEND).getTime();
		Long dinStart = DateUtil.getAppointDate(Constants.LIVE_DINSTART).getTime();
		Long dinEnd = DateUtil.getAppointDate(Constants.LIVE_DINEND).getTime();
		if(lunStart <= currentTime && lunEnd >= currentTime){
			weekKey = week;
			foodType =  Constants.LUN;
		}else if(dinStart <= currentTime && dinEnd >= currentTime){
			weekKey = week;
			foodType =  Constants.DIN;
		}
		
		//根据订单ID取出订单信息
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar now2 = Calendar.getInstance();  
	    String dayTime =  now2.get(Calendar.YEAR)+"-"+(now2.get(Calendar.MONTH)+1)+"-"+now2.get(Calendar.DAY_OF_MONTH);
	    Date aMStart = formatter.parse((dayTime+" "+Constants.LIVE_LUNSTART)); //上开
	    Date aMSEnd = formatter.parse((dayTime+" "+Constants.LIVE_LUNSEND)); //上结
	    Date pMStart = formatter.parse((dayTime+" "+Constants.LIVE_DINSTART)); //下开
	    Date pMEnd= formatter.parse((dayTime+" "+Constants.LIVE_DINEND)); //下结
		XOrders xOrders =dao.get(orderId);
		//判断当前时间类型（上午，下午）
		boolean b = true;
		if(foodType.equals(Constants.LUN)){//上午
			if("1".equals(orderType)){
				foodType = xOrders.getOrderType();
				weekKey = DateUtil.getWeek(xOrders.getInsTime());
				if(xOrders.getInsTime().getTime() > now.getTime() ){
					b = true;
				}else{
					b = false;
				}
			}else{
				if(aMStart.getTime() <=xOrders.getInsTime().getTime() && xOrders.getInsTime().getTime() <= aMSEnd.getTime()){
					if(aMStart.getTime() <=now.getTime() && now.getTime() <= aMSEnd.getTime()){
						b = true;
					}
				}else{
					b = false;
				}
			}
			
		}else if(foodType.equals(Constants.DIN)){//下午
			if("1".equals(orderType)){
				foodType = xOrders.getOrderType();
				weekKey = DateUtil.getWeek(xOrders.getInsTime());
				if(xOrders.getInsTime().getTime() > now.getTime() ){
					b = true;
				}else{
					b = false;
				}
			}else{
				if(pMStart.getTime() <=xOrders.getInsTime().getTime() && xOrders.getInsTime().getTime() <= pMEnd.getTime()){
					if(pMStart.getTime() <=now.getTime() && now.getTime() <= pMEnd.getTime()){
						b = true;
					}
				}else{
					b = false;
				}
			}
		}
		if(!b){
			msgMap.put("status", "false"); 
			msgMap.put("msg", "已完成不能取消订单"); 
		}else{
			//1：自己的2：客人的3：领导的
			if("1".equals(xOrders.getFlag()) || "3".equals(xOrders.getFlag())){
					XStaffSum xStaffSum =null;
					 String hql = "from XStaffSum where userId = '"+xOrders.getForWhoId()+"'";
					 List<XStaffSum> list = this.getPubDao().findByHql(hql);
					 if(ListUtil.isNotEmpty(list)){
						 xStaffSum = list.get(0);
					 }
					 if(xStaffSum != null){
						 //返回金额
						 int f_balance = 0;
						 
						 //根据订单ID取出套餐价格
						 String shql = "from XOrderDetail where orderId='"+orderId+"'";
						 List<XOrderDetail> xdList = this.getPubDao().findByHql(shql);
						 if(ListUtil.isNotEmpty(xdList)){
							 for (XOrderDetail xOrderDetail : xdList) {
								 /*String sql2 = "select xff.sell_price from x_food_bill xf " +
								 		" LEFT JOIN x_dictionary xd ON xf.dictionary_id = xd.id " +
								 		" LEFT JOIN x_food xff ON xff.id = xf.food_id " +
								 		" where xd.food_type='"+foodType+"'" +
								 		" and xd.week = '"+weekKey+"'" +
								 		" and xf.food_id = '"+xOrderDetail.getFoodId()+"'";
								 List<Map> list2 = this.getPubDao().findBySqlToMap(sql2);
								 if(ListUtil.isNotEmpty(list2)){
									 Integer sell_price = Integer.parseInt(list2.get(0).get("sell_price").toString());
									 f_balance += xOrderDetail.getFoodNum() * sell_price;
								 }*/
								 
								 f_balance += xOrderDetail.getFoodNum() * xOrderDetail.getPayPrice();
							}
						 }
						 
						 /**
						  * 更新员工账户余额
						  */
						 //原有金额
						 int balance = Integer.parseInt(xStaffSum.getBalance()); 
						 //返回金额
						 balance = balance + f_balance;
						 xStaffSum.setBalance(balance+"");
						 xStaffSum.setUpdTime(new Date());
						 //更新余额表
						 xStaffSumService.update(xStaffSum);
						 if(xOrders !=null){
							 xOrders.setUpdUser(user.getUserName()); 
							 xOrders.setUpdTime(new Date());
							 //0 : 未取消订单 1：取消订单
							 xOrders.setDelFlag(1);
							 String sql ="UPDATE x_orders SET upd_user='"+xOrders.getUpdUser()+"',upd_time='"+f.format(xOrders.getUpdTime())+"',del_flag='"+xOrders.getDelFlag()+"' where id ='"+orderId+"'";
							 dao.executeSql(sql);//更新订单信息
						 }
						 
						 /**
						  * 插入记录表信息
						  */
						 XDetailRecord xDetailRecord = new XDetailRecord();
						 // 0：收入 1：支出
						 xDetailRecord.setType("0");
						 if(xOrders.getFlag().endsWith("3")){ //领导
							 xDetailRecord.setUserId(xOrders.getForWhoId());
						 }else{
							 xDetailRecord.setUserId(user.getUserId()); //自己
						 }
						 xDetailRecord.setBalance(balance+"");
						 xDetailRecord.setInsTime(new Date());
						 xDetailRecord.setInsUser(user.getUserName());
						 xDetailRecord.setRemark("取消订单返回金额");
						 xDetailRecord.setChangeMoney(f_balance+"");
						 xDetailRecordDao.save(xDetailRecord);//返回余额操纵记录
						 msgMap.put("status", "true"); 
						 msgMap.put("msg", "取消订单成功"); 
					 }
				 }else if("2".equals(xOrders.getFlag())){
					 XDeptSum xDeptSum = null;
					 String hql = "from XDeptSum where deptId = '"+user.getDeptId()+"'";
					 List<XDeptSum> list = this.getPubDao().findByHql(hql);
					 if(ListUtil.isNotEmpty(list)){
						 xDeptSum = list.get(0);
					 }
					 if(xDeptSum != null){
						 //返回金额
						 int f_balance = 0;
						 
						 //根据订单ID取出套餐价格
						 String shql = "from XOrderDetail where orderId='"+orderId+"'";
						 List<XOrderDetail> xdList = this.getPubDao().findByHql(shql);
						 if(ListUtil.isNotEmpty(xdList)){
							 for (XOrderDetail xOrderDetail : xdList) {
								 /*String sql2 = "select xff.sell_price from x_food_bill xf " +
								 		" LEFT JOIN x_dictionary xd ON xf.dictionary_id = xd.id " +
								 		" LEFT JOIN x_food xff ON xff.id = xf.food_id " +
								 		" where xd.food_type='"+foodType+"'" +
								 		" and xd.week = '"+weekKey+"'" +
								 		" and xf.food_id = '"+xOrderDetail.getFoodId()+"'";
								 List<Map> list2 = this.getPubDao().findBySqlToMap(sql2);
								 if(ListUtil.isNotEmpty(list2)){
									 Integer sell_price = Integer.parseInt(list2.get(0).get("sell_price").toString());
									 f_balance += xOrderDetail.getFoodNum() * sell_price;
								 }*/
								 
								 f_balance += xOrderDetail.getFoodNum() * xOrderDetail.getPayPrice();
							}
						 }
						 
						 /**
						  * 更新部门账户余额
						  */
						 //获取原有部门金额
						 int balance = xDeptSum.getDeptSum();
						 
						 xDeptSum.setDeptSum(f_balance+balance);
						 xDeptSum.setUpdTime(new Date());
						 xDeptSum.setUpdUser(user.getUserName());
						 String sql = "UPDATE x_dept_sum SET dept_sum='"+xDeptSum.getDeptSum()+"',upd_time='"+f.format(xDeptSum.getUpdTime())+"',upd_user='"+xDeptSum.getUpdUser()+"' where dept_id='"+user.getDeptId()+"'";
						 xDeptSumDao.executeSql(sql);//更新部门金额
						 if(xOrders !=null){
							 xOrders.setUpdUser(user.getUserName()); 
							 xOrders.setUpdTime(new Date());
							 //0 : 未取消订单 1：取消订单
							 xOrders.setDelFlag(1);
							 String sqlX ="UPDATE x_orders SET upd_user='"+xOrders.getUpdUser()+"',upd_time='"+f.format(xOrders.getUpdTime())+"',del_flag='"+xOrders.getDelFlag()+"' where id ='"+orderId+"'";
							 dao.executeSql(sqlX);//更新订单信息
						 }
						 
						 /**
						  * 插入记录表信息
						  */
						 XDetailRecord xDetailRecord = new XDetailRecord();
						 // 0：收入 1：支出
						 xDetailRecord.setType("0");
						 xDetailRecord.setBalance(balance+"");
						 xDetailRecord.setInsTime(new Date());
						 xDetailRecord.setUpdUser(user.getUserName());
						 xDetailRecord.setDeptId(user.getDeptId());
						 xDetailRecord.setRemark("取消部门订单返回金额");
						 xDetailRecord.setChangeMoney(f_balance+"");
						 xDetailRecordDao.save(xDetailRecord);//返回余额操纵记录
						 msgMap.put("status", "true"); 
						 msgMap.put("msg", "取消订单成功"); 
						 
					 }
				 }
			}
		return msgMap;
	}

	@Override
	public String getFoodBillByFoodId(String foodId) {
		 SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date now = new Date();
		//周几
		String weekKey = "";
		//订餐类型
		String foodType = "";
		String week = DateUtil.getWeek(now);
		Long currentTime = now.getTime();
		Long lunStart = DateUtil.getAppointDate(Constants.LUNSTART).getTime();
		Long lunEnd = DateUtil.getAppointDate(Constants.LUNSEND).getTime();
		Long dinStart = DateUtil.getAppointDate(Constants.DINSTART).getTime();
		Long dinEnd = DateUtil.getAppointDate(Constants.DINEND).getTime();
		if(lunStart <= currentTime && lunEnd >= currentTime){
			weekKey = week;
			foodType =  Constants.LUN;
		}else if(dinStart <= currentTime && dinEnd >= currentTime){
			weekKey = week;
			foodType =  Constants.DIN;
		}
		String sql2 = "select xf.food_remain food_remain from x_food_bill xf " +
		 		"LEFT JOIN x_dictionary xd ON xf.dictionary_id = xd.id" +
		 		" where xd.food_type='"+foodType+"'" +
		 		"and   xd.week = '"+weekKey+"'" +
		 		"and xf.food_id = '"+foodId+"'";
		 List<Map> list2 = this.getPubDao().findBySqlToMap(sql2);
		 String food_remain="";
		 if(ListUtil.isNotEmpty(list2)){
			 food_remain=  (String) list2.get(0).get("food_remain");//当前套餐库存
		 }
		return food_remain;
	}

	@Override
	public Map getMoney(String type, String id) {
		String sql = "";
		//领导和自己查询 staff_sum|客人查询dept_sum
		if(type.equals("LD")||type.equals("ZJ")){
			
			sql = "SELECT xu.real_name obj,xss.balance balance FROM x_user xu  LEFT JOIN x_staff_sum xss ON xss.user_id = xu.user_id WHERE xu.user_id = '"+id+"'";
			
		}else if(type.equals("KR")){
			sql = "SELECT xd.dept_name obj,xds.dept_sum balance FROM x_dept xd  LEFT JOIN x_dept_sum xds ON xds.dept_id = xd.dept_id WHERE xd.dept_id = '"+id+"'";
		}
		
		List<Map> list = this.getPubDao().findBySqlToMap(sql);
		if(ListUtil.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> getorderStatistics(SqlQueryFilter filter,String startTime,String endTime,String dep_id,String flag,XUser xuser) {
		StringBuffer sql = new StringBuffer();
		if(dep_id != null && dep_id != ""){
				if("1".equals(flag)){
					if(dep_id.indexOf("dept_")>=0){//
						String u_id = dep_id.substring(5);
						sql.append(" select xf.ins_user user_name_company, xc.company_name company_name, sum(xod.food_num) food_num,xd.dept_id dept_id, xd.dept_name dept_name ,sum(xod.food_num*xod.pay_price) money from x_order_detail xod ");
						sql.append(" LEFT JOIN x_orders xo ON xod.order_id = xo.id  ");
						sql.append(" LEFT JOIN x_user xu ON xo.user_id = xu.user_id  ");
						sql.append(" LEFT JOIN x_food xf ON xf.id = xod.food_id  ");
						sql.append(" LEFT JOIN x_company xc ON xu.company_id = xc.company_id ");
						sql.append(" LEFT JOIN x_dept xd ON xd.dept_id = xu.dept_id ");
						sql.append("   WHERE xo.del_flag = '0'   ");
						sql.append("  and  (xd.dept_id = '"+u_id+"' or xd.supdepid ='"+u_id+"') ");
						if(startTime !=null && startTime !=""){
							sql.append(" and xod.ins_time >= '"+startTime+"'");
						}
						if(endTime !=null && endTime !=""){
							sql.append(" and xod.ins_time <= '"+endTime+"'");
						}
						if(xuser.getType().equals("2")){ //2：代表餐饮公司
							sql.append(" and xf.ins_user = '"+xuser.getUserName()+"'");
						}
						sql.append(" GROUP BY xd.dept_id, xf.ins_user ");
						sql.append(" ORDER BY  xc.company_id,xd.dept_id,xf.ins_user ");
						
						filter.setBaseSql(sql.toString());
						List<Map> list = this.query(filter);
						if(ListUtil.isNotEmpty(list)){
							for (Map map : list) {
								String user_name_company = map.get("user_name_company").toString();
								XUser findFoodBusiness = xUserService.findFoodBusinessByUsername(user_name_company);
								map.put("food_company_name", findFoodBusiness.getFoodCompanyName());
							}
							return list;	
						}
					}else{//公司
						sql.append(" select xf.ins_user user_name_company, xc.company_name company_name, sum(xod.food_num) food_num,xd.dept_id dept_id, xd.dept_name dept_name ,sum(xod.food_num*xod.pay_price) money from x_order_detail xod ");
						sql.append(" LEFT JOIN x_orders xo ON xod.order_id = xo.id  ");
						sql.append(" LEFT JOIN x_user xu ON xo.user_id = xu.user_id  ");
						sql.append(" LEFT JOIN x_food xf ON xf.id = xod.food_id  ");
						sql.append(" LEFT JOIN x_company xc ON xu.company_id = xc.company_id ");
						sql.append("  LEFT JOIN x_dept xd ON xd.dept_id = xu.dept_id ");
						sql.append("   WHERE xo.del_flag = '0'   ");
						if(StringUtil.isNotEmpty(dep_id)){
							sql.append(" and xc.company_id = '"+dep_id+"'");
						}
						if(startTime !=null && startTime !=""){
							sql.append(" and xod.ins_time >= '"+startTime+"'");
						}
						if(endTime !=null && endTime !=""){
							sql.append(" and xod.ins_time <= '"+endTime+"'");
						}
						if(xuser.getType().equals("2")){ //2：代表餐饮公司
							sql.append(" and xf.ins_user = '"+xuser.getUserName()+"'");
						}
						
						sql.append(" GROUP BY xd.dept_id, xf.ins_user  ");
						sql.append(" ORDER BY  xc.company_id,xd.dept_id,xf.ins_user ");
						filter.setBaseSql(sql.toString());
						List<Map> list = this.query(filter);
						if(ListUtil.isNotEmpty(list)){
							for (Map map : list) {
								String user_name_company = map.get("user_name_company").toString();
								XUser findFoodBusiness = xUserService.findFoodBusinessByUsername(user_name_company);
								map.put("food_company_name", findFoodBusiness.getFoodCompanyName());
							}
							return list;	
						}
					}
				}else
					if("2".equals(flag)){
							if(dep_id.indexOf("dept_")>=0){//
								String u_id = dep_id.substring(5);
								sql.append(" select xf.ins_user user_name_company, xc.company_name company_name, sum(xod.food_num) food_num, xu.real_name real_name,xd.dept_id dept_id, xd.dept_name dept_name ,sum(xod.food_num*xod.pay_price) money,xo.vistor_name from x_order_detail xod");
								sql.append(" LEFT JOIN x_orders xo ON xod.order_id = xo.id");
								sql.append(" LEFT JOIN x_user xu ON xo.user_id = xu.user_id");
								sql.append(" LEFT JOIN x_food xf ON xf.id = xod.food_id  ");
								sql.append(" LEFT JOIN x_company xc ON xu.company_id = xc.company_id");
								sql.append(" LEFT JOIN x_dept xd ON xd.dept_id = xu.dept_id");
								sql.append(" WHERE xo.del_flag = '0'");
								sql.append("  and  (xd.dept_id = '"+u_id+"' or xd.supdepid ='"+u_id+"') ");
								if(startTime !=null && startTime !=""){
									sql.append(" and xod.ins_time >= '"+startTime+"'");
								}
								if(endTime !=null && endTime !=""){
									sql.append(" and xod.ins_time <= '"+endTime+"'");
								}
								if(xuser.getType().equals("2")){ //2：代表餐饮公司
									sql.append(" and xf.ins_user = '"+xuser.getUserName()+"'");
								}
								
								sql.append(" GROUP BY xo.vistor_name, xf.ins_user");
								sql.append(" ORDER BY  xc.company_id,xf.ins_user");
								 filter.setBaseSql(sql.toString());
								  List<Map> list = this.query(filter);
								  if(ListUtil.isNotEmpty(list)){
									  for (Map map : list) {
											String user_name_company = map.get("user_name_company").toString();
											XUser findFoodBusiness = xUserService.findFoodBusinessByUsername(user_name_company);
											map.put("food_company_name", findFoodBusiness.getFoodCompanyName());
										}
									  return list;	
								  }
							}else{//公司
								sql.append(" SELECT xf.ins_user user_name_company, xc.company_name company_name ,sum(xod.food_num) food_num, xd.dept_name dept_name,xu.real_name real_name,xc.company_id,sum(xod.food_num*xod.pay_price) money,xo.vistor_name FROM x_order_detail xod");
								sql.append("  LEFT JOIN x_orders xo ON xod.order_id = xo.id");
								sql.append(" LEFT JOIN x_user xu ON xo.user_id = xu.user_id");
								sql.append(" LEFT JOIN x_food xf ON xf.id = xod.food_id  ");
								sql.append(" LEFT JOIN x_company xc ON xu.company_id = xc.company_id");
								sql.append(" LEFT JOIN x_dept xd ON xd.dept_id = xu.dept_id");
								sql.append(" WHERE xo.del_flag = '0'");
								if(StringUtil.isNotEmpty(dep_id)){
									sql.append(" AND xc.company_id = '"+dep_id+"'");
								}
								if(startTime !=null && startTime !=""){
									sql.append(" and xod.ins_time >= '"+startTime+"'");
								}
								if(endTime !=null && endTime !=""){
									sql.append(" and xod.ins_time <= '"+endTime+"'");
								}
								if(xuser.getType().equals("2")){ //2：代表餐饮公司
									sql.append(" and xf.ins_user = '"+xuser.getUserName()+"'");
								}
								
								sql.append(" GROUP BY xo.vistor_name, xf.ins_user");
								sql.append(" ORDER BY  xc.company_id,xd.dept_name,xf.ins_user");
								 filter.setBaseSql(sql.toString());
								  List<Map> list = this.query(filter);
								  if(ListUtil.isNotEmpty(list)){
									  for (Map map : list) {
											String user_name_company = map.get("user_name_company").toString();
											XUser findFoodBusiness = xUserService.findFoodBusinessByUsername(user_name_company);
											map.put("food_company_name", findFoodBusiness.getFoodCompanyName());
										}
									  return list;	
								  }
							}
					}
				 
		}
		return null;
	}

	@Override
	public XAppointRelation getXAppointRelation(String leadId) {
		String hql ="from XAppointRelation where leadId = ?";
		List<XAppointRelation> list= this.relationDao.findByHql(hql, new Object[]{leadId});
		if(ListUtil.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> getRechargeObjExcel( String flag,
			String deptId,String companyId, String str) {
		
		StringBuffer sql = new StringBuffer();
		
		//部门
		if(flag.equals("1")){
			sql.append("select 1 flag,xd.dept_id id,xd.dept_name,xds.dept_sum,xy.company_name" +
					"  from x_dept xd left join x_dept_sum xds on xd.dept_id = xds.dept_id " +
					"   left join x_company xy on  xy.company_id=  xd.company_id   " +
					"  where 1 = 1 ");
			
			if(StringUtil.isNotEmpty(deptId)){
				sql.append(" and xd.dept_id = '"+deptId+"' ");
			}
			
			if(StringUtil.isNotEmpty(companyId)){
				sql.append(" and xd.company_id = '"+companyId+"' ");
			}
			
		}else if(flag.equals("2")){
			  
			sql.append(" SELECT  2 flag,xu.user_id id,xd.dept_name,xu.real_name,xu.jobtitle,balance,xy.company_name ");
			sql.append(" FROM x_user xu  ");
			sql.append(" LEFT JOIN x_dept xd ON xu.dept_id = xd.dept_id  ");
			sql.append(" LEFT JOIN x_staff_sum xs ON xu.user_id = xs.user_id ");
			sql.append(" left join x_company xy on  xy.company_id=  xd.company_id");
			sql.append(" WHERE  xu.type='1'  AND xu.del_flag = '0' ");

			if(StringUtil.isNotEmpty(deptId)){
				sql.append(" AND xd.dept_id = '"+deptId+"' ");
			}
			
			if(StringUtil.isNotEmpty(companyId)){
				sql.append(" and xd.company_id = '"+companyId+"' ");
			}
			
			if(StringUtil.isNotEmpty(str)){
				sql.append("   AND (user_name LIKE '%"+str+"%' OR real_name LIKE '%"+str+"%' OR tel LIKE '%"+str+"%') ");
			}
		}
		
		List<Map> list = this.getPubDao().findBySqlToMap(sql.toString());
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getRechargeObjExcelL(String flag, String deptId,
			String companyId, String str, String startTime, String endTime) {
		StringBuffer sb = new StringBuffer();
		if("1".equals(flag)){
			sb.append(" select  xdr.dept_id,sum(xdr.change_money) count,xc.company_name,xd.dept_name from x_detail_record  xdr ");
			sb.append(" left join x_dept xd  on xd.dept_id = xdr.dept_id ");
			sb.append(" left join x_company xc on xc.company_id = xd.company_id ");
			sb.append(" where  xdr.type ='0'  ");
			sb.append(" and xdr.dept_id   is not null     ");
			sb.append(" and xdr.remark like '%充值%'  ");
			if(StringUtil.isNotEmpty(startTime)){
				sb.append(" and xdr.ins_time>= '" + startTime + " 00:00:00'");
			}
			if(StringUtil.isNotEmpty(endTime)){
				sb.append(" and xdr.ins_time<='" + endTime + " 23:59:59'");
			}
			if(StringUtil.isNotEmpty(companyId)){
				sb.append(" and xd.company_id = '"+companyId+"' ");
			}
			if(StringUtil.isNotEmpty(deptId)){
				sb.append(" and xd.dept_id ='"+deptId+"' ");				
			}
			if(StringUtil.isNotEmpty(str)){
				sb.append(" and xd.dept_name like '%"+str+"%' ");	
			}
			sb.append(" group by xc.company_id,xd.dept_id   ");
		}else if("2".equals(flag)){
			sb.append(" select xu.real_name,xdr.user_id,sum(xdr.change_money) count,xc.company_name,xd.dept_name from x_detail_record  xdr ");
			sb.append(" left join x_user xu on xu.user_id = xdr.user_id ");
			sb.append(" left join x_dept xd on xu.dept_id = xd.dept_id ");
			sb.append(" left join x_company xc on xc.company_id = xd.company_id ");
			sb.append(" where  xdr.type ='0'   ");
			sb.append(" and xdr.dept_id   is null   ");
			sb.append(" and xdr.remark like '%充值%'    ");
			if(StringUtil.isNotEmpty(startTime)){
				sb.append(" and xdr.ins_time>= '" + startTime + " 00:00:00'");
			}
			if(StringUtil.isNotEmpty(endTime)){
				sb.append(" and xdr.ins_time<='" + endTime + " 23:59:59'");
			}
			if(StringUtil.isNotEmpty(companyId)){
				sb.append(" and xd.company_id = '"+companyId+"' ");
			}
			if(StringUtil.isNotEmpty(deptId)){
				sb.append(" and xd.dept_id ='"+deptId+"' ");				
			}
			if(StringUtil.isNotEmpty(str)){
				sb.append(" and (xu.real_name like '%"+str+"%' ");	
				sb.append(" or xd.dept_name like '%"+str+"%') ");	
			}
			sb.append(" group by xc.company_id,xu.user_id   ");
		}
		
		List<Map> list = this.getPubDao().findBySqlToMap(sb.toString());
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}


	@SuppressWarnings("rawtypes")
	@Override
	public boolean clearZero(String ids, String insUser, Date insTime) {
		System.out.println("--[clearZero]--start--");
		boolean boo = true;
		//员工余额、id
		String staffSql = "SELECT xss.user_id,xss.balance FROM x_staff_sum xss  WHERE xss.user_id IN ("+ids+")";
		List<Map> staffList = this.getPubDao().findBySqlToMap(staffSql);
		//插入x_detail_record记录
		XDetailRecord xdr;
		if(ListUtil.isNotEmpty(staffList)){
			for (Map map : staffList) {
				xdr = new XDetailRecord();
				xdr.setBalance("0");									//当前余额
				xdr.setChangeMoney(map.get("balance").toString()); 		//交易金额	
				xdr.setType("1");										//支出
				xdr.setRemark("年底清零");									//备注
				xdr.setUserId(map.get("user_id").toString());			//用户id
				xdr.setInsUser(insUser);								//插入者
				xdr.setInsTime(new Date());								//插入时间
				//保存记录
				XDetailRecord xdrFlag = xDetailRecordDao.save(xdr);
				if(null == xdrFlag){
					boo = false;
					break;
				}
			}
		}
		if(boo){
			String updSql = "UPDATE x_staff_sum SET balance = '0' WHERE user_id IN ("+ids+")";
			try{
				this.getPubDao().executeSql(updSql);
				boo = true;
			}catch(Exception e){
				e.printStackTrace();
				boo = false;
			}
		}
		System.out.println("--[clearZero]--end--");
		return boo;
	}

	@Override
	public List<Map> getCurrentFood(String week, String type, String foodBusiness){
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT xfb.id,xfb.food_id,xfb.food_remain as food_num,xf.food_name,xf.food_img,xf.food_desc, xf.sell_price ");
		sql.append("  FROM x_food_bill xfb  ");
		sql.append("  LEFT JOIN x_food xf ON xf.id = xfb.food_id ");
		sql.append("  WHERE xfb.dictionary_id = (SELECT xd.id FROM x_dictionary xd WHERE xd.week = '"+week+"' AND xd.food_type='"+type+"' ) and xf.del_flag='0' and xf.ins_user='"+foodBusiness+"'  and xfb.ins_user='"+foodBusiness+"'");
		sql.append(" GROUP BY xfb.food_id");
		List<Map> list = this.getPubDao().findBySqlToMap(sql.toString());
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map> getorderStatisticsExcel(String startTime,String endTime,String dep_id,String flag,XUser xuser) {
		List<Map> listAll = new ArrayList<Map>();
		if(dep_id != null && dep_id != ""){
			StringBuffer sql = new StringBuffer();
			if("1".equals(flag)){
				if(dep_id.indexOf("dept_")>=0){//
					String u_id = dep_id.substring(5);
					sql.append(" select xf.ins_user user_name_company, xc.company_name company_name, sum(xod.food_num) food_num,xd.dept_id dept_id, xd.dept_name dept_name ,sum(xod.food_num*xod.pay_price) money   ");
					if(startTime !=null && startTime !=""){
						sql.append("   ,'"+startTime+"' startTime  ");
					}
					if(endTime !=null && endTime !=""){
						sql.append("  ,'"+endTime+"' endTime ");
					}
					sql.append(" from x_order_detail xod  ");
					sql.append(" LEFT JOIN x_orders xo ON xod.order_id = xo.id  ");
					sql.append(" LEFT JOIN x_user xu ON xo.user_id = xu.user_id  ");
					sql.append(" LEFT JOIN x_food xf ON xf.id = xod.food_id  ");
					sql.append(" LEFT JOIN x_company xc ON xu.company_id = xc.company_id ");
					sql.append(" LEFT JOIN x_dept xd ON xd.dept_id = xu.dept_id ");
					sql.append("   WHERE xo.del_flag = '0'   ");
//					sql.append("  and  xd.dept_id = '"+u_id+"'");
					sql.append("  and  (xd.dept_id = '"+u_id+"' or xd.supdepid ='"+u_id+"') ");
					if(startTime !=null && startTime !=""){
						sql.append(" and xod.ins_time >= '"+startTime+"'");
					}
					if(endTime !=null && endTime !=""){
						sql.append(" and xod.ins_time <= '"+endTime+"'");
					}
					if(xuser.getType().equals("2")){ //2：代表餐饮公司
						sql.append(" and xf.ins_user = '"+xuser.getUserName()+"'");
					}
					sql.append(" GROUP BY xd.dept_id, xf.ins_user  ");
					sql.append(" ORDER BY  xc.company_id,xd.dept_id, xf.ins_user ");
					List<Map> list = this.getPubDao().findBySqlToMap(sql.toString());
					if(ListUtil.isNotEmpty(list)){
						for (Map map : list) {
							String user_name_company = map.get("user_name_company").toString();
							XUser findFoodBusiness = xUserService.findFoodBusinessByUsername(user_name_company);
							map.put("food_company_name", findFoodBusiness.getFoodCompanyName());
						}
						return list;	
					}
				}else{//公司
					sql.append(" select xf.ins_user user_name_company, xc.company_name company_name, sum(xod.food_num) food_num,xd.dept_id dept_id, xd.dept_name dept_name ,sum(xod.food_num*xod.pay_price) money ");
					if(startTime !=null && startTime !=""){
						sql.append("   ,'"+startTime+"' startTime ");
					}
					if(endTime !=null && endTime !=""){
						sql.append("  ,'"+endTime+"' endTime");
					}
					sql.append(" from x_order_detail xod  ");
					sql.append(" LEFT JOIN x_orders xo ON xod.order_id = xo.id  ");
					sql.append(" LEFT JOIN x_user xu ON xo.user_id = xu.user_id  ");
					sql.append(" LEFT JOIN x_food xf ON xf.id = xod.food_id  ");
					sql.append(" LEFT JOIN x_company xc ON xu.company_id = xc.company_id ");
					sql.append("  LEFT JOIN x_dept xd ON xd.dept_id = xu.dept_id ");
					sql.append("   WHERE xo.del_flag = '0'   ");
					if(StringUtil.isNotEmpty(dep_id)){
						sql.append(" and xc.company_id = '"+dep_id+"'");
					}
					if(startTime !=null && startTime !=""){
						sql.append(" and xod.ins_time >= '"+startTime+"'");
					}
					if(endTime !=null && endTime !=""){
						sql.append(" and xod.ins_time <= '"+endTime+"'");
					}
					if(xuser.getType().equals("2")){ //2：代表餐饮公司
						sql.append(" and xf.ins_user = '"+xuser.getUserName()+"'");
					}
					sql.append(" GROUP BY xd.dept_id, xf.ins_user  ");
					sql.append(" ORDER BY  xc.company_id,xd.dept_id, xf.ins_user ");
					List<Map> list = this.getPubDao().findBySqlToMap(sql.toString());
					if(ListUtil.isNotEmpty(list)){
						for (Map map : list) {
							String user_name_company = map.get("user_name_company").toString();
							XUser findFoodBusiness = xUserService.findFoodBusinessByUsername(user_name_company);
							map.put("food_company_name", findFoodBusiness.getFoodCompanyName());
						}
						return list;	
					}
				}
			}else if("2".equals(flag)){
				if(dep_id.indexOf("dept_")>=0){//
					String u_id = dep_id.substring(5);
					sql.append(" select xf.ins_user user_name_company, xc.company_name company_name, sum(xod.food_num) food_num, xu.real_name real_name,xd.dept_id dept_id, xd.dept_name dept_name ,sum(xod.food_num*xod.pay_price) money,xo.vistor_name ");
					if(startTime !=null && startTime !=""){
						sql.append("   ,'"+startTime+"' startTime  ");
					}
					if(endTime !=null && endTime !=""){
						sql.append("  ,'"+endTime+"' endTime ");
					}
					sql.append(" from x_order_detail xod  ");
					sql.append(" LEFT JOIN x_orders xo ON xod.order_id = xo.id");
					sql.append(" LEFT JOIN x_user xu ON xo.user_id = xu.user_id");
					sql.append(" LEFT JOIN x_food xf ON xf.id = xod.food_id  ");
					sql.append(" LEFT JOIN x_company xc ON xu.company_id = xc.company_id");
					sql.append(" LEFT JOIN x_dept xd ON xd.dept_id = xu.dept_id");
					sql.append(" WHERE xo.del_flag = '0'");
//					sql.append("  and  xd.dept_id = '"+u_id+"'");
					sql.append("  and  (xd.dept_id = '"+u_id+"' or xd.supdepid ='"+u_id+"') ");
					if(startTime !=null && startTime !=""){
						sql.append(" and xod.ins_time >= '"+startTime+"'");
					}
					if(endTime !=null && endTime !=""){
						sql.append(" and xod.ins_time <= '"+endTime+"'");
					}
					if(xuser.getType().equals("2")){ //2：代表餐饮公司
						sql.append(" and xf.ins_user = '"+xuser.getUserName()+"'");
					}
					sql.append(" GROUP BY xo.vistor_name, xf.ins_user ");
					sql.append(" ORDER BY  xc.company_id, xf.ins_user");
					List<Map> list = this.getPubDao().findBySqlToMap(sql.toString());
					if(ListUtil.isNotEmpty(list)){
						for (Map map : list) {
							String user_name_company = map.get("user_name_company").toString();
							XUser findFoodBusiness = xUserService.findFoodBusinessByUsername(user_name_company);
							map.put("food_company_name", findFoodBusiness.getFoodCompanyName());
						}
						return list;	
					}
				}else{//公司
					sql.append(" SELECT xf.ins_user user_name_company, xc.company_name company_name ,sum(xod.food_num) food_num, xd.dept_name dept_name,xu.real_name real_name,xc.company_id,sum(xod.food_num*xod.pay_price) money,xo.vistor_name  ");
					if(startTime !=null && startTime !=""){
						sql.append("   ,'"+startTime+"' startTime  ");
					}
					if(endTime !=null && endTime !=""){
						sql.append("  ,'"+endTime+"' endTime ");
					}
					sql.append(" from x_order_detail xod  ");
					sql.append("  LEFT JOIN x_orders xo ON xod.order_id = xo.id");
					sql.append(" LEFT JOIN x_user xu ON xo.user_id = xu.user_id");
					sql.append(" LEFT JOIN x_food xf ON xf.id = xod.food_id  ");
					sql.append(" LEFT JOIN x_company xc ON xu.company_id = xc.company_id");
					sql.append(" LEFT JOIN x_dept xd ON xd.dept_id = xu.dept_id");
					sql.append(" WHERE xo.del_flag = '0'");
					if(StringUtil.isNotEmpty(dep_id)){
						sql.append(" AND xc.company_id = '"+dep_id+"'");
					}
					if(startTime !=null && startTime !=""){
						sql.append(" and xod.ins_time >= '"+startTime+"'");
					}
					if(endTime !=null && endTime !=""){
						sql.append(" and xod.ins_time <= '"+endTime+"'");
					}
					if(xuser.getType().equals("2")){ //2：代表餐饮公司
						sql.append(" and xf.ins_user = '"+xuser.getUserName()+"'");
					}
					sql.append(" GROUP BY xo.vistor_name, xf.ins_user ");
					sql.append(" ORDER BY  xc.company_id,xd.dept_name, xf.ins_user");
					List<Map> list = this.getPubDao().findBySqlToMap(sql.toString());
					if(ListUtil.isNotEmpty(list)){
						for (Map map : list) {
							String user_name_company = map.get("user_name_company").toString();
							XUser findFoodBusiness = xUserService.findFoodBusinessByUsername(user_name_company);
							map.put("food_company_name", findFoodBusiness.getFoodCompanyName());
						}
						return list;	
					}
				}
			}
		}
		return listAll;	
			
	}

	@Override
	public List<Map> getMyOrderById(String orderId) {
		StringBuffer detailSql = new StringBuffer();
		detailSql.append("  SELECT xod.food_num,xf.food_name,xf.food_desc,xf.food_img,xf.id FROM x_order_detail xod  ");
		detailSql.append("  LEFT JOIN x_food xf ON xf.id = xod.food_id   ");
		detailSql.append("  WHERE order_id = '"+orderId+"'"    );
		List<Map> detailMap = this.getPubDao().findBySqlToMap(detailSql.toString());
		if(ListUtil.isNotEmpty(detailMap)){
			return detailMap;
		}
		return detailMap;
	}

	@Override
	public List<Map> getStaffInfo(String companyId, String deptId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select real_name name,user_id id,dept_id from x_user where type='1' ");
		sb.append(" and company_id ='"+companyId+"'");
		sb.append(" and dept_id ='"+deptId+"'");
		List<Map> staffList = this.getPubDao().findBySqlToMap(sb.toString());
		if(ListUtil.isNotEmpty(staffList)){
			return staffList;
		}
		return null;
	}

	@Override
	public List<MyOrder> getReserveMyOrder(String loginUserId) throws Exception {
		List<MyOrder> orderList = null;
		MyOrder myOrder = null;
		try{
			StringBuffer orderSql = new StringBuffer();
			orderSql.append("  SELECT 	xo.order_type, xo.for_who_id,xo.id,xo.flag, xo.rec_id, xo.express_num, date_format(xo.ins_time, '%Y-%m-%d %H:%i:%s' )  ordertime,  ");
			orderSql.append("  CASE flag   ");
			orderSql.append("  WHEN '1' THEN (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '3' THEN (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '2' THEN (SELECT xd.dept_name FROM x_dept xd WHERE xd.dept_id = xo.for_who_id )  ");
			orderSql.append("  END orderobj,  ");
			orderSql.append("  CASE flag   ");
			orderSql.append("  WHEN '1' THEN (SELECT xss.balance FROM x_staff_sum  xss WHERE xss.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '3' THEN (SELECT xss.balance FROM x_staff_sum  xss WHERE xss.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '2' THEN (SELECT xds.dept_sum FROM x_dept_sum xds WHERE xds.dept_id = xo.for_who_id )  ");
			orderSql.append("  END objbalance  ");
			orderSql.append("  FROM x_orders xo   ");
			orderSql.append("  WHERE xo.user_id = '"+loginUserId+"'  ");
			//查询条件：非产品
			orderSql.append("  and xo.order_type != 'PRO'");
			orderSql.append("  and xo.order_category = 'YY'"); //预约订餐
			
			orderSql.append("  and xo.ins_time > now()");
			orderSql.append("  and xo.ins_time >= '"+DateUtil.getLastMonth2()+"'");
			orderSql.append("  and xo.del_flag ='0' order by ordertime desc");
			List<Map> l = this.getPubDao().findBySqlToMap(orderSql.toString());
			if(ListUtil.isNotEmpty(l)){
				orderList = new ArrayList<MyOrder>();
				for (Map map : l) {
					myOrder = new MyOrder();
					myOrder.setBalance((String) map.get("objbalance"));
					myOrder.setOrderObj((String) map.get("orderobj"));
					myOrder.setOrderTime(map.get("ordertime").toString());
					myOrder.setId((String) map.get("id"));
					myOrder.setFlag((String) map.get("flag"));
					myOrder.setId((String) map.get("id"));
					myOrder.setForWhoId((String) map.get("for_who_id"));
					myOrder.setOrderType((String) map.get("order_type"));
					String setFoodNameStr = "";
					Integer setFoodTypeNum = 0;
					Integer orderNum=0;
					String id = (String) map.get("id");
					StringBuffer detailSql = new StringBuffer();
					detailSql.append("  SELECT xod.food_num,xod.pay_price,xf.food_name,xf.sell_price,xf.food_desc,xf.food_img,xf.id FROM x_order_detail xod  ");
					detailSql.append("  LEFT JOIN x_food xf ON xf.id = xod.food_id   ");
					detailSql.append("  WHERE order_id = '"+map.get("id").toString()+"'"    );
					List<Map> detailMap = this.getPubDao().findBySqlToMap(detailSql.toString());
					
					//订单总价
					Integer total = 0;
					if(ListUtil.isNotEmpty(detailMap)){
						for (Map m : detailMap) {
							setFoodNameStr += m.get("food_name") + ",";
							setFoodTypeNum +=Integer.valueOf(m.get("food_num").toString());
							orderNum +=setFoodTypeNum;
							
							total += Integer.parseInt(m.get("food_num").toString()) * Integer.parseInt(m.get("pay_price").toString());
							
							//获取餐饮所属的餐饮公司
							XFood xFood = xFoodService.get(m.get("id").toString());
							if(xFood != null){
								XUser foodBusiness = xUserService.findFoodBusinessByUsername(xFood.getInsUser());
								myOrder.setxUser(foodBusiness);
							}
						}
					}
					
					myOrder.setDetailList(detailMap);
					myOrder.setFoodNameStr(setFoodNameStr==""?"":setFoodNameStr.substring(0, setFoodNameStr.lastIndexOf(",")));
					myOrder.setFoodTypeNum(setFoodTypeNum.toString());
					myOrder.setMoenyTotal(total.toString());
					myOrder.setOrdeNum(orderNum.toString());
					
					if(map.get("rec_id") != null){
						//收货人信息
						XFoodSendAddress xFoodSendAddress = xFoodSendAddressService.get((String)map.get("rec_id"));
						if(xFoodSendAddress != null){
							XFoodSendRegion xFoodSendRegion = xFoodSendRegionService.get(xFoodSendAddress.getRegionId());
							xFoodSendAddress.setRegionName(xFoodSendRegion.getRegionName());
							
							myOrder.setxFoodSendAddress(xFoodSendAddress);
						}
					}
					
					orderList.add(myOrder);
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("获取我的订单失败！");
		}
		if(ListUtil.isNotEmpty(orderList)){
			return orderList;
		}
		
		return null;
	}
	@Override
	@Transactional 
	public Map<String, Object> orderReserve(String type, XUser xuser,
			String orderId, List<Map> map, int z_num,String vistorName,String time,String recId) throws ParseException {
		 Map<String, Object> msgMap = new HashMap<String, Object>();
		 int sum = z_num;
		 
		 //modify by dingzhj at date 2017-0317
		 SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date now = new Date();
		//周几
		String weekKey = "";
		//订餐类型
		String foodType = "";
	/*	String week = DateUtil.getWeek(now);
		Long currentTime = now.getTime();
		Long lunStart = DateUtil.getAppointDate(Constants.LUNSTART).getTime();
		Long lunEnd = DateUtil.getAppointDate(Constants.LUNSEND).getTime();
		Long dinStart = DateUtil.getAppointDate(Constants.DINSTART).getTime();
		Long dinEnd = DateUtil.getAppointDate(Constants.DINEND).getTime();
		if(lunStart <= currentTime && lunEnd >= currentTime){
			weekKey = week;
			foodType =  Constants.LUN;
		}else if(dinStart <= currentTime && dinEnd >= currentTime){
			weekKey = week;
			foodType =  Constants.DIN;
		}*/
		
		 if("0".equals(type)||"2".equals(type)){
			// XStaffSum xStaffSum = xStaffSumDao.get(orderId);
			 XStaffSum xStaffSum =null;
			 String hql = "from XStaffSum where userId = '"+orderId+"'";
			 List<XStaffSum> list = this.getPubDao().findByHql(hql);
			 if(ListUtil.isNotEmpty(list)){
				 xStaffSum = list.get(0);
				}
			 if(xStaffSum!=null){
				 XDetailRecord xDetailRecord = new XDetailRecord();
				 int balance = Integer.parseInt(xStaffSum.getBalance());//当前
				 if(balance>=sum){
					 balance = balance-sum;
					 xStaffSum.setBalance(balance+"");
					 xStaffSum.setUpdTime(now);
					 xStaffSum.setUpdUser(xuser.getUserName());
					 xStaffSumDao.save(xStaffSum);//更新余额
					 xDetailRecord.setBalance(balance+"");
					 xDetailRecord.setChangeMoney(sum+"");
					 xDetailRecord.setType(Constants.PAY);
					 xDetailRecord.setDeptId(xuser.getDeptId());
					 xDetailRecord.setUserId(orderId);
					 xDetailRecord.setRemark("订餐消费");
					 xDetailRecord.setInsTime(now);
					 xDetailRecord.setInsUser(xuser.getUserName());
					 xDetailRecordDao.save(xDetailRecord);//添加余额操作记录
					 XOrders xOrders =new XOrders();
					 if("0".equals(type)){
						 xOrders.setFlag("1");
					 }else if("2".equals(type)){
						 xOrders.setFlag("3");
					 }
					 if(vistorName != null && vistorName!=""){
						 xOrders.setVistorName(vistorName);
					 }
					 String week = DateUtil.getWeek(f.parse(DateWeek.printWeekdays(time)));
					 Long currentTime = now.getTime();
					 Long lunStart = DateUtil.getAppointDate(Constants.LUNSTART).getTime();
					 Long lunEnd = DateUtil.getAppointDate(Constants.LUNSEND).getTime();
					 Long dinStart = DateUtil.getAppointDate(Constants.DINSTART).getTime();
					 Long dinEnd = DateUtil.getAppointDate(Constants.DINEND).getTime();
					 if(lunStart <= currentTime && lunEnd >= currentTime){
						weekKey = week;
						foodType =  Constants.LUN;
					 }else if(dinStart <= currentTime && dinEnd >= currentTime){
						weekKey = week;
						foodType =  Constants.DIN;
					 }
					 xOrders.setOrderType(foodType); //午餐还是晚餐
					 xOrders.setOrderCategory("YY"); //订餐种类：正常订餐（ZC）预约订餐（YY）现场订餐（XC）
					 xOrders.setForWhoId(orderId);
					 xOrders.setUserId(xuser.getUserId());
					 xOrders.setInsTime(f.parse(DateWeek.printWeekdays(time)));
					 xOrders.setInsUser(xuser.getUserName());
					 xOrders.setRecId(recId); //收餐地址id
					 dao.save(xOrders);//生成订单
					  for (Map map1:map) {
						  XOrderDetail xOrderDetail =new XOrderDetail();
						  xOrderDetail.setOrderId(xOrders.getId());
						  // modify by dingzhj 
						  xOrderDetail.setInsTime(f.parse(DateWeek.printWeekdays(map1.get("weeknum").toString())));
						  xOrderDetail.setInsUser(xuser.getUserName());
						  xOrderDetail.setFoodId(map1.get("id").toString());
						  xOrderDetail.setFoodNum(Integer.parseInt(map1.get("num").toString()));
						  xOrderDetail.setPayPrice(Integer.parseInt(map1.get("price").toString())); //实付套餐价格
						  xOrderDetailDao.save(xOrderDetail);//保存订餐详情
						  //查询套餐库存
						  String num = getFoodBillReserveByFoodId(DateWeek.dinAndLun(map1.get("weeknum").toString()),map1.get("id").toString());
						  int food_remain = Integer.parseInt(num) -Integer.parseInt(map1.get("num").toString());
						  String sl = "UPDATE x_food_bill SET food_remain ='"+food_remain+"' where food_id = '"+map1.get("id").toString()+"'";
						  xFoodBillDao.executeSql(sl);//更新套餐库存
					   }
					  msgMap.put("status", "true"); 
					  msgMap.put("msg", "订餐成功"); 
				 }else{
					 msgMap.put("status", "false"); 
					 msgMap.put("msg", "账户余额不足,请联系管理员"); 
				 }
			 }else{
				 msgMap.put("status", "false");
				 msgMap.put("msg", "该账户余额不存在，请联系系统管理员");
			 }
		 }else if("1".equals(type)){
			 XDeptSum xDeptSum = null;
			 String hql = "from XDeptSum where deptId = '"+xuser.getDeptId()+"'";
			 List<XDeptSum> list = this.getPubDao().findByHql(hql);
			 if(ListUtil.isNotEmpty(list)){
				 xDeptSum = list.get(0);
				}
			 
			 if(xDeptSum!=null){
				 XDetailRecord xDetailRecord = new XDetailRecord();
				 int balance = xDeptSum.getDeptSum();
				 if(balance>=sum){
					 balance = balance - sum;
					 xDeptSum.setDeptSum(balance);
					 xDeptSum.setUpdTime(now);
					 xDeptSum.setUpdUser(xuser.getUserName());
					 xDeptSumDao.save(xDeptSum);//更新余额
					 xDetailRecord.setBalance(balance+"");
					 xDetailRecord.setChangeMoney(sum+"");
					 xDetailRecord.setType(Constants.PAY);
					 xDetailRecord.setDeptId(xDeptSum.getDeptId());
					 xDetailRecord.setRemark("订餐消费");
					 xDetailRecord.setInsTime(now);
					 xDetailRecord.setInsUser(xuser.getUserName());
					 xDetailRecordDao.save(xDetailRecord);//添加余额操作记录
					 XOrders xOrders =new XOrders();
					 xOrders.setFlag("2");
					 if(vistorName != null && vistorName!=""){
						 xOrders.setVistorName(vistorName);
					 }
					 String week = DateUtil.getWeek(f.parse(DateWeek.printWeekdays(time)));
					 Long currentTime = now.getTime();
					 Long lunStart = DateUtil.getAppointDate(Constants.LUNSTART).getTime();
					 Long lunEnd = DateUtil.getAppointDate(Constants.LUNSEND).getTime();
					 Long dinStart = DateUtil.getAppointDate(Constants.DINSTART).getTime();
					 Long dinEnd = DateUtil.getAppointDate(Constants.DINEND).getTime();
					 if(lunStart <= currentTime && lunEnd >= currentTime){
						weekKey = week;
						foodType =  Constants.LUN;
					 }else if(dinStart <= currentTime && dinEnd >= currentTime){
						weekKey = week;
						foodType =  Constants.DIN;
					 }
					 xOrders.setOrderType(foodType); //午餐还是晚餐
					 xOrders.setOrderCategory("YY"); //订餐种类：正常订餐（ZC）预约订餐（YY）现场订餐（XC）
					 xOrders.setForWhoId(xDeptSum.getDeptId());
					 xOrders.setUserId(xuser.getUserId());
					 xOrders.setInsTime(f.parse(DateWeek.printWeekdays(time)));
					 xOrders.setInsUser(xuser.getUserName());
					 xOrders.setRecId(recId); //收餐地址id
					 dao.save(xOrders);//生成订单
					 for (Map map1:map) {
						  XOrderDetail xOrderDetail =new XOrderDetail();
						  xOrderDetail.setOrderId(xOrders.getId());
						  xOrderDetail.setInsTime(f.parse(DateWeek.printWeekdays(map1.get("weeknum").toString())));
						  xOrderDetail.setInsUser(xuser.getUserName());
						  xOrderDetail.setFoodId(map1.get("id").toString());
						  xOrderDetail.setFoodNum(Integer.parseInt(map1.get("num").toString()));
						  xOrderDetail.setPayPrice(Integer.parseInt(map1.get("price").toString())); //实付套餐价格
						  xOrderDetailDao.save(xOrderDetail);//保存订餐详情
						  //查询套餐库存
						  String num = getFoodBillReserveByFoodId(DateWeek.dinAndLun(map1.get("weeknum").toString()),map1.get("id").toString());
						  int food_remain = Integer.parseInt(num) -Integer.parseInt(map1.get("num").toString());
						  String sl = "UPDATE x_food_bill SET food_remain ='"+food_remain+"' where food_id = '"+map1.get("id").toString()+"'";
						  xFoodBillDao.executeSql(sl);//更新套餐库存
					   }
					 msgMap.put("status", "true"); 
					  msgMap.put("msg", "订餐成功"); 
				 }else{
					 msgMap.put("status", "false"); 
					 msgMap.put("msg", "部门账户余额不足,请联系管理员"); 
				 }
				 
			 }
		 }
		 
		return msgMap;
	}

	@Override
	public List<Map> getFoodByCondtion(String week, String type, String foodBusiness) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  "); 
		sql.append("  xfb.id,  "); 
		sql.append("  xfb.food_id,  "); 
		sql.append("  xfb.ins_user,  "); 
		sql.append("  xfb.food_remain as food_num  ,  "); 
		sql.append("  xf.food_name,  "); 
		sql.append("  xf.food_desc,  "); 
		sql.append("  xf.food_img,  "); 
		sql.append("  xf.food_type,  "); 
		sql.append("  xf.sell_price,  "); 
		sql.append("  xf.ins_user,  "); 
		sql.append("  xd.week,  "); 
		sql.append("  xd.food_type  ft"); 
		sql.append(" FROM x_food_bill xfb  "); 
		sql.append(" LEFT JOIN x_food xf ON xf.id = xfb.food_id  "); 
		sql.append(" INNER JOIN x_dictionary xd ON xd.id = xfb.dictionary_id AND xd.week='"+week+"'  "); 
		if(StringUtil.isNotEmpty(type)){
			sql.append(" AND xd.food_type='"+type+"'");
		}
		sql.append(" where xf.ins_user='"+foodBusiness+"' and xfb.ins_user='"+foodBusiness+"'");
		
		List<Map> list = this.getPubDao().findBySqlToMap(sql.toString());
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}

	@Override
	public String getFoodBillReserveByFoodId(String weeknum, String foodId) {
		 SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date now = new Date();
		//周几
		String weekKey = weeknum.substring(0,6);
		//订餐类型
		String foodType = weeknum.substring(7);
		/*String week = DateUtil.getWeek(now);
		Long currentTime = now.getTime();
		Long lunStart = DateUtil.getAppointDate(Constants.LUNSTART).getTime();
		Long lunEnd = DateUtil.getAppointDate(Constants.LUNSEND).getTime();
		Long dinStart = DateUtil.getAppointDate(Constants.DINSTART).getTime();
		Long dinEnd = DateUtil.getAppointDate(Constants.DINEND).getTime();
		if(lunStart <= currentTime && lunEnd >= currentTime){
			weekKey = week;
			foodType =  Constants.LUN;
		}else if(dinStart <= currentTime && dinEnd >= currentTime){
			weekKey = week;
			foodType =  Constants.DIN;
		}*/
		String sql2 = "select xf.food_remain food_remain from x_food_bill xf " +
		 		"LEFT JOIN x_dictionary xd ON xf.dictionary_id = xd.id" +
		 		" where xd.food_type='"+foodType+"'" +
		 		"and   xd.week = '"+weekKey+"'" +
		 		"and xf.food_id = '"+foodId+"'";
		 List<Map> list2 = this.getPubDao().findBySqlToMap(sql2);
		 String food_remain="";
		 if(ListUtil.isNotEmpty(list2)){
			 food_remain=  (String) list2.get(0).get("food_remain");//当前套餐库存
		 }
		return food_remain;
	}

	@Override
	public List<MyOrder> getMyOrderAppoint(String userId) throws Exception {
		List<MyOrder> orderList = null;
		MyOrder myOrder = null;
		try{
			
			StringBuffer orderSql = new StringBuffer();
			orderSql.append("  SELECT 	xo.order_type, xo.order_category, xo.for_who_id,xo.id,xo.flag, xo.rec_id, date_format(xo.ins_time, '%Y-%m-%d %H:%i:%s' )  ordertime,  ");
			orderSql.append("  CASE flag   ");
			orderSql.append("  WHEN '1'  THEN (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '3' THEN (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '2' THEN (SELECT xd.dept_name FROM x_dept xd WHERE xd.dept_id = xo.for_who_id )  ");
			orderSql.append("  END orderobj,  ");
			orderSql.append("  CASE flag   ");
			orderSql.append("  WHEN '1'  THEN (SELECT xss.balance FROM x_staff_sum  xss WHERE xss.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '3' THEN (SELECT xss.balance FROM x_staff_sum  xss WHERE xss.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '2' THEN (SELECT xds.dept_sum FROM x_dept_sum xds WHERE xds.dept_id = xo.for_who_id )  ");
			orderSql.append("  END objbalance,  ");
			orderSql.append("  (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.user_id)   orderName  ");
			orderSql.append("  FROM x_orders xo   ");
			orderSql.append("  WHERE xo.for_who_id = '"+userId+"'  ");
			
			orderSql.append("  and xo.ins_time <= now()");
			orderSql.append("  and xo.ins_time >= '"+DateUtil.getLastMonth2()+"'");
			
			orderSql.append("  and xo.order_type != 'PRO' "); //非产品
			
			orderSql.append("  and xo.flag = '3' "); //领导
			orderSql.append("  and xo.del_flag ='0' order by ordertime desc");
			List<Map> l = this.getPubDao().findBySqlToMap(orderSql.toString());
			if(ListUtil.isNotEmpty(l)){
				orderList = new ArrayList<MyOrder>();
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar now = Calendar.getInstance();  
			    String dayTime =  now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH);
			    Date aMStart = formatter.parse((dayTime+" "+Constants.AMSTART)); //上开
			    Date aMSEnd = formatter.parse((dayTime+" "+Constants.AMEND)); //上结
			    Date pMStart = formatter.parse((dayTime+" "+Constants.PMSTART)); //下开
			    Date pMEnd= formatter.parse((dayTime+" "+Constants.PMEND)); //下结
				for (Map map : l) {
					myOrder = new MyOrder();
					//add dingzhj at date 2017-02-22  添加时间判断  START
					//获取订单时间
					String insTime =map.get("ordertime").toString();
					Date insDate = formatter.parse(insTime);
					String  orderType = (String) map.get("order_type");
					//判断当前时间类型（上午，下午）
					if(orderType.equals(Constants.LUN)){//上午
						if(aMStart.getTime() <=insDate.getTime() && insDate.getTime() <= aMSEnd.getTime()){
							myOrder.setIsShow("ON");
						}else{
							//myOrder.setIsShow("OFF");
						}
					}else if(orderType.equals(Constants.DIN)){//下午
						if(pMStart.getTime() <=insDate.getTime() && insDate.getTime() <= pMEnd.getTime()){
							myOrder.setIsShow("ON");
						}else{
							//myOrder.setIsShow("OFF");
						}
					}
					//add dingzhj at date 2017-02-22  添加时间判断  END
					myOrder.setBalance((String) map.get("objbalance"));
					myOrder.setOrderObj((String) map.get("orderobj"));
					myOrder.setOrderTime(map.get("ordertime").toString());
					myOrder.setId((String) map.get("id"));
					myOrder.setFlag((String) map.get("flag"));
					myOrder.setId((String) map.get("id"));
					myOrder.setForWhoId((String) map.get("for_who_id"));
					myOrder.setOrderType((String) map.get("order_type"));
					myOrder.setOrderName((String) map.get("orderName"));
					myOrder.setOrderCategory((String) map.get("order_category"));
					
					String setFoodNameStr = "";
					Integer setFoodTypeNum = 0;
					Integer orderNum=0;
					String id = (String) map.get("id");
					StringBuffer detailSql = new StringBuffer();
					detailSql.append("  SELECT xod.food_num,xod.pay_price,xf.food_name,xf.sell_price,xf.food_desc,xf.food_img,xf.id FROM x_order_detail xod  ");
					detailSql.append("  LEFT JOIN x_food xf ON xf.id = xod.food_id   ");
					detailSql.append("  WHERE order_id = '"+map.get("id").toString()+"'"    );
					List<Map> detailMap = this.getPubDao().findBySqlToMap(detailSql.toString());
					
					//订单总价
					Integer total = 0;
					if(ListUtil.isNotEmpty(detailMap)){
						for (Map m : detailMap) {
							setFoodNameStr += m.get("food_name") + ",";
							setFoodTypeNum +=Integer.valueOf(m.get("food_num").toString());
							orderNum +=setFoodTypeNum;
							
							total += Integer.parseInt(m.get("food_num").toString()) * Integer.parseInt(m.get("pay_price").toString());
							
							//获取餐饮所属的餐饮公司
							XFood xFood = xFoodService.get(m.get("id").toString());
							if(xFood != null){
								XUser foodBusiness = xUserService.findFoodBusinessByUsername(xFood.getInsUser());
								myOrder.setxUser(foodBusiness);
							}
						}
					}
					
					myOrder.setDetailList(detailMap);
					myOrder.setFoodNameStr(setFoodNameStr==""?"":setFoodNameStr.substring(0, setFoodNameStr.lastIndexOf(",")));
					myOrder.setFoodTypeNum(setFoodTypeNum.toString());
					myOrder.setMoenyTotal(total.toString());
					myOrder.setOrdeNum(orderNum.toString());
					
					if(map.get("rec_id") != null){
						//收货人信息
						XFoodSendAddress xFoodSendAddress = xFoodSendAddressService.get((String)map.get("rec_id"));
						if(xFoodSendAddress != null){
							XFoodSendRegion xFoodSendRegion = xFoodSendRegionService.get(xFoodSendAddress.getRegionId());
							xFoodSendAddress.setRegionName(xFoodSendRegion.getRegionName());
							
							myOrder.setxFoodSendAddress(xFoodSendAddress);
						}
					}
					
					orderList.add(myOrder);
			}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("获取我的订单失败！");
		}
		if(ListUtil.isNotEmpty(orderList)){
			return orderList;
		}
		
		return null;
	}

	@Override
	public List<Map> getRechargeRecord(SqlQueryFilter filter , String flag, String deptId,String companyId,String str){
		StringBuffer sb = new StringBuffer();
		if("1".equals(flag)){
			sb.append(" select  xdr.dept_id,sum(xdr.change_money) count,xc.company_name,xd.dept_name from x_detail_record  xdr ");
			sb.append(" left join x_dept xd  on xd.dept_id = xdr.dept_id ");
			sb.append(" left join x_company xc on xc.company_id = xd.company_id ");
			sb.append(" where  xdr.type ='0'  ");
			sb.append(" and xdr.dept_id   is not null     ");
			sb.append(" and xdr.remark like '%充值%'  ");
			if(filter.getConditionMap().containsKey("startTime")){
				SqlConditionSpeller s= filter.getConditionMap().get("startTime");
				sb.append(" and xdr.ins_time>= '"+s.getValue()+" 00:00:00'");
				filter.getConditionMap().remove("startTime");
			}
			if(filter.getConditionMap().containsKey("endTime")){
				SqlConditionSpeller s= filter.getConditionMap().get("endTime");
				sb.append(" and xdr.ins_time<='"+s.getValue()+" 23:59:59'");
				filter.getConditionMap().remove("endTime");
			}
			if(StringUtil.isNotEmpty(companyId)){
				sb.append(" and xd.company_id = '"+companyId+"' ");
			}
			if(StringUtil.isNotEmpty(deptId)){
				sb.append(" and xd.dept_id ='"+deptId+"' ");				
			}
			if(StringUtil.isNotEmpty(str)){
				sb.append(" and xd.dept_name like '%"+str+"%' ");	
			}
			sb.append(" group by xc.company_id,xd.dept_id   ");
		}else if("2".equals(flag)){
			sb.append(" select xu.real_name,xdr.user_id,sum(xdr.change_money) count,xc.company_name,xd.dept_name from x_detail_record  xdr ");
			sb.append(" left join x_user xu on xu.user_id = xdr.user_id ");
			sb.append(" left join x_dept xd on xu.dept_id = xd.dept_id ");
			sb.append(" left join x_company xc on xc.company_id = xd.company_id ");
			sb.append(" where  xdr.type ='0'   ");
			sb.append(" and xdr.dept_id   is null   ");
			sb.append(" and xdr.remark like '%充值%'    ");
			if(filter.getConditionMap().containsKey("startTime")){
				SqlConditionSpeller s= filter.getConditionMap().get("startTime");
				sb.append(" and xdr.ins_time>= '"+s.getValue()+" 00:00:00'");
				filter.getConditionMap().remove("startTime");
			}
			if(filter.getConditionMap().containsKey("endTime")){
				SqlConditionSpeller s= filter.getConditionMap().get("endTime");
				sb.append(" and xdr.ins_time<='"+s.getValue()+" 23:59:59'");
				filter.getConditionMap().remove("endTime");
			}
			if(StringUtil.isNotEmpty(companyId)){
				sb.append(" and xd.company_id = '"+companyId+"' ");
			}
			if(StringUtil.isNotEmpty(deptId)){
				sb.append(" and xd.dept_id ='"+deptId+"' ");				
			}
			if(StringUtil.isNotEmpty(str)){
				sb.append(" and (xu.real_name like '%"+str+"%' ");	
				sb.append(" or xd.dept_name like '%"+str+"%') ");	
			}
			sb.append(" group by xc.company_id,xu.user_id   ");
		}
		filter.setBaseSql(sb.toString());
		List<Map> list= this.query(filter);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}

	@Override
	public List<Map> getAllOrders(SqlQueryFilter qf,String flag,String deptId,String companyId,String str) {
		StringBuffer baseSql=new StringBuffer();
		//部门      
		String ids=""; 
		if (flag.equals("1")) {
			baseSql.append("SELECT 1 flag,xd.dept_id id,xd.dept_name,xy.company_name");
			baseSql.append(" from");
			baseSql.append(" x_dept xd");
			baseSql.append(" LEFT JOIN x_company xy ON xy.company_id = xd.company_id WHERE 1 = 1");
			if(companyId!=null && !"".equals(companyId)) {
				baseSql.append(" AND xd.company_id ="+companyId);
			}
			System.err.print(baseSql.toString());
			qf.setBaseSql(baseSql.toString());
			List<Map> list = this.queryV2(qf);
			
			if (ListUtil.isNotEmpty(list)) {
				for (Map map : list) {
					ids+=map.get("id")+",";
				}
			}
			
			if (ids!=null) {
				ids = ids.substring(0,ids.lastIndexOf(","));
			}
			
		}
		
		StringBuffer sbf=new StringBuffer();			
		sbf.append("SELECT DISTINCT o.id, o.order_type, o.del_flag, o.ins_time,o.send_out_flag,o.send_out_time, o.rec_flag, o.express_num, o.vistor_name, u.user_id,u.real_name,d.dept_name,d.dept_id,r.rec_area,r.rec_detail_address,r.rec_address,r.rec_phone,r.rec_name");
		sbf.append(" from x_orders o");
		sbf.append(" LEFT JOIN x_pro_order_detail pod ON o.id = pod.order_id");
		sbf.append(" LEFT JOIN x_products p ON pod.pro_id = p.pro_code");
		sbf.append(" LEFT JOIN x_user u ON u.user_id = o.user_id");
		sbf.append(" LEFT JOIN x_dept d ON u.dept_id = d.dept_id");
		sbf.append(" LEFT JOIN x_receiver_info r ON o.rec_id = r.id");
		sbf.append(" where 1=1");
		sbf.append(" and o.order_type='PRO'"); //产品标志
		sbf.append(" and o.del_flag='0'"); //未取消订单
		
		if (ids!=null&& !"".equals(ids) &&companyId!=null&& !"".equals(companyId)) {
			sbf.append(" and u.dept_id IN("+ids+")");
		}
		if (str!=null&& !"".equals(str)){
			String sql="select user_id from x_user where 1=1 and real_name like '"+str+"%'";
			List list = xUserDao.findBySql(sql);
			String user_id = (String)list.get(0);
			sbf.append(" and o.user_id="+user_id+"");
		}
		if (deptId!=null&& !"".equals(deptId)) {
			sbf.append(" and d.dept_id="+deptId+"");
		}
		
		sbf.append(" ORDER BY o.ins_time DESC");
		qf.setBaseSql(sbf.toString());
		
		List<Map> map=this.query(qf);
		
		return map;
	}
	
	
	@Override
	public List<Map> getMyOrderById2(String orderId) {
		StringBuffer detailSql = new StringBuffer();
		
		detailSql.append("  select xc.company_name company_name, sum(xod.food_num) food_num, xf.food_name,xf.ins_user, xd.dept_name dept_name, xu.real_name, xo.ins_time, xo.vistor_name ");
		detailSql.append("  from x_order_detail xod  ");
		detailSql.append("  LEFT JOIN x_orders xo ON xod.order_id = xo.id ");
		detailSql.append("  LEFT JOIN x_food xf ON xf.id = xod.food_id  ");
		detailSql.append("  LEFT JOIN x_user xu ON xo.user_id = xu.user_id ");
		detailSql.append("  LEFT JOIN x_company xc ON xu.company_id = xc.company_id ");
		detailSql.append("  LEFT JOIN x_dept xd ON xd.dept_id = xu.dept_id ");
		detailSql.append("  WHERE xo.id = '"+orderId+"'"    );
		detailSql.append("  GROUP BY xf.id " );
		
		List<Map> detailMap = this.getPubDao().findBySqlToMap(detailSql.toString());
		if(ListUtil.isNotEmpty(detailMap)){
			return detailMap;
		}
		return detailMap;
	}
	

	@Override
	public List<Map> getCurrentXCOrder(SqlQueryFilter qf, String startTime,
			String endTime, XUser xuser) {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT xo.id, xo.order_type, xo.user_id, xo.vistor_name, xo.ins_time, xu.real_name, xd.dept_name, xc.company_name, ");
		//hibernate不支持GROUP_CONCAT()函数
		//sql.append(" GROUP_CONCAT(xf.food_name) food_name, xf.sell_price, SUM(xod.food_num) food_num, SUM(xf.sell_price*xod.food_num) order_sum, xuc.food_company_name ");
		sql.append(" xf.food_name, xf.sell_price, xod.food_num,xod.pay_price, SUM(xod.food_num) total_food_num, SUM(xod.pay_price*xod.food_num) order_sum, xuc.food_company_name ");
		sql.append(" FROM x_orders xo ");
		sql.append(" LEFT JOIN x_user xu ON xu.user_id=xo.user_id ");
		sql.append(" LEFT JOIN x_dept xd ON xd.dept_id=xu.dept_id ");
		sql.append(" LEFT JOIN x_company xc ON xc.company_id=xd.company_id ");
		sql.append(" LEFT JOIN x_order_detail xod ON xo.id=xod.order_id ");
		sql.append(" LEFT JOIN x_food xf ON xf.id=xod.food_id ");
		sql.append(" LEFT JOIN x_user xuc ON xuc.user_name=xf.ins_user ");
		sql.append(" WHERE xo.del_flag='0' AND xo.order_category='XC' "); //现场的订单
		
		if(startTime != null && startTime != ""){
			sql.append(" AND xo.ins_time >= '"+startTime+"' ");
		}
		if(endTime != null && endTime != ""){
			sql.append(" AND xo.ins_time <= '"+endTime+"' ");	
		}
		
		sql.append(" AND xf.ins_user='"+xuser.getUserName()+"' "); //所属餐饮公司的订单
		sql.append(" GROUP BY xo.id ");
		sql.append(" ORDER BY xo.ins_time DESC ");
		
		qf.setBaseSql(sql.toString());
		List<Map> list = this.query(qf);
		if(ListUtil.isNotEmpty(list)){
			for (Map map : list) {
				String food_nameNew = "";
				String sell_priceNew = "";
				String food_numNew = "";
				String hql = "from XOrderDetail where orderId = ?";
				Object[] objs = {map.get("id")};
				List<XOrderDetail> xOrderDetailList = xOrderDetailDao.findByHql(hql, objs);
				if(xOrderDetailList != null && xOrderDetailList.size() > 1){
					for(int i=0; i<xOrderDetailList.size(); i++){
						XFood xFood = xFoodService.get(xOrderDetailList.get(i).getFoodId());
						if(i != xOrderDetailList.size()-1){
							food_nameNew = food_nameNew + xFood.getFoodName() + ",";
							sell_priceNew = sell_priceNew + xFood.getSellPrice() + ",";
							food_numNew = food_numNew + xOrderDetailList.get(i).getFoodNum() + ",";
						}else{
							food_nameNew = food_nameNew + xFood.getFoodName();
							sell_priceNew = sell_priceNew + xFood.getSellPrice();
							food_numNew = food_numNew + xOrderDetailList.get(i).getFoodNum();
						}
					}
					map.put("food_name", food_nameNew);
					map.put("sell_price", sell_priceNew);
					map.put("food_num", food_numNew);
				}
			}
			return list;
		}
		return null;
	}

	@Override
	public void updateOrderRecStatus() {
		String sql = "UPDATE x_orders SET rec_flag = 2 WHERE order_type = 'PRO' AND rec_flag != 0 AND now() >= DATE_ADD(send_out_time,INTERVAL 7 DAY)";
		this.dao.executeSql(sql);
	}

	@Override
	public String getCurrentStaffCompanyId(XUser xuser) {
		String sql = "SELECT xc.company_id FROM x_company xc, x_dept xd WHERE xd.dept_id="+xuser.getDeptId()+" AND xd.company_id=xc.company_id";
		List companyIdList = xDeptDao.findBySql(sql);
		if(companyIdList != null && companyIdList.size() > 0){
			String companyId = (String)companyIdList.get(0);
			return companyId;
		}
		return null;
	}

}
