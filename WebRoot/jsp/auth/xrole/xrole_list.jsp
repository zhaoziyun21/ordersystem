<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>角色管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script>
		var grid1=null;
		 
		function initGrids(){
			grid1 = $(".table_box").ligerGrid({
				url: '${root}xrole/getAllRoleListXRole.do',
				pageSize:10,
// 				height:'98%',	
				toolbar:{items: [	{ text: '增加', click: add , icon:'add'},
	              					{ line:true }
	              				]
	              		},
				columns: [
					{ display: '角色名称', name: 'roleName' },
					{ display: '角色描述', name: 'roleDesc' },
					{ display: '操作', name: 'opt', render: function(row){
						var html = "";
						if(row.id == null){
							return null;
						}
						html += "<div onclick=\"view1('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>查看</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
						if(row.id != "1" && row.id != "2" && row.id != "3" && row.id != "4" && row.id != "5"){
							html += "<div onclick=\"del('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>删除</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-delete\"></div></div></div>";						
						}
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
			initGrids();
		});
		
		//添加
		function add(){
				$.ligerDialog.open({ 
                                height: 400,
                                url: '${root}jsp/auth/xrole/xrole_form.jsp',
                                width: 600,
                                name:'view',
                                title:'新增角色',
                                isResize:true	
                                 });  
		}
		//删除
		function del(id){
			$.ligerDialog.confirm('确定删除该角色吗？', function (yes)
                      {
                      	if(yes){
	                          $.ajax({
	                          	url:'${root}xrole/delXRole.do',
	                          	type:'post',
	                          	data:{'roleid':id},
	                          	dataType:'html',
	                          	async:false,
	                          	success:function(data){
	                          		if(data == "Y"){
	                          			$.ligerDialog.success("删除成功");
	                          			initGrids();//刷新列表
	                          		}else{
	                          			$.ligerDialog.error("删除失败");
	                          		}
	                          	}
	                          });
                      	}
                      });
		}
		//查看
		function view1(id){
			$.ligerDialog.open({ 
                                height: 400,
                                url: '${root}xrole/viewXRole.do?roleid='+id,
                                width: 600,
                                name:'view',
                                title:'',
                                isResize:true
                               }); 
		}
		
	</script>
  </head>
  
  <body>
  <form id="searchBox">
    <div class="table_box" >
   </div>
</div>
</form>
  </body>
</html>
