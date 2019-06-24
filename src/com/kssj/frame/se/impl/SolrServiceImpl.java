package com.kssj.frame.se.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.kssj.base.listener.AppUtil;
import com.kssj.base.util.HttpClientUtils;
import com.kssj.base.util.StrInsFile;
import com.kssj.frame.se.SolrService;
import com.kssj.frame.se.command.SeQueryFilter;

/**
* @Description: solr工具类
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-6-5 下午7:16:29
* @version V1.0
*/
@SuppressWarnings("rawtypes")
public class SolrServiceImpl implements SolrService {
	private static Logger logger = Logger.getLogger(SolrServiceImpl.class);
	
/*	@Resource
	private RedResourceService redResourceService;
	@Resource
	private RedResCategoryService redResCategoryService;*/
	
	//TODO
	private static final List<String> indexList = new ArrayList<String>();
	static{
		indexList.add("id");
		indexList.add("category");
		indexList.add("type");
		indexList.add("name");
		indexList.add("author");
		indexList.add("content");
		indexList.add("reprePic");
		indexList.add("scans");
		
		indexList.add("downloads");
		indexList.add("createDate");
		indexList.add("publisher");
		indexList.add("publishDate");
	}
	
	
	private static HttpSolrServer server = null;
	private static String URL = null;
	/**
	* @method: init
	* @Description: 初始化连接solr--system loading...
	*
	* @author: ChenYW
	* @date 2014-5-28 下午5:42:05
	*/
	@SuppressWarnings("unchecked")
	public static void init(){
		Map<String, String> solrUrl = AppUtil.getSysConfig();
		URL = solrUrl.get("solr.url");
		server = new HttpSolrServer(URL);
	}
	
	/**
	* @method: testIndex
	* @Description: 创建索引 TODO 
	* 				定时执行：每天晚上12点。
	* 				索引字段：id，
	* 						  分类（10大类+自定义类）， -- category
	* 						  类别（资源、图片、音频、视频），--type
	* 						  标题， --name
	* 						  发布人， --author
	* 						  内容（只有资源类为“内容+文档内容”）， --content
	* 						  代表图片（只用于图片/视频/音频），--reprePic
	* 						  访问量 --scans
	* 
	* 						 下载量：downloads,
	* 						  制造时间：createDate
	* 						  发布者：publisher
	* 						  发布时间：publishDate
	*
	* @return void
	*
	* @author: ChenYW
	 * @throws IOException 
	 * @throws Exception 
	* @date 2014-4-15 下午03:00:06
	*/
	public void index() throws Exception {
		System.out.println("-------------Index:开始创建索引!");
		//①删除所有索引
		deleteAllIndex();
		
		//②创建索引
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		//SolrInputDocument doc = null;
		
		/*//取得未审核的资源list
		List<RedResource> resList1 = redResourceService.findAllRes(Constants.RES_STATUS_1);
		//取得审核通过的资源list
		List<RedResource> resList3 = redResourceService.findAllRes(Constants.RES_STATUS_3);*/
		
		//List<RedResource> resList = new ArrayList<RedResource>();
/*		resList.addAll(resList1);
		resList.addAll(resList3);
		*/
		
		/*for (RedResource red : resList) {
			doc = new SolrInputDocument();
			doc.addField("id", red.getResId());
			
			String tempType = null;
			if (red.getRcType()!=null) {
				if (red.getRcType().equals(Constants.RES_TYPE_DOC)) {
					tempType = Constants.RES_TYPE_DOC_VA;
				}else if (red.getRcType().equals(Constants.RES_TYPE_PIC)) {
					tempType = Constants.RES_TYPE_PIC_VA;
				}else if (red.getRcType().equals(Constants.RES_TYPE_VIE)) {
					tempType = Constants.RES_TYPE_VIE_VA;
				}else if (red.getRcType().equals(Constants.RES_TYPE_MUS)) {
					tempType = Constants.RES_TYPE_MUS_VA;
				}else {
					tempType = "";
				}
			}else{
				tempType = "";
			}
			doc.addField("type", tempType);
			
			//获取资源类别名称
			if (red.getRcId() != null) {
				//RedResCategory redResCategory = redResCategoryService.get(red.getRcId());
				if (redResCategory != null && redResCategory.getName() != null) {
					doc.addField("category", redResCategory.getName());
				}
			}else {
				doc.addField("category", "");
			}
			
			doc.addField("name", red.getName()==null?"":red.getName());
			doc.addField("author", red.getAuthor()==null?"":red.getAuthor());
			doc.addField("content", (red.getTempContent()==null?"":red.getTempContent())+(red.getTikaContent()==null?"":red.getTikaContent()));
			doc.addField("reprePic", red.getReprePic()==null?"":red.getReprePic());
			doc.addField("scans", red.getScans()==null?"":red.getScans().toString());
			
			doc.addField("downloads", red.getDownloads()==null?"":red.getDownloads().toString());
			doc.addField("createDate", red.getCreateDate()==null?"":red.getCreateDate().toString());
			doc.addField("publisher", red.getPublisher()==null?"":red.getPublisher().toString());
			doc.addField("publishDate", red.getPublishDate()==null?"":red.getPublishDate().toString());
			
			docs.add(doc);
		}*/
		server.add(docs);
		//优化
		optimize();
		server.commit();
		System.out.println("-------------Index:结束创建索引!");
	}
	
