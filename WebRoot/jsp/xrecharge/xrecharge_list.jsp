<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>用户列表</title>
    
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
				url: '${root}xuser/getUserListXUser.do',
				parms:[{name: 'Q|u.type|S|EQ', value:'1'}],
				pageSize:10,
// 				height:'99%',	
				columns: [
					{ display: '姓名', name: 'real_name' },
					{ display: '邮箱', name: 'email' },
					{ display: '电话', name: 'tel' },
					{ display: '角色', name: 'role_name' },
					{ display: '操作', name: 'opt', render: function(row){
						var html = "";
						if(row.id == null){
							return null;
						}
						html += "<div onclick=\"view1('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>查看</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
						html += "<div onclick=\"del('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>删除</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-delete\"></div></div></div>";
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
		
		 
		//查看
		function view1(id){
			$.ligerDialog.open({ 
                                height: 400,
                                url: '${root}xuser/viewXUser.do?userid='+id,
                                width: 600,
                                name:'view',
                                title:'用户详情',
                                isResize:true
                               });  
		}
		
	</script>
  </head>
  
  <body>
  <form id="searchBox"> 
    <div class="table_box" >
   </div>
</form>
  </body>
</html>
