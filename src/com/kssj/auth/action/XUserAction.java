package com.kssj.auth.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.junit.runner.Request;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kssj.Test;
import com.kssj.auth.model.XMenu;
import com.kssj.auth.model.XRole;
import com.kssj.auth.model.XRoleMenu;
import com.kssj.auth.model.XUser;
import com.kssj.auth.model.XUserRole;
import com.kssj.auth.service.XMenuService;
import com.kssj.auth.service.XRoleMenuService;
import com.kssj.auth.service.XRoleService;
import com.kssj.auth.service.XUserRoleService;
import com.kssj.auth.service.XUserService;
import com.kssj.base.datasource.DataSourceConst;
import com.kssj.base.datasource.DataSourceContextHolder;
import com.kssj.base.util.BeanUtil;
import com.kssj.base.util.Constants;
import com.kssj.base.util.DateUtil;
import com.kssj.base.util.DateUtils2;
import com.kssj.base.util.ListUtil;
import com.kssj.base.util.Md5Security;
import com.kssj.base.util.StringUtil;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.command.sql.SqlSpellerDbType;
import com.kssj.frame.web.action.BaseAction;
import com.kssj.order.service.XOrdersService;
import com.kssj.product.model.XProExamine;
import com.kssj.product.model.XReceiverInfo;
import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("serial")
public class XUserAction extends BaseAction {
	
	@Resource
	private XUserService xUserService;
	@Resource
	private XRoleService xRoleService;
	@Resource
	private XUserRoleService xUserRoleService;
	@Resource
	private XRoleMenuService xRoleMenuService;
	@Resource
	private XMenuService xMenuService;
	@Resource
	private XOrdersService xOrdersService;
	
	//用户
	private XUser xuser;
	//角色list
	private List<XRole> roleList;
	//角色id
	private String roleId;
	
