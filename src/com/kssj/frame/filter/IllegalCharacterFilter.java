package com.kssj.frame.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
* @Description: Illegal Character
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-16 上午11:19:33
* @version V1.0
*/
public class IllegalCharacterFilter implements Filter {
	private static Logger logger = Logger.getLogger(IllegalCharacterFilter.class); 
	
	public void destroy() {

	}

	@SuppressWarnings("rawtypes")
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
			
		HttpServletRequest httpRequest=(HttpServletRequest) req;
		HttpServletResponse httpResponse=(HttpServletResponse)res;
		
			req.setCharacterEncoding("utf-8");
			res.setCharacterEncoding("utf-8");
			String[] strBadChar = {
					"insert"
				    ,"select"
				    ,"delete"
				    ,"update"
				    ,"drop"
				    ,"declare"
				    //,"*"
				    //,"%"
				    //,"'"
				    };
			
			Map pamamap=req.getParameterMap();
			Object[] obj = pamamap.keySet().toArray();
			boolean state = true;
			
			if(!pamamap.isEmpty()){
				 for(int i=0;i<obj.length;i++){
						String content=req.getParameter(obj[i].toString());
						if(content != null){
							for(String str : strBadChar){
								
								if(content.toUpperCase().indexOf(str.toUpperCase()) != -1){
									state = false;
									logger.info("过滤器：IllegalCharacterFilter.....");
									logger.info("参数[" + content + "]含有特殊字符[" + str +"]");
									httpRequest.setAttribute("err", "params[" + content + "]Contain special characters[" + str +"]");
									break;
								}
							}
						}
						
						if(!state){
							break;
						}
						
				}
			}
			
			if(state){
				chain.doFilter(req, res);
			}else{
				String loginDir="/illegalErr.jsp";
				httpRequest.getRequestDispatcher(loginDir).forward(httpRequest, httpResponse);
				
			}
       
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

	

}
