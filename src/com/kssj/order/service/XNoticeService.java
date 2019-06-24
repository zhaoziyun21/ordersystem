package com.kssj.order.service;

import java.util.List;
import java.util.Map;

import com.kssj.auth.model.XUser;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.GenericService;
import com.kssj.order.model.XNotice;

public interface XNoticeService extends GenericService<XNotice,String>{

	List<XNotice> getNotice(String noticeName);

	List<Map> getAllNotices(SqlQueryFilter qf, XUser xuser);

	List<Map> getBillMap(XUser xuser);

	List<Map> getAllNotice(SqlQueryFilter qf, XUser xuser);

	List<Map> getNoticebillByWeekId(String billId, XUser xuser);

	boolean saveBillMap(Map<String, String> billMap, String userName);

	List<Map> getCurrentNotice(String week, String type, String foodBusiness);

	List<Map> getNoticeByCondtion(String week, String type,
			String foodBusiness);

}