	public String getUserRoleId() {
		return userRoleId;
	}


	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}

	//用户角色ID
	private String userRoleId;
	

	public XUser getXuser() {
		return xuser;
	}


	public void setXuser(XUser xuser) {
		this.xuser = xuser;
	}


	public List<XRole> getRoleList() {
		return roleList;
	}


	public void setRoleList(List<XRole> roleList) {
		this.roleList = roleList;
	}
	
	public String getRoleId() {
		return roleId;
	}


	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@SuppressWarnings("rawtypes")
	public void getUserList() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			String str = this.getRequest().getParameter("str");
			List<Map> userMap = xUserService.getXUser(getQf(),str);
			/*for (int i = 0; i < userMap.size(); i++) {
					Map map = userMap.get(i);
						for (Object key : map.keySet()) {
						Object	value=map.get(key);
				this.getRequest().setAttribute(key.toString(),value.toString());
						System.out.println(value);
						}
				}*/
			StringBuffer buff = new StringBuffer("{\"Total\":")
			.append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson=new Gson();
			jsonString=gson.toJson(userMap);
			buff.append(jsonString);
			if(userMap==null){
				buff.append("[]");
			}
			buff.append("}");
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try {
				out = this.getResponse().getWriter();
				out.write(buff.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(out!=null){
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("查询列表失败!");
		}
	}
	
	
	/**
	 * @method: view
	 * @Description: 餐饮用户详情
	 * @return
	 * @throws Exception
	 * @author : lig
	 * @date 2016-9-20 上午11:32:02
	 */
	public String view() throws Exception{
		try {			
			String userid = this.getRequest().getParameter("userid");
			XUser u = xUserService.get(userid);
			this.getRequest().setAttribute("xuser", u);
			List<Map> roleIdList = xUserService.getRoleId(userid);
			if(ListUtil.isNotEmpty(roleIdList)){
				this.roleId = roleIdList.get(0).get("role_id").toString();
			}
			this.roleList = xRoleService.getRole();
			XUserRole xur = xUserRoleService.getByUserId(u.getId());
			userRoleId = xur.getId();
			this.setForwardPage("/jsp/auth/xfooduser/xfooduser_view.jsp");
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("查询详情失败!");
		}
	}
	
	
	/**
	 * @method: toAdd
	 * @Description: 新增页
	 * @return
	 * @throws Exception
	 * @author : lig
	 * @date 2016-9-20 下午2:08:51
	 */
	public String toAdd() throws Exception{
		try {			
			
			this.roleList = xRoleService.getRole();
			
			this.setForwardPage("/jsp/auth/xfooduser/xfooduser_form.jsp");
			
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("角色查询详情失败!");
		}
	}
	
	
	
	/**
	 * @method: doAdd
	 * @Description: 执行保存
	 * @return
	 * @throws Exception
	 * @author : lig
	 * @date 2016-9-20 下午2:44:20
	 */
	public void doAdd() throws Exception{
		String result = "Y";
		String type = this.getRequest().getParameter("type");
		try {			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			XUser loginUser = (XUser)this.getRequest().getSession().getAttribute("loginUser");
//			xuser.setDelFlag("0");
			//添加或者修改用户
			XUser u;
			if(type.equals("update")){
				XUser xUserObj = xUserService.get(xuser.getId());
				xUserObj.setEmail(xuser.getEmail());
				xUserObj.setPassword(xuser.getPassword());
				xUserObj.setRealName(xuser.getRealName());
				xUserObj.setSendRegion(xuser.getSendRegion());
				xUserObj.setTel(xuser.getTel());
				xUserObj.setFoodCompanyName(xuser.getFoodCompanyName());
				xUserObj.setWhetherSendStatus(xuser.getWhetherSendStatus());
				xUserObj.setUpdTime(new Date());
				xUserObj.setUpdUser(loginUser.getUserName());//session中获取
				//BeanUtil.copyNotNullProperties(xUserObj, xuser);
				u = xUserService.addUser(xUserObj);
			}else{
				xuser.setType("2");
				xuser.setDelFlag("0");
				xuser.setInsUser(loginUser.getUserName());//从session中获取
				xuser.setInsTime(new Date());
				
				u = xUserService.addUser(xuser);
			}
			if(u == null){
				result = "N";
			}else{
				//在添加或者修改用户角色
				
				XUserRole userRole = new XUserRole();
				XUserRole ur ;
				userRole.setUserId(u.getId());
				userRole.setRoleId(roleId);
				
				if(userRoleId != null && type.equals("update")){
					XUserRole xUserRole = xUserRoleService.getByUserId(xuser.getId());
					userRole.setUpdTime(new Date());
					userRole.setUpdUser(loginUser.getUserName());//session中获取
					BeanUtil.copyNotNullProperties(xUserRole, userRole);
					ur =xUserRoleService.save(xUserRole);
				}else{
					userRole.setInsUser(loginUser.getUserName());	//从session获取
					userRole.setInsTime(new Date());
					ur = xUserRoleService.addUserRole(userRole);
				}
				if(ur == null){
					result = "N";
				}
			}
			try {
				out = this.getResponse().getWriter();
				out.write(result);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(out!=null){
					out.flush();
					out.close();
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("操作失败!");
		}
	}
	
	
	
	
	
	/**
	 * @method: del
	 * @Description: 启用禁用餐饮用户
	 * @throws Exception
	 * @author : lig
	 * @date 2016-9-20 下午5:05:35
	 */
	public void del() throws Exception{
		String result = "Y";
		String userid = this.getRequest().getParameter("userid");
		try {			
			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			//删除用户角色
			XUser xu = xUserService.get(userid);
			if("0".equals(xu.getDelFlag())){
				xu.setDelFlag("1");
				xUserService.save(xu);
			}else if("1".equals(xu.getDelFlag())){
				xu.setDelFlag("0");
				xUserService.save(xu);
			}
//			xUserService.delUser(userid);
			
			//删除用户
//			xUserRoleService.delUserRole(userid);
			
			try {
				out = this.getResponse().getWriter();
				out.write(result);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(out!=null){
					out.flush();
					out.close();
				}
			}
			
			
		} catch (Exception e) { 
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("角色查询详情失败!");
		}
	}
	
	
	/**
	 * @method: login
	 * @Description: 后台登录
	 * @return
	 * @throws Exception
	 * @author : lig
	 * @date 2016-9-23 下午2:51:07
	 */
	public void login() throws Exception{
		// 同步组织
//		Test.main(null);
		//单点登录标志
		String flag = this.getRequest().getParameter("flag");
		
		String result = "Y";
		try {	
			/**
			 * 登录校验
			 */
			if(flag != null && flag.equals("sso")){
				Cookie[] cookie = ServletActionContext.getRequest().getCookies();
		        if(cookie!=null) {
		            for(Cookie c : cookie) {
		            	if(c.getName().contains("ssocookie") && c.getValue().contains("sso")){
		            		xuser = new XUser();
		            		String[] nameArr = c.getName().split("_");
		            		xuser.setUserName(nameArr[0]);
		            		String[] valueArr = c.getValue().split("_");
		                	xuser.setPassword(valueArr[0]);
		                	
		                	//刪除之前的cookie
		    	            Cookie killCookie = new Cookie(nameArr[0]+"_ssocookie", null);
		    	            killCookie.setPath("/");
		    	            HttpServletResponse response = ServletActionContext.getResponse();
		    	            response.addCookie(killCookie);
		            	}
		                
		            }
		        }
			}
			
	        String passwordCookie = null;
			if(xuser != null){
				passwordCookie = xuser.getPassword();
				
				//mysql中用户信息
				XUser user = xUserService.getUserByUserName(xuser);
				String type = "";
				if(user!=null){
					type=user.getType();
				}else{
					result = "N";
				}
				if(Constants.STAFF_TYPE.equals(type)){
					String str = Md5Security.getMD5(xuser.getPassword());
					xuser.setPassword(str.toUpperCase());
					DataSourceContextHolder holder = new DataSourceContextHolder();
					holder.setDataSourceType(DataSourceConst.SQLSERVER);
					boolean b = xUserService.orderLoginVerification(xuser);
					holder.setDataSourceType(DataSourceConst.MYSQL);
					//sqlServer验证通过
					if(b){
						if(user == null){
							result = "N";
						}else{
							//获取用户角色
							XUserRole xu = xUserRoleService.getByUserId(user.getUserId());
							if(xu!=null){
								XRole role = xRoleService.get(xu.getRoleId());
								//获取角色权限
								List<XRoleMenu> xrmp = xRoleMenuService.getByRoleId(role.getId());
								//根据权限获取菜单
								List<XMenu> xList = xMenuService.getMenuByIds(xrmp);
								user.setXmenu(xList);
								this.getSession().setAttribute("xrmp", xrmp);
								this.getSession().setAttribute("loginUser", user);
							}else{
								result ="N";
							}
							
						}
					}else{
						result ="N";
					}
				}else if(Constants.FOOD_COMPANY_TYPE.equals(type)){
				    user = xUserService.loginVerification(xuser);
				    if(user == null){
						result = "N";
					}else{
						//获取用户角色
						XUserRole xu = xUserRoleService.getByUserId(user.getId());
						XRole role = xRoleService.get(xu.getRoleId());
						//获取角色权限
						List<XRoleMenu> xrmp = xRoleMenuService.getByRoleId(role.getId());
						
						//根据权限获取菜单
						List<XMenu> xList = xMenuService.getMenuByIds(xrmp);
						user.setXmenu(xList);
						this.getSession().setAttribute("xrmp", xrmp);
						this.getSession().setAttribute("loginUser", user);
					}
				}else if(Constants.ADMIN_TYPE.equals(type)){
				    user = xUserService.loginVerification(xuser);
				    if(user == null){
						result = "N";
					}else{
						//获取用户角色
						XUserRole xu = xUserRoleService.getByUserId(user.getId());
						XRole role = xRoleService.get(xu.getRoleId());
						//获取角色权限
						List<XRoleMenu> xrmp = xRoleMenuService.getByRoleId(role.getId());
						
						//根据权限获取菜单
						List<XMenu> xList = xMenuService.getMenuByIds(xrmp);
						user.setXmenu(xList);
						this.getSession().setAttribute("xrmp", xrmp);
						this.getSession().setAttribute("loginUser", user);
					}
				}
			}else{
				result = "Login";
			}
			
			
			/**
			 * 把cookie保存到客户端
			 */
			if(result.equals("Y") && xuser != null){
				//保存cookie
	            Cookie cookie2 = new Cookie(xuser.getUserName()+"_ssocookie",passwordCookie+"_sso");
	            //设置Cookie的有效期为1天
	            cookie2.setMaxAge(60 * 60 * 24);
	            cookie2.setPath("/");
	            HttpServletResponse response = ServletActionContext.getResponse();
	            response.addCookie(cookie2);//把cookie保存到客户端
			}
			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try {
				out = this.getResponse().getWriter();
				out.write(result);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(out!=null){
					out.flush();
					out.close();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("登录失败!");
		}
	}
	
	/**
	 * 
	 * @method: goIndex
	 * @Description: 跳转后台首页
	 * @return
	 * @author : zhaoziyun
	 * @date 2017-3-17 下午2:28:20
	 */
	public String goIndex(){
		Object o = this.getSession().getAttribute("loginUser");
		XUser user = (XUser) o;
		if(user!=null){
			this.setForwardPage("/index.jsp");
			return SUCCESS;
		}
		return ERROR;
	}
	
	
	/**
	 * 
	 * @method: orderLogin
	 * @Description: 订餐用户pc登录
	 * @throws Exception
	 * @author : zhaoziyun
	 * @date 2017-3-2 上午11:13:26
	 */
	public void orderLogin() throws Exception{
		//单点登录标志
		String flag = this.getRequest().getParameter("flag");

		String result = "Y";
		try {		
			/**
			 * 登录校验
			 */
			if(flag != null && flag.equals("sso")){
				Cookie[] cookie = ServletActionContext.getRequest().getCookies();
		        if(cookie!=null) {
		            for(Cookie c : cookie) {
		            	if(c.getName().contains("ssocookie") && c.getValue().contains("sso")){
		            		xuser = new XUser();
		            		String[] nameArr = c.getName().split("_");
		            		xuser.setUserName(nameArr[0]);
		            		String[] valueArr = c.getValue().split("_");
		                	xuser.setPassword(valueArr[0]);
		                	
		                	//刪除之前的cookie
		    	            Cookie killCookie = new Cookie(nameArr[0]+"_ssocookie", null);
		    	            killCookie.setPath("/");
		    	            HttpServletResponse response = ServletActionContext.getResponse();
		    	            response.addCookie(killCookie);
		            	}
		                
		            }
		        }
			}
			
			String passwordCookie = null;
			if(xuser != null){
				passwordCookie = xuser.getPassword();
				
				String str = Md5Security.getMD5(xuser.getPassword());
				xuser.setPassword(str.toUpperCase());
				DataSourceContextHolder holder = new DataSourceContextHolder();
				holder.setDataSourceType(DataSourceConst.SQLSERVER);
				boolean b = xUserService.orderLoginVerification(xuser);
				holder.setDataSourceType(DataSourceConst.MYSQL);
				if(!b){
					result = "N";
				}else{
					XUser u = xUserService.getUserByUserName(xuser);
					if(u==null){
						u = xUserService.addOaUser(xuser.getUserName());
					}else{
						  xUserService.updOaUser(u);
					}
					this.getSession().setAttribute("orderLoginUser", u);
					//设置cookie 记住密码
					Cookie userCookie = new Cookie("username-password", xuser.getUserName()+"-"+xuser.getPassword());
					//设置Cookie的有效期为1年
					userCookie.setMaxAge(60 * 60 * 24 * 365);
					userCookie.setPath("/");
					this.getResponse().addCookie(userCookie);
				}
			}else{
				result = "Login";
			}
			
			
			/**
			 * 把cookie保存到客户端
			 */
			if(result.equals("Y") && xuser != null){
				//保存cookie
	            Cookie cookie2 = new Cookie(xuser.getUserName()+"_ssocookie",passwordCookie+"_sso");
	            //设置Cookie的有效期为1天
	            cookie2.setMaxAge(60 * 60 * 24);
	            cookie2.setPath("/");
	            HttpServletResponse response = ServletActionContext.getResponse();
	            response.addCookie(cookie2);//把cookie保存到客户端
			}
			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try {
				out = this.getResponse().getWriter();
				out.write(result);
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(out!=null){
					out.flush();
					out.close();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("登录失败!");
		}
	}
	
	 /**
	  * 
	  * @method: orderLogout
	  * @Description: 订餐用户pc退出
	  * @return
	  * @throws Exception
	  * @author : zhaoziyun
	  * @date 2017-3-6 下午5:01:01
	  */
	public String orderLogout() throws Exception{
		try {		
			//清除session
			this.getSession().removeAttribute("orderLoginUser");
			//清除cookie
//		    Cookie cookie = new Cookie(Constants.USERNAME+"-"+Constants.PASSWORD, null);
//            cookie.setMaxAge(0);
//            cookie.setPath("/");
//            this.getResponse().addCookie(cookie);
			//跳转登录页
			this.setForwardPage("/orders/orderLoginSSO.jsp");
			
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询详情失败!");
		}
	}
	
	public void test() throws Exception{
		MultiPartRequestWrapper multipartRequest = (MultiPartRequestWrapper) this.getRequest();
		File[] fs  = multipartRequest.getFiles("Filedata");
		 InputStream in = null;
		 OutputStream out = null;
//		 String fileName = multipartRequest.getFileNames("Filedata")[0];
		 try {
			 String path = ServletActionContext.getServletContext().getRealPath("upload");
			 String url="upload/"+System.currentTimeMillis();
			 path +="/"+System.currentTimeMillis();
			  
			 File newFile = new File(path);
			 in = new BufferedInputStream(new FileInputStream(fs[0]));
			 out = new BufferedOutputStream(new FileOutputStream(newFile));
			 byte[] buffer = new byte[1024];
			 while(in.read(buffer)>0){
				 out.write(buffer);
			 }
			 this.getResponse().setContentType("text/json; charset=utf-8");
			 PrintWriter o = this.getResponse().getWriter();
			 o.println(url);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null!=in){
				in.close();
			}
			if(null!=out){
				out.close();
			}
		}
	}
	
	
	/**
	 * @method: logout
	 * @Description: 退出
	 * @return
	 * @throws Exception
	 * @author : lig
	 * @date 2016-10-16 下午5:36:28
	 */
	public String logout() throws Exception{
		try {		
			//清除session
			this.getSession().removeAttribute("loginUser");
			//跳转登录页
			this.setForwardPage("/loginSSO.jsp");
			
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询详情失败!");
		}
	}
	
	
	
	/**
	 * @method: getOrderUserList
	 * @Description: 离职
	 * @throws Exception
	 * @author : lig
	 * @date 2017-3-1 下午6:20:12
	 */
	public void dimission() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			String id = this.getRequest().getParameter("id");
			System.out.println("");
			XUser user = xUserService.getByUserId(id);
			XUser loginUser = (XUser)this.getRequest().getSession().getAttribute("loginUser");
			PrintWriter out = null;
			Long w = null;
			try {
				this.getResponse().setContentType("text/json; charset=utf-8");
				out = this.getResponse().getWriter();
				if (user != null) {
					user.setDelFlag("1");
					user.setUpdUser(loginUser.getRealName());
					user.setUpdTime(new Date());
					w = xUserService.updDelFlag(user);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if (w != null) {
					out.write(w.toString());
				} else {
					out.write("0");
				}
				if(out!=null){
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("查询列表失败!");
		}
	}
	/**
	 * @method: getOrderUserList
	 * @Description: 订餐用户列表
	 * @throws Exception
	 * @author : lig
	 * @date 2017-3-1 下午6:20:12
	 */
	public void getOrderUserList() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			String companyId = this.getRequest().getParameter("companyId");
			String userName = this.getRequest().getParameter("userName");
			//如果前台没传子公司id，就查询当前登录用户所在总公司下的
			if(StringUtil.isEmpty(companyId)){
				companyId = this.getSysUserBySession().getCompanyId();
			}
			String deptId = this.getRequest().getParameter("deptId");
			List<Map> userMap = xUserService.getOrderXUser(getQf(),companyId,deptId,userName);
			StringBuffer buff = new StringBuffer("{\"Total\":")
			.append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson=new Gson();
			jsonString=gson.toJson(userMap);
			buff.append(jsonString);
			if(userMap==null){
				buff.append("[]");
			}
			buff.append("}");
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try {
				out = this.getResponse().getWriter();
				out.write(buff.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(out!=null){
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("查询列表失败!");
		}
	}
	
	
	/**
	 * @method: toAddRole
	 * @Description: to添加角色page
	 * @return
	 * @throws Exception
	 * @author : lig
	 * @date 2017-3-2 上午10:49:36
	 */
	public String toAddRole() throws Exception{
		try {			
			String userid = this.getRequest().getParameter("userid");
			XUser u = xUserService.getByUserId(userid);
			List<XUserRole> rlist = xUserRoleService.getRoleByUserId(userid);
			this.getRequest().setAttribute("u", u);
			this.getRequest().setAttribute("rlist", rlist);
			List<Map> roleIdList = xUserService.getRoleId(userid);
			if(ListUtil.isNotEmpty(roleIdList)){
				this.roleId = roleIdList.get(0).get("role_id").toString();
			}
			
			//当前登录用户角色列表
			List<XUserRole> currentUserRoleList = xUserRoleService.getRoleByUserId(this.getSysUserBySession().getUserId());
			
			//去掉餐饮公司角色，如果当前登录用户不是超级管理员就不显示超级管理员角色
			List<XRole> listOld = xRoleService.getRole();
			List<XRole> listNew = new ArrayList<XRole>();
			if(ListUtil.isNotEmpty(listOld)){
				for (XRole xRole : listOld) {
					if(!Constants.FOOD_USER_ROLE.equals(xRole.getId()) && !Constants.ADMIN_ROLE.equals(xRole.getId())){ //餐饮公司角色
						listNew.add(xRole);
					}
					if(ListUtil.isNotEmpty(currentUserRoleList)){
						for (XUserRole xur : currentUserRoleList) {
							if(Constants.ADMIN_ROLE.equals(xRole.getId())){
								//是否是超级管理员
								if(Constants.ADMIN_ROLE.equals(xur.getRoleId()) ){
									listNew.add(xRole);
								}
							}
						}
					}
					
				}
				
			}
			
			
			this.roleList = listNew;
			
			
			
			this.setForwardPage("/jsp/auth/xuser/xuser_addrole.jsp");
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("查询详情失败!");
		}
	}
	
	
	/**
	 * @method: doAddRole
	 * @Description: 执行添加
	 * @throws Exception
	 * @author : lig
	 * @date 2017-3-2 上午11:28:27
	 */
	public void doAddRole() throws Exception{
		String result = "Y";
		XUser sysUser = this.getSysUserBySession();
		String userid = this.getRequest().getParameter("userId");//用户id
		String roleids = this.getRequest().getParameter("ids");//角色id
		try {			
			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			
			//删除用户角色
			xUserRoleService.delUserRole(userid);
			
			//重新添加用户角色
			XUserRole xUserRole = new  XUserRole();
			if(StringUtil.isNotEmpty(roleids)){
				String [] ids = roleids.split(",");
				Date d = new Date();
				if(ids.length > 0){
					for (int i = 0; i < ids.length; i++) {
						xUserRole.setId(null);
						xUserRole.setUserId(userid);
						xUserRole.setRoleId(ids[i]);
						xUserRole.setInsTime(d);
						xUserRole.setInsUser(sysUser.getUserName());
						
						//=================删除原有角色 start ==============================
						if(ids[i].equals(Constants.DEPT_ONLY_ID)){//如果有部门专用角色
							Map map = xUserService.getDeptRoleByUserIdAndRoleId(userid, Constants.DEPT_ONLY_ID);
							if(map!=null){//如果部门已经有专用角色，删除该角色
								xUserService.delRoleByUserIdAndRoleId(map.get("user_id").toString(), Constants.DEPT_ONLY_ID);
							}
						}
						if(ids[i].equals(Constants.DEPT_CONTACT_ID)){//如果有部门联系人角色
							Map map = xUserService.getDeptRoleByUserIdAndRoleId(userid, Constants.DEPT_CONTACT_ID);
							if(map!=null){//如果部门已经有部门联系人角色，删除该角色
								xUserService.delRoleByUserIdAndRoleId(map.get("user_id").toString(), Constants.DEPT_CONTACT_ID);
							}
						}
						//=================删除原有角色 end==============================
						xUserRoleService.addUserRole(xUserRole);
					}
				}
			}
			try {
				out = this.getResponse().getWriter();
				out.write(result);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(out!=null){
					out.flush();
					out.close();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("角色查询详情失败!");
		}
	}
	
	/**
	 * @method: toUserList
	 * @Description:to订餐用户管理
	 * @return
	 * @throws Exception
	 * @author : lig
	 * @date 2017-3-9 下午4:35:16
	 */
	public String toUserList() throws Exception{
		try {		
//			XUser user = getSysUserBySession();
//			List<Map> deptList = xOrdersService.getTreeData(user.getCompanyId(), false,"");
//			JSONArray jsonArray = JSONArray.fromObject(deptList);
//			this.getRequest().setAttribute("deptList", jsonArray.toString());
			this.setForwardPage("/jsp/auth/xuser/xuser_list.jsp");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("角色查询详情失败!");
		}
	}
	/**
	 * 
	 * @method: checkUserName
	 * @Description: 验证菜单名唯一
	 * @author : zhaoziyun
	 * @date 2017-3-8 下午2:54:59
	 */
	 public void checkUserName(){
		 String result ="Y";
		 String username =this.getRequest().getParameter("userName");
		 XUser user = new XUser();
		 user.setUserName(username);
		 XUser xuser= xUserService.getUserByUserName(user);
		 if(xuser!=null){
			 result =  "N";
		 }else{
			 result="Y";
		 }
		 ajaxJson(result);
	 }
	 /**
	  * 
	  * @method: toEditRoomNum
	  * @Description: 跳转到部门房间号列表
	  * @author : zhaoziyun
	 * @throws Exception 
	  * @date 2017-3-8 下午2:54:59
	  */
	 public String toEditRoomNum() throws Exception{
		 try {		
			 this.setForwardPage("/jsp/auth/xuser/xdept_roomnum_list.jsp");
				return SUCCESS;
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new Exception("部门查询失败!");
			}
		
	 }
	 /**
	  * 
	  * @method: findDept
	  * @Description: 部门房间号列表
	  * @author : zhaoziyun
	 * @throws Exception 
	  * @date 2017-3-8 下午2:54:59
	  */
	 public void findDept() throws Exception{
		 try {		
			 setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			 String companyId = this.getRequest().getParameter("companyId");
			 String deptId = this.getRequest().getParameter("deptId");
			 if("".equals(companyId)&&"".equals(deptId)){
				 companyId="1";
			 }
			 List<Map> deptList = xUserService.findDept(getQf(),companyId,deptId);
			 StringBuffer buff = new StringBuffer("{\"Total\":")
				.append(getQf().getTotal()).append(",\"Rows\":");
				Gson gson=new Gson();
				jsonString=gson.toJson(deptList);
				buff.append(jsonString);
				if(deptList==null){
					buff.append("[]");
				}
				buff.append("}");
				PrintWriter out = null;
				this.getResponse().setContentType("text/json; charset=utf-8");
				try {
					out = this.getResponse().getWriter();
					out.write(buff.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(out!=null){
						out.flush();
						out.close();
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new Exception("部门查询失败!");
			}
		
	 }
	 /**
	  * 
	  * @method: editRoomNum
	  * @Description: 编辑部门房间号
	  * @author : zhaoziyun
	  * @date 2017-3-8 下午2:54:59
	  */
	 public void editRoomNum(){
		 String result ="Y";
		 String roomNum =this.getRequest().getParameter("roomNum");
		 String id =this.getRequest().getParameter("id");
		 Long i= xUserService.editRoomNum(id,roomNum);
		 if(i==0){
			 result =  "N";
		 }else{
			 result="Y";
		 }
		 ajaxJson(result);
	 }
	 
	 /**
	 * 获取用户输入金额
	 */
	public void getuus(){
		try {
			String dianpu=this.getRequest().getParameter("dianpu");//餐厅编号
			String jine =this.getRequest().getParameter("jine");//输入的金额
			HttpSession session = this.getSession();
			String userid=(String) session.getAttribute("");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	      
	}
	
	/**
	 * 设置默认收餐地址
	 */
	public void setUpAddressDefault(){
		try {
			String result = "true";
			
			//获取收餐地址id
			String addressId = this.getRequest().getParameter("addressId");
			XUser xuser = (XUser)this.getRequest().getSession().getAttribute("orderLoginUser");
			xuser.setSendDefaultAddress(addressId);
			xUserService.save(xuser);
		
			PrintWriter out = null;  
			this.getResponse().setContentType("text/json; charset=utf-8");
			out = this.getResponse().getWriter();
			out.write(result);
			if(out != null){
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置默认收餐地址--微信端
	 */
	public void setUpAddressDefaultWx(){
		try {
			String result = "true";
			
			//获取收餐地址id
			String addressId = this.getRequest().getParameter("addressId");
			XUser xuser = (XUser)this.getRequest().getSession().getAttribute("wxOrderLoginUser");
			xuser.setSendDefaultAddress(addressId);
			xUserService.save(xuser);
		
			PrintWriter out = null;  
			this.getResponse().setContentType("text/json; charset=utf-8");
			out = this.getResponse().getWriter();
			out.write(result);
			if(out != null){
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
