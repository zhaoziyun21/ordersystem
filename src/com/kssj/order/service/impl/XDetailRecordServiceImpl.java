package com.kssj.order.service.impl;

import java.util.List;

import com.kssj.base.util.ListUtil;
import com.kssj.base.util.StringUtil;
import com.kssj.frame.service.impl.GenericServiceImpl;
import com.kssj.order.dao.XDetailRecordDao;
import com.kssj.order.model.XDetailRecord;
import com.kssj.order.service.XDetailRecordService;

public class XDetailRecordServiceImpl extends GenericServiceImpl<XDetailRecord, String> implements XDetailRecordService{

	@SuppressWarnings("unused")
	private XDetailRecordDao dao;
	
	public XDetailRecordServiceImpl(XDetailRecordDao dao) {
		super(dao);
		this.dao = dao;
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<XDetailRecord> getRecordById(String flag, String id) {
		StringBuffer hql = new StringBuffer();
		if(flag.equals("1")){
			hql.append("from  XDetailRecord xdr where xdr.deptId = '"+id+"' order by insTime desc");
		}else if(flag.equals("2")){
			hql.append("from  XDetailRecord xdr where xdr.userId = '"+id+"' order by insTime desc");
		}
		List<XDetailRecord> recordList = this.getPubDao().findByHql(hql.toString());
		if(ListUtil.isNotEmpty(recordList)){
			return recordList;
		}
		return null; 
	}

}
