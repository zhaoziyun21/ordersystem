package com.kssj.order.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.kssj.auth.model.XUser;
import com.kssj.auth.service.XUserService;
import com.kssj.base.util.StringUtil;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.impl.GenericServiceImpl;
import com.kssj.order.dao.XFoodSendAddressDao;
import com.kssj.order.model.XFoodSendAddress;
import com.kssj.order.service.XFoodSendAddressService;
import com.mysql.fabric.xmlrpc.base.Array;

public class XFoodSendAddressServiceImpl extends GenericServiceImpl<XFoodSendAddress, String> implements XFoodSendAddressService {

	@SuppressWarnings("unused")
	private XFoodSendAddressDao xFoodSendAddressDao;
	
	@Resource
	private XUserService xUserService;
	
	public XFoodSendAddressServiceImpl(XFoodSendAddressDao xFoodSendAddressDao) {
		super(xFoodSendAddressDao);
		this.xFoodSendAddressDao = xFoodSendAddressDao;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map> getAddressList(SqlQueryFilter filter, String str) {
		StringBuffer sql = new StringBuffer();
		sql.append("select xfsa.id, xfsa.region_id, xfsa.park, xfsa.high_building, xfsa.unit, xfsa.floor, xfsa.room_num, xfsa.del_flag, xfsr.region_name from x_food_send_address xfsa");
		sql.append(" left join x_food_send_region xfsr ");
		sql.append(" on xfsa.region_id = xfsr.id ");
		sql.append(" where 1=1 and xfsr.del_flag='0' ");
		if(StringUtil.isNotEmpty(str)){
			sql.append("  AND (xfsa.room_num = '"+str+"') ");
		}
		
		filter.setBaseSql(sql.toString());
		return this.query(filter);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map> getAddressList2(SqlQueryFilter filter, String foodBusiness) {
		String sql = "select xfsa.*, xfsr.* from x_food_send_address xfsa left join x_food_send_region xfsr on xfsa.region_id=xfsr.id where xfsr.del_flag='0' and xfsa.del_flag='0' ";
		
		XUser xUser = xUserService.findFoodBusinessByUsername(foodBusiness);
		if(StringUtils.isNotBlank(xUser.getSendRegion())){
			String[] sendRegionArr = xUser.getSendRegion().split(",");
			for (int i=0; i<sendRegionArr.length; i++) {
				if(i==0){
					sql += " and xfsa.region_id='"+sendRegionArr[i]+"'";
				}else{
					sql += " or xfsa.region_id='"+sendRegionArr[i]+"'";
				}
			}
		}
		
		filter.setBaseSql(sql);
		return this.query(filter);
	}

}
