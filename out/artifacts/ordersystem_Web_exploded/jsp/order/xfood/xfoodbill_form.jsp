<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>头部</title>
<link rel="stylesheet" type="text/css" href="${root }css/common.css">
<script type="text/javascript" src="${root }js/jquery.min.js"></script>
<style type="text/css">
body{
	 background-color: #EEEEEE;
}

	.table{
	    border:1px solid #DDDDDD;
	    background-color: #FFFFFF;
	    color:#333333;
} 
	.table th,.table td {
		height:42px;
		text-align:center;
	}
	.table tr td {
		font-size:14px;
	}
.jian {
    display: inline-block;;
    text-align: center;
    width: 16px;
    height: 16px;
    border-radius: 50%;
    border: 1px solid #BBB;
    color: #BBB;
    line-height: 16px;
    font-size: 16px;
    font-weight: 800;
    background-color: #FFF;
    cursor:pointer;
}
.jia {
    display: inline-block;
    text-align: center;
    width: 16px;
    height: 16px;
    border-radius: 50%;
    border: 1px solid #BBB;
    color: #BBB;
    line-height: 16px;
    font-size: 16px;
    font-weight: 800;
    background-color: #FFF;
    cursor:pointer;
}
.number {
    font-weight: 800;
    width:80px;
    display:inline-block;
    text-align:center;
    font-size:14px;
    outline:none;
    margin:0px 5px;
    border:1px solid #F1F1F1;
}

/* 编辑菜单 */
.edit {
	width:800px;
	height:454px;
	background-color:#FFFFFF;
	display:none;
	position:fixed;
    left:50%;
    top:50%;
    margin-left:-400px;
    margin-top:-227px;
    z-Index:9999;
}
.edit-title {
	height:50px;
	background-color:#F5F3F3;
}
.title {
	float:left;
	line-height:50px;
	margin-left:31px;
	font-weight:700;
	color:#333333;
}
.close {
	float:right;
	line-height:50px;
	margin-right:31px;
	color:#EA1E1E;
	font-weight:700;
	display:block;
	cursor:pointer;
}
#all {
	position:absolute;
	left:100px;
	top:72px;
	color:#666666;
	font-weight:700;
	font-size:14px;
}
#checked{
	position:absolute;
	left:453px;
	top:72px;
	color:#333333;
	font-weight:700;
	font-size:14px;
}
.all {
	width:248px;
	height:286px;
	border:1px solid #DDDDDD;
	margin-top:10px;
	overflow-y:auto;
	box-sizing:border-box;
}
.all li {
	height:34px;
	border-bottom:1px solid #DDDDDD;
	text-align:left;
	line-height:34px;
	color:#666666;
	white-space:nowrap;
	overflow:hidden;
	text-overflow:ellipsis;
	padding-left:15px;
	position:relative;
}
.all li input {
	width:100%;
	height:34px;
	position:absolute;
	left:0px;
	top:0px;
	opacity:0;
}
.add,.remove {
	height:22px;
	width:65px;
	background-color:#FF8F33;
	position:absolute;
	color:#FFFFFF;
	font-weight:700;
	left:369px;
	top:205px;
}
.remove {
	top:235px;
}
.confirm {
	width:75px;
	height:30px;
	background-color:#FF8F33;
	color:#FFFFFF;
	font-weight:700;
	position:absolute;
	right:34px;
	bottom:21px;
}
#mask {
	z-index: 9998;
	position:fixed;
	top:0;
	left:0;
	width:100%;
	height:100%;
	background:#000;
	opacity:0.4;
	filter:alpha(opacity=40);
	display:none
}
#queding {
	width:60px;
	height:30px;
	background-color:skyBlue;
	border:none;
	color:#333333;
	margin:10px 0px 10px 10px;
}
 .id {
	display:none;
}  
</style>
</head>
<script type="text/javascript">
	var s=null;
	
	function goView(){
		/* location.href="${root}jsp/order/xfood/xfoodbill_view.jsp"; */
		if($("#queding").val()=="确定"){
			var boo =true;
			$(".table td").each(function(i,e){
				if($(this).html()=="暂无数据"){
					boo=  false;
					return false;
				}
			})
			if(boo){
				 for(var i=1;i<7;i++){
					  zhou["week_"+i]=getDay("week_"+i,"week_"+i+"-dinner");
				  }
				var Sunday=getFir("week_7","week_7-dinner");
				zhou["week_7"]=Sunday;
				console.log(zhou)
				 $.ajax({
					  type:"post",
					  url:"${root}order/saveFoodbillXFood.do",
					  data: {"foodbillInfo":JSON.stringify(zhou)},  
					  dataType:"json",
					  success:function(data){
						  
					  }
				  })
				s=$(".table").clone(true);
				$(".option").css("display","none");
				$("td").each(function(){
					if($(this).html()=="编辑"){
						$(this).css("display","none");
					}
				})
				$(".jia").css("display","none");
				$(".jian").css("display","none");
				$("#queding").val("编辑");
				$(".number").attr("readOnly","true");
			}else{
				alert("请选择完整套餐");
			}
			
		}else if($("#queding").val()=="编辑"){
			$(".table").remove();
			$("#queding").after(s);
			$("#queding").val("确定");
		}
		
	}
