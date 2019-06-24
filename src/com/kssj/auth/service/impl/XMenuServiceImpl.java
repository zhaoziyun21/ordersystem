package com.kssj.auth.service.impl;

import java.util.List;
import java.util.Map;

import com.kssj.auth.dao.XMenuDao;
import com.kssj.auth.model.XMenu;
import com.kssj.auth.model.XRoleMenu;
import com.kssj.auth.service.XMenuService;
import com.kssj.base.util.ListUtil;
import com.kssj.frame.service.impl.GenericServiceImpl;

public class XMenuServiceImpl extends GenericServiceImpl<XMenu, String> implements XMenuService{

	private XMenuDao dao;
	
	public XMenuServiceImpl(XMenuDao dao) {
		super(dao);
		this.dao = dao;
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<XMenu> getAllMenu() {
		
		
		return this.dao.getAll();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getMenuByRoleId(String roleId) {
		StringBuffer sql = new StringBuffer();
		sql.append("   select ");
		sql.append("     distinct mp.menu_id,m.id,m.menu_name,m.parent_id,mp.permission_values ");
		sql.append(" from x_menu  m left join x_role_menu mp on m.id = mp.menu_id ");
		sql.append(" and mp.role_id='"+roleId+"' order by m.id");
		return this.getPubDao().findBySqlToMap(sql.toString());
	}

	@Override
	public List<XMenu> getMenuByIds(List<XRoleMenu> l) {
		StringBuffer sql = new StringBuffer();
		sql.append(" from XMenu  m where 1 = 1  ");
		
		if(ListUtil.isNotEmpty(l)){
			sql.append(" and (");
			for (int i = 0; i < l.size() ;i ++) {
				sql.append(" m.id = '"+l.get(i).getMenuId()+"'");
				if(i < l.size() -1){
					sql.append(" or ");
				}
			}
			sql.append(" ) ");
			
		}
		
		List<XMenu> lm = this.getPubDao().findByHql(sql.toString());
		if(ListUtil.isNotEmpty(lm)){
			return lm;
		}
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map> getMenuByRoleId2(String roleId) {
		StringBuffer sql = new StringBuffer();
		sql.append("   select ");
		sql.append("     distinct  mp.menu_id,m.id ");
		sql.append(" from x_menu  m left join x_role_menu mp on m.id = mp.menu_id ");
		sql.append(" and mp.role_id='"+roleId+"' ");
		return this.getPubDao().findBySqlToMap(sql.toString());
	}

}
