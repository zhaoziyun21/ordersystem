package com.kssj.frame.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
* @Description: 用于为某个URL加上相应的文件头，以便输出至浏览器可以识别，如缓存图片,CSS等
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-15 上午11:53:54
* @version V1.0
*/
@SuppressWarnings("rawtypes")
public class ResponseHeaderFilter implements Filter {   
    private FilterConfig fc;    
  
    
	public void doFilter(ServletRequest req, ServletResponse res,   
            FilterChain chain) throws IOException, ServletException {   
        HttpServletResponse response = (HttpServletResponse) res;   
        // set the provided HTTP response parameters   
        for (Enumeration e = fc.getInitParameterNames(); e.hasMoreElements();) {   
            String headerName = (String) e.nextElement();   
            response.addHeader(headerName, fc.getInitParameter(headerName));   
        }   
       
        chain.doFilter(req, response);   
    }    
  
    public void init(FilterConfig filterConfig) {   
        this.fc = filterConfig;   
    }    
  
    public void destroy() {   
        this.fc = null;   
    }    
  
}