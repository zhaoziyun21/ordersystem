package com.kssj.auth.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.kssj.auth.dao.XCompanyDao;
import com.kssj.auth.dao.XDeptDao;
import com.kssj.auth.dao.XDeptSumDao;
import com.kssj.auth.dao.XStaffSumDao;
import com.kssj.auth.dao.XUserDao;
import com.kssj.auth.model.XCompany;
import com.kssj.auth.model.XDept;
import com.kssj.auth.model.XDeptSum;
import com.kssj.auth.model.XStaffSum;
import com.kssj.auth.model.XUser;
import com.kssj.auth.service.XUserService;
import com.kssj.base.datasource.DataSourceConst;
import com.kssj.base.datasource.DataSourceContextHolder;
import com.kssj.base.util.Constants;
import com.kssj.base.util.ListUtil;
import com.kssj.base.util.StringUtil;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.impl.GenericServiceImpl;
import com.sun.xml.internal.bind.v2.TODO;

public class XUserServiceImpl extends GenericServiceImpl<XUser, String> implements XUserService{

	private XUserDao dao;
	private XStaffSumDao xStaffSumDao;
	private XDeptDao xDeptDao;
	private XDeptSumDao xDeptSumDao;
	private XCompanyDao xCompanyDao;
	public XUserServiceImpl(XUserDao dao,XStaffSumDao xStaffSumDao,XDeptDao xDeptDao,XDeptSumDao xDeptSumDao,XCompanyDao xCompanyDao) {
		super(dao);
		this.dao = dao;
		this.xStaffSumDao = xStaffSumDao;
		this.xDeptDao = xDeptDao;
		this.xDeptSumDao = xDeptSumDao;
		this.xCompanyDao = xCompanyDao;
		
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> getXUser(SqlQueryFilter filter,String str) {
		StringBuffer sql = new StringBuffer();
		sql.append("select u.id,u.user_name,u.password,u.food_company_name,u.real_name,u.email,u.tel,r.role_name,r.id as rid,u.del_flag from x_user u ");
		sql.append("   left join x_user_role ur on ur.user_id = u.id ");
		sql.append("   left join x_role r on ur.role_id = r.id");
		sql.append(" where 1=1");
		if(StringUtil.isNotEmpty(str)){
			sql.append("   AND (u.user_name LIKE '%"+str+"%' OR u.real_name LIKE '%"+str+"%' OR u.tel LIKE '%"+str+"%') ");
		}
		filter.setBaseSql(sql.toString());
		return this.query(filter);
	}


	@Override
	public XUser getUserById(String userid) {
		// TODO Auto-generated method stub
		return this.get(userid);
	}


	@Override
	public XUser addUser(XUser xuser) {
		return this.dao.saveOrUpdate(xuser);
	}


	@Override
	public void delUser(String userid) {
		// TODO Auto-generated method stub
		this.dao.remove(userid);
	}


	@SuppressWarnings("unchecked")
	@Override
	public XUser loginVerification(XUser xuser) {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" from XUser x where x.userName = '"+xuser.getUserName()+"' and x.password = '"+xuser.getPassword()+"' and delFlag = '0'");
		List<XUser> xuList = this.getPubDao().findByHql(hql.toString());
		if(xuList != null && xuList.size() > 0){
			return xuList.get(0);
		}
		return null;
	}


	@Override
	public List<Map> getRoleId(String userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select role_id from x_user_role ur ");
		sql.append(" where ur.user_id = '"+userId+"'");
		List<Map> l = this.getPubDao().findBySqlToMap(sql.toString(), null);
		if(ListUtil.isNotEmpty(l)){
			return l;
		}
		return null;
	}


	@Override
	public boolean orderLoginVerification(XUser xuser) {
//		DataSourceContextHolder holder = new DataSourceContextHolder();
//		holder.setDataSourceType(DataSourceConst.SQLSERVER);
		StringBuffer sql = new StringBuffer();
		sql.append(" select id from dbo.HrmResource hr where hr.status<=4 and hr.loginid = '"+xuser.getUserName()+"' and hr.password = '"+xuser.getPassword()+"'");
		List<Map> xuList = this.dao.findBySqlToMap(sql.toString(),null,0,0);
//		holder.setDataSourceType(DataSourceConst.MYSQL);
		if(xuList != null && xuList.size() > 0){
			return true;
		}
		
		return false;
	}


