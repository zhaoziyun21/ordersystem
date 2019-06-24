<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>产品类别列表</title>
    
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
	var parmsData =[{name: 'Q|xpc.del_flag|S|EQ', value:'0'}];
	function initGrids(parmsData){
		grid1 = $(".table_box").ligerGrid({
			url: '${root}product/getProCateListXProCategory.do',
			parms:parmsData,
			pageSize:10,
			//height:'99%',	
			toolbar:{items: [	{ text: '增加', click: add , icon:'add'},
              					{ line:true }
              				]
              		},
			columns: [
				{ display: '产品类别名称', width:'30%', name: 'pro_cate_name' },
				{ display: '描述', width:'40%', name: 'pro_cate_desc' },
				{ display: '启用状态', width:'15%', name: 'del_flag', render: function(row){
					if(row.del_flag==0){
					return '启用';
					}else if(row.del_flag==1){
					return '禁用';
					}
				} },
				{ display: '操作', width:'15%', name: 'opt', render: function(row){
					var html = "";
					if(row.id == null){
						return null;
					}
					html += "<div onclick=\"view1('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>编辑</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
					if(row.del_flag==0){
						html += "<div onclick=\"del('"+row.id+"','"+row.del_flag+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>停用</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-delete\"></div></div></div>";
					}else if(row.del_flag==1){
						html += "<div onclick=\"del('"+row.id+"','"+row.del_flag+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>启用</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-add\"></div></div></div>";
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
		initGrids(parmsData);
	});
		
	//添加
	function add(){
		$.ligerDialog.open({ 
            height: 350,
            url: '${root}product/toAddProCateXProCategory.do',
            width: 500,
            name:'view',
            title:'新增产品类别',
            isResize:true	
		});  
	}
		
	//删除
	function del(id,del_flag){
		var p ='操作' ;
		if(del_flag==0){
			p="停用";
			parmsData =[{name: 'Q|xpc.del_flag|S|EQ', value:'0'}];
		}else if(del_flag==1){
			p="启用";
			parmsData =[{name: 'Q|xpc.del_flag|S|EQ', value:'1'}];
		}
		
	    $.ligerDialog.confirm('确定'+p+'该《产品类别》吗？', function (yes){
          	  if(yes){
          	  	if(id == '1'){
          	  		$.ligerDialog.error("对不起，不允许删除此《产品类别》！");
          	  		return;
          	  	}
                $.ajax({
              	   url:'${root}product/delProCateXProCategory.do',
              	   type:'post',
              	   data:{'xProCategoryId':id},
              	   dataType:'html',
              	   async:false,
              	   success:function(data){
              		 if(data == "Y"){
              			$.ligerDialog.success("操作成功");
              			initGrids(parmsData);//刷新列表
              		 }else{
              		 	$.ligerDialog.error("操作失败");
              		 }
              	   }
              });
          	}
        });
	}
		
	//编辑产品类别
	function view1(id){
		if(id == '1'){
   	  		$.ligerDialog.error("对不起，不允许编辑此《产品类别》！");
   	  		return;
   	  	}
		$.ligerDialog.open({ 
	        height: 350,
	        url: '${root}product/editProCateXProCategory.do?xProCategoryId='+id,
	        width: 500,
	        name:'view',
	        title:'编辑产品类别',
	        isResize:true
	    });  
	}
		
	//查询
	function search(){
		var conditions = $("#searchBox").serializeArray();
		grid1.setOptions({parms:conditions});
		grid1.loadData();
	}
	
  </script>
  </head>
  
  <body>
  <form id="searchBox"> 
	  <input type="radio" name="Q|xpc.del_flag|S|EQ" value="0" checked="checked"/>启用
	  <input type="radio" name="Q|xpc.del_flag|S|EQ" value="1"/>禁用
	  <input type="text" id="str" name="str" value="" placeholder="请输入产品类别名称"/>
	  <input type="button" onclick="search()" value="搜索"/>
	  
	  <div class="table_box" >
	  </div>
  </form>
  </body>
</html>
