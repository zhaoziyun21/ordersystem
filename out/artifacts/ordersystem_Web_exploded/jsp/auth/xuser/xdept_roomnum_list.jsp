<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>部门房间号管理</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script>
		var dialog;
		var grid = null;
		var flag;
		
		//条件
		//公司id
		//部门：部门id
		//用户：部门id、模糊查询字段
		var deptId = ""; 
		var str = "";
		var companyId = "";
		
		function initGrids(){
			flag = $("#searchBox").find("input[name='chargeObj']").val();
			deptId = $("#dept").val();
			companyId = $("#company").val();
			if(flag != null){
				//部门
				if(flag == 1){
						grid = $(".table_box").ligerGrid({
								url: '${root}xuser/findDeptXUser.do',
								parms:[{name:"companyId",value:companyId},{name:"deptId",value:deptId}],
								pageSize:10,
								columns: [
									{ display: '公司',width:'30%', name: 'company_name' },
									{ display: '部门',width:'15%', name: 'name' },
									{ display: '房间号',width:'35%', name: 'room_num' },
									{ display: '操作',width:'20%', name: 'opt', render: function(row){
										var html = "";
										if(row.id == null){
											return null;
										}
										html += "<div onclick=\"edit('"+row.id+"','"+row.flag+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>编辑房间号</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
										return html;
									}}
									]
							});
				
				}
			}
		
		}
	
		$(document).ready(function(){
		
		//需要checkbox ，父部门下的所有的部门
		 var combo2 = $("#txt2").ligerComboBox({
                width: 180,
                selectBoxWidth: 200,
                selectBoxHeight: 200, 
                valueField : 'id',
                textField: 'name',
                treeLeafOnly:false,
                tree: { 
//                 data:${deptList},
				url:'${root}/order/getTreeDataXOrders.do',
                checkbox: false, 
                ajaxType: 'get', 
                 textFieldName: 'name',
                idFieldName: 'id',
                 parentIDFieldName: 'parent_id'
                 },
                 onSelected: function (newvalue)
                 {
                 	if(newvalue.indexOf("dept") != -1){
                 		$("#dept").val(newvalue.split("_")[1]);
                 		$("#company").val("");
                 	}else{
                 		$("#company").val(newvalue);
                 		$("#dept").val("");
                 	}
                  }
            });
            initGrids();
			$("#query").click(function(){
				initGrids();
			});
		});
		
		
		//编辑
		function edit(id,flag){
				dialog = $.ligerDialog.open({ 
                                height: 250,
                                url: '${root}jsp/auth/xuser/xdept_roomnum_form.jsp?id='+id,
                                width: 500,
                                name:'view',
                                title:'编辑',
                                isResize:true	
                                 });  
		}
		 
</script>
<style type="text/css">
	.org {
		display:inline-block;
		position:relative;
		top:7px;
	}
	.table_box_p {
		margin-top:4px;
	}
</style>
</head>
<body>
	<form id="searchBox">
		<input type="hidden"  name="chargeObj"   value="1" />
		<span>组织机构：</span>
		<div class="org">
		  <input type="text" id="txt2"/>
		</div>
		  <input type="hidden" id="dept"/>
		  <input type="hidden" id="company"/>
		<input type="button" id="query" value="搜索"/>
		<div class="table_box_p"><div class="table_box"></div></div>
	</form>
</body>
</html>