</script>
<body>
	<input type="button" value="确定" id="queding" onclick="goView();"/>
	<table border="1" class="table">
		<tr id="count">
			<th colspan="2">时间</th>
			<th width="151px">套餐名</th>
			<th class="id">ID</th>
			<th width="232px">描述</th>
			<th width="154px">单价</th>
			<th width="171px">份数</th>
			<th  width="152px" class="option">操作</th>
		</tr>
		<tr id="week_1">
			<td rowspan="5" width="106px"">周一</td>
			<td rowspan="3" width="102px" class="meal">午餐</td>
			<td class="meal-1">套餐A</td>
			<td class="id">G</td>
			<td class="meal-2">猪肉粉条+可乐</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1">
			</td>
			<td rowspan="3">编辑</td>
		</tr>
		<tr>
			<td class="meal-1">套餐B</td>
			<td class="id">G</td>
			<td>鱼香茄子+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1">
			</td>
		</tr>
		<tr>
			<td class="meal-1">套餐C</td>
			<td class="id">G</td>
			<td>宫保鸡丁+可乐</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1">
			</td>
		</tr>
		<tr id="week_1-dinner">
			<td rowspan="2"  class="meal">晚餐</td>
			<td class="meal-1">套餐D</td>
			<td class="id">G</td>
			<td class="meal-2">宫保鸡丁+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1">
			</td>
			<td rowspan="2">编辑</td>
		</tr>
		<tr>
			<td class="meal-1">套餐E</td>
			<td class="id">G</td>
			<td>宫保鸡丁+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr id="week_2">
			<td rowspan="5" width="106px">周二</td>
			<td rowspan="3" width="102px" class="meal">午餐</td>
			<td class="meal-1">套餐A</td>
			<td class="id">G</td>
			<td class="meal-2">猪肉粉条+可乐</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
			<td rowspan="3">编辑</td>
		</tr>
		<tr>
			<td class="meal-1">套餐B</td>
			<td class="id">G</td>
			<td>鱼香茄子+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr>
			<td class="meal-1">套餐C</td>
			<td class="id">G</td>
			<td>宫保鸡丁+可乐</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr id="week_2-dinner">
			<td rowspan="2" id="week_2-dinner"  class="meal">晚餐</td>
			<td class="meal-1">套餐D</td>
			<td class="id">G</td>
			<td class="meal-2">宫保鸡丁+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
			<td rowspan="2">编辑</td>
		</tr>
		<tr>
			<td class="meal-1">套餐C</td>
			<td class="id">G</td>
			<td>宫保鸡丁+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr id="week_3">
			<td rowspan="5" width="106px">周三</td>
			<td rowspan="3" width="102px" class="meal">午餐</td>
			<td class="meal-1">套餐A</td>
			<td class="id">G</td>
			<td class="meal-2">猪肉粉条+可乐</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
			<td rowspan="3">编辑</td>
		</tr>
		<tr>
			<td class="meal-1">套餐B</td>
			<td class="id">G</td>
			<td>鱼香茄子+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr>
			<td class="meal-1">套餐C</td>
			<td class="id">G</td>
			<td>宫保鸡丁+可乐</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr id="week_3-dinner">
			<td rowspan="2" id="week_2-dinner"  class="meal">晚餐</td>
			<td class="meal-1">套餐D</td>
			<td class="id">G</td>
			<td class="meal-2">宫保鸡丁+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
			<td rowspan="2">编辑</td>
		</tr>
		<tr>
			<td class="meal-1">套餐C</td>
			<td class="id">G</td>
			<td>宫保鸡丁+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr id="week_4">
			<td rowspan="5" width="106px">周四</td>
			<td rowspan="3" width="102px"  class="meal">午餐</td>
			<td class="meal-1">套餐A</td>
			<td class="id">G</td>
			<td class="meal-2">猪肉粉条+可乐</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
			<td rowspan="3">编辑</td>
		</tr>
		<tr>
			<td class="meal-1">套餐B</td>
			<td class="id">G</td>
			<td>鱼香茄子+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr>
			<td class="meal-1">套餐C</td>
			<td class="id">G</td>
			<td>宫保鸡丁+可乐</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr id="week_4-dinner">
			<td rowspan="2"  class="meal">晚餐</td>
			<td class="meal-1">套餐D</td>
			<td class="id">G</td>
			<td class="meal-2">宫保鸡丁+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
			<td rowspan="2">编辑</td>
		</tr>
		<tr>
			<td class="meal-1">套餐C</td>
			<td class="id">G</td>
			<td>宫保鸡丁+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr id="week_5">
			<td rowspan="5" width="106px">周五</td>
			<td rowspan="3" width="102px" class="meal">午餐</td>
			<td class="meal-1">套餐A</td>
			<td class="id">G</td>
			<td class="meal-2">猪肉粉条+可乐</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
			<td rowspan="3">编辑</td>
		</tr>
		<tr>
			<td class="meal-1">套餐B</td>
			<td class="id">G</td>
			<td>鱼香茄子+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr>
			<td class="meal-1">套餐C</td>
			<td class="id">G</td>
			<td>宫保鸡丁+可乐</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr id="week_5-dinner">
			<td rowspan="2"  class="meal">晚餐</td>
			<td class="meal-1">套餐D</td>
			<td class="id">G</td>
			<td class="meal-2">宫保鸡丁+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
			<td rowspan="2">编辑</td>
		</tr>
		<tr>
			<td class="meal-1">套餐C</td>
			<td class="id">G</td>
			<td>宫保鸡丁+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr id="week_6">
			<td rowspan="5" width="106px">周六</td>
			<td rowspan="3" width="102px" class="meal">午餐</td>
			<td class="meal-1">套餐A</td>
			<td class="id">G</td>
			<td class="meal-2">猪肉粉条+可乐</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
			<td rowspan="3">编辑</td>
		</tr>
		<tr>
			<td class="meal-1">套餐B</td>
			<td class="id">G</td>
			<td>鱼香茄子+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr>
			<td class="meal-1">套餐C</td>
			<td class="id">G</td>
			<td>宫保鸡丁+可乐</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr id="week_6-dinner">
			<td rowspan="2"  class="meal">晚餐</td>
			<td class="meal-1">套餐D</td>
			<td class="id">G</td>
			<td class="meal-2">宫保鸡丁+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
			<td rowspan="2">编辑</td>
		</tr>
		<tr>
			<td class="meal-1">套餐C</td>
			<td class="id">G</td>
			<td>宫保鸡丁+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr id="week_7">
			<td rowspan="5" width="106px">周日</td>
			<td rowspan="3" width="102px" class="meal">午餐</td>
			<td class="meal-1">套餐A</td>
			<td class="id">G</td>
			<td class="meal-2">猪肉粉条+可乐</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
			<td rowspan="3">编辑</td>
		</tr>
		<tr>
			<td class="meal-1">套餐B</td>
			<td class="id">G</td>
			<td>鱼香茄子+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr>
			<td class="meal-1">套餐C</td>
			<td class="id">G</td>
			<td>宫保鸡丁+可乐</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
		<tr id="week_7-dinner">
			<td rowspan="2"  class="meal">晚餐</td>
			<td class="meal-1">套餐D</td>
			<td class="id">G</td>
			<td class="meal-2">宫保鸡丁+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
			<td rowspan="2">编辑</td>
		</tr>
		<tr>
			<td class="meal-1">套餐C</td>
			<td class="id">G</td>
			<td>宫保鸡丁+橙汁</td>
			<td class="price-1">25</td>
			<td>
				<input class="number" value="1" type="number" min="1" step="1"> 
			</td>
		</tr>
	</table>
	
	<div class="edit">
		<div class="edit-title">
			<div class="title">编辑</div>
			<div class="close">关闭</div>
		</div>
		<div id="all">
			所有套餐：
			<div class="all">
				<ul id="allFood">
				
					<!-- <li><span>套餐A</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox" id="A"></li>
					<li><span>套餐AB</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox" id="B"></li>
					<li><span>套餐AC</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox"></li>
					<li><span>套餐AD</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox"></li>
					<li><span>套餐A</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox"></li>
					<li><span>套餐A</span><span>（韭菜鸡蛋盖饭+可乐）套餐A（韭菜鸡蛋盖饭+可乐</span><input type="checkbox"></li>
					<li><span>套餐A</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox"></li>
					<li><span>套餐A</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox"></li>
					<li><span>套餐A</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox"></li>
					<li><span>套餐A</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox"></li>
					<li><span>套餐A</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox"></li>
					<li><span>套餐A</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox"></li> -->
				</ul>
			</div>
		</div>
		<input type="button" value="添加 &gt;&gt;" id="btn1" class="add">
		<input type="button" value="&lt;&lt; 删除" id="btn2" class="remove">
		<div id="checked">
			已选择套餐：
			<div class="all">
				<ul id="checkedFood">
