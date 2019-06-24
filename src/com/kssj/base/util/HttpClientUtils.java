package com.kssj.base.util;

import java.io.IOException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
* @Description: 获取html内容
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-3-12 上午10:57:17
* @version V1.0
*/
public class HttpClientUtils {
	
	public static String getHttpsByUrl(String url){
		
	     HttpClient httpClient = new HttpClient();//构造HttpClient的实例
	     
	     httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);//设置 Http 连接超时为5秒
	     
	     //String url = "http://192.168.1.237:9000/solr/clustering?q=name%3A*&wt=html&indent=true&encoding=utf-8";
	     GetMethod getMethod = new GetMethod(url);//创建GET方法的实例
	     //getMethod.getParams().setContentCharset("GB2312");
	     
	     getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,5000);//设置 get 请求超时为 5 秒
	     
	     getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());//使用系统提供的默认的恢复策略
	     try{
		      //执行getMethod
		      int statusCode = httpClient.executeMethod(getMethod);
		      if (statusCode != HttpStatus.SC_OK){
		    	  System.err.println("Method failed: "+ getMethod.getStatusLine());
		      }
		      
		      //读取内容 ,第二种方式获取
		      String newStr = null;
		      String os = System.getProperty("os.name");
		      System.out.println(os);
		      if (os != null && os.startsWith("Windows")) { 
		    	  newStr = new String(getMethod.getResponseBodyAsString().getBytes(),"GB2312");
		      }else {
		    	  newStr = new String(getMethod.getResponseBodyAsString().getBytes(),"UTF-8");
		      }
		      //System.out.println(newStr);
		      return new String(newStr);
		      
		      //读取内容 ,第一种方式获取
		      //byte[] responseBody = getMethod.getResponseBody();
		      //System.out.println(new String(responseBody));//处理内容
		      
	     }catch(HttpException e){
	    	 //发生致命的异常，可能是协议不对或者返回的内容有问题
	    	 System.out.println("Please check your provided http address!");
	    	 e.printStackTrace();
	     }catch(IOException e){
	    	 //发生网络异常
	    	 e.printStackTrace();
	     }finally{
	    	 //释放连接
	    	 getMethod.releaseConnection();
	     }
		return null;
	}
}
