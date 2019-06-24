<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="common/public/TagLib.jsp"%>
<%@ include file="common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>后台管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	 
	 <script type="text/javascript">
	 
	 	var myNode = null;
	 	var myE = null; 
	 	var tabId = null;
        var mytab;
        function addtab(id, atext, aurl) {
            if (!mytab) return;
            mytab.addTabItem({tabid: id, text: atext, url: aurl})
        }
        $(function () {
            $("#main").ligerLayout({topHeight: 40, leftWidth: 240,bottomHeight:25});
            var tree1 = $("#tree1").ligerTree({
            	url:'${root}/xmenu/getAllMenu3XMenu.do',
                checkbox: false,
                slide: false,
                textFieldName: 'menuName',
                idFieldName: 'id',
                parentIDFieldName: 'parentId',
                onSelect: function (node) {
                    if (!node.data.menuUrl) return;
                    var tabid = $(node.target).attr("tabid");
                    if (!tabid) {
                        tabid = new Date().getTime();
                        $(node.target).attr("tabid", tabid)
                    }
                    f_addTab(node.data.id, node.data.menuName,"${root}"+node.data.menuUrl);
                },
                onContextmenu: function (node, e) {
                	   myNode = node;
                	   myE = e;
//                     menuNodeID = node.data.id;
//                     menuNodeParentID = node.data.parentid;
//                     menuNodeText = node.data.text;
                    menu.show({ top: e.pageY, left: e.pageX });
                    return false;
                }
            });
            
            
       		 
       		 
            mytab = $("#home").ligerTab({
                width: "100%", height: "100%",
                showSwitchInTab: true,
                showSwitch: true,
                dblClickToClose: true,
                dragToMove: true,
                contextmenu: true });
                
            function f_addTab(tabid, text, url) {
                mytab.addTabItem({
                    tabid: tabid,
                    text: text,
                    url: url,
                    callback: function () {
//                        addFrameSkinLink(tabid);
                    }
                });
            }
            function onSelect(note) {
                alert('onSelect:' + note.data.text);
            }
        })
        
        
        
         //退出
          function logout(){window.location.href="${root}xuser/logoutXUser.do";}
    </script>
</head>
<body style="padding:2px; overflow: hidden;">
<div id="main">
    <div position="top">
    <h2>后台管理</h2>
    <span>当前用户：${loginUser.userName }</span>
    <a href="javascript:void(0);" onclick="logout();">退出</a>
    </div>
    <div position="left" title="主要功能">
        <ul id="tree1" style="margin-top:3px;"/>
    </div>
    <div id="home" position="center">
        <div tabid="a1" title="首页">
            <iframe src="${root }welcome.jsp" frameborder="0" width="100%"></iframe>
        </div>
    </div>
<!--     <div position="right"></div> -->
    <div   position="bottom">
    	<jsp:include page="down.jsp"/>
    </div>
</div>
<!-- 	<div id="rMenu2">   -->
<!--      <ul>   -->
<!--           <li>名称：   -->
<!--                 <input type="text"  maxlength="30" id="caption_bbs" style="width: 100px; height: 16px;" />   -->
<!--                 <a style="cursor: pointer;" onclick="editNodeName();">确定</a> -->
<!--           </li>   -->
<!--         </ul>   -->
<!--     </div>   -->
  </body>
</html>
