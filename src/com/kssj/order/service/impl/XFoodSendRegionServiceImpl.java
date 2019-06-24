package com.kssj.order.service.impl;

import java.util.List;
import java.util.Map;

import com.kssj.base.util.StringUtil;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.impl.GenericServiceImpl;
import com.kssj.order.dao.XFoodSendRegionDao;
import com.kssj.order.model.XFoodSendRegion;
import com.kssj.order.service.XFoodSendRegionService;

public class XFoodSendRegionServiceImpl extends GenericServiceImpl<XFoodSendRegion, String> implements XFoodSendRegionService {

	@SuppressWarnings("unused")
	private XFoodSendRegionDao xFoodSendRegionDao;
	
	public XFoodSendRegionServiceImpl(XFoodSendRegionDao xFoodSendRegionDao) {
		super(xFoodSendRegionDao);
		
		this.xFoodSendRegionDao = xFoodSendRegionDao;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map> getRegionList(SqlQueryFilter filter, String str) {
		StringBuffer sql = new StringBuffer();
		sql.append("select xfsr.id, xfsr.region_name, xfsr.region_desc, xfsr.del_flag from x_food_send_region xfsr");
		sql.append(" where 1=1 ");
		if(StringUtil.isNotEmpty(str)){
			sql.append("  AND (xfsr.region_name LIKE '%"+str+"%') ");
		}
		
		filter.setBaseSql(sql.toString());
		return this.query(filter);
	}

	@Override
	public List<XFoodSendRegion> getAllAvailable() {
		String hql = "from XFoodSendRegion where delFlag=?";
		Object[] objs = {"0"};
		List<XFoodSendRegion> xFoodSendRegionList = xFoodSendRegionDao.findByHql(hql , objs);
		if(xFoodSendRegionList != null && xFoodSendRegionList.size() > 0){
			return xFoodSendRegionList;
		}
		return null;
	}

}
