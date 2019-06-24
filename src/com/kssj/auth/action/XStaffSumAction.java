package com.kssj.auth.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.google.code.morphia.annotations.Reference;
import com.kssj.auth.model.XUser;
import com.kssj.auth.service.XStaffSumService;
import com.kssj.base.util.Constants;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.command.sql.SqlSpellerDbType;
import com.kssj.frame.web.action.BaseAction;

@SuppressWarnings("serial")
public class XStaffSumAction extends BaseAction{
	@Resource
	private XStaffSumService xStaffSumService;
	
	private Map<String,String> uploadMsp;
	private File uploadFile;// 封装文件属性
	private String uploadFileFileName;// 文件名称
	private String filePath;

	public Map<String, String> getUploadMsp() {
		return uploadMsp;
	}

	public void setUploadMsp(Map<String, String> uploadMsp) {
		this.uploadMsp = uploadMsp;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * add  dingzhj at date 2017-03-04  查询 微信订餐余额
	 * @return
	 * @throws Exception
	 */
	public String getXStaById() throws Exception{
		try {
			XUser loginUser = (XUser)this.getOrderUserBySession();
			String user_id =loginUser.getUserId();
			//String user_id ="3";
			String balance = xStaffSumService.getXStaffSumBalanceById(user_id);
			List<Map> recordList = xStaffSumService.getXStaffSumRecordById(user_id);
			this.getRequest().setAttribute("balance", balance);
			this.getRequest().setAttribute("recordList", recordList);
			this.setForwardPage("/orders/balance.jsp");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("查询订餐余额失败！");
		}
		
		
	}
	
	/**
	 * add dingzhj at date 2017-03-04 订餐余额明细
	 * @return
	 * @throws Exception
	 */
	public String goXStaDetailed() throws Exception{
		try {
			XUser loginUser = (XUser)this.getRequest().getSession().getAttribute("loginUser");
			List<Map> recordList = xStaffSumService.getXStaffSumRecordById(loginUser.getUserId());
			this.getRequest().setAttribute("recordList", recordList);
			this.setForwardPage("/jsp/auth/xstaffsum/xStaffSum_record.jsp");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("查询订餐余额明细失败！");
		}
		
	}
	
	/**
	* @Title: upload123
	* @Description: 导入excel
	* @author liyy
	* @return
	*/
	public String upload(){
		try {
			this.getSession().setAttribute("jdtCount", null);
			this.getSession().setAttribute("jdtCurrent", null);
			String companyId = this.getRequest().getParameter("companyId");
			String extName = "";
			String newFileName = "";
			// 设置传入的文件的编码 
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			// 服务器目录
			String targetDirectory = ServletActionContext.getServletContext().getRealPath("/upload");
			 
			if (uploadFileFileName!=null&&uploadFileFileName.lastIndexOf(".") >= 0) {
				extName = uploadFileFileName.substring(uploadFileFileName
						.lastIndexOf("."));
			}
			
			// 设置上传文件的新文件名
			String nowTime = new SimpleDateFormat("yyyymmddHHmmss").format(new Date());// 当前时间
			newFileName = nowTime + extName;
			// 生成上传的文件对象
			File targetFile = new File(targetDirectory, newFileName);
			// 文件已经存在删除原有文件
			if (targetFile.exists()) {
				targetFile.delete();
			}
			// 复制file对象上传
			if(uploadFile!=null){
				FileUtils.copyFile(uploadFile, targetFile);
			}
			// 上传数据到数据库
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			if(uploadFileFileName!=null){
//				companyId="101";
				companyId="1";
				   uploadMsp = xStaffSumService.doUploadFile(targetFile,companyId,this.getSession());
				String impType = getRequest().getParameter("impType");   
				if(uploadMsp.get("failureMsgList") != null && !"[]".equals(uploadMsp.get("failureMsgList"))){
					 uploadMsp.put("isFail", "true");
				}else{
					 uploadMsp.put("isFail", "false");
				}
				this.setForwardPage("/jsp/auth/xstaffsum/result.jsp");
			}
			// 删除上传数据
			targetFile.delete();
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getStackTrace());
			logger.error(e.getMessage());
		}
	
		return SUCCESS;
	}
	
	public String goExportStaffExcel(){
		this.setForwardPage("/jsp/auth/xstaffsum/exportStaffExcel.jsp");
		return SUCCESS;
	}
}
