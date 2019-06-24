<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>订餐</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no, email=no" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/adapter.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/index.css">
    <script type="text/javascript" src="<%=basePath%>wx_orders/js/msgbox.js"></script>
    <script type="text/javascript" src="<%=basePath%>wx_orders/js/index.js"></script>
    <script type="text/javascript" src="<%=basePath%>wx_orders/js/msgbox.js"></script>
	<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
	<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
	<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	
</head>
	<div id="mask"></div>
 	<div>
        <div class="header">
           	<span id="orderType">正常订餐</span>
           	<div class="triangle"></div>
           		<ul>
           			<li style="border: 1px solid #f1f1f1;"><span class="choose">正常订餐 </span><div class="choosed"></div></li>
           			<li style="border: 1px solid #f1f1f1;"><span>预约订餐</span><div></div></li>
           			<li><span>现场订餐</span><div></div></li>
           		</ul>
        </div>
        <div  id="zcOrder" style="text-align: center;">
        <span style="display: block;margin-top: 20px;color: red;font-weight: 500;font-size: 14px;"><b>订餐时间：午餐时间 ：00:00:00 - 10:20:00 </b></span>
        <span style="display: block;margin-left:77px;margin-top: 20px;color: red;font-weight: 500;font-size: 14px;"><b>晚餐时间：12:00:00 - 16:00:00</b></span>
        <span style="display: block;margin-top: 20px;color: red;font-weight: 500;font-size: 14px;"><b>（注：晚餐暂不开放）</b></span>
        </div>
        <div  id="ydOrder" style="text-align: center;display: none;">
        <span style="display: block;margin-top: 20px;color: red;font-weight: 500;font-size: 14px;"><b>预定时间：午餐时间 ：00:00:00 - 10:20:00 </b></span>
        <span style="display: block;margin-left:77px;margin-top: 20px;color: red;font-weight: 500;font-size: 14px;"><b>晚餐时间：12:00:00 - 16:00:00</b></span>
        <span style="display: block;margin-top: 20px;color: red;font-weight: 500;font-size: 14px;"><b>（注：只能预定当前订餐时间之后的套餐，晚餐暂不开放）</b></span>
        </div>
       <div class="chooseType">
       	<ul>
       		<%-- <li class="orderType" id="zhifu222"  onclick="window.location='${root}onlinePay/togetonliOnlinePay.do'">
       			到店支付&gt;&gt;
       		</li> --%>
       		<li class="orderType" id="selfOrder">
       			给自己点餐&gt;&gt;
       		</li>
       		<!-- <li class="orderType btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
       			到店支付&gt;&gt;
       		</li> -->
       		<c:if test="${isKR }">
       		<li class="orderType" id="guestOrder">
       			点客餐&gt;&gt;
       		</li>
       		</c:if>
       		<c:if test="${isLD }">
       		<li class="orderType" id="leaderOrder">
       			点领导餐&gt;&gt;
       		</li>
       		</c:if>
       		<c:if test="${isZD }">
       		<c:if test="${appointUser!=null  }">
       		<li class="orderOthers">
       			您的餐由${appointUser.realName }负责！(点击修改)
       		</li>
       		</c:if>
       		<c:if test="${appointUser==null }">
	       		<li class="orderOthers">
	       			指定人点餐
	       		</li>
       		</c:if>
       		</c:if>
       		
       	</ul>
       </div>
        <div class="footer" >
            <ul>
                <li class="order" onclick="window.location='${root}wechat/toIndexWeChat.do'">
                    <div class="footer-title" >订餐</div>
                </li>
               <li class="myOrder"   onclick="window.location='${root}wechat/toMyOrderWeChat.do'">
                    <div class="footer-title">我的订单</div>
                </li>
                <li class="balance"  onclick="window.location='${root}wechat/toBalanceWeChat.do'">
                    <div class="footer-title">余额</div>
                </li>
            </ul>
        </div>
        <div class="guest">
        	<ul>
        		<li class="guest-top">请输入客人姓名</li>
        		<li class="guest-middle"><input type="text" id="vistorName"></li>
        		<li class="guest-bottom"><input type="button" value="确定"></li>
        	</ul>
        </div>
        
        
        <div class="leader">
        	<ul>
        		<li class="leader-top">领导姓名</li>
        		<li class="leader-middle">
        			<select name="ld" id="c" 	style="positive:relative;top:-2px;">
					<c:forEach items="${leadList }" var="led">
						<option value="${led.lead_id }">${led.lead_name }</option>
					</c:forEach>
				</select>
        		</li>
        		<li class="leader-bottom"><input type="button" value="确定"></li>
        	</ul>
        </div>
         <div class="orderOth">
        	<div class="orderOth-title">请选择你指定的人</div>
        	<ul>
        		<li class="department">
	        		<select id="dept" name="dept">
	        			<option value="0">请选择</option>
	        			  <c:forEach items="${deptList}" var="list">
				                <option value="${list.id}">${list.name}</option>
				        </c:forEach>
	        		</select>
        		</li>
        		<li class="orderName">
	        		<select id="staff" name="staff">
	        			<option value="0">请选择</option>
	        		</select>
        		</li>
        		<li class="reset"><input type="button" value="取消"></li>
        		<li class="confirm"><input type="button" value="确定"></li>
        	</ul>
        </div>
    </div>
</body>
	<script>
	  $("#zhifu").click(function(){
		  debugger;
		  var zhi=$("#selc option:selected").text();
     	  console.log("1");
     	 // window.location.href="../wx_orders/hello.jsp?zhi="+zhi+"";
     	// window.location="${root}/onlinePay/toOnlinePayListOnlinePay.do";	
	  })
     
      /* $("#jin").click(function(){
     	$.ajax({
             url:'${root}xuser/getuusXUser.do',//请求后台加载数据的方法
             data: "year=" + year,
             success: function (data) {
             alert(data);
             }
         })
	  }) */
   
	</script>
</html>
