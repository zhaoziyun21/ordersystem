package com.kssj.product.service.impl;

import java.util.List;
import java.util.Map;

import com.kssj.base.util.StringUtil;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.impl.GenericServiceImpl;
import com.kssj.product.dao.XProCategoryDao;
import com.kssj.product.model.XProCategory;
import com.kssj.product.service.XProCategoryService;

public class XProCategoryServiceImpl extends GenericServiceImpl<XProCategory, String> implements XProCategoryService {

	private XProCategoryDao xProCategoryDao;
	
	public XProCategoryServiceImpl(XProCategoryDao xProCategoryDao) {
		super(xProCategoryDao);
		
		this.xProCategoryDao = xProCategoryDao;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map> getProCategoryList(SqlQueryFilter filter, String str) {
		StringBuffer sql = new StringBuffer();
		sql.append("select xpc.id, xpc.pro_cate_name, xpc.pro_cate_desc, xpc.del_flag from x_pro_category xpc");
		sql.append(" where 1=1 ");
		if(StringUtil.isNotEmpty(str)){
			sql.append("  AND (xpc.pro_cate_name LIKE '%"+str+"%') ");
		}
		System.out.println("==========="+sql.toString());
		filter.setBaseSql(sql.toString());
		return this.query(filter);
	}

	@Override
	public List<XProCategory> getAllAvailable() {
		String hql = "from XProCategory where delFlag=?";
		Object[] objs = {"0"};
		List<XProCategory> xProCategoryList = xProCategoryDao.findByHql(hql, objs);
		if(xProCategoryList != null && xProCategoryList.size() > 0){
			return xProCategoryList;
		}
		return null;
	}

}
