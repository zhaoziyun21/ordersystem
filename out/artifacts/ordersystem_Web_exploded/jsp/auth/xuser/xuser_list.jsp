<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<style>
	.org {
		display:inline-block;
		position:relative;
		top:7px;
	}
	.table_box_p {
		margin-top:5px;
	}
	.table_box {
		margin-top:6px;
	}
	</style>
  <head>
    <base href="<%=basePath%>">
    
    <title>订餐用户管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script>
		var grid1 = null;
		var tree1 = null;
		var dialog;
		function initGrids(){
			grid1 = $(".table_box").ligerGrid({
				url: '${root}xuser/getOrderUserListXUser.do',
				parms:[{name: 'companyId', value:$("#company").val()},{name: 'deptId', value:$("#dept").val()},{name: 'userName', value:$("#user_id").val()}],
				pageSize:10,
// 				height:'99%',	
				columns: [
					{ display: '部门', name: 'dept_name' },
					{ display: '姓名', name: 'real_name' },
// 					{ display: '职务', name: 'jobtitle' },
					{ display: '余额', name: 'balance' },
					{ display: '角色', name: 'role_name' },
					{ display: '操作', name: 'opt', render: function(row){
						var html = "";
						if(row.id == null){
							return null;
						}
						html += "<div onclick=\"addRole('"+row.user_id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>角色赋予</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-delete\"></div></div></div>";
						html += "<div onclick=\"dimission('"+row.user_id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>离职</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-delete\"></div></div></div>";
						return html;
					}}
					]
			});
		}
		
	
		$(document).ready(function(){
			  var h = $(window).height(), h2;
		      $(".auditstaff_content").css("height", h);
		      $(window).resize(function() {
		          h2 = $(this).height();
		          $(".auditstaff_content").css("height", h2);
		      });
		      loadTree();
			  initGrids();
			
		});
		
		 
		//查看
		function addRole(id){
			dialog = $.ligerDialog.open({
                                height: 400,
                                url: '${root}xuser/toAddRoleXUser.do?userid='+id,
                                width: 300,
                                name:'view',
                                title:'添加角色',
                                isResize:true
                               });  
		}
		
		// 离职
		function dimission(id){
			$.ligerDialog.confirm('确定员工已经离职吗？', function (yes) {
                      	if(yes){
	                          $.ajax({
		                          	url:'${root}xuser/dimissionXUser.do',
		                          	type:'post',
		                          	data:{'id':id},
		                          	dataType:'html',
		                          	async:false,
		                          	success:function(data){
		                          		if(data == "1"){
		                          			$.ligerDialog.success("离职成功");
		                          			initGrids();//刷新列表
		                          		}else{
		                          			$.ligerDialog.error("离职失败");
		                          		}
		                          	}
	                          });
                      	}
           });
		}
		
		
		
		
		
		
		
		
		//部门tree-----------------------------------------------------
		function loadTree(){
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
                 	
                 	//initGrids();
                 	
                  }
            });
		
		}
		function search(){
			initGrids();
		}
	</script>
  </head>
  <body  >
 	
  <input type="hidden" id="dept"/>
  <input type="hidden" id="company"/>
  <form id="searchBox"> 
  		<span>组织机构：</span>
		<div class="org">
		  <input type="text" id="txt2"/>
		</div>
  	 姓名：<input type="text" id="user_id" value=""/>
  	 <input type="button" value="搜索" onclick="search();"/>
    <div class="table_box" >
   </div>
</form>
  </body>
</html>
