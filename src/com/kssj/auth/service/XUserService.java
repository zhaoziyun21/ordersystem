package com.kssj.auth.service;

import java.util.List;
import java.util.Map;

import com.kssj.auth.model.XDept;
import com.kssj.auth.model.XUser;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.GenericService;

public interface XUserService extends GenericService<XUser, String>{
	
	/**
	 * @method: getAllXUser
	 * @Description: 
	 * @return
	 * @author : lig
	 * @date 2016-9-19 下午5:26:08
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getXUser(SqlQueryFilter filter,String str);
	
	
	/**
	 * @method: getUserById
	 * @Description: 根据id查询
	 * @param userid
	 * @return
	 * @author : lig
	 * @date 2016-9-20 上午11:30:31
	 */
	public XUser getUserById(String userid);
	
	/**
	 * @method: addUser
	 * @Description: 新增用户
	 * @param xuser
	 * @author : lig
	 * @date 2016-9-20 下午3:33:09
	 */
	public XUser addUser(XUser xuser);
	
	
	/**
	 * @method: delUser
	 * @Description: 删除用户
	 * @param xuser
	 * @author : lig
	 * @date 2016-9-20 下午4:48:24
	 */
	public void delUser(String userid);
	
	/**
	 * @method: loginVerification
	 * @Description: 登录验证
	 * @param xuser
	 * @return
	 * @author : lig
	 * @date 2016-9-23 下午2:57:59
	 */
	public XUser loginVerification(XUser xuser);
	public boolean orderLoginVerification(XUser xuser);
	
	/**
	 * 
	 * @method: getRoleId
	 * @Description: TODO
	 * @param userId
	 * @return
	 * @author : zhaoziyun
	 * @date 2017-2-23 下午4:11:03
	 */
	public List<Map> getRoleId(String userId);
	
	/**
	 * @method: getOrderXUser
	 * @Description: 订餐用户列表
	 * @param filter
	 * @return
	 * @author : lig
	 * @param userName 
	 * @date 2017-3-1 下午6:21:20
	 */
	public List<Map> getOrderXUser(SqlQueryFilter filter,String comId,String deptId, String userName);
	/**
	 * 
	 * @method: getUserByUserName
	 * @Description: 通过用户账号账号用户信息
	 * @param xuser
	 * @return
	 * @author : zhaoziyun
	 * @param type 
	 * @date 2017-3-2 下午1:33:40
	 */
	public XUser getUserByUserName(XUser xuser);
	/**
	 * 
	 * @method: addOaUser
	 * @Description: 添加数据库中没有的用户
	 * @param username
	 * @return
	 * @author : zhaoziyun
	 * @date 2017-3-2 下午3:21:50
	 */
	public XUser addOaUser(String username);
	/**
	 * 
	 * @method: updDelFlag
	 * @Description: 离职
	 * @param username
	 * @return
	 * @author : zhaoziyun
	 * @date 2017-3-2 下午3:21:50
	 */
	public Long updDelFlag(XUser u);
	/**
	 * 
	 * @method: updOaUser
	 * @Description: 更新数据库与oa不一致的部门，公司
	 * @param username
	 * @return
	 * @author : zhaoziyun
	 * @date 2017-3-2 下午3:21:50
	 */
	public void updOaUser(XUser u);
	
	/**
	 * @method: getByUserId
	 * @Description: TODO
	 * @param userId
	 * @return
	 * @author : lig
	 * @date 2017-3-3 下午4:58:06
	 */
	public XUser getByUserId(String userId);
	
	
	/**
	 * @method: getDeptRoleByUserIdAndRoleId
	 * @Description: 根据用户查询部门专用角色、和部门联系人
	 * @param userId
	 * @param roleId
	 * @return
	 * @author : lig
	 * @date 2017-3-13 下午6:35:33
	 */
	public Map getDeptRoleByUserIdAndRoleId(String userId,String roleId);
	
	/**
	 * @method: delRoleByUserIdAndRoleId
	 * @Description: 根据用户id删除部门专用角色和部门联系人角色
	 * @param userId
	 * @param roleId
	 * @author : lig
	 * @date 2017-3-13 下午6:55:25
	 */
	public void delRoleByUserIdAndRoleId(String userId,String roleId);	
	/**
	 * 
	 * @method: getDeptByCompanyId
	 * @Description: TODO
	 * @param companyId
	 * @return
	 * @author : zhaoziyun
	 * @date 2017-3-21 下午2:16:23
	 */
	public List<Map> getDeptByCompanyId(String companyId);	
	/**
	 * 
	 * @method: findDept
	 * @Description: TODO
	 * @param companyId
	 * @return
	 * @author : zhaoziyun
	 * @date 2017-3-21 下午2:16:23
	 */
	public List<Map> findDept(SqlQueryFilter filter,String companyId,String deptId);
	/**
	 * 
	 * @method: editRoomNum
	 * @Description: 编辑房间号
	 * @param id
	 * @param roomNum
	 * @return
	 * @author : zhaoziyun
	 * @date 2017-5-11 下午5:28:47
	 */
	public Long editRoomNum(String id,String roomNum);

	//查询所有美食商家
	public List<XUser> findFoodBusiness();
	
	//通过username查询所有美食商家
	public XUser findFoodBusinessByUsername(String foodBusiness);	
	
}
