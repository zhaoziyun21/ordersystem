package com.kssj.auth.service;

import java.util.List;
import java.util.Map;

import com.kssj.auth.model.XRoleMenu;
import com.kssj.frame.service.GenericService;

public interface XRoleMenuService extends GenericService<XRoleMenu, String>{
	
	/**
	 * @method: addXRoleMenuPermission
	 * @Description: 角色菜单权限分配
	 * @param xrmp
	 * @return
	 * @author : lig
	 * @date 2016-9-21 上午11:41:03
	 */
	public XRoleMenu addXRoleMenuPermission(XRoleMenu xrmp);
	
	
	/**
	 * @method: delPermissionByRoleId
	 * @Description: 根据角色id删除
	 * @param roleId
	 * @author : lig
	 * @date 2016-9-23 上午10:35:23
	 */
	public void delPermissionByRoleId(String roleId);
	
	
	/**
	 * @method: getByRoleId
	 * @Description: 获取角色权限
	 * @param roleId
	 * @return
	 * @author : lig
	 * @date 2016-10-15 上午11:45:41
	 */
	public List<XRoleMenu> getByRoleId(String roleId);
	
	public List<Map> getByRoleId2(String roleId);
}
