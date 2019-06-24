package com.kssj.auth.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.google.gson.Gson;
import com.kssj.auth.model.XRole;
import com.kssj.auth.model.XRoleMenu;
import com.kssj.auth.model.XUser;
import com.kssj.auth.service.XRoleMenuService;
import com.kssj.auth.service.XRoleService;
import com.kssj.auth.service.XUserRoleService;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.command.sql.SqlSpellerDbType;
import com.kssj.frame.web.action.BaseAction;

@SuppressWarnings("serial")
public class XRoleAction extends BaseAction {

	@Resource
	private XRoleService xRoleService; 
	@Resource
	private XRoleMenuService xRoleMenuPermissionService;
	@Resource
	private XUserRoleService xUserRoleService;
	
	private XUser u = (XUser)this.getSession().getAttribute("loginUser");
	
	//角色
	private XRole xrole;
	
	//所选权限
	private String mper;
	
	
	public String getMper() {
		return mper;
	}


	public void setMper(String mper) {
		this.mper = mper;
	}


	public XRole getXrole() {
		return xrole;
	}


	public void setXrole(XRole xrole) {
		this.xrole = xrole;
	}


	/**
	 * @method: getAllRoleList
	 * @Description: 角色列表
	 * @throws Exception
	 * @author : lig
	 * @date 2016-9-20 下午5:47:16
	 */
	public void getAllRoleList() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			List<XRole> roleList = xRoleService.getRole();
			StringBuffer buff = new StringBuffer("{\"Total\":")
			.append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson=new Gson();
			jsonString=gson.toJson(roleList);
			buff.append(jsonString);
			if(roleList==null){
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
			throw new Exception("查询列表失败!");
		}
	}
	
	
	
	/**
	 * @method: doAdd
	 * @Description: 添加角色 或者修改
	 * @throws Exception
	 * @author : lig
	 * @date 2016-9-21 上午11:37:19
	 */
	public void doAdd() throws Exception{
		String result = "Y";
		
		//获取标识
		String type = this.getRequest().getParameter("type");
		
		try {			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			/**
			 *  如有id自动获取id，即修改
			 */
//			xrole.setDelFlag("0");
			if(!type.equals("update")){
//				xrole.setInsUser(u.getLoginName());	//从session中获取
				xrole.setInsTime(new Date());
			}else{
				xrole.setUpdTime(new Date());
//				xrole.setUpdUser(u.getLoginName());	//session中获取
			}
			//先添加角色
			XRole r	= xRoleService.addRole(xrole);
			if(r == null){
				result = "N";
			}else{
				
				//角色id
				String roleId = r.getId();
				/**
				 * 如果是编辑
				 * 1、先删除x_role_menu_permission表中该角色权限
				 * 2、再向其表中添加该角色权限
				 */
				
				//删除
				if(type.equals("update")){
					xRoleMenuPermissionService.delPermissionByRoleId(roleId);
				}
				
				//添加角色功能权限
				XRoleMenu xrmp;
				String mpers [] = null;
				if(mper != null && mper != ""){
					mpers  = mper.split(",");
					for (int i = 0; i < mpers.length; i++) {
						xrmp = new XRoleMenu();
						xrmp.setRoleId(roleId);				//角色ID
						xrmp.setMenuId(mpers[i]);				//功能菜单ID
//						xrmp.setPermissionValues(perms);	//权限字符串用逗号隔开
						XRoleMenu xrmps = xRoleMenuPermissionService.save(xrmp);
						if(xrmps == null){
							result = "N";
							break;
						}
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
			throw new Exception("操作失败!");
		}
	
}
	
	/**
	 * @method: view
	 * @Description: 查看
	 * @return
	 * @throws Exception
	 * @author : lig
	 * @date 2016-9-22 下午2:53:04
	 */
	public String view() throws Exception{
		try {			
			String roleid = this.getRequest().getParameter("roleid");
			XRole r = xRoleService.get(roleid);
			this.getRequest().setAttribute("xrole", r);
			List<Map> rmpList = null;
			String menustr = "";
			if(r != null){
				rmpList = xRoleMenuPermissionService.getByRoleId2(r.getId());
			}
			if(rmpList != null){
				for (int i = 0; i < rmpList.size() ;i ++) {
					menustr += rmpList.get(i).get("menu_Id").toString() + ",";
				}
			}
			this.getRequest().setAttribute("menustr", menustr);
			this.setForwardPage("/jsp/auth/xrole/xrole_view.jsp");
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("查询详情失败!");
		}
	}
	
	
	/**
	 * @method: del
	 * @Description: 删除角色
	 * @throws Exception
	 * @author : lig
	 * @date 2016-9-23 上午11:08:31
	 */
	public void del() throws Exception{
		String result;
		String roleid = this.getRequest().getParameter("roleid");
		try {			
			
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			
			boolean b = xRoleService.delRole(roleid);
			if(b){
				  result = "Y";
			}else{
				  result = "N";
			}
			out = this.getResponse().getWriter();
			out.write(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new Exception("角色删除失败!");
		}
	}
	
}