	/**
	* @method: insert
	* @Description: 添加单个索引：（需按以下规范传入map）
	*					索引字段：id，
	* 					分类（10大类+自定义类）， -- category
	* 					类别（资源、图片、音频、视频），--type
	* 					标题， --name
	* 					发布人， --author
	* 					内容（只有资源类为“内容+文档内容”）， --content
	* 					代表图片（只用于图片/视频/音频），--reprePic
	* 					访问量 --scans
	*
	* 					下载量：downloads,
	* 					制造时间：createDate
	* 					发布时间：publishDate
	* 
	* @param entityMap : 需要填入的key仅为：(id, category, type, name, author, content, reprePic, scans)
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-28 上午09:46:17
	*/
	public void insertEntity(Map<String, String> entityMap) throws Exception{
		SolrInputDocument doc = new SolrInputDocument();
		
		if (entityMap == null || entityMap.get("id") == null) {
			return ;
		}
		
		for (String key : entityMap.keySet()) {
			if (indexList.contains(key)) {
				doc.addField(key, entityMap.get(key));
			}
		}
		
		server.add(doc);
		server.commit();
	}
	
	/**
	* @method: deleteIndex
	* @Description: 删除所有索引
	*
	* @return void
	*
	* @author: ChenYW
	* @date 2014-6-5 下午4:14:44
	*/
	public void deleteAllIndex() throws Exception {
		server.deleteByQuery("*:*");//这样就删除了所有文档索引 
		server.commit();
	}
	
	/**
	* @method: deleteByIds
	* @Description: 根据id集合从索引中删除记录
	*
	* @param propertyName
	* @param params
	*
	* @author: ChenYW
	* @date 2014-6-5 下午7:24:06
	*/
	public void deleteByParams(String propertyName,List params) throws Exception {
        if (params.size() > 0) {
            StringBuffer query = new StringBuffer(propertyName + ":" + params.get(0));
            for (int i = 1; i < params.size(); i++) {
                if (null != params.get(i)) {
                    query.append(" OR " + propertyName + ":" + params.get(i).toString());
                }
            }
            server.deleteByQuery(query.toString());
            server.commit(false, false);
        }
    }
	
	/**
	* @method: deleteByParammaps
	* @Description: 删除：根据参数map
	*
	* @param parammaps	: map 中的key是参数名，value是参数值
	* @throws Exception
	* @return void
	*
	* @author: ChenYW
	* @date 2014-6-5 下午7:31:33
	*/
	public void deleteByParammaps(Map<String, String> parammaps) throws Exception {
		StringBuffer query = new StringBuffer();
		int i = 0;
		for (String key : parammaps.keySet()) {
			if (i == 0) {
				query.append(key + ":" + parammaps.get(key));
			} else {
				query.append(" OR " + key + ":" + parammaps.get(key));
			}
			i++;
		}
		
        server.deleteByQuery(query.toString());
        server.commit(false, false);
	}
	
	
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
	public void updateEntity(RedResource red) throws Exception{
		if(null!=red && red.getResId() != null){
             SolrQuery query = new SolrQuery();
             query.setQuery("id:" + red.getResId().toString());
             query.setStart(0);
             query.setRows(1);
             QueryResponse response = server.query(query);
             SolrDocument document = response.getResults().get(0);

//           if (red.getRcType()!=null) {document.addField("type", red.getRcType());}if (red.getCategory()!=null) {document.addField("category", red.getCategory());}if (red.getName()!=null) {document.addField("name", red.getName());}if (red.getAuthor()!=null) {document.addField("author", red.getAuthor());}if (red.getContent()!=null) {document.addField("content", red.getContent());}if (red.getReprePic()!=null) {document.addField("reprePic", red.getReprePic());}if (red.getScans()!=null) {document.addField("scans", red.getScans().toString());}
             
             UpdateRequest updateRequest = new UpdateRequest();
             
             updateRequest.setAction(AbstractUpdateRequest.ACTION.COMMIT, false, false);
             updateRequest.add(solrDocument2SolrInputDocument(document, red));
             updateRequest.process(server);
 			 //server.add(document);
 			 //optimize();
 			 //server.commit();
		}
	}*/
	
