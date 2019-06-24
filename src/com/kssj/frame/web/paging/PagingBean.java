package com.kssj.frame.web.paging;


/**
* @Description: web分页pojo类（结合框架）
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午04:21:05
* @version V1.0
*/
public class PagingBean{
	
	public static final String PAGING_BEAN="_paging_bean";
	
	/**  The default number of each page display */
	public static Integer DEFAULT_PAGE_SIZE=10;
	/**  最多显示页码数 */
	public static final int SHOW_PAGES=6;
	/**  每页开始的索引号 */
	public Integer start;
	/**  页码大小--每页记录数 */
	private Integer pageSize;
	/**  总记录数 */
	private Integer totalItems;
	
	
	public PagingBean(int start,int limit){
		this.pageSize=limit;
		this.start=start;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;		
				
	}
	
	public int getFirstResult(){
		return start;
	}

}
