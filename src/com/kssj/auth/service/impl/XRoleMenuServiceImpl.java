package com.kssj.auth.service.impl;

import java.util.List;
import java.util.Map;

import com.kssj.auth.dao.XRoleMenuDao;
import com.kssj.auth.model.XRoleMenu;
import com.kssj.auth.service.XRoleMenuService;
import com.kssj.frame.service.impl.GenericServiceImpl;

public class XRoleMenuServiceImpl extends GenericServiceImpl<XRoleMenu, String> implements XRoleMenuService {

	private XRoleMenuDao dao;
	
	public XRoleMenuServiceImpl(XRoleMenuDao dao) {
		super(dao);
		this.dao = dao;
		// TODO Auto-generated constructor stub
	}

	@Override
	public XRoleMenu addXRoleMenuPermission(XRoleMenu xrmp) {
		// TODO Auto-generated method stub
		return this.dao.saveOrUpdate(xrmp);
	}

	@Override
	public void delPermissionByRoleId(String roleId) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("delete from x_role_menu where role_id = '"+roleId+"'");
		this.dao.executeSql(sql.toString());
		
		
	}

	@Override
	public List<XRoleMenu> getByRoleId(String roleId) {
		
		String hql = " from XRoleMenu where roleId = '" +roleId+ "'";
		
		return this.getPubDao().findByHql(hql);
	}
	
	
	@Override
	public List<Map> getByRoleId2(String roleId) {
		
		String hql = "select distinct menu_Id  from x_role_menu where role_Id = '" +roleId+ "'";
		
		return this.getPubDao().findBySqlToMap(hql);
	}

}
