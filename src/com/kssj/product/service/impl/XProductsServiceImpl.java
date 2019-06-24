package com.kssj.product.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import com.kssj.auth.dao.XDeptSumDao;
import com.kssj.auth.dao.XStaffSumDao;
import com.kssj.auth.model.XDeptSum;
import com.kssj.auth.model.XStaffSum;
import com.kssj.auth.model.XUser;
import com.kssj.auth.service.XStaffSumService;
import com.kssj.base.util.Constants;
import com.kssj.base.util.DateUtil;
import com.kssj.base.util.DateUtils2;
import com.kssj.base.util.DateWeek;
import com.kssj.base.util.ListUtil;
import com.kssj.base.util.StringUtil;
import com.kssj.frame.service.impl.GenericServiceImpl;
import com.kssj.order.dao.XDetailRecordDao;
import com.kssj.order.dao.XOrdersDao;
import com.kssj.order.model.MyOrder;
import com.kssj.order.model.XDetailRecord;
import com.kssj.order.model.XOrderDetail;
import com.kssj.order.model.XOrders;
import com.kssj.order.service.XDetailRecordService;
import com.kssj.product.dao.XProOrderDetailDao;
import com.kssj.product.dao.XProductsDao;
import com.kssj.product.dao.XReceiverInfoDao;
import com.kssj.product.model.MyProOrder;
import com.kssj.product.model.XProOrderDetail;
import com.kssj.product.model.XProducts;
import com.kssj.product.model.XReceiverInfo;
import com.kssj.product.service.XProductsService;
import com.kssj.product.service.XReceiverInfoService;

