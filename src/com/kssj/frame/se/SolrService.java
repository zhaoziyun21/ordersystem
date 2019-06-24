package com.kssj.frame.se;

import java.util.List;
import java.util.Map;

import com.kssj.frame.se.command.SeQueryFilter;


@SuppressWarnings("rawtypes")
public interface SolrService {
	
	/**
	* @method: testIndex
	* @Description: 创建索引
	* 				定时执行：每天晚上12点。
	*
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-15 下午03:00:06
	*/
	public void index() throws Exception;
	
	/**
	* @method: insert
	* @Description: 添加
	*				索引字段：id，
	* 				分类（只有资源才有分类：10大类）， -- category
	* 				类别（资源、图片、音频、视频），--type
	* 				标题， --name
	* 				发布人， --author
	* 				内容（只有资源类为“内容+文档内容”）， --content
	* 				代表图片（只用于图片/视频/音频），--reprePic
	* 				访问量 --scans
	* 
	*
	* @param id		: 索引ID
	* @param content：索引内容
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-28 上午09:46:17
	*/
	public void insertEntity(Map<String, String> entityMap) throws Exception;
	
	/**
    * @method: deleteByIds
    * @Description: 根据id集合从索引中删除记录
    *
    * @param idName
    * @param ids
    *
    * @author: ChenYW
    * @date 2014-6-5 下午7:07:34
    */
	public void deleteByParams(String idName,List ids) throws Exception;
	
	/**
	* @method: updateEntity
	* @Description: 更新单个实体
	*
	* @param red
	* @throws Exception
	* @return void
	*
	* @author: ChenYW
	* @date 2014-6-5 下午9:04:32
	*//*
	public void updateEntity(RedResource red) throws Exception;*/
	
	/**
	* @method: testSearch
	* @Description: 高亮检索 TODO 高级检索
	* 
	* 		权重分配问题:
				有两种方法，在查询参数中指定，和配置到文件当中。
				①用solrj指定：query.set("defType","dismax");
				②solrconfig.xml：
					<requestHandler name="search" class="solr.SearchHandler" default="true">
					     <lst name="defaults">
						       <str name="echoParams">explicit</str>
						       <str name="defType">edismax</str><!-- 设置权重 -->
						       <int name="rows">10</int>
						       <bool name="hl">true</bool><!-- 设置高亮 -->
						       <str name="hl.fl">title,content</str> 
						       <str name="f.content.hl.fragsize">200</str>
						       <str name="qf">
						         id^20.0 info^15 title^10.0 content^1.0
						       </str>
					     </lst>
					</requestHandler>
	*
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-15 下午03:33:05
	*/
	public List<Map<String, String>> getAll(SeQueryFilter filter);
	
	/**
	* @method: testFacet
	* @Description: 切面（分组）
	*
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-15 下午03:41:18
	*/
	public Map<String, List<String>> facet(SeQueryFilter filter) ;
	
	/**
	* @method: 
	* @Description: pinyin 切面（分组）
	*
	* @author: ChenYW
	* @date 2014-4-15 下午03:41:18
	*/
	public Map<String, List<String>> facetPinyin(SeQueryFilter filter);
	
	/**
    * @method: testSpellcheck
    * @Description: 智能提示
    * 				Solr里叫做Suggest模块
    *				
    * @return void
    *
    * @author: ChenYW
    * @date 2014-4-15 下午04:29:24
    */
	public List<String> suggest(String word);
	
    /**
	* @method: testPhoneticize
	* @Description: 拼音检索
	*				??? 也会把不符合条件的检索出来
	*				(解决方法：把汉子和相应的拼音建立同义词)
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-16 下午01:44:57
	*/
    public List<Map<String, String>> phoneticize(String pinyin);
	
    /**
	* @method: testSimilar
	* @Description: 相似匹配
	* 				思路：1.在前台页面输入长篇文章
	* 					 2.将此文章存入solr
	* 					 3.把文章的id传入此方法，获取相似文档。
	*				
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-25 上午09:36:21
	*/
	public String similar(String id);
	
	/**
    * @method: spellCheck
    * @Description: 拼写检查 
    * 
    * @author: ChenYW
    * @date 2014-4-15 下午06:14:56
    */
    public String spellCheck(String word);
    
    /**
	* @method: optimize
	* @Description: 优化Lucene 的索引文件以改进搜索性能。
	*
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-21 上午09:50:18
	*/
	public void optimize();
	
	/**
	* @method: getAllClustering
	* @Description: 获取所有资源的聚类
	*
	* @return
	* @return List<Map<String,String>>
	*
	* @author: ChenYW
	* @date 2014-7-4 下午4:00:45
	*/
	public List<Map<String, String>> getAllClustering();
}