	/**
     * 更新数据时用到，给出要更新的对象，Id为必须给出的属性，然后加上要更新的属性
     * 如果对应的属性的值为空或者为0，这不需要更新
     * 
     * @param sd 查询到得SolrDocument
     * @param object
     * @return SolrInputDocument
     */
    public SolrInputDocument solrDocument2SolrInputDocument(SolrDocument sd, Object object) {
        if (object != null && sd != null) {
            SolrInputDocument sid = new SolrInputDocument();
            Collection<String> fieldNameCollection = sd.getFieldNames();            // 得到所有的属性名
            Class<?> cls = object.getClass();
            Object o = null;
            for (String fieldName : fieldNameCollection) {
                try {
                    //需要说明的是返回的结果集中的FieldNames()比类属性多
                    Field[] filedArrays = cls.getDeclaredFields();                        //获取类中所有属性
                    for (Field f : filedArrays) {    
                        //如果实体属性名和查询返回集中的字段名一致,填充对应的set方法
                        if(f.getName().equals(fieldName)){
                            // 如果对应的属性的值为空或者为0，这不需要更新
                            //o = cls.getMethod(EntityConvert.dynamicMethodName(fieldName, "get")).invoke(object);
                            f.setAccessible(true); //设置些属性是可以访问的
                            o = f.get(object);//得到此属性的值   
                            Class<?> fieldType = cls.getDeclaredField(fieldName).getType();
                            
                            if (fieldType.equals(Integer.TYPE)) {
                                Integer fieldValue = Integer.class.cast(o);
                                if (fieldValue != null && fieldValue.compareTo(0) != 0) {
                                    sid.addField(fieldName, fieldValue);
                                }
                            } else if (fieldType.equals(Float.TYPE)) {
                                Float fieldValue = Float.class.cast(o);
                                if (fieldValue != null && fieldValue.compareTo(0f) != 0) {
                                    sid.addField(fieldName, fieldValue);
                                }
                            } else if (fieldType.equals(Double.TYPE)) {
                                Double fieldValue = Double.class.cast(o);
                                if (fieldValue != null && fieldValue.compareTo(0d) != 0) {
                                    sid.addField(fieldName, fieldValue);
                                }
                            } else if (fieldType.equals(Short.TYPE)) {
                                Short fieldValue = Short.class.cast(o);
                                if (fieldValue != null && fieldValue.compareTo((short)0) != 0) {
                                    sid.addField(fieldName, fieldValue);
                                }
                            } else if (fieldType.equals(Long.TYPE)) {
                                Long fieldValue = Long.class.cast(o);
                                if (fieldValue != null && fieldValue.compareTo((long)0) != 0) {
                                    sid.addField(fieldName, fieldValue);
                                }
                            } else if(fieldType.equals(List.class)){
                                List fieldValue = List.class.cast(o);
                                if(fieldValue != null){
                                    sid.addField(fieldName, fieldValue);
                                }
                            }else {
                                if (o != null) {
                                    sid.addField(fieldName, o.toString());
                                }
                            }
                        }
                    }
                
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }catch (NoSuchFieldException e) {
                	logger.error("请检查schema中的field是否不存在于PO类中！");
                    e.printStackTrace();
                }
            }
            return sid;
        }
        logger.warn("即将要转换的SolrDocument或者要更新的Object为null");
        return null;
    }
	
