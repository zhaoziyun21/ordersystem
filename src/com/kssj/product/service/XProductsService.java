package com.kssj.product.service;

import java.util.List;
import java.util.Map;

import com.kssj.auth.model.XUser;
import com.kssj.frame.service.GenericService;
import com.kssj.order.model.MyOrder;
import com.kssj.order.model.XOrders;
import com.kssj.product.model.MyProOrder;
import com.kssj.product.model.XProducts;

public interface XProductsService extends GenericService<XProducts, String> {

	//根据产品名称进行查询
	List<Map> getProductsByCondtion(String proName, String proCateId);

	//查询产品库存
	String getProRemainReserveByProductId(String productId);

	Map<String, Object> orderReserve(String dcType, XUser orderUser,
			String userId, List<Map> listMap, int z_num, String vistorName,
			String time) throws Exception;

	//产品订单详情
	List<MyProOrder> getMyOrder(String userId) throws Exception;

	//产品预定详情
	List<MyProOrder> getReserveMyOrder(String userId) throws Exception;

	//取消订购产品
	Map<String, Object> deleteOrder(String orderId, String moneyTotal, XUser orderUser) throws Exception;

	//指定人查询产品订单信息
	List<MyProOrder> getMyOrderAppoint(String userId) throws Exception;

	XOrders getResOrder(String id);

}
