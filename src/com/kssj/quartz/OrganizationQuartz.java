package com.kssj.quartz;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
import com.kssj.base.datasource.DataSourceConst;
import com.kssj.base.datasource.DataSourceContextHolder;

/**
 * 组织架构同步定时任务
 * 
 * @author zhangxuejiao
 * 
 */
public class OrganizationQuartz {

	private Logger logger = Logger.getLogger(OrganizationQuartz.class);

	public void synchronData() {
		try {
			logger.info(new Date() + " 执行定时任务开始...");

			logger.info(new Date() + " 执行《导入公司信息》定时任务开始...");
			exportCompany();
			logger.info(new Date() + " 执行《导入公司信息》定时任务结束...");
			
			logger.info(new Date() + " 执行《导入部门信息和部门余额》定时任务开始...");
			exportDept();
			logger.info(new Date() + " 执行《导入部门信息和部门余额》定时任务结束...");
			
			logger.info(new Date() + " 执行《导入员工信息和员工余额》定时任务开始...");
			exportStaff();
			logger.info(new Date() + " 执行《导入员工信息和员工余额》定时任务结束...");

			logger.info(new Date() + " 执行定时任务结束...");
		} catch (Exception e) {
			logger.error(new Date() + " 执行定时任务出现异常...");
		}
	}

