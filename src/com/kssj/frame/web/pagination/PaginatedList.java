package com.kssj.frame.web.pagination;

import java.util.List;

public abstract interface PaginatedList
{
  @SuppressWarnings("rawtypes")
  public abstract List getList();

  public abstract int getPageNumber();

  public abstract int getObjectsPerPage();

  public abstract int getFullListSize();

  public abstract int getTotalPageCount();

  public abstract String getSortCriterion();

  public abstract SortOrderEnum getSortDirection();

  public abstract String getSearchId();
}