	/**
	* @method: testSearch
	* @Description: 高亮检索  TODO 高级检索
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
	public List<Map<String, String>> getAll(SeQueryFilter filter) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			//条件
			String conditions = filter.toSql();
			
			//当前页
			int currentPage = filter.getPage();
			//每页显示条目数量
			int rowsPerpage = filter.getRows();
			
			SolrQuery query = new SolrQuery();
			if (conditions == null || "".equals(conditions.trim())) {//普通检索
				query = new SolrQuery("*:*");//匹配所有文档：*:*
				
				query.setSort("publishDate", ORDER.desc);
				//获取总数量
				QueryResponse respCounts = server.query(query);
				SolrDocumentList sdlCounts = respCounts.getResults();
				if (null != sdlCounts) {
					filter.setTotal(sdlCounts.size());
				}
				
				query.setStart((currentPage-1)*rowsPerpage);
		        query.setRows(rowsPerpage);
				
				QueryResponse resp = server.query(query);
				SolrDocumentList sdl = resp.getResults();
				
				for(SolrDocument sd:sdl) {
					Map<String, String> mapRe = new HashMap<String, String>();
					mapRe.put("id", sd.getFieldValue("id").toString());
					mapRe.put("name", sd.getFieldValue("name").toString());
					mapRe.put("type", sd.getFieldValue("type").toString());
					mapRe.put("category", sd.getFieldValue("category").toString());
					mapRe.put("author", sd.getFieldValue("author").toString());
					mapRe.put("content", sd.getFieldValue("content").toString().length()>210?sd.getFieldValue("content").toString().substring(0, 200):sd.getFieldValue("content").toString());
					mapRe.put("reprePic", sd.getFieldValue("reprePic").toString());
					mapRe.put("scans", sd.getFieldValue("scans").toString());
					
					mapRe.put("downloads", sd.getFieldValue("downloads").toString());
					mapRe.put("createDate", sd.getFieldValue("createDate").toString());
					mapRe.put("publisher", sd.getFieldValue("publisher").toString());
					mapRe.put("publishDate", sd.getFieldValue("publishDate").toString());
					
					list.add(mapRe);
				}
			}else{//高亮检索
				query.set("q", conditions);//查询字符串，必须的。
				
				query.setSort("publishDate", ORDER.desc);
				//获取总数量
				QueryResponse respCounts = server.query(query);
				SolrDocumentList sdlCounts = respCounts.getResults();
				if (null != sdlCounts) {
					filter.setTotal(sdlCounts.size());
				}
				
				query.setStart((currentPage-1)*rowsPerpage);
		        query.setRows(rowsPerpage);
				//query.set("msg_title","dismax");	//加权：第一个参数是“域名”
				//query.set("qf", "date_time:[20081001 TO 20091031]");//filter query。使用Filter Query可以充分利用Filter Query Cache，提高检索性能。
				//fl	----field list。指定返回结果字段。以空格“ ”或逗号“,”分隔。
				//start	----用于分页定义结果起始记录数，默认为0
				//rows  ----用于分页定义结果每页返回记录数，默认为10。
				//sort  ----排序，格式:sort=<field name>+<desc|asc>[,<field name>+<desc|asc>]… 。
				//			示例：（inStock desc, price asc）表示先 “inStock” 降序, 再 “price” 升序，默认是相关性降序。
				//df	----默认的查询字段，一般默认指定。
				//q.op	----覆盖schema.xml的defaultOperator（有空格时用"AND"还是用"OR"操作逻辑），一般默认指定。必须大写
				//wt	----writer type。指定查询输出结构格式，默认为“xml”。在solrconfig.xml中定义了查询输出格式：xml、json、python、ruby、php、phps、custom。
				//qt	----query type，指定查询使用的Query Handler，默认为“standard”
				//indent----返回的结果是否缩进，默认关闭，用 indent=true|on 开启，一般调试json,php,phps,ruby输出才有必要用这个参数。
				
				query.set("defType","edismax");//加权
				query.set("qf","name^20.0 text^0.5 author^10.0 content^5.0");
				
				//指定高亮字段
				StringBuffer sb = new StringBuffer();
				if(conditions.contains("type")){
					sb.append(",type");
				}
				if(conditions.contains("category:")){
					sb.append(",category");
				}
				if(conditions.contains("name:") || conditions.contains("text:")){
					sb.append(",name");
				}
				if(conditions.contains("author:")){
					sb.append(",author");
				}
				if(conditions.contains("publisher:")){
					sb.append(",publisher");
				}
				/*if(conditions.contains("createDate:")){
					sb.append(",createDate");
				}
				if(conditions.contains("publishDate:")){
					sb.append(",publishDate");
				}*/
				
				query.setHighlight(true);//开启高亮功能  
					//.setHighlightSnippets(10);  
				query.addHighlightField("content".concat(sb.toString()));//高亮字段  :多个域可以用空格或者逗号分隔
				query.setHighlightSimplePre("<font color=red >");//渲染标签  
				query.setHighlightSimplePost("</font>");//渲染标签  
				query.setParam("f.content.hl.fragsize", "200");
				//query.setSort("name",ORDER.desc);
				
