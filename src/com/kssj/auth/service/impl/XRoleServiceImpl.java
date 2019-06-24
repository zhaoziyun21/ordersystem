package com.kssj.auth.service.impl;

import java.util.List;

import com.kssj.auth.dao.XRoleDao;
import com.kssj.auth.model.XRole;
import com.kssj.auth.service.XRoleService;
import com.kssj.frame.service.impl.GenericServiceImpl;

public class XRoleServiceImpl extends GenericServiceImpl<XRole, String> implements XRoleService{

	private XRoleDao dao;
	
	public XRoleServiceImpl(XRoleDao dao) {
		super(dao);
		this.dao = dao;
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<XRole> getRole() {
		String hql = "from XRole";
		return this.getPubDao().findByHql(hql);
	}

	@Override
	public XRole addRole(XRole role) {
		// TODO Auto-generated method stub
		return this.dao.saveOrUpdate(role);
	}

	@Override
	public boolean delRole(String roleId) {
		boolean b = true;
		 try {
			this.dao.remove(roleId);
			StringBuffer roleMenuSql = new StringBuffer();
			roleMenuSql.append("delete from x_role_menu where role_id = '"+roleId+"'");
			this.dao.executeSql(roleMenuSql.toString());
			StringBuffer userRoleSql = new StringBuffer();
			userRoleSql.append("delete from x_user_role where role_id = '"+roleId+"'");
			this.dao.executeSql(userRoleSql.toString());
		} catch (Exception e) {
			b =false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return b;
	}
	
	
	
	

}
