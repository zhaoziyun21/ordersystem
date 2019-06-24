package com.kssj.frame.web.pagination;

import java.util.List;


@SuppressWarnings("rawtypes")
public class PaginatedListHelper implements PaginatedList {

	private List list; // 数据集

	private int pageNumber = 1;// 页号

	private int objectsPerPage = 10;// 每页显示的记录数

	private int fullListSize = 0;// 全部记录数

	private String sortCriterion;

	private SortOrderEnum sortDirection;

	private String searchId;

	private Object searchObject;

	private String sortName;

	private boolean isAsc;

	private int totalPageCount=0;

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getObjectsPerPage() {
		return objectsPerPage;
	}

	public void setObjectsPerPage(int objectsPerPage) {
		this.objectsPerPage = objectsPerPage;
	}

	public int getFullListSize() {
		return fullListSize;
	}

	public void setFullListSize(int fullListSize) {
		this.fullListSize = fullListSize;
	}

	public String getSortCriterion() {
		return sortCriterion;
	}

	public void setSortCriterion(String sortCriterion) {
		this.sortCriterion = sortCriterion;
	}

	public SortOrderEnum getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(SortOrderEnum sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}

	public static PaginatedListHelper getHelperFromPage(Page page){
		PaginatedListHelper pageHelper=new PaginatedListHelper();
		pageHelper.setFullListSize(new Long(page.getTotalCount()).intValue());
		pageHelper.setList((List)page.getResult());
		pageHelper.setObjectsPerPage(page.getPageSize());
		pageHelper.setPageNumber(new Long(page.getCurrentPageNo()).intValue());
		return pageHelper;
	}

	public Object getSearchObject() {
		return searchObject;
	}

	public void setSearchObject(Object searchObject) {
		this.searchObject = searchObject;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public boolean isAsc() {
		return isAsc;
	}

	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}
	public static PaginatedListHelper getHelper(Page page,String orderBy, boolean isAsc){
		PaginatedListHelper pageHelper=new PaginatedListHelper();
		pageHelper.setFullListSize(new Long(page.getTotalCount()).intValue());//总个数
		pageHelper.setList((List)page.getResult());//当前页的结果集
		pageHelper.setObjectsPerPage(page.getPageSize());//每页显示的记录数
		pageHelper.setTotalPageCount(new Long(page.getTotalPageCount()).intValue());//总页数
		pageHelper.setSortName(orderBy);
		pageHelper.setSortCriterion(orderBy);
		pageHelper.setSortDirection((isAsc?SortOrderEnum.ASCENDING:SortOrderEnum.DESCENDING));
		pageHelper.setPageNumber(new Long(page.getCurrentPageNo()).intValue());//当前页码
		return pageHelper;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

}