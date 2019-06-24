package com.kssj.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @Description: 去除文本里的HTML标签
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-6-27 下午1:48:59
* @version V1.0
*/
public class RmHtmlUtil {

	/**
	* @method: ClearHtml
	* @Description: 第一种去除html的方法
	*
	* @param Content
	* @return
	* @return String
	*
	* @author: ChenYW
	* @date 2014-6-27 下午1:48:43
	*/
	public static String ClearHtml(String Content){
        Content = Zxj_ReplaceHtml("&#[^>]*;", "", Content);
        Content = Zxj_ReplaceHtml("</?marquee[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?object[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?param[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?embed[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?table[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("&nbsp;", "", Content);
        Content = Zxj_ReplaceHtml("&hellip;", "", Content);//&hellip;
        Content = Zxj_ReplaceHtml("&mdash;", "", Content);//&mdash;
        Content = Zxj_ReplaceHtml("&ldquo;", "", Content);//&ldquo;
        Content = Zxj_ReplaceHtml("&rdquo;", "", Content);//&rdquo;
        Content = Zxj_ReplaceHtml("</?tr[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?th[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?p[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?a[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?img[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?tbody[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?li[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?span[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?div[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?th[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?td[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?script[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("(javascript|jscript|vbscript|vbs):", "", Content);
        Content = Zxj_ReplaceHtml("on(mouse|exit|error|click|key)", "", Content);
        Content = Zxj_ReplaceHtml("<\\?xml[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("<\\/?[a-z]+:[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?font[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?b[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?u[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?i[^>]*>", "", Content);
        Content = Zxj_ReplaceHtml("</?strong[^>]*>", "", Content);
        String clearHtml = Content;
        return clearHtml;
    }
	public static String Zxj_ReplaceHtml(String patrn, String strRep, String content){
         if (StringUtil.isEmpty(content)){
             content = "";
         }
         //Regex rgEx = new Regex(patrn, RegexOptions.IgnoreCase);
         //String strTxt = rgEx.Replace(content, strRep);
         
         Pattern rgEx = Pattern.compile(patrn, Pattern.CASE_INSENSITIVE);        
         Matcher StrTxt = rgEx.matcher(content);
         content = StrTxt.replaceAll(""); 
         
         return content;
     }

	
	/**
	* @method: HtmlText
	* @Description: 第二种去除html方法
	*
	* @param inputString
	* @return
	* @return String
	*
	* @author: ChenYW
	* @date 2014-6-27 下午1:43:32
	*/
	public static String HtmlText(String inputString) { 	      
		String htmlStr = inputString; //含html标签的字符串 	      
		String textStr =""; 	      
		java.util.regex.Pattern p_script; 	      
		java.util.regex.Matcher m_script; 	      
		java.util.regex.Pattern p_style; 	      
		java.util.regex.Matcher m_style; 	      
		java.util.regex.Pattern p_html; 	      
		java.util.regex.Matcher m_html; 	      
		try { 	       
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> } 	       
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> } 	          
			String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式 	      	          
			
			p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 	          
			m_script = p_script.matcher(htmlStr); 	          
			htmlStr = m_script.replaceAll(""); //过滤script标签 		          
			p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 	          
			m_style = p_style.matcher(htmlStr); 	          
			htmlStr = m_style.replaceAll(""); //过滤style标签 	      	          
			p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 	          
			m_html = p_html.matcher(htmlStr); 	          
			htmlStr = m_html.replaceAll(""); //过滤html标签 	          	          
			/* 空格 ——   */	         
			// p_html = Pattern.compile("\\ ", Pattern.CASE_INSENSITIVE);	          
			m_html = p_html.matcher(htmlStr);	          
			htmlStr = htmlStr.replaceAll(" "," ");
			textStr = htmlStr; 	      	      
		}catch(Exception e) { 	      
			e.printStackTrace();
		} 	      
		return textStr;  
	}
	
}
