package com.kssj.order.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.kssj.auth.model.XUser;
import com.kssj.base.util.Constants;
import com.kssj.base.util.DateUtil;
import com.kssj.base.util.ListUtil;
import com.kssj.base.util.StringUtil;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.impl.GenericServiceImpl;
import com.kssj.order.dao.XNoticeBillDao;
import com.kssj.order.dao.XNoticeDao;
import com.kssj.order.model.XFood;
import com.kssj.order.model.XFoodBill;
import com.kssj.order.model.XNotice;
import com.kssj.order.model.XNoticeBill;
import com.kssj.order.service.XNoticeService;

public class XNoticeServiceImpl extends GenericServiceImpl<XNotice, String> implements XNoticeService {

	private XNoticeDao dao;
	private XNoticeBillDao xNoticeBillDao;
	
	public XNoticeServiceImpl(XNoticeDao dao, XNoticeBillDao xNoticeBillDao) {
		super(dao);
		this.dao = dao;
		this.xNoticeBillDao = xNoticeBillDao;
	}

	@Override
	public List<XNotice> getNotice(String noticeName) {
		String hql ="from XNotice where noticeName = ? and delFlag = '0' ";
		Object[] o ={noticeName};
		List<XNotice> list = this.dao.findByHql(hql,o);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}

	@Override
	public List<Map> getAllNotices(SqlQueryFilter qf, XUser xuser) {
		String sql = "select id, notice_name, notice_desc, ins_time, ins_user, upd_time, upd_user from x_notice where del_flag='0' and ins_user='"+xuser.getUserName()+"'";
		qf.setBaseSql(sql);
		List<Map> list = this.query(qf);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}
	
	@Override
	public List<Map> getBillMap(XUser xuser) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select xd.week, xd.food_type, xn.notice_desc, ");
		sb.append(" xnb.dictionary_id, xnb.notice_id, xnb.notice_name ");
		sb.append(" from x_notice_bill xnb ");
		sb.append(" left join x_dictionary xd on xd.id = xnb.dictionary_id ");
		sb.append(" left join x_notice xn on xn.id = xnb.notice_id ");
		sb.append(" where xn.del_flag ='0' and xnb.ins_user='"+xuser.getUserName()+"' ");
		
		List<Map> list = this.dao.findBySqlToMap(sb.toString(), null, 0, 0);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}

	@Override
	public List<Map> getAllNotice(SqlQueryFilter qf, XUser xuser) {
		String sql = "select id, notice_name, notice_desc from x_notice where del_flag='0' and ins_user='"+xuser.getUserName()+"'";
		qf.setBaseSql(sql);
		List<Map> list = this.query(qf);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}

	@Override
	public List<Map> getNoticebillByWeekId(String billId, XUser xuser) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select xn.id, xn.notice_name, xn.notice_desc ");
		sql.append(" from x_notice xn ");
		sql.append(" left join x_notice_bill xnb on xnb.notice_id = xn.id ");
		sql.append(" where xn.del_flag='0' and xnb.dictionary_id = ? and xn.ins_user = ? ");
		
		List<Map> list = this.dao.findBySqlToMap(sql.toString(), new Object[]{billId,xuser.getUserName()}, 0, 0);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}

	@Override
	public boolean saveBillMap(Map<String, String> billMap, String userName) {
		try {
			//获取当前登陆人姓名  根据姓名 删除想foodBill 数据
			String sql = "delete from x_notice_bill where ins_user='"+userName+"'";
			xNoticeBillDao.executeSql(sql);
			
			for (Map.Entry<String, String> entry : billMap.entrySet()){  
				  String weekDay = entry.getKey().toString();
				  Map<String, String> foodTypeMap  = (Map)JSONObject.fromObject(entry.getValue());
				  JSONArray lunchArray = JSONArray.fromObject(foodTypeMap.get("lunch"));
				  JSONArray dinnerArray = JSONArray.fromObject(foodTypeMap.get("dinner"));
				  String lunchDictionaryId = Constants.billMap.get(weekDay).toString();
				  String dinnerDictionaryId = Constants.billMap.get(weekDay+"-dinner").toString();
				  
				  XNoticeBill xNoticeBill = new XNoticeBill();
					  
				  for (int j = 0; j < lunchArray.size(); j++) {
					  Map m = (Map)lunchArray.get(j);
					  xNoticeBill.setId(null);
					  xNoticeBill.setDictionaryId(lunchDictionaryId);
					  xNoticeBill.setNoticeId(m.get("noticeId").toString());
					  xNoticeBill.setNoticeName(m.get("noticeName").toString());
					  xNoticeBill.setInsTime(new Date());
					  xNoticeBill.setInsUser(userName);
					  xNoticeBillDao.save(xNoticeBill);
				  }
				  
				  for (int j = 0; j < dinnerArray.size(); j++) {
					  Map m = (Map)dinnerArray.get(j);
					  xNoticeBill.setId(null);
					  xNoticeBill.setDictionaryId(dinnerDictionaryId);
					  xNoticeBill.setNoticeId(m.get("noticeId").toString());
					  xNoticeBill.setNoticeName(m.get("noticeName").toString());
					  xNoticeBill.setInsTime(new Date());
					  xNoticeBill.setInsUser(userName);
					  xNoticeBillDao.save(xNoticeBill);
				  }
			}  
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Map> getCurrentNotice(String week, String type, String foodBusiness) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT xnb.id, xnb.notice_id, xn.notice_name, xn.notice_desc, xdy.food_type ft ");
		sql.append(" FROM x_notice_bill xnb ");
		sql.append(" LEFT JOIN x_notice xn ON xn.id = xnb.notice_id ");
		sql.append(" LEFT JOIN x_dictionary xdy ON xdy.id = xnb.dictionary_id ");
		sql.append(" WHERE xnb.dictionary_id = (SELECT xd.id FROM x_dictionary xd WHERE xd.week = '"+week+"' AND xd.food_type='"+type+"' ) ");
		sql.append(" and xn.del_flag='0' and xn.ins_user='"+foodBusiness+"'  and xnb.ins_user='"+foodBusiness+"' ");
		sql.append(" GROUP BY xnb.notice_id ");
		
		List<Map> list = this.getPubDao().findBySqlToMap(sql.toString());
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}

	@Override
	public List<Map> getNoticeByCondtion(String week, String type, String foodBusiness) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT xnb.id, xnb.notice_id, xn.notice_name, xn.notice_desc, xd.week, xd.food_type ft "); 
		sql.append(" FROM x_notice_bill xnb "); 
		sql.append(" LEFT JOIN x_notice xn ON xn.id = xnb.notice_id  "); 
		sql.append(" INNER JOIN x_dictionary xd ON xd.id = xnb.dictionary_id AND xd.week='"+week+"' "); 
		if(StringUtil.isNotEmpty(type)){
			sql.append(" AND xd.food_type='"+type+"'");
		}
		sql.append(" where xn.ins_user='"+foodBusiness+"' and xnb.ins_user='"+foodBusiness+"'");
		
		List<Map> list = this.getPubDao().findBySqlToMap(sql.toString());
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}	
	
}