	/**
	 * 导入公司信息
	 */
	public static void exportCompany() {
		ClassPathXmlApplicationContext context = null;
		try {
			String[] contextLocations = new String[] { "classpath:spring/app-context.xml" };
			System.out.println("进入main方法.....");
			System.out.println("加载完成配置文件.....");
			context = new ClassPathXmlApplicationContext(contextLocations);
			DataSourceContextHolder holder = new DataSourceContextHolder();
			XCompanyDao dao = (XCompanyDao) context.getBean("xCompanyDao");

			holder.setDataSourceType(DataSourceConst.SQLSERVER);
			String sql = "select * from dbo.HrmSubCompany where canceled is null or canceled ='0'";
			List<Map> list = dao.findBySqlToMap(sql, null, 0, 0);
			XCompany xc = new XCompany();
			holder.setDataSourceType(DataSourceConst.MYSQL);
			for (int i = 0; i < list.size(); i++) {
				String hql = "from XCompany where companyId="+ list.get(i).get("id").toString();
				List<XCompany> xCompanyList = dao.findByHql(hql, null);
				if (xCompanyList != null && xCompanyList.size() > 0) { // 如果存在，执行更新操作
					XCompany xCompany = xCompanyList.get(0);
					xc.setId(xCompany.getId());
					xc.setCompanyId(list.get(i).get("id").toString());
					xc.setCompanyName(list.get(i).get("subcompanyname").toString());
					xc.setCompanyDesc(list.get(i).get("subcompanydesc").toString());
					xc.setParentId(list.get(i).get("supsubcomid").toString());
					if (xCompany.getInsTime() != null) {
						xc.setInsTime(xCompany.getInsTime());
						xc.setUpdTime(new Date());
					} else {
						xc.setInsTime(new Date());
					}
					dao.save(xc);
				} else { // 如果不存在，执行插入操作
					xc.setId(null);
					xc.setCompanyId(list.get(i).get("id").toString());
					xc.setCompanyName(list.get(i).get("subcompanyname").toString());
					xc.setCompanyDesc(list.get(i).get("subcompanydesc").toString());
					xc.setParentId(list.get(i).get("supsubcomid").toString());
					xc.setInsTime(new Date());
					dao.save(xc);
				}
			}

			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("core 服务已经启动！按任意键退出");
		System.out.println("输入任意字符停止!");
		context.stop();
		System.out.println("core 服务停止！.....");
	}

	/**
	 * 导入部门信息和部门余额
	 */
	public static void exportDept() {
		ClassPathXmlApplicationContext context = null;
		try {
			String[] contextLocations = new String[] { "classpath:spring/app-context.xml" };
			System.out.println("进入main方法.....");
			System.out.println("加载完成配置文件.....");
			context = new ClassPathXmlApplicationContext(contextLocations);
			DataSourceContextHolder holder = new DataSourceContextHolder();
			XDeptDao deptDao = (XDeptDao) context.getBean("xDeptDao");
			XDeptSumDao deptSumDao = (XDeptSumDao) context.getBean("xDeptSumDao");

			holder.setDataSourceType(DataSourceConst.SQLSERVER);
			String sql = "select id,departmentname,subcompanyid1,supdepid from dbo.HrmDepartment where canceled is null or canceled ='0'";
			List<Map> list = deptDao.findBySqlToMap(sql, null, 0, 0);
			XDept xd = new XDept();
			XDeptSum xds = new XDeptSum();
			holder.setDataSourceType(DataSourceConst.MYSQL);
			for (int i = 0; i < list.size(); i++) {
				// 部门主要信息
				String hql = "from XDept where deptId="+ list.get(i).get("id").toString();
				List<XDept> xDeptList = deptDao.findByHql(hql, null);
				if (xDeptList != null && xDeptList.size() > 0) { // 如果存在，执行更新操作
					XDept xDept = xDeptList.get(0);
					xd.setId(xDept.getId());
					xd.setDeptId(list.get(i).get("id").toString());
					xd.setDeptName(list.get(i).get("departmentname").toString());
					xd.setCompanyId(list.get(i).get("subcompanyid1").toString());
					xd.setSupdepid(list.get(i).get("supdepid").toString());
					xd.setRoomNum(xDept.getRoomNum());
					if (xDept.getInsTime() != null) {
						xd.setInsTime(xDept.getInsTime());
						xd.setUpdTime(new Date());
					} else {
						xd.setInsTime(new Date());
					}
					deptDao.save(xd);
				} else { // 如果不存在，执行插入操作
					xd.setId(null);
					xd.setDeptId(list.get(i).get("id").toString());
					xd.setDeptName(list.get(i).get("departmentname").toString());
					xd.setCompanyId(list.get(i).get("subcompanyid1").toString());
					xd.setSupdepid(list.get(i).get("supdepid").toString());
					xd.setInsTime(new Date());
					deptDao.save(xd);
				}

				// 部门余额信息
				String hql2 = "from XDeptSum where deptId="+ list.get(i).get("id").toString();
				List<XDeptSum> xDeptSumList = deptSumDao.findByHql(hql2, null);
				if (xDeptSumList != null && xDeptSumList.size() > 0) {
					XDeptSum xDeptSum = xDeptSumList.get(0);
					xds.setId(xDeptSum.getId());
					xds.setDeptSum(xDeptSum.getDeptSum());
					if (xDeptSum.getInsTime() != null) {
						xds.setInsTime(xDeptSum.getInsTime());
						xds.setUpdTime(new Date());
					} else {
						xds.setInsTime(new Date());
					}
					xds.setDeptId(list.get(i).get("id").toString());
					deptSumDao.save(xds);
				} else {
					xds.setId(null);
					xds.setDeptSum(0);
					xds.setInsTime(new Date());
					xds.setDeptId(list.get(i).get("id").toString());
					deptSumDao.save(xds);
				}

			}

			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("core 服务已经启动！按任意键退出");
		System.out.println("输入任意字符停止!");
		context.stop();
		System.out.println("core 服务停止！.....");
	}

	/**
	 * 导入员工信息和员工余额
	 */
	public static void exportStaff() {
		ClassPathXmlApplicationContext context = null;
		try {
			String[] contextLocations = new String[] { "classpath:spring/app-context.xml" };
			System.out.println("进入main方法.....");
			System.out.println("加载完成配置文件.....");
			context = new ClassPathXmlApplicationContext(contextLocations);
			DataSourceContextHolder holder = new DataSourceContextHolder();
			XUserDao userDao = (XUserDao) context.getBean("xUserDao");
			XStaffSumDao xStaffSumDao = (XStaffSumDao) context.getBean("xStaffSumDao");

			holder.setDataSourceType(DataSourceConst.SQLSERVER);
			String sql = "select id,loginid,lastname,telephone,departmentid,email,subcompanyid1,jobtitle,seclevel,managerid,status from dbo.HrmResource where status<=3";
			List<Map> list = userDao.findBySqlToMap(sql, null, 0, 0);
			holder.setDataSourceType(DataSourceConst.MYSQL);
			XUser xu = new XUser();
			XStaffSum staffSum = new XStaffSum();
			for (int i = 0; i < list.size(); i++) {

				// 员工主要信息
				String hql = "from XUser where user_id="+ String.valueOf(Math.round(Double.parseDouble(list.get(i).get("id").toString())));
				List<XUser> xUserList = userDao.findByHql(hql, null);
				if (xUserList != null && xUserList.size() > 0) {
					XUser xUser = xUserList.get(0);
					xu.setId(xUser.getId());
					xu.setUserId(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("id").toString()))));
					if (list.get(i).get("loginid") != null) {
						xu.setUserName(list.get(i).get("loginid").toString());
					}
					if (list.get(i).get("lastname") != null) {
						xu.setRealName(list.get(i).get("lastname").toString());
					}
					if (list.get(i).get("telephone") != null) {
						xu.setTel(list.get(i).get("telephone").toString());
					}
					if (list.get(i).get("departmentid") != null) {
						xu.setDeptId(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("departmentid").toString()))));
					}
					if (list.get(i).get("email") != null) {
						xu.setEmail(list.get(i).get("email").toString());
					}
					if (list.get(i).get("subcompanyid1") != null) {
						xu.setCompanyId(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("subcompanyid1").toString()))));
					}
					if (list.get(i).get("jobtitle") != null) {
						xu.setJobtitle(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("jobtitle").toString()))));
					}
					if (list.get(i).get("seclevel") != null) {
						xu.setSeclevel(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("seclevel").toString()))));
					}
					if (list.get(i).get("managerid") != null) {
						xu.setManagerid(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("managerid").toString()))));
					}
					if (list.get(i).get("status") != null) {
						xu.setStatus(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("status").toString()))));
					}
					xu.setType("1");
					xu.setDelFlag("0");
					if (xUser.getInsTime() != null) {
						xu.setInsTime(xUser.getInsTime());
						xu.setUpdTime(new Date());
					} else {
						xu.setInsTime(new Date());
					}
					userDao.save(xu);
				} else {
					xu.setId(null);
					xu.setUserId(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("id").toString()))));
					if (list.get(i).get("loginid") != null) {
						xu.setUserName(list.get(i).get("loginid").toString());
					}
					if (list.get(i).get("lastname") != null) {
						xu.setRealName(list.get(i).get("lastname").toString());
					}
					if (list.get(i).get("telephone") != null) {
						xu.setTel(list.get(i).get("telephone").toString());
					}
					if (list.get(i).get("departmentid") != null) {
						xu.setDeptId(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("departmentid").toString()))));
					}
					if (list.get(i).get("email") != null) {
						xu.setEmail(list.get(i).get("email").toString());
					}
					if (list.get(i).get("subcompanyid1") != null) {
						xu.setCompanyId(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("subcompanyid1").toString()))));
					}
					if (list.get(i).get("jobtitle") != null) {
						xu.setJobtitle(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("jobtitle").toString()))));
					}
					if (list.get(i).get("seclevel") != null) {
						xu.setSeclevel(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("seclevel").toString()))));
					}
					if (list.get(i).get("managerid") != null) {
						xu.setManagerid(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("managerid").toString()))));
					}
					if (list.get(i).get("status") != null) {
						xu.setStatus(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("status").toString()))));
					}
					xu.setType("1");
					xu.setDelFlag("0");
					xu.setInsTime(new Date());
					userDao.save(xu);
				}

				// 员工余额信息
				String hql2 = "from XStaffSum where user_id="+String.valueOf(Math.round(Double.parseDouble(list.get(i).get("id").toString())));
				List<XStaffSum> xStaffSumList = xStaffSumDao.findByHql(hql2, null);
				if (xStaffSumList != null && xStaffSumList.size() > 0) {
					XStaffSum xStaffSum = xStaffSumList.get(0);
					staffSum.setId(xStaffSum.getId());
					staffSum.setUserId(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("id").toString()))));
					if (xStaffSum.getInsTime() != null) {
						staffSum.setInsTime(xStaffSum.getInsTime());
						staffSum.setUpdTime(new Date());
					} else {
						staffSum.setInsTime(new Date());
					}
					staffSum.setBalance(xStaffSum.getBalance());
					xStaffSumDao.save(staffSum);
				} else {
					staffSum.setId(null);
					staffSum.setUserId(String.valueOf(Math.round(Double.parseDouble(list.get(i).get("id").toString()))));
					staffSum.setInsTime(new Date());
					staffSum.setBalance("0");
					xStaffSumDao.save(staffSum);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("core 服务已经启动！按任意键退出");
		System.out.println("输入任意字符停止!");
		context.stop();
		System.out.println("core 服务停止！.....");
	}

}
