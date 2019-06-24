<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>现场订餐列表</title>
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
			
			$("#startDate").val(currentYear+"-"+currentMonth+"-"+currentDay+" 11:30:00");
			$("#endDate").val(currentYear+"-"+currentMonth+"-"+currentDay+" 13:30:00");
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
				url: '${root}order/getCurrentXCOrderXOrders.do',
				parms:[{name:"startTime",value:startTime},{name:"endTime",value:endTime}],
				pageSize:10,
// 				usePager:false,
				async:false,
				columns: [
					{ display: '序号', width:'10%',name:"__index",render:function(row,index){
							return index+1;
						} 
					},
					{ display: '订餐时间',width:'15%', name: 'ins_time' },
					{ display: '公司名',width:'25%', name: 'company_name' },
					{ display: '部门',width:'15%', name: 'dept_name' },
					{ display: '订餐人',width:'10%', name: 'real_name' },
					{ display: '为谁订餐',width:'10%', name: 'vistor_name' },
					{ display: '餐饮公司',width:'15%', name: 'food_company_name' },
					{ display: '套餐名称',width:'15%', name: 'food_name' },
					{ display: '套餐单价',width:'10%', name: 'pay_price' },
					{ display: '套餐份数',width:'12%', name: 'food_num' },
					{ display: '实付金额',width:'15%', name: 'order_sum',
						totalSummary:
	                    {
	                        type: 'sum',
	                        render:function(order_sum,column){
								return "<span>当前页总金额：￥"+order_sum.sum+"</span>";
						    },
	                        algin:"center"
	                    }
					},
					{ display: '操作', name: '', width:'10%', render: function(row){
							var html = "<div onclick=\"del('"+row.id+"','"+row.food_num+"','"+row.ins_time+"','"+row.order_type+"','"+row.user_id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>取消订单</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-delete\"></div></div></div>";
							return html;
					 	}
					},
				]
			});
			staTime = "";
			eTime ="";
		}
		
		//取消现场订单
		function del(id,num,odderDate,orderType,userId){
			var dayTime = new Date($.ajax({async: false}).getResponseHeader("Date")); //获取服务器系统的当前时间
			//获取当前时间（时分秒）
			var year = dayTime.getFullYear(); //年
			var month = dayTime.getMonth()+1; //月
			var day = dayTime.getDate(); //日
			var amSart = year+"-"+month+"-"+day+LIVE_AMSTART; //上午开始
			var amEnd = year+"-"+month+"-"+day+LIVE_AMEND; //上午结束
			var pmStart = year+"-"+month+"-"+day+LIVE_PMSTART; //下午开始
			var pmEnd = year+"-"+month+"-"+day+LIVE_PMEND; //下午结束
			
			var amSartTime = new Date(amSart).getTime(); //上开
			var amEndTime = new Date(amEnd).getTime(); //上结束
			var pmSartTime = new Date(pmStart).getTime(); //下开
			var pmEndTime = new Date(pmEnd).getTime(); //下结束
			var odderTime = new Date(odderDate).getTime(); //订单
			
			if(orderType == "LUN"){ //上午
				if(odderTime >= amSartTime && odderTime <= amEndTime){
					if(dayTime >= amSartTime && dayTime <= amEndTime){
						q_my_order(id,num,userId);
					}else{
						alert("已经超时不能取消订单");
					}
				}else{
					alert("已经超时不能取消订单");
				}
			}else if(orderType == "DIN"){//下午
				if(odderTime>=pmSartTime && odderTime<=pmEndTime){
					q_my_order(id,num,userId);
				}else{
					alert("已经超时不能取消订单");
				}
			}else{
				alert("已经超时不能取消订单");
			}
		   
		}
		
		function q_my_order(id,num,userId){
			$.ligerDialog.confirm('确定要取消该订单吗？', function (yes){
			  	if(yes){
					$.ajax({
					    url:"${root}order/deleteLiveOrderXOrders.do", //请求的url地址
					    dataType:"json", //返回格式为json
					    data:{
					    	"orderId":id,
					    	"foodNum":num,
					    	"userId":userId,
					    	"orderType":"0"
					    },    
					    type:"POST", //请求方式
					    success:function(req){
					    	if(req.success == "false"){
					    		alert("已经完成不能取消订单");
					    	}else{
					    		$.ligerDialog.success("操作成功");
              					initGrids();
					    	}
					    },
					    error:function(req){
					    	alert(req.msg);
					    }
					});
				} 
			}); 
			
		}

		$(document).ready(function(){
			initGrids();
		});
		 
		function search(){
			initGrids();
		}
		
	</script>
</head>
<body>
	<div style="width:100%;">
		<h3 style="display:block;width:66px;height:20px;font-weight: 600;font-size: 16px;margin:10px auto;color:red;">温馨提示</h3>
		<h4 style="font-size: 14px;padding:0 116px;padding-bottom:4px;color:red;">1.首次点击“现场订餐管理”栏目，搜索条件默认系统当天的11:30:00-13:30:00这个时间段的信息！！！</h4>
		<h4 style="font-size: 14px;padding:0 116px;padding-bottom:4px;color:red;">2.该“现场订餐管理”的订单信息与“订餐管理”的订单信息是互不相干的！！！</h4>
		<h4 style="font-size: 14px;padding:0 116px;padding-bottom:14px;color:red;">3.如要查询其它时间段的信息，请自行输入，但是最好一天一天的查询，否则数据量大，容易导致系统崩溃！！！</h4>
	</div>
	<form id="searchBox">
		<span style="margin-left:4px;">时间：</span>
		<input id="startDate" class="Wdate" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/> 
		 ~
		<input id="endDate" class="Wdate" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
		<button type="button"  onclick="search()">搜索</button> 
		
		<span id="saText"></span>
		<div class="table_box" style="margin-top: 5px;">
		</div>
	</form>
</body>
</html>
