package com.kssj.auth.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.kssj.auth.model.XStaffSum;
import com.kssj.frame.service.GenericService;

public interface XStaffSumService extends GenericService<XStaffSum, String>{
	
	/**
	 * add dingzhj at date 2017-03-04 微信订餐系统余额
	 * @param id
	 * @return
	 */
	public String getXStaffSumBalanceById(String id);
	/**
	 * add dingzhj at date 2017-03-04 微信订餐系统余额明细
	 * @param userId
	 * @return
	 */
	public List<Map> getXStaffSumRecordById(String userId);
	/**
	 * 更新余额
	 * @param xStaffSum
	 */
	public void update(XStaffSum xStaffSum);
	
	
	public Map<String, String> doUploadFile(File file,String companyId,HttpSession session) throws Exception;

}
