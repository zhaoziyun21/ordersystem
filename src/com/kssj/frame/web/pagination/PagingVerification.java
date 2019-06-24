package com.kssj.frame.web.pagination;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
* @Description: 分页的页脚参数校验
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-6-11 下午9:04:01
* @version V1.0
*/
public class PagingVerification {
	private static Logger log = Logger.getLogger(PagingVerification.class);

	private int pageNumber;
	private static int pageSize = 15;

	/**
	 * 分页校验
	 * 
	 * @param request
	 * @return
	 */
	public boolean verification(HttpServletRequest request) {

		this.setPageNumber(1);

		if (!StringUtils.isBlank(request.getParameter("pageNo"))) {

			try {
				int pageNo = Integer.parseInt(request.getParameter("pageNo"));
				int totalPageCount = Integer.parseInt(request.getParameter("totalPageCount"));
				String action = request.getParameter("action");

				if (StringUtils.isBlank(action)) {
					log.debug("方法verification 校验失败！action：执行的动作不存在！");
					return false;
				}

				if (action.equals("chosen")) {
					if (pageNo > 0 && pageNo <= totalPageCount) {
						this.setPageNumber(pageNo);
					}
				} else if (action.equals("prev")) {
					if (pageNo > 1) {
						pageNo = pageNo - 1;
						this.setPageNumber(pageNo);
					}
				} else if (action.equals("next")) {
					if (pageNo >= 1 && pageNo < totalPageCount) {
						pageNo = pageNo + 1;
						this.setPageNumber(pageNo);
					}
				}

				return true;
			} catch (Exception ex) {
				log.error("方法verification 校验失败！" + ex.getMessage());
				return false;
			}
		}

		return true;

	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public static int getPageSize() {
		return pageSize;
	}

	public static void setPageSize(int pageSize) {
		PagingVerification.pageSize = pageSize;
	}

	
	
}
