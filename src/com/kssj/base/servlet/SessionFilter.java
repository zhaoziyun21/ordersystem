package com.kssj.base.servlet;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kssj.auth.model.XUser;
import com.kssj.auth.service.XUserService;
import com.kssj.base.datasource.DataSourceConst;
import com.kssj.base.datasource.DataSourceContextHolder;
import com.kssj.base.util.Constants;
import com.kssj.frame.exception.IllegalUrlException;
import com.kssj.frame.exception.SessionTimeException;

/**
* @Description: Seesion out filter
* Company:数码易知
* @author ChenYW
* 
* @date 2013-3-23 下午03:10:23
* @version V1.0
*/
public class SessionFilter implements Filter {
	private static Logger logger = Logger.getLogger(SessionFilter.class);
	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException, SessionTimeException, IllegalUrlException {
		try {
			HttpServletResponse httpResponse = (HttpServletResponse)response;
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			HttpSession session = httpRequest.getSession();
			Object manager = session.getAttribute("loginUser");
			Object orderLoginUser = session.getAttribute("orderLoginUser");
			Object wxOrderLoginUser = session.getAttribute("wxOrderLoginUser");
			
			if(manager!=null){
				logger.info("url:"+httpRequest.getRequestURL().toString());
				chain.doFilter(httpRequest, httpResponse);
			}else if(httpRequest.getRequestURL().toString().contains("/orderLogin.jsp")){
				Cookie cookies[] = httpRequest.getCookies();
				String username="";
				String password="";
				String str = "";
				if(cookies!=null){
					 for(int i = 0; i < cookies.length; i++){
		                    if ((Constants.USERNAME+'-'+Constants.PASSWORD).equals(cookies[i].getName())) {
		                           str = cookies[i].getValue();
		                           cookies[i].setMaxAge(60 * 60 * 24 * 365);
		                           break;
		                    }
		             }
					String[] s =  str.split("-");
					if(s.length>1){
						username = s[0];
						password = s[1];
						//验证cookie用户信息
						XUser xuser = new XUser();
						xuser.setUserName(username);
						xuser.setPassword(password);
						ClassPathXmlApplicationContext context = null;
						String[] contextLocations = new String[] { "classpath:spring/app-context.xml"};
						context = new ClassPathXmlApplicationContext(
								contextLocations);
						DataSourceContextHolder holder = new DataSourceContextHolder();
						holder.setDataSourceType(DataSourceConst.SQLSERVER);
						XUserService xUserService = (XUserService)context.getBean("xUserService");
						boolean b = xUserService.orderLoginVerification(xuser);
						holder.setDataSourceType(DataSourceConst.MYSQL);
						//验证通过，调用黎刚的显示套餐方法
						if(b){
							XUser u = xUserService.getUserByUserName(xuser);
							if(u==null){
								u = xUserService.addOaUser(xuser.getUserName());
							}else{
								  xUserService.updOaUser(u);
							}
							session.setAttribute("orderLoginUser", u);
							String baseDir="http://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + httpRequest.getContextPath();
							String loginDir=baseDir + "/order/toOrderingPageXOrders.do";
							System.out.println(loginDir);
							httpResponse.sendRedirect(loginDir);
						}else{
							logger.info(httpRequest.getRequestURL().toString());
							chain.doFilter(httpRequest, httpResponse);
						}
					}else{
						logger.info(httpRequest.getRequestURL().toString());
						chain.doFilter(httpRequest, httpResponse);
					}
				}else{
					logger.info(httpRequest.getRequestURL().toString());
					chain.doFilter(httpRequest, httpResponse);
				}
			}else if(orderLoginUser!=null){
				logger.info("url:"+httpRequest.getRequestURL().toString());
				chain.doFilter(httpRequest, httpResponse);
			}else if(httpRequest.getRequestURL().toString().contains("/wx_orderLogin.jsp")){
				Cookie cookies[] = httpRequest.getCookies();
				String username="";
				String password="";
				String str = "";
				if(cookies!=null){
					 for(int i = 0; i < cookies.length; i++){
		                    if ((Constants.USERNAME+'-'+Constants.PASSWORD).equals(cookies[i].getName())) {
		                           str = cookies[i].getValue();
		                           cookies[i].setMaxAge(60 * 60 * 24 * 365);
		                           break;
		                    }
		             }
					String[] s =  str.split("-");
					if(s.length>1){
						username = s[0];
						password = s[1];
						//验证cookie用户信息
						XUser xuser = new XUser();
						xuser.setUserName(username);
						xuser.setPassword(password);
						ClassPathXmlApplicationContext context = null;
						String[] contextLocations = new String[] { "classpath:spring/app-context.xml"};
						context = new ClassPathXmlApplicationContext(
								contextLocations);
						DataSourceContextHolder holder = new DataSourceContextHolder();
						holder.setDataSourceType(DataSourceConst.SQLSERVER);
						XUserService xUserService = (XUserService)context.getBean("xUserService");
						boolean b = xUserService.orderLoginVerification(xuser);
						holder.setDataSourceType(DataSourceConst.MYSQL);
						//验证通过，调用黎刚的显示套餐方法
						if(b){
							XUser u = xUserService.getUserByUserName(xuser);
							if(u==null){
								u = xUserService.addOaUser(xuser.getUserName());
							}else{
								  xUserService.updOaUser(u);
							}
							session.setAttribute("wxOrderLoginUser", u);
							String baseDir="http://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + httpRequest.getContextPath();
							String loginDir=baseDir + "/wechat/toIndexWeChat.do";
							System.out.println(loginDir);
							httpResponse.sendRedirect(loginDir);
						}else{
							logger.info(httpRequest.getRequestURL().toString());
							chain.doFilter(httpRequest, httpResponse);
						}
					}else{
						logger.info(httpRequest.getRequestURL().toString());
						chain.doFilter(httpRequest, httpResponse);
					}
				}else{
					logger.info(httpRequest.getRequestURL().toString());
					chain.doFilter(httpRequest, httpResponse);
				}
			}else if(wxOrderLoginUser!=null){
				
				logger.info("url:"+httpRequest.getRequestURL().toString());
				chain.doFilter(httpRequest, httpResponse);
			}else if(httpRequest.getRequestURL().toString().contains("/public/")
					||httpRequest.getRequestURL().toString().contains("/login.jsp")
					||httpRequest.getRequestURL().toString().contains("/loginXUser.do")
					||httpRequest.getRequestURL().toString().contains("/logoutXUser.do")
					||httpRequest.getRequestURL().toString().contains("/orderLoginWeChat.do")
					||httpRequest.getRequestURL().toString().contains("/orderLogoutWeChat.do")
					||httpRequest.getRequestURL().toString().contains("/orderLoginXUser.do")
					||httpRequest.getRequestURL().toString().contains("/orderLogoutXUser.do")
//					||httpRequest.getRequestURL().toString().contains("/wechat")
					||httpRequest.getRequestURL().toString().contains(".jsp")
					|| httpRequest.getRequestURL().toString().endsWith("/")
					){
					logger.info(httpRequest.getRequestURL().toString());
					chain.doFilter(httpRequest, httpResponse);
			}else if(httpRequest.getRequestURL().toString().contains("/order/")
					||httpRequest.getRequestURL().toString().contains("/orders/")){
				String baseDir="http://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + httpRequest.getContextPath();
				String loginDir=baseDir + "/orders/orderLogin.jsp";
				httpResponse.sendRedirect(loginDir);
			}else if(httpRequest.getRequestURL().toString().contains("WeChat.do")){
				String baseDir="http://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + httpRequest.getContextPath();
				String loginDir=baseDir + "/wx_orders/wx_orderLogin.jsp";
				httpResponse.sendRedirect(loginDir);
			}else{
				logger.info("用户不在Session中，session已过期");										
				String baseDir="http://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + httpRequest.getContextPath();
				String loginDir=baseDir + "/common/public/SessionTimeMessage.jsp";
//				String loginDir=baseDir + "/common/public/SessionTimeToIndex.jsp";
				System.out.println(loginDir);
				httpResponse.sendRedirect(loginDir);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}

}

