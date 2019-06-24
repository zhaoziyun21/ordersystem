package com.kssj.auth.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;

import com.kssj.auth.dao.XStaffSumDao;
import com.kssj.auth.model.XStaffSum;
import com.kssj.auth.model.XUser;
import com.kssj.auth.service.XStaffSumService;
import com.kssj.base.util.FileUtil;
import com.kssj.base.util.ListUtil;
import com.kssj.frame.service.impl.GenericServiceImpl;
import com.kssj.order.dao.XDetailRecordDao;
import com.kssj.order.model.XDetailRecord;
import com.kssj.order.service.XDetailRecordService;
import com.kssj.order.service.XOrdersService;

public class XStaffSumServiceImpl extends GenericServiceImpl<XStaffSum,String> implements XStaffSumService{

	@SuppressWarnings("unused")
	private XStaffSumDao dao;
	private XDetailRecordDao xDetailRecordDao;
	
	@Resource
	private XOrdersService xOrdersService;
	
	public XStaffSumServiceImpl(XStaffSumDao dao,XDetailRecordDao xDetailRecordDao) {
		super(dao);
		this.dao = dao;
		this.xDetailRecordDao = xDetailRecordDao;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getXStaffSumBalanceById(String userId) {
		String sql  = "select  balance money from  x_staff_sum  where user_id = '"+userId+"'";
		List<Map> list = this.getPubDao().findBySqlToMap(sql);
		if(ListUtil.isNotEmpty(list)){
			return list.get(0).get("money").toString();
		}
		return null;
	}

	@Override
	public List<Map> getXStaffSumRecordById(String userId) {
		String sql = "select type,ins_time,change_money,balance,remark from x_detail_record where user_id = '"+userId+"' order by ins_time desc";
		List<Map> list = this.getPubDao().findBySqlToMap(sql);
		if(ListUtil.isNotEmpty(list)){
			return list;
		}
		return null;
	}

	@Override
	public void update(XStaffSum xStaffSum) {
		 SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "UPDATE x_staff_sum SET balance = '"+xStaffSum.getBalance()+"',upd_time='"+f.format(xStaffSum.getUpdTime())+"' where id= '"+xStaffSum.getId()+"'";
		this.dao.executeSql(sql);
		
	}
	/**
	 * @Title: doUploadFile
	 * @Description: 导入excel  
	 * @author zhaoziyun
	 * @param file
	 * @param companyId
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@Transactional()
	public Map<String, String> doUploadFile(File file,String companyId,HttpSession session) throws Exception {
		String tableName ="x_staff_sum";
		XUser user = (XUser)session.getAttribute("loginUser");
		Map<String,String> map = new HashMap<String, String>();
		Workbook wb = null;
		Sheet sheet = null;
		Row row = null;// 表格行
		try {
			Long impBatch = (new Date()).getTime();

			map.put("impBatch", impBatch.toString());
			// 将文件转成文件输入流
			InputStream is = new FileInputStream(file);
			// 判断Excel版本
			if (file.getName().toUpperCase().endsWith(".XLSX")) {
				wb = new XSSFWorkbook(is);// Excel 2007
			} else {
				wb = new HSSFWorkbook(is);// Excel 2003
			}
		 
			FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();// 解析公式结果
			// 获得第一个表格页
			sheet = wb.getSheet("sheet1");
			// 失败信息
			List<String> failureMsgList = new ArrayList<String>();
			boolean puloadStatus = true;
			if(sheet == null){
				failureMsgList.add("数据模板错误！");
				map.put("failureMsgList",failureMsgList.toString());
				return map;
			}
			
			if(failureMsgList.size() <= 0 ){
				if (sheet.getLastRowNum()<=0){
					failureMsgList.add("数据模板中没有数据！");
					map.put("failureMsgList",failureMsgList.toString());
					return map;
				}
				
				map.put("count","总条数:" + (sheet.getLastRowNum()) + "条");
				session.setAttribute("jdtCount", sheet.getLastRowNum());
				// 遍历数据
				
				// 开始写入数据库
				// 成功条数
				int successNum = 0;
				// 行数
				int rowNum = 0;
				// 列数
				int cellNum = 0;
				
				String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());// 当前时间
				// 获取某一行
				row = sheet.getRow(1);
				short colNo = row.getLastCellNum();
				//模板为2列
				if (colNo!=2){
					failureMsgList.add("数据模板错误！");
					map.put("failureMsgList",failureMsgList.toString());
					return map;
				}
				for (int j = 1; j <= sheet.getLastRowNum(); j++) {
					row = sheet.getRow(j);
					if (FileUtil.isBlankRow(row)) {
						continue;
					}
					session.setAttribute("jdtCurrent", j);
					System.out.println(session.getAttribute("jdtCurrent"));
					rowNum = j;
					
					//2列
						if(row == null){
							failureMsgList.add(" 第 " + (rowNum) + " 行数据不能为空 ");
						}else{
							if((row.getCell(0)!= null && !"".equals(row.getCell(0)))&&(row.getCell(1) != null && !"".equals(row.getCell(1)))){
							//列全不为空
								String name = FileUtil.getValue(row.getCell(0), formulaEvaluator);
								String num = FileUtil.getValue(row.getCell(1), formulaEvaluator);
								String s="select * from x_user where company_id ='"+companyId+"' and real_name='"+name+"'";
								List<Map> staffList = this.dao.findBySqlToMap(s, null, 0, 0);
								if(ListUtil.isNotEmpty(staffList)){
									if(staffList.size()==1){
										//获取当前余额
										String userId = staffList.get(0).get("user_id").toString();
										String balance = xOrdersService.getBalanceById("0", userId);
										int balaceNew = Integer.parseInt(balance)+Integer.parseInt(num);
										// 拼接字段
										String sql = "update   "+tableName+" set balance = '"+balaceNew+"' where user_id ='"+userId+"'";
										this.dao.executeSql(sql.toString());
										XDetailRecord xDetailRecord = new XDetailRecord();
										xDetailRecord.setBalance(balaceNew+"");
										xDetailRecord.setChangeMoney(num);
										xDetailRecord.setUserId(userId);
										xDetailRecord.setType("0");
										xDetailRecord.setRemark("excel导入充值");
										xDetailRecord.setInsTime(new Date());
										xDetailRecord.setInsUser(user.getUserId());
										xDetailRecordDao.save(xDetailRecord);
										successNum++;
									}else if(staffList.size()>1){
										//有重名不保存，提示
										failureMsgList.add(0,"姓名为："+name+",有重名,请手动导入");
									}
								}
							}else{
								failureMsgList.add(" 第 " + (rowNum) + " 行数据列不能为空 ");
							}
						}
				}
				map.put("success","成功上传" + successNum + "条!");
				map.put("countData",successNum+"");
			}else{
				puloadStatus = false;
			}
			map.put("failureMsgList",failureMsgList.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getStackTrace());
			logger.error(e.getMessage());
			throw e;
		}finally{
		}
		return map;
	}
}
