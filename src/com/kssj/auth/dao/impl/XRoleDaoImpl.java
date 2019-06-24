package com.kssj.auth.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.kssj.auth.dao.XRoleDao;
import com.kssj.auth.model.XRole;
import com.kssj.frame.dao.impl.GenericDaoImpl;

@SuppressWarnings("unchecked")
public class XRoleDaoImpl extends GenericDaoImpl<XRole, String> implements XRoleDao{

	public XRoleDaoImpl() {
		super(XRole.class);
		// TODO Auto-generated constructor stub
	}
}
