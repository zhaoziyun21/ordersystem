package com.kssj.auth.action;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import com.google.gson.Gson;
import com.kssj.auth.model.XMenu;
import com.kssj.auth.model.XRole;
import com.kssj.auth.model.XRoleMenu;
import com.kssj.auth.model.XUser;
import com.kssj.auth.model.XUserRole;
import com.kssj.auth.service.XMenuService;
import com.kssj.auth.service.XRoleMenuService;
import com.kssj.auth.service.XRoleService;
import com.kssj.auth.service.XUserRoleService;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.command.sql.SqlSpellerDbType;
import com.kssj.frame.web.action.BaseAction;

@SuppressWarnings("serial")
public class XMenuAction extends BaseAction {

	@Resource
	private XMenuService xMenuService;
	@Resource
	private XUserRoleService xUserRoleService;
	@Resource
	private XRoleService xRoleService;
	@Resource
	private XRoleMenuService xRoleMenuService;
	
	/**
	 * @method: getAllMenu
	 * @Description: 所有菜单
	 * @throws Exception
	 * @author : lig
	 * @date 2016-9-21 上午10:35:17
	 */
	public void getAllMenu() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			List<XMenu> menuList = xMenuService.getAll();
			StringBuffer buff = new StringBuffer("{\"Total\":")
			.append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson=new Gson();
			jsonString=gson.toJson(menuList);
			buff.append(jsonString);
			if(menuList==null){
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
	 * @method: getAllMenu2
	 * @Description: 菜单树
	 * @throws Exception
	 * @author : lig
	 * @date 2016-12-1 下午6:08:11
	 */
	public void getAllMenu2() throws Exception{
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.MYSQL));
			List<XMenu> menuList = xMenuService.getAll();
			JSONArray json = JSONArray.fromObject(menuList);
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try {
				out = this.getResponse().getWriter();
				out.write(json.toString());
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
	 * @method: getAllMenu3
	 * @Description: 获取用户所拥有的菜单
	 * @throws Exception
	 * @author : lig
	 * @date 2016-12-2 下午2:52:35
	 */
	public void getAllMenu3() throws Exception{
		try {		
			List<XMenu> menuList = null;
			if(getSession().getAttribute("loginUser") != null){
				XUser u =  (XUser)getSession().getAttribute("loginUser");
				menuList = u.getXmenu();
			}
			
			JSONArray json = JSONArray.fromObject(menuList);
			PrintWriter out = null;
			this.getResponse().setContentType("text/json; charset=utf-8");
			try {
				out = this.getResponse().getWriter();
				out.write(json.toString());
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
	 * @method: getMenuByRoleId
	 * @Description: 获取角色权限
	 * @throws Exception
	 * @author : lig
	 * @date 2016-9-22 下午3:46:23
	 */
	@SuppressWarnings("rawtypes")
	public void getMenuByRoleId() throws Exception{
			String roleId = this.getRequest().getParameter("roleid");
		try {			
			setQf(new SqlQueryFilter(this.getRequest(),SqlSpellerDbType.ORACLE));
			
			List<Map> menuMap = xMenuService.getMenuByRoleId(roleId);
//			List<Map> menuMap2 = new ArrayList<Map>();
			//[{MENU_NAME=警用专题查询, ID=1, MENU_ID=1, PERMISSION_VALUES=查询,, PARENT_ID=0}
			//去重
//			List<Map> menuids = xMenuService.getMenuByRoleId2(roleId);
			
//			for (Map map : menuMap2) {
//				for (Map m : menuids) {
//					if(map.get("MENU_ID") != null){
//						if(map.get("MENU_ID").equals(m.get("MENU_ID"))){
//							menuMap.add(map);
//						}
//					}
//				}
//				
//			}
			
			StringBuffer buff = new StringBuffer("{\"Total\":")
			.append(getQf().getTotal()).append(",\"Rows\":");
			Gson gson=new Gson();
			jsonString=gson.toJson(menuMap);
			buff.append(jsonString);
			if(menuMap==null){
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
	 * @method: updateMenu
	 * @Description: 修改菜单名称
	 * @throws Exception
	 * @author : lig
	 * @date 2016-12-6 上午10:22:44
	 */
	public void updateMenu() throws Exception{
		String result = "Y";
		try {		
			String id = getRequest().getParameter("id");
			String menuName = getRequest().getParameter("menuName");
			
			XMenu m = xMenuService.get(id);
			m.setMenuName(menuName);
			XMenu xm = xMenuService.save(m);
			if(xm == null){
				result= "N";
			}else{
				//重新加载当前用户menuList
				
				
				XUser user = (XUser)getSession().getAttribute("loginUser");
				
				//获取用户角色
				XUserRole xu = xUserRoleService.getByUserId(user.getId());
				XRole role = xRoleService.get(xu.getRoleId());
				//获取角色权限
				List<XRoleMenu> xrmp = xRoleMenuService.getByRoleId(role.getId());
				//根据权限获取菜单
				List<XMenu> xList = xMenuService.getMenuByIds(xrmp);
				user.setXmenu(xList);
				getSession().setAttribute("loginUser", user);
				
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
			throw new Exception("修改失败!");
		}
	}
	
	
}