<!-- 					<li><span>套餐A</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox"></li> -->
<!-- 					<li><span>套餐B</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox"></li> -->
<!-- 					<li><span>套餐C</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox"></li> -->
<!-- 					<li><span>套餐D</span><span>（韭菜鸡蛋盖饭+可乐）</span><input type="checkbox"></li> -->
				</ul>
			</div>
		</div>
		<button class="confirm">确定</button>
	</div>
	<div id="mask"></div>
	<script type="text/javascript">
	var foodbillInfo=eval(${ foodbillInfo});
	var week_1=[],week_2=[],week_3=[],week_4=[],week_5=[],week_6=[],week_7=[];
	var week_1_l=[],week_1_d=[],week_2_l=[],week_2_d=[],week_3_l=[],week_3_d=[],week_4_l=[],week_4_d=[],week_5_l=[],week_5_d=[],week_6_l=[],week_6_d=[],week_7_l=[],week_7_d=[];
	if(foodbillInfo[0]!=null){
		/* 将每天菜单分开保存  */
		$.each(foodbillInfo,function(i,e){
			if(e.week=="WEEK_1"){
				week_1.push(e);
			}else if(e.week=="WEEK_2"){
				week_2.push(e);
			}else if(e.week=="WEEK_3"){
				week_3.push(e);
			}else if(e.week=="WEEK_4"){
				week_4.push(e);
			}else if(e.week=="WEEK_5"){
				week_5.push(e);
			}else if(e.week=="WEEK_6"){
				week_6.push(e);
			}else if(e.week=="WEEK_7"){
				week_7.push(e);
			};
		});
	}
	
	/* 将每天菜单分午餐晚餐保存 */
	$.each(week_1,function(i,e){
		if(e.food_type=='LUN'){
			week_1_l.push(e)
		}else if(e.food_type=="DIN"){
			week_1_d.push(e)
		}
	})
	$.each(week_2,function(i,e){
		if(e.food_type=='LUN'){
			week_2_l.push(e)
		}else if(e.food_type=="DIN"){
			week_2_d.push(e)
		}
	})
	$.each(week_3,function(i,e){
		if(e.food_type=='LUN'){
			week_3_l.push(e)
		}else if(e.food_type=="DIN"){
			week_3_d.push(e)
		}
	})
	$.each(week_4,function(i,e){
		if(e.food_type=='LUN'){
			week_4_l.push(e)
		}else if(e.food_type=="DIN"){
			week_4_d.push(e)
		}
	})
	$.each(week_5,function(i,e){
		if(e.food_type=='LUN'){
			week_5_l.push(e)
		}else if(e.food_type=="DIN"){
			week_5_d.push(e)
		}
	})
	$.each(week_6,function(i,e){
		if(e.food_type=='LUN'){
			week_6_l.push(e)
		}else if(e.food_type=="DIN"){
			week_6_d.push(e)
		}
	})
	$.each(week_7,function(i,e){
		if(e.food_type=='LUN'){
			week_7_l.push(e)
		}else if(e.food_type=="DIN"){
			week_7_d.push(e)
		}
	})
	
	/* 周一 */
	/* 设置午餐第一行 */
	 if(week_1_l.length==0){
		 	$("#week_1").children(".meal-1").html("暂无数据");	
			$("#week_1").children(".id").html("暂无数据");
			$("#week_1").children(".meal-2").html("暂无数据");
			$("#week_1").children().find(".number").val('1');
	  }else {
		  $("#week_1").children(".meal-1").html(week_1_l[week_1_l.length-1].food_name);	
			$("#week_1").children(".id").html(week_1_l[week_1_l.length-1].food_id);
			$("#week_1").children(".meal-2").html(week_1_l[week_1_l.length-1].food_desc);
			$("#week_1").children(".price-1").html(week_1_l[week_1_l.length-1].sell_price);
			$("#week_1").children().find(".number").val(week_1_l[week_1_l.length-1].food_num);
	  }
	
	
	  
	  /* 午餐去掉除第一行其余部分 */
	  $($("#week_1").nextAll()).each(function(){
		  if($(this).children().length<6){
			  $(this).remove();
		  }else{
			  return false;
		  }
	  })
	  /* 午餐动态添加除第一行的其他内容 */
	for(var i=0;i<week_1_l.length-1;i++){
				 var str="";
				  str+=	'<tr>'
				  str+=		'<td class="meal-1">'+week_1_l[i].food_name+'</td>'
				  str+=		'<td class="id">'+week_1_l[i].food_id+'</td>'
				  str+=		'<td>'+week_1_l[i].food_desc+'</td>'
				  str+=		'<td>'+week_1_l[i].sell_price+'</td>'
				  str+=		'<td>'
				  str+=			'<input min="1" step="1" type="number" class="number" value='+week_1_l[i].food_num+'>'
				  str+=		'</td>'
				  str+='</tr>' 
				  $("#week_1").after(str);
				  $(".jia").unbind('click').click(function(e){
		    			var s=parseInt($(this).siblings("input[class='number']").val());
		    			s++;
		    			$(this).siblings("input[class='number']").val(s);
				});
				$(".jian").unbind('click').click(function(){
					var n=parseInt($(this).siblings("input[class='number']").val());
					n--;
					if(n<1||n==1){
		    			n=1;
					}
					$(this).siblings("span[class='number']").val(n);
				})
				  
			}
	 
	
	  /* 设置晚餐第一行 */
	  if(week_1_d.length==0){
		  	$("#week_1-dinner").children(".meal-1").html("暂无数据");	
			$("#week_1-dinner").children(".id").html("暂无数据");
			$("#week_1-dinner").children(".meal-2").html("暂无数据");
			$("#week_1-dinner").children().find(".number").val("1");
	  }else {
		  	$("#week_1-dinner").children(".meal-1").html(week_1_d[week_1_d.length-1].food_name);	
			$("#week_1-dinner").children(".id").html(week_1_d[week_1_d.length-1].food_id);
			$("#week_1-dinner").children(".meal-2").html(week_1_d[week_1_d.length-1].food_desc);
			$("#week_1-dinner").children(".price-1").html(week_1_d[week_1_d.length-1].sell_price);
			$("#week_1-dinner").children().find(".number").val(week_1_d[week_1_d.length-1].food_num);
	  }
		 	
	  	
	  
		 /* 晚餐去掉除第一行其余部分 */
		  $($("#week_1-dinner").nextAll()).each(function(){
			  if($(this).children().length<6){
				  $(this).remove();
			  }else{
				  return false;
			  }
		  })
	   /* 晚餐动态添加除第一行的其他内容 */
	   
	 if(week_1_d.length!=0){
		 for(var i=0;i<week_1_d.length-1;i++){
			 var str="";
			  str+=	'<tr>'
			  str+=		'<td class="meal-1">'+week_1_d[i].food_name+'</td>'
			  str+=		'<td class="id">'+week_1_d[i].food_id+'</td>'
			  str+=		'<td>'+week_1_d[i].food_desc+'</td>'
			  str+=		'<td>'+week_1_d[i].sell_price+'</td>'
			  str+=		'<td>'
			  str+=			'<input type="number" class="number" min="1" step="1" value='+week_1_d[i].food_num+'>'
			  str+=		'</td>'
			  str+='</tr>' 
			  $("#week_1-dinner").after(str);
			  $(".jia").unbind('click').click(function(e){
	    			var s=parseInt($(this).siblings("input[class='number']").val());
	    			s++;
	    			$(this).siblings("input[class='number']").val(s);
			});
			$(".jian").unbind('click').click(function(){
				var n=parseInt($(this).siblings("input[class='number']").val());
				n--;
				if(n<1||n==1){
	    			n=1;
				}
				$(this).siblings("input[class='number']").val(n);
			})
			  
		} 
	 }
		 
		  /* 设置编辑、午餐、周时间合并列 */
		  if(week_1_l.length==0){
			  $("#week_1").find(".meal")[0].rowSpan=1;
			  $("#week_1").children()[7].rowSpan=1;
		  }else{
			  $("#week_1").find(".meal")[0].rowSpan=week_1_l.length;
			  $("#week_1").children()[7].rowSpan=week_1_l.length;
		  }
		  /* 设置编辑、晚餐合并列 */
		  $("#week_1-dinner").find(".meal")[0].rowSpan=week_1_d.length;
		  $("#week_1-dinner").children()[6].rowSpan=week_1_d.length;
		  
		  if(week_1_d.length==0){
			  $("#week_1-dinner").find(".meal")[0].rowSpan=1;
			  $("#week_1-dinner").children()[6].rowSpan=1;
		  }else{
			  $("#week_1-dinner").find(".meal")[0].rowSpan=week_1_d.length;
			  $("#week_1-dinner").children()[6].rowSpan=week_1_d.length;
		  }
		  $("#week_1").children()[0].rowSpan=$("#week_1").find(".meal")[0].rowSpan+ $("#week_1-dinner").find(".meal")[0].rowSpan;
	
		 /* 周二 */
		 
		 /* 设置午餐第一行 */
	 if(week_2_l.length==0){
		 $("#week_2").children(".meal-1").html("暂无数据");	
			$("#week_2").children(".id").html("暂无数据");
			$("#week_2").children(".meal-2").html("暂无数据");
			$("#week_2").children().find(".number").val('1');
	  }else {
		  $("#week_2").children(".meal-1").html(week_2_l[week_2_l.length-1].food_name);	
			$("#week_2").children(".id").html(week_2_l[week_2_l.length-1].food_id);
			$("#week_2").children(".meal-2").html(week_2_l[week_2_l.length-1].food_desc);
			$("#week_2").children(".price-1").html(week_2_l[week_2_l.length-1].sell_price);
			$("#week_2").children().find(".number").val(week_2_l[week_2_l.length-1].food_num);
	  }
	
	
	  
	  /* 午餐去掉除第一行其余部分 */
	  $($("#week_2").nextAll()).each(function(){
		  if($(this).children().length<6){
			  $(this).remove();
		  }else{
			  return false;
		  }
	  })
	  /* 午餐动态添加除第一行的其他内容 */
	for(var i=0;i<week_2_l.length-1;i++){
				 var str="";
				  str+=	'<tr>'
				  str+=		'<td class="meal-1">'+week_2_l[i].food_name+'</td>'
				  str+=		'<td class="id">'+week_2_l[i].food_id+'</td>'
				  str+=		'<td>'+week_2_l[i].food_desc+'</td>'
				  str+=		'<td>'+week_2_l[i].sell_price+'</td>'
				  str+=		'<td>'
				  str+=			'<input type="number" class="number" min="1" step="1" value='+week_2_l[i].food_num+'>'
				  str+=		'</td>'
				  str+='</tr>' 
				  $("#week_2").after(str);
				  $(".jia").unbind('click').click(function(e){
		    			var s=parseInt($(this).siblings("input[class='number']").val());
		    			s++;
		    			$(this).siblings("input[class='number']").val(s);
				});
				$(".jian").unbind('click').click(function(){
					var n=parseInt($(this).siblings("input[class='number']").val());
					n--;
					if(n<1||n==1){
		    			n=1;
					}
					$(this).siblings("input[class='number']").val(n);
				})
				  
			}
	 
	  /* 设置晚餐第一行 */
	  if(week_2_d.length==0){
		  	$("#week_2-dinner").children(".meal-1").html("暂无数据");	
			$("#week_2-dinner").children(".id").html("暂无数据");
			$("#week_2-dinner").children(".meal-2").html("暂无数据");
			$("#week_2-dinner").children().find(".number").val("1");
	  }else {
		  	$("#week_2-dinner").children(".meal-1").html(week_2_d[week_2_d.length-1].food_name);	
			$("#week_2-dinner").children(".id").html(week_2_d[week_2_d.length-1].food_id);
			$("#week_2-dinner").children(".meal-2").html(week_2_d[week_2_d.length-1].food_desc);
			$("#week_2-dinner").children(".price-1").html(week_2_d[week_2_d.length-1].sell_price);
			$("#week_2-dinner").children().find(".number").val(week_2_d[week_2_d.length-1].food_num);
	  }
		 	
	  	
	  
		 /* 晚餐去掉除第一行其余部分 */
		  $($("#week_2-dinner").nextAll()).each(function(){
			  if($(this).children().length<6){
				  $(this).remove();
			  }else{
				  return false;
			  }
		  })
	   /* 晚餐动态添加除第一行的其他内容 */
	   
	 if(week_2_d.length!=0){
		 for(var i=0;i<week_2_d.length-1;i++){
			 var str="";
			  str+=	'<tr>'
			  str+=		'<td class="meal-1">'+week_2_d[i].food_name+'</td>'
			  str+=		'<td class="id">'+week_2_d[i].food_id+'</td>'
			  str+=		'<td>'+week_2_d[i].food_desc+'</td>'
			  str+=		'<td>'+week_2_d[i].sell_price+'</td>'
			  str+=		'<td>'
			  str+=			'<input type="number" class="number" min="1" step="1" value='+week_2_d[i].food_num+'>'
			  str+=		'</td>'
			  str+='</tr>' 
			  $("#week_2-dinner").after(str);
			  $(".jia").unbind('click').click(function(e){
	    			var s=parseInt($(this).siblings("input[class='number']").val());
	    			s++;
	    			$(this).siblings("input[class='number']").val(s);
			});
			$(".jian").unbind('click').click(function(){
				var n=parseInt($(this).siblings("input[class='number']").val());
				n--;
				if(n<1||n==1){
	    			n=1;
				}
				$(this).siblings("input[class='number']").val(n);
			})
			  
		} 
	 }
		  /* 设置编辑、午餐、周时间合并列 */
		  if(week_2_l.length==0){
			  $("#week_2").find(".meal")[0].rowSpan=1;
			  $("#week_2").children()[7].rowSpan=1;
		  }else{
			  $("#week_2").find(".meal")[0].rowSpan=week_2_l.length;
			  $("#week_2").children()[7].rowSpan=week_2_l.length;
		  }
		  
		  if(week_2_d.length==0){
			  $("#week_2-dinner").find(".meal")[0].rowSpan=1;
			  $("#week_2-dinner").children()[6].rowSpan=1;
		  }else{
			  $("#week_2-dinner").find(".meal")[0].rowSpan=week_2_d.length;
			  $("#week_2-dinner").children()[6].rowSpan=week_2_d.length;
		  }
		  $("#week_2").children()[0].rowSpan=$("#week_2").find(".meal")[0].rowSpan+ $("#week_2-dinner").find(".meal")[0].rowSpan;
		 
		  /* 周三 */
		  /* 设置午餐第一行 */
	 if(week_3_l.length==0){
		 $("#week_3").children(".meal-1").html("暂无数据");	
			$("#week_3").children(".id").html("暂无数据");
			$("#week_3").children(".meal-2").html("暂无数据");
			$("#week_3").children().find(".number").val('1');
	  }else {
		  $("#week_3").children(".meal-1").html(week_3_l[week_3_l.length-1].food_name);	
			$("#week_3").children(".id").html(week_3_l[week_3_l.length-1].food_id);
			$("#week_3").children(".meal-2").html(week_3_l[week_3_l.length-1].food_desc);
			$("#week_3").children(".price-1").html(week_3_l[week_3_l.length-1].sell_price);
			$("#week_3").children().find(".number").val(week_3_l[week_3_l.length-1].food_num);
	  }
	
	
	  
	  /* 午餐去掉除第一行其余部分 */
	  $($("#week_3").nextAll()).each(function(){
		  if($(this).children().length<6){
			  $(this).remove();
		  }else{
			  return false;
		  }
	  })
	  /* 午餐动态添加除第一行的其他内容 */
	for(var i=0;i<week_3_l.length-1;i++){
				 var str="";
				  str+=	'<tr>'
				  str+=		'<td class="meal-1">'+week_3_l[i].food_name+'</td>'
				  str+=		'<td class="id">'+week_3_l[i].food_id+'</td>'
				  str+=		'<td>'+week_3_l[i].food_desc+'</td>'
				  str+=		'<td>'+week_3_l[i].sell_price+'</td>'
				  str+=		'<td>'
				  str+=			'<input type="number" class="number" min="1" step="1" value='+week_3_l[i].food_num+'>'
				  str+=		'</td>'
				  str+='</tr>' 
				  $("#week_3").after(str);
				  $(".jia").unbind('click').click(function(e){
		    			var s=parseInt($(this).siblings("input[class='number']").val());
		    			s++;
		    			$(this).siblings("input[class='number']").val(s);
				});
				$(".jian").unbind('click').click(function(){
					var n=parseInt($(this).siblings("input[class='number']").val());
					n--;
					if(n<1||n==1){
		    			n=1;
					}
					$(this).siblings("input[class='number']").val(n);
				})
				  
			}
	
	  /* 设置晚餐第一行 */
	  if(week_3_l.length==0){
		  	$("#week_3-dinner").children(".meal-1").html("暂无数据");	
			$("#week_3-dinner").children(".id").html("暂无数据");
			$("#week_3-dinner").children(".meal-2").html("暂无数据");
			$("#week_3-dinner").children().find(".number").val("1");
	  }else {
		  	$("#week_3-dinner").children(".meal-1").html(week_3_d[week_3_d.length-1].food_name);	
			$("#week_3-dinner").children(".id").html(week_3_d[week_3_d.length-1].food_id);
			$("#week_3-dinner").children(".meal-2").html(week_3_d[week_3_d.length-1].food_desc);
			$("#week_3-dinner").children(".price-1").html(week_3_d[week_3_d.length-1].sell_price);
			$("#week_3-dinner").children().find(".number").val(week_3_d[week_3_d.length-1].food_num);
	  }
		 	
	  	
	  
		 /* 晚餐去掉除第一行其余部分 */
		  $($("#week_3-dinner").nextAll()).each(function(){
			  if($(this).children().length<6){
				  $(this).remove();
			  }else{
				  return false;
			  }
		  })
	   /* 晚餐动态添加除第一行的其他内容 */
	   
	 if(week_3_d.length!=0){
		 for(var i=0;i<week_3_d.length-1;i++){
			 var str="";
			  str+=	'<tr>'
			  str+=		'<td class="meal-1">'+week_3_d[i].food_name+'</td>'
			  str+=		'<td class="id">'+week_3_d[i].food_id+'</td>'
			  str+=		'<td>'+week_3_d[i].food_desc+'</td>'
			  str+=		'<td>'+week_3_d[i].sell_price+'</td>'
			  str+=		'<td>'
			  str+=			'<input type="number" class="number" min="1" step="1" value='+week_3_d[i].food_num+'>'
			  str+=		'</td>'
			  str+='</tr>' 
			  $("#week_3-dinner").after(str);
			  $(".jia").unbind('click').click(function(e){
	    			var s=parseInt($(this).siblings("input[class='number']").val());
	    			s++;
	    			$(this).siblings("input[class='number']").val(s);
			});
			$(".jian").unbind('click').click(function(){
				var n=parseInt($(this).siblings("input[class='number']").val());
				n--;
				if(n<1||n==1){
	    			n=1;
				}
				$(this).siblings("input[class='number']").val(n);
			})
			  
		} 
	 }
		  /* 设置编辑、午餐、周时间合并列 */
		  if(week_3_l.length==0){
			  $("#week_3").find(".meal")[0].rowSpan=1;
			  $("#week_3").children()[7].rowSpan=1;
		  }else{
			  $("#week_3").find(".meal")[0].rowSpan=week_3_l.length;
			  $("#week_3").children()[7].rowSpan=week_3_l.length;
		  }
		  
		  if(week_3_d.length==0){
			  $("#week_3-dinner").find(".meal")[0].rowSpan=1;
			  $("#week_3-dinner").children()[6].rowSpan=1;
		  }else{
			  $("#week_3-dinner").find(".meal")[0].rowSpan=week_3_d.length;
			  $("#week_3-dinner").children()[6].rowSpan=week_3_d.length;
		  }
		  $("#week_3").children()[0].rowSpan=$("#week_3").find(".meal")[0].rowSpan+ $("#week_3-dinner").find(".meal")[0].rowSpan;
		  
		  /* 周四 */
		    /* 设置午餐第一行 */
	 if(week_4_l.length==0){
		 $("#week_4").children(".meal-1").html("暂无数据");	
			$("#week_4").children(".id").html("暂无数据");
			$("#week_4").children(".meal-2").html("暂无数据");
			$("#week_4").children().find(".number").val('1');
	  }else {
		  $("#week_4").children(".meal-1").html(week_4_l[week_4_l.length-1].food_name);	
			$("#week_4").children(".id").html(week_4_l[week_4_l.length-1].food_id);
			$("#week_4").children(".meal-2").html(week_4_l[week_4_l.length-1].food_desc);
			$("#week_4").children(".price-1").html(week_4_l[week_4_l.length-1].sell_price);
			$("#week_4").children().find(".number").val(week_4_l[week_4_l.length-1].food_num);
	  }
	
	
	  
	  /* 午餐去掉除第一行其余部分 */
	  $($("#week_4").nextAll()).each(function(){
		  if($(this).children().length<6){
			  $(this).remove();
		  }else{
			  return false;
		  }
	  })
	  /* 午餐动态添加除第一行的其他内容 */
	for(var i=0;i<week_4_l.length-1;i++){
				 var str="";
				  str+=	'<tr>'
				  str+=		'<td class="meal-1">'+week_4_l[i].food_name+'</td>'
				  str+=		'<td class="id">'+week_4_l[i].food_id+'</td>'
				  str+=		'<td>'+week_4_l[i].food_desc+'</td>'
				  str+=		'<td>'+week_4_l[i].sell_price+'</td>'
				  str+=		'<td>'
				  str+=			'<input type="number" class="number" min="1" step="1" value='+week_4_l[i].food_num+'>'
				  str+=		'</td>'
				  str+='</tr>' 
				  $("#week_4").after(str);
				  $(".jia").unbind('click').click(function(e){
		    			var s=parseInt($(this).siblings("input[class='number']").val());
		    			s++;
		    			$(this).siblings("input[class='number']").val(s);
				});
				$(".jian").unbind('click').click(function(){
					var n=parseInt($(this).siblings("input[class='number']").val());
					n--;
					if(n<1||n==1){
		    			n=1;
					}
					$(this).siblings("input[class='number']").val(n);
				})
				  
			}
	  /* 设置编辑、午餐、周时间合并列 */
	  $("#week_4").find(".meal")[0].rowSpan=week_4_l.length;
	  $("#week_4").children()[7].rowSpan=week_4_l.length;
	  $("#week_4").children()[0].rowSpan=week_4_l.length+week_4_l.length+1;
	
	  /* 设置晚餐第一行 */
	  if(week_4_l.length==0){
		  	$("#week_4-dinner").children(".meal-1").html("暂无数据");	
			$("#week_4-dinner").children(".id").html("暂无数据");
			$("#week_4-dinner").children(".meal-2").html("暂无数据");
			$("#week_4-dinner").children().find(".number").val("1");
	  }else {
		  	$("#week_4-dinner").children(".meal-1").html(week_4_d[week_4_d.length-1].food_name);	
			$("#week_4-dinner").children(".id").html(week_4_d[week_4_d.length-1].food_id);
			$("#week_4-dinner").children(".meal-2").html(week_4_d[week_4_d.length-1].food_desc);
			$("#week_4-dinner").children(".price-1").html(week_4_d[week_4_d.length-1].sell_price);
			$("#week_4-dinner").children().find(".number").val(week_4_d[week_4_d.length-1].food_num);
	  }
		 	
	  	
	  
		 /* 晚餐去掉除第一行其余部分 */
		  $($("#week_4-dinner").nextAll()).each(function(){
			  if($(this).children().length<6){
				  $(this).remove();
			  }else{
				  return false;
			  }
		  })
	   /* 晚餐动态添加除第一行的其他内容 */
	   
	 if(week_4_d.length!=0){
		 for(var i=0;i<week_4_d.length-1;i++){
			 var str="";
			  str+=	'<tr>'
			  str+=		'<td class="meal-1">'+week_4_d[i].food_name+'</td>'
			  str+=		'<td class="id">'+week_4_d[i].food_id+'</td>'
			  str+=		'<td>'+week_4_d[i].food_desc+'</td>'
			  str+=		'<td>'+week_4_d[i].sell_price+'</td>'
			  str+=		'<td>'
			  str+=			'<input type="number" class="number" min="1" step="1" value='+week_4_d[i].food_num+'>'
			  str+=		'</td>'
			  str+='</tr>' 
			  $("#week_4-dinner").after(str);
			  $(".jia").unbind('click').click(function(e){
	    			var s=parseInt($(this).siblings("input[class='number']").val());
	    			s++;
	    			$(this).siblings("input[class='number']").val(s);
			});
			$(".jian").unbind('click').click(function(){
				var n=parseInt($(this).siblings("input[class='number']").val());
				n--;
				if(n<1||n==1){
	    			n=1;
				}
				$(this).siblings("input[class='number']").val(n);
			})
			  
		} 
	 }
		  /* 设置编辑、晚餐合并列 */
		  /* 设置编辑、午餐、周时间合并列 */
		  if(week_4_l.length==0){
			  $("#week_4").find(".meal")[0].rowSpan=1;
			  $("#week_4").children()[7].rowSpan=1;
		  }else{
			  $("#week_4").find(".meal")[0].rowSpan=week_4_l.length;
			  $("#week_4").children()[7].rowSpan=week_4_l.length;
		  }
		  
		  if(week_4_d.length==0){
			  $("#week_4-dinner").find(".meal")[0].rowSpan=1;
			  $("#week_4-dinner").children()[6].rowSpan=1;
		  }else{
			  $("#week_4-dinner").find(".meal")[0].rowSpan=week_4_d.length;
			  $("#week_4-dinner").children()[6].rowSpan=week_4_d.length;
		  }
		  $("#week_4").children()[0].rowSpan=$("#week_4").find(".meal")[0].rowSpan+ $("#week_4-dinner").find(".meal")[0].rowSpan;
		  
		  
		  /* 周五 */
		  /* 设置午餐第一行 */
			 if(week_5_l.length==0){
				 $("#week_5").children(".meal-1").html("暂无数据");	
					$("#week_5").children(".id").html("暂无数据");
					$("#week_5").children(".meal-2").html("暂无数据");
					$("#week_5").children().find(".number").val('1');
			  }else {
				  $("#week_5").children(".meal-1").html(week_5_l[week_5_l.length-1].food_name);	
					$("#week_5").children(".id").html(week_5_l[week_5_l.length-1].food_id);
					$("#week_5").children(".meal-2").html(week_5_l[week_5_l.length-1].food_desc);
					$("#week_5").children(".price-1").html(week_5_l[week_5_l.length-1].sell_price);
					$("#week_5").children().find(".number").val(week_5_l[week_5_l.length-1].food_num);
			  }
			
			
			  
			  /* 午餐去掉除第一行其余部分 */
			  $($("#week_5").nextAll()).each(function(){
				  if($(this).children().length<6){
					  $(this).remove();
				  }else{
					  return false;
				  }
			  })
			  /* 午餐动态添加除第一行的其他内容 */
			for(var i=0;i<week_5_l.length-1;i++){
						 var str="";
						  str+=	'<tr>'
						  str+=		'<td class="meal-1">'+week_5_l[i].food_name+'</td>'
						  str+=		'<td class="id">'+week_5_l[i].food_id+'</td>'
						  str+=		'<td>'+week_5_l[i].food_desc+'</td>'
						  str+=		'<td>'+week_5_l[i].sell_price+'</td>'
						  str+=		'<td>'
						  str+=			'<input type="number" class="number" min="1" step="1" value='+week_5_l[i].food_num+'>'
						  str+=		'</td>'
						  str+='</tr>' 
						  $("#week_5").after(str);
						  $(".jia").unbind('click').click(function(e){
				    			var s=parseInt($(this).siblings("input[class='number']").val());
				    			s++;
				    			$(this).siblings("input[class='number']").val(s);
						});
						$(".jian").unbind('click').click(function(){
							var n=parseInt($(this).siblings("input[class='number']").val());
							n--;
							if(n<1||n==1){
				    			n=1;
							}
							$(this).siblings("input[class='number']").val(n);
						})
						  
					}
			  /* 设置编辑、午餐、周时间合并列 */
			  $("#week_5").find(".meal")[0].rowSpan=week_5_l.length;
			  $("#week_5").children()[7].rowSpan=week_5_l.length;
			  $("#week_5").children()[0].rowSpan=week_5_l.length+week_5_l.length+1;
			
			  /* 设置晚餐第一行 */
			  if(week_5_d.length==0){
				  	$("#week_5-dinner").children(".meal-1").html("暂无数据");	
					$("#week_5-dinner").children(".id").html("暂无数据");
					$("#week_5-dinner").children(".meal-2").html("暂无数据");
					$("#week_5-dinner").children().find(".number").val("1");
			  }else {
				  	$("#week_5-dinner").children(".meal-1").html(week_5_d[week_5_d.length-1].food_name);	
					$("#week_5-dinner").children(".id").html(week_5_d[week_5_d.length-1].food_id);
					$("#week_5-dinner").children(".meal-2").html(week_5_d[week_5_d.length-1].food_desc);
					$("#week_5-dinner").children(".price-1").html(week_5_d[week_5_d.length-1].sell_price);
					$("#week_5-dinner").children().find(".number").val(week_5_d[week_5_d.length-1].food_num);
			  }
				 	
			  	
			  
				 /* 晚餐去掉除第一行其余部分 */
				  $($("#week_5-dinner").nextAll()).each(function(){
					  if($(this).children().length<6){
						  $(this).remove();
					  }else{
						  return false;
					  }
				  })
			   /* 晚餐动态添加除第一行的其他内容 */
			 if(week_5_d.length!=0){
				 for(var i=0;i<week_5_d.length-1;i++){
					 var str="";
					  str+=	'<tr>'
					  str+=		'<td class="meal-1">'+week_5_d[i].food_name+'</td>'
					  str+=		'<td class="id">'+week_5_d[i].food_id+'</td>'
					  str+=		'<td>'+week_5_d[i].food_desc+'</td>'
					  str+=		'<td>'+week_5_d[i].sell_price+'</td>'
					  str+=		'<td>'
					  str+=			'<input type="number" class="number" min="1" step="1" value='+week_5_d[i].food_num+'>'
					  str+=		'</td>'
					  str+='</tr>' 
					  $("#week_5-dinner").after(str);
					  $(".jia").unbind('click').click(function(e){
			    			var s=parseInt($(this).siblings("input[class='number']").val());
			    			s++;
			    			$(this).siblings("input[class='number']").val(s);
					});
					$(".jian").unbind('click').click(function(){
						var n=parseInt($(this).siblings("input[class='number']").val());
						n--;
						if(n<1||n==1){
			    			n=1;
						}
						$(this).siblings("input[class='number']").val(n);
					})
				} 
			 }
				  /* 设置编辑、晚餐合并列 */
				  /* 设置编辑、午餐、周时间合并列 */
		  if(week_5_l.length==0){
			  $("#week_5").find(".meal")[0].rowSpan=1;
			  $("#week_5").children()[7].rowSpan=1;
		  }else{
			  $("#week_5").find(".meal")[0].rowSpan=week_5_l.length;
			  $("#week_5").children()[7].rowSpan=week_5_l.length;
		  }
		  
		  if(week_5_d.length==0){
			  $("#week_5-dinner").find(".meal")[0].rowSpan=1;
			  $("#week_5-dinner").children()[6].rowSpan=1;
		  }else{
			  $("#week_5-dinner").find(".meal")[0].rowSpan=week_5_d.length;
			  $("#week_5-dinner").children()[6].rowSpan=week_5_d.length;
		  }
		  $("#week_5").children()[0].rowSpan=$("#week_5").find(".meal")[0].rowSpan+ $("#week_5-dinner").find(".meal")[0].rowSpan;
		
		  /* 周六 */
		/* 设置午餐第一行 */
     if(week_6_l.length==0){
            $("#week_6").children(".meal-1").html("暂无数据");  
            $("#week_6").children(".id").html("暂无数据");
            $("#week_6").children(".meal-2").html("暂无数据");
            $("#week_6").children().find(".number").val('1');
      }else {
          $("#week_6").children(".meal-1").html(week_6_l[week_6_l.length-1].food_name); 
            $("#week_6").children(".id").html(week_6_l[week_6_l.length-1].food_id);
            $("#week_6").children(".meal-2").html(week_6_l[week_6_l.length-1].food_desc);
            $("#week_6").children(".price-1").html(week_6_l[week_6_l.length-1].sell_price);
            $("#week_6").children().find(".number").val(week_6_l[week_6_l.length-1].food_num);
      }
    
    
      
      /* 午餐去掉除第一行其余部分 */
      $($("#week_6").nextAll()).each(function(){
          if($(this).children().length<6){
              $(this).remove();
          }else{
              return false;
          }
      })
      /* 午餐动态添加除第一行的其他内容 */
    for(var i=0;i<week_6_l.length-1;i++){
                 var str="";
                  str+= '<tr>'
                  str+=     '<td class="meal-1">'+week_6_l[i].food_name+'</td>'
                  str+=     '<td class="id">'+week_6_l[i].food_id+'</td>'
                  str+=     '<td>'+week_6_l[i].food_desc+'</td>'
                  str+=     '<td>'+week_6_l[i].sell_price+'</td>'
                  str+=     '<td>'
                  str+=         '<input type="number" class="number" min="1" step="1" value='+week_6_l[i].food_num+'>'
                  str+=     '</td>'
                  str+='</tr>' 
                  $("#week_6").after(str);
                  $(".jia").unbind('click').click(function(e){
                        var s=parseInt($(this).siblings("input[class='number']").val());
                        s++;
                        $(this).siblings("input[class='number']").val(s);
                });
                $(".jian").unbind('click').click(function(){
                    var n=parseInt($(this).siblings("input[class='number']").val());
                    n--;
                    if(n<1||n==1){
                        n=1;
                    }
                    $(this).siblings("input[class='number']").val(n);
                })
                  
            }
     
    
      /* 设置晚餐第一行 */
      if(week_6_d.length==0){
            $("#week_6-dinner").children(".meal-1").html("暂无数据");   
            $("#week_6-dinner").children(".id").html("暂无数据");
            $("#week_6-dinner").children(".meal-2").html("暂无数据");
            $("#week_6-dinner").children().find(".number").val("1");
      }else {
            $("#week_6-dinner").children(".meal-1").html(week_6_d[week_6_d.length-1].food_name);    
            $("#week_6-dinner").children(".id").html(week_6_d[week_6_d.length-1].food_id);
            $("#week_6-dinner").children(".meal-2").html(week_6_d[week_6_d.length-1].food_desc);
           	$("#week_6-dinner").children(".price-1").html(week_6_d[week_6_d.length-1].sell_price);
            $("#week_6-dinner").children().find(".number").val(week_6_d[week_6_d.length-1].food_num);
      }
            
        
      
         /* 晚餐去掉除第一行其余部分 */
          $($("#week_6-dinner").nextAll()).each(function(){
              if($(this).children().length<6){
                  $(this).remove();
              }else{
                  return false;
              }
          })
       /* 晚餐动态添加除第一行的其他内容 */
       
     if(week_6_d.length!=0){
         for(var i=0;i<week_6_d.length-1;i++){
             var str="";
              str+= '<tr>'
              str+=     '<td class="meal-1">'+week_6_d[i].food_name+'</td>'
              str+=     '<td class="id">'+week_6_d[i].food_id+'</td>'
              str+=     '<td>'+week_6_d[i].food_desc+'</td>'
              str+=     '<td>'+week_6_d[i].sell_price+'</td>'
              str+=     '<td>'
              str+=         '<input type="number" class="number" min="1" step="1" value='+week_6_d[i].food_num+'>'
              str+=     '</td>'
              str+='</tr>' 
              $("#week_6-dinner").after(str);
              $(".jia").unbind('click').click(function(e){
                    var s=parseInt($(this).siblings("input[class='number']").val());
                    s++;
                    $(this).siblings("input[class='number']").val(s);
            });
            $(".jian").unbind('click').click(function(){
                var n=parseInt($(this).siblings("input[class='number']").val());
                n--;
                if(n<1||n==1){
                    n=1;
                }
                $(this).siblings("input[class='number']").val(n);
            })
              
        } 
     }
         
          /* 设置编辑、午餐、周时间合并列 */
          if(week_6_l.length==0){
              $("#week_6").find(".meal")[0].rowSpan=1;
              $("#week_6").children()[7].rowSpan=1;
          }else{
              $("#week_6").find(".meal")[0].rowSpan=week_6_l.length;
              $("#week_6").children()[7].rowSpan=week_6_l.length;
          }
          /* 设置编辑、晚餐合并列 */
          $("#week_6-dinner").find(".meal")[0].rowSpan=week_6_d.length;
          $("#week_6-dinner").children()[6].rowSpan=week_6_d.length;
          
          if(week_6_d.length==0){
              $("#week_6-dinner").find(".meal")[0].rowSpan=1;
              $("#week_6-dinner").children()[6].rowSpan=1;
          }else{
              $("#week_6-dinner").find(".meal")[0].rowSpan=week_6_d.length;
              $("#week_6-dinner").children()[6].rowSpan=week_6_d.length;
          }
          $("#week_6").children()[0].rowSpan=$("#week_6").find(".meal")[0].rowSpan+ $("#week_6-dinner").find(".meal")[0].rowSpan;
    
          /* 周日 */
          /* 设置午餐第一行 */
     if(week_7_l.length==0){
            $("#week_7").children(".meal-1").html("暂无数据");  
            $("#week_7").children(".id").html("暂无数据");
            $("#week_7").children(".meal-2").html("暂无数据");
            $("#week_7").children().find(".number").val('1');
      }else {
          $("#week_7").children(".meal-1").html(week_7_l[week_7_l.length-1].food_name); 
            $("#week_7").children(".id").html(week_7_l[week_7_l.length-1].food_id);
            $("#week_7").children(".meal-2").html(week_7_l[week_7_l.length-1].food_desc);
            $("#week_7").children(".price-1").html(week_7_l[week_7_l.length-1].sell_price);
            $("#week_7").children().find(".number").val(week_7_l[week_7_l.length-1].food_num);
      }
    
    
      
      /* 午餐去掉除第一行其余部分 */
      $($("#week_7").nextAll()).each(function(){
          if($(this).children().length<6){
              $(this).remove();
          }else{
              return false;
          }
      })
      /* 午餐动态添加除第一行的其他内容 */
    for(var i=0;i<week_7_l.length-1;i++){
                 var str="";
                  str+= '<tr>'
                  str+=     '<td class="meal-1">'+week_7_l[i].food_name+'</td>'
                  str+=     '<td class="id">'+week_7_l[i].food_id+'</td>'
                  str+=     '<td>'+week_7_l[i].food_desc+'</td>'
                  str+=     '<td>'+week_7_l[i].sell_price+'</td>'
                  str+=     '<td>'
                  str+=         '<input type="number" class="number" min="1" step="1" value='+week_7_l[i].food_num+'>'
                  str+=     '</td>'
                  str+='</tr>' 
                  $("#week_7").after(str);
                  $(".jia").unbind('click').click(function(e){
                        var s=parseInt($(this).siblings("input[class='number']").val());
                        s++;
                        $(this).siblings("input[class='number']").val(s);
                });
                $(".jian").unbind('click').click(function(){
                    var n=parseInt($(this).siblings("input[class='number']").val());
                    n--;
                    if(n<1||n==1){
                        n=1;
                    }
                    $(this).siblings("input[class='number']").val(n);
                })
                  
            }
     
    
      /* 设置晚餐第一行 */
      if(week_7_d.length==0){
            $("#week_7-dinner").children(".meal-1").html("暂无数据");   
            $("#week_7-dinner").children(".id").html("暂无数据");
            $("#week_7-dinner").children(".meal-2").html("暂无数据");
            $("#week_7-dinner").children().find(".number").val("1");
      }else {
            $("#week_7-dinner").children(".meal-1").html(week_7_d[week_7_d.length-1].food_name);    
            $("#week_7-dinner").children(".id").html(week_7_d[week_7_d.length-1].food_id);
            $("#week_7-dinner").children(".meal-2").html(week_7_d[week_7_d.length-1].food_desc);
            $("#week_7-dinner").children(".price-1").html(week_7_d[week_7_d.length-1].sell_price);
            $("#week_7-dinner").children().find(".number").val(week_7_d[week_7_d.length-1].food_num);
      }
            
        
      
         /* 晚餐去掉除第一行其余部分 */
          $($("#week_7-dinner").nextAll()).each(function(){
              if($(this).children().length<6){
                  $(this).remove();
              }else{
                  return false;
              }
          })
       /* 晚餐动态添加除第一行的其他内容 */
       
     if(week_7_d.length!=0){
         for(var i=0;i<week_7_d.length-1;i++){
             var str="";
              str+= '<tr>'
              str+=     '<td class="meal-1">'+week_7_d[i].food_name+'</td>'
              str+=     '<td class="id">'+week_7_d[i].food_id+'</td>'
              str+=     '<td>'+week_7_d[i].food_desc+'</td>'
              str+=     '<td>'+week_7_d[i].sell_price+'</td>'
              str+=     '<td>'
              str+=         '<input type="number" class="number" min="1" step="1" value='+week_7_d[i].food_num+'>'
              str+=     '</td>'
              str+='</tr>' 
              $("#week_7-dinner").after(str);
              $(".jia").unbind('click').click(function(e){
                    var s=parseInt($(this).siblings("input[class='number']").val());
                    s++;
                    $(this).siblings("input[class='number']").val(s);
            });
            $(".jian").unbind('click').click(function(){
                var n=parseInt($(this).siblings("input[class='number']").val());
                n--;
                if(n<1||n==1){
                    n=1;
                }
                $(this).siblings("input[class='number']").val(n);
            })
              
        } 
     }
         
          /* 设置编辑、午餐、周时间合并列 */
          if(week_7_l.length==0){
              $("#week_7").find(".meal")[0].rowSpan=1;
              $("#week_7").children()[7].rowSpan=1;
          }else{
              $("#week_7").find(".meal")[0].rowSpan=week_7_l.length;
              $("#week_7").children()[7].rowSpan=week_7_l.length;
          }
          /* 设置编辑、晚餐合并列 */
          $("#week_7-dinner").find(".meal")[0].rowSpan=week_7_d.length;
          $("#week_7-dinner").children()[6].rowSpan=week_7_d.length;
          
          if(week_7_d.length==0){
              $("#week_7-dinner").find(".meal")[0].rowSpan=1;
              $("#week_7-dinner").children()[6].rowSpan=1;
          }else{
              $("#week_7-dinner").find(".meal")[0].rowSpan=week_7_d.length;
              $("#week_7-dinner").children()[6].rowSpan=week_7_d.length;
          }
          $("#week_7").children()[0].rowSpan=$("#week_7").find(".meal")[0].rowSpan+ $("#week_7-dinner").find(".meal")[0].rowSpan;
    
		  
		  
	$(".table td").each(function(){
		if($(this).html()=="编辑"){
			$(this).css("color","#FF8F33")
		}
	})
	$(".table td").each(function(){
		if($(this).html()=="编辑"){
			$(this).click(function(){
				var that=$(this);
				var id = that.parent().attr('id')
				$(".edit").slideDown("fast");
				$("#mask").css("display","block")
				//加载所有未选套餐
						$.ajax({
								url:'${root}order/getFoodListXFood.do',
								type:'post',
								dataType:'text',
								data: {"type":id},  
// 								async:false,
								success:function(data){
									$("#allFood").html("");
									$("#checkedFood").html("");
									var allFoodHtml = "";
									var checkedFoodHtml = "";
									if(data != null && data != "[]" && data != ""){
										var json = eval('('+data+')');
										var allFood = json.foodList;
										var checkedFood = json.weekFoodList;
										for(var i = 0; i < allFood.length;i ++){
											allFoodHtml += "<li><span>"+allFood[i]["food_name"]+"</span><span>（"+allFood[i]["food_desc"]+"）</span><span>"+allFood[i]["sell_price"]+"</span><input type=\"checkbox\" id=\""+allFood[i]["id"]+"\"></li>";
										
										}
										if(checkedFood!=null){
											for(var i = 0; i < checkedFood.length;i ++){
												checkedFoodHtml += "<li><span>"+checkedFood[i]["food_name"]+"</span><span>（"+checkedFood[i]["food_desc"]+"）</span><span>"+checkedFood[i]["sell_price"]+"</span><input type=\"checkbox\" id=\""+checkedFood[i]["id"]+"\"></li>";
											}
										}
										
									}else{
										allFoodHtml += "<li><span>暂无数据</span></li>";
										checkedFoodHtml += "<li><span>暂无数据</span></li>";
									}
									$("#allFood").append(allFoodHtml);
									$("#checkedFood").append(checkedFoodHtml);
									//========重新绑定事件==========
											 /* 鼠标经过 */
											   $("#all li,#checked li").mouseover(function(){
												   if(!$(this).find("input")[0].checked){
													   $(this).css("background-color","#EEE");
												   }
												});
											  $("#all li,#checked li").mouseout(function(){
												  if(!$(this).find("input")[0].checked){
													  $(this).css("background-color","#FFFFFF");
												   }
											  });
										
									//====================
								}
							
							});
			
				
				 /* 点击确定按钮 */
		  $(".confirm").unbind("click").click(function(){
		  var n=$("#checked li span:first-child").length;
		   if(n==0){
				  alert("请选择套餐");
				  return true;
			  }
			  
			  var m=$(that).parent().nextAll();
			  
			  $($(that).parent().nextAll()).each(function(){
				  if($(this).children().length<6){
					  $(this).remove();
				  }else{
					  return false;
				  }
			  })
 
			for(var i=0;i<n-1;i++){
				 var str="";
				  str+=	'<tr>'
				  str+=		'<td class="meal-1">'+$($("#checked li span:first-child")[i]).html()+'</td>'
				  str+=		'<td class="id">'+$("#checked input")[i].id+'</td>'
				  str+=		'<td>'+$($("#checked li span:nth-child(2)")[i]).html()+'</td>'
				  str+=		'<td>'+$($("#checked li span:nth-child(3)")[i]).html()+'</td>'
				  str+=		'<td>'
				  str+=			'<input type="number" class="number" min="1" step="1" value="1"> '
				  str+=		'</td>'
				  str+='</tr>' 
				  $(that).parent().after(str);
				  $(".jia").unbind('click').click(function(e){
		    			var s=parseInt($(this).siblings("input[class='number']").val());
		    			s++;
		    			$(this).siblings("input[class='number']").val(s);
				});
				$(".jian").unbind('click').click(function(){
					var n=parseInt($(this).siblings("input[class='number']").val());
					n--;
					if(n<1||n==1){
		    			n=1;
					}
					$(this).siblings("input[class='number']").val(n);
				})
				  
			}
			$(that).siblings(".meal-1").html($($("#checked li span:first-child")[n-1]).html());
			$(that).siblings(".meal-2").html($($("#checked li span:nth-child(2)")[n-1]).html())
			$(that).siblings(".price-1").html($($("#checked li span:nth-child(3)")[n-1]).html())
			$(that).siblings(".id").html($("#checked input")[n-1].id);
			$(that).siblings(".meal")[0].rowSpan=n;
			$(that)[0].rowSpan=n;
			if($(that).siblings().length==7){
				var a=0;
				 $($(that).parent().nextAll()).each(function(){
					  if($(this).children().length<8){
						  a++;
					  }else{
						  return false;
					  }
				  })
				$($(that).siblings()[0])[0].rowSpan=a+1;
			}else if($(that).siblings().length==6){
				var b=0;
				var c=0;
				 $($(that).parent().prevAll()).each(function(){
					  if($(this).children().length<8){
						  b++;
					  }else{
						  return false;
					  }
				  })
				   $($(that).parent().nextAll()).each(function(){
					  if($(this).children().length<8){
						  c++;
					  }else{
						  return false;
					  }
				  })
				  $($(that).parent().prevAll()).each(function(){
					  if($(this).children().length==8){
						  $($(this).children()[0])[0].rowSpan=b+c+2;
						  return false;
					  };
				  })
			}
			
			$(".edit").slideUp("fast");
			$("#mask").css("display","none")
		  })
			});
		}
	});
	$(".close").click(function(){
		$(".edit").slideUp("fast");
		$("#mask").css("display","none")
	})
	  /*  点击加减号 */
			$(".jia").unbind('click').click(function(e){
    			var s=parseInt($(this).siblings("input[class='number']").val());
    			s++;
    			$(this).siblings("input[class='number']").val(s);
		});
		$(".jian").unbind('click').click(function(){
			var n=parseInt($(this).siblings("input[class='number']").val());
			n--;
			if(n<1||n==1){
    			n=1;
			}
			$(this).siblings("input[class='number']").val(n);
		})
	  
	  /* 点击编辑 */
	  $("#all input,#checked input").click(function(){
		if($(this)[0].checked){
			$(this).parent().css({"background-color":"#FF8F33"});
			$(this).siblings().css({"color":"#FFFFFF"})
		}else{
			$(this).parent().css({"background-color":"#FFFFFF"});
			$(this).siblings().css({"color":"#666666"})
		}
	  })
	  /* 鼠标经过 */
	   $("#all li,#checked li").mouseover(function(){
		   if(!$(this).find("input")[0].checked){
			   $(this).css("background-color","#EEE");
		   }
		});
	  $("#all li,#checked li").mouseout(function(){
		  if(!$(this).find("input")[0].checked){
			  $(this).css("background-color","#FFFFFF");
		   }
			});
	  $("#btn1").click(function(){
		 $("#all input:checked").each(function(i,e){
			 $(this).parent().prependTo($("#checked ul"));
			 $(this)[0].checked=false;
			 $(this).parent().css({"background-color":"#FFFFFF"});
			 $(this).siblings().css({"color":"#666666"})
			 
		 })
	  });
	  
	  $("#btn2").click(function(){
			 $("#checked input:checked").each(function(i,e){
				 $(this).parent().prependTo($("#all ul"));
				 $(this).parent().css({"background-color":"#FFFFFF"});
				 $(this)[0].checked=false;
				 $(this).siblings().css({"color":"#666666"})
			 })
		  });
	  /* 获得每天午餐晚餐 */
	  function getDay(id1,id2){
		  var o1={};
		  function getLunch(id1){
				var o=[];
				 $($("#"+id1).nextAll()).each(function(){
					 var p={};
					  if($(this).children().length<7){
						  p.foodName=$(this).children(".meal-1").html();
						  p.foodNum=$(this).find(".number").val();
						  p.foodid=$(this).find(".id").html();
						  o.push(p);
						 /*  o[$(this).children(".meal-1").html()]=$(this).find(".number").val(); */
					  }else{
						  p.foodName=$($("#"+id1).children()[2]).html();
						  p.foodNum=$($("#"+id1).children().find(".number")).val();
						  p.foodid=$($("#"+id1).children()[3]).html();
						  o.push(p);
						  /* o[$($("#"+id1).children()[2]).html()]=$($("#"+id1).children().find(".number")).html() */
						  return false;
					  } 
				  })
				  return o;
			}
		  function getDinner(id2){
				var o=[];
				 $($("#"+id2).nextAll()).each(function(){
					 var p={};
					  if($(this).children().length<7){
						  p.foodName=$(this).children(".meal-1").html();
						  p.foodNum=$(this).find(".number").val();
						  p.foodid=$(this).find(".id").html();
						  o.push(p);
						  /* o[$(this).children(".meal-1").html()]=$(this).find(".number").val(); */
					  }else{
						  p.foodName=$($("#"+id2).children()[1]).html();
						  p.foodNum=$($("#"+id2).children().find(".number")).val();
						  p.foodid=$($("#"+id2).children()[2]).html();
						  o.push(p);
						  /* o[$($("#"+id2).children()[1]).html()]=$($("#"+id2).children().find(".number")).html() */
						  return false;
					  } 
				  })
				  return o;
				
				 
			}
		  var f=getLunch(id1);
		  var g=getDinner(id2);
		  o1.lunch=f;
		  o1.dinner=g;
		  return o1;
	  }
	  function getFir(id1,id2){
		  var o1={};
		  function getLunch(id1){
				var o=[];
				 $($("#"+id1).nextAll()).each(function(){
					 var p={};
					  if($(this).children().length<7){
						  p.foodName=$(this).children(".meal-1").html();
						  p.foodNum=$(this).find(".number").val();
						  p.foodid=$(this).find(".id").html();
						  o.push(p);
						 /*  o[$(this).children(".meal-1").html()]=$(this).find(".number").val(); */
					  }else{
						  p.foodName=$($("#"+id1).children()[2]).html();
						  p.foodNum=$($("#"+id1).children().find(".number")).val();
						  p.foodid=$($("#"+id1).children()[3]).html();
						  o.push(p);
						  /* o[$($("#"+id1).children()[2]).html()]=$($("#"+id1).children().find(".number")).html() */
						  return false;
					  } 
				  })
				  return o;
			}
		  function getDinner(id2){
				var o=[];
				 $($("#"+id2).nextAll()).each(function(){
					 var p={};
						  p.foodName=$(this).children(".meal-1").html();
						  p.foodNum=$(this).find(".number").val();
						  p.foodid=$(this).find(".id").html();
						  o.push(p);
						  /* o[$(this).children(".meal-1").html()]=$(this).find(".number").val(); */
					  
				  })
				  var q={};
				 		  q.foodName=$($("#"+id2).children()[1]).html();
						  q.foodNum=$($("#"+id2).children().find(".number")).val();
						  q.foodid=$($("#"+id2).children()[2]).html();
						  o.push(q);

				  return o;
				
				 
			}
		  var f=getLunch(id1);
		  var g=getDinner(id2);
		  o1.lunch=f;
		  o1.dinner=g;
		  return o1;
	  }
	  var zhou={};
	  $(".table td").each(function(i,e){
			if($(this).html()=="null"){
				$(this).html("暂无数据");
			}
		})
	 Initialization();
	  function Initialization(){
		  s=$(".table").clone(true);
			$(".option").css("display","none");
			$("td").each(function(){
				if($(this).html()=="编辑"){
					$(this).css("display","none");
				}
			})
			$(".jia").css("display","none");
			$(".jian").css("display","none");
			$("#queding").val("编辑");
			$(".number").attr("readOnly","true");
	  } 
	</script>
</body>
</html>


