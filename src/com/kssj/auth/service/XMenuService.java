package com.kssj.auth.service;

import java.util.List;
import java.util.Map;

import com.kssj.auth.model.XMenu;
import com.kssj.auth.model.XRoleMenu;
import com.kssj.frame.service.GenericService;

public interface XMenuService extends GenericService<XMenu, String>{
	
	/**
	 * @method: getAllMenu
	 * @Description: 查询所有菜单
	 * @return
	 * @author : lig
	 * @date 2016-9-21 上午10:31:36
	 */
	public List<XMenu> getAllMenu();
	
	/**
	 * @method: getMenuByRoleId
	 * @Description: 角色权限菜单
	 * @param roleId
	 * @return
	 * @author : lig
	 * @date 2016-9-22 下午3:43:02
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getMenuByRoleId(String roleId);
	
	
	/**
	 * @method: getMenuByIds
	 * @Description: 查询菜单
	 * @param ids
	 * @return
	 * @author : lig
	 * @date 2016-10-15 下午12:01:46
	 */
	public List<XMenu> getMenuByIds(List<XRoleMenu> l);
	
	public List<Map> getMenuByRoleId2(String roleId);

}
