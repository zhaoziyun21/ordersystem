package com.kssj.product.service.impl;

import java.util.List;
import java.util.Map;

import com.kssj.base.util.ListUtil;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.impl.GenericServiceImpl;
import com.kssj.product.dao.XProExamineDao;
import com.kssj.product.dao.XProductsManageDao;
import com.kssj.product.model.XProExamine;
import com.kssj.product.model.XProducts;
import com.kssj.product.service.XProductsManageService;

public class XProductsManageServiceImpl extends GenericServiceImpl<XProducts, String> implements XProductsManageService {
	
	private XProductsManageDao dao;
	
	private XProExamineDao xProExamineDao;

	public XProductsManageServiceImpl(XProductsManageDao dao, XProExamineDao xProExamineDao) {
		super(dao);
		this.dao = dao;
		this.xProExamineDao = xProExamineDao;
		
	}

	@Override
	public List<Map> getAllProducts(SqlQueryFilter filter, String startTime,
			String endTime, String flag) {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		sql.append(" xp.id, ");
		sql.append(" xp.pro_code, ");
		sql.append(" xp.pro_name, ");
		sql.append(" xp.pro_num, ");
		sql.append(" xp.pro_remain, ");
		sql.append(" xp.pro_price, ");
		sql.append(" xp.pro_reference_price, ");
		sql.append(" xp.pro_describe, ");
		sql.append(" xp.pro_image_url, ");
		sql.append(" xp.pro_status, ");
		sql.append(" xp.ins_time, ");
		sql.append(" xp.upd_time, ");
		sql.append(" xp.pro_out_url, ");
		sql.append(" xpc.pro_cate_name, ");
		sql.append(" xpe.pro_id, ");
		sql.append(" xpe.ex_status, ");
		sql.append(" xpe.ex_ins_time, ");
		sql.append(" xpe.ex_upd_time ");
		sql.append(" FROM x_products xp");
		sql.append(" LEFT JOIN x_pro_category xpc");
		sql.append(" ON xp.pro_cate_id = xpc.id ");
		sql.append(" LEFT JOIN x_pro_examine xpe ");
		sql.append(" ON xp.id = xpe.pro_id where 1=1 ");
		
		if (flag != null && !"".equals(flag)) {
			sql.append(" and xp.pro_status=" + flag);
		}
		if (startTime != null && !"".equals(startTime)) {
			sql.append(" and xp.upd_time>= '" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime)) {
			sql.append(" and xp.upd_time<= '" + endTime + "'");
		}
		filter.setBaseSql(sql.toString());
		List<Map> list = this.query(filter);

		if (ListUtil.isNotEmpty(list)) {
			return list;
		}
		return null;
	}

	/*@Override
	public void saveProduct(XProducts producs) {
		this.getPubDao().getHibernateTemplate().save(producs);
	}*/
	@Override
	public void saveProductAndProExamine(XProducts xProducts,
			XProExamine xProExamine) {
		//保存商品
		this.getPubDao().getHibernateTemplate().save(xProducts);
		
		Object[] o = {xProducts.getProCode()};
		List<XProducts> xProductsList = this.dao.findByHql("from XProducts where proCode=?", o);
		if(xProductsList != null && xProductsList.size() > 0){
			xProExamine.setProId(xProductsList.get(0).getId());
			//保存审批
			xProExamineDao.save(xProExamine);
		}
		
	}

	@Override
	public void updateStatus(String id, String status) {

		/*
		 * String
		 * hqlString="update from XProducts product set product.status="+status
		 * +" where product.id="+id;
		 * 
		 * dao.getMySession().createQuery(hqlString).executeUpdate();
		 */

	}


	@Override
	public List<Map> queryMyProOrder(String order_id) {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		sql.append(" xpod.pro_id, "); //订购产品编码
		sql.append(" xpod.pro_num, "); //订购产品数量
		sql.append(" xp.pro_name, ");
		sql.append(" xp.pro_describe, ");
		sql.append(" xp.pro_price, ");
		sql.append(" xp.pro_image_url ");
		sql.append(" FROM x_pro_order_detail xpod ");
		sql.append(" LEFT JOIN x_products xp ");
		sql.append(" ON xp.pro_code = xpod.pro_id ");
		sql.append(" WHERE xpod.order_id='"+order_id+"' ");
		
		List<Map> proOrderList = this.getPubDao().findBySqlToMap(sql.toString());
		if(proOrderList != null && proOrderList.size() > 0){
			return proOrderList;
		}
		
		return null;
	}

	/**
	 * 审批状态中的产品
	 */
	@Override
	public List<Map> getExamineAllProducts(SqlQueryFilter filter, String startTime,
			String endTime, String flag) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		sql.append(" xp.id, ");
		sql.append(" xp.pro_code, ");
		sql.append(" xp.pro_name, ");
		sql.append(" xp.pro_num, ");
		sql.append(" xp.pro_remain, ");
		sql.append(" xp.pro_price, ");
		sql.append(" xp.pro_describe, ");
		sql.append(" xp.pro_image_url, ");
		sql.append(" xp.pro_status, ");
		sql.append(" xp.ins_time, ");
		sql.append(" xp.upd_time, ");
		sql.append(" xp.pro_out_url, ");
		sql.append(" xpe.pro_id, ");
		sql.append(" xpe.ex_status, ");
		sql.append(" xpe.ex_ins_time, ");
		sql.append(" xpe.ex_upd_time ");
		sql.append(" FROM x_products xp");
		sql.append(" LEFT JOIN x_pro_examine xpe");
		sql.append(" ON xp.id = xpe.pro_id ");
		sql.append(" where xp.pro_status=2 ");
		if (flag != null && !"".equals(flag)) {
			sql.append(" and xpe.ex_status=" + flag);
		}
		if (startTime != null && !"".equals(startTime)) {
			sql.append(" and xpe.ex_upd_time>= '" + startTime + "'");
		}
		if (endTime != null && !"".equals(endTime)) {
			sql.append(" and xpe.ex_upd_time<= '" + endTime + "'");
		}
		filter.setBaseSql(sql.toString());
		List<Map> list = this.query(filter);

		if (ListUtil.isNotEmpty(list)) {
			return list;
		}
		return null;
	}


}
