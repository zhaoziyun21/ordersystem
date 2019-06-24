<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>公告列表</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script>
		var dialog;
		var grid = null;
		function initGrids(){
			grid = $(".table_box").ligerGrid({
				url: '${root}order/getAllNoticesXNotice.do',
				pageSize:10,
				toolbar:{items: [	{ text: '新增公告', click: add , icon:'add'},
	              					{ line:true }
	              				]
	              		},
				columns: [
					{ display: '公告名', name: 'notice_name',width:'15%' },
					{ display: '公告描述', name: 'notice_desc',width:'70%'},
					{ display: '操作', name: 'opt',width:'15%',render: function(row){
						var html = "";
						if(row.id == null){
							return null;
						}
						html += "<div onclick=\"edit('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>查看</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
						html += "<div onclick=\"del('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>删除</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-delete\"></div></div></div>";
						return html;
					}}
					]
			});
		}
		
	
		$(document).ready(function(){
			initGrids();
		});
		
		//添加
		function add(){
				dialog = $.ligerDialog.open({ 
                                height: 400,
                                url: '${root}jsp/order/xfood/notice_form.jsp',
                                width: 500,
                                name:'view',
                                title:'新增公告',
                                isResize:true	
                                 });  
		}
		//删除
		function del(id){
			dialog = $.ligerDialog.confirm('确定删除该公告吗？', function (yes)
                      {
                      	if(yes){
                          $.ajax({
                          	url:'${root}order/delNoticeXNotice.do',
                          	type:'post',
                          	data:{'noticeId':id},
                          	dataType:'html',
                          	async:false,
                          	success:function(data){
                          		if(data == "Y"){
                          			$.ligerDialog.success("删除成功");
                          			initGrids();
                          		}else{
                          			$.ligerDialog.error("删除失败");
                          		}
                          	}
                          });
                      	}
                      });
		}
		
		//查看
		function edit(id){
			dialog = $.ligerDialog.open({ 
	                     height: 400,
	                     url: '${root}order/toEditNoticeXNotice.do?noticeId='+id,
	                     width: 500,
	                     name:'view',
	                     title:'公告操作',
	                     isResize:true	
                     }); 
		}
		
	</script>
</head>
<body>
	<form id="searchBox">
		<div class="table_box"></div>
	</form>
	
</body>
</html>
