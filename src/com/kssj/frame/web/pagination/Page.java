/*     */ package com.kssj.frame.web.pagination;
/*     */ 
/*     */ import java.io.Serializable;
import java.util.ArrayList;
/*     */ 
/*     */ @SuppressWarnings({"serial","rawtypes"})
		  public class Page implements Serializable{
/*  15 */   private static int DEFAULT_PAGE_SIZE = 20;
/*     */ 
/*  17 */   private int pageSize = DEFAULT_PAGE_SIZE;
/*     */   private long start;
/*     */   private Object data;
/*     */   private long totalCount;
/*     */ 
		  public Page()
/*     */   {
/*  29 */     this(0L, 0L, DEFAULT_PAGE_SIZE, new ArrayList());
/*     */   }
/*     */ 
/*     */   public Page(long start, long totalSize, int pageSize, Object data)
/*     */   {
/*  45 */     this.pageSize = pageSize;
/*  46 */     this.start = start;
/*  47 */     this.totalCount = totalSize;
/*  48 */     this.data = data;
/*     */   }
/*     */ 
/*     */   public long getTotalCount()
/*     */   {
/*  55 */     return this.totalCount;
/*     */   }
/*     */ 
/*     */   public long getTotalPageCount()
/*     */   {
/*  62 */     if (this.totalCount % this.pageSize == 0L) {
/*  63 */       return this.totalCount / this.pageSize;
/*     */     }
/*  65 */     return this.totalCount / this.pageSize + 1L;
/*     */   }
/*     */ 
/*     */   public int getPageSize()
/*     */   {
/*  72 */     return this.pageSize;
/*     */   }
/*     */ 
/*     */   public Object getResult()
/*     */   {
/*  79 */     return this.data;
/*     */   }
/*     */ 
/*     */   public long getCurrentPageNo()
/*     */   {
/*  86 */     return this.start / this.pageSize + 1L;
/*     */   }
/*     */ 
/*     */   public boolean hasNextPage()
/*     */   {
/*  93 */     return getCurrentPageNo() < getTotalPageCount() - 1L;
/*     */   }
/*     */ 
/*     */   public boolean hasPreviousPage()
/*     */   {
/* 100 */     return getCurrentPageNo() > 1L;
/*     */   }
/*     */ 
/*     */   protected static int getStartOfPage(int pageNo)
/*     */   {
/* 109 */     return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
/*     */   }
/*     */ 
/*     */   public static int getStartOfPage(int pageNo, int pageSize)
/*     */   {
/* 122 */     return (pageNo - 1) * pageSize;
/*     */   }
/*     */ }
