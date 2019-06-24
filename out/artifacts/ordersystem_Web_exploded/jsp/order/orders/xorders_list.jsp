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

<link rel="stylesheet" type="text/css" href="${root}css/loading.css">
<script type="text/javascript" src="${root}js/loading.js"></script>
<script type="text/javascript" src="../../../js/WdatePicker.js"></script>
<script>
		$(function(){
			var currentDate = new Date($.ajax({async: false}).getResponseHeader("Date")); //获取服务器系统的当前时间
			//var currentDate = new Date(); //获取本地电脑当前时间
			var currentYear = currentDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var currentMonth = currentDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			var currentDay = currentDate.getDate(); //获取当前日(1-31)
			
			$("#startDate").val(currentYear+"-"+currentMonth+"-"+currentDay+" 00:00:00");
			$("#endDate").val(currentYear+"-"+currentMonth+"-"+currentDay+" 12:00:00");
		})

		//当前订餐开始结束时间常量
		var staTime ="";
		var eTime = "";
		var dialog;
		var grid = null;
		function initGrids(){
		 if(staTime !="" && eTime !=""){
				startTime = staTime;
				endTime = eTime;
			}else{
				startTime =  $("#startDate").val();
				endTime = $("#endDate").val();
			} 
			grid = $(".table_box").ligerGrid({
				url: '${root}order/getCurrentOrderXOrders.do',
				parms:[{name:"startTime",value:startTime},{name:"endTime",value:endTime}],
				pageSize:10,
// 				usePager:false,
				async:false,
				toolbar:{items: [	{ text: '明细', click: detailed , icon:'add'},
	              					{ line:true }
	              				]
	              		},
				columns: [
					 { display: '序号', width:'10%',name:"__index",render:function(row,index){
						return index+1;
					} },
					{ display: '套餐名',width:'30%', name: 'food_name' },
					{ display: '描述',width:'40%', name: 'food_desc' },
					{ display: '份数', name: 'food_num',
						totalSummary:
	                    {
	                        type: 'sum',
	                         render:function(food_num,column){
								return "<span>当前页总份数："+food_num.sum+"</span>";
						      },
	                        algin:"center"
	                    }
					 }  
					]
			});
			staTime = "";
			eTime ="";
		}

		$(document).ready(function(){
			initGrids();
			//orderDay();
		});
		 
		function loading() {
			$('body').loading({
				loadingWidth:240,
				title:'请耐心等待...',
				name:'test',
				discription:'',
				direction:'column',
				type:'origin',
				// originBg:'#71EA71',
				originDivWidth:40,
				originDivHeight:40,
				originWidth:6,
				originHeight:6,
				smallLoading:false,
				loadingMaskBg:'rgba(0,0,0,0.2)'
			});
		}
		
		//add dingzhj at date 2016-03-02  订餐管理明细
		function detailed(){
			var startTime1 =startTime;
			var endTime2 =endTime;
			
			//定时器遮罩层
			loading();
			setTimeout(function(){
				removeLoading('test');
			},3000);
			dialog = $.ligerDialog.open({ 
	            height: 500,
	            url: '${root}order/toDetailedXOrders.do?startTime='+startTime1+"&endTime="+endTime2,
	            width: 1000,
	            name:'view',
	            title:'明细',
	            isResize:true	
            });  
		}
		//add dingzhj at date 2017-03-3 搜索
		function search(){
			//var startTime = $("#startDate").val();
			//var endTime = $("#endDate").val();
			initGrids();
		}
		// 当前订单
		function orderDay(){
			var dayDate = new Date();
			//var day = new Date("201-03-03 15:00:56");
			//获取当前时间（时分秒）
			var year = dayDate.getFullYear();
			var month = dayDate.getMonth()+1;
			var day = dayDate.getDate();
			var am = year+"-"+month+"-"+day+" 00:00:00"; //上午
			var pm = year+"-"+month+"-"+day+" 12:00:00";// 下午
			var night = year+"-"+month+"-"+day+" 22:00:00";//晚上
			var amTime = new Date(am.replace(/-/g,"/"));
			var pmTime = new Date(pm.replace(/-/g,"/"));
			var nightTime = new Date(night.replace(/-/g,"/"));
			// 上午点餐列表
			if(dayDate <  pmTime){
				staTime= am;
				eTime = pm;
				initGrids();
			}else if(dayDate >=pmTime){
				staTime= pm;
				eTime = night;
				initGrids();
			}else{
				//$("#saText").innerHtml("暂无数据");
				//initGrids2();
			}
			
		}
		
		
		//导出excel
		function exportExcel(){
			var fileName = "从业人员统计";
			var colId = "";
			var colName = "";
			var list = JSON.stringify(grid.rows);
			for(var i = 0;i<grid.getColumns().length;i++){
				if(grid.getColumns()[i].name != 'id'){
				colId+=grid.getColumns()[i].name+",";
				}
				colName += grid.getColumns()[i].display+",";
			}
			colId = colId.substr(0,colId.length-1);
			colName = colName.substr(0,colName.length-1);
			location.href="${root}/export/exportLocalBaseExport.do?fileName="+encodeURI(encodeURI(fileName))+"&colId="+encodeURI(encodeURI(colId))+"&colName="+encodeURI(encodeURI(colName))+"&typeList="+encodeURI(encodeURI(list));
			//location.href="${root}/staffSum/getXStaByIdXStaffSum.do";
		}
	</script>
</head>
<body>
	<div style="width:100%;">
		<h3 style="display:block;width:66px;height:20px;font-weight: 600;font-size: 16px;margin:10px auto;color:red;">温馨提示</h3>
		<h4 style="font-size: 14px;padding:0 116px;padding-bottom:2px;color:red;">1.首次点击“订餐管理”栏目，搜索条件默认系统当天的00:00:00-12:00:00这个时间段，页面展示以及“明细”是该时段的信息！！！</h4>
		<h4 style="font-size: 14px;padding:0 116px;padding-bottom:4px;color:red;">2.该“订餐管理”的订单信息与“现场订餐管理”的订单信息是互不相干的！！！</h4>
		<h4 style="font-size: 14px;padding:0 116px;padding-bottom:14px;color:red;">3.如要查询其它时间段的信息，请自行输入，但是最好一天一天的查询，否则数据量大，容易导致系统崩溃！！！</h4>
	</div>
	<form id="searchBox">
		<span style="margin-left:4px;">时间：</span>
		<input id="startDate" class="Wdate" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/> 
		 ~
		<input id="endDate" class="Wdate" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
		<button type="button"  onclick="search()">搜索</button> 
		<!-- <button type="button"  onclick="orderDay()">当前订单</button>  -->
		<!-- <input type="button" value="导出excel" onclick="exportExcel();"/> -->
		</br>
		<span id="saText"></span>
		<!-- <input type="hidden" id ="list" value=${orderList}/> -->
		<div class="table_box" style="margin-top: 5px;">
		</div>
	</form>
</body>
</html>
