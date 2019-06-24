<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>菜谱列表</title>
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
				url: '${root}order/getAllFoodsXFood.do',
				pageSize:10,
// 				rownumbers:true,
				toolbar:{items: [	{ text: '新增套餐', click: add , icon:'add'},
	              					{ line:true }
	              				]
	              		},
				columns: [
// 					{ display: 'id', name: 'ID',hide:true },
// 					{ display: '序号', render:function(row,index){
// 						return index+1;
// 					} },
					{ display: '套餐名', name: 'food_name',width:'20%' },
					{ display: '描述', name: 'food_desc',width:'30%'},
					{ display: '单价', name: 'sell_price' },
					{ display: '套餐类型', name: 'food_type' },
					{ display: '操作', name: 'opt',width:'20%',render: function(row){
						var html = "";
						if(row.id == null){
							return null;
						}
						html += "<div onclick=\"edit('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>编辑</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
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
                                url: '${root}jsp/order/xfood/xfood_form.jsp',
                                width: 600,
                                name:'view',
                                title:'新增菜谱',
                                isResize:true	
                                 });  
		}
		//删除
		function del(id){
			dialog = $.ligerDialog.confirm('确定删除该菜谱吗？', function (yes)
                      {
                      	if(yes){
                          $.ajax({
                          	url:'${root}order/delFoodXFood.do',
                          	type:'post',
                          	data:{'foodid':id},
                          	dataType:'html',
                          	async:false,
                          	success:function(data){
                          		if(data == "Y"){
                          			$.ligerDialog.success("删除成功");
// 									alert("删除成功！");
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
                                url: '${root}order/toEditFoodXFood.do?foodid='+id,
                                width: 600,
                                name:'view',
                                title:'菜谱编辑',
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
