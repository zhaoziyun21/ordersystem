package com.kssj.auth.service;

import java.util.List;

import com.kssj.auth.model.XRole;
import com.kssj.frame.service.GenericService;

public interface XRoleService extends GenericService<XRole, String>{
	
	/**
	 * @method: getRole
	 * @Description: 获取所有角色
	 * @return
	 * @author : lig
	 * @date 2016-9-20 下午2:16:24
	 */
	public List<XRole> getRole();
	
	/**
	 * @method: addRole
	 * @Description: 新增角色
	 * @param role
	 * @return
	 * @author : lig
	 * @date 2016-9-21 上午11:39:29
	 */
	public XRole addRole(XRole role);
	
	
	
	public boolean delRole(String roleId);
}
