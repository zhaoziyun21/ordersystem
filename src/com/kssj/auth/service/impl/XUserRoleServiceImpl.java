package com.kssj.auth.service.impl;

import java.util.List;

import com.kssj.auth.dao.XUserRoleDao;
import com.kssj.auth.model.XUserRole;
import com.kssj.auth.service.XUserRoleService;
import com.kssj.base.util.ListUtil;
import com.kssj.frame.service.impl.GenericServiceImpl;

public class XUserRoleServiceImpl extends GenericServiceImpl<XUserRole, String> implements XUserRoleService{

	private XUserRoleDao dao;
	
	public XUserRoleServiceImpl(XUserRoleDao dao) {
		super(dao);
		this.dao = dao;
		// TODO Auto-generated constructor stub
	}

	@Override
	public XUserRole addUserRole(XUserRole userRole) {
		// TODO Auto-generated methozd stub
		return this.dao.saveOrUpdate(userRole);
	}

	@Override
	public void delUserRole(String userid) {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from x_user_role where user_id = '"+userid+"' ");
		 this.dao.executeSql(sql.toString());
	}

	@Override
	public void delRoleUserByRoleId(String roleid) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("delete from x_user_role where role_id = '"+roleid+"'");
		this.dao.executeSql(sql.toString());
		
	}

	@Override
	public XUserRole getByUserId(String userid) {
		String hql = "from XUserRole where userId = '" +userid+ "'";
		List<XUserRole> l =  this.getPubDao().findByHql(hql);
		if(ListUtil.isNotEmpty(l)){
			return l.get(0);
		}
		return null;
	}

	@Override
	public List<XUserRole> getRoleByUserId(String userid) {
		String hql = "from XUserRole where userId = '" +userid+ "'";
		List<XUserRole> l =  this.getPubDao().findByHql(hql);
		if(ListUtil.isNotEmpty(l)){
			return l;
		}
		return null;
	}

	
	
}
