package com.kssj.auth.service;

import java.util.List;

import com.kssj.auth.model.XUserRole;
import com.kssj.frame.service.GenericService;

public interface XUserRoleService extends GenericService<XUserRole, String>{
	
	/**
	 * @method: addUserRole
	 * @Description: 添加用户角色
	 * @param userRole
	 * @return
	 * @author : lig
	 * @date 2016-9-20 下午4:36:23
	 */
	public XUserRole addUserRole(XUserRole userRole);
	
	
	/**
	 * @method: delUserRole
	 * @Description: 删除用户角色
	 * @param userid
	 * @return
	 * @author : lig
	 * @date 2016-9-20 下午4:53:51
	 */
	public void delUserRole(String userid);
	
	/**
	 * @method: delRoleUserByRoleId
	 * @Description: 删除该角色下所有用户
	 * @param roleid
	 * @author : lig
	 * @date 2016-9-23 上午11:15:56
	 */
	public void delRoleUserByRoleId(String roleid);
	
	
	/**
	 * @method: getByUserId
	 * @Description: 获取用户角色
	 * @param userid
	 * @return
	 * @author : lig
	 * @date 2016-10-15 上午11:37:33
	 */
	public XUserRole getByUserId(String userid);
	
	
	/**
	 * @method: getRoleByUserId
	 * @Description: 获取用户角色集合
	 * @param userid
	 * @return
	 * @author : lig
	 * @date 2017-3-3 下午5:02:05
	 */
	public List<XUserRole> getRoleByUserId(String userid);
	
	
}
