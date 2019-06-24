<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>审批产品列表</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!-- <script type="text/javascript" src="../../../js/WdatePicker.js"></script> -->
<script>
		var dialog;
		var grid = null;
		
		function initGrids(){
		    var startTime = $("#startDate").val();
			var endTime =  $("#endDate").val();
			flag = $("#searchBox").find("input[name='exStatus']:checked").val();
			grid = $(".table_box").ligerGrid({
				url: '${root}productManage/getExamineAllXProductManage.do',			
				pageSize:10,
				parms:[{name:'startTime',value:startTime},{name:'endTime',value:endTime},{name:'flag',value:flag}],
				columns: [
					/* { display: '序号', width:'10%',name:"__index",render:function(row,index){
						return index+1;
					} }, */
					{ display: '产品名称',width:'15%', name: 'pro_name' },
					{ display: '产品描述',width:'15%', name: 'pro_describe' },
					{ display: '产品价格',width:'6%', name: 'pro_price'},
					{ display: '产品外链地址',width:'25%', name: 'pro_out_url'},
					{ display: '产品库存量',width:'6%', name: 'pro_remain' },
					{ display: '产品状态',width:'8%', name: 'pro_status',
						render:function(rowData){
							if(rowData.pro_status=='2'){
								return '上架中';
							}
						}
					},
					{ display: '产品录入时间',width:'12%',
						render:function(result){
							if(result.upd_time != null) {
								return result.upd_time;
							}else{
								return result.ins_time;
							} 
						}
						
					},
					{ display: '审批状态',width:'8%', name: 'pro_status',
						render:function(rowData){
							if(rowData.ex_status=='0'){
								return '审批中';
							}else if(rowData.ex_status=='1'){
								return '通过';
							}else if(rowData.ex_status=='2'){
								return '未通过';
							}
						}
					},
					{ display: '审批更新时间',width:'12%', 
						render:function(result){
							if(result.ex_upd_time != null) {
								return result.ex_upd_time;
							}else{
								return result.ex_ins_time;
							} 
						}
						
					},
					{ display: '操作', name: 'opt',width:'15%',render: function(row,rowData){
						var html = "";
						if(row.id == null){
							return null;
						}
						html += "<div onclick=\"select('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>查看</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
						html += "<div onclick=\"examine('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>审批</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
						return html;
					}}
				]
			});
		}

		$(document).ready(function(){
			initGrids();
		});
		 
		//搜索
		function search(){
			//var startTime = $("#startDate").val();
			//var endTime = $("#endDate").val();
			initGrids();
		}
		
		//产品明细
		function select(id) {
		dialog = $.ligerDialog.open({ 
	                 height: 530,
	                 url: '${root}productManage/getExamineProDescXProductManage.do?id='+id,
	                 width: 600,
	                 name:'view',
	                 title:'产品详情',
	                 isResize:true	
               	 });  
		}
		
		//明细和审批产品
		function examine(id) {
			dialog = $.ligerDialog.open({ 
	                     height: 500,
	                     url: '${root}productManage/getExamineDescXProductManage.do?id='+id,
	                     width: 600,
	                     name:'view',
	                     title:'审批详情/编辑',
	                     isResize:true	
                     });  
		}
		
	</script>
</head>
<body>
	<form id="searchBox">
		审批状态:
		<input type="radio"  name="exStatus" value="0" checked="checked"/>审批中
		<input type="radio"  name="exStatus" value="1"/>通过
		<input type="radio"  name="exStatus" value="2"/>未通过
		<span style="margin-left:4px;">审批时间：</span>
		<input id="startDate" class="Wdate" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/> 
		 ~
		<input id="endDate" class="Wdate" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
		<button type="button"  onclick="search()">搜索</button> 
		
		<!-- <input type="button" value="导出excel" onclick="exportExcel();"/> -->
		</br>
		<span id="saText"></span>
		<!-- <input type="hidden" id ="list" value=${orderList}/> -->
		<div class="table_box">
		</div>
	</form>
</body>
</html>