	@Override
	public XUser getUserByUserName(XUser user) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from XUser x where x.userName = '"+user.getUserName()+"'");
		if(StringUtil.isNotEmpty(user.getType())){
			if(Constants.FOOD_COMPANY_TYPE.equals(user.getType())){
				
				hql.append(" and x.type = '"+user.getType()+"'");
			}
		}
		List<XUser> xuList = this.getPubDao().findByHql(hql.toString());
		if(xuList != null && xuList.size() > 0){
			return xuList.get(0);
		}
		return null;
	}


	@Override
	public XUser addOaUser(String username) {
		DataSourceContextHolder holder = new DataSourceContextHolder();
		holder.setDataSourceType(DataSourceConst.SQLSERVER);
		String sql = "select id,loginid,lastname,telephone,departmentid,email,subcompanyid1,jobtitle,seclevel,managerid,status from dbo.HrmResource where status<=3 and loginid ='"+username+"'";
		List<Map> list = this.dao.findBySqlToMap(sql, null, 0, 0);
		XUser xu = new XUser();
		if(ListUtil.isNotEmpty(list)){
			for (int i = 0; i < list.size(); i++) {
				xu.setId(null);
				xu.setUserId(list.get(i).get("id").toString());
				if(list.get(i).get("loginid")!=null){
					xu.setUserName(list.get(i).get("loginid").toString());
				}
				
				if(list.get(i).get("lastname")!=null){
					xu.setRealName(list.get(i).get("lastname").toString());
				}
				if(list.get(i).get("telephone")!=null){
					xu.setTel(list.get(i).get("telephone").toString());
				}
				if(list.get(i).get("departmentid")!=null){
					xu.setDeptId(list.get(i).get("departmentid").toString());
				}
				if(list.get(i).get("email")!=null){
					xu.setEmail(list.get(i).get("email").toString());
				}
				if(list.get(i).get("subcompanyid1")!=null){
					xu.setCompanyId(list.get(i).get("subcompanyid1").toString());
				}
				if(list.get(i).get("jobtitle")!=null){
					xu.setJobtitle(list.get(i).get("jobtitle").toString());
				}
				if(list.get(i).get("seclevel")!=null){
					xu.setSeclevel(list.get(i).get("seclevel").toString());
				}
				if(list.get(i).get("managerid")!=null){
					xu.setManagerid(list.get(i).get("managerid").toString());
				}
				if(list.get(i).get("status")!=null){
					xu.setStatus(list.get(i).get("status").toString());
				}
				xu.setType("1");
				xu.setDelFlag("0");
				xu.setInsTime(new Date());
			}
			holder.setDataSourceType(DataSourceConst.MYSQL);
			XUser u =  this.dao.save(xu);
			XStaffSum xss = new XStaffSum();
			xss.setBalance("0");
			xss.setUserId(u.getUserId());
			xss.setInsTime(new Date());
			xss.setInsUser(u.getUserName());
			this.xStaffSumDao.save(xss);
			return u;
		}
		return null;
	}
	
	@Override
	public Long updDelFlag(XUser u) {
		Long w = null;
		w = dao.update("update XUser set del_flag =?,upd_time=?,upd_user=? where Id = ?", new Object[]{u.getDelFlag(),u.getUpdTime(),u.getUpdUser(),u.getId()});
//		w = dao.save(u);
		return w;
	}


	 
	@Override
	public void updOaUser(XUser u) {
		DataSourceContextHolder holder = new DataSourceContextHolder();
		holder.setDataSourceType(DataSourceConst.SQLSERVER);
		StringBuffer sb = new StringBuffer();
		sb.append(" select hr.id userId,hr.loginid userName,hr.lastname realName, ");
		sb.append(" hd.id deptId,hd.departmentname deptName,hd.supdepid, ");
		sb.append(" hsc.id companyId,hsc.subcompanyname companyName,hsc.supsubcomid parentId,hsc.subcompanydesc companydesc from dbo.HrmResource  hr ");
		sb.append(" left join dbo.HrmDepartment hd on hd.id = hr.departmentid ");
		sb.append(" left join dbo.HrmSubCompany hsc on hsc.id=hr.subcompanyid1 ");
		sb.append(" where status<=3 and loginid ='"+u.getUserName()+"' ");
		List<Map> list = this.dao.findBySqlToMap(sb.toString(), null, 0, 0);
		holder.setDataSourceType(DataSourceConst.MYSQL);
		if(ListUtil.isNotEmpty(list)){
				String oldDeptId = u.getDeptId();
				String oldCompanyId = u.getCompanyId();
				String newDeptId = list.get(0).get("deptId").toString();
				String newCompanyId = list.get(0).get("companyId").toString();
				if(!oldDeptId.equals(newDeptId)){
					String deptSql = "select * from x_dept where dept_id ='"+newDeptId+"'";
					List<Map> deptList = this.dao.findBySqlToMap(deptSql, null, 0, 0);
					//TODO更新deptid
					u.setDeptId(newDeptId);
					if(!ListUtil.isNotEmpty(deptList)){
						//TODO添加dept,添加deptSum
						XDept xd = new XDept();
						XDeptSum xds = new XDeptSum();
						xd.setId(null);
						xd.setDeptId(list.get(0).get("deptId").toString());
						xd.setDeptName(list.get(0).get("deptName").toString());
						xd.setCompanyId(list.get(0).get("companyId").toString());
						xd.setSupdepid(list.get(0).get("supdepid").toString());
						// modify by dingzhj at date 2017-03-01
						xd.setUpdTime(new Date());
						xDeptDao.save(xd);
						// add dingzhj at date 2017-03-01
						xds.setId(null);
						xds.setDeptSum(0);
						xds.setInsTime(new Date());
						xds.setDeptId(list.get(0).get("deptId").toString());
						xDeptSumDao.save(xds);
						
					}
					
					if(!oldCompanyId.equals(newCompanyId)){
						//更新companyId	
						u.setCompanyId(newCompanyId);
						//TODO 检查companyId
						String companySql = "select * from x_company where company_id ='"+newCompanyId+"'";
						List<Map> companyList = this.dao.findBySqlToMap(companySql, null, 0, 0);
						//mysql库查询companyId是否存在
						if(!ListUtil.isNotEmpty(companyList)){
							//不存在，添加公司
								XCompany xc = new XCompany();
								xc.setId(null);
								xc.setCompanyId(list.get(0).get("companyId").toString());
								xc.setCompanyName(list.get(0).get("companyName").toString());
								xc.setCompanyDesc(list.get(0).get("companydesc").toString());
								xc.setParentId(list.get(0).get("parentId").toString());
								xc.setInsTime(new Date());
								xCompanyDao.save(xc);
							}
						}
						u.setUpdTime(new Date());
						this.dao.save(u);
					}
		}
	}

	@Override
	public List<Map> getOrderXUser(SqlQueryFilter filter,String comId,String deptId,String reaName) {
		
		String sql = "SELECT xss.balance,xd.dept_name,xu.*   FROM x_user xu LEFT JOIN x_dept xd ON xu.dept_id = xd.dept_id "
					+"LEFT JOIN x_staff_sum xss ON xss.user_id = xu.user_id "
					+"WHERE 1 = 1 and xu.type='1' and xu.del_flag ='0' ";
		if(StringUtil.isNotEmpty(comId)){
			sql += " and xu.company_id = '"+comId+"'";
		}
		
		if(StringUtil.isNotEmpty(deptId)){
			sql += " and xu.dept_id = '"+deptId+"'";
		}
		if(StringUtil.isNotEmpty(reaName)){
			sql += " and xu.real_name like '%"+reaName+"%'";
		}
		String roleSql = "SELECT xur.user_id, xr.role_name FROM x_user_role xur LEFT JOIN  x_role xr ON xur.role_id = xr.id";
		filter.setBaseSql(sql);
		List<Map> l = this.query(filter);
		List<Map> roleList = this.getPubDao().findBySqlToMap(roleSql);
		if(ListUtil.isNotEmpty(l)){
			for (int i = 0; i < l.size(); i++) {
				if(ListUtil.isNotEmpty(roleList)){
					String role = "";
					for (int j = 0; j < roleList.size(); j++) {
						if(l.get(i).get("user_id").toString().equals(roleList.get(j).get("user_id"))){
							role += roleList.get(j).get("role_name") + ",";
						}
					}
					if(role != ""){
						role = role.substring(0, role.lastIndexOf(","));
					}
					l.get(i).put("role_name", role);
				}
			}
			return l;
		}
		return null;
	}


	@Override
	public XUser getByUserId(String userId) {
		String hql = "from XUser where userId = '"+userId+"'";
		
		List<XUser> l = this.getPubDao().findByHql(hql);
		if(ListUtil.isNotEmpty(l)){
			return l.get(0);
		}
		return null;
	}


	@Override
	public Map getDeptRoleByUserIdAndRoleId(String userId, String roleId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM x_user_role xur WHERE xur.user_id IN (SELECT user_id FROM x_user WHERE dept_id = (SELECT dept_id FROM x_user WHERE user_id = '"+userId+"')) AND xur.role_id = '"+roleId+"'");
		List<Map> list = this.getPubDao().findBySqlToMap(sql.toString());
		if(ListUtil.isNotEmpty(list)){
			return list.get(0);//部门专用角色和部门联系人是唯一的，所以返回一条
		}
		
		return null;
	}


	@Override
	public void delRoleByUserIdAndRoleId(String userId, String roleId) {
		String sql = " DELETE FROM x_user_role WHERE user_id = '"+userId+"' AND role_id='"+roleId+"'";
		this.dao.executeSql(sql);
	}


	@Override
	public List<Map> getDeptByCompanyId(String companyId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT dept_id id,dept_name name FROM x_dept xd WHERE xd.company_id ='"+companyId+"'");
		List<Map> list = this.getPubDao().findBySqlToMap(sql.toString());
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}


	@Override
	public List<Map> findDept(SqlQueryFilter filter, String companyId, String deptId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT dept_id id,dept_name name,xc.company_name,xd.room_num FROM x_dept xd ");
		sql.append("left join x_company xc on xc.company_id =xd.company_id ");
		if(!"".equals(companyId)){
			sql.append("  WHERE xd.company_id ='"+companyId+"'  ");
		}
		if(!"".equals(deptId)){
			sql.append(" WHERE xd.dept_id ='"+deptId+"'  ");
		}
		filter.setBaseSql(sql.toString());
		List<Map> list = this.query(filter);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}


	@Override
	public Long editRoomNum(String id, String roomNum) {
		String sql ="update XDept set roomNum =? where deptId = ?";
		Long i = this.dao.update(sql, new Object[]{roomNum,id});
		return i;
	}


	/**
	 * 查询所有美食商家
	 */
	@Override
	public List<XUser> findFoodBusiness() {
		String hql = "from XUser where type='2' and delFlag='0'";
		Object[] objs = {};
		List<XUser> foodBusinessList = this.dao.findByHql(hql, objs);
		if(foodBusinessList != null && foodBusinessList.size() > 0){
			return foodBusinessList;
		}
		return null;
	}

	/**
	 * 通过username查询所有美食商家
	 */
	@Override
	public XUser findFoodBusinessByUsername(String foodBusiness) {
		String hql = "from XUser where userName='"+foodBusiness+"'";
		Object[] objs = {};
		List<XUser> foodBusinessList = this.dao.findByHql(hql, objs);
		if(foodBusinessList != null && foodBusinessList.size() > 0){
			return foodBusinessList.get(0);
		}
		return null;
	}

}