public class XProductsServiceImpl extends GenericServiceImpl<XProducts, String>
		implements XProductsService {

	private static final String Date = null;
	private XProductsDao dao;
	private XStaffSumDao xStaffSumDao;
	private XDetailRecordDao xDetailRecordDao;
	private XDeptSumDao xDeptSumDao;
	private XOrdersDao xOrdersDao;
	private XProOrderDetailDao xProOrderDetailDao;
	private XReceiverInfoDao xReceiverInfoDao;
	
	@Resource
	private XDetailRecordService xDetailRecordService;
	@Resource
	private XStaffSumService xStaffSumService;
	@Resource
	private XReceiverInfoService xReceiverInfoService;

	public XProductsServiceImpl(XProductsDao dao, XStaffSumDao xStaffSumDao,
			XDetailRecordDao xDetailRecordDao, XDeptSumDao xDeptSumDao,
			XOrdersDao xOrdersDao, XProOrderDetailDao xProOrderDetailDao, XReceiverInfoDao xReceiverInfoDao) {
		super(dao);
		this.dao = dao;
		this.xStaffSumDao = xStaffSumDao;
		this.xDetailRecordDao = xDetailRecordDao;
		this.xDeptSumDao = xDeptSumDao;
		this.xOrdersDao = xOrdersDao;
		this.xProOrderDetailDao = xProOrderDetailDao;
		this.xReceiverInfoDao = xReceiverInfoDao;
	}

	/**
	 * 根据产品名称进行查询
	 */
	@Override
	public List<Map> getProductsByCondtion(String proName, String proCateId) {
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
		sql.append(" xp.pro_out_url, ");
		sql.append(" xpc.pro_cate_name ");
		sql.append(" FROM x_products xp ");
		sql.append(" LEFT JOIN x_pro_category xpc ON xpc.id = xp.pro_cate_id");
		sql.append(" WHERE pro_status='0' ");
		sql.append(" and xpc.del_flag='0' ");
		if (StringUtil.isNotEmpty(proName)) {
			sql.append(" and xp.pro_name like '%" + proName + "%'");
		}
		if (StringUtil.isNotEmpty(proCateId)) {
			sql.append(" and xp.pro_cate_id = '"+proCateId+"'");
		}
		sql.append(" ORDER BY xp.ins_time DESC");
		
		List<Map> list = this.getPubDao().findBySqlToMap(sql.toString());
		if (ListUtil.isNotEmpty(list)) {
			return list;
		}
		return null;
	}

	@Override
	public String getProRemainReserveByProductId(String productId) {
		String sql2 = "select xp.pro_remain from x_products xp where pro_code='"
				+ productId + "' and pro_status='0'";
		List<Object> list = this.getPubDao().findBySql(sql2);
		String pro_remain = "";
		if (ListUtil.isNotEmpty(list)) {
			pro_remain = (String) list.get(0); // 当前产品库存
		}
		return pro_remain;
	}

	@Override
	@Transactional
	public Map<String, Object> orderReserve(String type, XUser xuser,
			String orderId, List<Map> map, int z_num, String vistorName,
			String recId) throws Exception {
		Map<String, Object> msgMap = new HashMap<String, Object>();
		int sum = z_num;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();

		if ("0".equals(type) || "2".equals(type)) {
			XStaffSum xStaffSum = null;
			String hql = "from XStaffSum where userId = '" + orderId + "'";
			List<XStaffSum> list = this.getPubDao().findByHql(hql);
			if (ListUtil.isNotEmpty(list)) {
				xStaffSum = list.get(0);
			}
			if (xStaffSum != null) {
				XDetailRecord xDetailRecord = new XDetailRecord();
				int balance =Integer.parseInt(xStaffSum.getBalance());// 当前
				if (balance >= sum) {
					balance = balance - sum;
					xStaffSum.setBalance(balance + "");
					xStaffSum.setUpdTime(now);
					xStaffSum.setUpdUser(xuser.getUserName());
					xStaffSumDao.save(xStaffSum);// 更新余额

					xDetailRecord.setBalance(balance + "");
					xDetailRecord.setChangeMoney(sum + "");
					xDetailRecord.setType(Constants.PAY);
					xDetailRecord.setDeptId(xuser.getDeptId());
					xDetailRecord.setUserId(orderId);
					xDetailRecord.setRemark("订购产品消费");
					xDetailRecord.setInsTime(now);
					xDetailRecord.setInsUser(xuser.getUserName());
					xDetailRecordDao.save(xDetailRecord);// 添加余额操作记录

					XOrders xOrders = new XOrders();
					if ("0".equals(type)) {
						xOrders.setFlag("1");
					} else if ("2".equals(type)) {
						xOrders.setFlag("3");
					}
					if (vistorName != null && vistorName != "") {
						xOrders.setVistorName(vistorName);
					}
					xOrders.setOrderType(Constants.PRO);// 产品标志
					xOrders.setForWhoId(orderId);
					xOrders.setUserId(xuser.getUserId());
					xOrders.setInsTime(new Date());
					xOrders.setInsUser(xuser.getUserName());
					xOrders.setSendOutFlag(1); //未发货
					xOrders.setRecFlag(1); //未收货
					xOrders.setRecId(recId); //收货人id
					xOrdersDao.save(xOrders);// 生成订单

					for (Map map1 : map) {
						XProOrderDetail xProOrderDetail = new XProOrderDetail();
						xProOrderDetail.setOrderId(xOrders.getId());
						xProOrderDetail.setInsTime(new Date());
						xProOrderDetail.setInsUser(xuser.getUserName());
						xProOrderDetail.setProId(map1.get("id").toString());
						xProOrderDetail.setProNum(Integer.parseInt(map1.get(
								"num").toString()));
						xProOrderDetailDao.save(xProOrderDetail); // 保存订购产品详情

						// 查询套餐库存
						String num = getProRemainReserveByProductId(map1.get(
								"id").toString());
						int pro_remain = Integer.parseInt(num)
								- Integer.parseInt(map1.get("num").toString());
						String sl = "UPDATE x_products SET pro_remain ='"
								+ pro_remain + "' where pro_code = '"
								+ map1.get("id").toString() + "'";
						dao.executeSql(sl);// 更新订购产品库存
					}
					msgMap.put("status", "true");
					msgMap.put("msg", "订购产品成功");
				} else {
					msgMap.put("status", "false");
					msgMap.put("msg", "账户余额不足,请联系管理员");
				}
			} else {
				msgMap.put("status", "false");
				msgMap.put("msg", "该账户余额不存在，请联系系统管理员");
			}
		} else if ("1".equals(type)) {
			XDeptSum xDeptSum = null;
			String hql = "from XDeptSum where deptId = '" + xuser.getDeptId()
					+ "'";
			List<XDeptSum> list = this.getPubDao().findByHql(hql);
			if (ListUtil.isNotEmpty(list)) {
				xDeptSum = list.get(0);
			}

			if (xDeptSum != null) {
				XDetailRecord xDetailRecord = new XDetailRecord();
				int balance = xDeptSum.getDeptSum();
				if (balance >= sum) {
					balance = balance - sum;
					xDeptSum.setDeptSum((int) balance);
					xDeptSum.setUpdTime(now);
					xDeptSum.setUpdUser(xuser.getUserName());
					xDeptSumDao.save(xDeptSum);// 更新余额

					xDetailRecord.setBalance(balance + "");
					xDetailRecord.setChangeMoney(sum + "");
					xDetailRecord.setType(Constants.PAY);
					xDetailRecord.setDeptId(xDeptSum.getDeptId());
					xDetailRecord.setRemark("订购产品消费");
					xDetailRecord.setInsTime(now);
					xDetailRecord.setInsUser(xuser.getUserName());
					xDetailRecordDao.save(xDetailRecord);// 添加余额操作记录

					XOrders xOrders = new XOrders();
					xOrders.setFlag("2");
					if (vistorName != null && vistorName != "") {
						xOrders.setVistorName(vistorName);
					}
					xOrders.setOrderType(Constants.PRO);// 产品标志
					xOrders.setForWhoId(xDeptSum.getDeptId());
					xOrders.setUserId(xuser.getUserId());
					xOrders.setInsTime(new Date());
					xOrders.setInsUser(xuser.getUserName());
					xOrders.setDelFlag(0);
					xOrders.setSendOutFlag(1); //未发货
					xOrders.setRecId(recId); //收货人id
					xOrdersDao.save(xOrders);// 生成订单

					for (Map map1 : map) {
						XProOrderDetail xProOrderDetail = new XProOrderDetail();
						xProOrderDetail.setOrderId(xOrders.getId());
						xProOrderDetail.setInsTime(new Date());
						xProOrderDetail.setInsUser(xuser.getUserName());
						xProOrderDetail.setProId(map1.get("id").toString());
						xProOrderDetail.setProNum(Integer.parseInt(map1.get("num").toString()));
						xProOrderDetailDao.save(xProOrderDetail);// 保存订购产品详情

						// 查询订购产品库存
						String num = getProRemainReserveByProductId(map1.get(
								"id").toString());
						int pro_remain = Integer.parseInt(num)
								- Integer.parseInt(map1.get("num").toString());
						String sl = "UPDATE x_products SET pro_remain ='"
								+ pro_remain + "' where pro_code = '"
								+ map1.get("id").toString() + "'";
						dao.executeSql(sl);// 更新产品库存
					}
					msgMap.put("status", "true");
					msgMap.put("msg", "订购产品成功");
				} else {
					msgMap.put("status", "false");
					msgMap.put("msg", "部门账户余额不足,请联系管理员");
				}

			}
		}
		return msgMap;
	}

	@Override
	public List<MyProOrder> getMyOrder(String loginUserId) throws Exception {
		List<MyProOrder> orderList = null;
		MyProOrder myOrder = null;
		try {
			StringBuffer orderSql = new StringBuffer();
			orderSql.append("  SELECT 	xo.order_type, xo.for_who_id,xo.id,xo.flag,xo.send_out_flag,xo.rec_id, xo.rec_flag,xo.express_num, xo.rec_flag, date_format(xo.ins_time, '%Y-%m-%d %H:%i:%s' )  ordertime,  ");
			orderSql.append("  CASE flag   ");
			orderSql.append("  WHEN '1'  THEN (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '3' THEN (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '2' THEN (SELECT xd.dept_name FROM x_dept xd WHERE xd.dept_id = xo.for_who_id )  ");
			orderSql.append("  END orderobj,  ");
			orderSql.append("  CASE flag   ");
			orderSql.append("  WHEN '1'  THEN (SELECT xss.balance FROM x_staff_sum  xss WHERE xss.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '3' THEN (SELECT xss.balance FROM x_staff_sum  xss WHERE xss.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '2' THEN (SELECT xds.dept_sum FROM x_dept_sum xds WHERE xds.dept_id = xo.for_who_id )  ");
			orderSql.append("  END objbalance  ");
			orderSql.append("  FROM x_orders xo   ");
			
			orderSql.append("  WHERE (xo.user_id = '" + loginUserId + "'  ");
			orderSql.append("  and xo.send_out_flag = '0' "); //发货
			orderSql.append("  and now() >= DATE_ADD(xo.send_out_time,INTERVAL 7 DAY) )"); //条件：发货时间  + 一周
			
			orderSql.append("  or (xo.user_id = '" + loginUserId + "'  ");
			orderSql.append("  and xo.send_out_flag = '0' "); //发货
			orderSql.append("  and xo.rec_flag !='1' ) "); //手动收货 或 系统默认收货
			
			// 查询条件：产品
			orderSql.append("  and xo.order_type = 'PRO'");
			orderSql.append("  and xo.del_flag ='0' order by ordertime desc");
			List<Map> l = this.getPubDao().findBySqlToMap(orderSql.toString());
			if (ListUtil.isNotEmpty(l)) {
				orderList = new ArrayList<MyProOrder>();
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Calendar now = Calendar.getInstance();
				String dayTime = now.get(Calendar.YEAR) + "-"
						+ (now.get(Calendar.MONTH) + 1) + "-"
						+ now.get(Calendar.DAY_OF_MONTH);
				Date aMStart = formatter
						.parse((dayTime + " " + Constants.AMSTART)); // 上开
				Date aMSEnd = formatter
						.parse((dayTime + " " + Constants.AMEND)); // 上结
				Date pMStart = formatter
						.parse((dayTime + " " + Constants.PMSTART)); // 下开
				Date pMEnd = formatter.parse((dayTime + " " + Constants.PMEND)); // 下结
				for (Map map : l) {
					myOrder = new MyProOrder();
					// add dingzhj at date 2017-02-22 添加时间判断 START
					// 获取订单时间
					String insTime = map.get("ordertime").toString();
					Date insDate = formatter.parse(insTime);
					String orderType = (String) map.get("order_type");
					// 判断当前时间类型（上午，下午）
					if (orderType.equals(Constants.LUN)) {// 上午
						if (aMStart.getTime() <= insDate.getTime()
								&& insDate.getTime() <= aMSEnd.getTime()) {
							if (aMStart.getTime() <= date.getTime()
									&& date.getTime() <= aMSEnd.getTime()) {
								myOrder.setIsShow("ON");
							}
						} else {
							// myOrder.setIsShow("OFF");
						}
					} else if (orderType.equals(Constants.DIN)) {// 下午
						if (pMStart.getTime() <= insDate.getTime()
								&& insDate.getTime() <= pMEnd.getTime()) {
							if (pMStart.getTime() <= date.getTime()
									&& date.getTime() <= pMEnd.getTime()) {
								myOrder.setIsShow("ON");
							}
						} else {
							// myOrder.setIsShow("OFF");
						}
					}
					// add dingzhj at date 2017-02-22 添加时间判断 END

					myOrder.setBalance((String) map.get("objbalance"));
					myOrder.setOrderObj((String) map.get("orderobj"));
					myOrder.setOrderTime(map.get("ordertime").toString());
					myOrder.setId((String) map.get("id"));
					myOrder.setFlag((String) map.get("flag"));
					myOrder.setForWhoId((String) map.get("for_who_id"));
					myOrder.setOrderType((String) map.get("order_type"));
					myOrder.setSendOutFlag(map.get("send_out_flag")+"");
					myOrder.setExpressNum((String) map.get("express_num"));
					if(map.get("rec_flag") != null){
						myOrder.setRecFlag((Integer) map.get("rec_flag"));
					}

					StringBuffer detailSql = new StringBuffer();
					detailSql
							.append("  SELECT xpod.pro_num,xp.pro_name,xp.pro_price,xp.pro_describe,xp.pro_image_url,xp.pro_code FROM x_pro_order_detail xpod  ");
					detailSql
							.append("  LEFT JOIN x_products xp ON xp.pro_code = xpod.pro_id   ");
					detailSql.append("  WHERE order_id = '"
							+ map.get("id").toString() + "'");
					List<Map> detailMap = this.getPubDao().findBySqlToMap(
							detailSql.toString());
					int total = 0; // 总计
					if (ListUtil.isNotEmpty(detailMap)) {
						for (Map m : detailMap) {
							// 总计
							total += Integer.valueOf((m.get("pro_price")==null?"0":m.get("pro_price"))
									.toString())
									* Integer.valueOf((m.get("pro_num")==null?"0":m.get("pro_num"))
											.toString());
						}
					}
					myOrder.setDetailList(detailMap);
					myOrder.setMoneyTotal(total+"");
					
					//收货人信息
					XReceiverInfo xReceiverInfo = xReceiverInfoService.findReceiverById((String)map.get("rec_id"));
					if(xReceiverInfo != null){
						String recArea = xReceiverInfo.getRecArea();
						String recAreaNew = recArea.replaceAll("/", " ");
						xReceiverInfo.setRecArea(recAreaNew);
						myOrder.setxReceiverInfo(xReceiverInfo);
					}

					orderList.add(myOrder);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("获取我的订单失败！");
		}
		if (ListUtil.isNotEmpty(orderList)) {
			return orderList;
		}

		return null;
	}

	@Override
	public List<MyProOrder> getReserveMyOrder(String loginUserId)
			throws Exception {
		List<MyProOrder> orderList = null;
		MyProOrder myOrder = null;
		try {
			StringBuffer orderSql = new StringBuffer();
			orderSql.append("  SELECT 	xo.order_type, xo.for_who_id,xo.id,xo.flag,xo.send_out_flag ,xo.rec_id, xo.rec_flag, xo.express_num, date_format(xo.ins_time, '%Y-%m-%d %H:%i:%s' )  ordertime,  ");
			orderSql.append("  CASE flag   ");
			orderSql.append("  WHEN '1' THEN (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '3' THEN (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '2' THEN (SELECT xd.dept_name FROM x_dept xd WHERE xd.dept_id = xo.for_who_id )  ");
			orderSql.append("  END orderobj,  ");
			orderSql.append("  CASE flag   ");
			orderSql.append("  WHEN '1' THEN (SELECT xss.balance FROM x_staff_sum  xss WHERE xss.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '3' THEN (SELECT xss.balance FROM x_staff_sum  xss WHERE xss.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '2' THEN (SELECT xds.dept_sum FROM x_dept_sum xds WHERE xds.dept_id = xo.for_who_id )  ");
			orderSql.append("  END objbalance  ");
			orderSql.append("  FROM x_orders xo   ");
			
			orderSql.append("  WHERE (xo.user_id = '" + loginUserId + "'  ");
			orderSql.append("  and xo.order_type = 'PRO'"); // 查询条件：产品
			orderSql.append("  and xo.del_flag ='0' ");
			orderSql.append("  and xo.send_out_flag = '0' "); //发货
			orderSql.append("  and xo.rec_flag ='1' "); //未收货
			orderSql.append("  and now() < DATE_ADD(xo.send_out_time,INTERVAL 7 DAY) )"); //条件：发货时间一周以内
			
			orderSql.append("  or (xo.user_id = '" + loginUserId + "'  ");
			orderSql.append("  and xo.order_type = 'PRO'"); // 查询条件：产品
			orderSql.append("  and xo.del_flag ='0' ");
			orderSql.append("  and xo.send_out_flag = '1' )"); //未发货
			
			orderSql.append("  order by ordertime desc");
			List<Map> l = this.getPubDao().findBySqlToMap(orderSql.toString());
			if (ListUtil.isNotEmpty(l)) {
				orderList = new ArrayList<MyProOrder>();
				for (Map map : l) {
					myOrder = new MyProOrder();
					myOrder.setBalance((String) map.get("objbalance"));
					myOrder.setOrderObj((String) map.get("orderobj"));
					myOrder.setOrderTime(map.get("ordertime").toString());
					myOrder.setId((String) map.get("id"));
					myOrder.setFlag((String) map.get("flag"));
					myOrder.setForWhoId((String) map.get("for_who_id"));
					myOrder.setOrderType((String) map.get("order_type"));
					myOrder.setExpressNum((String) map.get("express_num"));
					myOrder.setSendOutFlag(map.get("send_out_flag")+"");

					StringBuffer detailSql = new StringBuffer();
					detailSql
							.append("  SELECT xpod.pro_num,xp.pro_name,xp.pro_price,xp.pro_describe,xp.pro_image_url,xp.pro_code FROM x_pro_order_detail xpod  ");
					detailSql
							.append("  LEFT JOIN x_products xp ON xp.pro_code = xpod.pro_id   ");
					detailSql.append("  WHERE order_id = '"
							+ map.get("id").toString() + "'");
					List<Map> detailMap = this.getPubDao().findBySqlToMap(
							detailSql.toString());
					int total = 0; // 总计
					if (ListUtil.isNotEmpty(detailMap)) {
						for (Map m : detailMap) {
							// 总计
							total += Integer.valueOf(m.get("pro_price")
									.toString())
									* Integer.valueOf(m.get("pro_num")
											.toString());
						}
					}
					myOrder.setDetailList(detailMap);
					myOrder.setMoneyTotal(total+"");
					
					//收货人信息
					XReceiverInfo xReceiverInfo = xReceiverInfoService.findReceiverById((String)map.get("rec_id"));
					if(xReceiverInfo != null){
						String recArea = xReceiverInfo.getRecArea();
						String recAreaNew = recArea.replaceAll("/", " ");
						xReceiverInfo.setRecArea(recAreaNew);
						myOrder.setxReceiverInfo(xReceiverInfo);
					}
					
					orderList.add(myOrder);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("获取我的订单失败！");
		}
		if (ListUtil.isNotEmpty(orderList)) {
			return orderList;
		}

		return null;
	}

	@Override
	public Map<String, Object> deleteOrder(String orderId, String moneyTotal, XUser user) throws Exception {
		
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> msgMap = new HashMap<String, Object>();
		XOrders xOrders = xOrdersDao.get(orderId);
		
		boolean b = false; //未发货
		//判断是否已经“发货”
		if(xOrders.getSendOutFlag() == 0){ //发货
			b = true;
		}
		
		if (b) {
			msgMap.put("status", "false");
			msgMap.put("msg", "已完成不能取消订单");
		} else {
			// 1：自己的2：客人的3：领导的
			if ("1".equals(xOrders.getFlag()) || "3".equals(xOrders.getFlag())) {
				XStaffSum xStaffSum = null;
				String hql = "from XStaffSum where userId = '"
						+ xOrders.getForWhoId() + "'";
				List<XStaffSum> list = this.getPubDao().findByHql(hql);
				if (ListUtil.isNotEmpty(list)) {
					xStaffSum = list.get(0);
				}
				if (xStaffSum != null) {
					// 原有金额
					int balance = Integer.parseInt(xStaffSum.getBalance());
					// 返回金额
					int p_balance = Integer.parseInt(moneyTotal);
					balance = balance + p_balance;
					xStaffSum.setBalance(balance + "");
					xStaffSum.setUpdTime(new Date());
					// 更新余额表
					xStaffSumService.update(xStaffSum);
					if (xOrders != null) {
						xOrders.setUpdUser(user.getUserName());
						xOrders.setUpdTime(new Date());
						// 0 : 未取消订单 1：取消订单
						xOrders.setDelFlag(1);
						String sql = "UPDATE x_orders SET upd_user='"
								+ xOrders.getUpdUser() + "',upd_time='"
								+ f.format(xOrders.getUpdTime())
								+ "',del_flag='" + xOrders.getDelFlag()
								+ "' where id ='" + orderId + "'";
						xOrdersDao.executeSql(sql);// 更新订单信息
					}
					
					// 根据订单ID取出套餐份数
					String shql = "from XProOrderDetail where orderId='" + orderId
							+ "'";
					List<XProOrderDetail> xdList = this.getPubDao()
							.findByHql(shql);
					if (ListUtil.isNotEmpty(xdList)) {
						for (XProOrderDetail xProOrderDetail : xdList) {
							// 根据套餐ID取出 套餐剩余份数
							String pro_remain = "";
							String sql2 = "select xp.pro_remain pro_remain from x_products xp "
									+ " where xp.pro_code = '"
									+ xProOrderDetail.getProId() + "'";
							List<Map> list2 = this.getPubDao().findBySqlToMap(
									sql2);
							if (ListUtil.isNotEmpty(list2)) {
								pro_remain = (String) list2.get(0).get(
										"pro_remain");
								int num_pro = Integer.parseInt(pro_remain)
										+ xProOrderDetail.getProNum();
								String sl = "UPDATE x_products SET pro_remain ='"
										+ num_pro
										+ "' where pro_code = '"
										+ xProOrderDetail.getProId() + "'";
								dao.executeSql(sl);// 更新套餐库存
							}
						}
					}
					
					// 插入记录表
					XDetailRecord xDetailRecord = new XDetailRecord();
					// 0：收入 1：支出
					xDetailRecord.setType("0");
					xDetailRecord.setUserId(user.getUserId());
					xDetailRecord.setBalance(balance + "");
					xDetailRecord.setInsTime(new Date());
					xDetailRecord.setInsUser(user.getUserName());
					xDetailRecord.setRemark("取消产品订单返回金额");
					xDetailRecord.setChangeMoney(p_balance + "");
					xDetailRecordDao.save(xDetailRecord);// 返回余额操纵记录
					msgMap.put("status", "true");
					msgMap.put("msg", "取消产品订单成功");
				}
			} else if ("2".equals(xOrders.getFlag())) {
				XDeptSum xDeptSum = null;
				String hql = "from XDeptSum where deptId = '"
						+ user.getDeptId() + "'";
				List<XDeptSum> list = this.getPubDao().findByHql(hql);
				if (ListUtil.isNotEmpty(list)) {
					xDeptSum = list.get(0);
				}
				if (xDeptSum != null) {
					// 获取原有部门金额
					int balance = xDeptSum.getDeptSum();
					// 返回金额
					int p_balance = Integer.parseInt(moneyTotal);
					xDeptSum.setDeptSum(p_balance + balance);
					xDeptSum.setUpdTime(new Date());
					xDeptSum.setUpdUser(user.getUserName());
					String sql = "UPDATE x_dept_sum SET dept_sum='"
							+ xDeptSum.getDeptSum() + "',upd_time='"
							+ f.format(xDeptSum.getUpdTime()) + "',upd_user='"
							+ xDeptSum.getUpdUser() + "' where dept_id='"
							+ user.getDeptId() + "'";
					xDeptSumDao.executeSql(sql);// 更新部门金额
					if (xOrders != null) {
						xOrders.setUpdUser(user.getUserName());
						xOrders.setUpdTime(new Date());
						// 0 : 未取消订单 1：取消订单
						xOrders.setDelFlag(1);
						String sqlX = "UPDATE x_orders SET upd_user='"
								+ xOrders.getUpdUser() + "',upd_time='"
								+ f.format(xOrders.getUpdTime())
								+ "',del_flag='" + xOrders.getDelFlag()
								+ "' where id ='" + orderId + "'";
						xOrdersDao.executeSql(sqlX);// 更新订单信息
					}
					
					// 根据订单ID取出套餐份数
					String shql = "from XProOrderDetail where orderId='" + orderId
							+ "'";
					List<XProOrderDetail> xdList = this.getPubDao()
							.findByHql(shql);
					if (ListUtil.isNotEmpty(xdList)) {
						for (XProOrderDetail xProOrderDetail : xdList) {
							// 根据套餐ID取出 套餐剩余份数
							String pro_remain = "";
							String sql2 = "select xp.pro_remain pro_remain from x_products xp "
									+ " where xp.pro_code = '"
									+ xProOrderDetail.getProId() + "'";
							List<Map> list2 = this.getPubDao().findBySqlToMap(
									sql2);
							if (ListUtil.isNotEmpty(list2)) {
								pro_remain = (String) list2.get(0).get(
										"pro_remain");
								int num_pro = Integer.parseInt(pro_remain)
										+ xProOrderDetail.getProNum();
								String sl = "UPDATE x_products SET pro_remain ='"
										+ num_pro
										+ "' where pro_code = '"
										+ xProOrderDetail.getProId() + "'";
								dao.executeSql(sl);// 更新套餐库存
							}
						}
					}
					
					// 插入记录表
					XDetailRecord xDetailRecord = new XDetailRecord();
					// 0：收入 1：支出
					xDetailRecord.setType("0");
					xDetailRecord.setBalance(balance + "");
					xDetailRecord.setInsTime(new Date());
					xDetailRecord.setUpdUser(user.getUserName());
					xDetailRecord.setDeptId(user.getDeptId());
					xDetailRecord.setRemark("取消部门产品订单返回金额");
					xDetailRecord.setChangeMoney(p_balance + "");
					xDetailRecordDao.save(xDetailRecord);// 返回余额操纵记录
					msgMap.put("status", "true");
					msgMap.put("msg", "取消产品订单成功");
				}
			}
		}
		return msgMap;
	}
	
	@Override
	public List<MyProOrder> getMyOrderAppoint(String userId) throws Exception {
		List<MyProOrder> orderList = null;
		MyProOrder myOrder = null;
		try{
			StringBuffer orderSql = new StringBuffer();
			orderSql.append("  SELECT 	xo.order_type, xo.for_who_id,xo.id,xo.flag,xo.rec_id,xo.express_num,xo.rec_flag,send_out_flag,send_out_time, date_format(xo.ins_time, '%Y-%m-%d %H:%i:%s' )  ordertime,  ");
			orderSql.append("  CASE flag   ");
			orderSql.append("  WHEN '1'  THEN (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '3' THEN (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '2' THEN (SELECT xd.dept_name FROM x_dept xd WHERE xd.dept_id = xo.for_who_id )  ");
			orderSql.append("  END orderobj,  ");
			orderSql.append("  CASE flag   ");
			orderSql.append("  WHEN '1'  THEN (SELECT xss.balance FROM x_staff_sum  xss WHERE xss.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '3' THEN (SELECT xss.balance FROM x_staff_sum  xss WHERE xss.user_id = xo.for_who_id )  ");
			orderSql.append("  WHEN '2' THEN (SELECT xds.dept_sum FROM x_dept_sum xds WHERE xds.dept_id = xo.for_who_id )  ");
			orderSql.append("  END objbalance,  ");
			orderSql.append("  (SELECT xu.real_name FROM x_user xu WHERE xu.user_id = xo.user_id)   orderName  ");
			orderSql.append("  FROM x_orders xo   ");
			
			orderSql.append("  WHERE xo.for_who_id = '"+userId+"'  ");
			orderSql.append("  and xo.flag = '3' ");
			orderSql.append("  and xo.order_type = 'PRO'"); // 查询条件：产品		
			orderSql.append("  and xo.del_flag ='0' order by ordertime desc");
			List<Map> l = this.getPubDao().findBySqlToMap(orderSql.toString());
			if (ListUtil.isNotEmpty(l)) {
				orderList = new ArrayList<MyProOrder>();
				for (Map map : l) {
					myOrder = new MyProOrder();
					myOrder.setBalance((String) map.get("objbalance"));
					myOrder.setOrderObj((String) map.get("orderobj"));
					myOrder.setOrderTime(map.get("ordertime").toString());
					myOrder.setId((String) map.get("id"));
					myOrder.setFlag((String) map.get("flag"));
					myOrder.setForWhoId((String) map.get("for_who_id"));
					myOrder.setOrderType((String) map.get("order_type"));
					myOrder.setOrderName((String) map.get("orderName"));
					myOrder.setSendOutFlag(map.get("send_out_flag")+"");
					myOrder.setExpressNum((String)map.get("express_num"));
					if(map.get("rec_flag") != null){
						myOrder.setRecFlag((Integer)map.get("rec_flag"));
					}
					
					if(map.get("send_out_time") != null){
						long sendOutTime = ((Date) map.get("send_out_time")).getTime();
						myOrder.setSendOutTime(sendOutTime);
					}

					StringBuffer detailSql = new StringBuffer();
					detailSql
							.append("  SELECT xpod.pro_num,xp.pro_name,xp.pro_price,xp.pro_describe,xp.pro_image_url,xp.pro_code FROM x_pro_order_detail xpod  ");
					detailSql
							.append("  LEFT JOIN x_products xp ON xp.pro_code = xpod.pro_id   ");
					detailSql.append("  WHERE order_id = '"
							+ map.get("id").toString() + "'");
					List<Map> detailMap = this.getPubDao().findBySqlToMap(
							detailSql.toString());
					int total = 0; // 总计
					if (ListUtil.isNotEmpty(detailMap)) {
						for (Map m : detailMap) {
							// 总计
							total += Integer.valueOf(m.get("pro_price")
									.toString())
									* Integer.valueOf(m.get("pro_num")
											.toString());
						}
					}
					myOrder.setDetailList(detailMap);
					myOrder.setMoneyTotal(total+"");
					
					//收货人信息
					XReceiverInfo xReceiverInfo = xReceiverInfoService.findReceiverById((String)map.get("rec_id"));
					if(xReceiverInfo != null){
						String recArea = xReceiverInfo.getRecArea();
						String recAreaNew = recArea.replaceAll("/", " ");
						xReceiverInfo.setRecArea(recAreaNew);
						myOrder.setxReceiverInfo(xReceiverInfo);
					}
					
					orderList.add(myOrder);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("获取我的订单失败！");
		}
		
		if(ListUtil.isNotEmpty(orderList)){
			return orderList;
		}
		
		return null;
	}

	@Override
	public XOrders getResOrder(String id) {
		XOrders xOrders = new XOrders();
		String sql = "SELECT * FROM x_orders WHERE id = '"+id+"'";
		List<Map> list = this.getPubDao().findBySqlToMap(sql);
		if(list != null && list.size() > 0){
			xOrders.setDelFlag((Integer)list.get(0).get("del_flag"));
			xOrders.setExpressNum((String)list.get(0).get("express_num"));
			xOrders.setFlag((String)list.get(0).get("flag"));
			xOrders.setForWhoId((String)list.get(0).get("for_who_id"));
			xOrders.setId((String)list.get(0).get("id"));
			xOrders.setInsTime(DateUtils2.strToDate(list.get(0).get("ins_time").toString()));
			xOrders.setInsUser((String)list.get(0).get("ins_user"));
			xOrders.setOrderCategory((String)list.get(0).get("order_category"));
			xOrders.setOrderType((String)list.get(0).get("order_type"));
			if(list.get(0).get("rec_flag") != null){
				xOrders.setRecFlag((Integer)list.get(0).get("rec_flag"));
			}
			xOrders.setRecId((String)list.get(0).get("rec_id"));
			xOrders.setSendOutFlag((Integer)list.get(0).get("send_out_flag"));
			if(list.get(0).get("send_out_time") != null){
				xOrders.setSendOutTime(DateUtils2.strToDate(list.get(0).get("send_out_time").toString()));
			}
			if(list.get(0).get("upd_time") != null){
				xOrders.setUpdTime(DateUtils2.strToDate(list.get(0).get("upd_time").toString()));
			}
			xOrders.setUpdUser((String)list.get(0).get("upd_user"));
			xOrders.setUserId((String)list.get(0).get("user_id"));
			xOrders.setVistorName((String)list.get(0).get("vistor_name"));
		}
		return xOrders;
	}

}