				QueryResponse qr=server.query(query);//执行查询  
				SolrDocumentList dlist=qr.getResults();  
				
//				System.out.println("总数："+dlist.getNumFound());
				//第一个Map的键是文档的ID，第二个Map的键是高亮显示的字段名    
				Map<String, Map<String, List<String>>> map = qr.getHighlighting();    
				for(int i=0;i<dlist.size();i++){  
					SolrDocument d=dlist.get(i);//获取每一个document  
					
					Map<String, String> mapRe = new HashMap<String, String>();
					mapRe.put("id", d.get("id").toString());
					
					if(map.get(d.get("id")).get("name") == null){
						mapRe.put("name", d.getFieldValue("name").toString());
					}else{
						String hlName = map.get(d.get("id")).get("name").toString();
						mapRe.put("name", hlName.substring(1, hlName.length()-1));
					}
					
					mapRe.put("type", d.getFieldValue("type").toString());
					if (null != d.getFieldValue("category")) {
						mapRe.put("category", d.getFieldValue("category").toString());
					}
					
					if(map.get(d.get("id")).get("author") == null){
						mapRe.put("author", d.getFieldValue("author").toString());
					}else{
						String hlAuthor = map.get(d.get("id")).get("author").toString();
						mapRe.put("author", hlAuthor.substring(1, hlAuthor.length()-1));
					}
					
					if(map.get(d.get("id")).get("content") == null){
						String hlContent = d.getFieldValue("content").toString().length()>210?d.getFieldValue("content").toString().substring(0, 200):d.getFieldValue("content").toString();
						mapRe.put("content", hlContent.substring(1, hlContent.length()-1));
					}else{
						String hlContent = map.get(d.get("id")).get("content").toString();
						mapRe.put("content", hlContent.substring(1, hlContent.length()-1));
					}
					
					mapRe.put("reprePic", d.getFieldValue("reprePic").toString());
					mapRe.put("scans", d.getFieldValue("scans").toString());
					
					mapRe.put("downloads", d.getFieldValue("downloads").toString());
					mapRe.put("createDate", d.getFieldValue("createDate").toString());
					mapRe.put("publisher", d.getFieldValue("publisher").toString());
					mapRe.put("publishDate", d.getFieldValue("publishDate").toString());
					
					list.add(mapRe);
				}    
			}
	        return list;
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	* @method: testFacet
	* @Description: 切面（分组） TODO 
	*
	*
	* @author: ChenYW
	* @date 2014-4-15 下午03:41:18
	*/
	public Map<String, List<String>> facet(SeQueryFilter filter) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		try {
			//条件
			String conditions = filter.toSql();
			
			SolrQuery query = new SolrQuery();
			if (conditions == null || "".equals(conditions.trim())) {
				query.set("q", "text:*");
			}else{
				query.set("q", conditions);
			}
			query.setIncludeScore(false);//是否按每组数量高低排序
			query.setFacet(true);//是否分组查询
			query.setRows(0);//设置返回结果条数，如果你时分组查询，你就设置为0
			
			//增加分组字段 
			query.addFacetField("type");
			query.addFacetField("category");
			query.addFacetField("author");
			query.addFacetField("createDate");
			query.addFacetField("publisher");
			query.addFacetField("publishDate");
			
			//query.addFacetQuery("type[0 TO 1]");
			QueryResponse rsp = server.query(query);
			//取出结果
			//List<FacetField> facetFields = rsp.getFacetFields(); 
			
			FacetField typeFacetField = rsp.getFacetField("type");
			if (typeFacetField != null) {
				List<Count> counts = null;
				 counts = typeFacetField.getValues();
				 if (counts != null) {
					 List<String> typelist = new ArrayList<String>();
					 for (Count count : counts) {
						 //System.out.println(count.getName()+" "+count.getCount());
						 if (count.getCount() != 0) {
							 typelist.add(count.getName()+"("+count.getCount()+")");
						 }
					 }
					 map.put("type", typelist);
				 }
			}
			
			FacetField cateFacetField = rsp.getFacetField("category");
			if (cateFacetField != null) {
				List<Count> counts = null;
				counts = cateFacetField.getValues();
				 if (counts != null) {
					 List<String> catelist = new ArrayList<String>();
					 for (Count count : counts) {
						 if (count.getCount() != 0) {
							 catelist.add(count.getName()+"("+count.getCount()+")");
						}
					 }
					 map.put("category", catelist);
				 }
			}
			FacetField authorFacetField = rsp.getFacetField("author");
			if (authorFacetField != null) {
				List<Count> counts = null;
				counts = authorFacetField.getValues();
				 if (counts != null) {
					 List<String> catelist = new ArrayList<String>();
					 for (Count count : counts) {
						 if (count.getCount() != 0) {
							 catelist.add(count.getName()+"("+count.getCount()+")");
						}
					 }
					 map.put("author", catelist);
				 }
			}
			FacetField createFacetField = rsp.getFacetField("createDate");
			if (createFacetField != null) {
				List<Count> counts = null;
				counts = createFacetField.getValues();
				 if (counts != null) {
					 List<String> facetlist = new ArrayList<String>();
					 for (Count count : counts) {
						 if (count.getCount() != 0) {
							 facetlist.add(count.getName()+"("+count.getCount()+")");
						}
					 }
					 map.put("createDate", facetlist);
				 }
			}
			FacetField publisherFacetField = rsp.getFacetField("publisher");
			if (publisherFacetField != null) {
				List<Count> counts = null;
				counts = publisherFacetField.getValues();
				 if (counts != null) {
					 List<String> facetlist = new ArrayList<String>();
					 for (Count count : counts) {
						 if (count.getCount() != 0) {
							 facetlist.add(count.getName()+"("+count.getCount()+")");
						}
					 }
					 map.put("publisher", facetlist);
				 }
			}
			FacetField publishDateFacetField = rsp.getFacetField("publishDate");
			if (publishDateFacetField != null) {
				List<Count> counts = null;
				counts = publishDateFacetField.getValues();
				 if (counts != null) {
					 List<String> facetlist = new ArrayList<String>();
					 for (Count count : counts) {
						 if (count.getCount() != 0) {
							 facetlist.add(count.getName()+"("+count.getCount()+")");
						}
					 }
					 map.put("publishDate", facetlist);
				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	* @method: 
	* @Description: pinyin 切面（分组） TODO
	*
	* @author: ChenYW
	* @date 2014-4-15 下午03:41:18
	*/
	public Map<String, List<String>> facetPinyin(SeQueryFilter filter) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		try {
			//条件
			String conditions = filter.toSql();
			
			SolrQuery query = new SolrQuery("");
			if (conditions == null || "".equals(conditions.trim())) {
				query.set("q", "pinyin:*");
			}else{
				query.set("q", conditions);
			}
			query.setIncludeScore(false);//是否按每组数量高低排序
			query.setFacet(true);//是否分组查询
			query.setRows(0);//设置返回结果条数，如果你时分组查询，你就设置为0
			//增加分组字段  
			query.addFacetField("type");
			query.addFacetField("category");
			query.addFacetField("author");
			query.addFacetField("createDate");
			query.addFacetField("publisher");
			query.addFacetField("publishDate");
			
			//query.addFacetQuery("type[0 TO 1]");
			QueryResponse rsp = server.query(query);
			//取出结果
			//List<FacetField> facetFields = rsp.getFacetFields(); 
			
			FacetField typeFacetField = rsp.getFacetField("type");
			if (typeFacetField != null) {
				List<Count> counts = null;
				 counts = typeFacetField.getValues();
				 if (counts != null) {
					 List<String> typelist = new ArrayList<String>();
					 for (Count count : counts) {
						 //System.out.println(count.getName()+" "+count.getCount());
						 if (count.getCount() != 0) {
							 typelist.add(count.getName()+"("+count.getCount()+")");
						 }
					 }
					 map.put("type", typelist);
				 }
			}
			FacetField cateFacetField = rsp.getFacetField("category");
			if (cateFacetField != null) {
				List<Count> counts = null;
				counts = cateFacetField.getValues();
				 if (counts != null) {
					 List<String> catelist = new ArrayList<String>();
					 for (Count count : counts) {
						 if (count.getCount() != 0) {
							 catelist.add(count.getName()+"("+count.getCount()+")");
						}
					 }
					 map.put("theme", catelist);
				 }
			}
			FacetField authorFacetField = rsp.getFacetField("author");
			if (authorFacetField != null) {
				List<Count> counts = null;
				counts = authorFacetField.getValues();
				 if (counts != null) {
					 List<String> authorlist = new ArrayList<String>();
					 for (Count count : counts) {
						 if (count.getCount() != 0) {
							 authorlist.add(count.getName()+"("+count.getCount()+")");
						}
					 }
					 map.put("author", authorlist);
				 }
			}
			FacetField createFacetField = rsp.getFacetField("createDate");
			if (createFacetField != null) {
				List<Count> counts = null;
				counts = createFacetField.getValues();
				 if (counts != null) {
					 List<String> facetlist = new ArrayList<String>();
					 for (Count count : counts) {
						 if (count.getCount() != 0) {
							 facetlist.add(count.getName()+"("+count.getCount()+")");
						}
					 }
					 map.put("createDate", facetlist);
				 }
			}
			FacetField publisherFacetField = rsp.getFacetField("publisher");
			if (publisherFacetField != null) {
				List<Count> counts = null;
				counts = publisherFacetField.getValues();
				 if (counts != null) {
					 List<String> facetlist = new ArrayList<String>();
					 for (Count count : counts) {
						 if (count.getCount() != 0) {
							 facetlist.add(count.getName()+"("+count.getCount()+")");
						}
					 }
					 map.put("publisher", facetlist);
				 }
			}
			FacetField publishFacetField = rsp.getFacetField("publishDate");
			if (publishFacetField != null) {
				List<Count> counts = null;
				counts = publishFacetField.getValues();
				 if (counts != null) {
					 List<String> facetlist = new ArrayList<String>();
					 for (Count count : counts) {
						 if (count.getCount() != 0) {
							 facetlist.add(count.getName()+"("+count.getCount()+")");
						}
					 }
					 map.put("publishDate", facetlist);
				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
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
	public List<String> suggest(String word) {
		List<String> wordList = new ArrayList<String>();
    	try {
			SolrQuery query = new SolrQuery();
			query.set("q", "text:" + word);//查询的词  
			query.set("qt", "/suggest");//请求到suggest中  
			query.set("spellcheck.count", "10");//返回数量  
			QueryResponse rsp = server.query(query);
			
			//上面取结果的代码   System.out.println("直接命中:"+rsp.getResults().size());
			SpellCheckResponse re = rsp.getSpellCheckResponse();//获取拼写检查的结果集  
			if (re != null) {
				//StringBuffer wordList1 = new StringBuffer();
				for (Suggestion s : re.getSuggestions()) {
					List<String> list = s.getAlternatives();//获取所有 的检索词  
					for (String spellWord : list) {
						wordList.add(spellWord);
					}
				}
				@SuppressWarnings("unused")
				String t = re.getFirstSuggestion(word);//获取第一个推荐词  
				//System.out.println("推荐词：" + t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return wordList;
    }
	
	
	
    /**
	* @method: testPhoneticize
	* @Description: 拼音检索 TODO
	*				??? 也会把不符合条件的检索出来
	*				(解决方法：把汉子和相应的拼音建立同义词)
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-16 下午01:44:57
	*/
    public List<Map<String, String>> phoneticize(String pinyin){
		try {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			
			SolrQuery query = new SolrQuery();  
			query.set("q", "pinyin:"+pinyin);//高亮查询字段  
	        
	        QueryResponse qr=server.query(query);//执行查询  
	        SolrDocumentList dlist=qr.getResults();  
	        
	        System.out.println("总数："+dlist.getNumFound());
	        Map<String, String> mapRe = null;
	        for(SolrDocument sd:dlist){  
	            mapRe = new HashMap<String, String>();
	            
	            mapRe.put("id", sd.getFieldValue("id").toString());
				mapRe.put("name", sd.getFieldValue("name").toString());
				mapRe.put("type", sd.getFieldValue("type").toString());
				mapRe.put("category", sd.getFieldValue("category").toString());
				mapRe.put("author", sd.getFieldValue("author").toString());
				mapRe.put("content", sd.getFieldValue("content").toString().length()>210?sd.getFieldValue("content").toString().substring(0, 200):sd.getFieldValue("content").toString());
				mapRe.put("reprePic", sd.getFieldValue("reprePic").toString());
				mapRe.put("scans", sd.getFieldValue("scans").toString());
	            
				mapRe.put("downloads", sd.getFieldValue("downloads").toString());
				mapRe.put("createDate", sd.getFieldValue("createDate").toString());
				mapRe.put("publisher", sd.getFieldValue("publisher").toString());
				mapRe.put("publishDate", sd.getFieldValue("publishDate").toString());
				
	            list.add(mapRe);
	        }
	        
	        return list;
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
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
	@SuppressWarnings("unused")
	public String similar(String id){
		try {
			SolrQuery  query = new SolrQuery();
			query.set("qt", "/mlt");
			query.set("mlt.fl","content");
			query.set("fl", "id,name");
			query.set("q", "id:"+id);//人生观
			query.setStart(0);
			query.setRows(5);
			QueryResponse  rsp = server.query(query);
			SolrDocumentList list = rsp.getResults();
			System.out.println("———————————————————相似的条数为："+list.size());
			for(int i=0;i<list.size(); i++ ){  
	            SolrDocument d=list.get(i);//获取每一个document  
	            return d.getFieldValue("name").toString();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	/**
    * @method: testSpellCheck
    * @Description: 拼写检查 
    * 
    * @author: ChenYW
    * @date 2014-4-15 下午06:14:56
    */
    public String spellCheck(String word){
        SolrQuery query = new SolrQuery();  
        query.set("defType","edismax");//加权
        query.set("qf","name^20.0");
        
        query.set("spellcheck", "true");  
        query.set("spellcheck.q", word);
        query.set("qt", "/spell");  
        query.set("spellcheck.build", "true");//遇到新的检查词，会自动添加到索引里面  
        query.set("spellcheck.count", 5);
           
        try {  
	        QueryResponse rsp = server.query(query);  
	        SpellCheckResponse re=rsp.getSpellCheckResponse();  
	        if (re != null) {  
	        	if(!re.isCorrectlySpelled()){
		            String t = re.getFirstSuggestion(word);//获取第一个推荐词  
					System.out.println("推荐词：" + t);
					return t;
	        	}                  
	        } 
        } catch (SolrServerException e) {  
            e.printStackTrace();  
        }  
        return null;
    }

    /**
     * @method: optimize
     * @Description: 优化Lucene 的索引文件以改进搜索性能。
     * 	索引完成后执行一下优化通常比较好。如果更新比较频繁，则应该在使用率较低的时候安排优化。
     * 	一个索引无需优化也可以正常地运行。优化是一个耗时较多的过程。
     *
     * @return void
     *
     * @author: ChenYW
     * @date 2014-4-21 上午09:50:18
     */
    public void optimize(){
    	try {
    		server.optimize();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    /**
	* @method: getAllClustering
	* @Description: 获取所有资源的聚类
	*
	* @return
	* @return List<Map<String,String>>
	* 			map:
	* 				name	-->类名
	* 				score	-->分值
	* 				docs	-->IDs
	*
	* @author: ChenYW
	* @date 2014-7-4 下午4:00:45
	*/
	public List<Map<String, String>> getAllClustering(){
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
 		
		String str = HttpClientUtils.getHttpsByUrl(URL+"/clustering?q=*%3A*&wt=xml&indent=true");
		
		InputStream in_withcode = StrInsFile.stringToInputStream(str); 
		File inputXml= new File("clustering.txt");
		StrInsFile.inputStreamToFile(in_withcode, inputXml);
		SAXReader saxReader = new SAXReader();   
		try {   
			Document document = saxReader.read(inputXml);   
			Element root=document.getRootElement();   
			for(Iterator i = root.elementIterator(); i.hasNext();){   
				Element first = (Element) i.next(); 
				if ("arr".equals(first.getName()) && first.attributeValue("name").equals("clusters")) {
					for(Iterator j = first.elementIterator(); j.hasNext();){   
						Element second=(Element) j.next();
						Map<String, String> map = new HashMap<String, String>();
						for(Iterator k = second.elementIterator(); k.hasNext();){ 
							
							Element third = (Element) k.next();
							if (third.attributeValue("name").equals("labels")) {//取clustering的名称
								map.put("name", third.element("str").getText());
							}
							if (third.attributeValue("name").equals("score")) {//取clustering的名称
								map.put("score", third.getText());
							}
							if (third.attributeValue("name").equals("docs")) {//取clustering的子doc的id
								String ids = "";
								StringBuffer sb = new StringBuffer();
								int length = 0;
								for(Iterator h = third.elementIterator(); h.hasNext();){
									Element fourth = (Element) h.next();
									sb.append(fourth.getText()).append(" ");
									length++;
								}
								if (!sb.toString().trim().equals("")) {
									ids = "("+sb.toString().substring(0, sb.length()-1)+")";
								}
								map.put("docs", ids);
								map.put("size", String.valueOf(length));
							}
						}
						list.add(map);
					}   
				}
			}
			return list;
		} catch (DocumentException e) { 
			e.printStackTrace();
		}
		return null;
	}
}