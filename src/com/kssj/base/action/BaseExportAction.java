package com.kssj.base.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.google.gson.Gson;
import com.kssj.base.util.ExportUtil;
import com.kssj.frame.web.action.BaseAction;

/**
 * @Title: BaseExportAction
 * @Description: 导出excel
 * @Company: tgrf
 * @author liyy
 * @date 2015-4-15 下午1:56:04
 */
public class BaseExportAction extends BaseAction {

	private String fileName; // 文件名
	private String colId; // 列id
	private String colName; // 列名
	private static List<Map> cbList;// 成本list
	private static List<Map> cbmbList;// 成本目标list
	private static List<Map> xsList;// 销售list
	private static List<Map> cwList;// 财务list
	private static List<Map> jhyyList;// 财务list
	private String type; // 导出列表类型
	public static final String[] cb = { "土地成本", "土地出让金", "契税", "征地拆迁费", "回建费",
			"附加物迁移费", "其他", "前期工程费", "设计费", "勘测费", "报批报建费", "咨询费", "工程监理及质监费",
			"三通一平费", "工程检测费", "专项基金及劳保费", "增容费", "其他前期费用", "建筑安装工程费", "基础工程费",
			"大型土石方及大型挡土墙工程费", "主体工程", "室内装修工程", "幕墙工程", "样板房家俬采购", "酒店开业用品费采购",
			"大型设备采购及安装", "其他工程", "市政建设设施配套费（费用类）", "社区市政工程费", "园林环境工程", "配套设施",
			"其他配套工程", "开发间接费", "临时售楼部工程", "非实楼样板房工程", "施工水电费（非施工单位使用）",
			"其他间接费", "营销费用", "媒体广告费", "销售制作费", "销售活动费", "销售设计费", "促销费",
			"销售代理费", "其他", "管理费用", "人力成本", "行政类费用", "财务费用", "利息收入", "手续费",
			"利息支出", "营业税及附加", "房地产税金", "租赁税金", "其他", "维度", "时间维度","组织结构维度",
			"地块维度","项目维度","产品类型维度","楼栋维度"};
	public static final String[] xs = { "销控总揽", "项目在建总面积", "项目在售面积", "预售证面积",
			"已售/未售/销控", "认筹总揽", "认筹", "退筹", "回款", "应收款", "未回收", "已回收", "目标",
			"认购目标", "签约目标", "回款目标", "成交", "认购", "签约", "剩余", 
			"指标","本月认购","本月签约","本月回款","本月去化","环比认购","环比签约","环比回款","环比去化","维度", "时间维度",
			"组织机构维度","地块维度","项目维度","产品类型维度","户型维度","楼栋维度","费率","预算目标费率","预算实际费率","支付费率"};
	public static final String[] cw = { "本期收入", "主营业务收入", "其他业务收入", "营业外收入",
			"本期支出", "主营业务成本", "其他业务成本", "管理费用", "销售费用", "财务费用", "营业外支出", "利润",
			"主营业务利润", "销售利润", "营业利润","回款","已回款","未回款","应回款","项目收入组成","总费用收入","业务收入","地产类收入",
			"投资股权收入","银行借贷收入","其他收入","项目支出组成","总费用支出","业务支出","地产类支出",
			"投资股权支出","银行借贷支出","其他支出","项目利润组成","总费用利润","业务利润","地产类利润",
			"投资股权利润","银行借贷利润","其他利润","现金流","1月预算","2月预算","3月预算","4月预算","5月预算","6月预算",
			"7月预算","8月预算","9月预算","10月预算","11月预算","12月预算","1月实际","2月实际","3月实际","4月实际",
			"5月实际","6月实际","7月实际","8月实际","9月实际","10月实际","11月实际","12月实际",
			"资产","非流动资产","流动资产","负债","非流动负债","流动负债","维度", "时间维度","组织结构维度","项目维度" };
	public static final String[] jhyy = { "吻合率", "完成度", "是否延期","是否风险","维度", "项目维度",
			"组织结构维度" };

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getColId() {
		return colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	/**
	 * @Title: exportLocal
	 * @Description: 下载模板
	 * @author liyy fileName：导出文件名 colName:列名 colId：列对应的字段名 type:导出哪个list
	 *         例子url：http
	 *         ://localhost:8080/test/export/exportLocalBaseExport.do?fileName
	 *         =456&colName=cai1,cai2&colId=name,url&type=test
	 * @throws Exception
	 */

	public void exportLocal() throws Exception {
		try {
			String path = getRequest().getSession().getServletContext()
					.getRealPath("\\template\\");
			String fileName = getRequest().getParameter("fileName");
			File f = new File(path + fileName + ".xls");
			if (!f.exists()) {// 判断文件是否真正存在,如果不存在,创建一个;
				f.createNewFile();
			}
			logger.error(f.getName());
			// 设置response的编码方式
			getResponse().setContentType("application/x-download");

			// // 写明要下载的文件的大小
			// getResponse().setContentLength((int) f.length());

			// 设置附加文件名
			// getResponse().setHeader("Content-Disposition","attachment;fname="+fname);

			// 解决中文乱码
			getResponse().setHeader(
					"content-disposition",
					"attachment;filename="
							+ new String(fileName.getBytes("gb2312"),
									"ISO8859-1") + ".xls");

			// 读出文件到i/o流
			FileInputStream fis = new FileInputStream(f);
			BufferedInputStream buff = new BufferedInputStream(fis);

			byte[] b = new byte[1024];// 相当于我们的缓存

			long k = 0;// 该值用于计算当前实际下载了多少字节

			// 从response对象中得到输出流,准备下载
			String typeList = getRequest().getParameter("typeList");
			typeList = java.net.URLDecoder.decode(typeList, "UTF-8");
			//List<Map> list =new ArrayList<Map>();
			List<Map> list = null;
			if("CZ".equals(typeList)){
				list = (List<Map>) this.getSession().getAttribute("orderList");
			}
			if("TJ".equals(typeList)){
				list = (List<Map>) this.getSession().getAttribute("tjList");
			}
			if ("CO".equals(typeList)) {
				list = (List<Map>) this.getSession().getAttribute("chargeObjList");
			}
			
		/*	JSONArray jsonArray = JSONArray.fromObject(typeList);  
	       // JSONArray new_jsonArray=JSONArray.fromObject(jsonArray.toArray());  
	        Collection java_collection=JSONArray.toCollection(jsonArray);  
	        if(java_collection!=null && !java_collection.isEmpty())  
	        {  
	            Iterator it=java_collection.iterator();  
	            while(it.hasNext())  
	            {  
	                JSONObject jsonObj=JSONObject.fromObject(it.next());  
	                Map map = (Map)jsonObj;
	                list.add(map);
	            }  
	        }*/
	        
			OutputStream myout = getResponse().getOutputStream();
			
			colId = java.net.URLDecoder.decode(colId, "UTF-8");
			colName = java.net.URLDecoder.decode(colName, "UTF-8");
			logger.error(colId);
			logger.error(colName);
			ExportUtil.ExportXls(list, myout, colId, colName);
			// 开始循环下载

			while (k < f.length()) {

				int j = buff.read(b, 0, 1024);
				k += j;

				// 将b中的数据写到客户端的内存
				myout.write(b, 0, j);

			}
			// 将写入到客户端的内存的数据,刷新到磁盘
			myout.flush();
			myout.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @method: upload
	 * @Description: 多文件上传
	 * @throws Exception
	 * @author : lig
	 * @date 2016-10-12 上午10:46:47
	 */
	public void upload() throws Exception {
		MultiPartRequestWrapper multipartRequest = (MultiPartRequestWrapper) this
				.getRequest();
		File[] fs = multipartRequest.getFiles("Filedata");
		InputStream in = null;
		OutputStream out = null;
		String fileName = multipartRequest.getFileNames("Filedata")[0];
		try {
			String path = ServletActionContext.getServletContext().getRealPath(
					"images");
			String url = "images/" + System.currentTimeMillis() + "."
					+ fileName.split("\\.")[1];
			path += "/" + System.currentTimeMillis() + "."
					+ fileName.split("\\.")[1];
			// String url="upload/"+fileName;
			// path +="/"+fileName;

			File newFile = new File(path);
			in = new BufferedInputStream(new FileInputStream(fs[0]));
			out = new BufferedOutputStream(new FileOutputStream(newFile));
			byte[] buffer = new byte[1024];
			while (in.read(buffer) > 0) {
				out.write(buffer);
			}
			this.getResponse().setContentType("text/json; charset=utf-8");
			PrintWriter o = this.getResponse().getWriter();
			o.println(url);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				in.close();
			}
			if (null != out) {
				out.close();
			}
		}
	}
	
	public void analyzeExcel2() throws IOException {
		cwList = new ArrayList<Map>();
		cbList = new ArrayList<Map>();
		cbmbList = new ArrayList<Map>();
		xsList = new ArrayList<Map>();
		jhyyList = new ArrayList<Map>();
		String path = getRequest().getSession().getServletContext().getRealPath("template"); 
		File file = new File(path+"/模板1.xlsx");
		if (!file.exists()) {
			System.out.println("文件不存在");
			return;
		}
		try {
			InputStream inputStream = new FileInputStream(file);
			String fileName = file.getName();
			Workbook wb = null;
			if (fileName.endsWith("xls")) {
				wb = new HSSFWorkbook(inputStream);// 解析xls格式
			} else if (fileName.endsWith("xlsx")) {
				wb = new XSSFWorkbook(inputStream);// 解析xlsx格式
			}
			int index = wb.getNumberOfSheets();
			for (int i = 0; i < index; i++) {
				Sheet sheet = wb.getSheetAt(i);// 获取工作表
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();
				Row row = sheet.getRow(lastRowIndex);
				int firstCellIndex = row.getFirstCellNum();
				int lastCellIndex = row.getLastCellNum();
				// sheet页的数据分别放入不同的list<Map>中
				switch (i) {
				case 0:
					for (int cIndex = firstCellIndex + 2; cIndex < lastCellIndex; cIndex++) {
						Map<String, String> m = new HashMap<String, String>();
						for (int rIndex = firstRowIndex; rIndex <=lastRowIndex; rIndex++) {
							Row r = sheet.getRow(rIndex);
							Cell cell = r.getCell(cIndex);
							if (cell != null) {
								cell.setCellType(Cell.CELL_TYPE_STRING);
							}
							m.put(cb[rIndex], getValue(cell));
						}
						if (cIndex % 2 == 0) {
							cbList.add(m);
						} else {
							cbmbList.add(m);
						}
					}
					break;
				case 1:
					for (int cIndex = firstCellIndex + 2; cIndex < lastCellIndex; cIndex++) {
						Map<String, String> m = new HashMap<String, String>();
						for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
							Row r = sheet.getRow(rIndex);
							Cell cell = r.getCell(cIndex);
							if (cell != null) {
								cell.setCellType(Cell.CELL_TYPE_STRING);
							}
							m.put(xs[rIndex], getValue(cell));
						}
						xsList.add(m);
					}
					break;
				case 2:
					for (int cIndex = firstCellIndex + 2; cIndex < lastCellIndex; cIndex++) {
						Map<String, String> m = new HashMap<String, String>();
						for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
							Row r = sheet.getRow(rIndex);
							Cell cell = r.getCell(cIndex);
							if (cell != null) {
								cell.setCellType(Cell.CELL_TYPE_STRING);
							}
							m.put(cw[rIndex], getValue(cell));
						}
						cwList.add(m);
					}
					break;
				case 3:
					for (int cIndex = firstCellIndex + 2; cIndex < lastCellIndex; cIndex++) {
						Map<String, String> m = new HashMap<String, String>();
						for (int rIndex = firstRowIndex; rIndex <=lastRowIndex; rIndex++) {
							Row r = sheet.getRow(rIndex);
							Cell cell = r.getCell(cIndex);
							if (cell != null) {
								cell.setCellType(Cell.CELL_TYPE_STRING);
							}
							m.put(jhyy[rIndex], getValue(cell));
						}
						jhyyList.add(m);
					}
					break;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void analyzeExcel1() throws FileNotFoundException {
		cwList = new ArrayList<Map>();
		cbList = new ArrayList<Map>();
		cbmbList = new ArrayList<Map>();
		xsList = new ArrayList<Map>();
		jhyyList = new ArrayList<Map>();
		File file = new File("F:/模板1.xlsx");
		if (!file.exists()) {
			System.out.println("文件不存在");
			return;
		}
		InputStream inputStream = new FileInputStream(file);
		try {
			
			String fileName = file.getName();
			Workbook wb = null;
			if (fileName.endsWith("xls")) {
				wb = new HSSFWorkbook(inputStream);// 解析xls格式
			} else if (fileName.endsWith("xlsx")) {
				wb = new XSSFWorkbook(inputStream);// 解析xlsx格式
			}
			int index = wb.getNumberOfSheets();
			for (int i = 0; i < index; i++) {
				Sheet sheet = wb.getSheetAt(i);// 获取工作表
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();
				Row row = sheet.getRow(lastRowIndex);
				int firstCellIndex = row.getFirstCellNum();
				int lastCellIndex = row.getLastCellNum();
				// sheet页的数据分别放入不同的list<Map>中
				switch (i) {
				case 0:
					for (int cIndex = firstCellIndex + 2; cIndex < lastCellIndex; cIndex++) {
						Map<String, String> m = new HashMap<String, String>();
						for (int rIndex = firstRowIndex; rIndex <=lastRowIndex; rIndex++) {
							Row r = sheet.getRow(rIndex);
							Cell cell = r.getCell(cIndex);
							if (cell != null) {
								cell.setCellType(Cell.CELL_TYPE_STRING);
							}
							m.put(cb[rIndex], getValue(cell));
						}
						if (cIndex % 2 == 0) {
							cbList.add(m);
						} else {
							cbmbList.add(m);
						}
					}
					break;
				case 1:
					for (int cIndex = firstCellIndex + 2; cIndex < lastCellIndex; cIndex++) {
						Map<String, String> m = new HashMap<String, String>();
						for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
							Row r = sheet.getRow(rIndex);
							Cell cell = r.getCell(cIndex);
							if (cell != null) {
								cell.setCellType(Cell.CELL_TYPE_STRING);
							}
							m.put(xs[rIndex], getValue(cell));
						}
						xsList.add(m);
					}
					break;
				case 2:
					for (int cIndex = firstCellIndex + 2; cIndex < lastCellIndex; cIndex++) {
						Map<String, String> m = new HashMap<String, String>();
						for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
							Row r = sheet.getRow(rIndex);
							Cell cell = r.getCell(cIndex);
							if (cell != null) {
								cell.setCellType(Cell.CELL_TYPE_STRING);
							}
							m.put(cw[rIndex], getValue(cell));
						}
						cwList.add(m);
					}
					break;
				case 3:
					for (int cIndex = firstCellIndex + 2; cIndex < lastCellIndex; cIndex++) {
						Map<String, String> m = new HashMap<String, String>();
						for (int rIndex = firstRowIndex; rIndex <=lastRowIndex; rIndex++) {
							Row r = sheet.getRow(rIndex);
							Cell cell = r.getCell(cIndex);
							if (cell != null) {
								cell.setCellType(Cell.CELL_TYPE_STRING);
							}
							m.put(jhyy[rIndex], getValue(cell));
						}
						jhyyList.add(m);
					}
					break;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			if (null != out) {
//				out.close();
//			}
			
		}
	}

	private String getValue(Cell hssfCell) {
		if (hssfCell != null) {
			if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
				// 返回布尔类型的值
				return String.valueOf(hssfCell.getBooleanCellValue());
			} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
				// 返回数值类型的值
				return String.valueOf(hssfCell.getNumericCellValue());
			} else {
				// 返回字符串类型的值
				return String.valueOf(hssfCell.getStringCellValue());
			}
		}
		return null;
	}

	public void getCbValue() {
		Gson gson = new Gson();
		Map<String, String> a = new HashMap();
		String param = getRequest().getParameter("param");
		JSONObject jsonObject = JSONObject.fromObject(param);
		Iterator ite = jsonObject.keys();
		// 遍历jsonObject数据,添加到Map对象
		while (ite.hasNext()) {
			String key = ite.next().toString();
			String value = jsonObject.get(key).toString();
			a.put(key, value);
		}
		List<Map> cblist = new ArrayList<Map>();
		List<Map> cbmblist = new ArrayList<Map>();
		for (int i = 0; i < cbList.size(); i++) {
			boolean flag = true;
			for (Map.Entry<String, String> entry : a.entrySet()) {
				if (!entry.getValue().equals(cbList.get(i).get(entry.getKey()))) {
					flag = false;
					break;
				}
			}
			if (flag) {
				cblist.add(cbList.get(i));
			}
		}
		// 实际成本循环相加
		if (cblist.size() > 1) {
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < cblist.size(); i++) {
				Map<String, String> m = cblist.get(i);
				for (Map.Entry<String, String> entry : m.entrySet()) {
					if (map.get(entry.getKey()) != null) {
						if (m.containsKey(entry.getKey())
								&& !entry.getKey().contains("维度")) {
							if (map.get(entry.getKey()) != null&&m.get(entry.getKey()) != null) {
									map.put(entry.getKey(), String.valueOf(Double
											.parseDouble(map.get(entry.getKey()))
											+ Double.parseDouble(entry.getValue())));
							}
						}
					}else{
						map.put(entry.getKey(), entry.getValue());
					}
				}
			}
			cblist.removeAll(cblist);
			cblist.add(map);
		}
		for (int i = 0; i < cbmbList.size(); i++) {
			boolean flag = true;
			for (Map.Entry<String, String> entry : a.entrySet()) {
				if (!entry.getValue().equals(
						cbmbList.get(i).get(entry.getKey()))) {
					flag = false;
					break;
				}
			}
			if (flag) {
				cbmblist.add(cbmbList.get(i));
			}
		}
		// 目标成本循环相加
		if (cbmblist.size() > 1) {
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < cbmblist.size(); i++) {
				Map<String, String> m = cbmblist.get(i);
				for (Map.Entry<String, String> entry : m.entrySet()) {
					if (map.get(entry.getKey()) != null) {
						if (m.containsKey(entry.getKey())
								&& !entry.getKey().contains("维度")) {
							if (map.get(entry.getKey()) != null&&m.get(entry.getKey()) != null) {
									map.put(entry.getKey(), String.valueOf(Double
											.parseDouble(map.get(entry.getKey()))
											+ Double.parseDouble(entry.getValue())));
							}
						}
					}else{
						map.put(entry.getKey(), entry.getValue());
					}
				}
			}
			cbmblist.removeAll(cbmblist);
			cbmblist.add(map);
		}
		StringBuffer sb = new StringBuffer();

		sb.append("{'成本':" + gson.toJson(cblist));
		sb.append(",'成本目标':" + gson.toJson(cbmblist) + "}");
		PrintWriter out = null;
		this.getResponse().setContentType("text/json; charset=utf-8");
		try {
			out = this.getResponse().getWriter();
			out.write(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.flush();
				out.close();
			}
		}
	}

	public void getXsValue() {
		try{
			Gson gson = new Gson();
			Map<String, String> a = new HashMap();
			String param = getRequest().getParameter("param");
			JSONObject jsonObject = JSONObject.fromObject(param);
			Iterator ite = jsonObject.keys();
			// 遍历jsonObject数据,添加到Map对象
			while (ite.hasNext()) {
				String key = ite.next().toString();
				String value = jsonObject.get(key).toString();
				a.put(key, value);
			}
			List<Map> xslist = new ArrayList<Map>();
			for (int i = 0; i < xsList.size(); i++) {
				boolean flag = true;
				for (Map.Entry<String, String> entry : a.entrySet()) {
					if (!entry.getValue().equals(xsList.get(i).get(entry.getKey()))) {
						flag = false;
						break;
					}
				}
				if (flag) {
					xslist.add(xsList.get(i));
				}
			}
			int total = xslist.size();
			// 实际成本循环相加
			if (xslist.size() > 1) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < xslist.size(); i++) {
					Map<String, String> m = xslist.get(i);
					for (Map.Entry<String, String> entry : m.entrySet()) {
						if (map.get(entry.getKey()) != null) {
							if (m.containsKey(entry.getKey())
									&& !entry.getKey().contains("维度")) {
								if (map.get(entry.getKey()) != null&&m.get(entry.getKey()) != null) {
										map.put(entry.getKey(), String.valueOf(Double
												.parseDouble(map.get(entry.getKey()))
												+ Double.parseDouble(entry.getValue())));
								}
							}
						}else{
							map.put(entry.getKey(), entry.getValue());
						}
					}
				}
				xslist.removeAll(xslist);
				xslist.add(map);
			}
	
			StringBuffer sb = new StringBuffer();
			sb.append("{'销售':" + gson.toJson(xslist) + ",'total':"+total+"}");
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			this.getResponse().setHeader("Access-Control-Allow-Origin", "*");
			try {
				out = this.getResponse().getWriter();
				out.write(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(out!=null){
					out.flush();
					out.close();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void getCwValue() {
		Gson gson = new Gson();
		Map<String, String> a = new HashMap();
		String param = getRequest().getParameter("param");
		JSONObject jsonObject = JSONObject.fromObject(param);
		Iterator ite = jsonObject.keys();
		// 遍历jsonObject数据,添加到Map对象
		while (ite.hasNext()) {
			String key = ite.next().toString();
			String value = jsonObject.get(key).toString();
			a.put(key, value);
		}
		List<Map> cwlist = new ArrayList<Map>();
		for (int i = 0; i < cwList.size(); i++) {
			boolean flag = true;
			for (Map.Entry<String, String> entry : a.entrySet()) {
				if (!entry.getValue().equals(cwList.get(i).get(entry.getKey()))) {
					flag = false;
					break;
				}
			}
			if (flag) {
				cwlist.add(cwList.get(i));
			}
		}
		// 实际成本循环相加
		if (cwlist.size() > 1) {
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < cwlist.size(); i++) {
				Map<String, String> m = cwlist.get(i);
				for (Map.Entry<String, String> entry : m.entrySet()) {
					if (m.containsKey(entry.getKey())
							&& !entry.getKey().contains("维度")) {
						if (map.get(entry.getKey()) != null) {
							if (m.containsKey(entry.getKey())
									&& !entry.getKey().contains("维度")) {
								if (map.get(entry.getKey()) != null&&m.get(entry.getKey()) != null) {
										map.put(entry.getKey(), String.valueOf(Double
												.parseDouble(map.get(entry.getKey()))
												+ Double.parseDouble(entry.getValue())));
								}
							}
						}else{
							map.put(entry.getKey(), entry.getValue());
						}
				}
			}
		}
			cwlist.removeAll(cwlist);
			cwlist.add(map);
		}
		StringBuffer sb = new StringBuffer();
		sb.append("{'财务':" + gson.toJson(cwlist) + "}");
		PrintWriter out = null;
		this.getResponse().setContentType("text/json; charset=utf-8");
		try {
			out = this.getResponse().getWriter();
			out.write(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.flush();
				out.close();
			}
		}
		
	}

	public void getJhyyValue() {
		Gson gson = new Gson();
		Map<String, String> a = new HashMap();
		String param = getRequest().getParameter("param");
		JSONObject jsonObject = JSONObject.fromObject(param);
		Iterator ite = jsonObject.keys();
		// 遍历jsonObject数据,添加到Map对象
		while (ite.hasNext()) {
			String key = ite.next().toString();
			String value = jsonObject.get(key).toString();
			a.put(key, value);
		}
		List<Map> jhyylist = new ArrayList<Map>();
		for (int i = 0; i < jhyyList.size(); i++) {
			boolean flag = true;
			for (Map.Entry<String, String> entry : a.entrySet()) {
				if (!entry.getValue().equals(
						jhyyList.get(i).get(entry.getKey()))) {
					flag = false;
					break;
				}
			}
			if (flag) {
				jhyylist.add(jhyyList.get(i));
			}
		}
		int total = jhyylist.size();
		// 实际成本循环相加
		if (jhyylist.size() > 1) {
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < jhyylist.size(); i++) {
				Map<String, String> m = jhyylist.get(i);
				for (Map.Entry<String, String> entry : m.entrySet()) {
					if (map.get(entry.getKey()) != null) {
						if (m.containsKey(entry.getKey())
								&& !entry.getKey().contains("维度")&& !entry.getKey().contains("是否延期")&& !entry.getKey().contains("吻合率")) {
							if (map.get(entry.getKey()) != null&&m.get(entry.getKey()) != null) {
									map.put(entry.getKey(), String.valueOf(Double
											.parseDouble(map.get(entry.getKey()))
											+ Double.parseDouble(entry.getValue())));
							}
						}
						if(entry.getKey().contains("项目维度")||entry.getKey().contains("是否延期")||entry.getKey().contains("吻合率")){
							map.put(entry.getKey(), map.get(entry.getKey()).toString()+","+entry.getValue());
						}
					}else{
						map.put(entry.getKey(), entry.getValue());
					}
				}
			}
			jhyylist.removeAll(jhyylist);
			jhyylist.add(map);
		}
		int fengxian =0;
		if(jhyylist.size()>0){
			for (int i = 0; i < jhyylist.size(); i++) {
				Map m = jhyylist.get(i);
				if(1==Double.parseDouble(m.get("是否风险").toString())){
					fengxian++;
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("{'计划运营':" + gson.toJson(jhyylist) + ",'total':"+total+",'fengxian':"+fengxian+"}");
		PrintWriter out = null;
		this.getResponse().setContentType("text/json; charset=utf-8");
		try {
			out = this.getResponse().getWriter();
			out.write(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.flush();
				out.close();
			}
		}
	}
}
