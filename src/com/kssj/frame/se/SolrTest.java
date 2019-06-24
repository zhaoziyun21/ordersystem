package com.kssj.frame.se;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
//import org.tiangong.dbUtil.DbUtilTest;

public class SolrTest {
	private final static String URL = "http://192.168.1.237:9000/solr";
	private HttpSolrServer server = null;
	
	@Before
	public void init() {
		try {
			server = new HttpSolrServer(URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDelete(){
		try {
			server.deleteByQuery("*.*");//这样就删除了所有文档索引 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	* @method: testIndex
	* @Description: 创建索引
	*
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-15 下午03:00:06
	*/
	@Test
	public void testIndex() {
		try {
			List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
			SolrInputDocument doc = null;
			
			//DbUtilTest dbUtil = new DbUtilTest();
			List<Map<String, String>> list = new ArrayList<Map<String,String>>();
			//list = dbUtil.query();
			
			for (Map<String, String> map: list) {
				doc = new SolrInputDocument();
				doc.addField("id", map.get("id"));
				doc.addField("name", map.get("name"));
				doc.addField("author", map.get("author"));
				doc.addField("region", map.get("region"));
				doc.addField("theme", map.get("theme"));
				doc.addField("create_time", map.get("create_time"));
				doc.addField("content", map.get("content"));
				
				docs.add(doc);
			}
			server.add(docs);
			server.commit();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	@Test
	public void testSimilar(){
		try {
			SolrQuery  query = new SolrQuery();
			query.set("qt", "/mlt");
			query.set("mlt.fl","content");
			query.set("fl", "id,name");
			query.set("q", "id:99");//人生观
			query.setStart(0);
			query.setRows(5);
			QueryResponse  rsp = server.query(query);
			SolrDocumentList list = rsp.getResults();
			System.out.println("———————————————————相似的条数为："+list.size());
			for(int i=0;i<list.size();i++){  
	            SolrDocument d=list.get(i);//获取每一个document  
	            System.out.println(d.getFieldValue("name"));
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	* @method: testClustering
	* @Description: 自动聚类
	*
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-25 上午10:36:53
	*/
	@Test
	public void testClustering(){
		try {
			SolrQuery  query = new SolrQuery();
			query.set("qt", "/clustering");
			query.set("q", "name:文档");
//			query.setStart(0);
//			query.setRows(20);
			QueryResponse  rsp = server.query(query);
			SolrDocumentList list = rsp.getResults();
			System.out.println("———————————————————聚类的条数为："+list.getNumFound());
			for(int i=0;i<list.size();i++){  
	            SolrDocument d=list.get(i);//获取每一个document  
	            System.out.println(d.getFieldValue("id")+"---"+d.getFieldValue("name"));
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* @method: testIndex
	* @Description: 评分--
	*				搜索关键字匹配某些字段的打分比其他的字段要高(qf^)
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-15 下午04:36:39
	*/
	@Test
	public void testScore() {
		//TODO 
	}
	
	/**
	* @method: testIndex
	* @Description: 加权
	*
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-15 下午04:36:56
	*/
	@Test
	public void testIndex11() {
		SolrQuery query = new SolrQuery();
		query.set("type","dismax");
	}
	
	/**
    * @method: testSpellCheck
    * @Description: 拼写检查 
    * 
    * @author: ChenYW
    * @date 2014-4-15 下午06:14:56
    */
	@Test
    public void testSpellCheck(){
		String word = "中华二女";
		
    	@SuppressWarnings("unused")
		List<String>  wordList=new ArrayList<String>();  
        SolrQuery query = new SolrQuery();  
        //默认是主索引  
        //query.set("q", "msg_content:"+word+"");
        query.set("defType","edismax");//加权
        query.set("qf","name^2000.0");
        
        query.set("spellcheck", "true");  
        query.set("spellcheck.q", word);
        query.set("qt", "/spell");  
        query.set("spellcheck.build", "true");//遇到新的检查词，会自动添加到索引里面  
        //query.set("spellcheck.dictionary", "file");//使用副索引，checkSpellFile里面的进行使用  
        //query.set("spellcheck.count", Integer.MAX_VALUE);
        query.set("spellcheck.count", 5);
           
        try {  
	        QueryResponse rsp = server.query(query);  
	        //上面取结果的代码  
	        SpellCheckResponse re=rsp.getSpellCheckResponse();  
	          
	        if (re != null) {  
//	        	String collation = re.getCollatedResult();
//	        	System.out.println(collation);
	        	if(!re.isCorrectlySpelled()){
//	        		List<String> wordList1 = new ArrayList<String>();
//		        	for(Suggestion s:re.getSuggestions()){
//			             List<String> list=s.getAlternatives();  
//			             for(String spellWord:list){  
//			                 System.out.println(spellWord);  
//			                // wordList.add(spellWord);  
//			             } 
//		        	}
		            String t = re.getFirstSuggestion(word);//获取第一个推荐词  
					System.out.println("推荐词：" + t);
	        	}                  
	        } 
        } catch (SolrServerException e) {  
            e.printStackTrace();  
        }  
    }
	
	/**
	* @method: testSearch
	* @Description: 高亮检索
	*
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-15 下午03:33:05
	*/
	@Test
	public void testSearch() {
		try {
			SolrQuery query = new SolrQuery();  
			query.set("q", "name:文档 ");//高亮查询字段  
	        query.setHighlight(true)//开启高亮功能  
	        	//.setHighlightSnippets(10);  
	        	.addHighlightField("name")//高亮字段  
	        	.setHighlightSimplePre("<font color=\"red\">")//渲染标签  
	        	.setHighlightSimplePost("</font>");//渲染标签  
	        
	        QueryResponse qr=server.query(query);//执行查询  
	        SolrDocumentList dlist=qr.getResults();  
	        
	        System.out.println("总数："+dlist.getNumFound());
	        //第一个Map的键是文档的ID，第二个Map的键是高亮显示的字段名    
	        Map<String, Map<String, List<String>>> map = qr.getHighlighting();    
	        for(int i=0;i<dlist.size();i++){  
	            SolrDocument d=dlist.get(i);//获取每一个document  
	            //sd.getFieldValue("msg_title")
	            System.out.println(d.get("id")+"---"+map.get(d.get("id")).get("name"));//打印高亮的内容  
	        }    
			
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	* @method: testFacet
	* @Description: 切面（分组）
	*
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-15 下午03:41:18
	*/
	@Test
	public void testFacet() {
		try {
			SolrQuery query = new SolrQuery();
			query.set("q", "msg_content:程序");
			query.setIncludeScore(false);//是否按每组数量高低排序
			query.setFacet(true);//是否分组查询
			query.setRows(0);//设置返回结果条数，如果你时分组查询，你就设置为0
			query.addFacetField("type");//增加分组字段   q
			//query.addFacetQuery("type[0 TO 1]");
			QueryResponse rsp = server.query(query);
//			//取出结果
//			List<FacetField> facetFields = rsp.getFacetFields(); 
//			System.out.println(facetFields);
//			
//			List<FacetField.Count> list = rsp.getFacetField("type").getValues();
//			Map<String, Integer> list1 = rsp.getFacetQuery();
			
			FacetField facetField = rsp.getFacetField("type");
			List<Count> counts = null;
			if (facetField != null) {
				 counts = facetField.getValues();
				 if (counts != null) {
					 for (Count count : counts) {
						 System.out.println(count.getName()+" "+count.getCount());
					 }
				 }
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

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
    @Test
	public void testSuggest() {
    	try {
			
			String word = "中";
			List<String> wordList = new ArrayList<String>();
			SolrQuery query = new SolrQuery();
			query.set("q", "msg_titile:" + word);//查询的词  
			query.set("qt", "/suggest");//请求到suggest中  
			query.set("spellcheck.count", "10");//返回数量  
			QueryResponse rsp = server.query(query);
			//  System.out.println("直接命中:"+rsp.getResults().size());                 
			//上面取结果的代码  
			SpellCheckResponse re = rsp.getSpellCheckResponse();//获取拼写检查的结果集  
			if (re != null) {
				for (Suggestion s : re.getSuggestions()) {
					List<String> list = s.getAlternatives();//获取所有 的检索词  
					for (String spellWord : list) {
						System.out.println(spellWord);
						wordList.add(spellWord);
					}
				}
				// List<Collation> list=re.getCollatedResults();//  
				String t = re.getFirstSuggestion(word);//获取第一个推荐词  
				System.out.println("推荐词：" + t);
				//       for(Collation c:list){  
				//             
				//           System.out.println("推荐词:"+c.getCollationQueryString());  
				//       }  

			}
		} catch (Exception e) {
			e.printStackTrace();
		}  
    }
	
	/**
	* @method: testPhoneticize
	* @Description: 拼音检索
	*
	* @return void
	*
	* @author: ChenYW
	* @date 2014-4-16 下午01:44:57
	*/
	@Test
    public void testPhoneticize(){
		try {
			SolrQuery query = new SolrQuery();  
			query.set("q", "pinyin:zhongguoren");//高亮查询字段  
	        //query.setHighlight(true)//开启高亮功能  
	        	//.setHighlightSnippets(10);
	        	//.addHighlightField("pinyin")//高亮字段  
	        	//.setHighlightSimplePre("<font color=\"red\">")//渲染标签  
	        	//.setHighlightSimplePost("</font>");//渲染标签  
	        
	        QueryResponse qr=server.query(query);//执行查询  
	        SolrDocumentList dlist=qr.getResults();  
	        
	        System.out.println("总数："+dlist.getNumFound());
	        for(int i=0;i<dlist.size();i++){  
	            SolrDocument d=dlist.get(i);//获取每一个document  
	            System.out.println(d.getFieldValue("msg_title"));
	            //System.out.println(d.get("id")+"#"+map.get(d.get("id")).get("msg_content"));//打印高亮的内容  
	        }    
			
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
}